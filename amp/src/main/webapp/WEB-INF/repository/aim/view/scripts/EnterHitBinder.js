function EnterHitBinder (buttonId) {
	$( window ).bind('keypress',{myObj:this}, function(e){
	   if ( e.keyCode == 13 && $(document.activeElement).attr("type") != "textarea") {
		   //console.log(e.target);
		   e.data.myObj.executeEvent(e);
	   }
	 });
	 this.eventMap	= new Object();
	 this.defaultButtonId	= buttonId;
};

EnterHitBinder.prototype.map	= function (inputList, buttonId) {
	if (inputList != null && inputList.length > 0) {
		for (var i=0; i<inputList.length; i++) {
			//this.bind(inputList[i], buttonId);
			this.eventMap[inputList[i]]	= buttonId;
		}
	}
};

EnterHitBinder.prototype.executeEvent	= function (e) {
	var inputId	= hasFocus(e);   
	
	if ( inputId != null ) {
		if ( this.eventMap[inputId] != null ) {
			$("#"+this.eventMap[inputId]).click();
			return;
		}
	}
	$("#"+this.defaultButtonId).click();
};


function hasFocus(e){	
	var focusControl;
	if ($(document.activeElement).attr("type") == "text"){
		focusControl = $(document.activeElement).attr("id");
	}
	
	return focusControl;
}
