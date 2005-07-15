<%@ page pageEncoding="UTF-8" %>
<%@ taglib uri="/taglib/struts-bean" prefix="bean" %>
<%@ taglib uri="/taglib/struts-logic" prefix="logic" %>
<%@ taglib uri="/taglib/struts-tiles" prefix="tiles" %>
<%@ taglib uri="/taglib/struts-html" prefix="html" %>
<%@ taglib uri="/taglib/digijava" prefix="digi" %>
<%@ taglib uri="/taglib/jstl-core" prefix="c" %>


<script language="JavaScript" type="text/javascript" src="<digi:file src="module/aim/scripts/relatedLinks.js"/>"></script>

<script language="JavaScript">
<!--

function editLinks(id,nm,ln) {

	window.name = "opener" + new Date().getTime();		  
	var t = ((screen.width)-420)/2;
	var l = ((screen.height)-110)/2;
	
	editLinksWindow = window.open("","",'resizable=no,width=420,height=110,top='+l+',left='+t);
	editLinksWindow.document.open();
	editLinksWindow.document.write(getEditLinksWindowString(id,nm,ln));
	editLinksWindow.document.close();
}

-->
</script>


<digi:instance property="aimMyDesktopForm" />
<digi:form action="/deleteLinks.do" method="post">
<html:hidden property="teamMemberId" />
<table width="100%" cellPadding="0" cellSpacing="0" vAlign="top" align="left" border=0>
<tr><td width="100%" vAlign="top" align="left">
<!--  AMP Admin Logo -->
<jsp:include page="teamPagesHeader.jsp" flush="true" />
<!-- End of Logo -->
</td></tr>
<tr><td width="100%" vAlign="top" align="left">
<table bgColor=#ffffff cellPadding=0 cellSpacing=0 width="100%" vAlign="top" align="center" border=0>
	<tr>
		<td class=r-dotted-lg width="10">&nbsp;</td>
		<td align=left vAlign=top class=r-dotted-lg>
			<table width="98%" cellSpacing="3" cellPadding="1" vAlign="top" align="left">
				<tr><td>
					<table width="100%" cellSpacing="1" cellPadding="1" vAlign="top">
						<tr>
							<td>
								<span class=crumb>
								<bean:define id="translation">
									<digi:trn key="aim:clickToViewMyDesktop">Click here to view MyDesktop </digi:trn>
								</bean:define>
								<digi:link href="/viewMyDesktop.do" styleClass="comment"  title="<%=translation%>">
									<digi:trn key="aim:portfolio">Portfolio</digi:trn>
								</digi:link>&nbsp;&gt;&nbsp;								
								<digi:trn key="aim:relatedLinks">Related Links</digi:trn>
								</span>
							</td>
						</tr>
					</table>
				</td></tr>
				<tr> <td>
					<digi:errors/>
				</td></tr>
				<tr><td>
					<table width="700" cellPadding=4 cellSpacing=1 bgcolor="#aaaaaa">		
						<tr>
							<td align="center" class="textalb">
								<digi:trn key="aim:relatedLinks">Related Links</digi:trn>
							</td>
						</tr>
						<tr bgcolor="#ffffff">
							<td>	
								<logic:empty name="aimMyDesktopForm" property="documents">
									<b>No Links</b>
								</logic:empty>
								<logic:notEmpty name="aimMyDesktopForm" property="documents">
								<table width="100%" cellPadding=2 cellSpacing=0>
									<% int i = 0; %>
									<logic:iterate name="aimMyDesktopForm" property="documents" id="link">
									<% if ((i % 2) == 0) { %>
									<tr class="rowNormal">
									<% } else { %>
									<tr class="rowAlternate">
									<% } i++; %>
										<td width="3%" valign=top>
											<html:multibox property="selLinks">
												<c:out value="${link.docId}"/>
											</html:multibox>
										</td>
										<td>
											<table width="100%" cellPadding=2 cellSpacing=1>
												<tr><td><c:out value="${link.title}"/></td></tr>
												<tr><td>&nbsp;&nbsp;&nbsp;&nbsp;
													<a href="<c:out value="${link.url}"/>" target="_blank">
														<c:out value="${link.url}"/></a>
												</td></tr>
											</table>
										</td>
										<td width="50" align="center" valign=top>
											<a href="javascript:editLinks('<c:out value="${link.docId}"/>','<c:out value="${link.title}"/>','<c:out value="${link.url}"/>')">
											Edit</a>
										</td>
									</tr>
									</logic:iterate>
									<tr>
										<td colspan="3" align="center">
											<html:submit value="Remove Selected Links" styleClass="buton"/>
										</td>
									</tr>
								</table>
								</logic:notEmpty>
							</td>
						</tr>
					</table>
				</td></tr>
				<tr><td>&nbsp;</td></tr>
			</table>
		</td>
		<td width="10">&nbsp;</td>
	</tr>
</table>
</td></tr>
</table>
</digi:form>

