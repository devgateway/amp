function FilterAsYouTypePanel (alignElId, onClickCallbackObj, nameprefix) {
	
	this.alignElId		= alignElId;
	this.onClickCallbackObj	= onClickCallbackObj;
	this.nameprefix		= nameprefix;
	
	this.overlay		= null;
	this.visible		= false;
	this.textboxEl		= null;
}

FilterAsYouTypePanel.globalLabelArray  = null;	

FilterAsYouTypePanel.prototype.createBody	= function() {
	var mouseoverCallbackObj	= function (e, divEl) {divEl.style.backgroundColor="orange";};
	var mouseoutCallbackObj		= function (e, divEl) {divEl.style.backgroundColor="white";};
	
	var clickCallbackObj		= function (e, label) {
		if ( (e.target != null && e.target.tagName.toLowerCase() == "input") || 
				e.srcElement != null && e.srcElement.tagName.toLowerCase() == "input" ) {
			return;
		}
		else
			label.parentObj.onClickCallbackObj.click(e, label);
	}
	
	var retArray		= new Array();
	if ( this.labelArray != null && this.labelArray.length > 0 ) {
		var bodyStr	= "";
		for (var i=0; i< this.labelArray.length; i++  ) {
			var label	= this.labelArray[i];
			if (label.type == "LEAF_LABEL") {
				var divId	= this.nameprefix + label.uuid;
				
				var divEl			= document.createElement("div");
				var spanEl			= document.createElement("span");
				var inputEl			= document.createElement("input");
				
				inputEl.name			= "selectedLabel";
				inputEl.value			= label.uuid;
				inputEl.type			= "checkbox";
				
				spanEl.innerHTML		= label.name;
				spanEl.id			= divId;
				spanEl.style.color	= label.color;
				spanEl.style.backgroundColor		= label.backgroundColor;
				spanEl.style.padding	= "1px";
				
				divEl.appendChild(inputEl);
				divEl.appendChild(spanEl);
				divEl.style.padding	= "2px";
				divEl.style.backgroundColor	= "white";
	//			divEl.style.border			= "1px solid black";
				
				label.divEl			= divEl;
				label.parentObj		= this;
				
				
				YAHOO.util.Event.addListener(divEl, "click", clickCallbackObj, label, false);
				
				YAHOO.util.Event.addListener(divEl, "mouseover", mouseoverCallbackObj, divEl, false);
				YAHOO.util.Event.addListener(divEl, "mouseout", mouseoutCallbackObj, divEl, false);
				
				retArray.push(divEl);
			}
		}
	}
	return retArray;
	
}

FilterAsYouTypePanel.prototype.render	= function() {
	this.overlay 	= new YAHOO.widget.Overlay(this.nameprefix, { context:[this.alignElId,"tl","bl"],
		  visible:false,
		  width:"150px" } );
	var divArray	= this.createBody();
	var bigDiv		= document.createElement("div");
	var textboxEl	= document.createElement("input");
	var brEl		= document.createElement("br");
	
	this.textboxEl	= textboxEl;
	textboxEl.type			= "text";
	textboxEl.style.border	= "1px solid gray";
	textboxEl.style.width	= "150px";
	
	YAHOO.util.Event.addListener(textboxEl, "keyup", this.onSearch, this, true); 
	
	bigDiv.style.border	= "1px solid gray";
	bigDiv.appendChild(textboxEl);
	bigDiv.appendChild(brEl);
	
	
	this.overlay.setBody(bigDiv);
	this.overlay.render(document.body);
	
	if ( divArray != null && divArray.length > 0 ) {
		for (var i=0; i < divArray.length; i++) {
			var divEl	= divArray[i];
			bigDiv.appendChild(divEl);
		}
	}
	
}

FilterAsYouTypePanel.prototype.toggleView	= function() {
	if ( this.visible ) {
		this.hide();
		this.visible	= false;
	}
	else {
		this.show();
		this.visible	= true;
	}
}

FilterAsYouTypePanel.prototype.hide	= function () {
	this.overlay.hide();
	this.textboxEl.value	= "";
	this.resetFilter();
}

FilterAsYouTypePanel.prototype.show	= function () {
	if ( this.overlay == null ){
		this.initLabelArray();
	}
	else {
		this.overlay.show();
		this.textboxEl.focus();
	}
}

FilterAsYouTypePanel.prototype.initLabelArray	= function () {
	if ( FilterAsYouTypePanel.globalLabelArray == null ) {
		var callbackObj	= {
				success: function (o) {
					if ( o.responseText.length > 0) {
						FilterAsYouTypePanel.globalLabelArray	= eval( o.responseText );
						this.fPanel.labelArray					= FilterAsYouTypePanel.globalLabelArray;
						this.fPanel.render();
						this.fPanel.overlay.show();
						this.fPanel.textboxEl.focus();
					}
					else
						alert("The returned label array is empty !");
				},
				failure: function () {
					alert("We are sorry but your request cannot be processed at this time");
				}
				
		}
		callbackObj.fPanel	= this;
		var currTime	= new Date().getTime();
		var request = YAHOO.util.Connect.asyncRequest('GET', '/contentrepository/labelManager.do?time='+currTime, callbackObj);
	}
	
}

FilterAsYouTypePanel.prototype.onSearch 	= function () {
	if (this.textboxEl != null) {
		if ( this.textboxEl.value.length > 0 ) {
			this.filterLabels();
		}
		else
			this.resetFilter();
	}
}

FilterAsYouTypePanel.prototype.filterLabels	= function () {
	if ( this.labelArray != null && this.labelArray.length > 0 ) {
		var searchValue		= this.getSearchText();
		for (var i=0; i<this.labelArray.length; i++) {
			var divEl	= this.labelArray[i].divEl;
			if ( divEl != null ) {
				var spanEl	= divEl.getElementsByTagName('span')[0];
				if ( spanEl.innerHTML.toLowerCase().indexOf(searchValue) == -1 ) {
					divEl.style.display	= "none";
				}
				else
					divEl.style.display	= "";
			}
		}
	}
}

FilterAsYouTypePanel.prototype.resetFilter	= function () {
	if ( this.labelArray != null && this.labelArray.length > 0 ) {
		for (var i=0; i<this.labelArray.length; i++) {
			if ( this.labelArray[i].divEl != null )
				this.labelArray[i].divEl.style.display = "";
		}
	}
}
FilterAsYouTypePanel.prototype.getSearchText	= function () {
	if (this.textboxEl != null && this.textboxEl.value.length > 0) {
		return this.textboxEl.value;
	}
	else 
		return null;

}