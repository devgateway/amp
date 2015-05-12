<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ taglib uri="/taglib/jstl-core" prefix="c" %>
<%@ taglib uri="/taglib/digijava" prefix="digi" %>
<%@ taglib uri="/taglib/struts-html" prefix="html" %>
<html style="background:none;">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title></title>
	<digi:ref href="/TEMPLATE/ampTemplate/css_2/amp-wicket.css" type="text/css" rel="stylesheet"/>
	<digi:ref href="/TEMPLATE/ampTemplate/css_2/amp.css" type="text/css" rel="stylesheet" />

<link rel="stylesheet" href="css_2/visualization.css" type="text/css" />
<link rel="stylesheet" type="text/css" href="js_2/yui/tabview/assets/skins/sam/tabview.css">
<digi:ref href="css_2/visualization_yui_tabs.css" type="text/css" rel="stylesheet" />
	<digi:ref href="/TEMPLATE/ampTemplate/css_2/yui_tabs.css" type="text/css" rel="stylesheet" />
	<digi:ref href="/TEMPLATE/ampTemplate/css_2/yui_popins.css" type="text/css" rel="stylesheet" />
	<digi:ref href="/TEMPLATE/ampTemplate/css_2/yui_datatable.css" type="text/css" rel="stylesheet" />
	<digi:ref href="/TEMPLATE/ampTemplate/css_2/desktop_yui_tabs.css" type="text/css" rel="stylesheet" />
	<link rel="stylesheet" type="text/css" href="/TEMPLATE/ampTemplate/js_2/yui/tabview/assets/tabview-core.css"> 

<style>
	.flashcontent {
		border: solid 1px #000;
		margin:5px 0px 0px 0px;
		padding:0px 0px 0px 0px;
		vertical-align:top;
		width:634px;
		height:460px;
		clear: both;
	    background-color: #FFFFFF;
	}
	.side_opt_sel {
		background-color: rgb(191, 210, 223); 
	}	
	.chart_header {
		font-size: 11px;
		padding: 5px 5px 10px 5px;
		margin: 0px 0px 10px 0px;
		font-weight: bold;
	 	border-color: #DADAD6 #C2C1BA #C2C1BA #DADAD6;
	    border-style: solid;
	    border-width: 1px 2px 2px 1px;
	    width:500px;
	    background-color: #FFFFFF;
    }
	.chart_header label {
		font-size: 11px;
    }
    .chartFieldset {
	    background-color: #F4F4F4;
    }	
</style>
<script type="text/javascript" src="/TEMPLATE/ampTemplate/js_2/yui/yahoo-dom-event/yahoo-dom-event.js"></script> 
<script type="text/javascript" src="/TEMPLATE/ampTemplate/js_2/yui/dragdrop/dragdrop-min.js"></script> 
<script type="text/javascript" src="/TEMPLATE/ampTemplate/js_2/yui/animation/animation-min.js"></script> 
<script type="text/javascript" src="/TEMPLATE/ampTemplate/js_2/yui/container/container-min.js"></script> 
<script type="text/javascript" src="/TEMPLATE/ampTemplate/js_2/yui/connection/connection-min.js"></script> 

<script type="text/javascript" src="/TEMPLATE/ampTemplate/js_2/yui/menu/menu-min.js"></script>

<script type="text/javascript" src="/TEMPLATE/ampTemplate/js_2/yui/json/json-min.js"></script> 
<script type="text/javascript" src="/TEMPLATE/ampTemplate/js_2/yui/element/element-min.js"></script> 
<script type="text/javascript" src="/TEMPLATE/ampTemplate/js_2/yui/tabview/tabview-min.js"></script> 
<script type="text/javascript" src="/TEMPLATE/ampTemplate/js_2/yui/selector/selector-min.js"></script> 
<script type="text/javascript" src="/TEMPLATE/ampTemplate/js_2/yui/yahoo/yahoo-min.js"></script> 

<digi:instance property="visualizationform"/>
<c:set var="graph" value="${visualizationform.graphList[0]}" ></c:set>
<c:set var="fromLaunchGraphAction" value="${true}" ></c:set>
<script type="text/javascript">
	var graphId = '${graph.containerId}';
	var trnShowSettings="<digi:trn jsFriendly='true'>Show settings</digi:trn>"; 
	var trnHideSettings="<digi:trn jsFriendly='true'>Hide settings</digi:trn>";
	var trnLoading = '<digi:trn>Loading, please wait...</digi:trn>';
	var trnCancel = '<digi:trn>Cancel</digi:trn>';
	var trnCommitments="<digi:trn jsFriendly='true'>Commitments</digi:trn>";
	var trnDisbursements="<digi:trn jsFriendly='true'>Disbursements</digi:trn>";
	var trnExpenditures="<digi:trn jsFriendly='true'>Expenditures</digi:trn>";
	
</script>

<script type="text/javascript" src="/TEMPLATE/ampTemplate/js_2/visualization/visualization.js"></script> 
<script type="text/javascript" src="/TEMPLATE/ampTemplate/js_2/flash/swfobject.js"></script>
<script type="text/javascript" src="/repository/aim/view/scripts/common.js"></script>

</head>
<body style="background:none;">
<digi:form module="visualization" action="/filters.do">


<div class="yui-content" style="width:635px;">
<%@include file="/repository/visualization/view/graph.jsp" %>
</div>
		
</digi:form>
</body>
</html>