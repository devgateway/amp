<%@ page pageEncoding="UTF-8" %>
<%@ taglib uri="/taglib/struts-bean" prefix="bean" %>
<%@ taglib uri="/taglib/struts-logic" prefix="logic" %>
<%@ taglib uri="/taglib/struts-tiles" prefix="tiles" %>
<%@ taglib uri="/taglib/struts-html" prefix="html" %>
<%@ taglib uri="/taglib/jstl-core" prefix="c" %>
<%@ taglib uri="/taglib/digijava" prefix="digi" %>
<%@ taglib uri="/taglib/fieldVisibility" prefix="field" %>
<%@ taglib uri="/taglib/featureVisibility" prefix="feature" %>
<%@ taglib uri="/taglib/moduleVisibility" prefix="module" %>

<jsp:include page="teamPagesHeader.jsp" flush="true" />
<jsp:include page="allVisibilityTags.jsp" />
<table bgColor="#ffffff" cellPadding="0" cellSpacing="0" width="1000" align="center">
  <tr>
   <td align="left" class="r-dotted-lg" vAlign="top" width="750px">
      <table cellPadding="5" cellSpacing="0" width="100%">
        <tr>
          <td nowrap="nowrap" vAlign="top" align="center">

<fieldset style="background-color:#F2F2F2;">
<legend><span class="legend_label">Administrative Tools</span></legend>
<table width="100%" border="0" cellspacing="0" cellpadding="0" class="admin_landing_page_tbl">
  <tr>
    <td width=314 class="admin_landing_page_box" valign=top>
	<div class="admin_landing_page_box_cont">
	<div class="admin_landing_page_box_title">Global administration and maintenance of AMP</div>
	<table border="0" cellspacing="2" cellpadding="2">
  	<module:display name="Global Settings" parentModule="ADMINISTRATIVE SECTION">
	  <tr>
	    <td><img width="16" align="left" src="/TEMPLATE/ampTemplate/module/aim/images/arrow-th-BABAB9.gif"></td>
		    <td class="admin_box_label">
			    <c:set var="trnSystemSettings">
					<digi:trn>Click here to view System Settings</digi:trn>
			    </c:set>
		       <digi:link href="/GlobalSettings.do" title="${trnSystemSettings}" >
					<digi:trn>Global Settings</digi:trn>
		       </digi:link>
		   	</td>
	  	</tr>
  	</module:display>
  	<module:display name="Global Permission Manager" parentModule="ADMINISTRATIVE SECTION">
    <tr>
    	<c:set var="gatePermLink">
        	<digi:trn>Click here to manage gate permissions and assignments</digi:trn>
        </c:set>
        <td><img width="16" align="left" src="/TEMPLATE/ampTemplate/module/aim/images/arrow-th-BABAB9.gif"></td>
    	<td class="admin_box_label">
    	<html:link href="/gateperm/managePermMap.do" title="${gatePermLink}">
        	<digi:trn>Global permission Manager</digi:trn>
        </html:link>
    	</td>
  	</tr>
  	<tr>
  		<td><img width="16" align="left" src="/TEMPLATE/ampTemplate/module/aim/images/arrow-th-BABAB9.gif"></td>
  		<td class="admin_box_label">
  		<c:set var="permManagerLink">
    		<digi:trn key="aim:permManagerLinkTitle">Click here to manage permissions</digi:trn>
  		</c:set>
  		<html:link href="/wicket/permmanager/" title="${permManagerLink}">
    		<digi:trn key="aim:permissionManager">Permission Manager</digi:trn>
  		</html:link>
  		</td>
  	<tr>	
  	</module:display>
  	<module:display name="Feature Manager" parentModule="ADMINISTRATIVE SECTION">
  	<tr>
	    <td>
	    	<img width="16" align="left" src="/TEMPLATE/ampTemplate/module/aim/images/arrow-th-BABAB9.gif">
	    </td>
	    <td class="admin_box_label">
		    <c:set var="trnFeatureManager">
				<digi:trn>Click here to access Feature Manager</digi:trn>
		    </c:set>
		    <digi:link href="/visibilityManager.do" title="${trnFeatureManager}" >
		    	<digi:trn>Global feature Manager</digi:trn>
		    </digi:link>
	    </td>				
  	</tr>
  	</module:display>
  	<module:display name="Quartz Job Manager" parentModule="ADMINISTRATIVE SECTION"> 
  	<tr>
    	<td><img width="16" align="left" src="/TEMPLATE/ampTemplate/module/aim/images/arrow-th-BABAB9.gif"></td>
    	<td class="admin_box_label">
	    	<c:set var="trn">
				<digi:trn>Click here to view Job Manager</digi:trn>
	    	</c:set>
	    	<digi:link module="aim" href="/quartzJobManager.do?action=all" title="${trn}">
	    		<digi:trn>Job Manager</digi:trn>
	    	</digi:link>
    	</td>
  	</tr>
  	</module:display>
  	
  	<module:display name="Flag uploader" parentModule="ADMINISTRATIVE SECTION">
  	<tr>
    	<td><img width="16" align="left" src="/TEMPLATE/ampTemplate/module/aim/images/arrow-th-BABAB9.gif"></td>
    	<td class="admin_box_label">
	    	<c:set var="trnUploadFlags">
	        	<digi:trn>Click here to upload and select flags</digi:trn>
	        </c:set>
			<digi:link href="/flagUploader.do" title="${trnUploadFlags}" >
            	<digi:trn>Flag uploader/selector</digi:trn>
			</digi:link>
    	</td>
  	</tr>
	</module:display>
