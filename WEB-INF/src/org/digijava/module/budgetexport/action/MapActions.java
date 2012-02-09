package org.digijava.module.budgetexport.action;


import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringEscapeUtils;
import org.apache.ecs.StringElement;
import org.apache.ecs.xml.XML;
import org.apache.ecs.xml.XMLDocument;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.actions.DispatchAction;

import org.apache.struts.upload.FormFile;
import org.dgfoundation.amp.importers.CSVImporter;
import org.digijava.module.aim.util.HierarchyListable;
import org.digijava.module.budgetexport.adapter.MappingEntityAdapter;
import org.digijava.module.budgetexport.adapter.MappingEntityAdapterUtil;
import org.digijava.module.budgetexport.dbentity.AmpBudgetExportCSVItem;
import org.digijava.module.budgetexport.dbentity.AmpBudgetExportMapItem;
import org.digijava.module.budgetexport.dbentity.AmpBudgetExportMapRule;
import org.digijava.module.budgetexport.form.BEMapActionsForm;
import org.digijava.module.budgetexport.util.AmpEntityMappedItem;
import org.digijava.module.budgetexport.util.BudgetExportUtil;
import org.digijava.module.budgetexport.util.DbUtil;


import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Created by IntelliJ IDEA.
 * User: flyer
 * Date: 2/4/12
 * Time: 3:01 PM
 * To change this template use File | Settings | File Templates.
 */
public class MapActions extends DispatchAction {
    private static final String TMP_MAP_SESSION_ATTR = "TMP_MAP_SESSION_ATTR";
    private static final String AMP_ENTITY_LIST_SESSION_ATTR = "AMP_ENTITY_LIST_SESSION_ATTR";
    private static final String AMP_MAPPED_ENTITY_LIST = "AMP_MAPPED_ENTITY_LIST";
    private static final String CURRENT_MAPPING_RULE = "CURRENT_MAPPING_RULE";

    public ActionForward unspecified(ActionMapping mapping, ActionForm form,
                HttpServletRequest request, HttpServletResponse response) throws java.lang.Exception {
        return view(mapping, form, request, response);
    }

