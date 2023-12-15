/**
 * Copyright (c) 2010 Development Gateway (www.developmentgateway.org)
 *
*/
package org.dgfoundation.amp.onepager.components.features.sections;

import java.util.List;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.markup.repeater.RepeatingView;
import org.apache.wicket.model.IModel;
import org.apache.wicket.request.cycle.RequestCycle;
import org.dgfoundation.amp.onepager.components.features.tables.AmpSectorsFormTableFeature;
import org.dgfoundation.amp.onepager.util.AmpFMTypes;
import org.digijava.module.aim.dbentity.AmpActivityVersion;
import org.digijava.module.aim.dbentity.AmpClassificationConfiguration;
import org.digijava.module.aim.dbentity.AmpSector;
import org.digijava.module.aim.util.SectorUtil;

import org.dgfoundation.amp.onepager.interfaces.ISectorTableUpdateListener;

/**
 * @author mpostelnicu@dgateway.org
 * since Oct 20, 2010
 */
public class AmpSectorsFormSectionFeature extends AmpFormSectionFeaturePanel
        implements ISectorTableUpdateListener {

    private static final long serialVersionUID = -5601918041949098629L;

    private AmpSectorsFormTableFeature primarySectorsTable;
    private AmpSectorsFormTableFeature secondarySectorsTable;

    /**
     * @param id
     * @param fmName
     * @throws Exception
     */
    public AmpSectorsFormSectionFeature(String id, String fmName,final IModel<AmpActivityVersion> am)
            throws Exception {
        super(id, fmName, am);
        this.fmType = AmpFMTypes.MODULE;

        RepeatingView view = new RepeatingView("allSectorsTables");
        view.setOutputMarkupId(true);
        add(view);

        List<AmpClassificationConfiguration> allClassificationConfigs = SectorUtil.getAllClassificationConfigsOrdered();

        AmpClassificationConfiguration primaryConf = new AmpClassificationConfiguration();
        AmpClassificationConfiguration secondaryConf = new AmpClassificationConfiguration();
        for (AmpClassificationConfiguration conf : allClassificationConfigs) {
            if (conf.getName().equals(AmpClassificationConfiguration.PRIMARY_CLASSIFICATION_CONFIGURATION_NAME)) {
                primaryConf = conf;
            } else if (conf.getName().equals(AmpClassificationConfiguration.SECONDARY_CLASSIFICATION_CONFIGURATION_NAME)) {
                secondaryConf = conf;
            }
        }

        primarySectorsTable = new AmpSectorsFormTableFeature(view.newChildId(),
                primaryConf.getName() + " Sectors", am, primaryConf);
        primarySectorsTable.setUpdateListener(this);

        secondarySectorsTable = new AmpSectorsFormTableFeature(view.newChildId(),
                secondaryConf.getName() + " Sectors", am, secondaryConf);

        view.add(primarySectorsTable);
        view.add(secondarySectorsTable);
    }


    @Override
    public void onUpdate(List<AmpSector> data) {
        if (secondarySectorsTable != null) {
            // Update user interface if the request is ajax
             AjaxRequestTarget target = RequestCycle.get().find(AjaxRequestTarget.class);
             if (target != null) {
                 secondarySectorsTable.updateBasedOnData(data);
                 target.add(secondarySectorsTable.getSearchSectors());
             }
        }
    }

}
