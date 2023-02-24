 <%@ page pageEncoding="UTF-8"%> 
<%@ taglib uri="/taglib/struts-bean" prefix="bean"%>
<%@ taglib uri="/taglib/struts-logic" prefix="logic"%>
<%@ taglib uri="/taglib/struts-tiles" prefix="tiles"%>
<%@ taglib uri="/taglib/struts-html" prefix="html"%>
<%@ taglib uri="/taglib/digijava" prefix="digi"%>
<%@ taglib uri="/taglib/jstl-core" prefix="c"%>
<%@ taglib uri="/taglib/fieldVisibility" prefix="field" %>

<%@ page import="java.util.List"%>


<digi:instance property="crDocumentManagerForm" />
<bean:define id="myForm" name="crDocumentManagerForm" toScope="page" type="org.digijava.module.contentrepository.form.DocumentManagerForm" />
	<div id="loadingDiv" style="text-align: center;display: none;">
		<digi:trn>Loading...</digi:trn> <br>
		<img src='/repository/contentrepository/view/images/ajax-loader-darkblue.gif' border='0' height='15px' align="middle"/>
	</div>
	<logic:notEmpty name="crDocumentManagerForm" property="otherDocuments">	
	<table>
				<thead>
							<tr>
								<th><digi:trn>Version</digi:trn></th>
								<th><digi:trn>Type</digi:trn></th>
								<th><digi:trn>Resource Name</digi:trn></th>
								<th><digi:trn>Date</digi:trn></th>
								<th><digi:trn>Pub.Year</digi:trn></th>
								<th><digi:trn>Size (MB)</digi:trn></th>
								<field:display name="Resource Index" feature="Resource Columns"><th><digi:trn>Index</digi:trn></th></field:display>
								<field:display name="Resource Category" feature="Resource Columns"><th><digi:trn>Category</digi:trn></th></field:display>								
								<th><digi:trn>Notes</digi:trn></th>
								<th><digi:trn>Actions</digi:trn></th>
							</tr>
						</thead>											
						<logic:iterate name="crDocumentManagerForm"	property="otherDocuments" id="documentData"	type="org.digijava.module.contentrepository.helper.DocumentData">
							<tr>
								<td>									
									 <bean:write name="documentData" property="versionNumber" />									 
								</td>
								<td>
									<digi:img skipBody="true" src="${documentData.iconPath}" border="0" alt="${ documentData.contentType }" align="absmiddle"/>
									<div>&nbsp;</div>
								</td>
								<td>
									<c:choose>
                                        <c:when test="${documentData.webLink == null}">
                                            &nbsp;<bean:write name="documentData" property="name" />
                                        </c:when>
                                        <c:otherwise>
                                            <c:set var="translation">
                                                <digi:trn>Follow link to</digi:trn>
                                            </c:set>
                                            <a onmouseover="Tip('${translation} ${documentData.webLink}')" onmouseout="UnTip()" onclick="window.open('${documentData.webLink}')"
                                                style="cursor:pointer;  color: blue; font-size: 11px">
                                                <bean:write name="documentData" property="name" />
                                            </a>
                                        </c:otherwise>
									</c:choose>
									<c:if test="${documentData.isPublic}">
										 <span style="color: CC0000;">*</span>
									</c:if>
									<c:if test="${documentData.isShared}">
                                        <span style="color: 00CC00;">*</span>
									</c:if>
								</td>
								<td>
									<bean:write name="documentData" property="calendar" />
								</td>
								<td>
									<bean:write name="documentData" property="yearofPublication" />
								</td>
								<td>
									<bean:write name="documentData" property="fileSize" />
								</td>
								<field:display name="Resource Index" feature="Resource Columns">
									<td>
										<bean:write name="documentData" property="index" />
									</td>
								
								</field:display>
								<field:display name="Resource Category" feature="Resource Columns">
									<td>
										<bean:write name="documentData" property="category" />
									</td>								
								</field:display>
								<td>
									<bean:write name="documentData" property="notes" />
									<a name="aDocumentUUID" class="invisible-item"><bean:write name="documentData" property="uuid" /></a>
								</td>
								<td>
								
								<c:choose>
									<c:when test="${documentData.webLink == null}">
										<c:set var="translation">
											<digi:trn key="contentrepository:documentManagerDownloadHint">Click here to download document</digi:trn>
										</c:set>
										
										<a style="cursor:pointer; text-decoration:underline; color: blue"
										onClick="downloadFile('<bean:write name='documentData' property='uuid' />');"
										title="${translation }"><img src= "/repository/contentrepository/view/images/check_out.gif" border="0"></a>
									</c:when>
									<c:otherwise>
										<c:set var="translation">
											<digi:trn key="contentrepository:documentManagerFollowLinkHint">Follow link to </digi:trn>
										</c:set> 
										<a style="cursor:pointer; text-decoration:underline; color: blue"
										onclick="window.open('${documentData.webLink}')" onmouseout="UnTip()"
										onmouseover="Tip('${translation} ${documentData.webLink}')"><img src= "/repository/contentrepository/view/images/link_go.gif" border="0"></a>
									</c:otherwise>
								</c:choose>
								
								<logic:equal name="documentData" property="hasApproveVersionRights" value="true">
									<c:if test="${documentData.currentVersionNeedsApproval==true}">
										<a  id="a<%=documentData.getUuid() %>" style="cursor:pointer; text-decoration:underline; color: blue"
											onClick="approveVersion('<%=documentData.getUuid() %>','<%=documentData.getBaseNodeUUID() %>');"><digi:trn>Approve</digi:trn></a>
										
											<a  id="a<%=documentData.getUuid() %>" style="cursor:pointer; text-decoration:underline; color: blue"
											onClick="rejectVersion('<%=documentData.getUuid() %>','<%=documentData.getBaseNodeUUID() %>');"><digi:trn>Reject</digi:trn></a>
										
									</c:if>
								</logic:equal>
								</td>
							</tr>
						</logic:iterate>
					</table>
					<div class="t_sm" style="text-align: left;">
                        <div>
                            <span style="color: 00CC00;">* star</span>&nbsp; Indicates shared versions of the document
                        </div>
                        <div>
                            <span style="color: CC0000;">* star</span>&nbsp; The colored row marks the public version
                        </div>
					</div>
										
					<br />
				</logic:notEmpty>
				