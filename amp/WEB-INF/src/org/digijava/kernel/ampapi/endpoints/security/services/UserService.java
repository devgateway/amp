package org.digijava.kernel.ampapi.endpoints.security.services;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import org.apache.log4j.Logger;
import org.digijava.kernel.ampapi.endpoints.common.EPConstants;
import org.digijava.kernel.ampapi.endpoints.security.dto.User;
import org.digijava.kernel.entity.Locale;
import org.digijava.kernel.entity.UserLangPreferences;
import org.digijava.kernel.request.TLSUtils;
import org.digijava.kernel.user.Group;
import org.digijava.kernel.util.UserUtils;
import org.digijava.module.aim.dbentity.AmpOrganisation;
import org.digijava.module.aim.dbentity.AmpUserExtension;
import org.digijava.module.um.util.AmpUserUtil;
import org.springframework.stereotype.Component;

/**
 * User related services
 * 
 * @author Nadejda Mandrescu
 */
@Component
public class UserService {
    private static Logger logger = Logger.getLogger(UserService.class);
    
    /**
     * Provides user info for the given user ids or for all users if the list of user ids is empty.
     * @param userIds
     * @return the list of users info 
     */
    public List<User> getUserInfo(List<Long> userIds) {
        List<User> users = new ArrayList<>();
        List<org.digijava.kernel.user.User> ampUsers;
        if (userIds.isEmpty()) {
            ampUsers = UserUtils.getAllUsers();
        } else {
            ampUsers = UserUtils.getUsers(userIds);
        }

        Map<Long, UserLangPreferences> userLangPreferences =
                UserUtils.getUserLangPreferences(ampUsers, TLSUtils.getSite());

        Map<Long, AmpUserExtension> ampUserExtensions = AmpUserUtil.getAmpUserExtensions(ampUsers);

        ampUsers.forEach(new Consumer<org.digijava.kernel.user.User>() {
            @Override
            public void accept(org.digijava.kernel.user.User ampUser) {
                users.add(UserService.this.convertDBUser(ampUser, userLangPreferences.get(ampUser.getId()),
                        ampUserExtensions.get(ampUser.getId())));
            }
        });
        return users;
    }
    
    private User convertDBUser(org.digijava.kernel.user.User ampUser, UserLangPreferences userLangPreferences,
            AmpUserExtension ampUserExtension) {
        User user = new User();
        user.setId(ampUser.getId());
        user.setFirstName(ampUser.getFirstNames());
        user.setLastName(ampUser.getLastName());
        user.setEmail(ampUser.getEmail());
        if (ampUser.getPasswordChangedAt() != null) {
            SimpleDateFormat sdf = new SimpleDateFormat(EPConstants.ISO8601_DATE_AND_TIME_FORMAT);
            user.setPasswordChangedAt(sdf.format(ampUser.getPasswordChangedAt()));
        }
        user.setBanned(ampUser.isBanned());
        if (ampUser.getPledger() != null) {
            user.setPledger(ampUser.getPledger());
        }
        user.setAdmin(ampUser.isGlobalAdmin());
        user.setLangIso2(getUserLang(ampUser, userLangPreferences));
        if (ampUser.getCountry() != null) {
            user.setCountryIso2(ampUser.getCountry().getIso());
        }
        setSelectedOrgDetails(user, ampUserExtension);
        if (!ampUser.getAssignedOrgs().isEmpty()) {
            user.setAssignedOrgIds(new TreeSet<>(ampUser.getAssignedOrgs().stream().map(new Function<AmpOrganisation, Long>() {
                @Override
                public Long apply(AmpOrganisation org) {
                    return org.getAmpOrgId();
                }
            })
                    .collect(Collectors.toSet())));
        }
        if (!ampUser.getGroups().isEmpty()) {
            user.setGroupKeys(new TreeSet<>(((Set<Group>) ampUser.getGroups()).stream().map(new Function<Group, String>() {
                @Override
                public String apply(Group group) {
                    return group.getKey();
                }
            })
                    .filter(new Predicate<String>() {
                        @Override
                        public boolean test(String key) {
                            return key != null;
                        }
                    }).collect(Collectors.toSet())));
        }
        return user;
    }
    
    private String getUserLang(org.digijava.kernel.user.User ampUser, UserLangPreferences userLangPreferences) {
        Locale lang;
        if (userLangPreferences != null) {
            lang = userLangPreferences.getAlertsLanguage();
        } else {
            lang = ampUser.getRegisterLanguage();
        }
        return lang.getCode();
    }
    
    private void setSelectedOrgDetails(User user, AmpUserExtension userExt) {
        if (userExt != null) {
            if (userExt.getOrgType() != null) {
                user.setOrgTypeId(userExt.getOrgType().getAmpOrgTypeId());
            }
            if (userExt.getOrgGroup() != null) {
                user.setOrgGroupId(userExt.getOrgGroup().getAmpOrgGrpId());
            }
            if (userExt.getOrganization() != null) {
                user.setOrgId(userExt.getOrganization().getAmpOrgId());
            }
        }
    }

}
