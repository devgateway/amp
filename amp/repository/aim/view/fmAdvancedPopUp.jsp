<%@ page pageEncoding="UTF-8" %>
<%@ taglib uri="/taglib/struts-html" prefix="html" %>
<%@ taglib uri="/taglib/digijava" prefix="digi" %>
<%@ taglib uri="/taglib/jstl-core" prefix="c" %>


<%@page import="org.digijava.module.aim.fmtool.util.FMToolConstants"%>

<script language="JavaScript">

function cbStatusChange(state, value){

	if (!state){
		var cbItem = document.getElementById('cbId_'+value);
		while (cbItem != null){
			cbItem.checked = state;
			cbItem = document.getElementById('cbId_'+ (++value));
		}
	}
}

</script> 

<digi:instance property="fmAdvancedForm" />

<c:set var="cModule" value="<%= FMToolConstants.FEATURE_TYPE_MODULE %>"/>
<c:set var="cFeature" value="<%= FMToolConstants.FEATURE_TYPE_FEATURE %>"/>
<c:set var="cField" value="<%= FMToolConstants.FEATURE_TYPE_FIELD %>"/>


<digi:form action="/fmAdvancedPopup.do?method=save" method="post" styleId="form">
	<c:forEach var="fVar" items="${fmAdvancedForm.fmeList}" varStatus="lStatus">
		<div style="padding-left: ${fmAdvancedForm.paddingOffset+fVar.paddingLeft+5};">
		
		<input id="cbId_${lStatus.index}" onClick="cbStatusChange(this.checked, ${lStatus.index})" type="checkbox" name="fmeList[${lStatus.index}].visib" <c:if test="${fVar.visib}">checked</c:if> />
		
		<c:if test="${fVar.type==cModule}">
			<img src="/TEMPLATE/ampTemplate/imagesSource/common/folder.gif" alt="" width="15" height="11" border="0"/>
		</c:if>
		<c:if test="${fVar.type==cFeature}">
			<img src="/TEMPLATE/ampTemplate/imagesSource/common/gfolder.gif" alt="" width="15" height="11" border="0"/>
		</c:if>
		<c:if test="${fVar.type==cField}">
			<img src="/TEMPLATE/ampTemplate/imagesSource/common/sheet.gif" alt="" width="15" height="11" border="0"/>
		</c:if>
		${fVar.name}</div>
	</c:forEach>
	<div style="text-align: center;"> <html:submit value="Save" onclick="window.close();"/> </div>
</digi:form>

<br/>
