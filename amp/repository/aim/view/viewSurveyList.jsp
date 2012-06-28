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

<style>

.tableEven {
	background-color:#dbe5f1;
	font-size:8pt;
	padding:2px;
}

.tableOdd {
	background-color:#FFFFFF;
	font-size:8pt;!important
	padding:2px;
}
 
.Hovered {
	background-color:#a5bcf2;
}

</style>
<script language="javascript">
function setStripsTable(tableId, classOdd, classEven) {
	var tableElement = document.getElementById(tableId);
	rows = tableElement.getElementsByTagName('tr');
	for(var i = 0, n = rows.length; i < n; ++i) {
		if(i%2 == 0)
			rows[i].className = classEven;
		else
			rows[i].className = classOdd;
	}
	rows = null;
}
function setHoveredTable(tableId, hasHeaders) {

	var tableElement = document.getElementById(tableId);
	if(tableElement){
    var className = 'Hovered',
        pattern   = new RegExp('(^|\\s+)' + className + '(\\s+|$)'),
        rows      = tableElement.getElementsByTagName('tr');

		for(var i = 0, n = rows.length; i < n; ++i) {
			rows[i].onmouseover = function() {
				this.className += ' ' + className;
			};
			rows[i].onmouseout = function() {
				this.className = this.className.replace(pattern, ' ');

			};
		}
		rows = null;
	}
	


}
</script>

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

   document.aimSurveyForm.action = "<%=addUrl%>~pageId=1~step=17~action=edit~surveyFlag=true~activityId=" + id;

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



<TABLE cellspacing="0" cellpadding="0" align="center" vAlign="top" border="0" width="100%">

	<TR>

		<TD vAlign="top" align="center">

			<!-- contents -->



			<TABLE width="99%" cellspacing="0" cellpadding="0" vAlign="top" align="center" bgcolor="#f4f4f4" class="box-border-nopadding">

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
										</TR>

									</TABLE>

								</TD>

							</TR>

							<TR bgColor=#f4f4f2>

								<TD vAlign="top" align="center" width="750">

									<TABLE width="98%" cellpadding="0" cellspacing="0" vAlign="top" align="center" bgColor=#f4f4f2>

										<TR>

											<TD width="750" bgcolor="#F4F4F2" align="center" class="box-border-nopadding">

												<TABLE width="100%"  border="0" cellpadding="4" cellspacing="1" id="dataTable">

                 									<TR >

                 									<%--

	                        							<TD width="212"><digi:trn key="aim:orgFundingId">Org Funding ID</digi:trn></TD>

				                    					<TD width="204"><digi:trn key="aim:organization">Organization</digi:trn></TD>

									    				<TD width="114"><digi:trn key="aim:termAssist">Term Assist</digi:trn></TD>

	                         	   						<TD width="173"><digi:trn key="aim:surveyYear">Survey Year</digi:trn></TD>

	                         	   					--%>

                                                    <TD width="172" bgcolor="#999999" style="color:black;font-weight:bold;"><digi:trn key="aim:aeSurvey">Aid Effectiveness Survey</digi:trn></TD>
                                                    <TD width="210" bgcolor="#999999" style="color:black;font-weight:bold;"><digi:trn key="aim:donorOrganization">Donor Organization</digi:trn></TD>
                                                    <TD width="200" bgcolor="#999999" style="color:black;font-weight:bold;"><digi:trn key="aim:pointOfDeliveryDonor">Point of delivery donor</digi:trn></TD>
													</TR>

												<nested:empty name="aimSurveyForm" property="survey">

			                    					<TR valign="top">

													<TD align="center" colspan="7" width="742"><span class="note"><digi:trn key="aim:activityNofunding">No funding for this activity</digi:trn> !</span></TD>

			                    					</TR>

			                    				</nested:empty>

			                    				<nested:notEmpty name="aimSurveyForm" property="survey">

													<nested:iterate name="aimSurveyForm" property="survey" id="surveyFund" indexId="cntr"

														     		type="org.digijava.module.aim.helper.SurveyFunding">

													<TR valign="top">

														<c:set target="${urlParams}" property="surveyId" value="${surveyFund.surveyId}" />

														<c:set target="${urlParams}" property="reset" value="true" />

														<c:set var="translation">

															<digi:trn key="aim:clickToViewAESurvey">Click here to view Aid Effectiveness Survey</digi:trn>

														</c:set>

														<TD width="162">

															<digi:link href="/viewSurvey.do" name="urlParams" styleClass="comment" title="${translation}" >

																<nested:write name="surveyFund" property="acronim" /></digi:link>

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

<br />
<script language="javascript">
setStripsTable("dataTable", "tableEven", "tableOdd");
setHoveredTable("dataTable", false);
</script>
</digi:form>
<script>
if(document.getElementById('showBottomBorder').value=='1'){
	document.write('</table><tr><td class="td_bottom1">&nbsp;</td></tr></table>&nbsp');
}
</script>