package com.example.a40212845_ass2;

public class Access {
    private Integer AccessId;
    private Integer ProfileId;
    private String AccessType;
    private String Timestamp;

    public Access(Integer profileId, String accessType, String timestamp) {
        ProfileId = profileId;
        AccessType = accessType;
        Timestamp = timestamp;
    }
    public Access(Integer accessId, Integer profileId, String accessType, String timestamp) {
        ProfileId = profileId;
        AccessType = accessType;
        Timestamp = timestamp;
        AccessId = accessId;
    }
    public Integer getAccessId() {
        return AccessId;
    }
    public Integer getProfileId() {
        return ProfileId;
    }
    public String getAccessType() {
        return AccessType;
    }
    public String getTimestamp() {
        return Timestamp;
    }
    public void setAccessId(Integer accessId) {
        AccessId = accessId;
    }
    public void setProfileId(Integer profileId) {
        ProfileId = profileId;
    }
    public void setAccessType(String accessType) {
        AccessType = accessType;
    }
    public void setTimestamp(String timestamp) {
        Timestamp = timestamp;
    }
}
