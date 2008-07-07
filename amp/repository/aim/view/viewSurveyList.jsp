<%@ page pageEncoding="UTF-8" %>

<%@ taglib uri="/taglib/struts-bean" prefix="bean" %>

<%@ taglib uri="/taglib/struts-logic" prefix="logic" %>

<%@ taglib uri="/taglib/struts-tiles" prefix="tiles" %>

<%@ taglib uri="/taglib/struts-html" prefix="html" %>

<%@ taglib uri="/taglib/struts-nested" prefix="nested" %>

<%@ taglib uri="/taglib/digijava" prefix="digi" %>

<%@ taglib uri="/taglib/jstl-core" prefix="c" %>
<%@ taglib uri="/taglib/fieldVisibility" prefix="field" %>
<%@ taglib uri="/taglib/featureVisibility" prefix="feature" %>
<%@ taglib uri="/taglib/moduleVisibility" prefix="module" %>


<script language="JavaScript" type="text/javascript" src="<digi:file src="module/aim/scripts/common.js"/>"></script>

<script language="Javascript">

<!--

	function move(cntr) {

		var sid = "survey[" + cntr + "].surveyId";

		var val = (document.aimSurveyForm.elements)[sid].value;



		if (val != "-1") {

			<digi:context name="survey" property="context/module/moduleinstance/viewSurvey.do" />

			document.aimSurveyForm.action = "<%=survey %>";

			document.aimSurveyForm.surveyId.value = val;

			document.aimSurveyForm.target = "_self" ;

			document.aimSurveyForm.submit();

		}

		else

			return false;

	}



function fnEditProject(id)

{

	<digi:context name="addUrl" property="context/module/moduleinstance/editActivity.do" />

   document.aimSurveyForm.action = "<%=addUrl%>~pageId=1~step=1~action=edit~surveyFlag=true~activityId=" + id;

	document.aimSurveyForm.target = "_self";

   document.aimSurveyForm.submit();

}



function preview(id)

{



	<digi:context name="addUrl" property="context/module/moduleinstance/viewActivityPreview.do" />

   document.aimSurveyForm.action = "<%=addUrl%>~pageId=2~activityId=" + id;

	document.aimSurveyForm.target = "_self";

   document.aimSurveyForm.submit();

}

function previewLogframe(id)
{
    <digi:context name="addUrl" property="context/module/moduleinstance/editActivity.do" />
	var url ="<%=addUrl%>~pageId=1~step=1~action=edit~surveyFlag=true~logframepr=true~activityId=" + id + "~actId=" + id;
	openURLinWindow(url,650,500);
}

function projectFiche(id)
{
	<digi:context name="ficheUrl" property="context/module/moduleinstance/projectFicheExport.do" />
	window.open ( "<%=ficheUrl%>~ampActivityId=" + id,"<digi:trn key="aim:projectFiche">Project Fiche</digi:trn>");
}


-->

</script>



<digi:instance property="aimSurveyForm" />

<digi:form action="/viewSurveyList.do" method="post">



<%--

<html:hidden property="ampActivityId" />

<html:hidden property="tabIndex" />

<input type="hidden" name="surveyId" value=""> --%>



