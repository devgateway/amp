<%@ page pageEncoding="UTF-8" %>
<%@ taglib uri="/taglib/struts-bean" prefix="bean" %>
<%@ taglib uri="/taglib/struts-logic" prefix="logic" %>
<%@ taglib uri="/taglib/struts-tiles" prefix="tiles" %>
<%@ taglib uri="/taglib/struts-html" prefix="html" %>
<%@ taglib uri="/taglib/digijava" prefix="digi" %>
<%@ taglib uri="/taglib/jstl-core" prefix="c" %>

<digi:ref href="css/styles.css" type="text/css" rel="stylesheet" />

<digi:instance property="addressbookForm" />
<digi:context name="digiContext" property="context" />

<script type="text/javascript">
	function createContact(){
		addressbookForm.action="${contextPath}/aim/addressBook.do?actionType=addContact";
		addressbookForm.target = "_self";
		addressbookForm.submit();
	}
</script>

<digi:form action="/addressBook.do?actionType=viewAddressBook" method="post">	
	<table bgColor=#ffffff cellPadding=0 cellSpacing=0 width=772>
		<tr>
			<td class=r-dotted-lg width=14>&nbsp;</td>
			<td align=left class=r-dotted-lg vAlign=top width=750>
				<table cellPadding=5 cellSpacing=0 width="879">
					<tr>
						<!-- Start Navigation -->
						<td height=33 colspan="7" width="867">
							<span class=crumb>
				              	<c:set var="translation">
									<digi:trn key="aim:clickToViewMyDesktop">Click here to view MyDesktop</digi:trn>
								</c:set>
								<digi:link href="/showDesktop.do" styleClass="comment" title="${translation}">
									<digi:trn>Portfolio</digi:trn>
								</digi:link>&nbsp;&gt;&nbsp;
								<digi:trn>Address Book</digi:trn>
			              </span>
						</td>
						<!-- End navigation -->
					</tr>
					<tr>
						<td height="16" vAlign="center" width="867" colspan="7">
							<span class=subtitle-blue><digi:trn>Address Book</digi:trn></span>
						</td>
					</tr>
					<tr>
						<td nowrap="nowrap">
				              <digi:trn>keyword:</digi:trn>
				              <html:text property="keyword" style="font-family:verdana;font-size:11px;"/>
			            </td>
			            <td align="left" width="10%" nowrap="nowrap"> 
			              	<digi:trn>Results</digi:trn>&nbsp;
							<html:select property="resultsPerPage" styleClass="inp-text">
								<html:option value="10">10</html:option>
								<html:option value="20">20</html:option>
								<html:option value="50">50</html:option>
								<html:option value="-1">ALL</html:option>
							</html:select>
			            </td>
						<td align="left" width="70%">
				              <c:set var="trn">
				                <digi:trn>Show</digi:trn>
				              </c:set>
				              <input type="submit" value="${trn}"  class="dr-menu" style="font-family:verdana;font-size:11px;" />
			            </td>
					</tr>
					<tr>
						<td noWrap width=867 vAlign="top" colspan="7">
						<table width="100%" cellspacing=1 cellSpacing=1>
						<tr>
							<td noWrap width=600 vAlign="top">
								<table bgColor=#ffffff cellPadding=0 cellSpacing=0 class=box-border-nopadding width="100%">
									<tr bgColor=#f4f4f2>
										<td vAlign="top" width="100%">
											&nbsp;
										</td>
									</tr>
									<tr bgColor=#f4f4f2>
										<td valign="top">
											<table align=center bgColor=#f4f4f2 cellPadding=0 cellSpacing=0 width="100%" border=0>
												<tr>
													<td bgColor=#ffffff class=box-border>
														<table border=0 cellPadding=1 cellSpacing=1 class=box-border width="100%">
															<tr bgColor=#dddddb>
																<!-- header -->
																<td bgColor=#dddddb height="20"	align="center" colspan="5"><B>
																	<digi:trn >Contact List</digi:trn>
	                                                              </b>
																</td>
																<!-- end header -->
															</tr>
															<!-- Page Logic -->
															<c:if test="${empty addressbookForm.contactsForPage}">
									                        	<tr>
																	<td colspan="5">
					                                                	<b><digi:trn>No Contacts present</digi:trn></b>
																	</td>
																</tr>
								                            </c:if>
								                            <c:if test="${not empty addressbookForm.contactsForPage}">
								                            	<tr>
																	<td width="100%">
																		<table  bgColor="#f4f4f2" cellpadding="2" width="100%">
																			<tr>																						
																				<td>
																					<c:if test="${not empty addressbookForm.sortBy && addressbookForm.sortBy!='nameAscending'}">
																						<digi:link href="/addressBook.do?actionType=searchContacts&sortBy=nameAscending&reset=false">
																							<b><digi:trn>Name</digi:trn></b>
																						</digi:link>																					
																					</c:if>
																					<c:if test="${empty addressbookForm.sortBy || addressbookForm.sortBy=='nameAscending'}">
																						<digi:link href="/addressBook.do?actionType=searchContacts&sortBy=nameDescending&reset=false">
																							<b><digi:trn>Name</digi:trn></b>
																						</digi:link>																					
																					</c:if>
																					<c:if test="${empty addressbookForm.sortBy || addressbookForm.sortBy=='nameAscending'}"><img  src="/repository/aim/images/up.gif"/></c:if>
																					<c:if test="${not empty addressbookForm.sortBy && addressbookForm.sortBy=='nameDescending'}"><img src="/repository/aim/images/down.gif"/></c:if>
																				</td>
																				<td>																		
																					<c:if test="${empty addressbookForm.sortBy || addressbookForm.sortBy!='emailAscending'}">
																						<digi:link href="/addressBook.do?actionType=searchContacts&sortBy=emailAscending&reset=false">
																							<b><digi:trn>Email</digi:trn></b>
																						</digi:link>																					
																					</c:if>
																					<c:if test="${not empty addressbookForm.sortBy && addressbookForm.sortBy=='emailAscending'}">
																						<digi:link href="/addressBook.do?actionType=searchContacts&sortBy=emailDescending&reset=false">
																							<b><digi:trn>Email</digi:trn></b>
																						</digi:link>
																					</c:if>
																					<c:if test="${not empty addressbookForm.sortBy && addressbookForm.sortBy=='emailAscending'}"><img  src="/repository/aim/images/up.gif"/></c:if>
																					<c:if test="${not empty addressbookForm.sortBy && addressbookForm.sortBy=='emailDescending'}"><img src="/repository/aim/images/down.gif"/></c:if>
																				</td>
																				<td>
																					<c:if test="${empty addressbookForm.sortBy || addressbookForm.sortBy!='orgNameAscending'}">
																						<digi:link href="/addressBook.do?actionType=searchContacts&sortBy=orgNameAscending&reset=false">
																							<b><digi:trn >Organisation Name</digi:trn></b>
																						</digi:link>																					
																					</c:if>
																					<c:if test="${not empty addressbookForm.sortBy && addressbookForm.sortBy=='orgNameAscending'}">
																						<digi:link href="/addressBook.do?actionType=searchContacts&sortBy=orgNameDescending&reset=false">
																							<b><digi:trn >Organisation Name</digi:trn></b>
																						</digi:link>																					
																					</c:if>
																					<c:if test="${not empty addressbookForm.sortBy && addressbookForm.sortBy=='orgNameAscending'}"><img  src="/repository/aim/images/up.gif"/></c:if>
																					<c:if test="${not empty addressbookForm.sortBy && addressbookForm.sortBy=='orgNameDescending'}"><img src="/repository/aim/images/down.gif"/></c:if>																																			
																				</td>
																				<td height="30">
																					<b><digi:trn>Title</digi:trn></b>													
																				</td>
																				<td height="30">
																					<b><digi:trn>Phone</digi:trn></b>													
																				</td>
																				<td height="30">
																					<b><digi:trn>Fax</digi:trn></b>															
																				</td>
																				<td height="30" colspan="2"><b>
																					<digi:trn>Actions</digi:trn></b>
																				</td>
																			</tr>
																			<c:forEach var="cont" items="${addressbookForm.contactsForPage}" varStatus="stat">
			                                                           		<c:set var="background">
																				<c:if test="${stat.index%2==0}">#cccccc</c:if>
																				<c:if test="${stat.index%2==1}">#f4f4f2</c:if>
																			</c:set>
																			<tr bgcolor="${background}">
				                                                           		<td height="30">
																				  ${cont.name}&nbsp;${cont.lastname}
																				</td>
																				<td height="30">
																				  	${cont.email}
																				</td>																	
																				<td height="30">
																					${cont.organisationName}
																				</td>
																				<td height="30">
																					${cont.title}
																				</td>
																				<td height="30">
																					${cont.phone}
																				</td>
																				<td height="30">
																					${cont.fax}
																				</td>
																				<td height="30" >
																					<jsp:useBean id="urlParams" type="java.util.Map" class="java.util.HashMap"/>
																					<c:set target="${urlParams}" property="contactId">
																						<bean:write name="cont" property="id"/>
																					</c:set>
																					<digi:link href="/addressBook.do?actionType=editContact" name="urlParams"><img src="/repository/message/view/images/edit.gif" border="0" /></digi:link>
																					<digi:link href="/addressBook.do?actionType=deleteContact" name="urlParams"><img src="/repository/message/view/images/trash_12.gif" border="0" /></digi:link>
																				</td>																			
			                                                            	</tr>
																		</c:forEach>	
																		</table>
																	</td>
																</tr>														
															</c:if>														
															<!-- end page logic -->
														</table>
													</td>
												</tr>
												<!-- page logic for pagination -->
												<logic:notEmpty name="addressbookForm" property="pages">
													<tr>
														<td colspan="4" nowrap="nowrap">
															<digi:trn>Pages :</digi:trn>
															<c:if test="${addressbookForm.currentPage > 1}">
																<jsp:useBean id="urlParamsFirst" type="java.util.Map" class="java.util.HashMap"/>
																<c:set target="${urlParamsFirst}" property="page" value="1"/>
																<c:set var="translation">
																	<digi:trn key="aim:firstpage">First Page</digi:trn>
																</c:set>
																
																<digi:link href="/addressBook.do?actionType=searchContacts"  style="text-decoration=none" name="urlParamsFirst" title="${translation}"  >
																	&lt;&lt;
																</digi:link>
															
																<jsp:useBean id="urlParamsPrevious" type="java.util.Map" class="java.util.HashMap"/>
																<c:set target="${urlParamsPrevious}" property="page" value="${umViewAllUsersForm.currentPage -1}"/>
																<c:set var="translation">
																	<digi:trn key="aim:previouspage">Previous Page</digi:trn>
																</c:set>
																<digi:link href="/addressBook.do?actionType=searchContacts" name="urlParamsPrevious" style="text-decoration=none" title="${translation}" >
																	&lt;
																</digi:link>
															</c:if>														
															<c:set var="start" value="${addressbookForm.offset}"/>
															<logic:iterate name="addressbookForm" property="pages" id="pages" type="java.lang.Integer" offset="${start}" length="5">	
																<jsp:useBean id="urlParams1" type="java.util.Map" class="java.util.HashMap"/>
																<c:set target="${urlParams1}" property="page"><%=pages%>
																</c:set>																
																<c:if test="${addressbookForm.currentPage == pages}">
																	<font color="#FF0000"><%=pages%></font>
																</c:if>
																<c:if test="${addressbookForm.currentPage != pages}">
																	<c:set var="translation">
																	<digi:trn key="aim:clickToViewNextPage">Click here to go to Next Page</digi:trn>
																	</c:set>
																	<digi:link href="/addressBook.do?actionType=searchContacts" name="urlParams1" title="${translation}" >
																		<%=pages%>
																	</digi:link>
																</c:if>
																|&nbsp;
															</logic:iterate>	
																
															<c:if test="${addressbookForm.currentPage != addressbookForm.pagesSize}">
																<jsp:useBean id="urlParamsNext" type="java.util.Map" class="java.util.HashMap"/>
																<c:set target="${urlParamsNext}" property="page" value="${addressbookForm.currentPage+1}"/>																
																<c:set var="translation">
																	<digi:trn key="aim:nextpage">Next Page</digi:trn>
																</c:set>
																<digi:link href="/addressBook.do?actionType=searchContacts"  style="text-decoration=none" name="urlParamsNext" title="${translation}"  >
																	&gt;
																</digi:link>
																<jsp:useBean id="urlParamsLast" type="java.util.Map" class="java.util.HashMap"/>																
																<c:if test="${addressbookForm.pagesSize < 5}">
																	<c:set target="${urlParamsLast}" property="page" value="${addressbookForm.pagesSize}"/>
																</c:if>																
																<c:set var="translation">
																<digi:trn key="aim:lastpage">Last Page</digi:trn>
																</c:set>
																<digi:link href="/addressBook.do?actionType=searchContacts"  style="text-decoration=none" name="urlParamsLast" title="${translation}">
																	&gt;&gt; 
																</digi:link>
															</c:if>
															&nbsp;
															<c:out value="${addressbookForm.currentPage}"></c:out>&nbsp;<digi:trn key="aim:of">of</digi:trn>&nbsp;<c:out value="${addressbookForm.pagesSize}"></c:out>
														</td>
													</tr>
													</logic:notEmpty>											
												<!-- end page logic for pagination -->
											</table>
										</td>
									</tr>
									<tr>
										<td align="right">
											<input type="button" onclick="createContact()" value="<digi:trn>Create Contact</digi:trn>" class="dr-menu" />
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