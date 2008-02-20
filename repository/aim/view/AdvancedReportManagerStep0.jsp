<%@ page pageEncoding="UTF-8" %>
<%@ taglib uri="/taglib/struts-bean" prefix="bean" %>
<%@ taglib uri="/taglib/struts-logic" prefix="logic" %>
<%@ taglib uri="/taglib/struts-tiles" prefix="tiles" %>
<%@ taglib uri="/taglib/struts-html" prefix="html" %>
<%@ taglib uri="/taglib/digijava" prefix="digi" %>
<%@ taglib uri="/taglib/jstl-core" prefix="c" %>
<%@ taglib uri="/taglib/category" prefix="category" %>
<%@ taglib uri="/taglib/globalsettings" prefix="gs" %>
<%@ taglib uri="/taglib/fieldVisibility" prefix="field" %>
<%@ taglib uri="/taglib/featureVisibility" prefix="feature" %>
<%@ taglib uri="/taglib/moduleVisibility" prefix="module" %>

<digi:instance property="aimAdvancedReportForm" />
<digi:form action="/advancedReportManager.do" method="post">

<script language="JavaScript" type="text/javascript" src="<digi:file src="module/aim/scripts/advanceReportManager.js"/>"></script>

<script language="JavaScript">
function check(){
  option = -1;
  for (i = document.aimAdvancedReportForm.reportType.length-1; i > -1; i--){
    if (document.aimAdvancedReportForm.reportType[i].checked){
      option = i;
      i = -1;
    }
  }
  if (option == -1){
	 alert('<digi:trn key="aim:reportBuilder:ReportTypeValidation">You must choose a report type!</digi:trn>');
    return false;
  }
  return true;
}
function gotoStep() {
	if(${aimAdvancedReportForm.reportEdit}== 'false'){
  if (!check())
  return false;
	}  
  <digi:context name="step" property="context/module/moduleinstance/advancedReportManager.do?check=SelectCols" />
  document.aimAdvancedReportForm.action = "<%= step %>";
  document.aimAdvancedReportForm.target = "_self";
  document.aimAdvancedReportForm.submit();
  return true;
  //	}
}

