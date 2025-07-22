package vanh.notify.service;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import vanh.notify.entity.EventUserPreference;
import vanh.notify.repository.EventUserPreferenceRepository;
import vanh.notify.repository.UserRepository;

import java.util.Date;

@Service
public class PreferenceService {

    private final EventUserPreferenceRepository preferenceRepo;
    private final UserRepository userRepo;
    private final StringRedisTemplate redisTemplate;

    public PreferenceService(EventUserPreferenceRepository preferenceRepo,
                             UserRepository userRepo,
                             StringRedisTemplate redisTemplate) {
        this.preferenceRepo = preferenceRepo;
        this.userRepo = userRepo;
        this.redisTemplate = redisTemplate;
    }

    @Transactional
    public void updatePreference(Long userId, Long channelId, Long categoryId, boolean enable) {
        // Kiểm tra user tồn tại
        if (!userRepo.existsById(userId)) {
            throw new EntityNotFoundException("User not found with id: " + userId);
        }

        if (enable) {
            // ON: Insert to DB and add to Redis
            EventUserPreference pref = preferenceRepo.findByUserChannelCategory(userId, channelId, categoryId);
            if (pref == null) {
                pref = new EventUserPreference(userId, channelId, categoryId, true);
                preferenceRepo.save(pref);
            } else if (!pref.getEnabled()) {
                pref.setEnabled(true);
                pref.setLastUpdate(new Date());
                preferenceRepo.save(pref);
            }

            // Add to Redis Set (sharded)
            String redisKey = getShardedKey(channelId, categoryId, userId);
            redisTemplate.opsForSet().add(redisKey, userId.toString());
        } else {
            // OFF: Delete from DB and remove from Redis
            preferenceRepo.deleteByUserChannelCategory(userId, channelId, categoryId);

            // Remove from Redis
            String redisKey = getShardedKey(channelId, categoryId, userId);
            redisTemplate.opsForSet().remove(redisKey, userId.toString());
        }
    }

    private String getShardedKey(Long channelId, Long categoryId, Long userId) {
        int shard = Math.abs(userId.hashCode()) % 100;
        return String.format("sub:C%d_CAT%d:%02d", channelId, categoryId, shard);
    }
}