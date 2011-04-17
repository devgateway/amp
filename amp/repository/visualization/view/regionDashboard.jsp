<%@ page pageEncoding="UTF-8" %>
<%@ taglib uri="/taglib/struts-bean" prefix="bean" %>
<%@ taglib uri="/taglib/struts-logic" prefix="logic" %>
<%@ taglib uri="/taglib/struts-tiles" prefix="tiles" %>
<%@ taglib uri="/taglib/struts-html" prefix="html" %>
<%@ taglib uri="/taglib/digijava" prefix="digi" %>
<%@ taglib uri="/taglib/jstl-core" prefix="c" %>
<%@ taglib uri="/taglib/jstl-functions" prefix="fn" %>

<!-- Visualization's Stylesheet-->
<link rel="stylesheet" href="css_2/visualization.css" type="text/css" />
<link rel="stylesheet" type="text/css" href="js_2/yui/tabview/assets/skins/sam/tabview.css">
<digi:ref href="css_2/visualization_yui_tabs.css" type="text/css" rel="stylesheet" />
<style>
	.flashcontent {
		border: solid 1px #000;
		margin:5px 0px 0px 0px;
		padding:0px 0px 0px 0px;
		vertical-align:top;
		width:634px;
		height:350px;
	}
</style>
<!-- Visualization's Scripts-->
<script type="text/javascript" src="/TEMPLATE/ampTemplate/js_2/yui/json/json-min.js"></script> 
<script type="text/javascript" src="/TEMPLATE/ampTemplate/js_2/yui/element/element-min.js"></script> 
<script type="text/javascript" src="/TEMPLATE/ampTemplate/js_2/yui/tabview/tabview-min.js"></script> 
<script type="text/javascript" src="/TEMPLATE/ampTemplate/js_2/flash/swfobject.js"></script>

<script language="javascript">
<!--

// TODO: Move this to individual script file
$D = YAHOO.util.Dom;
$E = YAHOO.util.Event;

var yuiLoadingPanel = function(conf){
    conf = conf == undefined ? new Array() : conf;
    conf.id = conf.id == undefined ? 'yuiLoadingPanel':confi.id;
    conf.header = conf.header == undefined ? 'Loading, please wait...':conf.header;
    conf.width = conf.width == undefined ? '240px':conf.width;
    this.conf = conf;
    this.cancelEvent = new YAHOO.util.CustomEvent("cancelEvent", this);
    this.init();
	
};

yuiLoadingPanel.prototype = {
    init:function(){
        var loadingPanel = new YAHOO.widget.Panel(this.conf.id,{
            width:this.conf.width,
	    fixedcenter:true,
            close:false,
            draggable:false,
            modal:true,
            visible:false
        });
    
       loadingPanel.setBody(this.conf.header + 
               '<img src="http://us.i1.yimg.com/us.yimg.com/i/us/per/gr/gp/rel_interstitial_loading.gif" />');
               loadingPanel.render(document.body);
               $D.addClass(loadingPanel.id, 'tcc_lightboxLoader');
               var cancelLink = document.createElement('a');
               $D.setStyle(cancelLink, 'cursor', 'pointer');
               cancelLink.appendChild(document.createTextNode('Cancel'));
               $E.on(cancelLink, 'click', function(e, o){
       	           o.self.loadingPanel.hide();
       	           o.self.cancelEvent.fire();
               }, {self:this});
               loadingPanel.appendToBody(document.createElement('br'));
               loadingPanel.appendToBody(cancelLink);
               $D.setStyle(loadingPanel.body, 'text-align', 'center');
//               $D.addClass(document.body, 'yui-skin-sam');
        this.loadingPanel = loadingPanel;
    },
    show:function(text){
        if(text != undefined){
            this.loadingPanel.setHeader(text);
        }else{
	    this.loadingPanel.setHeader(this.conf.header);
	}
	this.loadingPanel.show();
    },
    hide:function(){
        this.loadingPanel.hide();
    }
};
-->
</script>


