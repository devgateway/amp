
<%@ page pageEncoding="UTF-8" %>
<%@ taglib uri="/taglib/struts-bean" prefix="bean" %>
<%@ taglib uri="/taglib/struts-logic" prefix="logic" %>
<%@ taglib uri="/taglib/struts-tiles" prefix="tiles" %>
<%@ taglib uri="/taglib/struts-html" prefix="html" %>
<%@ taglib uri="/taglib/digijava" prefix="digi" %>
<%@ taglib uri="/taglib/jstl-core" prefix="c" %>

<digi:errors/>
<digi:instance property="highlightForm"/>
	<TABLE width="100%">
		<tr>
			<td>
			    <c:if test="${! empty highlightForm.activeHighlight}">
				<c:set var="activeHighlight" value="${highlightForm.activeHighlight}" /><!-- table>
				<tr>
					<td width="10%" align="right"><small>Created by: </small>
					</td>
					<td width="10%" align="left"><b><small>
						<bean:write name="activeHighlight" property="authorFirstName"/>
						<bean:write name="activeHighlight" property="authorLastName"/>
						<bean:write name="activeHighlight" property="creationDate"/></small> 
						</b>
					</td>
				</tr>
				<logic:present name="activeHighlight" property="updaterUserId">
				<tr>
					<td width="10%" align="right"><small>Last Updated by: </small>
					</td>
					<td width="10%" align="left"><b><small>
						<bean:write name="activeHighlight" property="updaterFirstName"/>
						<bean:write name="activeHighlight" property="updaterLastName"/>
						<bean:write name="activeHighlight" property="updationDate"/></small> 
						 </b>
					</td>
				</tr></logic:present></table-->
				<div align="center">	<!-- Layout1 -->
				    <c:if test="${highlightForm.layout1}">
					<table border="0" class="border" width="100%">
						<tr>
							<td align="center" class="text">
							    <c:if test="${!empty activeHighlight.title}">
								<b><h3><c:out value="${activeHighlight.title}" escapeXml="false" /></h3></b>
                                </c:if>
							</td>
						</tr>
						<c:if test="${activeHighlight.showImage}">
						<tr>
							<td align="center" valign="center">
								<table>
									<tr>
										<td class="text">
											<TABLE width="100%" cellSpacing="0" cellPadding="0" align="left" border="0">
												<TBODY>
													<TR>
														<TD align="center" valign="center">
														    <c:if test="${activeHighlight.haveImageSizes}">
															<html:hidden property="activeHighlightId"/>
															<digi:context name="showImg" property="context/module/moduleinstance/showImage.do"/><img src='<%= showImg%>?activeHighlightId=<c:out value="${highlightForm.activeHighlightId}" />' height="<c:out value='${activeHighlight.imageHeight}' />" width="<c:out value='${activeHighlight.imageWidth}' />"/></c:if>
														    <c:if test="${!activeHighlight.haveImageSizes}">
															<html:hidden property="activeHighlightId"/>
															<digi:context name="showImg" property="context/module/moduleinstance/showImage.do"/><img src='<%= showImg%>?activeHighlightId=<c:out value="${highlightForm.activeHighlightId}" />'/></c:if>
														</TD>
													</TR>
												</TBODY>
											</TABLE>
										</td>
									</tr>
								</table>
							</td>
						</tr></c:if>
						<tr>
							<td align="left" width="100%" class="text">
							    <c:if test="${!empty activeHighlight.topic}">
							    <c:if test="${activeHighlight.more}">
								 <c:out value="${activeHighlight.topic}" escapeXml="false" />
								<digi:link href="/showMore.do"><small>
								<digi:trn key="highlights:more">more</digi:trn></small></digi:link></c:if>
							    <c:if test="${! activeHighlight.more}">
								 <c:out value="${activeHighlight.topic}" escapeXml="false" />
								</c:if></c:if>
							</td>
						</tr>
						<c:if test="${!empty activeHighlight.links}">
						<tr>
							<td>
								<table>
								    <c:forEach var="lid" items="${activeHighlight.links}">
									<tr>
										<td align="left" class="text">
										   <c:if test="${!empty lid.name}">
											<a href='<c:out value="${lid.url}" />' class="text">
											<c:out value="${lid.name}" escapeXml="false" /></a></c:if>
										</td>
									</tr></c:forEach>
								</table>
						</tr>
			</td></c:if>
	</table></c:if>
	<!-- Layout2 -->
    <c:if test="${highlightForm.layout2}">
	<table border="0" class="border" width="100%">
		<tr>
			<td align="center" colspan="2" class="text">
			    <c:if test="${!empty activeHighlight.title}">
				<b><h3><c:out value="${activeHighlight.title}" escapeXml="false" /></h3></b></c:if>
			</td>
		</tr>
	    <c:if test="${activeHighlight.showImage}">
		<tr>
			<td width="50%" align="center" valign="center">
				<table>
					<tr>
						<td class="text">
							<TABLE cellSpacing="0" cellPadding="0" align="left" border="0">
								<TBODY>
									<TR>
										<TD>
										    <c:if test="${activeHighlight.haveImageSizes}">
											<html:hidden property="activeHighlightId"/>
											<digi:context name="showImg" property="context/module/moduleinstance/showImage.do"/><img src='<%= showImg%>?activeHighlightId=<c:out value="${highlightForm.activeHighlightId}" />' height="<c:out value='${activeHighlight.imageHeight}' />" width="<c:out value='${activeHighlight.imageWidth}' />"/></c:if>
											<c:if test="${! activeHighlight.haveImageSizes}">											
											<html:hidden property="activeHighlightId"/>
											<digi:context name="showImg" property="context/module/moduleinstance/showImage.do"/><img src='<%= showImg%>?activeHighlightId=<c:out value="${highlightForm.activeHighlightId}" />'/></c:if>
										</TD>
									</TR>
								</TBODY>
							</TABLE>
							<c:if test="${!empty activeHighlight.topic}">
							<c:if test="${activeHighlight.more}">
							   <c:out value="${activeHighlight.topic}" escapeXml="false" />
							<digi:link href="/showMore.do"><small>
							<digi:trn key="highlights:more">more</digi:trn></small></digi:link></c:if>
							<c:if test="${! activeHighlight.more}">							
							   <c:out value="${activeHighlight.topic}" escapeXml="false" />
						    </c:if></c:if>
						</td>
					</tr>
				</table>
			</td>
		</tr></c:if>
		<c:if test="${!empty activeHighlight.links}">
		<tr>
			<td>
				<table>
				<c:forEach var="lid" items="${activeHighlight.links}">
					<tr>
						<td align="left" class="text">
						<c:if test="${!empty lid.name}">
							<a href='<c:out value="${lid.url}" />' class="text">
							<c:out value="${lid.name}" escapeXml="false" /></a></c:if>
						</td>
					</tr></c:forEach>
				</table>
			</td>
		</tr></c:if>
	</table></c:if>
	<!-- Layout3 -->
	<c:if test="${highlightForm.layout3}">
	<table border="0" class="border" width="100%">
		<tr>
			<td align="center" class="text">
			<c:if test="${!empty activeHighlight.title}">
				<b><h3><c:out value="${activeHighlight.title}" escapeXml="false" /></h3></b></c:if>
			</td>
		</tr>
		<tr>
			<td align="left" class="text">
			<c:if test="${!empty activeHighlight.topic}">
			   <c:if test="${activeHighlight.more}">
   				 <c:out value="${activeHighlight.topic}" escapeXml="false" />				
				<digi:link href="/showMore.do"><small>
				<digi:trn key="highlights:more">more</digi:trn></small></digi:link></c:if>
				<c:if test="${! activeHighlight.more}">
   				 <c:out value="${activeHighlight.topic}" escapeXml="false" />				
				</c:if></c:if>
			</td>
		</tr>
		<c:if test="${!empty activeHighlight.links}">
		<tr>
			<td>
				<table>
					<c:forEach var="lid" items="${activeHighlight.links}">				
					<tr>
						<td align="left" class="text">
						<c:if test="${!empty lid.name}">
							<a href='<c:out value="${lid.url}" />' class="text">
							<c:out value="${lid.name}" escapeXml="false" /></a></c:if>
						</td>
					</tr></c:forEach>
				</table>
		</tr>
</td></c:if>
	</table></c:if>
</div>
	<digi:secure actions="WRITE,TRANSLATE">
	<table>
		<tr>
			<td noWrap align="right">
				<digi:link href="/showEditHighlight.do" paramName="activeHighlight" paramId="activeHighlightId" paramProperty="id"><small>
				<digi:trn  key="highlights:editHighlight">Edit Highlight</digi:trn></small></digi:link>
			</td>
		</tr>
	</table></digi:secure></c:if>
	<digi:secure actions="WRITE,TRANSLATE">
	<logic:notPresent name="highlightForm" property="activeHighlight">
	<table>
		<tr>
			<td noWrap>
				<digi:link href="/showCreateHighlight.do"><small>
				<digi:trn key="highlights:createHighlight">Create Highlight</digi:trn></small></digi:link>
			</td>
		</tr>
	</table></logic:notPresent></digi:secure>
	<c:if test="${highlightForm.archive}">
	<table>
		<tr>
			<td noWrap>
				<digi:link href="/viewAllHighlights.do"><small>
				<digi:trn key="highlights:viewAll">View All</digi:trn></small></digi:link>
			</td>
		</tr>
	</table></c:if>
</td>
</tr>
</TABLE>