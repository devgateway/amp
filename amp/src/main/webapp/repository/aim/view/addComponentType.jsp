<%@ page pageEncoding="UTF-8"%>
<%@ taglib uri="/taglib/struts-bean" prefix="bean"%>
<%@ taglib uri="/taglib/struts-logic" prefix="logic"%>
<%@ taglib uri="/taglib/struts-tiles" prefix="tiles"%>
<%@ taglib uri="/taglib/struts-html" prefix="html"%>
<%@ taglib uri="/taglib/digijava" prefix="digi"%>
<%@ taglib uri="/taglib/fieldVisibility" prefix="field"%>
<%@ taglib uri="/taglib/featureVisibility" prefix="feature"%>
<%@ taglib uri="/taglib/moduleVisibility" prefix="module"%>
<script language="JavaScript">

function onDelete() {
  var flag = confirm('<digi:trn jsFriendly="true" key="aim:deleteconfirm">Are you sure?</digi:trn>');
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
  if ((document.aimComponentsTypeForm.name.value).trim().length == 0)
  {
    alert("<digi:trn key="aim:errortypeName">Please Enter the name</digi:trn>");
    document.aimComponentsTypeForm.name.focus();
    return false;
  }
  if ((document.aimComponentsTypeForm.code.value).trim().length == 0)
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

var enterBinder	= new EnterHitBinder('addBtn');
</script>
<style type="text/css">
<!--
body {
	margin-left: 0px;
	margin-top: 0px;
	margin-right: 0px;
	margin-bottom: 0px;
}

.buttonx {background-color:#5E8AD1; border-top: 1px solid #99BAF1; border-left:1px solid #99BAF1; border-right:1px solid #225099; border-bottom:1px solid #225099; font-size:11px; color:#FFFFFF; font-weight:bold; padding-left:5px; padding-right:5px; padding-top:3px; padding-bottom:3px;}
table.inside, td.inside {border-color: #CCC; border-style: solid; font-size:12px;}
table.inside1, td.inside1 {border: 0; font-size:12px;}
table.inside, td.inside_zebra {}
table.inside {border-width: 0 0 1px 1px; border-spacing: 0; border-collapse: collapse;}
td.inside {margin: 0; padding: 4px; border-width: 1px 1px 0 0;}
td.inside_header {background-color:#C7D4DB; color:#000; height:30px; border-color: #fff; border-style: solid; font-size:12px; border-width: 1px 1px 1px 1px; border-spacing: 0; border-collapse: collapse; text-align:center;}
hr {border: 0; color: #E5E5E5; background-color: #E5E5E5; height: 1px; width: 100%; text-align: left;}

-->
</style>
<body onLoad="myOnload()">
<digi:instance property="aimComponentsTypeForm" />
<digi:form action="/updateComponentType.do" method="post">


<html:hidden property="check" />
	<html:hidden property="id" />
	<font color=red style="font-family:Arial, Helvetica, sans-serif; font-size:11px; padding:10px; display:block;">* 
								    <digi:trn
										key="aim:addNewComponent:mandatoryFields"> Mandatory fields </digi:trn>
									</font>
	<table width="100%" border="0" align="center" cellpadding="0" cellspacing="0" style="margin-top:15px;font-size:12px; font-family:Arial, Helvetica, sans-serif;">
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
							<td bgColor=#c7d4db height="25" align="center" colspan="5" style="font-size:12px;"><digi:trn
								key="aim:addType"><b>Add Type</b></digi:trn> <B> <!-- end header -->
						</tr>
						<!-- Page Logic -->
						<tr>
							<td width="100%">
							<table width="100%" border="0" style="font-size:12px; font-family:Arial, Helvetica, sans-serif;">
								<field:display name="Admin - Component Type Name" feature="Admin - Component Type">
						  		<tr>
									<td width="50%" height="22" align="right"><font color=red>*</font>
									<b><digi:trn key="aim:typeName">Name</digi:trn></b>
								    &nbsp;</td>
								  <td width="50%"><html:text property="name" size="40"/></td>
								</tr>
								</field:display>
								<field:display name="Admin - Component Type Code" feature="Admin - Component Type">
								<tr>
									<td height="22" align="right"><font color=red>*</font> <digi:trn
										key="aim:typeCode"> <b>Code</b></digi:trn>
								    &nbsp;</td>
								  <td><html:text property="code" size="10" /></td>
								</tr>
								</field:display>
								<field:display name="Admin - Component Type Enable checkbox" feature="Admin - Component Type">
								<tr>
									<td height="22" align="right"><digi:trn key="aim:typeEbable"><b>Enabled</b></digi:trn>
&nbsp;									</td>
								  <td><html:checkbox property="enable" /></td>
								</tr>
								</field:display>
								<field:display name="Admin - Component Type Selectable checkbox" feature="Admin - Component Type">
								<tr>
									<td height="22" align="right"><digi:trn key="aim:typeEbable"><b>Selectable</b></digi:trn>
&nbsp;									</td>
								  <td><html:checkbox property="selectable" /></td>
								</tr>
								</field:display>
								<tr>
									<td height="22" colspan="2" align="right"><hr></td>
								</tr>
								<tr>
									<td colspan="2">
									<table width="100%" cellspacing="5">
										<tr>
											<td height="25" align="center" colspan="2">
											<field:display name="Admin - Component Type Save Button" feature="Admin - Component Type">
											  <input class="buttonx" id="addBtn" type="button"
												value='<digi:trn jsFriendly="true" key="btn:save">Save</digi:trn>'
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
