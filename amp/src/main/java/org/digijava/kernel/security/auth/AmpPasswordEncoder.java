package org.digijava.kernel.security.auth;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.util.regex.Pattern;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * Hashes new/changed passwords with bcrypt (replaces {@code NoOpPasswordEncoder}),
 * while still being able to verify accounts whose password was stored in plaintext before this
 * encoder was introduced, so existing users are not locked out.
 */
public class AmpPasswordEncoder implements PasswordEncoder {

    private static final Pattern BCRYPT_PATTERN = Pattern.compile("^\\$2[aby]\\$\\d{2}\\$.{53}$");

    private final BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

    @Override
    public String encode(CharSequence rawPassword) {
        return encoder.encode(rawPassword);
    }

    @Override
    public boolean matches(CharSequence rawPassword, String encodedPassword) {
        if (rawPassword == null || encodedPassword == null) {
            return false;
        }
        if (isHashed(encodedPassword)) {
            return encoder.matches(rawPassword, encodedPassword);
        }
        // legacy account: password column still holds the plaintext value, compare directly
        return MessageDigest.isEqual(
                encodedPassword.getBytes(StandardCharsets.UTF_8),
                rawPassword.toString().getBytes(StandardCharsets.UTF_8));
    }

    /**
     * @return true if the stored value is already a bcrypt hash produced by this encoder
     */
    public boolean isHashed(String storedPassword) {
        return storedPassword != null && BCRYPT_PATTERN.matcher(storedPassword).matches();
    }
}
