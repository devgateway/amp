package org.digijava.module.aim.fmtool.action;

import java.io.File;
import java.io.OutputStreamWriter;
import java.util.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.struts.actions.*;
import org.apache.struts.action.*;

import org.dgfoundation.amp.visibility.AmpObjectVisibility;
import org.digijava.module.aim.dbentity.AmpModulesVisibility;
import org.digijava.module.aim.fmtool.dbentity.AmpFeatureSource;
import org.digijava.module.aim.fmtool.form.FeatureManagerCheckForm;
import org.digijava.module.aim.fmtool.types.*;
import org.digijava.module.aim.fmtool.util.FMToolConstants;
import org.digijava.module.aim.fmtool.util.FeatureManagerCheckDBUtil;
import org.digijava.module.aim.fmtool.util.FeatureManagerTreeHelper;
import org.digijava.module.aim.util.FeaturesUtil;
import org.digijava.module.dataExchange.form.ExportForm.LogStatus;

public class FeatureManagerCheckAction extends DispatchAction {
	
	public ActionForward show(ActionMapping mapping, ActionForm form,HttpServletRequest request, HttpServletResponse response) throws java.lang.Exception {
		FeatureManagerCheckForm fmForm = (FeatureManagerCheckForm)form;
		
		FeatureManagerTreeHelper fmth = new FeatureManagerTreeHelper(FeatureManagerCheckDBUtil.getAllAmpModulesVisibility(),
			FeatureManagerCheckDBUtil.getAllAmpFeaturesVisibility(),
			FeatureManagerCheckDBUtil.getAllAmpFieldsVisibility());
		
		fmForm.setSourceList(FeatureManagerCheckDBUtil.getAllFeatureSources());
		
		fmForm.getDuplicatesList().clear();
		fmForm.addDuplicatesList(FeatureManagerCheckDBUtil.getModulesDuplicates());
		fmForm.addDuplicatesList(FeatureManagerCheckDBUtil.getFeaturesDuplicates());
		fmForm.addDuplicatesList(FeatureManagerCheckDBUtil.getFieldsDuplicates());

		fmForm.getProblemList().clear();
		fmForm.addProblemList(FeatureManagerCheckDBUtil.getFeaturesCheckParent());
		fmForm.addProblemList(FeatureManagerCheckDBUtil.getFieldsCheckParent());
		
		fmForm.setFmeTree(fmth.getTree());
		fmForm.addProblemList(fmth.getCircularityElements());
		
//		fmForm.setShowAll(true);
		return mapping.findForward("default");
	}	

	public ActionForward removeFME(ActionMapping mapping, ActionForm form,HttpServletRequest request, HttpServletResponse response) throws java.lang.Exception {
		FeatureManagerCheckForm fmForm = (FeatureManagerCheckForm)form;

		System.out.println(fmForm.getFixValue());
		if (fmForm.getFixValue() != null && fmForm.getFixValueType() != null){
			if (fmForm.getFixValue().equalsIgnoreCase("fixAll")){
				FeatureManagerCheckDBUtil.deleteModulesByNames(fmForm.getFMEDuplicatesList(FMToolConstants.FEATURE_TYPE_MODULE),fmForm.isFixAll());
				FeatureManagerCheckDBUtil.deleteFeaturesByNames(fmForm.getFMEDuplicatesList(FMToolConstants.FEATURE_TYPE_FEATURE),fmForm.isFixAll());
				FeatureManagerCheckDBUtil.deleteFieldsByNames(fmForm.getFMEDuplicatesList(FMToolConstants.FEATURE_TYPE_FIELD),fmForm.isFixAll());

				FeatureManagerCheckDBUtil.deleteModulesByNames(fmForm.getFMEProblemList(FMToolConstants.FEATURE_TYPE_MODULE),fmForm.isFixAll());
				FeatureManagerCheckDBUtil.deleteFeaturesByNames(fmForm.getFMEProblemList(FMToolConstants.FEATURE_TYPE_FEATURE),fmForm.isFixAll());
				FeatureManagerCheckDBUtil.deleteFieldsByNames(fmForm.getFMEProblemList(FMToolConstants.FEATURE_TYPE_FIELD),fmForm.isFixAll());
				
				//TODO
			} else if (fmForm.getFixValueType().equals(FMToolConstants.FEATURE_TYPE_MODULE)){
				FeatureManagerCheckDBUtil.deleteModulesByNames(new String[]{fmForm.getFixValue()},fmForm.isFixAll());
			} else if (fmForm.getFixValueType().equals(FMToolConstants.FEATURE_TYPE_FEATURE)){
				FeatureManagerCheckDBUtil.deleteFeaturesByNames(new String[]{fmForm.getFixValue()},fmForm.isFixAll());
			} else if (fmForm.getFixValueType().equals(FMToolConstants.FEATURE_TYPE_FIELD)){
				FeatureManagerCheckDBUtil.deleteFieldsByNames(new String[]{fmForm.getFixValue()},fmForm.isFixAll());
			}
		}
		
//		return show( mapping,  form, request,  response);
		return mapping.findForward("show");
		
	}
	
