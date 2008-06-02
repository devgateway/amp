<%@ page pageEncoding="UTF-8" %>
<%@ taglib uri="/taglib/struts-bean" prefix="bean" %>
<%@ taglib uri="/taglib/struts-logic" prefix="logic" %>
<%@ taglib uri="/taglib/struts-tiles" prefix="tiles" %>
<%@ taglib uri="/taglib/struts-html" prefix="html" %>
<%@ taglib uri="/taglib/digijava" prefix="digi" %>
<%@ taglib uri="/taglib/jstl-core" prefix="c" %>

<jsp:include page="/repository/aim/view/teamPagesHeader.jsp" flush="true" />

<c:set var="contextPath" scope="session">${pageContext.request.contextPath}</c:set>
<script language="JavaScript" type="text/javascript" src="<digi:file src="module/aim/scripts/common.js"/>"></script>
<script type="text/javascript">
<!--
	function addColumn(myForm,id){
		openURLinWindow('${contextPath}/gis/adminTableWidgets.do?actType=showColumnPopup');
		<digi:context name="justSubmit" property="context/module/moduleinstance/adminTableWidgets.do?actType=edit" />
		myForm.action="<%=justSubmit%>&id="+id;  
		myForm.submit();
	}
	function cancelEdit(myForm){
		<digi:context name="justSubmit" property="context/module/moduleinstance/adminTableWidgets.do?actType=cancelEdit" />
		myForm.action="<%=justSubmit%>";  
		myForm.submit();
	}
//-->
</script>



<digi:instance id="dform" property="gisTableWidgetDataForm"/>
<digi:form action="/tableWidgetData.do?actType=save">


<table id="widgetOuter" border="0" cellpadding="15">
	<tr>
		<td colspan="2">
			<span class=crumb>
              <c:set var="translation">
                <digi:trn key="aim:clickToViewAdmin">Click here to goto Admin Home</digi:trn>
              </c:set>
              <html:link  href="/aim/admin.do" styleClass="comment" title="${translation}" >
                <digi:trn key="aim:AmpAdminHome">Admin Home</digi:trn>
              </html:link>&nbsp;&gt;&nbsp;
              <digi:link href="/adminTableWidgets.do" styleClass="comment">
                <digi:trn key="admin:Navigation:WidgetList">Table Widgets</digi:trn>
              </digi:link>&nbsp;&gt;&nbsp;
              <digi:trn key="admin:Navigation:editWidgetData">table widget data</digi:trn>
			</span>
		</td>
	</tr>
	<tr>
		<td colspan="2">
			<span class="subtitle-blue">Table widget data</span>
		</td>
	</tr>
	<tr>
		<td>
			REST !
		</td>
	</tr>
</table>


</digi:form>