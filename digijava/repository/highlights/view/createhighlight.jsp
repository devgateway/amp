
<%@ page pageEncoding="UTF-8" %>
<%@ taglib uri="/taglib/struts-bean" prefix="bean" %>
<%@ taglib uri="/taglib/struts-logic" prefix="logic" %>
<%@ taglib uri="/taglib/struts-tiles" prefix="tiles" %>
<%@ taglib uri="/taglib/struts-html" prefix="html" %>
<%@ taglib uri="/taglib/digijava" prefix="digi" %>
<%@ taglib uri="/taglib/jstl-core" prefix="c" %>


<script>function fnOnAddLink(index) {
      <digi:context name="addUrl" property="context/module/moduleinstance/addLink.do" />
      document.highlightItemForm.action = "<%= addUrl %>?CreateOrEdit=createHighlight&offset=" + index;
      document.highlightItemForm.submit();
  }
  function fnOnDeleteLink(index) {
      <digi:context name="deleteUrl" property="context/module/moduleinstance/deleteLink.do" />
      document.highlightItemForm.action = "<%= deleteUrl %>?CreateOrEdit=createHighlight&offset=" + index;
      document.highlightItemForm.submit();
  }
  function fnOnPreview() {
      <digi:context name="previewHighlight" property="context/module/moduleinstance/previewHighlight.do" />
      document.highlightItemForm.action = "<%= previewHighlight %>?CreateOrEdit=createHighlight";
      document.highlightItemForm.submit();
  }
