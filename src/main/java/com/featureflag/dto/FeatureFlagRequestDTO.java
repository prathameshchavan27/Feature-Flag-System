package com.featureflag.dto;

import jakarta.validation.constraints.*;

public class FeatureFlagRequestDTO {

    private String name;

    private boolean enabled;

    private boolean adminOnly;

    private int rolloutPercentage;

    // getters & setters

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setIsEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public boolean isAdminOnly() {
        return adminOnly;
    }

    public void setIsAdminOnly(Boolean adminOnly) {
        this.adminOnly = adminOnly;
    }

    public int getRolloutPercentage() {
        return rolloutPercentage;
    }

    public void setRolloutPercentage(int rolloutPercentage) {
        this.rolloutPercentage = rolloutPercentage;
    }
}