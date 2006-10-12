package org.digijava.module.aim.action;

import java.util.Collection;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.DynaActionForm;
import org.digijava.module.aim.dbentity.AmpFeature;
import org.digijava.module.aim.helper.Constants;
import org.digijava.module.aim.util.FeaturesUtil;

public class FeatureManager extends Action {
	
	private static Logger logger = Logger.getLogger(FeatureManager.class);
	
	private ServletContext ampContext = null;
	
	public ActionForward execute(ActionMapping mapping,ActionForm form,
			HttpServletRequest request,HttpServletResponse response) throws Exception {
		
		DynaActionForm fmForm = (DynaActionForm) form;
		
		String toggleAction = request.getParameter("toggle");
		String tempFId = request.getParameter("fId");

		Boolean featureOn = new Boolean(true);
		
		if (toggleAction != null && tempFId != null) {
			// Toggle the feature
			try {
				int fId = Integer.parseInt(tempFId);
				AmpFeature feature = FeaturesUtil.toggleFeature(new Integer(fId));
				ampContext = getServlet().getServletContext();
				if (feature != null) {
					if (feature.getCode().equalsIgnoreCase(Constants.ME_FEATURE)) {
						synchronized (ampContext) {
							if (feature.isActive()) {
								ampContext.setAttribute(Constants.ME_FEATURE,featureOn);
							} else {
								if (ampContext.getAttribute(Constants.ME_FEATURE) != null) {
									ampContext.removeAttribute(Constants.ME_FEATURE);
								}
							}
						}
					} else if (feature.getCode().equalsIgnoreCase(Constants.PI_FEATURE)) {
						synchronized (ampContext) {
							if (feature.isActive()) {
								ampContext.setAttribute(Constants.PI_FEATURE,featureOn);
							} else {
								if (ampContext.getAttribute(Constants.PI_FEATURE) != null) {
									ampContext.removeAttribute(Constants.PI_FEATURE);
								}
							}
						}
					} else if (feature.getCode().equalsIgnoreCase(Constants.AA_FEATURE)) {
						synchronized (ampContext) {
							if (feature.isActive()) {
								ampContext.setAttribute(Constants.AA_FEATURE,featureOn);
							} else {
								if (ampContext.getAttribute(Constants.AA_FEATURE) != null) {
									ampContext.removeAttribute(Constants.AA_FEATURE);
								}
							}
						}
					} else if (feature.getCode().equalsIgnoreCase(Constants.CL_FEATURE)) {
						synchronized (ampContext) {
							if (feature.isActive()) {
								ampContext.setAttribute(Constants.CL_FEATURE,featureOn);
							} else {
								if (ampContext.getAttribute(Constants.CL_FEATURE) != null) {
									ampContext.removeAttribute(Constants.CL_FEATURE);
								}
							}
						}
					} else if (feature.getCode().equalsIgnoreCase(Constants.DC_FEATURE)) {
						synchronized (ampContext) {
							if (feature.isActive()) {
								ampContext.setAttribute(Constants.DC_FEATURE,featureOn);
							} else {
								if (ampContext.getAttribute(Constants.DC_FEATURE) != null) {
									ampContext.removeAttribute(Constants.DC_FEATURE);
								}
							}
						}
					} else if (feature.getCode().equalsIgnoreCase(Constants.SC_FEATURE)) {
						synchronized (ampContext) {
							if (feature.isActive()) {
								ampContext.setAttribute(Constants.SC_FEATURE,featureOn);
							} else {
								if (ampContext.getAttribute(Constants.SC_FEATURE) != null) {
									ampContext.removeAttribute(Constants.SC_FEATURE);
								}
							}
						}
					}
				}
			} catch (NumberFormatException nfe) {
				logger.error("Trying to parse " + tempFId + " to int"); 
			}
		}
		
		Collection features = FeaturesUtil.getAMPFeatures();
		fmForm.set("features",features);
		
		return mapping.findForward("forward");
	}
}