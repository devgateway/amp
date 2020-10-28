package org.digijava.module.aim.startup;

import java.util.List;

import org.dgfoundation.amp.ar.ARUtil;
import org.digijava.kernel.entity.Locale;
import org.digijava.kernel.entity.UserLangPreferences;
import org.digijava.kernel.exception.DgException;
import org.digijava.kernel.request.Site;
import org.digijava.kernel.user.User;
import org.digijava.kernel.util.DgUtil;
import org.digijava.kernel.util.SiteUtils;
import org.digijava.kernel.util.UserUtils;
import org.digijava.module.aim.dbentity.AmpOrgGroup;
import org.digijava.module.aim.dbentity.AmpOrgType;
import org.digijava.module.aim.dbentity.AmpOrganisation;
import org.digijava.module.aim.dbentity.AmpTeam;
import org.digijava.module.aim.dbentity.AmpTeamMember;
import org.digijava.module.aim.dbentity.AmpTeamMemberRoles;
import org.digijava.module.aim.dbentity.AmpUserExtension;
import org.digijava.module.aim.dbentity.AmpUserExtensionPK;
import org.digijava.module.aim.util.DbUtil;
import org.digijava.module.aim.util.FeaturesUtil;
import org.digijava.module.aim.util.TeamMemberUtil;
import org.digijava.module.aim.util.TeamUtil;
import org.digijava.module.um.util.AmpUserUtil;


public final class AmpBackgroundActivitiesUtil {
    public static final String AMP_USER_PASSWORD = "DuMmY_PaSsWoRd_NoBoDy_ShOuLd_UsE_iT";
    public static final String AMP_ORGANIZATION_NAME = "Development Gateway Internal Org";
    public static final String AMP_ORGANIZATION_ACRONYM = "DGIO";
    public static final String AMP_ORGANIZATION_CODE = "DGIO";

    private AmpBackgroundActivitiesUtil() {
    }

    /**
     * gets an AmpOrgType for creating an AmpOrgGroup for creating an AmpOrganisation for creating an User
     * for creating an AmpTeamMember for marking changes done to AmpActivityVersions
     *
     * @return
     */
    private static AmpOrgType getAmpOrgType() {
        List<AmpOrgType> allAmpOrgTypes = DbUtil.getAmpOrgTypes();
        AmpOrgType otherOrgType = null, bilateralOrgType = null;

        for (AmpOrgType type : allAmpOrgTypes) {
            if (type.getOrgType().toLowerCase().equals("other"))
                otherOrgType = type;

            if (type.getOrgType().toLowerCase().equals("bilateral"))
                bilateralOrgType = type;
        }

        if (otherOrgType != null)
            return otherOrgType;

        if (bilateralOrgType != null)
            return bilateralOrgType;

        // fallback: just take any
        return allAmpOrgTypes.get(0);
    }

    protected static AmpOrganisation getOrCreateInternalOrganization() throws DgException {
        AmpOrganisation organization = DbUtil.getOrganisationByName(AMP_ORGANIZATION_NAME);
        if (organization != null)
            return organization;

        organization = new AmpOrganisation();
        organization.setName(AMP_ORGANIZATION_NAME);
        organization.setAcronym(AMP_ORGANIZATION_ACRONYM);

        AmpOrgGroup ampOrgGroup = DbUtil.getAmpOrgGroupByName("Other");
        if (ampOrgGroup == null) {
            // create amp org group
            ampOrgGroup = new AmpOrgGroup();
            ampOrgGroup.setOrgGrpName("Other");
            ampOrgGroup.setOrgGrpCode("OTHER");
            AmpOrgType ot = getAmpOrgType();
            ampOrgGroup.setOrgType(ot);
            ARUtil.clearOrgGroupTypeDimensions();
            DbUtil.add(ampOrgGroup);
        }
        organization.setOrgGrpId(ampOrgGroup);
        organization.setOrgCode(AMP_ORGANIZATION_CODE);

        organization.setDescription("Organization used for internal users of the AMP system");
        DbUtil.saveOrg(organization);
        return organization;
    }

