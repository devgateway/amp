<%@ page pageEncoding="UTF-8" %>
<%@ taglib uri="/taglib/struts-bean" prefix="bean" %>
<%@ taglib uri="/taglib/struts-logic" prefix="logic" %>
<%@ taglib uri="/taglib/struts-tiles" prefix="tiles" %>
<%@ taglib uri="/taglib/struts-html" prefix="html" %>
<%@ taglib uri="/taglib/digijava" prefix="digi" %>
<%@ taglib uri="/taglib/fieldVisibility" prefix="field" %>
<%@ taglib uri="/taglib/featureVisibility" prefix="feature" %>
<%@ taglib uri="/taglib/moduleVisibility" prefix="module" %>
<%@ taglib uri="/taglib/jstl-functions" prefix="fn" %>

<script language="JavaScript">

function onDelete() {
  var flag = confirm('<digi:trn jsFriendly="true" key="aim:deleteconfirm">Are you sure?</digi:trn>');
  return flag;
}

function onCancel() {
  <digi:context name="cancelComponents" property="context/module/moduleinstance/updateComponents.do?event=cancel" />
  document.aimUpdateComponentsForm.action = "<%= cancelComponents%>";
  document.aimUpdateComponentsForm.target = "_self";
  document.aimUpdateComponentsForm.submit();
}


function validate()
{
  if ((document.aimUpdateComponentsForm.compTitle.value).length == 0)
  {
    alert("<digi:trn key="aim:componenttitlename">Please Enter component title name</digi:trn>");
    document.aimUpdateComponentsForm.compTitle.focus();
    return false;
  }
  if ((document.aimUpdateComponentsForm.compCode.value).length == 0)
  {
    alert("<digi:trn key="aim:componentcode">Please Enter component code</digi:trn>");
    document.aimUpdateComponentsForm.compCode.focus();
    return false;
  }
  return true;
}

function updateComponents(id)
{
  var temp = validate();
  if (temp == true)
  {
    document.aimUpdateComponentsForm.addBtn.disabled = true;
    <digi:context name="updateComponents" property="context/module/moduleinstance/updateComponents.do?event=save" />
    document.aimUpdateComponentsForm.action = "<%= updateComponents%>&componentId="+id;
    document.aimUpdateComponentsForm.target = "_self";
    document.aimUpdateComponentsForm.submit();
    }
  return temp;
}

function myOnload(){
  //alert("onload="+document.aimUpdateComponentsForm.check.value);
  if(document.aimUpdateComponentsForm.check.value=="save"){
    <digi:context name="refreshComp" property="context/module/moduleinstance/getComponents.do" />
    document.aimUpdateComponentsForm.action = "<%= refreshComp %>";
    document.aimUpdateComponentsForm.target = window.opener.name;
    document.aimUpdateComponentsForm.submit();
    closeWindow();
  }
}

function unload(){}

function closeWindow()
{
  window.close();
}
</script>
<body onLoad="myOnload()">
<digi:instance property="aimUpdateComponentsForm" />
<digi:form action="/updateComponents.do" method="post">
  <html:hidden styleId="check" property="check"/>
  <table bgColor=#ffffff cellpadding="0" cellspacing="0">
    <tr>
      <td class=r-dotted-lg width=14>&nbsp;</td>
      <td align=left class=r-dotted-lg valign="top" width=750>
        <table cellPadding=5 cellspacing="0" width="100%">
          <tr>

            <td height=16 valign="center" width=571><span class=subtitle-blue>
              <digi:trn key="aim:addNewComponent">
              Add A New Component
              </digi:trn>
