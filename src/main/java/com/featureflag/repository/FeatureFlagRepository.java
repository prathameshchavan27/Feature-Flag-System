package com.featureflag.repository;

import com.featureflag.pojos.FeatureFlag;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface FeatureFlagRepository extends JpaRepository<FeatureFlag, Long> {
    FeatureFlag findByName(String name);
}