</script>
<html:javascript formName="highlightItemForm"/>
<digi:errors/>
<digi:form action="/createHighlight.do" method="post" enctype="multipart/form-data"><!-- Preview Hightlight -->
	<c:if test="${ !empty highlightItemForm.previewItem}">
	<c:if test="${highlightItemForm.preview}" >
	<TABLE width="100%">
		<tr>
			<td noWrap align="center"><b>
				<digi:trn key="highlights:reviewHighlight">Please review the highlight before publishing it:</digi:trn></b>
			</td>
		</tr>
		<tr>
			<td>	&nbsp;
			</td>
		</tr>
		<tr>
			<td>
				<c:set var="previewItem" value="${highlightItemForm.previewItem}" /> 
				<div align="center">	
				<!-- Layout1 -->
				    <c:if test="${highlightItemForm.layout1}">
					<TABLE cellSpacing="0" cellPadding="0" width="100%" border="0">
						<TBODY>
							<TR>
								<TD colSpan="3">
									<TABLE cellSpacing="0" cellPadding="0" width="100%" border="0">
										<TBODY>
											<TR>
												<TD bgColor="#00499b">
													<DIV class="mheading"><FONT color="#ffffff">
														<c:out value="${previewItem.description}" /></FONT>
													</DIV>
												</TD>
											</TR>
										</TBODY>
									</TABLE>
								</TD>
							</TR>
							<TR>
								<TD colSpan="3">
								</TD>
							</TR>
							<TR>
								<TD rowSpan="4">	&nbsp;
								</TD>
							</TR>
							<TR>
								<TD align="center" class="text">
							    	<c:if test="${!empty previewItem.title}">
									<b><h3><c:out value="${previewItem.title}" escapeXml="false"/></h3></b> 
									</c:if><BR>
									<SPAN class="date"><FONT color="#666666" size="2">
									<c:out value="${previewItem.creationDate}" />
									</FONT></SPAN>
								</TD>
							</TR>
							<TR>
								<TD>
								</TD>
							</TR>
							<TR>
								<TD colSpan="3">
								</TD>
							</TR>
							<TR>
								<TD>
								</TD>
								<TD>
									<TABLE cellSpacing="0" cellPadding="0" width="100%" border="0">
										<TBODY>
											<TR>
												<TD class="text">
													<TABLE width="100%" cellSpacing="0" cellPadding="0" align="left" border="0">
														<TBODY>
															<TR>
																<TD align="center" valign="center">
																	<digi:context name="previewImage" property="context/module/moduleinstance/previewImage.do"/>
																	<c:if test="${previewItem.haveImageSizes}"><img src="<%= previewImage%>" height="<c:out value='${previewItem.imageHeight}' />" width="<c:out value='${previewItem.imageWidth}' />"/></c:if>
																	<c:if test="${! previewItem.haveImageSizes}"><img src="<%= previewImage%>"/></c:if><BR>
																</TD>
															</TR>
														</TBODY>
													</TABLE>
												</TD>
											</TR>
											<TR>
												<TD class="text">
												    <c:if test="${!empty previewItem.topic}">
													<c:out value="${previewItem.topic}" escapeXml="false"/>
													</c:if><BR><BR>
													<FONT color="#0000cc"><B>Related links:</B><BR></FONT>
												</TD>
											</TR>
										</TBODY>
									</TABLE>
								</TD>
							</TR>
							<c:if test="${ ! empty previewItem.links}">
							<TR>
								<TD>
								</TD>
								<TD>
									<TABLE cellSpacing="0" cellPadding="0" width="100%" border="0">
										<TBODY>
										    <c:forEach var="lid" items="${previewItem.links}"> 
											<TR>
												<TD class="news" align="left">
													<digi:img src="module/highlights/images/arrow.gif"/>
													<c:if test="${!empty lid.name}">
													<a href='<c:out value='${lid.url}' />' class="text"><B>
													<c:out value="${lid.name}" escapeXml="false"/></B></a><BR></c:if>
												</TD>
											</TR></c:forEach>
										</TBODY>
									</TABLE>
								</TD>
							</TR></c:if>
						</TBODY>
					</TABLE></c:if>
					<!-- Layout2 -->
					<c:if test="${highlightItemForm.layout2}">
					<TABLE cellSpacing="0" cellPadding="0" width="100%" border="0">
						<TBODY>
							<TR>
								<TD colSpan="3">
									<TABLE cellSpacing="0" cellPadding="0" width="100%" border="0">
										<TBODY>
											<TR>
												<TD bgColor="#00499b">
													<DIV class="mheading"><FONT color="#ffffff">
													    <c:out value="${previewItem.description}" />
														</FONT>
													</DIV>
												</TD>
											</TR>
										</TBODY>
									</TABLE>
								</TD>
							</TR>
							<TR>
								<TD colSpan="3">
								</TD>
							</TR>
							<TR>
								<TD rowSpan="4">	&nbsp;
								</TD>
							</TR>
							<TR>
								<TD align="center" colspan="2" class="text">
  								    <c:if test="${!empty previewItem.title}">
									<b><h3><c:out value= "${previewItem.title}" escapeXml="false" /></h3></b></c:if><BR>
									<SPAN class="date"><FONT color="#666666" size="2">
									<c:out value="${previewItem.creationDate}" />
									</FONT></SPAN>
								</TD>
							</TR>
							<TR>
								<TD>
								</TD>
							</TR>
							<TR>
								<TD colSpan="3">
								</TD>
							</TR>
							<TR>
								<TD>
								</TD>
								<TD>
									<TABLE cellSpacing="0" cellPadding="0" width="100%" border="0">
										<TBODY>
											<TR>
												<TD class="text">
													<TABLE cellSpacing="0" cellPadding="0" align="left" border="0">
														<TBODY>
															<TR>
																<TD>
																	<digi:context name="previewImage" property="context/module/moduleinstance/previewImage.do"/>
																	<c:if test="${previewItem.haveImageSizes}"><img src="<%= previewImage%>" height="<c:out value='${previewItem.imageHeight}' />" width="<c:out value='${previewItem.imageWidth}' />"/></c:if>
																	<c:if test="${! previewItem.haveImageSizes}"><img src="<%= previewImage%>"/></c:if>
																</TD>
															</TR>
														</TBODY>
													</TABLE>
													<c:if test="${!empty previewItem.topic}">
			   										<c:out value="${previewItem.topic}" escapeXml="false" />
													</c:if>
												</TD>
											</TR>
											<TR>
												<TD>
												<FONT color="#0000cc"><B>Related links:</B></FONT>
												</TD>
											</TR>
										</TBODY>
									</TABLE>
								</TD>
							</TR>
							<c:if test="${!empty previewItem.links}">
							<TR>
								<TD>
								</TD>
								<TD>
									<TABLE cellSpacing="0" cellPadding="0" width="100%" border="0">
										<TBODY>
										    <c:forEach var="lid" items="${previewItem.links}"> 
											<TR>
												<TD class="news" align="left">
													<digi:img src="module/highlights/images/arrow.gif"/>
													<c:if test="${!empty lid.name}">
													   <a href='<c:out value="${lid.url}" />' class="text"><B>
													<c:out value="${lid.name}" escapeXml="false" /></B></a><BR></c:if>
												</TD>
											</TR></c:forEach>
										</TBODY>
									</TABLE>
								</TD>
							</TR></c:if>
						</TBODY>
					</TABLE></c:if>
					<!-- Layout3 -->
					<c:if test="${highlightItemForm.layout3}">
					<TABLE cellSpacing="0" cellPadding="0" width="100%" border="0">
						<TBODY>
							<TR>
								<TD colSpan="3">
									<TABLE cellSpacing="0" cellPadding="0" width="100%" border="0">
										<TBODY>
											<TR>
												<TD bgColor="#00499b">
													<DIV class="mheading"><FONT color="#ffffff">
													    <c:out value="${previewItem.description}" />
														</FONT>
													</DIV>
												</TD>
											</TR>
										</TBODY>
									</TABLE>
								</TD>
							</TR>
							<TR>
								<TD colSpan="3">
								</TD>
							</TR>
							<TR>
								<TD rowSpan="4">	&nbsp;
								</TD>
							</TR>
							<TR>
								<TD align="center" class="text">
  								    <c:if test="${ !empty previewItem.title}">
									<b><h3><c:out value="${previewItem.title}" escapeXml="false" /></h3></b></c:if><BR>
									<SPAN class="date"><FONT color="#666666" size="2">
									<c:out value="${previewItem.creationDate}" />
									</FONT></SPAN>
								</TD>
							</TR>
							<TR>
								<TD><IMG height="5" src="" width="1">
								</TD>
							</TR>
							<TR>
								<TD colSpan="3">
								</TD>
							</TR>
							<TR>
								<TD>
								</TD>
								<TD>
									<TABLE cellSpacing="0" cellPadding="0" width="100%" border="0">
										<TBODY>
											<TR>
												<TD class="text">
 												    <c:if test="${!empty previewItem.topic}">
													<c:out value="${previewItem.topic}" escapeXml="false" />
													</c:if><BR><BR>
													<FONT color="#0000cc"><B>Related links:</B><BR></FONT>
												</TD>
											</TR>
										</TBODY>
									</TABLE>
								</TD>
							</TR>
							<c:if test="${ !empty previewItem.links}">
							<TR>
								<TD>
								</TD>
								<TD>
									<TABLE cellSpacing="0" cellPadding="0" width="100%" border="0">
										<TBODY>
										    <c:forEach var="lid" items="${previewItem.links}"> 
											<TR>
												<TD class="news" align="left">
													<digi:img src="module/highlights/images/arrow.gif"/>
													<c:if test="${!empty lid.name}">
													<a href='<c:out value="${lid.url}" />' class="text"><B>
													<c:out value="${lid.name}" escapeXml="false" /></B></a><BR></c:if>
												</TD>
											</TR></c:forEach>
										</TBODY>
									</TABLE>
								</TD>
							</TR></c:if>
						</TBODY>
					</TABLE></c:if>
					<TABLE>
			</td>
		</tr>
		<tr>
			<td align="center">
				<digi:context name="goBack" property="context/module/moduleinstance/showCreateHighlight.do?reset=false"/>
				<html:hidden property="activeHighlightId"/>
				<input type="button" value="Edit" onclick="location.href='<%= goBack%>'">
			</td>
		</tr>
	</TABLE></c:if></c:if><!-- End Preview -->
	<c:if test="${! highlightItemForm.preview}">
	<table width="100%">
		<tr>
			<td noWrap width="50%" align="left"><b>
				<digi:trn key="highlights:createHighlight">Create highlight</digi:trn></b>
			</td>
		</tr>
	</table>
	<table width="100%">
		<tr>
			<td width="50%" valign="top">
				<table border="0">
					<tr>
						<td noWrap align="left" colspan="2">
							<digi:trn key="highlights:enterTitle">Enter the title here</digi:trn><BR>
							<html:text name="highlightItemForm" property="title" size="30"/>
						</td>
					</tr>
					<tr>
						<td noWrap align="left" colspan="2">
							<digi:trn key="highlights:enterDescription">Enter highlight description</digi:trn><BR>
							<html:text name="highlightItemForm" property="description" size="30"/>
						</td>
					</tr>
					<tr>
						<td noWrap>
							<digi:trn key="highlights:enterLinkName">Enter link name here:</digi:trn>
						</td>
						<td noWrap>
							<digi:trn key="highlights:enterLinkURL">Enter related link:</digi:trn>
						</td>
					</tr>
					<logic:iterate indexId="index" name="highlightItemForm" id="link" property="links" type="org.digijava.module.highlights.form.HighlightItemForm.LinkInfo">
					<tr>
						<html:hidden name="link" property="id" indexed="true"/>
						<html:hidden name="link" property="index" indexed="true"/>
						<bean:write name="link" property="index"/>
						<td>
							<html:text name="link" property="name" indexed="true"/>
						</td>
						<td>
							<html:text name="link" property="url" indexed="true"/>
						</td>
					</tr>	
					<tr>
						<bean:write name="index"/>
						<td noWrap><a href='javascript:fnOnAddLink("<%= index %>")'>
							<digi:trn key="highlights:add">Add</digi:trn></a>
						</td>
						<td noWrap><a href='javascript:fnOnDeleteLink("<%= index %>")'>
							<digi:trn key="highlights:delete">Delete</digi:trn></a>
						</td>
					</tr></logic:iterate>
				</table>	<!-- submit buttons -->
				<table>
					<tr>
						<td>
							<html:submit value="Save" onclick="this.disabled = true; form.submit()" />&nbsp;
							<html:submit value="Preview" onclick="javascript:fnOnPreview()"/>&nbsp;
							<html:reset value="Reset"/>&nbsp;
						</td>
					</tr>
				</table>
			</td>
			<td width="50%" valign="top">
				<table border="0">
					<tr>
						<td noWrap align="left">
							<digi:trn key="highlights:enterTopicHighlight">Enter the topic highlight here</digi:trn><BR>
							<html:textarea name="highlightItemForm" property="topic" rows="5" cols="30"/><BR><SMALL>
							<digi:trn key="highlights:shortVersLength">Short version's length: (in characters)</digi:trn></SMALL>
							<html:text name="highlightItemForm" property="shortTopicLength" size="4"/>
						</td>
					</tr>
					<tr>
						<td>
							<table>
								<tr>
									<td noWrap align="left"><small>
										<digi:trn key="highlights:browseForPicture">Use the 'Browse...' button to locate your picture <br> Max file size is 30 KBytes</digi:trn></small><BR>
										<html:file name="highlightItemForm" property="photoFile" size="30"/><BR>
									</td>
								</tr>
								<tr>
									<td>	&nbsp;
									</td>
								</tr>
							</table>
						</td>
					</tr>
					<tr>
						<td noWrap valign="bottom">
							<html:radio name="highlightItemForm" property="layout" value="1"/>
							<digi:trn key="highlights:layout1">Layout1</digi:trn>
							<digi:img src="module/highlights/images/topics1.jpg" width="50" height="50"/>
							<digi:trn key="highlights:height">Height:</digi:trn>
							<html:text name="highlightItemForm" property="imageHeight1" size="4"/>&nbsp;
							<digi:trn key="highlights:width">Width:</digi:trn>
							<html:text name="highlightItemForm" property="imageWidth1" size="4"/>&nbsp;
						</td>
					</tr>
					<tr>
						<td noWrap valign="bottom">
							<html:radio name="highlightItemForm" property="layout" value="2"/>
							<digi:trn key="highlights:layout2">Layout2</digi:trn>
							<digi:img src="module/highlights/images/topics2.jpg" width="50" height="50"/>
							<digi:trn key="highlights:height">Height:</digi:trn>
							<html:text name="highlightItemForm" property="imageHeight2" size="4"/>&nbsp;
							<digi:trn key="highlights:width">Width:</digi:trn>
							<html:text name="highlightItemForm" property="imageWidth2" size="4"/>&nbsp;
						</td>
					</tr>
					<tr>
						<td noWrap valign="bottom">
							<html:radio name="highlightItemForm" property="layout" value="3"/>
							<digi:trn key="highlights:layout3">Layout3</digi:trn>
							<digi:img src="module/highlights/images/topics3.jpg" width="50" height="50"/>
						</td>
					</tr>
				</table>
			</td>
		</tr>
	</table></c:if>
</digi:form>