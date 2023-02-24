scheduler.uid=function(){
	if (!this._seed) this._seed=(new Date).valueOf();
	return this._seed++;
}
scheduler._events={};
scheduler.clearAll=function(){
	this._events={};
	this._loaded={};
	this.clear_view();
}
scheduler.addEvent=function(start_date,end_date,text,id,extra_data){
	var ev=start_date;
	if (arguments.length!=1){
		ev=extra_data||{};
		ev.start_date=start_date;
		ev.end_date=end_date;
		ev.text=text;
		ev.id=id
	}
	ev.id = ev.id||scheduler.uid();
	ev.text = ev.text||"";
	
	if (typeof ev.start_date == "string")  ev.start_date=this.templates.api_date(ev.start_date);
	if (typeof ev.end_date == "string")  ev.end_date=this.templates.api_date(ev.end_date);
	ev._timed=this.is_one_day_event(ev);

	var is_new=!this._events[ev.id];
	this._events[ev.id]=ev;
	this.event_updated(ev);
	if (!this._loading)
		this.callEvent(is_new?"onEventAdded":"onEventChanged",[ev.id,ev]);
}
scheduler.deleteEvent=function(id,silent){ 
	var ev=this._events[id];
	if (!silent && !this.callEvent("onBeforeEventDelete",[id,ev])) return;
	
	if (ev){
		delete this._events[id];
		this.unselect(id);
		this.event_updated(ev);
	}
}
scheduler.getEvent=function(id){
	return this._events[id];
}
scheduler.setEvent=function(id,hash){
	this._events[id]=hash;
}
scheduler.for_rendered=function(id,method){
	for (var i=this._rendered.length-1; i>=0; i--)
		if (this._rendered[i].getAttribute("event_id")==id)
			method(this._rendered[i],i);
}
scheduler.changeEventId=function(id,new_id){
	if (id == new_id) return;
	var ev=this._events[id];
	if (ev){
		ev.id=new_id;
		this._events[new_id]=ev;
		delete this._events[id];
	}
	this.for_rendered(id,function(r){
		r.setAttribute("event_id",new_id);
	})
	if (this._select_id==id) this._select_id=new_id;
	if (this._edit_id==id) this._edit_id=new_id;
	this.callEvent("onEventIdChange",[id,new_id]);
};

(function(){
	var attrs=["text","Text","start_date","StartDate","end_date","EndDate"];
	var create_getter=function(name){
		return function(id){ return (scheduler.getEvent(id))[name]; }
	}
	var create_setter=function(name){
		return function(id,value){ 
			var ev=scheduler.getEvent(id); ev[name]=value; 
			ev._changed=true; 
			ev._timed=this.is_one_day_event(ev);
			scheduler.event_updated(ev,true); 
		}
	}
	for (var i=0; i<attrs.length; i+=2){
		scheduler["getEvent"+attrs[i+1]]=create_getter(attrs[i]);
		scheduler["setEvent"+attrs[i+1]]=create_setter(attrs[i]);
	}
})();