	public ActionForward sourceAjax(ActionMapping mapping, ActionForm form,HttpServletRequest request, HttpServletResponse response) throws java.lang.Exception {
		FeatureManagerCheckForm fmForm = (FeatureManagerCheckForm)form;

        JSONObject json = new JSONObject();
        json.put("identifier", "fme_get_source");
        
        String type = (String)request.getParameter("fmeType");
        String name = (String)request.getParameter("fmeName");
        json.put("title", name);
        
        
        AmpObjectVisibility fmeObj = null;
        
        if (type.equalsIgnoreCase(FMToolConstants.FEATURE_TYPE_MODULE)){
        	fmeObj = FeaturesUtil.getModuleVisibility(name);
        } else if (type.equalsIgnoreCase(FMToolConstants.FEATURE_TYPE_FEATURE)){
        	fmeObj = FeaturesUtil.getFeatureVisibility(name);
        } else if (type.equalsIgnoreCase(FMToolConstants.FEATURE_TYPE_FIELD)){
        	fmeObj = FeaturesUtil.getFieldVisibility(name);
        }
        
		if (fmeObj != null && fmeObj.getSources()!= null){
			JSONArray logItems = new JSONArray();
	        for (Iterator iterator = fmeObj.getSources().iterator(); iterator.hasNext();) {
				AmpFeatureSource source = (AmpFeatureSource) iterator.next();
				
				JSONObject jsonActivity = new JSONObject();
				jsonActivity.put("source", source.getName());
				logItems.add(jsonActivity);
			}
			json.put("items", logItems);
		}
        
		response.setContentType("text/json-comment-filtered");
		OutputStreamWriter outputStream =  null;
        
        try {
            outputStream = new OutputStreamWriter( response.getOutputStream(),"UTF-8");
            outputStream.write(json.toString());
        } finally {
            if (outputStream != null) {
                outputStream.close();
            }
        }		
		
		return null;
	}
	
	public ActionForward clearSource(ActionMapping mapping, ActionForm form,HttpServletRequest request, HttpServletResponse response) throws java.lang.Exception {
		FeatureManagerCheckForm fmForm = (FeatureManagerCheckForm)form;

        String type = (String)request.getParameter("fmeType");
        String name = (String)request.getParameter("fmeName");
        if (name != null && type != null){
            AmpObjectVisibility fmeObj = null;
	        if (type.equalsIgnoreCase(FMToolConstants.FEATURE_TYPE_MODULE)){
	        	fmeObj = FeaturesUtil.getModuleVisibility(name);
	    		FeatureManagerCheckDBUtil.clearModuleSource(fmeObj.getId());
	        } else if (type.equalsIgnoreCase(FMToolConstants.FEATURE_TYPE_FEATURE)){
	        	fmeObj = FeaturesUtil.getFeatureVisibility(name);
	    		FeatureManagerCheckDBUtil.clearFeatureSource(fmeObj.getId());
	        } else if (type.equalsIgnoreCase(FMToolConstants.FEATURE_TYPE_FIELD)){
	        	fmeObj = FeaturesUtil.getFieldVisibility(name);
	    		FeatureManagerCheckDBUtil.clearFieldSource(fmeObj.getId());
	        } else if (type.equalsIgnoreCase("allFME")){
	        	FeatureManagerCheckDBUtil.clearAllSource();
	        }
        }

        
		
		return mapping.findForward("show");
	}
	
}
