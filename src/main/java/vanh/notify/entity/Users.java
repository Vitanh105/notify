package vanh.notify.entity;

import jakarta.persistence.*;

import java.util.Date;

@Entity
@Table(name = "USERS")
public class Users {
    @Id
    @Column(name = "ID")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_USERS")
    @SequenceGenerator(sequenceName = "SEQ_USERS", allocationSize = 1, name = "SEQ_USERS")
    private Long id;

    @Column(name = "IDENTIFIER")
    private String identifier;

    @Column(name = "PASSWORD")
    private String password;

    @Column(name = "NAME")
    private String name;

    @Column(name = "NAME_ASCII")
    private String nameAscii;

    @Column(name = "DATE_OF_BIRTH")
    private Date dateOfBirth;

    @Column(name = "ADDRESS")
    private String address;

    @Column(name = "REGISTER_CHANNEL")
    private String registerChannel;

    @Column(name = "STATUS")
    private Long status;

    @Column(name = "CREATE_DATE")
    private Date createDate;

    @Column(name = "LAST_UPDATE")
    private Date lastUpdate;

    @Column(name = "CREATE_BY")
    private String createBy;

    @Column(name = "RANK_ID")
    private Long rankId;

    @Column(name = "USER_TYPE")
    private Long userType;

    @Column(name = "PHONE_NUMBER")
    private String phoneNumber;

    @Column(name = "RANK_EXPIRE_DATE")
    private Date rankExpireDate;

    @Column(name = "EMAIL")
    private String email;

    @Column(name = "PROCESSING_STEP")
    private String processingStep;

    @Column(name = "TOTAL_POINT_RANK")
    private Long totalPointRank;

    @Column(name = "USER_HASH_CODE")
    private String userHashCode;

    @Column(name = "RANK_CODE")
    private String rankCode;
    @Column(name = "CHECK_MNP")
    private String checkMnp;


    public Long getTotalPointRank() {
        return totalPointRank;
    }

    public void setTotalPointRank(Long totalPointRank) {
        this.totalPointRank = totalPointRank;
    }

    public String getProcessingStep() {
        return processingStep;
    }

    public void setProcessingStep(String processingStep) {
        this.processingStep = processingStep;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getIdentifier() {
        return this.identifier;
    }

    public void setIdentifier(String identifier) {
        this.identifier = identifier;
    }

    public String getPassword() {
        return this.password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNameAscii() {
        return this.nameAscii;
    }

    public void setNameAscii(String nameAscii) {
        this.nameAscii = nameAscii;
    }

    public Date getDateOfBirth() {
        return this.dateOfBirth;
    }

    public void setDateOfBirth(Date dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public String getAddress() {
        return this.address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getRegisterChannel() {
        return this.registerChannel;
    }

    public void setRegisterChannel(String registerChannel) {
        this.registerChannel = registerChannel;
    }

    public Long getStatus() {
        return this.status;
    }

    public void setStatus(Long status) {
        this.status = status;
    }

    public Date getCreateDate() {
        return this.createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public Date getLastUpdate() {
        return this.lastUpdate;
    }

    public void setLastUpdate(Date lastUpdate) {
        this.lastUpdate = lastUpdate;
    }

    public String getCreateBy() {
        return this.createBy;
    }

    public void setCreateBy(String createBy) {
        this.createBy = createBy;
    }

    public Long getRankId() {
        return this.rankId;
    }

    public void setRankId(Long rankId) {
        this.rankId = rankId;
    }

    public Long getUserType() {
        return this.userType;
    }

    public void setUserType(Long userType) {
        this.userType = userType;
    }

    public String getPhoneNumber() {
        return this.phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public Date getRankExpireDate() {
        return this.rankExpireDate;
    }

    public void setRankExpireDate(Date rankExpireDate) {
        this.rankExpireDate = rankExpireDate;
    }

    public String getUserHashCode() {
        return userHashCode;
    }

    public void setUserHashCode(String userHashCode) {
        this.userHashCode = userHashCode;
    }

    public String getRankCode() {
        return rankCode;
    }

    public void setRankCode(String rankCode) {
        this.rankCode = rankCode;
    }

    public String getCheckMnp() {
        return checkMnp;
    }

    public void setCheckMnp(String checkMnp) {
        this.checkMnp = checkMnp;
    }
}