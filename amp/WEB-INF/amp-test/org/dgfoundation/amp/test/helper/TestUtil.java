package org.dgfoundation.amp.test.helper;

import javax.servlet.http.HttpServletRequest;

import org.digijava.kernel.Constants;
import org.digijava.kernel.entity.Locale;
import org.digijava.kernel.exception.DgException;
import org.digijava.kernel.request.Site;
import org.digijava.kernel.request.SiteDomain;
import org.digijava.kernel.util.SiteUtils;
import org.digijava.module.aim.action.Login;
import org.digijava.module.aim.util.DbUtil;
import org.digijava.module.aim.util.TeamMemberUtil;

public class TestUtil {

	public static void setSiteDomain(HttpServletRequest request) throws DgException {

		Site s = SiteUtils.getSite("demo");
		SiteDomain sd = new SiteDomain();
		sd.setSite(s);
		request.setAttribute(Constants.CURRENT_SITE, sd);

	}

	public static void setLocaleEn(HttpServletRequest request) {
		Locale lang = new Locale();
		lang.setCode("en");
		request.setAttribute(Constants.NAVIGATION_LANGUAGE, lang);

	}

	

}
