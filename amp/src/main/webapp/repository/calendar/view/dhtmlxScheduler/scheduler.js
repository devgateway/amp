window.dhtmlXScheduler=window.scheduler={version:2.1};
dhtmlxEventable(scheduler);
scheduler.init=function(id,date,mode){
	date=date||(new Date());
	mode=mode||"week";
	
	this._obj=(typeof id == "string")?document.getElementById(id):id;
	this._els=[];
	this._scroll=true;
	this._quirks=(_isIE && document.compatMode == "BackCompat");
	this._quirks7=(_isIE && navigator.appVersion.indexOf("MSIE 8")==-1);
	
	this.init_templates();
	this.get_elements()		
	this.set_actions();
	dhtmlxEvent(window,"resize",function(){
		window.clearTimeout(scheduler._resize_timer);
		scheduler._resize_timer=window.setTimeout(function(){
			if (scheduler.callEvent("onSchedulerResize",[]))
				scheduler.update_view();
		}, 100);
	})
	
	this.set_sizes();
	this.setCurrentView(date,mode);
}
scheduler.xy={
	nav_height:22,
	scale_width:50,
	bar_height:20,
	scroll_width:18,
	scale_height:20
}
scheduler.set_sizes=function(){
	var w = this._x = this._obj.clientWidth;
	var h = this._y = _isIE? this._obj.clientHeight*0.85 :  this._obj.clientHeight;
	
	//alert(w+"-"+h);
	
	//not-table mode always has scroll - need to be fixed in future
	var scale_x=this._table_view?0:(this.xy.scale_width+this.xy.scroll_width);
	var scale_s=this._table_view?-1:this.xy.scale_width;
	var data_y=this.xy.scale_height+this.xy.nav_height+(this._quirks?-2:0);
	
	this.set_xy(this._els["dhx_cal_navline"][0],w,this.xy.nav_height,0,0);
	this.set_xy(this._els["dhx_cal_header"][0],w-scale_x-20,this.xy.scale_height,scale_s+11,this.xy.nav_height+(this._quirks?-1:1)+15);
	this.set_xy(this._els["dhx_cal_data"][0],w-20,h-(data_y+2),0+11,data_y+2+15);
}
scheduler.set_xy=function(node,w,h,x,y){
	node.style.width=Math.max(0,w)+"px";
	node.style.height=Math.max(0,h)+"px";
	if (arguments.length>3){
		node.style.left=x+"px";
		node.style.top=y+"px";	
	}
}
scheduler.get_elements=function(){
	//get all child elements as named hash
	var els=this._obj.getElementsByTagName("DIV");
	for (var i=0; i < els.length; i++){
		var name=els[i].className;
		if (!this._els[name]) this._els[name]=[];
		this._els[name].push(els[i]);
		
		//check if name need to be changed
		var t=scheduler.locale.labels[els[i].getAttribute("name")||name];
		if (t) els[i].innerHTML=t;
	}
}
scheduler.set_actions=function(){
	for (var a in this._els)
		if (this._click[a])
			for (var i=0; i < this._els[a].length; i++)
				this._els[a][i].onclick=scheduler._click[a];
	this._obj.onselectstart=function(e){ return false; }
	this._obj.onmousemove=function(e){
		scheduler._on_mouse_move(e||event);
	}
	this._obj.onmousedown=function(e){
		scheduler._on_mouse_down(e||event);
	}
	this._obj.onmouseup=function(e){
		scheduler._on_mouse_up(e||event);
	}
	this._obj.ondblclick=function(e){
		scheduler._on_dbl_click(e||event);
	}
}
scheduler.select=function(id){
	if (this._table_view || !this.getEvent(id)._timed) return; //temporary block
	if (this._select_id==id) return;
	this.editStop(false);
	this.unselect();
	this._select_id = id;
	this.updateEvent(id);
}
scheduler.unselect=function(id){
	if (id && id!=this._select_id) return;
	var t=this._select_id;
	this._select_id = null;
	if (t) this.updateEvent(t);
}
scheduler._click={
	dhx_cal_data:function(e){
		var trg = e?e.target:event.srcElement;
		var id = scheduler._locate_event(trg);
		if ((id && !scheduler.callEvent("onClick",[id,(e||event)])) ||scheduler.config.readonly) return;
		if (id) {	
//			scheduler.select(id);
//			var mask = trg.className;
//			if (mask.indexOf("_icon")!=-1)
//				scheduler._click.buttons[mask.split(" ")[1].replace("icon_","")](id);
		} else {
			scheduler._close_not_saved();
		}
	},
	dhx_cal_prev_button:function(){
		scheduler.setCurrentView(scheduler.date.add(scheduler._date,-1,scheduler._mode));
	},
	dhx_cal_next_button:function(){
		scheduler.setCurrentView(scheduler.date.add(scheduler._date,1,scheduler._mode));
	},
	dhx_cal_today_button:function(){
		scheduler.setCurrentView(new Date());
	},
	dhx_cal_tab:function(){
		var mode = this.getAttribute("name").split("_")[0];
		scheduler.setCurrentView(scheduler._date,mode);
	},
	buttons:{
		"delete":function(id){ var c=scheduler.locale.labels.confirm_deleting; if (!c||confirm(c)) scheduler.deleteEvent(id); },
		edit:function(id){ scheduler.edit(id); },
		save:function(id){ scheduler.editStop(true); },
		details:function(id){ scheduler.showLightbox(id); },
		cancel:function(id){ scheduler.editStop(false); }
	}
}

