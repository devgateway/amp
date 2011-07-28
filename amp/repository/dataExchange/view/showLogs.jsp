<%@ page pageEncoding="UTF-8"%> 
<%@ taglib uri="/taglib/struts-bean" prefix="bean"%>
<%@ taglib uri="/taglib/struts-logic" prefix="logic"%>
<%@ taglib uri="/taglib/struts-tiles" prefix="tiles"%>
<%@ taglib uri="/taglib/struts-html" prefix="html"%>
<%@ taglib uri="/taglib/digijava" prefix="digi"%>
<%@ taglib uri="/taglib/category" prefix="category"%>
<%@ taglib uri="/taglib/jstl-core" prefix="c"%>
<%@ taglib uri="/taglib/category" prefix="category" %>
<%@ taglib uri="/taglib/fieldVisibility" prefix="field" %>
<%@ taglib uri="/taglib/featureVisibility" prefix="feature" %>
<%@ taglib uri="/taglib/moduleVisibility" prefix="module" %>

<script type="text/javascript">
function changeSource() {
	var form = document.getElementById('logForm');
	var selectedSourceSetting = document.getElementById("logFor").value;
	form.action = "/dataExchange/showLogs.do?htmlView=true&selectedSourceId="+selectedSourceSetting;
	form.target="_self"
	form.submit();
}

function chechLog(logId) {
	alert('needs implementation');
}
</script>

<digi:instance property="showLogsForm" />

<body bgcolor="#FFFFFF" leftmargin="0" topmargin="0" marginwidth="0" marginheight="0">

<!-- MAIN CONTENT PART START -->
<digi:form action="/showLogs.do" styleId="logForm">
	<table width="1000" border="0" cellspacing="0" cellpadding="0" align="center">
		<!-- BREADCRUMP START -->
		<tr>
			<td height="33">
				<div class="breadcrump_cont"> 
					<span class="sec_name"><digi:trn>Partial Data Import Manager</digi:trn></span>
					<span class="breadcrump_sep">|</span> <a href="/admin.do" class="l_sm"><digi:trn>Admin Home</digi:trn></a>
					<span class="breadcrump_sep"><b>»</b></span><a href="/dataExchange/manageSource.do" class="l_sm"><digi:trn>Import Manager</digi:trn></a>
					<span class="breadcrump_sep"><b>»</b></span>
					<span class="bread_sel"><digi:trn>Show Logs</digi:trn></span>
				</div>
				<br>
			</td>
		</tr>
		<!-- BREADCRUMP END -->
		<tr>
		    <td class="main_side_1">
				<table width="980" border="0" cellspacing="0" cellpadding="0" style="margin:10px; font-size:12px;">
					<tr>
					    <td width="33%">
					    	<a href="/dataExchange/manageSource.do" class="t_sm"><b>« <digi:trn>Back to details</digi:trn></b></a>
					    </td>
					    <td width="33%" align=center><b><digi:trn>Log file for:</digi:trn> ${showLogsForm.selectedSourceName }</b></td>
					    <td width="33%" align=right><a href="/dataExchange/createSource.do?htmlView=true" class="t_sm"><b>[+] Create New Source</b></a></td>
					</tr>
				</table>
		
				<table class="inside" width=980 border=0 cellpadding="0" cellspacing="0" style="margin:10px;">
					<tr>
						<td colspan="6" align=right background="images/ins_header.gif" class=inside><b class="ins_header">
						<digi:trn>See log file for </digi:trn> :
						  <html:select property="selectedSourceId" styleClass="dropdwn_sm" styleId="logFor">
						  	<html:optionsCollection property="availableSourceSettings" value="id" label="name" />					    
						  </html:select>
						  <input type="button" value="See" class="buttonx_sm" onclick="changeSource()"/>
						</b></td>
					</tr>
					<tr>
					    <td background="images/ins_bg.gif" class=inside><b class="ins_title">DbID</b></td>
					    <td background="images/ins_bg.gif" class=inside><b class="ins_title">Date</b></td>
					    <td background="images/ins_bg.gif" class=inside><b class="ins_title">Time</b></td>
					    <td background="images/ins_bg.gif" class=inside><b class="ins_title">External Timestamp</b></td>
					    <td background="images/ins_bg.gif" class=inside align=center><b class="ins_title">Description</b></td>
					    <td background="images/ins_bg.gif" class=inside align=center><b class="ins_title">Actions</b></td>
					</tr>
					<logic:empty name="showLogsForm" property="logs">
						<tr>
							<td bgcolor="#FFFFFF" class="inside" colspan="6"><div class="t_sm"><digi:trn>No Records Found</digi:trn> </div></td>
						</tr>
					</logic:empty>
					<logic:notEmpty name="showLogsForm" property="logs">
						<logic:iterate id="log" name="showLogsForm" property="logs">
							<tr>
							    <td bgcolor=#FFFFFF class=inside>
							    	<div class="t_sm">${log.id}</div>
							    </td>
							    <td bgcolor=#FFFFFF class=inside>
							    	<div class="t_sm">${log.dateAsString}</div>
							    </td>
							    <td bgcolor=#FFFFFF class=inside>
							    	<div class="t_sm">${log.timeAsString }</div>
							    </td>
							    <td bgcolor=#FFFFFF class=inside>
							    	<div class="t_sm">${log.externalTimestamp }</div>
							    </td>
							    <td bgcolor=#FFFFFF class=inside>
							    	<div class="t_sm">
							    		${log.description }
							    			<%-- 
							    			Name:	  |   ${log.name}
							    		Database ID:	 ${log.id}	  
							    		|   Log Level:	 ERROR	  
							    		|   Date:	 ${log.dateAsString}	  
							    		|   Time:	 ${log.timeAsString }	  
							    		|   Description:	${log.logType }	  
							    		|   Item Type:	Activity
							    		 --%>						    		
							    	</div>
							    </td>
							    <td bgcolor=#FFFFFF class=inside align="center">
							    	<div class="t_sm">
							    		<a href="/dataExchange/showLogs.do?selectedLogPerExecId=${log.id}" class="t_sm"><b>view</b></a> | 
							    		<a href="javascript:chechLog('${log.id}')" class="t_sm"><b>check</b></a>
							    	</div>
							    </td>
							</tr>
						</logic:iterate>
					</logic:notEmpty>		
				</table>
			</td>
		</tr>
	</table>
</digi:form>

<br /><br />
<!-- MAIN CONTENT PART END -->
</body>