<%@taglib uri="/taglib/struts-bean" prefix="bean"%>
<%@taglib uri="/taglib/struts-logic" prefix="logic"%>
<%@taglib uri="/taglib/struts-tiles" prefix="tiles"%>
<%@taglib uri="/taglib/struts-html" prefix="html"%>
<%@taglib uri="/taglib/digijava" prefix="digi"%>
<%@ taglib uri="/taglib/jstl-core" prefix="c" %>



<html>
<body bgcolor="#ffffff">
<digi:form method="post" action="/exceptionReport.do" module="exception">

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
		addContract();
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
        	<table width=100%>
        		<tr>
        			<td>
        				<img alt="AMP Error Image" src="../ampTemplate/images/newerror.jpg" />
        			</td>
        			<td align="left" valign="middle">
        				<br/>
        				<span style="font-size:14px">
				        	<b><digi:trn key="exception:newErrorText">Hey there! I wasn't expecting you, but since you're here let me tell you what happened.</digi:trn></b><br/>
				        	They say it was "<i><bean:write name="exceptionReportForm" property="exceptionInfo.userMessage"/></i>" 
				        	<logic:notEmpty name="exceptionReportForm" property="exceptionInfo.mainTag">
				        		and it's has to do with "<i><bean:write name="exceptionReportForm" property="exceptionInfo.mainTag"/></i>"
				        	</logic:notEmpty>
				        	! <br/>
				        	In my opinion you can:
				        		<li>Go back to the
				        			<a href="<bean:write name="exceptionReportForm" property="exceptionInfo.backLink"/>">
					        			<digi:trn key="exception:previousPage">
					        				previous page
				        				</digi:trn>
				        			</a>
				        		</li> 
				        		<li>Go to the 
					        		<logic:present name="ampAdmin" scope="session">
										<logic:equal name="ampAdmin" value="yes">
											<digi:link href="/admin.do" module="aim" >
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
				        	You don't happen to be a <a href="javascript:toggleLayer('devInfo');"> developer</a>, do you? 
				        </span>
        			</td>
        		</tr>
        		<tr>
        			<td colspan="2">
       					<div id="devInfo" style="display: none; width: 80%; margin-left: 10%"  >
							<fieldset>
					          <legend >
					       		<a href="javascript:toggleLayer('docDiv');">
				            		<digi:trn key="exception:suggestedDocumentation">Suggested Documentation</digi:trn>
						        </a>
					          </legend>
					          <div id="docDiv" style="display: block;">
						          <p align="center">
						          	  <img src="/TEMPLATE/ampTemplate/images/ajax-loader.gif" alt="loading..."/>
						          </p>
							  </div>
							</fieldset>

							<fieldset>
					          <legend>
					          	<a href="javascript:toggleLayer('errMsg');">
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
						          	<a href="javascript:toggleLayer('stackDiv');">
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
