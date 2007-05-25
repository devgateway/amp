
<%@ page pageEncoding="UTF-8" %>
<%@ taglib uri="/taglib/struts-bean" prefix="bean" %>
<%@ taglib uri="/taglib/struts-logic" prefix="logic" %>
<%@ taglib uri="/taglib/struts-tiles" prefix="tiles" %>
<%@ taglib uri="/taglib/struts-html" prefix="html" %>
<%@ taglib uri="/taglib/digijava" prefix="digi" %>
<%@ taglib uri="/taglib/jstl-core" prefix="c" %>
<%@ taglib uri="/taglib/jstl-core" prefix="c" %>

<script>function fnOnAddLink(index) {
      <digi:context name="addUrl" property="context/module/moduleinstance/addLink.do" />
      document.highlightItemForm.action = "<%= addUrl %>?CreateOrEdit=editHighlight&offset=" + index;
      document.highlightItemForm.submit();
  }
  function fnOnDeleteLink(index) {
      <digi:context name="deleteUrl" property="context/module/moduleinstance/deleteLink.do" />
      document.highlightItemForm.action = "<%= deleteUrl %>?CreateOrEdit=editHighlight&offset=" + index;
      document.highlightItemForm.submit();
  }
  function fnOnPreview() {
      <digi:context name="previewHighlight" property="context/module/moduleinstance/previewHighlight.do" />
      document.highlightItemForm.action = "<%= previewHighlight %>?CreateOrEdit=editHighlight";
      document.highlightItemForm.submit();
  }
  function fnOnArchive() {
      <digi:context name="archiveHighlight" property="context/module/moduleinstance/archiveHighlight.do" />
      document.highlightItemForm.action = "<%= archiveHighlight %>";
      document.highlightItemForm.submit();
  }
</script>
<digi:errors/>
<digi:form action="/editHighlight.do" method="post" enctype="multipart/form-data"><!-- Preview Hightlight -->
    <c:if test="${ !empty highlightItemForm.previewItem}">
    <c:if test="${highlightItemForm.preview}">
	<table width="100%">
		<tr>
			<td noWrap align="center" class="bold">
				<digi:trn key="highlights:reviewHighlight">Please review the highlight before publishing it:</digi:trn>
			</td>
		</tr>
		<tr>
			<td>	&nbsp;
			</td>
		</tr>
		<tr>
			<td>
			    <c:set var="previewItem" value="${highlightItemForm.previewItem}" />
				<div align="center">	<!-- Layout1 -->
				  <c:if test="${highlightItemForm.layout1}">
					<TABLE cellSpacing="0" cellPadding="0" width="100%" border="0">
						<TBODY>
							<TR>
								<TD colSpan="3">
									<TABLE cellSpacing="0" cellPadding="0" width="100%" border="0">
										<TBODY>
											<TR>
												<TD class="bgblue" height="21">	&nbsp;&nbsp;
													<span class="txtwhitebold">
													<c:out value="${previewItem.description}"/></span>
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
									<c:if test="${previewItem.title}">
									<b><h3><c:out value="${previewItem.title}" escapeXml="false" /></h3></b></c:if>
								</TD>
							</TR>
							<TR>
								<TD>
									<c:out value="${previewItem.creationDate}"/>
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
												<TD>
													<TABLE width="100%" cellSpacing="0" cellPadding="0" align="left" border="0">
														<TBODY>
															<TR>
																<TD align="center" valign="center">
																	<digi:context name="previewImage" property="context/module/moduleinstance/previewImage.do"/>
																	<c:if test="${previewItem.haveImageSizes}"><img src="<%= previewImage%>" height="<c:out value='${previewItem.imageHeight}' />" width="<c:out value='${previewItem.imageWidth}' />"/></c:if>
																	<c:if test="${! previewItem.haveImageSizes}"><img src="<%= previewImage%>"/></c:if>
																</TD>
															</TR>
														</TBODY>
													</TABLE>
												</TD>
											</TR>
											<TR>
												<TD>
													<c:if test="${!empty previewItem.topic}">
													  <c:out value="${previewItem.topic}" escapeXml="false"/>
													</c:if><BR><BR>
													<span class="bold">
													<span class="dgTitleColor">Related links:</span></span>
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
												<TD align="left">
													<digi:img src="module/common/images/bullet_new.gif"/>
													<c:if test="${ !empty lid.name}">
													<a href='<c:out value="${lid.url}"/>' class="text">
													<c:out value="${lid.name}" escapeXml="false" /></a><BR></c:if>
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
												<TD class="blue">
													<span class="txtwhitebold">
													<c:out value="${previewItem.description}"/></span>
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
									<b><h3><c:out value="${previewItem.title}" escapeXml="false" /></h3></b></c:if>
								</TD>
							</TR>
							<TR>
								<TD>
									<c:out value="${previewItem.creationDate}"/></FONT>
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
												<TD>
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
													  <c:out value="${previewItem.topic}" escapeXml="false"/>
													</c:if>												</TD>
											</TR>
											<TR>
												<TD class="dgtitletopic">	Related links:
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
													<digi:img src="module/common/images/bullet_new.gif"/>
													<c:if test="${ !empty lid.name}">
													<a href='<c:out value="${lid.url}"/>' class="text">
													<c:out value="${lid.name}" escapeXml="false" /></a><BR></c:if>
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
												<TD class="bgblue">
													<span class="txtwhitebold">
													<c:out value="${previewItem.description}"/></span>
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
									<c:if test="${previewItem.title}">
									<b><h3><c:out value="${previewItem.title}" escapeXml="false" /></h3></b></c:if>
								</TD>
							</TR>
							<TR>
								<TD>
									<c:out value="${previewItem.creationDate}"/>
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
												<TD>
													<c:if test="${!empty previewItem.topic}">
													  <c:out value="${previewItem.topic}" escapeXml="false"/>
													</c:if>
												</TD>
											</TR>
											<tr>
												<td class="dgtitletopic">	Related links:
												</td>
											</tr>
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
													<digi:img src="images/arrow.gif"/>
													<c:if test="${ !empty lid.name}">
													<a href='<c:out value="${lid.url}"/>' class="text"><B>
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
				<digi:context name="goBack" property="context/module/moduleinstance/showEditHighlight.do?reset=false"/>
				<html:hidden property="activeHighlightId"/>
				<input type="button" value="Edit" onclick="location.href='<%= goBack%>'">
			</td>
		</tr>
	</TABLE></c:if></c:if><!-- End Preview -->
