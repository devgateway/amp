if(!window.DHTMLSuite)var DHTMLSuite = new Object();
/************************************************************************************************************
*	listModel
*
*	Created:						December, 14th, 2006
*	@class Purpose of class:		An object storing a collection of values and texts
*			
*	Css files used by this script:	
*
*	Demos of this class:			
*
*	Uses classes:					DHTMLSuite.textEditModel
*
* 	Update log:
*
************************************************************************************************************/


/**
* @constructor
* @class Purpose of class:	listModel (<a href="../../demos/demo-text-edit.html" target="_blank">Demo</a>)
* @version 1.0
* @author	Alf Magne Kalleland(www.dhtmlgoodies.com)
*/
DHTMLSuite.listModel = function(inputArray)
{
	var options;
	this.options = new Array();
}



DHTMLSuite.listModel.prototype = 
{
	// {{{ addElement()
    /**
     *	Add a single element to the listModel
     *
     *  @param String value = Value of element
     *  @param String text = Text of element
     *
     *  @public	
     */		
	addElement : function(value,text)
	{
		var index = this.options.length;
		this.options[index] = new Array();
		this.options[index]['value'] = value;
		this.options[index]['text'] = text;
	}
	,
	// {{{ createFromMarkupSelect()
    /**
     *	Create listModel object from Select tag. value and text of option tags becomes value and text in the listModel.
     *	This method hides the select box when done.
     *
     *  @param String elId Id of SELECT tag
     *
     *  @public	
     */		
	createFromMarkupSelect : function(elId)
	{
		var obj = document.getElementById(elId);
		if(obj && obj.tagName.toLowerCase()!='select')obj = false;
		if(!obj){
			alert('Error in listModel.createFromMarkupSelect - cannot create elements from select box with id ' + elId);
			return;
		}	
		for(var no=0;no<obj.options.length;no++){
			var index = this.options.length;
			this.options[index] = new Array();
			this.options[index]['value'] = obj.options[no].value;
			this.options[index]['text'] = obj.options[no].text;
		}	
		obj.style.display='none';	
	}
	,
	// {{{ createFromMarkupUlLi()
    /**
     *	Create listModel object from UL,LI tags. the value is the title of the lis, text is innerHTML, example <LI title="1">Norway</li>
     *	This methods hides the UL object
     *
     *  @param String elId Id of UL tag
     *
     *  @public	
     */		
	createFromMarkupUlLi : function(elId)
	{
		var obj = document.getElementById(elId);
		if(obj && obj.tagName.toLowerCase()!='ul')obj = false;
		if(!obj){
			alert('Error in listModel.createFromMarkupSelect - cannot create elements from select box with id ' + elId);
			return;
		}			
		var lis = obj.getElementsByTagName('LI');
		for(var no=0;no<lis.length;no++){
			var index = this.options.length;
			this.options[index] = new Array();
			this.options[index]['value'] = lis[no].getAttribute('title');
			if(!this.options[index]['value'])this.options[index]['value'] = lis[no].title;
			this.options[index]['text'] = lis[no].innerHTML;			
		}
		obj.style.display='none';
	}
}