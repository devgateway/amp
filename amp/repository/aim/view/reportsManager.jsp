<%@ page pageEncoding="UTF-8" %>
<%@ taglib uri="/taglib/struts-bean" prefix="bean" %>
<%@ taglib uri="/taglib/struts-logic" prefix="logic" %>
<%@ taglib uri="/taglib/struts-tiles" prefix="tiles" %>
<%@ taglib uri="/taglib/struts-html" prefix="html" %>
<%@ taglib uri="/taglib/digijava" prefix="digi" %>
<%@ taglib uri="/taglib/jstl-core" prefix="c" %>

<digi:errors/>
<digi:instance property="aimReportsForm" />
<digi:context name="digiContext" property="context" />


<!--  AMP Admin Logo -->
<jsp:include page="teamPagesHeader.jsp"  />
<!-- End of Logo -->

<table bgColor=#ffffff cellpadding="0" cellspacing="0" width=772>
	<tr>
		<td class=r-dotted-lg width=14>&nbsp;</td>
		<td align=left class=r-dotted-lg valign="top" width=750>
			<table cellPadding=5 cellspacing="0" width="100%">
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
						<digi:trn key="aim:reportsManager">
						Reports Manager
						</digi:trn>
					</td>
					<!-- End navigation -->
				</tr>
				<tr>
					<td height=16 valign="center" width=571><span class=subtitle-blue>
						<digi:trn key="aim:reportsManager">
						Reports Manager
						</digi:trn>
						</span>
					</td>
				</tr>
				<tr>
					<td noWrap width="100%" vAlign="top">
					<table width="100%" cellspacing="1" cellspacing="1">
					<tr><td noWrap width=600 vAlign="top">
						<table bgColor=#ffffff cellpadding="0" cellspacing="0" class=box-border-nopadding width="100%">
							<tr bgColor=#f4f4f2>
								<td vAlign="top" width="100%">&nbsp;
									
								</td>
							</tr>
							<tr bgColor=#f4f4f2>
								<td valign="top">
									<table align="center" bgColor=#f4f4f2 cellpadding="0" cellspacing="0" width="90%" border="0">	
										<tr>
											<td bgColor=#ffffff class=box-border>
												<table border="0" cellpadding="1" cellspacing="1" class=box-border width="100%">
									
													<tr bgColor=#dddddb>
														<!-- header -->
														<td bgColor=#dddddb height="20" align="center" colspan="5"><B>
															<digi:trn key="aim:reportList">
															Report List
															</digi:trn></B>
														</td>
														<!-- end header -->
													</tr>
													<!-- Page Logic -->
	<logic:empty name="aimReportsForm" property="reports">
		<tr>
			<td>
				<b>
					<digi:trn key="aim:noReportsPresent">
						No reports present
					</digi:trn>
				</b>
			</td>
		</tr>
	</logic:empty>
	<logic:notEmpty name="aimReportsForm" property="reports">
		<tr>
			<td>
				<table width="100%" border="0" bgColor=#f4f4f2>
					<tr>
						<td colspan="3">
							<b>
							<digi:trn key="aim:report">
							Report
							</digi:trn>
							</b>
						</td>
					</tr>
					<logic:iterate name="aimReportsForm" property="reports" id="reports" 
					type="org.digijava.ampModule.aim.dbentity.AmpReports">
					<tr>
																		<td height=1 colspan="5" bgcolor="#FFFFFF">
																		</td>
																	</tr>
					<tr>
						<td width="85%">
							<bean:write name="reports" property="name"/>
						</td>
						<td align="center">
							<jsp:useBean id="urlParams" type="java.util.Map" class="java.util.HashMap"/>
							<c:set target="${urlParams}" property="id">
								<bean:write name="reports" property="ampReportId" />
							</c:set>
							<c:set target="${urlParams}" property="action" value="edit" />
							<c:set var="translation">
								<digi:trn key="aim:clickToEditReport">Click here to Edit Report</digi:trn>
							</c:set>
							[ <digi:link href="/getReports.do" name="urlParams" title="${translation}" >
								<digi:trn key="aim:reportManagerEdit">Edit</digi:trn>
							</digi:link> ]
						</td>
						<td align="center">
							<c:set target="${urlParams}" property="action" value="delete" />
							<c:set var="translation">
								<digi:trn key="aim:clickToDeleteReport">Click here to Delete Report</digi:trn>
							</c:set>
							[ <digi:link href="/getReports.do" name="urlParams" title="${translation}" >
								<digi:trn key="aim:reportManagerDelete">Delete</digi:trn>
							</digi:link> ]
						</td>
					</tr>
					</logic:iterate>
				</table>
			</td>
		</tr>
		</logic:notEmpty>
													<!-- end page logic -->
												</table>
											</td>
										</tr>

									
									</table>
								</td>
							</tr>
							<tr><td bgColor=#f4f4f2>&nbsp;
								
							</td></tr>
						</table>
					</td>
					<td noWrap width="100%" vAlign="top">
						<table align="center" cellpadding="0" cellspacing="0" width="90%" border="0">	
							<tr>
								<td>
									<!-- Other Links -->
									<table cellpadding="0" cellspacing="0" width="120">
										<tr>
											<td bgColor=#c9c9c7 class=box-title>
												<digi:trn key="aim:otherLinks">
												Other links
												</digi:trn>
											</td>
											<td background="ampModule/aim/images/corner-r.gif" height="17" width="17">&nbsp;
												
											</td>
										</tr>
									</table>
								</td>
							</tr>
							<tr>
								<td bgColor=#ffffff class=box-border>
									<table cellPadding=5 cellspacing="1" width="100%">
										<tr>
											<td class="inside">
												<digi:img src="ampModule/aim/images/arrow-014E86.gif" width="15" height="10"/>
						<c:set var="translation">
							<digi:trn key="aim:clickToAddNewReport">Click here to Add New Report</digi:trn>
						</c:set>
						<digi:link href="/addReports.do"  title="${translation}" >
						<digi:trn key="aim:addReport">
						Add a new report
						</digi:trn>
						</digi:link>
											</td>
										</tr>
										<tr>
											<td class="inside">
												<digi:img src="ampModule/aim/images/arrow-014E86.gif" width="15" height="10"/>
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



