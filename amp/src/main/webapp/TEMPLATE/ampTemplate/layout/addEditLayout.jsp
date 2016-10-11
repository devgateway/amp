<%@ page contentType="text/html; charset=UTF-8" %> 
<%@ taglib uri="/taglib/struts-tiles" prefix="tiles" %>
<%@ taglib uri="/taglib/struts-html" prefix="html" %>
<%@ taglib uri="/taglib/digijava" prefix="digi" %>
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
	<META http-equiv=Content-Type content="text/html; charset=utf-8">
</HEAD>
<BODY>
<TABLE cellspacing="0" cellpadding="0" width="100%" border="0" bordercolor="red">
	<TBODY>
  	<TR bgColor=#323232>
    <TD>
			<digi:insert attribute="headerTop" />
	 </TD>
    <TD align=right>
		<TABLE cellspacing="0" cellpadding="0" border="0" bordercolor="black">
     		<TBODY>
        	<TR>
         	<TD height=35></TD>
          	<TD>&nbsp;&nbsp;</TD>
			</TR>
			</TBODY>
		</TABLE>
	 </TD>
	</TR>
	</TBODY>
</TABLE>
<TABLE cellspacing="0" cellpadding="0" width="100%" border="0" bordercolor="green">
	<TBODY>
  	<TR bgColor=#5a5a58>
   	<TD>
			<digi:insert attribute="headerMiddle" />
		</TD>
    	<TD>
     	</TD>
	</TR>
  	<TR background="<%= digiContext %>/TEMPLATE/ampTemplate/images/bg-header.gif">
    	<TD background="<%= digiContext %>/TEMPLATE/ampTemplate/images/bg-header.gif" 
    	colSpan=2 height=66>
			<jsp:include page="../../../repository/aim/view/teamPagesHeader.jsp"  />
		</TD>
	</TR>
	</TBODY>
</TABLE>

         	
<TABLE width="789" border="0" cellpadding="0" cellspacing="0" bgcolor="#FFFFFF">
  	<TR>
	   	<TD class="r-dotted-lg">&nbsp;</TD>
		<TD width="789" align="left" valign="top">
			<TABLE width="100%">
				<TR>
					<TD	width="75%"><digi:insert attribute="addEditBody"/>	</TD>
					<TD width="25%"><digi:insert attribute="addEditNavigator"/>	</TD>
				</TR>
			</TABLE>
		</TD>
		<TD class=r-dotted-lg valign="top">&nbsp;</TD>
	</TR>
</TABLE>
<digi:insert attribute="footer" />
</BODY>
</HTML>


