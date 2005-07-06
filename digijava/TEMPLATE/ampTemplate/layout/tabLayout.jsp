<%@ page contentType="text/html; charset=UTF-8" %> 
<%@ taglib uri="/taglib/struts-tiles" prefix="tiles" %>
<%@ taglib uri="/taglib/struts-html" prefix="html" %>
<%@ taglib uri="/taglib/digijava" prefix="digi" %>

<HTML>
	<digi:base />
	<digi:ref href="css/styles.css" type="text/css" rel="stylesheet" />
	<digi:context name="digiContext" property="context"/>

	<HEAD>
		<TITLE>AMP - <tiles:getAsString name="title"/></TITLE>
		<META http-equiv=Content-Type content="text/html; charset=utf-8">
		<META HTTP-EQUIV="expires" CONTENT="Wed, 09 Aug 2000 08:21:57 GMT">
		<META HTTP-EQUIV="Cache-Control" CONTENT="no-cache">
		<SCRIPT type="text/javascript">
			function formSubmit()	{
				document.forms[0].submit();
			}
		</SCRIPT>
	</HEAD>

	<BODY leftmargin="0" topmargin="0" rightmargin="0" bottommargin="0">
		<TABLE cellSpacing=0 cellPadding=0 width="100%" border=0 valign="top" align="left">
			<TBODY>
			<TR height="40">
				<TD width="100%" bgColor=#323232 vAlign="center" align="left" height="40">
					<digi:insert attribute="headerTop" />
				</TD>
			</TR>
			<TR height="27">
				<TD width="100%" align="center" vAlign="top" bgcolor="#5a5a58">
					<TABLE cellSpacing=0 cellPadding=0 width="98%" border=0 vAlign="top" align="top">
						<TBODY>
						  	<TR bgColor=#5a5a58 height="28">
						   	<TD align="left" vAlign="center" height="28">
									<digi:insert attribute="headerMiddle" />
								</TD>	
							  	<TD align="right" vAlign="top" height="28">
										<digi:insert attribute="dropdownLangSwitch" />
								</TD>
							</TR>
						</TBODY>
					</TABLE>
				</TD>
			</TR>
			<TR>
				<TD width="100%" vAlign="top" align="left">
					<jsp:include page="../../../repository/aim/view/teamPagesHeader.jsp" flush="true" />				
				</TD>
			</TR>
			<TR>
				<TD width="100%" vAlign="top" align="left">
					<TABLE bgColor=#ffffff cellPadding=0 cellSpacing=0 width="785" vAlign="top" align="left" border=0>
						<TR>
							<TD class=r-dotted-lg width="10">&nbsp;</td>
							<TD align=center vAlign=top class=r-dotted-lg width="775">
								<TABLE width="775" cellPadding=0 cellSpacing=0 vAlign="top" align="left" border="0">
									<TR><TD vAlign="bottom" align="center" width="775">
										<digi:insert attribute="tabHeader" />
									</TD></TR>
									<TR><TD vAlign="top" align="center" width="775">
										<digi:insert attribute="tabBody" />										
									</TD></TR>
								</TABLE>
							</TD>
						</TR>
					</TABLE>
				</TD>
			</TR>
			<TR>
				<TD width="100%"  bgcolor="#323232">
				   <digi:insert attribute="footer" />
				</TD>
			</TR>
			</TBODY>
		</TABLE>
	</BODY>
</HTML>
