<%@ page pageEncoding="UTF-8" %>

<%@ taglib uri="/taglib/struts-bean" prefix="bean" %>

<%@ taglib uri="/taglib/struts-logic" prefix="logic" %>

<%@ taglib uri="/taglib/struts-tiles" prefix="tiles" %>

<%@ taglib uri="/taglib/struts-html" prefix="html" %>

<%@ taglib uri="/taglib/digijava" prefix="digi" %>
<%@ taglib uri="/taglib/tags-fckeditor" prefix="FCK" %>
<%@ taglib uri="/taglib/jstl-core" prefix="c" %>


<script>

<%

  String contextPath = request.getContextPath();

  String _editor_url = new String();



  if (contextPath.length()==0 || contextPath.startsWith("/")){

   _editor_url = contextPath + "/repository/editor/view/";

  } else {

   _editor_url = "/"+contextPath + "/repository/editor/view/";

  }

%>



  _editor_url = "<%=_editor_url%>";

</script>



	<digi:form action="/saveText.do" method="post">

	<html:hidden name="editorForm" property="key"/>

	<html:hidden name="editorForm" property="lang"/>

	<html:hidden name="editorForm" property="returnUrl"/>

	<table width="80%">

		<tr>

			<td noWrap width="7%" align="right"><B style="COLOR: #004080"><digi:trn key="editor:title">Title</digi:trn>:</B>

			</td>

			<td width="93%">

				<html:text name="editorForm" property="title" size="70" style="COLOR: #004080"/>

			</td>

		</tr>

	</table><BR>
            <FCK:editor id="content"   basePath="/FCKeditor/"
            width="80%"
            height="500" toolbarSet="AMP" defaultLanguage="${editorForm.lang}">
          ${editorForm.content}
           </FCK:editor>


	<b><span style="color: #cc4000">

	<bean:write name="editorForm" property="key"/></span></b>&nbsp;
	
	<c:set var="trn"><digi:trn key="editor:savebutton">Save now</digi:trn> </c:set>
	<html:submit value="${trn}" style="BACKGROUND: #f1efed url('images/editor/grad-btn.gif') repeat-x;cursor: hand"/>&nbsp;
	<!-- 
	<html:select property="lang" onchange="ChangeLanguage(this)">

	<bean:define id="lid" name="editorForm" property="languages" type="java.util.Collection"/>

	<html:options collection="lid" property="code" labelProperty="name"/></html:select>
	 -->
	
			<html:select property="lang" onchange="ChangeLanguage(this)">
				<c:forEach var="lng" items="${editorForm.languages}">
					<c:set var="trn">
						<digi:trn key="editor:sourceLanguage:${lng.name}">${lng.name}</digi:trn>
					</c:set>
					<html:option value="${lng.code}">${trn}</html:option>
				</c:forEach>
			</html:select>
	</digi:form>



<script>function ChangeLanguage (obj) {

  var lang = obj.value;

  <digi:context name="showText" property="context/module/moduleinstance/showEditText.do" />

  var szQuery = "<%= showText %>?langCode="+lang;

  ret = window.confirm ("Switching language will refresh \n page with content on that language.");



  if (ret) {document.location.href = szQuery;}

}

</script><!-- list of other languages -->

	<logic:present name="editorForm" property="editorList"><BR>

	<table width="100%">

		<tr>

			<td>

				<hr>

			</td>

		</tr>

	</table>	<!-- -->

	<table width="100%">

		<tr>

			<td noWrap align="center"><font color="darkblue"><b><digi:trn key="editor:availableLanguages">Available Languages for this Text</digi:trn></b></font>

			</td>

		</tr>

		<tr>

			<td align="center">	&nbsp;

			</td>

		</tr>

	</table>

	<table width="100%">

		<tr>

			<td noWrap align="left"><small><b><digi:trn key="editor:language">Language</digi:trn></b></small>

			</td>

			<td noWrap align="left"><small><b><digi:trn key="editor:lastModificationDate">Last Modification Date</digi:trn></b></small>

			</td>

			<td noWrap align="left"><small><b><digi:trn key="editor:lastUser"> Last User</digi:trn></b></small>

			</td>

		</tr>

		<logic:iterate id="editorList" name="editorForm" property="editorList" type="org.digijava.module.editor.form.EditorForm.TextInfo">

		<tr align="left">

			<td><small>

				<bean:write name="editorList" property="langName"/></small>

				<digi:link href="/showEditText.do" paramName="editorList" paramId="langCode" paramProperty="langCode">[<small>Edit</small>]</digi:link>

			</td>

			<td align="left"><small>

				<bean:write name="editorList" property="lastModDate"/></small>

			</td>

			<td align="left"><small>

				<bean:write name="editorList" property="userName"/></small>

			</td>

		</tr></logic:iterate>

	</table></logic:present>
