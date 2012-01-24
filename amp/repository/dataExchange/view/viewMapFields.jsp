<%@ page pageEncoding="UTF-8"%> 
<%@ taglib uri="/taglib/struts-bean" prefix="bean"%>
<%@ taglib uri="/taglib/struts-logic" prefix="logic"%>
<%@ taglib uri="/taglib/struts-tiles" prefix="tiles"%>
<%@ taglib uri="/taglib/struts-html" prefix="html"%>
<%@ taglib uri="/taglib/digijava" prefix="digi"%>
<%@ taglib uri="/taglib/category" prefix="category"%>
<%@ taglib uri="/taglib/jstl-core" prefix="c"%>
<%@ taglib uri="/taglib/fieldVisibility" prefix="field" %>
<%@ taglib uri="/taglib/featureVisibility" prefix="feature" %>
<%@ taglib uri="/taglib/moduleVisibility" prefix="module" %>

<digi:instance property="mapFieldsForm" id="mff"/>
<script type="text/javascript">
function saveRecord(id) {
	var loadingImgDiv = document.getElementById("loadingImg");
	loadingImgDiv.style.display="block";
		 var el = document.getElementById("ampValues["+id+"]");
		 var txt = el.options[el.selectedIndex].innerHTML;
		 <digi:context name="saveRecord" property="context/module/moduleinstance/mapFields.do"/>
		 url = "<%= saveRecord %>?actionType=saveRecord&id="+id+"&ampId="+el.value+"&mappedValue="+txt;
		 mapFieldsForm.action =url;
		 //alert(url);
		 mapFieldsForm.submit();
		 return true;
}

function saveAll() {

	 var checks = document.getElementsByName("selectedFields");
	 var isChecked = false;

	 var params = "&actionType=saveAllRecords";
	 for(i=0;i<checks.length;i++){
		 if(checks[i].checked) {
			 isChecked=true;
			 var opts = document.getElementById("ampValues["+checks[i].value+"]");
			 params+="&selectedAmpIds="+opts.value;
			 params+="&selectedAmpValues="+opts.options[opts.selectedIndex].text;
		 }
	 }
	 
	 if(isChecked != true) {
		 alert("Please check at least one record");
		 return true;
     }
	 
	 var loadingImgDiv = document.getElementById("loadingImg");
	 loadingImgDiv.style.display="block";
		
	 <digi:context name="saveRecord" property="context/module/moduleinstance/mapFields.do"/>
	 url = "<%= saveRecord %>";
	 var postString = params;
	 YAHOO.util.Connect.setForm( document.getElementById("logForm") );
	 YAHOO.util.Connect.asyncRequest('POST', url, { 
		 	            success: function() { 
		 	            	window.location.replace(url);
		 	            }, 
		 	            failure: function() { 
		 	            } 
		 	        },postString); 

     
	 return true;
}

function checksAll() {
	 var check = document.getElementById("checkAll");
	 $('[id^="Check_Amp"]').attr('checked', false);
	 $('[id^="Check_Amp"]').attr('checked', check.checked);
	 return true;
	 //var records = document.getElementById("filterAmpClass");
	 //var v = records.options[records.selectedIndex].text;
	 //var v = records.value;
	 //if("all" == v)
	 //else $('[id^="Check_'+v+'_"]').attr('checked', check.checked);
}

function showFilter() {
	var records = document.getElementById("filterAmpClass");
	var selectedAmpClass = records.value;
	page(1,selectedAmpClass);	
}

function page (page, ampSelectedClass){
	var form = document.getElementById('logForm');
//	var records = document.getElementById("filterAmpClass");
//	var selectedAmpClass = records.options[records.selectedIndex].text;
	
	form.action = "/dataExchange/mapFields.do?page="+page+"&selectedAmpClass="+ampSelectedClass;
	form.target="_self";
	form.submit();	
}

</script>


<body bgcolor="#FFFFFF" leftmargin="0" topmargin="0" marginwidth="0" marginheight="0">

