scheduler._props = {};
scheduler.createUnitsView=function(name,property,list){
	scheduler.date[name+"_start"]= scheduler.date.day_start;
	scheduler.templates[name+"_date"] = function(date){
		return scheduler.templates.day_date(date);
	}
	scheduler.templates[name+"_scale_date"] = function(date){
		return scheduler.templates.day_scale_date(date);
	}
	scheduler.templates[name+"_scale_date"] = function(date){
		return list[Math.floor((date.valueOf()-scheduler._min_date.valueOf())/(60*60*24*1000))].label;
	}
	scheduler.date["add_"+name]=function(date,inc){ return scheduler.date.add(date,inc,"day"); }
	scheduler.date["get_"+name+"_end"]=function(date){ return scheduler.date.add(date,list.length,"day"); }
	var order = {};
	for(var i=0; i<list.length;i++)
		order[list[i].key]=i;
	scheduler._props[name]={map_to:property, options:list, order:order };
};
(function(){
	var fix_und=function(pr,ev){
		if (pr && typeof ev[pr.map_to] == "undefined"){
			var s = scheduler;
			var dx = 24*60*60*1000;
			var ind = Math.floor((ev.end_date - s._min_date)/dx);
			ev.end_date = new Date(s.date.time_part(ev.end_date)*1000+s._min_date.valueOf());
			ev.start_date = new Date(s.date.time_part(ev.start_date)*1000+s._min_date.valueOf());
			ev[pr.map_to]=pr.options[ind].key;
			return true;
		}
	}
	var t = scheduler._reset_scale;
	scheduler._reset_scale = function(){
		var pr = scheduler._props[this._mode];
		var ret = t.apply(this,arguments);
		if (pr)
			this._max_date=this.date.add(this._min_date,1,"day");
		return ret;
	}
	var r = scheduler._get_event_sday;
	scheduler._get_event_sday=function(ev){
		var pr = scheduler._props[this._mode];
		if (pr){
			fix_und(pr,ev);
			return pr.order[ev[pr.map_to]];	
		}
		return r.call(this,ev);
	}
	var l = scheduler.locate_holder_day;
	scheduler.locate_holder_day=function(a,b,ev){
		var pr = scheduler._props[this._mode];
		if (pr){
			fix_und(pr,ev);
			return pr.order[ev[pr.map_to]]*1+(b?1:0);	
		}
		return l.apply(this,arguments);
	}
	var p = scheduler._mouse_coords;
	scheduler._mouse_coords=function(){
		var pr = scheduler._props[this._mode];
		var pos=p.apply(this,arguments);
		if (pr){
			var ev = this._drag_event;
			if (this._drag_id){
				ev = this.getEvent(this._drag_id);
				this._drag_event.start_date = new Date();
			}
			
			ev[pr.map_to]=pr.options[pos.x].key;
			pos.x = 0;
		}
		return pos;
	}
	var o = scheduler._time_order;
	scheduler._time_order = function(evs){
		var pr = scheduler._props[this._mode];
		if (pr){
			evs.sort(function(a,b){
				return pr.order[a[pr.map_to]]>pr.order[b[pr.map_to]]?1:-1;
			});
		} else
			o.apply(this,arguments);
	}
	
	scheduler.attachEvent("onEventAdded",function(id,ev){
		if (this._loading) return true;
		for (var a in scheduler._props){
			var pr = scheduler._props[a];
			if (typeof ev[pr.map_to] == "undefined")
				ev[pr.map_to] = pr.options[0].key;
		}
		return true;
	})
	scheduler.attachEvent("onEventCreated",function(id,n_ev){
		var pr = scheduler._props[this._mode];
		var ev = this.getEvent(id);
		this._mouse_coords(n_ev);
		fix_und(pr,ev);
		this.event_updated(ev);
		return true;
	})
			
})();