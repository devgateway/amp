function FilterAsYouTypePanel (alignElId, onClickCallbackObj, nameprefix) {
	
	this.alignElId		= alignElId;
	this.onClickCallbackObj	= onClickCallbackObj;
	this.nameprefix		= nameprefix;
	
	this.overlay		= null;
	this.visible		= false;
	this.textboxEl		= null;
	
	this.labelArray		= null;
	this.labelMap		= new Object();
	
	this.bigDiv		= document.createElement("div"); //holds all label divs inside
}

FilterAsYouTypePanel.globalLabelArray  = null;	

FilterAsYouTypePanel.prototype.createBody	= function() {
	var mouseoverCallbackObj	= function (e, divEl) {divEl.style.backgroundColor="#CCDBFF";};
	var mouseoutCallbackObj		= function (e, divEl) {divEl.style.backgroundColor="white";};
	
	var myBigDiv				= this.bigDiv;
	var myOnClickCallbackObj	= this.onClickCallbackObj;
	var myLabelMap				= this.labelMap;
	var clickCallbackObj		= {
			click: function (e, label) {
				if ( (e.target != null && e.target.tagName.toLowerCase() == "input") || 
						e.srcElement != null && e.srcElement.tagName.toLowerCase() == "input" ) {
					return;
				}
				else {
					var inputEl		= this.getElementsByTagName("input")[0];
					inputEl.checked	= !inputEl.checked;
					label.parentObj.onClickCallbackObj.click.call(label.parentObj.onClickCallbackObj, e, label);
				}
			},
			applyClick: function (e) {
				var retArray	= new Array();
				var allInputs	= this.bigDiv.getElementsByTagName("input");
				for (var i=0; i<allInputs.length; i++) {
					if (allInputs[i].name == "selectedLabel" && allInputs[i].checked ) {
						var label	= this.labelMap[allInputs[i].value]; 
						retArray.push( label );
					}
				}
				this.onClickCallbackObj.applyClick.call(this.onClickCallbackObj, e, retArray);
			},
			bigDiv: myBigDiv,
			onClickCallbackObj: myOnClickCallbackObj,
			labelMap: myLabelMap
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
				inputEl.type			= "checkbox";
				inputEl.value			= label.uuid;
				
				
				spanEl.innerHTML		= label.name;
				spanEl.id			= divId;
				spanEl.style.color					= label.color;
				spanEl.style.backgroundColor		= label.backgroundColor;
				spanEl.style.padding				= "1px";
				spanEl.style.cursor					= "pointer";
				spanEl.style.verticalAlign			= "middle";
				
				divEl.appendChild(inputEl);
				divEl.appendChild(spanEl);
				divEl.style.padding	= "2px";
				divEl.style.backgroundColor	= "white";
	//			divEl.style.border			= "1px solid black";
				
				label.divEl			= divEl;
				label.parentObj		= this;
				
				
				YAHOO.util.Event.addListener(divEl, "click", clickCallbackObj.click, label, false);
				
				YAHOO.util.Event.addListener(divEl, "mouseover", mouseoverCallbackObj, divEl, false);
				YAHOO.util.Event.addListener(divEl, "mouseout", mouseoutCallbackObj, divEl, false);
				
				retArray.push(divEl);
			}
		}
		var hrDivEl			= document.createElement("div");
		var hrEl			= document.createElement("hr");
				
		hrDivEl.appendChild(hrEl);
		hrDivEl.style.backgroundColor	= "white";
		hrDivEl.style.padding	= "2px";
		
		var applyDivEl			= document.createElement("div");
		var applySpanEl			= document.createElement("span");
		
		applySpanEl.innerHTML			= applylabels;
		applySpanEl.style.padding		= "1px";
		applySpanEl.style.cursor		= "pointer";
		
		applyDivEl.appendChild(applySpanEl);
		applyDivEl.style.backgroundColor	= "white";
		applyDivEl.style.padding			= "2px";
		applyDivEl.style.textAlign			= "center";
		
		YAHOO.util.Event.addListener(applyDivEl, "mouseover", mouseoverCallbackObj, applyDivEl, false);
		YAHOO.util.Event.addListener(applyDivEl, "mouseout", mouseoutCallbackObj, applyDivEl, false);
		YAHOO.util.Event.addListener(applyDivEl, "click", clickCallbackObj.applyClick, clickCallbackObj, true);
		
		retArray.push(hrDivEl);
		retArray.push(applyDivEl);
		
	}
	return retArray;
	
}

