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

function ac_preg_quote( str ) {
    //http://kevin.vanzonneveld.net - replace all regex chars with escaped versions
    return (str+'').replace(/([\\\.\+\*\?\[\^\]\$\(\)\{\}\=\!\<\>\|\:])/g, "\\$1");
};

//left padding a string + colorize siblings
function ac_left_padding(str,level) {
	var color=222+(222*level);
   return (level>0?"<span style='color:#"+color+"'>":"")+Array(level*3).join("&nbsp;")+str+(level>0?"</span>":"");
};


/* Function that is executed when mouse over an element */
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

/* Updates the panels header, body and position and makes it visible */
function showPanel(bodyText, posX, posY) {
	informationPanel.setBody(bodyText);
	informationPanel.moveTo(posX+2, posY+2);
	informationPanel.show();
}

/* Just makes the panel invisible */
function hidePanel() {
	informationPanel.hide();
}


YAHOO.widget.WicketAutoComplete = function(inputId, callbackUrl, containerId, toggleButtonId,indicatorId, useCache,applyLocalFilter) {
    this.dataSource = new YAHOO.widget.WicketDataSource(callbackUrl);
    if(useCache) {
    	this.dataSource.maxCacheEntries = 100;
    	this.dataSource.queryMatchSubset = true;	
    }
    this.autoComplete = new YAHOO.widget.AutoComplete(inputId, containerId, this.dataSource);
    this.autoComplete.prehighlightClassName = "yui-ac-prehighlight";
    this.autoComplete.useShadow = true;
    this.autoComplete.animVert=.01;
    this.autoComplete.animHoriz=.01;
    this.autoComplete.animSpeed=0.5;
    this.autoComplete.minQueryLength=2;
    this.autoComplete.queryDelay=1;
    this.autoComplete.forceSelection=true;
    this.autoComplete.maxResultsDisplayed = 1000;
    this.autoComplete.applyLocalFilter=applyLocalFilter; // for adding custom data in resultset
    this.autoComplete.formatResult = function(pResultData, pQuery, pResultMatch) {
    	var formatedResult;
    	if(pQuery==""){
    		formatedResult=ac_left_padding(pResultMatch,pResultData[1]);
    	}
    	else{
    		formatedResult=ac_left_padding(pResultMatch.replace( new RegExp( "(" + ac_preg_quote( pQuery ) + ")" , 'gi' ), "<b><u>$1</u></b>" ),pResultData[1]);
    	}   	
    	if(pResultData[2]!=''){
    		return "<span onmouseover=\"eventFunction(event,'"+pResultData[2]+"')\" onmouseout=\"hidePanel()\">"+formatedResult+"</span>";
    	}
    	return formatedResult;
    }; 
    var autoComplete=this.autoComplete;
    //handler for selected items
    this.itemSelectHandler = function(sType, aArgs) {
    	ac_show_loading(indicatorId,inputId,toggleButtonId);
    	var oData = aArgs[2];
    	//Function name is dynamic, ensure it exists
    	var callWicketFuncName="callWicket"+inputId;
    	if (typeof(window[callWicketFuncName]) === "function")  window[callWicketFuncName](oData[0]);
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
			mySpan.innerHTML = "No results found!";
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

//show loading icon while list is loading
function ac_show_loading(indicatorId,inputId,toggleButtonId) {
	var mySpan = YAHOO.util.Dom.get(indicatorId);
	mySpan.style.display = 'block';
	YAHOO.util.Dom.get(inputId).disabled=true;
	YAHOO.util.Dom.get(toggleButtonId).disabled=true;
	mySpan.innerHTML = '<span id="'+indicatorId+'"><img src="/TEMPLATE/ampTemplate/js_2/yui/carousel/assets/skins/sam/ajax-loader.gif" /></span>';	
};
