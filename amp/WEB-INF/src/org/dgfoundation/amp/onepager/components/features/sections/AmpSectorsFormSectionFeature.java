/**
 * Copyright (c) 2010 Development Gateway (www.developmentgateway.org)
 *
*/
package org.dgfoundation.amp.onepager.components.features.sections;

import java.util.List;

import org.apache.wicket.markup.repeater.RepeatingView;
import org.apache.wicket.model.IModel;
import org.dgfoundation.amp.onepager.components.features.tables.AmpSectorsFormTableFeature;
import org.digijava.module.aim.dbentity.AmpActivity;
import org.digijava.module.aim.dbentity.AmpClassificationConfiguration;
import org.digijava.module.aim.util.SectorUtil;

/**
 * @author mpostelnicu@dgateway.org
 * since Oct 20, 2010
 */
public class AmpSectorsFormSectionFeature extends AmpFormSectionFeaturePanel {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5601918041949098629L;

	/**
	 * @param id
	 * @param fmName
	 * @throws Exception
	 */
	public AmpSectorsFormSectionFeature(String id, String fmName,final IModel<AmpActivity> am)
			throws Exception {
		super(id, fmName, am);
		RepeatingView view = new RepeatingView("allSectorsTables");
		view.setOutputMarkupId(true);
		add(view);
		List<AmpClassificationConfiguration> allClassificationConfigs = SectorUtil.getAllClassificationConfigs();
		for (AmpClassificationConfiguration sectorConf : allClassificationConfigs) {
			AmpSectorsFormTableFeature sectorsTable=new AmpSectorsFormTableFeature(view.newChildId(), sectorConf.getName()+" Sectors", am,sectorConf);
		    view.add(sectorsTable);	
		}
	    
	}

}
