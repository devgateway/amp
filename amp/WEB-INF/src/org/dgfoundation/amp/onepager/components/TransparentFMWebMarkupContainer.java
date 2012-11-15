/**
 * Copyright (c) 2011 Development Gateway (www.developmentgateway.org)
*/
package org.dgfoundation.amp.onepager.components;

import java.util.LinkedList;
import java.util.StringTokenizer;

import javax.servlet.ServletContext;

import org.apache.wicket.Application;
import org.apache.wicket.markup.html.TransparentWebMarkupContainer;
import org.apache.wicket.model.Model;
import org.apache.wicket.protocol.http.WebApplication;
import org.dgfoundation.amp.onepager.AmpAuthWebSession;
import org.dgfoundation.amp.onepager.util.AmpFMTypes;
import org.dgfoundation.amp.onepager.util.FMInfo;
import org.dgfoundation.amp.onepager.util.FMUtil;
import org.dgfoundation.amp.onepager.util.FMUtil.PathException;
import org.dgfoundation.amp.visibility.AmpTreeVisibility;

/**
 * 
 * @author aartimon@dginternational.org
 * @since Nov 21, 2011
 */
public class TransparentFMWebMarkupContainer extends TransparentWebMarkupContainer {

	private Model<String> model;

	public TransparentFMWebMarkupContainer(String id, Model<String> model) {
		super(id);
		this.model = model;
	}

	@Override
	protected void onConfigure() {
		super.onConfigure();
		LinkedList<FMInfo> fmList;
		try {
			fmList = FMUtil.getFmPath(this);
		} catch (PathException e) {
			return;
		}
		String fmPath = FMUtil.getFmPathString(fmList);
		String names = (String) model.getObject();
		boolean allInvisible = true;
		if (names != null){
			ServletContext context   = ((WebApplication)Application.get()).getServletContext();
			AmpAuthWebSession session = (AmpAuthWebSession) org.apache.wicket.Session.get();
			AmpTreeVisibility ampTreeVisibility=(AmpTreeVisibility) context.getAttribute("ampTreeVisibility");

			StringTokenizer tok = new StringTokenizer(names, ";");
			while (tok.hasMoreElements()) {
				String name = (String) tok.nextElement();
				String fmPathString = fmPath + "/" + name.replaceAll("/", " ");;
				if(ampTreeVisibility!=null)
					if (FMUtil.checkIsVisible(ampTreeVisibility, fmPathString, AmpFMTypes.MODULE)){
						allInvisible = false;
						break;
					}
			}
		}
		boolean fmMode = ((AmpAuthWebSession)getSession()).isFmMode();
		if (allInvisible)
			this.setVisible(fmMode?true:false);
	}
}
