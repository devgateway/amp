<%@ page pageEncoding="UTF-8" %>
<%@ taglib uri="/taglib/struts-bean" prefix="bean" %>
<%@ taglib uri="/taglib/struts-logic" prefix="logic" %>
<%@ taglib uri="/taglib/struts-tiles" prefix="tiles" %>
<%@ taglib uri="/taglib/struts-html" prefix="html" %>
<%@ taglib uri="/taglib/digijava" prefix="digi" %>
<%@ taglib uri="/taglib/jstl-core" prefix="c" %>


<script langauage="JavaScript">
	function onDelete() {
		var flag = confirm("Delete this Scheme?");
		return flag;
	}
	function saveScheme() {
		6/20/2006
			<digi:context name="addScheme" property="context/module/moduleinstance/updateSectorSchemes.do?event=saveScheme" />
			document.aimAddSectorForm.action = "<%= addScheme%>";
			document.aimAddSectorForm.target = "_self";
			document.aimAddSectorForm.submit();
	
	}
	
	 var enterBinder	= new EnterHitBinder('addSectSchemeBtn');
</script>

<digi:instance property="aimAddSectorForm" />
<digi:context name="digiContext" property="context" />
<digi:form action="/updateSectorSchemes.do" method="post">
<!--  AMP Admin Logo -->
<jsp:include page="teamPagesHeader.jsp"  />
<!-- End of Logo -->


<table bgColor=#ffffff cellPadding=0 cellSpacing=0 width=772>
	<tr>
		<td class=r-dotted-lg width=14>&nbsp;</td>
		<td align=left class=r-dotted-lg vAlign=top width=750>
			<table cellPadding=5 cellSpacing=0 width="100%" border=0>
				<tr>
					<!-- Start Navigation -->
					<td height=33><span class=crumb>
						<c:set var="translation">
							<digi:trn key="aim:clickToViewAdmin">Click here to goto Admin Home</digi:trn>
						</c:set>
						<digi:link href="/admin.do" styleClass="comment" title="${translation}" >
						<digi:trn key="aim:AmpAdminHome">
						Admin Home
						</digi:trn>
						</digi:link>&nbsp;&gt;&nbsp;
						<digi:link href="/getSectorSchemes.do" styleClass="comment" title="${translation}" >
						<digi:trn key="aim:schemes">
						Schemes
						</digi:trn>
						</digi:link>
					</td>
					<!-- End navigation -->
				</tr>
				<tr>
					<td height=16 vAlign=center width=571><span class=subtitle-blue><digi:trn key="aim:scheme:sector">Sector Manager</digi:trn></span>
					</td>
				</tr>
				<tr>
					<td height=16 vAlign=center width=571>
						<digi:errors />
					</td>
				</tr>				
				<tr>
					<td noWrap width=100% vAlign="top">
					<table width="100%" cellspacing=1 cellSpacing=1 border=0>
					<tr><td noWrap width=600 vAlign="top">
						<table bgColor=#d7eafd cellPadding=1 cellSpacing=1 width="100%" valign="top">
							<tr bgColor=#ffffff>
								<td vAlign="top" width="100%">
									
									<table width="100%" cellspacing=1 cellpadding=1 valign=top align=left>	
										
										<tr>
											<td>	
												<table width="100%">
													<tr>
														<td>
															<digi:trn key="aim:SchemeName">Scheme Name</digi:trn><font color="red">*</font>:
														</td>
														<td>
															<html:textarea  name ="aimAddSectorForm" property="secSchemeName" rows="1" cols= "35"/> 
														</td>
													</tr>
													<tr>
														<td>
															<digi:trn key="aim:SchemeCode">Scheme Code</digi:trn><font color="red">*</font>:
														</td>
														<td>
															<html:text name ="aimAddSectorForm" property="secSchemeCode" size="5"/> 
														</td>
													</tr>
													<tr>
														<td>
															&nbsp;
														</td>
														<td >&nbsp;&nbsp;
														<input  type="button" name="addBtn" value="Save" onclick="saveScheme()" id="addSectSchemeBtn">
														<td>
													</tr>
											</table>
											</td>
											
										</tr>	
										
									
										<tr><td>
											<table width="100%" cellspacing=1 cellpadding=4 valign=top align=left bgcolor="#d7eafd">
												
													<!-- end page logic -->													
											</table>
										</td></tr>
									</table>
									
								</td>
							</tr>
						</table>
					</td>
					
					<td noWrap width=100% vAlign="top">
						<table align=center cellPadding=0 cellSpacing=0 width="90%" border=0>	
							<tr>
								<td>
									<!-- Other Links -->
									<table cellPadding=0 cellSpacing=0 width=100>
										<tr>
											<td bgColor=#c9c9c7 class=box-title>
												<digi:trn key="aim:otherLinks">
												Other links
												</digi:trn>
											</td>
											<td background="module/aim/images/corner-r.gif" height="17" width=17>
												&nbsp;
											</td>
										</tr>
									</table>
								</td>
							</tr>
							<tr>
								<td bgColor=#ffffff class=box-border>
									<table cellPadding=5 cellSpacing=1 width="100%">
										<tr>
											<td>
												<digi:img src="module/aim/images/arrow-014E86.gif" width="15" height="10"/>
												<c:set var="translation">
					                            	<digi:trn key="aim:clickToViewSectorManager">Click here to view Sector Manager</digi:trn>
					                            </c:set>
												<digi:link href="/getSectorSchemes.do" title="${translation}" >
					                            	<digi:trn key="aim:sectorManager">Sector Manager</digi:trn>
												</digi:link>
											</td>
										</tr>
										<tr>
											<td>
												<digi:img src="module/aim/images/arrow-014E86.gif" width="15" height="10"/>
												<c:set var="translation">
													<digi:trn key="aim:clickToViewAdmin">Click here to goto Admin Home</digi:trn>
												</c:set>
												<digi:link href="/admin.do" title="${translation}" >
												<digi:trn key="aim:AmpAdminHome">
												Admin Home
												</digi:trn>
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
</digi:form>



