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
		
		AmpProgramFormTableFeature npdTable = new AmpProgramFormTableFeature(
				"npoTable", "National Plan Objective", am, ProgramUtil.NATIONAL_PLAN_OBJECTIVE);
		add(npdTable);
		AmpProgramFormTableFeature ppTable = new AmpProgramFormTableFeature(
				"ppTable", "Primary Programs", am, ProgramUtil.PRIMARY_PROGRAM);
		add(ppTable);
		AmpProgramFormTableFeature spTable = new AmpProgramFormTableFeature(
				"spTable", "Secondary Programs", am, ProgramUtil.SECONDARY_PROGRAM);
		add(spTable);
		add(new AmpTextAreaFieldPanel<String>("description",
				new PropertyModel<String>(am, "programDescription"),
				"Program Description", true, AmpFMTypes.FEATURE));

	}

}
