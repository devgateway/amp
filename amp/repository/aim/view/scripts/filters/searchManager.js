function SearchManager(inputEl) {
	this.inputEl	= inputEl;
	this.position	= 0;
	this.lastSearchString	= "";
	this.divEl		= null;
	this.inputId	= inputEl.id;
	this.lastNumFound	= 0;
	

}
SearchManager.prototype.objectMap	= new Object();
SearchManager.prototype.keyListenerNext	= null;
SearchManager.prototype.keyListenerPrev	= null;

function getSearchManagerInstanceByEl(inputEl) {
	var sm	= SearchManager.prototype.objectMap[inputEl.id];
	if (sm == null) {
		sm = new SearchManager(inputEl);
		SearchManager.prototype.objectMap[inputEl.id]	= sm;
	}
	
	var keyListenerNext	= new YAHOO.util.KeyListener(document, {  ctrl:true, keys:190 },  							
			  { fn:sm.findNext,
				scope:sm,
				correctScope:true } );
	if ( SearchManager.prototype.keyListenerNext != null ) {
		SearchManager.prototype.keyListenerNext.disable();
	}
	keyListenerNext.enable();
	SearchManager.prototype.keyListenerNext		= keyListenerNext;
	
	
	var keyListenerPrev	= new YAHOO.util.KeyListener(document, {  ctrl:true, keys:188 },  							
			{ fn:sm.findPrev,
		scope:sm,
		correctScope:true } );
	if ( SearchManager.prototype.keyListenerPrev != null ) {
		SearchManager.prototype.keyListenerPrev.disable();
	}
	keyListenerPrev.enable();
	SearchManager.prototype.keyListenerPrev		= keyListenerPrev;
	
	return sm;
}
function getSearchManagerInstanceById(inputId) {
	var sm	= SearchManager.prototype.objectMap[inputId];
	if (sm == null) {
		sm = new SearchManager( document.getElementById(inputId) );
		SearchManager.prototype.objectMap[inputId]	= sm;
	}
	return sm;
}

SearchManager.prototype.setDiv		= function(divEl) {
	if ( this.divEl != null )
		this.clear();
	this.divEl				= divEl;
	this.inputEl.value		= "";
	this.inputEl			= document.getElementById(this.inputId);
	//alert("lala " + divEl.id);
}

SearchManager.prototype.findNext	= function() {
		if (this.inputEl.value != this.lastSearchString ) {
			this.clear();
		}
		if ( this.lastNumFound > 0 && this.lastNumFound == this.position) 
			return;
		var spans		= this.divEl.getElementsByTagName("span");
		var numFound	= 0;
		if (spans != null) {
			for (var i=0; i<spans.length; i++) {
				spans[i].style.fontWeight	= "";
				spans[i].style.color		= "";
				var spanString	= spans[i].innerHTML.toLowerCase();
				if ( spanString.indexOf(this.inputEl.value.toLowerCase()) >= 0 ) {
					spans[i].style.fontWeight	= "bold";
					if ( this.position == numFound ) {
						spans[i].style.color	= "red";
						var parentNode			= spans[i].parentNode;
						for (var ii=0; ii<5; ii++) {
							if ( parentNode.nodeName.toLowerCase() == "li" ) {
								break;
							}
							parentNode			= parentNode.parentNode;
						}
						if ( parentNode.nodeName.toLowerCase() == "li" )
							parentNode.getElementsByTagName("input")[0].focus();
					}
					numFound++;
				}
			}
		}
		
		this.position++;
		this.lastSearchString	= this.inputEl.value;
		this.lastNumFound		= numFound;
}

SearchManager.prototype.findPrev	= function() {
	if (this.inputEl.value != this.lastSearchString ) {
		this.clear();
	}
	if ( this.lastNumFound > 0 && 1 == this.position) 
		return;
	var spans		= this.divEl.getElementsByTagName("span");
	var numFound	= 0;
	if (spans != null) {
		for (var i=0; i<spans.length; i++) {
			spans[i].style.fontWeight	= "";
			spans[i].style.color		= "";
			var spanString	= spans[i].innerHTML.toLowerCase();
			if ( spanString.indexOf(this.inputEl.value.toLowerCase()) >= 0 ) {
				spans[i].style.fontWeight	= "bold";
				if ( this.position-2 == numFound ) {
					spans[i].style.color	= "red";
					var parentNode			= spans[i].parentNode;
					for (var ii=0; ii<5; ii++) {
						if ( parentNode.nodeName.toLowerCase() == "li" ) {
							break;
						}
						parentNode			= parentNode.parentNode;
					}
					if ( parentNode.nodeName.toLowerCase() == "li" )
						parentNode.getElementsByTagName("input")[0].focus();
				}
				numFound++;
			}
		}
	}
	
	this.position--;
	this.lastSearchString	= this.inputEl.value;
	this.lastNumFound		= numFound;
}

SearchManager.prototype.clear	= function() {
	if (this.position == 0){
		return;
	}
	var spans	= this.divEl.getElementsByTagName("span");
	if (spans != null) {
		for (var i=0; i<spans.length; i++) {
			spans[i].style.fontWeight	= "";
			spans[i].style.color		= "";
		}
	}
	this.position	= 0;
	this.lastNumFound	= 0;
	
}