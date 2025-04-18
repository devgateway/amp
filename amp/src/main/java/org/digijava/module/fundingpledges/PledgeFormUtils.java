package org.digijava.module.fundingpledges;

import javax.servlet.http.HttpServletRequest;

public final class PledgeFormUtils {
    private PledgeFormUtils() {

    }
    public static void pumpFlashAttribute(HttpServletRequest request, String attrName) {
        if (request.getSession().getAttribute(attrName) != null) {
            request.setAttribute(attrName, request.getSession().getAttribute(attrName));
            request.getSession().removeAttribute(attrName);
        }
    }
}
