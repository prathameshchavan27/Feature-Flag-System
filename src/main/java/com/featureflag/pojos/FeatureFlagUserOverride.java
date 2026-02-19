package com.featureflag.pojos;

import jakarta.persistence.*;

@Entity
@Table(
        uniqueConstraints = @UniqueConstraint(
                columnNames = {"feature_flag_id", "userId"}
        )
)
public class FeatureFlagUserOverride {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "feature_flag_id", nullable = false)
    private FeatureFlag featureFlag;

    @Column(nullable = false)
    private Long userId;

    @Column(nullable = false)
    private boolean enabled;

    public FeatureFlagUserOverride() {
    }

    public FeatureFlagUserOverride(FeatureFlag featureFlag, Long userId, boolean enabled) {
        this.featureFlag = featureFlag;
        this.userId = userId;
        this.enabled = enabled;
    }

    // getters & setters
    public FeatureFlag getFeatureFlag() {
        return featureFlag;
    }

    public void setFeatureFlag(FeatureFlag featureFlag) {
        this.featureFlag = featureFlag;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Boolean isEnabled() {
        return enabled;
    }

    public void setIsEnabled(Boolean enabled) {
        this.enabled = enabled;
    }
}
