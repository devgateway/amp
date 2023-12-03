<%@ taglib uri="/taglib/struts-bean" prefix="bean" %>
<%@ taglib uri="/taglib/struts-logic" prefix="logic" %>
<%@ taglib uri="/taglib/struts-tiles" prefix="tiles" %>
<%@ taglib uri="/taglib/struts-html" prefix="html" %>
<%@ taglib uri="/taglib/digijava" prefix="digi" %>

<%@ page import="org.digijava.ampModule.editor.dbentity.Editor" %>
<%@ page import="org.digijava.ampModule.editor.form.EditorForm" %>
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
