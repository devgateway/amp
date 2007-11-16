<%@ page pageEncoding="UTF-8" %>
<%@ taglib uri="/taglib/struts-bean" prefix="bean" %>
<%@ taglib uri="/taglib/struts-logic" prefix="logic" %>
<%@ taglib uri="/taglib/struts-tiles" prefix="tiles" %>
<%@ taglib uri="/taglib/struts-html" prefix="html" %>
<%@ taglib uri="/taglib/digijava" prefix="digi" %>
<%@ taglib uri="/taglib/jstl-core" prefix="c" %>
<%@ taglib uri="/taglib/category" prefix="category" %>
<script language="javascript">
function selectLocation(){
  document.getElementById("indAction").value="add";
  document.forms[0].submit();
  window.opener.document.forms[0].submit();
  window.close();
}
</script>
<digi:instance property="aimThemeForm" />
<digi:form action="/selectLocationForIndicatorValue.do" method="post">
  <html:hidden property="action" styleId="indAction" value=""/>
  <table align="center" width="100%">
    <tr style="background-color:Silver;">
      <td colspan="10" align="center" style="font-size:14px;height:30px;">
        <b>Select location</b>
      </td>
    </tr>
    <tr>
      <td colspan="10">
        &nbsp;
      </td>
    </tr>
    <c:if test="${!empty aimThemeForm.locationsCol}">
      <tr align="center">
        <td>
          Keyword: <html:text property="keyword"/>
          <input type="submit" value="Search"/>
        </td>
      </tr>
      <tr>
        <td colspan="10">
        &nbsp;
        </td>
      </tr>
      <tr>
        <td colspan="10">
          <table align="center" width="100%" style="margin:0px 3px 3px 3px;padding:0px 3px 3px 3px;">
            <c:forEach var="loc" items="${aimThemeForm.locationsCol}">
              <tr>
                <td colspan="10">
                &nbsp;
                </td>
              </tr>
              <tr style="background-color:#EFFAF0;">
                <td valign="middle" style="margin:5px 1px 5px 5px;padding:5px 1px 5px 5px;width:5px;" >
                  <html:radio property="selectedLocationId" value="${loc.ampRegionId}"/>
                </td>
                <td>
                ${loc.name}
                </td>
              </tr>
            </c:forEach>
          </table>
        </td>
      </tr>
      <tr>
        <td colspan="10">
        &nbsp;
        </td>
      </tr>
      <tr align="center">
        <td>
          <input type="button" value="Select" onclick="selectLocation();"/>
        </td>
      </tr>
    </c:if>
    <c:if test="${empty aimThemeForm.locationsCol}">
      <tr align="center">
        <td>
        No Locations
        </td>
      </tr>
    </c:if>
  </table>
</digi:form>
