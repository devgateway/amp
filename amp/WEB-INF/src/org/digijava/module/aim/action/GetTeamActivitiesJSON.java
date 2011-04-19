package org.digijava.module.aim.action;

import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.log4j.Logger;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.digijava.module.aim.dbentity.AmpActivity;
import org.digijava.module.aim.dbentity.AmpApplicationSettings;
import org.digijava.module.aim.dbentity.AmpTeam;
import org.digijava.module.aim.form.TeamActivitiesForm;
import org.digijava.module.aim.helper.Constants;
import org.digijava.module.aim.helper.TeamMember;
import org.digijava.module.aim.util.DbUtil;
import org.digijava.module.aim.util.RepairDbUtil;
import org.digijava.module.aim.util.TeamUtil;

public class GetTeamActivitiesJSON
        extends Action {

    private static Logger logger = Logger.getLogger(GetTeamActivities.class);

    public ActionForward execute(ActionMapping mapping, ActionForm form,
                                 HttpServletRequest request, HttpServletResponse response) throws java.lang.Exception {

        TeamActivitiesForm taForm = (TeamActivitiesForm) form;

        Long id = null;

        try {

            boolean permitted = false;
            HttpSession session = request.getSession();
            TeamMember tm = (TeamMember) session.getAttribute("currentMember");
            if(session.getAttribute("ampAdmin") != null) {
                String key = (String) session.getAttribute("ampAdmin");
                if(key.equalsIgnoreCase("yes")) {
                    permitted = true;
                } else {
                    if(session.getAttribute("teamLeadFlag") != null) {
                        key = (String) session.getAttribute("teamLeadFlag");
                        if(key.equalsIgnoreCase("true")) {
                            permitted = true;
                        }
                    }
                }
            }
            if(!permitted) {
                return mapping.findForward("index");
            }

            int numRecords = Constants.NUM_RECORDS;
            int page = 0;
            
            RepairDbUtil.repairDb();

            if(request.getParameter("id") != null){
                id = new Long(Long.parseLong(request.getParameter("id")));
            }else{
            	id=taForm.getTeamId();
            }
            if (id!=null)
             {
                AmpApplicationSettings appSettings = DbUtil.getTeamAppSettings(id);
                if(appSettings != null) {
                    numRecords = appSettings.getDefaultRecordsPerPage().intValue();
                }
            } else if(request.getAttribute("teamId") != null) {
                id = (Long) request.getAttribute("teamId");
                AmpApplicationSettings appSettings = DbUtil.getTeamAppSettings(id);
                if(appSettings != null) {
                    numRecords = appSettings.getDefaultRecordsPerPage().intValue();
                }
            } else if(tm != null) {

                id = tm.getTeamId();
                if(tm.getAppSettings() != null)
                    numRecords = tm.getAppSettings().getDefRecsPerPage();
            }
            //taForm.setTeamId(id);

            if(id != null) {
                if(request.getParameter("page") == null) {
                    page = 1;
                } else {
                    page = Integer.parseInt(request.getParameter("page"));
                }

                page = (page < 0) ? 1 : page;
                taForm.setPage(page);

                AmpTeam ampTeam = TeamUtil.getAmpTeam(id);
                taForm.setTeamId(id);
                taForm.setTeamName(ampTeam.getName());

                if(taForm.getAllActivities() == null) {
                    Collection col = null;
                    if(tm.getTeamType() == null) {
                        if(ampTeam.getAccessType().equalsIgnoreCase(Constants.ACCESS_TYPE_MNGMT)) {
                            col = TeamUtil.getManagementTeamActivities(id,taForm.getKeyword());
                            taForm.setDonorFlag(true);
                        } else if(ampTeam.getTeamCategory() != null) {
                            col = TeamUtil.getAllTeamActivities(id,null);
                            taForm.setDonorFlag(true);
                        } else {
                            //col = TeamUtil.getAllTeamActivities(id);
                        	col = TeamUtil.getAllTeamAmpActivities(id,true,null);
                            taForm.setDonorFlag(false);
                        }
                    }else {
                        col = TeamUtil.getAllTeamActivities(id,null);
                        taForm.setDonorFlag(false);
                    }
                    logger.info("Loaded " + col.size() + " activities for the team " + ampTeam.getName());
                    taForm.setAllActivities(col);
                }

                Comparator acronymComp = new Comparator() {
                    public int compare(Object o1, Object o2) {
                        AmpActivity r1 = (AmpActivity) o1;
                        AmpActivity r2 = (AmpActivity) o2;
                        
                        return r1.getDonors().trim().toLowerCase().compareTo(r2.getDonors().trim().toLowerCase());
                    }
                };
                Comparator racronymComp = new Comparator() {
                    public int compare(Object o1, Object o2) {
                    	AmpActivity r1 = (AmpActivity) o1;
                    	AmpActivity r2 = (AmpActivity) o2;
                        return -(r1.getDonors().trim().toLowerCase().compareTo(r2.getDonors().trim().toLowerCase()));
                    }
                };

                List temp = (List) taForm.getAllActivities();
                String sort = (taForm.getSort() == null) ? null : taForm.getSort().trim();
                String sortOrder = (taForm.getSortOrder() == null) ? null : taForm.getSortOrder().trim();

                if(sort == null || "".equals(sort) || sortOrder == null || "".equals(sortOrder)) {
                    Collections.sort(temp);
                    taForm.setSort("activity");
                    taForm.setSortOrder("asc");
                } else {
                    if("activity".equals(sort)) {
                        if("asc".equals(sortOrder))
                            Collections.sort(temp);
                        else
                            Collections.sort(temp, Collections.reverseOrder());
                    } else if("donor".equals(sort)) {
                        if("asc".equals(sortOrder))
                            Collections.sort(temp, acronymComp);
                        else
                            Collections.sort(temp, racronymComp);
                    }
                }
                taForm.setAllActivities(temp);

                /*int totActivities = taForm.getAllActivities().size();
                int stIndex = ((page - 1) * numRecords) + 1;
                int edIndex = 1;
                if(page != 0) edIndex = page * numRecords;
                else edIndex = numRecords;
                edIndex = (edIndex > totActivities) ? totActivities : edIndex;
                if (stIndex<1) stIndex=1;
                Vector vect = new Vector();
                vect.addAll(taForm.getAllActivities());

                taForm.setActivities(new ArrayList());
                for(int i = (stIndex - 1); i < edIndex; i++) {
                    taForm.getActivities().add(vect.get(i));
                }

                int numPages = totActivities / numRecords;
                numPages += (totActivities % numRecords != 0) ? 1 : 0;

                Collection pages = null;
                if(numPages > 1) {
                    pages = new ArrayList();
                    for(int i = 0; i < numPages; i++) {
                        Integer pageNum = new Integer(i + 1);
                        pages.add(pageNum);
                    }
                }
                taForm.setCurrentPage(new Integer(page));
                taForm.setPages(pages);
                session.setAttribute("pageno", new Integer(page));
                taForm.setSelActivities(new Long[0]);
 				*/
                
    			JSONArray jsonArray = new JSONArray();
    			Collection col =  taForm.getAllActivities();

    			for (Iterator it = col.iterator(); it.hasNext();) {
    				AmpActivity act = (AmpActivity) it.next();
    				JSONObject jact = new JSONObject();
    				jact.put("ID", act.getAmpActivityId());
    				jact.put("name", act.getName());
    				jsonArray.add(jact);

    			}

    			response.setContentType("text/json-comment-filtered");
    			OutputStreamWriter outputStream = null;

    			try {
    				outputStream = new OutputStreamWriter(response.getOutputStream(),
    						"UTF-8");
    				outputStream.write(jsonArray.toString());
    			} finally {
    				if (outputStream != null) {
    					outputStream.close();
    				}
    			}			
            } else {
                return null;
            }
        } catch(Exception e) {
            e.printStackTrace(System.out);
        }

        return null;
    }
}
