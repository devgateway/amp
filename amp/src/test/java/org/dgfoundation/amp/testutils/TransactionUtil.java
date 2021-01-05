package org.dgfoundation.amp.testutils;

import org.apache.struts.mock.MockHttpServletRequest;
import org.apache.struts.mock.MockHttpSession;
import org.digijava.kernel.entity.Locale;
import org.digijava.kernel.request.Site;
import org.digijava.kernel.request.SiteDomain;
import org.digijava.kernel.request.TLSUtils;
import org.digijava.kernel.translator.util.TrnUtil;

import java.util.ArrayList;

public class TransactionUtil {

    public static void setUpWorkspaceEmptyPrefixes() {
        MockHttpServletRequest mockRequest = new MockHttpServletRequest(new MockHttpSession());
        Site site = new Site("Test Site", "1");
        site.setDefaultLanguage(new Locale("en", "English"));
        SiteDomain siteDomain = new SiteDomain();
        siteDomain.setSite(site);
        siteDomain.setDefaultDomain(true);
        TLSUtils.populate(mockRequest, siteDomain);
        TLSUtils.getRequest().setAttribute(TrnUtil.PREFIXES, new ArrayList<>());
    }
}
