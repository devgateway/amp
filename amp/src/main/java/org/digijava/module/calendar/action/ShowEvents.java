package org.digijava.module.calendar.action;

import java.io.PrintWriter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.apache.log4j.Logger;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.digijava.kernel.user.User;
import org.digijava.kernel.util.RequestUtils;
import org.digijava.module.aim.dbentity.AmpTeamMember;
import org.digijava.module.aim.helper.TeamMember;
import org.digijava.module.aim.util.TeamMemberUtil;
import org.digijava.module.calendar.entity.EventsFilter;
import org.digijava.module.calendar.util.CalendarUtil;


public class ShowEvents extends Action {
    
    private static Logger logger = Logger.getLogger("calendarEvents");

    public ActionForward execute(ActionMapping mapping,ActionForm form,HttpServletRequest request,HttpServletResponse response){
         String siteId = RequestUtils.getSite(request).getSiteId();
         String moduleInstance = RequestUtils.getRealModuleInstance(request).getInstanceName();
         User currentUser = RequestUtils.getUser(request);
         HttpSession ses = request.getSession();
         TeamMember mem = (TeamMember) ses.getAttribute("currentMember");
         AmpTeamMember member = null;
         if (mem!=null) {
             member = TeamMemberUtil.getAmpTeamMember(mem.getMemberId());
         }
            
         EventsFilter filter = new EventsFilter();
         
         String xml ="";         
         try {
             filter.setShowPublicEvents((Integer) ses.getAttribute("publicEvent"));
             
            String[] donors =  (String[]) ses.getAttribute("donor");
            filter.setSelectedDonors(donors);
            
            String[] eventTypes =  (String[]) ses.getAttribute("eventTypes");
            filter.setSelectedEventTypes(eventTypes);           
            
             
            String xmlEvents =  CalendarUtil.getCalendarEventsXml(member,filter.getShowPublicEvents(),siteId,filter.getSelectedEventTypes(),moduleInstance);   
            response.setContentType("text/xml; charset=UTF-8");               
            PrintWriter out = response.getWriter();
            xml+="<data>";
            xml+=xmlEvents;
            xml+="</data>";
            out.println(xml);
            out.flush();
            out.close();
            //clear session
            ses.removeAttribute("filter");
            ses.removeAttribute("year");
            ses.removeAttribute("month");
            ses.removeAttribute("day");
            ses.removeAttribute("calendarMode");
            ses.removeAttribute("type");
            ses.removeAttribute("print");
                
        } catch (Exception e) {
            e.printStackTrace();
        }
       return null;
    }


    private void ShowCalendarView() {
        // TODO Auto-generated method stub
        
    }


}
