<%@ page pageEncoding="UTF-8" %>
<%@ taglib uri="/taglib/struts-bean" prefix="bean" %>
<%@ taglib uri="/taglib/struts-logic" prefix="logic" %>
<%@ taglib uri="/taglib/struts-tiles" prefix="tiles" %>
<%@ taglib uri="/taglib/struts-html" prefix="html" %>
<%@ taglib uri="/taglib/digijava" prefix="digi" %>
<%@ taglib uri="/taglib/jstl-core" prefix="c" %>
<div class="admin-content">
<digi:errors/>
<digi:instance property="aimAddSectorForm" />
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
						<c:set var="translation">
							<digi:trn key="aim:clickToViewSectorManager">Click here to view Sector Manager</digi:trn>
						</c:set>
						<digi:link href="/sectorManager.do" styleClass="comment" title="${translation}" >
						<digi:trn key="aim:sectorManager">
						Sector Manager
						</digi:trn>
						</digi:link>&nbsp;&gt;&nbsp;
						<digi:trn key="aim:sectorDetails">Sector Details</digi:trn>
					</td>
					<!-- End navigation -->
				</tr>
				<tr>
					<td height=16 valign="center" width=571><span class=subtitle-blue>
						<digi:trn key="aim:sectorManager">
						Sector Manager
						</digi:trn>
						</span>
					</td>
				</tr>
				<tr>
					<td noWrap width="100%" vAlign="top">
					<table width="100%" cellspacing="1" cellspacing="1">
					<tr>
						<td noWrap width=600 vAlign="top">
							<table bgColor=#ffffff cellpadding="0" cellspacing="0" class=box-border-nopadding width="100%">
								<tr bgColor=#f4f4f2>
									<td vAlign="top" width="100%">
										&nbsp;
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
															<td bgColor=#dddddb height="20" 			align="center" colspan="5"><B>
																<digi:trn key="aim:sectorDetails">Sector Details</digi:trn>
															<!-- end header -->
														</tr>
														<!-- Page Logic -->
														<tr>
															<td width="100%">	
																<table width="100%" border="0"	 bgColor=#f4f4f2>
																	<tr>
																		<td width="30%" align="right">
																		<digi:trn key="aim:sectorName">Sector Name : </digi:trn>
																		</td>
																	    <td width="30%" >
																	          <bean:write name="aimAddSectorForm" property="sectorName" />			
																			  </td>
																	</tr>
																	<tr>
																		<td width="30%" align="right">
																	       <digi:trn key="aim:sectorCode">Sector Code : </digi:trn>
																		</td>
																	    <td width="30%">
																           <bean:write name="aimAddSectorForm" property="sectorCodeOfficial" />
																		</td>
																	</tr>
																	<tr>
																		<td width="30%" align="right">
																	        <digi:trn key="aim:parentSectorName">Parent Sector : </digi:trn>
																		</td>
																		<td width="30%">
																           <bean:write name="aimAddSectorForm" property="parentSectorName" />
																		</td>
																	</tr>
																	<tr>
																		<td width="30%" align="right">
																	        <digi:trn key="aim:sectorDescription">Description : </digi:trn>
																		</td>
																		<td width="30%">
																	          <bean:write name="aimAddSectorForm" property="description" />
																		</td>
																	</tr>
																	<tr>
																		<td width="30%" align="right">
																	        <digi:trn key="aim:sectorOrganisation">Organisation</digi:trn>
																		</td>
																		<td width="30%">
																	          <bean:write name="aimAddSectorForm" property="ampOrganisation" />
																		</td>
																	</tr>
																	<tr>
		<td width="100%" align="center" colspan="2">
			<table width="90%" >
				<tr>
					<td width="30%" align="right">
						<jsp:useBean id="urlParams" type="java.util.Map" class="java.util.HashMap"/>
						<c:set target="${urlParams}" property="id">
							<bean:write name="aimAddSectorForm" property="sectorId" />
						</c:set>
						<c:set var="translation">
							<digi:trn key="aim:clickToEditCurrentSector">Click here to Edit Current Sector</digi:trn>
						</c:set>
						<digi:link href="/editSector.do" name="urlParams" title="${translation}" >
							Edit this sector
						</digi:link>					
					</td>
					<td width="30%" align="right">
						<c:set var="translation">
							<digi:trn key="aim:clickToDeleteCurrentSector">Click here to Delete Current Sector</digi:trn>
						</c:set>
						<digi:link href="/deleteSector.do" name="urlParams" title="${translation}" >
							Delete this sector
						</digi:link>					
					</td>
					<td width="30%" align="right">
						<jsp:useBean id="urlParams1" type="java.util.Map" class="java.util.HashMap"/>
						<c:set target="${urlParams1}" property="sectorId">
							<bean:write name="aimAddSectorForm" property="parentSectorId" />
						</c:set>
																
					</td>
				</tr>
			</table>
		</td>
	</tr>
																</table>	
															</td>
														</tr>
													<!-- end page logic -->
													</table>
												</td>
											</tr>
											
										</table>
									</td>
								</tr>
								<tr>
									<td bgColor=#f4f4f2>
										&nbsp;
									</td>
								</tr>
							</table>
						</td>
						<td noWrap width="100%" vAlign="top">
						</td>
					</tr>
				</table>
			</td>
		</tr>
	</table>
	</td>
	</tr>
</table>
</div>



















