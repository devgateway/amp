package org.digijava.module.aim.action;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionServlet;
import org.apache.struts.action.DynaActionForm;
import org.dgfoundation.amp.utils.MultiAction;
import org.digijava.module.aim.dbentity.AmpFeature;
import org.digijava.module.aim.dbentity.FeatureTemplates;
import org.digijava.module.aim.helper.Constants;
import org.digijava.module.aim.helper.GlobalSettingsConstants;
import org.digijava.module.aim.util.FeaturesUtil;
 
public class FeatureManager extends MultiAction {
    
    private static Logger logger = Logger.getLogger(FeatureManager.class);
    
    private ServletContext ampContext = null;
    
    public ActionForward modePrepare(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        // TODO Auto-generated method stub
        DynaActionForm fmForm = (DynaActionForm) form;
        Collection features = FeaturesUtil.getAMPFeatures();
        Collection templates=FeaturesUtil.getAMPTemplates();
        fmForm.set("templates",templates);
        fmForm.set("features",features);
        
        return modeSelect(mapping, form, request, response);
    }


    public ActionForward modeSelect(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        // TODO Auto-generated method stub
        if(request.getParameter("newTemplate")!=null) return modeNew(mapping, form, request, response);
        if(request.getParameter("saveEditTemplate")!=null) return modeSaveEdit(mapping, form, request, response);
        if(request.getParameter("event")!=null)
        {
            if(request.getParameter("event").compareTo("delete")==0) return modeDelete(mapping, form, request, response);
            if(request.getParameter("event").compareTo("edit")==0) return modeEdit(mapping, form, request, response);
        }
        //return modeContinue(mapping, form, request, response);
        HttpSession session=request.getSession();
        session.setAttribute("newEditTemplate",null);
        return mapping.findForward("forward");
    }
    
    public ActionForward modeNew(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        // TODO Auto-generated method stub
        DynaActionForm fmForm = (DynaActionForm) form;
        if(FeaturesUtil.existTemplate(request.getParameter("templateName")))
        {
            ActionMessages errors = new ActionMessages();
            errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage(
                    "error.aim.templateExistent"));
            
            saveErrors(request, errors);
        }
        else
        {
            String x=(String)fmForm.get("ampFeatures");
            //////System.out.println(x);
            Collection features = FeaturesUtil.getAMPFeatures();
            Collection newToAddFeatures=new ArrayList();
            for(Iterator it=features.iterator();it.hasNext();)
            {
                AmpFeature ampFeature=(AmpFeature)it.next();
                String existentFeature=ampFeature.getName().replaceAll(" ","");
                
                if(request.getParameter("feature:"+existentFeature).compareTo("enable")==0)
                    newToAddFeatures.add(ampFeature);
            
            }
            //for refreshing the page

            FeaturesUtil.insertTemplateFeatures( newToAddFeatures, request.getParameter("templateName"));
            
            {
                Collection features11 = FeaturesUtil.getAMPFeatures();
                Collection templates11=FeaturesUtil.getAMPTemplates();
                fmForm.set("templates",templates11);
                fmForm.set("features",features11);
                
            }
        }
        
