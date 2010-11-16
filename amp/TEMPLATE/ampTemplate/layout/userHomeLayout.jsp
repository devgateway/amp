<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib uri="/taglib/struts-tiles" prefix="tiles" %>
<%@ taglib uri="/taglib/struts-html" prefix="html" %>
<%@ taglib uri="/taglib/digijava" prefix="digi" %>
<%@ taglib uri="/taglib/struts-logic" prefix="logic" %>
<%@ taglib uri="/taglib/struts-bean" prefix="bean" %>
<%@ taglib uri="/taglib/jstl-core" prefix="c" %>
<%@ taglib uri="/taglib/fieldVisibility" prefix="field" %>
<%@ taglib uri="/taglib/featureVisibility" prefix="feature" %>
<%@ taglib uri="/taglib/moduleVisibility" prefix="module" %>




<html>
	<digi:base />
	<digi:context name="digiContext" property="context"/>
	<head>
		<title>
			<%
			String title=(String)((org.apache.struts.tiles.ComponentContext) request.getAttribute("org.apache.struts.taglib.tiles.CompContext")).getAttribute("title");
			String key=(title.replaceAll(" ",""));
			%>
			<c:set var="key">aim:pagetitle:<%=key%></c:set>
				<digi:trn>Aid Management Platform </digi:trn> 
				<digi:trn key="${key}">
					<%=title%>
				</digi:trn>
		</title>
		<META HTTP-EQUIV="Content-Type" CONTENT="text/html; charset=UTF-8">
		<META HTTP-EQUIV="Expires" CONTENT="0">
		<META HTTP-EQUIV="Cache-Control" CONTENT="private"
		
        <!-- Dependencies --> 
        <script type="text/javascript" src="<digi:file src="js_2/yui/yahoo-dom-event.js"/>"></script>
        <script type="text/javascript" src="<digi:file src="js_2/yui/container_core-min.js"/>"></script>
        <script type="text/javascript" src="<digi:file src="js_2/yui/element-beta-min.js"/>"></script>
        <script type="text/javascript" src="<digi:file src="js_2/yui/connection-min.js"/>"></script>
        
        <!-- Source File -->
        <script type="text/javascript" src="<digi:file src="js_2/yui/menu-min.js"/>"></script>
		<script type="text/javascript" src="<digi:file src="js_2/yui/yahoo-dom-event.js"/>"></script> 
        <script type="text/javascript" src="<digi:file src="js_2/yui/container-min.js"/>"></script> 
        <script type="text/javascript" src="<digi:file src="js_2/yui/menu-min.js"/>"></script> 
        <script type="text/javascript" src="<digi:file src="js_2/yui/element-beta-min.js"/>"></script> 
        <script type="text/javascript" src="<digi:file src="js_2/yui/tabview-min.js"/>"></script>
        
        <link type="text/css" href="css_2/tabs.css" rel="stylesheet" />
        
	</head>
     	
	<body bgcolor="#FFFFFF" leftmargin="0" topmargin="0" marginwidth="0" marginheight="0">
	
	<jsp:include page="headerTop_2.jsp"/>
	 <digi:insert attribute="headerMiddle"/>	

	<!-- BREADCRUMP START -->
	<div class="breadcrump">
		<div class="centering">
			<div class="breadcrump_cont">
				<span class="sec_name">My Desktop</span>
			</div>
		</div>
	</div>
	<!-- BREADCRUMP END -->
	<table width="1000" border="0" cellspacing="0" cellpadding="0" align=center>
		<tbody>
			<tr>
				<td width=980px valign=top>
					<div id="tabs">
						<ul class="desktop_tab_base">
							<li class="desktop_tab"><a href="#tabs-1" class="tab_link">Pardonateur</a></li>
							<li class="desktop_tab"><a href="#tabs-2" class="tab_link">Pardonateur 2</a></li>
							<li class="desktop_tab"><a href="#tabs-3" class="tab_link">MoreTabs</a></li>
						</ul>
					<div id="tabs-1">
					<div class="tab_opt_box">
					<div class="show_hide_setting"><a href=#>
						<b>Hide settings</b></a>
					</div>
					<div class="tab_opt">
						<div class="tab_opt_cont">
							<a href="" class="l_sm">ChangeSorting</a> &nbsp;|&nbsp; 
							<a href=""  class="l_sm">Change Filters</a>&nbsp;|&nbsp; 
							<a href="" class="l_sm">Save Tab</a> &nbsp;|&nbsp; 
							<a href="" class="l_sm">Tab Settings</a>
						</div>
					</div>
			<div class="tab_opt_box_cont"><b>Selected Filters</b>:
			accessType: Team | activitiesRejectedByFilter: 0 | amountinthousand:
			true | calendarType: Gregorian Calendar | currency: USD | draft:
			false | justSearch: false | sortByAsc: true |<br />
			<b>Selected Range</b>: Start Year: All | End Year: All |</div>
			</div>
			<div class="show_legend">Amounts are in thousands (000) - US
			Dollar &nbsp;|&nbsp; <a href=#>Show Legend</a></div>
			<div class="paging"><b class="paging_sel">1</b> &nbsp;|&nbsp; <a
				href=# class="l_sm">2</a> &nbsp;|&nbsp; <a href=# class="l_sm">3</a>
			&nbsp;|&nbsp; <a href=# class="l_sm">4</a> &nbsp;|&nbsp; <a href=#
				class="l_sm">5</a> &nbsp;|&nbsp; <a href=# class="l_sm">6</a>
			&nbsp;|&nbsp; <a href=# class="l_sm">Next</a> &nbsp;|&nbsp; <a href=#
				class="l_sm">»</a></div>
			<table class="inside" width=98% cellpadding="0" cellspacing="0">
				<tr>
					<td width="500" colspan="2" rowspan="2"
						background="img_2/ins_bg.gif" class=inside
						style="background-repeat: repeat-x; padding-left: 33px; font-size: 12px;"><b
						class="ins_title">Project Title</b></td>
					<td colspan="2" background="img_2/ins_bg.gif" class=inside
						align=center><b class="ins_title">Total costs</b></td>
				</tr>
				<tr>
					<td background="img_2/ins_bg.gif" class=inside align=center
						style="background-repeat: repeat-x;"><span
						class="ins_title_reg">Actual Commitments</span></td>
					<td background="img_2/ins_bg.gif" class=inside align=center
						style="background-repeat: repeat-x;"><span
						class="ins_title_reg">Actual Disbursements</span></td>
				</tr>
				<tr>
					<td colspan="2" align=center class=inside
						style="background-color: #F2F2F2;">
					<div class="desktop_project_name"><b>Par donateur (541)</b></b></div>
					</td>
					<td class=inside style="background-color: #F2F2F2;">
					<div class="desktop_project_count"><b>3 266 256,92</b></div>
					</td>
					<td class=inside style="background-color: #F2F2F2;">
					<div class="desktop_project_count"><b>1 092 763,77</b></div>
					</td>
				</tr>

				<tr>
					<td class=inside align=center width=20><img
						src="img_2/ico_plus.gif" /></td>
					<td width=500 class=inside>
					<div class="desktop_project_name">Adventiste Development and
					Relief Agency <b>(1)</b></div>
					</td>
					<td class=inside>
					<div class="desktop_project_count">478,1</div>
					</td>
					<td class=inside>
					<div class="desktop_project_count"></div>
					</td>
				</tr>
				<tr>
					<td class=inside align=center width=20><img
						src="img_2/ico_plus.gif" /></td>
					<td width=500 class=inside>
					<div class="desktop_project_name">Agence Canadienne de
					Développement International <b>(4)</b></div>
					</td>
					<td class=inside>
					<div class="desktop_project_count">7 017,36</div>
					</td>
					<td class=inside>
					<div class="desktop_project_count">3 218,64</div>
					</td>
				</tr>
				<tr>
					<td class=inside align=center width=20><img
						src="img_2/ico_minus.gif" /></td>
					<td width=500 class=inside>
					<div class="desktop_project_name_op"><b>Agence Espagnole
					de Coopération Internationale pour le Développement (4)</b></div>
					</td>
					<td class=inside>
					<div class="desktop_project_count"><b>1 062,55</b></div>
					</td>
					<td class=inside>
					<div class="desktop_project_count"><b>537,41</b></div>
					</td>
				</tr>


				<tr>
					<td class=inside align=center width=20>&nbsp;</td>
					<td width=500 class=inside>
					<div class="desktop_project_name_sel">* Projet d'eau potable
					et d'assainissement dans le departement du sud-est (volet
					assainissement)</div>
					</td>
					<td class=inside>
					<div class="desktop_project_count_sel">198,86</div>
					</td>
					<td class=inside>
					<div class="desktop_project_count_sel">137,41</div>
					</td>
				</tr>
				<tr>
					<td class=inside align=center width=20>&nbsp;</td>
					<td width=500 class=inside>
					<div class="desktop_project_name_sel">Projet d'eau potable et
					assainissement dans le sud-est (volet eau potable)</div>
					</td>
					<td class=inside>
					<div class="desktop_project_count_sel">300</div>
					</td>
					<td class=inside>
					<div class="desktop_project_count_sel">&nbsp;</div>
					</td>
				</tr>

				<tr>
					<td class=inside align=center width=20>&nbsp;</td>
					<td width=500 class=inside>
					<div class="desktop_project_name_sel">* Projet d'eau potable
					et d'assainissement dans le departement du sud-est (volet
					assainissement)</div>
					</td>
					<td class=inside>
					<div class="desktop_project_count_sel">400</div>
					</td>
					<td class=inside>
					<div class="desktop_project_count_sel">400</div>
					</td>
				</tr>

				<tr>
					<td class=inside align=center width=20>&nbsp;</td>
					<td width=500 class=inside>
					<div class="desktop_project_name_sel">* Projet d'eau potable
					et d'assainissement dans le departement du sud-est (volet
					assainissement)</div>
					</td>
					<td class=inside>
					<div class="desktop_project_count_sel">163,68</div>
					</td>
					<td class=inside>
					<div class="desktop_project_count_sel">&nbsp;</div>
					</td>
				</tr>

				<tr>
					<td class=inside align=center width=20><img
						src="img_2/ico_plus.gif" /></td>
					<td width=500 class=inside>
					<div class="desktop_project_name">Agence Française de
					Développement <b>(19)</b></div>
					</td>
					<td class=inside>
					<div class="desktop_project_count">47 774,87</div>
					</td>
					<td class=inside>
					<div class="desktop_project_count">23 062,39</div>
					</td>
				</tr>
				<tr>
					<td class=inside align=center width=20><img
						src="img_2/ico_plus.gif" /></td>
					<td width=500 class=inside>
					<div class="desktop_project_name">Agence d'Aide à la
					Coopération Technique et au Développement <b>(6)</b></div>
					</td>
					<td class=inside>
					<div class="desktop_project_count">20 005,94</div>
					</td>
					<td class=inside>
					<div class="desktop_project_count"></div>
					</td>
				</tr>
				<tr>
					<td class=inside align=center width=20><img
						src="img_2/ico_plus.gif" /></td>
					<td width=500 class=inside>
					<div class="desktop_project_name">Agence de Coopération
					Internationale Royaume Uni <b>(1)</b></div>
					</td>
					<td class=inside>
					<div class="desktop_project_count">2 552</div>
					</td>
					<td class=inside>
					<div class="desktop_project_count"></div>
					</td>
				</tr>
				<tr>
					<td class=inside align=center width=20><img
						src="img_2/ico_plus.gif" /></td>
					<td width=500 class=inside>
					<div class="desktop_project_name">Agence des Nations Unies
					pour l'imagerie satellite <b>(1)</b></div>
					</td>
					<td class=inside>
					<div class="desktop_project_count">250,38</div>
					</td>
					<td class=inside>
					<div class="desktop_project_count"></div>
					</td>
				</tr>
				<tr>
					<td class=inside align=center width=20><img
						src="img_2/ico_plus.gif" /></td>
					<td width=500 class=inside>
					<div class="desktop_project_name">Agence des États-Unis pour
					le développement international <b>(36)</b></div>
					</td>
					<td class=inside>
					<div class="desktop_project_count">567 735,87</div>
					</td>
					<td class=inside>
					<div class="desktop_project_count">3 000</div>
					</td>
				</tr>
				<tr>
					<td class=inside align=center width=20><img
						src="img_2/ico_plus.gif" /></td>
					<td width=500 class=inside>
					<div class="desktop_project_name">Agronomes et Vétérinaires
					sans Frontières <b>(2)</b></div>
					</td>
					<td class=inside>
					<div class="desktop_project_count">1 720,46</div>
					</td>
					<td class=inside>
					<div class="desktop_project_count"></div>
					</td>
				</tr>
				<tr>
					<td class=inside align=center width=20><img
						src="img_2/ico_plus.gif" /></td>
					<td width=500 class=inside>
					<div class="desktop_project_name">Aide et Action <b>(2)</b></div>
					</td>
					<td class=inside>
					<div class="desktop_project_count">1 109,38</div>
					</td>
					<td class=inside>
					<div class="desktop_project_count"></div>
					</td>
				</tr>
			</table>
			<div class="show_legend">Amounts are in thousands (000) - US
			Dollar &nbsp;|&nbsp; <a href=#>Show Legend</a></div>
			<div class="paging"><b class="paging_sel">1</b> &nbsp;|&nbsp; <a
				href=# class="l_sm">2</a> &nbsp;|&nbsp; <a href=# class="l_sm">3</a>
			&nbsp;|&nbsp; <a href=# class="l_sm">4</a> &nbsp;|&nbsp; <a href=#
				class="l_sm">5</a> &nbsp;|&nbsp; <a href=# class="l_sm">6</a>
			&nbsp;|&nbsp; <a href=# class="l_sm">Next</a> &nbsp;|&nbsp; <a href=#
				class="l_sm">»</a></div>

			</div>
			<div id="tabs-2">Morbi tincidunt, dui sit amet facilisis
			feugiat, odio metus gravida ante, ut pharetra massa metus id nunc.
			Duis scelerisque molestie turpis. Sed fringilla, massa eget luctus
			malesuada, metus eros molestie lectus, ut tempus eros massa ut dolor.
			Aenean aliquet fringilla sem. Suspendisse sed ligula in ligula
			suscipit aliquam. Praesent in eros vestibulum mi adipiscing
			adipiscing. Morbi facilisis. Curabitur ornare consequat nunc. Aenean
			vel metus. Ut posuere viverra nulla. Aliquam erat volutpat.
			Pellentesque convallis. Maecenas feugiat, tellus pellentesque pretium
			posuere, felis lorem euismod felis, eu ornare leo nisi vel felis.
			Mauris consectetur tortor et purus.</div>
			<div id="tabs-3">Mauris eleifend est et turpis. Duis id erat.
			Suspendisse potenti. Aliquam vulputate, pede vel vehicula accumsan,
			mi neque rutrum erat, eu congue orci lorem eget lorem. Vestibulum non
			ante. Class aptent taciti sociosqu ad litora torquent per conubia
			nostra, per inceptos himenaeos. Fusce sodales. Quisque eu urna vel
			enim commodo pellentesque. Praesent eu risus hendrerit ligula tempus
			pretium. Curabitur lorem enim, pretium nec, feugiat nec, luctus a,
			lacus.
			<p>Duis cursus. Maecenas ligula eros, blandit nec, pharetra at,
			semper at, magna. Nullam ac lacus. Nulla facilisi. Praesent viverra
			justo vitae neque. Praesent blandit adipiscing velit. Suspendisse
			potenti. Donec mattis, pede vel pharetra blandit, magna ligula
			faucibus eros, id euismod lacus dolor eget odio. Nam scelerisque.
			Donec non libero sed nulla mattis commodo. Ut sagittis. Donec nisi
			lectus, feugiat porttitor, tempor ac, tempor vitae, pede. Aenean
			vehicula velit eu tellus interdum rutrum. Maecenas commodo.
			Pellentesque nec elit. Fusce in lacus. Vivamus a libero vitae lectus
			hendrerit hendrerit.</p>
			</div>
			</div>

			</td>
				<td width=20px align=center background="img_2/close_panel_bg.gif" valign=top>
					<a style="cursor: pointer;" ><img src="img_2/close_panel.gif" width="9" height="96" border=0 id="closepanel"></a>
				</td>
				<td>
				<div id="rightpanel">
					<div class="right_menu">
						<div class="right_menu_header"><div class="right_menu_header_cont">Search</div></div>
							<div class="right_menu_box"><div class="right_menu_cont">
								<table width="100%" border="0" cellspacing="0" cellpadding="0">
	  								<tr>
	    								<td class="tbl_spacing"><div class="search_label">Keyword:</div></td>
	    								<td align=right class="tbl_spacing"><input name="" type="text" class="inputx" style="width:147px;"></td>
	  								</tr>
									<tr>
	    								<td>
	    									<div class="search_label">Type:</div></td>
	    								<td align=right>
	    									<select name="select" class=dropdwn_sm style="width:150px;">
	  											<option value="Na">All</option>
											</select>
	    								</td>
	    							</tr>
								</table>
							</div>
						</div>
					</div>
					<logic:present name="currentMember">
						<digi:insert attribute="myLinks" />
						<digi:insert attribute="myReports"/>
						<digi:insert attribute="myMessages"/>
					</logic:present>
					<div class="right_menu">
						<div class="right_menu_header"><div class="right_menu_header_cont">Latest created / updated projects</div></div>
							<div class="right_menu_box">
								<div class="right_menu_cont">
									<li class="tri"><a href=#>Essential Services for Health</a></li>
									<li class="tri"><a href=#>WTO Capacity Building Program</a></li>
									<li class="tri"><a href=#>Polio Vaccination Program</a></li>
									<li class="tri"><a href=#>Water Sector Support Project</a></li>
									<li class="tri"><a href=#>Forest Protection andReforestation Project</a></li>
									
									<li class="tri"><a href=#>Tax Administration Project</a></li>
									<li class="tri"><a href=#>Initiative to End Hunger</a></li>
									<li class="tri"><a href=#>Rural Productivity Project</a></li>
									<li class="tri"><a href=#>Nutritional Survey</a></li>
									<li class="tri"><a href=#>National Voter Registration</a></li>
								</div>
							</div>
						</div>
					</div>
					</td>
					
					</tr>
			</tbody>
		</table>		
		<digi:insert attribute="footer" />
    </body>
</html>
<script language=javascript>
		$(document).ready(function(){
	   		$("#closepanel").click(function(event){
	     	
	     	$("#rightpanel").toggle('slow');
		});
	 });
		
		$(function() {
			$("#tabs").tabs();
		});
		
</script>

