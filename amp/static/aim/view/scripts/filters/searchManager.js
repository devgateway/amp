function SearchManager(inputEl) {
	this.inputEl	= inputEl;
	this.position	= 0;
	this.lastSearchString	= "";
	this.divEl		= null;
	if ( inputEl != null )
		this.inputId	= inputEl.id;
	this.lastNumFound	= 0;
	

}
SearchManager.prototype.objectMap	= new Object();
SearchManager.prototype.keyListenerNext	= null;
SearchManager.prototype.keyListenerPrev	= null;

function getSearchManagerInstanceByEl(inputEl, smCreator) {
	var sm	= SearchManager.prototype.objectMap[inputEl.id];
	if (sm == null) {
		if ( smCreator == null )
			sm = standardSearchManagerCreator(inputEl);
		else
			sm = smCreator+'('+inputEl+')';
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
function getSearchManagerInstanceById(inputId, smCreator) {
	var sm	= SearchManager.prototype.objectMap[inputId];
	if (sm == null) {
		if ( smCreator == null )
			sm = standardSearchManagerCreator(document.getElementById(inputId));
		else
			sm = smCreator(document.getElementById(inputId));
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

SearchManager.prototype.getMainElements	= function( ) {
	return new Array();
};

SearchManager.prototype.getAdditionalElements	= function() {
	return new Array();
};

SearchManager.prototype.findNext	= function() {
		if (this.inputEl.value != this.lastSearchString ) {
			this.clear();
		}
		if ( this.lastNumFound > 0 && this.lastNumFound == this.position) 
			return;
		var spans				= this.getMainElements();//this.divEl.getElementsByTagName("span");
		var additionalSrchDivs	= this.getAdditionalElements();//this.divEl.getElementsByTagName("DIV");

		//console.log(spans.length);
		//console.log(additionalSrchDivs.length);
		var numFound	= 0;
		
		var searchStr = this.inputEl.value.toLowerCase();
		
		if (spans != null) {
			for (var i=0; i<spans.length; i++) {
				spans[i].style.fontWeight	= "";
				spans[i].style.color		= "";
				var spanString	= spans[i].innerHTML.toLowerCase();
				var additionalSrchDivString	= '';
				if(additionalSrchDivs.length > 0){
					//if (i>0) {
						additionalSrchDivString = additionalSrchDivs[i].innerHTML.toLowerCase();
					//}
				}				 
				
				if ( spanString.indexOf(searchStr) >= 0 || additionalSrchDivString.indexOf(searchStr) >= 0) {
					spans[i].style.fontWeight	= "bold";
					if ( this.position == numFound ) {
						spans[i].style.color	= "red";
						var parentNode			= spans[i].parentNode;
						var inputs				= null;
						for (var ii=0; ii<5; ii++) {
							if ( parentNode.nodeName.toLowerCase() == "li" || parentNode.nodeName.toLowerCase() == "div") {
								inputs		= parentNode.getElementsByTagName("input");
								if ( inputs.length > 0 )
									break;
							}
							parentNode			= parentNode.parentNode;
						}
						if ( parentNode.nodeName.toLowerCase() == "li" || parentNode.nodeName.toLowerCase() == "div")
							if ( inputs != null && inputs.length > 0 )
								inputs[0].focus();
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
	var spans				= this.getMainElements();//this.divEl.getElementsByTagName("span");
	var additionalSrchDivs	= this.getAdditionalElements();//this.divEl.getElementsByTagName("DIV");
	var numFound	= 0;
	
	var searchStr = this.inputEl.value.toLowerCase();
	if (spans != null) {
		for (var i=0; i<spans.length; i++) {
			spans[i].style.fontWeight	= "";
			spans[i].style.color		= "";
			var spanString	= spans[i].innerHTML.toLowerCase();
			var additionalSrchDivString	= '';
			if(additionalSrchDivs.length > 0){
				//if (i>0) {
					additionalSrchDivString = additionalSrchDivs[i].innerHTML.toLowerCase();
				//}
			}
			
			if (spanString.indexOf(searchStr) >= 0 || additionalSrchDivString.indexOf(searchStr) >= 0) {
				spans[i].style.fontWeight	= "bold";
				if ( this.position-2 == numFound ) {
					spans[i].style.color	= "red";
					var parentNode			= spans[i].parentNode;
					var inputs				= null;
					for (var ii=0; ii<5; ii++) {
						if ( parentNode.nodeName.toLowerCase() == "li" || parentNode.nodeName.toLowerCase() == "div") {
							inputs		= parentNode.getElementsByTagName("input");
							if ( inputs.length > 0 )
								break;
						}
						parentNode			= parentNode.parentNode;
					}
					if ( parentNode.nodeName.toLowerCase() == "li" || parentNode.nodeName.toLowerCase() == "div")
						if ( inputs != null && inputs.length > 0 )
							inputs[0].focus();
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
//	if (this.position == 0){
//		return;
//	}
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

var standardSearchManagerCreator = function (inputEl) {
	return new StandardSearchManager(inputEl);
}

StandardSearchManager.prototype				= new SearchManager();
StandardSearchManager.prototype.parent		= SearchManager;
StandardSearchManager.prototype.constructor	= StandardSearchManager;
function StandardSearchManager( inputEl ) {
	this.parent.call(this, inputEl);
	
};

StandardSearchManager.prototype.getMainElements	= function( ) {	
	return this.divEl.getElementsByTagName("span");
};

StandardSearchManager.prototype.getAdditionalElements	= function() {
	var myDivs = this.divEl.getElementsByTagName("div");
	var retVal= new Array();
	for (var i=0;i<myDivs.length;i++){
		if(myDivs[i].className=='additionalSearchStringMarker'){
			retVal.push(myDivs[i]);
		}
	}
	
	return retVal;
};

var messagesSearchManagerCreator = function (inputEl) {
	return new MessagesSearchManager(inputEl);
}

MessagesSearchManager.prototype				= new SearchManager();
MessagesSearchManager.prototype.parent		= SearchManager;
MessagesSearchManager.prototype.constructor	= MessagesSearchManager;
function MessagesSearchManager( inputEl ) {
	this.parent.call(this, inputEl);
	
};

MessagesSearchManager.prototype.getMainElements	= function( ) {
	return this.divEl.getElementsByTagName("span");
};

MessagesSearchManager.prototype.getAdditionalElements	= function() {
	return new Array();
};
