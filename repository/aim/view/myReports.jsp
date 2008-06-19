<%@ page pageEncoding="UTF-8" %>
<%@ taglib uri="/taglib/struts-bean" prefix="bean" %>
<%@ taglib uri="/taglib/struts-logic" prefix="logic" %>
<%@ taglib uri="/taglib/struts-html" prefix="html" %>
<%@ taglib uri="/taglib/digijava" prefix="digi" %>
<%@ taglib uri="/taglib/jstl-core" prefix="c" %>
<%@ taglib uri="/taglib/fieldVisibility" prefix="field" %>
<%@ taglib uri="/taglib/featureVisibility" prefix="feature" %>
<%@ taglib uri="/taglib/moduleVisibility" prefix="module" %>

<SCRIPT TYPE="text/javascript">
<!--
function popup(mylink, windowname)
{
if (! window.focus)return true;
var href;
if (typeof(mylink) == 'string')
   href=mylink;
else
   href=mylink.href;
window.open(href, windowname,'channelmode=no,directories=no,menubar=no,resizable=yes,status=no,toolbar=no,scrollbars=yes,location=yes');
return false;
}
//-->
</SCRIPT>
<br/>
<div id="content"  class="yui-skin-sam" style="width:100%;"> 
	<div id="demo" class="yui-navset" style="font-family:Arial, Helvetica, sans-serif;">
                        <ul class="yui-nav">
                          <li class="selected">
                          <a title='<digi:trn key="aim:PortfolioOfReports">Portfolio Reports </digi:trn>'>
                          <div>
                          	<digi:trn key="aim:portfolioReports">Reports</digi:trn>
                          </div>
                          </a>
                          </li>
                        </ul>
                        <div class="yui-content" style="height:auto;font-size:11px;font-family:Verdana,Arial,Helvetica,sans-serif;">

				<% int rCount = 0; %>

				<c:set var="translation">
					<digi:trn key="aim:clickToViewReport">Click here to view Report</digi:trn>
				</c:set>
				<logic:notPresent name="currentMember" scope="session">
				<logic:empty name="myReports" scope="session">
						<digi:trn key="aim:noPublicReports">No public reports</digi:trn>
				</logic:empty>
				</logic:notPresent>
				
				<logic:notEmpty name="myReports" scope="session">

					<logic:iterate name="myReports" id="report" scope="session" type="org.digijava.module.aim.dbentity.AmpReports" length="5">
						<div style="margin:2px">
							<IMG alt=Link height=10 src="../ampTemplate/images/arrow-gr.gif" width=10>
							<digi:link href="/viewNewAdvancedReport.do?view=reset&widget=false" paramName="report"  paramId="ampReportId" paramProperty="ampReportId" onclick="return popup(this,'');">
								<bean:write name="report" property="name"/>
							</digi:link>
							</div>
					</logic:iterate>

                    <bean:size id="repCount" name="myReports" scope="session" />
                    <div style="margin-left:12px;margin-top:5px; margin-bottom: 7px" title="'<digi:trn key="aim:clickToViewMoreReports">Click here to view More Reports</digi:trn>'>">
                  
                    	 <digi:link href="/viewTeamReports.do">
                            <digi:trn key="aim:moreReports">More Reports...</digi:trn>
                        </digi:link>
                    </div>
				</logic:notEmpty>
				<logic:present name="currentMember" scope="session">
                    <logic:empty name="myReports" scope="session">
                            <digi:trn key="aim:noReportForThisWorkspace">No reports for this workspace</digi:trn>
                    </logic:empty>
				</logic:present>
