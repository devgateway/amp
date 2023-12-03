<%@ page pageEncoding="UTF-8" %>
<%@ taglib uri="/taglib/struts-bean" prefix="bean" %>
<%@ taglib uri="/taglib/struts-logic" prefix="logic" %>
<%@ taglib uri="/taglib/struts-tiles" prefix="tiles" %>
<%@ taglib uri="/taglib/struts-html" prefix="html" %>
<%@ taglib uri="/taglib/digijava" prefix="digi" %>
<%@ taglib uri="/taglib/jstl-core" prefix="c" %>

<digi:instance property="helpForm" />
<digi:form action="/helpActions.do?actionType=searchHelpTopic"> 
	<table width="100%">
	<tr>
		<td>	
			 <table bgColor=#ffffff cellpadding="0" cellspacing="0" width=772>
				<tr>
					<td class=r-dotted-lg width=14>&nbsp;</td>
					<td align=left class=r-dotted-lg valign="top" width=750>
						<table cellPadding=5 cellspacing="0" width="100%">
							<tr>
								<td height=16 align="center" valign="center">
									<span class=subtitle-blue>
										<digi:trn key="help:searchResults">Search results</digi:trn>
									</span>
								</td>
							</tr>
							<tr>
								<td>
									<table class="box-border-nopadding" width="100%" cellspacing="0" cellpadding="0" bgcolor="#ffffff">
										<tr bgcolor="#f4f4f2">
											<td>&nbsp;</td>
										</tr>
										<tr bgcolor="#f4f4f2">
											<td>
												<table width="90%" cellspacing="0" cellpadding="0" bgcolor="#f4f4f2" align="center">
													<tr>
														<td class="box-border" bgcolor="#ffffff">
															<table class="box-border" width="100%" cellspacing="3" cellpadding="3">
																<tr bgcolor="#dddddb">
																	<td align="left" >
																		<b><digi:trn key="help:searchResultsFor">Search Results For:</digi:trn>${helpForm.keywords}</b>
																	</td>
																</tr>
																<tr bgcolor="#ffffff">
																	<td>
																		<table width="100%">
																			<c:if test="${empty helpForm.helpTopics}">
																				<digi:trn key="help:noTopicFound">Topics Not Found</digi:trn>
																			</c:if>
																			<c:if test="${not empty helpForm.helpTopics}">
																				<c:forEach var="helpTopic" items="${helpForm.helpTopics}" varStatus="status">
																					<c:choose>
																						<c:when test="${(status.index)%2 eq 0}">
																							<c:set var="backGround">#ffffff</c:set>
																						</c:when>
																						<c:when test="${(status.index)%2 eq 1}">
																							<c:set var="backGround">#eeeeee</c:set>
																						</c:when>
																					</c:choose>
																					<tr height="20px" style="background-color: ${backGround};" >
																						<td width="100%" nowrap>
                                                                                                                                                                                    <digi:context name="url" property="context/ampModule/moduleinstance/helpActions.do?actionType=viewSelectedHelpTopic" />
																							<a href="${url}&topicKey=${helpTopic.topicKey}">
																								<digi:trn key="${helpTopic.titleTrnKey}"></digi:trn>
																							</a>
																						</td>																
																					</tr>
															 					</c:forEach>
																			</c:if>
															 			</table>	
																	</td>
																</tr>
															</table>												
														</td>
													</tr>
												</table>
											</td>
										</tr>
										<tr bgcolor="#f4f4f2">
											<td>&nbsp;</td>
										</tr>
									</table>
								</td>
							</tr>											
						</table>
					</td>			
				</tr>
			</table>
		</td>
		<td>
			<TABLE align="center" border="0" cellPadding=2 cellSpacing=3 width="100%" bgcolor="#f4f4f2">
				<TR>
					<TD class=r-dotted-lg-buttom valign="top">
						<TABLE border="0" cellpadding="0" cellspacing="0" width="100%" >
			        		<TR><TD>
			              		<TABLE border="0" cellpadding="0" cellspacing="0" >
			              			<TR bgColor=#f4f4f2>
			                 			<TD bgColor=#c9c9c7 class=box-title>
			                 				<digi:trn key="help:search">Search</digi:trn>
			                 			</TD>
			                    		<TD background="ampModule/aim/images/corner-r.gif"	height=17 width=17></TD>
									</TR>
								</TABLE>
							</TD></TR>
							<TR><TD bgColor=#ffffff class=box-border align=left>
								<TABLE>
									<TR>
										<TD>
											<html:text property="keywords" />
										</TD>
										<TD>
											<c:set var="searchtpc">
												<digi:trn key="help:SearchText">Search Topic</digi:trn>
											</c:set>
											<input type="submit" class="dr-menu"  value="${searchtpc}"/>
										</TD>
									</TR>
								</TABLE>
							</TD></TR>
						</TABLE>
					</TD>
				</TR>
			</TABLE>
		</td>
	</tr>
	</table>
</digi:form>
 