<!-- BREADCRUMB START -->
<div class="centering">
<span class="sec_name"><digi:trn>Region Profile Dashboard</digi:trn></span><span class="breadcrump_sep">|</span><a href=# class="l_sm"><digi:trn>Dashboards</digi:trn></a><span class="breadcrump_sep"><b>»</b></span><span class="bread_sel"><digi:trn>Region Profile Dashboard</digi:trn></span>
</div>
<br/>
<!-- BREADCRUMB END -->
<!-- MAIN CONTENT PART START -->
<digi:form action="/filters.do">
<html:hidden property="filter.year" styleId="currentYear"/>
<html:hidden property="filter.yearsInRange" styleId="yearsInRange" />
<center>
<div class="dashboard_header">
<!--<div class="dashboard_total"><b class="dashboard_total_num">${visualizationform.summaryInformation.totalCommitments}</b><br /><digi:trn>Total Commitments</digi:trn> ( ${visualizationform.filter.currencyId} )</div>-->
<div class="dashboard_total"><div id="divTotalComms"></div><br /><digi:trn>Total Commitments</digi:trn> ( ${visualizationform.filter.currencyId} )</div>
</center>
<table border="0" cellspacing="0" cellpadding="0">
  <tr>
    <td>
    <div class="dashboard_name" id="region_name">
    	<!--<c:if test="${visualizationform.filter.selRegionIds eq '-1' }">
	    	<digi:trn>ALL SECTORS</digi:trn>
    	</c:if>
    	<c:if test="${visualizationform.filter.selRegionIds ne '-1' }">
			<c:forEach var="region" items="${visualizationform.filter.sectors}">
			<c:if test="${sector.ampSectorId eq visualizationform.filter.sectorId}">
				${region}
			</c:if>
			</c:forEach>
    	</c:if>-->
    	<digi:trn>ALL REGIONS</digi:trn>
    </div>
    </td>
    <td><div class="dash_ico"><img src="/TEMPLATE/ampTemplate/img_2/ico_pdf.gif" align=left style="margin-right:5px;"> <div class="dash_ico_link"><a href=# class="l_sm">Export to PDF</a></div></div> <div class="dash_ico"><img src="/TEMPLATE/ampTemplate/img_2/ico_word_1.gif" align=left style="margin-right:5px;"> <div class="dash_ico_link"><a href=# class="l_sm">Export to DOC</a></div></div> <div class="dash_ico"><img src="/TEMPLATE/ampTemplate/img_2/ico_export.gif" align=left style="margin-right:5px;"> <div class="dash_ico_link"><a href=# class="l_sm">Export Options</a></div></div></td>
  </tr>
</table>
<div class="dashboard_stat" id="divSummaryInfo" ></div>
<!--<div class="dashboard_stat">Total Disbursements: <div id="divTotalDisbs"></div> <span class="breadcrump_sep">|</span>Total Number of Projects: <div id="divNumOfProjs"></div><span class="breadcrump_sep">|</span>Total Number of Sectors: <div id="divNumOfSecs"></div><span class="breadcrump_sep">|</span>Total Number of Regions: <div id="divNumOfRegs"></div><span class="breadcrump_sep">|</span>Average Project Size: <div id="divAvgProjSize"></div></div>-->

</div>
<table width="1000" border="0" cellspacing="0" cellpadding="0" align="center" style="margin-top:15px;">
  <tr>
    <td width=296 bgcolor="#F4F4F4" valign="top">
	<div style="background-color:#FFFFFF; height:7px;">&nbsp;</div>
	<div class="dash_left">
	<fieldset>
	<legend><span class=legend_label>Quick Filter</span></legend>
	<html:checkbox property="filter.workspaceOnly">Show Data from All Workspaces</html:checkbox>
	<hr />
	<table cellspacing="0" cellpadding="0" width="100%"> 
   <tr>
    <td><digi:trn>Region</digi:trn>:</td>
    <td align=right>
       <html:select property="filter.selRegionIds" styleId="region_dropdown_id" styleClass="dropdwn_sm" style="width:145px;">
           <html:option value="-1"><digi:trn>All</digi:trn></html:option>
           <html:optionsCollection property="filter.regions" value="id" label="name" />
       </html:select>
	</td>
  </tr> 
   <tr>
    <td><digi:trn>Zone</digi:trn>:</td>
    <td align=right>
       <html:select property="filter.selZoneIds" styleId="zone_dropdown_id" styleClass="dropdwn_sm" style="width:145px;">
           <html:option value="-1"><digi:trn>All</digi:trn></html:option>
       </html:select>
	</td>
  </tr> 
  <tr>
  <td><digi:trn>Sector</digi:trn>:</td>
	   <td align=right>
	   <html:select property="filter.sectorIds" styleId="sector_dropdown_id" styleClass="dropdwn_sm" style="width:145px;">
           <html:option value="-1"><digi:trn>All</digi:trn></html:option>
           <html:optionsCollection property="filter.sectors" value="ampSectorId" label="name" />
       </html:select>
	</td>
  </tr>
  <tr>
    <td><digi:trn>Sub-Sector</digi:trn>:</td>
    <td align=right>
       <html:select property="filter.subSectorIds" styleId="sub_sector_dropdown_id" styleClass="dropdwn_sm" style="width:145px;">
           <html:option value="-1"><digi:trn>All</digi:trn></html:option>
       </html:select>
	</td>
  </tr>
  <tr>
    <td><digi:trn>Organization Group</digi:trn>:</td>
    <td align=right>
       <html:select property="filter.organizationGroupId" styleId="org_group_dropdown_id" styleClass="dropdwn_sm" style="width:145px;">
           <html:option value="-1"><digi:trn>All</digi:trn></html:option>
           <html:optionsCollection property="filter.orgGroups" value="ampOrgGrpId" label="orgGrpName" />
       </html:select>
	</td>
  </tr>
   <tr>
    <td><digi:trn>Organization</digi:trn>:</td>
    <td align=right>
       <html:select property="filter.orgIds" styleId="org_dropdown_id" styleClass="dropdwn_sm" style="width:145px;">
           <html:option value="-1"><digi:trn>All</digi:trn></html:option>
       </html:select>
