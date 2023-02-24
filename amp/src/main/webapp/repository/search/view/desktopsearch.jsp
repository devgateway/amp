<%@ taglib uri="/taglib/digijava" prefix="digi" %>
<%@ taglib uri="/taglib/jstl-core" prefix="c"%>

<script type="text/javascript">

function search() {
	var trn= '<digi:trn jsFriendly="true">Both fields in the search form must be completed</digi:trn>'
	
	if (!$("#querytype").val().trim || !$("#keyword").val().trim()){
		alert(trn);
	} else {
		$("<form/>", { action: '/search/search.do', method: 'POST' })
		.append($("<input>", {type:'hidden', name:'desksearch', value:'true'}))
		.append($("<input>", {type:'hidden', name:'type', value: $("#querytype").val()}))
		.append($("<input>", {type:'hidden', name:'searchMode', value: $("#searchMode").val()}))
		.append($("<input>", {type:'hidden', name:'keyword', value: $("#keyword").val()}))
		.appendTo($(document.body))
		.submit();
	}
}

function searchEnter(e) {
    if (typeof e == 'undefined' && window.event) 
        e = window.event; 
    if (e.keyCode == 13)
    	$("#searchButton").click();
}

</script>

<c:set var="suffix">
	<c:if test="${sessionScope.mode}">
_big
</c:if>
</c:set>
<c:set var="searchDescription">
				<digi:trn>You can use the * wildcard for matching any character</digi:trn>
			</c:set>


<div class="right_menu">

	<div class="right_menu_header${suffix}">
		<div class="right_menu_header_cont"><digi:trn>Search</digi:trn></div>
		</div>
			<div class="right_menu_box${suffix}">
				<div class="right_menu_cont">
				 	<table width="85%" border="0" cellspacing="1px" cellpadding="0">
						<tr>
								<td class="tbl_spacing" align="left">
									<div class="search_label"><digi:trn>Keyword</digi:trn>:</div>
								</td>
								<td align="left" class="tbl_spacing">
									<input name="" type="text" class="inputx" onkeypress="searchEnter(event)" id="keyword" title="${searchDescription}">
								</td>
						</tr>
						<tr>
								<td class="tbl_spacing">
									<div class="search_label"><digi:trn>Type</digi:trn>:</div>
								</td>
								<td align="left" class="tbl_spacing">
									<select class="dropdwn_sm" id="querytype" >
									<option value="-1"><digi:trn>ALL</digi:trn></option>
									<option value="0"><digi:trn>Activities</digi:trn></option>
									<option value="1"><digi:trn>Reports</digi:trn></option>
									<option value="2"><digi:trn>Tabs</digi:trn></option>
									<option value="3"><digi:trn>Resources</digi:trn></option>
								</select>
	  							</td>
							</tr>
							<tr>
								<td>
									<div class="search_label"><digi:trn>Mode</digi:trn>:</div>
								</td>
								<td align="left">
									<select class="dropdwn_sm" id="searchMode" >
									<option value="0"><digi:trn>Any keyword</digi:trn></option>
									<option value="1"><digi:trn>All keywords</digi:trn></option>
								</select>
	  							</td>
							</tr>
							<tr>
								<td colspan="2" align="center">
									<input onclick="search()" id="searchButton" type="button" class="buttonx" value="<digi:trn>Search</digi:trn>" style="margin-top: 10px">
								</td>
							</tr>
							<tr>
								<td colspan="2" align="center">
									<br />
									<a style="font-size: 11px;" href="/aim/queryEngine.do"><digi:trn>Advanced Search</digi:trn></a>
								</td>
							</tr>
					</table>
				</div>
		</div>
</div>