if(!window.DHTMLSuite)var DHTMLSuite = new Object();
/************************************************************************************************************
*	Table widget page handler class
*
*	Created:			December, 15th, 2006
*	Purpose of class:	Displays paginating below a server sorted table
*
*	CSS used:			
*
* 	Update log:
*
************************************************************************************************************/

/**
* @constructor
* @class Purpose of class:	Make HTML tables sortable<br><br>
*/
DHTMLSuite.tableWidgetPageHandler = function()
{
	var tableRef;					// Reference to object of class DHTMLSuite.tableWidget
	var targetRef;					// Where to insert the pagination.
	
	var txtPrevious;				// Label - "Previous"
	var txtNext;					// Label - "Next"
	var txtFirst;					// Label - "First"
	var txtLast;					// Label - "last"
	
	var txtResultPrefix;			// Prefix : result - default = "Result: "
	var txtResultTo;				// Text label Result: 1 "to" 10 of 51 - default value = "to"
	var txtResultOf;				// Text label Result: 1 to 10 "of" 51 - default value = "of"
	
	var totalNumberOfRows;			// Total number of rows in dataset
	var rowsPerPage;				// Number of rows per page.
	
	var layoutCSS;					// Name of CSS file for the table widget.
	var activePageNumber;			// Active page number
	var mainDivElement;				// Reference to main div for the page handler
	var resultDivElement;			// Reference to div element which is parent for the result
	var pageListDivElement;			// Reference to div element which is parent to pages [1],[2],[3]...[Next]
	
	var objectIndex;				// Index of this widget in the arrayOfDhtmlSuiteObjects array
	
	var linkPagePrefix;				// Text in front of each page link
	var linkPageSuffix;				// Text behind each page link
	
	var maximumNumberOfPageLinks;	// Maximum number of page links.
	var callbackOnAfterNavigate;	// Callback function - executed when someone navigates to a different page
	this.txtPrevious = 'Previous';	// Default label
	this.txtNext = 'Next';			// Default label
	this.txtResultPrefix = 'Result: ';			// Default label
	this.txtResultTo = 'to';			// Default label
	this.txtResultOf = 'of';			// Default label
	this.txtFirst = 'First';
	this.txtLast = 'Last';
	
	this.tableRef = false;
	this.targetRef = false;
	this.totalNumberOfRows = false;
	this.activePageNumber = 1;
	this.layoutCSS = 'table-widget-page-handler.css';
	
	this.linkPagePrefix = '';
	this.linkPageSuffix = '';
	this.maximumNumberOfPageLinks = false;
	this.callbackOnAfterNavigate = false;
	
	
	this.objectIndex = DHTMLSuite.variableStorage.arrayOfDhtmlSuiteObjects.length;
	DHTMLSuite.variableStorage.arrayOfDhtmlSuiteObjects[this.objectIndex] = this;
		
	
}

