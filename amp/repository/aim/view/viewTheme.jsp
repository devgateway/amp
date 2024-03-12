<%@ page pageEncoding="UTF-8" %>
<%@ taglib uri="/src/main/webapp/WEB-INF/tld/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/src/main/webapp/WEB-INF/tld/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/src/main/webapp/WEB-INF/tld/struts-tiles.tld" prefix="tiles" %>
<%@ taglib uri="/src/main/webapp/WEB-INF/tld/struts-html.tld" prefix="html" %>
<%@ taglib uri="/src/main/webapp/WEB-INF/tld/digijava.tld" prefix="digi" %>
<%@ taglib uri="/src/main/webapp/WEB-INF/tld/c.tld" prefix="c" %>

<digi:instance property="aimAddThemeForm" />
<digi:context name="digiContext" property="context" />

<c:set var="translation">
	<digi:trn key="aim:clickToViewAdmin">Click here to goto Admin Home</digi:trn>
</c:set>
<digi:link href="/admin.do" title="${translation}" >
	Admin Home Page
</digi:link><br>
<table width="100%" border="0" cellspacing="8">
	<tr>
		<td bgcolor="#006699" colspan="2">
			<b><font color="#FFFFFF">
				<digi:trn key="aim:themeDetails">Theme Details</digi:trn>					
			</font></b>
		</td>
	</tr>
	<tr>
		<td width="30%" align="right">
			<b><digi:trn key="aim:themeName">Theme Name</digi:trn></b>
		</td>
		<td width="30%" align="left">
			<bean:write name="aimAddThemeForm" property="themeName" />
		</td>
	</tr>
	<tr>
		<td width="30%" align="right">
			<b><digi:trn key="aim:themeCode">Theme Code</digi:trn></b>
		</td>
		<td width="30%" align="left">
			<bean:write name="aimAddThemeForm" property="themeCode" />
		</td>
	</tr>
	<tr>
		<td width="30%" align="right">
			<b><digi:trn key="aim:themeDescription">Description</digi:trn></b>
		</td>
		<td width="30%" align="left">
			<bean:write name="aimAddThemeForm" property="description" />
		</td>
	</tr>
	<tr>
		<td width="30%" align="right">
			<b><digi:trn key="aim:themeType">Type</digi:trn></b>
		</td>
		<td width="30%" align="left">
			<bean:write name="aimAddThemeForm" property="type" />
		</td>
	</tr>	
	<tr>
		<td width="100%" align="center" colspan="2">
			<table width="90%">	
				<tr>
					<td width="30%" align="center">
						<jsp:useBean id="urlParams" type="java.util.Map" class="java.util.HashMap"/>
						<c:set target="${urlParams}" property="id">
							<bean:write name="aimAddThemeForm" property="themeId" />
						</c:set>
						<c:set target="${urlParams}" property="action" value="edit" />
						<c:set var="translation">
							<digi:trn key="aim:clickToEditCurrentTheme">Click here to Edit Current Theme</digi:trn>
						</c:set>
						<digi:link href="/getTheme.do" name="urlParams" title="${translation}" >
							Edit this theme
						</digi:link>
					</td>
					<td width="30%" align="center">
						<c:set target="${urlParams}" property="action" value="delete" />
						<c:set var="translation">
							<digi:trn key="aim:clickToDeleteCurrentTheme">Click here to Delete Current Theme</digi:trn>
						</c:set>
						<digi:link href="/getTheme.do" name="urlParams" title="${translation}" >
							Delete this theme
						</digi:link>					
					</td>
					<td width="30%" align="center">
						<c:set var="translation">
							<digi:trn key="aim:clickToGoBackToThemeManager">Click here to go back to Theme Manager</digi:trn>
						</c:set>
						<digi:link href="/themeManager.do" title="${translation}" >Back to Theme Manager</digi:link>
					</td>
			</table>
		</td>
	</tr>
</table>




