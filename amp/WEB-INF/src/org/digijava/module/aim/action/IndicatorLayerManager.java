package org.digijava.module.aim.action;

import java.util.HashSet;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.digijava.module.aim.dbentity.AmpIndicatorColor;
import org.digijava.module.aim.dbentity.AmpIndicatorLayer;
import org.digijava.module.aim.form.AddIndicatorLayerForm;
import org.digijava.module.aim.util.ColorRampUtil;
import org.digijava.module.aim.util.DbUtil;
import org.digijava.module.aim.util.DynLocationManagerUtil;
import org.digijava.module.categorymanager.util.CategoryManagerUtil;

public class IndicatorLayerManager extends Action {

	public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws java.lang.Exception {

		AddIndicatorLayerForm indicatorLayerForm = (AddIndicatorLayerForm) form;
		 
		if (indicatorLayerForm.getEvent().equals("manager")) {
				indicatorLayerForm.setIndicatorLayers(DynLocationManagerUtil.getIndicatorLayers());
				return mapping.findForward("viewIndicatorLayerManager");	
			}
		if (indicatorLayerForm.getEvent().equals("new")) {
			indicatorLayerForm.setAdmLevelId(null);
			indicatorLayerForm.setColorRamp(new HashSet<AmpIndicatorColor>());
			indicatorLayerForm.setName(null);
			indicatorLayerForm.setUnit(null);
			indicatorLayerForm.setDescription(null);
			indicatorLayerForm.setIndicatorLayerId(null);
			indicatorLayerForm.setNumberOfClasses(null);
			indicatorLayerForm.setAdmLevelList(CategoryManagerUtil.getAmpCategoryValueCollectionByKey(
					"implementation_location", true));
			return mapping.findForward("addEdit");
		} 
		else if (indicatorLayerForm.getEvent().equals ("delete")) {
			AmpIndicatorLayer layerToDelete = (AmpIndicatorLayer)DbUtil.getObject(AmpIndicatorLayer.class,indicatorLayerForm.getIdOfIndicator());
			DynLocationManagerUtil.deleteIndicatorLayer(layerToDelete);
			indicatorLayerForm.setIndicatorLayers(DynLocationManagerUtil.getIndicatorLayers());
			return mapping.findForward("viewIndicatorLayerManager");	
		}
		else if (indicatorLayerForm.getEvent().equals("edit")) {
			AmpIndicatorLayer layerToEdit = (AmpIndicatorLayer)DbUtil.getObject(AmpIndicatorLayer.class,indicatorLayerForm.getIdOfIndicator());
			indicatorLayerForm.setAdmLevelId(layerToEdit.getAdmLevel().getId());
			indicatorLayerForm.setColorRamp(new HashSet<AmpIndicatorColor>());
			indicatorLayerForm.setName(layerToEdit.getName());
			indicatorLayerForm.setDescription(layerToEdit.getDescription());
			indicatorLayerForm.setUnit(layerToEdit.getUnit());
			indicatorLayerForm.setIndicatorLayerId(layerToEdit.getId());
			indicatorLayerForm.setNumberOfClasses(layerToEdit.getNumberOfClasses());
			indicatorLayerForm.setSelectedColorRampIndex(ColorRampUtil.getIndexByColors(layerToEdit.getColorRamp()));
			indicatorLayerForm.setAdmLevelList(CategoryManagerUtil.getAmpCategoryValueCollectionByKey(
					"implementation_location", true));
			return mapping.findForward("addEdit");
			
		
		}
		else {
			AmpIndicatorLayer indLayer = new AmpIndicatorLayer();
			Set<AmpIndicatorColor> colorRamp = new HashSet<AmpIndicatorColor>();
			if (indicatorLayerForm.getIdOfIndicator() !=null && indicatorLayerForm.getIdOfIndicator()!=0l) {
				org.apache.log4j.Logger logger = Logger.getLogger(IndicatorLayerManager.class);
				indLayer = (AmpIndicatorLayer)DbUtil.getObject(AmpIndicatorLayer.class,indicatorLayerForm.getIdOfIndicator());	
				colorRamp = indLayer.getColorRamp();
				colorRamp.clear();
			}
			String[] colorRampColors = ColorRampUtil.getColorRamp(indicatorLayerForm.getSelectedColorRamp(),
					indicatorLayerForm.getNumberOfClasses());
			for (int i = 0; i < colorRampColors.length; i++) {
				AmpIndicatorColor color = new AmpIndicatorColor();
				int payload = i + 1;
				color.setPayload(new Long(payload));
				color.setColor(colorRampColors[i]);
				colorRamp.add(color);
			}
			indLayer.setName(indicatorLayerForm.getName());
			indLayer.setUnit(indicatorLayerForm.getUnit());
			indLayer.setDescription(indicatorLayerForm.getDescription());
			indLayer.setNumberOfClasses(indicatorLayerForm.getNumberOfClasses());
			indLayer.setAdmLevel(CategoryManagerUtil.getAmpCategoryValueFromDb(Long.valueOf(indicatorLayerForm
					.getAdmLevelId())));
			indLayer.setColorRamp(colorRamp);
			DbUtil.saveOrUpdateObject(indLayer);
			DbUtil.update(indLayer);
			indicatorLayerForm.setIndicatorLayers(DynLocationManagerUtil.getIndicatorLayers());
			return mapping.findForward("added");

		}

	}
	
}
