package org.digijava.kernel.security;

import org.digijava.module.aim.helper.Constants;
import org.digijava.module.aim.util.FeaturesUtil;
import org.passay.CharacterRule;
import org.passay.EnglishCharacterData;
import org.passay.LengthRule;
import org.passay.PasswordData;
import org.passay.PasswordValidator;
import org.passay.RuleResult;
import org.passay.UsernameRule;

import java.util.Arrays;

public class PasswordPolicyValidator {
    private static PasswordValidator validator = new PasswordValidator(Arrays.asList(
            new UsernameRule(true, true),
            // length between 8 and 16 characters
            new LengthRule(8, 9999),
            // at least one upper-case character
            new CharacterRule(EnglishCharacterData.UpperCase, 1),
            // at least one lower-case character
            new CharacterRule(EnglishCharacterData.LowerCase, 1),
            // at least one digit character
            new CharacterRule(EnglishCharacterData.Digit, 1)));

    public static boolean isValid(String password, String username) {

        if (FeaturesUtil.getGlobalSettingValueBoolean(Constants.STRONG_PASSWORD)) {
            PasswordData passwordData = new PasswordData(password);
            passwordData.setUsername(username);
            RuleResult result = validator.validate(passwordData);

            return result.isValid();

        } else {
            return true;
        }

    }

}

