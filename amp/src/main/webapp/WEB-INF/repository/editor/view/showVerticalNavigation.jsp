<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean" %>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic" %>
<%@ taglib uri="http://struts.apache.org/tags-tiles" prefix="tiles" %>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html" %>
<%@ taglib uri="http://digijava.org/digi" prefix="digi" %>
<%@ page import="org.digijava.module.editor.dbentity.Editor" %>



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