package org.digijava.module.aim.action;

import org.apache.log4j.Logger;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.dgfoundation.amp.utils.MultiAction;
import org.dgfoundation.amp.visibility.AmpTreeVisibility;
import org.dgfoundation.amp.visibility.data.DataVisibility;
import org.dgfoundation.amp.visibility.feed.fm.schema.VisibilityTemplates;
import org.digijava.kernel.persistence.PersistenceManager;
import org.digijava.module.aim.dbentity.AmpFeaturesVisibility;
import org.digijava.module.aim.dbentity.AmpFieldsVisibility;
import org.digijava.module.aim.dbentity.AmpModulesVisibility;
import org.digijava.module.aim.dbentity.AmpTemplatesVisibility;
import org.digijava.module.aim.form.VisibilityManagerForm;
import org.digijava.module.aim.helper.Constants;
import org.digijava.module.aim.helper.VisibilityManagerExportHelper;
import org.digijava.module.aim.util.DbUtil;
import org.digijava.module.aim.util.FeaturesUtil;
import org.hibernate.HibernateException;
import org.hibernate.Session;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.util.*;

public class VisibilityManager extends MultiAction {

    private static Logger logger = Logger.getLogger(VisibilityManager.class);

    private ServletContext ampContext = null;
    
    public ActionForward modePrepare(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        //generateAllFieldsInFile();
        HttpSession session = request.getSession();
        if (session.getAttribute("ampAdmin") == null) {
            return mapping.findForward("index");
        } else {
            String str = (String)session.getAttribute("ampAdmin");
            if (str.equals("no")) {
                    return mapping.findForward("index");
                }
            }
        return  modeSelect(mapping, form, request, response);
    }

    public ActionForward modeSelect(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        // TODO Auto-generated method stub
        //clear all message previously added
        ((VisibilityManagerForm) form).clearMessages();

        if (request.getParameter("action") != null) {
            if(request.getParameter("action").compareTo("add")==0) return modeAddTemplate(mapping, form, request, response);
            if(request.getParameter("action").compareTo("viewFields")==0) return modeViewFields(mapping, form, request, response);
            if(request.getParameter("action").compareTo("cleanUp")==0) return modeStartCleanUp(mapping, form, request, response);
            if(request.getParameter("action").compareTo("step2clean")==0) return modeCleanUpStep2(mapping, form, request, response);
            if(request.getParameter("action").compareTo("step3clean")==0) return modeCleanUpStep3(mapping, form, request, response);//TODO  return modeCleanUpStep2(mapping, form, request, response);
            if(request.getParameter("action").compareTo("step4clean")==0) return modeCleanUpStep4(mapping, form, request, response);
            if(request.getParameter("action").compareTo("edit")==0) {

                return modeEditTemplate(mapping, form, request, response);
            }
            if(request.getParameter("action").compareTo("delete")==0) return modeDeleteTemplate(mapping, form, request, response);              
            if(request.getParameter("action").compareTo("deleteFFM")==0) return modeDeleteFFM(mapping, form, request, response);
            if(request.getParameter("action").compareTo("manageTemplates")==0) return modeManageTemplates(mapping, form, request, response);
        }
        if(request.getParameter("newTemplate")!=null) return modeSaveTemplate(mapping, form, request, response);
        if(request.getParameter("saveEditTemplate")!=null) return modeSaveEditTemplate(mapping, form, request, response);
        if(request.getParameter("saveTreeVisibility")!=null) return modeSaveTreeVisibility(mapping, (VisibilityManagerForm) form, request, response);
        if(request.getParameter("exportTreeVisibility")!=null) return modeExportTreeVisibility(mapping, form, request, response);
        if(request.getParameter("importTreeVisibility")!=null) return modeImportTreeVisibility(mapping, form, request, response);
        
        Collection templates=FeaturesUtil.getAMPTemplatesVisibilityWithSession();
        FeaturesUtil.setUsedByTeamNames(templates);
        VisibilityManagerForm vForm=(VisibilityManagerForm) form;
        vForm.setTemplates(templates);
        vForm.setMode("manageTemplates");
        return mapping.findForward("forward");
    }
    
