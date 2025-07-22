package vanh.notify.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import vanh.notify.entity.EventUserPreference;

@Repository
public interface EventUserPreferenceRepository extends JpaRepository<EventUserPreference, Long> {

    @Modifying
    @Query("DELETE FROM EventUserPreference e WHERE e.userId = :userId AND e.channelId = :channelId AND e.categoryId = :categoryId")
    void deleteByUserChannelCategory(@Param("userId") Long userId,
                                     @Param("channelId") Long channelId,
                                     @Param("categoryId") Long categoryId);

    @Query("SELECT e FROM EventUserPreference e WHERE e.userId = :userId AND e.channelId = :channelId AND e.categoryId = :categoryId")
    EventUserPreference findByUserChannelCategory(@Param("userId") Long userId,
                                                  @Param("channelId") Long channelId,
                                                  @Param("categoryId") Long categoryId);
}