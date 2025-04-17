/**
* Asynchronous requests for Digi
* Author : Irakli Kobiashvili  ikobiashvili@powerdot.org
**/
function Asynchronous(){
         this._xmlhttp=factoryXMLHttpRequest();
}


function Asynchronous_call(url){
         var instance=this;
         //changed POST by get for AMP-6044
          this._xmlhttp.open('GET',url,true);
              
          	// this line is not functional on modern browsers - test if it is still relevant? 
            // disabling it because it pollutes the browser's error logs, masking legitimate ones
            /*if (url.indexOf("?") > 0){
	         	this._xmlhttp.setRequestHeader("Content-length",url.substr(url.indexOf("?")+1,url.length).length);
			 }else{
				this._xmlhttp.setRequestHeader("Content-length",0);
			 }*/
		
		 this._xmlhttp.onreadystatechange=function(){
            try{
            switch(instance._xmlhttp.readyState){
              case 1:
                   instance.loading();
                   break;
              case 2:
                   instance.loaded();
                   break;
              case 3:
                   instance.interactive();
                   break;
              case 4:
                   instance.complete(
                      instance._xmlhttp.status,
                      instance._xmlhttp.statusText,
                      instance._xmlhttp.responseText,
                      instance._xmlhttp.responseXML);
                   break;
            }
            }catch(e){
            //Commented by Sebastian dimunzio
            //Under a timer call, this will be show a recursive error, I will comment it untill we find a better way to hande the error
             /// alert('Error!!!'+e);
            }
         }

         this._xmlhttp.send(null);
}

function Asynchronous_loading(){
}
function Asynchronous_loaded(){
}
function Asynchronous_interactive(){
}
function Asynchronous_complete(status, statusText, responseText, responseHTML){
}


Asynchronous.prototype.loading=Asynchronous_loading;
Asynchronous.prototype.loaded=Asynchronous_loaded;
Asynchronous.prototype.interactive=Asynchronous_interactive;
Asynchronous.prototype.complete=Asynchronous_complete;

Asynchronous.prototype.call=Asynchronous_call;



function factoryXMLHttpRequest() {

  if (window.XMLHttpRequest) {
    return new XMLHttpRequest();
  } else if (window.ActiveXObject) {

    var msxmls=new Array(
        'Msxml2.XMLHTTP.5.0',
        'Msxml2.XMLHTTP.4.0',
        'Msxml2.XMLHTTP.3.0',
        'Msxml2.XMLHTTP',
        'Microsoft.XMLHTTP');

    for (var i=0; i<msxmls.length; i++){
        try{
          return new ActiveXObject(msxmls[i]);
        }catch(e){
        }
    }
  }
  throw new Error("Could not instantiate XMLHttpRequest!");
}