</table>
	</div></td>
    <td width=10>&nbsp;</td>
    <td width=314 class="admin_landing_page_box" valign=top>
	<div class="admin_landing_page_box_cont">
	<div class="admin_landing_page_box_title">User management</div>
	<table border="0" cellspacing="2" cellpadding="2">
	  <tr>
	    <td>
	    	<img width="16" align="left" src="/TEMPLATE/ampTemplate/module/aim/images/arrow-th-BABAB9.gif">
	   	</td>
	    <td class="admin_box_label">
			<c:set var="trnWorkspaceManager">
	        	<digi:trn>Click here to view Workspace Manager</digi:trn>
	        </c:set>
	        <digi:link href="/workspaceManager.do~page=1~reset=true" title="${trnWorkspaceManager}" >
	        	<digi:trn>Workspace Manager</digi:trn>
	       </digi:link>
	    </td>
	  </tr>
      <module:display name="User Manager" parentModule="ADMINISTRATIVE SECTION">
      <tr>
	  	<td><img width="16" align="left" src="/TEMPLATE/ampTemplate/module/aim/images/arrow-th-BABAB9.gif"></td>
    	<td class="admin_box_label">
    		<c:set var="translation">
				<digi:trn>Click here to view User Manager</digi:trn>
            </c:set>
            <digi:link module="um" href="/viewAllUsers.do?reset=true" title="${translation}">
            	<digi:trn>User Manager</digi:trn>
            </digi:link>
    	</td>
	  </tr>
	 </module:display>
