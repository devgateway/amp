package org.digijava.module.aim.action;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.digijava.module.aim.dbentity.AmpIndicator;
import org.digijava.module.aim.dbentity.AmpTheme;
import org.digijava.module.aim.dbentity.IndicatorTheme;
import org.digijava.module.aim.form.ThemeForm;
import org.digijava.module.aim.util.IndicatorUtil;
import org.digijava.module.aim.util.ProgramUtil;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;


public class SearchAmpIndicators extends Action {

    public ActionForward execute(ActionMapping mapping, ActionForm form,
            javax.servlet.http.HttpServletRequest request,
            javax.servlet.http.HttpServletResponse response)
            throws java.lang.Exception {

        ThemeForm eaForm = (ThemeForm) form;

        if(eaForm.getAction()!=null && eaForm.getAction().equals("clear")){

            eaForm.setSectorName("-1");
            eaForm.setKeyword("");
            eaForm.setCols(null);
            eaForm.setPagedCol(null);
            eaForm.setAction("");
            eaForm.setAlpha(null);
            eaForm.setSelectedindicatorFromPages(1);
            eaForm.setTempNumResults(10);
            eaForm.setNumResults(10);
            return mapping.findForward("forward");
            
        }
        
        
        String alpha = eaForm.getAlpha();
        List col = null;
        List colAlpha = null;
        

        if (alpha == null || alpha.trim().length() == 0) {
            col = new ArrayList();
            eaForm.setNumResults(eaForm.getTempNumResults());
            
            if(eaForm.getAction() == null){
                eaForm.setAction("selected");
                
            }
            
            /**
             * New code  AMP-2564
             */     
                    
            boolean searchCriteriaEntered=false;    
            if (!eaForm.getAction().equals("viewall")) {
                if(eaForm.getKeyword()!=null && eaForm.getKeyword().trim().length()>0 ||(eaForm.getSectorName()!=null&&!eaForm.getSectorName().equals("-1"))){
                    col = IndicatorUtil.searchIndicators(eaForm.getKeyword(),eaForm.getSectorName());
                    searchCriteriaEntered=true;
                }
            }
            if((col==null||col.size() ==0)&&!searchCriteriaEntered){
                col = IndicatorUtil.getAmpIndicator();
            }
            
            AmpTheme theme=ProgramUtil.getThemeById(eaForm.getThemeId());
            if(theme.getIndicators()!=null && theme.getIndicators().size() >0){
                for (IndicatorTheme indTheme : theme.getIndicators()) {
                    for (AmpIndicator ind : (List<AmpIndicator>)col) {
                        if(indTheme.getIndicator().getIndicatorId().equals(ind.getIndicatorId())){
                            col.remove(ind);
                            break;
                        }
                    }
                }
            }

/**
 * Old Code
 */         
//          if (!eaForm.getAction().equals("viewall")) {
//              if (eaForm.getKeyword().trim().length() != 0) {
//                  // serach for indicators based on the keyword and the
//                  // organisation type
//                  //col = ProgramUtil.searchForindicators(eaForm.getKeyword().trim(),eaForm.getSectorName());
//                  col = IndicatorUtil.searchForindicator(eaForm.getKeyword());
////                        if(col.size()==0 ){
////                            col = ProgramUtil.getThemeindicators(eaForm.getKeyword().trim());
////                         }
//              } else {
//                  // search for indicators based on organisation type only
//                  col = IndicatorUtil.searchForindicator(eaForm.getSectorName());
////                    if(col.size()==0 ){
////                        col = ProgramUtil.getThemeindicators(eaForm.getSectorName());
////                    }
//               }
//          } else if (eaForm.getKeyword().trim().length() != 0) {
//              // search based on the given keyword only.
//              col = IndicatorUtil.searchForindicator(eaForm.getKeyword().trim());
//          } else  {
//              // get all indicators since keyword field is blank and ind type field has 'ALL'.
//              col = IndicatorUtil.getAmpIndicator();
//              
//              /**
//               * Returns All project indicator.
//               */     
//              
////                coll =  MEIndicatorsUtil.getAllActivityIds();
////                 
////                 for(Iterator itr = coll.iterator(); itr.hasNext(); ) {
////                        AllActivities act = (AllActivities) itr.next();
////                            Collection<AllMEIndicators> prjIndsCol = act.getAllMEIndicators();
////                                 if(prjIndsCol != null){
////                                    List<AllMEIndicators> prjIndsList = new ArrayList<AllMEIndicators>(prjIndsCol);
////                                     for(Iterator indItr = prjIndsList.iterator(); indItr.hasNext(); ) {
////                                        AllMEIndicators tInd = (AllMEIndicators) indItr.next();
////                                        AmpThemeIndicators ind = new AmpThemeIndicators();
////                                        
////                                        ind.setAmpThemeIndId(tInd.getAmpMEIndId());
////                                        ind.setName(tInd.getName());
////                                        
////                                        col.add(ind);
////                                     }
////                                     
////                             }
////                      }
//               
//          }
            if (col != null && col.size() > 0) {

                if (eaForm.getCurrentAlpha() != null) {
                    eaForm.setCurrentAlpha(null);
                    //eaForm.setStartAlphaFlag(true);
                }
                eaForm.setStartAlphaFlag(true);

                String[] alphaArray = new String[26];
                int i = 0;
                for(char c = 'A'; c <= 'Z'; c++) {
                    Iterator itr = col.iterator();
                    while(itr.hasNext()) {
                        AmpIndicator org = (AmpIndicator) itr.next();
                        if (org.getName().toUpperCase().indexOf(c) == 0) {
                            alphaArray[i++] = String.valueOf(c);
                            break;
                        }
                    }
                }
                eaForm.setAlphaPages(alphaArray);
            } else {
                eaForm.setAlphaPages(null);
            }

        } else {
            eaForm.setCurrentAlpha(alpha);
            col=(List)eaForm.getCols();
            if (!alpha.equals("viewAll")) {
                eaForm.setStartAlphaFlag(false);
                colAlpha = new ArrayList();
                Iterator itr = col.iterator();
                while(itr.hasNext()) {
                    AmpIndicator org = (AmpIndicator) itr.next();
                    if (org.getName().toUpperCase().startsWith(alpha)) {
                        colAlpha.add(org);
                    }
                }
                
            }
            else
                eaForm.setStartAlphaFlag(true);
        }

   //     OrgProjectId hvOrgs[] = eaForm.getSelectedOrganizations();

       
    
        int stIndex = 1;
                if(eaForm.getSelectedindicatorFromPages()!=null){
                    stIndex=eaForm.getSelectedindicatorFromPages();
                }
        int edIndex = stIndex*eaForm.getNumResults();
        
        int numPages;
                 List<AmpIndicator> tempCol = new ArrayList();

        if (alpha == null || alpha.trim().length() == 0 || alpha.equals("viewAll")) {
            if (edIndex > col.size()) {
                edIndex = col.size();
            }
            
            numPages = col.size() / eaForm.getNumResults();
            numPages += (col.size() % eaForm.getNumResults() != 0) ? 1 : 0;
                        tempCol.addAll(col.subList((stIndex-1)*eaForm.getNumResults(),edIndex));
        } else {
            if (edIndex > colAlpha.size()) {
                edIndex = colAlpha.size();
            }
            
            numPages = colAlpha.size() / eaForm.getNumResults();
            numPages += (colAlpha.size() % eaForm.getNumResults() != 0) ? 1 : 0;
                        tempCol.addAll(colAlpha.subList((stIndex-1)*eaForm.getNumResults(),edIndex));
        }


                       
        Collection pages = null;

        if (numPages > 1) {
            pages = new ArrayList();
            for (int i = 0; i < numPages; i++) {
                Integer pageNum = new Integer(i + 1);
                pages.add(pageNum);
            }
        }

                eaForm.setCols(col);
        eaForm.setPagedCol(tempCol);
        
        eaForm.setPages(pages);
        eaForm.setCurrentPage(stIndex);
       
        return mapping.findForward("forward");
    }
}

