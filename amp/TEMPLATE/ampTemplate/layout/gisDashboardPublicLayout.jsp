<%@ page contentType="text/html; charset=UTF-8" %> 
<%@ taglib uri="/taglib/struts-tiles" prefix="tiles" %>
<%@ taglib uri="/taglib/struts-html" prefix="html" %>
<%@ taglib uri="/taglib/digijava" prefix="digi" %>
<%@ taglib uri="/taglib/struts-logic" prefix="logic" %>

<%@ taglib uri="/taglib/fieldVisibility" prefix="field" %>
<%@ taglib uri="/taglib/featureVisibility" prefix="feature" %>



<style>
	#content{
		height: 100%;
	}
	#demo{
		height: 100%;
	}
	#div1{
		height: 96%;
	}
	
	SELECT {
	Font-size:10px;
	font-family:Verdana,Arial,Helvetica,sans-serif;
	width:200px;
}
</style>

<%
String countryName = "";
String ISO = null;
java.util.Iterator itr1 = org.digijava.module.aim.util.FeaturesUtil.getDefaultCountryISO().iterator();
while (itr1.hasNext()) {
	org.digijava.module.aim.dbentity.AmpGlobalSettings ampG = (org.digijava.module.aim.dbentity.AmpGlobalSettings) itr1.next();
	ISO = ampG.getGlobalSettingsValue();
}

if(ISO != null && !ISO.equals("")){
	org.digijava.kernel.dbentity.Country cntry = org.digijava.module.aim.util.DbUtil.getDgCountry(ISO);
	countryName = " " + cntry.getCountryName();
}
else {
	countryName = "";
}
%>


<HTML>
	<digi:base />
	<digi:context name="digiContext" property="context"/>
	<HEAD>
		<TITLE><digi:trn>Aid Management Platform</digi:trn> - <digi:trn key="gis:resultsmatrix">Results Matrix: </digi:trn><%=countryName%></TITLE>
		<META HTTP-EQUIV="CACHE-CONTROL" CONTENT="NO-CACHE">		
		<META HTTP-EQUIV="Content-Type" CONTENT="text/html; charset=UTF-8">
		<META HTTP-EQUIV="PRAGMA" CONTENT="NO-CACHE">
     	<META HTTP-EQUIV="EXPIRES" CONTENT="0">		
      	<link type="text/css" href="<digi:file src="/TEMPLATE/ampTemplate/css_2/tabs.css"/>" rel="stylesheet" />
		<script language="JavaScript" type="text/javascript" src="<digi:file src="module/aim/scripts/common.js"/>"></script>
			
				<%--
        <digi:ref href="css/gis.css" type="text/css" rel="stylesheet" />
        --%>
        
		<script type="text/javascript">
		<!--
			var lastTimeStamp;
			function yearChanged(){
								
			}
		-->
		</script>	

        	
	</HEAD>
	
    <BODY bgcolor="#FFFFFF" leftmargin="0" topmargin="0" marginwidth="0" marginheight="0">	
    	 <digi:insert attribute="headerTop" />
	
	<div class="main_menu">
		<digi:insert attribute="headerMiddle"/>
	</div>

<%--
<h2 style="padding-left:10px;font-size:15pt;"><digi:trn key="gis:resultsmatrix">Results Matrix: </digi:trn><%=countryName%></h2>
--%>
<TABLE cellspacing="0" cellpadding="0"  width="100%"  border="0"  align="left">
  <TBODY>
			<TR>
				<TD>&nbsp;</TD>
			</TR>
			<TR>
    	<TD align="center">
				<TABLE width="100" cellPadding="5" cellSpacing="0" border="0">
					<TR>
						<TD style="width: 600;max-width: 600;">
							<jsp:include page="/TEMPLATE/ampTemplate/layout/gisReportToolbarPublic.jsp" />
						</TD>
					</TR>
					<TR>
						<TD vAlign="top" align="center" height="100%" rowspan="3" style="width: 100%;max-width: 600;">
							<digi:insert attribute="body" />
						</TD>
					</TR>
				</TABLE>
			</TD>
		</TR>
		<TR>
			<TD>&nbsp;</TD>
		</TR>
		<TR>
	    <TD width="100%" >
	        <digi:insert attribute="footer" />
	    </TD>
  </TR>
  </TBODY>
</TABLE>
	</BODY>
</HTML>

