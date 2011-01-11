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
	}
</style>
<!-- Visualization's Scripts-->
<script type="text/javascript" src="/TEMPLATE/ampTemplate/js_2/yui/json/json-min.js"></script> 
<script type="text/javascript" src="/TEMPLATE/ampTemplate/js_2/yui/element/element-min.js"></script> 
<script type="text/javascript" src="/TEMPLATE/ampTemplate/js_2/yui/tabview/tabview-min.js"></script> 
<script type="text/javascript" src="/TEMPLATE/ampTemplate/js_2/flash/AC_OETags.js"></script>

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
<span class="sec_name"><digi:trn>Donor Profile Dashboard</digi:trn></span><span class="breadcrump_sep">|</span><a href=# class="l_sm"><digi:trn>Dashboards</digi:trn></a><span class="breadcrump_sep"><b>»</b></span><span class="bread_sel"><digi:trn>Donor Profile Dashboard</digi:trn></span>
</div>
<br/>
<!-- BREADCRUMB END -->
<!-- MAIN CONTENT PART START -->
<div class="dashboard_header">
<digi:form action="/filters.do">
<div class="dashboard_total"><b class="dashboard_total_num">${visualizationform.summaryInformation.totalCommitments}</b><br /><digi:trn>Total Commitments</digi:trn> ( ${visualizationform.filter.currencyId} )</div>
<table border="0" cellspacing="0" cellpadding="0">
  <tr>
    <td>
    <div class="dashboard_name">
    	<c:if test="${visualizationform.filter.organizationGroupId eq '-1' }">
	    	<digi:trn>ALL DONORS</digi:trn>
    	</c:if>
    	<c:if test="${visualizationform.filter.organizationGroupId ne '-1' }">
			<c:forEach var="organizationGroup" items="${visualizationform.filter.orgGroups}">
			<c:if test="${organizationGroup.ampOrgGrpId eq visualizationform.filter.organizationGroupId}">
				${organizationGroup}
			</c:if>
			</c:forEach>
    	</c:if>
    </div>
    </td>
    <td><div class="dash_ico"><img src="images/ico_pdf.gif" align=left style="margin-right:5px;"> <div class="dash_ico_link"><a href=# class=l_sm>Export to PDF</a></div></div> <div class="dash_ico"><img src="images/ico_word_1.gif" align=left style="margin-right:5px;"> <div class="dash_ico_link"><a href=# class=l_sm>Export to DOC</a></div></div> <div class="dash_ico"><img src="images/ico_export.gif" align=left style="margin-right:5px;"> <div class="dash_ico_link"><a href=# class=l_sm>Export Options</a></div></div></td>
  </tr>
</table>
<div class="dashboard_stat">Total Disbursements: <b>${visualizationform.summaryInformation.totalDisbursements}</b><span class="breadcrump_sep">|</span>Total Number of Projects: <b>${visualizationform.summaryInformation.numberOfProjects}</b><span class="breadcrump_sep">|</span>Total Number of Sectors: <b>${visualizationform.summaryInformation.numberOfSectors}</b><span class="breadcrump_sep">|</span>Total Number of Regions: <b>${visualizationform.summaryInformation.numberOfRegions}</b><span class="breadcrump_sep">|</span>Average Project Size: <b>${visualizationform.summaryInformation.averageProjectSize}</b></div>
</div>
<table width="1000" border="0" cellspacing="0" cellpadding="0" align=center style="margin-top:15px;">
  <tr>
    <td width=296 bgcolor="#F4F4F4" valign=top>
	<div style="background-color:#FFFFFF; height:7px;">&nbsp;</div>
	<div class="dash_left">
	<fieldset>
	<legend><span class=legend_label>Quick Filter</span></legend>
	<html:checkbox property="filter.workspaceOnly">Show Data from All Workspaces</html:checkbox>
	<hr />
	<table cellspacing="0" cellpadding="0" width=100%> 
  <tr>
    <td>Organization Group:</td>
    <td align=right>
       <html:select property="filter.organizationGroupId" styleId="org_group_dropdown_id" styleClass="dropdwn_sm" style="width:145px;">
           <html:option value="-1"><digi:trn>All</digi:trn></html:option>
           <html:optionsCollection property="filter.orgGroups" value="ampOrgGrpId" label="orgGrpName" />
       </html:select>
	</td>
  </tr>
   <tr>
    <td>Organization:</td>
    <td align=right>
    <select class="dropdwn_sm" style="width:145px;" name="org_dropdown_id" id="org_dropdown_id">
		<option value="All">All</option>
	</select>
	</td>
  </tr>
   <tr>
    <td><digi:trn>Region</digi:trn>:</td>
    <td align=right>
       <html:select property="filter.selRegionId" styleId="region_dropdown_id" styleClass="dropdwn_sm" style="width:145px;">
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
		   <select class="dropdwn_sm" style="width:145px;" name="funding.proProjCost.currencyCode">
			<option value="All">All</option>
		</select>
		</td>
  </tr>
  <tr>
    <td><digi:trn>Sub-Sector</digi:trn>:</td>
    <td align=right>
    	<select class="dropdwn_sm" style="width:145px;" name="funding.proProjCost.currencyCode">
			<option value="All">All</option>
		</select>
	</td>
  </tr>
 </table>

	<center>
	<input type="button" value="Filter" class="buttonx" style="margin-top:10px;" id="applyButton">
	</center>
	</fieldset>
	<fieldset>
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
	<fieldset>
	<legend><span class=legend_label>Top Projects</span></legend>
	<div class="field_text">
