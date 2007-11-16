

package org.digijava.module.aim.action;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.digijava.module.aim.dbentity.AmpThemeIndicators;
import org.digijava.module.aim.form.ThemeForm;
import org.digijava.module.aim.helper.AmpPrgIndicatorValue;
import org.digijava.module.aim.util.ProgramUtil;

public class AddEditData
    extends Action {

    private static Logger logger = Logger.getLogger(EditAllIndicators.class);

    public ActionForward execute(ActionMapping mapping, ActionForm form,
                                 HttpServletRequest request, HttpServletResponse response) throws java.lang.Exception {



        ThemeForm themeForm = (ThemeForm) form;

        String parent=request.getParameter("parent");
        if(parent!=null){
            Long parentId=Long.valueOf(parent);
            Collection<AmpPrgIndicatorValue> indValCol=ProgramUtil.getThemeIndicatorValues(parentId);
            if(indValCol!=null){
                List<AmpPrgIndicatorValue> indValuesList=new ArrayList<AmpPrgIndicatorValue>(indValCol);
                themeForm.setPrgIndValues(indValuesList);
            }else{
                themeForm.setPrgIndValues(null);
            }
            themeForm.setParentId(parentId);
            themeForm.setCreationDate(null);
            themeForm.setValAmount(null);
            themeForm.setValueType(null);
        }
        String event = request.getParameter("event");
       


        List<AmpPrgIndicatorValue> indValues = themeForm.getPrgIndValues();
        if(indValues == null) {
            indValues = new ArrayList();
        } else if(themeForm.getCreationDate()!=null &&
                  themeForm.getValAmount()!=null &&
                  themeForm.getValueType()!=null){
            for(ListIterator iter = indValues.listIterator(); iter.hasNext(); ) {
                AmpPrgIndicatorValue item = (AmpPrgIndicatorValue) iter.next();
                
                item.setCreationDate(themeForm.getCreationDate()[iter.nextIndex() - 1]);
                item.setValAmount(themeForm.getValAmount()[iter.nextIndex() - 1]);
                item.setValueType(themeForm.getValueType()[iter.nextIndex() - 1]);
            }
        }

        if(event!=null && event.equals("addIndValue")){
            AmpPrgIndicatorValue prgIndVal = getPrgIndicatorValue();
            indValues.add(prgIndVal);
            themeForm.setPrgIndValues(indValues);
        }else if(event!=null && event.equals("delIndValue")){
            String index=request.getParameter("index");
            if(indValues!=null){
                AmpPrgIndicatorValue prgIndVal=indValues.get(Integer.valueOf(index).intValue());
                if(prgIndVal.getIndicatorValueId()==null){
                    indValues.remove(Integer.valueOf(index).intValue());
                }else{
                    prgIndVal.setIndicatorValueId(-prgIndVal.getIndicatorValueId());
                }
            }
            themeForm.setPrgIndValues(indValues);
        }else if(event!=null && event.equals("save")){
            if(themeForm.getParentId()!=null){
                for(Iterator indValIter = indValues.iterator(); indValIter.hasNext();) {
                    AmpPrgIndicatorValue indVal = (AmpPrgIndicatorValue) indValIter.next();
                    if(indVal.getIndicatorValueId()!=null && (indVal.getIndicatorValueId().longValue()<0)){
                        ProgramUtil.deletePrgIndicatorValueById(themeForm.getParentId(),-indVal.getIndicatorValueId());
                    }
                }
            }

            AmpThemeIndicators themeInd=ProgramUtil.getThemeIndicatorById(themeForm.getParentId());
            ProgramUtil.saveEditPrgIndValues(indValues,themeInd);
        }
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
