/**
 * 
 */
package org.dgfoundation.amp.onepager.web.pages;

import java.util.Date;

import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Button;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.model.Model;
import org.dgfoundation.amp.onepager.components.AmpFundingDetailComponent;
import org.digijava.module.aim.dbentity.AmpCurrency;
import org.digijava.module.aim.dbentity.AmpFundingDetail;

/**
 * @author mihai
 * 
 */
public class HelloWorld extends WebPage {

	private AmpFundingDetail fd = new AmpFundingDetail();
	private AmpFundingDetailComponent fdc;
	private Label response;
	public HelloWorld() {

		fd.setThousandsTransactionAmount(1231232d);
		fd.setTransactionDate(new Date());
		AmpCurrency c = new AmpCurrency();
		c.setCurrencyCode("USD");
		fd.setAmpCurrencyId(c);
		Form f = new Form("form");
		fdc = new AmpFundingDetailComponent(
				"fundingDetail", new Model<AmpFundingDetail>(fd), "Ceva");
		add(f);
		add(response=new Label("response",new Model<String>("")));
		f.add(fdc);
		f.add(new Button("submitButton", new Model("Pressing matters")) {
			@Override
			public void onSubmit() {
				fdc = new AmpFundingDetailComponent(
						"fundingDetail", new Model<AmpFundingDetail>(fd), "Ceva");
				response.setDefaultModelObject("AmpFundingDetail bean: amount="+fd.getThousandsTransactionAmount()+ " transactionDate="+fd.getTransactionDate()+" currency="+fd.getAmpCurrencyId().getCurrencyCode());
			}
		});

		add(new Link("now") {
			@Override
			public void onClick() {
				fd.setTransactionDate(new Date());
			}
		});
	}
}