FilterAsYouTypePanel.prototype.render	= function() {
	
	for (var i=0; i<this.labelArray.length; i++) {
		var l	= this.labelArray[i];
		this.labelMap[l.uuid]	= l;
	}
	
	this.overlay 	= new YAHOO.widget.Overlay(this.nameprefix, { context:[this.alignElId,"tl","bl"],
		  visible:false,
		  width:"150px" } );
	var divArray	= this.createBody();
	var textboxEl	= document.createElement("input");
	var brEl		= document.createElement("br");
	
	this.textboxEl	= textboxEl;
	textboxEl.type			= "text";
	textboxEl.style.border	= "1px solid lightgrey";
	textboxEl.style.width	= "140px";
	var textboxElDiv		= document.createElement("div");
	textboxElDiv.appendChild(textboxEl);
	textboxElDiv.style.textAlign			= "center";
	
	
	YAHOO.util.Event.addListener(textboxEl, "keyup", this.onSearch, this, true);
	YAHOO.util.Event.addListener(document,"click", this.outsideClickHide, this, true );
	
	this.bigDiv.style.border			= "1px solid gray";
	this.bigDiv.style.backgroundColor	= "white";
	this.bigDiv.style.padding			= "2px";
	this.bigDiv.appendChild(textboxElDiv);
	this.bigDiv.appendChild(brEl);
	
	
	this.overlay.setBody(this.bigDiv);
	this.overlay.render(document.body);
	
	
	if ( divArray != null && divArray.length > 0 ) {
		for (var i=0; i < divArray.length; i++) {
			var divEl	= divArray[i];
			this.bigDiv.appendChild(divEl);
		}
	}
}

FilterAsYouTypePanel.prototype.toggleViewWithSel	= function(uuidArray) {
	this.toggleView();
	var allInputEls	= this.bigDiv.getElementsByTagName("input");
	for ( var j=0; j<allInputEls.length; j++ ) {
		allInputEls[j].checked	= false;
	}
	for ( var i=0; i<uuidArray.length; i++ ) {
		for ( var j=0; j<allInputEls.length; j++ ) {
			if ( allInputEls[j].value == uuidArray[i] ) {
				allInputEls[j].checked	= true;
			}
			
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
FilterAsYouTypePanel.prototype.outsideClickHide	= function (e) {
	var clickedEl	= e.target;
	if ( !clickedEl )
		clickedEl	= e.srcElement;
	var nodeName	= clickedEl.nodeName.toLowerCase();		
	if (nodeName != "button" && nodeName != "a" && nodeName != "input") {
		this.hide();
	}
}

FilterAsYouTypePanel.prototype.hide	= function () {
	this.overlay.hide();
	this.textboxEl.value	= "";
	this.resetFilter();
	this.visible	= false;
}

FilterAsYouTypePanel.prototype.show	= function () {
	if ( this.overlay == null ){
		this.initLabelArray(true);
	}
	else {
		this.overlay.show();
		this.textboxEl.focus();
	}
	this.reposition(this.alignElId);
}

FilterAsYouTypePanel.prototype.initLabelArray	= function (shouldShow) {
	if ( FilterAsYouTypePanel.globalLabelArray == null ) {
		FilterAsYouTypePanel.globalLabelArray	= new Array();
		var callbackObj	= {
				success: function (o) {
					if ( o.responseText.length > 0) {
						var tempLabelArray				= eval( o.responseText );
						this.fPanel.labelArray			= tempLabelArray;
						for (var i=0; i<this.fPanel.labelArray.length; i++) {
							var l	= this.fPanel.labelArray[i];
							FilterAsYouTypePanel.globalLabelArray.push(l);
						}
						if (shouldShow) {
							this.fPanel.render();
							this.fPanel.overlay.show();
							this.fPanel.textboxEl.focus();
						}
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
	else {
		this.labelArray				= FilterAsYouTypePanel.globalLabelArray;
		if (shouldShow) {
			this.render();
			this.overlay.show();
			this.textboxEl.focus();
		}
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
		return this.textboxEl.value.toLowerCase();
	}
	else 
		return null;

}
FilterAsYouTypePanel.prototype.reposition	= function (elementId) {
	this.alignElId	= elementId;
	this.overlay.cfg.setProperty("context",[elementId,"tl","bl"]);
//	this.overlay.align("tl", "tl");
}
