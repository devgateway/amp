<%@ page pageEncoding="UTF-8" %>
<%@ taglib uri="/taglib/struts-bean" prefix="bean" %>
<%@ taglib uri="/taglib/struts-logic" prefix="logic" %>
<%@ taglib uri="/taglib/struts-tiles" prefix="tiles" %>
<%@ taglib uri="/taglib/struts-html" prefix="html" %>
<%@ taglib uri="/taglib/digijava" prefix="digi" %>
<%@ taglib uri="/taglib/jstl-core" prefix="c" %>

<link href="css/global.css" rel="stylesheet" type="text/css">
<link href="/TEMPLATE/ampTemplate/css/styles.css" rel="stylesheet" type="text/css"></link>

<script language="JavaScript">
<!--

function load() {
}

function unload() {
}

-->
</script>

<digi:ref href="css/styles.css" type="text/css" rel="stylesheet" />
<digi:instance property="aimUserDetailForm" />

<digi:errors/>

<table width="100%" cellSpacing=5 cellPadding=5 vAlign="top" border=0>
	<tr><td vAlign="top">
		<table bgcolor=#f4f4f2 cellPadding=5 cellSpacing=5 width="100%" class=box-border-nopadding border=0 height="470">
			<tr>
				<td align=left vAlign=top>
					<table width="100%" cellSpacing="1" cellPadding="4" vAlign="top"
					bgcolor="#aaaaaa">
						<tr>
							<td colspan="2" class="textalb" align="center">
								<digi:trn key="aim:userProfile">User Profile </digi:trn>
							</td>
						</tr>
						<tr>
							<td class="box-title" bgcolor="#f4f4f2" width="150">
								<digi:trn key="aim:firstName" >First Name </digi:trn>
							</td>
							<td bgcolor="#f4f4f2">
								 <bean:write name="aimUserDetailForm" property="firstNames" />
							</td>
						</tr>
						<tr>
							<td bgcolor="#f4f4f2" class="box-title">
								<digi:trn key="aim:lastName" >	Last Name 	</digi:trn>
							</td>
							<td bgcolor="#f4f4f2">
								<bean:write name="aimUserDetailForm" property="lastName" />
							</td>
						</tr>
						<tr>
							<td bgcolor="#f4f4f2" class="box-title">
								<digi:trn key="aim:mailingAddress" >	Mailing Address		</digi:trn>
							</td>
							<td bgcolor="#f4f4f2">
								<bean:write name="aimUserDetailForm"
								property="mailingAddress" />
							</td>
						</tr>
						<tr>
							<td bgcolor="#f4f4f2" class="box-title">
								<digi:trn key="aim:address" >	Address		</digi:trn>
							</td>
							<td bgcolor="#f4f4f2">
								<bean:write name="aimUserDetailForm" property="address" />
							</td>
						</tr>
						<tr>
							<td bgcolor="#f4f4f2" class="box-title">
								<digi:trn key="aim:organizationName" >	Organization Name	</digi:trn>
							</td>
							<td bgcolor="#f4f4f2">
								<bean:write name="aimUserDetailForm" property="organizationName" />
							</td>
						</tr>
						<tr>
							<td bgcolor="#f4f4f2" colspan="2">
								<table width="97%" cellPadding=3 cellSpacing="1" bgcolor="#aaaaaa" align="center" border=0>
									<tr>
										<td align="center" bgcolor="#dddddd" colspan="2" class="box-title">
											<digi:trn key="aim:teamsAssociatedWith" ><b>Teams Associated with</b>	</digi:trn>
										</td>
									</tr>
									<% int i = 0; %>
									<c:forEach var="info" items="${aimUserDetailForm.info}">
									<tr>
										<td bgcolor="#f4f4f2" class="box-title" width="100">
											<% if ((i%2) == 0) { %>
												<digi:trn key="aim:teamName" >Team Name		</digi:trn>
											<% } else { %>
												<digi:trn key="aim:role" >	Role		</digi:trn>
											<% }
											   i++;
											%>
										</td>
										<td bgcolor="#f4f4f2">
											<c:out value="${info}"/>
										</td>
									</tr>
									</c:forEach>
								</table>
							</td>
						</tr>
					</table>
				</td>
			</tr>
			<tr align="center">
				<td>
					<input type="button" value="Close" class="dr-menu" onclick="hidePWorkspaceframe()">
				</td>
			</tr>
		</table>
	</td></tr>
</table>
