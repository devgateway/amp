

<script type="text/javascript" src="<digi:file src="script/yui/element-min.js"/>"></script>
<script type="text/javascript" src="<digi:file src="script/yui/tabview-min.js"/>"></script>


<link rel="stylesheet" type="text/css" href="<digi:file src='module/aim/scripts/tab/assets/tabview.css'/>">


<script type="text/javascript">

	YAHOO.namespace("YAHOO.amp");
	YAHOO.amp.panels	= new Array();
	function initPanel() {
		for (counter=0; counter<YAHOO.amp.panelCounter; counter++) {
			YAHOO.amp.panels[counter]		= new YAHOO.widget.Panel("aPanel"+counter, {
					width:"600px", 
					fixedcenter: true, 
					constraintoviewport: true, 
					underlay:"none", 
					modal: true,
					close:true, 
					visible:false, 
					draggable:true} );
					
			YAHOO.amp.panels[counter].setHeader("Action Panel");
			YAHOO.amp.panels[counter].setBody("Empty");
			YAHOO.amp.panels[counter].setFooter("");
			//var div							= document.getElementById('tableForReportSheet');
			//YAHOO.reportsheet.myPanel.setBody( div );
			//YAHOO.reportsheet.myPanel.render(document.body);
			YAHOO.amp.panels[counter].render(document.body);
		}
	}
	

	//YAHOO.util.Event.addListener(window, "load", initPanel) ;

</script>

<script type="text/javascript">
    var tabView = new YAHOO.widget.TabView("demo"); 
</script>    

<script type="text/javascript">
	YAHOO.namespace("YAHOO.amp");

	function setPanelHeader(panelNum, title) {
		YAHOO.amp.panels[panelNum].setHeader(title);
	}
	function setPanelFooter(panelNum, title) {
		YAHOO.amp.panels[panelNum].setFooter(title);
	}

	function showMyPanel(panelNum, elementId) {
		setPanelBody(panelNum, elementId);
		showPanel(panelNum);
	}
	function showMyPanelCopy(panelNum, elementId) {
		setPanelBodyCopy(panelNum, elementId);
		showPanel(panelNum);
	}
	
	function setPanelBody(panelNum, elementId){
		var element				= document.getElementById(elementId);
		element.style.display	= "inline";
		element.parentNode.removeChild(element);
		YAHOO.amp.panels[panelNum].setBody(element);
		
	}
	function setPanelBodyCopy(panelNum, elementId){
		var element				= document.getElementById(elementId);
		YAHOO.amp.panels[panelNum].setBody(element.innerHTML);
	}
	function showPanel (panelNum) {
		YAHOO.amp.panels[panelNum].show();
	}
	function hidePanel (panelNum) {
		YAHOO.amp.panels[panelNum].hide();
	}
	
	YAHOO.amp.panelCounter	= 3;

	YAHOO.util.Event.addListener(window, "load", initPanel) ;

</script>
