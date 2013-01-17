<%@ page pageEncoding="UTF-8" %>
<%@ taglib uri="/taglib/struts-bean" prefix="bean" %>
<%@ taglib uri="/taglib/struts-logic" prefix="logic" %>
<%@ taglib uri="/taglib/struts-tiles" prefix="tiles" %>
<%@ taglib uri="/taglib/struts-html" prefix="html" %>
<%@ taglib uri="/taglib/digijava" prefix="digi" %>
<%@ taglib uri="/taglib/jstl-core" prefix="c" %>
<%@ taglib uri="/taglib/fieldVisibility" prefix="field" %>
<%@ taglib uri="/taglib/featureVisibility" prefix="feature" %>
<%@ taglib uri="/taglib/moduleVisibility" prefix="module" %>

<jsp:include page="/repository/aim/view/teamPagesHeader.jsp"  />

<c:set var="contextPath" scope="session">${pageContext.request.contextPath}</c:set>
<script language="JavaScript" type="text/javascript" src="<digi:file src="module/aim/scripts/common.js"/>"></script>
<script type="text/javascript">
<!--
	var reallyDeleteColumn = '<digi:trn jsFriendly="true">Do you really want to remove the column? this will remove data in this column.</digi:trn>';

	function addColumn(id){
		var myForm = document.getElementById('tableId').form;
		refreshThis(myForm,id);
		openURLinWindow('${contextPath}/widget/adminTableWidgets.do?actType=showColumnPopup');
	}
	function cancelEdit(){
		var myForm = document.getElementById('tableId').form;
		<digi:context name="justSubmit" property="context/module/moduleinstance/adminTableWidgets.do?actType=cancelEdit" />
		myForm.action="<%=justSubmit%>";  
		myForm.submit();
	}
	function refreshThis(myForm,id){
		<digi:context name="justSubmit" property="context/module/moduleinstance/adminTableWidgets.do?actType=redraw" />
		myForm.action="<%=justSubmit%>&id="+id;  
		myForm.submit();
	}
	function editCol(colId,id){
		var myForm = document.getElementById('tableId').form;
		refreshThis(myForm,id);
		openURLinWindow('${contextPath}/widget/adminTableWidgets.do~actType=showColumnEditPopup~colId='+colId);
	}
	function deleteCol(colId){
		var myForm = document.getElementById('tableId').form;
		if ( confirm(reallyDeleteColumn) ){
			<digi:context name="justSubmit" property="context/module/moduleinstance/adminTableWidgets.do?actType=removeColumn" />
			myForm.action="<%=justSubmit%>&colId="+colId;  
			myForm.submit();
		}
	}
	function moveUp(colId){
		var myForm = document.getElementById('tableId').form;
		<digi:context name="justSubmit" property="context/module/moduleinstance/adminTableWidgets.do?actType=reorderUp" />
		myForm.action="<%=justSubmit%>&colId="+colId;  
		myForm.submit();
	}
	function moveDown(colId){
		var myForm = document.getElementById('tableId').form;
		<digi:context name="justSubmit" property="context/module/moduleinstance/adminTableWidgets.do?actType=reorderDown" />
		myForm.action="<%=justSubmit%>&colId="+colId;  
		myForm.submit();
	}
	function nameAsTitleSettingChanged(){
		var chk=document.getElementsByName('nameAsTitle')[0];
		chk.value=chk.checked; 
		alert('nameAsTitle='+chk.value);
	}
	$(document).ready(function(){
		var mainTextBox = document.getElementsByName('name')[0];
		mainTextBox.focus();
	});
	function checkRequiredFields(){
		var name=document.gisTableWidgetCreationForm.name;
		if(name.value==""){
			alert("<digi:trn jsFriendly='true'>Please provide name for widget</digi:trn>");
			name.focus();
			return false;
		}
		else{
			return true;
		}
	}
	
//-->
</script>



<digi:instance id="wform" property="gisTableWidgetCreationForm"/>
<digi:form action="/adminTableWidgets.do?actType=save">

