scheduler._dp_init=function(dp){
	dp._methods=["setEventTextStyle","","changeEventId","deleteEvent"];
	
	this.attachEvent("onEventAdded",function(id){
		if (!this._loading && this.validId(id))
			dp.setUpdated(id,true,"inserted");
	});
	this.attachEvent("onBeforeEventDelete",function(id){
		if (!this.validId(id)) return;
        var z=dp.getState(id);
		if (z=="inserted") {  dp.setUpdated(id,false);		return true; }
		if (z=="deleted")  return false;
    	if (z=="true_deleted")  return true;
    	
		dp.setUpdated(id,true,"deleted");
      	return false;
	});
	this.attachEvent("onEventChanged",function(id){
		if (!this._loading && this.validId(id))
			dp.setUpdated(id,true,"updated");
	});
	
	dp._getRowData=function(id,pref){
		pref=pref||"";
		var ev=this.obj.getEvent(id);
		
		var str=[];
		for (var a in ev){
			if (a.indexOf("_")==0) continue;
			if (ev[a] && ev[a].getUTCFullYear) //not very good, but will work
				str.push(a+"="+this.obj.templates.xml_format(ev[a]));
			else
				str.push(a+"="+this.escape(ev[a]));
		}
		
		return pref+str.join("&"+pref);
	}
	dp._clearUpdateFlag=function(){}
}


scheduler.setUserData=function(id,name,value){
	this.getEvent(id)[name]=value;
}
scheduler.getUserData=function(id,name){
	return this.getEvent(id)[name];
}
scheduler.setEventTextStyle=function(id,style){
	this.for_rendered(id,function(r){
		r.style.cssText+=";"+style;
	})
	var ev = this.getEvent(id);
	ev["_text_style"]=style;
	this.event_updated(ev);
}
scheduler.validId=function(id){
	return true;
}