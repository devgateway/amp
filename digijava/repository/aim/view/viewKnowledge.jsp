<%@ page pageEncoding="UTF-8" %>
<%@ taglib uri="/taglib/struts-bean" prefix="bean" %>
<%@ taglib uri="/taglib/struts-logic" prefix="logic" %>
<%@ taglib uri="/taglib/struts-tiles" prefix="tiles" %>
<%@ taglib uri="/taglib/struts-html" prefix="html" %>
<%@ taglib uri="/taglib/digijava" prefix="digi" %>
<%@ taglib uri="/taglib/jstl-core" prefix="c" %>
<script type="text/javascript">
function login()
{
	<digi:context name="addUrl" property="context/module/moduleinstance/login.do" />
    document.aimKnowledgeForm.action = "<%=addUrl%>";
    document.aimKnowledgeForm.submit();
}
</script>

<digi:context name="digiContext" property="context" />
<digi:instance property="aimKnowledgeForm" />
<logic:equal name="aimKnowledgeForm" property="validLogin" value="false">
	<digi:form action="/viewKnowledge.do" name="aimKnowledgeForm" 
	type="org.digijava.module.aim.form.KnowledgeForm" method="post">
		<h3 align="center">
		Invalid Login. Please Login Again. 
		</h3>
		<p align="center"><input type="button" class="dr-menu" value="Log In" onclick="login()" /></p>
	</digi:form>
</logic:equal>

<logic:equal name="aimKnowledgeForm" property="validLogin" value="true">

