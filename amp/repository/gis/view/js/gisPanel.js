
	YAHOO.namespace("YAHOO.amp");
	YAHOO.amp.panels	= new Array();
	var gisPanel;
	function initPanel() {
		gisPanel = new YAHOO.widget.Panel("myGisPanel", {
			width:"600px", 
			height:"550px",
			fixedcenter: true, 
			constraintoviewport: true, 
			underlay:"shadow", 
			modal: true,
			close:true, 
			visible:false, 
			draggable:true} );

		gisPanel.setHeader("Map Filter");
		gisPanel.setBody("Empty");
		gisPanel.setFooter("");
		gisPanel.render(document.body);		
	}
	

	//YAHOO.util.Event.addListener(window, "load", initPanel) ;


	YAHOO.namespace("YAHOO.amp");

	function setPanelHeader(panelNum, title) {
		gisPanel.setHeader(title);
	}
	function setPanelFooter(panelNum, title) {
		gisPanel.setFooter(title);
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
		gisPanel.setBody(element);
	}
	function setPanelBodyCopy(panelNum, elementId){
		var element				= document.getElementById(elementId);
		gisPanel.setBody(element.innerHTML);
	}
	function showPanel (panelNum) {
		//gisPanel.setBody(document.getElementById("filter_dialog"));
		gisPanel.show();
	}
	function hidePanel (panelNum) {
		gisPanel.hide();
	}