<table id="widgetOuter" border="0" cellpadding="0" width=1000 align=center style="font-size:12px;">
	<!--<tr>
		<td colspan="2" bgcolor=#f2f2f2 height=40 style="padding-left:10px; padding-top:10px; padding-bottom:10px;">
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
              <digi:trn key="admin:Navigation:createEditWidget"><b>table widget form</b></digi:trn>		
			</span>
		</td>
	</tr>-->
	<tr>
		<td colspan="2" style="padding-bottom:10px; padding-top:10px;">
			<h1 class="admintitle"><digi:trn key="gis:editTableWidget:pageTitle">Edit table widget</digi:trn></h1>
		</td>
	</tr>
	<tr>
		<td width="50%" bgcolor=#f2f2f2 style="padding-top:20px; padding-bottom:20px;">

			<table width="90%" border="0" align="center" cellpadding="5" id="tableNames" style="font-family:verdana;font-size:11px; border:1px solid silver;" bgcolor=#FFFFFF>
				<tr>
					<td width="50%" align="right" nowrap="nowrap"><font color="red">*</font><strong>
				  <digi:trn key="gis:editTableWidget:nameTitle">Name:</digi:trn></strong></td>
					<td width="50%"><html:text name="wform" property="name" style="width : 200px"/></td>
				</tr>
                                <field:display name="Table Show name as widget title" feature="Table Widgets">
				<tr>
					<td align="right" nowrap="nowrap"><strong><digi:trn key="gis:editTableWidget:nameAsTitleTitle">Show name as widget title:</digi:trn></strong></td>
					<td><html:checkbox name="wform" property="nameAsTitle"/></td>
				</tr>
                                </field:display>
                                <field:display name="Table Code" feature="Table Widgets">
				<tr>
					<td align="right" nowrap="nowrap"><strong><digi:trn key="gis:editTableWidget:codeTitle">Code:</digi:trn></strong></td>
					<td><html:text name="wform" property="code" style="width : 200px"/></td>
				</tr>
                                </field:display>
                                <field:display name="Table CSS class" feature="Table Widgets">
				<tr>
					<td align="right" nowrap="nowrap"><strong><digi:trn key="gis:editTableWidget:cssClassTitle">CSS class:</digi:trn></strong></td>
					<td><html:text name="wform" property="cssClass" style="width : 200px"/></td>
				</tr>
                                </field:display>
                                <field:display name="Table Style" feature="Table Widgets">
				<tr>
					<td align="right" nowrap="nowrap"><strong><digi:trn key="gis:editTableWidget:styleTitle">Style:</digi:trn></strong></td>
					<td><html:text name="wform" property="htmlStyle" style="width : 200px"/></td>
				</tr>
                                </field:display>
                                <field:display name="Table Width" feature="Table Widgets">
				<tr>
					<td align="right" nowrap="nowrap"><strong><digi:trn key="gis:editTableWidget:widthTitle">Width:</digi:trn></strong></td>
					<td><html:text name="wform" property="width" style="width : 200px"/></td>
				</tr>
                                </field:display>
                                <field:display name="Table Place" feature="Table Widgets">
				<tr>
					<td align="right" nowrap="nowrap"><strong><digi:trn key="gis:editTableWidget:placeTitle">Place:</digi:trn></strong></td>
					<td>
						<html:select name="wform" property="selPlaces" multiple="true" size="7" style="width : 200px">
							<html:optionsCollection name="wform" property="places" label="label" value="value"/>
						</html:select>
					</td>
				</tr>
                                </field:display>
				<tr>
					<td align="right">
						<c:set var="cancelButton"><digi:trn key="gis:cancelButton">Cancel</digi:trn></c:set>
						<input type="button" value="${cancelButton}" title="Cancel and return to list" class="buttonx" onclick="cancelEdit()">
					</td>
					<td>
						<c:set var="saveButton"><digi:trn key="gis:saveButton">Save</digi:trn></c:set>
						<html:submit title="Save table widget" styleClass="buttonx" value="${saveButton}" onclick="return checkRequiredFields()" />
					</td>
				</tr>
		  </table>

	  </td>
		<td width="50%" valign="top" bgcolor=#f2f2f2 style="padding-top:20px; padding-bottom:20px;"> 
		
			<table id="columns_list" width="90%"  align="center" style="font-size:11px;border:1px solid silver;">
				<tr bgColor="#c7d4db">
					<td><strong><digi:trn key="gis:editTableWidget:colName">Column Name</digi:trn></strong></td>
                                        <field:display name="Table Column Code" feature="Table Widgets"><td><strong><digi:trn key="gis:editTableWidget:colCode">Code</digi:trn></strong></td></field:display>
					<field:display name="Table Column CSS class" feature="Table Widgets"><td><strong><digi:trn key="gis:editTableWidget:colCssClass">CSS class</digi:trn></strong></td></field:display>
                                        <field:display name="Table Column Pattern" feature="Table Widgets"><td><strong><digi:trn key="gis:editTableWidget:colPate">Pattern</digi:trn></strong></td></field:display>
					<td colspan="4"><strong><digi:trn key="gis:editTableWidget:colOps">Operations</digi:trn></strong></td>
				</tr>
				<c:forEach var="column" items="${wform.columns}" varStatus="varStat">
					<tr>
						<td>
							<c:out value="${column.name}"/>
						</td>
                                                <field:display name="Table Column Code" feature="Table Widgets">
						<td>
							<c:out value="${column.code}"/>
						</td>
                                                </field:display>
                                                <field:display name="Table Column CSS class" feature="Table Widgets">
						<td>
							<c:out value="${column.cssClass}"/>
						</td>
                                                </field:display>
                                                <field:display name="Table Column Pattern" feature="Table Widgets">
						<td>
							<c:out value="${column.pattern}"/>
						</td>
                                                </field:display>
						<td>
							<c:if test="${not empty wform.id}">
								<a href="javascript:editCol(${column.id},${wform.id})"><digi:trn key="gis:editLink">Edit</digi:trn></a>
							</c:if>			
							<c:if test="${empty wform.id}">
								<a href="javascript:editCol(${column.id},null)"><digi:trn key="gis:editLink">Edit</digi:trn></a>
							</c:if>			
						</td>
						<td>
							<a href="javascript:deleteCol(${column.id})"><img border="0" src='<digi:file src="images/deleteIcon.gif"/>'></a>
						</td>
						<td>
							<c:if test="${varStat.first != true}">
								<a href="javascript:moveUp(${column.id})"><img border="0" src='<digi:file src="images/up.gif"/>'></a>
							</c:if>
						</td>
						<td>
							<c:if test="${varStat.last != true}">
								<a href="javascript:moveDown(${column.id})"><img border="0" src='<digi:file src="images/down.gif"/>'></a>
							</c:if>
						</td>
					</tr>
				</c:forEach>
		  </table>
			<br>
			<c:set var="addColumnButton"><digi:trn key="gis:addColumnButton">Add Column</digi:trn></c:set>
			<c:if test="${not empty wform.id}">
				<input type="button" onclick="addColumn(${wform.id})" class="buttonx" value="${addColumnButton}" title="Submit" style="margin-left:25px;">
			</c:if>			
			<c:if test="${empty wform.id}">
				<input type="button" onclick="addColumn(null)" class="buttonx" value="${addColumnButton}" title="Submit" style="margin-left:25px;">
			</c:if>			
	  </td>
	</tr>
</table>
<html:hidden styleId="tableId" property="id"/>

</digi:form>