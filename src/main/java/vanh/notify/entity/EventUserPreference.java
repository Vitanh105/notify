package vanh.notify.entity;

import jakarta.persistence.*;
import java.util.Date;

@Entity
@Table(name = "EVENT_USER_PREFERENCE")
public class EventUserPreference {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "USER_ID", nullable = false)
    private Long userId;

    @Column(name = "CHANNEL_ID", nullable = false)
    private Long channelId;

    @Column(name = "CATEGORY_ID", nullable = false)
    private Long categoryId;

    @Column(name = "IS_ENABLED", nullable = false)
    private Boolean isEnabled;

    @Column(name = "CREATE_DATE")
    @Temporal(TemporalType.TIMESTAMP)
    private Date createDate;

    @Column(name = "LAST_UPDATE")
    @Temporal(TemporalType.TIMESTAMP)
    private Date lastUpdate;

    // Constructors, getters, setters
    public EventUserPreference() {
        this.createDate = new Date();
    }

    public EventUserPreference(Long userId, Long channelId, Long categoryId, Boolean isEnabled) {
        this.userId = userId;
        this.channelId = channelId;
        this.categoryId = categoryId;
        this.isEnabled = isEnabled;
        this.createDate = new Date();
        this.lastUpdate = new Date();
    }

    // Getters and setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
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

    public Boolean getEnabled() {
        return isEnabled;
    }

    public void setEnabled(Boolean enabled) {
        isEnabled = enabled;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public Date getLastUpdate() {
        return lastUpdate;
    }

    public void setLastUpdate(Date lastUpdate) {
        this.lastUpdate = lastUpdate;
    }
}