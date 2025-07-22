package vanh.notify.service;

import org.springframework.stereotype.Service;
import java.util.*;

@Service
public class FcmService {

    public BatchSendResult sendBatch(List<String> userIds, String title, String content) throws TransientException {
        // 1. Fetch FCM tokens from database
        List<String> fcmTokens = fetchFcmTokens(userIds);

        if (fcmTokens.isEmpty()) {
            return new BatchSendResult(0, Collections.emptyList());
        }

        // 2. Send to FCM (simplified for example)
        try {
            // Giả lập: 90% thành công, 10% lỗi
            List<String> failedTokens = new ArrayList<>();
            for (String token : fcmTokens) {
                if (Math.random() < 0.1) {
                    failedTokens.add(token);
                }
            }

            return new BatchSendResult(fcmTokens.size() - failedTokens.size(), failedTokens);
        } catch (Exception e) {
            throw new TransientException("FCM service unavailable");
        }
    }

    private List<String> fetchFcmTokens(List<String> userIds) {
        // Giả lập: Trả về danh sách token
        List<String> tokens = new ArrayList<>();
        for (String userId : userIds) {
            tokens.add("token_" + userId);
        }
        return tokens;
    }

    public static class BatchSendResult {
        private final int successCount;
        private final List<String> failedTokens;

        public BatchSendResult(int successCount, List<String> failedTokens) {
            this.successCount = successCount;
            this.failedTokens = failedTokens;
        }

        public int getSuccessCount() {
            return successCount;
        }

        public List<String> getFailedTokens() {
            return failedTokens;
        }
    }

    public static class TransientException extends Exception {
        public TransientException(String message) {
            super(message);
        }
    }
}