    public ActionForward modeImportTreeVisibility(ActionMapping mapping,ActionForm form, HttpServletRequest request,HttpServletResponse response) throws Exception {
        VisibilityManagerForm vForm=(VisibilityManagerForm) form;
        JAXBContext jc = JAXBContext.newInstance("org.dgfoundation.amp.visibility.feed.fm.schema");
        Unmarshaller um = jc.createUnmarshaller();
        try {
            VisibilityTemplates  vtemplate = (VisibilityTemplates) um.unmarshal(vForm.getUploadFile().getInputStream());
            VisibilityManagerExportHelper vhelper = new VisibilityManagerExportHelper();
            vhelper.importXmlVisbilityTemplate(vtemplate);
        } catch (JAXBException je) {
            logger.error(je.getMessage(), je);
        }
        return modeManageTemplates(mapping, form, request, response);
    }
    
    public ActionForward modeExportTreeVisibility(ActionMapping mapping,ActionForm form, HttpServletRequest request,HttpServletResponse response) throws Exception {
        JAXBContext jc = JAXBContext.newInstance("org.dgfoundation.amp.visibility.feed.fm.schema");
        Marshaller m = jc.createMarshaller();
        VisibilityManagerExportHelper vhelper = new VisibilityManagerExportHelper();
        if (vhelper.buildVisibilityTemplate() != null ){
            response.setContentType("text/xml");
            response.setHeader("content-disposition","attachment; filename=FmBackUp.xml");
            m.marshal(vhelper.buildVisibilityTemplate(), response.getOutputStream());
        }
        return null;
}
    
    public ActionForward modeManageTemplates(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        // TODO Auto-generated method stub
        Collection templates=FeaturesUtil.getAMPTemplatesVisibility();
        VisibilityManagerForm vForm=(VisibilityManagerForm) form;
        vForm.setTemplates(templates);
        vForm.setMode("manageTemplates");
        return mapping.findForward("forward");
    }

    public ActionForward modeAddTemplate(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        // TODO Auto-generated method stub
        VisibilityManagerForm vForm=(VisibilityManagerForm) form;
        Collection modules=FeaturesUtil.getAMPModulesVisibility();
        vForm.setMode("addNew");
        vForm.setModules(modules);
        vForm.setFmTemplate(null);
        vForm.setFmTemplateList(FeaturesUtil.getAMPTemplatesVisibility());
        PersistenceManager.getRequestDBSession().flush();
        return mapping.findForward("forward");
    }


    public ActionForward modeViewFields(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        // TODO Auto-generated method stub
        VisibilityManagerForm vForm=(VisibilityManagerForm) form;
        Collection modules=FeaturesUtil.getAMPModulesVisibility();
        Collection features=FeaturesUtil.getAMPFeaturesVisibility();
        Collection fields=FeaturesUtil.getAMPFieldsVisibility();
        vForm.setMode("viewFields");
        
        // Create a new Collection only with AmpModulesVisibility object not repeated (can't change the query because is used elsewhere). 
        Collection modulesGrouped = new ArrayList<AmpModulesVisibility>();
        String nameAux = "";

        for (Object module : modules) {
            AmpModulesVisibility auxAmp = (AmpModulesVisibility) module;
            // Assuming the query is ordered by name.
            if (!nameAux.equals(auxAmp.getName())) {
                modulesGrouped.add(auxAmp);
                nameAux = auxAmp.getName();
            }
        }
        
        vForm.setAllModules(modulesGrouped);
        vForm.setAllFeatures(features);
        vForm.setAllFields(fields);

        return mapping.findForward("forward");
    }


