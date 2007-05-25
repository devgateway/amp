<%@ taglib uri="/taglib/struts-bean" prefix="bean" %>
<%@ taglib uri="/taglib/struts-logic" prefix="logic" %>
<%@ taglib uri="/taglib/struts-tiles" prefix="tiles" %>
<%@ taglib uri="/taglib/struts-html" prefix="html" %>
<%@ taglib uri="/taglib/digijava" prefix="digi" %>

<%@ page import="org.digijava.module.editor.dbentity.Editor" %>
<%@ page import="org.digijava.module.editor.util.Constants" %>


<digi:instance property="editorAdminForm" />

<digi:errors property="editor"/>

<script language="JavaScript">
	function addEditor() {
      <digi:context name="addEditor" property="context/module/moduleinstance/addEditor.do" />
      document.editorAdminForm.action = "<%= addEditor %>";
      document.editorAdminForm.submit();	
	}
</script>

<digi:form action="/admin.do?update=true">
	<table border="1" width="100%">
	<tr>
		<td>Editor key</td>
		<td>Language</td>
		<td colspan="4">&nbsp;</td>		
	</tr>
	<logic:present name="editorAdminForm" property="siteEditors">
	
		<bean:define id="editorCollection" name="editorAdminForm" property="siteEditors" type="java.util.Collection"/>
	
		<logic:iterate indexId="index" id="editor" name="editorCollection" type="Editor">
			<tr>
				<td width="70%">
					<digi:context name="indexUrl" property="context" />
					<a href="<%=indexUrl%>/editor/showEditText.do?langCode=<bean:write name="editor" property="language"/>&id=<bean:write name="editor" property="editorKey"/>&referrer=<bean:write name="editorAdminForm" property="refUrl"/>">
						<bean:write name="editor" property="editorKey"/>
					</a>
				</td>
				<td width="20%">
					<bean:write name="editor" property="language"/>
				</td>
				<td width="25" align="center">
					<html:multibox name="editorAdminForm" property="multiboxList" value="<%= editor.getEditorKey() %>"/>
				</td>
				<td width="25" align="center">
					<logic:lessThan name="index" value="<%= String.valueOf(editorCollection.size()-1)%>">
						<digi:link href="<%= "/moveEditor.do?key=" + editor.getEditorKey() + "&lang=" + editor.getLanguage() + "&moveDir=" + Constants.MOVE_DOWN%>">
							&#9660;
						</digi:link>
					</logic:lessThan>
					<logic:equal name="index" value="<%= String.valueOf(editorCollection.size()-1)%>">
							&#9660;
					</logic:equal>					
				</td>
				<td width="25" align="center">
					<logic:greaterThan name="editor" property="orderIndex" value="1">				
						<digi:link href="<%= "/moveEditor.do?key=" + editor.getEditorKey() + "&lang=" + editor.getLanguage() + "&moveDir=" + Constants.MOVE_UP%>">
							&#9650;
						</digi:link>
					</logic:greaterThan>
					<logic:lessEqual name="editor" property="orderIndex" value="1">
						&#9650;
					</logic:lessEqual>					
				</td>
				<td>
				<digi:link href="<%= "/deleteEditor.do?key=" + editor.getEditorKey() + "&lang=" + editor.getLanguage()%>">
					Delete
				</digi:link>
					
				</td>
			</tr>
		</logic:iterate>
	
	</logic:present>
	<tr>
		<td colspan="7" align="center">
			<table border="0" cellpadding="0" cellspacing="3">
				<tr>
					<td width="100%" nowrap>
						Add new editor:
					</td>
					<td nowrap>
					Key <html:text name="editorAdminForm" property="key"/>
					</td>
					<td nowrap>
					Default value <html:text name="editorAdminForm" property="value"/>
					</td>					
					<td>
						<input type="Button" value="Add" onclick="addEditor()"/>
					</td>
				</tr>
			</table>
		</td>
	</tr>	
	<tr>
		<td colspan="5" align="center">
			<html:submit value="Update"/>
		</td>
	</tr>
	</table>
</digi:form>
