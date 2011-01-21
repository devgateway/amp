if (window.dhtmlXGridObject){
	dhtmlXGridObject.prototype._init_point_connector=dhtmlXGridObject.prototype._init_point;
	dhtmlXGridObject.prototype._init_point=function(){
		var clear_url=function(url){
			url=url.replace(/(\?|\&)connector[^\f]*/g,"");
			return url+(url.indexOf("?")!=-1?"&":"?")+"connector=true";
		}
		var combine_urls=function(url){
			return clear_url(url)+(this._connector_sorting||"")+(this._connector_filter||"");
		}
		var sorting_url=function(url,ind,dir){
			this._connector_sorting="&sort_ind="+ind+"&sort_dir="+dir;
			return combine_urls.call(this,url);
		}
		var filtering_url=function(url,inds,vals){
			this._connector_filter="&filter="+this._cCount+"&";
			for (var i=0; i<inds.length; i++)
				inds[i]="col"+inds[i]+"="+encodeURIComponent(vals[i]);
			this._connector_filter+=inds.join("&");
			return combine_urls.call(this,url);
		}
		this.attachEvent("onCollectValues",function(ind){
				if (this._server_lists && this._server_lists[ind])
					return this._server_lists[ind];
				return true;
		});		
		this.attachEvent("onBeforeSorting",function(ind,type,dir){
			if (type=="connector"){
				var self=this;
				this.clearAndLoad(sorting_url.call(this,this.xmlFileUrl,ind,dir),function(){
					self.setSortImgState(true,ind,dir);
				});
				return false;
			}
			return true;
		});
		this.attachEvent("onFilterStart",function(a,b){
			if (this._connector_filter_used){
				this.clearAndLoad(filtering_url.call(this,this.xmlFileUrl,a,b));
				return false;
			}
			return true;
		});
		this.attachEvent("onXLE",function(a,b,c,xml){
			if (!xml) return;
			
			var form=this.getUserData("","!linked_form");
			
			if (form && (form=document.forms[form]) && !form.dhtmlx){
				this.linked_form=new dhtmlXForm(form.name,this.xmlFileUrl);
				this.attachEvent("onRowSelect",function(id){
					this.linked_form.load(id);
					return;
				});
				if (this.on_form_update) this.linked_form.on_update=this.on_form_update;
			}
			
			if (!this._server_lists){
				var selects=this.xmlLoader.doXPath("//options",xml);
				if (selects) this._server_lists=[];
				for (var i=0; i < selects.length; i++) {
					var ind = selects[i].getAttribute("for");
					var opts = this.xmlLoader.doXPath("./option",selects[i]);
					var result = [];
					for (var k=0; k < opts.length; k++) {
						result[k]=opts[k].firstChild?opts[k].firstChild.data:"";
					};
					this._server_lists[ind]=result;
					this._loadSelectOptins(this.getFilterElement(ind),ind)
				};
			}
			//we are using server side defined filters, so blocking filter updates
			if (this.refreshFilters) this._loadSelectOptins=function(){};
		});
		
		if (this._init_point_connector) this._init_point_connector();
	}
	dhtmlXGridObject.prototype._in_header_connector_text_filter=function(t,i){
		this._connector_filter_used=true;
		return this._in_header_text_filter(t,i);
	}
	dhtmlXGridObject.prototype._in_header_connector_select_filter=function(t,i){
		this._connector_filter_used=true;
		return this._in_header_select_filter(t,i);
	}
}

if (window.dataProcessor){
	dataProcessor.prototype.init_original=dataProcessor.prototype.init;
	dataProcessor.prototype.init=function(obj){
		this.init_original(obj);
		obj._dataprocessor=this;
		
		this.setTransactionMode("POST",true);
		this.serverProcessor+=(this.serverProcessor.indexOf("?")!=-1?"&":"?")+"editing=true";
	}
}
/*dhtmlxError.catchError("LoadXML",function(a,b,c){
	alert(c[0].responseText);
});*/
