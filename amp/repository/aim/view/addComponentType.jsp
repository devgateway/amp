<%@ page pageEncoding="UTF-8"%>
<%@ taglib uri="/taglib/struts-bean" prefix="bean"%>
<%@ taglib uri="/taglib/struts-logic" prefix="logic"%>
<%@ taglib uri="/taglib/struts-tiles" prefix="tiles"%>
<%@ taglib uri="/taglib/struts-html" prefix="html"%>
<%@ taglib uri="/taglib/digijava" prefix="digi"%>
<%@ taglib uri="/taglib/fieldVisibility" prefix="field"%>
<%@ taglib uri="/taglib/featureVisibility" prefix="feature"%>
<%@ taglib uri="/taglib/moduleVisibility" prefix="module"%>
<script defer src="ie_onload.js" type="text/javascript"></script>
<script language="JavaScript">

function onDelete() {
  var flag = confirm('<digi:trn key="aim:deleteconfirm">Are you sure?</digi:trn>');
  return flag;
}

function onCancel() {
  <digi:context name="cancelComponents" property="context/module/moduleinstance/updateComponentType..do?event=cancel" />
  document.aimComponentsTypeForm.action = "<%= cancelComponents%>";
  document.aimComponentsTypeForm.target = "_self";
  document.aimComponentsTypeForm.submit();
}


function validate()
{
  if ((document.aimComponentsTypeForm.name.value).length == 0)
  {
    alert("<digi:trn key="aim:errortypeName">Please Enter the name</digi:trn>");
    document.aimComponentsTypeForm.name.focus();
    return false;
  }
  if ((document.aimComponentsTypeForm.code.value).length == 0)
  {
    alert("<digi:trn key="aim:errortypeCode">Please Enter the code</digi:trn>");
    document.aimComponentsTypeForm.code.focus();
    return false;
  }
  return true;
}

function updateComponentsType()
{
  var temp = validate();
  if (temp == true)
  {
    document.aimComponentsTypeForm.addBtn.disabled = true;
	
    <digi:context name="update" property="context/module/moduleinstance/updateComponentType.do?event=save" />
    
	document.aimComponentsTypeForm.action = "<%=update%>";
    
	document.aimComponentsTypeForm.target = "_self";
    
	document.aimComponentsTypeForm.submit();
    
	}
  
  return temp;
}

function myOnload(){
  if(document.aimComponentsTypeForm.check.value=="save"){
    <digi:context name="refresh" property="context/module/moduleinstance/updateComponentType.do" />
    document.aimComponentsTypeForm.action = "<%= refresh %>";
    document.aimComponentsTypeForm.target = window.opener.name;
    document.aimComponentsTypeForm.submit();
    closeWindow();
  }
}

function unload(){}

function closeWindow(){window.close();}
</script>
<style type="text/css">
<!--
body {
	margin-left: 0px;
	margin-top: 0px;
	margin-right: 0px;
	margin-bottom: 0px;
}
-->
</style>
<body onLoad="myOnload()">
<digi:instance property="aimComponentsTypeForm" />
<digi:form action="/updateComponentType.do" method="post">


<html:hidden property="check" />
	<html:hidden property="id" />
	<font color=red>* 
								    <digi:trn
										key="aim:addNewComponent:mandatoryFields"> Mandatory fields </digi:trn>
									</font>
	<table width="100%" border="0" align="center" cellpadding="0" cellspacing="0" style="font-size:12px; font-family:Arial, Helvetica, sans-serif;">
		<!--<tr>
			<td height="30"><span class=subtitle-blue> <digi:trn
				key="aim:addNewComponentType"><b>Add A New Component Type</b></digi:trn> </span></td>
		</tr>-->
		<tr>
			<td>
			<table align="center" bgColor=#f4f4f2 cellpadding="0" cellspacing="0"
				width="100%" border="0" style="font-size:12px; font-family:Arial, Helvetica, sans-serif;">
				<tr>
					<td bgColor=#ffffff class=box-border>
					<table border="0" cellpadding="1" cellspacing="1" class=box-border
						width="100%">
						<tr bgColor=#dddddb>
							<!-- header -->
							<td bgColor=#c7d4db height="20" align="center" colspan="5"><digi:trn
								key="aim:addType"><b>Add Type</b></digi:trn> <B> <!-- end header -->
						</tr>
						<!-- Page Logic -->
						<tr>
							<td width="100%">
							<table width="100%" border="0" class="inside" style="font-size:12px; font-family:Arial, Helvetica, sans-serif;">
								<field:display name="Admin - Component Type Name" feature="Admin - Component Type">
						  		<tr>
									<td width="35%" height="22" align="right" class="inside"><font color=red>*</font> <digi:trn
										key="aim:typeName">Name</digi:trn>
								    &nbsp;</td>
								  <td width="65%" class="inside"><html:text property="name" size="40"/></td>
								</tr>
								</field:display>
								<field:display name="Admin - Component Type Code" feature="Admin - Component Type">
								<tr>
									<td width="35%" height="22" align="right" class="inside"><font color=red>*</font> <digi:trn
										key="aim:typeCode"> Code</digi:trn>
								    &nbsp;</td>
								  <td width="65%" class="inside"><html:text property="code" size="10" /></td>
								</tr>
								</field:display>
								<field:display name="Admin - Component Type Enable checkbox" feature="Admin - Component Type">
								<tr>
									<td width="35%" height="22" align="right" class="inside"><digi:trn key="aim:typeEbable">Enabled</digi:trn>
&nbsp;									</td>
								  <td width="65%" class="inside"><html:checkbox property="enable" /></td>
								</tr>
								</field:display>
								<field:display name="Admin - Component Type Selectable checkbox" feature="Admin - Component Type">
								<tr>
									<td width="35%" height="22" align="right" class="inside"><digi:trn key="aim:typeEbable">Selectable</digi:trn>
&nbsp;									</td>
								  <td width="65%" class="inside"><html:checkbox property="selectable" /></td>
								</tr>
								</field:display>
								<tr>
									<td height="22" colspan="2" align="right"><hr></td>
								</tr>
								<tr>
									<td colspan="2" class="inside">
									<table width="100%" cellspacing="5">
										<tr>
											<td height="25" align="center" colspan="2">
											<field:display name="Admin - Component Type Save Button" feature="Admin - Component Type">
											  <input class="buttonx" id="addBtn" type="button"
												value='<digi:trn key="btn:save">Save</digi:trn>'
												onClick="return updateComponentsType()">
											</field:display>
											<field:display name="Admin - Component Type Cancel Button" feature="Admin - Component Type">
											  <html:reset styleClass="buttonx" property="submitButton">
												<digi:trn key="btn:reset">Reset</digi:trn></html:reset>
											</field:display> 	
											<field:display name="Admin - Component Type Close Button" feature="Admin - Component Type">					
            								  <html:button
												styleClass="buttonx" property="submitButton"
												onclick="closeWindow()">
												<digi:trn key="btn:close">Close</digi:trn>
											  </html:button>
											</field:display>											</td>
										</tr>
									</table>								  </td>
								</tr>
							</table>
						  </td>
							<td height="20">
						</tr>
						<!-- end page logic -->
					</table>
					</td>
				</tr>
			</table>
			</td>
		</tr>
	</table>

	</td>
	</tr>
</digi:form>
</body>
