<%@ page pageEncoding="UTF-8"%>
<%@ taglib uri="/taglib/struts-bean" prefix="bean"%>
<%@ taglib uri="/taglib/struts-logic" prefix="logic"%>
<%@ taglib uri="/taglib/struts-tiles" prefix="tiles"%>
<%@ taglib uri="/taglib/struts-html" prefix="html"%>
<%@ taglib uri="/taglib/digijava" prefix="digi"%>
<%@ taglib uri="/taglib/jstl-core" prefix="c"%>
<%@ taglib uri="/taglib/jstl-functions" prefix="fn"%>
<%@ taglib uri="/taglib/category" prefix="category"%>
<%@ taglib uri="/taglib/fieldVisibility" prefix="field"%>
<%@ taglib uri="/taglib/featureVisibility" prefix="feature"%>
<%@ taglib uri="/taglib/moduleVisibility" prefix="module"%>


<script language="JavaScript" type="text/javascript" src="<digi:file src="module/aim/scripts/common.js"/>"></script>
<script type="text/javascript" src="<digi:file src="/TEMPLATE/ampTemplate/js_2/jquery/jquery-contains-ignorecase.js"/>"></script>

<script type="text/javascript">
//Global declaration
	trnPrimary = "";
	trnAll="";
	trnAdvancedFilter = ""; 
	trnCancel = "";
	trnLoading = "";
	trnCommitments="";
	trnDisbursements="";
	trnExpenditures="";
	trnAidType="";
	trnFinancingInstrument="";
	trnSubSectorProfile="";

//Section for all translation as global so included javascript can use them
function initializeTranslations(){
	trnPrimary = "<digi:trn jsFriendly='true'>Primary</digi:trn>";
	trnAll="<digi:trn jsFriendly='true'>All</digi:trn>";
	trnAdvancedFilter = '\n<digi:trn jsFriendly="true">Advanced Filters</digi:trn>'; 
	trnCancel = '<digi:trn jsFriendly="true">Cancel</digi:trn>';
	trnLoading = '<digi:trn jsFriendly="true">Loading...</digi:trn>';
	trnCommitments="<digi:trn jsFriendly='true'>Commitments</digi:trn>";
	trnDisbursements="<digi:trn jsFriendly='true'>Disbursements</digi:trn>";
	trnExpenditures="<digi:trn jsFriendly='true'>Expenditures</digi:trn>";
	trnAidType="<digi:trn jsFriendly='true'>Aid Type</digi:trn>";
	trnFinancingInstrument="<digi:trn jsFriendly='true'>Financing Instrument</digi:trn>";
	trnSubSectorProfile="<digi:trn jsFriendly='true'>Sub-sector breakdown</digi:trn>";
}
</script>
<script type="text/javascript" src="/TEMPLATE/ampTemplate/js_2/esrigis/filter.js"></script> 
<script language="JavaScript" type="text/javascript" src="<digi:file src="module/aim/scripts/common.js"/>"></script>
<script type="text/javascript" src="<digi:file src="/TEMPLATE/ampTemplate/js_2/jquery/jquery-contains-ignorecase.js"/>"></script>

