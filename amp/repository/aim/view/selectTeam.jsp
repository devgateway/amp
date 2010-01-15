<%@ page pageEncoding="UTF-8" %>
<%@ taglib uri="/taglib/struts-bean" prefix="bean" %>
<%@ taglib uri="/taglib/struts-logic" prefix="logic" %>
<%@ taglib uri="/taglib/struts-tiles" prefix="tiles" %>
<%@ taglib uri="/taglib/struts-html" prefix="html" %>
<%@ taglib uri="/taglib/digijava" prefix="digi" %>
<%@ taglib uri="/taglib/jstl-core" prefix="c" %>

<digi:instance property="aimLoginForm" />


<script language="JavaScript" type="text/javascript" src="/repository/aim/view/scripts/jSortTable.js"></script>




<table width="100%" valign="top" align="left" cellpadding=0 cellSpacing=0 border=0>
<tr><td width="100%" valign="top" align="left">
<table bgColor=#ffffff border=0 cellPadding=0 cellSpacing=0 width=772 height="201">
	<tr>
		<td width=4>&nbsp;
		</td>
		<td align=left vAlign=top width=600><br>
			<table border=0 cellPadding=5 cellSpacing=3 width="100%">
				
			
				 <thead>
				<tr>
					<th style="text-align: left;">&nbsp;</th>
					<th style="text-align: left; padding-left:24px;">
					<a href="" onclick="this.blur(); return sortTable('offTblBdy', 1, false);" title="Team Name" style="text-decoration:none;">
						<span class="page-title">
						<digi:trn key="aim:selectTheTeam">
						Select the team you want to use in this current session</digi:trn>
						</span>
						</a>
					</th>
				</tr>	
				</thead>
				 <tr>
				 	<td></td>
				 	<td></td>
				 	
				 	<td align="center"><B><digi:trn>Set Default</digi:trn><B></td>
				 </tr>
				 <tbody id="offTblBdy">
						
				<c:forEach var="members" items="${aimLoginForm.members}">
					<tr>
						<td>
						
						</td>
						<td><IMG height=10 src="/TEMPLATE/ampTemplate/imagesSource/arrows/arrow-014E86.gif" width="15">
							<jsp:useBean id="urlParams" type="java.util.Map" class="java.util.HashMap"/>
							<c:set target="${urlParams}" property="id">
								<c:out value="${members.ampTeamMemId}"/>
							</c:set>								
								<digi:link href="/selectTeam.do" name="urlParams"><c:out value="${members.ampTeam.name}"/></digi:link>
								
						</td>
					
						<td align="center">
							<c:if  test="${members.byDefault==null}">
								<a href="/aim/myWorkspaces.do?action=default&id=${members.ampTeamMemId}">
									<img alt="Set ${members.ampTeam.name} as default " src="/repository/aim/images/grayCheckIcon.png" border="0">
								</a>
							</c:if>
							<c:if  test="${members.byDefault==false}">
								<a href="/aim/myWorkspaces.do?action=default&id=${members.ampTeamMemId}">
									<img alt="Set ${members.ampTeam.name} as default " src="/repository/aim/images/grayCheckIcon.png" border="0">
								</a>
							</c:if>
							
							<c:if  test="${members.byDefault==true}">
								<a href="/aim/myWorkspaces.do?action=default&id=${members.ampTeamMemId}&unset=true">
								<img alt="Unset ${members.ampTeam.name} as default " src="/repository/aim/images/greenCheckIcon.png" border="0">
								</a>
							</c:if>
						</td>
				
					</tr>
				</c:forEach>
				 </tbody>
			</table>
		</td>
	</tr>
</table>
</td></tr>
</table>

<script language="javascript">
$(document).ready(function() {
	sortTable('offTblBdy', 1, true);
});



</script>