<TABLE cellSpacing=0 cellPadding=0 align="center" vAlign="top" border=0 width="100%">

	<TR>

		<TD vAlign="top" align="center">

			<!-- contents -->



			<TABLE width="99%" cellSpacing=0 cellPadding=0 vAlign="top" align="center" bgcolor="#f4f4f4" class="box-border-nopadding">

				<TR>

					<TD bgcolor="#f4f4f4">

						<TABLE width="100%" cellSpacing=3 cellPadding=3 vAlign="top" align="center" bgcolor="#f4f4f4">

							<TR bgColor=#f4f4f2>

      	      			<TD align=left>


										<TR>

											<TD align="left">

												<SPAN class=crumb>

													<c:set var="translation">

														<digi:trn key="aim:clickToViewAESurveys">Click here to view Aid Effectiveness Surveys</digi:trn>

													</c:set>

													<jsp:useBean id="urlParams" type="java.util.Map" class="java.util.HashMap" />

													<c:set target="${urlParams}" property="ampActivityId" value="${aimSurveyForm.ampActivityId}" />

													<c:set target="${urlParams}" property="tabIndex" value="${aimSurveyForm.tabIndex}" />

													<digi:link href="/viewSurveyList.do" name="urlParams" styleClass="comment" title="${translation}" >

													<digi:trn key="aim:viewAESurveys">Aid Effectiveness Surveys</digi:trn>

													</digi:link>&nbsp;&gt;&nbsp;

													<digi:trn key="aim:actOverview">Overview</digi:trn>

												</SPAN>

											</TD>

											<TD align="right">&nbsp;

												

											</TD>

											<TD align=right>

											<module:display name="Previews" parentModule="PROJECT MANAGEMENT">
												<feature:display name="Preview Activity" module="Previews">
													<field:display feature="Preview Activity" name="Preview Button">
														<input type="button" value="<digi:trn key="aim:physical:preview">Preview</digi:trn>" class="dr-menu"
															onclick="preview(<c:out value="${aimSurveyForm.ampActivityId}"/>)">
													</field:display>
												</feature:display>
											</module:display>
											<module:display name="Previews" parentModule="PROJECT MANAGEMENT">
												<feature:display name="Edit Activity" module="Previews">
													<field:display feature="Edit Activity" name="Edit Activity Button">
														<input type="button" value="<digi:trn key="aim:physical:edit">Edit</digi:trn>" class="dr-menu"
															onclick="fnEditProject(<c:out value="${aimSurveyForm.ampActivityId}"/>)">
													</field:display>
												</feature:display>
											</module:display>
											<module:display name="Previews" parentModule="PROJECT MANAGEMENT">
												<feature:display name="Logframe" module="Previews">
													<field:display name="Logframe Preview Button" feature="Logframe" >
														<input type="button" value="Preview Logframe" class="dr-menu"	onclick="previewLogframe(<c:out value="${aimSurveyForm.ampActivityId}"/>)">
													</field:display>
												</feature:display>
											</module:display>

											<module:display name="Previews" parentModule="PROJECT MANAGEMENT">
												<feature:display name="Project Fiche" module="Previews">
													<field:display name="Project Fiche Button" feature="Project Fiche" >
														<input type='button' value='<digi:trn key="aim:projectFiche">Project Fiche</digi:trn>' class='dr-menu'
															onclick='projectFiche(<c:out value="${aimSurveyForm.ampActivityId}"/>)'>
													</field:display>
												</feature:display>
											</module:display>


											</TD>

										</TR>

									</TABLE>

								</TD>

							</TR>

							<TR bgColor=#f4f4f2>

								<TD vAlign="top" align="center" width="750">

									<TABLE width="98%" cellPadding=0 cellSpacing=0 vAlign="top" align="center" bgColor=#f4f4f2>

										<TR>

											<TD width="750" bgcolor="#F4F4F2" height="17">

												<TABLE border="0" cellpadding="0" cellspacing="0" bgcolor="#F4F4F2" height="17">

                        							<TR bgcolor="#F4F4F2" height="17">

                          	  							<TD bgcolor="#C9C9C7" class="box-title">&nbsp;&nbsp;

															<digi:trn key="aim:viewAESurveysList">Aid Effectiveness Survey List</digi:trn>

							  							</TD>

	                          							<TD background="module/aim/images/corner-r.gif" height=17 width=17>&nbsp;

                                						 

							  							</TD>

													</TR>

												</TABLE>

											</TD>

										</TR>

										<TR>

											<TD width="750" bgcolor="#F4F4F2" align="center" class="box-border-nopadding">

												<TABLE width="100%"  border="0" cellpadding="4" cellspacing="1">

                 									<TR bgcolor="#DDDDDB" >

                 									<%--

	                        							<TD width="212"><digi:trn key="aim:orgFundingId">Org Funding ID</digi:trn></TD>

				                    					<TD width="204"><digi:trn key="aim:organization">Organization</digi:trn></TD>

									    				<TD width="114"><digi:trn key="aim:termAssist">Term Assist</digi:trn></TD>

	                         	   						<TD width="173"><digi:trn key="aim:surveyYear">Survey Year</digi:trn></TD>

	                         	   					--%>

                                                    <TD width="172"><digi:trn key="aim:aeSurvey">Aid Effectiveness Survey</digi:trn></TD>
                                                    <TD width="210"><digi:trn key="aim:donorOrganization">Donor Organization</digi:trn></TD>
                                                    <TD width="200"><digi:trn key="aim:pointOfDeliveryDonor">Point of delivery donor</digi:trn></TD>
													</TR>

												<nested:empty name="aimSurveyForm" property="survey">

			                    					<TR valign="top">

													<TD align="center" colspan="7" width="742"><span class="note"><digi:trn key="aim:activityNofunding">No funding for this activity</digi:trn> !</span></TD>

			                    					</TR>

			                    				</nested:empty>

			                    				<nested:notEmpty name="aimSurveyForm" property="survey">

													<nested:iterate name="aimSurveyForm" property="survey" id="surveyFund" indexId="cntr"

														     		type="org.digijava.module.aim.helper.SurveyFunding">

													<TR valign="top" bgcolor="#f4f4f2">

														<c:set target="${urlParams}" property="surveyId" value="${surveyFund.surveyId}" />

														<c:set target="${urlParams}" property="reset" value="true" />

														<c:set var="translation">

															<digi:trn key="aim:clickToViewAESurvey">Click here to view Aid Effectiveness Survey</digi:trn>

														</c:set>

														<TD width="162">

															<digi:link href="/viewSurvey.do" name="urlParams" styleClass="comment" title="${translation}" >

																AES-<%=cntr.intValue()+1%></digi:link>

														</TD>

						               					<TD width="210"><nested:write name="surveyFund" property="fundingOrgName" /></TD>
                                                        <TD width="200"><nested:write name="surveyFund" property="deliveryDonorName" /></TD>

													<%--

					    	           					<TD width="212">

					    	           						<nested:write name="surveyFund" property="fundingId" />

						               					</TD>

					                  					<TD width="204"><nested:write name="surveyFund" property="fundingOrgName" /></TD>

							                			<TD align="left" width="114"><nested:write name="surveyFund" property="termAssist"/></TD>

							                			<TD align="left" width="173">

							                   				<nested:select property="surveyId"  onchange='<%="move(" + cntr + ")" %>'>

																<html:option value="-1">-- Select Year --</html:option>

															<nested:notEmpty name="surveyFund" property="survey">

																<nested:optionsCollection name="surveyFund" property="survey"

																						  value="surveyId" label="year" />

															</nested:notEmpty>

															</nested:select>

							                			</TD>

							                		--%>

												    </TR>

													</nested:iterate>

												</nested:notEmpty>

												</TABLE>

											</TD>

										</TR>

									</TABLE>

								</TD>

							</TR>

						</TABLE>

					</TD>

				</TR>

			</TABLE>

		</TD>

	</TR>

</TABLE>

</digi:form>