<script type="text/javascript" src="/TEMPLATE/ampTemplate/js_2/esrigis/filter.js"></script>
<digi:instance property="datadispatcherform" />
<digi:form action="/mainmap.do" styleId = "datadispatcherform_real_one">
	<table>
		<tr>
			<td>
				<div id="dialog2" class="dialog" title="Advanced Filters">
					<div id="popinContent" class="content">
						<c:set var="selectorHeaderSize" scope="page" value="11" />
							<div class="yui-content" style="height: 92%; margin-top: 10px;">
								<div id="generalTab" style="height: 91%;">
										<div class="grouping_selector_wrapper" style="float: left; width: 40%; padding: 0px; height: 98%;">
											<div
												style="background-image: url(/TEMPLATE/ampTemplate/img_2/ins_header.gif); margin: 0px; color: white; padding: 2px; height: 32px; border: 1px solid #CCCCCC; border-bottom: 0px;">
												<div class="inside">
													<b class="ins_header"><digi:trn>Grouping Selector</digi:trn>
													</b>
												</div>
											</div>
											<div style="height: 180px; border: 1px solid #CCCCCC; overflow: auto; background: white; maxHeight: 180px; padding: 20px;">
												<table style="width: 95%; margin-top: 15px;" align="center"
													class="inside">
													<tr style="cursor: pointer;">
														<td class="side_opt_sel" id="general_selector_0">
															<div class="selector_type_cont" onclick="changeTab(0)">
																<digi:trn>General</digi:trn>
															</div></td>

													</tr>
													<tr style="cursor: pointer;">
														<td class="side_opt_sel" id="general_selector_1" >
															<div class="selector_type_cont" onclick="changeTab(1)">
																<digi:trn>Organization Groups With Organizations</digi:trn>
															</div>
														</td>
													</tr>
													<tr style="cursor: pointer;">
														<td class="side_opt_sel" id="general_selector_2" >
															<div class="selector_type_cont" onclick="changeTab(4)">
																<digi:trn>Organization Type</digi:trn>
															</div>
														</td>
													</tr>
													<tr style="cursor: pointer;">
														<td class="side_opt_sel" id="general_selector_3">
															<div class="selector_type_cont" onclick="changeTab(2)">
																<digi:trn>Regions With Zones</digi:trn>
															</div></td>

													</tr>
													<tr style="cursor: pointer;">
														<td class="side_opt_sel" id="general_selector_4">
															<div class="selector_type_cont" onclick="changeTab(3)">
																<digi:trn>Sectors and Sub Sectors</digi:trn>
															</div></td>
													</tr>
													<tr style="cursor: pointer;">
														<td class="side_opt_sel" id="general_selector_5">
															<div class="selector_type_cont" onclick="changeTab(5)">
																<digi:trn>Aid Modality</digi:trn>
															</div>
														</td>
													</tr>
													<tr style="cursor: pointer;">
														<td class="side_opt_sel" id="general_selector_6">
															<div class="selector_type_cont" onclick="changeTab(6)">
																<digi:trn>Aid Type</digi:trn>
															</div>
														</td>
													</tr>
													<tr style="cursor: pointer;">
														<td class="side_opt_sel" id="general_selector_7">
															<div class="selector_type_cont" onclick="changeTab(7)">
																<digi:trn>Project Status</digi:trn>
															</div>
														</td>
													</tr>
													<tr style="cursor: pointer;">
														<td class="side_opt_sel" id="general_selector_8">
															<div class="selector_type_cont" onclick="changeTab(8)">
																<digi:trn>Programs</digi:trn>
															</div></td>	
													</tr>
												</table>
											</div>
										</div>
										<div class="member_selector_wrapper" style="margin-left: 40%; padding: 0px; height: 98%;" id="generalInfoId">
											<div style="background-image: url(/TEMPLATE/ampTemplate/img_2/ins_header.gif); margin: 0px; color: white; padding: 2px; height: 32px; border: 1px solid #CCCCCC; border-bottom: 0px;">
												<div class="inside" style="float: left">
													&nbsp; <b class="ins_header"> <digi:trn>Options Selector</digi:trn>
													</b>
												</div>
											</div>
											<div style="height: 180px; border: 1px solid #CCCCCC; overflow: auto; background: white; maxHeight: 180px; padding: 20px;"
												id="generalDivList">
												<!-- 
												<c:if test="${!datadispatcherform.filter.fromPublicView}">
													<html:checkbox property="filter.workspaceOnly" styleId="workspace_only">
														<digi:trn>Show Only Data From This Workspace</digi:trn>
													</html:checkbox>
													<c:set var="translation">
														<digi:trn>Dashboards will show only data from activities of current workspace.</digi:trn>
													</c:set>
													<img src="/TEMPLATE/ampTemplate/img_2/ico_quest.gif" title="${translation}"/>
													<br />
												</c:if>
												 -->
												<hr />
												<br />
												<digi:trn>What data should the map show?</digi:trn>
												<c:set var="translation">
													<digi:trn>What type of funding the map should use.</digi:trn>
												</c:set>
												<img src="/TEMPLATE/ampTemplate/img_2/ico_quest.gif" title="${translation}"/><br />
												<html:radio property="filter.transactionType" styleId="transaction_type_0" value="0">
													<digi:trn>Commitments</digi:trn>
												</html:radio>
												<br />
												<html:radio property="filter.transactionType" styleId="transaction_type_1" value="1">
													<digi:trn>Disbursements</digi:trn>
												</html:radio>
												<br />
												<html:radio property="filter.transactionType" styleId="transaction_type_127" value="127">
													<digi:trn>Commitments</digi:trn> <digi:trn>and</digi:trn> <digi:trn>Disbursements</digi:trn>
												</html:radio>
												<br />
												<module:display name="/Activity Form/Funding/Funding Group/Funding Item/MTEF Projections"
                                                            	parentModule="/Activity Form/Funding/Funding Group/Funding Item">
													<html:radio property="filter.transactionType" styleId="transaction_type_3" value="3">
														<digi:trn>MTEF Projections</digi:trn>
													</html:radio>
												</module:display>
												<hr />
											</div>
										</div>
										<div class="member_selector_wrapper" id="orgGrpContent"
											style="margin-left: 40%; padding: 0px; height: 98%; display:none">
											<div style="background-image: url(/TEMPLATE/ampTemplate/img_2/ins_header.gif); margin: 0px; color: white; padding: 2px; height: 32px; border: 1px solid #CCCCCC; border-bottom: 0px;">
												<div class="inside" style="float: left">
													&nbsp; <b class="ins_header"> <digi:trn>Member Selector</digi:trn>
													</b>
												</div>
												<div style="float: right">
													<input onkeypress="clearSearch('orgGrpDivList')"
														id="orgGrpDivList_search" type="text" class="inputx" /> <input
														type="button" class="buttonx"
														onclick="findPrev('orgGrpDivList')" value='&lt;&lt;' /> <input
														type="button" onclick="findNext('orgGrpDivList')"
														class="buttonx" value="&gt;&gt;" />
												</div>
											</div>
											<div style="height: 180px; width:400px; border: 1px solid #CCCCCC; overflow: auto; background: white; maxHeight: 180px; padding: 20px;" id="orgGrpDivList">
												<ul style="list-style-type: none;margin-left: 0px;">
													<li>
														<input type="checkbox" value="-1" id="org_grp_check_all" name="org_grp_check"
															onClick="allOptionChecked(this,'org_grp_check','organization_check')" />
														<span><digi:trn>All</digi:trn></span>
													</li>
													<c:forEach items="${datadispatcherform.filter.orgGroupWithOrgsList}" var="item">
														<c:set var="orgGrp">
															<c:out value="${item.mainEntity.orgGrpName}"/>
														</c:set>
														<li>
															<input type="checkbox" name="org_grp_check"
																	title="${orgGrpe}"
																	value="${item.mainEntity.ampOrgGrpId}"
																	onClick="uncheckAllOption('org_grp_check');checkRelatedEntities(this,'organization_check',${item.mainEntity.ampOrgGrpId})" />
															<span><c:out value="${orgGrp}"/>
														</span> <br />
															<ul style="list-style-type: none">
																<c:forEach items="${item.subordinateEntityList}" var="organization">
																	<li><input type="checkbox"
																		class="organization_check_${item.mainEntity.ampOrgGrpId}"
																		name="organization_check" title="<c:out value='${organization.name}'/>"
																		value="${organization.ampOrgId}"/> 
																		<span><c:out value="${organization.name}"/></span>
																	</li>
																</c:forEach>
															</ul></li>
													</c:forEach>
												</ul>
											</div>
										</div>
										<div class="member_selector_wrapper" id="regionDivContent" style="margin-left: 40%; padding: 0px; height: 98%; display:none">
											<div style="background-image: url(/TEMPLATE/ampTemplate/img_2/ins_header.gif); margin: 0px; color: white; padding: 2px; height: 32px; border: 1px solid #CCCCCC; border-bottom: 0px;">
												<div class="inside" style="float: left">
													&nbsp; <b class="ins_header"> <digi:trn>Member Selector</digi:trn>
													</b>

												</div>
												<div class="inside" style="float: right">
													<input onkeypress="clearSearch('regionDivList')" id="regionDivList_search" type="text" class="inputx" /> 
													<input type="button" class="buttonx" onclick="findPrev('regionDivList')" value='&lt;&lt;' /> 
													<input type="button" onclick="findNext('regionDivList')" class="buttonx" value="&gt;&gt;" />
												</div>
											</div>
											<div style="height: 180px; border: 1px solid #CCCCCC; overflow: auto; background: white; maxHeight: 180px; padding: 20px;"
												id="regionDivList">
												<ul style="list-style-type: none;margin-left: 0px;">
													<li class="all_checkbox">
														<input type="checkbox" id="region_check_all" name="region_check" value="-1"
																onClick="allOptionChecked(this,'region_check','zone_check')" />
														<span><digi:trn>All</digi:trn></span>
													</li>
													<c:forEach items="${datadispatcherform.filter.regionWithZones}" var="item">
														<li>
															<input type="checkbox" name="region_check"
																	title="${item.mainEntity.name}"
																	value="${item.mainEntity.id}"
																	onClick="uncheckAllOption('region_check');checkRelatedEntities(this,'zone_check',${item.mainEntity.id})">
															<span><c:out value="${item.mainEntity.name}"/></span> 
															<br />
															<ul style="list-style-type: none">
																<c:forEach items="${item.subordinateEntityList}" var="admLevel1Location">
																	<li><input type="checkbox"
																		class="zone_check_${item.mainEntity.id}"
																		name="zone_check" title="${admLevel1Location.name}"
																		value="${admLevel1Location.id}"
																		 /><span><c:out value="${admLevel1Location.name}"/></span>
																	</li>
																</c:forEach>
															</ul></li>
													</c:forEach>
												</ul>
											</div>
										</div>
										<div class="member_selector_wrapper" id="sectorDivContent" style="margin-left: 40%; padding: 0px; height: 98%; display:none">
												<div style="background-image: url(/TEMPLATE/ampTemplate/img_2/ins_header.gif); margin: 0px; color: white; padding: 2px; height: 32px; border: 1px solid #CCCCCC; border-bottom: 0px;">
													<div class="inside" style="float: left">
														&nbsp;<b class="ins_header"> <digi:trn>Member Selector</digi:trn></b>
													</div>
													<div class="inside" style="float: right">
														<input onkeypress="clearSearch('sectorDivList')" id="sectorDivList_search" type="text" class="inputx" />
														<input type="button" class="buttonx" onclick="findPrev('sectorDivList')" value='&lt;&lt;' />
														<input type="button" onclick="findNext('sectorDivList')" class="buttonx" value="&gt;&gt;" />
													</div>
												</div>
												<div style="height: 180px; border: 1px solid #CCCCCC; overflow: auto; background: white; maxHeight: 180px; padding: 20px;"
													id="sectorDivList">
													<ul style="list-style-type: none;margin-left: 0px;">
														<c:forEach items="${datadispatcherform.filter.configWithSectorAndSubSectors}" var="item">
															<c:set var="item" scope="request" value="${item}" />
															<c:choose>
																<c:when test="${item.mainEntity.name=='Primary'}">
																	<field:display name="Primary Sector" feature="Sectors">
																		<jsp:include page="sectorPopinHelper.jsp" flush="true" />
																	</field:display>
																</c:when>
																<c:when test="${item.mainEntity.name=='Secondary'}">
																	<field:display name="Secondary Sector" feature="Sectors">
																		<jsp:include page="sectorPopinHelper.jsp" />
																	</field:display>
																</c:when>
																<c:when test="${item.mainEntity.name=='Tertiary'}">
																	<field:display name="Tertiary Sector" feature="Sectors">
																		<jsp:include page="sectorPopinHelper.jsp" />
																	</field:display>
																</c:when>
															</c:choose>
														</c:forEach>
													</ul>
												</div>
											</div>
										<div>
											<div class="member_selector_wrapper" id="programDivContent" style="margin-left: 40%; padding: 0px; height: 98%; display:none">
												<div style="background-image: url(/TEMPLATE/ampTemplate/img_2/ins_header.gif); margin: 0px; color: white; padding: 2px; height: 32px; border: 1px solid #CCCCCC; border-bottom: 0px;">
													<div class="inside" style="float: left">
														&nbsp;<b class="ins_header"> <digi:trn>Member Selector</digi:trn></b>
													</div>
													<div class="inside" style="float: right">
														<input onkeypress="clearSearch('programDivList')" id="programDivList_search" type="text" class="inputx" />
														<input type="button" class="buttonx" onclick="findPrev('programDivList')" value='&lt;&lt;' />
														<input type="button" onclick="findNext('programDivList')" class="buttonx" value="&gt;&gt;" />
													</div>
												</div>
												<div style="height: 180px; border: 1px solid #CCCCCC; overflow: auto; background: white; maxHeight: 180px; padding: 20px;"
													id="programDivList">
													<ul style="list-style-type: none;margin-left: 0px;">
														<c:forEach items="${datadispatcherform.filter.programElements}" var="item">
														<c:set var="item" scope="request" value="${item}" />
														<jsp:include page="programPopinHelper.jsp" />
														</c:forEach>
													</ul>
												</div>
											</div>
										<div>
							
										<div class="member_selector_wrapper" id="structuresDivContent" style="margin-left: 40%; padding: 0px; height: 98%; display:none">
											<div style="background-image: url(/TEMPLATE/ampTemplate/img_2/ins_header.gif); margin: 0px; color: white; padding: 2px; height: 32px; border: 1px solid #CCCCCC; border-bottom: 0px;">
												<div class="inside" style="float: left">
													&nbsp; <b class="ins_header"> <digi:trn>Member Selector</digi:trn>
													</b>

												</div>
												<div class="inside" style="float: right">
													<input onkeypress="clearSearch('structuresDivList')" id="structuresDivList_search" type="text" class="inputx" /> 
													<input type="button" class="buttonx" onclick="findPrev('structuresDivList')" value='&lt;&lt;' /> 
													<input type="button" onclick="findNext('structuresDivList')" class="buttonx" value="&gt;&gt;" />
												</div>
											</div>
											<div style="height: 180px; border: 1px solid #CCCCCC; overflow: auto; background: white; maxHeight: 180px; padding: 20px;"
												id="structuresDivList">
												<ul style="list-style-type: none;margin-left: 0px;">
													<li class="all_checkbox">
														<input type="checkbox" id="st_check_all" name="st_check" value="-1"
																onClick="allOptionChecked(this,'structures_check','structures_check')" />
														<span><digi:trn>All</digi:trn></span>
													</li>
													<c:forEach items="${datadispatcherform.filter.structureTypes}" var="item">
														<li>
															<input type="checkbox" name="structures_check" title="${item.name}" value="${item.typeId}">
															<c:set var="translation">
																<digi:trn>${item.name}</digi:trn>
															</c:set>
															<span><c:out value="${translation}"/></span> 
															<br/>
														</li>
													</c:forEach>
												</ul>
											</div>
										</div>
										<div class="member_selector_wrapper" id="aidmodalityDivContent" style="margin-left: 40%; padding: 0px; height: 98%; display:none">
											<div style="background-image: url(/TEMPLATE/ampTemplate/img_2/ins_header.gif); margin: 0px; color: white; padding: 2px; height: 32px; border: 1px solid #CCCCCC; border-bottom: 0px;">
												<div class="inside" style="float: left">
													&nbsp; <b class="ins_header"> <digi:trn>Member Selector</digi:trn>
													</b>

												</div>
												<div class="inside" style="float: right">
													<input onkeypress="clearSearch('aidmodalityDivList')" id="aidmodalityDivList_search" type="text" class="inputx" /> 
													<input type="button" class="buttonx" onclick="findPrev('aidmodalityDivList')" value='&lt;&lt;' /> 
													<input type="button" onclick="findNext('aidmodalityDivList')" class="buttonx" value="&gt;&gt;" />
												</div>
											</div>
											<div style="height: 180px; border: 1px solid #CCCCCC; overflow: auto; background: white; maxHeight: 180px; padding: 20px;"
												id="aidmodalityDivList">
												<ul style="list-style-type: none;margin-left: 0px;">
													<li class="all_checkbox">
														<input type="checkbox" id="aid_check_all" name="aid_check" value="-1"
																onClick="allOptionChecked(this,'aidmodality_check','aidmodality_check')" />
														<span><digi:trn>All</digi:trn></span>
													</li>
													<c:forEach items="${datadispatcherform.filter.financingInstruments}" var="item">
														<li>
															<input type="checkbox" name="aidmodality_check" title="${item.value}" value="${item.id}">
															<c:set var="translation">
																<digi:trn>${item.value}</digi:trn>
															</c:set>
															<span><c:out value="${translation}"/></span>  
															<br/>
														</li>
													</c:forEach>
												</ul>
											</div>
										</div>
										<div class="member_selector_wrapper" id="typeofassisDivContent" style="margin-left: 40%; padding: 0px; height: 98%; display:none">
											<div style="background-image: url(/TEMPLATE/ampTemplate/img_2/ins_header.gif); margin: 0px; color: white; padding: 2px; height: 32px; border: 1px solid #CCCCCC; border-bottom: 0px;">
												<div class="inside" style="float: left">
													&nbsp; <b class="ins_header"> <digi:trn>Member Selector</digi:trn>
													</b>

												</div>
												<div class="inside" style="float: right">
													<input onkeypress="clearSearch('typeofassisDivList')" id="typeofassisDivList_search" type="text" class="inputx" /> 
													<input type="button" class="buttonx" onclick="findPrev('typeofassisDivList')" value='&lt;&lt;' /> 
													<input type="button" onclick="findNext('typeofassisDivList')" class="buttonx" value="&gt;&gt;" />
												</div>
											</div>
											<div style="height: 180px; border: 1px solid #CCCCCC; overflow: auto; background: white; maxHeight: 180px; padding: 20px;"
												id="typeofassisDivList">
												<ul style="list-style-type: none;margin-left: 0px;">
													<li class="all_checkbox">
														<input type="checkbox" id="typeofassis_check_all" name="assis_check" value="-1"
																onClick="allOptionChecked(this,'typeofassis_check','typeofassis_check')" />
														<span><digi:trn>All</digi:trn></span>
													</li>
													<c:forEach items="${datadispatcherform.filter.typeofassistences}" var="item">
														<li>
															<input type="checkbox" name="typeofassis_check" title="${item.value}" value="${item.id}">
															<c:set var="translation">
																<digi:trn>${item.value}</digi:trn>
															</c:set>
															<span><c:out value="${translation}"/></span>  
															<br/>
														</li>
													</c:forEach>
												</ul>
											</div>
										</div>
										<div class="member_selector_wrapper" id="projectstatusDivContent" style="margin-left: 40%; padding: 0px; height: 98%; display:none">
											<div style="background-image: url(/TEMPLATE/ampTemplate/img_2/ins_header.gif); margin: 0px; color: white; padding: 2px; height: 32px; border: 1px solid #CCCCCC; border-bottom: 0px;">
												<div class="inside" style="float: left">
													&nbsp; <b class="ins_header"> <digi:trn>Member Selector</digi:trn>
													</b>

												</div>
												<div class="inside" style="float: right">
													<input onkeypress="clearSearch('projectstDivList')" id="projectstDivList_search" type="text" class="inputx" /> 
													<input type="button" class="buttonx" onclick="findPrev('projectstDivList')" value='&lt;&lt;' /> 
													<input type="button" onclick="findNext('projectstDivList')" class="buttonx" value="&gt;&gt;" />
												</div>
											</div>
											<div style="height: 180px; border: 1px solid #CCCCCC; overflow: auto; background: white; maxHeight: 180px; padding: 20px;"
												id="projectstDivList">
												<ul style="list-style-type: none;margin-left: 0px;">
													<li class="all_checkbox">
														<input type="checkbox" id="typeofassis_check_all" name="assis_check" value="-1"
																onClick="allOptionChecked(this,'projectst_check','projectst_check')" />
														<span><digi:trn>All</digi:trn></span>
													</li>
													<c:forEach items="${datadispatcherform.filter.projectstatus}" var="item">
														<li>
															<input type="checkbox" name="projectst_check" title="${item.value}" value="${item.id}">
															<c:set var="translation">
																<digi:trn>${item.value}</digi:trn>
															</c:set>
															<span><c:out value="${translation}"/></span> 
															<br/>
														</li>
													</c:forEach>
												</ul>
											</div>
										</div>
										<div class="member_selector_wrapper" id="orgtypeDivContent" style="margin-left: 40%; padding: 0px; height: 98%; display:none">
											<div style="background-image: url(/TEMPLATE/ampTemplate/img_2/ins_header.gif); margin: 0px; color: white; padding: 2px; height: 32px; border: 1px solid #CCCCCC; border-bottom: 0px;">
												<div class="inside" style="float: left">
													&nbsp; <b class="ins_header"> <digi:trn>Member Selector</digi:trn>
													</b>

												</div>
												<div class="inside" style="float: right">
													<input onkeypress="clearSearch('orgtypeDivList')" id="org_type_search" type="text" class="inputx" /> 
													<input type="button" class="buttonx" onclick="findPrev('orgtypeDivList')" value='&lt;&lt;' /> 
													<input type="button" onclick="findNext('orgtypeDivList')" class="buttonx" value="&gt;&gt;" />
												</div>
											</div>
											<div style="height: 180px; border: 1px solid #CCCCCC; overflow: auto; background: white; maxHeight: 180px; padding: 20px;"
												id="orgtypeDivList">
												<ul style="list-style-type: none;margin-left: 0px;">
													<li class="all_checkbox">
														<input type="checkbox" id="typeofassis_check_all" name="assis_check" value="-1"
																onClick="allOptionChecked(this,'orgtypes_check','orgtypes_check')" />
														<span><digi:trn>All</digi:trn></span>
													</li>
													<c:forEach items="${datadispatcherform.filter.organizationsType}" var="item">
														<li>
															<input type="checkbox" name="orgtypes_check" title="${item.orgType}"
																	value="${item.ampOrgTypeId}">
															<span><c:out value="${item.orgType}"/></span> 
															<br/>
														</li>
													</c:forEach>
												</ul>
											</div>
										</div>
										<table border="0" cellspacing="3" cellpadding="3">
											<tr>
												<td>
													<b><digi:trn>Currency Type</digi:trn>:</b>
												</td>
												<td>
													<html:select property="filter.currencyId"
														styleId="currencies_dropdown_ids" styleClass="dropdwn_sm"
														style="width:150px;">
														<html:optionsCollection property="filter.currencies"
															value="ampCurrencyId" label="currencyName" />
													</html:select>
												</td>
												<td>
													<b><digi:trn>Start year</digi:trn>:</b>
												</td>
												<td>
													<html:select property="filter.startYearFilter" styleId="startYear_dropdown" styleClass="dropdwn_sm"
														style="width:70px;">
														<html:optionsCollection property="filter.years" label="key" value="value" />
													</html:select>
												</td>
												<td>
													<b><digi:trn>Budget</digi:trn>:</b>
												</td>
												<td>
													<html:select property="filter.selectedBudget"
													styleId="budget_dropdown" styleClass="dropdwn_sm"
													style="width:80px;">
													<html:option value="0"><digi:trn>None</digi:trn></html:option>
													<html:optionsCollection property="filter.budgets"
															label="value" value="id" />
												   </html:select>
												</td>
											</tr>
											<tr>
												<td>
													<b><digi:trn>Fiscal Calendar</digi:trn>:</b>
												</td>	
												<td>
													<html:select property="filter.fiscalCalendarId"
														styleId="fiscalCalendar_dropdown_Id"
														styleClass="dropdwn_sm" style="width:150px;">
														<html:option value="-1">
															<digi:trn>None</digi:trn>
														</html:option>
														<html:optionsCollection property="filter.fiscalCalendars"
															label="name" value="ampFiscalCalId" />
													</html:select>
												</td>
												<td>
													<b><digi:trn>End year</digi:trn>:</b>
												</td>
												<td>
													<html:select property="filter.endYearFilter" styleId="endYear_dropdown" styleClass="dropdwn_sm"
														style="width:70px;">
														<html:optionsCollection property="filter.years" label="key" value="value" />
													</html:select>
												</td>
											</tr>
										</table>
									</div>
								</div>
								
						
	<input type="button" value="<digi:trn>Apply</digi:trn>" class="buttonx" onclick="applyFilterPopin()" style="margin-right:10px; margin-top:10px;" id="applyButtonPopin">
	<input type="button" value="<digi:trn>Reset to defaults</digi:trn>" onclick="resetToDefaults()" class="buttonx" style="margin-right:10px; margin-top:10px;">
	<input type="button" value="<digi:trn>Close</digi:trn>" class="buttonx" onclick="hidePopin()" style="margin-right:10px; margin-top:10px;">
	
	</div>
	</div>
	</td>
	</tr>
