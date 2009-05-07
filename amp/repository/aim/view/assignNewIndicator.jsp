<%@ page pageEncoding="UTF-8" %>
<%@ taglib uri="/taglib/struts-bean" prefix="bean" %>
<%@ taglib uri="/taglib/struts-logic" prefix="logic" %>
<%@ taglib uri="/taglib/struts-tiles" prefix="tiles" %>
<%@ taglib uri="/taglib/struts-html" prefix="html" %>
<%@ taglib uri="/taglib/digijava" prefix="digi" %>
<%@ taglib uri="/taglib/jstl-core" prefix="c" %>
<%@ taglib uri="/taglib/category" prefix="category" %>

<script language="JavaScript" type="text/javascript" src="<digi:file src="module/aim/scripts/common.js"/>"></script>
<digi:instance property="aimThemeForm" />
<digi:form action="/assignNewIndicator.do" method="post" name="aimThemeFormIndPopin" type="aimThemeForm">
<html:hidden property="step"/>
<html:hidden property="item" />
<html:hidden property="selectedindicatorFromPages" />
<html:hidden property="alpha" />

 
  <table bgColor=#ffffff cellPadding=0 cellSpacing=0 width=772>
    <tr>
      <td class=r-dotted-lg width=14>&nbsp;</td>
      <td align=left class=r-dotted-lg vAlign=top width=750>
        <table cellPadding=5 cellSpacing=0 width="100%" border=0>
            <table width="100%" cellSpacing=5 cellPadding=5 vAlign="top" border=0>
	<tr><td vAlign="top">
		<table bgcolor=#f4f4f2 cellPadding=5 cellSpacing=5 width="100%" class=box-border-nopadding>
			<tr>
				<td align=left vAlign=top>
					<table bgcolor=#f4f4f2 cellPadding=0 cellSpacing=0 width="100%" class=box-border-nopadding>
						<tr bgcolor="#006699">
							<td vAlign="center" width="100%" align ="center" class="textalb" height="20">
								<digi:trn key="aim:searchind">
								Search Indicators</digi:trn>
							</td></tr>
						<tr>
							<td align="center" bgcolor=#ECF3FD>
								<table cellSpacing=2 cellPadding=2>
									<tr>
									<td>
									<digi:trn key="aim:selsector">
											Select Sector
									</digi:trn>
									</td>
										<td>
										
                                      <html:select property="sectorName" styleClass="inp-text">
                                      			<html:option value="-1">-<digi:trn key="aim:selsector">Select sector</digi:trn>-</html:option>
												<c:if test="${!empty aimThemeForm.allSectors}">
									<html:optionsCollection name="aimThemeForm" property="allSectors" value="name" label="name" />						
												</c:if>
									</html:select>
										</td>
									</tr>
									<tr>
										<td>
											<digi:trn key="aim:enterKeyword">
											Enter a keyword</digi:trn>
										</td>
										<td>
										<html:text property="keyword" style="width:140px;font-family:verdana;font-size:11px;" />
										</td>
									</tr>
									<tr>
										<td>
											<digi:trn key="aim:numResultsPerPage">
											Number of results per page</digi:trn>
										</td>
										<td>
											<html:text property="tempNumResults" size="2" styleClass="inp-text" />
										</td>
									</tr>
									<tr>
										<td align="center" colspan=2>
											&nbsp;
											<!-- <input type="submit" value="Go" style="font-family:verdana;font-size:11px;" /> -->
											<html:button  styleClass="dr-menu" property="submitButton" onclick="return searchindicators()">
												<digi:trn key="btn:search">Search</digi:trn> 
											</html:button>
											&nbsp;
											<html:button  styleClass="dr-menu" property="submitButton" onclick="clearform()" >
												<digi:trn key="btn:clear">Clear</digi:trn> 
											</html:button>
											&nbsp;
											<html:button  styleClass="dr-menu" property="submitButton" onclick="closeWindow()">
												<digi:trn key="btn:close">Close</digi:trn> 
											</html:button>
											&nbsp;
											<html:button  styleClass="dr-menu" property="submitButton" onclick="viewall()">
												<digi:trn key="btn:viewall">View all</digi:trn>
											</html:button>
										</td>
									</tr>
								</table>
							</td>
						</tr>
					</table>
              <table width="100%" cellspacing=0 cellSpacing=0 border="0">
                <tr>
                  <td noWrap width=600 vAlign="top">
                    <table bgColor=#d7eafd cellPadding=0 cellSpacing=0 width="100%" valign="top">
                      <tr bgColor=#ffffff>
                        <td vAlign="top" width="100%">
                          <table width="100%" cellspacing=0 cellpadding=0 valign="top" align="left">
                            <tr>
                              <td>
                                <table style="font-family:verdana;font-size:11px;" width="100%">
                                  <tr>
                                    <td colspan="11" width="100%" align="center">
                                      <table width="100%" align="center" border="0" style="font-family:verdana;font-size:11px;">
                                        <tr bgcolor="#006699">
	                                        <td vAlign="center" width="100%" align ="center" class="textalb" height="20" colspan="2">
												<digi:trn key="aim:listofprgIndicators">
													List Of Program Indicators
												</digi:trn>
											</td>
										</tr>
                                    </tr>
                                    <tr>
                                     <td align="center">
                                     <c:if test="${empty aimThemeForm.pagedCol && aimThemeForm.pagedCol != null}">
									   <digi:trn key="aim:noindicators">No indicators match the search criteria</digi:trn>
									</c:if>	
                                     </td>
                                    </tr>
                              <logic:notEmpty name="aimThemeForm" property="pagedCol">  
                                    <logic:iterate name="aimThemeForm" id="indicators" property="pagedCol"
									type="org.digijava.module.aim.dbentity.AmpIndicator">
										<tr>
											<td bgcolor=#ECF3FD width="10%">
											<html:multibox property="indid">
													<bean:write name="indicators" property="indicatorId" />
											</html:multibox>
											</td>
											<td bgcolor=#ECF3FD width="90%">
											<bean:write name="indicators" property="name" />
											</td>
										</tr>
										<tr>
										<td>
										 &nbsp;
										</td>
										</tr>
									
									</logic:iterate>
									
                                        <tr>
                                          <td colspan="10" align="center">
                                            &nbsp;
                                          </td>
                                        </tr>
                                        <tr>
                                          <td colspan="10" align="center">
                                          <html:button  styleClass="dr-menu" property="submitButton"  onclick="return selectIndicators()">
															<digi:trn key="btn:add">Add</digi:trn> 
										</html:button>
                                          </td>
                                        </tr>
                                      </table>
                                          <logic:notEmpty name="aimThemeForm" property="pages">
											<tr>
												<td align="center">
													<table width="90%">
													<tr><td>
													<digi:trn key="aim:pages">
													Pages</digi:trn>
													<logic:iterate name="aimThemeForm" property="pages" id="pages" type="java.lang.Integer">
														<jsp:useBean id="urlParams1" type="java.util.Map" class="java.util.HashMap"/>
														<c:set target="${urlParams1}" property="page">
															<%=pages%>
														</c:set>
														<c:set target="${urlParams1}" property="orgSelReset" value="false"/>
														<c:set target="${urlParams1}" property="edit" value="true"/>
				
														<c:if test="${aimThemeForm.currentPage == pages}">
															<font color="#FF0000"><%=pages%></font>
														</c:if>
														<c:if test="${aimThemeForm.currentPage != pages}">
															<bean:define id="translation">
																<digi:trn key="aim:clickToViewNextPage">Click here to goto Next Page</digi:trn>
															</bean:define>
				
															<a href="javascript:selectIndicatorsPages(<%=pages%>);"><%=pages %></a>
														</c:if>
														|&nbsp;
													</logic:iterate>
													</td></tr>
													</table>
												</td>
											</tr>
										</logic:notEmpty>
									   <logic:notEmpty name="aimThemeForm" property="alphaPages">
											<tr>
												<td align="center">
													<table width="90%">
													<tr><td>
													<!-- <bean:define id="translation">
															<digi:trn key="aim:clickToViewAllSearchPages">Click here to view all search pages</digi:trn>
														</bean:define>
														<a href="javascript:searchAlphaAll('viewAll')" title="<%=translation%>">
															viewAll</a>&nbsp;|&nbsp;
													 -->														
														<logic:iterate name="aimThemeForm" property="alphaPages" id="alphaPages" type="java.lang.String">
															<c:if test="${alphaPages != null}">
																<c:if test="${aimThemeForm.currentAlpha == alphaPages}">
																	<font color="#FF0000"><%=alphaPages %></font>
																</c:if>
																<c:if test="${aimThemeForm.currentAlpha != alphaPages}">
																<bean:define id="translation">
																	<digi:trn key="aim:clickToViewNextPage">Click here to go to next page</digi:trn>
																</bean:define>
																	<a href="javascript:searchAlpha('<%=alphaPages%>')" title="<%=translation%>" >
																		<%=alphaPages %></a>
																</c:if>
															|&nbsp;
															</c:if>
														</logic:iterate>
													</td></tr>
													</table>
												</td>
											</tr>
										</logic:notEmpty>
				                     </logic:notEmpty>                
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
            </td>
          </tr>
        </table>
      </td>
    </tr>
  </table>
</digi:form>