scheduler.event_updated=function(ev,force){
	if (this.is_visible_events(ev))
		this.render_view_data();
	else this.clear_event(ev.id);
}
scheduler.is_visible_events=function(ev){
	if (ev.start_date<this._max_date && this._min_date<ev.end_date) return true;
	return false;
}
scheduler.is_one_day_event=function(ev){
	var delta = ev.end_date.getDate()-ev.start_date.getDate();
	return ( (!delta || (delta == 1 && !ev.end_date.getHours() && !ev.end_date.getMinutes())) && ev.start_date.getMonth()==ev.end_date.getMonth() && ev.start_date.getFullYear()==ev.end_date.getFullYear()) ;
}
scheduler.get_visible_events=function(){
	//not the best strategy for sure
	var stack=[];
	var filter = this["filter_"+this._mode];
	
	for( var id in this._events)
		if (this.is_visible_events(this._events[id]))
			if (this._table_view || this.config.multi_day || this._events[id]._timed)
				if (!filter || filter(id,this._events[id]))
					stack.push(this._events[id]);
				
	return stack;
}
scheduler.render_view_data=function(){
	if (this._not_render) {
		this._render_wait=true;
		return;
	}
	this._render_wait=false;
	
	this.clear_view();
	var evs=this.get_visible_events();
	
	if (this.config.multi_day && !this._table_view){
		var tvs = [];
		var tvd = [];
		for (var i=0; i < evs.length; i++){
			if (evs[i]._timed)
				tvs.push(evs[i]);
			else
				tvd.push(evs[i]);
		};
		this._table_view=true;
		this.render_data(tvd);
		this._table_view=false;		
		this.render_data(tvs);
	} else 
		this.render_data(evs);	
}
scheduler.render_data=function(evs,hold){
	evs=this._pre_render_events(evs,hold);
	for (var i=0; i<evs.length; i++)
		if (this._table_view)
			this.render_event_bar(evs[i]);
		else
			this.render_event(evs[i]);
}
scheduler._pre_render_events=function(evs,hold){
	var hb = this.xy.bar_height;
	var h_old = this._colsS.heights;	
	var h=this._colsS.heights=[0,0,0,0,0,0,0];
	
	if (!this._table_view) evs=this._pre_render_events_line(evs,hold); //ignore long events for now
	else evs=this._pre_render_events_table(evs,hold);
	
	if (this._table_view){
		if (hold)
			this._colsS.heights = h_old;
		else {
			var evl = this._els["dhx_cal_data"][0].firstChild;
			if (evl.rows){
				for (var i=0; i<evl.rows.length; i++){
					h[i]++;
					if ((h[i])*hb > this._colsS.height-hb-2){
						//we have overflow, update heights
						var cells = evl.rows[i].cells;
						for (var j=0; j < cells.length; j++) {
							cells[j].childNodes[1].style.height = h[i]*hb+"px";
						}
						h[i]=(h[i-1]||0)+cells[0].offsetHeight;
					}
					h[i]=(h[i-1]||0)+evl.rows[i].cells[0].offsetHeight;
				}	
				h.unshift(0);
				if (evl.parentNode.offsetHeight<evl.parentNode.scrollHeight && !evl._h_fix){
					//we have v-scroll, decrease last day cell
					for (var i=0; i<evl.rows.length; i++){
						var cell = evl.rows[i].cells[6].childNodes[0];
						var w = cell.offsetWidth-scheduler.xy.scroll_width+"px";
						cell.style.width = w;
						cell.nextSibling.style.width = w;
					}		
					evl._h_fix=true;
				}
			} else{
				
				if (!evs.length && this._els["dhx_multi_day"][0].style.visibility == "visible")
					h[0]=-1;
				if (evs.length || h[0]==-1){
					//shift days to have space for multiday events
					var childs = evl.parentNode.childNodes;
					var dh = (h[0]+1)*hb+"px";
					for (var i=0; i<childs.length; i++)
						if (this._colsS[i])
							childs[i].style.top=dh;
					var last = this._els["dhx_multi_day"][0];
					last.style.top = "0px";
					last.style.height=dh;
					last.style.visibility=(h[0]==-1?"hidden":"visible");
					last=this._els["dhx_multi_day"][1];
					last.style.height=dh;
					last.style.visibility=(h[0]==-1?"hidden":"visible");
					last.className=h[0]?"dhx_multi_day_icon":"dhx_multi_day_icon_small";
					
					this._dy_shift=(h[0]+1)*hb;
				}				
				
			}
		}
	}
	
	return evs;
}
scheduler._get_event_sday=function(ev){
	return Math.floor((ev.start_date.valueOf()-this._min_date.valueOf())/(24*60*60*1000));
}
scheduler._pre_render_events_line=function(evs,hold){
	evs.sort(function(a,b){ return a.start_date>b.start_date?1:-1; })
	var days=[]; //events by weeks
	var evs_originals = [];
	for (var i=0; i < evs.length; i++) {
		var ev=evs[i];

		//check scale overflow
		var sh = ev.start_date.getHours();
		var eh = ev.end_date.getHours();
		
		ev._sday=this._get_event_sday(ev);
		if (!days[ev._sday]) days[ev._sday]=[];
		
		if (!hold){
			ev._inner=false;
			var stack=days[ev._sday];
			while (stack.length && stack[stack.length-1].end_date<=ev.start_date)
				stack.splice(stack.length-1,1);
			if (stack.length) stack[stack.length-1]._inner=true;
			ev._sorder=stack.length; stack.push(ev);
			if (stack.length>(stack.max_count||0)) stack.max_count=stack.length;
		}
		
		if (sh < this.config.first_hour || eh >= this.config.last_hour){
			evs_originals.push(ev);
			evs[i]=ev=this._copy_event(ev);
			if (sh < this.config.first_hour){
				ev.start_date.setHours(this.config.first_hour);
				ev.start_date.setMinutes(0);
			}
			if (eh >= this.config.last_hour){
				ev.end_date.setMinutes(0);
				ev.end_date.setHours(this.config.last_hour);
			}
			if (ev.start_date>ev.end_date) {
				evs.splice(i,1); i--; continue;
			}
		}
				
	}
	if (!hold){
		for (var i=0; i < evs.length; i++) 
			evs[i]._count=days[evs[i]._sday].max_count;
		for (var i=0; i < evs_originals.length; i++) 
			evs_originals[i]._count=days[evs_originals[i]._sday].max_count;
	}
	
	return evs;
}	
scheduler._time_order=function(evs){
		evs.sort(function(a,b){ 
		if (a.start_date.valueOf()==b.start_date.valueOf()){
			if (a._timed && !b._timed) return 1;
			if (!a._timed && b._timed) return -1;
			return 0;
		}
		return a.start_date>b.start_date?1:-1;
	 })
}
scheduler._pre_render_events_table=function(evs,hold){ // max - max height of week slot
	this._time_order(evs);
	
	var out=[];
	var weeks=[[],[],[],[],[],[],[]]; //events by weeks
	var max = this._colsS.heights;
	var start_date;
	for (var i=0; i < evs.length; i++) {
		var ev=evs[i];
		var sd = (start_date||ev.start_date);
		var ed = ev.end_date;
		//trim events which are crossing through current view
		if (sd<this._min_date) sd=this._min_date;
		if (ed>this._max_date) ed=this._max_date;
		
		var locate_s = this.locate_holder_day(sd,false,ev);
		ev._sday=locate_s%7;
		var locate_e = this.locate_holder_day(ed,true,ev)||7;
		ev._eday=(locate_e%7)||7; //7 used to fill full week, when event end on monday
		ev._length=locate_e-locate_s;
		
		//3600000 - compensate 1 hour during winter|summer time shift
		ev._sweek=Math.floor((sd.valueOf()+3600000-this._min_date.valueOf())/(60*60*1000*24*7)); 	
		
		//current slot
		var stack=weeks[ev._sweek];
		//check order position
		while (stack.length && stack[stack.length-1]._eday<=ev._sday)
		//while (stack.length && stack[stack.length-1].end_date<=this.date.date_part(this.date.copy(ev.start_date)) )
				stack.splice(stack.length-1,1);
		//get max height of slot
		if (stack.length>max[ev._sweek]) max[ev._sweek]=stack.length;
				
		ev._sorder=stack.length; 
		
		if (ev._sday+ev._length<=7){
			start_date=null;
			out.push(ev);
			stack.push(ev);
		} else{ // split long event in chunks
			copy=this._copy_event(ev);
			copy._length=7-ev._sday;
			copy._eday=7; copy._sday=ev._sday;
			copy._sweek=ev._sweek; copy._sorder=ev._sorder;
			//copy.end_date=this.date.add(sd,copy._length,"day");
			copy.end_date=ev.end_date;
			
			out.push(copy);
			stack.push(copy);
			start_date=this.date.add(sd,copy._length,"day");
			i--; continue;  //repeat same step
		}
	};
	
	return out;
}
scheduler._copy_dummy=function(){ 
	this.start_date=new Date(this.start_date);
	this.end_date=new Date(this.end_date);
}
scheduler._copy_event=function(ev){
	this._copy_dummy.prototype = ev;
	return new this._copy_dummy();
	//return {start_date:ev.start_date, end_date:ev.end_date, text:ev.text, id:ev.id}
}
scheduler._rendered=[];
scheduler.clear_view=function(){
	for (var i=0; i<this._rendered.length; i++){
		var obj=this._rendered[i];
		if (obj.parentNode) obj.parentNode.removeChild(obj);		
	}
	this._rendered=[];
}
scheduler.updateEvent=function(id){
	var ev=this.getEvent(id);
	this.clear_event(id);
	if (ev) this.render_data([ev],true);
}
scheduler.clear_event=function(id){
	this.for_rendered(id,function(node,i){
		if (node.parentNode)
			node.parentNode.removeChild(node);
		scheduler._rendered.splice(i,1);
	})
}
scheduler.render_event=function(ev){
	var parent=scheduler.locate_holder(ev._sday);	
	if (!parent) return; //attempt to render non-visible event
	var top = (Math.round((ev.start_date.valueOf()-this._min_date.valueOf()-this.config.first_hour*60*60*1000)*this.config.hour_size_px/(60*60*1000)))%(this.config.hour_size_px*24)+1; //42px/hour
	var height = Math.max(25,Math.round((ev.end_date.valueOf()-ev.start_date.valueOf())*(this.config.hour_size_px+(this._quirks?1:0))/(60*60*1000))-14); //42px/hour
	var width=Math.ceil((parent.clientWidth-25)/ev._count);
	var left=ev._sorder*width+1;
	if (!ev._inner) width=width*(ev._count-ev._sorder);
	
	
	
	var d=this._render_v_bar(ev.id,25+left,top,width,height,ev._text_style,scheduler.templates.event_header(ev.start_date,ev.end_date,ev),scheduler.templates.event_text(ev.start_date,ev.end_date,ev));
		
	this._rendered.push(d);
	parent.appendChild(d);
	
	left=left+parseInt(parent.style.left)+25;
	
	top+=this._dy_shift; //corrupt top, to include possible multi-day shift
	if (this._edit_id==ev.id){
		width=Math.max(width-4,140);
		var d=document.createElement("DIV");
		d.setAttribute("event_id",ev.id);
		this.set_xy(d,width,height-6,left,top+14);
		d.className="dhx_cal_editor";
			
		var d2=document.createElement("DIV");
		this.set_xy(d2,width-6,height-12);
		d2.style.cssText+=";margin:2px 2px 2px 2px;overflow:hidden;";
		
		d.appendChild(d2);
		this._els["dhx_cal_data"][0].appendChild(d);
		this._rendered.push(d);
	
		d2.innerHTML="<textarea class='dhx_cal_editor'>"+ev.text+"</textarea>";
		if (this._quirks7) d2.firstChild.style.height=height-12+"px"; //IEFIX
		this._editor=d2.firstChild;
		this._editor.onkeypress=function(e){ 
			if ((e||event).shiftKey) return true;
			var code=(e||event).keyCode; 
			if (code==13) scheduler.editStop(true); 
			if (code==27) scheduler.editStop(false); 
		}
		this._editor.onselectstart=function(e){ return (e||event).cancelBubble=true; }
		d2.firstChild.focus();
		//IE and opera can add x-scroll during focusing
		this._els["dhx_cal_data"][0].scrollLeft=0;
		d2.firstChild.select();
	}
	
	if (this._select_id==ev.id){
		var icons=this.config["icons_"+((this._edit_id==ev.id)?"edit":"select")];
		var icons_str="";
		for (var i=0; i<icons.length; i++)
			icons_str+="<div class='dhx_menu_icon "+icons[i]+"' title='"+this.locale.labels[icons[i]]+"'></div>";
		var obj = this._render_v_bar(ev.id,left-24,top,25,icons.length*20+12,"","<div class='dhx_menu_head'></div>",icons_str,true);
		obj.style.left=left-(this._quirks7?24:24);
		this._els["dhx_cal_data"][0].appendChild(obj);
		this._rendered.push(obj);
	}
}
scheduler._render_v_bar=function(id,x,y,w,h,style,contentA,contentB,bottom){
	var d=document.createElement("DIV");
	
	var ev = this.getEvent(id);
	var cs = "dhx_cal_event";
	var cse = scheduler.templates.event_class(ev.start_date,ev.end_date,ev);
	if (cse) cs=cs+" "+cse;
	
	var html='<div event_id="'+id+'" class="'+cs+'" style="position:absolute; top:'+y+'px; left:'+x+'px; width:'+(w-4)+'px; height:'+h+'px;'+(style||"")+'">';
	html+='<div class="dhx_header" style=" width:'+(w-6)+'px;" >&nbsp;</div>';
	html+='<div class="dhx_title">'+contentA+'</div>';
	html+='<div class="dhx_body" style=" width:'+(w-(this._quirks?4:14))+'px; height:'+(h-(this._quirks?6:16))+'px;">'+contentB+'</div>';
	html+='<div class="dhx_footer" style=" width:'+(w-8)+'px;'+(bottom?' margin-top:-1px;':'')+'" ></div></div>';
	
	d.innerHTML=html;
	return d.firstChild;
}
scheduler.locate_holder=function(day){
	if (this._mode=="day") return this._els["dhx_cal_data"][0].firstChild; //dirty
	return this._els["dhx_cal_data"][0].childNodes[day];
}
scheduler.locate_holder_day=function(date,past){
	var day = Math.floor((date-this._min_date-((date.getTimezoneOffset()-this._min_date.getTimezoneOffset())*60000))/(60*60*24*1000));
	//when locating end data of event , we need to use next day if time part was defined
	if (past && this.date.time_part(date)) day++;
	return day;
}
scheduler.render_event_bar=function(ev){
	var parent=this._els["dhx_cal_data"][0];

	var x=this._colsS[ev._sday];
	var x2=this._colsS[ev._eday];
	if (x2==x) x2=this._colsS[ev._eday+1];
	var hb = this.xy.bar_height;
	
	var y=this._colsS.heights[ev._sweek]+(this._colsS.height?(this.xy.scale_height+2):2)+ev._sorder*hb; 
			
	var d=document.createElement("DIV");
	var cs = ev._timed?"dhx_cal_event_clear":"dhx_cal_event_line";
	var cse = scheduler.templates.event_class(ev.start_date,ev.end_date,ev);
	if (cse) cs=cs+" "+cse; 
	
	var html='<div event_id="'+ev.id+'" class="'+cs+'" style="position:absolute; top:'+y+'px; left:'+x+'px; width:'+(x2-x-15)+'px;'+(ev._text_style||"")+'">';
	
	if (ev._timed)
		html+=scheduler.templates.event_bar_date(ev.start_date,ev.end_date,ev);
	html+=scheduler.templates.event_bar_text(ev.start_date,ev.end_date,ev)+'</div>';
	html+='</div>';
	
	d.innerHTML=html;
	
	this._rendered.push(d.firstChild);
	parent.appendChild(d.firstChild);
}

scheduler._locate_event=function(node){
	var id=null;
	while (node && !id && node.getAttribute){
		id=node.getAttribute("event_id"); 
		node=node.parentNode
	}
	return id;
}


scheduler.edit=function(id){
	if (this._edit_id==id) return;
	this.editStop(false,id);
	this._edit_id=id;
	this.updateEvent(id);
}
scheduler.editStop=function(mode,id){
	if (id && this._edit_id==id) return;
	var ev=this.getEvent(this._edit_id);
	if (ev){
		if (mode) ev.text=this._editor.value;
		this._edit_id=null;
		this._editor=null;	
		this.updateEvent(ev.id);
		this._edit_stop_event(ev,mode);
	}
}
scheduler._edit_stop_event=function(ev,mode){
	if (this._new_event){
		if (!mode) this.deleteEvent(ev.id,true);		
		else this.callEvent("onEventAdded",[ev.id,ev]);
		this._new_event=null;
	} else
		if (mode) this.callEvent("onEventChanged",[ev.id,ev]);
}

scheduler.getEvents = function(from,to){
	var result = [];
	for (var a in this._events){
		var ev = this._events[a];
		if (ev && ev.start_date<to && ev.end_date>from)
			result.push(ev);
	}
	return result;
}