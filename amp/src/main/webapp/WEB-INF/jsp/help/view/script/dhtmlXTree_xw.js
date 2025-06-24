/*
Copyright Scand LLC http://www.scbr.com
To use this component please contact info@scbr.com to obtain license

*/ 
 

 
dhtmlXTreeObject.prototype.setSerializationLevel=function(userData,fullXML){
 this._xuserData=convertStringToBoolean(userData);
 this._xfullXML=convertStringToBoolean(fullXML);
}

 
dhtmlXTreeObject.prototype.serializeTree=function(){
 var out='<?xml version="1.0"?><tree id="0">';
 for(var i=0;i<this.htmlNode.childsCount;i++)
 out+=this._serializeItem(this.htmlNode.childNodes[i]);
 out+="</tree>";
 return out;
};
 
dhtmlXTreeObject.prototype._serializeItem=function(itemNode){


 if(itemNode.unParsed)
 if(document.all)
 return itemNode.unParsed.xml;
 else{
 var xmlSerializer = new XMLSerializer();
 return xmlSerializer.serializeToString(itemNode.unParsed);
}

 
 var out="";
 if(this._selected.length)
 var lid=this._selected[0].id;
 else lid="\"";

 
 if(!this._xfullXML)
 out='<item id="'+itemNode.id+'" '+(this._getOpenState(itemNode)==1?' open="1" ':'')+(lid==itemNode.id?' select="1"':'')+' text="'+itemNode.span.innerHTML+'"'+(((this.XMLsource)&&(itemNode.XMLload==0))?" child=\"1\" ":"")+'>';
 else 
 out='<item id="'+itemNode.id+'" '+(this._getOpenState(itemNode)==1?' open="1" ':'')+(lid==itemNode.id?' select="1"':'')+' text="'+itemNode.span.innerHTML+'" im0="'+itemNode.images[0]+'" im1="'+itemNode.images[1]+'" im2="'+itemNode.images[2]+'" '+(itemNode.acolor?('aCol="'+itemNode.acolor+'" '):'')+(itemNode.scolor?('sCol="'+itemNode.scolor+'" '):'')+(itemNode.checkstate==1?'checked="1" ':(itemNode.checkstate==2?'checked="-1"':''))+(itemNode.closeable?'closeable="1" ':'')+'>';
 
 if((this._xuserData)&&(itemNode._userdatalist))
{
 var names=itemNode._userdatalist.split(",");
 for(var i=0;i<names.length;i++)
 out+="<userdata name=\""+names[i]+"\">"+itemNode.userData["t_"+names[i]]+"</userdata>";
}

 for(var i=0;i<itemNode.childsCount;i++)
 out+=this._serializeItem(itemNode.childNodes[i]);
 


 out+="</item>";
 return out;
}
 
dhtmlXTreeObject.prototype.saveSelectedItem=function(name,cookie_param){
 name=name||"";
 this.setCookie("treeStateSelected"+name,this.getSelectedItemId(),cookie_param);
}
 
dhtmlXTreeObject.prototype.restoreSelectedItem=function(name){
 name=name||"";
 var z=this.getCookie("treeStateSelected"+name);
 this.selectItem(z,false);
}


 
dhtmlXTreeObject.prototype.enableAutoSavingSelected=function(mode,cookieName){
 this.assMode=convertStringToBoolean(mode);
 this.assCookieName=cookieName;
}


 
dhtmlXTreeObject.prototype.saveState=function(name,cookie_param){
 var z=this._escape(this.serializeTree());
 var kusok = 4000;
 if(z.length>kusok)
{
 if(navigator.appName.indexOf("Microsoft")!=-1)
 return false;
 this.setCookie("treeStatex"+name,Math.ceil(z.length/kusok));
 for(var i=0;i<Math.ceil(z.length/kusok);i++)
{
 this.setCookie("treeStatex"+name+"x"+i,z.substr(i*kusok,kusok),cookie_param);
}
}
 else
 this.setCookie("treeStatex"+name,z,cookie_param);
 var z=this.getCookie("treeStatex"+name);
 if(!z){
 this.setCookie("treeStatex"+name,"",cookie_param);
 return false;
}
 return true;
}
 
dhtmlXTreeObject.prototype.loadState=function(name){
 var z=this.getCookie("treeStatex"+name);
 
 if(!z)return false;

 if(z.length)
{
 if(z.toString().length<4)
{

 var z2="";
 for(var i=0;i<z;i++){
 z2+=this.getCookie("treeStatex"+name+"x"+i);
}
 z=z2;
}
 this.loadXMLString((this.utfesc=="utf8")?decodeURI(z):unescape(z));
}

 return true;
}
 

dhtmlXTreeObject.prototype.setCookie=function(name,value,cookie_param){
 var str = name+"="+value+(cookie_param?(";"+cookie_param):"");
 
 document.cookie = str;
}

 
dhtmlXTreeObject.prototype.getCookie=function(name){
 var search = name+"=";
 if(document.cookie.length > 0){
 var offset = document.cookie.indexOf(search);
 if(offset != -1){
 offset+= search.length;
 var end = document.cookie.indexOf(";",offset);
 if(end == -1)
 end = document.cookie.length;
 return document.cookie.substring(offset,end);
}}
};

