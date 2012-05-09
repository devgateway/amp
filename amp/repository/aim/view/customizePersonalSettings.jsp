<%@ page pageEncoding="UTF-8" %>
<%@ taglib uri="/taglib/struts-bean" prefix="bean" %>
<%@ taglib uri="/taglib/struts-logic" prefix="logic" %>
<%@ taglib uri="/taglib/struts-tiles" prefix="tiles" %>
<%@ taglib uri="/taglib/struts-html" prefix="html" %>
<%@ taglib uri="/taglib/digijava" prefix="digi" %>

<digi:errors/>
<digi:instance property="aimUpdateAppSettingsForm" />
<digi:form action="/saveApplicationSettings.do" method="post">

<html:hidden property="type" />
<html:hidden property="appSettingsId" />

<table cellspacing="0" cellpadding="0" vAlign="top" align="left" width="100%">
<tr><td width="100%">
<jsp:include page="teamPagesHeader.jsp"  />
</td></tr>
<tr><td>
<table bgColor=#ffffff cellpadding="0" cellspacing="0" width=772>
	<tr>
		<td class=r-dotted-lg width=14>&nbsp;</td>
		<td align=left class=r-dotted-lg valign="top" width=750>
			<table cellPadding=5 cellspacing="0" width="100%">
				<tr>
					<td height=33><span class=crumb>
						<c:set var="translation">
							<digi:trn key="aim:clickToViewMyDesktop">Click here to view MyDesktop</digi:trn>
						</c:set>
						<digi:link href="/viewMyDesktop.do" styleClass="comment" title="${translation}" >
						<digi:trn key="aim:portfolio">
						Portfolio
						</digi:trn>
						</digi:link>&nbsp;&gt;&nbsp;
						<digi:trn key="aim:customizePersonalSettings">
						Customize Personal Settings
						</digi:trn>
					</td>
				</tr>
				<tr>
					<td height=16 valign="center" width=571>
						<span class=subtitle-blue>
							<digi:trn key="aim:cutsomizePersonalSettings">
								Customize Personal Settings
							</digi:trn>
						</span>
					</td>
				</tr>
				<tr>
					<td noWrap width=571 vAlign="top">
						<table bgColor=#ffffff cellpadding="0" cellspacing="0" class=box-border-nopadding width="100%" 
						valign="top" align="left">
							<tr><td bgColor=#f4f4f2>
								&nbsp;
							</td></tr>												
							<tr><td bgColor=#f4f4f2>
								<digi:errors/>
							</td></tr>						
							<tr bgColor=#f4f4f2>
								<td valign="top">
									<table align="center" bgColor=#f4f4f2 cellpadding="0" cellspacing="0" width="97%">	
										<tr bgColor=#f4f4f2>
											<td bgColor=#f4f4f2>
												<table cellpadding="0" cellspacing="0" width="100%">
													<tr bgColor=#f4f4f2>
														<td bgColor=#c9c9c7 class=box-title align="center" height="20">
															<digi:trn key="aim:customizeDefaultSettingsFor">
															Customize default settings for 
															</digi:trn>
															<bean:write name="aimUpdateAppSettingsForm" 
															property="memberName" />
														</td>
													</tr>
												</table>
											</td>
										</tr>
										<tr>
											<td bgColor=#ffffff class=box-border>
												<table width="100%" border="0" cellspacing="1" cellPadding="3" bgcolor="#dddddd">
													<logic:equal name="aimUpdateAppSettingsForm" property="updated" value="true">
													<tr>
														<td colspan="2" align="center" bgcolor="#f4f4f2">
															<font color="blue"><b>
															<digi:trn key="aim:updateToAMPComplete">
																Update to AMP Complete
															</digi:trn>
															</font></b>
														</td>
													</tr>	
													</logic:equal>
													<tr>
														<td bgcolor="#f4f4f2" align="right" width="50%">
															<digi:trn key="aim:defaultRecordPerPage">
															Number of records per page</digi:trn>	
														</td>
														<td align="left" width="50%" bgcolor="#f4f4f2">
															<html:text property="defRecsPerPage" size="5" styleClass="inp-text"/>
														</td>
													</tr>
													<tr>
														<td bgcolor="#f4f4f2"  align="right" width="50%">
															<digi:trn key="aim:defLanguage">
															Language</digi:trn>	
														</td>
														<td align="left" width="50%" bgcolor="#f4f4f2">
															<html:select property="language" styleClass="inp-text">
										                <bean:define id="languages" name="aimUpdateAppSettingsForm" 
															 property="languages" type="java.util.Collection" />
										                <html:options collection="languages" property="name" labelProperty="name" />
															</html:select>
														</td>
													</tr>
													<tr>
														<td bgcolor="#f4f4f2"  align="right" width="50%">
															<digi:trn key="aim:defPerspective">
															Perspective</digi:trn>	
														</td>
														<td align="left" width="50%" bgcolor="#f4f4f2">
															<html:select property="defPerspective" styleClass="inp-text">
																<html:option value="MOFED"><digi:trn key="aim:MOFED">Mofed</digi:trn></html:option>
																<html:option value="Donor">Donor</html:option>
															</html:select>
														</td>
													</tr>
													<tr>
														<td bgcolor="#f4f4f2"  align="right" width="50%">
															<digi:trn key="aim:defCurrency">
															Currency</digi:trn>	
														</td>
														<td align="left" width="50%" bgcolor="#f4f4f2">
															<html:select property="currencyId"  styleClass="inp-text">
															<html:option value="">------ Select Currency ------</html:option>
															<html:optionsCollection name="aimUpdateAppSettingsForm" 
															property="currencies" value="ampCurrencyId" label="currencyCode" />
															</html:select>
														</td>
													</tr>
													<tr>
														<td bgcolor="#f4f4f2"  align="right" width="50%">
															<digi:trn key="aim:defFisCalendar">Fiscal Calendar</digi:trn>	
														</td>
														<td align="left" width="50%" bgcolor="#f4f4f2">
															<html:select property="fisCalendarId" styleClass="inp-text">
															<html:option value="">------ Select Fiscal Calendar ------</html:option>
															<html:optionsCollection name="aimUpdateAppSettingsForm" 
															property="fisCalendars" value="ampFiscalCalId" label="name" />
															</html:select>
														</td>
													</tr>	

													<tr>
														<td colspan="2" align="center" bgcolor="#ffffff">
															<table cellspacing="10">
																<tr>
																	<td align="right">
																		<html:submit value="Restore" property="restore" styleClass="dr-menu"/>
																	</td>
																	<td align="center">
																		<html:submit value="Save" property="save" styleClass="dr-menu"/>
																	</td>
																	<td align="left">
																		<html:reset value="Cancel" styleClass="dr-menu" onclick="javascript:history.go(-1)"/>
																	</td>
																</tr>
															</table>	
														</td>
													</tr>
												</table>		
											
											</td>
										</tr>
									</table>
								</td>
							</tr>
							<tr><td bgColor=#f4f4f2>
								&nbsp;
							</td></tr>
						</table>			
					</td>
				</tr>
			</table>
		</td>
	</tr>
</table>
</td></tr>
</table>
</digi:form>



