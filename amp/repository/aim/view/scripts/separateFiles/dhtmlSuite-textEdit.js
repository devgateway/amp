if(!window.DHTMLSuite)var DHTMLSuite = new Object();
/************************************************************************************************************
*	DHTML Text Edit Class
*
*	Created:						November, 4th, 2006
*	@class Purpose of class:		Make standard HTML elements editable
*			
*	Css files used by this script:	text-edit.css
*
*	Demos of this class:			demo-text-edit.html
*
*	Uses classes:					DHTMLSuite.textEditModel
*									DHTMLSuite.listModel;
*
* 	Update log:
*
************************************************************************************************************/


/**
* @constructor
* @class Purpose of class:	Make standard HTML elements editable (<a href="../../demos/demo-text-edit.html" target="_blank">Demo</a>)
*							
* @version 1.0
* @author	Alf Magne Kalleland(www.dhtmlgoodies.com)
*/




DHTMLSuite.textEdit = function()
{
	var layoutCSS;			// Name of css file
	var elements;			// Array of editable elements
	var elementsAssociative;	// Associative version of the array above - need two because of conflicts with Prototype library when using for in loops.
	var serversideFile;		// Path to file on the server where changes are sent.
	var objectIndex;
	var inputObjects;		// Array of inputs or select boxes
	
	this.layoutCSS = 'text-edit.css';
	this.elements = new Array();
	this.elementsAssociative = new Array();
	this.inputObjects = new Array();
	
	try{
		if(!standardObjectsCreated)DHTMLSuite.createStandardObjects();
	}catch(e){
		alert('You need to include the dhtmlSuite-common.js file');
	}
	
	this.objectIndex = DHTMLSuite.variableStorage.arrayOfDhtmlSuiteObjects.length;
	DHTMLSuite.variableStorage.arrayOfDhtmlSuiteObjects[this.objectIndex] = this;
		
	
}

