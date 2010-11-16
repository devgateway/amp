<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib uri="/taglib/struts-tiles" prefix="tiles" %>
<%@ taglib uri="/taglib/struts-html" prefix="html" %>
<%@ taglib uri="/taglib/digijava" prefix="digi" %>
<%@ taglib uri="/taglib/jstl-core" prefix="c" %>
<%@page import="org.digijava.module.aim.util.FeaturesUtil"%>
<%@page import="org.digijava.module.aim.helper.GlobalSettingsConstants"%>

<html>
	<digi:base />
	<digi:ref href="css/styles.css" type="text/css" rel="stylesheet" />
	<digi:context name="digiContext" property="context"/>
	<head>
		<tiltle>
				<%
				String tiltle=(String)((org.apache.struts.tiles.ComponentContext) request.getAttribute("org.apache.struts.taglib.tiles.CompContext")).getAttribute("tiltle");
				String key=(tiltle.replaceAll(" ",""));
				%>
				<c:set var="key">aim:pagetiltle:<%=key%></c:set>
					<digi:trn key="aim:pagetiltle:amp">AMP </digi:trn> 
					<digi:trn key="aim:pagetiltle:${key}">
						<%=tiltle%>
					</digi:trn>
			</tiltle>
		<script type="text/javascript">
			function formSubmit()	{
				document.forms[0].submit();
			}
		</script>
	</head>
	<body leftmargin="0" topmargin="0" rightmargin="0" bottommargin="0">
		<table cellSpacing=0 cellPadding=0 width="100%" border=0 valign="top" align="left">
			<tbody>
				<tr height="15">
					<td width="100%" bgColor=#323232 vAlign="center" align="left" height="20">
						<jsp:include page="headerTop_2.jsp"/>
					</td>
				</tr>
				<tr height="15">
					<td width="100%" align="center" bgcolor="#376091">
						<table cellSpacing=0 cellPadding=0 width="98%">
							<tbody>
								<tr>
									<td align="left" vAlign="center">
										<digi:insert attribute="headerMiddle" />
									</td>
									<td align="right" vAlign="top" height="15">
										<digi:insert attribute="loginWidget" />
									</td>
								</tr>
							</tbody>
						</table>
					</td>
				</tr>
				<tr>
					<td width="100%" vAlign="top" align="left">
						<jsp:include page="../../../repository/aim/view/teamPagesHeader.jsp" flush="true" />
					</td>
				</tr>
				<tr>
					<td width="100%" vAlign="top" align="left">
						<table bgColor=#ffffff cellPadding=0 cellSpacing=0 width="770" vAlign="top" align="left" border=0>
							<tr>
								<td width="10">&nbsp;</td>
								<td align=center vAlign=top width="900">
									<table width="900" cellPadding=0 cellSpacing=0 vAlign="top" align="left" border="0">
										<tr>
											<td vAlign="bottom" align="center" width="900">
												<digi:insert attribute="tabHeader" />
											</td>
										</tr>
										<tr>
											<td vAlign="top" align="center" width="900">
												<digi:insert attribute="tabBody" />
											</td>
										</tr>
									</table>
								</td>
							</tr>
						</table>
					</td>
				</tr>
			<tr>
				<td width="100%"  bgcolor="#323232">
					 <digi:insert attribute="footer" />
				</td>
			</tr>
		</tbody>
	</table>
	</body>

</html>