<!-- MAIN CONTENT PART START -->
<digi:form action="/mapFields.do" styleId="logForm">

		<table width="1000px" border="0" cellspacing="0" cellpadding="0" align="center">
			<!-- BREADCRUMP START -->
			<tr>
				<td height="33">
					<div class="breadcrump_1"> 
						<span class="sec_name"><digi:trn>Data Import Manager</digi:trn></span>
						<span class="breadcrump_sep">|</span> <a href="/admin.do" class="l_sm"><digi:trn>Admin Home</digi:trn></a>
						<span class="breadcrump_sep"><b>»</b></span><a href="/dataExchange/manageSource.do" class="l_sm"><digi:trn>Import Manager</digi:trn></a>
						<span class="breadcrump_sep"><b>»</b></span>
						<span class="bread_sel"><digi:trn>Mapping Tool</digi:trn></span>
					</div>
					<br>
					<div style="text-align: center; display: none;" id="loadingImg">
						<img src="/TEMPLATE/ampTemplate/js_2/yui/assets/skins/sam/loading.gif" border="0" height="17px"/>&nbsp;&nbsp; 
		        		<b class="ins_title"><digi:trn>Loading, please wait ...</digi:trn></b>
					</div>
				</td>
			</tr>
		</table>
		<!-- BREADCRUMP END -->
		<!-- MAIN CONTENT PART START -->
  		<table width="1000px" border="0" cellspacing="0" cellpadding="0" align="center">
			<tr>
			    <td class="main_side_1">
				    <table class="inside" width="980px" border=0 cellpadding="0" cellspacing="0" style="margin:10px;" id="tableRecords">
				    	<tr>
		    				<td align=right colspan="8" class="inside">
		    					<a href="/dataExchange/mapFields.do" >
		    					<b><digi:trn>Mapping Tool</digi:trn></b></a>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
		    					<a href="/dataExchange/createEditSource.do?action=gotoCreatePage&htmlView=true" class="t_sm"><b>[+] <digi:trn>Create New Source</digi:trn></b></a>
		   					 </td>
	  					</tr>
						<tr>
						<td colspan="8" align="center" background="images/ins_header.gif" class="inside"><b class="ins_header"><digi:trn>Filter by</digi:trn>:
							<html:select property="selectedAmpClass" styleClass="dropdwn_sm" onchange="showFilter()" styleId="filterAmpClass">
        						<logic:iterate id="cls" name="mapFieldsForm" property="ampClasses">
        							<%-- <bean:define id="itemAmpClass"><%= cls.toString().replaceAll(" ","") %></bean:define>--%>
        							<html:option value="${cls}" >${cls}</html:option>
								</logic:iterate>
        					</html:select>
						</b></td>
						</tr>
						<tr>
						    <td width="20" background="images/ins_bg.gif" class="inside"><b class="ins_title"><input id="checkAll" type="checkbox" onclick="checksAll()" /></b></td>
						    <td width="400" background="images/ins_bg.gif" class="inside"><b class="ins_title"><digi:trn>Iati Items</digi:trn></b></td>
						    <td background="images/ins_bg.gif" class="inside"><b class="ins_title"><digi:trn>IATI values</digi:trn></b></td>
						    <td background="images/ins_bg.gif" class="inside"><b class="ins_title"><digi:trn>Current value</digi:trn></b></td>
						    <td background="images/ins_bg.gif" class="inside"><b class="ins_title"><digi:trn>Status</digi:trn></b></td>
						    <td background="images/ins_bg.gif" class="inside"><b class="ins_title"><digi:trn>Values from AMP</digi:trn></b></td>
						    <td width="50" background="images/ins_bg.gif" class="inside" align="center"><b class="ins_title"><digi:trn>Actions</digi:trn></b></td>
						</tr>
						<logic:notEmpty name="mapFieldsForm" property="mappedFields">
							<logic:iterate id="field" name="mapFieldsForm" property="mappedFields">
								<tr id="Amp${field.ampField.shortAmpClass}_${field.ampField.id}">
								    <td bgcolor="#FFFFFF" class="inside">
								    	<html:checkbox name="mapFieldsForm"  property="selectedFields"  value="${field.ampField.id}" 
								    		styleId="Check_Amp${field.ampField.shortAmpClass}_${field.ampField.id}"/>
								    </td>
								    <td bgcolor="#FFFFFF" class="inside"><div class="t_sm">${field.ampField.iatiItems}</div></td>
								    <td bgcolor="#FFFFFF" class="inside"><div class="t_sm">${field.ampField.iatiValues}</div></td>
								    <td bgcolor="#FFFFFF" class="inside"><div class="t_sm">${field.ampField.ampValues}</div></td>
								    <td bgcolor="#FFFFFF" class="inside">
								    	<div class="t_sm" align="center">
								    	<c:if test="${field.ampField.ampValues== null }">
							    			<img src="/TEMPLATE/ampTemplate/img_2/not_ok_ico.gif" />
								    	</c:if>
								    	<c:if test="${field.ampField.ampValues!= null }">
								    		<c:if test="${field.ampField.ampValues=='Add new' }">
								    			<bean:define id="itemDescr">
								    				<c:if test="${field.ampField.iatiPath=='Activity' }">
								    					<digi:trn>Activity will be added as new activity when import will be performed</digi:trn>
								    				</c:if>
								    				<c:if test="${field.ampField.iatiPath=='Sector' || field.ampField.iatiPath=='Vocabulary Code' || field.ampField.iatiPath=='Location' }">
								    					<digi:trn>Can not be automatically added. Please go to admin and manually add it</digi:trn>
								    				</c:if>
								    				&nbsp;
								    			</bean:define>
							    				<img src="/TEMPLATE/ampTemplate/img_2/ico_info.gif" title="${itemDescr}" />
							    			</c:if>
							    			<c:if test="${field.ampField.ampValues!='Add new' }">
								    			<img src="/TEMPLATE/ampTemplate/img_2/ok_ico.gif" />
							    			</c:if>
								    	</c:if>
								    	</div>
								    </td>
								    <td bgcolor="#FFFFFF" class="inside">
								  		<html:select  name="mapFieldsForm"  property="allSelectedAmpValues" styleClass="dropdwn_sm" styleId="ampValues[${field.ampField.id}]">
								  			<html:option value="-1"><digi:trn>Add new</digi:trn></html:option>
        									<logic:iterate id="cls" name="field" property="sortedLabels">
												<html:option value="${cls.key}">
												${cls.value}
												</html:option>
											</logic:iterate>
        								</html:select>
								  	</td>
								    <td bgcolor="#FFFFFF" class="inside" align="center">
								    		<input type="button" value="<digi:trn>Save</digi:trn>" class="buttonx_sm" onclick="saveRecord(${field.ampField.id})" />
								    </td>
								</tr>
							</logic:iterate>
						</logic:notEmpty>
						<tr>
							  <td colspan="6" bgcolor="#FFFFFF" class="inside">&nbsp;</td>
							  <td bgcolor="#FFFFFF" class="inside" align="center"><input type="button" value="<digi:trn>Save All</digi:trn>" class="buttonx_sm" onclick="saveAll()"/></td>
						</tr>
					</table>
					<div class="paging" style="font-size:11px;margin:10px;">
						<b class="ins_title"><digi:trn>Pages :</digi:trn></b>
						<c:forEach var="page" begin="1" end="${mapFieldsForm.lastPage}">
							<bean:define id="currPage" name="mapFieldsForm" property="currentPage" />
							<c:if test="${mapFieldsForm.currentPage == page}">
								<b class="paging_sel">${page}</b>
							</c:if>
							<c:if test="${mapFieldsForm.currentPage != page}">
								<c:set var="translation">
									<digi:trn>Click here to goto Next Page</digi:trn>
								</c:set>
								<a href="javascript:page(${page},'${mapFieldsForm.selectedAmpClass}')" title="${translation}" class="l_sm">${page}</a>
							</c:if>
							|&nbsp;
						</c:forEach>
					</div>
				</td>
			</tr>
		</table>
</digi:form>

<br /><br />
<!-- MAIN CONTENT PART END -->
</body>