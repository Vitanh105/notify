package vanh.notify.controller;

import org.springframework.data.redis.core.Cursor;
import org.springframework.data.redis.core.ScanOptions;
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
    private static final int BATCH_SIZE = 1000; // Kích thước batch cho Redis SCAN

    public NotifyController(StringRedisTemplate redisTemplate,
                            KafkaTemplate<String, NotifyJob> kafkaTemplate) {
        this.redisTemplate = redisTemplate;
        this.kafkaTemplate = kafkaTemplate;
    }

    @PostMapping("/api/sendNotify")
    public ResponseEntity<Map<String, String>> sendNotify(@RequestBody NotifyRequest request) {
        Long channelId = request.getChannelId();
        Long categoryId = request.getCategoryId();

        // 1. Tìm tất cả Redis keys shard
        String keyPattern = String.format("sub:C%d_CAT%d:*", channelId, categoryId);
        Set<String> keys = redisTemplate.keys(keyPattern);

        if (keys == null || keys.isEmpty()) {
            return ResponseEntity.ok(Collections.singletonMap("status", "NO_SUBSCRIBERS"));
        }

        // 2. Duyệt qua từng shard và xử lý streaming
        for (String key : keys) {
            processRedisSet(request, key, channelId, categoryId);
        }

        return ResponseEntity.ok(Collections.singletonMap("status", "QUEUED"));
    }

    private void processRedisSet(NotifyRequest request, String key, Long channelId, Long categoryId) {
        ScanOptions scanOptions = ScanOptions.scanOptions().count(BATCH_SIZE).build();
        try (Cursor<String> cursor = redisTemplate.opsForSet().scan(key, scanOptions)) {

            List<String> currentBatch = new ArrayList<>(BATCH_SIZE);

            while (cursor.hasNext()) {
                String userId = cursor.next();
                currentBatch.add(userId);

                // Khi đủ batch size, gửi vào Kafka
                if (currentBatch.size() >= BATCH_SIZE) {
                    sendToKafka(request, currentBatch, channelId, categoryId);
                    currentBatch.clear();
                }
            }

            // Gửi phần còn lại
            if (!currentBatch.isEmpty()) {
                sendToKafka(request, currentBatch, channelId, categoryId);
            }
        }
    }

    private void sendToKafka(NotifyRequest request, List<String> userIds, Long channelId, Long categoryId) {
        NotifyJob job = new NotifyJob(
                userIds,
                request.getTitle(),
                request.getContent(),
                request.getAdditionalInfo(),
                channelId,
                categoryId
        );
        kafkaTemplate.send("notify_jobs", job);
    }
}