<%@ page contentType="text/html; charset=UTF-8" %>

<%@ taglib uri="/taglib/struts-tiles" prefix="tiles" %>

<%@ taglib uri="/taglib/struts-html" prefix="html" %>

<%@ taglib uri="/taglib/struts-logic" prefix="logic" %>

<%@ taglib uri="/taglib/struts-bean" prefix="bean" %>

<%@ taglib uri="/taglib/digijava" prefix="digi" %>
<%@ taglib uri="/taglib/jstl-core" prefix="c" %>


<HTML>

	<digi:base />

	<digi:ref href="css/styles.css" type="text/css" rel="stylesheet" />
	<script type="text/javascript" src="<digi:file src="script/jquery.js"/>"></script>
	
	<digi:context name="digiContext" property="context"/>



	<HEAD>


		<%
			String title=(String)((org.apache.struts.tiles.ComponentContext) request.getAttribute("org.apache.struts.taglib.tiles.CompContext")).getAttribute("title");
			String key=(title.replaceAll(" ",""));
			%>
		<logic:present name="extraTitle" scope="request">

			<bean:define id="extTitle" name="extraTitle" scope="request" type="java.lang.String" />

		<TITLE>
			
				<c:set var="key">aim:pagetitle:<%=key%><%=extTitle%></c:set>
				<digi:trn key="aim:pagetitle:amp">AMP </digi:trn> 
				<digi:trn key="${key}">
					<%=title%> <%=extTitle%>
				</digi:trn>
		</TITLE>
		</logic:present>

		<logic:notPresent name="extraTitle" scope="request">
		<TITLE>
				<c:set var="key">aim:pagetitle:<%=key%></c:set>
				<digi:trn key="aim:pagetitle:amp">AMP </digi:trn> 
				<digi:trn key="${key}">
					<%=title%>
				</digi:trn>
		</TITLE>
		</logic:notPresent>



		<META HTTP-EQUIV="CACHE-CONTROL" CONTENT="NO-CACHE">

		<META HTTP-EQUIV="Content-Type" CONTENT="text/html; charset=UTF-8">

		<META HTTP-EQUIV="PRAGMA" CONTENT="NO-CACHE">

     	<META HTTP-EQUIV="EXPIRES" CONTENT="0">



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

				<TD width="100%" bgColor=#323232 vAlign="center" align="left">

					<digi:insert attribute="headerTop" />

				</TD>

			</TR>

			<TR>

				<TD align="left" vAlign="top" bgcolor="#5a5a58" width="100%">

					<TABLE cellSpacing=0 cellPadding=0 width="98%"

					vAlign="center">

						<TBODY>

						  	<TR bgColor=#5a5a58>

						   	<TD align="left" vAlign="center">

									<digi:insert attribute="headerMiddle" />

								</TD>
							 	<TD align="right" vAlign="top">
										<digi:insert attribute="loginWidget" />
								</TD>

							  	<TD width="20" align="right" vAlign="top">

										<digi:insert attribute="dropdownLangSwitch" />

								</TD>

							</TR>

						</TBODY>

					</TABLE>

				</TD>

			</TR>

			<TR>

				<TD width="100%">

					<TABLE cellSpacing=0 cellPadding=0 width="100%" border=0 valign="top" align="left">

						<TR>

							<TD width="100%" align="left" valign="top">

				   			<digi:insert attribute="body" />

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

