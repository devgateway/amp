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
function toggleGroup(group_id){
	var strId='#'+group_id;
	$(strId+'_minus').toggle();
	$(strId+'_plus').toggle();
	$('#log_'+group_id).toggle('fast');
}

function importAll() {
	alert('needs implementation !');
}

function importItem(){
	alert('needs implementation !');
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
					<span class="breadcrump_sep">|</span> <a href="/dataExchange/showLogs.do?htmlView=true&selectedSourceId=${showLogsForm.selectedSourceId }" class="l_sm"><digi:trn>Show Logs</digi:trn></a>
					<span class="breadcrump_sep"><b>»</b></span>
					<span class="bread_sel"><digi:trn>Execution Log</digi:trn></span>
				</div>
				<br>
			</td>
		</tr>
		<!-- BREADCRUMP END -->
		<!-- MAIN CONTENT PART START -->
  		<tr>
		    <td class="main_side_1">
				<table width="980" border="0" cellspacing="0" cellpadding="0" style="margin:10px; font-size:12px;">
				  <tr>
				    <td width="33%"><a href="/dataExchange/showLogs.do?htmlView=true&selectedSourceId=${showLogsForm.selectedSourceId }" class="t_sm"><b>« Back to logs</b></a></td>
				    <td width="33%" align=center><b>Execution Log</b></td>
				    <td width="33%" align=right><a href="/dataExchange/createSource.do?htmlView=true" class="t_sm"><b>[+] Create New Source</b></a></td>
				  </tr>
				</table>
				
				<table class="inside" width=980 border=0 cellpadding="0" cellspacing="0" style="margin:10px;">
					<tr>
						<td colspan="6" align=center background="/TEMPLATE/ampTemplate/img_2/ins_header.gif" class=inside>
						<b>Execution Logs</b>
					</td>
					<tr>
					    <td width="20" background="/TEMPLATE/ampTemplate/img_2/ins_bg.gif" class=inside><b class="ins_title">
					      <input name="" type="checkbox" value="" /></b>
					    </td>
					    <td background="/TEMPLATE/ampTemplate/img_2/ins_bg.gif" class=inside>
					    	<b class="ins_title">Activity Name</b>
					    </td>
					    <td background="/TEMPLATE/ampTemplate/img_2/ins_bg.gif" class=inside align=center>
					    	<b class="ins_title">Status</b>
					    </td>
						<td background="/TEMPLATE/ampTemplate/img_2/ins_bg.gif" class=inside>
							<b class="ins_title">Details</b>
						</td>
						<td background="/TEMPLATE/ampTemplate/img_2/ins_bg.gif" class=inside>
							<b class="ins_title">Error Details</b>
						</td>
					    <td background="/TEMPLATE/ampTemplate/img_2/ins_bg.gif" class=inside align=center>
					    	<b class="ins_title">Actions</b>
					    </td>
					</tr>
					<logic:empty name="showLogsForm" property="logItems">
						<tr>
							<td bgcolor="#FFFFFF" class="inside" colspan="5">
								<div class="t_sm"><digi:trn>No Records Found</digi:trn> </div>
							</td>
						</tr>				
					</logic:empty>
					<logic:notEmpty name="showLogsForm" property="logItems">
						<logic:iterate id="item" name="showLogsForm" property="logItems">
							<tr>
							    <td bgcolor=#FFFFFF class=inside>
							    	<c:if test="${item.logType=='OK'}">
							    		<input name="id" type="checkbox"/>
							    	</c:if>
							    	
							    </td>
							    <td bgcolor=#FFFFFF class=inside>
							    	<div class="t_sm">${item.name}</div>
							    </td>
							    <td bgcolor=#FFFFFF class=inside align=center>
							    	<c:if test="${item.logType=='ERROR' }">
							    		<img src="/TEMPLATE/ampTemplate/img_2/not_ok_ico.gif" />
							    	</c:if>
							    	<c:if test="${item.logType=='INFO' }">
							    		<img src="/TEMPLATE/ampTemplate/img_2/ico_info.gif" />
							    	</c:if>
							    	<c:if test="${item.logType=='OK'}">
							    		<img src="/TEMPLATE/ampTemplate/img_2/ok_ico.gif" />
							    	</c:if>							    	
							    </td>
							    <td bgcolor=#FFFFFF class=inside>
							    	<div class="t_sm">
							    		<div class="t_sm">Name:	 ${item.name } |
							    		   Database ID:	 ${item.id }	  |
							    		   Log Level:	 ${item.logType }	  |
							    		   Date:	 ${item.dateAsString}	  |
							    		   Time:	 ${item.timeAsString }	  |							    		   
							    		   Item Type:	${item.itemType }
							    		</div>
							    	</div>
							    </td>
							    
							    <td bgcolor="#FFFFFF" class="inside" align="center" nowrap="nowrap">
							    	<img src="/TEMPLATE/ampTemplate/img_2/ico_plus.gif" id="${item.id}_plus" onclick="toggleGroup('${item.id}')" style="cursor: pointer;"/>
							    	<div id="log_${item.id}" style="display: none;width:500px; overflow: auto;">				    		
							    		<table border="0">
								    		<tr>
								    			<td style="border: none;vertical-align: text-top;" colspan="2" rowspan="8" class="inside">				    				
								    				<img src="/TEMPLATE/ampTemplate/img_2/ico_blue_minus.gif"  id="${item.id}_minus" style="display: none;cursor: pointer; " onclick="toggleGroup('${item.id}')"/>	
								    			</td>
								    			<td style="border: none;vertical-align: text-top;" class="inside" nowrap="nowrap">				    				
								    				<strong><digi:trn>Description</digi:trn>:</strong>
								    			</td>
								    			<td style="border: none;vertical-align: text-top;" class="inside" nowrap="nowrap">
								    				${item.description }
								    			</td>
								    		</tr>							    		
							    		</table>
							    	</div>
							    </td>							    
							    
							    <td width="20" align="center" bgcolor=#FFFFFF class=inside>
							    	<c:if test="${item.logType=='OK'}">
							    		<input type="button" class="buttonx_sm" value="Import" onclick="importItem('${item.id}');"/>
							    	</c:if>
							    </td>
							</tr>
						</logic:iterate>						
						<tr>
						    <td colspan="5" bgcolor=#FFFFFF class=inside>&nbsp;</td>
						    <td width="20" align="center" bgcolor=#FFFFFF class=inside>
						    	<input type="button" class="buttonx_sm" value="Import All" onclick="importAll()"/>
						    </td>
						</tr>		
					</logic:notEmpty>										
				</table>
			</td>
		</tr>
	</table>
</digi:form>

<br /><br />
<!-- MAIN CONTENT PART END -->
</body>