package org.digijava.module.widget.action;

import java.util.Collection;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.actions.DispatchAction;
import org.dgfoundation.amp.utils.AmpCollectionUtils;
import org.digijava.kernel.exception.DgException;
import org.digijava.module.aim.dbentity.IndicatorSector;
import org.digijava.module.aim.util.IndicatorUtil;
import org.digijava.module.widget.dbentity.AmpDaWidgetPlace;
import org.digijava.module.widget.dbentity.AmpWidgetIndicatorChart;
import org.digijava.module.widget.form.AdminIndicatorChartsForm;
import org.digijava.module.widget.util.ChartWidgetUtil;
import org.digijava.module.widget.util.WidgetUtil;

/**
 * Administer indicator chart widgets.
 * TODO try to create some generic action for all widget administration, if this is possible
 * @author Irakli Kobiashvili
 *
 */
public class AdminIndicatorChartWidgets extends DispatchAction {

	@Override
	protected ActionForward unspecified(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		return list(mapping, form, request, response);
	}

	public ActionForward list(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		AdminIndicatorChartsForm cForm = (AdminIndicatorChartsForm)form;
		List<AmpWidgetIndicatorChart> widgets = ChartWidgetUtil.getAllIndicatorChartWidgets();
		cForm.setWidgets(widgets);
		return mapping.findForward("forward");
	}
	
	public ActionForward create(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) throws Exception {
		AdminIndicatorChartsForm cForm = (AdminIndicatorChartsForm)form;
		cForm.setWidgetName("");
		cForm.setWidgetId(null);
		cForm.setSelIndicators(new Long(-1));
		cForm.setSelPlaces(new Long[0]);
		cForm.setIndicators(getIndicators());
		cForm.setPlaces(getPlaces());
		
	
		return mapping.findForward("showEdit");
	}

	public ActionForward edit(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) throws Exception {
		AdminIndicatorChartsForm cForm = (AdminIndicatorChartsForm)form;
		if (cForm.getWidgetId()==null){
			return mapping.findForward("showList");
		}
		AmpWidgetIndicatorChart widget = ChartWidgetUtil.getIndicatorChartWidget(cForm.getWidgetId());
		
		cForm.setWidgetName(widget.getName());
		cForm.setIndicators(getIndicators());
		cForm.setPlaces(getPlaces());
		
		if (widget.getIndicator()==null){
			cForm.setSelIndicators(new Long(-1));
		}else{
			cForm.setSelIndicators(widget.getIndicator().getId());
		}
		
		List<AmpDaWidgetPlace> places = WidgetUtil.getWidgetPlaces(widget.getId());
		if (places==null){
			cForm.setSelPlaces(new Long[0]);
		}else{
			Long[] placeIDs = new Long[places.size()];
			int i=0;
			for (AmpDaWidgetPlace place : places) {
				placeIDs[i++]=place.getId();
			}
			cForm.setSelPlaces(placeIDs);
		}
		
		
		return mapping.findForward("showEdit");
	}

	public ActionForward save(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) throws Exception {
		AdminIndicatorChartsForm cForm = (AdminIndicatorChartsForm)form;
		AmpWidgetIndicatorChart widget = null;
		List<AmpDaWidgetPlace> oldPlaces = null;
		List<AmpDaWidgetPlace> newPlaces = null;
		
		if (cForm.getWidgetId()==null || cForm.getWidgetId().longValue()==0){
			widget = new AmpWidgetIndicatorChart();
		}else{
			widget = ChartWidgetUtil.getIndicatorChartWidget(cForm.getWidgetId());
			oldPlaces = WidgetUtil.getWidgetPlaces(widget.getId());
		}
		widget.setName(cForm.getWidgetName());
		widget.setCode(cForm.getWidgetName());
		
		if (cForm.getSelIndicators()!=null){
			IndicatorSector indicator = IndicatorUtil.getConnectionToSector(cForm.getSelIndicators());
			widget.setIndicator(indicator);
		}
		widget = ChartWidgetUtil.saveOrUpdate(widget);
		if (cForm.getSelPlaces()!=null && cForm.getSelPlaces().length>0){
			newPlaces = WidgetUtil.getPlacesWithIDs(cForm.getSelPlaces());
			if (oldPlaces!=null && newPlaces!=null){
				Collection<AmpDaWidgetPlace> deleted = AmpCollectionUtils.split(oldPlaces, newPlaces, new WidgetUtil.PlaceKeyWorker());
				WidgetUtil.updatePlacesWithWidget(oldPlaces, widget);
				WidgetUtil.updatePlacesWithWidget(deleted, null);
			}else{
				WidgetUtil.updatePlacesWithWidget(newPlaces, widget);
			}
		}else {
			WidgetUtil.clearPlacesForWidget(widget.getId());
		}
		return mapping.findForward("showList");
	}

	public ActionForward delete(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) throws Exception {
		AdminIndicatorChartsForm cForm = (AdminIndicatorChartsForm)form;
		if (cForm.getWidgetId()==null){
			return mapping.findForward("showList");
		}
		AmpWidgetIndicatorChart widget = ChartWidgetUtil.getIndicatorChartWidget(cForm.getWidgetId());
		List<AmpDaWidgetPlace> places = WidgetUtil.getWidgetPlaces(widget.getId());
		if (places!=null && places.size()>0){
			WidgetUtil.updatePlacesWithWidget(places, null);
		}
		ChartWidgetUtil.delete(widget);
		return mapping.findForward("showList");
	}

	private List<IndicatorSector> getIndicators() throws DgException{
		return IndicatorUtil.getAllConnections(IndicatorSector.class);
	}
	private List<AmpDaWidgetPlace> getPlaces() throws DgException{
		return WidgetUtil.getAllPlaces();
	}
}
