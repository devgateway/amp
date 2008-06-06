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
	function addRow(myForm,indx){
		<digi:context name="justSubmit" property="context/module/moduleinstance/tableWidgetData.do?actType=addRow" />
		myForm.action="<%=justSubmit%>&rowIndex="+indx;  
		myForm.submit();
	}
	function removeRow(myForm,indx){
		<digi:context name="justSubmit" property="context/module/moduleinstance/tableWidgetData.do?actType=removeRow" />
		myForm.action="<%=justSubmit%>&rowIndex="+indx;  
		myForm.submit();
	}
	function cancelEdit(myForm){
		<digi:context name="justSubmit" property="context/module/moduleinstance/tableWidgetData.do?actType=cancelEdit" />
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
			<table width="100%">
				<tr bgColor="#d7eafd">
					<c:forEach var="col" items="${dform.columns}" varStatus="cvarstat">
						<td>
							<strong>
								${col.name}
							</strong>
						</td>
					</c:forEach>
					<td>
						<strong>
							Operations
						</strong>
					</td>
				</tr>
				<c:forEach var="row" items="${dform.matrix}" varStatus="rvarstat">
					<tr>
						<c:forEach var="cell" items="${row}" varStatus="cvarstat">
							<td>
								<html:text name="dform" property="cell[${rvarstat.index}][${cvarstat.index}]" value="${cell}"/>
							</td>
						</c:forEach>
						<td>
							<input type="button" value="Add" onclick="addRow(this.form,${rvarstat.index})">
							<input type="button" value="Remove" onclick="removeRow(this.form,${rvarstat.index})">
						</td>
					</tr>
				</c:forEach>
			</table>
		</td>
	</tr>
	<tr align="right">
		<td>
			<table>
				<tr>
					<td>
						<input type="button" value="Cancel" title="Cancel and return to list" onclick="cancelEdit(this.form)">
					</td>
					<td>
						<html:submit title="Submit all moidications" value="Save"/>
					</td>
				</tr>
			</table>
		</td>
	</tr>
</table>


</digi:form>