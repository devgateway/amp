package org.digijava.module.aim.action ;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.StringTokenizer;
import java.util.Vector;
import java.text.DecimalFormat;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.digijava.module.aim.dbentity.AmpActivity;
import org.digijava.module.aim.dbentity.AmpCurrency;
import org.digijava.module.aim.dbentity.AmpSector;
import org.digijava.module.aim.dbentity.AmpReports;
import org.digijava.module.aim.dbentity.AmpTeam;
import org.digijava.module.aim.form.MyDesktopForm;
import org.digijava.module.aim.helper.AmpProject;
import org.digijava.module.aim.helper.AmpProjectDonor;
import org.digijava.module.aim.helper.Constants;
import org.digijava.module.aim.helper.DecimalToText;
import org.digijava.module.aim.helper.TeamMember;
import org.digijava.module.aim.util.DbUtil;
import org.digijava.module.aim.util.TeamUtil;


public class ViewMyDesktop extends Action
{
	private static Logger logger = Logger.getLogger(ViewMyDesktop.class) ;

	public ActionForward execute(ActionMapping mapping, ActionForm form, 
								HttpServletRequest request, HttpServletResponse response) throws java.lang.Exception
	{
	    long t1 = System.currentTimeMillis();
		HttpSession session = request.getSession();
		TeamMember teamMember=(TeamMember)session.getAttribute("currentMember");
		if(teamMember==null)
			return mapping.findForward("index");
		Long ampTeamId=teamMember.getTeamId();
		
		Long ampMemberId=teamMember.getMemberId();
		boolean teamLeadFlag = teamMember.getTeamHead();
		
		MyDesktopForm formBean = (MyDesktopForm) form ;
		
		// clear filters
		formBean.setFiscalYears(null);
		formBean.setCurrency(null);
		formBean.setDonor(null);
		formBean.setRegion(null);
		formBean.setSector(null);
		formBean.setStatus(null);
		formBean.setAmpFromYear(null);
		formBean.setAmpToYear(null);
		formBean.setFilterFlag("false");
		
		formBean.setTeamMemberId(ampMemberId);
		long t2 = System.currentTimeMillis();
		logger.debug("1. at" + (t2-t1)+ "ms");
		
		t1 = System.currentTimeMillis();
		ArrayList dbReturnSet= null ;
		Iterator iter = null ;
		Iterator iterSub = null ;
		List list = null;
		Long ampStatusId=null;
		Long ampOrgId=null;
		Long ampSectorId=null;
		String region=null;
		Long ampCalType=null;
		Long ampFromYear=null;
		Long ampToYear=null;
		Long sortField=new Long(1);
		Long sortOrder=new Long(1);
		Long ampCurrencyId=null;
		String ampCurrencyCode=null;
		Long All=new Long(0);
		String search=null;
		
		boolean flag;
		Collection ampProjects=new ArrayList();
		DecimalFormat mf = new DecimalFormat("###,###,###,###,###") ;
		ArrayList filters=null;
		double grandTotal=0.0;
		if(request.getParameter("sortOrder")!=null)
			sortOrder=new Long(request.getParameter("sortOrder"));
		if(request.getParameter("sortField")!=null)
			sortField=new Long(request.getParameter("sortField"));
		
		
		String perspective = "DN";
		t2 = System.currentTimeMillis();
		logger.debug("2. at" + (t2-t1)+ "ms");		
		
		t1 = System.currentTimeMillis();
		if(formBean.getPerspective() == null)
		{
			if (teamMember.getAppSettings() != null)
				perspective = teamMember.getAppSettings().getPerspective();
		}
		else
			perspective = formBean.getPerspective();
		if(perspective.equals("Donor"))
			perspective="DN";
		if(perspective.equals("MOFED"))
			perspective="MA";
		if(perspective.equals("DN"))
			formBean.setPerspective("Donor");
		if(perspective.equals("MA"))
			formBean.setPerspective("MOFED");

		formBean.setTeamLeadFlag(teamLeadFlag);	
		formBean.setAmpFromYears(new ArrayList());
		formBean.setAmpToYears(new ArrayList());
		formBean.setSector(new ArrayList()) ;
		formBean.setDonor(new ArrayList()) ;
		t2 = System.currentTimeMillis();
		logger.debug("3. at" + (t2-t1)+ "ms");
		
		t1 = System.currentTimeMillis();
		filters=DbUtil.getTeamPageFilters(ampTeamId,Constants.DESKTOP);
		t2 = System.currentTimeMillis();
		logger.debug("4. at" + (t2-t1)+ "ms");		
		
		
		if(filters.size()==0)
			formBean.setFilterFlag("false");		
		if(filters.size()>0)
		{
		    t1 = System.currentTimeMillis();
			formBean.setFilterFlag("true");	
			if(filters.indexOf(Constants.PERSPECTIVE)!=-1)
				formBean.setPerspectives(DbUtil.getAmpPerspective());
			t2 = System.currentTimeMillis();
			logger.debug("5.0 at" + (t2-t1)+ "ms");		
			
			t1 = System.currentTimeMillis();
			if(filters.indexOf(Constants.CALENDAR)!=-1)
				formBean.setFiscalYears(DbUtil.getAllFisCalenders());
			t2 = System.currentTimeMillis();
			logger.debug("5.1 at" + (t2-t1)+ "ms");					

			t1 = System.currentTimeMillis();
			if(filters.indexOf(Constants.YEAR_RANGE)!=-1)
			{	
				Calendar c=Calendar.getInstance();
				int year=c.get(Calendar.YEAR);
				logger.debug("Year: " + year);
				for(int i=(year-Constants.FROM_YEAR_RANGE);i<=(year+Constants.TO_YEAR_RANGE);i++)
				{
					formBean.getAmpFromYears().add(new Long(i));
					formBean.getAmpToYears().add(new Long(i));
				}
			}
			t2 = System.currentTimeMillis();
			logger.debug("5.2 at" + (t2-t1)+ "ms");					
			
			t1 = System.currentTimeMillis();			
			if(filters.indexOf(Constants.STATUS)!=-1)
				formBean.setStatus(DbUtil.getAmpStatus());
			t2 = System.currentTimeMillis();
			logger.debug("5.3 at" + (t2-t1)+ "ms");					
			
			t1 = System.currentTimeMillis();			
			if(filters.indexOf(Constants.DONORS)!=-1)
			{
				dbReturnSet=(ArrayList) TeamUtil.getAllDonorsToDesktop(ampTeamId);
				formBean.setDonor(dbReturnSet);
				/*
				iter = dbReturnSet.iterator() ;
				while ( iter.hasNext() )
				{
					AmpOrganisation ampOrganisation = (AmpOrganisation) iter.next() ;
					if(ampOrganisation.getAcronym().length()>20)
					{
						String temp=ampOrganisation.getAcronym().substring(0,20) + "...";
						ampOrganisation.setAcronym(temp);
					}
					formBean.getDonor().add(ampOrganisation);
				}*/
			}	
			t2 = System.currentTimeMillis();
			logger.debug("5.4 at" + (t2-t1)+ "ms");
			
			t1 = System.currentTimeMillis();
			if(filters.indexOf(Constants.SECTOR)!=-1)
			{
				dbReturnSet=DbUtil.getAmpSectors();
				iter = dbReturnSet.iterator() ;
				while(iter.hasNext())
				{
					AmpSector ampSector=(AmpSector) iter.next();
					if(ampSector.getName().length()>20)
					{
						String temp=ampSector.getName().substring(0,20) + "...";
						ampSector.setName(temp);
					}
					formBean.getSector().add(ampSector);
					dbReturnSet=DbUtil.getAmpSubSectors(ampSector.getAmpSectorId());
					iterSub=dbReturnSet.iterator();
					while(iterSub.hasNext())
					{
						AmpSector ampSubSector=(AmpSector) iterSub.next();
						String temp=" -- " + ampSubSector.getName();
						if(temp.length()>20)
						{
							temp=temp.substring(0,20) + "...";
							ampSubSector.setName(temp);
						}
						//logger.debug("Sub Sector:" + temp);
						ampSubSector.setName(temp);
						formBean.getSector().add(ampSubSector);
						dbReturnSet=DbUtil.getAmpSubSectors(ampSubSector.getAmpSectorId());
						Iterator iterSubSub=dbReturnSet.iterator();
						while(iterSubSub.hasNext())
						{
							AmpSector ampSubSubSector=(AmpSector) iterSubSub.next();
							temp=" ---- " + ampSubSubSector.getName();
							if(temp.length()>20)
							{
								temp=temp.substring(0,20) + "...";
								ampSubSubSector.setName(temp);
							}
							//logger.debug("Sub Sector:" + temp);
							ampSubSubSector.setName(temp);
							formBean.getSector().add(ampSubSubSector);
						}
					}
				}
			}
			t2 = System.currentTimeMillis();
			logger.debug("5.5 at" + (t2-t1)+ "ms");			

			t1 = System.currentTimeMillis();
			if(filters.indexOf(Constants.REGION)!=-1)
				formBean.setRegion(DbUtil.getAmpLocations());
			t2 = System.currentTimeMillis();
			logger.debug("5.6 at" + (t2-t1)+ "ms");
			
			t1 = System.currentTimeMillis();
			if(filters.indexOf(Constants.CURRENCY)!=-1)
				formBean.setCurrency(DbUtil.getAmpCurrency()) ;
			t2 = System.currentTimeMillis();
			logger.debug("5.7 at" + (t2-t1)+ "ms");			
		}
		else
		{
			logger.debug("no filters");
			formBean.setFilterFlag("false");
		}

		t1 = System.currentTimeMillis();
		if(formBean.getAmpStatusId()==null)
			ampStatusId=All;
		else
			ampStatusId=formBean.getAmpStatusId();

		if(formBean.getAmpOrgId()==null)
			ampOrgId=All;
		else
			ampOrgId=formBean.getAmpOrgId();

		if(formBean.getAmpSectorId()==null)
			ampSectorId=All;
		else
			ampSectorId=formBean.getAmpSectorId();

		if(formBean.getAmpLocationId()==null)
			region="All";
		else
			region=formBean.getAmpLocationId();

		if(formBean.getAmpCalType()==null)
		{
			ampCalType=teamMember.getAppSettings().getFisCalId();
			formBean.setAmpCalType(ampCalType);
		}	
		else
			ampCalType=formBean.getAmpCalType();
		
		if(formBean.getAmpFromYear()==null)
			ampFromYear=All;
		else
			ampFromYear=formBean.getAmpFromYear();

		if(formBean.getAmpToYear()==null)
			ampToYear=All;
		else
			ampToYear=formBean.getAmpToYear();
		
		t2 = System.currentTimeMillis();
		logger.debug("6. at" + (t2-t1)+ "ms");				
		
		t1 = System.currentTimeMillis();
		if(formBean.getAmpCurrencyCode()==null || formBean.getAmpCurrencyCode().equals("0"))
		{
			AmpCurrency ampCurrency=DbUtil.getAmpcurrency(teamMember.getAppSettings().getCurrencyId());
			ampCurrencyCode=ampCurrency.getCurrencyCode();
			formBean.setAmpCurrencyCode(ampCurrencyCode);
		}
		else
		{
			ampCurrencyCode=formBean.getAmpCurrencyCode();
		}
		t2 = System.currentTimeMillis();
		logger.debug("7. at" + (t2-t1)+ "ms");
		
		t1 = System.currentTimeMillis();
		search=formBean.getSearchCriteria() + " ";
		if(request.getParameter("view")!=null)
		{
			if(request.getParameter("view").equals("reset"))
			{
				formBean.setAmpStatusId(All);
				formBean.setAmpOrgId(All);
				formBean.setAmpSectorId(All);
				formBean.setAmpLocationId("All");
				ampCalType=teamMember.getAppSettings().getFisCalId();
				formBean.setAmpCalType(ampCalType);
				formBean.setAmpFromYear(All);
				formBean.setAmpToYear(All);
				AmpCurrency ampCurrency=DbUtil.getAmpcurrency(teamMember.getAppSettings().getCurrencyId());
				ampCurrencyCode=ampCurrency.getCurrencyCode();
				formBean.setAmpCurrencyCode(ampCurrencyCode);
				formBean.setSearchCriteria("");
				ampStatusId=All;
				ampOrgId=All;
				ampSectorId=All;
				region="All";
				ampFromYear=All;
				ampToYear=All;
			}
		 }
		 Collection projects = new ArrayList();
		 int page = 0;
		 t2 = System.currentTimeMillis();
		 logger.debug("8.0 at" + (t2-t1)+ "ms");	
		
		 if (request.getParameter("page") == null) 
		 {
			page = 1;
//			session.setAttribute("ampCurrency",ampCurrencyCode);
					 
			if(request.getParameter("view")!=null)
			{
				if(request.getParameter("view").equals("search"))
				{
				    t1 = System.currentTimeMillis();
					ampProjects =DbUtil.getProjectList(ampTeamId,ampMemberId,teamLeadFlag,ampStatusId,ampOrgId,ampSectorId,region,ampCalType,ampFromYear,ampToYear,ampCurrencyCode,perspective,sortOrder,sortField);
					session.setAttribute("ampProjects",ampProjects);
					t2 = System.currentTimeMillis();
					logger.debug("8.1 at" + (t2-t1)+ "ms");
					
					t1 = System.currentTimeMillis();										
					iter=ampProjects.iterator();
					while(iter.hasNext())
					{
						flag=false;
						StringTokenizer st=new StringTokenizer(search," ,");
						AmpProject project=(AmpProject) iter.next();	
						while(st.hasMoreTokens())
						{	
							String key=st.nextToken();
							if(project.getName().equalsIgnoreCase(key) || project.getName().toLowerCase().indexOf(key.toLowerCase())!=-1)
							{
								flag=true;
								 break;
							}
							if(project.getDonor()!=null)
							{
								ArrayList donor=(ArrayList)project.getDonor();
								Iterator diter=donor.iterator();
								while(diter.hasNext())
								{
									AmpProjectDonor ampProjectDonor =(AmpProjectDonor) diter.next();
									String donorName=ampProjectDonor.getDonorName();
									if(donorName.equalsIgnoreCase(key) || donorName.toLowerCase().indexOf(key.toLowerCase())!=-1)
									{
										flag=true;
										break;
									}
								}
							}
							if(project.getSector()!=null && flag==false)
							{
								ArrayList sector=(ArrayList)project.getSector();
								Iterator siter=sector.iterator();
								while(siter.hasNext())
								{
									String sectorName=(String) siter.next();
									if(sectorName.equalsIgnoreCase(key) || sectorName.toLowerCase().indexOf(key.toLowerCase())!=-1)
									{
										flag=true;
										break;
									}
								}
							}
							if(flag==false)
							{
								AmpActivity ampActivity=DbUtil.getProjectChannelOverview(project.getAmpActivityId());
								if((ampActivity.getDescription().toLowerCase().indexOf(key.toLowerCase()))>=0 )
								{
									flag=true;
									 break;
								}
							}
						 }
						 if(flag==false)
							iter.remove();
					}
					t2 = System.currentTimeMillis();
					logger.debug("8.2 at" + (t2-t1)+ "ms");					
				}
				else
				{
				    t1 = System.currentTimeMillis();
//					ampCurrencyCode = (String)session.getAttribute("ampCurrency");
					ampProjects =DbUtil.getProjectList(ampTeamId,ampMemberId,teamLeadFlag,ampStatusId,ampOrgId,ampSectorId,region,ampCalType,ampFromYear,ampToYear,ampCurrencyCode,perspective,sortOrder,sortField);
					session.setAttribute("ampProjects",ampProjects);
					t2 = System.currentTimeMillis();
					logger.debug("8.3 at" + (t2-t1)+ "ms");					
				}
			}
			else
			{
			    t1 = System.currentTimeMillis();
//				ampCurrencyCode = (String)session.getAttribute("ampCurrency");
				ampProjects =DbUtil.getProjectList(ampTeamId,ampMemberId,teamLeadFlag,ampStatusId,ampOrgId,ampSectorId,region,ampCalType,ampFromYear,ampToYear,ampCurrencyCode,perspective,sortOrder,sortField);
				session.setAttribute("ampProjects",ampProjects);
				t2 = System.currentTimeMillis();
				logger.debug("8.4 at" + (t2-t1)+ "ms");									
			}
		}
		else 
		{
		    t1 = System.currentTimeMillis();
			page = Integer.parseInt(request.getParameter("page"));
			if(session.getAttribute("ampProjects")==null)
			{
//				ampCurrencyCode = (String)session.getAttribute("ampCurrency");
				ampProjects =DbUtil.getProjectList(ampTeamId,ampMemberId,teamLeadFlag,ampStatusId,ampOrgId,ampSectorId,region,ampCalType,ampFromYear,ampToYear,ampCurrencyCode,perspective,sortOrder,sortField);
				session.setAttribute("ampProjects",ampProjects);
			}
			else
				ampProjects=(ArrayList)session.getAttribute("ampProjects");
			
			t2 = System.currentTimeMillis();
			logger.debug("8.5 at" + (t2-t1)+ "ms");												
		 }

		 t1 = System.currentTimeMillis();
		 formBean.setPage(new Integer(page));
		 int defRecsPerPage = teamMember.getAppSettings().getDefRecsPerPage();			
		 int stIndex = ((page - 1) * defRecsPerPage) + 1;
		 int edIndex = page * defRecsPerPage;
		 if (edIndex > ampProjects.size()) 
		 {
			edIndex = ampProjects.size();
		 }
		 Vector vect = new Vector();
		 vect.addAll(ampProjects);
		 for (int i = (stIndex-1);i < edIndex;i ++) 
		 {
			projects.add(vect.get(i));
		 }
		 int numPages = ampProjects.size() / defRecsPerPage;
		 numPages += (ampProjects.size() % defRecsPerPage != 0) ? 1 : 0;
		 Collection pages = null;
		 if (numPages > 1) 
		 {
			pages = new ArrayList();
			for (int i = 0;i < numPages;i ++) 
			{
			  Integer pageNum=new Integer(i+1);
			  pages.add(pageNum);
			}
		}
		t2 = System.currentTimeMillis();
		logger.debug("8.6 at" + (t2-t1)+ "ms");
		
		t1 = System.currentTimeMillis();
		formBean.setAmpProjects(projects);
		formBean.setPages(pages);
		logger.debug("Page: " + page);
		logger.debug("Number of Page: " + numPages);

		iter = ampProjects.iterator() ;
		logger.debug("Grand Total :" + grandTotal);
		while ( iter.hasNext() )
		{
			AmpProject project=(AmpProject) iter.next();
			grandTotal=grandTotal + Double.parseDouble(DecimalToText.removeCommas(project.getTotalCommited()));
		}
		formBean.setGrandTotal(mf.format(grandTotal));

		if(page==numPages)
		{
			formBean.setGrandTotalFlag("true");
		}
		t2 = System.currentTimeMillis();
		logger.debug("9. at" + (t2-t1)+ "ms");
		
		// Modified by priyajith
		// start
		// showing all team members
		t1 = System.currentTimeMillis();		
		if (teamLeadFlag==true) 
			dbReturnSet = (ArrayList)DbUtil.getAllTeamReports(ampTeamId);
		else
			dbReturnSet = DbUtil.getAllMemberReports(ampMemberId);
		
		formBean.setReportCount(new Integer(dbReturnSet.size()));
		formBean.setAmpReports(new ArrayList());
		t2 = System.currentTimeMillis();
		logger.debug("10. at" + (t2-t1)+ "ms");
		
		t1 = System.currentTimeMillis();
		if(dbReturnSet.size()>5)
		{
			iter=dbReturnSet.iterator();
			for(int i=1;i<=5;i++)
			{
				AmpReports report=(AmpReports) iter.next();
				formBean.getAmpReports().add(report);
			}
		}
		else
			formBean.getAmpReports().addAll(dbReturnSet);
		t2 = System.currentTimeMillis();
		logger.debug("11. at" + (t2-t1)+ "ms");
		
		// show all documents
		
		t1 = System.currentTimeMillis();
		dbReturnSet = (ArrayList)TeamUtil.getMemberLinks(teamMember.getMemberId());
		if (dbReturnSet.size() > 0) {
			formBean.setDocumentCount(new Integer(dbReturnSet.size()));
			formBean.setDocuments(dbReturnSet);
		}
		t2 = System.currentTimeMillis();
		logger.debug("12. at" + (t2-t1)+ "ms");		
		
		//end
					 
		t1 = System.currentTimeMillis();		
		formBean.setAmpTeamMembers(TeamUtil.getAllTeamMembersToDesktop(ampTeamId));
		formBean.setAmpCurrencyCode(ampCurrencyCode);
		formBean.setSearchCriteria("");
		t2 = System.currentTimeMillis();
		logger.debug("13. at" + (t2-t1)+ "ms");
		
		t1 = System.currentTimeMillis();		
		AmpTeam ampTeam=TeamUtil.getAmpTeam(ampTeamId);
		t2 = System.currentTimeMillis();
		logger.debug("14. at" + (t2-t1)+ "ms");
		
		t1 = System.currentTimeMillis();
		if(ampTeam.getAccessType().equals("Team"))
			formBean.setWrite(true);
		else
			formBean.setWrite(false);
		t2 = System.currentTimeMillis();
		logger.debug("15. at" + (t2-t1)+ "ms");
		
		return mapping.findForward("forward");
	}
}
