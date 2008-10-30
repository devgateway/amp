<%@ page pageEncoding="UTF-8" %>
<%@ taglib uri="/taglib/struts-bean" prefix="bean" %>
<%@ taglib uri="/taglib/struts-logic" prefix="logic" %>
<%@ taglib uri="/taglib/struts-tiles" prefix="tiles" %>
<%@ taglib uri="/taglib/struts-html" prefix="html" %>
<%@ taglib uri="/taglib/digijava" prefix="digi" %>
<%@ taglib uri="/taglib/jstl-core" prefix="c" %>

<digi:ref href="css/styles.css" type="text/css" rel="stylesheet" />




<digi:instance property="expresionbuilderForm" />
<digi:context name="digiContext" property="context" />

<!--  AMP Admin Logo -->
<jsp:include page="teamPagesHeader.jsp" flush="true" />
<!-- End of Logo -->

<digi:form action="/expresionBuilderMaganer.do" method="post">
  <table bgColor=#ffffff cellPadding=0 cellSpacing=0 width=757>
    <tr>
      <td class=r-dotted-lg width=14>&nbsp;</td>
      <td align=left class=r-dotted-lg vAlign=top>
        <table cellPadding=5 cellSpacing=0 width="100%">
          <tr>
            <!-- Start Navigation -->
            <td height=33>
              <span class=crumb>
                <c:set var="translation">
                  <digi:trn key="um:clickToViewAdmin">Click here to goto Admin Home</digi:trn>
                </c:set>
                <digi:link module="aim" href="/admin.do" styleClass="comment" title="${translation}" >
                  <digi:trn key="um:AmpAdminHome">
                  Admin Home
                  </digi:trn>
                </digi:link>&nbsp;&gt;&nbsp;
				<digi:trn key="aim:expresionbuildermanager">Expresion Builder Manager</digi:trn>
              </span>
            </td>
            <!-- End navigation -->
          </tr>
          <tr>
            <td>
              <digi:errors/> 
              <span class=subtitle-blue>
				<digi:trn key="aim:listofexpresions">List of expresions</digi:trn>
              </span>
            </td>
          </tr>

          <tr>
            <td noWrap width=967 vAlign="top" colspan="7">
              <table width="100%" cellspacing=1 cellSpacing=1>
					<tr>
						<td noWrap width=700 vAlign="top">
							<table bgColor=#ffffff cellPadding=0 cellSpacing=0 class=box-border-nopadding width="100%">
								<tr bgColor=#f4f4f2>
									<td vAlign="top" width="100%">
										&nbsp;
									</td>
								</tr>
								<tr bgColor=#f4f4f2>
									<td valign="top">
										<table align=center bgColor=#f4f4f2 cellPadding=0 cellSpacing=0 width="90%" border=0>
											<tr>
												<td bgColor=#ffffff class=box-border>
													<table border=0 cellPadding=1 cellSpacing=1 class=box-border width="100%">
														<tr bgColor=#dddddb>
															<!-- header -->
															<td bgColor=#dddddb height="20" 			align="center" colspan="5"><B>
																<digi:trn key="aim:expresions">Expresions</digi:trn>
                                                              </b>
															</td>
															<!-- end header -->
														</tr>		
 														<tr>
															<td width="100%">
																<table width="734" BORDER=1 bordercolor="cccccc" RULES=ALL FRAME=VOID  bgColor="#f4f4f2">
																		<tr>
																			<td height="30" width="220" align="center">
																				<digi:link href="/viewAllUsers.do?sortBy=name&reset=false"><b>
																					<digi:trn key="aim:expresionbuilder:expresionname">Name</digi:trn></b>
																				</digi:link>
																			</td>	
																			<td height="30" width="220" align="center">
																				<digi:link href="/viewAllUsers.do?sortBy=email&reset=false">
																					<b><digi:trn key="aim:expresionbuilder:expresionvalue">Value</digi:trn></b>
																				</digi:link>
																			</td>																																			
																		</tr>

	                                                           			<tr>
		                                                           			<td height="30">																			 
																				<digi:link href="/expresionBuilderMaganer.do?method=editExpresion">EXP1</digi:link>
																			</td>
																			<td height="30" align="center">
																			  	X + X 															  
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
															<td background="module/aim/images/corner-r.gif" 	height="17" width=17>
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
																<digi:img src="module/aim/images/arrow-014E86.gif" 	width="15" height="10"/></td>
															<td>
																<digi:link module="aim"  href="/admin.do">
																<digi:trn key="aim:AmpAdminHome">
																Admin Home
																</digi:trn>
																</digi:link>
															</td>
														</tr>
														<tr>
															<td>
																<digi:img src="module/aim/images/arrow-014E86.gif" 	width="15" height="10"/></td>
															<td>
																<digi:link module="aim" href="/expresionBuilderMaganer.do?method=addExpresion">
																<digi:trn key="aim:expresionbuilder:addexpresion">
																	Add Expresion
																</digi:trn>
																</digi:link>
															</td>
														</tr>
														<!-- end of other links -->
													</table>
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
	</table>
</digi:form>

