/**
	* 	@desc: constructor, data processor object 
	*	@param: serverProcessorURL - url used for update
	*	@type: public
	*/
function dataProcessor(serverProcessorURL){
    this.serverProcessor = serverProcessorURL;
    this.action_param="!nativeeditor_status";
    
	this.obj = null;
	this.updatedRows = []; //ids of updated rows
	
	this.autoUpdate = true;
	this.updateMode = "cell";
	this._tMode="GET"; 
	
    this._waitMode=0;
    this._in_progress={};//?
    this._invalid={};
    this.mandatoryFields=[];
    this.messages=[];
    
    this.styles={
    	updated:"font-weight:bold;",
    	inserted:"font-weight:bold;",
    	deleted:"text-decoration : line-through;",
    	invalid:"background-color:FFE0E0;",
    	invalid_cell:"border-bottom:2px solid red;",
    	error:"color:red;",
    	clear:"font-weight:normal;text-decoration:none;"
    }
    
    this.enableUTFencoding(true);
    dhtmlxEventable(this);

    return this;
    }

dataProcessor.prototype={
	/**
	* 	@desc: select GET or POST transaction model
	*	@param: mode - GET/POST
	*	@param: total - true/false - send records row by row or all at once (for grid only)
	*	@type: public
	*/
	setTransactionMode:function(mode,total){
        this._tMode=mode;
		this._tSend=total;
    },
    escape:function(data){
    	if (this._utf)
    		return encodeURIComponent(data);
    	else
        	return escape(data);
	},
    /**
	* 	@desc: allows to set escaping mode
	*	@param: true - utf based escaping, simple - use current page encoding
	*	@type: public
	*/	
	enableUTFencoding:function(mode){
        this._utf=convertStringToBoolean(mode);
    },
    /**
	* 	@desc: allows to define, which column may trigger update
	*	@param: val - array or list of true/false values
	*	@type: public
	*/
	setDataColumns:function(val){
		this._columns=(typeof val == "string")?val.split(","):val;
    },
    /**
	* 	@desc: get state of updating
	*	@returns:   true - all in sync with server, false - some items not updated yet.
	*	@type: public
	*/
	getSyncState:function(){
		return !this.updatedRows.length;
	},
	/**
	* 	@desc: enable/disable named field for data syncing, will use column ids for grid
	*	@param:   mode - true/false
	*	@type: public
	*/
	enableDataNames:function(mode){
		this._endnm=convertStringToBoolean(mode);
	},
	/**
	* 	@desc: enable/disable mode , when only changed fields and row id send to the server side, instead of all fields in default mode
	*	@param:   mode - true/false
	*	@type: public
	*/
	enablePartialDataSend:function(mode){
		this._changed=convertStringToBoolean(mode);
	},
	/**
	* 	@desc: set if rows should be send to server automaticaly
	*	@param: mode - "row" - based on row selection changed, "cell" - based on cell editing finished, "off" - manual data sending
	*	@type: public
	*/
	setUpdateMode:function(mode,dnd){
		this.autoUpdate = (mode=="cell");
		this.updateMode = mode;
		this.dnd=dnd;
	},
	/**
	* 	@desc: mark row as updated/normal. check mandatory fields,initiate autoupdate (if turned on)
	*	@param: rowId - id of row to set update-status for
	*	@param: state - true for "updated", false for "not updated"
	*	@param: mode - update mode name
	*	@type: public
	*/
	setUpdated:function(rowId,state,mode){
		var ind=this.findRow(rowId);
		
		mode=mode||"updated";
		var existing = this.obj.getUserData(rowId,this.action_param);
		if (existing && mode == "updated") mode=existing;
			
		if (state){
			this.set_invalid(rowId,false); //clear previous error flag
			this.updatedRows[ind]=rowId;
			this.obj.setUserData(rowId,this.action_param,mode);
		} else{
			if (!this.is_invalid(rowId)){
				this.updatedRows.splice(ind,1);
				this.obj.setUserData(rowId,this.action_param,"");
			}
		}

		//clear changed flag
		if (!state)
			this._clearUpdateFlag(rowId);
     			
		this.markRow(rowId,state,mode);
		if (state && this.autoUpdate) this.sendData(rowId);
	},
	_clearUpdateFlag:function(){
		if (this.obj.mytype!="tree"){
        	var row=this.obj.getRowById(rowId);
            if (row)
	      	for (var j=0; j<this.obj._cCount; j++)
         		this.obj.cells(rowId,j).cell.wasChanged=false;	//using cells because of split
     	}			
	},
	markRow:function(id,state,mode){ 
		var str="";
		var invalid=this.is_invalid(id)
		if (invalid){
        	str=this.styles[invalid]
        	state=true;
    	}
		if (this.callEvent("onRowMark",[id,state,mode,invalid])){
			//default logic
			if (state)
				str+=this.styles[mode];
			else
        		str+=this.styles.clear;
        	this.obj[this._methods[0]](id,str);

			if (invalid && invalid.details){
				str+=this.styles[invalid+"_cell"];
				for (var i=0; i < invalid.details.length; i++)
					if (invalid.details[i])
        				this.obj[this._methods[1]](id,i,str);
			}
		}
	},
	getState:function(id){
		return this.obj.getUserData(id,this.action_param);
	},
	is_invalid:function(id){
		return this._invalid[id];
	},
	set_invalid:function(id,mode,details){ 
		if (details) mode={value:mode, details:details, toString:function(){ return this.value.toString(); }}
		this._invalid[id]=mode;
	},
	/**
	* 	@desc: check mandatory fields and varify values of cells, initiate update (if specified)
	*	@param: rowId - id of row to set update-status for
	*	@type: public
	*/
	checkBeforeUpdate:function(rowId){ 
		var valid=true; var c_invalid=[];
		for (var i=0; i<this.obj._cCount; i++)
			if (this.mandatoryFields[i]){
				var res=this.mandatoryFields[i](this.obj.cells(rowId,i).getValue(),rowId,i);
				if (typeof res == "string")
					this.messages.push(res);
				else {
					valid&=res;
					c_invalid[i]=!res;
				}
			}
		if (!valid){
			this.set_invalid(rowId,"invalid",c_invalid);
			this.setUpdated(rowId,false);
		}
		return valid;
	},
	/**
	* 	@desc: send row(s) values to server
	*	@param: rowId - id of row which data to send. If not specified, then all "updated" rows will be send
	*	@type: public
	*/
	sendData:function(rowId){
		if (this._waitMode && (this.obj.mytype=="tree" || this.obj._h2)) return;
		if (this.obj.editStop) this.obj.editStop();
		if (this.obj.linked_form) this.obj.linked_form.update();
		
		
		if(typeof rowId == "undefined" || this._tSend) return this.sendAllData();
		if (this._in_progress[rowId]) return false;
		
		this.messages=[];
		if (!this.checkBeforeUpdate(rowId) && this.callEvent("onValidatationError",[rowId,this.messages])) return false;
		this._beforeSendData(this._getRowData(rowId),rowId);
    },
    _beforeSendData:function(data,rowId){
    	if (!this.callEvent("onBeforeUpdate",[rowId,this.getState(rowId)])) return false;	
		this._sendData(data,rowId);
    },
    _sendData:function(a1,rowId){
    	if (!a1) return; //nothing to send
    	if (rowId)
			this._in_progress[rowId]=(new Date()).valueOf();
	    
		if (!this.callEvent("onBeforeDataSending",rowId?[rowId,this.getState(rowId)]:[])) return false;				
		var a2=new dtmlXMLLoaderObject(this.afterUpdate,this,true);
        var a3=this.serverProcessor;

		if (this._tMode!="POST")
        	a2.loadXML(a3+((a3.indexOf("?")!=-1)?"&":"?")+a1);
		else
        	a2.loadXML(a3,true,a1);

		this._waitMode++;
    },
	sendAllData:function(){
		if (!this.updatedRows.length) return;			

		this.messages=[]; var valid=true;
		for (var i=0; i<this.updatedRows.length; i++)
			valid&=this.checkBeforeUpdate(this.updatedRows[i]);
		if (!valid && !this.callEvent("onValidatationError",["",this.messages])) return false;
	
		if (this._tSend) 
			this._sendData(this._getAllData());
		else
			for (var i=0; i<this.updatedRows.length; i++)
				if (!this._in_progress[this.updatedRows[i]]){
					if (this.is_invalid(this.updatedRows[i])) continue;
					this._beforeSendData(this._getRowData(this.updatedRows[i]),this.updatedRows[i]);
					if (this._waitMode && (this.obj.mytype=="tree" || this.obj._h2)) return; //block send all for tree
				}
	},
    
	
	
	
	
	
	
	
	_getAllData:function(rowId){
		var out=new Array();
		var rs=new Array();
		for(var i=0;i<this.updatedRows.length;i++){
			var id=this.updatedRows[i];
			if (this._in_progress[id] || this.is_invalid(id)) continue;
			if (!this.callEvent("onBeforeUpdate",[id,this.getState(id)])) continue;	
			out[out.length]=this._getRowData(id,id+"_");
			rs[rs.length]=id;
			this._in_progress[id]=(new Date()).valueOf();
		}
		if (out.length)
			out[out.length]="ids="+rs.join(",");
		return out.join("&");
	},
	_getRowData:function(rowId,pref){
		pref=(pref||"");
        if (this.obj.mytype=="tree"){
			var z=this.obj._globalIdStorageFind(rowId);
			var z2=z.parentObject;
			
			var i=0;
			for (i=0; i<z2.childsCount; i++)
				if (z2.childNodes[i]==z) break;
			
			var str=pref+"tr_id="+this.escape(z.id);
			str+="&"+pref+"tr_pid="+this.escape(z2.id);
			str+="&"+pref+"tr_order="+i;
			str+="&"+pref+"tr_text="+this.escape(z.span.innerHTML);
			
			z2=(z._userdatalist||"").split(",");
			for (i=0; i<z2.length; i++)
				str+="&"+pref+this.escape(z2[i])+"="+this.escape(z.userData["t_"+z2[i]]);

        }
        else{
           var str=pref+"gr_id="+this.escape(rowId);
		   if (this.obj.isTreeGrid())
		   str+="&"+pref+"gr_pid="+this.escape(this.obj.getParentId(rowId));

           var r=this.obj.getRowById(rowId);

           for (var i=0; i<this.obj._cCount; i++)
               {
			   if (this.obj._c_order)
			   		var i_c=this.obj._c_order[i];
			   else
				   	var i_c=i;

			   var c=this.obj.cells(r.idd,i);
			   if (this._changed && !c.wasChanged()) continue;
			   if (this._endnm)
	               str+="&"+pref+this.obj.getColumnId(i)+"="+this.escape(c.getValue());
			   else
	               str+="&"+pref+"c"+i_c+"="+this.escape(c.getValue());
               }
           var data=this.obj.UserData[rowId];
           if (data){
               for (var j=0; j<data.keys.length; j++)
                   str+="&"+pref+data.keys[j]+"="+this.escape(data.values[j]);
           }
           var data=this.obj.UserData["gridglobaluserdata"];
           if (data){
               for (var j=0; j<data.keys.length; j++)
                   str+="&"+pref+data.keys[j]+"="+this.escape(data.values[j]);
           }
           
        }
        if (this.obj.linked_form)
        	str+=this.obj.linked_form.get_serialized(rowId,pref);
    	return str;
	},
	
	
	
	
	
	
	
	
	/**
	* 	@desc: specify column which value should be varified before sending to server
	*	@param: ind - column index (0 based)
	*	@param: verifFunction - function (object) which should verify cell value (if not specified, then value will be compared to empty string). Two arguments will be passed into it: value and column name
	*	@type: public
	*/
	setVerificator:function(ind,verifFunction){
		this.mandatoryFields[ind] = verifFunction||(function(value){return (value!="");});
	},
	/**
	* 	@desc: remove column from list of those which should be verified
	*	@param: ind - column Index (0 based)
	*	@type: public
	*/
	clearVerificator:function(ind){
		this.mandatoryFields[ind] = false;
	},
	
	
	
	
	
	findRow:function(pattern){
		var i=0;
    	for(i=0;i<this.updatedRows.length;i++)
		    if(pattern==this.updatedRows[i]) break;
	    return i;
    },

   
	


    





	/**
	* 	@desc: define custom actions
	*	@param: name - name of action, same as value of action attribute
	*	@param: handler - custom function, which receives a XMl response content for action
	*	@type: private
	*/
	defineAction:function(name,handler){
        if (!this._uActions) this._uActions=[];
            this._uActions[name]=handler;
	},




	/**
*     @desc: used in combination with setOnBeforeUpdateHandler to create custom client-server transport system
*     @param: sid - id of item before update
*     @param: tid - id of item after up0ate
*     @param: action - action name
*     @type: public
*     @topic: 0
*/
	afterUpdateCallback:function(sid, tid, action, btag) {
		delete this._in_progress[sid];
		var correct=(action!="error" && action!="invalid");
		if (!correct) this.set_invalid(sid,action);
		if ((this._uActions)&&(this._uActions[action])&&(!this._uActions[action](btag))) return;
    	this.setUpdated(sid, false);
	    
	    var soid = sid;
	
	    switch (action) {
	    case "inserted":
	    case "insert":
	        if (tid != sid) {
	            this.obj[this._methods[2]](sid, tid);
	            sid = tid;
	        }
	        break;
	    case "delete":
	    case "deleted":
	    	this.obj.setUserData(sid, this.action_param, "true_deleted");
	        this.obj[this._methods[3]](sid);
	        return this.callEvent("onAfterUpdate", [sid, action, tid, btag])
	        break;
	    }
	    //???
	    if (correct) this.obj.setUserData(sid, this.action_param,'');
	    this.callEvent("onAfterUpdate", [sid, action, tid, btag])
	},

	/**
	* 	@desc: response from server
	*	@param: xml - XMLLoader object with response XML
	*	@type: private
	*/
	afterUpdate:function(that,b,c,d,xml){
		xml.getXMLTopNode("data"); //fix incorrect content type in IE
		if (!xml.xmlDoc.responseXML) return;
		var atag=xml.doXPath("//data/action");
		for (var i=0; i<atag.length; i++){
        	var btag=atag[i];
			var action = btag.getAttribute("type");
			var sid = btag.getAttribute("sid");
			var tid = btag.getAttribute("tid");
			
		    
			that.afterUpdateCallback(sid,tid,action,btag);
		}
		if (that._waitMode) that._waitMode--;
		
		if ((that.obj.mytype=="tree" || that.obj._h2) && that.updatedRows.length) 
			that.sendData();
		that.callEvent("onAfterUpdateFinish",[]);
		if (!that.updatedRows.length)
			that.callEvent("onFullSync",[]);
	},




	
	/**
	* 	@desc: initializes data-processor
	*	@param: anObj - dhtmlxGrid object to attach this data-processor to
	*	@type: public
	*/
	init:function(anObj){
		this.obj = anObj;
		if (this.obj._dp_init) return this.obj._dp_init(this);
		var self = this;
		
        if (this.obj.mytype=="tree"){
        	this._methods=["setItemStyle","","changeItemId","deleteItem"];
            this.obj.attachEvent("onEdit",function(state,id){
                if (state==3)
                    self.setUpdated(id,true)
                return true;
            });
            this.obj.attachEvent("onDrop",function(id,id_2,id_3,tree_1,tree_2){
                if (tree_1==tree_2)
                	self.setUpdated(id,true);
            });
    		this.obj._onrdlh=function(rowId){
    			if (self.getState(rowId)=="true_deleted")
    				return true;
    			self.setUpdated(rowId,true,"deleted")
    			return false;
    		};
    		this.obj._onradh=function(rowId){
    			self.setUpdated(rowId,true,"inserted")
    		};
        }
        else{
        	this._methods=["setRowTextStyle","setCellTextStyle","changeRowId",,"deleteRow"];
      		this.obj.attachEvent("onEditCell",function(state,id,index){
      			if (self._columns && !self._columns[index]) return true;
      			var cell = self.obj.cells(id,index)
      			if(state==1){
					if(cell.isCheckbox()){
      					self.setUpdated(id,true)
      				}
      			}else if(state==2){
      				if(cell.wasChanged()){
						self.setUpdated(id,true)
      				}
      			}
                return true;
      		})
      		this.obj.attachEvent("onRowPaste",function(id){
      			self.setUpdated(id,true)
  			})
  			this.obj.attachEvent("onRowIdChange",function(id,newid){
  				var ind=self.findRow(id);
  				if (ind<self.updatedRows.length)
      				self.updatedRows[ind]=newid;
  			})
      		this.obj.attachEvent("onSelectStateChanged",function(rowId){
      			if(self.updateMode=="row")
      				self.sendData();
                    return true;
      		});
      		this.obj.attachEvent("onEnter",function(rowId,celInd){
      			if(self.updateMode=="row")
      				self.sendData();
                    return true;
      		});
      		this.obj.attachEvent("onBeforeRowDeleted",function(rowId){
      			if (this.dragContext && self.dnd) {
      				window.setTimeout(function(){
      					self.setUpdated(rowId,true);
  					},1)
      				return true;
  				}
                var z=self.getState(rowId);
				if (this._h2){
      				this._h2.forEachChild(rowId,function(el){
      					self.setUpdated(el.id,false);
      					self.markRow(el.id,true,"deleted");
  					},this);
      			}
    			if (z=="inserted") {  self.setUpdated(rowId,false);		return true; }
    			if (z=="deleted")  return false;
    			if (z=="true_deleted")  return true;

      			self.setUpdated(rowId,true,"deleted");
      			return false;
      		});
      		this.obj.attachEvent("onRowAdded",function(rowId){
      			if (this.dragContext && self.dnd) return true;
				self.setUpdated(rowId,true,"inserted")
                return true;
      		});
      		this.obj.on_form_update=function(id){
				self.setUpdated(id,true);
				return true;
			}
        }
	},
	
	
	link_form:function(obj){
		obj.on_update=this.obj.on_form_update;
	},
	setOnAfterUpdate:function(ev){
		this.attachEvent("onAfterUpdate",ev);
	},
	enableDebug:function(mode){
	},
	setOnBeforeUpdateHandler:function(func){  
		this.attachEvent("onBeforeDataSending",func);
	}
}
//(c)dhtmlx ltd. www.dhtmlx.com
