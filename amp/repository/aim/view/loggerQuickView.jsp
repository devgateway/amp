<%@ page pageEncoding="UTF-8"%>

<%@ taglib uri="/taglib/digijava" prefix="digi" %>

<style>

.quickView {
    border: 1;
    width: 100%;
    text-align: center;
    border: 1px solid #000000; 
    border-right: none; 
}

div.quickView span  {
    font-size:25pt;
    font-weight: bold;
    white-space:nowrap;
    display: block;
    padding-top:20px;
}

div.quickView div {
	height: 120px;
	border-right: 1px solid #000000; 
}

.column-left {float: left; width: 33%; border-right: 0;}
.column-right {float: right; width: 34%; border-left: 0;}
.column-center {display: inline-block; width: 33%; }

</style>

<script language="javascript">

$.ajax({
	  url: "/rest/scorecard/quickStats",
	  method: "GET",
	  context: document.body,
	  dataType: "text",
}).done(function(data) {
	var result = JSON.parse(data);
	$('#countOrgs').text(result.organizations);
	$('#countUsers').text(result.users);
	$('#countProjects').text(result.projects);
}).fail(function (jqXHR, textStatus) {
	var errorText = "<digi:trn jsFriendly='true'>Application error</digi:trn>";
	$('#countOrgs').text(errorText);
    $('#countUsers').text(errorText);
    $('#countProjects').text(errorText);
});
	
</script>

<div class="quickView">
	<div class="column-left">
		<br/><digi:trn>Active Organizations Past Quarter</digi:trn>
		<span id="countOrgs"></span>
	</div>
   	<div class="column-center">
		<br/><digi:trn>Users Logged into the System Past Quarter</digi:trn>
		<span id="countUsers"></span>
	</div>
   <div class="column-right">
   		<br/><digi:trn>Number of Projects with Action Past Quarter</digi:trn>
		<span id="countProjects"></span>
   </div>
</div>
