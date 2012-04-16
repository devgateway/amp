package org.dgfoundation.amp.onepager.components;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.form.AjaxFormComponentUpdatingBehavior;
import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.markup.html.form.IOnChangeListener;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.dgfoundation.amp.onepager.components.fields.AbstractAmpAutoCompleteTextField;
import org.dgfoundation.amp.onepager.components.fields.AmpSelectFieldPanel;
import org.dgfoundation.amp.onepager.models.AbstractAmpAutoCompleteModel.PARAM;
import org.dgfoundation.amp.onepager.models.AmpOrganisationSearchModel;
import org.dgfoundation.amp.onepager.translation.TrnLabel;
import org.dgfoundation.amp.onepager.yui.AmpAutocompleteFieldPanel;
import org.digijava.kernel.exception.DgException;
import org.digijava.kernel.persistence.PersistenceManager;
import org.digijava.module.aim.dbentity.AmpOrganisation;
import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;


public class AmpSearchOrganizationComponent<T> extends AmpComponentPanel<T>  implements IOnChangeListener{

	private AmpSelectFieldPanel<String> orgTypePanel;
	private AmpAutocompleteFieldPanel<AmpOrganisation> autocompletePanel;
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public AmpSearchOrganizationComponent(String id, IModel<T> model,
			String fmName,  final AmpAutocompleteFieldPanel<AmpOrganisation> autocompletePanel ) {
		super(id, model, fmName);
		//TrnLabel selectOrgTypeLabel = new TrnLabel("selectOrgTypeLabel", "Select Organization Type");
		//add(selectOrgTypeLabel);
		orgTypePanel = new AmpSelectFieldPanel<String>("selectOrgType", new Model<String>(), loadOrgTypes(), "Select Organization Type", false, false);
	    
		orgTypePanel.getChoiceContainer().add(new AjaxFormComponentUpdatingBehavior("onchange") {
				@Override
				protected  void onUpdate(AjaxRequestTarget target)
				{
					
					String org_type = (String) orgTypePanel.getChoiceContainer().getModelObject();
					if(org_type != null)						
					   autocompletePanel.getModelParams().put(AmpOrganisationSearchModel.PARAM.TYPE_FILTER, org_type);
					else
						autocompletePanel.getModelParams().remove(AmpOrganisationSearchModel.PARAM.TYPE_FILTER);
					target.addComponent(autocompletePanel);
				}
				
		});
		add(orgTypePanel);
	    
	    
	    orgTypePanel.setOutputMarkupId(true);
		this.autocompletePanel = autocompletePanel;
		add(autocompletePanel);

	}
	
	
	private Session session;
	
	protected List<String> loadOrgTypes() {
		List<String> ret = null;
		try {

			session = PersistenceManager.getRequestDBSession();
			Criteria crit = session.createCriteria(AmpOrganisation.class);
			crit.setCacheable(true);
			
			crit.setProjection(Projections.distinct(Projections.property("orgType")));
		//	crit.setProjection(Projections.distinct(Projections.property("orgCode")));

			crit.addOrder(Order.asc("orgType"));
		
			ret = crit.list();

		} catch (HibernateException e) {
			throw new RuntimeException(e);
		} catch (DgException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			try {
				PersistenceManager.releaseSession(session);
			} catch (HibernateException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		return ret;
	}

	@Override
	public void onSelectionChanged() {
		
		autocompletePanel.getModelParams().put(AmpOrganisationSearchModel.PARAM.TYPE_FILTER, orgTypePanel.getChoiceContainer().getValue());
	}

	
	

}