<!-- 
    <select class="dropdwn_sm" style="width:145px;" name="org_dropdown_id" id="org_dropdown_id">
		<option value="-1"><digi:trn>All</digi:trn></option>
	</select>
 -->
	</td>
  </tr>
  
 </table>

	<center>
	<input type="button" value="Filter" class="buttonx" style="margin-top:10px;" id="applyButton">
	</center>
	</fieldset>
	<fieldset>
<!-- 	
	<legend><span class=legend_label>Dashboard Info</span></legend>
	<div class="field_text">This is a <b>Sector</b> dashboard which presents 
statistics and aggregates related to the Sector 
<b>AGRICULTURE</b>. Read about our methodology.</div>
</fieldset>
 	<fieldset>
	<legend><span class=legend_label>Organization Profile</span></legend>
	<div class="field_text">
	Type: <b>All</b>
	<hr />
	Organization Name: <b>Not applicable</b>
	<hr />
	Organization Acronym: <b>Not applicable</b>
	<hr />
	Donor Group: <b>All</b>
	<hr />
	Web Link: <b>Not applicable</b>
	</div>
</fieldset>	
	<fieldset>
	<legend><span class=legend_label>Primary Contact Information</span></legend>
	<div class="field_text">
Last Name: <b>Org Contact Last Name</b>
<hr />
Organization Name: <b>Not applicable</b>
<hr />
Organization Acronym: <b>Not applicable</b>
<hr>
Donor Group: <b>All</b>
<hr />
Web Link: <b>Not applicable</b>
	</div>
</fieldset>	
-->	
	<fieldset>
	<legend><span class=legend_label>Top Projects</span></legend>
	<div id="divTopProjects" class="field_text">
		<c:set var="index" value="0"/>
		<c:forEach items="${visualizationform.ranksInformation.topProjects}" var="projectItem">
		<c:set var="index" value="${index+1}"/>
		
		 <c:out value="${index}"/>. <c:out value="${projectItem.key}"/>  <b>($<c:out value="${projectItem.value}"/>)</b>
			<hr />
		</c:forEach>
	
		<a href="javascript:showFullProjects()" style="float:right;">View Full List</a>
	</div>
	<div id="divFullProjects" class="field_text" style="display: none;">
		<c:set var="index" value="0"/>
		<c:forEach items="${visualizationform.ranksInformation.fullProjects}" var="projectItem">
		<c:set var="index" value="${index+1}"/>
		
		 <c:out value="${index}"/>. <c:out value="${projectItem.key}"/>  <b>($<c:out value="${projectItem.value}"/>)</b>
			<hr />
		</c:forEach>
	
		<a href="javascript:hideFullProjects()" style="float:right;">View Top List</a>
	</div>
</fieldset>	
	<fieldset>
	<legend><span class=legend_label>Top Donors</span></legend>
	<div id="divTopDonors" class="field_text">
		<c:set var="index" value="0"/>
		<c:forEach items="${visualizationform.ranksInformation.topDonors}" var="donorItem">
		<c:set var="index" value="${index+1}"/>
		
		 <c:out value="${index}"/>. <c:out value="${donorItem.key}"/>  <b>($<c:out value="${donorItem.value}"/>)</b>
			<hr />
		</c:forEach>
	
		<a href="javascript:showFullDonors()" style="float:right;">View Full List</a>
	</div>
	<div id="divFullDonors" class="field_text" style="display: none;">
		<c:set var="index" value="0"/>
		<c:forEach items="${visualizationform.ranksInformation.fullDonors}" var="donorItem">
		<c:set var="index" value="${index+1}"/>
		
		 <c:out value="${index}"/>. <c:out value="${donorItem.key}"/>  <b>($<c:out value="${donorItem.value}"/>)</b>
			<hr />
		</c:forEach>
	
		<a href="javascript:hideFullDonors()" style="float:right;">View Top List</a>
	</div>
