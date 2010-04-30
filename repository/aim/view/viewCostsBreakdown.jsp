<%@ page pageEncoding="UTF-8"%>
<%@ taglib uri="/taglib/struts-bean" prefix="bean"%>
<%@ taglib uri="/taglib/struts-logic" prefix="logic"%>
<%@ taglib uri="/taglib/struts-tiles" prefix="tiles"%>
<%@ taglib uri="/taglib/struts-html" prefix="html"%>
<%@ taglib uri="/taglib/digijava" prefix="digi"%>
<%@ taglib uri="/taglib/jstl-core" prefix="c"%>
<%@ page import="org.digijava.module.aim.form.FinancingBreakdownForm"%>
<%@ taglib uri="/taglib/fmt" prefix="fmt"%>
<%@ taglib uri="/taglib/fieldVisibility" prefix="field" %>
<%@ taglib uri="/taglib/featureVisibility" prefix="feature" %>
<%@ taglib uri="/taglib/moduleVisibility" prefix="module" %>

<digi:instance property="aimMainProjectDetailsForm"/>

<script language="JavaScript" type="text/javascript" src="<digi:file src="module/aim/scripts/common.js"/>"></script>
<script type="text/javascript">

function fnEditProject(id)
{
	<digi:context name="addUrl" property="context/module/moduleinstance/editActivity.do" />
   document.location.href="<%=addUrl%>?pageId=1&action=edit&step=11&surveyFlag=true&activityId=" + id;
}

function preview(id)
{
	<digi:context name="addUrl" property="context/module/moduleinstance/viewActivityPreview.do" />
   document.location.href = "<%=addUrl%>~pageId=2~activityId=" + id;
}


function projectFiche(id)
{
	<digi:context name="ficheUrl" property="context/module/moduleinstance/projectFicheExport.do" />
	window.open ( "<%=ficheUrl%>~ampActivityId=" + id,"<digi:trn key="aim:projectFiche">Project Fiche</digi:trn>");
}

</script>


<digi:errors />


<digi:context name="digiContext" property="context" />


<TABLE cellSpacing="0" cellPadding="0" align="center" vAlign="top"
	border="0" width="100%">
	<TR>
		<TD vAlign="top" align="center"><!-- contents -->
		<TABLE width="99%" cellSpacing="0" cellPadding="0" vAlign="top"
			align="center" bgcolor="#f4f4f4" class="box-border-nopadding">
			<TR>
				<TD bgcolor="#f4f4f4">
				<TABLE width="100%" cellSpacing="3" cellPadding="3" vAlign="top"
					align="center" bgcolor="#f4f4f4">
					<TR bgColor="#f4f4f2">
						<TD align="left">
						<TABLE width="100%" cellPadding="3" cellSpacing="2" align="left"
							vAlign="top">
							<TR>
								<TD align="left"><SPAN class="crumb"> <jsp:useBean
									id="urlCostsBreakdown" type="java.util.Map"
									class="java.util.HashMap" /> <c:set
									target="${urlCostsBreakdown}" property="ampActivityId">
									<bean:write name="aimMainProjectDetailsForm" property="ampActivityId" />
								</c:set> <c:set target="${urlCostsBreakdown}"
									property="tabIndex" value="8" /> <c:set var="translation">
									<digi:trn key="aim:clickToViewCosts">Click here to view Costing</digi:trn>
								</c:set> <digi:link href="/viewProjectCostsBreakdown.do"
									name="urlCostsBreakdown" styleClass="comment"
									title="${translation}">
									<digi:trn key="aim:projectCosting">Project Costing</digi:trn>
                                                                 </digi:link>&nbsp;&gt;&nbsp;<digi:trn key="aim:actOverview">Overview</digi:trn></SPAN>
								</TD>
							</TR>
						</TABLE>
						</TD>
					</TR>
					<TR>
						<TD bgcolor="#f4f4f4">
						<TABLE width="100%" cellSpacing="3" cellPadding="3" vAlign="top"
							align="center" bgcolor="#f4f4f4">
							<TR bgColor="#f4f4f2">
								<td><bean:define id="mode" value="view" type="java.lang.String"
									toScope="request" /> <jsp:include page="viewCostsSummary.jsp"
									flush="" /></td>
							</TR>
						</TABLE>
						</TD>
					</TR>
				</TABLE>
				</TD>
			</TR>
			<TR>
				<TD>&nbsp;</TD>
			</TR>
		</TABLE>

		</TD>
	</TR>
</TABLE>
<script>
if(document.getElementById('showBottomBorder').value=='1'){
	document.write('</table><tr><td class="td_bottom1">&nbsp;</td></tr></table>&nbsp');
}
</script>