    public ActionForward modeStartCleanUp(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception 
    {
        VisibilityManagerForm vForm=(VisibilityManagerForm) form;
        vForm.setMode("step1clean");
        logger.info("First step of the wizzard");
        return mapping.findForward("cleaning");
    }
    public ActionForward modeCleanUpStep2(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception 
    {
        VisibilityManagerForm vForm=(VisibilityManagerForm) form;
        vForm.setMode("step2clean");
        logger.info("Step 2 of the wizard...");
        return modeCleanUp( mapping,form, request, response);
        //return mapping.findForward("cleaning");
    }

    public ActionForward modeCleanUpStep3(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception 
    {
        VisibilityManagerForm vForm=(VisibilityManagerForm) form;
        vForm.setMode("step3clean");
        logger.info("Step 3 of the wizzard...");
        logger.info("Generating the file with all FM tags");
        generateAllFieldsInFile();
        logger.info("the file was generating... finished!");
        ampContext=this.getServlet().getServletContext();
        HttpSession htSession=request.getSession();
        htSession.setAttribute("FMcache", "readwrite");
        ampContext.setAttribute("FMcache","readwrite");

        //return modeCleanUp( mapping,form, request, response);
        return mapping.findForward("cleaning");
    }

    public ActionForward modeCleanUpStep4(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception 
    {
        //  VisibilityManagerForm vForm=(VisibilityManagerForm) form;
        //vForm.setMode("step4clean");
        logger.info("Step 4 of the wizard...");
        //TODO remove the flag from session!!!
        ampContext=this.getServlet().getServletContext();
        ampContext.removeAttribute("FMcache");
        HttpSession htSession=request.getSession();
        htSession.removeAttribute("FMcache");
        return mapping.findForward("forward");
    }

    public ActionForward modeCleanUp(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        // TODO Auto-generated method stub
        ArrayList<Long> allFieldsId=(ArrayList<Long>) FeaturesUtil.getAllFieldsId();
        ArrayList<Long> allFeaturesId=(ArrayList<Long>) FeaturesUtil.getAllFeaturesId();
        ArrayList<Long> allModulesId=(ArrayList<Long>) FeaturesUtil.getAllModulesId();

        logger.info("Deleting all fields...");
        for (Long idf : allFieldsId) {
            FeaturesUtil.deleteOneField(idf);

        }
        logger.info("...finished to delete all fields!");
        logger.info("Deleting all features...");
        for (Long idf : allFeaturesId) {
            FeaturesUtil.deleteOneFeature(idf);

        }
        logger.info("       ....finished to delete all features!");
        logger.info("Deleting all modules...");
        for (Long idf : allModulesId) {
            FeaturesUtil.deleteOneModule(idf);

        }
        logger.info("       ....finished to delete all modules!");
        allModulesId=(ArrayList<Long>) FeaturesUtil.getAllModulesId();
        logger.info("Deleting all modules...PART 2 :)");
        for (Long idf : allModulesId) {
            FeaturesUtil.deleteModule(idf);

        }
        logger.info("       ....finished to delete all modules! PART 2 :)");
        //refreshing the amp tree visibility from amp context!
        AmpTreeVisibility ampTreeVisibility = new AmpTreeVisibility();
        AmpTemplatesVisibility currentTemplate = FeaturesUtil.getDefaultAmpTemplateVisibility();
        ampTreeVisibility.buildAmpTreeVisibility(currentTemplate);
        ampContext=this.getServlet().getServletContext();
        FeaturesUtil.setAmpTreeVisibility(ampContext, request.getSession(), ampTreeVisibility);


        //refresh the collection from the view...
        VisibilityManagerForm vForm=(VisibilityManagerForm) form;
        Collection modules=FeaturesUtil.getAMPModulesVisibility();
        Collection features=FeaturesUtil.getAMPFeaturesVisibility();
        Collection fields=FeaturesUtil.getAMPFieldsVisibility();
        vForm.setAllModules(modules);
        vForm.setAllFeatures(features);
        vForm.setAllFields(fields);
        vForm.setMode("step2clean");
        PersistenceManager.getRequestDBSession().flush();
        return mapping.findForward("cleaning");
    }

    public ActionForward modeSaveTemplate(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        Session hbsession = PersistenceManager.getRequestDBSession();
        if(FeaturesUtil.existTemplateVisibility(request.getParameter("templateName"),null)) {
            ((VisibilityManagerForm) form).addError("aim:fm:errortemplateExistent", "Template name already exist in database. Please choose another name for template.");
            //errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("error.aim.templateExistent"));
            //saveErrors(request, errors);
        } else {
            VisibilityManagerForm vForm = (VisibilityManagerForm) form;

            if (vForm.getFmTemplate() == null) //we're not cloning an existing template
                FeaturesUtil.insertTemplate(request.getParameter("templateName"), hbsession);
            else{
                //we need to clone an existing template
                AmpTemplatesVisibility existing = FeaturesUtil.getTemplateById(vForm.getFmTemplate());
                AmpTemplatesVisibility newT = (AmpTemplatesVisibility) existing.clone();
                hbsession.evict(existing);
                newT.setName(vForm.getTemplateName());
                newT.setId(null); //fool hibernate
                DbUtil.saveOrUpdateObject(newT);
            }
        }
        //for refreshing the page...
        VisibilityManagerForm vForm=(VisibilityManagerForm) form;
        Collection templates=FeaturesUtil.getAMPTemplatesVisibility();
        vForm.setTemplates(templates);
        vForm.setMode("manageTemplates");
        PersistenceManager.getRequestDBSession().flush();
        return mapping.findForward("forward");
    }

    public ActionForward modeEditTemplate(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        
        VisibilityManagerForm vForm = (VisibilityManagerForm) form;
        Long templateId = null;
        HttpSession session=request.getSession();
        if(request.getParameter("templateId")!=null)
            templateId= Long.parseLong(request.getParameter("templateId"));
        if(templateId==null) templateId=(Long)request.getAttribute("templateId");

        String templateName=FeaturesUtil.getTemplateNameVisibility(templateId);
        session.setAttribute("templateName", templateName);
        session.setAttribute("templateId",templateId);
        //AmpTemplatesVisibility templateVisibility = FeaturesUtil.getTemplateVisibility(templateId, hbsession);
        AmpTemplatesVisibility templateVisibility = FeaturesUtil.getTemplateById(templateId);
        AmpTreeVisibility ampTreeVisibility=new AmpTreeVisibility();
        AmpTreeVisibility ampTreeVisibilityAux=new AmpTreeVisibility();
        ampTreeVisibility.buildAmpTreeVisibility(templateVisibility);
        String existSubmodules;
        //ampContext=this.getServlet().getServletContext();

        if( ampTreeVisibilityAux.buildAmpTreeVisibilityMultiLevel(templateVisibility) )
            existSubmodules="true";
        else existSubmodules="false";
        vForm.setExistSubmodules(existSubmodules);
        vForm.setAmpTreeVisibility(ampTreeVisibilityAux);


        vForm.setMode("editTemplateTree");

        {//for refreshing the page
            Collection templates=FeaturesUtil.getAMPTemplatesVisibility();
            vForm.setTemplates(templates);
        }
        PersistenceManager.getRequestDBSession().flush();
        return mapping.findForward("forward");
    }

    public ActionForward modeSaveEditTemplate(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        // TODO Auto-generated method stub
        VisibilityManagerForm vForm = (VisibilityManagerForm) form;
        Long templateId;
        HttpSession session=request.getSession();
        templateId=vForm.getTemplateId();
        String templateName=FeaturesUtil.getTemplateNameVisibility(templateId);
        session.setAttribute("templateName", templateName);
        Collection allAmpModules=FeaturesUtil.getAMPModules();
        Collection newTemplateModulesList=new ArrayList<>();
        for (Object allAmpModule : allAmpModules) {

            AmpModulesVisibility ampModule = (AmpModulesVisibility) allAmpModule;
            String existentModule = ampModule.getName().replaceAll(" ", "");
            if (request.getParameter("moduleVis:" + existentModule) != null)
                if (request.getParameter("moduleVis:" + existentModule).compareTo("enable") == 0) {
                    newTemplateModulesList.add(ampModule);
                }
        }
        FeaturesUtil.updateModulesTemplate(newTemplateModulesList, templateId, vForm.getTemplateName());
        {//for refreshing the page
            Collection templates=FeaturesUtil.getAMPTemplatesVisibility();
            vForm.setTemplates(templates);
        }

        vForm.addMessage("aim:ampfeaturemanager:updatedTemplate", "Template was updated");
        PersistenceManager.getRequestDBSession().flush();
        return mapping.findForward("forward");
    }

    public ActionForward modeDeleteTemplate(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {

        FeaturesUtil.deleteTemplateVisibility(Long.parseLong(request.getParameter("templateId")));
        ((VisibilityManagerForm)form).addMessage("aim:fm:message:deletedTemplate", "The template was deleted.");
//      return modeManageTemplates(mapping, form, request, response);
        
        {//for refreshing the page
            VisibilityManagerForm vForm = (VisibilityManagerForm) form;
            Collection templates=FeaturesUtil.getAMPTemplatesVisibilityWithSession();
            vForm.setTemplates(templates);
            vForm.setMode("manageTemplates");
        }

        return mapping.findForward("forward");
    }

    public ActionForward modeDeleteFFM(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        Session hbsession = PersistenceManager.getRequestDBSession();
        if(request.getParameter("fieldId")!=null) FeaturesUtil.deleteFieldVisibility(Long.parseLong(request.getParameter("fieldId")),hbsession);//delete field
        if(request.getParameter("featureId")!=null) FeaturesUtil.deleteFeatureVisibility(Long.parseLong(request.getParameter("featureId")),hbsession);//delete feature
        if(request.getParameter("moduleId")!=null) FeaturesUtil.deleteModuleVisibility(Long.parseLong(request.getParameter("moduleId")),hbsession);//delete module
        this.updateAmpContext(new AmpTreeVisibility(), hbsession, request.getSession());
        return modeViewFields(mapping, form, request, response);
    }

    public ActionForward modeSaveTreeVisibility(ActionMapping mapping, VisibilityManagerForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        // TODO Auto-generated method stub

        AmpTreeVisibility ampTreeVisibility=new AmpTreeVisibility();
        Session hbsession = PersistenceManager.getRequestDBSession();
        HttpSession session=request.getSession();
        Long templateId = (Long)session.getAttribute("templateId");
        
        if(FeaturesUtil.existTemplateVisibility(request.getParameter("templateName"),templateId)) {
            form.addError("aim:fm:errortemplateExistent", "Template name already exist in database. Please choose another name for template.");
            request.setAttribute("templateId", templateId);
        } else {
            AmpTemplatesVisibility templateVisibility = FeaturesUtil.getTemplateVisibility(templateId);
            ampTreeVisibility.buildAmpTreeVisibility(templateVisibility);
            TreeSet<AmpModulesVisibility> modules = new TreeSet<>();
            TreeSet<AmpFeaturesVisibility> features = new TreeSet<>();
            TreeSet<AmpFieldsVisibility> fields = new TreeSet<>();

            for (AmpTreeVisibility obj : ampTreeVisibility.getItems().values()) {
                if (obj.getRoot() instanceof AmpModulesVisibility)
                    if (request.getParameter("moduleVis:" + obj.getRoot().getId()) != null) {
                        modules.add((AmpModulesVisibility) obj.getRoot());
                    }
                recursivelyParseFMTree(obj, modules, features, fields, request);
            }

            FeaturesUtil.updateAmpTemplateNameTreeVisibility(request.getParameter("templateName"), templateId, hbsession);
            FeaturesUtil.updateAmpModulesTreeVisibility(modules, templateId, hbsession);
            FeaturesUtil.updateAmpFeaturesTreeVisibility(features, templateId, hbsession);
            FeaturesUtil.updateAmpFieldsTreeVisibility(fields, templateId, hbsession);
            request.setAttribute("templateId", templateId);
            
            form.addMessage("aim:ampfeaturemanager:visibilityTreeUpdated", Constants.FEATURE_MANAGER_VISIBILITY_TREE_UPDATED);
            // we need to refresh FM cache for non-default templates, since a user can continue to use a WS based on it  
            FeaturesUtil.setAmpTreeVisibility(this.getServlet().getServletContext(), session, ampTreeVisibility);

            AmpTemplatesVisibility currentTemplate = FeaturesUtil.getDefaultAmpTemplateVisibility();
            ampTreeVisibility.buildAmpTreeVisibility(currentTemplate);

            ampContext=this.getServlet().getServletContext();
            FeaturesUtil.setAmpTreeVisibility(ampContext, session, ampTreeVisibility);
            
            // notify about visibility change
            DataVisibility.notifyVisibilityChanged();
        }

        return modeEditTemplate(mapping,form,request,response);
    }


    public void recursivelyParseFMTree(AmpTreeVisibility atv, TreeSet<AmpModulesVisibility> modules, TreeSet<AmpFeaturesVisibility> features, TreeSet<AmpFieldsVisibility> fields,HttpServletRequest request)
    {
        if (atv.getItems() ==null ) return;
        if (atv.getItems().isEmpty()) return;
        for(AmpTreeVisibility auxTree:atv.getItems().values()) {
            if (auxTree.getRoot() instanceof AmpModulesVisibility)
                if(request.getParameter("moduleVis:"+auxTree.getRoot().getId())!=null)
                {
                    modules.add((AmpModulesVisibility) auxTree.getRoot());
                }
            if(auxTree.getRoot() instanceof AmpFeaturesVisibility)
                if(request.getParameter("featureVis:"+auxTree.getRoot().getId())!=null)
                {
                    features.add((AmpFeaturesVisibility) auxTree.getRoot());
                }
            if(auxTree.getRoot() instanceof AmpFieldsVisibility)
                if(request.getParameter("fieldVis:"+auxTree.getRoot().getId())!=null)
                {
                    fields.add((AmpFieldsVisibility) auxTree.getRoot());
                }
            recursivelyParseFMTree(auxTree, modules, features, fields, request);
        }
    }
    
    public String trimString(String s)
    {
        StringTokenizer st = new StringTokenizer(s);
        StringBuilder result= new StringBuilder();
        while (st.hasMoreTokens()) 
            result.append(st.nextToken()).append(" ");
        return result.toString();

    }


    public int getAllPatchesFiles(String abstractPatchesLocation, TreeSet<String> modules, TreeSet<String> features, TreeSet<String> fields)
    {
        File dir = new File(abstractPatchesLocation);
        String[] files = dir.list();

        if(files!=null)
            if(files.length>0)
                try {
                    for (String file : files) {
                        File f = new File(dir, file);

                        if (f.isDirectory()) {
                            getAllPatchesFiles(f.getAbsolutePath(), modules, features, fields);
                        }
                        if (!f.isDirectory() && f.getName().contains(".jsp") && !f.getName().contains("allVisibilityTagsComputed")) {
                            Scanner scanner = new Scanner(f);
                            scanner.useDelimiter(System.getProperty("line.separator"));
                            while (scanner.hasNext()) {
                                String s = scanner.next();
                                if (s.contains("<module:display")) {
                                    String aux = "";
                                    if (s.indexOf(">", (s.indexOf("<module:display")) + 1) < 0) {
                                        aux = scanner.next();
                                    }
                                    String module = "";
                                    if ("".equals(aux))
                                        module = s.substring(s.indexOf("<module:display"), s.indexOf(">", (s.indexOf("<module:display")) + 1) + 1);
                                    else module = s.substring(s.indexOf("<module:display"), s.length()) + aux;
                                    if (!module.contains("${"))
                                        modules.add(trimString(module + "</module:display>") + "\n");


                                }
                                if (s.contains("<field:display")) {
                                    String aux = "";
                                    if (s.indexOf(">", (s.indexOf("<field:display")) + 1) < 0) {
                                        aux = scanner.next();
                                        if (aux.indexOf(">", (aux.indexOf("<field:display")) + 1) < 0)
                                            aux += scanner.next();
                                    }
                                    String module = "";
                                    if ("".equals(aux))
                                        module = s.substring(s.indexOf("<field:display"), s.indexOf(">", (s.indexOf("<field:display")) + 1) + 1);
                                    else module = s.substring(s.indexOf("<field:display"), s.length()) + aux;
                                    if (!module.contains("${"))
                                        fields.add(trimString(module + "</field:display>") + "\n");
                                }

                                if (s.contains("<feature:display")) {
                                    String aux = "";
                                    if (s.indexOf(">", (s.indexOf("<feature:display")) + 1) < 0) {
                                        aux = scanner.next();
                                    }
                                    String module = "";
                                    if ("".equals(aux))
                                        module = s.substring(s.indexOf("<feature:display"), s.indexOf(">", (s.indexOf("<feature:display")) + 1) + 1);
                                    else module = s.substring(s.indexOf("<feature:display"), s.length()) + aux;
                                    if (!module.contains("${"))
                                        features.add(trimString(module + "</feature:display>") + "\n");
                                }

                            }
                            scanner.close();
                        }
                    }
                }catch(Exception e){ e.printStackTrace(); }
                return 1;
    }
    
    private void updateAmpContext(AmpTreeVisibility ampTreeVisibility, Session hbsession,HttpSession session) throws HibernateException {
        AmpTemplatesVisibility currentTemplate = FeaturesUtil.getDefaultAmpTemplateVisibility();
        ampTreeVisibility.buildAmpTreeVisibility(currentTemplate);
        ampContext = this.getServlet().getServletContext();
        FeaturesUtil.setAmpTreeVisibility(ampContext, session, ampTreeVisibility);
    }

    private void generateAllFieldsInFile(){
        TreeSet<String> modules=new TreeSet<String>();
        TreeSet<String> features=new TreeSet<String>();
        TreeSet<String> fields=new TreeSet<String>();
        getAllPatchesFiles(this.getServlet().getServletContext().getRealPath("/"),modules, features, fields);
        try {
            FileWriter fstream = new FileWriter(this.getServlet().getServletContext().getRealPath("/")+"repository/aim/view/allVisibilityTagsComputed.jsp");
            BufferedWriter out = new BufferedWriter(fstream);
            String outHeader="";
            outHeader="<%@ page pageEncoding=\"UTF-8\" %>\n"+
            "<%@ taglib uri=\"http://struts.apache.org/tags-bean\" prefix=\"bean\" %>\n"+
            "<%@ taglib uri=\"http://struts.apache.org/tags-logic\" prefix=\"logic\" %>\n"+
            "<%@ taglib uri=\"http://struts.apache.org/tags-tiles\" prefix=\"tiles\" %>\n"+
            "<%@ taglib uri=\"http://struts.apache.org/tags-html\" prefix=\"html\" %>\n"+
            "<%@ taglib uri=\"/src/main/resources/tld/digijava.tld\" prefix=\"digi\" %>\n"+
            "<%@ taglib uri=\"/src/main/resources/tld/c.tld\" prefix=\"c\" %>\n"+
            "<%@ taglib uri=\"/src/main/resources/tld/category.tld\" prefix=\"category\" %>\n"+
            "<%@ taglib uri=\"/src/main/resources/tld/fieldVisibility.tld\" prefix=\"field\" %>\n"+
            "<%@ taglib uri=\"/src/main/resources/tld/featureVisibility.tld\" prefix=\"feature\" %>\n"+
            "<%@ taglib uri=\"/src/main/resources/tld/moduleVisibility.tld\" prefix=\"module\" %>\n"+
            "<%@ taglib uri=\"/src/main/resources/tld/fn.tld\" prefix=\"fn\" %>\n";



            out.append(outHeader);

            for (String s : modules) {
                out.append(s);
            }
            for (String s : features) {
                out.append(s);
            }
            for (String s : fields) {
                out.append(s);
            }

            out.close();
        }catch (Exception e){//Catch exception if any
            System.err.println("Error: " + e.getMessage());
        }

    }
}
