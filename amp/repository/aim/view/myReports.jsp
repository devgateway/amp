<%@ page pageEncoding="UTF-8" %>
<%@ taglib uri="/taglib/struts-bean" prefix="bean" %>
<%@ taglib uri="/taglib/struts-logic" prefix="logic" %>
<%@ taglib uri="/taglib/struts-html" prefix="html" %>
<%@ taglib uri="/taglib/digijava" prefix="digi" %>
<%@ taglib uri="/taglib/jstl-core" prefix="c" %>
<%@ taglib uri="/taglib/fieldVisibility" prefix="field" %>
<%@ taglib uri="/taglib/featureVisibility" prefix="feature" %>
<%@ taglib uri="/taglib/moduleVisibility" prefix="ampModule" %>
<%@ taglib uri="/taglib/jstl-functions" prefix="fn" %>

<script type="text/javascript">
function popup(mylink, windowname)
{
	if (! window.focus)return true;

	var href;

	if (typeof(mylink) == 'string')
	   href=mylink;
	else
	   href=mylink.href;
	
	if(windowname == ""){
		windowname="popup"+new Date().getTime();;
	}

  	myWindow	= window.open('',windowname,'channelmode=no,directories=no,menubar=no,resizable=yes,status=no,toolbar=no,scrollbars=yes,location=yes');
  	myWindow.document.write("<html>");
	myWindow.document.write("<div style='height: 20px; left: 45%; position: absolute; text-align: center; top: 0%;width: 230px;padding: 5px;background-color:#27415F;font-family: arial; font-size: 14px;text-align: center;font-weight:bold;color: white;'>");
	myWindow.document.write("<digi:trn jsFriendly='true'>Loading...</digi:trn>");
	myWindow.document.write("<div><html>");
	myWindow.focus();
	
	if(navigator.appName.indexOf('Microsoft Internet Explorer') > -1){ //Workaround to allow HTTP REFERER to be sent in IE (AMP-12638)
		var referLink = document.createElement('a');
		referLink.href = href;
		referLink.target = windowname;
		document.body.appendChild(referLink);
		referLink.click();
	}
	else
	{
		myWindow.location = href;
	}
	return false;
}
</script>
<c:set var="translation2">
	<digi:trn key="aim:clickToViewMoreReports">Click here to view More Reports</digi:trn>
</c:set>
<div class="right_menu" style="margin-top:20px;">
	<div class="right_menu_header">
		<div class="right_menu_header_cont">	
			<digi:trn key="aim:portfolioReports">Reports</digi:trn>
		</div>
	</div>
	<div class="right_menu_box">
		<div class="right_menu_cont">
        <ul>
			<logic:notPresent name="currentMember" scope="session">
				<logic:empty name="myReports" scope="session">
						<digi:trn key="aim:noPublicReports">No public reports</digi:trn>
				</logic:empty>
			</logic:notPresent>
			<logic:notEmpty name="lastViewedReports" scope="session">
			<logic:iterate name="lastViewedReports" id="report" scope="session" type="org.digijava.ampModule.aim.dbentity.AmpReports" length="5">
				<li class="tri tri-desktop">
					<a title="${report.name}" href="${fn:getReportUrl(report)}" class="triText" onclick="return popup(this,'');">
						<c:choose>
	                    	<c:when test="${fn:length(report.name) > 50}" >
	                    	    <c:out value="${fn:substring(report.name, 0, 50)}"/>...
	                    	</c:when>
	                    	<c:otherwise>
	                    	    <c:out value="${report.name}"/>
	                    	</c:otherwise>
	                    </c:choose>
					</a>
				</li>
			</logic:iterate>
			<bean:size id="repCount" name="lastViewedReports" scope="session" />
                   <div style="padding-top:10px;margin-left:12px;margin-top:5px; margin-bottom: 7px" title="<digi:trn key="aim:clickToViewMoreReports">Click here to view More Reports</digi:trn>">
                  
                    	 <digi:link href="/viewTeamReports.do?tabs=false&reset=true&onlyFavourites=false">
                            <digi:trn key="aim:moreReports">More Reports...</digi:trn>
                        </digi:link>
                    </div>
           	</logic:notEmpty>
            </ul>
			<logic:present name="currentMember" scope="session">
				<logic:empty name="lastViewedReports" scope="session">
					<p class="right_menu_empty"><digi:trn key="aim:noReportHaveBeenViewed">No reports have been viewed.</digi:trn></p>
				</logic:empty>
			</logic:present>
		</div>
	</div>
</div>
