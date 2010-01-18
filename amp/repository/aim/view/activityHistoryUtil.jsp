
<div id="myHistory" style="display: none">
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
		var msgP6='\n<digi:trn key="aim:previewHistory">History</digi:trn>';
		myPanelHistory.setHeader(msgP6);
		myPanelHistory.setBody("");
		myPanelHistory.render(document.body);
		panelFirstShow = 1;
	}
	
	function showHistory() {
		var content = document.getElementById("myHistoryContent");
		var element6 = document.getElementById("myHistory"); 
		content.innerHTML = '<br/><br/><br/><br/><br/><br/><br/><br/><br/><br/><br/><br/><p align="center"><img align="top" src="/repository/aim/view/scripts/ajaxtabs/loading.gif" /><font size="3"><b>Loading...</b></font></p>';
		//if (panelFirstShow == 1){ 
			element6.style.display = "inline";
			myPanelHistory.setBody(element6);
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
	}	 
	function submitCompare() {
		var checkboxes = document.getElementsByName("compareCheckboxes");
		var selectedVersions = [];
		for(var i = 0; i < checkboxes.length; i++) {
			if(checkboxes[i].checked) selectedVersions.push(checkboxes[i]);
		}
		if(selectedVersions.length = 2){
			document.getElementById("activityOneId").value = selectedVersions[0].value;
			document.getElementById("activityTwoId").value = selectedVersions[1].value;
			document.aimCompareActivityVersionsForm.submit();
		}
	}
	function monitorCheckbox(){
		var checkboxes = document.getElementsByName("compareCheckboxes");
		var counter = 0;
		for(var i = 0; i < checkboxes.length; i++) {
			if(checkboxes[i].checked) counter++;
		}
		if (counter != 2)
		{
			document.getElementById("SubmitButton").disabled = true;
			document.getElementById("SubmitButton").style.color = "#CECECE";
		}
		else
		{
			document.getElementById("SubmitButton").disabled = false;
			document.getElementById("SubmitButton").style.color = "Black";
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
			YAHOOAmp.util.Connect.asyncRequest("POST", "/aim/viewActivityHistory.do", historyCallback, postString);
		}
	}

	var currentHistory = window.onload;
	addLoadEvent(function() {
        currentHistory.apply(currentHistory);
   	});
   	addLoadEvent(initScriptsHistory);



function setStripsTable(tableId, classOdd, classEven) {
	var tableElement = document.getElementById(tableId);
	rows = tableElement.getElementsByTagName('tr');
	for(var i = 0, n = rows.length; i < n; ++i) {
		if(i%2 == 0)
			rows[i].className = classEven;
		else
			rows[i].className = classOdd;
	}
	rows = null;
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
