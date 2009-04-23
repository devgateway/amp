package org.digijava.module.widget.action;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import javax.servlet.http.HttpSession;
import org.apache.log4j.Logger;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.actions.DispatchAction;
import org.dgfoundation.amp.utils.AmpCollectionUtils;
import org.digijava.module.aim.dbentity.AmpSector;
import org.digijava.module.aim.helper.ActivitySector;
import org.digijava.module.aim.util.FeaturesUtil;
import org.digijava.module.aim.util.SectorUtil;
import org.digijava.module.widget.dbentity.AmpDaWidgetPlace;
import org.digijava.module.widget.dbentity.AmpSectorOrder;
import org.digijava.module.widget.dbentity.AmpSectorTableWidget;
import org.digijava.module.widget.dbentity.AmpSectorTableYear;
import org.digijava.module.widget.form.AdminSectorTableWidgetForm;
import org.digijava.module.widget.util.SectorTableWidgetUtil;
import org.digijava.module.widget.util.WidgetUtil;

public class AdminSectorTableWidgets extends DispatchAction {

    private static Logger logger = Logger.getLogger(AdminSectorTableWidgets.class);

    @Override
    public ActionForward unspecified(ActionMapping mapping,
            ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        return viewAll(mapping, form, request, response);
    }

    public ActionForward create(ActionMapping mapping,
            ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        AdminSectorTableWidgetForm tablesForm = (AdminSectorTableWidgetForm) form;
        tablesForm.setName(null);
        tablesForm.setSectors(null);
        tablesForm.setSectorTableId(null);
        tablesForm.setSelectedPercentYears(null);
        tablesForm.setSelectedTotalYears(null);
        tablesForm.setSelectedSectors(null);
        tablesForm.setYears(new ArrayList());
        Long yearFrom = Long.parseLong(FeaturesUtil.getGlobalSettingValue(org.digijava.module.aim.helper.Constants.GlobalSettings.YEAR_RANGE_START));
        Long countYear = Long.parseLong(FeaturesUtil.getGlobalSettingValue(org.digijava.module.aim.helper.Constants.GlobalSettings.NUMBER_OF_YEARS_IN_RANGE));
        for (long i = yearFrom; i <= (yearFrom + countYear); i++) {
            tablesForm.getYears().add(new Long(i));
        }
        tablesForm.setSelPlaces(null);
        tablesForm.setPlaces(WidgetUtil.getAllPlaces());
        return mapping.findForward("create");

    }

    public ActionForward viewAll(ActionMapping mapping,
            ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        AdminSectorTableWidgetForm tablesForm = (AdminSectorTableWidgetForm) form;
        tablesForm.setSectorTables(SectorTableWidgetUtil.getAllSectorTableWidgets());
        return mapping.findForward("forward");
    }

    public ActionForward edit(ActionMapping mapping,
            ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        AdminSectorTableWidgetForm tablesForm = (AdminSectorTableWidgetForm) form;
        AmpSectorTableWidget secTbWidget = SectorTableWidgetUtil.getAmpSectorTableWidget(tablesForm.getSectorTableId());
        tablesForm.setName(secTbWidget.getName());

        Set<AmpSectorTableYear> totalYearsCol = secTbWidget.getTotalYears();
        Set<AmpSectorTableYear> totalPercentYearsCol = secTbWidget.getPercentYears();
        ArrayList<String> selectedYears = new ArrayList<String>();
        if (totalYearsCol != null) {
            Iterator<AmpSectorTableYear> totalYearsColIter = totalYearsCol.iterator();
            while (totalYearsColIter.hasNext()) {
                selectedYears.add(totalYearsColIter.next().getYear().toString());
            }
        }
        String[] years = new String[selectedYears.size()];
        tablesForm.setSelectedTotalYears(selectedYears.toArray(years));

        if (totalPercentYearsCol != null) {
            selectedYears.clear();
            Iterator<AmpSectorTableYear> percentYearsColIter = totalPercentYearsCol.iterator();
            while (percentYearsColIter.hasNext()) {
                selectedYears.add(percentYearsColIter.next().getYear().toString());
            }
        }
        String[] percentYears = new String[selectedYears.size()];
        tablesForm.setSelectedPercentYears(selectedYears.toArray(percentYears));

        tablesForm.setSectors(new ArrayList<AmpSectorOrder>(secTbWidget.getSectorsColumns()));

        tablesForm.setPlaces(WidgetUtil.getAllPlaces());
        List<AmpDaWidgetPlace> places = WidgetUtil.getWidgetPlaces(secTbWidget.getId());
        if (places == null) {
            tablesForm.setSelPlaces(new Long[0]);
        } else {
            Long[] placeIDs = new Long[places.size()];
            int i = 0;
            for (AmpDaWidgetPlace place : places) {
                placeIDs[i++] = place.getId();
            }
            tablesForm.setSelPlaces(placeIDs);
        }
        tablesForm.setYears(new ArrayList());
        Long yearFrom = Long.parseLong(FeaturesUtil.getGlobalSettingValue(org.digijava.module.aim.helper.Constants.GlobalSettings.YEAR_RANGE_START));
        Long countYear = Long.parseLong(FeaturesUtil.getGlobalSettingValue(org.digijava.module.aim.helper.Constants.GlobalSettings.NUMBER_OF_YEARS_IN_RANGE));
        for (long i = yearFrom; i <= (yearFrom + countYear); i++) {
            tablesForm.getYears().add(new Long(i));
        }
        tablesForm.setPlaces(WidgetUtil.getAllPlaces());
        return mapping.findForward("create");
    }

