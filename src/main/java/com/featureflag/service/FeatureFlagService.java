package com.featureflag.service;

import com.featureflag.dto.FeatureFlagRequestDTO;
import com.featureflag.pojos.FeatureFlag;
import com.featureflag.pojos.FeatureFlagUserOverride;

import java.io.IOException;
import java.util.List;

public interface FeatureFlagService {
    public void addFeatureFlag(FeatureFlagRequestDTO featureFlagRequestDTO) throws IOException;
    public List<FeatureFlag> getAllFlags();
    public FeatureFlag getFeatureFlag(Long id) throws IOException;
    String toggleFlag(Long id);
    String deleteFlag(Long id);

    boolean isFlagEnabled(Long id, Long userId, boolean isAdmin);

    FeatureFlagUserOverride applyUserOverride(Long fid,FeatureFlagUserOverride f);
}
