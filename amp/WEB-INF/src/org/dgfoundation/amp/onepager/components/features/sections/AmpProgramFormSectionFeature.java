/**
 * Copyright (c) 2010 Development Gateway (www.developmentgateway.org)
 *
 */
package org.dgfoundation.amp.onepager.components.features.sections;

import org.apache.wicket.model.IModel;
import org.apache.wicket.model.PropertyModel;
import org.dgfoundation.amp.onepager.components.features.tables.AmpProgramFormTableFeature;
import org.dgfoundation.amp.onepager.components.fields.AmpTextAreaFieldPanel;
import org.dgfoundation.amp.onepager.util.AmpFMTypes;
import org.digijava.module.aim.dbentity.AmpActivityProgramSettings;
import org.digijava.module.aim.dbentity.AmpActivityVersion;
import org.digijava.module.aim.util.ProgramUtil;

/**
 * @author aartimon@dginternational.org since Oct 26, 2010
 */
public class AmpProgramFormSectionFeature extends
		AmpFormSectionFeaturePanel {

	private static final long serialVersionUID = -6654390083784446344L;

	public AmpProgramFormSectionFeature(String id, String fmName,
			final IModel<AmpActivityVersion> am) throws Exception {
		super(id, fmName, am);
		this.fmType = AmpFMTypes.MODULE;
		AmpActivityProgramSettings npd=ProgramUtil.getAmpActivityProgramSettings(ProgramUtil.NATIONAL_PLAN_OBJECTIVE);
		AmpProgramFormTableFeature npdTable = new AmpProgramFormTableFeature(
				"npoTable", "National Plan Objective", am, ProgramUtil.NATIONAL_PLAN_OBJECTIVE);
		add(npdTable);
		if(npd==null){
			npdTable.setVisible(false);
		}
		AmpActivityProgramSettings pp=ProgramUtil.getAmpActivityProgramSettings(ProgramUtil.PRIMARY_PROGRAM);
		AmpProgramFormTableFeature ppTable = new AmpProgramFormTableFeature(
				"ppTable", "Primary Programs", am, ProgramUtil.PRIMARY_PROGRAM);
		add(ppTable);
		if(pp==null){
			ppTable.setVisible(false);
		}
		AmpActivityProgramSettings  sp=ProgramUtil.getAmpActivityProgramSettings(ProgramUtil.PRIMARY_PROGRAM);
		AmpProgramFormTableFeature spTable = new AmpProgramFormTableFeature(
				"spTable", "Secondary Programs", am, ProgramUtil.SECONDARY_PROGRAM);
		add(spTable);
		if(sp==null){
			spTable.setVisible(false);
		}
		add(new AmpTextAreaFieldPanel<String>("description",
				new PropertyModel<String>(am, "programDescription"),
				"Program Description", true, AmpFMTypes.MODULE));

	}

}
