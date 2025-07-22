package vanh.notify.dto;

import lombok.Data;

import java.util.List;
import java.util.Map;

@Data
public class NotifyJob {
    private List<String> userIds;
    private String title;
    private String content;
    private Map<String, Object> additionalInfo;
    private Long channelId;
    private Long categoryId;
    private int retryCount = 0;

    // Constructor
    public NotifyJob(List<String> userIds, String title, String content,
                     Map<String, Object> additionalInfo, Long channelId, Long categoryId) {
        this.userIds = userIds;
        this.title = title;
        this.content = content;
        this.additionalInfo = additionalInfo;
        this.channelId = channelId;
        this.categoryId = categoryId;
    }

    // Getters and setters
    public List<String> getUserIds() {
        return userIds;
    }

    public void setUserIds(List<String> userIds) {
        this.userIds = userIds;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Map<String, Object> getAdditionalInfo() {
        return additionalInfo;
    }

    public void setAdditionalInfo(Map<String, Object> additionalInfo) {
        this.additionalInfo = additionalInfo;
    }

    public Long getChannelId() {
        return channelId;
    }

    public void setChannelId(Long channelId) {
        this.channelId = channelId;
    }

    public Long getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Long categoryId) {
        this.categoryId = categoryId;
    }

    public int getRetryCount() {
        return retryCount;
    }

    public void setRetryCount(int retryCount) {
        this.retryCount = retryCount;
    }
}