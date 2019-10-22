package org.digijava.kernel.persistence;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.dgfoundation.amp.activity.builder.ActivityBuilder;
import org.digijava.module.aim.dbentity.AmpActivityGroup;
import org.digijava.module.aim.dbentity.AmpActivityVersion;
import org.springframework.util.Assert;

/**
 * Non-persistent implementation of {@code InMemoryManager} which is backed by an in-memory map.
 * <p>
 * Mainly intended for testing purposes, where a full blown persistent system isn't required.
 *
 * @author Viorel Chihai
 */
public class InMemoryActivityManager implements InMemoryManager<AmpActivityVersion> {
    
    private static InMemoryActivityManager instance;
    
    private final Map<Long, AmpActivityVersion> activities = new HashMap<>();
    
    private final Map<Long, Long> activityGroupVersions = new HashMap<>();
    
    public static InMemoryActivityManager getInstance() {
        if (instance == null) {
            instance = new InMemoryActivityManager();
        }
        
        return instance;
    }
    
    public void addActivity(AmpActivityVersion activity) {
        Assert.isTrue(!activityExists(activity.getAmpActivityId()));
        activities.put(activity.getAmpActivityId(), activity);
    }
    
    public AmpActivityVersion getActivity(Long activityId) {
        return activities.get(activityId);
    }
    
    public boolean activityExists(Long activityId) {
        return activities.containsKey(activityId);
    }
    
    private InMemoryActivityManager() {
        AmpActivityGroup group1 = new AmpActivityGroup();
        group1.setVersion(1L);
        
        addActivity(new ActivityBuilder()
                .withId(1L)
                .withTitle("Activity 1")
                .withDraft(false)
                .withGroup(group1)
                .getActivity());
        
        AmpActivityGroup group2 = new AmpActivityGroup();
        group2.setVersion(2L);
        
        addActivity(new ActivityBuilder()
                .withId(2L)
                .withTitle("Activity 2")
                .withDraft(true)
                .withGroup(group2)
                .getActivity());
        
        activityGroupVersions.put(2L, 2L);
    }
    
    public boolean isActivityLastVersion(Long activityId, Long activityGroupVersion) {
        return activityGroupVersions.containsKey(activityId)
                && activityGroupVersions.get(activityId).equals(activityGroupVersion);
    }
    
    @Override
    public AmpActivityVersion get(Long id) {
        return activities.get(id);
    }
    
    @Override
    public List<AmpActivityVersion> getAllValues() {
        return new ArrayList<>(activities.values());
    }
}
