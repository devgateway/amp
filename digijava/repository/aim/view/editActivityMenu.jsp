<%@ page pageEncoding="UTF-8" %>
<%@ taglib uri="/taglib/struts-bean" prefix="bean" %>
<%@ taglib uri="/taglib/struts-logic" prefix="logic" %>
<%@ taglib uri="/taglib/struts-tiles" prefix="tiles" %>
<%@ taglib uri="/taglib/struts-html" prefix="html" %>
<%@ taglib uri="/taglib/digijava" prefix="digi" %>
<%@ taglib uri="/taglib/jstl-core" prefix="c" %>


<script language="JavaScript">
<!--
function previewClicked() {
	document.aimEditActivityForm.step.value = "8";	  
	<digi:context name="preview" property="context/module/moduleinstance/previewActivity.do?edit=true" />
	document.aimEditActivityForm.action = "<%= preview %>";
	document.aimEditActivityForm.target = "_self";	
	document.aimEditActivityForm.submit();
}

function saveClicked() {
	<digi:context name="save" property="context/module/moduleinstance/saveActivity.do" />
	document.aimEditActivityForm.action = "<%= save %>";
	document.aimEditActivityForm.target = "_self";	
	document.aimEditActivityForm.submit();
}

function gotoStep(value) {
	document.aimEditActivityForm.step.value = value;
	<digi:context name="step" property="context/module/moduleinstance/addActivity.do" />
	document.aimEditActivityForm.action = "<%= step %>";
	document.aimEditActivityForm.target = "_self";
	document.aimEditActivityForm.submit();
}

-->
</script>

<digi:instance property="aimEditActivityForm" />

