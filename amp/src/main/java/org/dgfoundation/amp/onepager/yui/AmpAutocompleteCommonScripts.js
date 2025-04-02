YAHOO.widget.WicketDataSource = function(callbackUrl) {
    this.callbackUrl = callbackUrl;
    this.responseArray = [];
    this.transactionId = 0;
    this.queryMatchContains = true;
};

var levelColors = ["222222", "444444", "666666", "888888", "AAAAAA", "BBBBBB", "CCCCCC"];

YAHOO.widget.WicketDataSource.prototype = new YAHOO.util.LocalDataSource();

YAHOO.widget.WicketDataSource.prototype.makeConnection = function(oRequest, oCallback, oCaller) {
    var tId = this.transactionId++;
    this.fireEvent("requestEvent", {tId: tId, request: oRequest, callback: oCallback, caller: oCaller});
    var _this = this;
    var onWicketSuccessFn = function() {
        _this.handleResponse(oRequest, _this.responseArray, oCallback, oCaller, tId);
    };    
    var callUrl=this.callbackUrl + '&q=' + oRequest;
    Wicket.Ajax.get({"u":callUrl, "coh": [onWicketSuccessFn]});
};

function ac_preg_quote( str ) {
    //http://kevin.vanzonneveld.net - replace all regex chars with escaped versions
    return (str+'').replace(/([\\\.\+\*\?\[\^\]\$\(\)\{\}\=\!\<\>\|\:])/g, "\\$1");
};

//left padding a string + colorize siblings
function ac_left_padding(str, level) {
    return (level > 0 ? "<span style='color:#" + levelColors[level] + "'>" : "") + Array(level * 3).join("&nbsp;") + str + (level > 0 ? "</span>" : "");
};

//show loading icon while list is loading
function ac_show_loading(indicatorId,inputId,toggleButtonId) {
	var mySpan = YAHOO.util.Dom.get(indicatorId);
	mySpan.style.display = 'block';
	if (inputId && YAHOO.env.ua.ie <= 0 && /chrom(e|ium)/.test(navigator.userAgent.toLowerCase()) != true)
		YAHOO.util.Dom.get(inputId).disabled=true;
	YAHOO.util.Dom.get(toggleButtonId).disabled=true;
	mySpan.innerHTML = '<span id="'+indicatorId+'"><img src="/TEMPLATE/ampTemplate/js_2/yui/carousel/assets/skins/sam/ajax-loader.gif" align="left" /></span>';	
};
