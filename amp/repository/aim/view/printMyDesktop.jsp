<%@ page pageEncoding="UTF-8" %>
<%@ taglib uri="/taglib/struts-bean" prefix="bean" %>
<%@ taglib uri="/taglib/struts-logic" prefix="logic" %>
<%@ taglib uri="/taglib/struts-tiles" prefix="tiles" %>
<%@ taglib uri="/taglib/struts-html" prefix="html" %>
<%@ taglib uri="/taglib/digijava" prefix="digi" %>
<%@ taglib uri="/taglib/jstl-core" prefix="c" %>

<script language="JavaScript" type="text/javascript" src="<digi:file src="ampModule/aim/scripts/common.js"/>"></script>
<digi:context name="digiContext" property="context" />
<digi:errors/>
<body bgcolor="#ffffff" onLoad="init()" onunload="unload()">
<digi:form action="/filterDesktopActivities.do" method="POST">
<html:hidden name="aimDesktopForm" property="resetFliters" value="false"/>
<TABLE width="99%" cellspacing="1" cellpadding="4" valign="top" align="center">
	<TR>
      <TD>
		<TABLE border="0" cellpadding="0" cellspacing="0" width="580" align="center">
          <TR>
            <TD>
              <TABLE border="0" cellpadding="0" cellspacing="0" >
                <TR>
                  <TD bgColor=#c9c9c7 class=box-title>
                    &nbsp;<digi:trn key="aim:portfolio">Portfolio</digi:trn>
                  </TD>
                  <TD style="background-image:url(ampModule/aim/images/corner-r.gif);" height=17 width=17></TD>
                    <div align="right">
                      <input type="button" value="Close" onclick="window.close()" />
                    </div>

                </TR>
              </TABLE>
