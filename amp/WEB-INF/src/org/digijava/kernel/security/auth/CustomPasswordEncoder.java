package org.digijava.kernel.security.auth;

import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class CustomPasswordEncoder extends BCryptPasswordEncoder {
    private final String constantSalt;

    public CustomPasswordEncoder(String constantSalt) {
        this.constantSalt = constantSalt;
    }

    @Override
    public String encode(CharSequence rawPassword) {
        // Concatenate the constant salt with the raw password and hash the result
//        String saltedPassword = constantSalt + rawPassword.toString();
        // Delegate the rest of the password hashing to the parent BCryptPasswordEncoder
        return BCrypt.hashpw(rawPassword.toString(), constantSalt);
    }
}
