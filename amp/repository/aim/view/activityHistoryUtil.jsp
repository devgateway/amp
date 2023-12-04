<%@ taglib uri="/taglib/digijava" prefix="digi"%>


<script type="text/javascript" src="<digi:file src='js_2/yui/yahoo/yahoo-min.js'/>">.</script>
<script language="JavaScript" type="text/javascript" src="<digi:file src='js_2/yui/yahoo-dom-event/yahoo-dom-event.js'/>">.</script>
<script language="JavaScript" type="text/javascript" src="<digi:file src='js_2/yui/container/container-min.js'/>" >.</script>
<script language="JavaScript" type="text/javascript" src="<digi:file src='js_2/yui/element/element.js'/>" >.</script>
<script type="text/javascript" src="<digi:file src='js_2/yui/event/event-min.js'/>">.</script>
<script type="text/javascript" src="<digi:file src='js_2/yui/animation/animation-min.js'/>" >.</script>
<script type="text/javascript" src="<digi:file src='js_2/yui/dom/dom-min.js'/>">.</script>
<script language="JavaScript" type="text/javascript" src="<digi:file src='js_2/yui/tabview/tabview.js'/>" >.</script>
<script language="JavaScript" type="text/javascript" src="<digi:file src='js_2/yui/connection/connection-min.js'/>" > .</script>
<link rel="stylesheet" type="text/css" href="<digi:file src='js_2/yui/tabview/assets/tabview.css'/>">
	<link rel="stylesheet" type="text/css" href="<digi:file src='js_2/yui/tabview/assets/border_tabs.css'/>">
	<link rel="stylesheet" type="text/css" href="<digi:file src='module/aim/css/reportWizard/reportWizard.css'/>">
	<link rel="stylesheet" type="text/css" href="<digi:file src='/repository/aim/view/css/filters/filters2.css'/>">

	<link rel="stylesheet" type="text/css" href="<digi:file src='css_2/amp.css '/>">

<div id="myHistory" class="invisible-item">
	<div id="myHistoryContent" class="content">
		===== ERROR =====
	</div>
</div>


<script type="text/javascript">
	YAHOOAmp.namespace("YAHOOAmp.amptab");
	YAHOOAmp.amptab.init = function() {
	    		var tabView = new YAHOOAmp.widget.TabView('tabview_container');
	};
		
    var myPanelHistory = new YAHOOAmp.widget.Panel("newmyHistory", {
		width:"800px",
        height:"510px",
	    fixedcenter: true,
	    constraintoviewport: true,
	    underlay:"none",
	    close:true,
	    visible:false,
	    modal:true,
	    draggable:true,
	    context: ["showbtn", "tl", "bl"] 
	    }
	     );
	
	function initScriptsHistory() {
		var msgP6='\n<digi:trn jsFriendly="true">Version History</digi:trn>';
		myPanelHistory.setHeader(msgP6);
		myPanelHistory.setBody("");
		myPanelHistory.render(document.body);
		panelFirstShow = 1;
	}
	
	function showHistory() {
		var content = document.getElementById("myHistoryContent");
		var element6 = document.getElementById("myHistory");
        var loading='\n<digi:trn jsFriendly="true">Loading...</digi:trn>';
		content.innerHTML = '<p align="center"><img align="top" src="/TEMPLATE/ampTemplate/imagesSource/loaders/ajax-loader.gif" /><font size="3"><b>'+loading+'</b></font></p>';
		//if (panelFirstShow == 1){
			myPanelHistory.setBody(element6.innerHTML);
			panelFirstShow = 0;
		//}
		document.getElementById("myHistoryContent").scrollTop=0;
		myPanelHistory.show();
	}
	function hideHistory() {
		myPanelHistory.hide();
	}


    var responseSuccessHistory = function(o){ 
	/* Please see the Success Case section for more
	 * details on the response object's properties.
	 * o.tId
	 * o.status
	 * o.statusText
	 * o.getResponseHeader[ ]
	 * o.getAllResponseHeaders
	 * o.responseText
	 * o.responseXML
	 * o.argument
	 */
		var response = o.responseText; 
		var content = document.getElementById("myHistoryContent");
		content.innerHTML = response;
		postsuccessScripts();
	}
	function postsuccessScripts(){
		setStripsTable("dataTable", "tableEven", "tableOdd");
		setHoveredTable("dataTable", true);
		setHoveredRow("rowHighlight");
		monitorCheckbox();
        checkVersions();
	}	 
	function setVersion(activityId)
	{
		document.getElementById("action").value = "setVersion";
		document.getElementById("method").value = "compare";
		document.getElementById("activityCurrentVersion").value = activityId;
		document.aimCompareActivityVersionsForm.submit();
	}
	function submitCompare() {
		document.getElementById("action").value = "compare";
		document.getElementById("method").value = "compare";
		var checkboxes = document.getElementsByName("compareCheckboxes");
		var selectedVersions = [];
		for(var i = 0; i < checkboxes.length; i++) {
			if(checkboxes[i].checked) selectedVersions.push(checkboxes[i]);
		}
		if(selectedVersions.length = 2){
			document.getElementById("activityOneId").value = selectedVersions[0].value;
			document.getElementById("activityTwoId").value = selectedVersions[1].value;
			document.getElementById('showMergeColumn').value = "false";
			document.getElementById("ampActivityId").value = <%=request.getParameter("ampActivityId")%>;
			document.aimCompareActivityVersionsForm.submit();
		}
	}

	function submitChangeSummary() {
        document.aimViewActivityHistoryForm.actionMethod.value = "changesSummary";
		document.aimViewActivityHistoryForm.submit();
	}

	function monitorCheckbox(){
		var checkboxes = document.getElementsByName("compareCheckboxes");
		var counter = 0;
		for(var i = 0; i < checkboxes.length; i++) {
			if(checkboxes[i].checked) counter++;
		}
		if (counter != 2)
		{
		    if (document.getElementById("SubmitButton")) {
				document.getElementById("SubmitButton").disabled = true;
				document.getElementById("SubmitButton").style.color = "#CECECE";
            }
		}
		else
		{
            if (document.getElementById("SubmitButton")) {
                document.getElementById("SubmitButton").disabled = false;
                document.getElementById("SubmitButton").style.color = "Black";
            }
		}
	}

    function checkVersions(){
        var ids = document.getElementsByName("summaryChangesIds");
        var counter = 0;
        for(var i = 0; i < ids.length; i++) {
            counter++;
        }
        if (counter > 1)
        {
            if (document.getElementById("SubmitSummaryButton")) {
                document.getElementById("SubmitSummaryButton").disabled = false;
                document.getElementById("SubmitSummaryButton").style.color = "Black";
			}
        } else {
            if (document.getElementById("SubmitSummaryButton")) {
                document.getElementById("SubmitSummaryButton").disabled = true;
                document.getElementById("SubmitSummaryButton").style.color = "#CECECE";
            }
		}
    }

	var responseFailureHistory = function(o){ 
	// Access the response object's properties in the 
	// same manner as listed in responseSuccess( ). 
	// Please see the Failure Case section and 
	// Communication Error sub-section for more details on the 
	// response object's properties.
		alert("Connection Failure!"); 
	}  
	var historyCallback = 
	{ 
		success:responseSuccessHistory, 
		failure:responseFailureHistory 
	};
    
	function previewHistory(id)
	{
        var postString = "activityId=" + id;
        showHistory();
        YAHOOAmp.util.Connect.asyncRequest("POST", "/aim/viewActivityHistory.do", historyCallback, postString);
	}
	
	function previewHistoryClicked() {
		var flag = validateForm();
		if (flag == true) {
	        var postString		= "edit=true&logframe=true&currentlyEditing=true&step=9&pageId=1";
	        showHistory();
			Rutil.Connect.asyncRequest("POST", "/aim/viewActivityHistory.do", historyCallback, postString);
		}
	}

	var currentHistory = window.onload;
	addLoadEvent(function() {
        currentHistory.apply(currentHistory);
   	});
   	addLoadEvent(initScriptsHistory);



