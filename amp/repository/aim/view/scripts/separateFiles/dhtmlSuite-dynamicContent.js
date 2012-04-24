if(!window.DHTMLSuite)var DHTMLSuite = new Object();
/************************************************************************************************************
*	Ajax dynamic content script
*
*	Created:					August, 23rd, 2006
*
*			
* 	Update log:
*
************************************************************************************************************/

/**
* @constructor
* @class The purpose of this class is to load content of external files into HTML elements on your page(<a href="../../demos/demo-dynamic-content-1.html" target="_blank">demo</a>).
* @version				1.0
* @version 1.0
* 
* @author	Alf Magne Kalleland(www.dhtmlgoodies.com)
**/

DHTMLSuite.dynamicContent = function()
{
	var enableCache;	// Cache enabled.
	var jsCache;
	var dynamicContent_ajaxObjects;
	var waitMessage;
	
	this.enableCache = true;
	this.jsCache = new Array();
	this.dynamicContent_ajaxObjects = new Array();
	this.waitMessage = 'Loading content - please wait...';
	this.waitImage = 'ajax-loader-darkblue.gif';
	try{
		if(!standardObjectsCreated)DHTMLSuite.createStandardObjects();	// This line starts all the init methods
	}catch(e){
		alert('You need to include the dhtmlSuite-common.js file');
	}
	var objectIndex;
	
	this.objectIndex = DHTMLSuite.variableStorage.arrayOfDhtmlSuiteObjects.length;
	DHTMLSuite.variableStorage.arrayOfDhtmlSuiteObjects[this.objectIndex] = this;
		
}

