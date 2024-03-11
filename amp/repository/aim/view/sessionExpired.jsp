<%@ page pageEncoding="UTF-8" %>
<%@ taglib uri="/src/main/resources/tld/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/src/main/resources/tld/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="http://struts.apache.org/tags-tiles" prefix="tiles" %>
<%@ taglib uri="/src/main/resources/tld/struts-html.tld" prefix="html" %>
<%@ taglib uri="/src/main/resources/tld/digijava.tld" prefix="digi" %>

<table bgColor=#ffffff border="0" cellpadding="0" cellspacing="0" width=772>
	<tr>
		
		<td align=left class=r-dotted-lg valign="top" width=520><br>
			<table border="0" cellPadding=5 cellspacing="0" width="100%">
				<tr>
					<td width="3%">&nbsp;</td>				
					<td align="center" class=f-names noWrap width="31%">
							<digi:trn key="aim:sessionExpired">
							Your user session has expired !
							</digi:trn>
					</td>
				</tr>
				<tr>
					<td>&nbsp;</td>				
					<td align="center">
					<c:set var="translation">
						<digi:trn key="aim:clickToGoBackToHomePageAndRe-Login">Click here to go back to the Home Page and re-login</digi:trn>
					</c:set>
					<digi:link href="/" title="${translation}" >
					<digi:trn key="aim:backToHomePage">
					Click here to go back to the home page and re-login.
					</digi:trn>
					</digi:link>
					</td>
				</tr>
				<tr>
					<td>&nbsp;</td>				
					<td>&nbsp;</td>
				</tr>				
				<tr>
					<td>&nbsp;</td>				
					<td>&nbsp;</td>
				</tr>				
				<tr>
					<td>&nbsp;</td>				
					<td>&nbsp;</td>
				</tr>				
				<tr>
					<td>&nbsp;</td>				
					<td>&nbsp;</td>
				</tr>				
				<tr>
					<td>&nbsp;</td>				
					<td>&nbsp;</td>
				</tr>				
				<tr>
					<td>&nbsp;</td>				
					<td>&nbsp;</td>
				</tr>				
				<tr>
					<td>&nbsp;</td>				
					<td>&nbsp;</td>
				</tr>				
				
			</table>
		</td>
	</tr>
</table>