</script>



  <html:hidden property="moveColumn"/>

  <TABLE cellSpacing=0 cellPadding=0 align="center" vAlign="top" border=0 width="100%">
    <tr>
      <td>
        <jsp:include page="teamPagesHeader.jsp" flush="true" />
      </td>
    </tr>



    <tr>

      <td width="100%" vAlign="top" align="left">
        <table bgColor=#ffffff cellPadding=0 cellSpacing=0 width="770" vAlign="top" align="left" border=0>
          <tr>

            <td class=r-dotted-lg align=left vAlign=top >	&nbsp;</td>
            <td>
              <table>
                <tr>
                  <td>
                    <table cellPadding=5 cellSpacing=0 width="100%">
                      <tr>
                        <td height=33><span class=crumb>
                          <c:set var="translation">
                            <digi:trn key="aim:clickToViewMyDesktop">Click here to view MyDesktop</digi:trn>
                          </c:set>
                          <digi:link href="/viewMyDesktop.do" styleClass="comment" title="${translation}" >
                            <digi:trn key="aim:portfolio">
                            Portfolio
                            </digi:trn>
                          </digi:link>&nbsp;&gt;&nbsp;
                          <c:if test="${aimAdvancedReportForm.reportEdit==false}">
                          <digi:trn key="aim:reportBuilder:selectReportType">
                          Report Builder : Select Report Type
                          </digi:trn>
                          </c:if>
                          <c:if test="${aimAdvancedReportForm.reportEdit==true}">
                          	<digi:trn key="aim:reportBuilder:selectedReportType">
                          		Report Builder :  Report Type
                          	</digi:trn>
                          </c:if>
                          &gt;</span>
                        </td>
                      </tr>
                    </table>
                  </td>
                </tr>
                <tr>

                  <td height=16 vAlign=right align=center>
                    <span class=subtitle-blue>
                      <c:if test="${aimAdvancedReportForm.reportEdit==false}">
                      <digi:trn key="aim:reportBuilder:selectReportType">
                      Report Builder : Select Report Type
                      </digi:trn>
                          </c:if>
                          <c:if test="${aimAdvancedReportForm.reportEdit==true}">
                          	<digi:trn key="aim:reportBuilder:selectedReportType">
                          		Report Builder :  Report Type
                          	</digi:trn>
                          </c:if>
                    </span>
                  </td>
                </tr>
                <tr colspan="2">
                  <td class=box-title align="right" valign="top">
                  	<c:if test="${aimAdvancedReportForm.reportEdit==true}">
                  		<img src="module/aim/images/arrow-014E86.gif"><digi:trn key="aim:report:Type">Report Type :</digi:trn>
                  		<bean:write name="aimAdvancedReportForm" property="arReportType"/>
                  	</c:if>
                  	<c:if test="${aimAdvancedReportForm.reportEdit==false}">
                  		<img src="module/aim/images/arrow-014E86.gif">
                  		<digi:trn key="aim:reportBuilder:reportTypePleaseSelect">Report Type : Please Select</digi:trn>
                  	</c:if>                    
                      <td>
                </tr>
                <TR>
                  <TD vAlign="top" align="center">
                    <TABLE width="100%" cellSpacing=0 cellPadding=0 vAlign="top" align="left" bgcolor="#f4f4f4" class="box-border-nopadding">
                      <TR>
                        <TD bgcolor="#f4f4f4">
                          <TABLE width="100%" cellSpacing=1 cellPadding=0 valign="top" align="left" bgcolor="#f4f4f4">
                            <jsp:include page="AdvancedReportManagerMenu.jsp" flush="true"/>
                            <TR bgColor=#f4f4f2>
                              <TD vAlign="top" align="left" width="100%">
                              </TD>
                            </TR>

                            <TR bgColor=#f4f4f2>
                              <TD vAlign="top" align="center" width="100%" bgColor=#f4f4f2>
                                <TABLE width="98%" cellPadding=0 cellSpacing=0 vAlign="top" align="center" bgColor=#f4f4f2 >
                                  <TR>
                                    <TD width="100%" align="center"  valign=top>
                                      <br>
                                      <br>
                                      <br>
                                      <!-- Radios -->
										
                                      <table cellPadding=0 cellSpacing=0 vAlign="top" align="center" width="400" bgColor=#f4f4f2 border="0">
                                        <c:if test="${aimAdvancedReportForm.reportEdit==false}">
                                        <tr height="120">
                                          <td>&nbsp;&nbsp;&nbsp;
                                          </td>
                                          		<td align="left">
                                          	<gs:test name="<%= org.digijava.module.aim.helper.GlobalSettingsConstants.ACTIVITY_LEVEL %>" compareWith="true" onTrueEvalBody="true">
                                          	<table cellPadding=0 cellSpacing=1 bgColor=#f4f4f2 border="0">
                                          		<tr>
                                          			<td>
                                          			<category:showoptions listView="false" name="aimAdvancedReportForm" property="activityLevel" keyName="<%= org.digijava.module.aim.helper.CategoryConstants.ACTIVITY_LEVEL_KEY %>" styleClass="inp-text"  />
                                          			</td>
                                          		</tr>
                                          	</table>
                                          	<hr />
                                          	</gs:test>
                                          	<digi:errors />
                                            <table cellPadding=0 cellSpacing=1 bgColor=#f4f4f2 border="0">
                                              <feature:display name="Donor Report" module="Reports">
                                              <tr>
                                                <td>
                                                  <html:radio property="reportType" value="donor" >
                                                    <digi:trn key="aim:donorReport">
                                                    Donor Report (Donor Funding)
                                                    </digi:trn>
                                                  </html:radio>
                                                </td>
                                              </tr>
                                              </feature:display>
                                              <feature:display name="Regional Report" module="Reports">										
                                              <tr>
                                                <td>
                                                  <html:radio property="reportType" value="regional" >
                                                    <digi:trn key="aim:regionalReport">
                                                    Regional Report (Regional Funding)
                                                    </digi:trn>
                                                  </html:radio>
                                                </td>
                                              </tr>
                                              </feature:display>
                                              <feature:display name="Component Report" module="Reports">
                                              <tr>
                                                <td>
                                                  <html:radio property="reportType" value="component" >
                                                    <digi:trn key="aim:componentReport">
                                                    Component Report (Component Funding)
                                                    </digi:trn>
                                                  </html:radio>
                                                </td>
                                              </tr>
                                              </feature:display>
                                                <feature:display module="Reports" name="Contribution Report">
                                                  <tr>
                                                    <td>
                                                      <html:radio property="reportType" value="contribution" >
                                                        <digi:trn key="aim:contributionReport">
                                                        Contribution Report (Activity Contributions)
                                                        </digi:trn>
                                                      </html:radio>
                                                    </td>
                                                  </tr>
                                                </feature:display>
                                            </table>
                                          </td>
                                        </tr>
                                      </table>
                                          		</td>
		                                	</tr>
                                        </c:if>
                                        <c:if test="${aimAdvancedReportForm.reportEdit==true}">												
											<tr height="120">
                                          		<td>&nbsp;&nbsp;&nbsp;
		                                                </td>
                                          		<td align="left">
                                          			<table cellPadding=0 cellSpacing=1 bgColor=#f4f4f2 border="0">
                                          			<gs:test name="<%= org.digijava.module.aim.helper.GlobalSettingsConstants.ACTIVITY_LEVEL %>" compareWith="true" onTrueEvalBody="true">
		                                          		<tr>
		                                          			<td>
		                                          			<category:showoptions listView="false" name="aimAdvancedReportForm" property="activityLevel" keyName="<%= org.digijava.module.aim.helper.CategoryConstants.ACTIVITY_LEVEL_KEY %>" 
		                                          			styleClass="inp-text" innerdisabled="true"  />
		                                          			<hr />		                                          			
		                                          			</td>
		                                          		</tr>
		                                          	</gs:test>
                                          			<feature:display name="Donor Report" module="Reports">
			                                              <tr>
			                                                <td>
			                                                  <html:radio property="reportType" value="donor" disabled="true">
			                                                    <digi:trn key="aim:donorReport">
			                                                    Donor Report (Donor Funding)
			                                                    </digi:trn>
			                                                  </html:radio>
			                                                </td>
		                                              </tr>		                                        												
		                                              </feature:display>
		                                             <feature:display name="Regional Report" module="Reports">										
			                                              <tr>
			                                                <td>
			                                                  <html:radio property="reportType" value="regional" disabled="true">
			                                                    <digi:trn key="aim:regionalReport">
			                                                    Regional Report (Regional Funding)
			                                                    </digi:trn>
			                                                  </html:radio>
			                                                </td>
			                                              </tr>
			                                          </feature:display>
			                                          <feature:display name="Component Report" module="Reports">										
			                                              <tr>
			                                                <td>
			                                                  <html:radio property="reportType" value="component" disabled="true">
			                                                    <digi:trn key="aim:componentReport">
			                                                    Component Report (Component Funding)
			                                                    </digi:trn>
			                                                  </html:radio>
			                                                </td>
			                                              </tr>
	                                              </feature:display>
			                                                <feature:display module="Reports" name="Contribution Report">
			                                                  <tr>
			                                                    <td>
			                                                      <html:radio property="reportType" value="contribution" disabled="true">
			                                                        <digi:trn key="aim:contributionReport">
			                                                        Contribution Report (Activity Contributions)
			                                                        </digi:trn>
			                                                      </html:radio>
			                                                    </td>
			                                                  </tr>
			                                                </feature:display>
			                                            </table>
			                                          </td>
			                                        </tr>
			                                      </table>
                                          		</td>
		                                	</tr> 
											</c:if>                                       
		                              </table>

                                      <p>&nbsp;</p>
                                      <p>&nbsp;</p>
                                    </TD>
                                  </TR>
                                  <tr>
                                    <td align="right">
                                      <c:set var="message">
                                        <digi:trn key="aim:reports:DataNotSaved">Do you really want to quit Report Generator? \nWarning: All your Current Data Will be Lost... press OK to QUIT Report Generator.</digi:trn>
                                      </c:set>
                                      <html:button  styleClass="dr-menu" property="submitButton"  onclick="return quitAdvRptMngr('${message}')">
                                        <digi:trn key="btn:cancel">Cancel</digi:trn>
                                      </html:button>
                                      <html:button  styleClass="dr-menu" property="submitButton"  onclick="javascript:gotoStep()">
                                        <digi:trn key="btn:next">Next</digi:trn>
                                      </html:button>

                                    </td>
                                  </tr>
                                </TABLE>
                              </TD>
                            </TR>
                          </TABLE>
                        </TD>
                      </TR>
              </table>
                      </td>
                      <td class=r-dotted-lg align=left vAlign=top >	&nbsp;</td>
          </tr>
        </table>
            </td>
</TR>
  </TABLE>


</digi:form>