dhtmlXTreeObject.prototype._createSelfExC=dhtmlXTreeObject.prototype._createSelf;
dhtmlXTreeObject.prototype._createSelf=function(){
 this.oldOnSelect=this.onRowSelect;
 this.onRowSelect=function(e,htmlObject,mode){
 if(!htmlObject)htmlObject=this;
 htmlObject.parentObject.treeNod.oldOnSelect(e,htmlObject,mode);
 if(htmlObject.parentObject.treeNod.assMode)
 htmlObject.parentObject.treeNod.saveSelectedItem(htmlObject.parentObject.treeNod.assCookieName);
}

 return this._createSelfExC();
}

 
dhtmlXTreeObject.prototype.saveOpenStates=function(name,cookie_param){
 var z=this._collectOpenStates(this.htmlNode,"");
 this.setCookie("treeOpenStatex"+name,z,cookie_param);
};

 
dhtmlXTreeObject.prototype.loadOpenStates=function(name){
 for(var i=0;i<this.htmlNode.childsCount;i++)
 this._xcloseAll(this.htmlNode.childNodes[i]);

 var z=getCookie("treeOpenStatex"+name);
 if(z){
 var arr=z.split(this.dlmtr);
 for(var i=0;i<arr.length;i++)
{
 var zNode=this._globalIdStorageFind(arr[i]);
 if(zNode){
 if((!zNode.XMLload)&&(zNode.id!=this.rootId)){
 this._delayedLoad(zNode,"loadOpenStates('"+name+"')");
 return;
}
 else
 this.openItem(arr[i]);
}
}
}
};

dhtmlXTreeObject.prototype._delayedLoad=function(node,name){
 this.afterLoadMethod=name;
 this.onLoadReserve = this.onXLE;
 this.setOnLoadingEnd(this._delayedLoadStep2);
 this.loadXML(this.XMLsource+getUrlSymbol(this.XMLsource)+"id="+this._escape(node.id));
}
dhtmlXTreeObject.prototype._delayedLoadStep2=function(tree){
 tree.onXLE=tree.onLoadReserve;
 
 window.setTimeout(new function(){eval("tree."+tree.afterLoadMethod);},100);

}

 
dhtmlXTreeObject.prototype._collectOpenStates=function(node,list){
 if(this._getOpenState(node)==1)list+=this.dlmtr+node.id;
 for(var i=0;i<node.childsCount;i++)
 list=this._collectOpenStates(node.childNodes[i],list);
 return list;
};

 
function setCookie(name,value){
 document.cookie = name+'='+value;
}

 
function getCookie(name){
 var search = name+"=";
 if(document.cookie.length > 0){
 var offset = document.cookie.indexOf(search);
 if(offset != -1){
 offset+= search.length;
 var end = document.cookie.indexOf(";",offset);
 if(end == -1)
 end = document.cookie.length;
 return(document.cookie.substring(offset,end));
}}
};

 
 dhtmlXTreeObject.prototype.openAllItemsDynamic = function(itemId)
{
 this.ClosedElem=new Array();
 this.G_node=null;
 var itemNode = this._globalIdStorageFind(itemId||this.rootId);
 this.onLoadReserve = this.onXLE;
 this.setOnLoadingEnd(this._loadAndOpen);
 this._openAllNodeChilds(itemNode,0);
 if(this.ClosedElem.length>0)this._loadAndOpen(this);
};

 dhtmlXTreeObject.prototype._openAllNodeChilds = function(itemNode)
{
 for(var i=0;i<itemNode.childsCount;i++)
{
 
 if(this._getOpenState(itemNode.childNodes[i])<0)this._HideShow(itemNode.childNodes[i],2);
 if(itemNode.childNodes[i].childsCount>0)this._openAllNodeChilds(itemNode.childNodes[i]);

 
 if(itemNode.childNodes[i].XMLload==0)this.ClosedElem.push(itemNode.childNodes[i]);
}
}

 dhtmlXTreeObject.prototype._loadAndOpen = function(that)
{
 if(that.G_node)
{
 that._openItem(that.G_node);
 that._openAllNodeChilds(that.G_node);
 that.G_node = null;
}

 if(that.ClosedElem.length>0)that.G_node = that.ClosedElem.shift();

 if(that.G_node)
 window.setTimeout(function(){that._loadDynXML(that.G_node.id);},100);
 else
 that.onXLE = that.onLoadReserve;
}


 
 dhtmlXTreeObject.prototype.openItemsDynamic=function(list,flag){
 this._opnItmsDnmcFlg=convertStringToBoolean(flag);
 this.onLoadReserve = this.onXLE;
 this.setOnLoadingEnd(this._stepOpen);
 this.ClosedElem=list.split(",").reverse();
 this._stepOpen(this);
}

 dhtmlXTreeObject.prototype._stepOpen=function(that){
 if(that.ClosedElem.length)
{
 that.G_node=that.ClosedElem.pop();
 if(!that.ClosedElem.length){
 that.onXLE = that.onLoadReserve;
 that.selectItem(that.G_node,that._opnItmsDnmcFlg);
}
 var temp=that._globalIdStorageFind(that.G_node);
 if(temp.XMLload)
 that._stepOpen(that);
 else
 that.openItem(that.G_node);
}
}

 dhtmlXTreeObject.prototype._stepOpen=function(that){
 if(that.ClosedElem.length)
{
 that.G_node=that.ClosedElem.pop();
 if(!that.ClosedElem.length){
 that.onXLE = that.onLoadReserve;
 that.selectItem(that.G_node,that._opnItmsDnmcFlg);
}
 var temp=that._globalIdStorageFind(that.G_node);
 if(temp.XMLload)
 that._stepOpen(that);
 else
 that.openItem(that.G_node);
}
}



