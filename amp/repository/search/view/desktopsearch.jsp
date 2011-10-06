<%@ taglib uri="/taglib/digijava" prefix="digi" %>

<script type="text/javascript">
function search() {
	var trn= '<digi:trn jsFriendly="true">Both fields in the search form must be completed</digi:trn>'
	if ($("#querytype").val() == '' || $("#keyword").val()==''){
		alert(trn);
	}else{
		var url = "/search/search.do?desksearch=true&keyword="+$("#keyword").val()+"&type="+$("#querytype").val()+"&searchMode="+$("#searchMode").val();    
		$(location).attr('href',url);
	}
	return true;
}
</script>
<div class="right_menu">
	<div class="right_menu_header">
		<div class="right_menu_header_cont"><digi:trn>Search</digi:trn></div>
		</div>
			<div class="right_menu_box">
				<div class="right_menu_cont">
				 	<table width="85%" border="0" cellspacing="1px" cellpadding="0">
						<tr>
								<td class="tbl_spacing" align="left">
									<div class="search_label"><digi:trn>Keyword</digi:trn>:</div>
								</td>
								<td align=right class="tbl_spacing">
									<input name="" type="text" class="inputx" style="width:90px;" id="keyword">
								</td>
						</tr>
						<tr>
								<td>
									<div class="search_label"><digi:trn>Type</digi:trn>:</div>
								</td>
								<td align="left">
									<select class="dropdwn_sm" style="width: 90px;" id="querytype" >
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
									<select class="dropdwn_sm" style="width: 90px;" id="searchMode" >
									<option value="0"><digi:trn>Any keyword</digi:trn></option>
									<option value="1"><digi:trn>All keywords</digi:trn></option>
								</select>
	  							</td>
							</tr>
							<tr>
								<td colspan="2" align="center">
									<input onclick="search()" type="button" class="buttonx" value="<digi:trn>Search</digi:trn>" style="margin-top: 10px">
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