    public ActionForward view(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws java.lang.Exception {
            BEMapActionsForm beMapActionsForm = (BEMapActionsForm) form;

        if (!beMapActionsForm.isNoReload()) {
            AmpBudgetExportMapRule rule = DbUtil.getMapRuleById(beMapActionsForm.getRuleId(), true);
            request.getSession().setAttribute(CURRENT_MAPPING_RULE, rule);
            beMapActionsForm.setRule(rule);

            List<AmpEntityMappedItem> ampEntityMappedItems = BudgetExportUtil.getAmpEntityMappedItems(rule);
            request.getSession().setAttribute(AMP_MAPPED_ENTITY_LIST, ampEntityMappedItems);
            //beMapActionsForm.setAmpEntityMappedItems(ampEntityMappedItems);




            MappingEntityAdapter adapter = MappingEntityAdapterUtil.getEntityAdapter(rule.getAmpColumn().getExtractorView());
            List<HierarchyListable> ampEntityList = adapter.getAllObjects();
            request.getSession().setAttribute(AMP_ENTITY_LIST_SESSION_ATTR, ampEntityList);

            beMapActionsForm.setAmpEntities(ampEntityList);



            List<AmpBudgetExportMapItem> dbItems = new ArrayList();
            for (AmpBudgetExportMapItem item: rule.getItems()) {
                dbItems.add(item);
            }
            request.getSession().setAttribute(TMP_MAP_SESSION_ATTR, dbItems);
            //beMapActionsForm.setMapItems(dbItems);

        }

        beMapActionsForm.setRule((AmpBudgetExportMapRule)request.getSession().getAttribute(CURRENT_MAPPING_RULE));
        beMapActionsForm.setMapItems((List<AmpBudgetExportMapItem>)request.getSession().getAttribute(TMP_MAP_SESSION_ATTR));
        beMapActionsForm.setAmpEntityMappedItems((List<AmpEntityMappedItem>)request.getSession().getAttribute(AMP_MAPPED_ENTITY_LIST));

        return mapping.findForward("forward");
    }

    public ActionForward save(ActionMapping mapping, ActionForm form,
                HttpServletRequest request, HttpServletResponse response) throws java.lang.Exception {
        BEMapActionsForm beMapActionsForm = (BEMapActionsForm) form;

        AmpBudgetExportMapRule rule = DbUtil.getMapRuleById(beMapActionsForm.getRuleId());
        List<AmpEntityMappedItem> ampEntityMappedItems = (List<AmpEntityMappedItem>) request.getSession().getAttribute(AMP_MAPPED_ENTITY_LIST);

        //Add or update
        for (AmpEntityMappedItem item : ampEntityMappedItems) {
            if (item.getMapItem() != null) {
                AmpBudgetExportMapItem mapItem = item.getMapItem();
                if (mapItem.getId() != null) { //Update existing DB record
                    for (AmpBudgetExportMapItem mapDbItem : rule.getItems()) {
                        if (mapItem.getId().equals(mapDbItem.getId())) {
                            mapDbItem.setAmpLabel(mapItem.getAmpLabel());
                            mapDbItem.setAmpObjectID(mapItem.getAmpObjectID());
                            mapDbItem.setImportedCode(mapItem.getImportedCode());
                            mapDbItem.setImportedLabel(mapItem.getImportedLabel());
                            mapDbItem.setMatchLevel(mapItem.getMatchLevel());
                        }
                    }
                } else { //Add a new one
                    rule.getItems().add(mapItem);
                }
            }
        }



        DbUtil.saveOrUpdateMapRule(rule);

        request.getSession().removeAttribute(TMP_MAP_SESSION_ATTR);
        request.getSession().removeAttribute(AMP_ENTITY_LIST_SESSION_ATTR);
        request.getSession().removeAttribute(AMP_MAPPED_ENTITY_LIST);
        request.getSession().removeAttribute(CURRENT_MAPPING_RULE);

        StringBuilder fwdBuilder = new StringBuilder("/");
        fwdBuilder.append(mapping.findForward("projectRules").getPath());
        fwdBuilder.append("?id=");
        fwdBuilder.append(beMapActionsForm.getProjectId());
        ActionForward fwd = new ActionForward(fwdBuilder.toString());
        fwd.setRedirect(true);
        return fwd;
    }

    public ActionForward upload(ActionMapping mapping, ActionForm form,
                    HttpServletRequest request, HttpServletResponse response) throws java.lang.Exception {
        BEMapActionsForm beMapActionsForm = (BEMapActionsForm) form;

        AmpBudgetExportMapRule rule = DbUtil.getMapRuleById(beMapActionsForm.getRuleId());

        FormFile file = beMapActionsForm.getUpload();


        String csvContent = new String(file.getFileData(), "UTF-8");
        
        Map<String, String> csvMap = BudgetExportUtil.parseCSV(csvContent, rule.isHeader());

        
        Set <Map.Entry<String, String>> mapEntrySet = csvMap.entrySet();
        Iterator<Map.Entry<String, String>> mapEntrySetIt = mapEntrySet.iterator();
        rule.getCsvItems().clear();
        while (mapEntrySetIt.hasNext()) {
            Map.Entry<String, String> entry = mapEntrySetIt.next();
            AmpBudgetExportCSVItem csvItem = new AmpBudgetExportCSVItem(entry.getKey(), entry.getValue(), rule);
            rule.getCsvItems().add(csvItem);
        }

        DbUtil.saveOrUpdateMapRule(rule);


        List<HierarchyListable> ampEntityList = (List<HierarchyListable>)request.getSession().getAttribute(AMP_ENTITY_LIST_SESSION_ATTR);


        List<AmpBudgetExportMapItem> items = BudgetExportUtil.matchAndGetMapItems(csvMap, ampEntityList, rule);

        beMapActionsForm.setMapItems(items);
        request.getSession().setAttribute(TMP_MAP_SESSION_ATTR, items);

        //beMapActionsForm.setNoReload(true);

        return view(mapping, beMapActionsForm, request, response);
    }

    public ActionForward autocomplete(ActionMapping mapping, ActionForm form,
                                      HttpServletRequest request, HttpServletResponse response) throws java.lang.Exception {
        response.setContentType("text/xml");
        String searchStr = request.getParameter("searchStr");
        String searchIn = request.getParameter("searchIn");
        boolean searchCodes = searchIn != null && searchIn.equalsIgnoreCase("code");

        AmpBudgetExportMapRule rule = (AmpBudgetExportMapRule)request.getSession().getAttribute(CURRENT_MAPPING_RULE);


        XMLDocument responceXml = new XMLDocument();
        XML rootNode = new XML("result");
        responceXml.addElement(rootNode);
        List<AmpBudgetExportCSVItem> searchResults = BudgetExportUtil.searchCsvItems(rule.getCsvItems(), searchStr, searchCodes);

        for (AmpBudgetExportCSVItem obj : searchResults) {
            XML resultItem = new XML("item");
            resultItem.addAttribute("code", obj.getCode());
            StringElement body = new StringElement(StringEscapeUtils.escapeHtml(obj.getLabel()));
            resultItem.addElement(body);
            rootNode.addElement(resultItem);

        }
        responceXml.output(response.getOutputStream());
        return null;
    }
    
    public ActionForward updateMappingItem (ActionMapping mapping, ActionForm form,
                        HttpServletRequest request, HttpServletResponse response) throws java.lang.Exception {
        String importedCode = request.getParameter("importedCode");
        String importedLabel = request.getParameter("importedLabel");
        String ampObjId = request.getParameter("ampObjId");

        List<AmpEntityMappedItem> ampEntityMappedItems = (List<AmpEntityMappedItem>) request.getSession().getAttribute(AMP_MAPPED_ENTITY_LIST);
        AmpBudgetExportMapRule rule = (AmpBudgetExportMapRule) request.getSession().getAttribute(CURRENT_MAPPING_RULE);
        
        for (AmpEntityMappedItem item : ampEntityMappedItems) {
            if (item.getAmpEntity().getUniqueId().trim().equals(ampObjId)) {
                AmpBudgetExportMapItem mapItem = null;
                if (item.getMapItem() != null) {
                    mapItem = item.getMapItem();
                } else {
                    mapItem = new AmpBudgetExportMapItem();
                    mapItem.setAmpObjectID(Long.parseLong(item.getAmpEntity().getUniqueId()));
                    mapItem.setAmpLabel(item.getAmpEntity().getLabel());
                    mapItem.setRule(rule);
                }
                mapItem.setImportedCode(importedCode);
                mapItem.setImportedLabel(importedLabel);
                mapItem.setMatchLevel(AmpBudgetExportMapItem.MAP_MATCH_LEVEL_MANUAL);
                item.setMapItem(mapItem);
                break;
            }
        }
        
        return null;
    }

    public ActionForward automatch (ActionMapping mapping, ActionForm form,
                            HttpServletRequest request, HttpServletResponse response) throws java.lang.Exception {
        BEMapActionsForm beMapActionsForm = (BEMapActionsForm) form;
        beMapActionsForm.setNoReload(true);

        AmpBudgetExportMapRule rule = (AmpBudgetExportMapRule) request.getSession().getAttribute(CURRENT_MAPPING_RULE);
        List<AmpEntityMappedItem> ampEntityMappedItems = (List<AmpEntityMappedItem>) request.getSession().getAttribute(AMP_MAPPED_ENTITY_LIST);

        BudgetExportUtil.matchMapItems (ampEntityMappedItems, rule);

        return view(mapping, beMapActionsForm, request, response);
    }
}
