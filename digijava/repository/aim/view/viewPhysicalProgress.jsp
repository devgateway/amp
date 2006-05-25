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
	<TR><TD vAlign="top" align="center">
		<!-- contents -->
		<TABLE width="99%" cellSpacing=0 cellPadding=0 vAlign="top" align="center" bgcolor="#f4f4f4" 
		class="box-border-nopadding">
			<TR><TD bgcolor="#f4f4f4">
				<TABLE width="100%" cellSpacing=3 cellPadding=3 vAlign="top" align="center" bgcolor="#f4f4f4" border=0>
					<TR bgColor=#f4f4f2><TD align=left>
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
								<digi:trn key="aim:physicalProgress">Physical Progress</digi:trn>
							</digi:link>
							&gt; Overview &gt; 
							<bean:write name="aimPhysicalProgressForm" property="perspective"/> 
							Perspective
						</SPAN>
					</TD></TR>
					<TR bgColor=#f4f4f2><TD vAlign="top" align="center" width="100%">
						<TABLE width="98%" cellPadding=0 cellSpacing=0 vAlign="top" align="center" bgColor=#f4f4f2>
							<TR><TD width="100%" bgcolor="#F4F4F2" height="17">
								<TABLE border="0" cellpadding="0" cellspacing="0" bgcolor="#F4F4F2" height="17">
                          	<TR bgcolor="#F4F4F2" height="17">
										<TD bgcolor="#C9C9C7" class="box-title">&nbsp;&nbsp;
											<digi:trn key="aim:components">Components</digi:trn>
										</TD>
	                           <TD background="module/aim/images/corner-r.gif" height=17 width=17>
										</TD>
   	                     </TR>
      	               </TABLE>									
							</TD></TR>
							<TR><TD width="100%" bgcolor="#F4F4F2" align="center">
								<TABLE width="100%" cellPadding="2" cellSpacing="2" vAlign="top" align="center" bgColor=#f4f4f2
								class="box-border-nopadding">
									<TR><TD width="100%" vAlign="top" align="left">
										<TABLE width="100%" cellPadding="5" cellSpacing="1" vAlign="top" align="left" 
										bgcolor="#ffffff">
											<logic:notEmpty name="aimPhysicalProgressForm" property="components">
                    	<logic:iterate name="aimPhysicalProgressForm" property="components" 
											id="component" type="org.digijava.module.aim.helper.Components">
												<TR bgcolor="#f4f4f2">
													<TD>
														<jsp:useBean id="urlPP" type="java.util.Map" class="java.util.HashMap"/>
														<c:set target="${urlPP}" property="ampActivityId">
															<bean:write name="aimPhysicalProgressForm" property="ampActivityId"/>
														</c:set>
														<c:set target="${urlPP}" property="tabIndex" value="2"/>
														<c:set target="${urlPP}" property="compId">
															<bean:write name="component" property="componentId"/>
														</c:set>
														<digi:link href="/viewPhysicalProgressDetails.do" 
														name="urlPP">
														<bean:write name="component" property="title"/></digi:link><br>
														<i>Desc:</i> <bean:write name="component" property="description"/>
													</TD>
												</TR>
											</logic:iterate>
											</logic:notEmpty>
										</TABLE>
									</TD></TR>
								</TABLE>
							</TD></TR>
						</TABLE>
					</TD></TR>				
				</TABLE>
				<TR><TD bgcolor="#F4F4F2">
					<!-- issues --> 
					<TABLE width="96.5%" cellPadding=0 cellSpacing=0 vAlign="top" align="center" bgColor=#f4f4f2>
						<TR><TD width="100%" bgcolor="#F4F4F2" height="17">
							<TABLE border="0" cellpadding="0" cellspacing="0" bgcolor="#F4F4F2" height="17">
                     	<TR bgcolor="#F4F4F2" height="17"> 
                          	<TD bgcolor="#C9C9C7" class="box-title">&nbsp;&nbsp;
										<digi:trn key="aim:issues">Issues</digi:trn>
									</TD>
	                        <TD background="module/aim/images/corner-r.gif" height=17 width=17>
									</TD>
   	                  </TR>
      	            </TABLE>									
						</TD></TR>
						<TR><TD width="100%" bgcolor="#F4F4F2" align="center" class="box-border">
							<TABLE width="100%" cellPadding="0" cellSpacing="1" vAlign="top" align="center" bgColor=#dddddd>
	   	            	<logic:empty name="aimPhysicalProgressForm" property="issues">
									<TR bgcolor="#f4f4f2"><TD align="center"><font color="red"><digi:trn key="aim:noIssues">No issues
									</digi:trn></font></TD></TR>
								</logic:empty>
								<logic:notEmpty name="aimPhysicalProgressForm" property="issues">
									<logic:iterate name="aimPhysicalProgressForm" property="issues" id="issue" 
									type="org.digijava.module.aim.helper.Issues">
										<TR bgcolor="#f4f4f2"><TD width="100%" bgcolor="#F4F4F2" align="center">
											<TABLE width="100%" cellPadding="0" cellSpacing="0" vAlign="top" align="center"
											bgColor=#f4f4f2>
												<TR><TD width="100%" vAlign="top" align="left">
													<TABLE width="100%" cellPadding="2" cellSpacing="1" vAlign="top" align="left" 
													bgcolor="#ffffff">
														<TR bgcolor="#dfdfdf"><TD>
															<font color="#0000ff">Issue: </font><bean:write name="issue" property="name"/>
														</TD></TR>
														<logic:empty name="issue" property="measures">
															<TR><TD align="center">
																<font color="red">
																	<digi:trn key="aim:noMeasures">No measures</digi:trn>
																</font>	
															</TD></TR>
														</logic:empty>
														<logic:notEmpty name="issue" property="measures">
															<logic:iterate name="issue" property="measures" id="measure" 
															type="org.digijava.module.aim.helper.Measures">
																<TR><TD>
																	<TABLE width="95%" cellPadding="2" cellSpacing="1" vAlign="top" 
																	align="center" bgcolor="#dddddd">
																		<TR bgcolor="#f6f6f6"><TD>
																			<font color="#0000ff">Measure: </font>
																			<bean:write name="measure" property="name"/>
																		</TD></TR>
																		<logic:empty name="measure" property="actors">
																			<TR bgcolor="#ffffff"><TD align="center">
																			<font color="red">
																				<digi:trn key="aim:noActors">No actors</digi:trn>
																			</font>	
																			</TD></TR>
																		</logic:empty>
																		<logic:notEmpty name="measure" property="actors">
																			<TR bgcolor="#ffffff"><TD>
																				<TABLE width="100%" cellPadding="2" cellSpacing="1" 
																				vAlign="top" align="center" bgcolor="#ffffff">
																					<logic:iterate name="measure" property="actors" id="actor"
																					type="org.digijava.module.aim.dbentity.AmpActor">
																						<TR bgcolor="#ffffff"><TD>
																							<font color="#0000ff">Actor: </font>
																							<bean:write name="actor" property="name"/>
																						</TD></TR>
																					</logic:iterate>
																				</TABLE>
																			</TD></TR>
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
						</TD></TR>
					</TABLE>
					<TR><TD bgcolor="#F4F4F2">&nbsp</TD></TR>
				</TD></TR>
			</TABLE>
		<!-- end -->
	</TD></TR>
	<TR><TD>&nbsp;</TD></TR>
</TABLE>

</logic:equal>	
