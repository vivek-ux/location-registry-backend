package com.example.locationregistry;

import java.time.LocalDateTime;

import org.hibernate.annotations.CreationTimestamp;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class LocationVisit {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Double latitude;
    private Double longitude;

    private String photoUrl;

    private boolean revisited;

    @CreationTimestamp
    private LocalDateTime createdAt;

    // 🔴 REQUIRED by JPA
    public LocationVisit() {
    }

    public Long getId() {
        return id;
    }

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    public String getPhotoUrl() {
        return photoUrl;
    }

    public void setPhotoUrl(String photoUrl) {
        this.photoUrl = photoUrl;
    }

    public boolean isRevisited() {
        return revisited;
    }

    public void setRevisited(boolean revisited) {
        this.revisited = revisited;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
}