</fieldset>	
	<fieldset>
	<legend><span class=legend_label>Top Sectors</span></legend>
	<div id="divTopSectors" class="field_text">
		<c:set var="index" value="0"/>
		<c:forEach items="${visualizationform.ranksInformation.topSectors}" var="sectorItem">
		<c:set var="index" value="${index+1}"/>
		
		 <c:out value="${index}"/>. <c:out value="${sectorItem.key}"/>  <b>($<c:out value="${sectorItem.value}"/>)</b>
			<hr />
		</c:forEach>
	
		<a href="javascript:showFullSectors()" style="float:right;">View Full List</a>
	</div>
	<div id="divFullSectors" class="field_text" style="display: none;">
		<c:set var="index" value="0"/>
		<c:forEach items="${visualizationform.ranksInformation.fullSectors}" var="sectorItem">
		<c:set var="index" value="${index+1}"/>
		
		 <c:out value="${index}"/>. <c:out value="${sectorItem.key}"/>  <b>($<c:out value="${sectorItem.value}"/>)</b>
			<hr />
		</c:forEach>
	
		<a href="javascript:hideFullSectors()" style="float:right;">View Top List</a>
	</div>
	</fieldset>	

	</div>
	</td>
	<td width=15>&nbsp;</td>
    <td width=689 valign="top">
	<table width="689" border="0" cellspacing="0" cellpadding="0" align="center">
