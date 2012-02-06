package org.digijava.module.budgetexport.action;


import org.apache.commons.io.IOUtils;
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

    public ActionForward unspecified(ActionMapping mapping, ActionForm form,
                HttpServletRequest request, HttpServletResponse response) throws java.lang.Exception {
        return view(mapping, form, request, response);
    }

    public ActionForward view(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws java.lang.Exception {
            BEMapActionsForm beMapActionsForm = (BEMapActionsForm) form;

        if (!beMapActionsForm.isNoReload()) {
            AmpBudgetExportMapRule rule = DbUtil.getMapRuleById(beMapActionsForm.getRuleId());
            List<AmpEntityMappedItem> ampEntityMappedItems = BudgetExportUtil.getAmpEntityMappedItems(rule);
            beMapActionsForm.setAmpEntityMappedItems(ampEntityMappedItems);

            beMapActionsForm.setRule(rule);
            MappingEntityAdapter adapter = MappingEntityAdapterUtil.getEntityAdapter(rule.getAmpColumn().getExtractorView());
            List<HierarchyListable> ampEntityList = adapter.getAllObjects();
            beMapActionsForm.setAmpEntities(ampEntityList);

            request.getSession().setAttribute(AMP_ENTITY_LIST_SESSION_ATTR, ampEntityList);

            List dbItems = new ArrayList();
            for (AmpBudgetExportMapItem item: rule.getItems()) {
                dbItems.add(item);
            }
            request.getSession().setAttribute(TMP_MAP_SESSION_ATTR, dbItems);
            beMapActionsForm.setMapItems(dbItems);

        }
        return mapping.findForward("forward");
    }

    public ActionForward save(ActionMapping mapping, ActionForm form,
                HttpServletRequest request, HttpServletResponse response) throws java.lang.Exception {
        BEMapActionsForm beMapActionsForm = (BEMapActionsForm) form;
        AmpBudgetExportMapRule rule = DbUtil.getMapRuleById(beMapActionsForm.getRuleId());
        List<AmpBudgetExportMapItem> items = (List<AmpBudgetExportMapItem>)request.getSession().getAttribute(TMP_MAP_SESSION_ATTR);

        //Add or update
        for (AmpBudgetExportMapItem item : items) {
            if (item.getId() != null && item.getId().longValue() > 0l) {
                for (AmpBudgetExportMapItem dbItem : rule.getItems()) {
                    if (dbItem.getId().equals(item.getId())) {
                        dbItem.setImportedCode(item.getImportedCode());
                        dbItem.setAmpObjectID(item.getAmpObjectID());
                        dbItem.setImportedLabel(item.getImportedLabel());
                        dbItem.setAmpLabel(item.getAmpLabel());
                        dbItem.setMatchLevel(item.getMatchLevel());
                        break;
                    }
                }
            } else {
                rule.getItems().add(item);
            }
        }



        DbUtil.saveOrUpdateMapRule(rule);

        request.getSession().removeAttribute(TMP_MAP_SESSION_ATTR);

        StringBuilder fwdBuilder = new StringBuilder("/");
        fwdBuilder.append(mapping.findForward("projectRules").getPath());
        fwdBuilder.append("?id=");
        fwdBuilder.append(beMapActionsForm.getRuleId());
        return new ActionForward(fwdBuilder.toString());
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

        beMapActionsForm.setNoReload(true);

        return view(mapping, beMapActionsForm, request, response);
    }

    public ActionForward autocomplite (ActionMapping mapping, ActionForm form,
                        HttpServletRequest request, HttpServletResponse response) throws java.lang.Exception {
        response.setContentType("text/xml");
        String searchStr = request.getParameter("searchStr");
        List<HierarchyListable> ampEntityList = (List<HierarchyListable>)request.getSession().getAttribute(AMP_ENTITY_LIST_SESSION_ATTR);

        XMLDocument responceXml = new XMLDocument();
        XML rootNode = new XML("result");
        responceXml.addElement(rootNode);
        List<HierarchyListable> searchResults = BudgetExportUtil.searchAmpEntity(ampEntityList, searchStr);

        for (HierarchyListable obj : searchResults) {
            XML resultItem = new XML("item");
            resultItem.addAttribute("id", obj.getUniqueId());
            StringElement body = new StringElement(obj.getLabel());
            resultItem.addElement(body);
            rootNode.addElement(resultItem);

        }
        responceXml.output(response.getOutputStream());
        return null;
    }
    
    public ActionForward updateMappingItem (ActionMapping mapping, ActionForm form,
                        HttpServletRequest request, HttpServletResponse response) throws java.lang.Exception {
        String importedCode = request.getParameter("importedCode");
        String ampObjId = request.getParameter("ampObjId");
        List<HierarchyListable> ampEntityList = (List<HierarchyListable>)request.getSession().getAttribute(AMP_ENTITY_LIST_SESSION_ATTR);
        List<AmpBudgetExportMapItem> items = (List<AmpBudgetExportMapItem>)request.getSession().getAttribute(TMP_MAP_SESSION_ATTR);
        
        for (AmpBudgetExportMapItem item : items) {
            if (item.getImportedCode().trim().equals(importedCode.trim())) {
                for (HierarchyListable ampEntity : ampEntityList) {
                    if (ampEntity.getUniqueId().trim().equals(ampObjId.trim())) {
                        item.setAmpObjectID(Long.parseLong(ampEntity.getUniqueId()));
                        item.setAmpLabel(ampEntity.getLabel());
                        item.setMatchLevel(AmpBudgetExportMapItem.MAP_MATCH_LEVEL_MANUAL);
                        break;
                    }
                }
                break;
            }
        }
        
        return null;
    }
}
