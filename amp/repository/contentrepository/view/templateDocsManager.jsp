<%@ page pageEncoding="UTF-8" %>
<%@ taglib uri="/taglib/struts-bean" prefix="bean" %>
<%@ taglib uri="/taglib/struts-logic" prefix="logic" %>
<%@ taglib uri="/taglib/struts-tiles" prefix="tiles" %>
<%@ taglib uri="/taglib/struts-html" prefix="html" %>
<%@ taglib uri="/taglib/digijava" prefix="digi" %>
<%@ taglib uri="/taglib/jstl-core" prefix="c" %>

<script langauage="JavaScript">
	function onDelete() {
       	var confirmMsg="<digi:trn>Delete this Template?</digi:trn>";
        var flag = confirm(confirmMsg);
		return flag;
	}	
</script>
	
<digi:instance property="tempDocManagerForm" />
<digi:context name="digiContext" property="context" />

 <h1 class="admintitle" style="text-align:left;">Template Documents Manager</h1>
<table bgColor="#ffffff" cellPadding="0" cellSpacing="0" width="1000" align=center>
	<tr>
		<td align="left" class="r-dotted-lg" vAlign="top" width="750">
			<table cellPadding="0" cellSpacing="0" width="100%" border="0">
				<!--<tr> -->
					<!-- Start Navigation -->
					<!--<td height="33">
						<span class="crumb">
							<c:set var="translation">
								<digi:trn>Click here to goto Admin Home</digi:trn>
							</c:set>
	                         <digi:link ampModule="aim" href="/admin.do" styleClass="comment" title="${translation}" >
								<digi:trn>Admin Home</digi:trn>
							</digi:link>&nbsp;&gt;&nbsp;						
							<digi:trn>Template Docuements Manager</digi:trn>
						</span>
					</td>-->
					<!-- End navigation -->
				<!--</tr> -->
				<!--<tr>
					<td height="16" vAlign="center" width="571">
                        <span class="subtitle-blue">
                        	<digi:trn>Templates Manager</digi:trn>
                        </span>
					</td>
				</tr>-->
				<tr>
					<td height="16" vAlign="middle" width="571">
						<digi:errors />
					</td>
				</tr>
				<tr>
					<td noWrap width="100%" vAlign="top">
					<table width="100%" cellspacing="1" cellSpacing="1" border="0">
					<tr><td noWrap width="750" vAlign="top">
						<table cellPadding="1" cellSpacing="1" width="100%" valign="top">
							<tr bgColor="#ffffff">
								<td vAlign="top" width="100%">
									<table width="100%" cellspacing="1" cellpadding="1" valign="top" align="left">
										<tr><td bgColor="#c7d4db" class="box-title" height="30" align="center">
											<!-- Table title -->
											<b style="font-size:12px;"><digi:trn>Templates</digi:trn></b>
											<!-- end table title -->
										</td></tr>
										<tr><td>
											<table width="100%" cellspacing="1" cellpadding="4" valign="top" align="left" bgcolor="#cccccc" style="font-size:12px;"> 
													<logic:empty name="tempDocManagerForm" property="templates">
														<tr bgcolor="#ffffff">
															<td colspan="5" align="center"><b>
																<digi:trn>No templates present</digi:trn>
															</b></td>
														</tr>
													</logic:empty>
													<logic:notEmpty name="tempDocManagerForm" property="templates">
														<logic:iterate name="tempDocManagerForm" property="templates" id="template">
														<tr>
															<td bgcolor="#ffffff">
																${template.name}
															</td>
															<td bgcolor="#ffffff" width="65" align="center">
																<jsp:useBean id="urlParams1" type="java.util.Map" class="java.util.HashMap"/>
																<c:set target="${urlParams1}" property="templateId">
																	<bean:write name="template" property="id" />
																</c:set>
																<c:set var="translation">
																	<digi:trn>Click here to Edit Template</digi:trn>
																</c:set>
																[ <digi:link href="/tempDocManager.do?actType=editTemplateDocument" name="urlParams1" title="${translation}" >
																	<digi:trn>Edit</digi:trn>
																</digi:link> ]
															</td>
															<td bgcolor="#ffffff" width="75" align="center">
																<jsp:useBean id="urlParams2" type="java.util.Map" class="java.util.HashMap"/>
																<c:set target="${urlParams2}" property="templateId">
																	<bean:write name="template" property="id" />
																</c:set>																
																<c:set var="translation">
																	<digi:trn>Click here to Delete Template</digi:trn>
																</c:set>
																[ <digi:link href="/tempDocManager.do?actType=deleteTemplateDocument" name="urlParams2" title="${translation}" onclick="return onDelete()">
																	<digi:trn>	Delete</digi:trn>
																</digi:link> ]
															</td>															
														</tr>
														</logic:iterate>	
													</logic:notEmpty>
													<!-- end page logic -->
											</table>
										</td></tr>
									</table>

								</td>
							</tr>
						</table>
					</td>

					<td noWrap width="100%" vAlign="top">
						<table align="center" cellPadding="0" cellSpacing="0" width="90%" border="0">
							<tr>
								<td>
									<!-- Other Links -->
									<table cellPadding="0" cellSpacing="0" width="100">
										<tr>
											<td bgColor="#c9c9c7" class="box-title">
												<b style="font-size:12px; padding-left:5px;">
													<digi:trn key="aim:otherLinks">
														Other links
													</digi:trn>
												</b>
											</td>
											<td background="ampModule/aim/images/corner-r.gif" height="17" width="17"></td>
										</tr>
									</table>
								</td>
							</tr>
							<tr>
								<td bgColor="#ffffff">
									<table cellPadding="5" cellSpacing="1" width="100%" class="inside">
										<tr>
											<td class="inside">
												<digi:img src="ampModule/aim/images/arrow-014E86.gif" width="15" height="10"/>
												<c:set var="translation">
													<digi:trn>Click here to goto Admin Home</digi:trn>
												</c:set>
												<digi:link href="/admin.do" title="${translation}" ampModule="aim">
													<digi:trn>Admin Home</digi:trn>
												</digi:link>
											</td>
										</tr>
										<tr>
											<td class="inside">
												<digi:img src="ampModule/aim/images/arrow-014E86.gif" width="15" height="10"/>
												<c:set var="translation">
													<digi:trn>Click here to Add New Template</digi:trn>
												</c:set>
												<digi:link href="/tempDocManager.do?actType=addTemplateDocument" title="${translation}" >
													<digi:trn>Add Template</digi:trn>
												</digi:link>
											</td>
										</tr>																
										<!-- end of other links -->
									</table>
								</td>
							</tr>
						</table>
					</td></tr>
					</table>
					</td>
				</tr>				
			</table>
		</td>
	</tr>
</table>