</TD></TR>

			<TR><TD bgColor=#ffffff class=box-border align="center">
				<TABLE border="0" cellPadding=4 cellspacing="1" width="580" >
                  <tr>
                    <td style="width:150pt;">
                      <b>
                        <c:if test="${!empty aimDesktopForm.selSectors}">
                          <c:forEach var="theSectors" items="${aimDesktopForm.selSectors}">
                          ${theSectors.name}
                          </c:forEach>
                        </c:if>
                        <c:if test="${empty aimDesktopForm.selSectors}">
                        --ALL--
                        </c:if>
                      </b>
                    </td>
                    <td style="width:150pt;">
                      <b>
                        <c:if test="${!empty aimDesktopForm.selDonors}">
                          <c:forEach var="slDonors" items="${aimDesktopForm.selDonors}">
                          ${slDonors.name}
                          </c:forEach>
                        </c:if>
                        <c:if test="${empty aimDesktopForm.selDonors}">
                        --ALL--
                        </c:if>
                      </b>
                    </td>
                    <td style="width:150pt;">
                      <b>
                        <c:if test="${!empty aimDesktopForm.selStatus}">
                          <c:forEach var="theStatus" items="${aimDesktopForm.selStatus}">
                          ${theStatus.name}
                          </c:forEach>
                        </c:if>
                        <c:if test="${empty aimDesktopForm.selStatus}">
                        --ALL--
                        </c:if>
                      </b>
                    </td>
                  </tr>

				<!-- Project List -->
				<TR><TD colspan="3">
					<TABLE width="100%" cellpadding="4" cellSpacing="1" bgcolor="#ffffff" valign="top" align="left">
						<TR bgcolor="#DDDDDD" height="30">
							<TD class="colHeaderLink" onMouseOver="this.className='colHeaderOver'"
							onMouseOut="this.className='colHeaderLink'" width="40%"
							onClick="window.document.location='/aim/previewPrintMyDesktop.do?view=sorted&srt=1'"
							title='<digi:trn jsFriendly="true" key="aim:ProjectNames">Complete List of Projects for Team</digi:trn>'>
								<digi:trn key="aim:project">Project</digi:trn>
								<c:if test="${aimDesktopForm.srtFld == 1}">
									<c:if test="${aimDesktopForm.srtAsc == true}">
										<img src= "../ampTemplate/images/down.gif" align=absmiddle border="0">
									</c:if>
									<c:if test="${aimDesktopForm.srtAsc == false}">
										<img src= "../ampTemplate/images/up.gif" align=absmiddle border="0">
									</c:if>
								</c:if>
							</TD>
							<TD class="colHeaderLink" onMouseOver="this.className='colHeaderOver'"
							onMouseOut="this.className='colHeaderLink'"  width="14%"
							onClick="window.document.location='/aim/previewPrintMyDesktop.do?view=sorted&srt=2'"
							title='<digi:trn jsFriendly="true" key="aim:IdforAMP">System Generated Project ID</digi:trn>'>
								<digi:trn key="aim:ampId">AMP ID</digi:trn>
								<c:if test="${aimDesktopForm.srtFld == 2}">
									<c:if test="${aimDesktopForm.srtAsc == true}">
										<img src= "../ampTemplate/images/down.gif" align=absmiddle border="0">
									</c:if>
									<c:if test="${aimDesktopForm.srtAsc == false}">
										<img src= "../ampTemplate/images/up.gif" align=absmiddle border="0">
									</c:if>
								</c:if>
							</TD>
							<TD class="colHeaderLink" onMouseOver="this.className='colHeaderOver'"
							onMouseOut="this.className='colHeaderLink'"  width="28%"
							onClick="window.document.location='/aim/previewPrintMyDesktop.do?view=sorted&srt=3'"
							title='<digi:trn jsFriendly="true" key="aim:FundingDonor">Funding Donor for Project</digi:trn>'>
								<digi:trn key="aim:donor">Donor(s)</digi:trn>
								<c:if test="${aimDesktopForm.srtFld == 3}">
									<c:if test="${aimDesktopForm.srtAsc == true}">
										<img src= "../ampTemplate/images/down.gif" align=absmiddle border="0">
									</c:if>
									<c:if test="${aimDesktopForm.srtAsc == false}">
										<img src= "../ampTemplate/images/up.gif" align=absmiddle border="0">
									</c:if>
								</c:if>
							</TD>
							<TD class="colHeaderLink" onMouseOver="this.className='colHeaderOver'"
							onMouseOut="this.className='colHeaderLink'"  width="18%"
							onClick="window.document.location='/aim/previewPrintMyDesktop.do?view=sorted&srt=4'"
							title='<digi:trn jsFriendly="true" key="aim:TotalCommitMade">Total Committed Amount of Project</digi:trn>'>
								<FONT color="blue">*</FONT>
								<digi:trn key="aim:totalCommitments">Total Commitments</digi:trn>
								<c:if test="${aimDesktopForm.srtFld == 4}">
									<c:if test="${aimDesktopForm.srtAsc == true}">
										<img src= "../ampTemplate/images/down.gif" align=absmiddle border="0">
									</c:if>
									<c:if test="${aimDesktopForm.srtAsc == false}">
										<img src= "../ampTemplate/images/up.gif" align=absmiddle border="0">
									</c:if>
								</c:if>
							</TD>
						</TR>
						<c:set var="translation">
							<digi:trn key="aim:clickToViewProjectDetails">Click here to view Project Details</digi:trn>
						</c:set>
						<logic:notEmpty name="aimDesktopForm" property="activities">
							<c:forEach var="project" items="${aimDesktopForm.activities}"
							begin="${aimDesktopForm.stIndex}" end="${aimDesktopForm.edIndex - 1}">
								<TR bgcolor="#f4f4f2">
									<TD>
										<jsp:useBean id="urlChannelOverview" type="java.util.Map" class="java.util.HashMap"/>
										<c:set target="${urlChannelOverview}" property="ampActivityId">
											<bean:write name="project" property="ampActivityId"/>
										</c:set>
										<c:set target="${urlChannelOverview}" property="tabIndex" value="0"/>
										<div title='${translation}'>
											<c:out value="${project.name}" />
                                        </div>
										<c:if test='${project.approvalStatus == "started"}'>
											<FONT size="2" color="#FF0000">*</FONT>
										</c:if>
										<c:if test='${project.approvalStatus == "created"}'>
											<FONT size="2" color="#008000">*</FONT>
										</c:if>
										<c:if test='${project.approvalStatus == "edited"}'>
											<FONT size="2" color="#008000">**</FONT>
										</c:if>
									</TD>
									<TD>
										<c:out value="${project.ampId}" />
									</TD>
									<TD>
										<c:if test="${!empty project.donor}">
											<TABLE cellspacing="1" cellpadding="1">
												<c:forEach var="dnr" items="${project.donor}">
													<TR><TD>
														<c:out value="${dnr.donorName}" />
													</TD></TR>
												</c:forEach>
											</TABLE>
										</c:if>
										<c:if test="${empty project.donor}">
											<digi:trn key="aim:unspecified">Unspecified</digi:trn>
										</c:if>
									</TD>
									<TD align="right">
										<c:out value="${project.totalCommited}" />
									</TD>
								</TR>
							</c:forEach>
						</logic:notEmpty>
						<TR bgcolor="#FFFFFF">
							<TD>&nbsp;</TD>
							<TD>
								<b><c:out value="${aimDesktopForm.defCurrency}" /></b>
							</TD>
							<TD align="right">
								<b><digi:trn key="aim:total">Total</digi:trn></b>
							</TD>
							<TD align="right">
								<b><c:out value="${aimDesktopForm.totalCommitments}" /></b>
							</TD>
						</TR>
					</TABLE>
				</TD></TR>
				</TABLE>
			</TD></TR>
		</TABLE>
	</TD></TR>
</TABLE>

</digi:form>
</body>
<script language="JavaScript" type="text/javascript">
function load(){
  window.print();
}
</script>



