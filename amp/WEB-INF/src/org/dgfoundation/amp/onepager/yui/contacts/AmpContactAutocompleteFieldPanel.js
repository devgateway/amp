var informationPanel=null;
///* Function that is executed when mouse over an element */
function eventFunction(e, bodyText) {
    var x = 0;
    var y = 0;
    if (e.pageX || e.pageY) 	{
        x = e.pageX;
        y = e.pageY;
    }
    else if (e.clientX || e.clientY) 	{
        x = e.clientX + document.body.scrollLeft;
        y = e.clientY + document.body.scrollTop;
    }

	showPanel(bodyText, x,  y);
}

///* Updates the panels header, body and position and makes it visible */
function showPanel(bodyText, posX, posY) {
	if(informationPanel==null){
		informationPanel= new YAHOO.widget.Panel("infoPanel", { width:"300px", visible:false, draggable:false, close:false } );
		informationPanel.render("additionalDetailsContainer");
	}
	informationPanel.setBody(bodyText);
	informationPanel.moveTo(posX+2, posY+2);
	informationPanel.show();
}

///* Just makes the panel invisible */
function hidePanel() {
	if(informationPanel!=null){
		informationPanel.hide();
	}
}
function convertResult(str){
    return str.replace(/</g, "&lt;").replace(/>/g, "&gt;")
}


YAHOO.widget.WicketContactAutoComplete = function(inputId, callbackUrl, containerId, toggleButtonId,indicatorId, useCache, isDisabled) {
    this.dataSource = new YAHOO.widget.WicketDataSource(callbackUrl);
    this.autoComplete = new YAHOO.widget.AutoComplete(inputId, containerId, this.dataSource);
    this.autoComplete.prehighlightClassName = "yui-ac-prehighlight";
    this.autoComplete.useShadow = true;
    this.autoComplete.animVert=.01;
    this.autoComplete.animHoriz=.01;
    this.autoComplete.animSpeed=0.5;
    this.autoComplete.minQueryLength=0;
    this.autoComplete.queryDelay=1;
    this.autoComplete.forceSelection=true;
    this.autoComplete.maxResultsDisplayed = 1000;
    this.autoComplete.applyLocalFilter=false;
    this.autoComplete.formatEscapedResult = function(pResultData, pQuery, pResultMatch) {
    	var formatedResult;
    	var escapedResultData=pResultData[1];
    	
    	if(pQuery==""){
    		formatedResult=ac_left_padding(convertResult(pResultMatch),escapedResultData);
    	}
    	else{
    		formatedResult=ac_left_padding(convertResult(pResultMatch).replace( new RegExp( "(" + ac_preg_quote( pQuery ) + ")" , 'gi' ), "<b><u>$1</u></b>" ),escapedResultData);
    	}
    	
    	if(pResultData[2]!=''){
    		formatedResult= "<span onmouseover=\"eventFunction(event,'"+pResultData[2]+"')\" onmouseout=\"hidePanel()\">"+formatedResult+"</span>";
    	}
    	if(pResultData[3]!=''){
    		formatedResult="<span class='"+pResultData[3]+"'>" +formatedResult+"</span>";
    	}
    	return formatedResult;
    }; 
    
    
    // Enable HTML-escaping of result strings
    this.autoComplete.formatResult =  this.autoComplete.formatEscapedResult;
    
    var autoComplete=this.autoComplete;
    //handler for selected items
    this.itemSelectHandler = function(sType, aArgs) {
    	ac_show_loading(indicatorId,inputId,toggleButtonId);
    	hidePanel();
    	var oData = aArgs[2];
    	//Function name is dynamic, ensure it exists
    	var callWicketFuncName="callWicket"+inputId;
    	if (typeof(window[callWicketFuncName]) === "function")  window[callWicketFuncName](oData[4]);
    	 else throw("Error.  Function " + callWicketFuncName + " does not exist.");
    };
    this.autoComplete.itemSelectEvent.subscribe(this.itemSelectHandler);//subscribes this handler to YUI autocomplete
    var bToggler = YAHOO.util.Dom.get(toggleButtonId);
    var oPushButtonB;
    if (isDisabled)
        oPushButtonB = new YAHOO.widget.Button({container:bToggler, disabled:true});
    else
        oPushButtonB = new YAHOO.widget.Button({container:bToggler});

    var toggleB = function(e) {
        if(!YAHOO.util.Dom.hasClass(bToggler, "open")) {
            YAHOO.util.Dom.addClass(bToggler, "open");
        }
        
        if(autoComplete.isContainerOpen()) {
        	autoComplete.collapseContainer();
        }
        else {
        	//autoComplete.getInputEl().focus(); // Needed to keep widget active
			this._bFocused = true; // For Chrome
            setTimeout(function() { // For IE
            	autoComplete.sendQuery("");
            },0);
        }
    };
    oPushButtonB.on("click", toggleB);
    autoComplete.containerCollapseEvent.subscribe(function(){YAHOO.util.Dom.removeClass(bToggler, "open");});   
    // Show custom message if no results found
	var myOnDataReturn = function(sType, aArgs) {
		var aResults = aArgs[2];
		var mySpan = YAHOO.util.Dom.get(indicatorId);
		YAHOO.util.Dom.get(inputId).disabled=false;
		YAHOO.util.Dom.get(toggleButtonId).disabled=false;
		if (aResults.length == 0) {
			mySpan.style.display = 'block';
			mySpan.innerHTML = "${noResults}!";
		} else {
			mySpan.style.display = 'none';
		}
	};
	this.autoComplete.dataReturnEvent.subscribe(myOnDataReturn);
	//show loading icon while list is loading
	var myDataRequestEvent = function(ev, aArgs) {
		ac_show_loading(indicatorId,inputId,toggleButtonId);
	};
	this.autoComplete.dataRequestEvent.subscribe(myDataRequestEvent);
};

