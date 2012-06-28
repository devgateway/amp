package org.digijava.module.widget.action;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.tiles.ComponentContext;
import org.apache.struts.tiles.actions.TilesAction;
import org.digijava.module.aim.dbentity.AmpClassificationConfiguration;
import org.digijava.module.aim.helper.GlobalSettingsConstants;
import org.digijava.module.aim.util.FeaturesUtil;
import org.digijava.module.aim.util.SectorUtil;
import org.digijava.module.widget.dbentity.AmpDaTable;
import org.digijava.module.widget.dbentity.AmpDaWidgetPlace;
import org.digijava.module.widget.dbentity.AmpParisIndicatorTableWidget;
import org.digijava.module.widget.dbentity.AmpSectorTableWidget;
import org.digijava.module.widget.dbentity.AmpWidget;
import org.digijava.module.widget.dbentity.AmpWidgetIndicatorChart;
import org.digijava.module.widget.dbentity.AmpWidgetTopTenDonorGroups;
import org.digijava.module.widget.form.WidgetTeaserForm;
import org.digijava.module.widget.helper.WidgetVisitor;
import org.digijava.module.widget.helper.WidgetVisitorAdapter;
import org.digijava.module.widget.util.WidgetUtil;

/**
 * Generic widget teaser.
 * Renders staff depending on widget type assigned to the teaser place.
 * @author Irakli Kobiashvili
 *
 */
public class WidgetTeaser extends TilesAction {

    private static Logger logger = Logger.getLogger(WidgetTeaser.class);

    @Override
    public ActionForward execute(ComponentContext context,
            ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
		WidgetTeaserForm wform = (WidgetTeaserForm)form;
        AmpDaWidgetPlace place = WidgetUtil.saveOrUpdatePlace(context);
        String baseCurr= FeaturesUtil.getGlobalSettingValue(GlobalSettingsConstants.BASE_CURRENCY);
        if(baseCurr==null){
        	baseCurr="USD";
        }
        wform.setBaseCurr(baseCurr);

        //we have null if there is no place parameter in context - layout should define it when inserting teaser
		if (place==null){
            logger.debug("No place param specified!");
            wform.setRendertype(WidgetUtil.NO_PLACE_PARAM);
            return null;
        }
        //if we have place parameter then assign place name to form
        wform.setPlaceName(place.getName());
        // check if there is something assigned to place
		if (place!= null && place.getAssignedWidget()==null){
			logger.debug("Rendering empty widget for "+wform.getPlaceName());
            wform.setRendertype(WidgetUtil.EMPTY);
//			String gsValue = FeaturesUtil.getGlobalSettingValue(GlobalSettingsConstants.SHOW_WIDGET_PLACE_NAMES);
//			if (gsValue!=null){
//				wform.setShowPlaceInfo(gsValue.trim().equals("true"));
//			}
            return null;
        }
        //we have widget assigned to teaser place.
        AmpWidget widget = place.getAssignedWidget();
        wform.setId(widget.getId());
        final ArrayList rendertype = new ArrayList();
        WidgetVisitor adapter = new WidgetVisitorAdapter() {

            @Override
            public void visit(AmpWidgetIndicatorChart chart) {
                rendertype.add(WidgetUtil.CHART_INDICATOR);
            }

      

        
            @Override
            public void visit(AmpDaTable table){
                 rendertype.add(WidgetUtil.TABLE);
            }
            @Override
            public void visit(AmpSectorTableWidget sectorTable){
                 rendertype.add(WidgetUtil.SECTOR_TABLE);
            }
            @Override
            public void visit(AmpParisIndicatorTableWidget table){
                 rendertype.add(WidgetUtil.PARIS_INDICAROR_TABLE);
            }
            @Override
            public void visit(AmpWidgetTopTenDonorGroups table) {
                rendertype.add(WidgetUtil.TOP_TEN_DONORS);
            }
        };
        widget.accept(adapter);
        int rType = (Integer) rendertype.get(0);
        wform.setRendertype(rType);
       

        return null;
    }
}
