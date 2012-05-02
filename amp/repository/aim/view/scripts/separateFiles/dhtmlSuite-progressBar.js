if(!window.DHTMLSuite)var DHTMLSuite = new Object();
/************************************************************************************************************
*	DHTML progress bar script
*
*	Created:						October, 21st, 2006
*	@class Purpose of class:		Display a progress bar while content loads or dynamic content is created on the server.
*			
*	Css files used by this script:	progress-bar.css
*
*	Demos of this class:			demo-progress-bar.html
*
* 	Update log:
*
************************************************************************************************************/


/**
* @constructor
* @class Purpose of class:	Creates a progress bar. (<a href="../../demos/demo-progress-bar-1.html" target="_blank">Demo</a>)
* @version 1.0
* @author	Alf Magne Kalleland(www.dhtmlgoodies.com)
*/


DHTMLSuite.progressBar = function()
{

	var progressBar_steps;
	var div_progressPane;
	var div_progressBar_bg;
	var div_progressBar_outer;
	var div_progressBar_txt;
	
	var progressBarWidth;
	var currentStep;
	var layoutCSS;
	
	this.progressBar_steps = 50;
	this.progressPane = false;
	this.progressBar_bg = false;
	this.progressBar_outer = false;
	this.progressBar_txt = false;
	this.progressBarWidth;
	this.currentStep = 0;	
	this.layoutCSS = 'progress-bar.css';
	try{
		if(!standardObjectsCreated)DHTMLSuite.createStandardObjects();	
	}catch(e){
		alert('You need to include the dhtmlSuite-common.js file');
	}
}

DHTMLSuite.progressBar.prototype = {
	// {{{ setSteps()
    /**
     *	Initializes the progress bar script
     *
     *	@param Int numberOfSteps - Number of progress bar steps, example: 50 will show 2%,4%,6%...98%,100%.
     * 	
     *
     * @public	
     */			
	setSteps : function(numberOfSteps)
	{
		this.progressBar_steps = numberOfSteps;		
	}
	
	// }}}	
	,
	// {{{ init()
    /**
     *	Initializes the progress bar script
     *
     * 	
     *
     * @public	
     */		
	init : function()
	{		
		document.body.style.width = '100%';
		document.body.style.height = '100%';
		document.documentElement.style.overflow = 'hidden';
		DHTMLSuite.commonObj.loadCSS(this.layoutCSS);
		this.__createDivElementsForTheProgressBar();	
	}	
	// }}}	
	,
	// {{{ moveProgressBar()
    /**
     *	Moves the progress bar
     *
     *	@param Int Steps: Number of steps to move it. (Optional argument, if left empty, set the progress bar to 100% and hide it).
     * 	
     *
     * @public	
     */	
	moveProgressBar : function(steps){
		this.progressBarWidth = this.div_progressBar_bg.clientWidth;
		if(!steps){
			this.div_progressBar_outer.style.width = progressBarWidth + 'px';
			this.div_progressBar_txt.innerHTML = '100%';
			this.__hideProgressBar();
		}else{
			this.currentStep+=steps;
			if(this.currentStep>this.progressBar_steps)this.currentStep = this.progressBar_steps;
			var width = Math.ceil(this.progressBarWidth * (this.currentStep / this.progressBar_steps));
			this.div_progressBar_outer.style.width = width + 'px';
			
			var percent = Math.ceil((this.currentStep / this.progressBar_steps)*100);
			this.div_progressBar_txt.innerHTML = percent + '%';
			if(this.currentStep==this.progressBar_steps){
				this.__hideProgressBar();			
			}
		}	
	}
	// }}}	
	,
	// {{{ __hideProgressBar()
    /**
     *	Hides the progress bar when it's finished
     * 	
     *
     * @private	
     */	
	__hideProgressBar : function()
	{
		document.body.style.width = null;
		document.body.style.height = null;		
		document.documentElement.style.overflow = '';		
		setTimeout('document.getElementById("DHTMLSuite_progressPane").style.display="none"',50);
	}
	// }}}	
	,
	// {{{ __createDivElementsForTheProgressBar()
    /**
     *	Create the divs needed for the progress bar script
     * 	
     *
     * @private	
     */	
	__createDivElementsForTheProgressBar: function()
	{
		this.div_progressPane = document.createElement('DIV');
		this.div_progressPane.id = 'DHTMLSuite_progressPane';
		document.body.appendChild(this.div_progressPane);
		
		this.div_progressBar_bg = document.createElement('DIV');
		this.div_progressBar_bg.id = 'DHTMLSuite_progressBar_bg';
		this.div_progressPane.appendChild(this.div_progressBar_bg);		
		
		this.div_progressBar_outer = document.createElement('DIV');
		this.div_progressBar_outer.id='DHTMLSuite_progressBar_outer';
		this.div_progressBar_bg.appendChild(this.div_progressBar_outer);

		var div = document.createElement('DIV');
		div.id='DHTMLSuite_progressBar';
		this.div_progressBar_outer.appendChild(div);
		
		this.div_progressBar_txt = document.createElement('DIV');
		this.div_progressBar_txt.id='DHTMLSuite_progressBar_txt';
		this.div_progressBar_txt.innerHTML = '0 %';
		this.div_progressBar_bg.appendChild(this.div_progressBar_txt);			
	}		
}