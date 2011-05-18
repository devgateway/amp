<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib uri="/taglib/struts-tiles" prefix="tiles" %>
<%@ taglib uri="/taglib/struts-html" prefix="html" %>
<%@ taglib uri="/taglib/digijava" prefix="digi" %>
<%@ taglib uri="/taglib/struts-logic" prefix="logic" %>
<%@ taglib uri="/taglib/struts-bean" prefix="bean" %>
<%@ taglib uri="/taglib/jstl-core" prefix="c" %>

<HTML>
	<digi:base />
	<digi:ref href="css/styles.css" type="text/css" rel="stylesheet" />
	<digi:context name="digiContext" property="context"/>

	<HEAD>
		<TITLE>
			<%
			String title=(String)((org.apache.struts.tiles.ComponentContext) request.getAttribute("org.apache.struts.taglib.tiles.CompContext")).getAttribute("title");
			String key=(title.replaceAll(" ",""));
			%>
				<c:set var="key">aim:pagetitle:<%=key%></c:set>
				<digi:trn key="aim:pagetitle:amp">AMP </digi:trn> 
				<digi:trn key="${key}">
					<%=title%>
				</digi:trn>
		</TITLE>
		<META HTTP-EQUIV="Content-Type" CONTENT="text/html; charset=UTF-8">
		<META HTTP-EQUIV="Expires" CONTENT="0">
		<META HTTP-EQUIV="Cache-Control" CONTENT="private">
		<script language="javascript">
			function quitRnot1(message)
		{

		}
		</script>

	</HEAD>

	<BODY leftmargin="0" topmargin="0" rightmargin="0" bottommargin="0">
		<TABLE cellSpacing=0 cellPadding=0 width="100%" border=0 valign="top" align="left">
			<TBODY>
			<TR>
				<TD width="100%" bgColor=#F7F9E4 vAlign="center" align="left" height="15">
					<%--<digi:insert attribute="headerTop" />--%>
					<DIV id="head-top">
					<TABLE cellSpacing=0 cellPadding=0 width="100%" border=0
					vAlign="center">
						<TBODY>
						  	<TR bgColor=#F7F9E4>
						   	<TD align="left" vAlign="center" width="50%" nowrap>
									<digi:insert attribute="headerTop" />
								</TD>
								<TD align="left" vAlign="center" >
								<%--<digi:insert attribute="headerTopMiddle" />--%>
									<jsp:include page="../../../repository/aim/view/teamPagesHeader.jsp"  />
								</TD>
								<TD width="20" align="right" vAlign="center" bgColor=#195C79>
										<digi:insert attribute="dropdownLangSwitch" />
								</TD>

							</TR>
						</TBODY>
					</TABLE>
					</DIV>
				</TD>
			</TR>
			<TR>
				<TD width="100%" align="center" vAlign="top" bgcolor="#F7F9E4">
					<TABLE cellSpacing=0 cellPadding=0 width="98%" border=0 vAlign="center" bgcolor="#5a5a58">
						<TBODY>
						  	<TR bgColor=#F7F9E4 height="15">
						   	<TD align="left" vAlign="center" height="15">
									<digi:insert attribute="headerMiddle" />
								</TD>
							<td align="right" vAlign="top" height="15" nowrap="nowrap">
									<digi:insert attribute="loginWidget" />
							</td>

							</TR>
						</TBODY>
					</TABLE>
				</TD>
			</TR>
			<%--<TR>
				<TD width="100%" vAlign="top" align="left">
					<jsp:include page="../../../repository/aim/view/teamPagesHeader.jsp"  />
				</TD>
		  </TR>--%>
		  <TR>
				<TD width="100%" vAlign="top" align="left">
					<TABLE bgColor=#ffffff cellPadding=0 cellSpacing=0 width="99%" vAlign="top" align="left" border=0>
						<TR>
							<TD class=r-dotted-lg width="10">&nbsp;</td>
							<TD align=center vAlign=top>
								<TABLE width="100%" cellPadding=0 cellSpacing=0 vAlign="top" align="left" border="0">
									<TR>
										<TD vAlign="top" align="left" width="75%" class=r-dotted-lg>
											<digi:insert attribute="body" />
										</TD>
									</TR>
								</TABLE>
							</TD>
						</TR>
					</TABLE>
				</TD>
			</TR>
		  <tr>
				<TD width="100%"  bgcolor="#323232">
				   <digi:insert attribute="footer" />
				</TD>
			</TR>
			</TBODY>
		</TABLE>
	</BODY>
</HTML>