</table>

	</div></td>
    <td width=11>&nbsp;</td>
    <td width=314 class="admin_landing_page_box" valign=top>
	<div class="admin_landing_page_box_cont">
	<div class="admin_landing_page_box_title">Backbone lists of AMP</div>
	<table border="0" cellspacing="2" cellpadding="2">
	<module:display name="Category Manager" parentModule="ADMINISTRATIVE SECTION">
  	<tr>
    	<td><img width="16" align="left" src="/TEMPLATE/ampTemplate/module/aim/images/arrow-th-BABAB9.gif"></td>
    	<td class="admin_box_label">
    	<c:set var="translation">
			<digi:trn>Click here to view Category Manager</digi:trn>
        </c:set>
		<digi:link href="/categoryManager.do" title="${translation}" contextPath="/categorymanager" >
        	<digi:trn>Category Manager</digi:trn>
        </digi:link>
    	</td>
  	</tr>
  	</module:display>
    <module:display name="Dynamic Region Manager" parentModule="ADMINISTRATIVE SECTION">
    <tr>
    	<td><img width="16" align="left" src="/TEMPLATE/ampTemplate/module/aim/images/arrow-th-BABAB9.gif"></td>
    	<td class="admin_box_label">
	    	<c:set var="translation">
	        	<digi:trn>Click here to view Region Manager</digi:trn>
	       	</c:set>
	        <digi:link href="/dynLocationManager.do" title="${translation}" >
	        	<digi:trn>Region Manager</digi:trn>
	        </digi:link>
    	</td>
  	</tr>
  	</module:display>
  	<module:display name="Sector Manager" parentModule="ADMINISTRATIVE SECTION">
  	<tr>
    	<td><img width="16" align="left" src="/TEMPLATE/ampTemplate/module/aim/images/arrow-th-BABAB9.gif"></td>
    	<td class="admin_box_label">
    		<c:set var="translation">
            	<digi:trn>Click here to view Sector Manager</digi:trn>
            </c:set>
            <digi:link href="/getSectorSchemes.do" title="${translation}">
            	<digi:trn>Sector Manager</digi:trn>
			</digi:link>
    	</td>
  	</tr>
  	</module:display>
  	<module:display name="Fiscal Calendar Manager" parentModule="ADMINISTRATIVE SECTION">
  	<tr>
    	<td><img width="16" align="left" src="/TEMPLATE/ampTemplate/module/aim/images/arrow-th-BABAB9.gif"></td>
    	<td class="admin_box_label">
	    	<c:set var="translation">
				<digi:trn>Click here to view Fiscal Calendar Manager</digi:trn>
	        </c:set>
	       <digi:link href="/fiscalCalendarManager.do" title="${translation}" >
	       		<digi:trn>Calendar Manager</digi:trn>
	        </digi:link>
    	</td>
  	</tr>
  	</module:display>
  	<feature:display name="Admin - Component Type" module="Components">
  	<tr>
    	<td><img width="16" align="left" src="/TEMPLATE/ampTemplate/module/aim/images/arrow-th-BABAB9.gif"></td>
    	<td class="admin_box_label">
	     	<c:set var="translation">
				<digi:trn>Click here to view Components Types Manager</digi:trn>
	     	</c:set>
	    	 <digi:link href="/updateComponentType.do" title="${translation}" >
	     		<digi:trn>Component types manager</digi:trn>
     		</digi:link>   
    	</td>
  	</tr>
  	</feature:display>
  	<!-- NO MOUDULE TO CONTROL THIS SECTION -->
  	<tr>
	    <td><img width="16" align="left" src="/TEMPLATE/ampTemplate/module/aim/images/arrow-th-BABAB9.gif"></td>
	    <td class="admin_box_label">
	    	<c:set var="trnOrganizationManager">
	        	<digi:trn>Click here to view Organization Manager</digi:trn>
	        </c:set>
	        <digi:link href="/organisationManager.do?orgSelReset=true" title="${trnOrganizationManager}" >
	        	<digi:trn>Organization manager</digi:trn>
	        </digi:link>
	    </td>
   </tr>
    <module:display name="Program Managers" parentModule="ADMINISTRATIVE SECTION">
   <tr>
    <td><img width="16" align="left" src="/TEMPLATE/ampTemplate/module/aim/images/arrow-th-BABAB9.gif"></td>
    <td class="admin_box_label">
    	<c:set var="translation">
			<digi:trn>Click here to view Multi Program Manager</digi:trn>
        </c:set>
        <digi:link href="/themeManager.do~view=multiprogram" title="${translation}" >
        	<digi:trn>Multi Program Manager</digi:trn>
        </digi:link>
    </td>
   </tr>
  <tr>
    <td><img width="16" align="left" src="/TEMPLATE/ampTemplate/module/aim/images/arrow-th-BABAB9.gif"></td>
    <td class="admin_box_label">
    	<c:set var="translation">
			<digi:trn>Click here to view Multi Program Configuration Page</digi:trn>
        </c:set>
        <digi:link href="/programConfigurationPage.do" title="${translation}" >
        	<digi:trn>Multi Program Configuration</digi:trn>
        </digi:link>
    </td>
  </tr>
  </module:display>
  <!-- NO MOUDULE TO CONTROL THIS SECTION -->
  <tr>
    <td><img width="16" align="left" src="/TEMPLATE/ampTemplate/module/aim/images/arrow-th-BABAB9.gif"></td>
    <td class="admin_box_label">
    	<c:set var="translation">
        	<digi:trn>Click here to view Indicator Manager</digi:trn>
        </c:set>
        <digi:link href="/viewIndicators.do?sortBy=nameAsc" title="${translation}" >
        	<digi:trn>Indicator Manager</digi:trn>
        </digi:link>
    </td>  
  </tr>