    protected static void createAmpValidatorUser(String userEmail, String firstNames, String lastName)
            throws DgException {
        User user = new User(userEmail.toLowerCase(), firstNames, lastName);
        Site site = SiteUtils.getDefaultSite();

        // set client IP address
        user.setModifyingIP("0.0.0.0");

        // set password
        user.setPassword(AMP_USER_PASSWORD);
        user.setSalt(AMP_USER_PASSWORD);

        // set Website
        user.setUrl("/");

        // register through
        user.setRegisteredThrough(site);

        // set mailing address
        user.setAddress("empty Address");

        // set organization name
        user.setOrganizationName("Development Gateway");

        user.setPledger(false);
        user.setPledgeSuperUser(false);
        user.setOrganizationTypeOther(" ");

        // set country
        user.setCountry(DbUtil.getDgCountry(FeaturesUtil.getDefaultCountryIso()));

        // set default language
        user.setRegisterLanguage(site.getDefaultLanguage());
        user.setEmailVerified(false);
        user.setBanned(false);

        // ------------- SET USER LANGUAGES
        UserLangPreferences userLangPreferences = new UserLangPreferences(user, DgUtil.getRootSite(site));

        Locale language = new Locale();
        language.setCode(site.getDefaultLanguage().getCode());

        // set alert language
        userLangPreferences.setAlertsLanguage(language);

        // set navigation language
        userLangPreferences.setNavigationLanguage(site.getDefaultLanguage());
        user.setUserLangPreferences(userLangPreferences);

        AmpOrganisation organ = getOrCreateInternalOrganization();
        AmpOrgGroup orgGroup = organ.getOrgGrpId();
        AmpOrgType orgType = orgGroup.getOrgType();

        // ===== start user extension setup =====
        AmpUserExtension userExt = new AmpUserExtension();
        // org type
        userExt.setOrgType(orgType);
        userExt.setOrgGroup(orgGroup);
        userExt.setOrganization(organ);
        // ===== end user extension setup =====

        // if email register get error message
        if (org.digijava.module.um.util.DbUtil.isRegisteredEmail(user.getEmail())) {
            System.err.println("this shouldn't have happened: BUG 121");
        } else {
            org.digijava.module.um.util.DbUtil.registerUser(user);
            user.setEmailVerified(true);
        }

        org.digijava.kernel.user.Group memberGroup = DbUtil.getGroup(org.digijava.kernel.user.Group.MEMBERS,
                site.getId());
        Long[] uid = new Long[1];
        uid[0] = user.getId();
        org.digijava.module.admin.util.DbUtil.addUsersToGroup(memberGroup.getId(), uid);

        //save amp user extensions;
        AmpUserExtensionPK extPK = new AmpUserExtensionPK(user);
        userExt.setAmpUserExtId(extPK);
        AmpUserUtil.saveAmpUserExtension(userExt);
    }


    public static User createActivityUserIfNeeded(User u) throws Exception {
        User user = UserUtils.getUserByEmail(u.getEmail());
        if (user != null)
            return user;

        createAmpValidatorUser(u.getEmail(), u.getFirstNames(), u.getLastName());
        user = UserUtils.getUserByEmail(u.getEmail());
        if (user == null)
            throw new RuntimeException("bug creating user");
        return user;
    }

    /**
     * gets or created an AmpTeamMember linked to a given workspace.
     *
     * @param team the workspace
     * @return
     * @throws Exception
     */
    public static AmpTeamMember createActivityTeamMemberIfNeeded(AmpTeam team, User u) throws Exception {
        User user = createActivityUserIfNeeded(u);

        AmpTeamMember teamMember = TeamMemberUtil.getAmpTeamMemberByUserByTeam(user, team);
        if (teamMember != null && !teamMember.isSoftDeleted()) {
            return teamMember;
        }

        AmpTeamMemberRoles role = TeamMemberUtil.getWorkspaceMemberTeamMemberRole();
        if (teamMember == null) {

            AmpTeamMember newMember = new AmpTeamMember();
            newMember.setUser(user);
            newMember.setAmpTeam(team);
            newMember.setAmpMemberRole(role);

            newMember.setPublishDocPermission(false);

            TeamUtil.addTeamMember(newMember, SiteUtils.getDefaultSite());

            return newMember;
        } else {
            teamMember.setDeleted(false);
            teamMember.setAmpMemberRole(role);
            TeamMemberUtil.updateMember(teamMember);

            return teamMember;
        }
    }
}
