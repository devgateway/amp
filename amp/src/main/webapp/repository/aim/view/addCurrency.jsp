<%@ page pageEncoding="UTF-8" %>
<%@ taglib uri="/taglib/struts-bean" prefix="bean" %>
<%@ taglib uri="/taglib/struts-logic" prefix="logic" %>
<%@ taglib uri="/taglib/struts-tiles" prefix="tiles" %>
<%@ taglib uri="/taglib/struts-html" prefix="html" %>
<%@ taglib uri="/taglib/digijava" prefix="digi" %>
<%@ taglib uri="/taglib/jstl-core" prefix="c" %>

<digi:instance property="aimAddCurrencyForm" />
<digi:form action="/addCurrency.do" method="post">

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
							<digi:trn key="aim:clickToViewCurrencyManager">Click here to view Currency Manager</digi:trn>
						</c:set>
						<digi:link href="/currencyManager.do" styleClass="comment" title="${translation}" >
						<digi:trn key="aim:currencyManager">
						Currency Manager
						</digi:trn>
						</digi:link>&nbsp;&gt;&nbsp;
						<digi:trn key="aim:addCurrency">Add Currency</digi:trn>	
					</td>
					<!-- End navigation -->
				</tr>
				<tr>
					<td height=16 valign="center" width=571><span class=subtitle-blue>
						<digi:trn key="aim:currencyManager">
						Currency Manager
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
														<td height=20 bgColor=#dddddb align="center" colspan="5"><B>
															<digi:trn key="aim:addCurrency">Add Currency</digi:trn></B>
														</td>
														<!-- end header -->
													</tr>
													<!-- Page Logic -->
													<tr>
														<td><digi:errors /></td>
													</tr>
													<tr>
														<td>
															<table width="100%" border="0" cellspacing="4">
																<tr>
																	<td width="30%" align="right">
																		<digi:trn key="aim:currencyCode">Currency Code</digi:trn></td>
																	<td width="30%" align="left">
																		<html:text property="currencyCode" size="10" /></td></tr>
																<tr>
																	<td width="30%" align="right">
																		<digi:trn key="aim:countryName">Country Name</digi:trn></td>
																	<td width="30%" align="left">
																		<html:text property="countryName" size="20" /></td></tr>
																<tr>
																	<td width="30%" align="right">
																		<digi:trn key="aim:exchangeRate">Exchange Rate for USD</digi:trn></td>
																	<td width="30%" align="left">
																		<html:text property="exchangeRate" size="10" /></td></tr>
																<tr>
																	<td colspan="2" width="60%">	
																		<table width="100%" cellspacing="5">
																			<tr>
																				<td width="50%" align="right">
																					<html:submit value="Save" styleClass="dr-menu" /></td>
																				<td width="50%" align="left">
																					<html:reset value="Cancel" styleClass="dr-menu" onclick="javascript:history.go(-1)" /></td>
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
											<td background="module/aim/images/corner-r.gif" height="17" width=17>&nbsp;
												
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
												<digi:img src="module/aim/images/arrow-014E86.gif" width="15" height="10"/>
												<c:set var="translation">
													<digi:trn key="aim:clickToViewCurrencyManager">Click here to view Currency Manager</digi:trn>
												</c:set>
												<digi:link href="/currencyManager.do" title="${translation}" >
													<digi:trn key="aim:currencyManager">
														Currency Manager
													</digi:trn>
												</digi:link>
											</td>
										</tr>
										<tr>
											<td class="inside">
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