DHTMLSuite.tableWidgetPageHandler.prototype = {
	// {{{ setTableRef()
    /**
     *	Connect to a tableWidget object.
     *
	 *	@param Object tableRef = An object of class DHTMLSuite.tableWidget. It makes it possible for the tableWidget and this object to communicate.
     * 
     * @public
     */		
	setTableRef : function(tableRef)
	{
		this.tableRef = tableRef;
		this.tableRef.setPageHandler(this);
	}	
	// }}}
	,
	// {{{ setTargetId()
    /**
     *	Where do you want to insert the navigation links for the table
     *
	 *	@param String idOfHTMLElement = Id of HTML Element on your page.
     * 
     * @public
     */		
	setTargetId : function(idOfHTMLElement)
	{
		if(!document.getElementById(idOfHTMLElement)){
			alert('ERROR IN tableWidgetPageHandler.setTargetId:\nElement with id ' + idOfHTMLElement + ' does not exists');
			return;
		}
		this.targetRef = document.getElementById(idOfHTMLElement);		
	}
	// }}}
	,
	// {{{ setTxtPrevious()
    /**
     *	Set text label (previous page link)
     *
	 *	@param String newText = Text previous page link
     * 
     * @public
     */		
	setTxtPrevious : function(newText)
	{
		this.txtPrevious = newText;
	}
	// }}}
	,
	// {{{ setLinkPagePrefix()
    /**
     *	Set text/characters in front of each page link, example "[" to get page number in brackets
     *
	 *	@param String linkPagePrefix = Character(s) in front of page links
     * 
     * @public
     */		
	setLinkPagePrefix : function(linkPagePrefix)
	{
		this.linkPagePrefix = linkPagePrefix;
	}
	// }}}
	,
	// {{{ setLinkPageSuffix()
    /**
     *	Set text/characters in front of each page link, example "[" to get page number in brackets
     *
	 *	@param String linkPageSuffix = Character(s) in front of page links
     * 
     * @public
     */		
	setLinkPageSuffix : function(linkPageSuffix)
	{
		this.linkPageSuffix = linkPageSuffix;
	}
	
	// }}}
	,
	// {{{ setTxtNext()
    /**
     *	Set text label (next page link)
     *
	 *	@param String newText = Text next page link
     * 
     * @public
     */		
	setTxtNext : function(newText)
	{
		this.txtNext = newText;
	}
	// }}}
	,
	// {{{ setTxtResultOf()
    /**
     *	Set text label ("of" - result)
     *
	 *	@param String txtResultOf = Result of search, the "of" label ( Result: 1 to 10 "of" 51 )
     * 
     * @public
     */		
	setTxtResultOf : function(txtResultOf)
	{
		this.txtResultOf = txtResultOf;
	}
	// }}}
	,
	// {{{ setTxtResultTo()
    /**
     *	Set text label ("to" - result)
     *
	 *	@param String txtResultTo = Result of search, the "to" label ( Result: 1 "to" 10 of 51 )
     * 
     * @public
     */		
	setTxtResultTo : function(txtResultTo)
	{
		this.txtResultTo = txtResultTo;
	}
	// }}}
	,
	// {{{ setTxtResultPrefix()
    /**
     *	Set text label (prefix - result)
     *
	 *	@param String txtResultPrefix = Text next page link
     * 
     * @public
     */		
	setTxtResultPrefix : function(txtResultPrefix)
	{
		this.txtResultPrefix = txtResultPrefix;
	}
	// }}}
	,
	// {{{ setTxtFirstPage()
    /**
     *	Set text label ("Last" page)
     *
	 *	@param String txtFirst = Label of link to "First" page ( default = "First" ) .This option is only used when you are limiting the number of pages shown.
     * 
     * @public
     */		
	setTxtFirstPage : function(txtFirst)
	{
		this.txtFirst = txtFirst;
	}
	// }}}
	,
	// {{{ setTxtLastPage()
    /**
     *	Set text label ("First" page)
     *
	 *	@param String txtLast = Label of link to "Last" page ( default = "Last" ) .This option is only used when you are limiting the number of pages shown.
     * 
     * @public
     */		
	setTxtLastPage : function(txtLast)
	{
		this.txtLast = txtLast;
	}
	// }}}
	,
	// {{{ setTotalNumberOfRows()
    /**
     *	Specify total number of rows in the entire dataset
     *
	 *	@param Integer totalNumberOfRows = Total number of rows in the entire dataset.
     * 
     * @public
     */		
	setTotalNumberOfRows : function(totalNumberOfRows)
	{
		this.totalNumberOfRows = totalNumberOfRows;
	}
	// }}}
	,
	// {{{ setCallbackOnAfterNavigate()
    /**
     * Specify call back function to execute after page navigatoin
     *
     * @param String callbackOnAfterNavigate - name of javascript function.
     *
     * @public
     */		
	setCallbackOnAfterNavigate : function(callbackOnAfterNavigate)
	{
		this.callbackOnAfterNavigate = callbackOnAfterNavigate;
	}
	// }}}
	,
	// {{{ setLayoutCss()
    /**
     * set new CSS file
     *
     * @param String cssFileName - name of new css file(example: drag-drop.css). Has to be set before init is called. 
     *
     * @public
     */	
	setLayoutCss : function(layoutCSS)
	{
		this.layoutCSS = layoutCSS;
	}
	// }}}
	,
	// {{{ setMaximumNumberOfPageLinks()
    /**
     * Set maximum number of page links displayed below the table, i.e. if you have 50 pages, you can limit number of page links to 10 by sending 10 to this method.
     *
     * @param Integer maximumNumberOfPageLinks - (0 or false means = no limitation)
     *
     * @public
     */		
	setMaximumNumberOfPageLinks : function(maximumNumberOfPageLinks)
	{
		this.maximumNumberOfPageLinks = maximumNumberOfPageLinks;
	}
	// }}}
	,
	// {{{ init()
    /**
     * Initializes the script widget. Set methods should be called before your call this method.
     *
     *
     * @public
     */		
	init : function()
	{
		this.rowsPerPage = this.tableRef.getServersideSortNumberOfRows();
		DHTMLSuite.commonObj.loadCSS(this.layoutCSS);
		this.__createMainDivElements();
		this.setHTMLOfResultList();
		this.__createPageLinks();
	}
	// }}}
	,
	// {{{ __createMainDivElements()
    /**
     * Create main div elements for the page handler
     *
     *
     * @private
     */		
	__createMainDivElements : function()
	{
		if(!this.targetRef){
			alert('Error creating table widget page handler. Remember to specify targetRef');
			return;
		}
		this.mainDivElement = document.createElement('DIV');
		this.mainDivElement.className = 'DHTMLSuite_tableWidgetPageHandler_mainDiv';
		this.targetRef.appendChild(this.mainDivElement);		
		
		this.resultDivElement = document.createElement('DIV');
		this.resultDivElement.className = 'DHTMLSuite_tableWidgetPageHandler_result';
		this.mainDivElement.appendChild(this.resultDivElement);
		
		this.pageListDivElement = document.createElement('DIV');
		this.pageListDivElement.className = 'DHTMLSuite_tableWidgetPageHandler_pageList';
		this.mainDivElement.appendChild(this.pageListDivElement);
	}
	
	,
	// {{{ setHTMLOfResultList()
    /**
     *
     * 	Create result list div
     *	
	 *
     * 
     * @public
     */  	
	setHTMLOfResultList : function()
	{
		this.resultDivElement.innerHTML = '';		
		var html = this.txtResultPrefix + (((this.activePageNumber-1) * this.rowsPerPage) + 1) + ' ' + this.txtResultTo + ' ' + Math.min(this.totalNumberOfRows,(this.activePageNumber * this.rowsPerPage)) + ' ' + this.txtResultOf + ' ' + this.totalNumberOfRows;
		this.resultDivElement.innerHTML = html;
	}
	// }}}
	,
	// {{{ __createPageLinks()
    /**
     *
     * 	Create page links
     *	
	 *
     * 
     * @private
     */  	
	__createPageLinks : function()
	{
		var ind = this.objectIndex;
		
		this.pageListDivElement.innerHTML = '';	// Clearing the div element if it allready got content.

		var numberOfPages = Math.ceil(this.totalNumberOfRows/this.rowsPerPage);
		
		/* link to first page */
		if(this.maximumNumberOfPageLinks && this.maximumNumberOfPageLinks<numberOfPages){
			var span = document.createElement('SPAN');
			span.innerHTML = this.linkPagePrefix;
			this.pageListDivElement.appendChild(span);	
			span.className = 'DHTMLSuite_pageHandler_firstLink';
						
			var firstLink = document.createElement('A');	// "first" link
			firstLink.innerHTML = this.txtFirst;
			firstLink.href = '#';
			firstLink.id = 'pageLink_1';
			firstLink.onclick = function(e){ return DHTMLSuite.variableStorage.arrayOfDhtmlSuiteObjects[ind].__navigate(e); }
			span.appendChild(firstLink);
			DHTMLSuite.commonObj.__addEventElement(firstLink);				
		}					
		
		
		var span = document.createElement('SPAN');
		span.innerHTML = this.linkPagePrefix;
		this.pageListDivElement.appendChild(span);	
		span.className = 'DHTMLSuite_pageHandler_previousLink';
				
		var previousLink = document.createElement('A');	// "Previous" link
		previousLink.innerHTML = this.txtPrevious;
		previousLink.href = '#';
		previousLink.id = 'previous';
		previousLink.onclick = function(e){ return DHTMLSuite.variableStorage.arrayOfDhtmlSuiteObjects[ind].__navigate(e); }
		span.appendChild(previousLink);
		DHTMLSuite.commonObj.__addEventElement(previousLink);
		if(this.activePageNumber==1)previousLink.className = 'previousLinkDisabled'; else previousLink.className = 'previousLink';
		
		
		
		var startNumberToShow = 1;
		var endNumberToShow = numberOfPages;
		if(this.maximumNumberOfPageLinks && this.maximumNumberOfPageLinks<numberOfPages){
			startNumberToShow = Math.max(1,Math.round(this.activePageNumber - this.maximumNumberOfPageLinks/2));
			endNumberToShow = Math.min(numberOfPages,startNumberToShow + this.maximumNumberOfPageLinks - 1);	
			
			if(endNumberToShow-startNumberToShow < this.maximumNumberOfPageLinks){
				startNumberToShow = Math.max(1,endNumberToShow - this.maximumNumberOfPageLinks + 1);
			}
					
		}
		
		
		for(var no=startNumberToShow;no<=endNumberToShow;no++){
			
			var span = document.createElement('SPAN');
			span.innerHTML = this.linkPagePrefix;
			this.pageListDivElement.appendChild(span);	
			
			
			var pageLink = document.createElement('A');
			if(no==this.activePageNumber)pageLink.className='DHTMLSuite_tableWidgetPageHandler_activePage'; else pageLink.className = 'DHTMLSuite_tableWidgetPageHandler_inactivePage';
			pageLink.innerHTML = no;
			pageLink.href= '#';
			pageLink.id = 'pageLink_' + no;
			pageLink.onclick = function(e){ return DHTMLSuite.variableStorage.arrayOfDhtmlSuiteObjects[ind].__navigate(e); }
			DHTMLSuite.commonObj.__addEventElement(pageLink);
			this.pageListDivElement.appendChild(pageLink);		
			
			var span = document.createElement('SPAN');
			span.innerHTML = this.linkPageSuffix;
			this.pageListDivElement.appendChild(span);	
							
		}
		
		var span = document.createElement('SPAN');
		span.innerHTML = this.linkPagePrefix;
		this.pageListDivElement.appendChild(span);	
		span.className = 'DHTMLSuite_pageHandler_nextLink';
					
		var nextLink = document.createElement('A');	// "Next" link
		nextLink.innerHTML = this.txtNext;
		nextLink.id = 'next';
		nextLink.href = '#';
		nextLink.onclick = function(e){ return DHTMLSuite.variableStorage.arrayOfDhtmlSuiteObjects[ind].__navigate(e); }
		DHTMLSuite.commonObj.__addEventElement(nextLink);
		span.appendChild(nextLink);
		if(this.activePageNumber==numberOfPages)nextLink.className = 'nextLinkDisabled'; else nextLink.className = 'nextLink';
		
		/* link to Last page */
		if(this.maximumNumberOfPageLinks && this.maximumNumberOfPageLinks<numberOfPages){
			var span = document.createElement('SPAN');
			span.innerHTML = this.linkPagePrefix;
			this.pageListDivElement.appendChild(span);	
			span.className = 'DHTMLSuite_pageHandler_lastLink';
			
			var lastLink = document.createElement('A');	// "Last" link
			lastLink.innerHTML = this.txtLast;
			lastLink.href = '#';
			lastLink.id = 'pageLink_' + (numberOfPages);
			lastLink.onclick = function(e){ return DHTMLSuite.variableStorage.arrayOfDhtmlSuiteObjects[ind].__navigate(e); }
			span.appendChild(lastLink);
			DHTMLSuite.commonObj.__addEventElement(lastLink);				
		}
				
		
	}
	// }}}
	,
	// {{{ __navigate()
    /**
     *
     * 	Navigate - click on "next" or "previous" link or click on a page
     *	
     *	@param Event e	= Reference to event object. used to get a reference to the element triggering this action.
	 *
     * 
     * @private
     */  	
	__navigate : function(e)
	{
		if(document.all)e = event;
		var src = DHTMLSuite.commonObj.getSrcElement(e);
		var initActivePageNumber = this.activePageNumber;
		var numberOfPages = Math.ceil(this.totalNumberOfRows/this.rowsPerPage);
		
		if(src.id.indexOf('pageLink_')>=0){
			var pageNo = src.id.replace(/[^0-9]/gi,'')/1;
			this.activePageNumber = pageNo;
			
		}
		if(src.id=='next'){	// next link clicked
			this.activePageNumber++;
			if(this.activePageNumber>numberOfPages)this.activePageNumber = numberOfPages;		
		}
		if(src.id=='previous'){
			this.activePageNumber--;
			if(this.activePageNumber<1)this.activePageNumber=1;
		}
				
		if(this.activePageNumber!=initActivePageNumber){
			this.tableRef.serversideSortCurrentStartIndex = ((this.activePageNumber-1)*this.rowsPerPage);
			this.tableRef.__getItemsFromServer(this.callbackOnAfterNavigate);
			this.setHTMLOfResultList();
			this.__createPageLinks();
		}
		return false;
		
	}
	// }}}
	,
	// {{{ __resetActivePageNumber()
    /**
     *
     * 	Reset active page number - called from the tableWidget
	 *
     * 
     * @private
     */   	
	__resetActivePageNumber : function()
	{
		this.activePageNumber = 1;
		this.setHTMLOfResultList();
		this.__createPageLinks();
	}
}


