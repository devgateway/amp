<%@ page pageEncoding="UTF-8" %>
<%@ taglib uri="/taglib/struts-bean" prefix="bean" %>
<%@ taglib uri="/taglib/struts-logic" prefix="logic" %>
<%@ taglib uri="/taglib/struts-html" prefix="html" %>
<%@ taglib uri="/taglib/digijava" prefix="digi" %>
<%@ taglib uri="/taglib/jstl-core" prefix="c" %>

<digi:ref href="css/styles.css" type="text/css" rel="stylesheet" />

<digi:instance property="SetBudgetProgramForm" />

<script language="JavaScript" type="text/javascript" src="<digi:file src="module/aim/scripts/calendar.js"/>"></script>
<script language="JavaScript" type="text/javascript" src="<digi:file src="module/aim/scripts/common.js"/>"></script>

<script language="JavaScript">

function Save() {
    var dropdown=document.getElementById("seletedprogramDropDown");
    var index=dropdown.selectedIndex;
    var selectedOption=dropdown.options[index];
    if( selectedOption.value=='0'){
        alert("<digi:trn jsFriendly='true'>Please Select Program</digi:trn>!");
        return false;
    }
    <digi:context name="back" property="context/module/moduleinstance/setbudgetprogram.do" />
    document.SetBudgetProgramForm.action = "<%= back %>~save=true";
    document.SetBudgetProgramForm.submit();
    closePopup()
}

function closePopup() {
  <digi:context name="back" property="context/module/moduleinstance/BudgetManager.do" />
  document.SetBudgetProgramForm.action = "<%= back %>";
  document.SetBudgetProgramForm.target = window.opener.name;
  document.SetBudgetProgramForm.submit();
  window.close();
}

var enterBinder	= new EnterHitBinder('setBudgPrgBtn');
</script>
<digi:form action="/setbudgetprogram.do">
<table bgcolor=#f4f4f2 cellPadding=5 cellSpacing=5 width="100%" class=box-border-nopadding>
    <tr>
      <td align=left valign="top">
        <table bgcolor=#aaaaaa cellpadding="0" cellspacing="0" width="100%" class=box-border-nopadding>
          <tr bgcolor="#aaaaaa">
            <td vAlign="center" width="100%" align ="center" class="textalb" height="20">
              <digi:trn>Budget Program</digi:trn>
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
                        <digi:trn>Select Budget Program</digi:trn>&nbsp;
                      </td>
                      <td align="left" valign="middle">
                        <html:select name="SetBudgetProgramForm" property="seletedprogram" styleClass="inp-text" styleId="seletedprogramDropDown">
							<html:option value="0">Select Program</html:option>
							<html:optionsCollection property="programs" value="ampThemeId" label="name"/>
						</html:select>
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
                              <input type="button" value="${trnSaveBtn}" onclick="return Save()" class="dr-menu" id="setBudgPrgBtn">
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
                    <digi:trn>All fields marked with an </digi:trn><FONT color=red><B><BIG>*</BIG></B></FONT><digi:trn> are required. </digi:trn>
                </td>
              </tr>
        </table>
      </td>
            </tr>
  </table>
  </digi:form>
