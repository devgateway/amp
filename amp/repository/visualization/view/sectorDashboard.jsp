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

<!-- BREADCRUMB START -->
<div class="breadcrump">
<div class="centering">
<div class="breadcrump_cont">
<span class="sec_name">Sector Profile Dashboard</span><span class="breadcrump_sep">|</span><a href=# class="l_sm">Dashboards</a><span class="breadcrump_sep"><b>»</b></span><span class="bread_sel">Sector Profile Dashboard</span></div>
</div>
</div>
<!-- BREADCRUMB END -->

<!-- MAIN CONTENT PART START -->
<div class="dashboard_header">
<div class="dashboard_total"><b class="dashboard_total_num">$ 55,789,889,921</b><br />Total Commitment (USD)</div>
<table border="0" cellspacing="0" cellpadding="0">
  <tr>
    <td><div class="dashboard_name">DONOR PROFILE</div></td>
    <td><div class="dash_ico"><img src="images/ico_pdf.gif" align=left style="margin-right:5px;"> <div class="dash_ico_link"><a href=# class=l_sm>Export to PDF</a></div></div> <div class="dash_ico"><img src="images/ico_word_1.gif" align=left style="margin-right:5px;"> <div class="dash_ico_link"><a href=# class=l_sm>Export to DOC</a></div></div> <div class="dash_ico"><img src="images/ico_export.gif" align=left style="margin-right:5px;"> <div class="dash_ico_link"><a href=# class=l_sm>Export Options</a></div></div></td>
  </tr>
</table>
<div class="dashboard_stat">Total Distributement: <b>$300M</b><span class="breadcrump_sep">|</span>Total Number of Projects: <b>56</b><span class="breadcrump_sep">|</span>Total Number of Sectors: <b>36</b><span class="breadcrump_sep">|</span>Total Number of Regions: <b>16</b><span class="breadcrump_sep">|</span>Average Project Size: <b>$25M</b></div>
</div>
<digi:form action="/applyFilters.do">

</digi:form>


<table width="1000" border="0" cellspacing="0" cellpadding="0" align=center style="margin-top:15px;">
  <tr>
    <td width=296 bgcolor="#F4F4F4" valign=top>
	<div style="background-color:#FFFFFF; height:7px;">&nbsp;</div>
	<div class="dash_left">
	<fieldset>
	<legend><span class=legend_label>Quick Filter</span></legend>
	<input name="" type="checkbox" value="" /> Show Data from All Workspaces
	<hr />
	<table cellspacing="0" cellpadding="0" width=100%> 
  <tr>
    <td>Organization Group:</td>
    <td align=right><select class="dropdwn_sm" style="width:145px;" name="funding.proProjCost.currencyCode">
	<option value="All">All</option>
</select></td>
  </tr>
   <tr>
    <td>Organization:</td>
    <td align=right><select class="dropdwn_sm" style="width:145px;" name="funding.proProjCost.currencyCode">
	<option value="All">All</option>
</select></td>
  </tr>
   <tr>
    <td>Region:</td>
    <td align=right><select class="dropdwn_sm" style="width:145px;" name="funding.proProjCost.currencyCode">
	<option value="All">All</option>
</select></td>
  </tr> 
    <tr>
    <td>Zone:</td>
    <td align=right><select class="dropdwn_sm" style="width:145px;" name="funding.proProjCost.currencyCode">
	<option value="All">All</option>
</select></td>
  </tr>
    <tr>
    <td>District:</td>
    <td align=right><select class="dropdwn_sm" style="width:145px;" name="funding.proProjCost.currencyCode">
	<option value="All">All</option>
</select></td>
  </tr>
     <tr>
    <td>Sub-Sectror:</td>
    <td align=right><select class="dropdwn_sm" style="width:145px;" name="funding.proProjCost.currencyCode">
	<option value="All">All</option>
</select></td>
  </tr>
      <tr>
    <td>Sub-Sub-Sector:</td>
    <td align=right><select class="dropdwn_sm" style="width:145px;" name="funding.proProjCost.currencyCode">
	<option value="All">All</option>
</select></td>
  </tr>
 </table>
