
<%@ page pageEncoding="UTF-8" %>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean" %>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic" %>
<%@ taglib uri="http://struts.apache.org/tags-tiles" prefix="tiles" %>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html" %>
<%@ taglib uri="http://digijava.org" prefix="digi" %>
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