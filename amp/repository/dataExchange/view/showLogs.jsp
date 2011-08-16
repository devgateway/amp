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
	var selDesc = document.getElementById("descIs").value;
	form.action = "/dataExchange/showLogs.do?htmlView=true&selectedSourceId="+selectedSourceSetting+"&selectedDescription="+selDesc;
	form.target="_self"
	form.submit();
}

function checkLog(sourceId) {
	var loadingImgDiv = document.getElementById("loadingImg");
	loadingImgDiv.style.display="block";
	var form = document.getElementById('logForm');
	form.action = "/dataExchange/manageSource.do?action=executeIATI&executingSourceId="+sourceId;
	form.target="_self"
	form.submit();
}

function sortByVal (val,selectedSourceSetting) {
	var form = document.getElementById('logForm');
	var selectedSourceSetting = document.getElementById("logFor").value;
	form.action = "/dataExchange/showLogs.do?htmlView=true&selectedSourceId="+selectedSourceSetting+"&sortBy="+val;
	form.target="_self"
	form.submit();	
}
function page (page){
	var form = document.getElementById('logForm');
	form.action = "/dataExchange/showLogs.do?htmlView=true&page="+page;
	form.target="_self";
	form.submit();	
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
					    	<a href="/dataExchange/manageSource.do" class="t_sm"><b>« <digi:trn>List of sources</digi:trn></b></a>
					    </td>
					    <td width="33%" align=center><b><digi:trn>Log file for:</digi:trn> ${showLogsForm.selectedSourceName }</b></td>
					    <td width="33%" align=right>
					    <a href="/dataExchange/mapFields.do" class="t_sm"><b>Mapping Tool</b></a>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
					    <a href="javascript:checkLog('${showLogsForm.selectedSourceId}')" class="t_sm"><b>Check Source</b></a>
					    &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
					    <a href="/dataExchange/createEditSource.do?action=gotoCreatePage&htmlView=true" class="t_sm"><b>[+] Create New Source</b></a></td>
					</tr>
				</table>
				<div style="text-align: center; display: none;" id="loadingImg">
					<img src="/TEMPLATE/ampTemplate/js_2/yui/assets/skins/sam/loading.gif" border="0" height="17px"/>&nbsp;&nbsp; 
        			<b class="ins_title"><digi:trn>Loading, please wait ...</digi:trn></b>
				</div>
				<table class="inside" width=980 border=0 cellpadding="0" cellspacing="0" style="margin:10px;">
					<tr>
						<td colspan="6" align=right background="/TEMPLATE/ampTemplate/img_2/ins_header.gif" class=inside><b class="ins_header">
							
						<digi:trn>See log file for </digi:trn> :
						<html:select property="selectedSourceId" styleClass="dropdwn_sm" styleId="logFor">
						  	<html:optionsCollection property="availableSourceSettings" value="id" label="name" />
						</html:select>
						<digi:trn>Show only </digi:trn> :
						<html:select property="selectedDescription" styleClass="dropdwn_sm" styleId="descIs">
						  	<html:option value="All"><digi:trn>All</digi:trn> </html:option>
						  	<html:option value="Check feed source"><digi:trn>Check feed source</digi:trn></html:option>
						  	<html:option value="Import activities"><digi:trn>Import activities</digi:trn></html:option>
						</html:select>
						
						<input type="button" value="See" class="buttonx_sm" onclick="changeSource()"/>
						</b></td>
					</tr>
					<tr>
						<td class="inside" background="/TEMPLATE/ampTemplate/img_2/ins_bg.gif" align="center">
							<c:if test="${not empty showLogsForm.sortBy && showLogsForm.sortBy!='dbId'}">
								<a href="javascript:sortByVal('dbId','${showLogsForm.selectedSourceId }')">
									<b class="ins_title"><digi:trn>DbID</digi:trn></b>	                            	
								</a>
							</c:if> 
							<c:if test="${empty showLogsForm.sortBy || showLogsForm.sortBy=='dbId'}">
								<a href="javascript:sortByVal('dbId_desc','${showLogsForm.selectedSourceId }')">
									<b class="ins_title"><digi:trn>DbID</digi:trn></b> 
								</a>
							</c:if> 
							<c:if test="${not empty showLogsForm.sortBy && showLogsForm.sortBy=='dbId'}">
								<img  src="/repository/aim/images/up.gif" border="0"/>
							</c:if> 
							<c:if test="${not empty showLogsForm.sortBy && showLogsForm.sortBy=='dbId_desc'}">
								<img src="/repository/aim/images/down.gif" />
							</c:if>
						</td>
						<td class="inside" background="/TEMPLATE/ampTemplate/img_2//ins_bg.gif" align="center">
							<c:if test="${empty showLogsForm.sortBy || showLogsForm.sortBy!='date'}">
								<a href="javascript:sortByVal('date','${showLogsForm.selectedSourceId }')">
									<b class="ins_title"><digi:trn>Date</digi:trn></b>	                            	
								</a>
							</c:if>
							<c:if test="${not empty showLogsForm.sortBy && showLogsForm.sortBy=='date'}">
								<a href="javascript:sortByVal('date_desc','${showLogsForm.selectedSourceId }')">
									<b class="ins_title"><digi:trn>Date</digi:trn></b>	                            	
								</a>
							</c:if> 
							<c:if test="${not empty showLogsForm.sortBy && showLogsForm.sortBy=='date'}">
								<img src="/repository/aim/images/up.gif" />
							</c:if> 
							<c:if test="${ empty showLogsForm.sortBy || showLogsForm.sortBy=='date_desc'}">
								<img src="/repository/aim/images/down.gif" />
							</c:if>
						</td>
					
					    <td background="/TEMPLATE/ampTemplate/img_2//ins_bg.gif" class=inside align="center"> 
					    	<b class="ins_title">Time</b>
					   	</td>
					   	<!-- 
					    <td background="/TEMPLATE/ampTemplate/img_2//ins_bg.gif" class=inside><b class="ins_title">External Timestamp</b></td>
					     -->
					    <td background="/TEMPLATE/ampTemplate/img_2//ins_bg.gif" class=inside align=center><b class="ins_title">Description</b></td>
					    <td background="/TEMPLATE/ampTemplate/img_2//ins_bg.gif" class=inside align=center><b class="ins_title">Actions</b></td>
					</tr>
					<logic:empty name="showLogsForm" property="logs">
						<tr>
							<td bgcolor="#FFFFFF" class="inside" colspan="6"><div class="t_sm"><digi:trn>No Records Found</digi:trn> </div></td>
						</tr>
					</logic:empty>
					<logic:notEmpty name="showLogsForm" property="logs">
						<logic:iterate id="log" name="showLogsForm" property="logs">
							<tr>
							    <td bgcolor=#FFFFFF class=inside align="center">
							    	<div class="t_sm">${log.id}</div>
							    </td>
							    <td bgcolor=#FFFFFF class=inside align="center">
							    	<div class="t_sm">${log.dateAsString}</div>
							    </td>
							    <td bgcolor=#FFFFFF class=inside align="center">
							    	<div class="t_sm">${log.timeAsString }</div>
							    </td>
							    <!-- 
							    <td bgcolor=#FFFFFF class=inside>
							    	<div class="t_sm">${log.externalTimestamp }</div>
							    </td>
							     -->
							    <td bgcolor=#FFFFFF class=inside align="center">
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
							    		<a href="/dataExchange/showLogs.do?selectedLogPerExecId=${log.id}" class="t_sm"><b>view</b></a>							    		
							    	</div>
							    </td>
							</tr>
						</logic:iterate>
					</logic:notEmpty>		
				</table>
				<!-- Pagination -->
				<div class="paging" style="font-size:11px;margin:10px;">
					<b class="ins_title"><digi:trn>Pages :</digi:trn></b>
					<c:forEach var="page" begin="1" end="${showLogsForm.lastPage}">
						<bean:define id="currPage" name="showLogsForm" property="currentPage" />
						<c:if test="${showLogsForm.currentPage == page}">
							<b class="paging_sel">${page}</b>
						</c:if>
						<c:if test="${showLogsForm.currentPage != page}">
							<c:set var="translation">
								<digi:trn key="aim:clickToViewNextPage">Click here to goto Next Page</digi:trn>
							</c:set>
							<a href="javascript:page(${page})" title="${translation}" class="l_sm">${page}</a>
						</c:if>
						|&nbsp;
					</c:forEach>
				</div>	
				<!-- end of Pagination -->
			</td>
		</tr>
	</table>
</digi:form>

<br /><br />
<!-- MAIN CONTENT PART END -->
</body>