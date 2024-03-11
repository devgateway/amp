<%@ page pageEncoding="UTF-8" %>
<%@ taglib uri="/src/main/resources/tld/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/src/main/resources/tld/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/src/main/resources/tld/struts-tiles.tld" prefix="tiles" %>
<%@ taglib uri="/src/main/resources/tld/struts-html.tld" prefix="html" %>
<%@ taglib uri="/src/main/resources/tld/digijava.tld" prefix="digi" %>
<%@ taglib uri="/src/main/resources/tld/c.tld" prefix="c" %>


<table width="100%" cellpadding="0" cellspacing="0" border="0">
<tr>
	<td valign="top" align="left" width="100%">
		<jsp:include page="teamPagesHeader.jsp"  />
	<td>
</tr>
<tr>
	<td>
		<table bgColor=#ffffff cellpadding="0" cellspacing="0" width=772>
			<tr>
				<td class=r-dotted-lg width=14>&nbsp;</td>
				<td align=left class=r-dotted-lg valign="top" width=750>
					<table cellPadding=5 cellspacing="0" width="100%">
						<tr>
							<td height=33><span class=crumb>
								<c:set var="translation">
									<digi:trn key="aim:clickToViewMyDesktop">Click here to view MyDesktop</digi:trn>
								</c:set>
								<digi:link href="/viewMyDesktop.do" styleClass="comment" title="${translation}" >
								<digi:trn key="aim:portfolio">
								Portfolio
								</digi:trn>
								</digi:link>&nbsp;&gt;&nbsp;
								<c:set var="translation">
									<digi:trn key="aim:clickToViewAllDocuments">Click here to view All Documents</digi:trn>
								</c:set>
								<digi:link href="/viewAllDocuments.do" styleClass="comment" title="${translation}" >								
								<digi:trn key="aim:viewAllDocument">
									All Documents
								</digi:trn>
								</digi:link>&nbsp;&gt;&nbsp;								
								<digi:trn key="aim:viewDocument">
									View Document
								</digi:trn>
								</span>
							</td>
						</tr>
						<tr>
							<td height=16 valign="center" width=650><span class=subtitle-blue>
								<digi:trn key="aim:documentDetails">
									Document Details
								</digi:trn></span>
							</td>
						</tr>
						<tr>
							<td noWrap width=650 vAlign="top">
								<jsp:include page="viewDocumentDetails.jsp"  />
							</td>
						</tr>
						<tr><td>&nbsp;</td></tr>
					</table>
				</td>
			</tr>
		</table>
	</td>
</tr>
</table>



