package org.dgfoundation.amp.test.util;

import java.lang.reflect.Method;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.digijava.kernel.Constants;
import org.digijava.kernel.entity.Locale;
import org.digijava.kernel.exception.DgException;
import org.digijava.kernel.request.Site;
import org.digijava.kernel.request.SiteDomain;
import org.digijava.kernel.user.User;
import org.digijava.kernel.util.SiteUtils;
import org.digijava.module.aim.dbentity.AmpTeamMember;
import org.digijava.module.aim.helper.TeamMember;
import org.digijava.module.aim.util.DbUtil;
import org.digijava.module.aim.util.TeamMemberUtil;

/**
 * A Helper class for test cases
 * 
 * @author Sebas
 * 
 */
public class TestUtil {

	/**
	 * Set the AMP site to CURRENT_SITE request attribute
	 * 
	 * @param request
	 * @throws DgException
	 */
	public static void setSiteDomain(HttpServletRequest request) throws DgException {
		// AMP usually is the site #3
		Site s = SiteUtils.getSite(3L);
		SiteDomain sd = new SiteDomain();
		sd.setSite(s);
		request.setAttribute(Constants.CURRENT_SITE, sd);

	}

	/**
	 * Set English Locale to NAVIGATION_LANGUAGE request Attribute
	 * 
	 * @param request
	 */
	public static void setLocaleEn(HttpServletRequest request) {
		Locale lang = new Locale();
		lang.setCode("en");
		request.setAttribute(Constants.NAVIGATION_LANGUAGE, lang);

	}

	/**
	 * Get the first team member of ATL and set it to the currentMember session
	 * attribute
	 * 
	 * @param session
	 * @throws Exception
	 */
	public static void setCurrentMemberFirstATLTeam(HttpSession session) throws Exception {
		User user = DbUtil.getUser("atl@amp.org");

		if (user == null) {
			throw new Exception("There is not ATL user ");
		}
		java.util.List<AmpTeamMember> list = TeamMemberUtil.getTeamMemberbyUserId(user.getId());

		if (list.size() < 1) {
			throw new Exception("There is not teamember for the user ATL");
		}
		Long id = list.toArray(new Long[list.size()])[0];
		AmpTeamMember teamember = TeamMemberUtil.getAmpTeamMember(id);
		TeamMember finalTeamMember = new TeamMember();
		finalTeamMember.setMemberId(teamember.getAmpTeamMemId());
		finalTeamMember.setTeamId(teamember.getAmpTeam().getAmpTeamId());
		session.setAttribute("currentMember", finalTeamMember);

	}

	/**
	 * This will invoke the first method with name mehtodName from class
	 * objectClass. If there are 2 methods with the same name in does NOT check
	 * the methods signature.
	 * 
	 * @param objectClass
	 * @param classInstance
	 * @param methodName
	 * @param params
	 * @return
	 */
	public static Object runPrivateMethod(Class objectClass, Object classInstance, String methodName, Object[] params) {
		Method[] methods = objectClass.getDeclaredMethods();

		for (int i = 0; i < methods.length; i++) {
			if (methods[i].getName().equals(methodName)) {
				try {
					methods[i].setAccessible(true);
					return methods[i].invoke(classInstance, params);
				} catch (Exception e) {
					e.printStackTrace();
				}
				break;
			}
		}
		return null;
	}
}
