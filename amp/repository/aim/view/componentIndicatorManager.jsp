<%@ page pageEncoding="UTF-8" %>
<%@ taglib uri="/taglib/struts-bean" prefix="bean" %>
<%@ taglib uri="/taglib/struts-logic" prefix="logic" %>
<%@ taglib uri="/taglib/struts-tiles" prefix="tiles" %>
<%@ taglib uri="/taglib/struts-html" prefix="html" %>
<%@ taglib uri="/taglib/digijava" prefix="digi" %>
<%@ taglib uri="/taglib/jstl-core" prefix="c" %>

<script language="JavaScript">
<!--

	function onDelete() {
  	var flag = confirm('<digi:trn jsFriendly="true" key="aim:deletethiscomponentIndicator">Are you sure you want to remove this component indicator ?</digi:trn>');
  	return flag;
}
	function addingCompIndicators()
	{
		openNewWindow(500, 300);
		<digi:context name="addCompIndicator" property="context/module/moduleinstance/addCompIndicator.do?event=add" />
		document.aimComponentsForm.action = "<%= addCompIndicator %>";
		document.aimComponentsForm.target = popupPointer.name;
		document.aimComponentsForm.submit();
		return true;
	}
	
	function editIndicator(id){
		openNewWindow(500, 300);
		<digi:context name="editCompIndicator" property="context/module/moduleinstance/addCompIndicator.do?event=edit" />
		document.aimComponentsForm.action = "<%= editCompIndicator %>&id="+id;
		document.aimComponentsForm.target = popupPointer.name;
		document.aimComponentsForm.submit();
	}
	
	function deleteIndicator(id){
		<digi:context name="delCompIndicator" property="context/module/moduleinstance/addCompIndicator.do?event=delete" />
		document.aimComponentsForm.action = "<%= delCompIndicator %>&id="+id;
		document.aimComponentsForm.target = "_self";
		document.aimComponentsForm.submit();
	}
-->	
</script>

<digi:form action="/componentIndicatorManager.do" method="post">
<digi:instance property="aimComponentsForm" />
<digi:context name="digiContext" property="context" />
<input type="hidden" name="event">
<jsp:include page="teamPagesHeader.jsp"  />

<table bgColor=#ffffff cellpadding="0" cellspacing="0" width=757>
	<tr>
		<td align=left class=r-dotted-lg valign="top" width=750>
			<table cellPadding=5 cellspacing="0" width="100%" border="0">
				<tr>
					<td height=33><span class=crumb>
						<c:set var="ToViewAdmin">
							<digi:trn key="aim:clickToViewAdmin">Click here to goto Admin Home</digi:trn>
						</c:set>
						<digi:link href="/admin.do" styleClass="comment" title="${ToViewAdmin}" >
							<digi:trn key="aim:AmpAdminHome">
								Admin Home
							</digi:trn>
						</digi:link>&nbsp;&gt;&nbsp;
						<bean:define id="translation">
							<digi:trn key="aim:clickToViewComponentManager">Click here to view Component Manager</digi:trn>
						</bean:define>
						<digi:link href="/getComponents.do" styleClass="comment" title="<%=translation%>" >
							<digi:trn key="aim:componentManager">
								Component Manager
							</digi:trn>
						</digi:link>&nbsp;&gt;&nbsp;
						<digi:trn key="aim:componentsIndicatorManager">
							Component Indicator Manager
						</digi:trn>
					</td>
				</tr>
			</table>
			<tr><td noWrap width="100%" vAlign="top">
				<table width="100%" cellspacing="1" cellspacing="1" border="0">
					<tr><td noWrap width=600 vAlign="top">
						<table bgColor=#d7eafd cellpadding="1" cellspacing="1" width="100%" valign="top">
							<tr bgColor=#ffffff><td vAlign="top" width="100%">
								<table width="100%" cellspacing="1" cellpadding="1" valign="top" align=left>
									<tr><td bgColor=#d7eafd class=box-title height="20" align="center">
										<digi:trn key="aim:componentIndicatorList">
											Component Indicator List
										</digi:trn>
									</td></tr>
									<tr><td>
										<table width="100%" cellspacing="1" cellpadding=4 valign="top" align=left bgcolor="#ffffff">
											<logic:notEmpty name="aimComponentsForm" property="compIndicators">
												<tr><td>
													<table width="100%" cellspacing="1" cellpadding=3 bgcolor="#d7eafd">
														<logic:iterate name="aimComponentsForm" property="compIndicators" id="indicators" type="org.digijava.module.aim.dbentity.AmpComponentsIndicators">
															<tr bgcolor="#ffffff">
																<td width="9">
																	<img src= "../ampTemplate/images/arrow_right.gif" border="0">	
																</td>
																<td>
																<a href="javascript:editIndicator(<bean:write name="indicators" property="ampCompIndId"/>)">
																		<bean:write name="indicators" property="name"/>
																</a>
																</td>
																<td width="100">
																	<bean:write name="indicators" property="code"/>
																</td>
																<td align="center" width="22">
																	<bean:define id="translation">
																		<digi:trn key="aim:clickToDeleteIndicator">Click here to Delete Indicator</digi:trn>
																	</bean:define>
																	<a href="javascript:deleteIndicator(<bean:write name="indicators" property="ampCompIndId" />)" onclick="return onDelete()">
																		<img src= "../ampTemplate/images/trash_12.gif" border="0">
																	</a>
																</td>
															</tr>
														</logic:iterate>
													</table>
												</td></tr>
											</logic:notEmpty>
											<logic:empty name="aimComponentsForm" property="compIndicators">
												<tr align="center" bgcolor="#ffffff"><td><b>
													<digi:trn key="aim:noComponentIndicatorsPresent">No Component indicators present</digi:trn></b>
												</td></tr>
											</logic:empty>
											<tr bgcolor="#ffffff">
												<td height="20" bgColor=#d7eafd class=box-title align="center"><B>
													<html:button styleClass="dr-menu" property="submitButton"  onclick="addingCompIndicators()">
														<digi:trn key="btn:addCompIndicator">Add a Component Indicator</digi:trn> 
													</html:button>
												</td>
											</tr>
										</table>
									</td></tr>
								</table>
							</td></tr>
						</table>
					</td></tr>
				</table>
			</td>
		</tr>
	</table>
</digi:form>