/************************************************************************************************************
*	Table widget class
*
*	Created:			August, 18th, 2006
*	Purpose of class:	Make HTML tables sortable
*						Apply application look to the table
*						Create one object for each HTML table.
*
*	CSS used:			table-widget.css
*	images used:		arrow_up.gif
* 						arrow_down.gif
*
* 	Update log:
*
************************************************************************************************************/

/**
* @constructor
* @class Purpose of class:	Make HTML tables sortable<br><br>
*						Apply application look to the table<br>
*						Create one object for each HTML table.<br>
*<br>
*	Remember to have both &lt;THEAD> and &lt;TBODY> in your table.
* <br>
*	&lt;DIV><br>
*	&lt;table><br>
*		&lt;thead><br>
*			&lt;tr><br>
*				&lt;td>Header cell&lt;/td><br>
*				&lt;td>Header cell&lt;/td><br>
*			&lt;/tr><br>
*		&lt;/thead><br>
*		&lt;tbody><br>
*			&lt;tr><br>
*				&lt;td>Table data&lt;/td><br>
*				&lt;td>Table data&lt;/td><br>
*			&lt;/tr><br>
*			&lt;tr><br>
*				&lt;td>Table data&lt;/td><br>
*				&lt;td>Table data&lt;/td><br>
*			&lt;/tr><br>
*		&lt;/tbody><br>
*	&lt;/table><br>
*	&lt;/div><br>
*	<br><br>
*	Also remember:	If you put a table inside a non-displayed element, example an inactive tab(the tabView script), remember to create
*	and initialize the table objects before you create the tab objects. In some browsers, that's nescessary in order for the table to
*	display properly. <br>
*	(<a href="../../demos/demo-tablewidget.html" target="_blank">demo 1</a>)
*
* @version 1.0
* @author	Alf Magne Kalleland(www.dhtmlgoodies.com)
**/

DHTMLSuite.tableWidget = function()
{
	var tableWidget_okToSort;				// Variable indicating if it's ok to sort. This variable is "false" when sorting is in progress
	var activeColumn;						// Reference to active column, i.e. column currently beeing sorted	
	var idOfTable;							// Id of table, i.e. the <table> tag
	var tableObj;							// Reference to <table> tag.
	var widthOfTable;						// Width of table	(Used in the CSS)
	var heightOfTable; 						// Height of table	(Used in the CSS)
	var columnSortArray;					// Array of how table columns should be sorted
	var layoutCSS;							// Name of CSS file for the table widget.
	var noCssLayout;						// true or false, indicating if the table should have layout or not, if not, it would be a plain sortable table.
	var serversideSort;						// true or false, true if the widget is sorted on the server.
	var serversideSortAscending;
	var tableCurrentlySortedBy;
	var serversideSortFileName;				// Name of file on server to send request to when table data should be sorted
	var serversideSortNumberOfRows;			// Number of rows to receive from the server
	var serversideSortCurrentStartIndex;	// Index of first row in the dataset, i.e. if you move to next page, this value will be incremented
	var serversideSortExtraSearchCriterias;	// Extra param to send to the server, example: &firstname=Alf&lastname=Kalleland
	var pageHandler;						// Object of class DHTMLSuite.tableWidgetPageHandler
	var rowClickCallBackFunction;			// Row click call back function.
	var objectIndex;						// Index of this object in the DHTMLSuite.variableStorage.arrayOfDhtmlSuiteObjects array
	
	this.serversideSort = false;			// Default value for serversideSort(items are sorted in the client)
	this.serversideSortAscending = true;	// Current sort ( ascending or descending)
	this.tableCurrentlySortedBy = false;
	this.serversideSortFileName = false;
	this.serversideSortCurrentStartIndex=0;
	this.serversideSortExtraSearchCriterias = '';
	this.rowClickCallBackFunction = false;
	this.setRowDblClickCallBackFunction = false;
	try{
		if(!standardObjectsCreated)DHTMLSuite.createStandardObjects();	// This line starts all the init methods
	}catch(e){
		alert('You need to include the dhtmlSuite-common.js file');
	}

	this.objectIndex = DHTMLSuite.variableStorage.arrayOfDhtmlSuiteObjects.length;
	DHTMLSuite.variableStorage.arrayOfDhtmlSuiteObjects[this.objectIndex] = this;
}

