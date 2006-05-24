<%@ page pageEncoding="UTF-8" %>
<%@ taglib uri="/taglib/struts-bean" prefix="bean" %>
<%@ taglib uri="/taglib/struts-logic" prefix="logic" %>
<%@ taglib uri="/taglib/struts-tiles" prefix="tiles" %>
<%@ taglib uri="/taglib/struts-html" prefix="html" %>
<%@ taglib uri="/taglib/digijava" prefix="digi" %>
<%@ taglib uri="/taglib/jstl-core" prefix="c" %>

<script language="JavaScript">

	function toggleFeature(id) {
		<digi:context name="urlVal" property="context/module/moduleinstance/featureManager.do" />			  
		document.aimFeatureManagerForm.action = "<%= urlVal %>?toggle=true&fId="+id;
		document.aimFeatureManagerForm.submit();		
	}

</script>


<digi:instance property="aimFeatureManagerForm" />

<!--  AMP Admin Logo -->
<jsp:include page="teamPagesHeader.jsp" flush="true" />
<!-- End of Logo -->

<table bgColor=#ffffff cellPadding=0 cellSpacing=0 width=772>
	<tr>
		<td class=r-dotted-lg width=14>&nbsp;</td>
		<td align=left class=r-dotted-lg vAlign=top width=750>
			<digi:form action="/featureManager.do">
			<table cellPadding=5 cellSpacing=0 width="100%" border=0>
				<tr>
					<!-- Start Navigation -->
					<td height=33><span class=crumb>
						<bean:define id="translation">
							<digi:trn key="aim:clickToViewAdmin">Click here to goto Admin Home</digi:trn>
						</bean:define>
						<digi:link href="/admin.do" styleClass="comment" title="<%=translation%>" >
						<digi:trn key="aim:AmpAdminHome">
							Admin Home
						</digi:trn>
						</digi:link>&nbsp;&gt;&nbsp;
						<digi:trn key="aim:featureManager">
							Feature Manager
						</digi:trn>
					</td>
					<!-- End navigation -->
				</tr>
				<tr>
					<td height=16 vAlign=center width=571>
						<span class=subtitle-blue>
						<digi:trn key="aim:featureManager">
							Feature Manager
						</digi:trn>
						</span>
					</td>
				</tr>
				<tr>
					<td height=16 vAlign=center width=571>
						<html:errors />
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
										<tr><td bgColor=#d7eafd class=box-title height="20" align="center">
											<!-- Table title -->
											<digi:trn key="aim:ampFeatureList">
												AMP Features
											</digi:trn>
											<!-- end table title -->										
										</td></tr>
										<tr><td>
											<table width="100%" cellspacing=1 cellpadding=4 valign=top align=left bgcolor="#ffffff">
													<logic:notEmpty name="aimFeatureManagerForm" property="features">
														<tr><td>
															<table width="100%" cellspacing=1 cellpadding=3 bgcolor="#d7eafd">
																<logic:iterate name="aimFeatureManagerForm" property="features" id="feature"
																type="org.digijava.module.aim.dbentity.AmpFeature">	

																	<tr bgcolor="#ffffff">
																	<td width="9">
																		<c:if test="${feature.active == true}">
																			<img src= "../ampTemplate/images/bullet_green.gif" border=0>
																		</c:if>
																		<c:if test="${feature.active == false}">
																			<img src= "../ampTemplate/images/bullet_red.gif" border=0>
																		</c:if>
																	</td>																	
																	<td width="100%">
																		<bean:write name="feature" property="name"/>
																	</td>																	
																	<td align="left" height="11" align="center">
																		<c:if test="${feature.active == true}">
																			<input type="button" class="buton" value="OFF"
																			onclick="javascript:toggleFeature('<bean:write name="feature" property="ampFeatureId"/>')">
																		</c:if>
																		<c:if test="${feature.active == false}">
																			<input type="button" class="buton" value="ON"
																			onclick="javascript:toggleFeature('<bean:write name="feature" property="ampFeatureId"/>')">
																		</c:if>
																	</td>
																	</tr>
																</logic:iterate>
															</table>
														</td></tr>
													</logic:notEmpty>
													<logic:empty name="aimFeatureManagerForm" property="features">
														<tr align="center" bgcolor="#ffffff">
															<td>
																<b><digi:trn key="aim:noAMPFeaturesPresent">
																		No features persent
																	</digi:trn></b>
															</td>
														</tr>
													</logic:empty>
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
		</td>
	</tr>
</table>