<tr>
<td valign="top">
<div id="demo" class="yui-navset">
	<ul class="yui-nav">
		<li><a href="#tab1"><div>Visualization</div></a></li>
		<li><a href="#tab2"><div>Contact Information</div></a></li>
		<li><a href="#tab3"><div>Additional Notes</div></a></li>
	</ul>
	<div class="yui-content">
	<div id="tab1">
		<fieldset>
			<legend><span class=legend_label>Commitments, Disbursements, Expenditures, Pledges</span></legend>
			<div class="dash_graph_opt"><a onclick="changeChart(event, 'bar', 'FundingChart')" class="sel_sm_b">Bar Chart</a><span class="breadcrump_sep">|</span><a onclick="changeChart(event, 'donut', 'FundingChart')">Donut</a><span class="breadcrump_sep">|</span><a onclick="changeChart(event, 'line', 'FundingChart')">Line Chart</a><span class="breadcrump_sep">|</span><a onclick="changeChart(event, 'dataview', 'FundingChart')">Data View</a></div>
			<br />
			<div class="flashcontent" name="flashContent">
				<div id="FundingChart">
					<a href="http://www.adobe.com/go/getflashplayer">
						<img src="http://www.adobe.com/images/shared/download_buttons/get_flash_player.gif" alt="Get Adobe Flash player" />
					</a>
				</div>
			</div>
		</fieldset>
		<fieldset>
			<legend><span class=legend_label>Aid Predictability</span></legend>
			<div class="dash_graph_opt"><a onclick="changeChart(event, 'bar', 'AidPredictability')" class="sel_sm_b">Bar Chart</a><span class="breadcrump_sep">|</span><a onclick="changeChart(event, 'donut', 'AidPredictability')">Donut</a><span class="breadcrump_sep">|</span><a onclick="changeChart(event, 'line', 'AidPredictability')">Line Chart</a><span class="breadcrump_sep">|</span><a onclick="changeChart(event, 'dataview', 'AidPredictability')">Data View</a></div>
			<br />
			<div class="flashcontent" name="flashContent">
				<div id="AidPredictability">
					<a href="http://www.adobe.com/go/getflashplayer">
						<img src="http://www.adobe.com/images/shared/download_buttons/get_flash_player.gif" alt="Get Adobe Flash player" />
					</a>
				</div>
			</div>
		</fieldset>
		<fieldset>
			<legend><span class=legend_label>Aid Type</span></legend>
			<div class="dash_graph_opt"><a onclick="changeChart(event, 'bar', 'AidType')" class="sel_sm_b">Bar Chart</a><span class="breadcrump_sep">|</span><a onclick="changeChart(event, 'donut', 'AidType')">Donut</a><span class="breadcrump_sep">|</span><a onclick="changeChart(event, 'line', 'AidType')">Line Chart</a><span class="breadcrump_sep">|</span><a onclick="changeChart(event, 'dataview', 'AidType')">Data View</a></div>
			<br />
			<div class="flashcontent" name="flashContent">
				<div id="AidType">
					<a href="http://www.adobe.com/go/getflashplayer">
						<img src="http://www.adobe.com/images/shared/download_buttons/get_flash_player.gif" alt="Get Adobe Flash player" />
					</a>
				</div>
			</div>
		</fieldset>
		<fieldset>
			<legend><span class=legend_label>Financing Instrument</span></legend>
			<div class="dash_graph_opt"><a onclick="changeChart(event, 'bar', 'FinancingInstrument')" class="sel_sm_b">Bar Chart</a><span class="breadcrump_sep">|</span><a onclick="changeChart(event, 'donut', 'FinancingInstrument')">Donut</a><span class="breadcrump_sep">|</span><a onclick="changeChart(event, 'line', 'FinancingInstrument')">Line Chart</a><span class="breadcrump_sep">|</span><a onclick="changeChart(event, 'dataview', 'FinancingInstrument')">Data View</a></div>
			<br />
			<div class="flashcontent" name="flashContent">
				<div id="FinancingInstrument">
					<a href="http://www.adobe.com/go/getflashplayer">
						<img src="http://www.adobe.com/images/shared/download_buttons/get_flash_player.gif" alt="Get Adobe Flash player" />
					</a>
				</div>
			</div>
		</fieldset>
		<fieldset>
			<legend><span class=legend_label>Sector Profile</span></legend>
			<div class="dash_graph_opt"><a onclick="changeChart(event, 'bar', 'SectorProfile')" class="sel_sm_b">Bar Chart</a><span class="breadcrump_sep">|</span><a onclick="changeChart(event, 'donut', 'SectorProfile')">Donut</a><span class="breadcrump_sep">|</span><a onclick="changeChart(event, 'line', 'SectorProfile')">Line Chart</a><span class="breadcrump_sep">|</span><a onclick="changeChart(event, 'dataview', 'SectorProfile')">Data View</a></div>
			<br />
			<div class="flashcontent" name="flashContent">
				<div id="SectorProfile">
					<a href="http://www.adobe.com/go/getflashplayer">
						<img src="http://www.adobe.com/images/shared/download_buttons/get_flash_player.gif" alt="Get Adobe Flash player" />
					</a>
				</div>
			</div>
		</fieldset>
		<fieldset>
			<legend><span class=legend_label>Donor Profile</span></legend>
			<div class="dash_graph_opt"><a onclick="changeChart(event, 'bar', 'DonorProfile')" class="sel_sm_b">Bar Chart</a><span class="breadcrump_sep">|</span><a onclick="changeChart(event, 'donut', 'DonorProfile')">Donut</a><span class="breadcrump_sep">|</span><a onclick="changeChart(event, 'line', 'DonorProfile')">Line Chart</a><span class="breadcrump_sep">|</span><a onclick="changeChart(event, 'dataview', 'DonorProfile')">Data View</a></div>
			<br />
			<div class="flashcontent" name="flashContent">
				<div id="DonorProfile">
					<a href="http://www.adobe.com/go/getflashplayer">
						<img src="http://www.adobe.com/images/shared/download_buttons/get_flash_player.gif" alt="Get Adobe Flash player" />
					</a>
				</div>
			</div>
		</fieldset>
	</div>
	<div id="tab2">
		Morbi tincidunt, dui sit amet facilisis feugiat, odio metus gravida ante, ut pharetra massa metus id nunc. Duis scelerisque molestie turpis. Sed fringilla, massa eget luctus malesuada, metus eros molestie lectus, ut tempus eros massa ut dolor. Aenean aliquet fringilla sem. Suspendisse sed ligula in ligula suscipit aliquam. Praesent in eros vestibulum mi adipiscing adipiscing. Morbi facilisis. Curabitur ornare consequat nunc. Aenean vel metus. Ut posuere viverra nulla. Aliquam erat volutpat. Pellentesque convallis. Maecenas feugiat, tellus pellentesque pretium posuere, felis lorem euismod felis, eu ornare leo nisi vel felis. Mauris consectetur tortor et purus.
	</div>
	<div id="tab3">
		Mauris eleifend est et turpis. Duis id erat. Suspendisse potenti. Aliquam vulputate, pede vel vehicula accumsan, mi neque rutrum erat, eu congue orci lorem eget lorem. Vestibulum non ante. Class aptent taciti sociosqu ad litora torquent per conubia nostra, per inceptos himenaeos. Fusce sodales. Quisque eu urna vel enim commodo pellentesque. Praesent eu risus hendrerit ligula tempus pretium. Curabitur lorem enim, pretium nec, feugiat nec, luctus a, lacus.
		<p>Duis cursus. Maecenas ligula eros, blandit nec, pharetra at, semper at, magna. Nullam ac lacus. Nulla facilisi. Praesent viverra justo vitae neque. Praesent blandit adipiscing velit. Suspendisse potenti. Donec mattis, pede vel pharetra blandit, magna ligula faucibus eros, id euismod lacus dolor eget odio. Nam scelerisque. Donec non libero sed nulla mattis commodo. Ut sagittis. Donec nisi lectus, feugiat porttitor, tempor ac, tempor vitae, pede. Aenean vehicula velit eu tellus interdum rutrum. Maecenas commodo. Pellentesque nec elit. Fusce in lacus. Vivamus a libero vitae lectus hendrerit hendrerit.</p>
	</div>
	</div>
