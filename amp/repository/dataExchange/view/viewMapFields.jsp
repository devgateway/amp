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
	 var isChecked = false

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
	 //var checks = document.getElementsByName("selectedFields");
	 var check = document.getElementById("checkAll");
//	 for(i=0;i<checks.length;i++){
//		 checks[i].checked=check.checked;
//	 }
	 //$('input[name=foo]').attr('checked', true);
	 $('[id^="Check_Amp"]').attr('checked', false);
	 var records = document.getElementById("filterAmpClass");
	 var v = records.options[records.selectedIndex].text;
	 $('[id^="Check_'+v+'"]').attr('checked', true);;
	 return true;
}

function showFilter() {
	 var records = document.getElementById("filterAmpClass");
	 //alert(check.value + "::" +check.options[check.selectedIndex].text);
	 var v = records.options[records.selectedIndex].text;
	 if("all" == records.value)
	 	$('[id^="Amp"]').show();
	 else
		{
		 	$('[id^="Amp"]').hide();
			$('[id^="'+v+'"]').show();
		}
	 return true;
}

</script>


<body bgcolor="#FFFFFF" leftmargin="0" topmargin="0" marginwidth="0" marginheight="0">

<!-- MAIN CONTENT PART START -->
<digi:form action="/mapFields.do" styleId="logForm">

		<table width="1000" border="0" cellspacing="0" cellpadding="0" align="center">
			<!-- BREADCRUMP START -->
			<tr>
				<td height="33">
					<div class="breadcrump_cont"> 
						<span class="sec_name"><digi:trn>Partial Data Import Manager</digi:trn></span>
						<span class="breadcrump_sep">|</span> <a href="/admin.do" class="l_sm"><digi:trn>Admin Home</digi:trn></a>
						<span class="breadcrump_sep"><b>»</b></span><a href="/dataExchange/manageSource.do" class="l_sm"><digi:trn>Import Manager</digi:trn></a>
						<span class="breadcrump_sep"><b>»</b></span>
						<span class="bread_sel"><digi:trn>Mapping Tool</digi:trn></span>
					</div>
					<br>
				</td>
			</tr>
		</table>
		<!-- BREADCRUMP END -->
		<!-- MAIN CONTENT PART START -->
  		<table width="1000" border="0" cellspacing="0" cellpadding="0" align="center">
			<tr>
			    <td class="main_side_1">
				    <table class="inside" width=980 border=0 cellpadding="0" cellspacing="0" style="margin:10px;" id="tableRecords">
						<tr>
						<td colspan="6" align="center" background="images/ins_header.gif" class="inside"><b class="ins_header">Filter by:
							<html:select property="selectedAmpClass" styleClass="dropdwn_sm" onchange="showFilter()" styleId="filterAmpClass">
								<html:option value="all"  >View All</html:option>
        						<logic:iterate id="cls" name="mapFieldsForm" property="ampClasses">
        							<html:option value="${cls}"><%= cls.toString().substring(cls.toString().lastIndexOf(".")+1,cls.toString().length()) %></html:option>
								</logic:iterate>
        					</html:select>
						  <input type="button" value="Filter" class="buttonx_sm" />
						</b></td>
						</tr>
						<tr>
						    <td width="20" background="images/ins_bg.gif" class="inside"><b class="ins_title"><input id="checkAll" type="checkbox" onclick="checksAll()" /></b></td>
						    <td width="400" background="images/ins_bg.gif" class="inside"><b class="ins_title">Iati Items</b></td>
						    <td background="images/ins_bg.gif" class="inside"><b class="ins_title">IATI values</b></td>
						    <td background="images/ins_bg.gif" class="inside"><b class="ins_title">Current value</b></td>
						    <td background="images/ins_bg.gif" class="inside"><b class="ins_title">Status</b></td>
						    <td background="images/ins_bg.gif" class="inside"><b class="ins_title">Values from AMP</b></td>
						    <td width="50" background="images/ins_bg.gif" class="inside" align="center"><b class="ins_title">Actions</b></td>
						</tr>
						<logic:notEmpty name="mapFieldsForm" property="mappedFields">
							<logic:iterate id="field" name="mapFieldsForm" property="mappedFields">
								<tr id="${field.ampField.shortAmpClass}_${field.ampField.id}">
								    <td bgcolor="#FFFFFF" class="inside">
								    	<html:checkbox name="mapFieldsForm"  property="selectedFields"  value="${field.ampField.id}" 
								    		styleId="Check_${field.ampField.shortAmpClass}_${field.ampField.id}"/>
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
							    				<img src="/TEMPLATE/ampTemplate/img_2/ico_info.gif" />
							    			</c:if>
							    			<c:if test="${field.ampField.ampValues!='Add new' }">
								    			<img src="/TEMPLATE/ampTemplate/img_2/ok_ico.gif" />
							    			</c:if>
								    	</c:if>
								    	</div>
								    </td>
								    <td bgcolor="#FFFFFF" class="inside">
								  		<html:select  name="mapFieldsForm" property="allSelectedAmpValues" styleClass="dropdwn_sm" styleId="ampValues[${field.ampField.id}]">
								  			<html:option value="-1"  >Add new</html:option>
        									<logic:iterate id="cls" name="field" property="sortedLabels">
												<html:option value="${cls.key}">
												${cls.value}
												</html:option>
											</logic:iterate>
        								</html:select>
								  	</td>
								    <td bgcolor="#FFFFFF" class="inside" align="center">
								    		<input type="button" value="Save" class="buttonx_sm" onclick="saveRecord(${field.ampField.id})" />
								    </td>
								</tr>
							</logic:iterate>
						</logic:notEmpty>
						<tr>
							  <td colspan="6" bgcolor="#FFFFFF" class="inside">&nbsp;</td>
							  <td bgcolor="#FFFFFF" class="inside" align="center"><input type="button" value="Save All" class="buttonx_sm" onclick="saveAll()"/></td>
						</tr>
					</table>
				</td>
			</tr>
		</table>
</digi:form>

<br /><br />
<!-- MAIN CONTENT PART END -->
</body>