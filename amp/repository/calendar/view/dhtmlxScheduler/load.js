scheduler._loaded={};
scheduler._load=function(url,from){
	url=url||this._load_url;
	url+=(url.indexOf("?")==-1?"?":"&")+"timeshift="+(new Date()).getTimezoneOffset();
		
	var to;
	from=from||this._date;
	
	if (this._load_mode){
		var lf = this.templates.load_format;
		
		from = this.date[this._load_mode+"_start"](new Date(from.valueOf()));
		while (from>this._min_date) from=this.date.add(from,-1,this._load_mode);
		to = from;
		
		var cache_line = true;
		while (to<this._max_date){
			to=this.date.add(to,1,this._load_mode);	
			if (this._loaded[lf(from)] && cache_line) 
				from=this.date.add(from,1,this._load_mode);	
			else cache_line = false;
		}
		
		var temp_to=to;
		do {
			to = temp_to;
			temp_to=this.date.add(to,-1,this._load_mode);
		} while (temp_to>from && this._loaded[lf(temp_to)]);
			
		if (to<=from) 
			return false; //already loaded
		dhtmlxAjax.get(url+"&from="+lf(from)+"&to="+lf(to),function(l){scheduler.on_load(l);});
		while(from<to){
			this._loaded[lf(from)]=true;
			from=this.date.add(from,1,this._load_mode);	
		}
	} else
		dhtmlxAjax.get(url,function(l){scheduler.on_load(l);});
	this.callEvent("onXLS",[]);
	return true;
}
scheduler.on_load=function(loader){
	this._loading=true;
	if (this._process)
		var evs=this[this._process].parse(loader.xmlDoc.responseText);
	else
		var evs=this._magic_parser(loader);
	
	this._not_render=true;
	for (var i=0; i<evs.length; i++){
		if (!this.callEvent("onEventLoading",[evs[i]])) continue;
		this.addEvent(evs[i]);
	}
	this._not_render=false;
	if (this._render_wait) this.render_view_data();
	
	if (this._after_call) this._after_call();
	this._after_call=null;
	this._loading=false;
	this.callEvent("onXLE",[]);
}
scheduler.load=function(url,call){
	if (typeof call == "string"){
		this._process=call;
		call = arguments[2];
	}
	
	this._load_url=url;
	this._after_call=call;
	this._load(url,this._date);
};
//possible values - day,week,month,year,all
scheduler.setLoadMode=function(mode){
	if (mode=="all") mode="";
	this._load_mode=mode;
};

//current view by default, or all data if "true" as parameter provided
scheduler.refresh=function(refresh_all){
	alert("not implemented");
	/*
	this._loaded={};
	this._load();
	*/
}
scheduler._magic_parser=function(loader){
	//xml only for now
	var xml=loader.getXMLTopNode("data");
	if (xml.tagName!="data") return [];//not an xml
	
	var evs=[];
	var xml=loader.doXPath("//event");
	
	for (var i=0; i < xml.length; i++) {
		evs[i]=this.xmlNodeToJSON(xml[i])
		
		evs[i].text=evs[i].text||evs[i]._tagvalue;
		evs[i].start_date=this.templates.xml_date(evs[i].start_date);
		evs[i].end_date=this.templates.xml_date(evs[i].end_date);
	}
	return evs;
}
scheduler.xmlNodeToJSON = function(node){
        var t={};
        for (var i=0; i<node.attributes.length; i++)
            t[node.attributes[i].name]=node.attributes[i].value;
        
        for (var i=0; i<node.childNodes.length; i++){
        	var child=node.childNodes[i];
            if (child.nodeType==1)
                t[child.tagName]=child.firstChild?child.firstChild.nodeValue:"";
        }
                 
        if (!t.text) t.text=node.firstChild?node.firstChild.nodeValue:"";
        
        return t;
}

scheduler.attachEvent("onXLS",function(){
	if (this.config.show_loading===true){
		var t;
		t=this.config.show_loading=document.createElement("DIV");
		t.className='dhx_loading';
		t.style.left = Math.round((this._x-128)/2)+"px";
		t.style.top = Math.round((this._y-15)/2)+"px";
		this._obj.appendChild(t);
	}
});
scheduler.attachEvent("onXLE",function(){
	var t;
	if (t=this.config.show_loading)
		if (typeof t == "object"){
		this._obj.removeChild(t);
		this.config.show_loading=true;
	}
});
