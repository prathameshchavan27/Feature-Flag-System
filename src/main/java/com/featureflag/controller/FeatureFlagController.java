package com.featureflag.controller;

import com.featureflag.pojos.FeatureFlag;
import com.featureflag.pojos.FeatureFlagUserOverride;
import com.featureflag.service.FeatureFlagService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/flags")
public class FeatureFlagController {
    @Autowired
    private FeatureFlagService featureFlagService;


    @PostMapping
    public ResponseEntity<?> addFeatureFlag(@RequestBody FeatureFlag featureFlag){
        try{
            featureFlagService.addFeatureFlag(featureFlag);
            return ResponseEntity.status(HttpStatus.OK).body("Feature Flag Added");
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to save flag");
        }
    }

    @GetMapping
    public ResponseEntity<?> listAllFlags(){
        try{
            return ResponseEntity.status(HttpStatus.OK).body(featureFlagService.getAllFlags());
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getFeatureFlag(@PathVariable Long id){
        try{
            return ResponseEntity.status(HttpStatus.OK).body(featureFlagService.getFeatureFlag(id));
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    @GetMapping("/{id}/is-enabled")
    public ResponseEntity<?> isFeatureEnabled(@PathVariable Long id, @RequestParam Long userId, @RequestParam(defaultValue = "false") boolean isAdmin){
        try{
            return ResponseEntity.status(HttpStatus.OK).body(featureFlagService.isFlagEnabled(id,userId,isAdmin));
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    @PatchMapping("/{id}/toggle")
    public ResponseEntity<?> toggleFeatureFlag(@PathVariable Long id){
        try{
            return ResponseEntity.status(HttpStatus.OK).body(featureFlagService.toggleFlag(id));
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteFlag(@PathVariable Long id){
        try{
            return ResponseEntity.status(HttpStatus.OK).body(featureFlagService.deleteFlag(id));
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    @PostMapping("/{id}/override")
    public ResponseEntity<?> overrideUser(@PathVariable Long id, @RequestBody FeatureFlagUserOverride featureFlagUserOverride){
        try{
            return ResponseEntity.status(HttpStatus.OK).body(featureFlagService.applyUserOverride(id,featureFlagUserOverride));
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

}