<TABLE cellSpacing=0 cellPadding=0 align="center" vAlign="top" border=0 width="100%">
<TR>
	<TD vAlign="top" align="center">
		<!-- contents -->

			<TABLE width="760" cellSpacing=0 cellPadding=0 vAlign="top" align="center" bgcolor="#f4f4f4" class="box-border-nopadding">
			<TR><TD bgcolor="#f4f4f4">
			
			<TABLE width="760" cellSpacing=3 cellPadding=3 vAlign="top" align="center" bgcolor="#f4f4f4">
				<TR bgColor=#f4f4f2>
            	<TD align=left>
						<SPAN class=crumb>					
						<jsp:useBean id="urlKnowledge" type="java.util.Map" class="java.util.HashMap"/>
						<c:set target="${urlKnowledge}" property="ampActivityId">
							<bean:write name="aimKnowledgeForm" property="id"/>
						</c:set>
						<c:set target="${urlKnowledge}" property="tabIndex" value="3"/>
						<bean:define id="translation">
							<digi:trn key="aim:clickToViewKnowledge">Click here to view Knowledge</digi:trn>
						</bean:define>
						<digi:link href="/viewKnowledge.do" name="urlKnowledge" styleClass="comment" title="<%=translation%>" >
							<digi:trn key="aim:knowledge">Knowledge</digi:trn>
						</digi:link>
						&gt; Overview &gt; <bean:write name="aimKnowledgeForm" property="perspective"/> Perspective
						</SPAN>
					</TD>
				</TR>
				<TR bgColor=#f4f4f2>
					<TD vAlign="top" align="center" width="100%">
						<TABLE width="98%" cellPadding=0 cellSpacing=0 vAlign="top" align="center" bgColor=#f4f4f2>
							<TR>
								<TD width="100%" bgcolor="#F4F4F2" height="17">
									<TABLE border="0" cellpadding="0" cellspacing="0" bgcolor="#F4F4F2" height="17">
                           	<TR bgcolor="#F4F4F2" height="17"> 
                              	<TD bgcolor="#C9C9C7" class="box-title">&nbsp;&nbsp;
											<digi:trn key="aim:documents">Documents</digi:trn></TD>
	                              <TD><IMG src="../ampTemplate/images/corner-r.gif" width="17" height="17"></TD>
   	                        </TR>
      	                  </TABLE>									
								</TD>
							</TR>
							<TR>
								<TD width="100%" bgcolor="#F4F4F2" align="center">
									<TABLE width="100%" cellPadding="2" cellSpacing="2" vAlign="top" align="center" bgColor=#f4f4f2
									class="box-border-nopadding">
										<TR>
											<TD width="100%" vAlign="top" align="left">
												<TABLE width="100%" cellPadding="4" cellSpacing="1" vAlign="top" align="left" bgcolor="#ffffff">
													<TR bgcolor="#dddddd">
														<TD width="100%" colspan="2" align="center"><b>
															<digi:trn key="aim:item">Item</digi:trn></b>
														</TD>
													</TR>
													<jsp:useBean id="docParams" type="java.util.Map" class="java.util.HashMap"/>	
													<logic:iterate name="aimKnowledgeForm"  property="documents" 
													id="document" type="org.digijava.module.aim.helper.Documents">
													
													<c:if test="${document.isFile == true}">
													<TR bgcolor="#f4f4f2">
														<TD align="right">
															<IMG alt=Link height=10 src="../ampTemplate/images/arrow-gr.gif"></TD>
														<TD width="98%" align="left">
															<%--
															<c:set target="${docParams}" property="docId">
																<c:out value="${document.docId}"/>
															</c:set>
															<c:set target="${docParams}" property="actId">
																<c:out value="${document.activityId}"/>
															</c:set>		
															<c:set target="${docParams}" property="pageId" value="0"/>
															<c:set target="${docParams}" property="reset" value="true"/>
															<bean:define id="translation">
																<digi:trn key="aim:clickToViewDocumentDetails">Click here to view Document Details</digi:trn>
															</bean:define>
															<digi:link href="/getDocumentDetails.do" name="docParams" 
															title="<%=translation%>" >
																<bean:write name="document" property="title"/>
															</digi:link> - 															
															--%>
															<b>
															<bean:write name="document" property="title"/></b> - 
															<i>File :

															<a href="<%=digiContext%>/cms/downloadFile.do?itemId=
															<c:out value="${document.docId}"/>">
															<c:out value="${document.fileName}"/></i></a>
														</TD>
													</TR>
													<c:if test="${!empty document.docDescription}">
													<TR bgcolor="#f4f4f2">
														<TD width="98%" align="left" colspan="2">
															&nbsp;&nbsp;&nbsp;&nbsp;<c:out value="${document.docDescription}"/>
														</TD>
													</TR>	
													</c:if>
													</c:if>
													</logic:iterate>              
												</TABLE>
											</TD>
										</TR>
									</TABLE>
								</TD>
							</TR>
						</TABLE>
					</TD>
				</TR>	

				<TR bgColor=#f4f4f2>
					<TD vAlign="top" align="center" width="100%">
						<TABLE width="98%" cellPadding=0 cellSpacing=0 vAlign="top" align="center" bgColor=#f4f4f2>
							<TR>
								<TD width="100%" bgcolor="#F4F4F2" height="17">
									<TABLE border="0" cellpadding="0" cellspacing="0" bgcolor="#F4F4F2" height="17">
                           	<TR bgcolor="#F4F4F2" height="17"> 
                              	<TD bgcolor="#C9C9C7" class="box-title">&nbsp;&nbsp;
											<digi:trn key="aim:webResources">Web Resources</digi:trn></TD>
	                              <TD><IMG src="../ampTemplate/images/corner-r.gif" width="17" height="17"></TD>
   	                        </TR>
      	                  </TABLE>									
								</TD>
							</TR>
							<TR>
								<TD width="100%" bgcolor="#F4F4F2" align="center">
									<TABLE width="100%" cellPadding="2" cellSpacing="2" vAlign="top" align="center" bgColor=#f4f4f2
									class="box-border-nopadding">
										<TR>
											<TD width="100%" vAlign="top" align="left">
												<TABLE width="100%" cellPadding="4" cellSpacing="1" vAlign="top" align="left" bgcolor="#ffffff">
													<TR bgcolor="#dddddd">
														<TD width="100%" colspan="2" align="center"><b>
															<digi:trn key="aim:item">Item</digi:trn></b>
														</TD>
													</TR>
													<logic:iterate name="aimKnowledgeForm"  property="documents" 
													id="document" type="org.digijava.module.aim.helper.Documents">
													
													<c:if test="${document.isFile == false}">
													<TR bgcolor="#f4f4f2">
														<TD align="right">
															<IMG alt=Link height=10 src="../ampTemplate/images/arrow-gr.gif"></TD>
														<TD width="98%" align="left">
															<%--
															<c:set target="${docParams}" property="docId">
																<c:out value="${document.docId}"/>
															</c:set>
															<c:set target="${docParams}" property="actId">
																<c:out value="${document.activityId}"/>
															</c:set>		
															<c:set target="${docParams}" property="pageId" value="0"/>
															<c:set target="${docParams}" property="reset" value="true"/>
															<bean:define id="translation">
																<digi:trn key="aim:clickToViewDocumentDetails">Click here to view Document Details</digi:trn>
															</bean:define>
															<digi:link href="/getDocumentDetails.do" name="docParams" 
															title="Click here to view Document Details" >
																<bean:write name="document" property="title"/>
															</digi:link> - 															
															--%>
															<b>
															<bean:write name="document" property="title"/></b> - 
															<i>URL :
															<a href="<c:out value="${document.url}"/>" target="_blank">
															<c:out value="${document.url}"/></a></i>
														</TD>
													</TR>
													<c:if test="${!empty document.docDescription}">
													<TR bgcolor="#f4f4f2">
														<TD width="98%" align="left" colspan="2">
															&nbsp;&nbsp;&nbsp;&nbsp;<c:out value="${document.docDescription}"/>
														</TD>
													</TR>	
													</c:if>
													</c:if>
													</logic:iterate>              
												</TABLE>
											</TD>
										</TR>
									</TABLE>
								</TD>
							</TR>
						</TABLE>
					</TD>
				</TR>					

				<%--

				<TR bgColor=#f4f4f2>
					<TD vAlign="top" align="center" width="100%">
						<TABLE width="98%" cellPadding=0 cellSpacing=0 vAlign="top" align="center" bgColor=#f4f4f2>
							<TR>
								<TD width="100%" bgcolor="#F4F4F2" height="17">
									<TABLE border="0" cellpadding="0" cellspacing="0" bgcolor="#F4F4F2" height="17">
                           	<TR bgcolor="#F4F4F2" height="17"> 
                              	<TD bgcolor="#C9C9C7" class="box-title">&nbsp;&nbsp;
											<digi:trn key="aim:notes">Notes</digi:trn></TD>
	                              <TD><IMG src="../ampTemplate/images/corner-r.gif" width="17" height="17"></TD>
   	                        </TR>
      	                  </TABLE>									
								</TD>
							</TR>
							<TR>
								<TD width="100%" bgcolor="#F4F4F2" align="center">
									<TABLE width="100%" cellPadding="2" cellSpacing="2" vAlign="top" align="center" bgColor=#f4f4f2
									class="box-border-nopadding">
										<TR>
											<TD width="100%" vAlign="top" align="left">
												<TABLE width="100%" cellPadding="3" cellSpacing="1" vAlign="top" align="left" bgcolor="#ffffff">
													<TR bgcolor="#dddddd">
														<TD width="100%" colspan="2" align="center"><b>
															<digi:trn key="aim:item">Item</digi:trn></b>
														</TD>
													</TR>

													<logic:iterate name="aimKnowledgeForm" property="notes" id="notes"  
													type="org.digijava.module.aim.helper.Notes">
													<TR bgcolor="#f4f4f2">
														<TD align="right">
															<IMG alt=Link height=10 src="../ampTemplate/images/arrow-gr.gif"></TD>
														<TD width="98%" align="left">
															<bean:write name="notes" property="description"/>
							 								<logic:equal name="notes" property="dflag" value="1">
																<jsp:useBean id="urlKnowledgeNotes" type="java.util.Map" class="java.util.HashMap"/>
																<c:set target="${urlKnowledgeNotes}" property="ampActivityId">
																	<bean:write name="aimKnowledgeForm" property="id"/>
																</c:set>
																<c:set target="${urlKnowledgeNotes}" property="ampNotesId">
																	<bean:write name="notes" property="ampNotesId"/>
																</c:set>
																<c:set target="${urlKnowledgeNotes}" property="tabIndex" value="3"/>
																<bean:define id="translation">
																	<digi:trn key="aim:clickToViewMoreNotes">Click here to view more notes</digi:trn>
																</bean:define>
																<digi:link href="/viewKnowledgeNotes.do" name="urlKnowledgeNotes" styleClass="comment" title="<%=translation%>" >
																	<digi:trn key="aim:more">more...</digi:trn>
																</digi:link>
															</logic:equal>
														</TD>
													</TR>
													</logic:iterate>            
												</TABLE>
											</TD>
										</TR>
									</TABLE>
								</TD>
							</TR>
						</TABLE>
					</TD>
				</TR>					

				--%>
				
				

			</TABLE>

			</TD></TR>
			
			</TABLE>
		<!-- end -->
	</TD>
</TR>
<TR>
	<TD>&nbsp;</TD>
</TR>
</TABLE>
</logic:equal>
