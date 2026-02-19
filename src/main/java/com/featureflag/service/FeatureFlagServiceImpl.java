package com.featureflag.service;

import com.featureflag.pojos.FeatureFlag;
import com.featureflag.pojos.FeatureFlagUserOverride;
import com.featureflag.repository.FeatureFlagRepository;
import com.featureflag.repository.FeatureFlagUserOverrideRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static org.aspectj.weaver.tools.cache.SimpleCacheFactory.enabled;


@Transactional
@Service
public class FeatureFlagServiceImpl implements FeatureFlagService{
    private static final Logger logger = LoggerFactory.getLogger(FeatureFlagService.class);
    @Autowired
    private FeatureFlagRepository featureFlagRepository;
    @Autowired
    private FeatureFlagUserOverrideRepository overrideRepository;
    @Override
    public void addFeatureFlag(FeatureFlag featureFlag) throws IOException {
        FeatureFlag f = new FeatureFlag(featureFlag.getName(),featureFlag.isEnabled(),featureFlag.getRolloutPercentage(),featureFlag.isAdminOnly());
        featureFlagRepository.save(f);
    }

    @Override
    public List<FeatureFlag> getAllFlags() {
        return featureFlagRepository.findAll();
    }

    @Override
    public FeatureFlag getFeatureFlag(Long id) throws IOException{
        return featureFlagRepository.findById(id).orElseThrow(() -> new IOException("Feature flag not found"));
    }

    @Override
    public String toggleFlag(Long id) {
        FeatureFlag f = featureFlagRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Feature flag not found"));

        f.setEnabled(!f.isEnabled());
        f.setUpdatedAt(LocalDateTime.now());
        featureFlagRepository.save(f);
        if (f.isEnabled()) {
            return "Feature flag enabled";
        } else {
            return "Feature flag disabled";
        }
    }

    @Override
    public String deleteFlag(Long id) {
        FeatureFlag f = featureFlagRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Feature flag not found"));
        featureFlagRepository.delete(f);
        return "Feature flag deleted";
    }

    @Override
    public boolean isFlagEnabled(Long id, Long userId, boolean isAdmin) {
        FeatureFlag f = featureFlagRepository.findById(id).orElseThrow(() -> new RuntimeException("Feature flag not found"));
        if(!f.isEnabled())
            return false;
        Optional<FeatureFlagUserOverride> override = overrideRepository.findByFeatureFlag_IdAndUserId(id,userId);
        if(override.isPresent()){
            return override.get().isEnabled();
        }
        if(f.isAdminOnly() && !isAdmin){
            return false;
        }
        if(f.getRolloutPercentage()==100)
            return true;
        if(f.getRolloutPercentage() == 0)
            return false;
        int bucket = Math.abs(userId.hashCode())%100;
        logger.info("UserId: {}, Bucket: {}", userId, bucket);
        return bucket < f.getRolloutPercentage();
    }

    @Override
    public FeatureFlagUserOverride applyUserOverride(Long fid,FeatureFlagUserOverride f) {
        FeatureFlag flag = featureFlagRepository.findById(fid).orElseThrow(() -> new RuntimeException("Feature flag not found"));

        Optional<FeatureFlagUserOverride> existing = overrideRepository.findByFeatureFlag_IdAndUserId(flag.getId(), f.getUserId());

        if (existing.isPresent()) {
            FeatureFlagUserOverride override = existing.get();
            override.setIsEnabled(f.isEnabled());
            return overrideRepository.save(override);
        }

        FeatureFlagUserOverride of = new FeatureFlagUserOverride(flag, f.getUserId(), f.isEnabled());

        return overrideRepository.save(of);
    }


}
