package com.featureflag.repository;

import com.featureflag.pojos.FeatureFlagUserOverride;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface FeatureFlagUserOverrideRepository extends JpaRepository<FeatureFlagUserOverride, Long> {
    Optional<FeatureFlagUserOverride> findByFeatureFlag_IdAndUserId(Long fid, Long uid);
}
