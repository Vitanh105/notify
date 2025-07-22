package vanh.notify.controller;

import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import vanh.notify.dto.NotifyJob;
import vanh.notify.dto.NotifyRequest;

import java.util.*;

@RestController
public class NotifyController {

    private final StringRedisTemplate redisTemplate;
    private final KafkaTemplate<String, NotifyJob> kafkaTemplate;

    public NotifyController(StringRedisTemplate redisTemplate,
                            KafkaTemplate<String, NotifyJob> kafkaTemplate) {
        this.redisTemplate = redisTemplate;
        this.kafkaTemplate = kafkaTemplate;
    }

    @PostMapping("/api/sendNotify")
    public ResponseEntity<Map<String, String>> sendNotify(@RequestBody NotifyRequest request) {
        Long channelId = request.getChannelId();
        Long categoryId = request.getCategoryId();

        // 1. Find all Redis shard keys
        String keyPattern = String.format("sub:C%d_CAT%d:*", channelId, categoryId);
        Set<String> keys = redisTemplate.keys(keyPattern);

        if (keys == null || keys.isEmpty()) {
            return ResponseEntity.ok(Collections.singletonMap("status", "NO_SUBSCRIBERS"));
        }

        // 2. Get all user IDs from shards
        Set<String> allUserIds = new HashSet<>();
        for (String key : keys) {
            Set<String> userIds = redisTemplate.opsForSet().members(key);
            if (userIds != null) {
                allUserIds.addAll(userIds);
            }
        }

        // 3. Partition into chunks of 500 users
        List<List<String>> chunks = partition(new ArrayList<>(allUserIds), 500);

        // 4. Push each chunk to Kafka
        for (List<String> chunk : chunks) {
            NotifyJob job = new NotifyJob(
                    chunk,
                    request.getTitle(),
                    request.getContent(),
                    request.getAdditionalInfo(),
                    channelId,
                    categoryId
            );
            kafkaTemplate.send("notify_jobs", job);
        }

        return ResponseEntity.ok(Collections.singletonMap("status", "QUEUED"));
    }

    private <T> List<List<T>> partition(List<T> list, int size) {
        List<List<T>> partitions = new ArrayList<>();
        for (int i = 0; i < list.size(); i += size) {
            partitions.add(list.subList(i, Math.min(i + size, list.size())));
        }
        return partitions;
    }
}

// DTO Classes