DHTMLSuite.textEdit.prototype = 
{	
	// {{{ setLayoutCss()
    /**
     *	Add menu items
     *
     *  @param String cssFileName Name of css file 	
     *
     *  @public	
     */	
	setLayoutCss : function(layoutCSS)
	{
		this.layoutCSS = layoutCSS;
	}
	// }}}
	,
	// {{{ setServersideFile()
    /**
     *	Specify server side file.
     *
     *  @param String serversideFile 	Path to server side file where changes are sent. This file will be called with the following arguments: saveTextEdit=1 and textEditElementId=<elementId> and textEditValue=<value>
     *									This file should return OK when everything went fine with the request
     *				  
     *
     *	@type void
     *  @public	
     */		
	setServersideFile : function(serversideFile)
	{
		this.serversideFile = serversideFile;
	}
	// }}}
	,
	// {{{ addElement()
    /**
     *	Add editable element
     *
     *  @param Array Element description = Associative array, possible keys: labelId,elementId,listModel,serverFile
     *		if serverFile is given, this value will override the serversideFile property of this class for this particular element
     *
     *	@type void
     *  @public	
     */	
	addElement : function(inputArray)
	{
		var index = this.elements.length;
		try{
			this.elements[index] = new DHTMLSuite.textEditModel(inputArray);	
		}catch(e){
			alert('Error: You need to include dhtmlSuite-textEditModel.js in your html file');
		}
		this.elementsAssociative[inputArray['elementId']] = this.elements[index];	
	}
	// }}}	
	,
	// {{{ init()
    /**
     *	Initializes the widget
     *
     *
     * @public	
     */		
	init : function()
	{
		DHTMLSuite.commonObj.loadCSS(this.layoutCSS);	
		
		var index = this.objectIndex;
		
		for(var no=0;no<this.elements.length;no++){
			var obj = this.elements[no];

			var label = document.getElementById(obj.labelId);
			label.setAttribute('elementId',obj.elementId);
			if(!label.getAttribute('elementId'))label.elementId = obj.elementId;
			if(label){
				if(label.className){
					label.setAttribute('origClassname',label.className);
					label.origClassname = label.className;
				}
				label.onclick = function(e){ DHTMLSuite.variableStorage.arrayOfDhtmlSuiteObjects[index].__clickOnLabel(e); }				
				DHTMLSuite.commonObj.__addEventElement(label);
			}
			
			var el = document.getElementById(obj.elementId);
			DHTMLSuite.commonObj.__addEventElement(el);
			if(el){
				
				el.onclick = function(e) { DHTMLSuite.variableStorage.arrayOfDhtmlSuiteObjects[index].__clickOnElement(e); }
				
				if(obj.listModel){	/* List model exists - create select box */
					this.inputObjects[obj.elementId] = document.createElement('SELECT');
					var selObj = this.inputObjects[obj.elementId];
					selObj.className = 'DHTMLSuite_textEdit_select';
					for(var no2=0;no2<obj.listModel.options.length;no2++){
						selObj.options[selObj.options.length] = new Option(obj.listModel.options[no2].text,obj.listModel.options[no2].value);					
					}
					selObj.id = 'input___' + el.id;
					selObj.onblur = function(e){ DHTMLSuite.variableStorage.arrayOfDhtmlSuiteObjects[index].__exitEditMode(e); }
					DHTMLSuite.commonObj.__addEventElement(selObj);
					el.parentNode.insertBefore(selObj,el);
					selObj.style.display='none';
				}else{
					this.inputObjects[obj.elementId] = document.createElement('INPUT');
					var input = this.inputObjects[obj.elementId];
					input.onblur = function(e){ DHTMLSuite.variableStorage.arrayOfDhtmlSuiteObjects[index].__exitEditMode(e); }
					DHTMLSuite.commonObj.__addEventElement(input);
										
					input.className = 'DHTMLSuite_textEdit_input';
					input.id = 'input___' + el.id;
					input.value = el.innerHTML;
					el.parentNode.insertBefore(input,el);
					input.style.display='none';
						
				}				
				
			}				
		}	
			
	}
	// }}}
	,
	// {{{ __setLabelClassName()
    /**
     *	Update the class for the label
     *
     *  @param Event e - Id of element
     *
     * @private	
     */			
	__setLabelClassName : function(obj,state)
	{
		if(state=='active')
			obj.className = 'DHTMLSuite_textEdit_label';	
		else{
			var className = '';
			className = obj.getAttribute('origClassname');
			if(!className)className = obj.origClassname;
			obj.className = className;	
		}
	}
	// }}}
	,
	// {{{ __clickOnLabel()
    /**
     *	Click on label
     *
     *  @param Event e - Id of element
     *
     * @private	
     */		
	__clickOnLabel : function(e)
	{
		if(document.all)e = event;
		var obj = DHTMLSuite.commonObj.getSrcElement(e);	// Reference to element triggering the event.
		this.__setLabelClassName(obj,'active');
		var elementId = obj.getAttribute('elementId');
		this.__clickOnElement(false,document.getElementById(elementId));		
	}	
	// }}}
	,
	// {{{ __clickOnElement()
    /**
     *	Click on editable element
     *
     *  @param Event e - Id of element
     *	@param Object obj - Element triggering the event(this value is empty when the method is fired by an event)
     *
     * @private	
     */			
	__clickOnElement : function(e,obj)
	{
		if(document.all)e = event;
		if(!obj)var obj = DHTMLSuite.commonObj.getSrcElement(e);	// Reference to element triggering the event.
		var id = obj.id;
		var dataSource = this.elementsAssociative[id];		
		if(dataSource.listModel)this.__setSelectBoxValue(id,obj.innerHTML);
		if(dataSource.labelId)this.__setLabelClassName(document.getElementById(dataSource.labelId),'active');
		this.inputObjects[id].style.display='';
		this.inputObjects[id].focus();
		if(!dataSource.listModel)this.inputObjects[id].select();
		obj.style.display='none';		
	}
	// }}}
	,
	// {{{ __setSelectBoxValue()
    /**
     *	Update select box to the value of the element
     *
     *  @param String id - Id of element
     *	@param String value - Value of element
     *
     * @private	
     */		
	__setSelectBoxValue : function(id,value)
	{
		var selObj = this.inputObjects[id];
		for(var no=0;no<selObj.options.length;no++){
			if(selObj.options[no].text==value){
				selObj.selectedIndex = no;
				return;
			}
		}		
	}
	// }}}
	,
	// {{{ __exitEditMode()
    /**
     *	Exit text edit mode
     *
     *  @param Event e - Event
     *
     * @private	
     */		
	__exitEditMode : function(e)
	{
		if(document.all)e = event;
		
		var obj = DHTMLSuite.commonObj.getSrcElement(e);	// Reference to element triggering the event.	
		var elementId = obj.id.replace('input___','');	
		
		var dataSource = this.elementsAssociative[elementId];		
		
		var newValue;
		var valueToSendToAjax;
		if(dataSource.listModel){
			 newValue = obj.options[obj.options.selectedIndex].text;
			 valueToSendToAjax = obj.options[obj.options.selectedIndex].value;
		}else{
			newValue = obj.value;
			valueToSendToAjax = newValue;
		}
		if(e.keyCode && e.keyCode==27)newValue = document.getElementById(dataSource.elementId).innerHTML;
		if(newValue && newValue!=document.getElementById(dataSource.elementId).innerHTML)this.__sendRequest(dataSource.elementId,valueToSendToAjax);	// Send ajax request when changes has been made.
		document.getElementById(dataSource.elementId).innerHTML = newValue;
		
		
		document.getElementById(dataSource.elementId).style.display='';
		obj.style.display='none';
		if(dataSource.labelId)this.__setLabelClassName(document.getElementById(dataSource.labelId),'inactive');
	}
	// }}}
	,
	// {{{ __sendRequest()
    /**
     *	Send textEdit changes to the server
     *
     *  @param String elementId - Id of changed element
     *  @param String value - Value of changed element
     *
     * @private	
     */		
	__sendRequest : function(elementId,value)
	{
		var index = DHTMLSuite.variableStorage.ajaxObjects.length;	
		var ind = this.objectIndex;
		try{
			DHTMLSuite.variableStorage.ajaxObjects[index] = new sack();
		}catch(e){	// Unable to create ajax object - send alert message and return from sort method.
			alert('Unable to create ajax object. Please make sure that the sack js file is included on your page');	
			return;
		}
		
		var url;
		if(this.elementsAssociative[elementId].serverFile)url = this.elementsAssociative[elementId].serverFile; else url = this.serversideFile;
		if(url.indexOf('?')>=0)url=url+'&'; else url=url+'?';		
		url = url + 'saveTextEdit=1&textEditElementId=' + elementId + '&textEditValue='+escape(value);

		DHTMLSuite.variableStorage.ajaxObjects[index].requestFile = url;	// Specifying which file to get
		DHTMLSuite.variableStorage.ajaxObjects[index].onCompletion = function(){ DHTMLSuite.variableStorage.arrayOfDhtmlSuiteObjects[ind].__handleServerSideResponse(index,url); };	// Specify function that will be executed after file has been found
		DHTMLSuite.variableStorage.ajaxObjects[index].onError = function(){ DHTMLSuite.variableStorage.arrayOfDhtmlSuiteObjects[ind].__handleAjaxError(index,url); };	// Specify function that will be executed after file has been found
		DHTMLSuite.variableStorage.ajaxObjects[index].runAJAX();		// Execute AJAX function			
	}
	// }}}
	,
	// {{{ __handleServerSideResponse()
    /**
     *	Verify response from ajax.
     *
     *  @param Integer ajaxIndex - Index of used sack() object
     *  @param String url - Failing url
     *
     * @private	
     */		
	__handleServerSideResponse : function(ajaxIndex,url)
	{
		if(DHTMLSuite.variableStorage.ajaxObjects[ajaxIndex].response!='OK'){
			alert('An error occured in the textEdit widget when calling the url\n' + url);	
		}	
		DHTMLSuite.variableStorage.ajaxObjects[ajaxIndex] = null;	
	}
	// }}}
	,
	// {{{ __handleAjaxError()
    /**
     *	Ajax request failed
     *
     *  @param Integer ajaxIndex - Index of used sack() object
     *  @param String url - Failing url
     *
     * @private	
     */		
	__handleAjaxError : function(ajaxIndex,url)
	{
		alert('Error when calling the url:\n' + url);
		DHTMLSuite.variableStorage.ajaxObjects[ajaxIndex] = null;	
	}	
	
}