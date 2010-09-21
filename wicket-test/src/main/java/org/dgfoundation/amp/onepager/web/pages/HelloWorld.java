/**
 * 
 */
package org.dgfoundation.amp.onepager.web.pages;

import java.util.Date;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.form.AjaxFormValidatingBehavior;
import org.apache.wicket.ajax.markup.html.form.AjaxButton;
import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.util.time.Duration;
import org.dgfoundation.amp.onepager.components.AmpFundingDetailComponent;
import org.digijava.module.aim.dbentity.AmpCurrency;
import org.digijava.module.aim.dbentity.AmpFundingDetail;

/**
 * @author mihai
 * 
 */
public class HelloWorld extends WebPage {
	private final AmpFundingDetail fd = new AmpFundingDetail();
	

	public HelloWorld() {

		fd.setThousandsTransactionAmount(1231232d);
		fd.setTransactionDate(new Date());
		AmpCurrency c = new AmpCurrency();
		c.setCurrencyCode("USD");
		fd.setAmpCurrencyId(c);
		add(new FeedbackPanel("feedback"));
		
		Form<AmpFundingDetail> f = new Form<AmpFundingDetail>("form", new CompoundPropertyModel<AmpFundingDetail>(fd));
		add(f);
		f.setOutputMarkupId(true);
		 
		final AmpFundingDetailComponent fdc = new AmpFundingDetailComponent(
				"fundingDetail", "Ceva");
		fdc.setOutputMarkupId(true);
		f.add(fdc);

		final Label response = new Label("response",new Model<String>(""));
		response.setOutputMarkupId(true);
		add(response);
		f.add(new AjaxButton("submitButton", new Model("Pressing matters")) {
			@Override
			protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
//				fdc = new AmpFundingDetailComponent(
//						"fundingDetail", new Model<AmpFundingDetail>(fd), "Ceva");
				
				response.setDefaultModelObject("AmpFundingDetail bean: amount="+fd.getThousandsTransactionAmount()+ " transactionDate="+fd.getTransactionDate()+" currency="+fd.getAmpCurrencyId().getCurrencyCode());
				target.addComponent(response);
			}
			
			@Override
			protected void onError(AjaxRequestTarget target, Form<?> form) {
				target.addComponent(response);
			}
			
			
		});
		
		AjaxFormValidatingBehavior.addToAllFormComponents(f, "onkeyup", Duration.ONE_SECOND);
		add(new Link("now") {
			@Override
			public void onClick() {
				fd.setTransactionDate(new Date());
			}
		});
	}
}