scheduler.addEventNow=function(start,end,e){
	var d = this.config.time_step*60000;
	if (!start) start = Math.round((new Date()).valueOf()/d)*d;
	end = (end||(start+d));
	
	this._drag_id=this.uid();
	this._drag_mode="new-size";
	this._loading=true;
	
	this.addEvent(new Date(start), new Date(end),this.locale.labels.new_event,this._drag_id);
	this.callEvent("onEventCreated",[this._drag_id,e]);
	this._loading=false;
	this._drag_event={}; //dummy , to trigger correct event updating logic
	this._on_mouse_up(e);	
}
scheduler._on_dbl_click=function(e,src){
	/*src = src||(e.target||e.srcElement);
	if (this.config.readonly) return;
	var name = src.className.split(" ")[0];
	switch(name){
		case "dhx_scale_holder":
		case "dhx_scale_holder_now":
		case "dhx_month_body":
			if (!scheduler.config.dblclick_create) break;
			var pos=this._mouse_coords(e);
			var start=this._min_date.valueOf()+(pos.y*this.config.time_step+(this._table_view?0:pos.x)*24*60)*60000;
			start = this._correct_shift(start);
			this.addEventNow(start,null,e);
			break;
		case "dhx_body":
		case "dhx_cal_event_line":
		case "dhx_cal_event_clear":
			var id = this._locate_event(src);
			if (!this.callEvent("onDblClick",[id,e])) return;
			if (this.config.details_on_dblclick || this._table_view || !this.getEvent(id)._timed)
				this.showLightbox(id);
			else
				this.edit(id);
			break;
		case "":
			if (src.parentNode)
				return scheduler._on_dbl_click(e,src.parentNode);			
		default:
			var t = this["dblclick_"+name];
			if (t) t.call(this,e);
			break;
	}*/
}

