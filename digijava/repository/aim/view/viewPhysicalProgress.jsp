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
    document.aimPhysicalProgressForm.action = "<%=addUrl%>";
    document.aimPhysicalProgressForm.submit();
}
</script>
<digi:errors/>
<digi:instance property="aimPhysicalProgressForm" />
<digi:context name="digiContext" property="context"/>
<logic:equal name="aimPhysicalProgressForm" property="validLogin" value="false">
<digi:form action="/viewPhysicalProgress.do" name="aimPhysicalProgressForm" type="org.digijava.module.aim.form.PhysicalProgressForm" 
method="post">
<h3 align="center"> Invalid Login. Please Login Again. </h3><p align="center"><html:submit styleClass="dr-menu" value="Log In" onclick="login()" /></p>
</digi:form>
</logic:equal>

<logic:equal name="aimPhysicalProgressForm" property="validLogin" value="true">

<TABLE cellSpacing=0 cellPadding=0 align="center" vAlign="top" border=0 width="100%">
<TR>
	<TD vAlign="top" align="center">
		<!-- contents -->

			<TABLE width="760" cellSpacing=0 cellPadding=0 vAlign="top" align="center" bgcolor="#f4f4f4" class="box-border-nopadding">
			<TR><TD bgcolor="#f4f4f4">
			
			<TABLE width="760" cellSpacing=3 cellPadding=3 vAlign="top" align="center" bgcolor="#f4f4f4" border=0>
				<TR bgColor=#f4f4f2>
            	<TD align=left>
						<SPAN class=crumb>					
						<jsp:useBean id="urlPhysicalProgress" type="java.util.Map" class="java.util.HashMap"/>
						<c:set target="${urlPhysicalProgress}" property="ampActivityId">
							<bean:write name="aimPhysicalProgressForm" property="ampActivityId"/>
						</c:set>
						<c:set target="${urlPhysicalProgress}" property="tabIndex" value="2"/>
						<bean:define id="translation">
							<digi:trn key="aim:clickToViewPhysicalProgress">Click here to view Physical Progress</digi:trn>
						</bean:define>
						<digi:link href="/viewPhysicalProgress.do" name="urlPhysicalProgress" styleClass="comment" 
						title="<%=translation%>" >
							<digi:trn key="aim:physicalProgress">PhysicalProgress</digi:trn>
						</digi:link>
						&gt; Overview &gt; <bean:write name="aimPhysicalProgressForm" property="perspective"/> Perspective
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
											<digi:trn key="aim:progressDetails">Progress Details</digi:trn></TD>
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
												<TABLE width="100%" cellPadding="5" cellSpacing="1" vAlign="top" align="left" bgcolor="#ffffff">
													<TR bgcolor="#dddddd">
														<TD width="50%"><b>
															<digi:trn key="aim:component">Component</digi:trn></b>
														</TD>
														<TD width="20%"><b>
															<digi:trn key="aim:reportingDate">Reporting Date</digi:trn></b>
														</TD>
														<TD width="30%"><b>
															<digi:trn key="aim:amount">Amount</digi:trn></b>
														</TD>
													</TR>
                        					<logic:iterate name="aimPhysicalProgressForm"  property="selectComponent" id="selectComponent" type="org.digijava.module.aim.helper.SelectComponent">
													<TR bgcolor="#f4f4f2">
														<TD width="50%">
															<jsp:useBean id="urlComponentDescription" type="java.util.Map" class="java.util.HashMap"/>
																<c:set target="${urlComponentDescription}" property="ampActivityId">
																	<bean:write name="aimPhysicalProgressForm" property="ampActivityId"/>
																</c:set>
																<c:set target="${urlComponentDescription}" property="cid">
																	<bean:write name="selectComponent" property="cid"/>
																</c:set>
																<c:set target="${urlComponentDescription}" property="tabIndex" value="2"/>
																<bean:define id="translation">
																	<digi:trn key="aim:clickToViewComponentDescription">Click here to view Component Description</digi:trn>
																</bean:define>
																<digi:link href="/viewComponentDescription.do" name="urlComponentDescription" styleClass="comment" title="<%=translation%>" >
																	<bean:write name="selectComponent" property="title"/>
																</digi:link>
														</TD>
														<TD width="20%">
															<bean:write name="selectComponent" property="reportingDate"/>
														</TD>
														<TD width="30%">
															<bean:write name="selectComponent" property="currency"/>&nbsp;
															<bean:write name="selectComponent" property="amount"/>
														</TD>
													</TR>
													<logic:notEmpty name="selectComponent"  property="physicalProgress">
															<TR bgcolor="#f4f4f2">
                                                       			<TD colspan="3">
																	<TABLE width="90%" cellPadding="5" cellSpacing="1" vAlign="top" align="center" bgcolor="#ffffff">
																		<TR>
																			<TD width="50%"><b>
																				<digi:trn key="aim:title">Title</digi:trn></b>
																			</TD>
																			<TD width="50%"><b>
																				<digi:trn key="aim:reportingDate">Reporting Date</digi:trn></b>
																			</TD>
																		</TR>
																		<logic:iterate name="selectComponent"  property="physicalProgress" id="physicalProgress" type="org.digijava.module.aim.helper.PhysicalProgress">
																			<TR>
																				<TD width="50%">
																					<jsp:useBean id="urlPhysicalProgressDescription" type="java.util.Map" class="java.util.HashMap"/>
																					<c:set target="${urlPhysicalProgressDescription}" property="ampActivityId">
																						<bean:write name="aimPhysicalProgressForm" property="ampActivityId"/>
																					</c:set>
																					<c:set target="${urlPhysicalProgressDescription}" property="pid">
																						<bean:write name="physicalProgress" property="pid"/>
																					</c:set>
																					<c:set target="${urlPhysicalProgressDescription}" property="tabIndex" value="2"/>
																					<bean:define id="translation">
																						<digi:trn key="aim:clickToViewPhysicalProgress">Click here to view Physical Progress</digi:trn>
																					</bean:define>
																					<digi:link href="/viewPhysicalProgressDescription.do" name="urlPhysicalProgressDescription" styleClass="comment" title="<%=translation%>" >
																						<bean:write name="physicalProgress" property="title" />
			 																		</digi:link>
																				</TD>
																				<TD width="50%">
																					<bean:write name="physicalProgress" property="reportingDate" />
																				</TD>
																			</TR>
																		</logic:iterate>
																	</TABLE>
																</TD>
															</TR>
													</logic:notEmpty>										
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
				<TR>
					<TD>

						<!-- issues --> 

						<TABLE width="98%" cellPadding=0 cellSpacing=0 vAlign="top" align="center" bgColor=#f4f4f2>
							<TR>
								<TD width="100%" bgcolor="#F4F4F2" height="17">
									<TABLE border="0" cellpadding="0" cellspacing="0" bgcolor="#F4F4F2" height="17">
                           	<TR bgcolor="#F4F4F2" height="17"> 
                              	<TD bgcolor="#C9C9C7" class="box-title">&nbsp;&nbsp;
											<digi:trn key="aim:Issues">Issues</digi:trn></TD>
	                              <TD><IMG src="../ampTemplate/images/corner-r.gif" width="17" height="17"></TD>
   	                        </TR>
      	                  </TABLE>									
								</TD>
							</TR>
							<TR>
								<TD width="100%" bgcolor="#F4F4F2" align="center">
									<TABLE width="100%" cellPadding="2" cellSpacing="2" vAlign="top" align="center" bgColor=#f4f4f2
									class="box-border-nopadding">
	   	                  	<logic:empty name="aimPhysicalProgressForm" property="issues">
											<TR><TD>&nbsp;</TD></TR>
										</logic:empty>
										<logic:notEmpty name="aimPhysicalProgressForm" property="issues">
											<logic:iterate name="aimPhysicalProgressForm" property="issues" id="issue" 
											type="org.digijava.module.aim.helper.Issues">
												<TR><TD width="100%" bgcolor="#F4F4F2" align="center">
													<TABLE width="100%" cellPadding="2" cellSpacing="2" vAlign="top" align="center" 
													bgColor=#f4f4f2 class="box-border-nopadding">
														<TR>
															<TD width="100%" vAlign="top" align="left">
																<TABLE width="100%" cellPadding="5" cellSpacing="1" vAlign="top" align="left" 
																bgcolor="#ffffff">
																	<TR bgcolor="#dfdfdf"><TD>
																		<bean:write name="issue" property="name"/>
																	</TD></TR>
																	<logic:empty name="issue" property="measures">
																		<TR><TD>&nbsp;</TD></TR>
																	</logic:empty>
																	<logic:notEmpty name="issue" property="measures">
																		<logic:iterate name="issue" property="measures" id="measure" 
																		type="org.digijava.module.aim.helper.Measures">
																			<TR><TD>
																				<TABLE width="95%" cellPadding="5" cellSpacing="0" vAlign="top" 
																				align="center" bgcolor="#ffffff" bordercolor="#cccccc" border="1">
																					<TR bgcolor="#f6f6f6"><TD class="box-title"><b>
																						<bean:write name="measure" property="name"/></b>
																					</TD></TR>
																					<logic:empty name="measure" property="actors">
																						<TR><TD>&nbsp;</TD></TR>
																					</logic:empty>
																					<logic:notEmpty name="measure" property="actors">
																						<logic:iterate name="measure" property="actors" id="actor"
																						type="org.digijava.module.aim.dbentity.AmpActor">
																							<TR><TD>
																								<TABLE width="90%" cellPadding="5" 
																								cellSpacing="0" vAlign="top" align="center" 
																								bgcolor="#ffffff" bordercolor="#cccccc" border="1">
																									<TR bgcolor="#ffffff"><TD class="box-title"><b>
																										<bean:write name="actor" property="name"/></b>
																									</TD></TR>
																								</TABLE>
																							</TD></TR>
																						</logic:iterate>
																					</logic:notEmpty>
																				</TABLE>
																			</TD></TR>
																		</logic:iterate>
																	</logic:notEmpty>																			
																</TABLE>
														</TD></TR>
													</TABLE>	
												</TD></TR>
											</logic:iterate>
										</logic:notEmpty>																			
									</TABLE>
								</TD>
							</TR>
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
