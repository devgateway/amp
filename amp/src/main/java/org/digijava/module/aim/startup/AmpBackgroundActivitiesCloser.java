package org.digijava.module.aim.startup;


import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

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
import org.hibernate.Session;

public class AmpBackgroundActivitiesCloser 
{
	public final static String AMP_MODIFIER_USER_EMAIL = "amp_modifier@amp.org";
	public final static String AMP_MODIFIER_USER_PASSWORD = "DuMmY_PaSsWoRd_NoBoDy_ShOuLd_UsE_iT";
	public final static String AMP_MODIFIER_ORGANIZATION_NAME = "Development Gateway Internal Org";
	public final static String AMP_MODIFIER_ORGANIZATION_ACRONYM = "DGIO";
	public final static String AMP_MODIFIER_ORGANIZATION_CODE = "DGIO";
	public final static String AMP_MODIFIER_TEAM_NAME = "Development Gateway Internal Workspace";

	/**
	 * gets an AmpOrgType for creating an AmpOrgGroup for creating an AmpOrganisation for creating an User for creating an AmpTeamMember for marking changes done to AmpActivityVersions
	 * @return
	 */
	protected static AmpOrgType getAmpOrgType()
	{
		List<AmpOrgType> allAmpOrgTypes = DbUtil.getAmpOrgTypes();
		AmpOrgType otherOrgType = null, bilateralOrgType = null;
		
		for(AmpOrgType type:allAmpOrgTypes)
		{
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
	
	protected static AmpOrganisation getOrCreateInternalOrganization() throws DgException
	{
		AmpOrganisation organization = DbUtil.getOrganisationByName(AMP_MODIFIER_ORGANIZATION_NAME);
		if (organization != null)
			return organization;
		
		organization = new AmpOrganisation();
		organization.setName(AMP_MODIFIER_ORGANIZATION_NAME);
		organization.setAcronym(AMP_MODIFIER_ORGANIZATION_ACRONYM);
		
		AmpOrgGroup ampOrgGroup = DbUtil.getAmpOrgGroupByName("Other");
		if (ampOrgGroup == null)
		{
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
		organization.setOrgCode(AMP_MODIFIER_ORGANIZATION_CODE);
     
		organization.setDescription("Organization used for internal users of the AMP system");
		DbUtil.saveOrg(organization);
		return organization;
	}
	
	protected static void createAmpModifierUser() throws DgException
	{
		User user = new User(AMP_MODIFIER_USER_EMAIL.toLowerCase(),	"AMP", "Activities Modifier");
		Site site = SiteUtils.getDefaultSite();

		// set client IP address
		user.setModifyingIP("0.0.0.0");

		// set password
		user.setPassword(AMP_MODIFIER_USER_PASSWORD);
		user.setSalt(AMP_MODIFIER_USER_PASSWORD);

		// set Website
		user.setUrl("/");

		// register through
		user.setRegisteredThrough(site);

		// set mailing address
		user.setAddress("empty Address");

		// set organization name
		user.setOrganizationName("Development Gateway");

		user.setPledger(false);
		
		user.setOrganizationTypeOther(" ");
		
		// set country
        user.setCountry(org.digijava.module.aim.util.DbUtil.getDgCountry(FeaturesUtil.getDefaultCountryIso()));
        //////System.out.println(" this is the default country.... "+countryIso);
        //user.setCountry(new Country(countryIso));
		//user.setCountry(new Country(org.digijava.module.aim.helper.Constants.COUNTRY_ISO));

		// set default language
		user.setRegisterLanguage(site.getDefaultLanguage());
		user.setEmailVerified(false);
		user.setActive(false);
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
		AmpUserExtension userExt=new AmpUserExtension();
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
					
		org.digijava.kernel.user.Group memberGroup = org.digijava.module.aim.util.DbUtil.getGroup(org.digijava.kernel.user.Group.MEMBERS, site.getId());
		Long uid[] = new Long[1];
		uid[0] = user.getId();
		org.digijava.module.admin.util.DbUtil.addUsersToGroup(memberGroup.getId(),uid);

		//save amp user extensions;
		AmpUserExtensionPK extPK=new AmpUserExtensionPK(user);
		userExt.setAmpUserExtId(extPK);
		AmpUserUtil.saveAmpUserExtension(userExt);		
	}
	
	
	public static User createActivityCloserUserIfNeeded() throws Exception
	{
		User user = UserUtils.getUserByEmail(AMP_MODIFIER_USER_EMAIL);
		if (user != null)
			return user;

		createAmpModifierUser();
		user = UserUtils.getUserByEmail(AMP_MODIFIER_USER_EMAIL);
		if (user == null)
			throw new RuntimeException("bug creating AMP_modifier_user");
		return user;
	}
	
	/**
	 * gets or created an AmpTeamMember linked to a given workspace, which belongs to the {@link #AMP_MODIFIER_USER_EMAIL}
	 * @param team the workspace
	 * @return
	 * @throws Exception
	 */
	public static AmpTeamMember createActivityCloserTeamMemberIfNeeded(AmpTeam team) throws Exception
	{
		User user = createActivityCloserUserIfNeeded();

		AmpTeamMember teamMember = TeamMemberUtil.getAmpTeamMemberByUserByTeam(user, team);
		if (teamMember != null)
			return teamMember;
		
		AmpTeamMemberRoles role = TeamMemberUtil.getWorkspaceMemberTeamMemberRole();
		AmpTeamMember newMember = new AmpTeamMember();
		newMember.setUser(user);
		newMember.setAmpTeam(team);
		newMember.setAmpMemberRole(role);
		
		newMember.setPublishDocPermission(false);

		TeamUtil.addTeamMember(newMember, SiteUtils.getDefaultSite());
		teamMember = TeamMemberUtil.getAmpTeamMemberByUserByTeam(user, team);
		
		if (teamMember == null)
			return newMember;
		
		return teamMember;
	}
	
//	public static void createActivityCloserTeamIfNeeded() throws Exception
//	{
//		AmpTeam newTeam = new AmpTeam();
//		newTeam.setName(AMP_MODIFIER_TEAM_NAME);
//		newTeam.setTeamCategory(uwForm.getCategory());
//		newTeam.setAccessType(uwForm.getWorkspaceType());
//		newTeam.setAddActivity(uwForm.getAddActivity());
//		newTeam.setComputation(uwForm.getComputation());
//		newTeam.setUseFilter(uwForm.getUseFilter());
//		newTeam.setHideDraftActivities(uwForm.getHideDraftActivities());
//            newTeam.setWorkspaceGroup(CategoryManagerUtil.getAmpCategoryValueFromDb(uwForm.getWorkspaceGroup()));
//        
//        if(uwForm.getUseFilter()==null || !uwForm.getUseFilter()){
//			if (uwForm.getOrganizations() != null) {
//				TreeSet s = new TreeSet();
//				s.addAll(uwForm.getOrganizations());
//				newTeam.setOrganizations(s);
//			}
//        }else{
//        	newTeam.setOrganizations(null);
//        }
//		if (null == uwForm.getRelatedTeam()
//				|| "-1".equals(uwForm.getRelatedTeam().toString().trim()))
//			newTeam.setRelatedTeamId(null);
//		else
//			newTeam.setRelatedTeamId(TeamUtil.getAmpTeam(uwForm
//					.getRelatedTeam()));
//		if (uwForm.getDescription() != null
//				&& uwForm.getDescription().trim().length() > 0) {
//			newTeam.setDescription(uwForm.getDescription());
//		} else {
//			newTeam.setDescription(" ");
//		}
//		
//		if(uwForm.getFmTemplate() != null && !uwForm.getFmTemplate().equals(-1L))
//			newTeam.setFmTemplate(FeaturesUtil.getTemplateById(uwForm.getFmTemplate()));
//		else
//			newTeam.setFmTemplate(null);
//		
//		if (uwForm.getWorkspacePrefix() != null && !uwForm.getWorkspacePrefix().equals(-1L))
//			newTeam.setWorkspacePrefix(CategoryManagerUtil.getAmpCategoryValueFromDb(uwForm.getWorkspacePrefix()));
//	}
}
