package org.digijava.kernel.persistence;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.common.collect.ImmutableList;
import org.digijava.module.aim.dbentity.AmpRole;
import org.digijava.module.aim.helper.Constants;

/**
 * Non-persistent implementation of {@code InMemoryManager} which is backed by an in-memory map.
 * <p>
 * Mainly intended for testing purposes, where a full blown persistent system isn't required.
 *
 * @author Octavian Ciubotaru
 */
public class InMemoryRoleManager implements InMemoryManager<AmpRole> {
    
    private static InMemoryRoleManager instance;
    
    private final AmpRole donorRole;
    
    Map<Long, AmpRole> roles = new HashMap<>();
    
    public static InMemoryRoleManager getInstance() {
        if (instance == null) {
            instance = new InMemoryRoleManager();
        }
        
        return instance;
    }
    
    private InMemoryRoleManager() {
        donorRole = new AmpRole();
        donorRole.setAmpRoleId(120L);
        donorRole.setRoleCode(Constants.ROLE_CODE_DONOR);
        
        roles.put(donorRole.getAmpRoleId(), donorRole);
    }
    
    public AmpRole getDonorRole() {
        return donorRole;
    }
    
    @Override
    public AmpRole get(Long id) {
        return roles.get(id);
    }
    
    @Override
    public List<AmpRole> getAllValues() {
        return new ArrayList<>(roles.values());
    }
}
