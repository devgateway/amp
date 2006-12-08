<%@ page pageEncoding="UTF-8" %>
<%@ taglib uri="/taglib/struts-bean" prefix="bean" %>
<%@ taglib uri="/taglib/struts-logic" prefix="logic" %>
<%@ taglib uri="/taglib/struts-tiles" prefix="tiles" %>
<%@ taglib uri="/taglib/struts-html" prefix="html" %>
<%@ taglib uri="/taglib/digijava" prefix="digi" %>
<%@ taglib uri="/taglib/jstl-core" prefix="c" %>
<%@ taglib uri="/taglib/fmt" prefix="fmt" %>

<script language="javaScript" type="">
function setActionMethod(methodName) {
  document.getElementsByName('method')[0].value=methodName;
  return true;
}
function setDeleteId(id) {
  document.getElementsByName('deleteId')[0].value=id;
  setActionMethod('delete');
  document.calendarEventTypeForm.submit();
  return true;
}

</script>
<digi:form action="/eventTypes.do" method="post">

  <!--  AMP Admin Logo -->
  <jsp:include page="teamPagesHeader.jsp" flush="true" />
  <!-- End of Logo -->

  <input type="hidden" name="deleteId" value="-1" />

  <table bgColor=#ffffff cellPadding=0 cellSpacing=0 width=772>
    <tr>
      <td class=r-dotted-lg width=14>&nbsp;</td>
      <td>
        <table>
          <tr>
            <td>
              <span class=crumb>
                <c:set var="translation">
                  <digi:trn key="aim:clickToViewAdmin">Click here to goto Admin Home</digi:trn>
                </c:set>
                <digi:link href="/../admin.do" styleClass="comment" title="${translation}" >
                  <digi:trn key="aim:AmpAdminHome">Admin Home</digi:trn>
                </digi:link>&nbsp;&gt;&nbsp;
                <digi:trn key="calendar:eventTypes">Event Type Manager</digi:trn>
              </span>
            </td>
          </tr>
          <tr>
            <td height=16 vAlign=center>
              <span class=subtitle-blue><br />
                <input type="hidden" name="method" value="NONE" />
                <digi:trn key="calendar:eventTypes:page_header">Event Types</digi:trn>
              </span>
            </td>
          </tr>
          <tr>
            <td>
              <table>
                <tr>
                  <td style="font-size:14pt;"><digi:trn key="calendar:typeName"><b>Name</b></digi:trn></td>
                  <td style="font-size:14pt;"><digi:trn key="calendar:typeColor"><b>Color</b></digi:trn></td>
                  <td>&nbsp;</td>
                </tr>
                <c:forEach items="${calendarEventTypeForm.eventTypes}" var="eventType">
                  <tr>
                    <td>
                      <html:text name="eventType" property="name" indexed="true" readonly="true"/>
                    </td>
                    <td>
                      <html:text name="eventType" property="color" indexed="true" readonly="true"/>
                    </td>
                    <td>
                      <input type="button" value="Delete" onclick="setDeleteId('${eventType.id}');" />
                    </td>
                  </tr>
                </c:forEach>
              </table>
          </td>
        </tr>
        <tr>
          <td>
            <fieldset>
              <legend><digi:trn key="calendar:addNewType">Add a new type</digi:trn></legend>
              <table width="100%">
                <tr>
                  <td colspan="2" align="left">
                    <digi:errors />
                  </td>
                </tr>
                <tr>
                  <td><digi:trn key="calendar:typeName">Name</digi:trn></td>
                  <td><html:text property="addName" /></td>
                </tr>
                <tr>
                  <td><digi:trn key="calendar:typeColor">Color</digi:trn></td>
                  <td><html:text property="addColor" onclick="getElementById('AddBtn').getfocuce;"/></td>
                </tr>
                <tr>
                  <td colspan="2" align="right">
                    <html:submit value="Add" onclick="setActionMethod('addType')" />
                  </td>
                </tr>
              </table>
            </fieldset>
          </td>
        </tr>
    </table>
  </td>
  <td class=r-dotted-lg width=14>&nbsp;</td>
</tr>
</table>
</digi:form>
