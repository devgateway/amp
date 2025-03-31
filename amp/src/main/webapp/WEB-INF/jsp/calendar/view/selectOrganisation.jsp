<%@ page pageEncoding="UTF-8" import="org.digijava.module.aim.dbentity.AmpOrganisation, java.util.* "%>
<%@ taglib uri="/taglib/struts-bean" prefix="bean" %>
<%@ taglib uri="/taglib/struts-logic" prefix="logic" %>
<%@ taglib uri="/taglib/struts-tiles" prefix="tiles" %>
<%@ taglib uri="/taglib/struts-html" prefix="html" %>
<%@ taglib uri="/taglib/digijava" prefix="digi" %>
<%@ taglib uri="/taglib/jstl-core" prefix="c" %>

<digi:ref href="css/styles.css" type="text/css" rel="stylesheet" />

<script language="JavaScript">

<!--
function selectOrganization() {
  var flag = 0;
  var i=1;
  while(document.getElementById('chkBox' + i)!=null){
    var chk=document.getElementById('chkBox' + i);
    if(chk.checked){
      window.opener.addOrganisation(document.getElementById('chkBox' + i).value,document.getElementById('orgName' + i).innerHTML);
    }
    i++;
  }
  window.close();
}

function searchOrganization() {
  document.calendarEventForm.submit();
  return true;
}

function load() {
  //document.calendarEditActivityForm.keyword.focus();
  return;
}

function unload() {
  return;
}

function closeWindow() {
  window.close();
}
-->

</script>

<digi:instance property="calendarEventForm" />
<digi:form action="/selectOrganization.do" method="post">
  <table width="100%" cellSpacing=5 cellPadding=5 vAlign="top" border="0">
    <tr>
      <td vAlign="top">
        <table bgcolor=#f4f4f2 cellPadding=5 cellSpacing=5 width="100%" class=box-border-nopadding>
          <tr>
            <td align=left valign="top">
              <table bgcolor=#f4f4f2 cellpadding="0" cellspacing="0" width="100%" class=box-border-nopadding>
                <tr bgcolor="#006699">
                  <td vAlign="center" width="100%" align ="center" class="textalb" height="20">
                    <digi:trn key="calendar:searchOrganization">
                    Search Organizations
                    </digi:trn>
                  </td>
                </tr>
                <tr>
                  <td align="center" bgcolor=#ECF3FD>
                    <table cellSpacing=2 cellPadding=2>
                      <tr>
                        <td>
                          <digi:trn key="aim:enterKeyword">
                          Enter a keyword
                          </digi:trn>
                        </td>
                        <td>
                          <html:text property="searchOrgKey" styleClass="inp-text" />
                        </td>
                      </tr>
                      <tr>
                        <td align="center" colspan=2>
                          <c:set var="trn">
                            <digi:trn key="calendar:selectOrg:btnSearch">
                            Search
                            </digi:trn>
                          </c:set>
                          <input type="button" value="${trn}" class="dr-menu" onclick="return searchOrganization()">&nbsp;

                          <c:set var="trn">
                            <digi:trn key="calendar:selectOrg:btnClear">
                            Clear
                            </digi:trn>
                          </c:set>
                          <input type="reset" value="${trn}" class="dr-menu">&nbsp;

                          <c:set var="trn">
                            <digi:trn key="calendar:selectOrg:btnClose">
                            Close
                            </digi:trn>
                          </c:set>
                          <input type="button" value="${trn}" class="dr-menu" onclick="closeWindow()">
                        </td>
                      </tr>
                    </table>
                  </td>
                </tr>
              </table>
            </td>
          </tr>
          <tr>
            <td align=left valign="top">
              <table bgcolor=#f4f4f2 cellpadding="0" cellspacing="0" width="100%" class=box-border-nopadding>
                <tr bgcolor="#006699">
                  <td vAlign="center" width="100%" align ="center" class="textalb" height="20">
                    <digi:trn key="aim:organizationList">
                      List of Organizations</digi:trn>
                  </td>
                </tr>
                <tr>
                  <td align=left valign="top">
                    <table width="100%" cellPadding=3>
                      <c:forEach items="${calendarEventForm.organisations}" var="org" varStatus="cn" >
                        <tr>
                          <td bgcolor=#ECF3FD width="10%">
                          &nbsp;&nbsp;
                          <input type="checkbox" name="selOrganisations" value="${org.ampOrgId}" id="chkBox${cn.count}" />
                          &nbsp;
                          </td>
                          <td bgcolor=#ECF3FD width="90%" id="orgName${cn.count}">
                          ${org.name}
                          </td>
                        </tr>
                      </c:forEach>
                      <tr>
                        <td align="center" colspan="2">
                          <table cellPadding=5>
                            <tr>
                              <td>
                                <c:set var="trn">
                                  <digi:trn key="calendar:selectOrg:btnAdd">
                                  Add
                                  </digi:trn>
                                </c:set>
                                <input type="button" value="${trn}" class="dr-menu" onclick="return selectOrganization()">
                              </td>
                              <td>
                                <c:set var="trn">
                                  <digi:trn key="calendar:selectOrg:btnClose">
                                  Close
                                  </digi:trn>
                                </c:set>
                                <input type="button" value="${trn}" class="dr-menu" onclick="closeWindow()">
                              </td>
                            </tr>
                          </table>
                        </td>
                      </tr>
                    </table>
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



