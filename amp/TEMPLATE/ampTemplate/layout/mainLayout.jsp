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

    <!-- Stylesheet of AMP -->
    <digi:ref href="css/new_styles.css" type="text/css" rel="stylesheet" />

	<script type="text/javascript" src="<digi:file src="script/jquery.js"/>"></script>
	<script language="JavaScript" type="text/javascript" src="<digi:file src='module/aim/scripts/EnterHitBinder.js'/>" >.</script>
	
	<digi:context name="digiContext" property="context"/>

    <script language="JavaScript" type="text/javascript">
    <!--
    function addLoadEvent(func) {
    	  var oldonload = window.onload;
    	  if (typeof window.onload != 'function') {
    	    window.onload = func;
    	  } else {
    	    window.onload = function () {
    	      if (oldonload) {
    	        oldonload();
    	      }
    	      func();
    	  }
    	}
    }  	
	-->  
    </script>


	<HEAD>


		<%
			String title=(String)((org.apache.struts.tiles.ComponentContext) request.getAttribute("org.apache.struts.taglib.tiles.CompContext")).getAttribute("title");
			String key=(title.replaceAll(" ",""));
			%>
		<logic:present name="extraTitle" scope="request">

			<bean:define id="extTitle" name="extraTitle" scope="request" type="java.lang.String" />

		<TITLE>
			
				<c:set var="key">aim:pagetitle:<%=key%><%=extTitle%></c:set>
				<digi:trn>Aid Management Platform </digi:trn> 
				<digi:trn key="${key}">
					<%=title%> <%=extTitle%>
				</digi:trn>
		</TITLE>
		</logic:present>

		<logic:notPresent name="extraTitle" scope="request">
		<TITLE>
				<c:set var="key">aim:pagetitle:<%=key%></c:set>
				<digi:trn>Aid Management Platform </digi:trn> 
				<digi:trn key="${key}">
					<%=title%>
				</digi:trn>
		</TITLE>
		</logic:notPresent>



		



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

				<TD width="100%" align="center" bgcolor="#376091">
					<TABLE cellSpacing=0 cellPadding=0 width="98%">

						<TBODY>

						  	<TR>

						   	<TD align="left" vAlign="center">

									<digi:insert attribute="headerMiddle" />

								</TD>
							 	<TD align="right" vAlign="middle" style="padding-right:5px;" nowrap="nowrap">
										<digi:insert attribute="loginWidget" />
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
<!--  <br /><br /><br /><br /> -->
							</TD>

						</TR>

					</TABLE>

				</TD>

			</TR>

			<TR>
<logic:notPresent name="currentMember">
	<!-- Do not position the footer at the bottom if the user isn't logged-->
				<TD width="100%">
</logic:notPresent>
<logic:present name="currentMember">
				<TD width="100%" style="clear:both;bottom:0px;top:auto;">
</logic:present>
				   <digi:insert attribute="footer" />

				</TD>

			</TR>

			</TBODY>

		</TABLE>

	</BODY>

</HTML>

