/**
 * Copyright (c) 2011 Development Gateway (www.developmentgateway.org)
 *
 */
package org.dgfoundation.amp.onepager.components.features.sections;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.PropertyModel;
import org.dgfoundation.amp.onepager.OnePagerUtil;
import org.dgfoundation.amp.onepager.components.ListEditor;
import org.dgfoundation.amp.onepager.components.ListEditorRemoveButton;
import org.dgfoundation.amp.onepager.components.features.items.AmpContractsItemFeaturePanel;
import org.dgfoundation.amp.onepager.components.fields.AmpAjaxLinkField;
import org.digijava.module.aim.dbentity.AmpActivityVersion;
import org.digijava.module.aim.dbentity.IPAContract;
import org.digijava.module.aim.util.CurrencyUtil;

import java.util.Comparator;
import java.util.HashSet;
import java.util.Set;

/**
 * Contracting section
 * @author aartimon@dginternational.org 
 * @since Feb 7, 2011
 */
public class AmpContractingFormSectionFeature extends AmpFormSectionFeaturePanel {

    public AmpContractingFormSectionFeature(String id, String fmName,
            final IModel<AmpActivityVersion> am) throws Exception {
        super(id, fmName, am);
        final IModel<Set<IPAContract>> setModel = new PropertyModel<Set<IPAContract>>(am, "contracts");
        
        if (setModel.getObject() == null){
            setModel.setObject(new HashSet<IPAContract>());
        }
        
        Comparator<IPAContract> ipaComparator = new Comparator<IPAContract>() {
            @Override
            public int compare(IPAContract o1, IPAContract o2) {
                if (o1 == null || o1.getContractName() == null)
                    return 1;
                if (o2 == null || o2.getContractName() == null)
                    return -1;
                return o1.getContractName().compareTo(o2.getContractName());
            }
        };
        
        final ListEditor<IPAContract> list = new ListEditor<IPAContract>("list", setModel, ipaComparator) {
            private static final long serialVersionUID = 1L;

            @Override
            protected void onPopulateItem(
                    org.dgfoundation.amp.onepager.components.ListItem<IPAContract> item) {
                AmpContractsItemFeaturePanel contractItem = new AmpContractsItemFeaturePanel(
                        "item", "Contract Item", item.getModel());
                item.add(contractItem);
                ListEditorRemoveButton deleteField = new ListEditorRemoveButton("delete", "Delete Contract Item", 
                        "Do you really want to delete this contract?");
                item.add(deleteField);
            }
        };
        add(list);
        

        AmpAjaxLinkField addbutton = new AmpAjaxLinkField("add", "Add Contract", "Add Contract") {
            @Override
            public void onClick(AjaxRequestTarget target) {
                IPAContract comp = new IPAContract();
                comp.setActivity(am.getObject());
                //setModel.getObject().add(comp);
                comp.setTotalAmountCurrency(CurrencyUtil.getWicketWorkspaceCurrency());
                list.addItem(comp);
                target.add(this.getParent());
                target.appendJavaScript(OnePagerUtil.getToggleChildrenJS(AmpContractingFormSectionFeature.this));
            }
        };
        add(addbutton);
    }

}
