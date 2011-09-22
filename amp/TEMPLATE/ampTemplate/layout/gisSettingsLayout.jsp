<%@ page contentType="text/html; charset=UTF-8" %> 
<%@ taglib uri="/taglib/struts-tiles" prefix="tiles" %>
<%@ taglib uri="/taglib/struts-html" prefix="html" %>
<%@ taglib uri="/taglib/digijava" prefix="digi" %>
<%@ taglib uri="/taglib/struts-logic" prefix="logic" %>

<%@ taglib uri="/taglib/fieldVisibility" prefix="field" %>
<%@ taglib uri="/taglib/featureVisibility" prefix="feature" %>



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

            <digi:insert attribute="headerTop" />
			
			
			<center>
	<div class="main_menu" id="userHomeMenu" >
    	<table cellpadding="0"cellspacing="0" width="1000">
        	<tr>
            	<td style="width:900px;" valign="top"><digi:insert attribute="headerMiddle"/></td>
                <td><digi:secure authenticated="true">
         <div class="workspace_info"> <!-- I think this class should be renamed to correspong the logout item -->   						
   			<digi:link styleClass="loginWidget" href="/j_spring_logout" module="aim">
				<digi:trn key="aim:logout">LOGOUT</digi:trn>
			</digi:link>
		</div>	
		</digi:secure></td>
            </tr>
        </table>
	</div>
	</center>			


				<digi:insert attribute="body" />
	        <digi:insert attribute="footer" />
		
	</BODY>
</HTML>