</div>

</td>
</tr>
</table>
</td>
</tr>
</table>

</digi:form>

<script type="text/javascript">
<!--

//Filter Javascript
//Attach event to reload organizations from organization group
//Attach event to reload zones from regions
var callbackChildrenCall = {
	  success: function(o) {
		  try {
			    var results = YAHOO.lang.JSON.parse(o.responseText);
			    switch(results.objectType)
			    {
				    case "Organization":
			    		var orgDropdown = document.getElementById("org_dropdown_id");
			    		orgDropdown.options.length = 0;
			    		orgDropdown.options[0] = new Option("All", -1);
			    		for(var i = 0; i < results.children.length; i++){
			    			orgDropdown.options[orgDropdown.options.length] = new Option(results.children[i].name, results.children[i].ID);
			    		}
			    		break;
				    case "Sector":
			    		var subSectorDropdown = document.getElementById("sub_sector_dropdown_id");
			    		subSectorDropdown.options.length = 0;
			    		subSectorDropdown.options[0] = new Option("All", -1);
			    		for(var i = 1; i < results.children.length; i++){
			    			subSectorDropdown.options[i] = new Option(results.children[i].name, results.children[i].ID);
			    		}
			    		break;
			    	case "Region":
			    		var zonesDropdown = document.getElementById("zone_dropdown_id");
			    		zonesDropdown.options.length = 0;
			    		zonesDropdown.options[0] = new Option("All", -1);
			    		for(var i = 1; i < results.children.length; i++){
			    			zonesDropdown.options[i] = new Option(results.children[i].name, results.children[i].ID);
			    		}
			    		break;
			    
			    }
			}
			catch (e) {
			    alert("Invalid respose.");
			}
	  },
	  failure: function(o) {//Fail silently
		  }
	};

function callbackChildren(e) {
	var parentId, targetId;
	if (e == undefined){
		parentId = this.value;
		targetId = this.id;
	}
	else
	{
		parentId = e.target.value;
		targetId = e.target.id;
	}
	
	var objectType = "";

	switch(targetId){
		case "sector_dropdown_id":
			objectType = "Sector";
			break;
		case "region_dropdown_id":
			objectType = "Region";
			break;
		case "org_group_dropdown_id":
			objectType = "Organization";
			break;
	}

	if (parentId != "" && objectType != ""){
		var transaction = YAHOO.util.Connect.asyncRequest('GET', "/visualization/dataDispatcher.do?action=getJSONObject&objectType=" + objectType + "&parentId=" + parentId, callbackChildrenCall, null);
	}
}
var callbackApplyFilterCall = {
		  success: function(o) {
			  try {
				  refreshBoxes(o);
				  refreshGraphs();
				}
				catch (e) {
				    //alert("Invalid response.");
				}
				//Get array of graphs
				var allGraphs = document.getElementsByName("flashContent");
				
				//Iterate and refresh the graph
				for(var idx = 0; idx < allGraphs.length; idx++){
					// Get flash object and refresh it by calling internal
					allGraphs[idx].style.display = "";
				}
			  loadingPanel.hide();
		  },
		  failure: function(o) {
			  loadingPanel.hide();
		  }
		};

function callbackApplyFilter(e){
	//Get array of graphs
	var allGraphs = document.getElementsByName("flashContent");
	
	//Iterate and refresh the graph
	for(var idx = 0; idx < allGraphs.length; idx++){
		// Get flash object and refresh it by calling internal
		allGraphs[idx].style.display = "none";
	}
	loadingPanel.show();
	YAHOO.util.Connect.setForm('visualizationform');
/*	var orgIds = null;
	var secIds = null;
//	if (document.getElementById("org_dropdown_id").value != -1) {
		orgIds = document.getElementById("org_dropdown_id").value;
//	}
	if (document.getElementById("sub_sector_dropdown_id").value == -1) {
		if (document.getElementById("sector_dropdown_id").value != -1) {
			secIds = document.getElementById("sector_dropdown_id").value;
		}
	} else {
		secIds = document.getElementById("sub_sector_dropdown_id").value;
	}*/
	//var sUrl="/visualization/filters.do?orgIds="+orgIds+"&secIds="+secIds;
	var sUrl="/visualization/dataDispatcher.do?action=applyFilter";

	var cObj = YAHOO.util.Connect.asyncRequest('POST', sUrl, callbackApplyFilterCall);
}

