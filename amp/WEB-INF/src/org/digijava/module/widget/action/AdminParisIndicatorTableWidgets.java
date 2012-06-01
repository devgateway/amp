package org.digijava.module.widget.action;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.actions.DispatchAction;
import org.dgfoundation.amp.utils.AmpCollectionUtils;
import org.digijava.kernel.util.RequestUtils;
import org.digijava.module.aim.dbentity.AmpAhsurveyIndicator;
import org.digijava.module.aim.util.DbUtil;
import org.digijava.module.aim.util.FeaturesUtil;
import org.digijava.module.widget.dbentity.AmpDaWidgetPlace;
import org.digijava.module.widget.dbentity.AmpParisIndicatorBaseTargetValues;
import org.digijava.module.widget.dbentity.AmpParisIndicatorTableWidget;
import org.digijava.module.widget.form.AdminParisIndicatorTableWidgetForm;
import org.digijava.module.widget.util.ParisIndicatorTableWidgetUtil;
import org.digijava.module.widget.util.WidgetUtil;

public class AdminParisIndicatorTableWidgets extends DispatchAction {

    @Override
    public ActionForward unspecified(ActionMapping mapping,
            ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
    	
    	HttpSession session = request.getSession();
		if (!RequestUtils.isAdmin(response, session, request)) {
			return null;
		}
		
        return viewAll(mapping, form, request, response);
    }

    public ActionForward create(ActionMapping mapping,
            ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
    	
    	HttpSession session = request.getSession();
		if (!RequestUtils.isAdmin(response, session, request)) {
			return null;
		}
		
        AdminParisIndicatorTableWidgetForm tableForm = (AdminParisIndicatorTableWidgetForm) form;
        List<AmpParisIndicatorBaseTargetValues> indicators = new ArrayList<AmpParisIndicatorBaseTargetValues>();
        Collection<AmpAhsurveyIndicator> parisIndicators = DbUtil.getAllAhSurveyIndicators();
        Iterator<AmpAhsurveyIndicator> parisIndicatorsIter = parisIndicators.iterator();
        while (parisIndicatorsIter.hasNext()) {
            AmpAhsurveyIndicator parisIndicator = parisIndicatorsIter.next();
            AmpParisIndicatorBaseTargetValues indicator = new AmpParisIndicatorBaseTargetValues();
            indicator.setParisIndicator(parisIndicator);
            indicators.add(indicator);
        }
        tableForm.setParisIndicators(indicators);
        tableForm.setPiTableWidgetId(null);
        tableForm.setName(null);
        Long yearFrom = Long.parseLong(FeaturesUtil.getGlobalSettingValue(org.digijava.module.aim.helper.Constants.GlobalSettings.YEAR_RANGE_START));
        Long countYear = Long.parseLong(FeaturesUtil.getGlobalSettingValue(org.digijava.module.aim.helper.Constants.GlobalSettings.NUMBER_OF_YEARS_IN_RANGE));
        tableForm.setYears(new ArrayList<Long>());
        for (long i = yearFrom; i <= (yearFrom + countYear); i++) {
            tableForm.getYears().add(new Long(i));
        }
        tableForm.setSelPlaces(null);
        tableForm.setPlaces(WidgetUtil.getAllPlaces());
        tableForm.setDonorGroupYearColumn(null);
        return mapping.findForward("create");

    }

    public ActionForward viewAll(ActionMapping mapping,
            ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
    	
    	HttpSession session = request.getSession();
		if (!RequestUtils.isAdmin(response, session, request)) {
			return null;
		}
		
        AdminParisIndicatorTableWidgetForm tableForm = (AdminParisIndicatorTableWidgetForm) form;
        tableForm.setPiTableWidgets(ParisIndicatorTableWidgetUtil.getAllSectorTableWidgets());
        return mapping.findForward("forward");
    }