</table>
</div>
</td>
</tr>
</table>
<table width="100%" border="0" cellspacing="0" cellpadding="0" class="admin_landing_page_tbl">
  <tr>
    <td width=314 class="admin_landing_page_box" valign=top>
	<div class="admin_landing_page_box_cont">
	<div class="admin_landing_page_box_title">Currencies and rates</div>
	<table border="0" cellspacing="2" cellpadding="2">
  <module:display name="Currency Manager" parentModule="ADMINISTRATIVE SECTION">
  <tr>
    <td><img width="16" align="left" src="/TEMPLATE/ampTemplate/module/aim/images/arrow-th-BABAB9.gif"></td>
    <td class="admin_box_label">
    	<c:set var="translation">
			<digi:trn>Click here to view Currency Manager</digi:trn>
	    </c:set>
	    <digi:link href="/currencyManager.do~clean=true" title="${translation}" >
	    	<digi:trn>Currency Manager</digi:trn>
	    </digi:link>
    </td>
  </tr>
  </module:display>
  <module:display name="Currency Rates Manager" parentModule="ADMINISTRATIVE SECTION">
  <tr>
    <td><img width="16" align="left" src="/TEMPLATE/ampTemplate/module/aim/images/arrow-th-BABAB9.gif"></td>
    <td class="admin_box_label">
   		<c:set var="translation">
			<digi:trn>Click here to view Currency Rates Manager</digi:trn>
		</c:set>
		<digi:link href="/showCurrencyRates.do~clean=true~timePeriod=1" title="${translation}">
        	<digi:trn>Currency Rate Manager</digi:trn>
        </digi:link>
    </td>
  </tr>
  </module:display>
</table>

	</div></td>
    <td width=10>&nbsp;</td>
    <td width=314 class="admin_landing_page_box" valign=top>
	<div class="admin_landing_page_box_cont">
	<div class="admin_landing_page_box_title">Import and export of data</div>
	<table border="0" cellspacing="2" cellpadding="2">
  <module:display name="Code Chapitre Importer" parentModule="ADMINISTRATIVE SECTION">	
  <tr>
    <td><img width="16" align="left" src="/TEMPLATE/ampTemplate/module/aim/images/arrow-th-BABAB9.gif"></td>
    <td class="admin_box_label">
		<digi:link module="dataExchange" href="/importChapters.do" title="Import Chapters" >
			<digi:trn>Code Chapitre Importer</digi:trn>
		</digi:link>
    </td>
  </tr>
  </module:display>
  <module:display name="Budget Codes Exporter" parentModule="ADMINISTRATIVE SECTION">
  <tr>
    <td><img width="16" align="left" src="/TEMPLATE/ampTemplate/module/aim/images/arrow-th-BABAB9.gif"></td>
    <td class="admin_box_label">
    	<digi:link href="/export.do" title="" >
			<digi:trn>Budget Code Exporter</digi:trn>
		</digi:link>
    </td>
  </tr>
  </module:display>
  <module:display name="Activity Partial Import Manager" parentModule="ADMINISTRATIVE SECTION">
  <tr>
    <td><img width="16" align="left" src="/TEMPLATE/ampTemplate/module/aim/images/arrow-th-BABAB9.gif"></td>
    <td class="admin_box_label">
	    <c:set var="trnDataExchangeExportTitle">
			<digi:trn>Click here to view Partial Data Import Manager</digi:trn>
		</c:set>
	   <digi:link module="dataExchange"  href="/manageSource.do" title="${trnDataExchangeExportTitle}" >
	      <digi:trn>Partial Data Import Manager</digi:trn>
	   </digi:link>
	</td>
  </tr>
  </module:display>
  <module:display name="Activity Export Manager" parentModule="ADMINISTRATIVE SECTION">
  	<tr>
		<td><img width="16" align="left" src="/TEMPLATE/ampTemplate/module/aim/images/arrow-th-BABAB9.gif"></td>
    	<td class="admin_box_label">
        	<c:set var="trnDataExchangeExportTitle">
				<digi:trn>Click here to view Data Export Manager</digi:trn>
			</c:set>
			<digi:link module="dataExchange"  href="/exportWizard.do?method=prepear" title="${trnDataExchangeExportTitle}" >
				<digi:trn>Data Export Manager</digi:trn>
			</digi:link>
       </td>
   </tr>
 </module:display>
  
  
  <!-- 
  <module:display name="Admin Translation Manager" parentModule="ADMINISTRATIVE SECTION">
  <tr>
    <td><img width="16" align="left" src="/TEMPLATE/ampTemplate/module/aim/images/arrow-th-BABAB9.gif"></td>
    <td class="admin_box_label">
    	<c:set var="trnTranslationManager">
        	<digi:trn>Click here to view Translation Manager</digi:trn>
       </c:set>
       <digi:link module="translation" href="/importexport.do" title="${trnTranslationManager}">
     		<digi:trn>Translation Manager</digi:trn>
       </digi:link>	
    </td>
  </tr>
  </module:display>
   -->
