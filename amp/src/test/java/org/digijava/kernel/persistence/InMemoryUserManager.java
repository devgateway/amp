package org.digijava.kernel.persistence;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.digijava.kernel.user.User;
import org.springframework.util.Assert;

/**
 * Non-persistent implementation of {@code InMemoryManager} which is backed by an in-memory map.
 * <p>
 * Mainly intended for testing purposes, where a full blown persistent system isn't required.
 *
 * @author Viorel Chihai
 */
public class InMemoryUserManager implements InMemoryManager<User> {
    
    private static InMemoryUserManager instance;
    
    public final static String TEST_USER_NAME = "Test User";
    
    private final Map<String, User> users = new HashMap<>();
    
    
    private InMemoryUserManager() {
        User user = new User();
        user.setId(1L);
        user.setFirstNames(TEST_USER_NAME);
        addUser(user);
    }
    
    public static InMemoryUserManager getInstance() {
        if (instance == null) {
            instance = new InMemoryUserManager();
        }
        
        return instance;
    }
    
    public void addUser(User user) {
        Assert.isTrue(!userExists(user.getName()));
        users.put(user.getFirstNames().toLowerCase(), user);
    }
    
    public User getUser(String name) {
        return users.get(name.toLowerCase());
    }
    
    public boolean userExists(String username) {
        return users.containsKey(username.toLowerCase());
    }
    
    @Override
    public User get(Long id) {
        return users.values().stream()
                .filter(u -> u.getId().equals(id))
                .findFirst().orElse(null);
    }
    
    @Override
    public List<User> getAllValues() {
        return new ArrayList<>(users.values());
    }
}