<a href=# style="float:right;">Hide Advance Settings</a>
	<table cellspacing="0" cellpadding="0" width=100%> 
  <tr>
    <td>Fiscal year start:</td>
    <td align=right><select class="dropdwn_sm" style="width:145px;" name="funding.proProjCost.currencyCode">
	<option value="All">Choose</option>
</select></td>
  </tr>
   <tr>
    <td>Time scale:</td>
    <td align=right><select class="dropdwn_sm" style="width:145px;" name="funding.proProjCost.currencyCode">
	<option value="All">Choose</option>
</select></td>
  </tr>
   <tr>
    <td>Currency type:</td>
    <td align=right><select class="dropdwn_sm" style="width:145px;" name="funding.proProjCost.currencyCode">
	<option value="All">Choose</option>
</select></td>
  </tr> 
    <tr>
    <td>Fiscal calendar:</td>
    <td align=right><select class="dropdwn_sm" style="width:145px;" name="funding.proProjCost.currencyCode">
	<option value="All">Choose</option>
</select></td>
  </tr>
    <tr>
    <td>Show decimals:</td>
    <td><input name="" type="checkbox" value="" style="margin-left:18px;"></td>
  </tr>
 </table>
<center><input type="button" value="Filter" class="buttonx" style="margin-top:10px;"></center>
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
<div id="tabs">
	<ul class="desktop_tab_base">
		<li class="desktop_tab"><a href="#tabs-1" class="tab_link">Visualization</a></li>
		<li class="desktop_tab"><a href="#tabs-2" class="tab_link">Contact Information</a></li>
		<li class="desktop_tab"><a href="#tabs-3" class="tab_link">Additional Notes</a></li>
	</ul>
	<div id="tabs-1">
	<div class="fld_7"></div>
	<fieldset>
	<legend><span class=legend_label>Commitements, Distributements,  Expenditures, Pledges (since 1980)</span></legend>
	<div class="dash_graph_opt"><b class=sel_sm>Bar Chart</b></a><span class="breadcrump_sep">|</span><a href=#>Donut</a><span class="breadcrump_sep">|</span><a href=#>Line Chart</a><span class="breadcrump_sep">|</span><a href=#>Data View</a></div>
	<br />
	<img src="images/graph.gif" />
</fieldset>
	<fieldset>
	<legend><span class=legend_label>Aid Predictability (Actual vs Planned Commitements)</span></legend>
	<div class="dash_graph_opt"><b class=sel_sm>Bar Chart</b></a><span class="breadcrump_sep">|</span><a href=#>Donut</a><span class="breadcrump_sep">|</span><a href=#>Line Chart</a><span class="breadcrump_sep">|</span><a href=#>Data View</a></div>
	<br />
	<img src="images/graph.gif" />
</fieldset>
	<fieldset>
	<legend><span class=legend_label>Aid Predictability (Actual vs Planned Distributements)</span></legend>
	<div class="dash_graph_opt"><b class=sel_sm>Bar Chart</b></a><span class="breadcrump_sep">|</span><a href=#>Donut</a><span class="breadcrump_sep">|</span><a href=#>Line Chart</a><span class="breadcrump_sep">|</span><a href=#>Data View</a></div>
	<br />
	<img src="images/graph.gif" />
</fieldset>
	<fieldset>
	<legend><span class=legend_label>Aid Type</span></legend>
	<div class="dash_graph_opt"><b class=sel_sm>Bar Chart</b></a><span class="breadcrump_sep">|</span><a href=#>Donut</a><span class="breadcrump_sep">|</span><a href=#>Line Chart</a><span class="breadcrump_sep">|</span><a href=#>Data View</a></div>
	<br />
	<img src="images/graph.gif" />
</fieldset>
	<fieldset>
	<legend><span class=legend_label>Region</span></legend>
	<div class="dash_graph_opt"><b class=sel_sm>Bar Chart</b></a><span class="breadcrump_sep">|</span><a href=#>Donut</a><span class="breadcrump_sep">|</span><a href=#>Line Chart</a><span class="breadcrump_sep">|</span><a href=#>Data View</a></div>
	<br />
	<img src="images/graph.gif" />
</fieldset>
	<fieldset>
	<legend><span class=legend_label>Modality</span></legend>
	<div class="dash_graph_opt"><b class=sel_sm>Bar Chart</b></a><span class="breadcrump_sep">|</span><a href=#>Donut</a><span class="breadcrump_sep">|</span><a href=#>Line Chart</a><span class="breadcrump_sep">|</span><a href=#>Data View</a></div>
	<br />
	<img src="images/graph.gif" />
