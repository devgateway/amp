YAHOO.widget.WicketDataSource = function(callbackUrl) {
    this.callbackUrl = callbackUrl;
    this.responseArray = [];
    this.transactionId = 0;
    this.queryMatchContains = true;
};

YAHOO.widget.WicketDataSource.prototype = new YAHOO.util.LocalDataSource();

YAHOO.widget.WicketDataSource.prototype.makeConnection = function(oRequest, oCallback, oCaller) {
    var tId = this.transactionId++;
    this.fireEvent("requestEvent", {tId: tId, request: oRequest, callback: oCallback, caller: oCaller});
    var _this = this;
    var onWicketSuccessFn = function() {
        _this.handleResponse(oRequest, _this.responseArray, oCallback, oCaller, tId);
    };    
    wicketAjaxGet(this.callbackUrl + '&q=' + oRequest, onWicketSuccessFn);
};

function preg_quote( str ) {
    //http://kevin.vanzonneveld.net - replace all regex chars with escaped versions
    return (str+'').replace(/([\\\.\+\*\?\[\^\]\$\(\)\{\}\=\!\<\>\|\:])/g, "\\$1");
};


YAHOO.widget.WicketAutoComplete = function(inputId, callbackUrl, containerId, toggleButtonId,indicatorId) {
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
    this.autoComplete.formatResult = function(pResultData, pQuery, pResultMatch) {
    	if(pQuery=="") return pResultMatch;
    	return pResultMatch.replace( new RegExp( "(" + preg_quote( pQuery ) + ")" , 'gi' ), "<b><u>$1</u></b>" );
    }; 
    var autoComplete=this.autoComplete;
    //handler for selected items
    this.itemSelectHandler = function(sType, aArgs) {
    	var oData = aArgs[2]; // object literal of data for the result
    	// Since its name is being dynamically generated, always ensure your function actually exists
    	var callWicketFuncName="callWicket"+inputId;
    	if (typeof(window[callWicketFuncName]) === "function")  window[callWicketFuncName](oData);
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
		var myAutoComp = aArgs[0];
		var sQuery = aArgs[1];
		var aResults = aArgs[2];
		var mySpan = YAHOO.util.Dom.get(indicatorId);
		if (aResults.length == 0) {
			mySpan.style.display = 'block';
			mySpan.innerHTML = "No results found!";
		} else {
			mySpan.style.display = 'none';
		}
	};
	this.autoComplete.dataReturnEvent.subscribe(myOnDataReturn);
	//show loading icon while list is loading
	var myDataRequestEvent = function(ev, aArgs) {
		var ac = aArgs[0];
		var mySpan = YAHOO.util.Dom.get(indicatorId);
		mySpan.style.display = 'block';
		
		mySpan.innerHTML = '<span id="'+indicatorId+'"><img src="/TEMPLATE/ampTemplate/js_2/yui/carousel/assets/skins/sam/ajax-loader.gif" /></span>';
	};
	this.autoComplete.dataRequestEvent.subscribe(myDataRequestEvent);
};
