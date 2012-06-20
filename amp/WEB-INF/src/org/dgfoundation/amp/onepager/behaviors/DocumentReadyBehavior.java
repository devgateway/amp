package org.dgfoundation.amp.onepager.behaviors;

import java.util.HashMap;

import org.apache.wicket.Component;
import org.apache.wicket.behavior.Behavior;
import org.apache.wicket.markup.html.IHeaderResponse;
import org.apache.wicket.request.resource.PackageResourceReference;
import org.apache.wicket.util.template.JavaScriptTemplate;
import org.apache.wicket.util.template.PackageTextTemplate;
import org.apache.wicket.util.template.TextTemplateHeaderContributor;
import org.dgfoundation.amp.onepager.OnePagerConst;
import org.dgfoundation.amp.onepager.translation.AmpAjaxBehavior;
import org.digijava.module.aim.helper.GlobalSettingsConstants;
import org.digijava.module.aim.util.FeaturesUtil;

public class DocumentReadyBehavior extends Behavior {

	private static final long serialVersionUID = 1L;
	public static final String JS_FILE_NAME = "documentReady.js";

	@Override
	public void renderHead(Component component, IHeaderResponse response) {
		super.renderHead(component, response);
		
		HashMap<String, Object> variables = new HashMap<String, Object>();
		String activityFormOnePager = "false";
		try {
			activityFormOnePager = FeaturesUtil.getGlobalSettingValue(
					GlobalSettingsConstants.ACTIVITY_FORM_ONE_PAGER);
		} catch (Exception ignored) {}
		variables.put("onepagerMode", activityFormOnePager);
		variables.put("onepagerPath", "/" + OnePagerConst.ONEPAGER_URL_PREFIX + "/" + OnePagerConst.ONEPAGER_URL_PARAMETER_ACTIVITY + "/");
		
		
		PackageTextTemplate ptt = new PackageTextTemplate(DocumentReadyBehavior.class, JS_FILE_NAME);
		ptt.interpolate(variables);
		JavaScriptTemplate jst = new JavaScriptTemplate(ptt);
		response.renderString(jst.asString());
	}
	
	
}