</span>
            </td>
          </tr>
          <tr>
            <td noWrap width="100%" vAlign="top">
              <digi:errors/>					<table width="100%" cellspacing="1" cellspacing="1">
                <tr>
                  <td noWrap width=600 vAlign="top">
                    <table bgColor=#ffffff cellpadding="0" cellspacing="0" class=box-border-nopadding width="100%">
                      <tr bgColor=#f4f4f2>
                        <td vAlign="top" width="100%">&nbsp;
                        
                        </td>
                      </tr>
                      <tr bgColor=#f4f4f2>
                        <td valign="top">
                          <table align="center" bgColor=#f4f4f2 cellpadding="0" cellspacing="0" width="90%" border="0">
                            <tr>
                              <td bgColor=#ffffff class=box-border>
                                <table border="0" cellpadding="1" cellspacing="1" class=box-border width="100%">
                                  <tr bgColor=#dddddb>
                                    <!-- header -->
                                    <td bgColor=#dddddb height="20" 			align="center" colspan="5"><B>
                                      <digi:trn key="aim:addComponent">Add Component</digi:trn>
                                      <!-- end header -->
                                  </tr>
                                  <!-- Page Logic -->
                                  <tr>
                                    <td width="100%">
                                      <table width="100%" border="0"	 bgColor=#f4f4f2>
										<field:display name="Admin - Component Title" feature="Admin - Component">
	                                        <tr>
	                                            <td width="30%" align="right"><font color=red>*</font>
	                                             <digi:trn key="aim:compTitle">Component Title</digi:trn>
			                                    </td>
			                                    <td width="30%" >
			                                      <html:textarea property="compTitle" cols="40" rows="2" />
			                                    </td>
	                                        </tr>
										</field:display>
									    <field:display name="Admin - Component Code" feature="Admin - Component">
                                        <tr>
                                          <td width="30%" align="right"><font color=red>*</font>
                                            <digi:trn key="aim:compCode">Component Code</digi:trn>
                                    </td>
                                    <td width="30%">
                                      <html:text property="compCode" size="10" />
                                    </td>
                                        </tr>
										</field:display>
										<field:display name="Admin - Component Type" feature="Admin - Component">
                                        <tr>
                                          <td width="30%" align="right">
                                            <digi:trn key="aim:compType">Component Type</digi:trn>
                                          </td>
                                          <td width="30%">
                                            <html:select property="compType">
                                          	  <html:optionsCollection property="typeList" label="name" value="type_id"/>
                                            </html:select>
                                            
                                          </td>
                                        </tr>
										</field:display>
										<field:display name="Admin - Component Description" feature="Admin - Component">
                                        <tr>
                                          <td width="30%" align="right">
                                            <digi:trn key="aim:compDesc">Component Description</digi:trn>
                                          </td>
                                          <td width="30%">
                                            <html:textarea property="compDes" cols="40" rows="3" />
                                          </td>
                                        </tr>

								  		</field:display>
								  <tr>
                                  <td width=30% align = right>
                                     <font color=red>*
                                      <digi:trn key="aim:addNewComponent:mandatoryFields">
                                      Mandatory fields
                                      </digi:trn>
                                    </font>
                                  </td>
                                  </tr>
                                  <tr>
                                    <td colspan="2" width="60%">
                                      <table width="100%" cellspacing="5">
                                        <tr>
                                          <td bgColor=#dddddb height="25" align="center" colspan="2">
											<field:display name="Admin - Component Save Button" feature="Admin - Component">
                                            	<input class="dr-menu" id="addBtn" type="button" value='<digi:trn jsFriendly="true" key="btn:save">Save</digi:trn>' onClick="return updateComponents('<bean:write name="aimUpdateComponentsForm" property="id"/>')">&nbsp;&nbsp;
											</field:display>
											<field:display name="Admin - Component Cancel Button" feature="Admin - Component">
												<input type="button" value="<digi:trn jsFriendly="true" key='btn:cancel'>Cancel</digi:trn>" styleClass="dr-menu" onclick="window.close();" />
											</field:display>
                                          </td>
                                        </tr>
                                      </table>
                                    </td>
                                  </tr>
                                      </table>
                              </td>
                            </tr>
                            <!-- end page logic -->
                                </table>
                        </td>
                      </tr>
                          </table>
                  </td>
                </tr>
                <tr>
                  <td bgColor=#f4f4f2>&nbsp;
                  
                  </td>
                </tr>
                    </table>
            </td>
            <td noWrap width="100%" vAlign="top">
            </td>
          </tr>
        </table>
      </td>
    </tr>
  </table>
</td>
</tr>
</table>
</digi:form>
</body>
