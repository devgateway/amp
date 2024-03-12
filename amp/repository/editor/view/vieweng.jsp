
<%@ page pageEncoding="UTF-8" %>
<%@ taglib uri="/src/main/webapp/WEB-INF/tld/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/src/main/webapp/WEB-INF/tld/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/src/main/webapp/WEB-INF/tld/struts-tiles.tld" prefix="tiles" %>
<%@ taglib uri="/src/main/webapp/WEB-INF/tld/struts-html.tld" prefix="html" %>
<%@ taglib uri="/src/main/webapp/WEB-INF/tld/digijava.tld" prefix="digi" %>
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