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

		<%@include file="title.jsp"%>
		
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

		<TABLE cellspacing="0" cellpadding="0" width="100%" border="0" valign="top" align="left">

			<TBODY>

			<TR>

				<TD width="100%" bgColor=#323232 vAlign="center" align="left" height="15">

					<digi:insert attribute="headerTop" />

				</TD>

			</TR>

			<TR>

				<TD width="100%" vAlign="top" align="left">

					<TABLE bgColor=#ffffff cellpadding="0" cellspacing="0" width="99%" vAlign="top" align="left" border="0">

						<TR>

							<TD class=r-dotted-lg width="10">&nbsp;</td>

							<TD align="center" valign="top">

								<TABLE width="100%" cellpadding="0" cellspacing="0" vAlign="top" align="left" border="0">

									<TR>

										<TD vAlign="top" align="left" width="75%" class=r-dotted-lg>

											<digi:insert attribute="body" />

										</TD>

										<TD vAlign="top" align="left" width="25%" class=r-dotted-lg bgcolor="#f4f4f2">

											<TABLE width="100%" cellpadding="0" cellspacing="0" vAlign="top" align="left" border="0">

												<TR><TD vAlign="top" align="left">

													<digi:insert attribute="myReports" />

												</TD></TR>

											</TABLE>

										</TD>

									</TR>

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

