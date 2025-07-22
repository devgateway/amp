if(!window.DHTMLSuite)var DHTMLSuite = new Object();
/************************************************************************************************************
*	DHTML dynamic tooltip script
*
*	Created:						August, 26th, 2006
*	@class Purpose of class:		Displays tooltips on screen with content from external files.
*			
*	Css files used by this script:	dynamic-tooltip.css
*
*	Demos of this class:			demo-dyn-tooltip-1.html
*
* 	Update log:
*
************************************************************************************************************/


/**
* @constructor
* @class Purpose of class:	Display a tooltip on screen with content from an external file(AJAX) (<a href="../../demos/demo-dyn-tooltip-1.html" target="_blank">Demo</a>)
* @version 1.0
* @author	Alf Magne Kalleland(www.dhtmlgoodies.com)
*/

DHTMLSuite.dynamicTooltip = function()
{
	var x_offset_tooltip;					// X Offset tooltip
	var y_offset_tooltip;					// Y offset tooltip
	var ajax_tooltipObj;
	var ajax_tooltipObj_iframe;
	var dynContentObj;						// Reference to dynamic content object
	var layoutCss;
	
	/* Offset position of tooltip */
	this.x_offset_tooltip = 5;
	this.y_offset_tooltip = 0;
	this.ajax_tooltipObj = false;
	this.ajax_tooltipObj_iframe = false;
	this.layoutCss = 'dynamic-tooltip.css';
	
	try{
		if(!standardObjectsCreated)DHTMLSuite.createStandardObjects();	// This line starts all the init methods
	}catch(e){
		alert('You need to include the dhtmlSuite-common.js file');
	}
	
}

DHTMLSuite.dynamicTooltip.prototype = {
	// {{{ displayTooltip(externalFile,inputObj)
    /**
     *	Hides the tooltip - should be called in onmouseout events
     * 	
     *	@param String externalfile - Relative path to external file
     * 	@param Object inputObj - Reference to tag on webpage.(usually "this" in an onmouseover event)
     *
     * @public	
     */	
	displayTooltip : function(externalFile,inputObj)
	{
		DHTMLSuite.commonObj.loadCSS(this.layoutCss);
		if(!this.dynContentObj){
			try{
				this.dynContentObj = new DHTMLSuite.dynamicContent();	// Creating dynamic content object if it doesn't already exist.
			}catch(e){
				alert('You need to include dhtmlSuite-dynamicContent.js');
			}
		}
				
		if(!this.ajax_tooltipObj)	/* Tooltip div not created yet ? */
		{
		
			this.ajax_tooltipObj = document.createElement('DIV');
			this.ajax_tooltipObj.style.position = 'absolute';
			this.ajax_tooltipObj.id = 'DHTMLSuite_ajax_tooltipObj';		
			document.body.appendChild(this.ajax_tooltipObj);
	
			
			var leftDiv = document.createElement('DIV');	/* Create arrow div */
			leftDiv.className='DHTMLSuite_ajax_tooltip_arrow';
			leftDiv.id = 'DHTMLSuite_ajax_tooltip_arrow';
			leftDiv.style.backgroundImage = 'url(\'' +  DHTMLSuite.configObj.imagePath + 'dyn-tooltip-arrow.gif' + '\')';
			this.ajax_tooltipObj.appendChild(leftDiv);
			
			var contentDiv = document.createElement('DIV'); /* Create tooltip content div */
			contentDiv.className = 'DHTMLSuite_ajax_tooltip_content';
			this.ajax_tooltipObj.appendChild(contentDiv);
			contentDiv.id = 'DHTMLSuite_ajax_tooltip_content';
			
			if(DHTMLSuite.clientInfoObj.isMSIE){	/* Create iframe object for MSIE in order to make the tooltip cover select boxes */
				this.ajax_tooltipObj_iframe = document.createElement('<IFRAME frameborder="0">');
				this.ajax_tooltipObj_iframe.style.position = 'absolute';
				this.ajax_tooltipObj_iframe.border='0';
				this.ajax_tooltipObj_iframe.frameborder=0;
				this.ajax_tooltipObj_iframe.style.backgroundColor='#FFF';
				this.ajax_tooltipObj_iframe.src = 'about:blank';
				contentDiv.appendChild(this.ajax_tooltipObj_iframe);
				this.ajax_tooltipObj_iframe.style.left = '0px';
				this.ajax_tooltipObj_iframe.style.top = '0px';
			}
	
				
		}
		// Find position of tooltip
		this.ajax_tooltipObj.style.display='block';
		this.dynContentObj.loadContent('DHTMLSuite_ajax_tooltip_content',externalFile);
		if(DHTMLSuite.clientInfoObj.isMSIE){
			this.ajax_tooltipObj_iframe.style.width = this.ajax_tooltipObj.clientWidth + 'px';
			this.ajax_tooltipObj_iframe.style.height = this.ajax_tooltipObj.clientHeight + 'px';
		}
	
		this.__positionTooltip(inputObj);
	}	
	// }}}	
	,
	// {{{ setLayoutCss(newCssFileName)
    /**
     *	Set new CSS file name
     *
     *	@param String newCssFileName - name of new css file. Should be called before any tooltips are displayed on the screen.	
     *
     * @public	
     */	
	setLayoutCss : function(newCssFileName)
	{
		this.layoutCss = newCssFileName;
	}	
	// }}}		
	,
	// {{{ hideTooltip()
    /**
     *	Hides the tooltip - should be called in onmouseout events
     * 	
     *
     * @public	
     */	
	hideTooltip : function()
	{
		this.ajax_tooltipObj.style.display='none';
	}	
	// }}}	
	,
	// {{{ __positionTooltip()
    /**
     *	Positions the tooltip
     * 	
     *	@param Object inputobject = Reference to element on web page. Used when the script determines where to place the tooltip
     *
     * @private	
     */	
	__positionTooltip : function(inputObj)
	{
		var leftPos = (DHTMLSuite.commonObj.getLeftPos(inputObj) + inputObj.offsetWidth);
		var topPos = DHTMLSuite.commonObj.getTopPos(inputObj);
		var tooltipWidth = document.getElementById('DHTMLSuite_ajax_tooltip_content').offsetWidth +  document.getElementById('DHTMLSuite_ajax_tooltip_arrow').offsetWidth; 
		
		this.ajax_tooltipObj.style.left = leftPos + 'px';
		this.ajax_tooltipObj.style.top = topPos + 'px';		
	}	
	
	
}