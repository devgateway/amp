/**
 * Copyright (c) 2010 Development Gateway (www.developmentgateway.org)
 *
 */
package org.dgfoundation.amp.onepager.components.features.sections;

import org.apache.wicket.markup.html.panel.EmptyPanel;
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
                "npoTable", "National Plan Objective", am, ProgramUtil.NATIONAL_PLAN_OBJECTIVE,true);
        if(npd==null)
            add(new EmptyPanel("npoTable"));
        else
            add(npdTable);
        AmpActivityProgramSettings pp=ProgramUtil.getAmpActivityProgramSettings(ProgramUtil.PRIMARY_PROGRAM);
        AmpProgramFormTableFeature ppTable = new AmpProgramFormTableFeature(
                "ppTable", "Primary Programs", am, ProgramUtil.PRIMARY_PROGRAM);
        if(pp==null)
            add(new EmptyPanel("ppTable"));
        else
            add(ppTable);
        AmpActivityProgramSettings  sp=ProgramUtil.getAmpActivityProgramSettings(ProgramUtil.SECONDARY_PROGRAM);
        AmpProgramFormTableFeature spTable = new AmpProgramFormTableFeature(
                "spTable", "Secondary Programs", am, ProgramUtil.SECONDARY_PROGRAM);
        if(sp==null)
            add(new EmptyPanel("spTable"));
        else
            add(spTable);
        AmpActivityProgramSettings  tp=ProgramUtil.getAmpActivityProgramSettings(ProgramUtil.TERTIARY_PROGRAM);
        AmpProgramFormTableFeature tpTable = new AmpProgramFormTableFeature(
                "tpTable", "Tertiary Programs", am, ProgramUtil.TERTIARY_PROGRAM);
        if(tp==null)
            add(new EmptyPanel("tpTable"));
        else
            add(tpTable);
        add(new AmpTextAreaFieldPanel("description",
                new PropertyModel<>(am, "programDescription"),
                "Program Description", true, AmpFMTypes.MODULE));

    }

}