DHTMLSuite.tableWidget.prototype = {	
	/**
	* Public method used to initialize the table widget script. First use the set methods to configure the script, then
	* call the init method.
	**/
	// {{{ init()
    /**
     *
     * 	Initializes the table widget object
	 *
     * 
     * @public
     */    	
	init : function()
	{		
		this.tableWidget_okToSort = true;
		this.activeColumn = false;
		if(!this.layoutCSS)this.layoutCSS = 'table-widget.css';
		DHTMLSuite.commonObj.loadCSS(this.layoutCSS);
		this.__initTableWidget();
	}
	// }}}	
	,
	// {{{ setLayoutCss()
    /**
     *
     *  This function updates name of CSS file. This method should be called before init().
     *
     * @param String newCssFile = (File name of CSS file, not path)
     * 
     * @public
     */	
	setLayoutCss : function(newCssFile)
	{
		this.layoutCSS = newCSSFile;			
	}
	// }}}	
	,	
	// 	// {{{ setRowClickCallBackFunction()
    /**
     *
     *  This method specifies call back function to be called when a row is clicked. A reference to that row is sent as argument to this function
     *
     * @param String rowClickCallBackFunction = (Row click - call back function)
     * 
     * @public
     */	
	setRowClickCallBack : function(rowClickCallBackFunction)
	{
		this.rowClickCallBackFunction = rowClickCallBackFunction;			
	}
	// }}}	
	,
	// 	// {{{ setRowDblClickCallBackFunction()
    /**
     *
     *  This method specifies call back function to be called when a row is double clicked. A reference to that row is sent as argument to this function
     *
     * @param String setRowDblClickCallBackFunction = (Row click - call back function)
     * 
     * @public 
     */	
	setRowDblClickCallBack : function(setRowDblClickCallBackFunction)
	{
		this.setRowDblClickCallBackFunction = setRowDblClickCallBackFunction;			
	}
	// }}}	
	,	
	// {{{ setServerSideSort()
    /**
     *
     *  This method is used to specify if you want to your tables to be sorted on the server or not.
     *
     * @param Boolean serversideSort = Sort items on the server? (true = yes, false = no). 
     * 
     * @public
     */	
	setServerSideSort : function(serversideSort)
	{
		this.serversideSort = serversideSort;			
	}
	// }}}	
	,	
	// {{{ setServersideSearchCriterias()
    /**
     *
     *  This method is used to add extra params to the search url sent to the server.
     *
     * @param String serversideSortExtraSearchCriterias = String added to the url, example: "&firstname=John&lastname=Doe". This can be used in the sql query on the server.
     * 
     * @public
     */	
	setServersideSearchCriterias : function(serversideSortExtraSearchCriterias)
	{
		this.serversideSortExtraSearchCriterias = serversideSortExtraSearchCriterias;			
	}
	// }}}	
	,	
	// {{{ getServersideSortNumberOfRows()
    /**
     *
     *  Return numer of rows per page.
     *
     * @return Integer serversideSort = Number of rows
     * 
     * @public
     */	
	getServersideSortNumberOfRows : function(serversideSort)
	{
		return this.serversideSortNumberOfRows;		
	}
	// }}}	
	,		
	// {{{ setServersideSortNumberOfRows()
    /**
     *
     *  Specify how many records to receive from the server ( server side sort )
     *
     * @param Integer serversideSortNumberOfRows = Number of rows
     * 
     * @public
     */	
	setServersideSortNumberOfRows : function(serversideSortNumberOfRows)
	{
		this.serversideSortNumberOfRows = serversideSortNumberOfRows;			
	}
	// }}}	
	,	
	// {{{ setServersideSortFileName()
    /**
     *
     *  This method is used to specify which file to send the ajax request to when data should be sorted. (i.e. sort items on server instead of client).
     *
     * @param String serversideSortFileName = Path to file on server. This file will receive the request, parse it and send back new table data.
     * 
     * @public
     */	
	setServersideSortFileName : function(serversideSortFileName)
	{
		this.serversideSortFileName = serversideSortFileName;			
	}
	// }}}	
	,
	/* Start public methods */

	// {{{ setNoCssLayout()
    /**
     *
     *  No CSS layout
     *
     * 
     * @public
     */	
    setNoCssLayout : function()
	{
		this.noCssLayout = true;		
	}	
	// }}}	
	,	
	// {{{ sortTableByColumn()
    /**
     *
     *  This method sorts a table by a column
     *	You can call this method after the call to init if you want to sort the table by a column when the table is beeing displayed.
     *
     * @param Int columnIndex = Column to sort by (0 = first column)
     * @param String howToSort How to sort the table ("ascending" or "descending"
     * 
     * @public
     */	
	sortTableByColumn : function(columnIndex,howToSort)
	{
		if(!howToSort)howToSort = 'ascending';
		var tableObj = document.getElementById(this.idOfTable);
		var firstRow = tableObj.rows[0];
		var tds = firstRow.cells;
		if(tds[columnIndex] && this.columnSortArray[columnIndex]){
			this.__sortTable(tds[columnIndex],howToSort);
		}	
	}	
	// }}}	
	,		
	// {{{ setTableId()
    /**
     *
     *  Set id of table, i.e. the id of the <table> tag you want to apply the table widget to
     *
     * @param String idOfTable = Id of table
     * 
     * @public
     */	
	setTableId : function(idOfTable)
	{
		this.idOfTable = idOfTable;	
		try{
			this.tableObj = document.getElementById(idOfTable);
		}catch(e){
			
		}	
	}
	// }}}	
	,
	
	
	// {{{ setTableWidth()
    /**
	 *
     *  Set width of table
     *
     * @param Mixed width = (string if percentage width, integer if numeric/pixel width)
     * 
     * @public
     */	
	setTableWidth : function(width)
	{
		this.widthOfTable = width;			
	}
	// }}}	
	,	
	// {{{ setTableHeight()
    /**
	 *
     *  Set height of table
     *
     * @param Mixed height = (string if percentage height, integer if numeric/pixel height)
     * 
     * @public
     */	
	setTableHeight : function(height)
	{
		this.heightOfTable = height;
	}
	// }}}	
	,	
	// {{{ setColumnSort()
    /**
     *
     *  How to sort the table
     *
     * @param Array columnSortArray = How to sort the columns in the table(An array of the items 'N','S' or false)
     * 
     * @public
     */		
	setColumnSort : function(columnSortArray)
	{
		this.columnSortArray = columnSortArray;	
	}
	// }}}	
	,	
	
	// {{{ addNewRow()
    /**
     *  Adds a new row to the table dynamically
     *
     * @param Array cellContent = Array of strings - cell content
     * 
     * @public
     */		
	addNewRow : function(cellContent)
	{
		var tableObj = document.getElementById(this.idOfTable);
		var tbody = tableObj.getElementsByTagName('TBODY')[0];
		
		var row = tbody.insertRow(-1);
		for(var no=0;no<cellContent.length;no++){
			var cell = row.insertCell(-1);
			cell.innerHTML = cellContent[no];
		}
		this.__parseDataRows(tableObj);
		
	}
	// }}}	
	,
	
	
	// {{{ addNewColumn()
    /**
     *  Adds a new row to the table dynamically
     *
     * @param Array columnContent = Array of strings - content of new cells.
     * @param String headerText = Text - column header
     * @param mixed sortMethod = How to sort the new column('N','S' or false)
     * 
     * @public
     */		
	addNewColumn : function(columnContent,headerText,sortMethod)
	{
		this.columnSortArray[this.columnSortArray.length] = sortMethod;
		var tableObj = document.getElementById(this.idOfTable);	// Reference to the <table>
		var tbody = tableObj.getElementsByTagName('TBODY')[0];	// Reference to the <tbody>		
		var thead = tableObj.getElementsByTagName('THEAD')[0];	// Reference to the <tbody>		
		
		var bodyRows = tbody.rows;	// Reference to all the <tr> inside the <tbody> tag
		var headerRows = thead.rows;	// Reference to all <tr> inside <thead>
		
		cellIndexSubtract = 1;	// Firefox have a small cell at the right of each row which means that the new column should not be the last one, but second to last.
		if(DHTMLSuite.clientInfoObj.isMSIE) cellIndexSubtract = 0;	// Browser does not have this cell at the right
		// Add new header cell		
		var headerCell = headerRows[0].insertCell(headerRows[0].cells.length-cellIndexSubtract);
		if(!this.noCssLayout)headerCell.className = 'DHTMLSuite_tableWidget_headerCell';
		headerCell.onselectstart = DHTMLSuite.commonObj.cancelEvent;
		DHTMLSuite.commonObj.__addEventElement(headerCell);
		headerCell.innerHTML = headerText;
		if(sortMethod){
			this.__parseHeaderCell(headerCell);			
		}else{
			headerCell.style.cursor = 'default';	
		}
		
		// Setting width of header cells. The last cell shouldn't have any right border
		headerRows[0].cells[headerRows[0].cells.length-1].style.borderRightWidth = '0px';
		headerRows[0].cells[headerRows[0].cells.length-2].style.borderRightWidth = '1px';
		
		// Add rows to the table
		
		for(var no=0;no<columnContent.length;no++){
			var dataCell = bodyRows[no].insertCell(bodyRows[no].cells.length-cellIndexSubtract);
			dataCell.innerHTML = columnContent[no];			
		}
		
		this.__parseDataRows(tableObj);
					
	}
	// }}}	
	,
	// {{{ setPageHandler()
    /**
     *  Specify a reference to a page handler for this widget (in case of server side sort)
     *
     * @param tableWidgetPageHandler ref = Page handler reference
     * 
     * @public
     */		
	setPageHandler : function(ref)
	{
		this.pageHandler = ref;
	}
	,
	
	/* START PRIVATE METHODS */
	
	
	// {{{ __handleCallBackFromEvent()
    /**
     *  Handle call back events for the table widget ( this method is used to find the element triggering a callback function)
     *
     * @param Event e = Event object - we will use this to find the element triggering the vent
     * @param String whichCallBackAction = What kind of callback
     * 
     * @private
     */	
	__handleCallBackFromEvent : function(e,whichCallBackAction)
	{
		if(document.all)e = event;
		var src = DHTMLSuite.commonObj.getSrcElement(e);
		
		if((whichCallBackAction=='rowClick' || whichCallBackAction=='rowDblClick') && src.tagName.toLowerCase()!='tr'){
			while(src.tagName.toLowerCase()!='tr')src = src.parentNode;
		}
		
		this.__createCallBackJavascriptString(whichCallBackAction,src);
		
	}
	// }}}
	,
	// {{{ __createCallBackJavascriptString()
    /**
     *  Handle call back events for the table widget
     *
     * @param String whichCallBackAction = Which call back, example "rowClick"
     * @param Object el = Reference to the element triggering the event.
     * 
     * @private
     */		
	__createCallBackJavascriptString : function(whichCallBackAction,el)
	{
		var callbackString = "";
		switch(whichCallBackAction){
			case "rowClick":
				if(!this.rowClickCallBackFunction)return;
				callbackString = this.rowClickCallBackFunction + '(el)';
				break;	
			case "rowDblClick":
				if(!this.setRowDblClickCallBackFunction)return;
				callbackString = this.setRowDblClickCallBackFunction + '(el)';
				break;
		}	
		
		this.__executeCallBack(callbackString,el);
		
	}
	,
	// {{{ __executeCallBack()
    /**
     *  Execute a javascript call back string
     *
     * @param String callbackString = Javascript code
     * @param Object el = Reference to the element triggering the event.
     * 
     * @private
     */		
	__executeCallBack : function(callbackString,el)
	{
		if(!callbackString)return;
		try{
			eval(callbackString);
		}catch(e){
			
		}
		
	}	
	,
	// {{{ __parseHeaderCell()
    /**
     *  Parses a header cell, i.e. add mouse events, and a arrow image to it.
     *
     * @param Object inputCell = Reference to <TD>
     * 
     * @private
     */	
	__parseHeaderCell : function(inputCell)
	{
		if(!this.noCssLayout){
			inputCell.onmouseover = this.__highlightTableHeader;
			inputCell.onmouseout =  this.__removeHighlightEffectFromTableHeader;
			inputCell.onmousedown = this.__mousedownOnTableHeader;		
			inputCell.onmouseup = this.__highlightTableHeader;	
			
		}else{
			inputCell.style.cursor = 'pointer';	// No CSS layout -> just set cursor to pointer/hand.
		}
		
		var refToThis = this;	// It doesn't work with "this" on the line below, so we create a variable refering to "this".	
		inputCell.onclick = function(){ refToThis.__sortTable(this); };	
		DHTMLSuite.commonObj.__addEventElement(inputCell);

		var img = document.createElement('IMG');
		img.src = DHTMLSuite.configObj.imagePath + 'arrow_up.gif';
		inputCell.appendChild(img);	
		img.style.visibility = 'hidden';
	}
	// }}}	
	,
	
	// {{{ __parseDataRows()
    /**
     *  Parses rows in a table, i.e. add events and align cells.
     *
     * @param Object parentObj = Reference to <table>
     * 
     * @private
     */	
	__parseDataRows : function(parentObj)
	{
		var ind = this.objectIndex;
		// Loop through rows and assign mouseover and mouse out events + right align numeric cells.
		for(var no=1;no<parentObj.rows.length;no++){
			if(!this.noCssLayout){
				parentObj.rows[no].onmouseover = this.__highlightTableRow;
				parentObj.rows[no].onmouseout = this.__removeHighlightEffectFromTableRow;
			}
			parentObj.rows[no].onclick = function(e){ DHTMLSuite.variableStorage.arrayOfDhtmlSuiteObjects[ind].__handleCallBackFromEvent(e,'rowClick'); };
			parentObj.rows[no].ondblclick = function(e){ DHTMLSuite.variableStorage.arrayOfDhtmlSuiteObjects[ind].__handleCallBackFromEvent(e,'rowDblClick'); };
			DHTMLSuite.commonObj.__addEventElement(parentObj.rows[no]);
			
			for(var no2=0;no2<this.columnSortArray.length;no2++){	/* Right align numeric cells */
				try{
					if(this.columnSortArray[no2] && this.columnSortArray[no2]=='N')parentObj.rows[no].cells[no2].style.textAlign='right';
				}catch(e){
					alert('Error in __parseDataRows method - row: ' + no + ', column : ' + no2);
				}
			}
		}	
		// Right align header cells for numeric data
		for(var no2=0;no2<this.columnSortArray.length;no2++){	/* Right align numeric cells */
			if(this.columnSortArray[no2] && this.columnSortArray[no2]=='N')parentObj.rows[0].cells[no2].style.textAlign='right';
		}	
	}
	// }}}		
	,
	// {{{ __initTableWidget()
    /**
     *  Initializes the table widget script. This method formats the table and add events to the header cells.
     *
     * 
     * @private
     */	
	__initTableWidget : function()
	{
		
		if(!this.columnSortArray)this.columnSortArray = new Array();
		this.widthOfTable = this.widthOfTable + '';
		this.heightOfTable = this.heightOfTable + '';
		var obj = document.getElementById(this.idOfTable);
		obj.parentNode.className = 'DHTMLSuite_widget_tableDiv';
		
		
		
		if(navigator.userAgent.toLowerCase().indexOf('safari')==-1 && !this.noCssLayout){
			if(!DHTMLSuite.clientInfoObj.isMSIE)
				obj.parentNode.style.overflow='hidden';
			else {
				obj.parentNode.style.overflowX = 'hidden';
				obj.parentNode.style.overflowY = 'scroll';
			}
		}
		
		
		if(!this.noCssLayout){
			if(this.widthOfTable.indexOf('%')>=0){			
				obj.style.width = '100%';
				obj.parentNode.style.width = this.widthOfTable;			
			}else{
				obj.style.width = this.widthOfTable + 'px';
				obj.parentNode.style.width = this.widthOfTable + 'px';
			}		
			if(this.heightOfTable.indexOf('%')>=0){
				obj.parentNode.style.height = this.heightOfTable;				
			}else{
				obj.parentNode.style.height = this.heightOfTable + 'px';
			}
		}
		if(!DHTMLSuite.clientInfoObj.isMSIE){
			this.__addEndCol(obj);
		}else{
			obj.style.cssText = 'width:expression(this.parentNode.clientWidth)';
		}	
		
		obj.cellSpacing = 0;
		obj.cellPadding = 0;
		if(!this.noCssLayout)obj.className='DHTMLSuite_tableWidget';
		var tHead = obj.getElementsByTagName('THEAD')[0];
		var cells = tHead.getElementsByTagName('TD');
		
		var tBody = obj.getElementsByTagName('TBODY')[0];
		tBody.className = 'DHTMLSuite_scrollingContent';
		
		/* Add the last "cosmetic" cell in ie so that the scrollbar gets it's own column */
		if(DHTMLSuite.clientInfoObj.isMSIE && 1==2){	/* DEPRECATED */
			lastCell = tHead.rows[0].insertCell(-1);
			lastCell.innerHTML = '&nbsp;&nbsp;&nbsp;';	
			lastCell.className='DHTMLSuite_tableWidget_MSIESPACER';

		}
				
		for(var no=0;no<cells.length;no++){
			if(!this.noCssLayout)cells[no].className = 'DHTMLSuite_tableWidget_headerCell';
			cells[no].onselectstart = DHTMLSuite.commonObj.cancelEvent;
			DHTMLSuite.commonObj.__addEventElement(cells[no]);
			if(no==cells.length-1 && !this.noCssLayout){
				cells[no].style.borderRightWidth = '0px';	
			}
			if(this.columnSortArray[no]){
				this.__parseHeaderCell(cells[no]);			
			}else{
				cells[no].style.cursor = 'default';	
			}			
		}
		
		if(!this.noCssLayout){
			var tBody = obj.getElementsByTagName('TBODY')[0];
			if(document.all && navigator.userAgent.indexOf('Opera')<0){
				tBody.className='DHTMLSuite_scrollingContent';
				tBody.style.display='block';			
			}else{
				if(!this.noCssLayout)tBody.className='DHTMLSuite_scrollingContent';
				tBody.style.height = (obj.parentNode.clientHeight-tHead.offsetHeight) + 'px';
				if(navigator.userAgent.indexOf('Opera')>=0){
					obj.parentNode.style.overflow = 'auto';
				}
			}
		}		
		this.__parseDataRows(obj);
		var ind = this.objectIndex;				
	}	
	// }}}	
	,
	
	// {{{ __addEndCol()
    /**
     *  Adds a small empty cell at the right of the header row. This is done in order to make the table look pretty when the scrollbar appears.
     *
     * @param Object obj = Reference to <table>
     * 
     * @private
     */	
	__addEndCol : function(obj)
	{	
		var rows = obj.getElementsByTagName('TR');
		for(var no=0;no<rows.length;no++){
			var cell = rows[no].insertCell(rows[no].cells.length);
			cell.innerHTML = '<img src="' + DHTMLSuite.configObj.imagePath + 'transparent.gif" width="10" style="visibility:hidden">';
			cell.style.width = '13px';
			cell.width = '13';
			cell.style.overflow='hidden';
		}	
		
	}	
	// }}}
	,
	
	// {{{ __highlightTableHeader()
    /**
     *  Mouse over event: Highlights header cell on mouse over, i.e. applies an orange line at the top.
     *
     * 
     * @private
     */	
	__highlightTableHeader : function()
	{
		// Here, "this" is a reference to the HTML tag triggering this event and not the table widget object
		this.className='DHTMLSuite_tableWigdet_headerCellOver';
		if(document.all){	// I.E fix for "jumping" headings
			var divObj = this.parentNode.parentNode.parentNode.parentNode;
			this.parentNode.style.top = divObj.scrollTop + 'px';	
		}		
	}
	// }}}	
	,
	
	// {{{ __removeHighlightEffectFromTableHeader()
    /**
     *  Mouse out event: Remove the orange line at the top of header cells when the mouse moves away from the cell.
     *
     * 
     * @private
     */	
	__removeHighlightEffectFromTableHeader : function()
	{
		// Here, "this" is a reference to the HTML tag triggering this event and not the table widget object
		this.className='DHTMLSuite_tableWidget_headerCell';		
	}	
	// }}}
	,
	
	// {{{ __mousedownOnTableHeader()
    /**
     *  Mouse down event header cells. It changes the color of the header from light gray to dark gray.
     * 
     * @private
     */	
	__mousedownOnTableHeader : function()
	{
		// Here, "this" is a reference to the HTML tag triggering this event and not the table widget object
		this.className='DHTMLSuite_tableWigdet_headerCellDown';
		if(document.all){	// I.E fix for "jumping" headings
			var divObj = this.parentNode.parentNode.parentNode.parentNode;
			this.parentNode.style.top = divObj.scrollTop + 'px';
		}		
	}
	// }}}
	,
	// {{{ __sortNumeric()
    /**
     *  Sort the table numerically
	 *	ps! If you know that your tables always contains valid numbers(i.e. digits or decimal numbers like 7 and 7.5), 
	 * 	then you can remove everything except return a/1 - b/1; from this function. By removing these lines, the sort
	 *	process be faster.     
     *
     * @param String a = first number to compare
     * @param String b = second number to compare
     * 
     * @private
     */	
	__sortNumeric : function(a,b){
		// changing commas(,) to periods(.)
		a = a.replace(/,/,'.');
		b = b.replace(/,/,'.');
		// Remove non digit characters - example changing "DHTML12.5" to "12.5"
		a = a.replace(/[^\d\.\/]/g,'');
		b = b.replace(/[^\d\.\/]/g,'');
		// Dealing with fractions(example: changing 4/5 to 0.8)
		if(a.indexOf('/')>=0)a = eval(a);
		if(b.indexOf('/')>=0)b = eval(b);
		return a/1 - b/1;
	}	
	// }}}
	,
	
	// {{{ __sortString()
    /**
     *  Sort the table alphabetically
     *
     * @param String a = first number to compare
     * @param String b = second number to compare
     * 
     * @private
     */	
	__sortString : function(a, b) {	
	  if ( a.toUpperCase() < b.toUpperCase() ) return -1;
	  if ( a.toUpperCase() > b.toUpperCase() ) return 1;
	  return 0;
	}
	// }}}
	,
	// {{{ __parseDataContentFromServer()
    /**
     * This method parses data content from server and calls the __fillDataRow method with parsed data as argument.
     *
     * 
     * @private
     */		
	__parseDataContentFromServer : function(ajaxIndex)
	{
		var content = DHTMLSuite.variableStorage.ajaxObjects[ajaxIndex].response;
		if(content.indexOf('|||')==-1 && content.indexOf('###')==-1){
			alert('Error in data from server\n'+content);
			return;
		}
		
		this.__clearDataRows();	// Clear existing data
		var rows = content.split('|||');	// Create an array of each row
		for(var no=0;no<rows.length;no++){
			var items = rows[no].split('###');
			if(items.length>1)this.__fillDataRow(items);
			
		}	
		this.__parseDataRows(this.tableObj);
	}
	
	// }}}
	,
	// {{{ __clearDataRows()
    /**
     * This method clear all data from the table(except header cells).
     *
     * 
     * @private
     */		
	__clearDataRows : function()
	{
		if(!this.tableObj)this.tableObj = document.getElementById(this.idOfTable);
		while(this.tableObj.rows.length>1){
			this.tableObj.rows[this.tableObj.rows.length-1].parentNode.removeChild(this.tableObj.rows[this.tableObj.rows.length-1]);	
		}
	}
	
	// }}
	,
	// {{{ __fillDataRow()
    /**
     * Adds a new row of data to the table.
     *
     * @param Array data = Array of data
     * 
     * @private
     */		
	__fillDataRow : function(data)
	{
		if(!this.tableObj)this.tableObj = document.getElementById(this.idOfTable);
		var tbody = this.tableObj.getElementsByTagName('TBODY')[0];
		var row = tbody.insertRow(-1);
		for(var no=0;no<data.length;no++){
			var cell = row.insertCell(no);
			cell.innerHTML = data[no];
		}
		
	}
	// }}}
	,
	// {{{ updateTableHeader()
    /**
     * Updates the header of the table,i.e. shows the correct arrow. This is a method you call if you're sorting the table on the server
     *
     *
     * @param Integer columnIndex = Index of column the table is currently sorted by
     * @param String direction = How the table is sorted(ascending or descending)
     * 
     * @public
     */		
	updateTableHeader : function(columnIndex,direction)
	{
		var tableObj = document.getElementById(this.idOfTable);
		var firstRow = tableObj.rows[0];
		var tds = firstRow.cells;
		var tdObj = tds[columnIndex];
		tdObj.setAttribute('direction',direction);
		tdObj.direction = direction;
		var sortBy = tdObj.getAttribute('sortBy');
		if(!sortBy)sortBy = tdObj.sortBy;
		this.tableCurrentlySortedBy = sortBy;
		this.__updateSortArrow(tdObj,direction);		
	}
	// }}}
	,
	// {{{ reloadDataFromServer()
    /**
     * Simply reload data from server. 
     *
     * 
     * @public
     */			
	reloadDataFromServer : function()
	{
		this.__getItemsFromServer();
		if(this.pageHandler)this.pageHandler.__resetActivePageNumber();			
	}
	// }}}
	,
	// {{{ resetServersideSortCurrentStartIndex()
    /**
     * Reset current server side sort start index
     * It may be useful to call this method and then the reloadDataFromServer() 
     * method in case you want to reload data from the server starting with the first row in the record set.
     *
     * 
     * @public
     */			
	resetServersideSortCurrentStartIndex : function()
	{
		this.serversideSortCurrentStartIndex = 0;
		
	}
	// }}}
	,	
	// {{{ __updateSortArrow()
    /**
     * Sort table - This method is called when someone clicks on the header of one of the sortable columns
     *
     * @param Object obj = reference to header cell
     * @param String direction = How the table is sorted(ascending or descending)
     * 
     * @private
     */		
	__updateSortArrow : function(obj,direction)
	{
		var images = obj.getElementsByTagName('IMG');	// Array of the images inside the clicked header cell(i.e. arrow up and down)
		if(direction=='descending'){	// Setting visibility of arrow image based on sort(ascending or descending)
			images[0].src = images[0].src.replace('arrow_up','arrow_down');
			images[0].style.visibility='visible';
		}else{
			images[0].src = images[0].src.replace('arrow_down','arrow_up');
			images[0].style.visibility='visible';
		}		
		if(this.activeColumn && this.activeColumn!=obj){
			var images = this.activeColumn.getElementsByTagName('IMG');
			images[0].style.visibility='hidden';
			this.activeColumn.removeAttribute('direction');			
		}		
		
		this.activeColumn = obj;	// Setting this.activeColumn to the cell trigger this method 

	}
	// }}}
	,
	// {{{ __getParsedCallbackString()
    /**
     * __getParsedCallbackString - return call back javascript to execute. 
     *
     * @param String functionName = Name of call back function
     * 
     * @private
     */		
	__getParsedCallbackString : function(functionName)
	{
		var objIndex = this.objectIndex;	
		if(!functionName)
			functionName='true';
		else
			functionName = functionName + '(DHTMLSuite.variableStorage.arrayOfDhtmlSuiteObjects[' + objIndex + '])';	
		return functionName;	
	}	
	// }}}
	,
	// {{{ __getItemsFromServer()
    /**
     * Send ajax request to the server in order to get new data.
     *
     * @param String callbackFunctionNavigation = Name of eventual call back function to execute when new content has been received.
     * 
     * @private
     */		
	__getItemsFromServer : function(callbackFunctionNavigation)
	{
		callbackFunctionNavigation = this.__getParsedCallbackString(callbackFunctionNavigation);			
		var objIndex = this.objectIndex;	
		var url = this.serversideSortFileName + '?sortBy=' + this.tableCurrentlySortedBy + '&numberOfRows=' + this.serversideSortNumberOfRows + '&sortAscending=' + this.serversideSortAscending + '&startIndex=' + this.serversideSortCurrentStartIndex + this.serversideSortExtraSearchCriterias;
		var index = DHTMLSuite.variableStorage.ajaxObjects.length;	
		try{
			DHTMLSuite.variableStorage.ajaxObjects[index] = new sack();
		}catch(e){	// Unable to create ajax object - send alert message and return from sort method.
			alert('Unable to create ajax object. Please make sure that the sack js file is included on your page(js/ajax.js)');	
			return;
		}
		DHTMLSuite.variableStorage.ajaxObjects[index].requestFile = url;	// Specifying which file to get
		DHTMLSuite.variableStorage.ajaxObjects[index].onCompletion = function(){ DHTMLSuite.variableStorage.arrayOfDhtmlSuiteObjects[objIndex].__parseDataContentFromServer(index);eval(callbackFunctionNavigation) };	// Specify function that will be executed after file has been found
		DHTMLSuite.variableStorage.ajaxObjects[index].runAJAX();		// Execute AJAX function	
	}	
	// }}}
	,	
	// {{{ __sortTable()
    /**
     * Sort table - This method is called when someone clicks on the header of one of the sortable columns
     *
     * @param Object obj = reference to header cell triggering the sortTable method
     * 
     * @private
     */	
	__sortTable : function(obj,howToSort)
	{
		// "this" is a reference to the table widget obj
		// "obj" is a reference to the header cell triggering the sortTable method.
		
		// Server side sort ?
		if(this.serversideSort){
			// tableCurrentlySortedBy
					

			if(!this.serversideSortFileName){	// Server side file name defined.
				alert('No server side file defined. Use the setServersideSortFileName to specify where to send the ajax request');
				return;
			}
			var sortBy = obj.getAttribute('sortBy');
			if(!sortBy)sortBy = obj.sortBy;
			if(!sortBy){
				alert('Sort is not defined. Remember to set a sortBy attribute on the header <td> tags');
				return;
			}
			if(sortBy==this.tableCurrentlySortedBy)this.serversideSortAscending = !this.serversideSortAscending;else this.serversideSortAscending = true;
			if(howToSort)this.serversideSortAscending = (howToSort=='ascending'?true:false);
			this.tableCurrentlySortedBy = sortBy;
			this.serversideSortCurrentStartIndex =0;
			this.__getItemsFromServer();	
			
			if(this.pageHandler)this.pageHandler.__resetActivePageNumber();		
			this.__updateSortArrow(obj,this.serversideSortAscending?'ascending':'descending');
			
			return;	
		}
		
		
		
		if(!this.tableWidget_okToSort)return;
		this.tableWidget_okToSort = false;
		/* Getting index of current column */

		var indexThis = 0;
		
		var tmpObj = obj;
		while(tmpObj.previousSibling){
			tmpObj = tmpObj.previousSibling;
			if(tmpObj.tagName=='TD')indexThis++;		
		}		
		if(obj.getAttribute('direction') || obj.direction){	// Determine if we should sort ascending or descending
			direction = obj.getAttribute('direction');
			if(navigator.userAgent.indexOf('Opera')>=0)direction = obj.direction;
			if(direction=='ascending' || howToSort=='descending'){
				direction = 'descending';
				obj.setAttribute('direction','descending');
				obj.direction = 'descending';	
			}else{
				direction = 'ascending';
				obj.setAttribute('direction','ascending');		
				obj.direction = 'ascending';		
			}
		}else{ // First call to the sort method	
			var curDir = 'ascending';	// How to sort the table
			if(howToSort)curDir = howToSort; // Initial sort method sent as argument to this method, call it by this method.
			direction = curDir;
			obj.setAttribute('direction',curDir);
			obj.direction = curDir;
		}		

		this.__updateSortArrow(obj,direction);
				
		var tableObj = obj.parentNode.parentNode.parentNode;
		var tBody = tableObj.getElementsByTagName('TBODY')[0];
		
		var widgetIndex = tableObj.id.replace(/[^\d]/g,'');
		var sortMethod = this.columnSortArray[indexThis]; // N = numeric, S = String
		

		var cellArray = new Array();
		var cellObjArray = new Array();
		for(var no=1;no<tableObj.rows.length;no++){
			var content= tableObj.rows[no].cells[indexThis].innerHTML+'';
			cellArray.push(content);
			cellObjArray.push(tableObj.rows[no].cells[indexThis]);
		}
		// Calling sort methods
		if(sortMethod=='N'){
			cellArray = cellArray.sort(this.__sortNumeric);
		}else{
			cellArray = cellArray.sort(this.__sortString);
		}
		if(direction=='descending'){
			for(var no=cellArray.length;no>=0;no--){
				for(var no2=0;no2<cellObjArray.length;no2++){
					if(cellObjArray[no2].innerHTML == cellArray[no] && !cellObjArray[no2].getAttribute('allreadySorted')){
						cellObjArray[no2].setAttribute('allreadySorted','1');	
						tBody.appendChild(cellObjArray[no2].parentNode);				
					}				
				}			
			}
		}else{
			for(var no=0;no<cellArray.length;no++){
				for(var no2=0;no2<cellObjArray.length;no2++){
					if(cellObjArray[no2].innerHTML == cellArray[no] && !cellObjArray[no2].getAttribute('allreadySorted')){
						cellObjArray[no2].setAttribute('allreadySorted','1');	
						tBody.appendChild(cellObjArray[no2].parentNode);				
					}				
				}			
			}				
		}		
		for(var no2=0;no2<cellObjArray.length;no2++){
			cellObjArray[no2].removeAttribute('allreadySorted');		
		}	
		this.tableWidget_okToSort = true;		
	}	
	// }}}
	,
	
	// {{{ __highlightTableRow()
    /**
     *  Highlight data row on mouse over, i.e. applying css class tableWidget_dataRollOver
     *	To change the layout, look inside table-widget.css
     *
     * 
     * @private
     */	
	__highlightTableRow : function()
	{
		if(navigator.userAgent.indexOf('Opera')>=0)return;
		this.className='DHTMLSuite_tableWidget_dataRollOver';
		if(document.all){	// I.E fix for "jumping" headings
			var divObj = this.parentNode.parentNode.parentNode;
			var tHead = divObj.getElementsByTagName('TR')[0];
			tHead.style.top = divObj.scrollTop + 'px';			
		}	
	}
	// }}}
	,
	// {{{ __removeHighlightEffectFromTableRow()
    /**
     * Reset data row layout when mouse moves away from it.
     * 
     * @private
     */	
	__removeHighlightEffectFromTableRow : function()
	{
		if(navigator.userAgent.indexOf('Opera')>=0)return;
		this.className=null;
		if(document.all){	// I.E fix for "jumping" headings
			var divObj = this.parentNode.parentNode.parentNode;
			var tHead = divObj.getElementsByTagName('TR')[0];
			tHead.style.top = divObj.scrollTop + 'px';
		}			
	}	
	// }}}		
}