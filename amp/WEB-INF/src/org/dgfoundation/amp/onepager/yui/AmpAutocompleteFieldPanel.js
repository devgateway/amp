YAHOO.widget.WicketAutoComplete = function(inputId, callbackUrl, containerId, toggleButtonId,indicatorId, useCache) {
    this.dataSource = new YAHOO.widget.WicketDataSource(callbackUrl);
    if(useCache) {
    	this.dataSource.maxCacheEntries = 100;
    	this.dataSource.queryMatchSubset = true;	
    }
    this.autoComplete = new YAHOO.widget.AutoComplete(inputId, containerId, this.dataSource);
    this.autoComplete.prehighlightClassName = "yui-ac-prehighlight";
    this.autoComplete.highlightClassName = ""; 
    this.autoComplete.useShadow = true;
    this.autoComplete.animVert=.01;
    this.autoComplete.animHoriz=.01;
    this.autoComplete.animSpeed=0.5;
    this.autoComplete.minQueryLength=0;
    this.autoComplete.queryDelay=1;
    this.autoComplete.forceSelection=true;
    this.autoComplete.maxResultsDisplayed = 1000;
    this.autoComplete.formatEscapedResult = function(pResultData, pQuery, pResultMatch) {
    	var formatedResult;
    	var escapedResultData=pResultData[1];
    	
    	if(pQuery==""){
    		formatedResult=ac_left_padding(pResultMatch,escapedResultData);
    	}
    	else{
    		formatedResult=ac_left_padding(pResultMatch.replace( new RegExp( "(" + ac_preg_quote( pQuery ) + ")" , 'gi' ), "<b><u>$1</u></b>" ),escapedResultData);
    	}
    	
    	return formatedResult;
    }; 
    
    
    // Enable HTML-escaping of result strings
    this.autoComplete.formatResult =  this.autoComplete.formatEscapedResult;
    
    var autoComplete=this.autoComplete;
    //handler for selected items
    this.itemSelectHandler = function(sType, aArgs) {
    	ac_show_loading(indicatorId,inputId,toggleButtonId);
    	//hidePanel();
    	var oData = aArgs[2];
    	//Function name is dynamic, ensure it exists
    	var callWicketFuncName="callWicket"+inputId;
    	if (typeof(window[callWicketFuncName]) === "function")  window[callWicketFuncName](encodeURIComponent(oData[0]));
    	 else throw("Error.  Function " + callWicketFuncName + " does not exist.");
    };
    this.autoComplete.itemSelectEvent.subscribe(this.itemSelectHandler);//subscribes this handler to YUI autocomplete
    var bToggler = YAHOO.util.Dom.get(toggleButtonId);
    var oPushButtonB = new YAHOO.widget.Button({container:bToggler});
    var toggleB = function(e) {
        if(!YAHOO.util.Dom.hasClass(bToggler, "open")) {
            YAHOO.util.Dom.addClass(bToggler, "open");
        }
        
        if(autoComplete.isContainerOpen()) {
        	autoComplete.collapseContainer();
        }
        else {
        	autoComplete.getInputEl().focus(); // Needed to keep widget active
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