scheduler._mouse_coords=function(ev){
	var pos;
	var b=document.body;
	var d = document.documentElement;
	if(ev.pageX || ev.pageY)
	    pos={x:ev.pageX, y:ev.pageY};
	else pos={
	    x:ev.clientX + (b.scrollLeft||d.scrollLeft||0) - b.clientLeft,
	    y:ev.clientY + (b.scrollTop||d.scrollTop||0) - b.clientTop
	}

	//apply layout
	pos.x-=getAbsoluteLeft(this._obj)+(this._table_view?0:this.xy.scale_width);
	pos.y-=getAbsoluteTop(this._obj)+this.xy.nav_height+this._dy_shift+this.xy.scale_height-this._els["dhx_cal_data"][0].scrollTop;
	//transform to date
	if (!this._table_view){
		pos.x=Math.max(0,Math.ceil(pos.x/this._cols[0])-1);
		pos.y=Math.max(0,Math.ceil(pos.y*60/(this.config.time_step*this.config.hour_size_px))-1)+this.config.first_hour*(60/this.config.time_step);
	} else {
		var dy=0;
		for (dy=1; dy < this._colsS.heights.length; dy++)
			if (this._colsS.heights[dy]>pos.y) break;

		pos.y=(Math.max(0,Math.ceil(pos.x/this._cols[0])-1)+Math.max(0,dy-1)*7)*24*60/this.config.time_step; 
		pos.x=0;
	}
	return pos;
}
scheduler._close_not_saved=function(){
	if (new Date().valueOf()-(scheduler._new_event||0) > 500 && scheduler._edit_id){
		var c=scheduler.locale.labels.confirm_closing;
		if (!c || confirm(c))
			scheduler.editStop(scheduler.config.positive_closing);
	}
}
scheduler._correct_shift=function(start){
	return start-=((new Date(scheduler._min_date)).getTimezoneOffset()-(new Date(start)).getTimezoneOffset())*60000;	
}
scheduler._on_mouse_move=function(e){
	if (this._drag_mode){
		return;
	}
}
scheduler._on_mouse_context=function(e,src){
	return this.callEvent("onContextMenu",[this._locate_event(src),e]);
}
scheduler._on_mouse_down=function(e,src){
	if (this.config.readonly || this._drag_mode) return;
	src = src||(e.target||e.srcElement);
	if (e.button==2) return this._on_mouse_context(e,src);
		switch(src.className.split(" ")[0]){
		case "dhx_cal_event_line":
		case "dhx_cal_event_clear":
			if (this._table_view)
				this._drag_mode="move"; //item in table mode
			break;
		case "dhx_header":
		case "dhx_title":
			this._drag_mode="move"; //item in table mode
			break;
		case "dhx_footer":
			this._drag_mode="resize"; //item in table mode
			break;
		case "dhx_scale_holder":
		case "dhx_scale_holder_now":
		case "dhx_month_body":
			this._drag_mode="create";
			break;
		case "":
			if (src.parentNode)
				return scheduler._on_mouse_down(e,src.parentNode);
		default:
			this._drag_mode=null;
			this._drag_id=null;
	}
	if (this._drag_mode){
		var id = this._locate_event(src);
		if (!this.config["drag_"+this._drag_mode] || !this.callEvent("onBeforeDrag",[id, this._drag_mode, e]))
			this._drag_mode=this._drag_id=0;
		else {
			this._drag_id= id;
			this._drag_event=this._copy_event(this.getEvent(this._drag_id)||{});
		}
	}
	this._drag_start=null;
}
scheduler._on_mouse_up=function(e){
	if (this._drag_mode && this._drag_id){
		this._els["dhx_cal_data"][0].style.cursor="default";
		//drop
		var ev=this.getEvent(this._drag_id);
		if (!this._drag_event.start_date || ev.start_date.valueOf()!=this._drag_event.start_date.valueOf() || ev.end_date.valueOf()!=this._drag_event.end_date.valueOf()){
			var is_new=(this._drag_mode=="new-size");
			if (is_new && this.config.edit_on_create){
				this.unselect();
				this._new_event=new Date();//timestamp of creation
				if (this._table_view || this.config.details_on_create) {
					this._drag_mode=null;
					return this.showLightbox(this._drag_id);
				}
				this._drag_pos=true; //set flag to trigger full redraw
				this._select_id=this._edit_id=this._drag_id;
			} else if (!this._new_event)
				this.callEvent(is_new?"onEventAdded":"onEventChanged",[this._drag_id,this.getEvent(this._drag_id)]);
			
				
		}
		if (this._drag_pos) this.render_view_data(); //redraw even if there is no real changes - necessary for correct positioning item after drag
	}
	this._drag_mode=null;
	this._drag_pos=null;
}	
scheduler.update_view=function(){
	//this.set_sizes();
	this._reset_scale();
	if (this._load_mode && this._load()) return;
	this.render_view_data();
}
scheduler.setCurrentView=function(date,mode){
	
	if (!this.callEvent("onBeforeViewChange",[this._mode,this._date,mode,date])) return;
	//hide old custom view
	if (this[this._mode+"_view"] && mode && this._mode!=mode)
		this[this._mode+"_view"](false);
		
	this._close_not_saved();
	
	this._mode=mode||this._mode;
	this._date=date;
	this._table_view=(this._mode=="month");
	
	var tabs=this._els["dhx_cal_tab"];
	for (var i=0; i < tabs.length; i++) {
		tabs[i].className="dhx_cal_tab"+((tabs[i].getAttribute("name")==this._mode+"_tab")?" active":"");
	};
	
	//show new view
	var view=this[this._mode+"_view"];
	view?view(true):this.update_view();
	
	this.callEvent("onViewChange",[this._mode,this._date]);
}
scheduler._render_x_header = function(i,left,d,h){
	//header scale	
	var head=document.createElement("DIV"); head.className="dhx_scale_bar";
	this.set_xy(head,this._cols[i]-1,this.xy.scale_height-2,left,0);//-1 for border
	head.innerHTML=this.templates[this._mode+"_scale_date"](d,this._mode); //TODO - move in separate method
	h.appendChild(head);
}
scheduler._reset_scale=function(){
	var h=this._els["dhx_cal_header"][0];
	var b=this._els["dhx_cal_data"][0];
	var c = this.config;
	
	h.innerHTML="";
	b.innerHTML="";
	
	
	var str=((c.readonly||(!c.drag_resize))?" dhx_resize_denied":"")+((c.readonly||(!c.drag_move))?" dhx_move_denied":"");
	if (str) b.className = "dhx_cal_data"+str;
		
		
	this._cols=[];	//store for data section
	this._colsS={height:0};
	this._dy_shift=0;
	
	this.set_sizes();
	var summ=parseInt(h.style.width); //border delta
	var left=0;
	
	var d,dd,sd,today;
	dd=this.date[this._mode+"_start"](new Date(this._date.valueOf()));
	d=sd=this._table_view?scheduler.date.week_start(dd):dd;
	today=this.date.date_part(new Date());
	
	//reset date in header
	var ed=scheduler.date.add(dd,1,this._mode);
	var count = 7;
	
	if (!this._table_view){
		var count_n = this.date["get_"+this._mode+"_end"];
		if (count_n) ed = count_n(dd);
		count = Math.round((ed.valueOf()-dd.valueOf())/(1000*60*60*24));
	}
	
	this._min_date=d;
	this._els["dhx_cal_date"][0].innerHTML=this.templates[this._mode+"_date"](dd,ed,this._mode);
	
	
	for (var i=0; i<count; i++){
		this._cols[i]=Math.floor(summ/(count-i));
	
		this._render_x_header(i,left,d,h);
		if (!this._table_view){
			var scales=document.createElement("DIV");
			var cls = "dhx_scale_holder"
			if (d.valueOf()==today.valueOf()) cls = "dhx_scale_holder_now";
			scales.className=cls+" "+this.templates.week_date_class(d,today);
			this.set_xy(scales,this._cols[i]-1,c.hour_size_px*(c.last_hour-c.first_hour),left+this.xy.scale_width+1,0);//-1 for border
			b.appendChild(scales);
		}
		
		d=this.date.add(d,1,"day")
		summ-=this._cols[i];
		left+=this._cols[i];
		this._colsS[i]=(this._cols[i-1]||0)+(this._colsS[i-1]||(this._table_view?0:52));
	}
	this._max_date=d;
	this._colsS[count]=this._cols[count-1]+this._colsS[count-1];
	
	if (this._table_view)
		this._reset_month_scale(b,dd,sd);
	else{
		this._reset_hours_scale(b,dd,sd);
		if (c.multi_day){
			var c1 = document.createElement("DIV");
			c1.className="dhx_multi_day";
			c1.style.visibility="hidden";
			this.set_xy(c1,parseInt(h.style.width),0,this.xy.scale_width,0);
			b.appendChild(c1);
			var c2 = c1.cloneNode(true);
			c2.className="dhx_multi_day_icon";
			c2.style.visibility="hidden";
			this.set_xy(c2,this.xy.scale_width-1,0,0,0);
			b.appendChild(c2);
			
			this._els["dhx_multi_day"]=[c1,c2];
		}
	}
}
scheduler._reset_hours_scale=function(b,dd,sd){
	var c=document.createElement("DIV");
	c.className="dhx_scale_holder";
	
	var date = new Date(1980,1,1,this.config.first_hour,0,0);
	for (var i=this.config.first_hour*1; i < this.config.last_hour; i++) {
		var cc=document.createElement("DIV");
		cc.className="dhx_scale_hour";
		cc.style.height=this.config.hour_size_px-(this._quirks?0:1)+"px";
		cc.style.width=this.xy.scale_width+"px";
		cc.innerHTML=scheduler.templates.hour_scale(date);
		
		c.appendChild(cc);
		date=this.date.add(date,1,"hour");
	};
	b.appendChild(c);
	if (this.config.scroll_hour)
		b.scrollTop = this.config.hour_size_px*(this.config.scroll_hour-this.config.first_hour);
}
scheduler._reset_month_scale=function(b,dd,sd){
	var ed=scheduler.date.add(dd,1,"month");
	
	//trim time part for comparation reasons
	var cd=new Date();
	this.date.date_part(cd);
	this.date.date_part(sd);
	
	var rows=Math.ceil((ed.valueOf()-sd.valueOf())/(60*60*24*1000*7));
	var tdcss=[];
	var height=(Math.floor(b.clientHeight/rows)-22);
	
	this._colsS.height=height+22;
	if (height < 1) {
		height = 1;
	}
	for (var i=0; i<=7; i++)
		tdcss[i]=" style='height:"+height+"px; width:"+((this._cols[i]||0)-(_isIE?0:1))+"px;' "

	
	this._min_date=sd;
	var html="<table cellpadding='0' cellspacing='0'>";
	for (var i=0; i<rows; i++){
		html+="<tr>";
			for (var j=0; j<7; j++){
				html+="<td";
				var cls = "";
				if (sd<dd)
					cls='dhx_before';
				else if (sd>=ed)
					cls='dhx_after';
				else if (sd.valueOf()==cd.valueOf())
					cls='dhx_now';
				html+=" class='"+cls+" "+this.templates.month_date_class(sd,cd)+"' ";
				html+="><div class='dhx_month_head'>"+this.templates.month_day(sd)+"</div><div class='dhx_month_body' "+tdcss[j]+"></div></td>"
				sd=this.date.add(sd,1,"day");
			}
		html+="</tr>";
	}
	html+="</table>";
	this._max_date=sd;
	
	b.innerHTML=html;	
}