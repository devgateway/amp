<%@taglib uri="/taglib/struts-bean" prefix="bean"%>
<%@taglib uri="/taglib/struts-logic" prefix="logic"%>
<%@taglib uri="/taglib/struts-tiles" prefix="tiles"%>
<%@taglib uri="/taglib/struts-html" prefix="html"%>
<%@taglib uri="/taglib/digijava" prefix="digi"%>
<%@ taglib uri="/taglib/jstl-core" prefix="c" %>



<html>
<body bgcolor="#ffffff">
<digi:form method="post" action="/exceptionReport.do" ampModule="exception">

<script language="JavaScript">
<!--
    
    function toggleLayer(whichLayer){
		var elem, vis;
		if( document.getElementById ) // this is the way the standards work
			elem = document.getElementById( whichLayer );
		else 
			if( document.all ) // this is the way old msie versions work
				elem = document.all[whichLayer];
			else 
				if( document.layers ) // this is the way nn4 works
					elem = document.layers[whichLayer];
		vis = elem.style;
		// if the style.display value is blank we try to figure it out here
		if(vis.display==''&&elem.offsetWidth!=undefined&&elem.offsetHeight!=undefined)
			vis.display = (elem.offsetWidth!=0&&elem.offsetHeight!=0)?'block':'none';
		vis.display = (vis.display==''||vis.display=='block')?'none':'block';
	}

	var responseSuccess = function(o){ 
	/* Please see the Success Case section for more
	 * details on the response object's properties.
	 * o.tId
	 * o.status
	 * o.statusText
	 * o.getResponseHeader[ ]
	 * o.getAllResponseHeaders
	 * o.responseText
	 * o.responseXML
	 * o.argument
	 */
		var response = o.responseText; 
		var content = document.getElementById("docDiv");
	    //response = response.split("<!")[0];
		content.innerHTML = response;
	}
		 
	var responseFailure = function(o){ 
		alert("Connection Failure!"); 
	}
	  
	var callback = 
	{ 
		success:responseSuccess, 
		failure:responseFailure 
	};
	
	function addContract(){
	    var postString = 'rand=<bean:write name="exceptionReportForm" property="rand"/>';
		YAHOOAmp.util.Connect.asyncRequest("POST", "/exception/getConfluenceDocs.do", callback, postString);
	}


	var current2 = window.onload;
	window.onload = function() {
        current2.apply(current2);
		//addContract();
	};
	
