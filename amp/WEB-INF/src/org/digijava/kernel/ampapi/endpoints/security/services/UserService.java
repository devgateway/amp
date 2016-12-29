/**
 * 
 */
package org.digijava.kernel.ampapi.endpoints.security.services;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;
import java.util.stream.Collectors;

import org.apache.log4j.Logger;
import org.digijava.kernel.ampapi.endpoints.activity.InterchangeUtils;
import org.digijava.kernel.ampapi.endpoints.security.dto.User;
import org.digijava.kernel.entity.Locale;
import org.digijava.kernel.entity.UserLangPreferences;
import org.digijava.kernel.exception.DgException;
import org.digijava.kernel.request.TLSUtils;
import org.digijava.kernel.user.Group;
import org.digijava.kernel.util.UserUtils;
import org.digijava.module.aim.dbentity.AmpUserExtension;
import org.digijava.module.aim.exception.AimException;
import org.digijava.module.um.util.AmpUserUtil;

/**
 * User related services
 * 
 * @author Nadejda Mandrescu
 */
public class UserService {
    private static Logger logger = Logger.getLogger(UserService.class);
    
    /**
     * Provides user info for the given user ids 
     * @param userIds
     * @return the list of users info 
     */
    public List<User> getUserInfo(List<Long> userIds) {
        List<User> users = new ArrayList<>();
        // since normally there should be just a couple or may be up to 10 users, then we can load them all 
        List<org.digijava.kernel.user.User> ampUsers = UserUtils.getUsers(userIds);
        ampUsers.forEach(ampUser -> users.add(convertDBUser(ampUser)));
        return users;
    }
    
    private User convertDBUser(org.digijava.kernel.user.User ampUser) {
        User user = new User();
        user.setId(ampUser.getId());
        user.setFirstName(ampUser.getFirstNames());
        user.setLastName(ampUser.getLastName());
        user.setEmail(ampUser.getEmail());
        if (ampUser.getPasswordChangedAt() != null) {
            SimpleDateFormat sdf = new SimpleDateFormat(InterchangeUtils.ISO8601_DATE_FORMAT);
            user.setPasswordChangedAt(sdf.format(ampUser.getPasswordChangedAt()));
        }
        user.setBanned(ampUser.isBanned());
        user.setActive(ampUser.isActivate());
        if (ampUser.getPledger() != null) {
            user.setPledger(ampUser.getPledger());
        }
        user.setAdmin(ampUser.isGlobalAdmin());
        user.setLangIso2(getUserLang(ampUser));
        if (ampUser.getCountry() != null) {
            user.setCountryIso2(ampUser.getCountry().getIso());
        }
        setSelectedOrgDetails(user, ampUser);
        if (ampUser.getAssignedOrgId() != null && ampUser.getAssignedOrgId() != -1) {
            user.setAssignedOrgId(ampUser.getAssignedOrgId());
        }
        if (!ampUser.getAssignedOrgs().isEmpty()) {
            user.setAssignedOrgIds(new TreeSet<>(ampUser.getAssignedOrgs().stream().map(org -> org.getAmpOrgId())
                    .collect(Collectors.toSet())));
        }
        if (!ampUser.getGroups().isEmpty()) {
            user.setGroupKeys(new TreeSet<>(((Set<Group>) ampUser.getGroups()).stream().map(group -> group.getKey())
                    .filter(key -> key != null).collect(Collectors.toSet())));
        }
        return user;
    }
    
    private String getUserLang(org.digijava.kernel.user.User ampUser) {
        try {
            Locale lang = null;
            UserLangPreferences userLangPreferences = UserUtils.getUserLangPreferences(ampUser, TLSUtils.getSite());
            if (userLangPreferences != null) {
                lang = userLangPreferences.getAlertsLanguage();
            } else {
                lang = ampUser.getRegisterLanguage();
            }
            return lang.getCode();
        } catch (DgException e) {
            throw new RuntimeException(e);
        }
    }
    
    private void setSelectedOrgDetails(User user, org.digijava.kernel.user.User ampUser) {
        try {
            AmpUserExtension userExt = AmpUserUtil.getAmpUserExtension(ampUser);
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
        } catch (AimException e) {
            logger.error(e.getMessage());
            throw new RuntimeException(e);
        }
    }

}
