<%@ page contentType="text/html; charset=UTF-8" %> 
<%@ taglib uri="/taglib/struts-tiles" prefix="tiles" %>
<%@ taglib uri="/taglib/struts-html" prefix="html" %>
<%@ taglib uri="/taglib/digijava" prefix="digi" %>

<HTML>
	<digi:base />
	<digi:ref href="css/styles.css" type="text/css" rel="stylesheet" />
	<digi:context name="digiContext" property="context"/>

	<HEAD>
		<TITLE><tiles:getAsString name="title"/></TITLE>
		<META HTTP-EQUIV="Content-Type" CONTENT="text/html; charset=UTF-8">
		<META HTTP-EQUIV="Expires" CONTENT="0">
		<META HTTP-EQUIV="Cache-Control" CONTENT="private">
	</HEAD>

	<BODY leftmargin="0" topmargin="0" rightmargin="0" bottommargin="0">
		<TABLE cellSpacing=0 cellPadding=0 width="100%">
			<TBODY>
			<TR height="15" valign="center">
				<TD width="100%" bgColor=#323232 vAlign="center" align="left" height="15">
					<digi:insert attribute="headerTop" />
				</TD>
			</TR>
			<TR height="15">
				<TD width="100%" align="center" vAlign="top" bgcolor="#5a5a58">
					<TABLE cellSpacing=0 cellPadding=0 width="98%" border=0 vAlign="top" align="top">
						<TBODY>
						  	<TR bgColor=#5a5a58 height="15">
						   	<TD align="left" vAlign="center" height="15">
									<digi:insert attribute="headerMiddle" />
								</TD>	
							  	<TD align="right" vAlign="top" height="15">
										<digi:insert attribute="dropdownLangSwitch" />
								</TD>
							</TR>
						</TBODY>
					</TABLE>
				</TD>
			</TR>
			<TR>
				<TD>
				   <digi:insert attribute="logout" />
				</TD>
			</TR>
			<TR>
				<TD height="5%" width="100%"  bgcolor="#323232">
	      	<TABLE cellSpacing=0 cellPadding=0 width="100%" border=0 bgColor=#484846>
					<TR>
						<TD width=1 align="right"><digi:img src="images/feedback.gif"/></TD>
						<TD align=left vAlign="center" class="feedbk">
							<digi:trn key="aim:feedBackOrContact">
								FEEDBACK / CONTACT
							</digi:trn>
						</TD>
          			<TD align=right>
							<FONT color="#FFFFFF">
							<digi:trn key="aim:ampDevelepmentCredits">
								Developed in partnership with OECD, UNDP, WB and DGF
							</digi:trn>
							</FONT>
							&nbsp;&nbsp;&nbsp;
					 	</TD>
					</TR>
				</TABLE>
				</TD>
			</TR>
		</TABLE>
	</BODY>
</HTML>