function refreshGraphs(){

	//Get array of graphs
	var allGraphs = document.getElementsByName("flashContent");
	
	//Iterate and refresh the graph
	for(var idx = 0; idx < allGraphs.length; idx++){
		// Get flash object and refresh it by calling internal
		allGraphs[idx].children[0].refreshGraph();
	}
}
function refreshBoxes(o){

	var results = YAHOO.lang.JSON.parse(o.responseText);
	var inner = "";
	var trnTotalDisbs="<digi:trn jsFriendly='true'>Total Disbursements</digi:trn>: ";
	var trnNumOfProjs="<digi:trn jsFriendly='true'>Total Number of Projects</digi:trn>: ";
	var trnNumOfDons="<digi:trn jsFriendly='true'>Total Number of Donors</digi:trn>: ";
	var trnNumOfSecs="<digi:trn jsFriendly='true'>Total Number of Sectors</digi:trn>: ";
	var trnAvgProjSize="<digi:trn jsFriendly='true'>Average Project Size</digi:trn>: ";
	var valTotalDisbs="";
	var valNumOfProjs="";
	var valNumOfDons="";
	var valNumOfSecs="";
	var valAvgProjSize="";
	
	for(var j = 0; j < results.children.length; j++){
		var child = results.children[j];
		switch(child.type){
			case "ProjectsList":
				for(var i = 0; i < child.list.length; i++){
					inner = inner + (i+1) + ". " + child.list[i].name + "  <b>" + child.list[i].value + "</b> <hr />";
				}
				inner = inner + "<a href='javascript:hideFullProjects()' style='float:right;'>Hide Full List</a>";
				var div = document.getElementById("divFullProjects");
				div.innerHTML = inner;
				inner = "";
				for(var i = 0; i < child.top.length; i++){
					inner = inner + (i+1) + ". " + child.top[i].name + "  <b>" + child.top[i].value + "</b> <hr />";
				}
				inner = inner + "<a href='javascript:showFullProjects()' style='float:right;'>Show Full List</a>";
				var div = document.getElementById("divTopProjects");
				div.innerHTML = inner;
				break;
			case "DonorsList":
				inner = "";
				for(var i = 0; i < child.list.length; i++){
					inner = inner + (i+1) + ". " + child.list[i].name + "  <b>" + child.list[i].value + "</b> <hr />";
				}
				inner = inner + "<a href='javascript:hideFullDonors()' style='float:right;'>Hide Full List</a>";
				var div = document.getElementById("divFullDonors");
				div.innerHTML = inner;
				inner = "";
				for(var i = 0; i < child.top.length; i++){
					inner = inner + (i+1) + ". " + child.top[i].name + "  <b>" + child.top[i].value + "</b> <hr />";
				}
				inner = inner + "<a href='javascript:showFullDonors()' style='float:right;'>Show Full List</a>";
				var div = document.getElementById("divTopDonors");
				div.innerHTML = inner;
				break;
			case "SectorsList":
				inner = "";
				for(var i = 0; i < child.list.length; i++){
					inner = inner + (i+1) + ". " + child.list[i].name + "  <b>" + child.list[i].value + "</b> <hr />";
				}
				inner = inner + "<a href='javascript:hideFullSectors()' style='float:right;'>Hide Full List</a>";
				var div = document.getElementById("divFullSectors");
				div.innerHTML = inner;
				inner = "";
				for(var i = 0; i < child.top.length; i++){
					inner = inner + (i+1) + ". " + child.top[i].name + "  <b>" + child.top[i].value + "</b> <hr />";
				}
				inner = inner + "<a href='javascript:showFullSectors()' style='float:right;'>Show Full List</a>";
				var div = document.getElementById("divTopSectors");
				div.innerHTML = inner;
				break;
			case "TotalComms":
				inner = "<b class='dashboard_total_num'>" + child.value + "</b>";
				var div = document.getElementById("divTotalComms");
				div.innerHTML = inner;
				break;
			case "TotalDisbs":
				valTotalDisbs = child.value;
				break;
			case "NumberOfProjs":
				valNumOfProjs = child.value;
				break;
			case "NumberOfDons":
				valNumOfDons = child.value;
				break;
			case "NumberOfSecs":
				valNumOfSecs = child.value;
				break;
			case "AvgProjSize":
				valAvgProjSize = child.value;
				break;
				
		}
		inner = trnTotalDisbs + "<b>" + valTotalDisbs + "</b><span class='breadcrump_sep'>|</span>";
		inner = inner + trnNumOfProjs + "<b>" + valNumOfProjs + "</b><span class='breadcrump_sep'>|</span>";
		inner = inner + trnNumOfDons + "<b>" + valNumOfDons + "</b><span class='breadcrump_sep'>|</span>";
		inner = inner + trnNumOfSecs + "<b>" + valNumOfSecs + "</b><span class='breadcrump_sep'>|</span>";
		inner = inner + trnAvgProjSize + "<b>" + valAvgProjSize;
		var div = document.getElementById("divSummaryInfo");
		div.innerHTML = inner;
		
		var regionPlaceholder = document.getElementById("region_name");
		
		var regionName = document.getElementById("region_dropdown_id").options[document.getElementById("region_dropdown_id").selectedIndex].text;
		var zoneName = document.getElementById("zone_dropdown_id").options[document.getElementById("zone_dropdown_id").selectedIndex].text;
		regionPlaceholder.innerHTML = regionName + "<br/><span style=\"font-size:16px\">" + zoneName + "</span>";
		
	}
	
}


