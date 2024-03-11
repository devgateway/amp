<%@ page pageEncoding="UTF-8" %>
<%@ taglib uri="/taglib/struts-bean" prefix="bean" %>
<%@ taglib uri="/taglib/struts-logic" prefix="logic" %>
<%@ taglib uri="/taglib/struts-html" prefix="html" %>
<%@ taglib uri="/taglib/digijava" prefix="digi" %>
<%@ taglib uri="/taglib/jstl-core" prefix="c" %>

<digi:ref href="css/styles.css" type="text/css" rel="stylesheet" />

<digi:instance property="EditDepartmentForm" />

<script language="JavaScript" type="text/javascript" src="<digi:file src="module/aim/scripts/calendar.js"/>"></script>
<script language="JavaScript" type="text/javascript" src="<digi:file src="module/aim/scripts/common.js"/>"></script>

<script language="JavaScript">

function saveDep() {
    <digi:context name="back" property="context/module/moduleinstance/editdepartment.do" />
    document.EditDepartmentForm.action = "<%= back %>~edit=true";
    document.EditDepartmentForm.target = window.opener.name;
    document.EditDepartmentForm.submit();
    closePopup();
}
function validateDep(){
	if (document.EditDepartmentForm.depname.value==''){
		alert('<digi:trn jsFriendly="true">Please enter name</digi:trn>');
		return false;
	}
	if (document.EditDepartmentForm.depcode.value==''){
		alert('<digi:trn jsFriendly="true">Please enter code</digi:trn>');
		return false;
	}
	saveDep();
}

function closePopup() {
  window.close();
}

</script>
<digi:form action="/editdepartment.do">
<table bgcolor=#f4f4f2 cellPadding=5 cellSpacing=5 width="100%" class=box-border-nopadding>
    <tr>
      <td align=left valign="top">
        <table bgcolor=#aaaaaa cellpadding="0" cellspacing="0" width="100%" class=box-border-nopadding>
          <tr bgcolor="#aaaaaa">
            <td vAlign="center" width="100%" align ="center" class="textalb" height="20">
              <digi:trn>Departments Editor</digi:trn>
            </td></tr>
            <tr>
              <td vAlign="center" width="100%" align ="center" height="20">
              </td>
              </tr>
              <tr>
                <td align="center">
                  <table border="0" cellpadding="2" cellspacing="1" width="100%">
                    <tr bgcolor="#f4f4f2">
                      <td align="right" valign="middle" width="50%">
                        <FONT color=red>*</FONT>
                        <digi:trn>Department Code</digi:trn>&nbsp;
                      </td>
                      <td align="left" valign="middle">
                        <html:text property="depcode" styleClass="inp-text" size="7"/>
                      </td>
                    </tr>
                    <tr bgcolor="#f4f4f2">
                      <td align="right" valign="middle" width="50%">
                        <FONT color=red>*</FONT>
                        <digi:trn>Department Name</digi:trn>&nbsp;
                      </td>
                      <td align="left" valign="middle">
                        <html:text property="depname" styleClass="inp-text" size="40"/>
                      </td>
                    </tr>
                  	<tr bgcolor="#ffffff">
                      <td colspan="2">
                        <table width="100%" cellpadding="3" cellspacing="3" border="0">
                          <tr>
                            <td align="right">
                              <c:set var="trnSaveBtn">
                                <digi:trn key="aim:btnSave">Save</digi:trn>&nbsp;
                              </c:set>
                              <input type="button" value="${trnSaveBtn}" onclick="return validateDep()" class="dr-menu">
                            </td>
                            <td align="left">
                              <c:set var="trnCloseBtn">
                                <digi:trn key="aim:btnClose">Close</digi:trn>&nbsp;
                              </c:set>
                              <input type="button" value="${trnCloseBtn}" onclick="return closePopup()" class="dr-menu">
                            </td>
                          </tr>
                        </table>
                      </td>
                    </tr>
                  </table>
                </td>
              </tr>
              <tr>
                <td>&nbsp;&nbsp;
                    <digi:trn>All fields marked with an</digi:trn><FONT color=red><B><BIG> * </BIG></B></FONT><digi:trn>are required.</digi:trn>
       
                </td>
              </tr>
        </table>
      </td>
            </tr>
  </table>
  </digi:form>
