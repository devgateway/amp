<%@ page pageEncoding="UTF-8"%>
<%@ taglib uri="/src/main/webapp/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/src/main/webapp/WEB-INF/struts-tiles.tld" prefix="tiles"%>
<%@ taglib uri="/src/main/webapp/WEB-INF/digijava.tld" prefix="digi"%>
<%@ taglib uri="/src/main/webapp/WEB-INF/c.tld" prefix="c"%>
<%@ taglib uri="/src/main/webapp/WEB-INF/fieldVisibility.tld" prefix="field"%>

<bean:define name="largeTextFeature" id="largeTextFeature"
	scope="request" />
<bean:define name="largeTextLabel" id="largeTextLabel" scope="request" />
<bean:define name="largeTextKey" id="largeTextKey" scope="request"/>


	<field:display name="${largeTextLabel}" feature="${largeTextFeature}">
		<tr bgcolor="#ffffff">
			<td valign="top" align="left"><a
				title="<digi:trn key="aim:${largeTextLabel}">${largeTextLabel}</digi:trn>">
			<digi:trn key="aim:${largeTextLabel}">${largeTextLabel}</digi:trn> </a>
			</td>
			<td valign="top" align="left">
			<table cellpadding="0" cellspacing="0">
				<tr>
					<td><digi:edit key="${largeTextKey}" /></td>
				</tr>
				<tr>
					<td><a href="javascript:edit('${largeTextKey}','${largeTextLabel}')"> <digi:trn
						key="aim:edit">Edit</digi:trn></a></td>
				</tr>
			</table>
			</td>
		</tr>
	</field:display>