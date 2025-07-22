package vanh.notify.utils;

import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import org.springframework.kafka.core.KafkaTemplate;
import vanh.notify.dto.NotifyJob;
import vanh.notify.service.FcmService;

import java.util.ArrayList;
import java.util.List;

@Component
public class NotifyWorker {

    private final FcmService fcmService;
    private final StringRedisTemplate redisTemplate;
    private final KafkaTemplate<String, NotifyJob> kafkaTemplate;

    public NotifyWorker(FcmService fcmService,
                        StringRedisTemplate redisTemplate,
                        KafkaTemplate<String, NotifyJob> kafkaTemplate) {
        this.fcmService = fcmService;
        this.redisTemplate = redisTemplate;
        this.kafkaTemplate = kafkaTemplate;
    }

    @KafkaListener(topics = "notify_jobs", groupId = "notify-group")
    public void handleNotification(NotifyJob job) {
        // 1. Filter valid users (preference still ON)
        List<String> validUsers = new ArrayList<>();
        for (String userId : job.getUserIds()) {
            String key = getShardedKey(job.getChannelId(), job.getCategoryId(), Long.valueOf(userId));
            if (Boolean.TRUE.equals(redisTemplate.opsForSet().isMember(key, userId))) {
                validUsers.add(userId);
            }
        }

        // 2. Send batch
        try {
            FcmService.BatchSendResult result = fcmService.sendBatch(validUsers, job.getTitle(), job.getContent());

            // 3. Handle failed tokens
            if (!result.getFailedTokens().isEmpty() && job.getRetryCount() < 3) {
                retryFailedUsers(job, result.getFailedTokens());
            }
        } catch (FcmService.TransientException e) {
            // 4. Retry entire chunk for system errors
            if (job.getRetryCount() < 5) {
                job.setRetryCount(job.getRetryCount() + 1);
                kafkaTemplate.send("notify_retries", job);
            }
        }
    }

    private void retryFailedUsers(NotifyJob originalJob, List<String> failedTokens) {
        NotifyJob retryJob = new NotifyJob(
                failedTokens,
                originalJob.getTitle(),
                originalJob.getContent(),
                originalJob.getAdditionalInfo(),
                originalJob.getChannelId(),
                originalJob.getCategoryId()
        );
        retryJob.setRetryCount(originalJob.getRetryCount() + 1);

        long delay = calculateBackoff(retryJob.getRetryCount());
        kafkaTemplate.send("notify_retries", retryJob);
    }

    private long calculateBackoff(int retryCount) {
        return (long) (Math.pow(2, retryCount) * 1000 + (Math.random() * 1000));
    }

    private String getShardedKey(Long channelId, Long categoryId, Long userId) {
        int shard = Math.abs(userId.hashCode()) % 100;
        return String.format("sub:C%d_CAT%d:%02d", channelId, categoryId, shard);
    }
}