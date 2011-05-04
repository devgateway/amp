/**
 * Copyright (c) 2010 Development Gateway (www.developmentgateway.org)
 *
 */
package org.dgfoundation.amp.onepager.web.pages;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.behavior.HeaderContributor;
import org.apache.wicket.markup.html.JavascriptPackageResource;
import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.model.AbstractReadOnlyModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.protocol.http.servlet.ServletWebRequest;
import org.apache.wicket.util.template.TextTemplateHeaderContributor;
import org.dgfoundation.amp.onepager.AmpAuthWebSession;
import org.dgfoundation.amp.onepager.AmpWebSession;
import org.dgfoundation.amp.onepager.components.features.subsections.AmpSubsectionFeaturePanel;
import org.dgfoundation.amp.onepager.translation.AmpAjaxBehavior;
import org.wicketstuff.jquery.JQueryBehavior;

/**
 * @author mpostelnicu@dgateway.org
 * @since Sep 22, 2010
 */
public class AmpHeaderFooter extends WebPage {
	public AmpHeaderFooter() {
		add(new HeaderContributor(
				new com.google.excanvas.ExCanvasHeaderContributor()));
		add(new JQueryBehavior());
		AmpAjaxBehavior ampajax = new AmpAjaxBehavior();
		add(ampajax);
		final CharSequence callBackUrl = ampajax.getCallbackUrl();

		IModel variablesModel = new AbstractReadOnlyModel() {
			public Map getObject() {
				Map<String, CharSequence> variables = new HashMap<String, CharSequence>(
						2);
				variables.put("callBackUrl", callBackUrl);
				return variables;
			}
		};

		add(TextTemplateHeaderContributor.forJavaScript(AmpAjaxBehavior.class,
				"translations.js", variablesModel));
		add(JavascriptPackageResource.getHeaderContribution(
				AmpSubsectionFeaturePanel.class, "subsectionSlideToggle.js"));
		String changeTrnMode = "Enable TrnMode";
		/*
		 * 
		add(new AjaxLink("changeTrnMode", new Model(changeTrnMode)) {

			@Override
			public void onClick(AjaxRequestTarget arg0) {
				AmpAuthWebSession session = (AmpAuthWebSession) getSession();
				if (session.isTranslatorMode())
					session.setTranslatorMode(false);
				else
					session.setTranslatorMode(true);
				setResponsePage(OnePager.class);
			}
		});

		add(new AjaxLink("changeFmMode", new Model("FM Mode")) {
			@Override
			public void onClick(AjaxRequestTarget arg0) {
				AmpAuthWebSession session = (AmpAuthWebSession) getSession();
				if (session.isFmMode())
					session.setFmMode(false);
				else
					session.setFmMode(true);
				setResponsePage(OnePager.class);
			}
		});
		 */
	}
	
	public HttpServletRequest getServletRequest(){
		ServletWebRequest servletWebRequest = (ServletWebRequest) getRequest();
		HttpServletRequest request = servletWebRequest.getHttpServletRequest();
		return request;
	}
	
	public HttpSession getHttpSession(){
		HttpSession session = getServletRequest().getSession();
		return session;
	}
}
