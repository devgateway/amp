/*
 * AddThemeIndicator.java 
 */

package org.digijava.module.aim.action;

import org.apache.log4j.Logger;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.digijava.module.aim.dbentity.AmpIndicator;
import org.digijava.module.aim.dbentity.AmpIndicatorValue;
import org.digijava.module.aim.dbentity.AmpTheme;
import org.digijava.module.aim.dbentity.IndicatorTheme;
import org.digijava.module.aim.form.ThemeForm;
import org.digijava.module.aim.helper.*;
import org.digijava.module.aim.util.IndicatorUtil;
import org.digijava.module.aim.util.ProgramUtil;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.*;


public class AddThemeIndicator extends Action {
    private static Logger logger = Logger.getLogger(AddThemeIndicator.class);
    private ArrayList indValues = null;

    //TODO part of the code have been changed and many things here needs to be refactored to make this normal human code! Irakli. 

    public ActionForward execute(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response)
            throws java.lang.Exception {
        HttpSession session = request.getSession();
        if (session.getAttribute("ampAdmin") == null) {
            return mapping.findForward("index");
        } else {
            String str = (String) session.getAttribute("ampAdmin");
            if (str.equals("no")) {
                return mapping.findForward("index");
            }
        }

        ThemeForm themeForm = (ThemeForm) form;

        Long id = themeForm.getThemeId();
        String event = themeForm.getEvent();
        AmpTheme theme=ProgramUtil.getThemeById(themeForm.getThemeId());
        themeForm.setThemeName(theme.getName());

        
        
        //TODO INDIC  all IF code above this should be deleted!!! already deleted..
        if (themeForm.getIndid()!=null && themeForm.getIndid().length>0 && themeForm.getThemeId()!=null && themeForm.getThemeId().longValue()>0 && "assignIndicators".equals(themeForm.getEvent())){
            
            for (int i = 0; i < themeForm.getIndid().length; i++) {             
                AmpIndicator indicator=IndicatorUtil.getIndicator(themeForm.getIndid()[i]);
                //IndicatorUtil.assignIndicatorToTheme(theme, indicator);
                theme.getIndicators().add(IndicatorUtil.assignIndicatorToTheme(theme, indicator));
            }
        }
        
        
        if (event != null) {
            if (event.equals("save")) {
                String indId = request.getParameter("indicatorId");
                AllPrgIndicators allPrgInd = new AllPrgIndicators();
                if (indId != null) {
                    if (indId.trim().length() == 0 || indId.trim().equals("0")) {
                        AmpPrgIndicator ampPrgInd = new AmpPrgIndicator();
                        ampPrgInd.setName(themeForm.getName());
                        ampPrgInd.setCode(themeForm.getCode());
                        ampPrgInd.setType(themeForm.getType());
                        ampPrgInd
                                .setCreationDate(themeForm.getCreationDate()[0]);
                        ampPrgInd.setCategory(themeForm.getCategory());
                        ampPrgInd.setNpIndicator(themeForm.isNpIndicator());
                        ampPrgInd.setDescription(themeForm
                                .getIndicatorDescription());
                        ampPrgInd.setPrgIndicatorValues(themeForm
                                .getPrgIndValues());
                        ProgramUtil.saveThemeIndicators(ampPrgInd, id);
                    } else {
                        Long indicatorId = new Long(Long.parseLong(indId));
                        allPrgInd.setIndicatorId(indicatorId);
                        allPrgInd.setName(themeForm.getName());
                        allPrgInd.setCode(themeForm.getCode());
                        allPrgInd.setType(themeForm.getType());
                        allPrgInd.setDescription(themeForm
                                .getIndicatorDescription());
                        allPrgInd
                                .setCreationDate(themeForm.getCreationDate()[0]);
                        allPrgInd.setValueType(themeForm.getValueType()[0]);
                        allPrgInd.setCategory(themeForm.getCategory());
                        allPrgInd.setNpIndicator(themeForm.isNpIndicator());
                        allPrgInd
                                .setThemeIndValues(themeForm.getPrgIndValues());
                        ProgramUtil.saveEditThemeIndicators(allPrgInd, id);
                    }
                }
            }
            if (event.equals("indValue")) {
                indValues = new ArrayList();
                AmpPrgIndicatorValue prgIndVal = null;
                if (themeForm.getPrgIndValues() != null) {
                    indValues = new ArrayList(themeForm.getPrgIndValues());
                }
                prgIndVal = getPrgIndicatorValue();
                indValues.add(prgIndVal);
                themeForm.setPrgIndValues(indValues);
                return mapping.findForward("forward");
            }

            if (event != null && event.equals("Delete")) {
                Long indId[] = themeForm.getIndicatorsId();
                if (indId != null) {
                    for (int j = 0; j < indId.length; j++) {
                        //IndicatorUtil.deleteIndtheme(indId[j]);
                        theme.getIndicators().remove(IndicatorUtil.removeConnection(indId[j]));
                        //IndicatorUtil.removeConnection(indId[j]);
                    }
                }
            }

            if (event.equals("edit")) {
                AllPrgIndicators allPrgInd = new AllPrgIndicators();
                Long indId = new Long(Long.parseLong(request
                        .getParameter("prgIndicatorId")));
                allPrgInd = ProgramUtil.getThemeIndValues(indId);
                themeForm.setIndicatorId(allPrgInd.getIndicatorId());
                themeForm.setName(allPrgInd.getName());
                themeForm.setCode(allPrgInd.getCode());
                themeForm.setType(allPrgInd.getType());
                themeForm.setIndicatorDescription(allPrgInd.getDescription());
                String dt[] = new String[0];
                dt[0] = allPrgInd.getCreationDate();
                themeForm.setCreationDate(dt);
                themeForm.setCategory(allPrgInd.getCategory());
                themeForm.setNpIndicator(allPrgInd.isNpIndicator());
                List prgIndVal = new ArrayList(allPrgInd.getThemeIndValues());
                themeForm.setPrgIndValues(prgIndVal);
                themeForm.setPrgIndicators(ProgramUtil.getThemeIndicators(id));
                return mapping.findForward("forward");
            }
            if (event.equals("overall")) {
                themeForm.setIndType("overall");
            }
        }
        themeForm.setEvent(null);
        themeForm.setIndicatorsId(null);
        themeForm.setCode(null);
        themeForm.setName(null);
        themeForm.setType(null);
        themeForm.setCreationDate(null);
        themeForm.setProgramTypeCategValId(new Long(0));
        themeForm.setIndicatorDescription(null);
        themeForm.setCategory(0);
        themeForm.setNpIndicator(false);
        themeForm.setPrgIndValues(null);
        themeForm.setIndid(null);
        themeForm.setFlag("");


        Set<IndicatorTheme> themeIndicators=theme.getIndicators();
        List<IndicatorThemeBean> indList=new ArrayList<IndicatorThemeBean>();
        
        Iterator<IndicatorTheme> it=themeIndicators.iterator();
        while(it.hasNext()){
            List<AmpPrgIndicatorValue> indValuesList=new ArrayList<AmpPrgIndicatorValue>();
            IndicatorTheme indTheme=it.next();
            if(indTheme.getValues()!=null){
                            List<AmpIndicatorValue> sortedIndicatorValues=new ArrayList(indTheme.getValues());
                            Collections.sort(sortedIndicatorValues,new IndicatorValuesComparator());
                for (AmpIndicatorValue value : sortedIndicatorValues) {
                    AmpPrgIndicatorValue bean=new AmpPrgIndicatorValue();
                    bean.setCreationDate(DateConversion.convertDateToLocalizedString(value.getValueDate()));
                    bean.setValAmount(value.getValue());
                    bean.setValueType(value.getValueType());
                    bean.setIndicatorValueId(value.getIndValId());
                    bean.setLocation(value.getLocation());
                    indValuesList.add(bean);
                }           
            }           
            IndicatorThemeBean indThemeBean=new IndicatorThemeBean();
            indThemeBean.setIndicatorThemeId(indTheme.getId());
            indThemeBean.setIndicator(indTheme.getIndicator());
            indThemeBean.setProgramIndicatorValues(indValuesList);
            
            indList.add(indThemeBean);
        }
        Collections.sort(indList, new IndicatorUtil.IndThemeBeanComparatorByIndciatorName());
        themeForm.setProgramIndicators(indList);
        
        
        
        
//      List<IndicatorTheme> indicatorList = new ArrayList<IndicatorTheme>();
//      if (themeIndicators!=null){
//          indicatorList.addAll(themeIndicators);
//          Collections.sort(indicatorList,new IndicatorUtil.IndThemeIndciatorNameComparator());
//      }       
//      themeForm.setPrgIndicators(indicatorList);

        return mapping.findForward("forward");
    }

    private AmpPrgIndicatorValue getPrgIndicatorValue() {
        AmpPrgIndicatorValue prgIndVal = new AmpPrgIndicatorValue();
        prgIndVal.setCreationDate(null);
        prgIndVal.setValAmount(null);
        prgIndVal.setValueType(1);
        return prgIndVal;
    }
}
