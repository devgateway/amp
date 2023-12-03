<%@ taglib uri="/taglib/struts-bean" prefix="bean" %>
<%@ taglib uri="/taglib/struts-logic" prefix="logic" %>
<%@ taglib uri="/taglib/struts-tiles" prefix="tiles" %>
<%@ taglib uri="/taglib/struts-html" prefix="html" %>
<%@ taglib uri="/taglib/digijava" prefix="digi" %>
<%@ page import="org.digijava.ampModule.editor.dbentity.Editor" %>



<digi:instance property="editorForm" />
<digi:ref href="css/demoUI.css" rel="stylesheet" type="text/css" />


<digi:errors property="editorNavigationBar"/>
	<logic:present name="editorForm" property="navBarEditorList">
	<digi:context name="digiContext" property="context" />

<script language="JavaScript">
	function navMouseOver(trObj) {
		trObj.className = "navBarMouseOver";
		trObj.cells[0].getElementsByTagName("img")[0].style.visibility="visible";
	}
	function navMouseOut(trObj) {
		trObj.className = "navBarDefault";
		trObj.cells[0].getElementsByTagName("img")[0].style.visibility="hidden";
	}
</script>
	
<table border="0" cellpadding="0" cellspacing="0">
	<tr>
		<td width="10" height="25"><digi:img src="images/ui/teaserTitleLeft.gif"/></td>
		<td width="100%" height="25" class="teaserTitleBody">
			&nbsp;Pages
		</td>
		<td width="5" height="25"><digi:img src="images/ui/teaserTitleRight.gif"/></td>
	</tr>
	<tr>
		<td width="10" height="25"><digi:img src="images/ui/teaserExtrainfoLeft.gif"/></td>
		<td width="100%" height="25" class="teaserExtraBody">
			&nbsp; Navigation bar
		</td>
		<td width="5" height="25"><digi:img src="images/ui/teaserExtrainfoRight.gif"/></td>
	</tr>
	<tr>
	<td class="bodyLeftTile" width="10"><digi:img src="images/ui/spacer.gif"/></td>
	<td class="bodyField" width="100%">	
	
	
		
	<table border="0" width="100%" cellpadding="2" cellspacing="0">
		<logic:iterate id="editor" name="editorForm" property="navBarEditorList" type="Editor">
			<tr onmouseover="navMouseOver(this)" onmouseout="navMouseOut(this)">
				<td width="1">
					<digi:img src="images/ui/navBarLeftArrow.gif" style="visibility:hidden"/>
				</td>
				<td width="99%">
					&nbsp;
						<digi:link href="<%= "/showPage.do?key=" + editor.getEditorKey() %>" styleClass="navBar">
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
	
	
</td>
	<td class="bodyRightTile" width="10"><digi:img src="images/ui/spacer.gif"/></td>
	</tr>
	
	<tr>
	<td width="10" height="11"><digi:img src="images/ui/teaserBottomLeft.gif"/></td>
	<td width="100%" height="11" class="teaserBottomBody"><digi:img src="images/ui/spacer.gif"/></td>
	<td width="5" height="11"><digi:img src="images/ui/teaserBottomRight.gif"/></td>
	</tr>	
</table>	
	
</logic:present>