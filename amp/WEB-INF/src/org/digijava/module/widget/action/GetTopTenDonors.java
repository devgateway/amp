
package org.digijava.module.widget.action;
import java.text.DecimalFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.digijava.module.aim.helper.FormatHelper;
import org.digijava.module.aim.util.DbUtil;
import org.digijava.module.widget.form.WidgetTeaserForm;
import org.digijava.module.widget.helper.TopDonorGroupHelper;
import org.digijava.module.widget.util.WidgetUtil;


public class GetTopTenDonors extends Action  {
    public static final String ROOT_TAG = "DonorGroups";
    public static final String DONOR_TAG = "DonorGroup";
    private static Logger logger = Logger.getLogger(GetTopTenDonors.class);

    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
        response.setContentType("text/xml");
        WidgetTeaserForm topTenDonorGroupsForm=(WidgetTeaserForm)form;
        Integer fromYear = null;
        Integer toYear = null;

        //from year
        if ( topTenDonorGroupsForm.getSelectedFromYear()!=null && ! topTenDonorGroupsForm.getSelectedFromYear().equals("-1")){
        	fromYear = new Integer( topTenDonorGroupsForm.getSelectedFromYear());
        }else{
        	GregorianCalendar cal = new GregorianCalendar();
        	fromYear = new Integer(cal.get(Calendar.YEAR)-1);
        }
        //to year
        if ( topTenDonorGroupsForm.getSelectedToYear()!=null && ! topTenDonorGroupsForm.getSelectedToYear().equals("-1")){
        	toYear = new Integer( topTenDonorGroupsForm.getSelectedToYear());
        }else{
        	GregorianCalendar cal = new GregorianCalendar();
        	toYear = new Integer(cal.get(Calendar.YEAR));
        }
        String xml = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>";
        xml += "<" + ROOT_TAG;
        xml += " years=\"" + fromYear+"-"+toYear+"\" ";
        xml += ">";
        try {
           List<TopDonorGroupHelper> donorGroups=WidgetUtil.getTopTenDonorGroups(fromYear, toYear);
           Iterator<TopDonorGroupHelper> donorGroupIter=donorGroups.iterator();
           int order=1;
           DecimalFormat format=FormatHelper.getDecimalFormat();
           while(donorGroupIter.hasNext()){
               TopDonorGroupHelper donorGroup=donorGroupIter.next();
                xml += "<" + DONOR_TAG + " ";
                xml += " name=\"" + DbUtil.filter(donorGroup.getName())+"\" ";
                xml += " amount=\"" + format.format(donorGroup.getValue()) + "\" ";
                xml += " order=\"" + order + "\" ";
                xml += "/>";
                order++;

           }
            xml += "</" + ROOT_TAG + "> ";
            response.getWriter().print(xml);
        } catch (Exception ex) {
            logger.error(ex);

        }

              


           



        return null;
    }

}
