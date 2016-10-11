/**

@autor Sebastian Dimunzio
	   This a simple javascript number format
			var format=new Format(',',1,true,'.');
			var nume=Number(11036439.200);
			alert(nume.format(format));
**/


/*
	Parameters are: 
		decimalSymbol:  The character used for decimal separator
    	decimalPlaces:  How many places we will use on the decimal
    	useGroupSymbol: Indicate if the format wil include the group separator
    	groupSymbol:    The character used for group separator
*/

function createCookie(name,value,days) {
	if (days) {
		var date = new Date();
		date.setTime(date.getTime()+(days*24*60*60*1000));
		var expires = "; expires="+date.toGMTString();
	}
	else var expires = "";
	document.cookie = name+"="+value+expires+"; path=/";
}

function readCookie(name) {
	var nameEQ = name + "=";
	var ca = document.cookie.split(';');
	for(var i=0;i < ca.length;i++) {
		var c = ca[i];
		while (c.charAt(0)==' ') c = c.substring(1,c.length);
		if (c.indexOf(nameEQ) == 0) return c.substring(nameEQ.length,c.length);
	}
	return null;
}



function eraseCookie(name) {
	createCookie(name,"",-1);
}



function Format(decimalSymbol,decimalPlaces,useGroupSymbol,groupSymbol,groupSize){
	this.decimalSymbol=decimalSymbol;
	this.decimalPlaces=decimalPlaces;
	this.useGroupSymbol=useGroupSymbol;
	this.groupSymbol=groupSymbol;
	this.groupSize=groupSize;
}

Number.prototype.format=function(f){
	var snume=this.toString();
	var splitedSource=snume.split('.');
	var entPart=splitedSource[0];
	var decPart="0";

	if (splitedSource.length > 1){
		decPart=splitedSource[1];
	}
	//now are try to apply grouping format
	
	var newEntPart="";
	if (f.useGroupSymbol){
		
		if (entPart.length > f.groupSize) {
			var tmp="";
	
			while (entPart.length > 0){
				
				var curChar=entPart.charAt(entPart.length-1);
				tmp=curChar+tmp;			
				
				if (tmp.length==f.groupSize){
					if (entPart.length > 1){
						newEntPart=f.groupSymbol+tmp+newEntPart;
					}else{
					newEntPart=tmp+newEntPart;
					}
					tmp="";
				}
				
				entPart=entPart.substr(0,entPart.length-1);
			}
			
			entPart=tmp+newEntPart;
		}
		
	}
	//now we are going to work with decmial part
	
	if ((decPart.length!=f.decimalPlaces)&&(f.decimalPlaces!=-1)){
		if (decPart.length < f.decimalPlaces){
			for (i=0; i < (f.decimalPlaces-decPart.length)+1; i++){
			
				decPart=decPart+"0"
			}
		}else{
			decPart=decPart.substr(0,f.decimalPlaces);
		}
	}

	return entPart+f.decimalSymbol+decPart;
	
};


function addLoadEvent(func) {
  var oldonload = window.onload;
  if (typeof window.onload != 'function') {
    window.onload = func;
  } else {
    window.onload = function() {
      if (oldonload) {
        oldonload();
      }
      func();
    }
  }
}
