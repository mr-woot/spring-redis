package com.paisabazaar.kafka_producer.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.stereotype.Indexed;

import java.io.Serializable;
import java.util.Date;

@RedisHash("Producer")
public class Producer implements Serializable {
    @Id
    private String id;

    private String buName;

    private String type;

    private String topic;

    private String purpose;

    private Long retention;

    private String metadata;

    private Long expiry;

    private Date createdAt;

    private Date updatedAt;

    public Producer(String id, String buName, String type, String topic, String purpose, Long retention, String metadata, Long expiry, Date createdAt, Date updatedAt) {
        this.id = id;
        this.buName = buName;
        this.type = type;
        this.topic = topic;
        this.purpose = purpose;
        this.retention = retention;
        this.metadata = metadata;
        this.expiry = expiry;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    @Override
    public String toString() {
        return "Producer{" +
                "id='" + id + '\'' +
                ", buName='" + buName + '\'' +
                ", type='" + type + '\'' +
                ", topic='" + topic + '\'' +
                ", purpose='" + purpose + '\'' +
                ", retention=" + retention +
                ", metadata='" + metadata + '\'' +
                ", expiry=" + expiry +
                ", createdAt=" + createdAt +
                ", updatedAt=" + updatedAt +
                '}';
    }

    public String getMetadata() {
        return metadata;
    }

    public void setMetadata(String metadata) {
        this.metadata = metadata;
    }

    public String getPurpose() {
        return purpose;
    }

    public void setPurpose(String purpose) {
        this.purpose = purpose;
    }

    public Long getRetention() {
        return retention;
    }

    public void setRetention(Long retention) {
        this.retention = retention;
    }

    public Long getExpiry() {
        return expiry;
    }

    public void setExpiry(Long expiry) {
        this.expiry = expiry;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public Date getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Date updatedAt) {
        this.updatedAt = updatedAt;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getBuName() {
        return buName;
    }

    public void setBuName(String buName) {
        this.buName = buName;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getTopic() {
        return topic;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }
}
