package org.digijava.module.aim.action;

import org.apache.log4j.Logger;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.digijava.module.aim.helper.MTEFProjection;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

public class AddMTEFProjection extends Action{

    public static final int ADDITIONAL_AVAILABLE_YEARS  = 2;
    private static Logger logger = Logger.getLogger(AddMTEFProjection.class);
    
    private List<MTEFProjection> mtefProjections=null;
    
    private String event;
    
    public ActionForward execute(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) {
        
        throw new RuntimeException("not implemented");
//      HttpSession session = request.getSession();
//      EditActivityForm formBean = (EditActivityForm) form;
//      formBean.setReset(false);
//      event = formBean.getFunding().getEvent();
//      TeamMember teamMember = (TeamMember) session.getAttribute("currentMember");
//      
//      try {
//          formBean.getFunding().setProjections(CategoryManagerUtil.getAmpCategoryValueCollectionByKey(CategoryConstants.MTEF_PROJECTION_KEY, false));
//      } catch (Exception e) {
//          // TODO Auto-generated catch block
//          e.printStackTrace();
//      }
//      long index = formBean.getFunding().getTransIndexId();
//      String subEvent = event.substring(0,3);
//      
//      
//      MTEFProjection mp = null;
//      if (subEvent.equalsIgnoreCase("del") || subEvent.equalsIgnoreCase("add")) {
//          if (formBean.getFunding().getFundingMTEFProjections() == null || 
//                  formBean.getFunding().getFundingMTEFProjections().size() == 0) {
//              //boolean afterFiscalYearStart  = AddFunding.isAfterFiscalYearStart( null );
//              mtefProjections = new ArrayList<MTEFProjection>();
//              mp = AddFunding.getMTEFProjection(request.getSession(), 0, false, formBean.getFunding().getSelectedMTEFProjectionYear() );
//              mtefProjections.add(mp);        
//          } else {
//              int lastIndex                           = formBean.getFunding().getFundingMTEFProjections().size()-1;
//              //MTEFProjection lastMtef       = formBean.getFunding().getFundingMTEFProjections().get(lastIndex);
//              //boolean afterFiscalYearStart  = AddFunding.isAfterFiscalYearStart( firstMtef.getProjectionDate() );
//              //String [] dateSplit               = lastMtef.getProjectionDate().split("/");
//              //Integer year;
//              //try {
//              //  year    = Integer.parseInt( dateSplit[2] );
//              //}
//              //catch (Exception E) {
//              //  year    = null;
//              //  logger.error(E.getMessage());
//              //}
//              
//              mtefProjections             = formBean.getFunding().getFundingMTEFProjections();
//              if (subEvent.equals("del")) {
//                  mtefProjections.remove( (int)index );
////                    Iterator<MTEFProjection> iter   = mtefProjections.iterator();
////                    int offset;
////                    if (afterFiscalYearStart)
////                            offset  = 1;
////                    else
////                            offset  = 0;
////                    while (iter.hasNext()) {
////                        MTEFProjection proj = iter.next();
////                        proj.setProjectionDate( AddFunding.getFYDate(offset++, year) );
////                    }
//              } else { // In case we add a projection
//                  int selectedYear        = formBean.getFunding().getSelectedMTEFProjectionYear();
//                  int lastIdx         = mtefProjections.size()-1;
//                  if ( selectedYear > mtefProjections.get(lastIdx).getBaseYear() ) {
//                      mp = AddFunding.getMTEFProjection(request.getSession(), lastIdx+1, false, selectedYear );
//                      mtefProjections.add(mp);    
//                  }
//                  else
//                      for ( int i=0; i<mtefProjections.size(); i++ ) {
//                          if ( selectedYear < mtefProjections.get(i).getBaseYear() ) {
//                              mp = AddFunding.getMTEFProjection(request.getSession(), i, false, selectedYear );
//                              mtefProjections.add(i, mp);                         
//                              break;
//                          }
//                      }
//                  for ( int i=0; i<mtefProjections.size(); i++ ) {
//                      mtefProjections.get(i).setIndex(i);
//                  }
//              }
//          }
//          formBean.getFunding().setFundingMTEFProjections(mtefProjections);           
//          
//          List<KeyValue> availableMTEFProjectionYears     = 
//              AddFunding.generateAvailableMTEFProjectionYears( formBean.getFunding().getFundingMTEFProjections() ); 
//          formBean.getFunding().setAvailableMTEFProjectionYears( availableMTEFProjectionYears );
//          
//          int defaultIndex                = availableMTEFProjectionYears.size() -1 - AddMTEFProjection.ADDITIONAL_AVAILABLE_YEARS;
//          formBean.getFunding().setSelectedMTEFProjectionYear( Integer.parseInt( 
//                  availableMTEFProjectionYears.get(defaultIndex).getKey() )
//          );
//      }
//      formBean.getFunding().setEvent(null);
//      formBean.getFunding().setDupFunding(true);
//      formBean.getFunding().setFirstSubmit(false);
//      return mapping.findForward("forward");
    }
}