YAHOO.util.Event.addListener("region_dropdown_id", "change", callbackChildren);
YAHOO.util.Event.onAvailable("region_dropdown_id", callbackChildren);
YAHOO.util.Event.addListener("org_group_dropdown_id", "change", callbackChildren);
YAHOO.util.Event.onAvailable("org_group_dropdown_id", callbackChildren);
YAHOO.util.Event.addListener("sector_dropdown_id", "change", callbackChildren);
YAHOO.util.Event.onAvailable("sector_dropdown_id", callbackChildren);
YAHOO.util.Event.addListener("applyButton", "click", callbackApplyFilter);
YAHOO.util.Event.onDOMReady(initDashboard);

function initDashboard(){
	//Initialize First Chart
	changeChart(null, 'bar', 'FundingChart');
	changeChart(null, 'bar', 'AidPredictability');
	changeChart(null, 'bar', 'AidType');
	changeChart(null, 'bar', 'FinancingInstrument');
	changeChart(null, 'bar', 'SectorProfile');
	changeChart(null, 'bar', 'DonorProfile');
	callbackApplyFilter();
}

function changeChart(e, chartType, container){
	//Get the calling object to select it and remove style.
	if(e != null){
		var caller = e.target || e.srcElement;
		var linkBar = caller.parentNode.getElementsByTagName("A");
	    for(var i in linkBar)
	    {
	    	linkBar[i].className = "";
	    }
	    caller.className = "sel_sm_b";
	}
	var currentYear = document.getElementById("currentYear").value;
	var yearsInRange = document.getElementById("yearsInRange").value;
	var minSlider =  "" + (currentYear - yearsInRange + 1);
	var maxSlider =  "" + currentYear;
	var flashvars = { minSlider: minSlider, maxSlider: maxSlider};
	//var flashvars = {};
	var params = {};
	var attributes = {};
	attributes.id = container;
	switch(chartType){
		case "bar":
			swfobject.embedSWF("/repository/visualization/view/charts/BarChartSeries_" + container + ".swf", container, "634", "350", "10.0.0", false, flashvars, params, attributes);
			break;
		case "donut":
			swfobject.embedSWF("/repository/visualization/view/charts/PieChart_" + container + ".swf", container, "634", "350", "10.0.0", false, flashvars, params, attributes);
			break;
		case "line":
			swfobject.embedSWF("/repository/visualization/view/charts/LineAreaSeries_" + container + ".swf", container, "634", "350", "10.0.0", false, flashvars, params, attributes);
			break;
		case "dataview":
			swfobject.embedSWF("/repository/visualization/view/charts/DataViewSeries_" + container + ".swf", container, "634", "350", "10.0.0", false, flashvars, params, attributes);
			break;
	}
	return false;
}

var myTabs = new YAHOO.widget.TabView("demo");
myTabs.selectTab(0);
var loadingPanel = new yuiLoadingPanel();

function showFullProjects(){
	var divFull = document.getElementById("divFullProjects");
	var divTop = document.getElementById("divTopProjects");
	divFull.style.display = "";
	divTop.style.display = "none";
}

function hideFullProjects(){
	var divFull = document.getElementById("divFullProjects");
	var divTop = document.getElementById("divTopProjects");
	divFull.style.display = "none";
	divTop.style.display = "";
}

function showFullDonors(){
	var divFull = document.getElementById("divFullDonors");
	var divTop = document.getElementById("divTopDonors");
	divFull.style.display = "";
	divTop.style.display = "none";
}

function hideFullDonors(){
	var divFull = document.getElementById("divFullDonors");
	var divTop = document.getElementById("divTopDonors");
	divFull.style.display = "none";
	divTop.style.display = "";
}

function showFullSectors(){
	var divFull = document.getElementById("divFullSectors");
	var divTop = document.getElementById("divTopSectors");
	divFull.style.display = "";
	divTop.style.display = "none";
}

function hideFullSectors(){
	var divFull = document.getElementById("divFullSectors");
	var divTop = document.getElementById("divTopSectors");
	divFull.style.display = "none";
	divTop.style.display = "";
}

//-->
</script>