1. Project one <b>($25,1)</b>
<hr />
2. Project two <b>($21)</b>
<hr />
3. Project three <b>($19,3)</b>
<hr />
4. Project four <b>($18,2)</b>
<hr />
5. Project five <b>($15,2)</b>
<hr />
<a href=# style="float:right;">View Full List</a>
</div>
</fieldset>	
	<fieldset>
	<legend><span class=legend_label>Top Sectors</span></legend>
	<div class="field_text">
1. Sector one <b>($25,1)</b>
<hr />
2. Sector two <b>($21)</b>
<hr />
3. Sector three <b>($19,3)</b>
<hr />
4. Sector four <b>($18,2)</b>
<hr />
5. Sector five <b>($15,2)</b>
<hr />
<a href=# style="float:right;">View Full List</a>
</div>
</fieldset>	
	<fieldset>
	<legend><span class=legend_label>Top Regions</span></legend>
	<div class="field_text">
1. Region one <b>($25,1)</b>
<hr />
2. Region two <b>($21)</b>
<hr />
3. Region three <b>($19,3)</b>
<hr />
4. Region four <b>($18,2)</b>
<hr />
5. Region five <b>($15,2)</b>
<hr /><a href=# style="float:right;">View Full List</a>
</div>
</fieldset>	
	<fieldset>
	<legend><span class=legend_label>Sector Working Groups</span></legend>
	<div class="field_text">
1. Agriculture
<hr />
2. Energy
<hr />
3. Foresty
</div>
</fieldset>	
	</div>
	</td>
	<td width=15>&nbsp;</td>
    <td width=689 valign=top>
	<table width="689" border="0" cellspacing="0" cellpadding="0" align=center>
<tr>
<td valign=top>
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
			<div class="dash_graph_opt"><b class=sel_sm>Bar Chart</b></a><span class="breadcrump_sep">|</span><a href=#>Donut</a><span class="breadcrump_sep">|</span><a href=#>Line Chart</a><span class="breadcrump_sep">|</span><a href=#>Data View</a></div>
			<br />
			<div class="flashcontent" name="flashContent">
				<script language="JavaScript" type="text/javascript">
				<!--
						AC_FL_RunContent(
									"src", "/repository/visualization/view/charts/LineAreaSeries",
									"width", "634",
									"height", "350",
									"align", "middle",
									"id", "LineAreaSeries",
									"quality", "high",
									"bgcolor", "#869ca7",
									"name", "LineAreaSeries",
									"allowScriptAccess","sameDomain",
									"type", "application/x-shockwave-flash",
									"pluginspage", "http://www.adobe.com/go/getflashplayer"
					);
				// -->
				</script>
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
			    		for(var i = 0; i < results.children.length; i++){
			    			orgDropdown.options[i] = new Option(results.children[i].name, results.children[i].ID);
			    		}
			    		break;
			    	case "Region":
			    		break;
			    
			    }
			}
			catch (e) {
			    alert("Invalid respose.");
			}
	  },
	  failure: function(o) {alert("fasha");}
	};

function callbackChildren(e) {
	var parentId = e.target.value;
	var objectType = "";

	switch(e.target.id){
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
var callbackApplyCall = {
		  success: function(o) {
			  try {
				  refreshGraphs();
				  refreshBoxes();
				}
				catch (e) {
				    alert("Invalid response.");
				}
		  },
		  failure: function(o) {alert("fasha");}
		};

function callbackApply(e){
	YAHOO.util.Connect.setForm('visualizationform');
	var cObj = YAHOO.util.Connect.asyncRequest('POST', '/visualization/dataDispatcher.do?action=applyFilter', callbackApplyCall);
}

function refreshGraphs(){
	loadingPanel.show();

	//Get array of graphs
	var allGraphs = document.getElementsByName("flashcontent");
	
	//Iterate and refresh the source and graph
	for(var idx = 0; idx < allGraphs.length; idx++){
		allGraphs[idx];
	}
	loadingPanel.hide();
	
	
}
function refreshBoxes(){
	loadingPanel.show();
	
	loadingPanel.hide();
}


YAHOO.util.Event.addListener("org_group_dropdown_id", "change", callbackChildren);
YAHOO.util.Event.addListener("region_dropdown_id", "change", callbackChildren);
YAHOO.util.Event.addListener("org_group_dropdown_id", "change", callbackChildren);
YAHOO.util.Event.addListener("applyButton", "click", callbackApply);

var myTabs = new YAHOO.widget.TabView("demo");
myTabs.selectTab(0);
var loadingPanel = new yuiLoadingPanel();

//-->
</script>