</table>

	</div></td>
    <td width=11>&nbsp;</td>
    <td width=314 class="admin_landing_page_box" valign=top>
	<div class="admin_landing_page_box_cont">
	<div class="admin_landing_page_box_title">AMP workflow auditor</div>
	<table border="0" cellspacing="2" cellpadding="2">
  <module:display name="Audit Logger Manager" parentModule="ADMINISTRATIVE SECTION">
  <tr>
    <td><img width="16" align="left" src="/TEMPLATE/ampTemplate/module/aim/images/arrow-th-BABAB9.gif"></td>
    <td class="admin_box_label">
	    <c:set var="translation">
			<digi:trn>Click here to view Audit Logger Manager</digi:trn>
	    </c:set>
        <digi:link href="/auditLoggerManager.do" title="${translation}" >
        	<digi:trn>Audit Logger Manager</digi:trn>
        </digi:link>
    </td>
  </tr>
  </module:display>
  <module:display name="Activity Manager" parentModule="ADMINISTRATIVE SECTION">
	 <tr>
     	<td><img width="16" align="left" src="/TEMPLATE/ampTemplate/module/aim/images/arrow-th-BABAB9.gif"></td>
    	<td class="admin_box_label">
	    	<c:set var="trnActivityManager">
				<digi:trn>Click here to view Activity Manager</digi:trn>
	        </c:set>
			<digi:link href="/activityManager.do" title="${trnActivityManager}" >
	        	<digi:trn>Activity Manager</digi:trn>
	        </digi:link>
    	</td>
  	</tr>
  	</module:display>  
</table>

	</div></td>
  </tr>
</table>
<table width="100%" border="0" cellspacing="0" cellpadding="0" class="admin_landing_page_tbl">
  <tr>
    <td width=314 class="admin_landing_page_box" valign=top>
	<div class="admin_landing_page_box_cont">
	<div class="admin_landing_page_box_title">Public view toolbox</div>
	<table border="0" cellspacing="2" cellpadding="2">
   <tr>
		 <td><img width="16" align="left" src="/TEMPLATE/ampTemplate/module/aim/images/arrow-th-BABAB9.gif"></td>
		 <td class="admin_box_label">
		  	<c:set var="translation">
				<digi:trn>Click here to edit public view content</digi:trn>
			</c:set>  
			<digi:link module="content" href="/contentManager.do">
				<digi:trn>Public View Content</digi:trn>
			</digi:link>                      
		 </td>
	</tr>
	
	<module:display name="Public Filter Manager" parentModule="ADMINISTRATIVE SECTION">
      <tr>
    	<td><img width="16" align="left" src="/TEMPLATE/ampTemplate/module/aim/images/arrow-th-BABAB9.gif"></td>
    	<td class="admin_box_label">
    		<c:set var="translation">
	 	    	<digi:trn>Click here to manage public filters</digi:trn>
	 		</c:set>  
	 	 	<digi:link module="contentrepository" href="/publicDocTabManager.do?action=show">
	 	 		<digi:trn>Public Filter Manager</digi:trn>
	 	 </digi:link>                       
    	</td>
  	</tr>
	</module:display>
</table>

	</div></td>
    <td width=10>&nbsp;</td>
    <td width=314 class="admin_landing_page_box" valign=top>
	<div class="admin_landing_page_box_cont">
	<div class="admin_landing_page_box_title">Org Profile toolbox</div>
	<table border="0" cellspacing="2" cellpadding="2">
  <tr>
    <td><img width="16" align="left" src="/TEMPLATE/ampTemplate/module/aim/images/arrow-th-BABAB9.gif"></td>
    <td class="admin_box_label"><a href=#>Org profile widget manager</a></td>
  </tr>
    <tr>
    <td><img width="16" align="left" src="/TEMPLATE/ampTemplate/module/aim/images/arrow-th-BABAB9.gif"></td>
    <td class="admin_box_label"><a href=#>Widget Place Manager</a></td>
  </tr>
</table>

	</div></td>
    <td width=11>&nbsp;</td>
    <td width=314 class="admin_landing_page_box" valign=top>
	<div class="admin_landing_page_box_cont">
	<div class="admin_landing_page_box_title">Paris indicator Tools</div>
	<table border="0" cellspacing="2" cellpadding="2">
  <feature:display  name="Paris Indicator Table Widgets" module="WIDGETS">
  <tr>
    <td><img width="16" align="left" src="/TEMPLATE/ampTemplate/module/aim/images/arrow-th-BABAB9.gif"></td>
    <td class="admin_box_label">
    	<c:set var="translation">
			<digi:trn>Click here to view Paris Indicator Table Table Widgets Manager</digi:trn>
        </c:set>
        <digi:link module="widget" href="/piTableWidgetManager.do" title="${translation}">
        	<digi:trn> Paris Indicator Table Widget</digi:trn>
        </digi:link>
    </td>
  </tr>
  </feature:display>