</fieldset>
	<fieldset>
	<legend><span class=legend_label>Sector</span></legend>
	<div class="dash_graph_opt"><b class=sel_sm>Bar Chart</b></a><span class="breadcrump_sep">|</span><a href=#>Donut</a><span class="breadcrump_sep">|</span><a href=#>Line Chart</a><span class="breadcrump_sep">|</span><a href=#>Data View</a></div>
	<br />
	<img src="images/graph.gif" />
</fieldset>
	<fieldset>
	<legend><span class=legend_label>Paris Indicators</span></legend>
	<div class="dash_graph_opt"><b class=sel_sm>Bar Chart</b></a><span class="breadcrump_sep">|</span><a href=#>Donut</a><span class="breadcrump_sep">|</span><a href=#>Line Chart</a><span class="breadcrump_sep">|</span><a href=#>Data View</a></div>
	<br />
	<table class="inside" width=100% cellpadding="0" cellspacing="0">
<tr>
    <td background="images/ins_bg.gif" class=inside><b class="ins_title">Table</b></td>
    <td background="images/ins_bg.gif" class=inside><b class="ins_title">Info</b></td>
    <td background="images/ins_bg.gif" class=inside><b class="ins_title">PI Target</b></td>
    <td background="images/ins_bg.gif" class=inside><b class="ins_title">Year Comparison</b></td>
    <td background="images/ins_bg.gif" class=inside><b class="ins_title">Current</b></td>
</tr>
<tr>
  <td class=inside>&nbsp;</td>
  <td class=inside>&nbsp;</td>
  <td class=inside>&nbsp;</td>
  <td class=inside>&nbsp;</td>
  <td class=inside>&nbsp;</td>
</tr>
</table>
</fieldset>
		</div>
	<div id="tabs-2">
		Morbi tincidunt, dui sit amet facilisis feugiat, odio metus gravida ante, ut pharetra massa metus id nunc. Duis scelerisque molestie turpis. Sed fringilla, massa eget luctus malesuada, metus eros molestie lectus, ut tempus eros massa ut dolor. Aenean aliquet fringilla sem. Suspendisse sed ligula in ligula suscipit aliquam. Praesent in eros vestibulum mi adipiscing adipiscing. Morbi facilisis. Curabitur ornare consequat nunc. Aenean vel metus. Ut posuere viverra nulla. Aliquam erat volutpat. Pellentesque convallis. Maecenas feugiat, tellus pellentesque pretium posuere, felis lorem euismod felis, eu ornare leo nisi vel felis. Mauris consectetur tortor et purus.	</div>
	<div id="tabs-3">
		Mauris eleifend est et turpis. Duis id erat. Suspendisse potenti. Aliquam vulputate, pede vel vehicula accumsan, mi neque rutrum erat, eu congue orci lorem eget lorem. Vestibulum non ante. Class aptent taciti sociosqu ad litora torquent per conubia nostra, per inceptos himenaeos. Fusce sodales. Quisque eu urna vel enim commodo pellentesque. Praesent eu risus hendrerit ligula tempus pretium. Curabitur lorem enim, pretium nec, feugiat nec, luctus a, lacus.
		<p>Duis cursus. Maecenas ligula eros, blandit nec, pharetra at, semper at, magna. Nullam ac lacus. Nulla facilisi. Praesent viverra justo vitae neque. Praesent blandit adipiscing velit. Suspendisse potenti. Donec mattis, pede vel pharetra blandit, magna ligula faucibus eros, id euismod lacus dolor eget odio. Nam scelerisque. Donec non libero sed nulla mattis commodo. Ut sagittis. Donec nisi lectus, feugiat porttitor, tempor ac, tempor vitae, pede. Aenean vehicula velit eu tellus interdum rutrum. Maecenas commodo. Pellentesque nec elit. Fusce in lacus. Vivamus a libero vitae lectus hendrerit hendrerit.</p>
	</div>
	
</div></td>
</tr>
</table>
</td>
  </tr>
</table>

<<script type="text/javascript">
<!--
$(document).ready(function(){
	$("#tabs").tabs();
});

//-->
</script>
