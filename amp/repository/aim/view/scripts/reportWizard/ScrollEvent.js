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
	
	onScrollHide(this.markerElId, this);
	onScrollShow(this.markerElId, this);
}

KeepWithScroll.prototype.onHide	= function( arguments ) {
	if ( !this.hidden ) {
		this.hidden		= true;
		this.keeperEl.style.position	= "fixed";
		this.keeperEl.style.top			= "3px";
		this.keeperEl.style.left		= this.keeperElX;
		this.keeperEl.style.backgroundColor	= "white";
//		alert("hidden");
	}
};

KeepWithScroll.prototype.onShow	= function( arguments ) {
	if ( this.hidden ) {
		this.hidden		= false;
		this.keeperEl.style.position	= "";
		this.keeperEl.style.top			= "";
		this.keeperEl.style.left		= "";
		this.keeperEl.style.backgroundColor	= "";
//		alert("shown");
	}
};