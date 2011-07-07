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
		<TITLE><digi:trn>Result Matrix/GIS settings</digi:trn></TITLE>
		<META HTTP-EQUIV="CACHE-CONTROL" CONTENT="NO-CACHE">		
		<META HTTP-EQUIV="Content-Type" CONTENT="text/html; charset=UTF-8">
		<META HTTP-EQUIV="PRAGMA" CONTENT="NO-CACHE">
     	<META HTTP-EQUIV="EXPIRES" CONTENT="0">		
		<digi:ref href="css/styles.css" type="text/css" rel="stylesheet" />
	    <digi:ref href="css/new_styles.css" type="text/css" rel="stylesheet" />
        <digi:ref href="css/tabview.css" type="text/css" rel="stylesheet" />
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
	
    <BODY leftmargin="0" topmargin="0" rightmargin="0" bottommargin="0">    	

<%--
<h2 style="padding-left:10px;font-size:15pt;"><digi:trn key="gis:resultsmatrix">Results Matrix: </digi:trn><%=countryName%></h2>
--%>
<TABLE cellSpacing=0 cellPadding=0  width="100%"  border=0 valign="top" align="left">
  <TBODY>
    <TR>
        <TD width="100%" bgColor=#323232 vAlign="center" align="left" height="10">
            <digi:insert attribute="headerTop" />
        </TD>
    </TR>
    <TR>
        <TD width="100%" align="center" vAlign="top" bgcolor="#376091">
            <TABLE cellSpacing=0 cellPadding=0 width="98%" border=0 vAlign="center" bgcolor="#376091">
                <TBODY>
                    <TR bgColor=#376091 height="15">
                        <TD align="left" vAlign="center" height="15">
                            <digi:insert attribute="headerMiddle" />
                        </TD>
                        <TD align="right" vAlign="top" height="15">
                            <digi:insert attribute="loginWidget" />
                        </TD>
                    </TR>
                </TBODY>
            </TABLE>
        </TD>
    </TR>
    <TR>
    	<TD align="left">
				<digi:insert attribute="body" />
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
</TABLE>
		
		
	</BODY>
</HTML>