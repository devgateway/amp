<%@ page pageEncoding="UTF-8" %>
<%@ taglib uri="/taglib/struts-bean" prefix="bean" %>
<%@ taglib uri="/taglib/struts-logic" prefix="logic" %>
<%@ taglib uri="/taglib/struts-tiles" prefix="tiles" %>
<%@ taglib uri="/taglib/struts-html" prefix="html" %>
<%@ taglib uri="/taglib/digijava" prefix="digi" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<script language="javascript" type="text/javascript">
function setActionMethod(methodName) {
  document.getElementsByName('method')[0].value=methodName;
  document.getElementsByName('dmSettingsForm')[0].submit();
  return false;
}
function setBrowseItem(itemUUID) {
  document.getElementsByName('currentItem')[0].value=itemUUID;
  return setActionMethod('redisplay');
}
function setCurrentItem(itemPath) {
  document.getElementsByName('repositoryPath')[0].value=itemPath;
  return false;
  //return setActionMethod('choose');
}
</script>

<table width="100%">
  <digi:form action="/editSettings.do">
  <tr>
    <td colspan="2" align="center"><h1><digi:trn key="dm:chooseJCRRepository">Choose JCR Repository</digi:trn></h1></td>
  </tr>
    <tr>
      <td width="20%" nowrap="nowrap"><digi:trn key="dm:currentPath">Current Path</digi:trn></a></td>
      <td width="80%" nowrap="nowrap">
        <html:hidden property="method"  />
        <html:hidden property="currentItem"  />
        <html:text property="repositoryPath" style="width: 60%;" />
        <a href="#" onclick="return setActionMethod('save');"><digi:trn key="dm:save">Save</digi:trn></a>
    </tr>
    <tr>
      <td colspan="2">
        <table width="100%">
          <tr>
            <th colspan="3"><digi:trn key="dm:repositoryBrowser">Repository Browser</digi:trn></th>
          </tr>
          <tr>
            <td colspan="3">
              <c:forEach var="folder" varStatus="varStatus" items="${dmSettingsForm.breadcrumb}">
              >><a href="#" onclick="return setBrowseItem('${folder.node.UUID}');">${folder.name}</a>
              </c:forEach>
            </td>
          </tr>
          <c:forEach var="folder" items="${dmSettingsForm.folders}">
            <tr>
              <td width="80%">${folder.name}</td>
              <c:if test="${folder.containerItem}">
                <td><a href="#" onclick="return setBrowseItem('${folder.node.UUID}');"><digi:trn key="dm:browseFolder">Browse</digi:trn></a></td>
                <td><a href="#" onclick="return setCurrentItem('${folder.node.path}');"><digi:trn key="dm:chooseFolder">Choose</digi:trn></a></td>
              </c:if>
              <c:if test="${!folder.containerItem}">
                <td colspan="2">&nbsp;</td>
              </c:if>

            </tr>
          </c:forEach>
          <tr>
            <td colspan="3" style="border-top: 1px solid;"><a href="#" onclick="return setActionMethod('browseUp');"><digi:trn key="dm:browseUp">Browse Up</digi:trn></a></td>
          </tr>
        </table>
</td>
    </tr>

  </digi:form>
</table>