    public ActionForward edit(ActionMapping mapping,
            ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
    	
    	HttpSession session = request.getSession();
		if (!RequestUtils.isAdmin(response, session, request)) {
			return null;
		}
		
        AdminParisIndicatorTableWidgetForm tableForm = (AdminParisIndicatorTableWidgetForm) form;
        AmpParisIndicatorTableWidget piTbWidget = ParisIndicatorTableWidgetUtil.getAmpParisIndicatorTableWidget(tableForm.getPiTableWidgetId());
        tableForm.setName(piTbWidget.getName());
        tableForm.setPlaces(WidgetUtil.getAllPlaces());
        List<AmpDaWidgetPlace> places = WidgetUtil.getWidgetPlaces(piTbWidget.getId());
        if (places == null) {
            tableForm.setSelPlaces(new Long[0]);
        } else {
            Long[] placeIDs = new Long[places.size()];
            int i = 0;
            for (AmpDaWidgetPlace place : places) {
                placeIDs[i++] = place.getId();
            }
            tableForm.setSelPlaces(placeIDs);
        }
        tableForm.setYears(new ArrayList());
        Long yearFrom = Long.parseLong(FeaturesUtil.getGlobalSettingValue(org.digijava.module.aim.helper.Constants.GlobalSettings.YEAR_RANGE_START));
        Long countYear = Long.parseLong(FeaturesUtil.getGlobalSettingValue(org.digijava.module.aim.helper.Constants.GlobalSettings.NUMBER_OF_YEARS_IN_RANGE));
        for (long i = yearFrom; i <= (yearFrom + countYear); i++) {
            tableForm.getYears().add(new Long(i));
        }
        tableForm.setPlaces(WidgetUtil.getAllPlaces());
        tableForm.setDonorGroupYearColumn(piTbWidget.getDonorGroupYear());
        tableForm.setParisIndicators(piTbWidget.getParisIndicators());
        return mapping.findForward("create");
    }

    public ActionForward delete(ActionMapping mapping,
            ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
    	
    	HttpSession session = request.getSession();
		if (!RequestUtils.isAdmin(response, session, request)) {
			return null;
		}
		
        AdminParisIndicatorTableWidgetForm tableForm = (AdminParisIndicatorTableWidgetForm) form;
        AmpParisIndicatorTableWidget piTbWidget = ParisIndicatorTableWidgetUtil.getAmpParisIndicatorTableWidget(tableForm.getPiTableWidgetId());
        List<AmpDaWidgetPlace> places = WidgetUtil.getWidgetPlaces(piTbWidget.getId());
        if (places != null && places.size() > 0) {
            WidgetUtil.updatePlacesWithWidget(places, null);
        }
        ParisIndicatorTableWidgetUtil.delete(piTbWidget);
        return viewAll(mapping, form, request, response);

    }

    public ActionForward save(ActionMapping mapping,
            ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
    	
    	HttpSession session = request.getSession();
		if (!RequestUtils.isAdmin(response, session, request)) {
			return null;
		}
		
        AdminParisIndicatorTableWidgetForm tableForm = (AdminParisIndicatorTableWidgetForm) form;
        boolean isNew = true;
        AmpParisIndicatorTableWidget piTbWidget = null;
        List<AmpDaWidgetPlace> oldPlaces = null;
        List<AmpDaWidgetPlace> newPlaces = null;
        if (tableForm.getPiTableWidgetId() == null || tableForm.getPiTableWidgetId() == 0) {
            piTbWidget = new AmpParisIndicatorTableWidget();
            piTbWidget.setParisIndicators(new ArrayList());

        } else {
            piTbWidget = ParisIndicatorTableWidgetUtil.getAmpParisIndicatorTableWidget(tableForm.getPiTableWidgetId());
            oldPlaces = WidgetUtil.getWidgetPlaces(tableForm.getPiTableWidgetId());
            isNew = false;
        }
        piTbWidget.setDonorGroupYear(tableForm.getDonorGroupYearColumn());
        piTbWidget.setName(tableForm.getName());
        piTbWidget.getParisIndicators().clear();
        Iterator<AmpParisIndicatorBaseTargetValues> indicatorIter=tableForm.getParisIndicators().iterator();
        int index=0;
        while(indicatorIter.hasNext()){
            AmpParisIndicatorBaseTargetValues parisIndicator=indicatorIter.next();
            parisIndicator.setIndex(index);
            piTbWidget.addParisIndicatorBaseTarget(parisIndicator);
            index++;
        }
        ParisIndicatorTableWidgetUtil.saveWidget(piTbWidget, isNew);
        if (tableForm.getSelPlaces() != null && tableForm.getSelPlaces().length > 0) {
            newPlaces = WidgetUtil.getPlacesWithIDs(tableForm.getSelPlaces());
            if (oldPlaces != null && newPlaces != null) {
                Collection<AmpDaWidgetPlace> deleted = AmpCollectionUtils.split(oldPlaces, newPlaces, new WidgetUtil.PlaceKeyWorker());
                WidgetUtil.updatePlacesWithWidget(oldPlaces, piTbWidget);
                WidgetUtil.updatePlacesWithWidget(deleted, null);
            } else {
                WidgetUtil.updatePlacesWithWidget(newPlaces, piTbWidget);
            }
        } else {
            WidgetUtil.clearPlacesForWidget(piTbWidget.getId());
        }

        return viewAll(mapping, form, request, response);
    }
}
