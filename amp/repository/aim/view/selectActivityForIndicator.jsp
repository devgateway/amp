<%@ page pageEncoding="UTF-8" %>
<%@ taglib uri="/taglib/struts-bean" prefix="bean" %>
<%@ taglib uri="/taglib/struts-logic" prefix="logic" %>
<%@ taglib uri="/taglib/struts-tiles" prefix="tiles" %>
<%@ taglib uri="/taglib/struts-html" prefix="html" %>
<%@ taglib uri="/taglib/digijava" prefix="digi" %>
<%@ taglib uri="/taglib/jstl-core" prefix="c" %>
<%@ taglib uri="/taglib/category" prefix="category" %>
<script language="javascript">
function selectActivity(){

  <digi:context name="addprogram" property="context/ampModule/moduleinstance/selectActivityForIndicator.do?action=add" />
  document.aimNewIndicatorForm.action="<%=addprogram%>";
  document.aimNewIndicatorForm.target = window.opener.name;
  document.aimNewIndicatorForm.submit();
  window.close();
}

function closeWindow() {
		window.close();
	}
</script>
<digi:instance property="aimNewIndicatorForm" />
<digi:form action="/selectActivityForIndicator.do" method="post">
  <html:hidden property="action" styleId="indAction" value=""/>
  <table align="center" width="100%" border="0" bgcolor="white">
    <tr bgcolor="#006699" class=r-dotted-lg>
      <td colspan="10" align="center" style="font-size:14px;height:30px;color:white">
        <b> <digi:trn key="btn:selectact">Select activity</digi:trn></b>
      </td>
    </tr>
    <tr>
      <td colspan="10">
      <c:if test="${aimNewIndicatorForm.action == 'edit'}">
      <html:hidden property="forward" value="edit"/>
      </c:if>
        &nbsp;
      </td>
    </tr>
      <tr align="center">
        <td>
        	<digi:trn key="btn:keyword">Keyword</digi:trn>: <html:text property="keyword"/>
          <input type="submit" value="<digi:trn key="btn:keyword">Search</digi:trn>"/>
        </td>
      </tr>
      <tr>
        <td colspan="10">
        &nbsp;
        </td>
      </tr>
      <tr>
        <td colspan="10">
          <c:if test="${!empty aimNewIndicatorForm.activitiesCol}">
          <table border="0" align="center" width="100%" style="margin:0px 3px 3px 3px;padding:0px 3px 3px 3px;"  bgcolor="#ECF3FD">
            <c:forEach var="act" items="${aimNewIndicatorForm.activitiesCol}">
              
              <tr onmouseover="style.backgroundColor='#dddddd';" onmouseout="style.backgroundColor='#ECF3FD'">
                <td valign="middle" style="margin:5px 1px 5px 5px;padding:1px 1px 1px 1px;width:1px;">
                  
                  <!--
                  <html:checkbox property="selectedActivityId" value="${act.ampActivityId}"/>
                  -->
                  
                  <html:multibox property="selectedActivity">
                  ${act.ampActivityId}
                  </html:multibox>
                </td>
                <td>
                ${act.name}
                </td>
              </tr>
            </c:forEach>
           
          </table>
        </c:if>
        <c:if test="${empty aimNewIndicatorForm.activitiesCol}">
	          <tr align="center">
	            <td>
		       <b><digi:trn key="admin:noIndicator">No Activities</digi:trn> </b>
		        </td>
	        </tr>
	      </c:if>
        </td>
      </tr>
      <tr>
        <td colspan="10">
        &nbsp;
        </td>
      </tr>
      <tr align="center">
        <td>
        <c:if test="${!empty aimNewIndicatorForm.activitiesCol}">
         <input type="button" value="<digi:trn key="btn:Select">Select</digi:trn>" onclick="selectActivity();"/>
        </c:if>
         <html:button  styleClass="dr-menu" property="submitButton"  onclick="closeWindow();">
			<digi:trn key="btn:close">Close</digi:trn> 
	    </html:button>
        </td>
      </tr>
    
  </table>
</digi:form>
