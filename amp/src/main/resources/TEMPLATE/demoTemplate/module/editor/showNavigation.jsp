<%@ taglib uri="/taglib/struts-bean" prefix="bean" %>
<%@ taglib uri="/taglib/struts-logic" prefix="logic" %>
<%@ taglib uri="/taglib/struts-tiles" prefix="tiles" %>
<%@ taglib uri="/taglib/struts-html" prefix="html" %>
<%@ taglib uri="/taglib/digijava" prefix="digi" %>
<%@ page import="org.digijava.module.editor.dbentity.Editor" %>



<digi:instance property="editorForm" />

<digi:errors property="editorNavigationBar"/>
	<logic:present name="editorForm" property="navBarEditorList">
	<digi:context name="digiContext" property="context" />	
			<logic:iterate id="editor" name="editorForm" property="navBarEditorList" type="Editor">

				<td nowrap>
					&nbsp;
						<digi:link styleClass="demo" href="<%= "/showPage.do?key=" + editor.getEditorKey() %>">
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
				<td width="1"><digi:img src="images/ui/header/navLinkSep.gif"/></td>				
			</logic:iterate>
</logic:present>