/* Copyright (c) 2006, Yahoo! Inc. All rights reserved.Code licensed under the BSD License:http://developer.yahoo.net/yui/license.txt version: 0.12.0 */ if(typeof YAHOO=="undefined"){var YAHOO={};}YAHOO.namespace=function(){var a=arguments,o=null,i,j,d;for(i=0;i<a.length;++i){d=a[i].split(".");o=YAHOO;for(j=(d[0]=="YAHOO")?1:0;j<d.length;++j){o[d[j]]=o[d[j]]||{};o=o[d[j]];}}return o;};YAHOO.log=function(_2,_3,_4){var l=YAHOO.widget.Logger;if(l&&l.log){return l.log(_2,_3,_4);}else{return false;}};YAHOO.extend=function(_6,_7,_8){var F=function(){};F.prototype=_7.prototype;_6.prototype=new F();_6.prototype.constructor=_6;_6.superclass=_7.prototype;if(_7.prototype.constructor==Object.prototype.constructor){_7.prototype.constructor=_7;}if(_8){for(var i in _8){_6.prototype[i]=_8[i];}}};YAHOO.augment=function(r,s){var rp=r.prototype,sp=s.prototype,a=arguments,i,p;if(a[2]){for(i=2;i<a.length;++i){rp[a[i]]=sp[a[i]];}}else{for(p in sp){if(!rp[p]){rp[p]=sp[p];}}}};YAHOO.namespace("util","widget","example");