package org.digijava.kernel.persistence;

import java.util.HashMap;
import java.util.Map;

import org.digijava.kernel.user.User;
import org.springframework.util.Assert;

public class InMemoryUserManager {
    
    public final static String TEST_USER_NAME = "Test User";
    
    private final Map<String, User> users = new HashMap<>();
    
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
    
    public void init() {
        User user = new User();
        user.setId(1L);
        user.setFirstNames(TEST_USER_NAME);
        addUser(user);
    }
}
