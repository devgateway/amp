<%@ taglib uri="/src/main/resources/tld/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/src/main/resources/tld/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/src/main/resources/tld/struts-tiles.tld" prefix="tiles" %>
<%@ taglib uri="/src/main/resources/tld/struts-html.tld" prefix="html" %>
<%@ taglib uri="/src/main/resources/tld/digijava.tld" prefix="digi" %>

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
