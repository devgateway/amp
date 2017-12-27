package org.dgfoundation.amp.onepager.components.features.sections;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.PropertyModel;
import org.dgfoundation.amp.onepager.OnePagerUtil;
import org.dgfoundation.amp.onepager.components.ListEditor;
import org.dgfoundation.amp.onepager.components.ListEditorRemoveButton;
import org.dgfoundation.amp.onepager.components.fields.AmpActivityContractsField;
import org.dgfoundation.amp.onepager.components.fields.AmpAjaxLinkField;
import org.dgfoundation.amp.onepager.models.PersistentObjectModel;
import org.digijava.module.aim.dbentity.AmpActivityContracts;
import org.digijava.module.aim.dbentity.AmpActivityVersion;


import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

public class AmpContractInformationFormSectionFeature extends AmpFormSectionFeaturePanel {
    public AmpContractInformationFormSectionFeature(String id, String fmName,
                                                    final IModel<AmpActivityVersion> am) throws Exception {
        super(id, fmName, am);
        final PropertyModel<Set<AmpActivityContracts>> setModel = new PropertyModel<Set<AmpActivityContracts>>(am,
                "activityContracts");
        if (setModel.getObject() == null) {
            setModel.setObject(new TreeSet<AmpActivityContracts>());
        }
        final ListEditor<AmpActivityContracts> list;

        IModel<List<AmpActivityContracts>> listModel = OnePagerUtil.getReadOnlyListModelFromSetModel(setModel);

        list = new ListEditor<AmpActivityContracts>("list", setModel) {
            private static final long serialVersionUID = 1L;

            @Override
            protected void onPopulateItem(
                    final org.dgfoundation.amp.onepager.components.ListItem<AmpActivityContracts> comp) {

                AmpActivityContractsField acf = new AmpActivityContractsField("activityContract", am,
                        PersistentObjectModel
                                .getModel(comp.getModelObject()), "activityContracts");
                comp.add(acf);
                ListEditorRemoveButton delButton = new ListEditorRemoveButton("deleteContract", "Delete contract");
                comp.add(delButton);
            }
        };
        add(list);


        AmpAjaxLinkField addbutton = new AmpAjaxLinkField("addbutton", "Add Contract", "Add Contract") {
            @Override
            protected void onClick(AjaxRequestTarget target) {
                AmpActivityContracts issues = new AmpActivityContracts();
                issues.setContractDescription(new String(""));
                issues.setContractDate(new Date());
                issues.setContractAmount(new Double("0"));
                issues.setActivity(am.getObject());
                list.addItem(issues);
                target.add(this.getParent());
            }
        };
        add(addbutton);
    }
}
