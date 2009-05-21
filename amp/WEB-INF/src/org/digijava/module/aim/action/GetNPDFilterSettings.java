
package org.digijava.module.aim.action;

import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.digijava.module.aim.dbentity.AmpOrganisation;
import org.digijava.module.aim.form.ActivitiesForm;
import org.digijava.module.aim.util.DbUtil;
import org.digijava.module.categorymanager.dbentity.AmpCategoryValue;
import org.digijava.module.categorymanager.util.CategoryManagerUtil;

/**
 * Returns XML of Selected filters for NPD page
 *
 * @author medea
 *
 */
public class GetNPDFilterSettings extends Action{
  
    public ActionForward execute(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
        response.setContentType("text/xml");
        ActivitiesForm actForm = (ActivitiesForm) form;
        String donorNames="";
        String statuses="";
        String startYear="";
        String endYear="";
        String [] donorIds=null;
        if (actForm.getDonorIds() != null) {
            donorIds = actForm.getDonorIds().split(",");
            for (String donorId : donorIds) {
                if(donorId.equals("")){
                    break;
                }
                AmpOrganisation donor = DbUtil.getOrganisation(Long.parseLong(donorId));
                donorNames += donor.getName() + ",";

            }
            if (donorNames.length() > 0) {
                donorNames = donorNames.substring(0, donorNames.length() - 1);
            }
        }
        String [] statusIds=null;
        
        if (actForm.getStatusId() != null) {
            statusIds = actForm.getStatusId().split(",");
            for (String statusId : statusIds) {
                 if(statusId.equals("")){
                    break;
                }
                AmpCategoryValue value = CategoryManagerUtil.getAmpCategoryValueFromDb(Long.parseLong(statusId));
                statuses += value.getValue() + ",";

            }
             if (statuses.length() > 0) {
                statuses = statuses.substring(0, statuses.length() - 1);
            }
        }

       if(actForm.getStartYear()!=null){
           startYear=actForm.getStartYear().toString();
       }
       if(actForm.getEndYear()!=null){
           endYear=actForm.getEndYear().toString();
       }
       

        OutputStreamWriter outputStream = new OutputStreamWriter( response.getOutputStream(),"UTF-8");
		PrintWriter out = new PrintWriter(outputStream, true);
        String html =" <tr> " +
        " <td style=\"font-size:11px;font-family:Arial,Helvetica,sans-serif\" valign=\"top\">"
         +"<strong>"
         + "<digi:trn>Selected Filters:</digi:trn>"
         + " </strong>"
         +"</td>"
         +"</tr>"
         +"<tr>"
         +"<td><b><digi:trn>Status</digi:trn></b>: "+statuses+" </td>"
         + "</tr>"
         + "<tr>"
         + "<td><b><digi:trn>Donors</digi:trn></b>: "+donorNames+" </td>"
         +" </tr>"
         + "<tr>"
         + "<td><b><digi:trn>From</digi:trn></b>: "+startYear +" </td>"
         + "</tr>"
         +"<tr>"
          + "<td><b><digi:trn>To</digi:trn></b>: "+endYear +"</td>"
         + "</tr>";
       
        out.println(html);
		out.close();
        outputStream.close();
		return null;

    }

}
