<%@ page pageEncoding="UTF-8" %>
<%@ taglib uri="/taglib/struts-bean" prefix="bean" %>
<%@ taglib uri="/taglib/struts-logic" prefix="logic" %>
<%@ taglib uri="/taglib/struts-html" prefix="html" %>
<%@ taglib uri="/taglib/digijava" prefix="digi" %>
<%@ taglib uri="/taglib/jstl-core" prefix="c" %>
<%@ taglib uri="/taglib/category" prefix="category" %>

<digi:instance property="addressbookForm" />

<table width="100%" cellSpacing="1" cellPadding="5" vAlign="top">
	<tr>
    	<td width="100%" vAlign="top">
        	<table bgColor="#ffffff" cellPadding="0" cellSpacing="0" width="650" vAlign="top" align="left" border="0">
				<tr>
					<td>
						<table width="98%" cellSpacing=1 cellPadding="2">
												<tr>
													<td class="head2-name" width="100%" align="center" bgcolor="#ffffff">
			                                			<digi:trn>Contacts</digi:trn>
													</td>
												</tr>	
						</table>
					</td>					
				</tr>
				<tr><td>&nbsp;</td></tr>
				<tr>
					<td>
						<table width="98%" cellSpacing=0 cellpadding=4 style="border-collapse: collapse" border="1">
							<tr bgcolor="#f4f4f2">
								<td nowrap="nowrap"><digi:trn>Title</digi:trn></td>
								<td nowrap="nowrap"><digi:trn>Name</digi:trn></td>
								<td nowrap="nowrap"><digi:trn>Email</digi:trn></td>
								<td nowrap="nowrap"><digi:trn>Organizations</digi:trn></td>
								<td nowrap="nowrap"><digi:trn>Function</digi:trn></td>
								<td nowrap="nowrap"><digi:trn>Phone</digi:trn></td>
								<td nowrap="nowrap"><digi:trn>Fax</digi:trn></td>
							</tr>
							<c:forEach var="cont" items="${addressbookForm.contactsForPage}">
												<tr>
													<td nowrap="nowrap">
														${cont.title.value}								
													</td>
													<td nowrap="nowrap">
														${cont.name} &nbsp; ${cont.lastname} 
													</td>
													<td nowrap="nowrap">
														<c:if test="${not empty cont.properties}">
															<c:forEach var="prop" items="${cont.properties}"> 
																<c:if test="${prop.name=='contact email'}">
																	<li>
																		${prop.value}
																	</li>
																</c:if>
															</c:forEach>
														</c:if>
													</td>
													<td nowrap="nowrap">
														<c:if test="${not empty cont.organizationContacts}">
															<c:forEach var="org" items="${cont.organizationContacts}">
																	<li>
																		${org.organisation.name}
																	</li>
															</c:forEach>
														</c:if>
													</td>
													<td nowrap="nowrap">
														${cont.function}
													</td>
													<td nowrap="nowrap">
														<c:if test="${not empty cont.properties}">
															<c:forEach var="prop" items="${cont.properties}"> 
																<c:if test="${prop.name=='contact phone'}">
																	<li>
																		${prop.actualPhoneNumber}
																	</li>
																</c:if>
															</c:forEach>
														</c:if>
													</td>
													<td nowrap="nowrap">
														<c:if test="${not empty cont.properties}">
															<c:forEach var="prop" items="${cont.properties}"> 
																<c:if test="${prop.name=='contact fax'}">
																	<li>
																		${prop.value}
																	</li>
																</c:if>
															</c:forEach>
														</c:if>
													</td>
												</tr>
										</c:forEach>							
							
						</table>
					</td>
				</tr>
			</table>
        </td>
   </tr>
</table>