function setStripsTable(tableId, classOdd, classEven) {
	var tableElement = document.getElementById(tableId);
	if (tableElement) {
		rows = tableElement.getElementsByTagName('tr');
		for(var i = 0, n = rows.length; i < n; ++i) {
			if(i%2 == 0)
				rows[i].className = classEven;
			else
				rows[i].className = classOdd;
		}
		rows = null;
	}
}
function setHoveredTable(tableId, hasHeaders) {

	var tableElement = document.getElementById(tableId);
	if(tableElement){
    	var className = 'Hovered',
        pattern   = new RegExp('(^|\\s+)' + className + '(\\s+|$)'),
        rows      = tableElement.getElementsByTagName('tr');
		
		for(var i = 0, n = rows.length; i < n; ++i) {
			rows[i].onmouseover = function() {
				this.className += ' ' + className;
			};
			rows[i].onmouseout = function() {
				this.className = this.className.replace(pattern, ' ');

			};
		}
		rows = null;
	}
}


function setHoveredRow(rowId) {

	var rowElement = document.getElementById(rowId);
	if(rowElement){
    	var className = 'Hovered',
        pattern   = new RegExp('(^|\\s+)' + className + '(\\s+|$)'),
        cells      = rowElement.getElementsByTagName('td');

		for(var i = 0, n = cells.length; i < n; ++i) {
			cells[i].onmouseover = function() {
				this.className += ' ' + className;
			};
			cells[i].onmouseout = function() {
				this.className = this.className.replace(pattern, ' ');

			};
		}
		cells = null;
	}
}

</script>
<style type="text/css">
	#myHistory .content { 
	    overflow:auto; 
	    height:455px; 
	    background-color:fff; 
	    padding:10px; 
	} 
.tableEven {
	background-color: #dbe5f1;
	font-size: 8pt;
	padding: 2px;
}

.tableOdd {
	background-color: #FFFFFF;
	font-size: 8pt;
	padding: 2px;
}

.Hovered {
	background-color: #a5bcf2;
}

.notHovered {
	background-color: #FFFFFF;
}
	
</style>
