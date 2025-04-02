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

/**
 * Validates the supplied password per the requirements of these rules.
 *
 * @author Aldo Picca
 */
public class PasswordPolicyValidator {

    public static final String SHOW_PASSWORD_POLICY_RULES = "showPasswordPolicyRules";
    public static final int MIN_LENGTH = 8;
    public static final int MAX_LENGTH = 9999; //currently no max length is requested.
    public static final boolean USERNAME_MATCH_BACKWARDS = false; //whether to match backwards.
    public static final boolean USERNAME_IGNORE_CASE = true; //whether to ignore case.

    private static PasswordValidator validator = new PasswordValidator(Arrays.asList(
            new UsernameRule(USERNAME_MATCH_BACKWARDS, USERNAME_IGNORE_CASE),
            // length between MIN_LENGTH and MAX_LENGTH characters
            new LengthRule(MIN_LENGTH, MAX_LENGTH),
            // at least one upper-case character
            new CharacterRule(EnglishCharacterData.UpperCase, 1),
            // at least one lower-case character
            new CharacterRule(EnglishCharacterData.LowerCase, 1),
            // at least one digit character
            new CharacterRule(EnglishCharacterData.Digit, 1)));

    /**
     * Returns whether the result of the rule verification is a valid password.
     *
     * @return valid password for these rules
     */
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