    public ActionForward delete(ActionMapping mapping,
            ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception {

        AdminSectorTableWidgetForm tablesForm = (AdminSectorTableWidgetForm) form;
        AmpSectorTableWidget secTbWidget = SectorTableWidgetUtil.getAmpSectorTableWidget(tablesForm.getSectorTableId());
        List<AmpDaWidgetPlace> places = WidgetUtil.getWidgetPlaces(secTbWidget.getId());
        if (places != null && places.size() > 0) {
            WidgetUtil.updatePlacesWithWidget(places, null);
        }
        SectorTableWidgetUtil.delete(secTbWidget);
        return viewAll(mapping, form, request, response);

    }

    public ActionForward save(ActionMapping mapping,
            ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        boolean isNew=true;
        AdminSectorTableWidgetForm tablesForm = (AdminSectorTableWidgetForm) form;
        AmpSectorTableWidget secTableWidget = null;
        List<AmpDaWidgetPlace> oldPlaces = null;
        List<AmpDaWidgetPlace> newPlaces = null;
        List<AmpSectorOrder> sectorOrders = tablesForm.getSectors();
        String[] selTotalYears = tablesForm.getSelectedTotalYears();
        String[] selPercentYears = tablesForm.getSelectedPercentYears();

        if (tablesForm.getSectorTableId() == null || tablesForm.getSectorTableId() == 0) {
            secTableWidget = new AmpSectorTableWidget();

        } else {
            secTableWidget = SectorTableWidgetUtil.getAmpSectorTableWidget(tablesForm.getSectorTableId());
            isNew=false;
        }
        if (secTableWidget.getPercentYears() == null) {
            secTableWidget.setPercentYears(new HashSet<AmpSectorTableYear>());
        }
        if (secTableWidget.getTotalYears() == null) {
            secTableWidget.setTotalYears(new HashSet<AmpSectorTableYear>());
        }
        if (secTableWidget.getSectorsColumns()== null) {
            secTableWidget.setSectorsColumns(new HashSet<AmpSectorOrder>());
        }

       
        List<AmpSectorOrder> secOrderRetain = new ArrayList<AmpSectorOrder>();
        Set<AmpSectorOrder> sectors = secTableWidget.getSectorsColumns();
        Iterator<AmpSectorOrder> sectorOrdeIter = sectorOrders.iterator();
       
        int order = 0;
        while (sectorOrdeIter.hasNext()) {
            boolean sectorExist = false;
            AmpSectorOrder sectorOrder = sectorOrdeIter.next();
            Iterator<AmpSectorOrder> oldSecOrderIter = sectors.iterator();
            while (oldSecOrderIter.hasNext()) {
                AmpSectorOrder oldSectorOrder =oldSecOrderIter.next();
                if (sectorOrder.getSector().getAmpSectorId().equals(oldSectorOrder.getSector().getAmpSectorId())) {
                    sectorExist = true;
                    secOrderRetain.add(sectorOrder);
                    break;
                }
            }
            if (!sectorExist) {
                sectorOrder.setWidget(secTableWidget);
                secOrderRetain.add(sectorOrder);

            }
        }
        Iterator<AmpSectorOrder> secOrderRetainIter = secOrderRetain.iterator();
        while (secOrderRetainIter.hasNext()) {
            AmpSectorOrder sectOrder = secOrderRetainIter.next();
            sectOrder.setOrder(order);
            order++;
        }
        secTableWidget.getSectorsColumns().clear();
        secTableWidget.getSectorsColumns().addAll(secOrderRetain);

        // Total column
        Set<AmpSectorTableYear> oldTotYears = secTableWidget.getTotalYears();
        List<AmpSectorTableYear> yearsToRetain = new ArrayList<AmpSectorTableYear>();

        if (selTotalYears != null) {
            Arrays.sort(selTotalYears);
            for (int i = 0; i < selTotalYears.length; i++) {
                Long year = Long.parseLong(selTotalYears[i]);
                boolean yearExist = false;
                if (oldTotYears != null) {

                    Iterator<AmpSectorTableYear> yearIter = oldTotYears.iterator();
                    while (yearIter.hasNext()) {
                        AmpSectorTableYear yearCol = yearIter.next();
                        if (yearCol.getYear().equals(year)) {
                            yearExist = true;
                            yearsToRetain.add(yearCol);
                            break;
                        }

                    }
                }
                if (!yearExist) {
                    AmpSectorTableYear totColumn = new AmpSectorTableYear();
                    totColumn.setType(AmpSectorTableYear.TOTAL_TYPE_YEAR);
                    totColumn.setYear(year);
                    secTableWidget.addYear(totColumn);
                    yearsToRetain.add(totColumn);
                }

            }

        }
        secTableWidget.getTotalYears().retainAll(yearsToRetain);

         // Percent column
        Set<AmpSectorTableYear> oldPercYears = secTableWidget.getPercentYears();
        List<AmpSectorTableYear> yearsPercRetain = new ArrayList<AmpSectorTableYear>();
        if (selPercentYears != null) {
            Arrays.sort(selPercentYears);

            for (int i = 0; i < selPercentYears.length; i++) {
                Long year = Long.parseLong(selPercentYears[i]);
                boolean yearExist = false;
                if (oldPercYears != null) {

                    Iterator<AmpSectorTableYear> yearIter = oldPercYears.iterator();
                    while (yearIter.hasNext()) {
                        AmpSectorTableYear yearCol = yearIter.next();
                        if (yearCol.getYear().equals(year)) {
                            yearExist = true;
                            yearsPercRetain.add(yearCol);
                            break;
                        }


                    }

                }
                if (!yearExist) {
                    AmpSectorTableYear percCol = new AmpSectorTableYear();
                    percCol.setType(AmpSectorTableYear.PERCENT_TYPE_YEAR);
                    percCol.setYear(year);
                    secTableWidget.addYear(percCol);
                    yearsPercRetain.add(percCol);
                }

            }

        }
        secTableWidget.getPercentYears().retainAll(yearsPercRetain);



        secTableWidget.setName(tablesForm.getName());
        SectorTableWidgetUtil.saveWidget(secTableWidget,isNew);
        if (tablesForm.getSelPlaces() != null && tablesForm.getSelPlaces().length > 0) {
            newPlaces = WidgetUtil.getPlacesWithIDs(tablesForm.getSelPlaces());
            if (oldPlaces != null && newPlaces != null) {
                Collection<AmpDaWidgetPlace> deleted = AmpCollectionUtils.split(oldPlaces, newPlaces, new WidgetUtil.PlaceKeyWorker());
                WidgetUtil.updatePlacesWithWidget(oldPlaces, secTableWidget);
                WidgetUtil.updatePlacesWithWidget(deleted, null);
            } else {
                WidgetUtil.updatePlacesWithWidget(newPlaces, secTableWidget);
            }
        } else {
            WidgetUtil.clearPlacesForWidget(secTableWidget.getId());
        }
        return viewAll(mapping, form, request, response);
    }

    public ActionForward selectSector(ActionMapping mapping,
            ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception {

        AdminSectorTableWidgetForm tablesForm = (AdminSectorTableWidgetForm) form;
        HttpSession session = request.getSession();
        if (tablesForm.getSectors() == null) {
            tablesForm.setSectors(new ArrayList());
        }
        Object sectorObject = session.getAttribute(
                "sectorSelected");
        /* Don't be surprised , yes, you see ActivitySector class here :)
        Currently sector selector component is written in
        Such way that it uses ActivitySector class*/
        int size = tablesForm.getSectors().size();
        List<AmpSectorOrder> sectors = new ArrayList<AmpSectorOrder>();
        if (sectorObject instanceof ActivitySector) {
            ActivitySector selectedSector = (ActivitySector) sectorObject;
            Long sectorId = getSectorId(selectedSector);
            AmpSectorOrder sectorOrderHelper = new AmpSectorOrder(SectorUtil.getAmpSector(sectorId), ++size);
            sectors.add(sectorOrderHelper);
        } else {
            ArrayList selectedSectors = (ArrayList) sectorObject;
            Iterator iterSelectedSector = selectedSectors.iterator();
            while (iterSelectedSector.hasNext()) {
                ActivitySector selectedSector = (ActivitySector) iterSelectedSector.next();
                Long sectorId = getSectorId(selectedSector);
                AmpSectorOrder sectorOrderHelper = new AmpSectorOrder(SectorUtil.getAmpSector(sectorId), ++size);
                sectors.add(sectorOrderHelper);

            }
        }
        tablesForm.getSectors().addAll(sectors);
        session.removeAttribute("sectorSelected");
        return mapping.findForward("create");
    }

    public ActionForward remSectors(ActionMapping mapping,
            ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        AdminSectorTableWidgetForm tablesForm = (AdminSectorTableWidgetForm) form;
        String[] selectedSectors = tablesForm.getSelectedSectors();
        if (selectedSectors != null) {
            List<AmpSectorOrder> sectors = tablesForm.getSectors();
            ArrayList<AmpSectorOrder> removedSectors = new ArrayList<AmpSectorOrder>();
            for (int i = 0; i < selectedSectors.length; i++) {
                AmpSector sector = SectorUtil.getAmpSector(Long.parseLong(selectedSectors[i]));
                Iterator<AmpSectorOrder> iterSectors = sectors.iterator();
                while (iterSectors.hasNext()) {
                    AmpSectorOrder sec = iterSectors.next();
                    if (sec.getSector().getAmpSectorId().equals(sector.getAmpSectorId())) {
                        removedSectors.add(sec);
                    }
                }

            }
            tablesForm.getSectors().removeAll(removedSectors);
        }
        tablesForm.setSelectedSectors(null);
        return mapping.findForward("create");
    }

    public ActionForward reorderUp(ActionMapping mapping,
            ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        AdminSectorTableWidgetForm tablesForm = (AdminSectorTableWidgetForm) form;
        List<AmpSectorOrder> sectors = tablesForm.getSectors();
        Iterator<AmpSectorOrder> iterSectors = sectors.iterator();
        while (iterSectors.hasNext()) {
            AmpSectorOrder sec = iterSectors.next();
            if (sec.getSector().getAmpSectorId().equals(tablesForm.getSectorToReorderId())) {
                int index = sectors.indexOf(sec);
                sectors.remove(sec);
                sectors.add(index - 1, sec);
                tablesForm.setSectors(sectors);
                break;
            }
        }
        return mapping.findForward("create");
    }

    public ActionForward reorderDown(ActionMapping mapping,
            ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        AdminSectorTableWidgetForm tablesForm = (AdminSectorTableWidgetForm) form;
        List<AmpSectorOrder> sectors = tablesForm.getSectors();
        Iterator<AmpSectorOrder> iterSectors = sectors.iterator();
        while (iterSectors.hasNext()) {
            AmpSectorOrder sec = iterSectors.next();
            if (sec.getSector().getAmpSectorId().equals(tablesForm.getSectorToReorderId())) {
                int index = sectors.indexOf(sec);
                sectors.remove(sec);
                sectors.add(index + 1, sec);
                tablesForm.setSectors(sectors);
                break;
            }
        }
        return mapping.findForward("create");
    }

    private static Long getSectorId(ActivitySector selectedSector) {
        Long sectorId = null;
        if (selectedSector.getSubsectorLevel2Id() != -1) {
            sectorId = selectedSector.getSubsectorLevel2Id();
        } else {
            if (selectedSector.getSubsectorLevel1Id() != -1) {
                sectorId = selectedSector.getSubsectorLevel1Id();
            } else {
                sectorId = selectedSector.getSectorId();
            }
        }
        return sectorId;
    }
}