-->    
</script>

  <html:hidden property="exceptionInfo.sourceURL"/>
  <html:hidden property="exceptionInfo.exceptionCode"/>
  <html:hidden property="exceptionInfo.errorMessage"/>
  <html:hidden property="exceptionInfo.siteId"/>
  <html:hidden property="exceptionInfo.siteKey"/>
  <html:hidden property="exceptionInfo.siteName"/>
  <html:hidden property="exceptionInfo.stackTrace"/>
  <html:hidden property="name"/>
  <html:hidden property="email"/>
  <table width="100%">
    <tr>
      <td align="center">
        	<table width="85%" cellpadding="5">
        		<tr>
        			<td width="120px" valign="top" >
        				<img alt="AMP Error Image" src="../ampTemplate/images/exceptionImg.jpg" />
        			</td>
        			<td align="left" valign="middle" style="background-color: #F0F0F0">
        				<br/>
        				<span style="font-size:14px">
				        	<b><digi:trn key="exception:newErrorText2">The Aid Management Platform has temporarily encountered an issue. We apologize for any inconvenience. </digi:trn></b><br/>
				        	<c:if test="<%= org.digijava.ampModule.aim.util.FeaturesUtil.getGlobalSettingValue(org.digijava.ampModule.aim.helper.Constants.GLOBALSETTINGS_ECS).compareTo(\"true\") == 0 %>">
					        	<b><font color="blue"><digi:trn key="exception:errorHandled">This issue has been reported to the technical support team for resolution.</digi:trn></font></b><br/>
				        	</c:if>
				        	<br/>
				        	<!-- 
				        	<u><digi:trn key="exception:issueInformation">Issue Information</digi:trn></u><br/>
				        	<digi:trn key="exception:issueDescription">Issue Description</digi:trn>:
				        	<logic:notEmpty name="exceptionReportForm" property="exceptionInfo.userMessage">
				        		"<i><bean:write name="exceptionReportForm" property="exceptionInfo.userMessage"/></i>"
				        	</logic:notEmpty> 
				        	<logic:empty name="exceptionReportForm" property="exceptionInfo.userMessage">
				        		"<digi:trn key="exception:na">N/A</digi:trn>"
				        	</logic:empty>
				        	<br/>
				        	<logic:notEmpty name="exceptionReportForm" property="exceptionInfo.mainTag">
					        	<digi:trn key="exception:relatedTo">Related To</digi:trn>: "<i><bean:write name="exceptionReportForm" property="exceptionInfo.mainTag"/></i>" <br/>
				        	</logic:notEmpty>
				        	<br/>
				        	 -->
				        	<hr/><br/>
				        	<digi:trn key="exception:continueInfo">To resume normal operation of the Aid Management Platform please choose one of the following actions</digi:trn>:<br />
				        		<li><digi:trn key="exception:goBackToThe">Go Back to the</digi:trn>
				        			<a href="<bean:write name="exceptionReportForm" property="exceptionInfo.backLink"/>">
					        			<digi:trn key="exception:previousPage">
					        				previous page
				        				</digi:trn>
				        			</a>
				        		</li> 
				        		<li><digi:trn key="exception:goToThe">Go to the</digi:trn> 
					        		<logic:present name="ampAdmin" scope="session">
										<logic:equal name="ampAdmin" value="yes">
											<digi:link href="/admin.do" ampModule="aim" >
						                        <digi:trn key="aim:adminPage">Admin Page</digi:trn>
						                    </digi:link>
										</logic:equal>
									</logic:present>
									<logic:present name="ampAdmin" scope="session">
										<logic:equal name="ampAdmin" value="no">
				                              <a href="/showDesktop.do" >
				                                 <digi:trn key="aim:desktop">Desktop</digi:trn>
				                              </a>
										</logic:equal>
									</logic:present>
								</li>
							<br/>
							<digi:trn key="exception:viewThe">View the</digi:trn>	
				        		<a href="javascript:toggleLayer('devInfo');"> <digi:trn key="exception:developerNotes">Developer notes</digi:trn></a> <digi:trn key="exception:forThisIssue">for this issue</digi:trn> 
				        </span>
        			</td>
        		</tr>
        		<tr>
        			<td width="120px">&nbsp;
        				
        			</td>
        			<td style="background-color: #F0F0F0">
       					<div id="devInfo" style="display: none;"  >
       						<div style="display: none;">
							<fieldset>
					          <legend>
					       		<a href="javascript:toggleLayer('docDiv');" style="color:#FFFFFF;">
				            		<digi:trn key="exception:suggestedDocumentation">Suggested Documentation</digi:trn>
						        </a>
					          </legend>
					          <div id="docDiv" style="display: block;">
						          <p align="center">
						          	  <img src="/TEMPLATE/ampTemplate/images/ajax-loader.gif" alt="Loading..."/>
						          </p>
							  </div>
							</fieldset>
							</div>
							
							<fieldset>
					          <legend>
					          	<a href="javascript:toggleLayer('errMsg');" style="color:#FFFFFF;">
				            		<digi:trn key="exception:exceptionDetails">Exception details</digi:trn>
					            </a>
					          </legend>
					          
					          <div id="errMsg" style="display: block;">
						          <table width="100%" border="0">
						            <tr>
						              <td>Error Message:</td>
						              <td>
						                <b>
						                  <font color="red">
						                    <bean:write name="exceptionReportForm" property="exceptionInfo.errorMessage"/>
						                  </font>
						                </b>
						              </td>
						            </tr>
						            <logic:notEmpty name="exceptionReportForm" property="exceptionInfo.tags">
							            <tr>
							              <td>Tags:</td>
							              <td><bean:write name="exceptionReportForm" property="exceptionInfo.tags"/></td>
										</tr>
						            </logic:notEmpty>
						            <logic:notEmpty name="exceptionReportForm" property="exceptionInfo.exceptionCode">
							            <tr>
							              <td>Status code:</td>
							              <td><bean:write name="exceptionReportForm" property="exceptionInfo.exceptionCode"/></td>
									    </tr>
								    </logic:notEmpty>
								    <tr>
								      <td>Site:</td>
								      <td>
								        <bean:write name="exceptionReportForm" property="exceptionInfo.siteName"/>
								        (#
								        <bean:write name="exceptionReportForm" property="exceptionInfo.siteId"/>
								        /
								        <bean:write name="exceptionReportForm" property="exceptionInfo.siteKey"/>
								        )
								      </td>
								    </tr>
								    <tr>
								      <td>Module instance:</td>
								      <td>
								        <bean:write name="exceptionReportForm" property="exceptionInfo.moduleName"/>
								        :
								        <bean:write name="exceptionReportForm" property="exceptionInfo.instanceName"/>
								      </td>
								    </tr>
								    <tr>
								      <td width="100%" colspan="2">&nbsp;</td>
								    </tr>
								</table>
							</div>
							</fieldset>
							
							<logic:notEmpty name="exceptionReportForm" property="exceptionInfo.stackTrace">
								<fieldset>
						          <legend>
						          	<a href="javascript:toggleLayer('stackDiv');" style="color:#FFFFFF;">
							            <digi:trn key="exception:stackTrace">Stack trace</digi:trn>
						            </a>
						          </legend>
						          <div id="stackDiv" style="display: none;">
				                  	<html:textarea style="width:100%;height:350px" readonly="true" name="exceptionReportForm" property="exceptionInfo.stackTrace" />
				                  </div>
						        </fieldset>
							</logic:notEmpty>

						</div>
        			</td>
        		</tr>
        	</table>
      		<br/><br/><br/><br/><br/><br/><br/><br/><br/><br/><br/><br/>
      </td>
</table></digi:form>
</body>
</html>
