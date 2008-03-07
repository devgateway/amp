/*
 * RegisterUser.java
 */

package org.digijava.module.aim.action;

import java.util.Collection;
import java.util.Iterator;

import org.apache.log4j.Logger;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.digijava.kernel.Constants;
import org.digijava.kernel.dbentity.Country;
import org.digijava.kernel.entity.Locale;
import org.digijava.kernel.entity.UserLangPreferences;
import org.digijava.kernel.request.Site;
import org.digijava.kernel.request.SiteDomain;
import org.digijava.kernel.user.Group;
import org.digijava.kernel.user.User;
import org.digijava.kernel.util.DgUtil;
import org.digijava.kernel.util.RequestUtils;
import org.digijava.module.aim.dbentity.AmpGlobalSettings;
import org.digijava.module.aim.dbentity.AmpOrgGroup;
import org.digijava.module.aim.dbentity.AmpOrgType;
import org.digijava.module.aim.dbentity.AmpOrganisation;
import org.digijava.module.aim.dbentity.AmpUserExtension;
import org.digijava.module.aim.dbentity.AmpUserExtensionPK;
import org.digijava.module.aim.form.OrgManagerForm;
import org.digijava.module.aim.util.FeaturesUtil;
import org.digijava.module.um.form.UserRegisterForm;
import org.digijava.module.um.util.AmpUserUtil;
import org.digijava.module.um.util.DbUtil;

public class RegisterUser extends Action {

	private static Logger logger = Logger.getLogger(RegisterUser.class);

	public ActionForward execute(ActionMapping mapping, ActionForm form,
			javax.servlet.http.HttpServletRequest request,
			javax.servlet.http.HttpServletResponse response) throws Exception {

		UserRegisterForm userRegisterForm = (UserRegisterForm) form;

		logger.debug("In UserRegisterAction");

		try {

			User user = new User(userRegisterForm.getEmail().toLowerCase(),
					userRegisterForm.getFirstNames(), userRegisterForm
							.getLastName());

			// set client IP address
			user.setModifyingIP(RequestUtils.getRemoteAddress(request));

			// set password
			user.setPassword(userRegisterForm.getPassword().trim());
			user.setSalt(userRegisterForm.getPassword().trim());

			// set Website
			user.setUrl(userRegisterForm.getWebSite());

			// register through
			user.setRegisteredThrough(RequestUtils.getSite(request));

			// set mailing address
			user.setAddress(userRegisterForm.getMailingAddress());

			// set organization name
			user.setOrganizationName(userRegisterForm.getOrganizationName());

			user.setOrganizationTypeOther(new String(" "));

			// set country
            ;
            user.setCountry(org.digijava.module.aim.util.DbUtil.getDgCountry(userRegisterForm.getSelectedCountryResidence()));
            //System.out.println(" this is the default country.... "+countryIso);
            //user.setCountry(new Country(countryIso));
			//user.setCountry(new Country(org.digijava.module.aim.helper.Constants.COUNTRY_ISO));

			// set default language
			user.setRegisterLanguage(RequestUtils
					.getNavigationLanguage(request));

			SiteDomain siteDomain = (SiteDomain) request
					.getAttribute(Constants.CURRENT_SITE);

			// ------------- SET USER LANGUAGES
			UserLangPreferences userLangPreferences = new UserLangPreferences(
					user, DgUtil.getRootSite(siteDomain.getSite()));

			Locale language = new Locale();
			language.setCode(userRegisterForm.getSelectedLanguage());

			// set alert language
			userLangPreferences.setAlertsLanguage(language);

			// set navigation language
			userLangPreferences.setNavigationLanguage(RequestUtils
					.getNavigationLanguage(request));
			user.setUserLangPreferences(userLangPreferences);

			// ===== start user extension setup =====
			AmpUserExtension userExt=new AmpUserExtension();
			// org type
			AmpOrgType orgType=org.digijava.module.aim.util.DbUtil.getAmpOrgType(userRegisterForm.getSelectedOrgType());
			userExt.setOrgType(orgType);
			AmpOrgGroup orgGroup=org.digijava.module.aim.util.DbUtil.getAmpOrgGroup(userRegisterForm.getSelectedOrgGroup());
			userExt.setOrgGroup(orgGroup);
			AmpOrganisation organ = org.digijava.module.aim.util.DbUtil.getOrganisation(userRegisterForm.getSelectedOrganizationId());
			userExt.setOrganization(organ);
			// ===== end user extension setup =====

			// if email register get error message

			if (DbUtil.isRegisteredEmail(user.getEmail())) {
				ActionErrors errors = new ActionErrors();
				errors.add(ActionErrors.GLOBAL_ERROR, new ActionError("error.registration.emailexits"));
				saveErrors(request, errors);
				//return (new ActionForward(mapping.getInput()));
				return (mapping.getInputForward());
			} else {
				DbUtil.registerUser(user);
				Site site = RequestUtils.getSite(request);
				Group memberGroup = org.digijava.module.aim.util.DbUtil.getGroup(Group.MEMBERS,site.getId());
				Long uid[] = new Long[1];
				uid[0] = user.getId();
				org.digijava.module.admin.util.DbUtil.addUsersToGroup(memberGroup.getId(),uid);

				//save amp user extensions;
				AmpUserExtensionPK extPK=new AmpUserExtensionPK(user);
				userExt.setAmpUserExtId(extPK);
				AmpUserUtil.saveAmpUserExtension(userExt);
			}



		} catch (Exception e) {
			logger.error("Exception from RegisterUser :" + e);
		}

		return (mapping.findForward("forward"));
	}
}