</table>

	</div></td>
  </tr>
</table>
<table width="100%" border="0" cellspacing="0" cellpadding="0" class="admin_landing_page_tbl">
  <tr>
    <td width=314 class="admin_landing_page_box" valign=top>
	<div class="admin_landing_page_box_cont">
	<div class="admin_landing_page_box_title">Message tools</div>
	<table border="0" cellspacing="2" cellpadding="2">
  	<feature:display name="Message Manager" module="ADMIN">
  	<tr>
    	<td><img width="16" align="left" src="/TEMPLATE/ampTemplate/module/aim/images/arrow-th-BABAB9.gif"></td>
    	<td class="admin_box_label">
    		<c:set var="trn">
				<digi:trn>Click here to view Message Settings</digi:trn>
	       	</c:set>
	        <digi:link module="message" href="/msgSettings.do?actionType=getSettings" title="${trn}">
	        	<digi:trn>Message Manager</digi:trn>
	        </digi:link>
    	</td>
  	</tr>
  	</feature:display>
</table>

	</div></td>
    <td width=10>&nbsp;</td>
    <td width=314 class="admin_landing_page_box" valign=top>
	<div class="admin_landing_page_box_cont">
	<div class="admin_landing_page_box_title">Resources tools</div>
	<table border="0" cellspacing="2" cellpadding="2">
  <module:display name="Resource Label Manager" parentModule="ADMINISTRATIVE SECTION">
  <tr>
    <td><img width="16" align="left" src="/TEMPLATE/ampTemplate/module/aim/images/arrow-th-BABAB9.gif"></td>
    <td class="admin_box_label">
    	<c:set var="translation">
			<digi:trn>Click here to manage resource labels</digi:trn>
 		</c:set>  
 	 	<digi:link module="contentrepository" href="/labelManager.do?htmlView=true">
 	 		<digi:trn>Resource Label Manager</digi:trn>
 	 	</digi:link>
    </td>
  </tr>
  </module:display>
    <tr>
    <td><img width="16" align="left" src="/TEMPLATE/ampTemplate/module/aim/images/arrow-th-BABAB9.gif"></td>
    <td class="admin_box_label">
   		<c:set var="translation">
			<digi:trn>Click here to view template document manager</digi:trn>
 		</c:set>  
 	 	<digi:link module="contentrepository" href="/tempDocManager.do?actType=viewTemplateDocuments">
 	 		<digi:trn>Template Documents Manager</digi:trn>
 	 	</digi:link>   
    </td>
  </tr>
</table>

	</div></td>
    <td width=11>&nbsp;</td>
    <td width=314 class="admin_landing_page_box" valign=top>
	<div class="admin_landing_page_box_cont">
	<div class="admin_landing_page_box_title">Results dashboard tools</div>
	<table border="0" cellspacing="2" cellpadding="2">
  <feature:display  name="Results Dashboard Data" module="WIDGETS">
  <tr>
    <td><img width="16" align="left" src="/TEMPLATE/ampTemplate/module/aim/images/arrow-th-BABAB9.gif"></td>
    <td class="admin_box_label">
    	<c:set var="translation">
			<digi:trn>Click here to view Results Dashboard Data Manager</digi:trn>
        </c:set>
        <a href="/widget/indSectRegManager.do" title="${translation}">
        	<digi:trn>Results Dashboard Data Manager</digi:trn>
        </a>
    </td>
  </tr>
  </feature:display>
   <feature:display  name="Indicator chart Widgets" module="WIDGETS">
    <tr>
    <td><img width="16" align="left" src="/TEMPLATE/ampTemplate/module/aim/images/arrow-th-BABAB9.gif"></td>
    <td class="admin_box_label">
    	<c:set var="translation">
			<digi:trn>Click here to view Indicator Chart Widgets Manager</digi:trn>
        </c:set>
        <a href="/widget/indicatorchartwidgets.do" title="${translation}">
        	<digi:trn>Indicator Chart Widget Manager</digi:trn>
        </a>
    </td>
  </tr>
  </feature:display>
</table>

	</div></td>
  </tr>
</table>			
</fieldset>
			
			
			
			
			<br />
          </td>
        </tr>
      </table>
    </td>
  </tr>
</table>
