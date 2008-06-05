
<%@ page pageEncoding="UTF-8" %>
<%@ taglib uri="/taglib/struts-bean" prefix="bean" %>
<%@ taglib uri="/taglib/struts-logic" prefix="logic" %>
<%@ taglib uri="/taglib/struts-tiles" prefix="tiles" %>
<%@ taglib uri="/taglib/struts-html" prefix="html" %>
<%@ taglib uri="/taglib/digijava" prefix="digi" %>
<digi:errors/>
<digi:instance property="editorForm"/>
<BR><BR>
	<table width="100%" height="100%">
		<tr>
			<td valign="top" align="left" height="93%" width="100%">
				<bean:define id="contentEng" name="editorForm" property="contentEng" type="java.lang.String"/>&nbsp;&nbsp;&nbsp;
				<%=contentEng%>
			</td>
		</tr>
		<tr bgcolor="lightgrey">
			<td align="center" valign="center" height="7%" width="100%">
			    <a href="javascript:window.close()" class="navtop4"><digi:trn key="um:closeThisWindow">Close this window</digi:trn></a>
			</td>
		</tr>
	</table>