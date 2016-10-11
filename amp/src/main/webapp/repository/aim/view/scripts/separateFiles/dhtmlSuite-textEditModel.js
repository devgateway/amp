if(!window.DHTMLSuite)var DHTMLSuite = new Object();
/************************************************************************************************************
*	DHTML Text Edit Model Class
*
*	Created:						December, 14th, 2006
*	@class Purpose of class:		Data model for the textEdit class
*			
*	Css files used by this script:	
*
*	Demos of this class:			
*
*	Uses classes:					DHTMLSuite.listModel
*
* 	Update log:
*
************************************************************************************************************/


/**
* @constructor
* @class Purpose of class:	Data model for the textEdit class. (<a href="../../demos/demo-text-edit.html" target="_blank">Demo</a>)
* @version 1.0
* @author	Alf Magne Kalleland(www.dhtmlgoodies.com)
*/
DHTMLSuite.textEditModel = function(inputArray)
{
	var labelId;				// Id of label for editable element.
	var targetId;				// Id of editable element.
	var serversideFile;			// If individual serverside file should be used for this option
	var optionObj;				// Reference to object of class DHTMLSuite.listModel
	
	try{
		if(!standardObjectsCreated)DHTMLSuite.createStandardObjects();
	}catch(e){
		alert('You need to include the dhtmlSuite-common.js file');
	}
	
	if(inputArray)this.addElement(inputArray);
}

DHTMLSuite.textEditModel.prototype = 
{
	// {{{ addElement()
    /**
     *	Add item
     *
     *  @param Array inputArray - Associative array of properties, possible keys: labelId,elementId,serverFile,listModel
     *
     *  @public	
     */		
	addElement : function(inputArray)
	{
		if(inputArray['labelId'])this.labelId = inputArray['labelId'];	
		if(inputArray['elementId'])this.elementId = inputArray['elementId'];	
		if(inputArray['serverFile'])this.serverFile = inputArray['serverFile'];	
		if(inputArray['listModel'])this.listModel = inputArray['listModel'];	
	}
}