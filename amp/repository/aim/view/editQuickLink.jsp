<%@ page pageEncoding="UTF-8" %>
<%@ taglib uri="/taglib/struts-bean" prefix="bean" %>
<%@ taglib uri="/taglib/struts-logic" prefix="logic" %>
<%@ taglib uri="/taglib/struts-html" prefix="html" %>
<%@ taglib uri="/taglib/digijava" prefix="digi" %>
<%@ taglib uri="/taglib/jstl-core" prefix="c" %>

<style type="text/css">
.label{
   FONT-WEIGHT: bold;
   FONT-SIZE: 11px;
   COLOR: #666666;
   FONT-FAMILY: Verdana, Arial, Helvetica, sans-serif;
}
.inp-text {
   FONT-SIZE: 11px;
   FONT-FAMILY: Verdana, Arial, Helvetica, sans-serif;
   COLOR: #000000 ;
}
</style>

<SCRIPT LANGUAGE="JavaScript">
function saveNewLink() {
  document.forms[0].submit();
  opener.location.reload(true);
  window.close();
}

function load() {
  //document.forms[0].linkName.focus();
}
</SCRIPT>
<digi:context name="digiContext" property="context" />

<digi:form action="/editQuickLink.do" method="post">
<html:hidden styleId="tempId" property="tempId" />
<html:hidden styleId="oId" property="id" />
<html:hidden property="action"/>

<TABLE bgcolor="#dddddd" width="400" border="1">
  <TR>
    <TD width="90" class="label">
    Link Name
    </TD>
    <TD>
      <html:text styleId="linkName" property="linkName" size="45" styleClass="inp-text"/>

    </TD>
  </TR>
  <TR>
    <TD width="90" class="label">
    Link
    </TD>
    <TD>
      <html:text styleId="link" property="link" size="45" styleClass="inp-text"/>

    </TD>
  </TR>
  <TR>
    <TD colspan="2" align="center">
      <input type="button" value="Save" onclick="saveNewLink()" class="inp-text">&nbsp;&nbsp;
      <input type="button" value="Close" onclick="window.close()" class="inp-text">
    </TD>
  </TR>
</TABLE>
</digi:form>