        return mapping.findForward("forward");
    }
    
    public ActionForward modeSaveEdit(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        // TODO Auto-generated method stub
        DynaActionForm fmForm = (DynaActionForm) form;
        HttpSession session=request.getSession();
        Collection features = FeaturesUtil.getAMPFeatures();
        Collection newToAddFeatures=new ArrayList();
        Boolean featureOn=new Boolean(true);
        FeatureTemplates defaultTemplate=FeaturesUtil.getTemplate(FeaturesUtil.getGlobalSettingValue(GlobalSettingsConstants.FEATURE_TEMPLATE));
        Long currentTemplateId=new Long(Long.parseLong(session.getAttribute("templateId").toString()));
        ampContext = getServlet().getServletContext();
        boolean flag=false;
        if(defaultTemplate!=null)
            if(defaultTemplate.getTemplateId()!=null)
                if(defaultTemplate.getTemplateId().compareTo(currentTemplateId)==0) flag=true;
        if(flag)    
            {
                    ampContext.removeAttribute(Constants.ME_FEATURE);
                    ampContext.removeAttribute(Constants.PI_FEATURE);
                    ampContext.removeAttribute(Constants.AA_FEATURE);
                    ampContext.removeAttribute(Constants.CL_FEATURE);
                    ampContext.removeAttribute(Constants.DC_FEATURE);
                    ampContext.removeAttribute(Constants.SC_FEATURE);
                    ampContext.removeAttribute(Constants.MS_FEATURE);
                    ampContext.removeAttribute(Constants.LB_FEATURE);
                    ampContext.removeAttribute(Constants.SA_FEATURE);
            }
        for(Iterator it=features.iterator();it.hasNext();)
        {
            AmpFeature ampFeature=(AmpFeature)it.next();
            String existentFeature=ampFeature.getName().replaceAll(" ","");
            
            if(request.getParameter("feature:"+existentFeature).compareTo("enable")==0)
            {   newToAddFeatures.add(ampFeature);
//          //TODO  if template is the default one (current template)
                    
            if(flag)
            {
            
             if (ampFeature.getCode().equalsIgnoreCase(Constants.ME_FEATURE)) {
                synchronized (ampContext) {
                        ampContext.setAttribute(Constants.ME_FEATURE,featureOn);
                    } 
                }
            
            if (ampFeature.getCode().equalsIgnoreCase(Constants.PI_FEATURE)) {
                synchronized (ampContext) {
                        ampContext.setAttribute(Constants.PI_FEATURE,featureOn);
                    } 
            }
                
            if (ampFeature.getCode().equalsIgnoreCase(Constants.AA_FEATURE)) {
                synchronized (ampContext) {
                        ampContext.setAttribute(Constants.AA_FEATURE,featureOn);
                    } 
            }

            if (ampFeature.getCode().equalsIgnoreCase(Constants.CL_FEATURE)) {
                synchronized (ampContext) {
                        ampContext.setAttribute(Constants.CL_FEATURE,featureOn);
                    } 
            }

            if (ampFeature.getCode().equalsIgnoreCase(Constants.DC_FEATURE)) {
                synchronized (ampContext) {
                        ampContext.setAttribute(Constants.DC_FEATURE,featureOn);
                    } 
            }

            if (ampFeature.getCode().equalsIgnoreCase(Constants.SC_FEATURE)) {
                synchronized (ampContext) {
                        ampContext.setAttribute(Constants.SC_FEATURE,featureOn);
                    } 
            }

            if (ampFeature.getCode().equalsIgnoreCase(Constants.MS_FEATURE)) {
                synchronized (ampContext) {
                        ampContext.setAttribute(Constants.MS_FEATURE,featureOn);
                    } 
            }

            if (ampFeature.getCode().equalsIgnoreCase(Constants.LB_FEATURE)) {
                synchronized (ampContext) {
                        ampContext.setAttribute(Constants.LB_FEATURE,featureOn);
                    } 
            }
            
            if (ampFeature.getCode().equalsIgnoreCase(Constants.SA_FEATURE)) {
                synchronized (ampContext) {
                        ampContext.setAttribute(Constants.SA_FEATURE,featureOn);
                    } 
            }

            }
    
            //hide disabled features
            else {
                //TODO if template is the default one (current template)
                if(defaultTemplate.getTemplateId().compareTo(currentTemplateId)==0)
                {
                if (ampFeature.getCode().equalsIgnoreCase(Constants.ME_FEATURE)) 
                    if (ampContext.getAttribute(Constants.ME_FEATURE) != null) {
                        ampContext.removeAttribute(Constants.ME_FEATURE);
                    }

                if (ampFeature.getCode().equalsIgnoreCase(Constants.PI_FEATURE)) 
                    if (ampContext.getAttribute(Constants.PI_FEATURE) != null) {
                        ampContext.removeAttribute(Constants.PI_FEATURE);
                    }

                if (ampFeature.getCode().equalsIgnoreCase(Constants.AA_FEATURE)) 
                    if (ampContext.getAttribute(Constants.AA_FEATURE) != null) {
                        ampContext.removeAttribute(Constants.AA_FEATURE);
                    }

                if (ampFeature.getCode().equalsIgnoreCase(Constants.CL_FEATURE)) 
                    if (ampContext.getAttribute(Constants.CL_FEATURE) != null) {
                        ampContext.removeAttribute(Constants.CL_FEATURE);
                    }

                if (ampFeature.getCode().equalsIgnoreCase(Constants.DC_FEATURE)) 
                    if (ampContext.getAttribute(Constants.DC_FEATURE) != null) {
                        ampContext.removeAttribute(Constants.DC_FEATURE);
                    }

                if (ampFeature.getCode().equalsIgnoreCase(Constants.SC_FEATURE)) 
                    if (ampContext.getAttribute(Constants.SC_FEATURE) != null) {
                        ampContext.removeAttribute(Constants.SC_FEATURE);
                    }
                
                if (ampFeature.getCode().equalsIgnoreCase(Constants.MS_FEATURE)) 
                    if (ampContext.getAttribute(Constants.MS_FEATURE) != null) {
                        ampContext.removeAttribute(Constants.MS_FEATURE);
                    }

                if (ampFeature.getCode().equalsIgnoreCase(Constants.LB_FEATURE)) {
                    if (ampContext.getAttribute(Constants.LB_FEATURE) != null) {
                        ampContext.removeAttribute(Constants.LB_FEATURE);
                    }
                }
                
                if (ampFeature.getCode().equalsIgnoreCase(Constants.SA_FEATURE)) 
                    if (ampContext.getAttribute(Constants.SA_FEATURE) != null) {
                        ampContext.removeAttribute(Constants.SA_FEATURE);
                    }
                }
                
                }

            }
        
        }
        FeaturesUtil.updateTemplateFeatures( newToAddFeatures, new Long(Long.parseLong(session.getAttribute("templateId").toString())),request.getParameter("templateName").toString());

        session.setAttribute("templateId",null);
        session.setAttribute("templateName",null);
        session.setAttribute("newEditTemplate","new");
        ActionMessages errors = new ActionMessages();
        errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage(
                "error.aim.templateSaved"));
        saveErrors(request, errors);
        {//for refreshing the page
            Collection features1 = FeaturesUtil.getAMPFeatures();
            Collection templates=FeaturesUtil.getAMPTemplates();
            fmForm.set("templates",templates);
            fmForm.set("features",features1);
        }
        return mapping.findForward("forward");
    }

    
    public ActionForward modeDelete(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        // TODO Auto-generated method stub
        FeaturesUtil.deleteTemplate(new Long(Long.parseLong(request.getParameter("templateId"))));
        {//for refreshing the page
            DynaActionForm fmForm = (DynaActionForm) form;
            Collection features1 = FeaturesUtil.getAMPFeatures();
            Collection templates=FeaturesUtil.getAMPTemplates();
            fmForm.set("templates",templates);
            fmForm.set("features",features1);
        }
        return mapping.findForward("forward");
    }
    
    public ActionForward modeEdit(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        // TODO Auto-generated method stub Collection features = FeaturesUtil.getAMPFeatures();
        DynaActionForm fmForm = (DynaActionForm) form;
        HttpSession session=request.getSession();
        Long templateId;
        if(request.getParameter("templateId")!=null) templateId=new Long(Long.parseLong(request.getParameter("templateId")));
            else templateId=(Long)session.getAttribute("templateId");
        Collection templateFeatures=FeaturesUtil.getTemplateFeatures(templateId);
        Collection allAmpFeatures=FeaturesUtil.getAMPFeatures();
        Collection templateFeaturesNotActive=new ArrayList();
        fmForm.set("templateFeatures",templateFeatures);
        String templateName=FeaturesUtil.getTemplateName(templateId);
        //fmForm.set("templateName",templateName);
    
        //fmForm.set("templateId",);
        for(Iterator it=allAmpFeatures.iterator();it.hasNext();)
        {
            boolean found=false;
            AmpFeature x=(AmpFeature)it.next();
            for(Iterator jt=templateFeatures.iterator();jt.hasNext();)
            {
                AmpFeature y=(AmpFeature)jt.next();
                if(x.getName().compareTo(y.getName())==0) found=true;
            }
            if(!found) templateFeaturesNotActive.add(x);
        }
        fmForm.set("templateFeaturesNotActive",templateFeaturesNotActive);
        
        session.setAttribute("newEditTemplate","edit");
        session.setAttribute("templateName",templateName);
        session.setAttribute("templateId",templateId);
        {//for refreshing the page
            Collection features11 = FeaturesUtil.getAMPFeatures();
            Collection templates11=FeaturesUtil.getAMPTemplates();
            fmForm.set("templates",templates11);
            fmForm.set("features",features11);
        }
        return mapping.findForward("forward");
    }
    
    public static void refreshTemplateGlobalSettings1(ActionServlet x)
    {
        
        FeatureTemplates defaultTemplate=FeaturesUtil.getTemplate(FeaturesUtil.getGlobalSettingValue(GlobalSettingsConstants.FEATURE_TEMPLATE));
        ServletContext ampContext1 = null;
        ampContext1 = x.getServletContext();
        Collection features = FeaturesUtil.getTemplateFeatures(defaultTemplate.getTemplateId());
        Boolean featureOn=new Boolean(true);
        {
            ampContext1.removeAttribute(Constants.ME_FEATURE);
            ampContext1.removeAttribute(Constants.PI_FEATURE);
            ampContext1.removeAttribute(Constants.AA_FEATURE);
            ampContext1.removeAttribute(Constants.CL_FEATURE);
            ampContext1.removeAttribute(Constants.DC_FEATURE);
            ampContext1.removeAttribute(Constants.SC_FEATURE);
            ampContext1.removeAttribute(Constants.MS_FEATURE);
            ampContext1.removeAttribute(Constants.LB_FEATURE);
            ampContext1.removeAttribute(Constants.SA_FEATURE);
        }

        for(Iterator it=features.iterator();it.hasNext();)
        {
            AmpFeature ampFeature=(AmpFeature)it.next();
            
             if (ampFeature.getCode().equalsIgnoreCase(Constants.ME_FEATURE)) {
                synchronized (ampContext1) {
                        ampContext1.setAttribute(Constants.ME_FEATURE,featureOn);
                    } 
                }
            
            if (ampFeature.getCode().equalsIgnoreCase(Constants.PI_FEATURE)) {
                synchronized (ampContext1) {
                        ampContext1.setAttribute(Constants.PI_FEATURE,featureOn);
                    } 
            }
                
            if (ampFeature.getCode().equalsIgnoreCase(Constants.AA_FEATURE)) {
                synchronized (ampContext1) {
                        ampContext1.setAttribute(Constants.AA_FEATURE,featureOn);
                    } 
            }

            if (ampFeature.getCode().equalsIgnoreCase(Constants.CL_FEATURE)) {
                synchronized (ampContext1) {
                        ampContext1.setAttribute(Constants.CL_FEATURE,featureOn);
                    } 
            }

            if (ampFeature.getCode().equalsIgnoreCase(Constants.DC_FEATURE)) {
                synchronized (ampContext1) {
                        ampContext1.setAttribute(Constants.DC_FEATURE,featureOn);
                    } 
            }

            if (ampFeature.getCode().equalsIgnoreCase(Constants.SC_FEATURE)) {
                synchronized (ampContext1) {
                        ampContext1.setAttribute(Constants.SC_FEATURE,featureOn);
                    } 
            }

            if (ampFeature.getCode().equalsIgnoreCase(Constants.MS_FEATURE)) {
                synchronized (ampContext1) {
                        ampContext1.setAttribute(Constants.MS_FEATURE,featureOn);
                    } 
            }

            if (ampFeature.getCode().equalsIgnoreCase(Constants.LB_FEATURE)) {
                synchronized (ampContext1) {
                        ampContext1.setAttribute(Constants.LB_FEATURE,featureOn);
                    } 
            }
            
            if (ampFeature.getCode().equalsIgnoreCase(Constants.SA_FEATURE)) {
                synchronized (ampContext1) {
                        ampContext1.setAttribute(Constants.SA_FEATURE,featureOn);
                    } 
            }

            
        
        }

        
    }


    
}
