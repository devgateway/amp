<%@ page pageEncoding="UTF-8"%>
<%@ taglib uri="/taglib/struts-bean" prefix="bean"%>
<%@ taglib uri="/taglib/struts-logic" prefix="logic"%>
<%@ taglib uri="/taglib/struts-tiles" prefix="tiles"%>
<%@ taglib uri="/taglib/struts-html" prefix="html"%>
<%@ taglib uri="/taglib/digijava" prefix="digi"%>
<%@ taglib uri="/taglib/jstl-core" prefix="c"%>
<%@ taglib uri="/taglib/fieldVisibility" prefix="field"%>
<%@ taglib uri="/taglib/featureVisibility" prefix="feature"%>

<script type="text/javascript" src="/ckeditor_4.4.6/ckeditor.js"></script>
<script type="text/javascript" src="/ckeditor_4.4.6/adapters/jquery.js"></script>

<script>
<%String contextPath = request.getContextPath();
String _editor_url = new String();
if (contextPath.length() == 0 || contextPath.startsWith("/")) {
	_editor_url = contextPath + "/repository/editor/view/";
} else {
	_editor_url = "/" + contextPath + "/repository/editor/view/";
}%>
_editor_url = "<%=_editor_url%>";
</script>

<script language="javascript">
function validate(){
	var size=<%=(request.getParameter("size")!=null)?request.getParameter("size"):-1%>;
	if (size==-1){
		size=100000;
	}
	if ($("[name='content']").ckeditor().html().length > size){
		var msg='<digi:trn jsFriendly="true" key="editor:longtextError">The text is too long.</digi:trn>'+' Max is '+size+' chrs';
		alert(msg);
		return false;
	}
	return true;
}

function cancelText(){
	<digi:context name="url" property="context/module/moduleinstance/cancelText.do" />
	editorForm.action="${url}";
	editorForm.target = "_self";
	editorForm.submit();
}
</script>


<digi:form onsubmit="return validate()" action="/saveText.do" method="post">

	<html:hidden name="editorForm" property="key" />
	<html:hidden name="editorForm" property="lang" />
	<html:hidden name="editorForm" property="returnUrl" />
	<html:hidden name="editorForm" property="title" />

	<c:if test="${not empty editorForm.activityName}">
		<table width="80%">
			<tr>
				<td><br /></td>
			</tr>
			<tr>
				<td noWrap width="7%" align="right"><B><digi:trn>Title</digi:trn>:</B>
				</td>
				<td width="93%"><html:text name="editorForm"
						property="activityName" size="70" readonly="true" /></td>
			</tr>
		</table>
	</c:if>
	<c:if test="${not empty editorForm.activityFieldName}">
		<table width="80%">
			<tr>
				<td><br /></td>
			</tr>
			<tr>
				<td noWrap width="7%" align="right"><B><digi:trn>Field</digi:trn>:</B>
				</td>
				<td width="93%"><input
					value="<digi:trn>${editorForm.activityFieldName}</digi:trn>"
					type="text" size="70" readonly="true" /> <!--   <html:text name="editorForm" property="activityFieldName" size="70"  readonly="true"/>   -->
				</td>
			</tr>
		</table>
	</c:if>
	<br>
	<html:textarea property="content" name="editorForm" cols="100" rows="5"></html:textarea>
	<b>
		<span style="color: #cc4000"> <bean:write name="editorForm" property="key" /></span>
	</b>
	&nbsp;
	
	<c:set var="trn">
		<digi:trn>Save now</digi:trn>
	</c:set>
	<html:submit value="${trn}" />&nbsp;
	
	<html:button property="" onclick="cancelText()">
		<digi:trn>Cancel</digi:trn>
	</html:button>
	<!-- 
	<html:select property="lang" onchange="ChangeLanguage(this)">

	<bean:define id="lid" name="editorForm" property="languages" type="java.util.Collection"/>

	<html:options collection="lid" property="code" labelProperty="name"/></html:select>
	 -->

	<bean:define id="showLanguageSwitch">true</bean:define>
	<c:set var="showLanguageSwitch">
		<feature:display name="Editor language switch"
			module="Project ID and Planning">true</feature:display>
	</c:set>

	<c:if test="${showLanguageSwitch==''}">
		<c:set var="showLanguageSwitch">false</c:set>
	</c:if>

	<%-- Always enabled for admin users --%>
	<digi:secure group="ADMIN">
		<c:set var="showLanguageSwitch">true</c:set>
	</digi:secure>
	<digi:secure group="Administrators">
		<c:set var="showLanguageSwitch">true</c:set>
	</digi:secure>
	<digi:secure group="Translators">
		<c:set var="showLanguageSwitch">true</c:set>
	</digi:secure>

	<c:if test="${showLanguageSwitch == true}">
		<html:select property="lang" onchange="ChangeLanguage(this)">
			<c:forEach var="lng" items="${editorForm.languages}">
				<c:set var="trn">
					<digi:trn key="editor:sourceLanguage:${lng.name}">${lng.name}</digi:trn>
				</c:set>
				<html:option value="${lng.code}">${trn}</html:option>
			</c:forEach>
		</html:select>
	</c:if>
</digi:form>



<script>
	function ChangeLanguage (obj) {
	var lang = obj.value;
	<digi:context name="showText" property="context/module/moduleinstance/showEditText.do" />

  	var szQuery = "<%=showText%>?langCode=" + lang;
		ret = window.confirm("Switching language will refresh \n page with content on that language.");
		if (ret) {
			document.location.href = szQuery;
		}
	}
</script>

<!-- list of other languages -->
<logic:present name="editorForm" property="editorList">
	<BR>
	<table width="100%">
		<tr>
			<td>
				<hr>
			</td>
		</tr>
	</table>
	<!-- -->
	<table width="100%">
		<tr>
			<td noWrap align="center">
				<font color="darkblue">
					<b><digi:trn key="editor:availableLanguages">Available Languages for this Text</digi:trn></b>
				</font>
			</td>
		</tr>
		<tr>
			<td align="center">&nbsp;</td>
		</tr>
	</table>
	<table width="100%">
		<tr>
			<td noWrap align="left">
				<small><b><digi:trn>Language</digi:trn></b></small>
			</td>
			<td noWrap align="left">
				<small><b><digi:trn>Last Modification Date</digi:trn></b></small>
			</td>
			<td noWrap align="left">
				<small><b><digi:trn> Last User</digi:trn></b></small>
			</td>
		</tr>
		<logic:iterate id="editorList" name="editorForm" property="editorList"
			type="org.digijava.module.editor.form.EditorForm.TextInfo">
			<tr align="left">
				<td><small> <bean:write name="editorList"
							property="langName" /></small> 
					<digi:link href="/showEditText.do" paramName="editorList" paramId="langCode" paramProperty="langCode">[<small>Edit</small>]</digi:link>
				</td>
				<td align="left"><small> <bean:write name="editorList"
							property="lastModDate" /></small></td>
				<td align="left"><small> <bean:write name="editorList"
							property="userName" /></small></td>
			</tr>
		</logic:iterate>
	</table>
</logic:present>

<script type="text/javascript">
	var config = {
        baseHref: '/ckeditor_4.4.6/',
        customConfig: '/ckeditor_4.4.6/config_admin_editor.js',
        width: 800,
        height: 400
    }
    var editor = $("[name='content']").ckeditor(config);
</script>