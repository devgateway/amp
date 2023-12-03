<%@ taglib uri="/taglib/struts-bean" prefix="bean" %>
<%@ taglib uri="/taglib/struts-logic" prefix="logic" %>
<%@ taglib uri="/taglib/struts-tiles" prefix="tiles" %>
<%@ taglib uri="/taglib/struts-html" prefix="html" %>
<%@ taglib uri="/taglib/digijava" prefix="digi" %>
<%@ page import="org.digijava.ampModule.editor.dbentity.Editor" %>



<digi:instance property="editorForm" />

<digi:errors property="editorNavigationBar"/>
	<logic:present name="editorForm" property="navBarEditorList">
	<digi:context name="digiContext" property="context" />	
	<table border="1">
		<logic:iterate id="editor" name="editorForm" property="navBarEditorList" type="Editor">
			<tr>
				<td>
					&nbsp;
						<digi:link href="<%= "/showPage.do?key=" + editor.getEditorKey() %>">
							<logic:present name="editor" property="title">
								<digi:trn key="<%= "editor:" + editor.getTitle() %>">
									<bean:write name="editor" property="title"/>
								</digi:trn>
							</logic:present>
							<logic:notPresent name="editor" property="title">
								<digi:trn key="<%= "editor:" + editor.getEditorKey() %>">
									<bean:write name="editor" property="title"/>
								</digi:trn>
							</logic:notPresent>
							
						</digi:link>
						
					&nbsp;
				</td>
			</tr>
		</logic:iterate>
	</table>
</logic:present>