<table width="209" cellSpacing=0 cellPadding=0 vAlign="top" align="left" border=0>
<tr><td width="209" height="10" background="module/aim/images/top.gif">
</td></tr>
<tr><td>
<table width="209" cellSpacing=4 cellPadding=2 vAlign="top" align="left" 
bgcolor="#006699">
	<tr>
		<c:if test="${aimEditActivityForm.step != 1}">
		<td>
			<IMG alt=Link height=10 src="../ampTemplate/images/arrow-th-BABAB9.gif" width=15>
			<bean:define id="translation">
				<digi:trn key="aim:clickToAdd/UpdateActivityIdentificationFields">Add / Update Activity Identification fields</digi:trn>
			</bean:define>
			<a href="javascript:gotoStep(1)" class="menu" title="<%=translation%>">
				<digi:trn key="aim:identification">
				Identification</digi:trn>
			</a>
		</td>
		</c:if>	
		<c:if test="${aimEditActivityForm.step == 1}">
		<td>
			<table width="100%" cellspacing=0 cellpadding=0 valign=top align=left border=0> 
				<tr>
					<td width="10" height="19" background="module/aim/images/left-arc.gif">
					</td>
					<td bgcolor="#3399ff" height="19">
					<IMG alt=Link height=10 src="../ampTemplate/images/arrow-th-BABAB9.gif" width=15>
						<span class="textalb">
						<digi:trn key="aim:identification">
						Identification</digi:trn>
					</span>
					</td>
					<td width="10" height="19"  background="module/aim/images/right-arc.gif">
					</td>
				</tr>
			</table>
		</td>
		</c:if>				
	</tr>
	<tr>
		<c:if test="${aimEditActivityForm.step != 1}">
		<td>
			<IMG alt=Link height=10 src="../ampTemplate/images/arrow-th-BABAB9.gif" width=15>
			<bean:define id="translation">
				<digi:trn key="aim:clickToAdd/UpdateActivityPlanningFields">Add / Update Activity Planning fields</digi:trn>
			</bean:define>
			<a href="javascript:gotoStep(1)" class="menu" title="<%=translation%>">
				<digi:trn key="aim:planning">
				Planning</digi:trn>
			</a>
		</td>
		</c:if>	
		<c:if test="${aimEditActivityForm.step == 1}">
		<td>
			<table width="100%" cellspacing=0 cellpadding=0 valign=top align=left border=0> 
				<tr>
					<td width="10" height="19" background="module/aim/images/left-arc.gif">
					</td>
					<td bgcolor="#3399ff" height="19">
						<IMG alt=Link height=10 src="../ampTemplate/images/arrow-th-BABAB9.gif" width=15>
						<span class="textalb">
							<digi:trn key="aim:planning">
							Planning</digi:trn>
						</span>
					</td>
					<td width="10" height="19"  background="module/aim/images/right-arc.gif">
					</td>
				</tr>
			</table>
		</td>		
		</c:if>				
	</tr>
	<tr>
		<c:if test="${aimEditActivityForm.step != 2}">
		<td>
			<IMG alt=Link height=10 src="../ampTemplate/images/arrow-th-BABAB9.gif" width=15>
			<bean:define id="translation">
				<digi:trn key="aim:clickToAdd/UpdateLocation">Add / Update Location</digi:trn>
			</bean:define>
			<a href="javascript:gotoStep(2)" class="menu" title="<%=translation%>">
				<digi:trn key="aim:location">
				Location</digi:trn>
			</a>
		</td>
		</c:if>	
		<c:if test="${aimEditActivityForm.step == 2}">
		<td>
			<table width="100%" cellspacing=0 cellpadding=0 valign=top align=left border=0> 
				<tr>
					<td width="10" height="19" background="module/aim/images/left-arc.gif">
					</td>
					<td bgcolor="#3399ff" height="19">
						<IMG alt=Link height=10 src="../ampTemplate/images/arrow-th-BABAB9.gif" width=15>
						<span class="textalb">
							<digi:trn key="aim:location">
								Location</digi:trn>
						</span>
					</td>
					<td width="10" height="19"  background="module/aim/images/right-arc.gif">
					</td>
				</tr>
			</table>
		</td>		
		</c:if>				
	</tr>
	<tr>
		<c:if test="${aimEditActivityForm.step != 2}">
		<td>
			<IMG alt=Link height=10 src="../ampTemplate/images/arrow-th-BABAB9.gif" width=15>
			<bean:define id="translation">
				<digi:trn key="aim:clickToAdd/UpdateSectorsandSubsectors">Add / Update Sectors and Sub sectors</digi:trn>
			</bean:define>
			<a href="javascript:gotoStep(2)" class="menu" title="<%=translation%>">
				<digi:trn key="aim:sectors">
				Sectors</digi:trn>
			</a>
		</td>
		</c:if>	
		<c:if test="${aimEditActivityForm.step == 2}">
		<td>
			<table width="100%" cellspacing=0 cellpadding=0 valign=top align=left border=0> 
				<tr>
					<td width="10" height="19" background="module/aim/images/left-arc.gif">
					</td>
					<td bgcolor="#3399ff" height="19">
			<IMG alt=Link height=10 src="../ampTemplate/images/arrow-th-BABAB9.gif" width=15>
			<span class="textalb">
				<digi:trn key="aim:sectors">
				Sectors</digi:trn>
			</span>
					</td>
					<td width="10" height="19"  background="module/aim/images/right-arc.gif">
					</td>
				</tr>
			</table>
		</td>				
		</c:if>				
	</tr>
	<tr>
		<c:if test="${aimEditActivityForm.step != 2}">
		<td>
			<IMG alt=Link height=10 src="../ampTemplate/images/arrow-th-BABAB9.gif" width=15>
			<bean:define id="translation">
				<digi:trn key="aim:clickToAdd/UpdateProgram">Add / Update Program</digi:trn>
			</bean:define>
			<a href="javascript:gotoStep(2)" class="menu" title="<%=translation%>">
				<digi:trn key="aim:program">
				Program</digi:trn>
			</a>
		</td>
		</c:if>	
		<c:if test="${aimEditActivityForm.step == 2}">
		<td>
			<table width="100%" cellspacing=0 cellpadding=0 valign=top align=left border=0> 
				<tr>
					<td width="10" height="19" background="module/aim/images/left-arc.gif">
					</td>
					<td bgcolor="#3399ff" height="19">
			<IMG alt=Link height=10 src="../ampTemplate/images/arrow-th-BABAB9.gif" width=15>
			<span class="textalb">
				<digi:trn key="aim:program">
				Program</digi:trn>
			</span>
					</td>
					<td width="10" height="19"  background="module/aim/images/right-arc.gif">
					</td>
				</tr>
			</table>
		</td>						
		</c:if>				
	</tr>	
	<tr>
		<c:if test="${aimEditActivityForm.step != 3}">
		<td>
			<IMG alt=Link height=10 src="../ampTemplate/images/arrow-th-BABAB9.gif" width=15>
			<bean:define id="translation">
				<digi:trn key="aim:clickToAdd/UpdateFundingDetails">Add / Update Funding details</digi:trn>
			</bean:define>
			<a href="javascript:gotoStep(3)" class="menu" title="<%=translation%>">
				<digi:trn key="aim:funding">
				Funding</digi:trn>
			</a>
		</td>
		</c:if>	
		<c:if test="${aimEditActivityForm.step == 3}">
		<td>
			<table width="100%" cellspacing=0 cellpadding=0 valign=top align=left border=0> 
				<tr>
					<td width="10" height="19" background="module/aim/images/left-arc.gif">
					</td>
					<td bgcolor="#3399ff" height="19">
			<IMG alt=Link height=10 src="../ampTemplate/images/arrow-th-BABAB9.gif" width=15>
			<span class="textalb">
				<digi:trn key="aim:funding">
				Funding</digi:trn>
			</span>
					</td>
					<td width="10" height="19"  background="module/aim/images/right-arc.gif">
					</td>
				</tr>
			</table>
		</td>			
		</c:if>				
	</tr>

	<tr>
		<c:if test="${aimEditActivityForm.step != 4}">
		<td>
			<IMG alt=Link height=10 src="../ampTemplate/images/arrow-th-BABAB9.gif" width=15>
			<bean:define id="translation">
				<digi:trn key="aim:clickToAdd/UpdateComponents">Add / Update Components</digi:trn>
			</bean:define>
			<a href="javascript:gotoStep(4)" class="menu" title="<%=translation%>">
				<digi:trn key="aim:components">
				Components</digi:trn>
			</a>
		</td>
		</c:if>	
		<c:if test="${aimEditActivityForm.step == 4}">
		<td>
			<table width="100%" cellspacing=0 cellpadding=0 valign=top align=left border=0> 
				<tr>
					<td width="10" height="19" background="module/aim/images/left-arc.gif">
					</td>
					<td bgcolor="#3399ff" height="19">
			<IMG alt=Link height=10 src="../ampTemplate/images/arrow-th-BABAB9.gif" width=15>
			<span class="textalb">
				<digi:trn key="aim:components">
				Components</digi:trn>
			</span>
					</td>
					<td width="10" height="19"  background="module/aim/images/right-arc.gif">
					</td>
				</tr>
			</table>
		</td>				
		</c:if>
	</tr>
	<tr>
		<c:if test="${aimEditActivityForm.step != 5}">
		<td>
			<IMG alt=Link height=10 src="../ampTemplate/images/arrow-th-BABAB9.gif" width=15>
			<bean:define id="translation">
				<digi:trn key="aim:clickToAdd/UpdateDocumentsAndLinks">Add / Update the documents and links</digi:trn>
			</bean:define>
			<a href="javascript:gotoStep(5)" class="menu" title="<%=translation%>">
				<digi:trn key="aim:relatedDocuments">
				Related Documents</digi:trn>
			</a>
		</td>
		</c:if>	
		<c:if test="${aimEditActivityForm.step == 5}">
		<td>
			<table width="100%" cellspacing=0 cellpadding=0 valign=top align=left border=0> 
				<tr>
					<td width="10" height="19" background="module/aim/images/left-arc.gif">
					</td>
					<td bgcolor="#3399ff" height="19">
			<IMG alt=Link height=10 src="../ampTemplate/images/arrow-th-BABAB9.gif" width=15>
			<span class="textalb">
				<digi:trn key="aim:relatedDocuments">
				Related Documents</digi:trn>
			</span>
					</td>
					<td width="10" height="19"  background="module/aim/images/right-arc.gif">
					</td>
				</tr>
			</table>
		</td>						
		</c:if>
	</tr>
	<tr>
		<c:if test="${aimEditActivityForm.step != 6}">
		<td>
			<IMG alt=Link height=10 src="../ampTemplate/images/arrow-th-BABAB9.gif" width=15>
			<bean:define id="translation">
				<digi:trn key="aim:clickToAdd/UpdateOrganizationsInvolved">Add / Update the organizations involved</digi:trn>
			</bean:define>
			<a href="javascript:gotoStep(6)" class="menu" title="<%=translation%>">
				<digi:trn key="aim:relatedOrgs">
				Related Organizations</digi:trn>
			</a>
		</td>
		</c:if>	
		<c:if test="${aimEditActivityForm.step == 6}">
		<td>
			<table width="100%" cellspacing=0 cellpadding=0 valign=top align=left border=0> 
				<tr>
					<td width="10" height="19" background="module/aim/images/left-arc.gif">
					</td>
					<td bgcolor="#3399ff" height="19">
			<IMG alt=Link height=10 src="../ampTemplate/images/arrow-th-BABAB9.gif" width=15>
			<span class="textalb">
				<digi:trn key="aim:relatedOrgs">
				Related Organizations</digi:trn>
			</span>
					</td>
					<td width="10" height="19"  background="module/aim/images/right-arc.gif">
					</td>
				</tr>
			</table>
		</td>				
		</c:if>	
	</tr>	
	<tr>
		<c:if test="${aimEditActivityForm.step != 7}">
		<td>
			<IMG alt=Link height=10 src="../ampTemplate/images/arrow-th-BABAB9.gif" width=15>
			<bean:define id="translation">
				<digi:trn key="aim:clickToAdd/UpdateContactPersonDetails">Add / Update the contact person details</digi:trn>
			</bean:define>
			<a href="javascript:gotoStep(7)" class="menu" title="<%=translation%>">
				<digi:trn key="aim:contactInformation">
				Contact Information</digi:trn>
			</a>
		</td>
		</c:if>	
		<c:if test="${aimEditActivityForm.step == 7}">
		<td>
			<table width="100%" cellspacing=0 cellpadding=0 valign=top align=left border=0> 
				<tr>
					<td width="10" height="19" background="module/aim/images/left-arc.gif">
					</td>
					<td bgcolor="#3399ff" height="19">
			<IMG alt=Link height=10 src="../ampTemplate/images/arrow-th-BABAB9.gif" width=15>
			<span class="textalb">
				<digi:trn key="aim:contactInformation">
				Contact Information</digi:trn>
			</span>
					</td>
					<td width="10" height="19"  background="module/aim/images/right-arc.gif">
					</td>
				</tr>
			</table>
		</td>					
		</c:if>		
	</tr>	
	<tr>
		<td align="center">
		</td>
	</tr>	
	<tr>
		<td align="center">
			<input type="button" value="Preview" class="buton" onclick="previewClicked()">
		</td>
	</tr>	
	<tr>
		<td align="center">
			<input type="button" value="Save" class="buton" onclick="saveClicked()">
		</td>
	</tr>		
</table>
</td></tr>
<tr><td width="209" height="10" background="module/aim/images/bottom.gif">
</td></tr>
</table>
