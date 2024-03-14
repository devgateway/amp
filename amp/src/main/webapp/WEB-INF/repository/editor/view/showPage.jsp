<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean" %>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic" %>
<%@ taglib uri="http://struts.apache.org/tags-tiles" prefix="tiles" %>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html" %>
<%@ taglib uri="http://digijava.org/digi" prefix="digi" %>

<%@ page import="org.digijava.module.editor.dbentity.Editor" %>
<%@ page import="org.digijava.module.editor.form.EditorForm" %>
<digi:instance property="editorForm" />

<digi:errors property="editor"/>
	<logic:present name="editorForm" property="key">
	<table border="0" width="100%">
		<tr>
			<td>
				<bean:define id="editor" name="editorForm" property="editor" type="Editor"/>
				<digi:edit key="<%= editor.getEditorKey() %>"><bean:write name="editorForm" property="title" filter="false"/></digi:edit>
			</td>
		</tr>
	</table>
	</logic:present>