DHTMLSuite.dynamicContent.prototype = {

	// {{{ loadContent()
    /**
     * Load content from external files into an element on your web page.
     *
     * @param String divId = Id of HTML element
     * @param String url = Path to content on the server(Local content only)
     * @param String functionToCallOnLoaded = Function to call when ajax is finished. This string will be evaulated, example of string: "fixContent()" (with the quotes).
     * 
     * @public
     */	
	loadContent : function(divId,url,functionToCallOnLoaded)
	{
		
		var ind = this.objectIndex;
		if(this.enableCache && this.jsCache[url]){
			document.getElementById(divId).innerHTML = this.jsCache[url];
			this.__evaluateJs(document.getElementById(divId));	// Call private method which evaluates JS content
			this.__evaluateCss(document.getElementById(divId));	// Call private method which evaluates JS content			
			return;
		}
		var ajaxIndex = 0;
		
		/* Generating please wait message */
		var waitMessageToShow = '';
		if(this.waitImage){	// Wait image exists ?
			waitMessageToShow = waitMessageToShow + '<div style="text-align:center;padding:10px"><img src="' + DHTMLSuite.configObj.imagePath + this.waitImage + '" border="0" alt=""></div>';
		}
		if(this.waitMessage){	// Wait message exists ?
			waitMessageToShow = waitMessageToShow + '<div style="text-align:center">' + this.waitMessage + '</div>';
		}		
		try{
			document.getElementById(divId).innerHTML = waitMessageToShow ;
		}catch(e){
			
		}		
		
		var ajaxIndex = this.dynamicContent_ajaxObjects.length;
		try{
			this.dynamicContent_ajaxObjects[ajaxIndex] = new sack();
		}catch(e){
			alert('Could not create ajax object. Please make sure that ajax.js is included');
		}
		this.dynamicContent_ajaxObjects[ajaxIndex].requestFile = url;	// Specifying which file to get

		
		this.dynamicContent_ajaxObjects[ajaxIndex].onCompletion = function(){ DHTMLSuite.variableStorage.arrayOfDhtmlSuiteObjects[ind].__ajax_showContent(divId,ajaxIndex,url,functionToCallOnLoaded); };	// Specify function that will be executed after file has been found
		this.dynamicContent_ajaxObjects[ajaxIndex].onError = function(){ DHTMLSuite.variableStorage.arrayOfDhtmlSuiteObjects[ind].__ajax_displayError(divId,ajaxIndex,url,functionToCallOnLoaded); };	// Specify function that will be executed after file has been found
		this.dynamicContent_ajaxObjects[ajaxIndex].runAJAX();		// Execute AJAX function	
	}
	// }}}		
	,
	// {{{ setWaitMessage()
    /**
     * Specify which message to show when Ajax is busy.
     *
     * @param String newWaitMessage = New wait message (Default = "Loading content - please wait") - use false if you don't want any wait message
     * 
     * @public
     */		
	setWaitMessage : function(newWaitMessage)
	{
		this.waitMessage = newWaitMessage;		
	}
	// }}}
	,
	// {{{ setWaitImage()
    /**
     * Specify an image to show when Ajax is busy working.
     *
     * @param String newWaitImage = New wait image ( default = ajax-loader-blue.gif - it is by default located inside the image_dhtmlsuite folder. - If you like a new image, try to generate one at http://www.ajaxload.info/
     * 
     * @public
     */		
	setWaitImage : function(newWaitImage)
	{
		this.waitImage = newWaitImage;
	}
	// }}}
	,
	// {{{ setCache()
    /**
     * Cancel selection when drag is in process
     *
     * @param Boolean enableCache = true if you want to enable cache, false otherwise(default is true). You can also send HTMl code in here, example an &lt;img> tag.
     * 
     * @public
     */		
	setCache : function(enableCache)
	{
		this.enableCache = enableCache;		
	}
	// }}}
	,
	// {{{ __evaluateJs()
    /**
     * Evaluate Javascript in the inserted content
     *
     * @private
     */		
	__ajax_showContent :function(divId,ajaxIndex,url,functionToCallOnLoaded)
	{
		var obj = document.getElementById(divId);
		obj.innerHTML = this.dynamicContent_ajaxObjects[ajaxIndex].response;
		
		if(this.enableCache){	// Cache is enabled
			this.jsCache[url] = this.dynamicContent_ajaxObjects[ajaxIndex].response;	// Put content into cache
		}
		
		this.__evaluateJs(obj);	// Call private method which evaluates JS content
		this.__evaluateCss(obj);	// Call private method which evaluates JS content
		if(functionToCallOnLoaded)eval(functionToCallOnLoaded);
		this.dynamicContent_ajaxObjects[ajaxIndex] = null;	// Clear sack object
	}
	// }}}
	,
	// {{{ __ajax_displayError()
    /**
     * Display error message when the request failed.
     *
     * @private
     */		
	__ajax_displayError : function(divId,ajaxIndex,url,functionToCallOnLoaded)
	{
		document.getElementById(divId).innerHTML = '<h2>Message from DHTMLSuite.dynamicContent</h2><p>The ajax request for ' + url + ' failed</p>';		
	}
	// }}}		
	,	
	// {{{ __evaluateJs()
    /**
     * Evaluate Javascript in the inserted content
     *
     * @private
     */	
	__evaluateJs : function(obj)
	{
		var scriptTags = obj.getElementsByTagName('SCRIPT');
		var string = '';
		var jsCode = '';
		for(var no=0;no<scriptTags.length;no++){	
			if(scriptTags[no].src){
		        var head = document.getElementsByTagName("head")[0];
		        var scriptObj = document.createElement("script");
		
		        scriptObj.setAttribute("type", "text/javascript");
		        scriptObj.setAttribute("src", scriptTags[no].src);  	
			}else{
				if(DHTMLSuite.clientInfoObj.isOpera){
					jsCode = jsCode + scriptTags[no].text + '\n';
				}
				else
					jsCode = jsCode + scriptTags[no].innerHTML;	
			}
			
		}

		if(jsCode)this.__installScript(jsCode);
	}
	// }}}
	,
	// {{{ __evaluateJs()
    /**
     *  "Installs" the content of a <script> tag.
     *
     * @private        
     */		
	__installScript : function ( script )
	{		
	    if (!script)
	        return;		
        if (window.execScript){        	
        	window.execScript(script)
        }else if(window.jQuery && jQuery.browser.safari){ // safari detection in jQuery
            window.setTimeout(script,0);
        }else{        	
            window.setTimeout( script, 0 );
        } 
	}	
	// }}}
	,
	// {{{ __evaluateCss()
    /**
     *  Evaluates css
     *
     * @private        
     */	
	__evaluateCss : function(obj)
	{
		var cssTags = obj.getElementsByTagName('STYLE');
		var head = document.getElementsByTagName('HEAD')[0];
		for(var no=0;no<cssTags.length;no++){
			head.appendChild(cssTags[no]);
		}	
	}
	
}