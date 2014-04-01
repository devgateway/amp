<%@ page pageEncoding="UTF-8"%>
<%@ taglib uri="/taglib/struts-bean" prefix="bean"%>
<%@ taglib uri="/taglib/struts-logic" prefix="logic"%>
<%@ taglib uri="/taglib/struts-html" prefix="html"%>
<%@ taglib uri="/taglib/digijava" prefix="digi"%>
<%@ taglib uri="/taglib/jstl-core" prefix="c"%>
<%@ taglib uri="/taglib/fieldVisibility" prefix="field"%>
<%@ taglib uri="/taglib/featureVisibility" prefix="feature"%>
<%@ taglib uri="/taglib/moduleVisibility" prefix="module"%>

<%
	if (org.dgfoundation.amp.ar.WorkspaceFilter.isActivityWithinWorkspace((Long) request.getAttribute("actId")))
		request.setAttribute("ALLOW_EDIT_ACTIVITY", true);
%>
<digi:instance property="aimEditActivityForm" />
<table width="100%" border="0" cellspacing="0" cellpadding="0">
  <tr>
    <td>
    	<input type="button" value="<digi:trn>Collapse All</digi:trn>" class="buttonx_sm" id="collapseall_1">    
   		<%  if ("true".equals(request.getParameter("messages_on"))) {%>
   		<td colspan="2" style="color:red;text-align: center">
				<c:forEach var="element" items="${aimEditActivityForm.warningMessges}">
 					<digi:trn>${element}</digi:trn>
 				</c:forEach>
			</td>
   		<%}%>
   		<td align=right>    	
		<logic:present name="ALLOW_EDIT_ACTIVITY" scope="request">
    		<c:set var="trn"><digi:trn>Version History</digi:trn></c:set>		
    		<input type="button" class="buttonx_sm" onclick="javascript:previewHistory(<%=request.getAttribute("actId")%>); return false;" value="${trn}"/> 
   			<module:display name="Previews" parentModule="PROJECT MANAGEMENT">
				<feature:display name="Edit Activity" module="Previews">
					<field:display feature="Edit Activity" name="Edit Activity Button">
						<logic:equal name="aimEditActivityForm" property="buttonText" value="edit">
							<c:set var="trn"><digi:trn>Edit</digi:trn></c:set>
							<input type="button" class="buttonx_sm" onclick="javascript:editActivity()" value="${trn}"/>
						</logic:equal>
						<logic:equal name="aimEditActivityForm" property="buttonText" value="validate">
							<c:set var="trn"><digi:trn>Validate</digi:trn></c:set>							
							<input type="button" class="buttonx_sm" onclick="javascript:editActivity()" value="${trn}"/>
						</logic:equal>
					</field:display>
				</feature:display>
			</module:display>   	
		</logic:present>
	</td>
  </tr>
</table>
