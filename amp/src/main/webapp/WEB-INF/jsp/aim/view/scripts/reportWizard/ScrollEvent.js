function onScrollShow (el, callback) {
    var y = YAHOO.util.Dom.getY(el);
    function  cb() {
        var top = YAHOO.util.Dom.getDocumentScrollTop();
        var vHeight = YAHOO.util.Dom.getViewportHeight();
        var view = parseInt(vHeight + top);

        if ( view >= y && y >= top) {
        	callback.onShow();
        }

    }

    YAHOO.util.Event.on(window, 'scroll', cb);

};

function onScrollHide(el, callback) {
    var y = YAHOO.util.Dom.getY(el);
    function  cb() {
        var top = YAHOO.util.Dom.getDocumentScrollTop();
        var vHeight = YAHOO.util.Dom.getViewportHeight();
        var view = parseInt(vHeight + top);

        if ( view < y || y < top ) {
        	callback.onHide();
        }

    }

    YAHOO.util.Event.on(window, 'scroll', cb);

};


function KeepWithScroll (markerElId, keeperElId) {
	this.markerElId		= markerElId;
	this.keeperElId		= keeperElId;
	
	this.hidden			= false;
	
	this.keeperEl		= document.getElementById(this.keeperElId);
	this.keeperElX		= YAHOO.util.Dom.getX(this.keeperEl);
	this.keeperElWidth	= YAHOO.util.Dom.getRegion(this.keeperEl).width + 10;
	//alert(this.keeperElWidth);
	
	onScrollHide(this.markerElId, this);
	onScrollShow(this.markerElId, this);
}

KeepWithScroll.prototype.onHide	= function( arguments ) {
	if (YAHOO.env.ua.ie > 0) {
		var top = YAHOO.util.Dom.getDocumentScrollTop();
		//var vHeight = YAHOO.util.Dom.getViewportHeight();
		var newTop	= top + 10 ;
		
		this.hidden		= true;
		this.keeperEl.style.position	= "absolute";
		this.keeperEl.style.width		= this.keeperElWidth + "px";
		//this.keeperEl.style.float		= "";
		this.keeperEl.style.top			= newTop + "px";
		this.keeperEl.style.left		= (this.keeperElX-10) + "px";
		this.keeperEl.style.backgroundColor	= "white";
	}
	if ( !this.hidden ) {
		this.hidden		= true;
		this.keeperEl.style.position	= "fixed";
		//this.keeperEl.style.float		= "";
		this.keeperEl.style.top			= "3px";
		this.keeperEl.style.left		= this.keeperElX + "px";
		this.keeperEl.style.backgroundColor	= "white";
	//	alert(this.keeperElX);
	//	alert("hidden");
	}
};

KeepWithScroll.prototype.onShow	= function( arguments ) {
	if ( this.hidden ) {
		this.hidden		= false;
		this.keeperEl.style.position	= "";
		this.keeperEl.style.top			= "";
		this.keeperEl.style.left		= "";
		this.keeperEl.style.backgroundColor	= "";
	//	alert("shown");
	}
};