</table>
	<html:hidden property="filter.currencyCode" styleId="currencyCode" />
	<html:hidden property="filter.yearsInRange" styleId="yearsInRange" />
	<html:hidden property="filter.workspaceOnly" styleId="workspaceOnly" />
	<html:hidden property="filter.currencyId" styleId="currencyId" />
	<html:hidden property="filter.defaultCurrencyId" styleId="defaultCurrencyId" />
	<html:hidden property="filter.fiscalCalendarId" styleId="fiscalCalendarId" />
	<html:hidden property="filter.defaultFiscalCalendarId" styleId="defaultFiscalCalendarId" />
	<html:hidden property="filter.startYear" styleId="startYear"/>
	<html:hidden property="filter.endYear" styleId="endYear" />
	<html:hidden property="filter.defaultStartYear" styleId="defaultStartYear"/>
	<html:hidden property="filter.defaultEndYear" styleId="defaultEndYear" />
	<html:hidden property="filter.transactionType" styleId="transactionType" />
	<html:hidden property="filter.selectedBudget" styleId="selectedBudget" />
	<html:hidden property="filter.selectedPeacebuildingMarkerId" styleId="selectedPeacebuildingMarkerId" />
		
</digi:form>

<script type="text/javascript">
function filterenable(){
	isenable = <bean:write name='datadispatcherform' property='filter.modeexport'/>;
	return !isenable;
}
</script>