<!-- Edit HIGHLIGHT -->
	<c:if test="${! highlightItemForm.preview}">
	<table width="100%">
		<tr>
			<td noWrap width="50%" class="dgTitle">
				<digi:trn key="highlights:editHighlight">Edit highlight</digi:trn>
			</td>
		</tr>
	</table><BR>
	<table cellpadding="3" cellspacing="0" border="0">
		<tr>
			<td noWrap align="right" class="span">
				<digi:trn key="highlights:createdBy">Created by:</digi:trn>
			</td>
			<td width="775" align="left" class="small">
				<span class="bold">
				<c:out value="${highlightItemForm.authorFirstName}"/>
				<c:out value="${highlightItemForm.authorLastName}"/>-
				<c:out value="${highlightItemForm.creationDate}"/>
				</span>
			</td>
		</tr>
		<c:if test="${! empty highlightItemForm.updaterUserId}">
		<tr>
			<td noWrap align="right">
				<digi:trn key="highlights:updatedBy">Last Updated by:</digi:trn>
			</td>
			<td width="775" class="bold" align="left">
				<c:out value="${highlightItemForm.updaterFirstName}"/>
				<c:out value="${highlightItemForm.updaterLastName}"/>-
				<c:out value="${highlightItemForm.updationDate}"/>
			</td>
		</tr></c:if>
		<tr>
			<td colspan="2">	&nbsp;
			</td>
		</tr>
	</table>
	<table width="100%">
		<tr>
			<td width="50%" valign="top">
				<table border="0" cellpadding="3" cellspacing="0">
					<tr>
						<td noWrap align="left" colspan="2" class="bold">
							<digi:trn key="highlights:enterTitle">Enter the title here</digi:trn><BR>
							<html:text name="highlightItemForm" property="title" size="30"/>
						</td>
					</tr>
					<tr>
						<td noWrap align="left" colspan="2" class="bold">
							<digi:trn key="highlights:enterDescription">Enter highlight description</digi:trn><BR>
							<html:text name="highlightItemForm" property="description" size="30"/>
						</td>
					</tr>
					<tr>
						<td noWrap class="bold">
							<digi:trn key="highlights:enterLinkName">Enter link name here:</digi:trn>
						</td>
						<td noWrap class="bold">
							<digi:trn key="highlights:enterLinkURL">Enter related link:</digi:trn>
						</td>
					</tr>
					<logic:iterate indexId="index" name="highlightItemForm" id="link" property="links" type="org.digijava.module.highlights.form.HighlightItemForm.LinkInfo">
					<tr>
						<html:hidden name="link" property="id" indexed="true"/>
						<html:hidden name="link" property="index" indexed="true"/>
						<td>
							<html:text name="link" property="name" indexed="true"/>
						</td>
						<td>
							<html:text name="link" property="url" indexed="true"/>
						</td>
					</tr>
					<tr>
						<td noWrap><a href='javascript:fnOnAddLink("<%= index %>")'>
							<span class="blue">
							<digi:trn key="highlights:add">Add</digi:trn></span></a>
						</td>
						<td noWrap><a href='javascript:fnOnDeleteLink("<%= index %>")'>
							<span class="blue">
							<digi:trn key="highlights:delete">Delete</digi:trn></span></a>
						</td>
					</tr></logic:iterate>
				</table>	<!-- submit buttons -->
				<table>
					<tr>
						<td>
							<html:hidden property="activeHighlightId"/>
							<html:submit value="Update" onclick="this.disabled = true; form.submit()" />&nbsp;
							<html:hidden property="activeHighlightId"/>
							<html:submit value="Preview" onclick="javascript:fnOnPreview()"/>&nbsp;
							<logic:equal name="highlightItemForm" property="active" value="true">
							  <html:submit value="Archive" onclick="javascript:fnOnArchive(); this.disabled = true; form.submit()" />&nbsp;
							</logic:equal>  
							<html:reset value="Reset"/>&nbsp;
						</td>
					</tr>
				</table>
			</td>
			<td width="50%" valign="top">
				<table border="0">
					<tr>
						<td noWrap align="left">
							<span class="bold">
							<digi:trn key="highlights:enterTopicHighlight">Enter the topic highlight here</digi:trn></span><BR>
							<html:textarea name="highlightItemForm" property="topic" rows="5" cols="30"/><BR><SMALL>
							<digi:trn key="highlights:shortVersLength">Short version's length: (in characters)</digi:trn></SMALL>
							<html:text name="highlightItemForm" property="shortTopicLength" size="4"/>
						</td>
					</tr>
					<tr>
						<td>
							<table>
								<tr>
									<c:if test="${highlightItemForm.haveImage}">
									<td width="50%" align="center" valign="middle">
										<table>
											<tr>
												<td>
												    <c:if test="${highlightItemForm.haveImageSizes}">
													<digi:context name="previewImage" property="context/module/moduleinstance/previewImage.do"/>
													   <img src="<%= previewImage%>" height="<c:out value='${highlightItemForm.imageHeight}' />" width="<c:out value='${highlightItemForm.imageWidth}' />"/></c:if>
													<c:if test="${! highlightItemForm.haveImageSizes}">
													<digi:context name="previewImage" property="context/module/moduleinstance/previewImage.do"/><img src="<%= previewImage%>"/></c:if>
												</td>
											</tr>
										</table>
									</td></c:if>
									<td noWrap width="50%" align="left" valign="top"><small>
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
							<span class="bold">
							<digi:trn key="highlights:layout1">Layout1</digi:trn></span>
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
							<span class="bold">
							<digi:trn key="highlights:layout2">Layout2</digi:trn></span>
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
							<span class="bold">
							<digi:trn key="highlights:layout3">Layout3</digi:trn></span>
							<digi:img src="module/highlights/images/topics3.jpg" width="50" height="50"/>
						</td>
					</tr>
				</table>
			</td>
		</tr>
	</table></c:if>
</digi:form>