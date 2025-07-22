<%@ page pageEncoding="UTF-8" %>
<%@ taglib uri="/taglib/struts-bean" prefix="bean" %>
<%@ taglib uri="/taglib/struts-logic" prefix="logic" %>
<%@ taglib uri="/taglib/struts-tiles" prefix="tiles" %>
<%@ taglib uri="/taglib/struts-html" prefix="html" %>
<%@ taglib uri="/taglib/digijava" prefix="digi" %>
<%@ taglib uri="/taglib/jstl-core" prefix="c" %>
<%@ taglib uri="/taglib/category" prefix="category" %>
<script language="javascript">


function selectProgram(){

  <digi:context name="addprogram" property="context/module/moduleinstance/selectProgramForIndicator.do?action=add" />
  document.aimNewIndicatorForm.action="<%=addprogram%>";
  document.aimNewIndicatorForm.target = window.opener.name;
  document.aimNewIndicatorForm.submit();
  window.close();
  
}

</script>
<digi:instance property="aimNewIndicatorForm" />
<digi:form action="/selectProgramForIndicator.do" method="post">
  <html:hidden property="action" styleId="indAction" value=""/>
  <c:if test="${aimNewIndicatorForm.action == 'edit'}">
  <html:hidden property="edit"  value="edit"/>
  </c:if>
  <table align="center" width="100%">
    <tr style="background-color:Silver;">
      <td colspan="10" align="center" style="font-size:14px;height:30px;">
        <b>Select program</b>
        ${aimNewIndicatorForm.action}
      </td>
    </tr>
    <tr>
      <td colspan="10">
        &nbsp;
      </td>
    </tr>
    <c:if test="${!empty aimNewIndicatorForm.programsCol}">
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
            <c:forEach var="prog" items="${aimNewIndicatorForm.programsCol}">
              <tr>
                <td colspan="10">
                &nbsp;
                </td>
              </tr>
              <tr style="background-color:#EFFAF0;">
                <td valign="middle" style="margin:5px 1px 5px 5px;padding:5px 1px 5px 5px;width:5px;" >
                  <html:radio property="selectedProgramId" value="${prog.ampThemeId}"/>
                </td>
                <td>
                ${prog.name}
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
          <input type="button" value="Select" onclick="selectProgram()"/>
        </td>
      </tr>
    </c:if>
    <c:if test="${empty aimNewIndicatorForm.programsCol}">
      <tr align="center">
        <td>  
        <digi:trn key="aim:noProgramsPresent">No Programs present</digi:trn>
        </td>
      </tr>
    </c:if>
  </table>
</digi:form>
