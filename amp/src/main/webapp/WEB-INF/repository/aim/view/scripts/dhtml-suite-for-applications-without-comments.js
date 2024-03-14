if(!String.trim)String.prototype.trim = function() { return this.replace(/^\s+|\s+$/, ''); };
var DHTMLSuite = new Object();
var standardObjectsCreated = false;	// The classes below will check this variable, if it is false, default help objects will be created
DHTMLSuite.eventElements = new Array();	// Array of elements that has been assigned to an event handler.
DHTMLSuite.createStandardObjects = function()
{
DHTMLSuite.clientInfoObj = new DHTMLSuite.clientInfo();	// Create browser info object
DHTMLSuite.clientInfoObj.init();
if(!DHTMLSuite.configObj){	// If this object isn't allready created, create it.
DHTMLSuite.configObj = new DHTMLSuite.config();	// Create configuration object.
DHTMLSuite.configObj.init();
}
DHTMLSuite.commonObj = new DHTMLSuite.common();	// Create configuration object.
DHTMLSuite.variableStorage = new DHTMLSuite.globalVariableStorage();;	// Create configuration object.
DHTMLSuite.commonObj.init();
DHTMLSuite.domQueryObj = new DHTMLSuite.domQuery();
window.onunload = function() { DHTMLSuite.commonObj.__clearMemoryGarbage(); }
standardObjectsCreated = true;
}
DHTMLSuite.config = function()
{
var imagePath;	// Path to images used by the classes.
var cssPath;	// Path to CSS files used by the DHTML suite.
var defaultCssPath;
var defaultImagePath;
}
DHTMLSuite.config.prototype = {
init : function()
{
this.imagePath = '../images_dhtmlsuite/';	// Path to images
this.cssPath = '../css_dhtmlsuite/';	// Path to images
this.defaultCssPath = this.cssPath;
this.defaultImagePath = this.imagePath;
}
,
setCssPath : function(newCssPath)
{
this.cssPath = newCssPath;
}
,
resetCssPath : function()
{
this.cssPath = this.defaultCssPath;
}
,
resetImagePath : function()
{
this.imagePath = this.defaultImagePath;
}
,
setImagePath : function(newImagePath)
{
this.imagePath = newImagePath;
}
}
DHTMLSuite.globalVariableStorage = function()
{
var menuBar_highlightedItems;	// Array of highlighted menu bar items
this.menuBar_highlightedItems = new Array();
var arrayOfDhtmlSuiteObjects;	// Array of objects of class menuItem.
this.arrayOfDhtmlSuiteObjects = new Array();
var ajaxObjects;
this.ajaxObjects = new Array();
}
DHTMLSuite.globalVariableStorage.prototype = {
}
DHTMLSuite.common = function()
{
var loadedCSSFiles;	// Array of loaded CSS files. Prevent same CSS file from being loaded twice.
var cssCacheStatus;	// Css cache status
var eventElements;
var isOkToSelect;	// Boolean variable indicating if it's ok to make text selections
this.okToSelect = true;
this.cssCacheStatus = true;	// Caching of css files = on(Default)
this.eventElements = new Array();
}
DHTMLSuite.common.prototype = {
init : function()
{
this.loadedCSSFiles = new Array();
}
,
loadCSS : function(cssFileName)
{
if(!this.loadedCSSFiles[cssFileName]){
this.loadedCSSFiles[cssFileName] = true;
var linkTag = document.createElement('LINK');
if(!this.cssCacheStatus){
if(cssFileName.indexOf('?')>=0)cssFileName = cssFileName + '&'; else cssFileName = cssFileName + '?';
cssFileName = cssFileName + 'rand='+ Math.random();	// To prevent caching
}
linkTag.href = DHTMLSuite.configObj.cssPath + cssFileName;
linkTag.rel = 'stylesheet';
linkTag.media = 'screen';
linkTag.type = 'text/css';
document.getElementsByTagName('HEAD')[0].appendChild(linkTag);
}
}
,
getTopPos : function(inputObj)
{
var returnValue = inputObj.offsetTop;
while((inputObj = inputObj.offsetParent) != null){
if(inputObj.tagName!='HTML'){
returnValue += (inputObj.offsetTop - inputObj.scrollTop);
if(document.all)returnValue+=inputObj.clientTop;
}
}
return returnValue;
}
,
__setOkToMakeTextSelections : function(okToSelect){
this.okToSelect = okToSelect;
}
,
__getOkToMakeTextSelections : function()
{
return this.okToSelect;
}
,
setCssCacheStatus : function(cssCacheStatus)
{
this.cssCacheStatus = cssCacheStatus;
}
,
getLeftPos : function(inputObj)
{
var returnValue = inputObj.offsetLeft;
while((inputObj = inputObj.offsetParent) != null){
if(inputObj.tagName!='HTML'){
returnValue += inputObj.offsetLeft;
if(document.all)returnValue+=inputObj.clientLeft;
}
}
return returnValue;
}
,
getCookie : function(name) {
var start = document.cookie.indexOf(name+"=");
var len = start+name.length+1;
if ((!start) && (name != document.cookie.substring(0,name.length))) return null;
if (start == -1) return null;
var end = document.cookie.indexOf(";",len);
if (end == -1) end = document.cookie.length;
return unescape(document.cookie.substring(len,end));
}
,
setCookie : function(name,value,expires,path,domain,secure) {
expires = expires * 60*60*24*1000;
var today = new Date();
var expires_date = new Date( today.getTime() + (expires) );
var cookieString = name + "=" +escape(value) +
( (expires) ? ";expires=" + expires_date.toGMTString() : "") +
( (path) ? ";path=" + path : "") +
( (domain) ? ";domain=" + domain : "") +
( (secure) ? ";secure" : "");
document.cookie = cookieString;
}
,
deleteCookie : function( name, path, domain )
{
if ( this.getCookie( name ) ) document.cookie = name + "=" +
( ( path ) ? ";path=" + path : "") +
( ( domain ) ? ";domain=" + domain : "" ) +
";expires=Thu, 01-Jan-1970 00:00:01 GMT";
}
,
cancelEvent : function()
{
return false;
}
,
addEvent : function(whichObject,eventType,functionName,suffix)
{
if(!suffix)suffix = '';
if(whichObject.attachEvent){
whichObject['e'+eventType+functionName+suffix] = functionName;
whichObject[eventType+functionName+suffix] = function(){whichObject['e'+eventType+functionName+suffix]( window.event );}
whichObject.attachEvent( 'on'+eventType, whichObject[eventType+functionName+suffix] );
} else
whichObject.addEventListener(eventType,functionName,false);
this.__addEventElement(whichObject);
}
,
removeEvent : function(whichObject,eventType,functionName)
{
if(whichObject.detachEvent){
whichObject.detachEvent('on'+eventType, whichObject[eventType+functionName]);
whichObject[eventType+functionName] = null;
} else
whichObject.removeEventListener(eventType,functionName,false);
}
,
__clearMemoryGarbage : function()
{
if(!DHTMLSuite.clientInfoObj.isMSIE)return;
for(var no in DHTMLSuite.variableStorage.arrayOfDhtmlSuiteObjects){
DHTMLSuite.variableStorage.arrayOfDhtmlSuiteObjects[no] = false;
}
for(var no=0;no<DHTMLSuite.eventElements.length;no++){
DHTMLSuite.eventElements[no].onclick = null;
DHTMLSuite.eventElements[no].onmousedown = null;
DHTMLSuite.eventElements[no].onmousemove = null;
DHTMLSuite.eventElements[no].onmouseout = null;
DHTMLSuite.eventElements[no].onmouseover = null;
DHTMLSuite.eventElements[no].onmouseup = null;
DHTMLSuite.eventElements[no].onfocus = null;
DHTMLSuite.eventElements[no].onblur = null;
DHTMLSuite.eventElements[no].onkeydown = null;
DHTMLSuite.eventElements[no].onkeypress = null;
DHTMLSuite.eventElements[no].onkeyup = null;
DHTMLSuite.eventElements[no].onselectstart = null;
DHTMLSuite.eventElements[no].ondragstart = null;
DHTMLSuite.eventElements[no].oncontextmenu = null;
DHTMLSuite.eventElements[no].onscroll = null;
}
window.onunload = null;
DHTMLSuite = null;
}
,
__addEventElement : function(el)
{
DHTMLSuite.eventElements[DHTMLSuite.eventElements.length] = el;
}
,
getSrcElement : function(e)
{
var el;
if (e.target) el = e.target;
else if (e.srcElement) el = e.srcElement;
if (el.nodeType == 3) // defeat Safari bug
el = el.parentNode;
return el;
}
,
isObjectClicked : function(obj,e)
{
var src = this.getSrcElement(e);
var string = src.tagName + '(' + src.className + ')';
if(src==obj)return true;
while(src.parentNode && src.tagName.toLowerCase()!='html'){
src = src.parentNode;
string = string + ',' + src.tagName + '(' + src.className + ')';
if(src==obj)return true;
}
return false;
}
,
getObjectByClassName : function(e,className)
{
var src = this.getSrcElement(e);
if(src.className==className)return src;
while(src && src.tagName.toLowerCase()!='html'){
src = src.parentNode;
if(src.className==className)return src;
}
return false;
}
,
getObjectByAttribute : function(e,attribute)
{
var src = this.getSrcElement(e);
var att = src.getAttribute(attribute);
if(!att)att = src[attribute];
if(att)return src;
while(src && src.tagName.toLowerCase()!='html'){
src = src.parentNode;
var att = src.getAttribute('attribute');
if(!att)att = src[attribute];
if(att)return src;
}
return false;
}
,
getUniqueId : function()
{
var no = Math.random() + '';
no = no.replace('.','');
var no2 = Math.random() + '';
no2 = no2.replace('.','');
return no + no2;
}
,
getAssociativeArrayFromString : function(propertyString)
{
if(!propertyString)return;
var retArray = new Array();
var items = propertyString.split(/,/g);
for(var no=0;no<items.length;no++){
var tokens = items[no].split(/:/);
retArray[tokens[0]] = tokens[1];
}
return retArray;
}
}
DHTMLSuite.clientInfo = function()
{
var browser;			// Complete user agent information
var isOpera;			// Is the browser "Opera"
var isMSIE;				// Is the browser "Internet Explorer"
var isOldMSIE;			// Is this browser and older version of Internet Explorer ( by older, we refer to version 6.0 or lower)
var isFirefox;			// Is the browser "Firefox"
var navigatorVersion;	// Browser version
}
DHTMLSuite.clientInfo.prototype = {
init : function()
{
this.browser = navigator.userAgent;
this.isOpera = (this.browser.toLowerCase().indexOf('opera')>=0)?true:false;
this.isFirefox = (this.browser.toLowerCase().indexOf('firefox')>=0)?true:false;
this.isMSIE = (this.browser.toLowerCase().indexOf('msie')>=0)?true:false;
this.isOldMSIE = (this.browser.toLowerCase().match(/msie [0-6]/gi))?true:false;
this.isSafari = (this.browser.toLowerCase().indexOf('safari')>=0)?true:false;
this.navigatorVersion = navigator.appVersion.replace(/.*?MSIE (\d\.\d).*/g,'$1')/1;
}
,
getBrowserWidth : function()
{
return document.documentElement.offsetWidth;
}
,
getBrowserHeight: function()
{
return document.documentElement.offsetHeight;
}
}
DHTMLSuite.domQuery = function()
{
document.getElementsByClassName = this.getElementsByClassName;
document.getElementsByAttribute = this.getElementsByAttribute;
}
DHTMLSuite.domQuery.prototype = {
getElementsByClassName : function(className,inputObj)
{
var returnArray = new Array();
if(inputObj)
var allElements = inputObj.getElementsByTagName('*');
else
var allElements = document.getElementsByTagName('*');
for(var no=0;no<allElements.length;no++){
if(allElements[no].className==className)returnArray[returnArray.length] = allElements[no];
}
return returnArray;
}
,
getElementsByAttribute : function(attribute,attributeValue,inputObj)
{
var returnArray = new Array();
if(inputObj)
var allElements = inputObj.getElementsByTagName('*');
else
var allElements = document.getElementsByTagName('*');
for(var no=0;no<allElements.length;no++){
var att = allElements[no].getAttribute(attribute);
if(!attributeValue){
if(att)returnArray[returnArray.length] = allElements[no];
}
else
if(att==attributeValue)returnArray[returnArray.length] = allElements[no];
}
return returnArray;
}
}
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
setTableRef : function(tableRef)
{
this.tableRef = tableRef;
this.tableRef.setPageHandler(this);
}
,
setTargetId : function(idOfHTMLElement)
{
if(!document.getElementById(idOfHTMLElement)){
alert('ERROR IN tableWidgetPageHandler.setTargetId:\nElement with id ' + idOfHTMLElement + ' does not exists');
return;
}
this.targetRef = document.getElementById(idOfHTMLElement);
}
,
setTxtPrevious : function(newText)
{
this.txtPrevious = newText;
}
,
setLinkPagePrefix : function(linkPagePrefix)
{
this.linkPagePrefix = linkPagePrefix;
}
,
setLinkPageSuffix : function(linkPageSuffix)
{
this.linkPageSuffix = linkPageSuffix;
}
,
setTxtNext : function(newText)
{
this.txtNext = newText;
}
,
setTxtResultOf : function(txtResultOf)
{
this.txtResultOf = txtResultOf;
}
,
setTxtResultTo : function(txtResultTo)
{
this.txtResultTo = txtResultTo;
}
,
setTxtResultPrefix : function(txtResultPrefix)
{
this.txtResultPrefix = txtResultPrefix;
}
,
setTxtFirstPage : function(txtFirst)
{
this.txtFirst = txtFirst;
}
,
setTxtLastPage : function(txtLast)
{
this.txtLast = txtLast;
}
,
setTotalNumberOfRows : function(totalNumberOfRows)
{
this.totalNumberOfRows = totalNumberOfRows;
}
,
setCallbackOnAfterNavigate : function(callbackOnAfterNavigate)
{
this.callbackOnAfterNavigate = callbackOnAfterNavigate;
}
,
setLayoutCss : function(layoutCSS)
{
this.layoutCSS = layoutCSS;
}
,
setMaximumNumberOfPageLinks : function(maximumNumberOfPageLinks)
{
this.maximumNumberOfPageLinks = maximumNumberOfPageLinks;
}
,
init : function()
{
this.rowsPerPage = this.tableRef.getServersideSortNumberOfRows();
DHTMLSuite.commonObj.loadCSS(this.layoutCSS);
this.__createMainDivElements();
this.setHTMLOfResultList();
this.__createPageLinks();
}
,
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
setHTMLOfResultList : function()
{
this.resultDivElement.innerHTML = '';
var html = this.txtResultPrefix + (((this.activePageNumber-1) * this.rowsPerPage) + 1) + ' ' + this.txtResultTo + ' ' + Math.min(this.totalNumberOfRows,(this.activePageNumber * this.rowsPerPage)) + ' ' + this.txtResultOf + ' ' + this.totalNumberOfRows;
this.resultDivElement.innerHTML = html;
}
,
__createPageLinks : function()
{
var ind = this.objectIndex;
this.pageListDivElement.innerHTML = '';	// Clearing the div element if it allready got content.
var numberOfPages = Math.ceil(this.totalNumberOfRows/this.rowsPerPage);
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
,
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
,
__resetActivePageNumber : function()
{
this.activePageNumber = 1;
this.setHTMLOfResultList();
this.__createPageLinks();
}
}
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
init : function()
{
this.tableWidget_okToSort = true;
this.activeColumn = false;
if(!this.layoutCSS)this.layoutCSS = 'table-widget.css';
DHTMLSuite.commonObj.loadCSS(this.layoutCSS);
this.__initTableWidget();
}
,
setLayoutCss : function(newCssFile)
{
this.layoutCSS = newCSSFile;
}
,
setRowClickCallBack : function(rowClickCallBackFunction)
{
this.rowClickCallBackFunction = rowClickCallBackFunction;
}
,
setRowDblClickCallBack : function(setRowDblClickCallBackFunction)
{
this.setRowDblClickCallBackFunction = setRowDblClickCallBackFunction;
}
,
setServerSideSort : function(serversideSort)
{
this.serversideSort = serversideSort;
}
,
setServersideSearchCriterias : function(serversideSortExtraSearchCriterias)
{
this.serversideSortExtraSearchCriterias = serversideSortExtraSearchCriterias;
}
,
getServersideSortNumberOfRows : function(serversideSort)
{
return this.serversideSortNumberOfRows;
}
,
setServersideSortNumberOfRows : function(serversideSortNumberOfRows)
{
this.serversideSortNumberOfRows = serversideSortNumberOfRows;
}
,
setServersideSortFileName : function(serversideSortFileName)
{
this.serversideSortFileName = serversideSortFileName;
}
,
setNoCssLayout : function()
{
this.noCssLayout = true;
}
,
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
,
setTableId : function(idOfTable)
{
this.idOfTable = idOfTable;
try{
this.tableObj = document.getElementById(idOfTable);
}catch(e){
}
}
,
setTableWidth : function(width)
{
this.widthOfTable = width;
}
,
setTableHeight : function(height)
{
this.heightOfTable = height;
}
,
setColumnSort : function(columnSortArray)
{
this.columnSortArray = columnSortArray;
}
,
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
,
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
headerRows[0].cells[headerRows[0].cells.length-1].style.borderRightWidth = '0px';
headerRows[0].cells[headerRows[0].cells.length-2].style.borderRightWidth = '1px';
for(var no=0;no<columnContent.length;no++){
var dataCell = bodyRows[no].insertCell(bodyRows[no].cells.length-cellIndexSubtract);
dataCell.innerHTML = columnContent[no];
}
this.__parseDataRows(tableObj);
}
,
setPageHandler : function(ref)
{
this.pageHandler = ref;
}
,
__handleCallBackFromEvent : function(e,whichCallBackAction)
{
if(document.all)e = event;
var src = DHTMLSuite.commonObj.getSrcElement(e);
if((whichCallBackAction=='rowClick' || whichCallBackAction=='rowDblClick') && src.tagName.toLowerCase()!='tr'){
while(src.tagName.toLowerCase()!='tr')src = src.parentNode;
}
this.__createCallBackJavascriptString(whichCallBackAction,src);
}
,
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
__executeCallBack : function(callbackString,el)
{
if(!callbackString)return;
try{
eval(callbackString);
}catch(e){
}
}
,
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
,
__parseDataRows : function(parentObj)
{
var ind = this.objectIndex;
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
for(var no2=0;no2<this.columnSortArray.length;no2++){	/* Right align numeric cells */
if(this.columnSortArray[no2] && this.columnSortArray[no2]=='N')parentObj.rows[0].cells[no2].style.textAlign='right';
}
}
,
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
,
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
,
__highlightTableHeader : function()
{
this.className='DHTMLSuite_tableWigdet_headerCellOver';
if(document.all){	// I.E fix for "jumping" headings
var divObj = this.parentNode.parentNode.parentNode.parentNode;
this.parentNode.style.top = divObj.scrollTop + 'px';
}
}
,
__removeHighlightEffectFromTableHeader : function()
{
this.className='DHTMLSuite_tableWidget_headerCell';
}
,
__mousedownOnTableHeader : function()
{
this.className='DHTMLSuite_tableWigdet_headerCellDown';
if(document.all){	// I.E fix for "jumping" headings
var divObj = this.parentNode.parentNode.parentNode.parentNode;
this.parentNode.style.top = divObj.scrollTop + 'px';
}
}
,
__sortNumeric : function(a,b){
a = a.replace(/,/,'.');
b = b.replace(/,/,'.');
a = a.replace(/[^\d\.\/]/g,'');
b = b.replace(/[^\d\.\/]/g,'');
if(a.indexOf('/')>=0)a = eval(a);
if(b.indexOf('/')>=0)b = eval(b);
return a/1 - b/1;
}
,
__sortString : function(a, b) {
if ( a.toUpperCase() < b.toUpperCase() ) return -1;
if ( a.toUpperCase() > b.toUpperCase() ) return 1;
return 0;
}
,
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
,
__clearDataRows : function()
{
if(!this.tableObj)this.tableObj = document.getElementById(this.idOfTable);
while(this.tableObj.rows.length>1){
this.tableObj.rows[this.tableObj.rows.length-1].parentNode.removeChild(this.tableObj.rows[this.tableObj.rows.length-1]);
}
}
,
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
,
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
,
reloadDataFromServer : function()
{
this.__getItemsFromServer();
if(this.pageHandler)this.pageHandler.__resetActivePageNumber();
}
,
resetServersideSortCurrentStartIndex : function()
{
this.serversideSortCurrentStartIndex = 0;
}
,
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
,
__getParsedCallbackString : function(functionName)
{
var objIndex = this.objectIndex;
if(!functionName)
functionName='true';
else
functionName = functionName + '(DHTMLSuite.variableStorage.arrayOfDhtmlSuiteObjects[' + objIndex + '])';
return functionName;
}
,
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
,
__sortTable : function(obj,howToSort)
{
if(this.serversideSort){
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
,
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
,
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
}
var DHTMLSuite_dragDropSimple_curZIndex = 100000;
var DHTMLSuite_dragDropSimple_curObjIndex=false;
DHTMLSuite.dragDropSimple = function(elementRef,elementId,initOffsetX,initOffsetY,cloneNode)
{
var divElement;
var dragTimer;	// -1 no drag, 0-4 = initializing , 5 = drag in process
var cloneNode;
this.cloneNode = true;
if(cloneNode===false || cloneNode)this.cloneNode=cloneNode;
var callbackOnAfterDrag;
var callbackOnBeforeDrag;
var mouse_x;
var mouse_y;
var positionSet;
var dragHandle;	// If a specific element is specified to be a drag handle.
this.positionSet = false;
this.dragHandle = new Array();
var initOffsetX;
var initOffsetY;
if(!initOffsetX)initOffsetX = 0;
if(!initOffsetY)initOffsetY = 0;
this.initOffsetX = initOffsetX;
this.initOffsetY = initOffsetY;
this.callbackOnAfterDrag = false;
this.callbackOnBeforeDrag = false;
this.dragStatus = -1;
try{
if(!standardObjectsCreated)DHTMLSuite.createStandardObjects();	// This line starts all the init methods
}catch(e){
alert('You need to include the dhtmlSuite-common.js file');
}
if(!elementRef && elementId)elementRef = document.getElementById(elementId);
this.divElement = elementRef;
var objectIndex;
this.objectIndex = DHTMLSuite.variableStorage.arrayOfDhtmlSuiteObjects.length;
DHTMLSuite.variableStorage.arrayOfDhtmlSuiteObjects[this.objectIndex] = this;
this.__init();
}
DHTMLSuite.dragDropSimple.prototype = {
__init : function()
{
var ind = this.objectIndex;
this.divElement.objectIndex = ind;
this.divElement.setAttribute('objectIndex',ind);
this.divElement.style.padding = '0px';
this.divElement.style.left = (DHTMLSuite.commonObj.getLeftPos(this.divElement) + this.initOffsetX) + 'px';
this.divElement.style.top = (DHTMLSuite.commonObj.getTopPos(this.divElement) + this.initOffsetY) + 'px';
this.divElement.style.position = 'absolute';
this.divElement.style.margin = '0px';
if(this.divElement.style.zIndex && this.divElement.style.zIndex/1>DHTMLSuite_dragDropSimple_curZIndex)DHTMLSuite_dragDropSimple_curZIndex=this.divElement.style.zIndex/1;
DHTMLSuite_dragDropSimple_curZIndex = DHTMLSuite_dragDropSimple_curZIndex/1 + 1;
this.divElement.style.zIndex = DHTMLSuite_dragDropSimple_curZIndex;
if(this.cloneNode){
var copy = this.divElement.cloneNode(true);
this.divElement.parentNode.insertBefore(copy,this.divElement);
copy.style.visibility = 'hidden';
document.body.appendChild(this.divElement);
}
this.divElement.onmousedown = this.__initDragProcess;
DHTMLSuite.commonObj.addEvent(document.documentElement,'mousemove',function(e){ DHTMLSuite.variableStorage.arrayOfDhtmlSuiteObjects[ind].__moveDragableElement(e); });
DHTMLSuite.commonObj.addEvent(document.documentElement,'mouseup',function(e){ DHTMLSuite.variableStorage.arrayOfDhtmlSuiteObjects[ind].__stopDragProcess(e); });
if(!document.documentElement.onselectstart)document.documentElement.onselectstart = function() { return DHTMLSuite.commonObj.__getOkToMakeTextSelections(); };
}
,
setCallbackOnAfterDrag : function(functionName)
{
this.callbackOnAfterDrag = functionName;
}
,
setCallbackOnBeforeDrag : function(functionName)
{
this.callbackOnBeforeDrag = functionName;
}
,
addDragHandle : function(dragHandle)
{
this.dragHandle[this.dragHandle.length] = dragHandle;
}
,
__initDragProcess : function(e)
{
if(document.all)e = event;
var ind = this.getAttribute('objectIndex');
if(!ind)ind=this.objectIndex;
DHTMLSuite_dragDropSimple_curObjIndex = ind;
var thisObject = DHTMLSuite.variableStorage.arrayOfDhtmlSuiteObjects[ind];
if(!DHTMLSuite.commonObj.isObjectClicked(thisObject.divElement,e))return;
if(thisObject.divElement.style.zIndex && thisObject.divElement.style.zIndex/1>DHTMLSuite_dragDropSimple_curZIndex)DHTMLSuite_dragDropSimple_curZIndex=thisObject.divElement.style.zIndex/1;
DHTMLSuite_dragDropSimple_curZIndex = DHTMLSuite_dragDropSimple_curZIndex/1 +1;
thisObject.divElement.style.zIndex = DHTMLSuite_dragDropSimple_curZIndex;
if(thisObject.callbackOnBeforeDrag){
thisObject.__handleCallback('beforeDrag');
}
if(thisObject.dragHandle.length>0){	// Drag handle specified?
var objectFound;
for(var no=0;no<thisObject.dragHandle.length;no++){
if(!objectFound)objectFound = DHTMLSuite.commonObj.isObjectClicked(thisObject.dragHandle[no],e);
}
if(!objectFound)return;
}
DHTMLSuite.commonObj.__setOkToMakeTextSelections(false);
thisObject.mouse_x = e.clientX;
thisObject.mouse_y = e.clientY;
thisObject.el_x = thisObject.divElement.style.left.replace('px','')/1;
thisObject.el_y = thisObject.divElement.style.top.replace('px','')/1;
thisObject.dragTimer = 0;
thisObject.__waitBeforeDragProcessStarts();
return false;
}
,
__waitBeforeDragProcessStarts : function()
{
var ind = this.objectIndex;
if(this.dragTimer>=0 && this.dragTimer<5){
this.dragTimer++;
setTimeout('DHTMLSuite.variableStorage.arrayOfDhtmlSuiteObjects[' + ind + '].__waitBeforeDragProcessStarts()',5);
}
}
,
__moveDragableElement : function(e)
{
if(DHTMLSuite_dragDropSimple_curObjIndex===false)return false;
var thisObj = DHTMLSuite.variableStorage.arrayOfDhtmlSuiteObjects[DHTMLSuite_dragDropSimple_curObjIndex];
if(thisObj.dragTimer==5){
thisObj.divElement.style.left = (e.clientX - thisObj.mouse_x + thisObj.el_x) + 'px';
thisObj.divElement.style.top = (e.clientY - thisObj.mouse_y + thisObj.el_y) + 'px';
}
return false;
}
,
__stopDragProcess : function()
{
if(DHTMLSuite_dragDropSimple_curObjIndex===false)return;
DHTMLSuite.commonObj.__setOkToMakeTextSelections(true);
var thisObj = DHTMLSuite.variableStorage.arrayOfDhtmlSuiteObjects[DHTMLSuite_dragDropSimple_curObjIndex];
if(thisObj.dragTimer==5){
thisObj.__handleCallback('afterDrag');
}
thisObj.dragTimer = -1;
}
,
__handleCallback : function(action)
{
var callbackString = '';
switch(action){
case "afterDrag":
callbackString = this.callbackOnAfterDrag;
break;
case "beforeDrag":
callbackString = this.callbackOnBeforeDrag;
break;
}
if(callbackString){
callbackString = callbackString + '()';
try{
eval(callbackString);
}catch(e){
alert('Could not execute callback function(' + callbackstring + ') after drag');
}
}
}
,
__setNewCurrentZIndex : function(zIndex)
{
if(zIndex > DHTMLSuite_dragDropSimple_curZIndex){
DHTMLSuite_dragDropSimple_curZIndex = zIndex/1+1;
}
}
}
DHTMLSuite.dragDrop = function()
{
var mouse_x;					// mouse x position when drag is started
var mouse_y;					// mouse y position when drag is started.
var el_x;						// x position of dragable element
var el_y;						// y position of dragable element
var dragDropTimer;				// Timer - short delay from mouse down to drag init.
var numericIdToBeDragged;		// numeric reference to element currently being dragged.
var dragObjCloneArray;			// Array of cloned dragable elements. every
var dragDropSourcesArray;		// Array of source elements, i.e. dragable elements.
var dragDropTargetArray;		// Array of target elements, i.e. elements where items could be dropped.
var currentZIndex;				// Current z index. incremented on each drag so that currently dragged element is always on top.
var okToStartDrag;				// Variable which is true or false. It would be false for 1/100 seconds after a drag has been started.
var moveBackBySliding;			// Variable indicating if objects should slide into place moved back to their location without any slide animation.
var dragX_allowed;				// Possible to drag this element along the x-axis?
var dragY_allowed;				// Possible to drag this element along the y-axis?
var currentEl_allowX;
var currentEl_allowY;
var drag_minX;
var drag_maxX;
var drag_minY;
var drag_maxY;
var dragInProgress;				// Variable which is true when drag is in progress, false otherwise
try{
if(!standardObjectsCreated)DHTMLSuite.createStandardObjects();	// This line starts all the init methods
}catch(e){
alert('You need to include the dhtmlSuite-common.js file');
}
this.dragX_allowed = true;
this.dragY_allowed = true;
this.currentZIndex = 21000;
this.dragDropTimer = -1;
this.dragObjCloneArray = new Array();
this.numericIdToBeDragged = false;
this.okToStartDrag = true;
this.moveBackBySliding = true;
this.dragInProgress = false;
var objectIndex;
this.objectIndex = DHTMLSuite.variableStorage.arrayOfDhtmlSuiteObjects.length;
DHTMLSuite.variableStorage.arrayOfDhtmlSuiteObjects[this.objectIndex] = this;
}
DHTMLSuite.dragDrop.prototype = {
init : function()
{
this.__initDragDropScript();
}
,
addSource : function(sourceId,slideBackAfterDrop,xAxis,yAxis,dragOnlyWithinElId)
{
if(!this.dragDropSourcesArray)this.dragDropSourcesArray = new Array();
if(!document.getElementById(sourceId)){
alert('The source element with id ' + sourceId + ' does not exists. Check your HTML code');
return;
}
if(xAxis!==false)xAxis = true;
if(yAxis!==false)yAxis = true;
var obj = document.getElementById(sourceId);
this.dragDropSourcesArray[this.dragDropSourcesArray.length]  = [obj,slideBackAfterDrop,xAxis,yAxis,dragOnlyWithinElId];
obj.setAttribute('dragableElement',this.dragDropSourcesArray.length-1);
obj.dragableElement = this.dragDropSourcesArray.length-1;
}
,
addTarget : function(targetId,functionToCallOnDrop)
{
if(!this.dragDropTargetArray)this.dragDropTargetArray = new Array();
if(!document.getElementById(targetId))alert('The target element with id ' + targetId + ' does not exists.  Check your HTML code');
var obj = document.getElementById(targetId);
this.dragDropTargetArray[this.dragDropTargetArray.length]  = [obj,functionToCallOnDrop];
}
,
setSlide : function(isSlidingAnimationEnabled)
{
this.moveBackBySliding = isSlidingAnimationEnabled;
}
,
__initDragDropScript : function()
{
var ind = this.objectIndex;
var refToThis = this;
var startIndex = Math.random() + '';
startIndex = startIndex.replace('.','')/1;
for(var no=0;no<this.dragDropSourcesArray.length;no++){
var el = this.dragDropSourcesArray[no][0].cloneNode(true);
var el2 = this.dragDropSourcesArray[no][0];
eval("el.onmousedown =function(e,index){ DHTMLSuite.variableStorage.arrayOfDhtmlSuiteObjects[" + ind + "].__initializeDragProcess(e," + no + "); }");
DHTMLSuite.commonObj.__addEventElement(el);
var tmpIndex = startIndex + no;
el.id = el2.id;	/* Override the value above */
el.style.position='absolute';
el.style.visibility='hidden';
el.style.display='none';
document.body.appendChild(el);
el.style.top = DHTMLSuite.commonObj.getTopPos(this.dragDropSourcesArray[no][0]) + 'px';
el.style.left = DHTMLSuite.commonObj.getLeftPos(this.dragDropSourcesArray[no][0]) + 'px';
eval("el2.onmousedown =function(e,index){ return DHTMLSuite.variableStorage.arrayOfDhtmlSuiteObjects[" + ind + "].__initializeDragProcess(e," + no + "); }");
DHTMLSuite.commonObj.__addEventElement(this.dragDropSourcesArray[no][0]);
this.dragObjCloneArray[no] = el;
}
eval("DHTMLSuite.commonObj.addEvent(document.documentElement,\"mousemove\",function(e){ DHTMLSuite.variableStorage.arrayOfDhtmlSuiteObjects[" + ind + "].__moveDragableElement(e); } )");
eval("DHTMLSuite.commonObj.addEvent(document.documentElement,\"mouseup\",function(e){ DHTMLSuite.variableStorage.arrayOfDhtmlSuiteObjects[" + ind + "].__stopDragProcess(e); } );");
if(!document.documentElement.onselectstart)document.documentElement.onselectstart = function() { return DHTMLSuite.commonObj.__getOkToMakeTextSelections(); };
document.documentElement.ondragstart = function() { return DHTMLSuite.commonObj.cancelEvent() };
DHTMLSuite.commonObj.__addEventElement(document.documentElement);
}
,
__initializeDragProcess : function(e,index)
{
var ind = this.objectIndex;
if(!this.okToStartDrag)return false;
setTimeout('DHTMLSuite.variableStorage.arrayOfDhtmlSuiteObjects[' + ind + '].okToStartDrag = true;',100);
if(document.all)e = event;
this.numericIdToBeDragged = index;
this.numericIdToBeDragged = this.numericIdToBeDragged + '';
this.dragDropTimer=0;
DHTMLSuite.commonObj.__setOkToMakeTextSelections(false);
this.mouse_x = e.clientX;
this.mouse_y = e.clientY;
this.currentZIndex = this.currentZIndex + 1;
this.dragObjCloneArray[this.numericIdToBeDragged].style.zIndex = this.currentZIndex;
this.currentEl_allowX = this.dragDropSourcesArray[this.numericIdToBeDragged][2];
this.currentEl_allowY = this.dragDropSourcesArray[this.numericIdToBeDragged][3];
var parentEl = this.dragDropSourcesArray[this.numericIdToBeDragged][4];
this.drag_minX = false;
this.drag_minY = false;
this.drag_maxX = false;
this.drag_maxY = false;
if(parentEl){
var obj = document.getElementById(parentEl);
if(obj){
this.drag_minX = DHTMLSuite.commonObj.getLeftPos(obj);
this.drag_minY = DHTMLSuite.commonObj.getTopPos(obj);
this.drag_maxX = this.drag_minX + obj.clientWidth;
this.drag_maxY = this.drag_minY + obj.clientHeight;
}
}
if(this.dragDropSourcesArray[this.numericIdToBeDragged][1]){
this.dragObjCloneArray[this.numericIdToBeDragged].style.top = DHTMLSuite.commonObj.getTopPos(this.dragDropSourcesArray[this.numericIdToBeDragged][0]) + 'px';
this.dragObjCloneArray[this.numericIdToBeDragged].style.left = DHTMLSuite.commonObj.getLeftPos(this.dragDropSourcesArray[this.numericIdToBeDragged][0]) + 'px';
}
this.el_x = this.dragObjCloneArray[this.numericIdToBeDragged].style.left.replace('px','')/1;
this.el_y = this.dragObjCloneArray[this.numericIdToBeDragged].style.top.replace('px','')/1;
this.__waitBeforeDragProcessStarts();
return false;
}
,
__waitBeforeDragProcessStarts : function()
{
var ind = this.objectIndex;
if(this.dragDropTimer>=0 && this.dragDropTimer<5){
this.dragDropTimer = this.dragDropTimer + 1;
setTimeout('DHTMLSuite.variableStorage.arrayOfDhtmlSuiteObjects[' + ind + '].__waitBeforeDragProcessStarts()',2);
return;
}
if(this.dragDropTimer>=5){
if(this.dragObjCloneArray[this.numericIdToBeDragged].style.display=='none'){
this.dragDropSourcesArray[this.numericIdToBeDragged][0].style.visibility = 'hidden';
this.dragObjCloneArray[this.numericIdToBeDragged].style.display = 'block';
this.dragObjCloneArray[this.numericIdToBeDragged].style.visibility = 'visible';
this.dragObjCloneArray[this.numericIdToBeDragged].style.top = DHTMLSuite.commonObj.getTopPos(this.dragDropSourcesArray[this.numericIdToBeDragged][0]) + 'px';
this.dragObjCloneArray[this.numericIdToBeDragged].style.left = DHTMLSuite.commonObj.getLeftPos(this.dragDropSourcesArray[this.numericIdToBeDragged][0]) + 'px';
}
}
}
,
__moveDragableElement : function(e)
{
var ind = this.objectIndex;
if(document.all)e = event;
if(this.dragDropTimer<5)return false;
if(this.dragInProgress)return false;
this.dragInProgress = true;
var dragObj = this.dragObjCloneArray[this.numericIdToBeDragged];
if(this.currentEl_allowX){
var leftPos = (e.clientX - this.mouse_x + this.el_x);
if(this.drag_maxX){
var tmpMaxX = this.drag_maxX - dragObj.offsetWidth;
if(leftPos > tmpMaxX)leftPos = tmpMaxX
if(leftPos < this.drag_minX)leftPos = this.drag_minX;
}
dragObj.style.left = leftPos + 'px';
}
if(this.currentEl_allowY){
var topPos = (e.clientY - this.mouse_y + this.el_y);
if(this.drag_maxY){
var tmpMaxY = this.drag_maxY - dragObj.offsetHeight;
if(topPos > tmpMaxY)topPos = tmpMaxY;
if(topPos < this.drag_minY)topPos = this.drag_minY;
}
dragObj.style.top = topPos + 'px';
}
this.dragInProgress = false;
return false;
}
,
__stopDragProcess : function(e)
{
if(this.dragDropTimer<5)return false;
if(document.all)e = event;
var dropDestination = DHTMLSuite.commonObj.getSrcElement(e);
var leftPosMouse = e.clientX + Math.max(document.body.scrollLeft,document.documentElement.scrollLeft);
var topPosMouse = e.clientY + Math.max(document.body.scrollTop,document.documentElement.scrollTop);
if(!this.dragDropTargetArray)this.dragDropTargetArray = new Array();
for(var no=0;no<this.dragDropTargetArray.length;no++){
var leftPosEl = DHTMLSuite.commonObj.getLeftPos(this.dragDropTargetArray[no][0]);
var topPosEl = DHTMLSuite.commonObj.getTopPos(this.dragDropTargetArray[no][0]);
var widthEl = this.dragDropTargetArray[no][0].offsetWidth;
var heightEl = this.dragDropTargetArray[no][0].offsetHeight;
if(leftPosMouse > leftPosEl && leftPosMouse < (leftPosEl + widthEl) && topPosMouse > topPosEl && topPosMouse < (topPosEl + heightEl)){
if(this.dragDropTargetArray[no][1]){
try{
eval(this.dragDropTargetArray[no][1] + '("' + this.dragDropSourcesArray[this.numericIdToBeDragged][0].id + '","' + this.dragDropTargetArray[no][0].id + '",' + e.clientX + ',' + e.clientY + ')');
}catch(e){
alert('Unable to execute \n' + this.dragDropTargetArray[no][1] + '("' + this.dragDropSourcesArray[this.numericIdToBeDragged][0].id + '","' + this.dragDropTargetArray[no][0].id + '",' + e.clientX + ',' + e.clientY + ')');
}
}
break;
}
}
if(this.dragDropSourcesArray[this.numericIdToBeDragged][1]){
this.__slideElementBackIntoItsOriginalPosition(this.numericIdToBeDragged);
}
this.dragDropTimer = -1;
DHTMLSuite.commonObj.__setOkToMakeTextSelections(true);
this.numericIdToBeDragged = false;
}
,
__slideElementBackIntoItsOriginalPosition : function(numId)
{
var currentX = this.dragObjCloneArray[numId].style.left.replace('px','')/1;
var currentY = this.dragObjCloneArray[numId].style.top.replace('px','')/1;
var targetX = DHTMLSuite.commonObj.getLeftPos(this.dragDropSourcesArray[numId][0]);
var targetY = DHTMLSuite.commonObj.getTopPos(this.dragDropSourcesArray[numId][0]);;
if(this.moveBackBySliding){
this.__processSlideByPixels(numId,currentX,currentY,targetX,targetY);
}else{
this.dragObjCloneArray[numId].style.display='none';
this.dragDropSourcesArray[numId][0].style.visibility = 'visible';
}
}
,
__processSlideByPixels : function(numId,currentX,currentY,targetX,targetY)
{
var slideX = Math.round(Math.abs(Math.max(currentX,targetX) - Math.min(currentX,targetX)) / 10);
var slideY = Math.round(Math.abs(Math.max(currentY,targetY) - Math.min(currentY,targetY)) / 10);
if(slideY<3 && Math.abs(slideX)<10)slideY = 3;	// 3 is minimum slide value
if(slideX<3 && Math.abs(slideY)<10)slideX = 3;	// 3 is minimum slide value
if(currentX > targetX) slideX*=-1;	// If current x is larger than target x, make slide value negative<br>
if(currentY > targetY) slideY*=-1;	// If current y is larger than target x, make slide value negative
currentX = currentX + slideX;
currentY = currentY + slideY;
if(Math.max(currentX,targetX) - Math.min(currentX,targetX) < 4)currentX = targetX;
if(Math.max(currentY,targetY) - Math.min(currentY,targetY) < 4)currentY = targetY;
this.dragObjCloneArray[numId].style.left = currentX + 'px';
this.dragObjCloneArray[numId].style.top = currentY + 'px';
if(currentX!=targetX || currentY != targetY){
window.thisRef = this;	// Reference to this dragdrop object
setTimeout('window.thisRef.__processSlideByPixels("' + numId + '",' + currentX + ',' + currentY + ',' + targetX + ',' + targetY + ')',5);
}else{	// Slide completed. Make absolute positioned element invisible and original element visible
this.dragObjCloneArray[numId].style.display='none';
this.dragDropSourcesArray[numId][0].style.visibility = 'visible';
}
}
}
var refToTabViewObjects = new Array();	// Reference to objects of this class.
DHTMLSuite.tabView = function()
{
var textPadding;				// Tab spacing
var strictDocType ; 			// Using a strict document type, i.e. <!DOCTYPE>
var DHTMLSuite_tabObj;		// Reference to div surrounding the tab set
var activeTabIndex;				// Currently displayed tab(index - 0 = first tab)
var initActiveTabIndex;			// Initially displayed tab(index - 0 = first tab)
var ajaxObjects;				// Reference to ajax objects
var tabView_countTabs;
var tabViewHeight;
var tabSetParentId;				// Id of div surrounding the tab set.
var tabTitles;					// Tab titles
var width;						// width of tab view
var height;						// height of tab view
var layoutCSS;
var outsideObjectRefIndex;		// Which index of refToTabViewObjects refers to this object.
var maxNumberOfTabs;
var dynamicContentObj;
var closeButtons;
this.textPadding = 3;
this.strictDocType = true;
this.ajaxObjects = new Array();
this.tabTitles = new Array();
this.layoutCSS = 'tab-view.css';
this.maxNumberOfTabs = 6;
this.dynamicContentObj = false;
this.closeButtons = new Array();
try{
if(!standardObjectsCreated)DHTMLSuite.createStandardObjects();	// This line starts all the init methods
}catch(e){
alert('You need to include the dhtmlSuite-common.js file');
}
}
DHTMLSuite.tabView.prototype = {
init : function()
{
DHTMLSuite.commonObj.loadCSS(this.layoutCSS);
this.outsideObjectRefIndex = refToTabViewObjects.length;
refToTabViewObjects[this.outsideObjectRefIndex] = this;
try{
this.dynamicContentObj = new DHTMLSuite.dynamicContent();
}catch(e){
alert('You need to include DHTMLSuite-dynamicContent.js');
}
this.__initializeAndParseTabs(false,false);
}
,
getMaximumNumberOfTabs : function()
{
return this.maxNumberOfTabs;
}
,
setMaximumTabs : function(maximumNumberOfTabs)
{
this.maxNumberOfTabs = maximumNumberOfTabs;
}
,
setParentId : function(idOfParentHTMLElement)
{
this.tabSetParentId = idOfParentHTMLElement;
this.DHTMLSuite_tabObj = document.getElementById(idOfParentHTMLElement);
}
,
setWidth : function(newWidth)
{
this.width = newWidth;
}
,
setHeight : function(newHeight)
{
this.height = newHeight;
}
,
setIndexActiveTab : function(indexOfNewActiveTab)
{
this.initActiveTabIndex = indexOfNewActiveTab;
}
,
setTabTitles : function(titleOfTabs)
{
this.tabTitles = titleOfTabs;
}
,
setCloseButtons : function(closeButtons)
{
this.closeButtons = closeButtons;
}
,
createNewTab : function(parentId,tabTitle,tabContent,tabContentUrl,closeButton)
{
if(this.tabView_countTabs>=this.maxNumberOfTabs)return;	// Maximum number of tabs reached - return
var div = document.createElement('DIV');	// Create new tab content div.
div.className = 'DHTMLSuite_aTab';	// Assign new tab to CSS class DHTMLSuite_aTab
this.DHTMLSuite_tabObj.appendChild(div);			// Appending new tab content div to main tab view div
var tabId = this.__initializeAndParseTabs(true,tabTitle,closeButton);	// Call the init method in order to create tab header and tab images
if(tabContent)div.innerHTML = tabContent;	// Static tab content specified, put it into the new div
if(tabContentUrl){	// Get content from external file
this.dynamicContentObj.loadContent('tabView' + parentId +'_' + tabId,tabContentUrl);
}
}
,
deleteTab : function(tabLabel,tabIndex)
{
if(tabLabel){	// Delete tab by tab title
var index = this.getTabIndexByTitle(tabLabel);	// Get index of tab
if(index!=-1){	// Tab exists if index<>-1
this.deleteTab(false,index);
}
}else if(tabIndex>=0){	// Delete tab by tab index.
if(document.getElementById('tabTab' + this.tabSetParentId + '_' + tabIndex)){
var obj = document.getElementById('tabTab' + this.tabSetParentId + '_' + tabIndex);
var id = obj.parentNode.parentNode.id;
obj.parentNode.removeChild(obj);
var obj2 = document.getElementById('tabView' + this.tabSetParentId + '_' + tabIndex);
obj2.parentNode.removeChild(obj2);
this.__resetTabIds(this.tabSetParentId);
this.initActiveTabIndex=-1;
var newIndex = 0;
if(refToTabViewObjects[this.outsideObjectRefIndex].activeTabIndex==tabIndex)refToTabViewObjects[this.outsideObjectRefIndex].activeTabIndex=-1;
this.__showTab(this.tabSetParentId,newIndex,this.outsideObjectRefIndex);
}
}
}
,  	// {{{ addContentToTab()
addContentToTab : function(tabLabel,filePath)
{
var index = this.getTabIndexByTitle(tabLabel);	// Get index of tab
if(index!=-1){	// Tab found
this.dynamicContentObj.loadContent('tabView' + this.tabSetParentId + '_' + index,filePath);
}
}
,
displayATab : function(tabLabel,tabIndex)
{
if(tabLabel){	// Delete tab by tab title
var index = this.getTabIndexByTitle(tabLabel);	// Get index of tab
if(index!=-1){	// Tab exists if index<>-1
this.initActiveTabIndex = index;
}else return false;
}else{
this.initActiveTabIndex = tabIndex;
}
this.__showTab(this.tabSetParentId,this.initActiveTabIndex,this.outsideObjectRefIndex)
}
,
getTabIndex : function()
{
var divs = this.DHTMLSuite_tabObj.getElementsByTagName('DIV');
var tabIndex = 0;
for(var no=0;no<divs.length;no++){
if(divs[no].id.indexOf('tabTab')>=0){
if(divs[no].className!='tabInactive')return tabIndex;
tabIndex++;
}
}
return tabIndex;
}
,
__setPadding : function(obj,padding){
var span = obj.getElementsByTagName('SPAN')[0];
span.style.paddingLeft = padding + 'px';
span.style.paddingRight = padding + 'px';
}
,
__showTab : function(parentId,tabIndex,objectIndex)
{
var parentId_div = parentId + "_";
if(!document.getElementById('tabView' + parentId_div + tabIndex)){
return;
}
if(refToTabViewObjects[objectIndex].activeTabIndex>=0){
if(refToTabViewObjects[objectIndex].activeTabIndex==tabIndex){
return;
}
var obj = document.getElementById('tabTab'+parentId_div + refToTabViewObjects[objectIndex].activeTabIndex);
if(!obj){
refToTabViewObjects[objectIndex].activeTabIndex = 0;
var obj = document.getElementById('tabTab'+parentId_div + refToTabViewObjects[objectIndex].activeTabIndex);
}
obj.className='tabInactive';
obj.style.backgroundImage = 'url(\'' + DHTMLSuite.configObj.imagePath + 'tab_left_inactive.gif' + '\')';
var imgs = obj.getElementsByTagName('IMG');
var img = imgs[imgs.length-1];
img.src = DHTMLSuite.configObj.imagePath + 'tab_right_inactive.gif';
document.getElementById('tabView' + parentId_div + refToTabViewObjects[objectIndex].activeTabIndex).style.display='none';
}
var thisObj = document.getElementById('tabTab'+ parentId_div +tabIndex);
thisObj.className='tabActive';
thisObj.style.backgroundImage = 'url(\'' + DHTMLSuite.configObj.imagePath + 'tab_left_active.gif' + '\')';
var imgs = thisObj.getElementsByTagName('IMG');
var img = imgs[imgs.length-1];
img.src = DHTMLSuite.configObj.imagePath + 'tab_right_active.gif';
document.getElementById('tabView' + parentId_div + tabIndex).style.display='block';
refToTabViewObjects[objectIndex].activeTabIndex = tabIndex;
var parentObj = thisObj.parentNode;
var aTab = parentObj.getElementsByTagName('DIV')[0];
countObjects = 0;
var startPos = 2;
var previousObjectActive = false;
while(aTab){
if(aTab.tagName=='DIV'){
if(previousObjectActive){
previousObjectActive = false;
startPos-=2;
}
if(aTab==thisObj){
startPos-=2;
previousObjectActive=true;
refToTabViewObjects[objectIndex].__setPadding(aTab,refToTabViewObjects[objectIndex].textPadding+1);
}else{
refToTabViewObjects[objectIndex].__setPadding(aTab,refToTabViewObjects[objectIndex].textPadding);
}
aTab.style.left = startPos + 'px';
countObjects++;
startPos+=2;
}
aTab = aTab.nextSibling;
}
return;
}
,
__tabClick : function(inputObj,index)
{
var idArray = inputObj.id.split('_');
var parentId = inputObj.getAttribute('parentRefId');
if(!parentId)parentId=  inputObj.parentRefId;
this.__showTab(parentId,idArray[idArray.length-1].replace(/[^0-9]/gi,''),index);
}
,
__rolloverTab : function()
{
if(this.className.indexOf('tabInactive')>=0){
this.className='inactiveTabOver';
this.style.backgroundImage = 'url(\'' + DHTMLSuite.configObj.imagePath + 'tab_left_over.gif' + '\')';
var imgs = this.getElementsByTagName('IMG');
var img = imgs[imgs.length-1];
img.src = DHTMLSuite.configObj.imagePath + 'tab_right_over.gif';
}
}
,
__rolloutTab : function()
{
if(this.className ==  'inactiveTabOver'){
this.className='tabInactive';
this.style.backgroundImage = 'url(\'' + DHTMLSuite.configObj.imagePath + 'tab_left_inactive.gif' + '\')';
var imgs = this.getElementsByTagName('IMG');
var img = imgs[imgs.length-1];
img.src = DHTMLSuite.configObj.imagePath + 'tab_right_inactive.gif';
}
}
,
__initializeAndParseTabs : function(additionalTab,nameOfAdditionalTab,additionalCloseButton)
{
this.DHTMLSuite_tabObj.className = ' DHTMLSuite_tabWidget';
window.refToThisTabSet = this;
if(!additionalTab || additionalTab=='undefined'){
this.DHTMLSuite_tabObj = document.getElementById(this.tabSetParentId);
this.width = this.width + '';
if(this.width.indexOf('%')<0)this.width= this.width + 'px';
this.DHTMLSuite_tabObj.style.width = this.width;
this.height = this.height + '';
if(this.height.length>0){
if(this.height.indexOf('%')<0)this.height= this.height + 'px';
this.DHTMLSuite_tabObj.style.height = this.height;
}
var tabDiv = document.createElement('DIV');
var firstDiv = this.DHTMLSuite_tabObj.getElementsByTagName('DIV')[0];
this.DHTMLSuite_tabObj.insertBefore(tabDiv,firstDiv);
tabDiv.className = 'DHTMLSuite_tabContainer';
this.tabView_countTabs = 0;
var tmpTabTitles = this.tabTitles;	// tmpTab titles set to current tab titles - this variable is used in the loop below
}else{	// A new tab being created dynamically afterwards.
var tabDiv = this.DHTMLSuite_tabObj.getElementsByTagName('DIV')[0];
var firstDiv = this.DHTMLSuite_tabObj.getElementsByTagName('DIV')[1];
this.initActiveTabIndex = this.tabView_countTabs;
var tmpTabTitles = Array(nameOfAdditionalTab);	// tmpTab titles set to only the new tab
}
for(var no=0;no<tmpTabTitles.length;no++){
var aTab = document.createElement('DIV');
aTab.id = 'tabTab' + this.tabSetParentId + "_" +  (no + this.tabView_countTabs);
aTab.onmouseover = this.__rolloverTab;
aTab.onmouseout = this.__rolloutTab;
aTab.setAttribute('parentRefId',this.tabSetParentId);
aTab.parentRefId = this.tabSetParentId;
var numIndex = window.refToThisTabSet.outsideObjectRefIndex+'';
aTab.onclick = function() { window.refToThisTabSet.__tabClick(this,numIndex); };
DHTMLSuite.commonObj.__addEventElement(aTab);
aTab.className='tabInactive';
aTab.style.backgroundImage = 'url(\'' + DHTMLSuite.configObj.imagePath + 'tab_left_inactive.gif' + '\')';
tabDiv.appendChild(aTab);
var span = document.createElement('SPAN');
span.innerHTML = tmpTabTitles[no];
aTab.appendChild(span);
if(this.closeButtons[no] || additionalCloseButton){
var closeButton = document.createElement('IMG');
closeButton.src = DHTMLSuite.configObj.imagePath + 'tab-view-close.gif';
closeButton.style.position='absolute';
closeButton.style.top = '4px';
closeButton.style.right = '2px';
closeButton.onmouseover = this.__mouseOverEffectCloseButton;
closeButton.onmouseout = this.__mouseOutEffectForCloseButton;
DHTMLSuite.commonObj.__addEventElement(closeButton);
span.innerHTML = span.innerHTML + '&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;';
var deleteTxt = span.innerHTML+'';
closeButton.onclick = function(){ refToTabViewObjects[numIndex].deleteTab( this.parentNode.innerHTML) };
span.appendChild(closeButton);
}
var img = document.createElement('IMG');
img.valign = 'bottom';
img.src = DHTMLSuite.configObj.imagePath + 'tab_right_inactive.gif';
if((DHTMLSuite.clientInfoObj.navigatorVersion && DHTMLSuite.clientInfoObj.navigatorVersion<6) || (DHTMLSuite.clientInfoObj.isMSIE && !this.strictDocType)){
img.style.styleFloat = 'none';
img.style.position = 'relative';
img.style.top = '4px'
span.style.paddingTop = '4px';
aTab.style.cursor = 'hand';
}	// End IE5.x FIX
aTab.appendChild(img);
}
var tabs = this.DHTMLSuite_tabObj.getElementsByTagName('DIV');
var divCounter = 0;
for(var no=0;no<tabs.length;no++){
if(tabs[no].className=='DHTMLSuite_aTab' && tabs[no].parentNode == this.DHTMLSuite_tabObj){
if(this.height.length>0){
if(this.height.indexOf('%')==-1){
var tmpHeight = (this.height.replace('px','')/1 - 22);
tabs[no].style.height = tmpHeight + 'px';
}else
tabs[no].style.height = this.height;
}
tabs[no].style.display='none';
tabs[no].id = 'tabView' + this.tabSetParentId + "_" + divCounter;
divCounter++;
}
}
this.tabView_countTabs = this.tabView_countTabs + this.tabTitles.length;
this.__showTab(this.tabSetParentId,this.initActiveTabIndex,this.outsideObjectRefIndex);
return this.activeTabIndex;
}
,
__mouseOutEffectForCloseButton : function()
{
this.src = this.src.replace('close-over.gif','close.gif');
}
,
__mouseOverEffectCloseButton : function()
{
this.src = this.src.replace('close.gif','close-over.gif');
}
,
__fillTabWithContentFromAjax : function(ajaxIndex,objId,tabId)
{
var obj = document.getElementById('tabView'+objId + '_' + tabId);
obj.innerHTML = this.ajaxObjects[ajaxIndex].response;
}
,
__resetTabIds : function(parentId)
{
var tabTitleCounter = 0;
var tabContentCounter = 0;
var divs = this.DHTMLSuite_tabObj.getElementsByTagName('DIV');
for(var no=0;no<divs.length;no++){
if(divs[no].className=='DHTMLSuite_aTab' && divs[no].parentNode==this.DHTMLSuite_tabObj){
divs[no].id = 'tabView' + parentId + '_' + tabTitleCounter;
tabTitleCounter++;
}
if(divs[no].id.indexOf('tabTab')>=0 && divs[no].parentNode.parentNode==this.DHTMLSuite_tabObj){
divs[no].id = 'tabTab' + parentId + '_' + tabContentCounter;
tabContentCounter++;
}
}
this.tabView_countTabs = tabContentCounter;
}
,
getTabIndexByTitle : function(tabTitle)
{
tabTitle = tabTitle.replace(/(.*?)&nbsp.*$/gi,'$1');
var divs = this.DHTMLSuite_tabObj.getElementsByTagName('DIV');
for(var no=0;no<divs.length;no++){
if(divs[no].id.indexOf('tabTab')>=0){
var span = divs[no].getElementsByTagName('SPAN')[0];
var spanTitle = span.innerHTML.replace(/(.*?)&nbsp.*$/gi,'$1');
if(spanTitle == tabTitle){
var tmpId = divs[no].id.split('_');
return tmpId[tmpId.length-1].replace(/[^0-9]/g,'')/1;
}
}
}
return -1;
}
}
var JSTreeObj;
var treeUlCounter = 0;
var nodeId = 1;
DHTMLSuite.JSDragDropTree = function()
{
var idOfTree;
var folderImage;
var plusImage;
var minusImage;
var maximumDepth;
var dragNode_source;
var dragNode_parent;
var dragNode_sourceNextSib;
var dragNode_noSiblings;
var dragNode_destination;
var floatingContainer;
var dragDropTimer;
var dropTargetIndicator;
var insertAsSub;
var indicator_offsetX;
var indicator_offsetX_sub;
var indicator_offsetY;
var messageMaximumDepthReached;
var ajaxObjects;
var layoutCSS;
var cookieName;
this.folderImage = 'DHTMLSuite_folder.gif';
this.plusImage = 'DHTMLSuite_plus.gif';
this.minusImage = 'DHTMLSuite_minus.gif';
this.maximumDepth = 6;
this.layoutCSS = 'drag-drop-folder-tree.css';
this.floatingContainer = document.createElement('UL');
this.floatingContainer.style.position = 'absolute';
this.floatingContainer.style.display='none';
this.floatingContainer.id = 'floatingContainer';
this.insertAsSub = false;
document.body.appendChild(this.floatingContainer);
this.dragDropTimer = -1;
this.dragNode_noSiblings = false;
this.cookieName = 'DHTMLSuite_expandedNodes';
if(document.all){
this.indicator_offsetX = 1;	// Offset position of small black lines indicating where nodes would be dropped.
this.indicator_offsetX_sub = 3;
this.indicator_offsetY = 14;
}else{
this.indicator_offsetX = 1;	// Offset position of small black lines indicating where nodes would be dropped.
this.indicator_offsetX_sub = 3;
this.indicator_offsetY = 5;
}
if(navigator.userAgent.indexOf('Opera')>=0){
this.indicator_offsetX = 2;	// Offset position of small black lines indicating where nodes would be dropped.
this.indicator_offsetX_sub = 3;
this.indicator_offsetY = -7;
}
this.messageMaximumDepthReached = ''; // Use '' if you don't want to display a message
this.ajaxObjects = new Array();
try{
if(!standardObjectsCreated)DHTMLSuite.createStandardObjects();	// This line starts all the init methods
}catch(e){
alert('You need to include the dhtmlSuite-common.js file');
}
var objectIndex;
this.objectIndex = DHTMLSuite.variableStorage.arrayOfDhtmlSuiteObjects.length;
DHTMLSuite.variableStorage.arrayOfDhtmlSuiteObjects[this.objectIndex] = this;
}
DHTMLSuite.JSDragDropTree.prototype = {
init : function()
{
var ind = this.objectIndex;
DHTMLSuite.commonObj.loadCSS(this.layoutCSS);
JSTreeObj = this;
this.__createDropIndicator();
if(!document.documentElement.onselectstart)document.documentElement.onselectstart = function() { return DHTMLSuite.commonObj.__getOkToMakeTextSelections(); };
document.documentElement.ondragstart = document.documentElement.ondragstart = function() { return DHTMLSuite.commonObj.cancelEvent() };
DHTMLSuite.commonObj.__addEventElement(document.documentElement);
var nodeId = 0;
var DHTMLSuite_tree = document.getElementById(this.idOfTree);
var menuItems = DHTMLSuite_tree.getElementsByTagName('LI');	// Get an array of all menu items
for(var no=0;no<menuItems.length;no++){
var noChildren = false;
var tmpVar = menuItems[no].getAttribute('noChildren');
if(!tmpVar)tmpVar = menuItems[no].noChildren;
if(tmpVar=='true')noChildren=true;
var noDrag = false;
var tmpVar = menuItems[no].getAttribute('noDrag');
if(!tmpVar)tmpVar = menuItems[no].noDrag;
if(tmpVar=='true')noDrag=true;
nodeId++;
var subItems = menuItems[no].getElementsByTagName('UL');
var img = document.createElement('IMG');
img.src = DHTMLSuite.configObj.imagePath + this.plusImage;
img.onclick = function(e) { DHTMLSuite.variableStorage.arrayOfDhtmlSuiteObjects[ind].showHideNode(e); };
DHTMLSuite.commonObj.__addEventElement(img);
if(subItems.length==0)img.style.visibility='hidden';else{
subItems[0].id = 'tree_ul_' + treeUlCounter;
treeUlCounter++;
}
var aTag = menuItems[no].getElementsByTagName('A')[0];
if(!noDrag)aTag.onmousedown = this.__initializeDragProcess;
if(!noChildren){
DHTMLSuite.commonObj.addEvent(aTag,'mousemove',function(e){ DHTMLSuite.variableStorage.arrayOfDhtmlSuiteObjects[ind].__moveDragableNodes(e,'text'); });
}
DHTMLSuite.commonObj.__addEventElement(aTag);
menuItems[no].insertBefore(img,aTag);
var folderImg = document.createElement('IMG');
if(!noDrag)folderImg.onmousedown = this.__initializeDragProcess;
if(!noChildren){
DHTMLSuite.commonObj.addEvent(folderImg,'mousemove',function(e){ DHTMLSuite.variableStorage.arrayOfDhtmlSuiteObjects[ind].__moveDragableNodes(e,'folder'); });
}
if(menuItems[no].className){
folderImg.src = DHTMLSuite.configObj.imagePath + menuItems[no].className;
}else{
folderImg.src = DHTMLSuite.configObj.imagePath + this.folderImage;
}
DHTMLSuite.commonObj.__addEventElement(folderImg);
menuItems[no].insertBefore(folderImg,aTag);
}
initExpandedNodes = DHTMLSuite.commonObj.getCookie(this.cookieName);
if(initExpandedNodes){
var nodes = initExpandedNodes.split(',');
for(var no=0;no<nodes.length;no++){
if(nodes[no])this.showHideNode(false,nodes[no]);
}
}
DHTMLSuite.commonObj.addEvent(document.documentElement,"mousemove",DHTMLSuite.variableStorage.arrayOfDhtmlSuiteObjects[this.objectIndex].__moveDragableNodes);
DHTMLSuite.commonObj.addEvent(document.documentElement,"mouseup",DHTMLSuite.variableStorage.arrayOfDhtmlSuiteObjects[this.objectIndex].__dropDragableNodes);
}
,
setCookieName : function(cookieName)
{
this.cookieName = cookieName;
}
,
setLayoutCss : function(cssFileName)
{
this.layoutCSS = cssFileName;
}
,
setFolderImage : function(newFolderImage)
{
this.folderImage = newFolderImage;
}
,
setPlusImage : function(newPlusImage)
{
this.plusImage = newPlusImage;
}
,
setMinusImage : function(newMinusImage)
{
this.minusImage = newMinusImage;
}
,
setMaximumDepth : function(maxDepth)
{
this.maximumDepth = maxDepth;
}
,setMessageMaximumDepthReached : function(newMessage)
{
this.messageMaximumDepthReached = newMessage;
}
,
setTreeId : function(idOfTree)
{
this.idOfTree = idOfTree;
}
,
expandAll : function()
{
var menuItems = document.getElementById(this.idOfTree).getElementsByTagName('LI');
for(var no=0;no<menuItems.length;no++){
var subItems = menuItems[no].getElementsByTagName('UL');
if(subItems.length>0 && subItems[0].style.display!='block'){
this.showHideNode(false,menuItems[no].id);
}
}
}
,
collapseAll : function()
{
var menuItems = document.getElementById(this.idOfTree).getElementsByTagName('LI');
for(var no=0;no<menuItems.length;no++){
var subItems = menuItems[no].getElementsByTagName('UL');
if(subItems.length>0 && subItems[0].style.display=='block'){
this.showHideNode(false,menuItems[no].id);
}
}
}
,
showHideNode : function(e,inputId)
{
if(inputId){
if(!document.getElementById(inputId))return;
thisNode = document.getElementById(inputId).getElementsByTagName('IMG')[0];
}else {
if(document.all)e = event;
var srcEl = DHTMLSuite.commonObj.getSrcElement(e);
thisNode = srcEl;
if(srcEl.tagName=='A')thisNode = srcEl.parentNode.getElementsByTagName('IMG')[0];
}
if(thisNode.style.visibility=='hidden')return;
var parentNode = thisNode.parentNode;
inputId = parentNode.id.replace(/[^0-9]/g,'');
if(thisNode.src.indexOf(this.plusImage)>=0){
thisNode.src = thisNode.src.replace(this.plusImage,this.minusImage);
var ul = parentNode.getElementsByTagName('UL')[0];
ul.style.display='block';
if(!initExpandedNodes)initExpandedNodes = ',';
if(initExpandedNodes.indexOf(',' + inputId + ',')<0) initExpandedNodes = initExpandedNodes + inputId + ',';
}else{
thisNode.src = thisNode.src.replace(this.minusImage,this.plusImage);
parentNode.getElementsByTagName('UL')[0].style.display='none';
initExpandedNodes = initExpandedNodes.replace(',' + inputId,'');
}
DHTMLSuite.commonObj.setCookie(this.cookieName,initExpandedNodes,500);
return false;
}
,
getSaveString : function(initObj,saveString)
{
if(!saveString)var saveString = '';
if(!initObj){
initObj = document.getElementById(this.idOfTree);
}
var lis = initObj.getElementsByTagName('LI');
if(lis.length>0){
var li = lis[0];
while(li){
if(li.id){
if(saveString.length>0)saveString = saveString + ',';
saveString = saveString + li.id.replace(/[^0-9]/gi,'');
saveString = saveString + '-';
if(li.parentNode.id!=this.idOfTree)saveString = saveString + li.parentNode.parentNode.id.replace(/[^0-9]/gi,''); else saveString = saveString + '0';
var ul = li.getElementsByTagName('UL');
if(ul.length>0){
saveString = this.getSaveString(ul[0],saveString);
}
}
li = li.nextSibling;
}
}
if(initObj.id == this.idOfTree){
return saveString;
}
return saveString;
}
,
__initializeDragProcess : function(e)
{
if(document.all)e = event;
var subs = JSTreeObj.floatingContainer.getElementsByTagName('LI');
if(subs.length>0){
if(JSTreeObj.dragNode_sourceNextSib){
JSTreeObj.dragNode_parent.insertBefore(JSTreeObj.dragNode_source,JSTreeObj.dragNode_sourceNextSib);
}else{
JSTreeObj.dragNode_parent.appendChild(JSTreeObj.dragNode_source);
}
}
JSTreeObj.dragNode_source = this.parentNode;
JSTreeObj.dragNode_parent = this.parentNode.parentNode;
JSTreeObj.dragNode_sourceNextSib = false;
if(JSTreeObj.dragNode_source.nextSibling)JSTreeObj.dragNode_sourceNextSib = JSTreeObj.dragNode_source.nextSibling;
JSTreeObj.dragNode_destination = false;
JSTreeObj.dragDropTimer = 0;
DHTMLSuite.commonObj.__setOkToMakeTextSelections(false);
JSTreeObj.__waitBeforeDragProcessStarts();
return false;
}
,
__waitBeforeDragProcessStarts : function()
{
if(this.dragDropTimer>=0 && this.dragDropTimer<10){
this.dragDropTimer = this.dragDropTimer + 1;
setTimeout('JSTreeObj.__waitBeforeDragProcessStarts()',20);
return;
}
if(this.dragDropTimer==10)
{
JSTreeObj.floatingContainer.style.display='block';
JSTreeObj.floatingContainer.appendChild(JSTreeObj.dragNode_source);
}
}
,
__moveDragableNodes : function(e,tagType)
{
if(JSTreeObj.dragDropTimer<10)return;
if(document.all)e = event;
dragDrop_x = e.clientX/1 + 5 + document.body.scrollLeft;
dragDrop_y = e.clientY/1 + 5 + document.documentElement.scrollTop;
JSTreeObj.floatingContainer.style.left = dragDrop_x + 'px';
JSTreeObj.floatingContainer.style.top = dragDrop_y + 'px';
var thisObj = DHTMLSuite.commonObj.getSrcElement(e);
var thisObjOrig = DHTMLSuite.commonObj.getSrcElement(e);
if(thisObj.tagName=='A' || thisObj.tagName=='IMG')thisObj = thisObj.parentNode;
JSTreeObj.dragNode_noSiblings = false;
var tmpVar = thisObj.getAttribute('noSiblings');
if(!tmpVar)tmpVar = thisObj.noSiblings;
if(tmpVar=='true')JSTreeObj.dragNode_noSiblings=true;
if(thisObj && tagType)
{
JSTreeObj.dragNode_destination = thisObj;
var img = thisObj.getElementsByTagName('IMG')[1];
var tmpObj= JSTreeObj.dropTargetIndicator;
tmpObj.style.display='block';
var eventSourceObj = thisObjOrig;
if(JSTreeObj.dragNode_noSiblings && eventSourceObj.tagName=='IMG')eventSourceObj = eventSourceObj.nextSibling;
var tmpImg = tmpObj.getElementsByTagName('IMG')[0];
if(thisObjOrig.tagName=='A' || JSTreeObj.dragNode_noSiblings){
tmpImg.src = tmpImg.src.replace('ind1','ind2');
JSTreeObj.insertAsSub = true;
tmpObj.style.left = (DHTMLSuite.commonObj.getLeftPos(eventSourceObj) + JSTreeObj.indicator_offsetX_sub) + 'px';
}else{
tmpImg.src = tmpImg.src.replace('ind2','ind1');
JSTreeObj.insertAsSub = false;
tmpObj.style.left = (DHTMLSuite.commonObj.getLeftPos(eventSourceObj) + JSTreeObj.indicator_offsetX) + 'px';
}
tmpObj.style.top = (DHTMLSuite.commonObj.getTopPos(thisObj) + JSTreeObj.indicator_offsetY) + 'px';
}
return false;
}
,
__dropDragableNodes:function()
{
if(JSTreeObj.dragDropTimer<10){
JSTreeObj.dragDropTimer = -1;
DHTMLSuite.commonObj.__setOkToMakeTextSelections(true);
return;
}
var showMessage = false;
if(JSTreeObj.dragNode_destination){	// Check depth
var countUp = JSTreeObj.__getDepthOfABranchInTheTree(JSTreeObj.dragNode_destination,'up');
var countDown = JSTreeObj.__getDepthOfABranchInTheTree(JSTreeObj.dragNode_source,'down');
var countLevels = countUp/1 + countDown/1 + (JSTreeObj.insertAsSub?1:0);
if(countLevels>JSTreeObj.maximumDepth){
JSTreeObj.dragNode_destination = false;
showMessage = true; 	// Used later down in this function
}
}
if(JSTreeObj.dragNode_destination){
if(JSTreeObj.insertAsSub){
var uls = JSTreeObj.dragNode_destination.getElementsByTagName('UL');
if(uls.length>0){
ul = uls[0];
ul.style.display='block';
var lis = ul.getElementsByTagName('LI');
if(lis.length>0){	// Sub elements exists - drop dragable node before the first one
ul.insertBefore(JSTreeObj.dragNode_source,lis[0]);
}else {	// No sub exists - use the appendChild method - This line should not be executed unless there's something wrong in the HTML, i.e empty <ul>
ul.appendChild(JSTreeObj.dragNode_source);
}
}else{
var ul = document.createElement('UL');
ul.style.display='block';
JSTreeObj.dragNode_destination.appendChild(ul);
ul.appendChild(JSTreeObj.dragNode_source);
}
var img = JSTreeObj.dragNode_destination.getElementsByTagName('IMG')[0];
img.style.visibility='visible';
img.src = img.src.replace(JSTreeObj.plusImage,JSTreeObj.minusImage);
}else{
if(JSTreeObj.dragNode_destination.nextSibling){
var nextSib = JSTreeObj.dragNode_destination.nextSibling;
nextSib.parentNode.insertBefore(JSTreeObj.dragNode_source,nextSib);
}else{
JSTreeObj.dragNode_destination.parentNode.appendChild(JSTreeObj.dragNode_source);
}
}
var tmpObj = JSTreeObj.dragNode_parent;
var lis = tmpObj.getElementsByTagName('LI');
if(lis.length==0){
var img = tmpObj.parentNode.getElementsByTagName('IMG')[0];
img.style.visibility='hidden';	// Hide [+],[-] icon
tmpObj.parentNode.removeChild(tmpObj);
}
}else{
if(JSTreeObj.dragNode_sourceNextSib){
JSTreeObj.dragNode_parent.insertBefore(JSTreeObj.dragNode_source,JSTreeObj.dragNode_sourceNextSib);
}else{
JSTreeObj.dragNode_parent.appendChild(JSTreeObj.dragNode_source);
}
}
JSTreeObj.dropTargetIndicator.style.display='none';
JSTreeObj.dragDropTimer = -1;
DHTMLSuite.commonObj.__setOkToMakeTextSelections(true);
if(showMessage && JSTreeObj.messageMaximumDepthReached)alert(JSTreeObj.messageMaximumDepthReached);
}
,
__createDropIndicator : function()
{
this.dropTargetIndicator = document.createElement('DIV');
this.dropTargetIndicator.style.zIndex = 240000;
this.dropTargetIndicator.style.position = 'absolute';
this.dropTargetIndicator.style.display='none';
var img = document.createElement('IMG');
img.src = DHTMLSuite.configObj.imagePath + 'dragDrop_ind1.gif';
img.id = 'dragDropIndicatorImage';
this.dropTargetIndicator.appendChild(img);
document.body.appendChild(this.dropTargetIndicator);
}
,
__getDepthOfABranchInTheTree : function(obj,direction,stopAtObject){
var countLevels = 0;
if(direction=='up'){
while(obj.parentNode && obj.parentNode!=stopAtObject){
obj = obj.parentNode;
if(obj.tagName=='UL')countLevels = countLevels/1 +1;
}
return countLevels;
}
if(direction=='down'){
var subObjects = obj.getElementsByTagName('LI');
for(var no=0;no<subObjects.length;no++){
countLevels = Math.max(countLevels,JSTreeObj.__getDepthOfABranchInTheTree(subObjects[no],"up",obj));
}
return countLevels;
}
}
,
__cancelSelectionEvent : function()
{
if(JSTreeObj.dragDropTimer<10)return true;
return false;
}
}
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
,
setWaitMessage : function(newWaitMessage)
{
this.waitMessage = newWaitMessage;
}
,
setWaitImage : function(newWaitImage)
{
this.waitImage = newWaitImage;
}
,
setCache : function(enableCache)
{
this.enableCache = enableCache;
}
,
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
,
__ajax_displayError : function(divId,ajaxIndex,url,functionToCallOnLoaded)
{
document.getElementById(divId).innerHTML = '<h2>Message from DHTMLSuite.dynamicContent</h2><p>The ajax request for ' + url + ' failed</p>';
}
,
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
,
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
,
__evaluateCss : function(obj)
{
var cssTags = obj.getElementsByTagName('STYLE');
var head = document.getElementsByTagName('HEAD')[0];
for(var no=0;no<cssTags.length;no++){
head.appendChild(cssTags[no]);
}
}
}
DHTMLSuite.colorHelp = function()
{
}
DHTMLSuite.colorHelp.prototype = {
baseConverter : function(numberToConvert,oldBase,newBase) {
numberToConvert = numberToConvert + "";
numberToConvert = numberToConvert.toUpperCase();
var listOfCharactersOfCharacters = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ";
var dec = 0;
for (var i = 0; i <=  numberToConvert.length; i++) {
dec += (listOfCharacters.indexOf(numberToConvert.charAt(i))) * (Math.pow(oldBase , (numberToConvert.length - i - 1)));
}
numberToConvert = "";
var magnitude = Math.floor((Math.log(dec))/(Math.log(newBase)));
for (var i = magnitude; i >= 0; i--) {
var amount = Math.floor(dec/Math.pow(newBase,i));
numberToConvert = numberToConvert + listOfCharacters.charAt(amount);
dec -= amount*(Math.pow(newBase,i));
}
if(numberToConvert.length==0)numberToConvertToConvert=0;
return numberToConvert;
}
,
getHSV : function(rgbColor){
rgbColor = rgbColor.replace('#','');
red = baseConverter(rgbColor.substr(0,2),16,10);
green = baseConverter(rgbColor.substr(2,2),16,10);
blue = baseConverter(rgbColor.substr(4,2),16,10);
if(red.length==0)red=0;
if(green.length==0)green=0;
if(blue.length==0)blue=0;
red = red/255;
green = green/255;
blue = blue/255;
maxValue = Math.max(red,green,blue);
minValue = Math.min(red,green,blue);
var hue = 0;
if(maxValue==minValue){
hue = 0;
saturation=0;
}else{
if(red == maxValue){
hue = (green - blue) / (maxValue-minValue)/1;
}else if(green == maxValue){
hue = 2 + (blue - red)/1 / (maxValue-minValue)/1;
}else if(blue == maxValue){
hue = 4 + (red - green) / (maxValue-minValue)/1;
}
saturation = (maxValue-minValue) / maxValue;
}
hue = hue * 60;
valueBrightness = maxValue;
if(valueBrightness/1<0.5){
}
if(valueBrightness/1>= 0.5){
}
returnArray = [hue,saturation,valueBrightness];
return returnArray;
}
,
toRgb : function(hue,saturation,valueBrightness){
Hi = Math.floor(hue / 60);
if(hue==360)Hi=0;
f = hue/60 - Hi;
p = (valueBrightness * (1- saturation)).toPrecision(2);
q = (valueBrightness * (1 - (f * saturation))).toPrecision(2);
t = (valueBrightness * (1 - ((1-f)*saturation))).toPrecision(2);
switch(Hi){
case 0:
red = valueBrightness;
green = t;
blue = p;
break;
case 1:
red = q;
green = valueBrightness;
blue = p;
break;
case 2:
red = q;
green = valueBrightness;
blue = t;
break;
case 3:
red = p;
green = q;;
blue = valueBrightness;
break;
case 4:
red = t;
green = p;
blue = valueBrightness;
break;
case 5:
red = valueBrightness;
green = p;
blue = q;
break;
}
if(saturation==0){
red = valueBrightness;
green = valueBrightness;
blue = valueBrightness;
}
red*=255;
green*=255;
blue*=255;
red = Math.round(red);
green = Math.round(green);
blue = Math.round(blue);
red = baseConverter(red,10,16);
green = baseConverter(green,10,16);
blue = baseConverter(blue,10,16);
red = red + "";
green = green + "";
blue = blue + "";
while(red.length<2){
red = "0" + red;
}
while(green.length<2){
green = "0" + green;
}
while(blue.length<2){
blue = "0" + "" + blue;
}
rgbColor = "#" + red + "" + green + "" + blue;
return rgbColor.toUpperCase();
}
,
findColorByDegrees : function(rgbColor,degrees){
rgbColor = rgbColor.replace('#','');
myArray = this.getHSV(rgbColor);
myArray[0]+=degrees;
if(myArray[0]>=360)myArray[0]-=360;
if(myArray[0]<0)myArray[0]+=360;
return toRgb(myArray[0],myArray[1],myArray[2]);
}
,
findColorByBrightness : function(rgbColor,brightness){
rgbColor = rgbColor.replace('#','');
myArray = thhis.getHSV(rgbColor);
myArray[2]+=brightness/100;
if(myArray[2]>1)myArray[2]=1;
if(myArray[2]<0)myArray[2]=0;
myArray[1]+=brightness/100;
if(myArray[1]>1)myArray[1]=1;
if(myArray[1]<0)myArray[1]=0;
return toRgb(myArray[0],myArray[1],myArray[2]);
}
,
getRgbFromNumbers : function(red,green,blue)
{
red = this.baseConverter(red,10,16);
if(red.length==0)red = '0' + red;
green = this.baseConverter(green,10,16);
if(green.length==0)green = '0' + green;
blue = this.baseConverter(blue,10,16);
if(blue.length==0)blue = '0' + blue;
return '#' + red + green + blue;
}
}
DHTMLSuite.sliderObjects = new Array();	// Array of slider objects. Used in events when "this" refers to the tag trigger the event and not the object.
DHTMLSuite.indexOfCurrentlyActiveSlider = false;	// Index of currently active slider(i.e. index in the DHTMLSuite.sliderObjects array)
DHTMLSuite.slider_generalMouseEventsAdded = false;	// Only assign mouse move and mouseup events once. This variable make sure that happens.
DHTMLSuite.slider = function()
{
var width;							// Width of slider
var height;							// height of slider
var targetObj;						// Object where slider will be added.
var sliderWidth;					// Width of slider image. this is needed in order to position the slider and calculate values correctly.
var sliderDirection;				// Horizontal or vertical
var functionToCallOnChange; 		// Function to call when the slider is moved.
var layoutCss;
var sliderMaxValue;					// Maximum value to return from slider
var sliderMinValue;					// Minimum value to return from slider
var initialValue;					// Initial value of slider
var sliderSize;						// Size of sliding area.
var directionOfPointer;				// Direction of slider pointer;
var slideInProcessTimer;			// Private variable used to determine if slide is in progress or not.
var indexThisSlider;				// Index of object in the DHTMLSuite.sliderObjects array
var numberOfSteps;					// Hardcoded steps
var stepLinesVisibility;			// Visibility of lines indicating where the slider steps are
var slide_event_pos;					// X position of mouse when slider drag starts
var slide_start_pos;					// X position of slider when drag starts
var sliderHandleImg;				// Reference to the small slider handle
var sliderName;						// A name you can use to identify a slider. Useful if you have more than one slider, but only one onchange event.
var sliderValueReversed;			// Variable indicating if the value of the slider is reversed(i.e. max at left and min at right)
this.sliderWidth = 9;				// Initial width of slider.
this.layoutCss = 'slider.css';		// Default css file
this.sliderDirection = 'hor';		// Horizontal is default
this.width = 0;						// Initial widht
this.height = 0;					// Initial height
this.sliderMaxValue = 100; 			// Default max value to return from slider
this.sliderMinValue = 0;			// Default min value to return from slider
this.initialValue = 0;				// Default initial value of slider
this.targetObj = false;				// Set target obj to false initially.
this.directionOfPointer = 'up';		// Default pointer direction for slider handle.
this.slideInProcessTimer = -1;
this.sliderName = '';
this.numberOfSteps = false;
this.stepLinesVisibility = true;
this.sliderValueReversed = false;	// Default value of sliderValueReversed, i.e. max at right, min at left or max at top, min at bottom.
try{
if(!standardObjectsCreated)DHTMLSuite.createStandardObjects();	// This line starts all the init methods
}catch(e){
alert('You need to include the dhtmlSuite-common.js file');
}
var objectIndex;
this.objectIndex = DHTMLSuite.variableStorage.arrayOfDhtmlSuiteObjects.length;
DHTMLSuite.variableStorage.arrayOfDhtmlSuiteObjects[this.objectIndex] = this;
}
DHTMLSuite.slider.prototype = {
init : function()	// Initializes the script
{
if(!this.targetObj){
alert('Error! - No target for slider specified');
return;
}
this.__setWidthAndHeightDynamically();
DHTMLSuite.commonObj.loadCSS(this.layoutCss);
this.__createSlider();
}
,
setSliderTarget : function(targetId)
{
this.targetObj = document.getElementById(targetId);
}
,
setSliderDirection : function(newDirection)
{
newDirection = newDirection + '';
newDirection = newDirection.toLowerCase();
if(newDirection!='hor' && newDirection!='ver'){
alert('Invalid slider direction - possible values: "hor" or "ver"');
return;
}
this.sliderDirection = newDirection;
}
,
setSliderWidth : function(newWidth)
{
newWidth = newWidth + '';
if(newWidth.indexOf('%')==-1)newWidth = newWidth + 'px';
this.width = newWidth;
}
,
setSliderHeight : function(newHeight)
{
newHeight = newHeight + '';
if(newHeight.indexOf('%')==-1)newHeight = newHeight + 'px';
this.height = height;
}
,
setSliderReversed : function()
{
this.sliderValueReversed = true;
}
,
setOnChangeEvent : function(nameOfFunction)
{
this.functionToCallOnChange = nameOfFunction;
}
,
setSliderMaxValue : function(newMaxValue)
{
this.sliderMaxValue = newMaxValue;
}
,
setSliderMinValue : function(newMinValue)
{
this.sliderMinValue = newMinValue;
}
,
setSliderName : function(nameOfSlider)
{
this.sliderName = nameOfSlider;
}
,
setLayoutCss : function(nameOfNewCssFile)
{
this.layoutCss = nameOfNewCssFile;
}
,
setInitialValue : function(newInitialValue)
{
this.initialValue = newInitialValue;
}
,
setSliderPointerDirection : function(directionOfPointer)
{
this.directionOfPointer = directionOfPointer;
}
,
setSliderValue : function(newValue)
{
var position = Math.floor((newValue / this.sliderMaxValue) * this.sliderSize);
if(this.sliderDirection=='hor'){
this.sliderHandleImg.style.left = position + 'px';
}else{
this.sliderHandleImg.style.top = position + 'px';
}
}
,
setNumberOfSliderSteps : function(numberOfSteps)
{
this.numberOfSteps = numberOfSteps;
}
,
setStepLinesVisible : function(visible)
{
this.stepLinesVisibility = visible;
}
,
__setWidthAndHeightDynamically : function()
{
if(!this.width || this.width==0)this.width = this.targetObj.clientWidth+'';
if(!this.height || this.height==0)this.height = this.targetObj.clientHeight+'';
if(!this.width || this.width==0)this.width = this.targetObj.offsetWidth+'';
if(!this.height || this.height==0)this.height = this.targetObj.offsetHeight+'';
if(this.width==0)return;
if(this.height==0)return;
if(this.width.indexOf('px')==-1 && this.width.indexOf('%')==-1)this.width = this.width + 'px';
if(this.height.indexOf('px')==-1 && this.height.indexOf('%')==-1)this.height = this.height + 'px';
}
,
__createSlider : function(initWidth)
{
if(this.targetObj.clientWidth==0 || initWidth==0){
var timeoutTime = 100;
setTimeout('DHTMLSuite.variableStorage.arrayOfDhtmlSuiteObjects[' + this.objectIndex + '].__createSlider(' + this.targetObj.clientWidth + ')',timeoutTime);
return;
}
this.__setWidthAndHeightDynamically();
this.indexThisSlider = DHTMLSuite.sliderObjects.length;
DHTMLSuite.sliderObjects[this.indexThisSlider] = this;
window.refToThisObject = this;
var div = document.createElement('DIV');
div.style.width = this.width;
div.style.cursor = 'default';
div.style.height = this.height;
div.style.position = 'relative';
div.id = 'sliderNumber' + this.indexThisSlider;	// the numeric part of this id is used inside the __setPositionFromClick method
div.onmousedown = this.__setPositionFromClick;
DHTMLSuite.commonObj.__addEventElement(div);
this.targetObj.appendChild(div);
var sliderObj = document.createElement('DIV');
if(this.sliderDirection=='hor'){	// Horizontal slider.
sliderObj.className='DHTMLSuite_slider_horizontal';
sliderObj.style.width = div.clientWidth + 'px';
this.sliderSize = div.offsetWidth - this.sliderWidth;
var sliderHandle = document.createElement('IMG');
var srcHandle = 'slider_handle_down.gif';
sliderHandle.style.bottom = '2px';
if(this.directionOfPointer=='up'){
srcHandle = 'slider_handle_up.gif';
sliderHandle.style.bottom = '0px';
}
sliderHandle.src = DHTMLSuite.configObj.imagePath + srcHandle;
div.appendChild(sliderHandle);
var leftPos;
if(this.sliderValueReversed){
leftPos = Math.round(((this.sliderMaxValue - this.initialValue) / this.sliderMaxValue) * this.sliderSize) -1;
}else{
leftPos = Math.round((this.initialValue / this.sliderMaxValue) * this.sliderSize);
}
sliderHandle.style.left =  leftPos + 'px';
}else{
sliderObj.className='DHTMLSuite_slider_vertical';
sliderObj.style.height = div.clientHeight + 'px';
this.sliderSize = div.clientHeight - this.sliderWidth;
var sliderHandle = document.createElement('IMG');
var srcHandle = 'slider_handle_right.gif';
sliderHandle.style.left = '0px';
if(this.directionOfPointer=='left'){
srcHandle = 'slider_handle_left.gif';
sliderHandle.style.left = '0px';
}
sliderHandle.src = DHTMLSuite.configObj.imagePath + srcHandle;
div.appendChild(sliderHandle);
var topPos;
if(!this.sliderValueReversed){
topPos = Math.floor(((this.sliderMaxValue - this.initialValue) / this.sliderMaxValue) * this.sliderSize);
}else{
topPos = Math.floor((this.initialValue / this.sliderMaxValue) * this.sliderSize);
}
sliderHandle.style.top = topPos + 'px';
}
sliderHandle.id = 'sliderForObject' + this.indexThisSlider;
sliderHandle.style.position = 'absolute';
sliderHandle.style.zIndex = 5;
sliderHandle.onmousedown = this.__initializeSliderDrag;
sliderHandle.ondragstart = function() { return DHTMLSuite.commonObj.cancelEvent() };
sliderHandle.onselectstart = function() { return DHTMLSuite.commonObj.cancelEvent() };
DHTMLSuite.commonObj.__addEventElement(sliderHandle);
this.sliderHandleImg = sliderHandle;
if(!DHTMLSuite.slider_generalMouseEventsAdded){
DHTMLSuite.commonObj.addEvent(document.documentElement,"mousemove",this.__moveSlider);
DHTMLSuite.commonObj.addEvent(document.documentElement,"mouseup",this.__stopSlideProcess);
DHTMLSuite.slider_generalMouseEventsAdded = true;
}
sliderObj.innerHTML = '<span style="cursor:default"></span>';	// In order to get a correct height/width of the div
div.appendChild(sliderObj);
if(this.numberOfSteps && this.stepLinesVisibility){	// Number of steps defined, create graphical lines
var stepSize = this.sliderSize / this.numberOfSteps;
for(var no=0;no<=this.numberOfSteps;no++){
var lineDiv = document.createElement('DIV');
lineDiv.style.position = 'absolute';
lineDiv.innerHTML = '<span></span>';
div.appendChild(lineDiv);
if(this.sliderDirection=='hor'){
lineDiv.className='DHTMLSuite_smallLines_vertical';
lineDiv.style.left = Math.floor((stepSize * no) + (this.sliderWidth/2)) + 'px';
}else{
lineDiv.className='DHTMLSuite_smallLines_horizontal';
lineDiv.style.top = Math.floor((stepSize * no) + (this.sliderWidth/2)) + 'px';
lineDiv.style.left = '14px';
}
}
}
}
,
__initializeSliderDrag : function(e)
{
if(document.all)e = event;
var numIndex = this.id.replace(/[^0-9]/gi,'');	// Get index in the DHTMLSuite.sliderObject array. We get this from the id of the slider image(i.e. "this").
var sliderObj = DHTMLSuite.sliderObjects[numIndex];
DHTMLSuite.indexOfCurrentlyActiveSlider = numIndex;
sliderObj.slideInProcessTimer = 0;
if(sliderObj.sliderDirection=='hor'){
sliderObj.slide_event_pos = e.clientX;	// Get start x position of mouse pointer
sliderObj.slide_start_pos = this.style.left.replace('px','')/1;	// Get x position of slider
}else{
sliderObj.slide_event_pos = e.clientY;	// Get start x position of mouse pointer
sliderObj.slide_start_pos = this.style.top.replace('px','')/1;	// Get x position of slider
}
sliderObj.__waitBeforeSliderDragStarts();
return false;	// Firefox need this line.
}
,
__setPositionFromClick : function(e)
{
if(document.all)e = event;
if (e.target) srcEvent = e.target;
else if (e.srcElement) srcEvent = e.srcElement;
if (srcEvent.nodeType == 3) // defeat Safari bug
srcEvent = srcEvent.parentNode;
if(srcEvent.tagName!='DIV')return;
var numIndex = this.id.replace(/[^0-9]/gi,'');	// Get index in the DHTMLSuite.sliderObject array. We get this from the id of the slider image(i.e. "this").
var sliderObj = DHTMLSuite.sliderObjects[numIndex];
if(sliderObj.numberOfSteps){
modValue = sliderObj.sliderSize / sliderObj.numberOfSteps;	// Find value to calculate modulus by
}
if(sliderObj.sliderDirection=='hor'){
var handlePos = (e.clientX - DHTMLSuite.commonObj.getLeftPos(this) - sliderObj.sliderWidth);
}else{
var handlePos = (e.clientY - DHTMLSuite.commonObj.getTopPos(this) - sliderObj.sliderWidth);
}
if(sliderObj.numberOfSteps){	// Static steps defined
var mod = handlePos % modValue;	// Calculate modulus
if(mod>(modValue/2))mod = modValue-mod; else mod*=-1;	// Should we move the slider handle left or right?
handlePos = handlePos + mod;
}
if(handlePos<0)handlePos = 0;	// Don't allow negative values
if(handlePos > sliderObj.sliderSize)handlePos = sliderObj.sliderSize; // Don't allow values larger the slider size
if(sliderObj.sliderDirection=='hor'){
sliderObj.sliderHandleImg.style.left = handlePos + 'px';
if(!sliderObj.sliderValueReversed){
returnValue = Math.round((handlePos/sliderObj.sliderSize) * (sliderObj.sliderMaxValue - sliderObj.sliderMinValue));
}else{
returnValue = Math.round(((sliderObj.sliderSize - handlePos)/sliderObj.sliderSize) * (sliderObj.sliderMaxValue - sliderObj.sliderMinValue));
}
}else{
sliderObj.sliderHandleImg.style.top = handlePos + 'px';
if(sliderObj.sliderValueReversed){
returnValue = Math.round((topPos/sliderObj.sliderSize) * (sliderObj.sliderMaxValue - sliderObj.sliderMinValue));
}else{
returnValue = Math.round(((sliderObj.sliderSize - handlePos)/sliderObj.sliderSize) * (sliderObj.sliderMaxValue - sliderObj.sliderMinValue));
}
}
returnValue = returnValue + sliderObj.sliderMinValue;
if(sliderObj.functionToCallOnChange)eval(sliderObj.functionToCallOnChange + '(' + returnValue + ',"' + sliderObj.sliderName + '")');
}
,
__waitBeforeSliderDragStarts : function()
{
if(this.slideInProcessTimer<10 && this.slideInProcessTimer>=0){
this.slideInProcessTimer = this.slideInProcessTimer +1;
window.refToThisSlider = this;
setTimeout('window.refToThisSlider.__waitBeforeSliderDragStarts()',5);
}
}
,
__moveSlider : function(e)
{
if(DHTMLSuite.indexOfCurrentlyActiveSlider===false)return;
var sliderObj = DHTMLSuite.sliderObjects[DHTMLSuite.indexOfCurrentlyActiveSlider];
if(document.all)e = event;
if(sliderObj.slideInProcessTimer<10)return;
var returnValue;
if(sliderObj.numberOfSteps){	// Find value to calculate modulus by
modValue = sliderObj.sliderSize / sliderObj.numberOfSteps;
}
if(sliderObj.sliderDirection=='hor'){
var handlePos = e.clientX - sliderObj.slide_event_pos + sliderObj.slide_start_pos;
}else{
var handlePos = e.clientY - sliderObj.slide_event_pos + sliderObj.slide_start_pos;
}
if(sliderObj.numberOfSteps){
var mod = handlePos % modValue;
if(mod>(modValue/2))mod = modValue-mod; else mod*=-1;
handlePos = handlePos + mod;
}
if(handlePos<0)handlePos = 0;
if(handlePos > sliderObj.sliderSize)handlePos = sliderObj.sliderSize;
if(sliderObj.sliderDirection=='hor'){
sliderObj.sliderHandleImg.style.left = handlePos + 'px';
if(!sliderObj.sliderValueReversed){
returnValue = Math.round((handlePos/sliderObj.sliderSize) * (sliderObj.sliderMaxValue - sliderObj.sliderMinValue));
}else{
returnValue = Math.round(((sliderObj.sliderSize - handlePos)/sliderObj.sliderSize) * (sliderObj.sliderMaxValue - sliderObj.sliderMinValue));
}
}else{
sliderObj.sliderHandleImg.style.top = handlePos + 'px';
if(sliderObj.sliderValueReversed){
returnValue = Math.round((handlePos/sliderObj.sliderSize) * (sliderObj.sliderMaxValue - sliderObj.sliderMinValue));
}else{
returnValue = Math.round(((sliderObj.sliderSize - handlePos)/sliderObj.sliderSize) * (sliderObj.sliderMaxValue - sliderObj.sliderMinValue));
}
}
returnValue = returnValue + sliderObj.sliderMinValue;
if(sliderObj.functionToCallOnChange)eval(sliderObj.functionToCallOnChange + '(' + returnValue + ',"' + sliderObj.sliderName + '")');
}
,
__stopSlideProcess : function(e)
{
if(!DHTMLSuite.indexOfCurrentlyActiveSlider)return;
var sliderObj = DHTMLSuite.sliderObjects[DHTMLSuite.indexOfCurrentlyActiveSlider];
sliderObj.slideInProcessTimer = -1;
}
}
DHTMLSuite.modalMessage = function()
{
var url;								// url of modal message
var htmlOfModalMessage;					// html of modal message
var divs_transparentDiv;				// Transparent div covering page content
var divs_content;						// Modal message div.
var layoutCss;							// Name of css file;
var width;								// Width of message box
var height;								// Height of message box
var existingBodyOverFlowStyle;			// Existing body overflow css
var dynContentObj;						// Reference to dynamic content object
var cssClassOfMessageBox;				// Alternative css class of message box - in case you want a different appearance on one of them
var shadowDivVisible;					// Shadow div visible ?
var shadowOffset; 						// X and Y offset of shadow(pixels from content box)
var objectIndex;
this.url = '';							// Default url is blank
this.htmlOfModalMessage = '';			// Default message is blank
this.layoutCss = 'modal-message.css';	// Default CSS file
this.height = 200;						// Default height of modal message
this.width = 400;						// Default width of modal message
this.cssClassOfMessageBox = false;		// Default alternative css class for the message box
this.shadowDivVisible = true;			// Shadow div is visible by default
this.shadowOffset = 5;					// Default shadow offset.
try{
if(!standardObjectsCreated)DHTMLSuite.createStandardObjects();	// This line starts all the init methods
}catch(e){
alert('You need to include the dhtmlSuite-common.js file');
}
this.objectIndex = DHTMLSuite.variableStorage.arrayOfDhtmlSuiteObjects.length;
DHTMLSuite.variableStorage.arrayOfDhtmlSuiteObjects[this.objectIndex] = this;
var ind = this.objectIndex;
DHTMLSuite.commonObj.addEvent(window,"resize",function() { DHTMLSuite.variableStorage.arrayOfDhtmlSuiteObjects[ind].__resizeTransparentDiv(); });
}
DHTMLSuite.modalMessage.prototype = {
setSource : function(urlOfSource)
{
this.url = urlOfSource;
}
,
setHtmlContent : function(newHtmlContent)
{
this.htmlOfModalMessage = newHtmlContent;
}
,
setSize : function(width,height)
{
if(width)this.width = width;
if(height)this.height = height;
}
,
setCssClassMessageBox : function(newCssClass)
{
this.cssClassOfMessageBox = newCssClass;
if(this.divs_content){
if(this.cssClassOfMessageBox)
this.divs_content.className=this.cssClassOfMessageBox;
else
this.divs_content.className='modalDialog_contentDiv';
}
}
,
setShadowOffset : function(newShadowOffset)
{
this.shadowOffset = newShadowOffset
}
,
setWaitMessage : function(newMessage)
{
if(!this.dynContentObj){
try{
this.dynContentObj = new DHTMLSuite.dynamicContent();	// Creating dynamic content object if it doesn't already exist.
}catch(e){
alert('You need to include dhtmlSuite-dynamicContent.js');
}
}
this.dynContentObj.setWaitMessage(newMessage);	// Calling the DHTMLSuite.dynamicContent setWaitMessage
}
,
setWaitImage : function(newImage)
{
if(!this.dynContentObj){
try{
this.dynContentObj = new DHTMLSuite.dynamicContent();	// Creating dynamic content object if it doesn't already exist.
}catch(e){
alert('You need to include dhtmlSuite-dynamicContent.js');
}
}
this.dynContentObj.setWaitImage(newImage);	// Calling the DHTMLSuite.dynamicContent setWaitImage
}
,
setCache : function(cacheStatus)
{
if(!this.dynContentObj){
try{
this.dynContentObj = new DHTMLSuite.dynamicContent();	// Creating dynamic content object if it doesn't already exist.
}catch(e){
alert('You need to include dhtmlSuite-dynamicContent.js');
}
}
this.dynContentObj.setCache(cacheStatus);	// Calling the DHTMLSuite_dynamicContent setCache
}
,
display : function()
{
if(!this.divs_transparentDiv){
DHTMLSuite.commonObj.loadCSS(this.layoutCss);
this.__createDivElements();
}
this.divs_transparentDiv.style.display='block';
this.divs_content.style.display='block';
this.divs_shadow.style.display='block';
this.__resizeAndPositionDivElements();
window.refToThisModalBoxObj = this;
setTimeout('window.refToThisModalBoxObj.__resizeAndPositionDivElements()',150);
this.__addHTMLContent();	// Calling method which inserts content into the message div.
}
,
setShadowDivVisible : function(visible)
{
this.shadowDivVisible = visible;
}
,
close : function()
{
document.documentElement.style.overflow = '';	// Setting the CSS overflow attribute of the <html> tag back to default.
this.divs_transparentDiv.style.display='none';
this.divs_content.style.display='none';
this.divs_shadow.style.display='none';
}
,
__createDivElements : function()
{
this.divs_transparentDiv = document.createElement('DIV');
this.divs_transparentDiv.className='DHTMLSuite_modalDialog_transparentDivs';
this.divs_transparentDiv.style.left = '0px';
this.divs_transparentDiv.style.top = '0px';
document.body.appendChild(this.divs_transparentDiv);
this.divs_content = document.createElement('DIV');
this.divs_content.className = 'DHTMLSuite_modalDialog_contentDiv';
this.divs_content.id = 'DHTMLSuite_modalBox_contentDiv';
document.body.appendChild(this.divs_content);
this.divs_shadow = document.createElement('DIV');
this.divs_shadow.className = 'DHTMLSuite_modalDialog_contentDiv_shadow';
document.body.appendChild(this.divs_shadow);
}
,
__resizeAndPositionDivElements : function()
{
var topOffset = Math.max(document.body.scrollTop,document.documentElement.scrollTop);
if(this.cssClassOfMessageBox)
this.divs_content.className=this.cssClassOfMessageBox;
else
this.divs_content.className='DHTMLSuite_modalDialog_contentDiv';
if(!this.divs_transparentDiv)return;
document.documentElement.style.overflow = 'hidden';
var bodyWidth = document.documentElement.clientWidth;
var bodyHeight = document.documentElement.clientHeight;
this.divs_content.style.width = this.width + 'px';
this.divs_content.style.height= this.height + 'px';
var tmpWidth = this.divs_content.offsetWidth;
var tmpHeight = this.divs_content.offsetHeight;
this.divs_content.style.left = Math.ceil((bodyWidth - tmpWidth) / 2) + 'px';;
this.divs_content.style.top = (Math.ceil((bodyHeight - tmpHeight) / 2) +  topOffset) + 'px';
this.divs_shadow.style.left = (this.divs_content.style.left.replace('px','')/1 + this.shadowOffset) + 'px';
this.divs_shadow.style.top = (this.divs_content.style.top.replace('px','')/1 + this.shadowOffset) + 'px';
this.divs_shadow.style.height = tmpHeight + 'px';
this.divs_shadow.style.width = tmpWidth + 'px';
if(!this.shadowDivVisible)this.divs_shadow.style.display='none';	// Hiding shadow if it has been disabled
this.__resizeTransparentDiv();
}
,
__resizeTransparentDiv : function()
{
if(!this.divs_transparentDiv)return;
var divHeight = Math.max(document.documentElement.offsetHeight,document.body.offsetHeight);
var divWidth = Math.max(document.documentElement.offsetWidth,document.body.offsetWidth);
this.divs_transparentDiv.style.height = divHeight +'px';
this.divs_transparentDiv.style.width = divWidth + 'px';
}
,
__addHTMLContent : function()
{
if(!this.dynContentObj){// dynamic content object doesn't exists?
try{
this.dynContentObj = new DHTMLSuite.dynamicContent();	// Create new DHTMLSuite_dynamicContent object.
}catch(e){
alert('You need to include dhtmlSuite-dynamicContent.js');
}
}
if(this.url){	// url specified - load content dynamically
this.dynContentObj.loadContent('DHTMLSuite_modalBox_contentDiv',this.url);
}else{	// no url set, put static content inside the message box
this.divs_content.innerHTML = this.htmlOfModalMessage;
}
}
}
DHTMLSuite.dynamicTooltip = function()
{
var x_offset_tooltip;					// X Offset tooltip
var y_offset_tooltip;					// Y offset tooltip
var ajax_tooltipObj;
var ajax_tooltipObj_iframe;
var dynContentObj;						// Reference to dynamic content object
var layoutCss;
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
this.ajax_tooltipObj.style.display='block';
this.dynContentObj.loadContent('DHTMLSuite_ajax_tooltip_content',externalFile);
if(DHTMLSuite.clientInfoObj.isMSIE){
this.ajax_tooltipObj_iframe.style.width = this.ajax_tooltipObj.clientWidth + 'px';
this.ajax_tooltipObj_iframe.style.height = this.ajax_tooltipObj.clientHeight + 'px';
}
this.__positionTooltip(inputObj);
}
,
setLayoutCss : function(newCssFileName)
{
this.layoutCss = newCssFileName;
}
,
hideTooltip : function()
{
this.ajax_tooltipObj.style.display='none';
}
,
__positionTooltip : function(inputObj)
{
var leftPos = (DHTMLSuite.commonObj.getLeftPos(inputObj) + inputObj.offsetWidth);
var topPos = DHTMLSuite.commonObj.getTopPos(inputObj);
var tooltipWidth = document.getElementById('DHTMLSuite_ajax_tooltip_content').offsetWidth +  document.getElementById('DHTMLSuite_ajax_tooltip_arrow').offsetWidth;
this.ajax_tooltipObj.style.left = leftPos + 'px';
this.ajax_tooltipObj.style.top = topPos + 'px';
}
}
DHTMLSuite.infoPanel = function()
{
var xpPanel_slideActive;			// Slide down/up active?
var xpPanel_slideSpeed ;			// Speed of slide
var xpPanel_onlyOneExpandedPane;	// Only one pane expanded at a time ?
var savedActivePane;
var savedActiveSub;
var xpPanel_currentDirection;
var cookieNames;
var layoutCSS;
var arrayOfPanes;
var dynamicContentObj;
var paneHeights;					// Array of info pane heights.
var currentlyExpandedPane = false;
this.xpPanel_slideActive = true;	// Slide down/up active?
this.xpPanel_slideSpeed = 20;	// Speed of slide
this.xpPanel_onlyOneExpandedPane = false;	// Only one pane expanded at a time ?
this.savedActivePane = false;
this.savedActiveSub = false;
this.xpPanel_currentDirection = new Array();
this.cookieNames = new Array();
this.currentlyExpandedPane = false;
this.layoutCSS = 'info-pane.css';	// Default css file for this widget.
this.arrayOfPanes = new Array();
this.paneHeights = new Array();
try{
if(!standardObjectsCreated)DHTMLSuite.createStandardObjects();	// This line starts all the init methods
}catch(e){
alert('You need to include the dhtmlSuite-common.js file');
}
try{
this.dynamicContentObj = new DHTMLSuite.dynamicContent();
}catch(e){
alert('You need to include dhtmlSuite-dynamicContent.js');
}
}
DHTMLSuite.infoPanel.prototype = {
addPane : function(idOfPane,labelOfPane,state,nameOfCookie,width)
{
var index = this.arrayOfPanes.length;
this.arrayOfPanes[index] = [idOfPane,labelOfPane,state,nameOfCookie,width];
}
,
addContentToPane : function(idOfPane,pathToExternalFile)
{
var obj = document.getElementById(idOfPane);
var subDivs = obj.getElementsByTagName('DIV');
for(var no=0;no<subDivs.length;no++){
if(subDivs[no].className=='DHTMLSuite_infoPaneContent'){
window.refToThisPane = this;
this.__slidePane(this.xpPanel_slideSpeed,subDivs[no].id);
this.dynamicContentObj.loadContent(subDivs[no].id,pathToExternalFile,"window.refToThisPane.__resizeAndRepositionPane('" + idOfPane + "')");
if(subDivs[no].parentNode.style.display=='none' || subDivs[no].parentNode.style.height=='0px'){	// Pane is collapsed, expand it
var topBarObj = DHTMLSuite.domQueryObj.getElementsByClassName('DHTMLSuite_infoPaneTopBar',subDivs[no].parentNode.parentNode);
this.__showHidePaneContent(topBarObj[0]);
}
return;
}
}
}
,
addStaticContentToPane : function(idOfPane,newContent)
{
var obj = document.getElementById(idOfPane);
var subDivs = obj.getElementsByTagName('DIV');
for(var no=0;no<subDivs.length;no++){
if(subDivs[no].className=='DHTMLSuite_infoPaneContent'){
window.refToThisPane = this;
this.__slidePane(this.xpPanel_slideSpeed,subDivs[no].id);
subDivs[no].innerHTML = newContent;
if(subDivs[no].parentNode.style.display=='none' || subDivs[no].parentNode.style.height=='0px'){	// Pane is collapsed, expand it
var topBarObj = DHTMLSuite.domQueryObj.getElementsByClassName('DHTMLSuite_infoPaneTopBar',subDivs[no].parentNode.parentNode);
this.__showHidePaneContent(topBarObj[0]);
}
this.__resizeAndRepositionPane(idOfPane);
return;
}
}
}
,
init : function()
{
DHTMLSuite.commonObj.loadCSS(this.layoutCSS);
for(var no=0;no<this.arrayOfPanes.length;no++){		// Loop through panes
var tmpDiv = document.getElementById(this.arrayOfPanes[no][0]);	// Creating reference to pane div.
tmpDiv.className = 'DHTMLSuite_panel';	// Assigning it to class DHTMLSuite_panel
var panelTitle = this.arrayOfPanes[no][1];
var panelDisplayed = this.arrayOfPanes[no][2];
var nameOfCookie = this.arrayOfPanes[no][3];
var widthOfPane = this.arrayOfPanes[no][4];
if(widthOfPane)tmpDiv.style.width = widthOfPane;
var outerContentDiv = document.createElement('DIV');
var contentDiv = tmpDiv.getElementsByTagName('DIV')[0];
contentDiv.className = 'DHTMLSuite_infoPaneContent';
contentDiv.id = 'infoPaneContent' + no;
outerContentDiv.appendChild(contentDiv);
this.cookieNames[this.cookieNames.length] = nameOfCookie;
outerContentDiv.id = 'paneContent' + no;
outerContentDiv.className = 'DHTMLSuite_panelContent';
outerContentDiv.style.backgroundImage = 'url(\'' + DHTMLSuite.configObj.imagePath + 'xp-info-pane-bg_pane_right.gif' + '\')';
var topBar = document.createElement('DIV');
topBar.onselectstart = function() { return DHTMLSuite.commonObj.cancelEvent() };
DHTMLSuite.commonObj.__addEventElement(topBar);
var span = document.createElement('SPAN');
span.innerHTML = panelTitle;
topBar.appendChild(span);
topBar.style.backgroundImage = 'url(\'' + DHTMLSuite.configObj.imagePath + 'xp-info-pane-bg_panel_top_right.gif' + '\')';
window.refToXpPane = this;
topBar.onclick = function(){ window.refToXpPane.__showHidePaneContent(this) };
if(document.all)topBar.ondblclick = function(){ window.refToXpPane.__showHidePaneContent(this) };;
topBar.onmouseover = this.__mouseoverTopbar;	// Adding mouseover effect to heading
topBar.onmouseout = this.__mouseoutTopbar;	// Adding mouseout effect to heading
topBar.style.position = 'relative';	// Relative positioning of heading
var img = document.createElement('IMG');	// Adding arrow image
img.id = 'showHideButton' + no;
img.src = DHTMLSuite.configObj.imagePath + 'xp-info-pane-arrow_up.gif';
topBar.appendChild(img);
if(nameOfCookie){	// Cookie defined?
cookieValue =  DHTMLSuite.commonObj.getCookie(nameOfCookie);
if(cookieValue)panelDisplayed = cookieValue==1?true:false; // Cookie value exists? -> Expand or collapse pane.
}
if(!panelDisplayed){	// Hide pane initially.
outerContentDiv.style.height = '0px';
contentDiv.style.top = 0 - contentDiv.offsetHeight + 'px';
if(document.all)outerContentDiv.style.display='none';
img.src = DHTMLSuite.configObj.imagePath + 'xp-info-pane-arrow_down.gif';
}
topBar.className='DHTMLSuite_infoPaneTopBar';
topBar.id = 'infoPane_topBar' + no;
tmpDiv.appendChild(topBar);
tmpDiv.appendChild(outerContentDiv);
}
}
,
__resizeAndRepositionPane : function(idOfPane)
{
var obj = document.getElementById(idOfPane);
var subDivs = obj.getElementsByTagName('DIV');
for(var no=0;no<subDivs.length;no++){
if(subDivs[no].className=='DHTMLSuite_panelContent'){
subDivs[no].style.overflow = 'auto';
subDivs[no].style.height = '';
var contentDiv = subDivs[no].getElementsByTagName('DIV')[0];
var tmpHeight = subDivs[no].clientHeight;
tmpHeight = subDivs[no].offsetHeight;
subDivs[no].style.height = tmpHeight + 'px';
if(tmpHeight)this.paneHeights[subDivs[no].id] = tmpHeight;
subDivs[no].style.top = '0px';
subDivs[no].style.overflow = 'hidden';
var subSub = subDivs[no].getElementsByTagName('DIV')[0];
subSub.style.top = '0px';
}
}
}
,
__showHidePaneContent : function(inputObj,methodWhenFinished)
{
var img = inputObj.getElementsByTagName('IMG')[0];
var numericId = img.id.replace(/[^0-9]/g,'');
var obj = document.getElementById('paneContent' + numericId);
if(img.src.toLowerCase().indexOf('up')>=0){
this.currentlyExpandedPane = false;
img.src = img.src.replace('up','down');
if(this.xpPanel_slideActive){
obj.style.display='block';
this.xpPanel_currentDirection[obj.id] = (this.xpPanel_slideSpeed*-1);
this.__slidePane((this.xpPanel_slideSpeed*-1), obj.id,methodWhenFinished);
}else{
obj.style.display='none';
}
if(this.cookieNames[numericId])DHTMLSuite.commonObj.setCookie(this.cookieNames[numericId],'0',100000);
}else{
if(inputObj){
if(this.currentlyExpandedPane && this.xpPanel_onlyOneExpandedPane)this.__showHidePaneContent(this.currentlyExpandedPane);
this.currentlyExpandedPane = inputObj;
}
img.src = img.src.replace('down','up');
if(this.xpPanel_slideActive){
if(document.all){
obj.style.display='block';
}
this.xpPanel_currentDirection[obj.id] = this.xpPanel_slideSpeed;
this.__slidePane(this.xpPanel_slideSpeed,obj.id,methodWhenFinished);
}else{
obj.style.display='block';
subDiv = obj.getElementsByTagName('DIV')[0];
obj.style.height = subDiv.offsetHeight + 'px';
}
if(this.cookieNames[numericId])DHTMLSuite.commonObj.setCookie(this.cookieNames[numericId],'1',100000);
}
return true;
}
,
__slidePane : function(slideValue,id,methodWhenFinished)
{
if(slideValue!=this.xpPanel_currentDirection[id]){
return false;
}
var activePane = document.getElementById(id);
if(activePane==this.savedActivePane){
var subDiv = this.savedActiveSub;
}else{
var subDiv = activePane.getElementsByTagName('DIV')[0];
}
this.savedActivePane = activePane;
this.savedActiveSub = subDiv;
var height = activePane.offsetHeight;
var innerHeight = subDiv.offsetHeight;
if(this.paneHeights[activePane.id])innerHeight = this.paneHeights[activePane.id];
height+=slideValue;
if(height<0)height=0;
if(height>innerHeight)height = innerHeight;
if(document.all){
activePane.style.filter = 'alpha(opacity=' + Math.round((height / innerHeight)*100) + ')';
}else{
var opacity = (height / innerHeight);
if(opacity==0)opacity=0.01;
if(opacity==1)opacity = 0.99;
activePane.style.opacity = opacity;
}
window.refToThisInfoPane = this;
if(slideValue<0){
activePane.style.height = height + 'px';
subDiv.style.top = height - innerHeight + 'px';
if(height>0){
setTimeout('window.refToThisInfoPane.__slidePane(' + slideValue + ',"' + id + '","' + methodWhenFinished + '")',10);
}else{
if(document.all)activePane.style.display='none';
if(methodWhenFinished)eval(methodWhenFinished);
}
}else{
subDiv.style.top = height - innerHeight + 'px';
activePane.style.height = height + 'px';
if(height<innerHeight){
setTimeout('window.refToThisInfoPane.__slidePane(' + slideValue + ',"' + id + '","' + methodWhenFinished + '")',10);
}else{
if(methodWhenFinished)eval(methodWhenFinished);
}
}
}
,
__mouseoverTopbar : function()
{
var img = this.getElementsByTagName('IMG')[0];
var src = img.src;
img.src = img.src.replace('.gif','_over.gif');
var span = this.getElementsByTagName('SPAN')[0];
span.style.color='#428EFF';
}
,
__mouseoutTopbar : function()
{
var img = this.getElementsByTagName('IMG')[0];
var src = img.src;
img.src = img.src.replace('_over.gif','.gif');
var span = this.getElementsByTagName('SPAN')[0];
span.style.color='';
}
}
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
setSteps : function(numberOfSteps)
{
this.progressBar_steps = numberOfSteps;
}
,
init : function()
{
document.body.style.width = '100%';
document.body.style.height = '100%';
document.documentElement.style.overflow = 'hidden';
DHTMLSuite.commonObj.loadCSS(this.layoutCSS);
this.__createDivElementsForTheProgressBar();
}
,
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
,
__hideProgressBar : function()
{
document.body.style.width = null;
document.body.style.height = null;
document.documentElement.style.overflow = '';
setTimeout('document.getElementById("DHTMLSuite_progressPane").style.display="none"',50);
}
,
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
DHTMLSuite.menuModelItem = function()
{
var id;					// id of this menu item.
var itemText;			// Text for this menu item
var itemIcon;			// Icon for this menu item.
var url;				// url when click on this menu item
var parentId;			// id of parent element
var separator;			// is this menu item a separator
var jsFunction;			// Js function to call onclick
var depth;				// Depth of this menu item.
var hasSubs;			// Does this menu item have sub items.
var type;				// Menu item type - possible values: "top" or "sub".
var helpText;			// Help text for this item - appear when you move your mouse over the item.
var state;
var submenuWidth;		// Width of sub menu items.
var visible;			// Visibility of menu item.
this.state = 'regular';
}
DHTMLSuite.menuModelItem.prototype = {
setMenuVars : function(id,itemText,itemIcon,url,parentId,helpText,jsFunction,type,submenuWidth)
{
this.id = id;
this.itemText = itemText;
this.itemIcon = itemIcon;
this.url = url;
this.parentId = parentId;
this.jsFunction = jsFunction;
this.separator = false;
this.depth = false;
this.hasSubs = false;
this.helpText = helpText;
this.submenuWidth = submenuWidth;
this.visible = true;
if(!type){
if(this.parentId)this.type = 'top'; else this.type='sub';
}else this.type = type;
}
,
setAsSeparator : function(id,parentId)
{
this.id = id;
this.parentId = parentId;
this.separator = true;
this.visible = true;
if(this.parentId)this.type = 'top'; else this.type='sub';
}
,
setVisibility : function(visible)
{
this.visible = visible;
}
,
getState : function()
{
return this.state;
}
,
setState : function(newState)
{
this.state = newState;
}
,
setSubMenuWidth : function(newWidth)
{
this.submenuWidth = newWidth;
}
,
setIcon : function(iconPath)
{
this.itemIcon = iconPath;
}
,
setText : function(newText)
{
this.itemText = newText;
}
}
DHTMLSuite.menuModel = function()
{
var menuItems;					// Array of menuModelItem objects
var menuItemsOrder;			// This array is needed in order to preserve the correct order of the array above. the array above is associative
var submenuType;				// Direction of menu items(one item for each depth)
var mainMenuGroupWidth;			// Width of menu group - useful if the first group of items are listed below each other
this.menuItems = new Array();
this.menuItemsOrder = new Array();
this.submenuType = new Array();
this.submenuType[1] = 'top';
for(var no=2;no<20;no++){
this.submenuType[no] = 'sub';
}
try{
if(!standardObjectsCreated)DHTMLSuite.createStandardObjects();
}catch(e){
alert('You need to include the dhtmlSuite-common.js file');
}
}
DHTMLSuite.menuModel.prototype = {
addItem : function(id,itemText,itemIcon,url,parentId,helpText,jsFunction,type,submenuWidth)
{
if(!id)id = this.__getUniqueId();	// id not present - create it dynamically.
try{
this.menuItems[id] = new DHTMLSuite.menuModelItem();
}catch(e){
alert('Error: You need to include dhtmlSuite-menuModel.js in your html file');
}
this.menuItems[id].setMenuVars(id,itemText,itemIcon,url,parentId,helpText,jsFunction,type,submenuWidth);
this.menuItemsOrder[this.menuItemsOrder.length] = id;
return this.menuItems[id];
}
,
addItemsFromMarkup : function(ulId)
{
if(!document.getElementById(ulId)){
alert('<UL> tag with id ' + ulId + ' does not exist');
return;
}
var ulObj = document.getElementById(ulId);
var liTags = ulObj.getElementsByTagName('LI');
for(var no=0;no<liTags.length;no++){	// Walking through all <li> tags in the <ul> tree
var id = liTags[no].id.replace(/[^0-9]/gi,'');	// Get id of item.
if(!id)id = this.__getUniqueId();
try{
this.menuItems[id] = new DHTMLSuite.menuModelItem();	// Creating new menuModelItem object
}catch(e){
alert('Error: You need to include dhtmlSuite-menuModel.js in your html file');
}
this.menuItemsOrder[this.menuItemsOrder.length] = id;
var parentId = 0;	// Default parent id
if(liTags[no].parentNode!=ulObj)parentId = liTags[no].parentNode.parentNode.id;	// parent node exists, set parentId equal to id of parent <li>.
var type = liTags[no].getAttribute('itemType');
if(!type)type = liTags[no].itemType;
if(type=='separator'){	// Menu item of type "separator"
this.menuItems[id].setAsSeparator(id,parentId);
continue;
}
if(parentId)type='sub'; else type = 'top';
var aTag = liTags[no].getElementsByTagName('A')[0];	// Get a reference to sub <a> tag
if(!aTag){
continue;
}
if(aTag)var itemText = aTag.innerHTML;	// Item text is set to the innerHTML of the <a> tag.
var itemIcon = liTags[no].getAttribute('itemIcon');	// Item icon is set from the itemIcon attribute of the <li> tag.
var url = aTag.href;	// url is set to the href attribute of the <a> tag
if(url=='#' || url.substr(url.length-1,1)=='#')url='';	// # = empty url.
var jsFunction = liTags[no].getAttribute('jsFunction');	// jsFunction is set from the jsFunction attribute of the <li> tag.
var submenuWidth = false;	// Not set from the <li> tag.
var helpText = aTag.getAttribute('title');
if(!helpText)helpText = aTag.title;
this.menuItems[id].setMenuVars(id,itemText,itemIcon,url,parentId,helpText,jsFunction,type,submenuWidth);
}
var subUls = ulObj.getElementsByTagName('UL');
for(var no=0;no<subUls.length;no++){
var width = subUls[no].getAttribute('width');
if(!width)width = subUls[no].width;
if(width){
var id = subUls[no].parentNode.id.replace(/[^0-9]/gi,'');
this.setSubMenuWidth(id,width);
}
}
ulObj.style.display='none';
}
,
setSubMenuWidth : function(id,newWidth)
{
this.menuItems[id].setSubMenuWidth(newWidth);
}
,
setMainMenuGroupWidth : function(newWidth)
{
this.mainMenuGroupWidth = newWidth;
}
,
addSeparator : function(parentId)
{
id = this.__getUniqueId();	// Get unique id
if(!parentId)parentId = 0;
try{
this.menuItems[id] = new DHTMLSuite.menuModelItem();
}catch(e){
alert('Error: You need to include dhtmlSuite-menuModel.js in your html file');
}
this.menuItems[id].setAsSeparator(id,parentId);
this.menuItemsOrder[this.menuItemsOrder.length] = id;
return this.menuItems[id];
}
,
init : function()
{
this.__getDepths();
this.__setHasSubs();
}
,
setMenuItemVisibility : function(id,visible)
{
this.menuItems[id].setVisibility(visible);
}
,
setSubMenuType : function(depth,newType)
{
this.submenuType[depth] = newType;
}
,
getItems : function(parentId,returnArray)
{
if(!parentId)return this.menuItems;
if(!returnArray)returnArray = new Array();
for(var no=0;no<this.menuItemsOrder.length;no++){
var id = this.menuItemsOrder[no];
if(!id)continue;
if(this.menuItems[id].parentId==parentId){
returnArray[returnArray.length] = this.menuItems[id];
if(this.menuItems[id].hasSubs)return this.getItems(this.menuItems[id].id,returnArray);
}
}
return returnArray;
}
,
__getUniqueId : function()
{
var num = Math.random() + '';
num = num.replace('.','');
num = '99' + num;
num = num /1;
while(this.menuItems[num]){
num = Math.random() + '';
num = num.replace('.','');
num = num /1;
}
return num;
}
,
__getDepths : function()
{
for(var no=0;no<this.menuItemsOrder.length;no++){
var id = this.menuItemsOrder[no];
if(!id)continue;
this.menuItems[id].depth = 1;
if(this.menuItems[id].parentId){
this.menuItems[id].depth = this.menuItems[this.menuItems[id].parentId].depth+1;
}
this.menuItems[id].type = this.submenuType[this.menuItems[id].depth];	// Save menu direction for this menu item.
}
}
,
__setHasSubs : function()
{
for(var no=0;no<this.menuItemsOrder.length;no++){
var id = this.menuItemsOrder[no];
if(!id)continue;
if(this.menuItems[id].parentId){
this.menuItems[this.menuItems[id].parentId].hasSubs = 1;
}
}
}
,
__hasSubs : function(id)
{
for(var no=0;no<this.menuItemsOrder.length;no++){
var id = this.menuItemsOrder[no];
if(!id)continue;
if(this.menuItems[id].parentId==id)return true;
}
return false;
}
,
__deleteChildNodes : function(parentId,recursive)
{
var itemsToDeleteFromOrderArray = new Array();
for(var prop=0;prop<this.menuItemsOrder.length;prop++){
var id = this.menuItemsOrder[prop];
if(!id)continue;
if(this.menuItems[id].parentId==parentId && parentId){
this.menuItems[id] = false;
itemsToDeleteFromOrderArray[itemsToDeleteFromOrderArray.length] = id;
this.__deleteChildNodes(id,true);	// Recursive call.
}
}
if(!recursive){
for(var prop=0;prop<itemsToDeleteFromOrderArray.length;prop++){
if(!itemsToDeleteFromOrderArray[prop])continue;
this.__deleteItemFromItemOrderArray(itemsToDeleteFromOrderArray[prop]);
}
}
this.__setHasSubs();
}
,
__deleteANode : function(id)
{
this.menuItems[id] = false;
this.__deleteItemFromItemOrderArray(id);
}
,
__deleteItemFromItemOrderArray : function(id)
{
for(var no=0;no<this.menuItemsOrder.length;no++){
var tmpId = this.menuItemsOrder[no];
if(!tmpId)continue;
if(this.menuItemsOrder[no]==id){
this.menuItemsOrder.splice(no,1);
return;
}
}
}
,
__appendMenuModel : function(newModel,parentId)
{
if(!newModel)return;
var items = newModel.getItems();
for(var no=0;no<newModel.menuItemsOrder.length;no++){
var id = newModel.menuItemsOrder[no];
if(!id)continue;
if(!items[id].parentId)items[id].parentId = parentId;
this.menuItems[id] = items[id];
for(var no2=0;no2<this.menuItemsOrder.length;no2++){	// Check to see if this item allready exists in the menuItemsOrder array, if it does, remove it.
if(!this.menuItemsOrder[no2])continue;
if(this.menuItemsOrder[no2]==items[id].id){
this.menuItemsOrder.splice(no2,1);
}
}
this.menuItemsOrder[this.menuItemsOrder.length] = items[id].id;
}
this.__getDepths();
this.__setHasSubs();
}
}
DHTMLSuite.menuItem = function()
{
var layoutCSS;
var divElement;							// the <div> element created for this menu item
var expandElement;						// Reference to the arrow div (expand sub items)
var cssPrefix;							// Css prefix for the menu items.
var modelItemRef;						// Reference to menuModelItem
this.layoutCSS = 'menu-item.css';
this.cssPrefix = 'DHTMLSuite_';
try{
if(!standardObjectsCreated)DHTMLSuite.createStandardObjects();
}catch(e){
alert('You need to include the dhtmlSuite-common.js file');
}
var objectIndex;
this.objectIndex = DHTMLSuite.variableStorage.arrayOfDhtmlSuiteObjects.length;
}
DHTMLSuite.menuItem.prototype =
{
createItem : function(menuModelItemObj)
{
DHTMLSuite.commonObj.loadCSS(this.layoutCSS);	// Load css
DHTMLSuite.variableStorage.arrayOfDhtmlSuiteObjects[this.objectIndex] = this;
this.modelItemRef = menuModelItemObj;
this.divElement = document.createElement('DIV');	// Create main div
this.divElement.id = 'DHTMLSuite_menuItem' + menuModelItemObj.id;	// Giving this menu item it's unque id
this.divElement.className = this.cssPrefix + 'menuItem_' + menuModelItemObj.type + '_regular';
this.divElement.onselectstart = function() { return DHTMLSuite.commonObj.cancelEvent() };
if(menuModelItemObj.helpText){	// Add "title" attribute to the div tag if helpText is defined
this.divElement.title = menuModelItemObj.helpText;
}
if(menuModelItemObj.type=='top'){
this.__createMenuElementsOfTypeTop(this.divElement);
}
if(menuModelItemObj.type=='sub'){
this.__createMenuElementsOfTypeSub(this.divElement);
}
if(menuModelItemObj.separator){
this.divElement.className = this.cssPrefix + 'menuItem_separator_' + menuModelItemObj.type;
this.divElement.innerHTML = '<span></span>';
}else{
var tmpVar = this.objectIndex/1;
this.divElement.onclick = function(e) { DHTMLSuite.variableStorage.arrayOfDhtmlSuiteObjects[tmpVar].__navigate(e); }
this.divElement.onmousedown = this.__clickMenuItem;			// on mouse down effect
this.divElement.onmouseup = this.__rolloverMenuItem;		// on mouse up effect
this.divElement.onmouseover = this.__rolloverMenuItem;		// mouse over effect
this.divElement.onmouseout = this.__rolloutMenuItem;		// mouse out effect.
DHTMLSuite.commonObj.__addEventElement(this.divElement);
}
return this.divElement;
}
,
setLayoutCss : function(newLayoutCss)
{
this.layoutCSS = newLayoutCss;
}
,
__createMenuElementsOfTypeTop : function(parentEl){
if(this.modelItemRef.itemIcon){
var iconDiv = document.createElement('DIV');
iconDiv.innerHTML = '<img src="' + this.modelItemRef.itemIcon + '">';
iconDiv.id = 'menuItemIcon' + this.modelItemRef.id
parentEl.appendChild(iconDiv);
}
if(this.modelItemRef.itemText){
var div = document.createElement('DIV');
div.innerHTML = this.modelItemRef.itemText;
div.className = this.cssPrefix + 'menuItem_textContent';
div.id = 'menuItemText' + this.modelItemRef.id;
parentEl.appendChild(div);
}
var div = document.createElement('DIV');
div.className = this.cssPrefix + 'menuItem_top_arrowShowSub';
div.id = 'DHTMLSuite_menuBar_arrow' + this.modelItemRef.id;
parentEl.appendChild(div);
this.expandElement = div;
if(!this.modelItemRef.hasSubs)div.style.display='none';
}
,
__createMenuElementsOfTypeSub : function(parentEl){
if(this.modelItemRef.itemIcon){
parentEl.style.backgroundImage = 'url(\'' + this.modelItemRef.itemIcon + '\')';
parentEl.style.backgroundRepeat = 'no-repeat';
parentEl.style.backgroundPosition = 'left center';
}
if(this.modelItemRef.itemText){
var div = document.createElement('DIV');
div.className = 'DHTMLSuite_textContent';
div.innerHTML = this.modelItemRef.itemText;
div.className = this.cssPrefix + 'menuItem_textContent';
div.id = 'menuItemText' + this.modelItemRef.id;
parentEl.appendChild(div);
}
var div = document.createElement('DIV');
div.className = this.cssPrefix + 'menuItem_sub_arrowShowSub';
parentEl.appendChild(div);
div.id = 'DHTMLSuite_menuBar_arrow' + this.modelItemRef.id;
this.expandElement = div;
if(!this.modelItemRef.hasSubs){
div.style.display='none';
}else{
div.previousSibling.style.paddingRight = '15px';
}
}
,
setCssPrefix : function(cssPrefix)
{
this.cssPrefix = cssPrefix;
}
,
setIcon : function(newPath)
{
this.modelItemRef.setIcon(newPath);
if(this.modelItemRef.type=='top'){	// Menu item is of type "top"
var div = document.getElementById('menuItemIcon' + this.modelItemRef.id);	// Get a reference to the div where the icon is located.
var img = div.getElementsByTagName('IMG')[0];	// Find the image
if(!img){	// Image doesn't exists ?
img = document.createElement('IMG');	// Create new image
div.appendChild(img);
}
img.src = newPath;	// Set image path
if(!newPath)img.parentNode.removeChild(img);	// No newPath defined, remove the image.
}
if(this.modelItemRef.type=='sub'){	// Menu item is of type "sub"
this.divElement.style.backgroundImage = 'url(\'' + newPath + '\')';		// Set backgroundImage for the main div(i.e. menu item div)
}
}
,
setText : function(newText)
{
this.modelItemRef.setText(newText);
document.getElementById('menuItemText' + this.modelItemRef.id).innerHTML = newText;
}
,
__clickMenuItem : function()
{
this.className = this.className.replace('_regular','_click');
this.className = this.className.replace('_over','_click');
}
,
__rolloverMenuItem : function()
{
this.className = this.className.replace('_regular','_over');
this.className = this.className.replace('_click','_over');
}
,
__rolloutMenuItem : function()
{
this.className = this.className.replace('_over','_regular');
}
,
setState : function(newState)
{
this.divElement.className = this.cssPrefix + 'menuItem_' + this.modelItemRef.type + '_' + newState;
this.modelItemRef.setState(newState);
}
,
getState : function()
{
var state = this.modelItemRef.getState();
if(!state){
if(this.divElement.className.indexOf('_over')>=0)state = 'over';
if(this.divElement.className.indexOf('_click')>=0)state = 'click';
this.modelItemRef.setState(state);
}
return state;
}
,
__setHasSub : function(hasSubs)
{
this.modelItemRef.hasSubs = hasSubs;
if(!hasSubs){
document.getElementById(this.cssPrefix +'menuBar_arrow' + this.modelItemRef.id).style.display='none';
}else{
document.getElementById(this.cssPrefix +'menuBar_arrow' + this.modelItemRef.id).style.display='block';
}
}
,
hide : function()
{
this.modelItemRef.setVisibility(false);
this.divElement.style.display='none';
}
,
show : function()
{
this.modelItemRef.setVisibility(true);
this.divElement.style.display='block';
}
,
__hideGroup : function()
{
if(this.modelItemRef.parentId){
this.divElement.parentNode.style.visibility='hidden';
if(DHTMLSuite.clientInfoObj.isMSIE){
try{
var tmpId = this.divElement.parentNode.id.replace(/[^0-9]/gi,'');
document.getElementById('DHTMLSuite_menuBarIframe_' + tmpId).style.visibility = 'hidden';
}catch(e){
}
}
}
}
,
__navigate : function(e)
{
if(document.all)e = event;
if(e){
var srcEl = DHTMLSuite.commonObj.getSrcElement(e);
if(srcEl.id.indexOf('arrow')>=0)return;
}
if(this.modelItemRef.state=='disabled')return;
if(this.modelItemRef.url){
location.href = this.modelItemRef.url;
}
if(this.modelItemRef.jsFunction){
try{
eval(this.modelItemRef.jsFunction);
}catch(e){
alert('Defined Javascript code for the menu item( ' + this.modelItemRef.jsFunction + ' ) cannot be executed');
}
}
}
}
DHTMLSuite.menuBar = function()
{
var menuItemObj;
var layoutCSS;					// Name of css file
var menuBarBackgroundImage;		// Name of background image
var menuItem_objects;			// Array of menu items - html elements.
var menuBarObj;					// Reference to the main dib
var menuBarHeight;
var menuItems;					// Reference to objects of class menuModelItem
var highlightedItems;			// Array of currently highlighted menu items.
var menuBarState;				// Menu bar state - true or false - 1 = expand items on mouse over
var activeSubItemsOnMouseOver;	// Activate sub items on mouse over	(instead of onclick)
var submenuGroups;				// Array of div elements for the sub menus
var submenuIframes;				// Array of sub menu iframes used to cover select boxes in old IE browsers.
var createIframesForOldIeBrowsers;	// true if we want the script to create iframes in order to cover select boxes in older ie browsers.
var targetId;					// Id of element where the menu will be inserted.
var menuItemCssPrefix;			// Css prefix of menu items.
var cssPrefix;					// Css prefix for the menu bar
var menuItemLayoutCss;			// Css path for the menu items of this menu bar
var objectIndex;			// Global index of this object - used to refer to the object of this class outside
this.cssPrefix = 'DHTMLSuite_';
this.menuItemLayoutCss = false;	// false = use default for the menuItem class.
this.layoutCSS = 'menu-bar.css';
this.menuBarBackgroundImage = 'menu_strip_bg.jpg';
this.menuItem_objects = new Array();
DHTMLSuite.variableStorage.menuBar_highlightedItems = new Array();
this.menuBarState = false;
this.menuBarObj = false;
this.menuBarHeight = 26;
this.submenuGroups = new Array();
this.submenuIframes = new Array();
this.targetId = false;
this.activeSubItemsOnMouseOver = false;
this.menuItemCssPrefix = false;
this.createIframesForOldIeBrowsers = true;
try{
if(!standardObjectsCreated)DHTMLSuite.createStandardObjects();
}catch(e){
alert('You need to include the dhtmlSuite-common.js file');
}
this.objectIndex = DHTMLSuite.variableStorage.arrayOfDhtmlSuiteObjects.length;;
DHTMLSuite.variableStorage.arrayOfDhtmlSuiteObjects[this.objectIndex] = this;
}
DHTMLSuite.menuBar.prototype = {
init : function()
{
DHTMLSuite.commonObj.loadCSS(this.layoutCSS);
this.__createMainMenuDiv();	// Create general divs
this.__createMenuItems();	// Create menu items
this.__setBasicEvents();	// Set basic events.
window.refToThismenuBar = this;
}
,
setTarget : function(idOfHTMLElement)
{
this.targetId = idOfHTMLElement;
}
,
setLayoutCss : function(nameOfNewCssFile)
{
this.layoutCSS = nameOfNewCssFile;
}
,
setMenuItemLayoutCss : function(nameOfNewCssFile)
{
this.menuItemLayoutCss = nameOfNewCssFile;
}
,
setCreateIframesForOldIeBrowsers : function(createIframesForOldIeBrowsers)
{
this.createIframesForOldIeBrowsers = createIframesForOldIeBrowsers;
}
,
addMenuItems : function(menuItemObj)
{
this.menuItemObj = menuItemObj;
this.menuItems = menuItemObj.getItems();
}
,
setActiveSubItemsOnMouseOver : function(activateSubOnMouseOver)
{
this.activeSubItemsOnMouseOver = activateSubOnMouseOver;
}
,
setMenuItemState : function(menuItemId,state)
{
this.menuItem_objects[menuItemId].setState(state);
}
,
setMenuItemCssPrefix : function(newCssPrefix)
{
this.menuItemCssPrefix = newCssPrefix;
}
,
setCssPrefix : function(newCssPrefix)
{
this.cssPrefix = newCssPrefix;
}
,
replaceMenuItems : function(idOfParentMenuItem,newMenuModel)
{
this.hideSubMenus();	// Hide all sub menus
this.__deleteMenuItems(idOfParentMenuItem);	// Delete old menu items.
this.menuItemObj.__appendMenuModel(newMenuModel,idOfParentMenuItem);	// Appending new menu items to the menu model.
this.__clearAllMenuItems();
this.__createMenuItems();
}
,
deleteMenuItems : function(idOfParentMenuItem,deleteParentElement)
{
this.hideSubMenus();	// Hide all sub menus
this.__deleteMenuItems(idOfParentMenuItem,deleteParentElement);
this.__clearAllMenuItems();
this.__createMenuItems();
}
,
appendMenuItems : function(idOfParentMenuItem,newMenuModel)
{
this.hideSubMenus();	// Hide all sub menus
this.menuItemObj.__appendMenuModel(newMenuModel,idOfParentMenuItem);	// Appending new menu items to the menu model.
this.__clearAllMenuItems();
this.__createMenuItems();
}
,
hideMenuItem : function(menuItemId)
{
this.menuItem_objects[menuItemId].hide();
}
,
showMenuItem : function(menuItemId)
{
this.menuItem_objects[menuItemId].show();
}
,
setText : function(menuItemId,newText)
{
this.menuItem_objects[menuItemId].setText(newText);
}
,
setIcon : function(menuItemId,newPath)
{
this.menuItem_objects[menuItemId].setIcon(newPath);
}
,
__clearAllMenuItems : function()
{
for(var prop=0;prop<this.menuItemObj.menuItemsOrder.length;prop++){
var id = this.menuItemObj.menuItemsOrder[prop];
if(!id)continue;
if(this.submenuGroups[id]){
this.submenuGroups[id].parentNode.removeChild(this.submenuGroups[id]);
this.submenuGroups[id] = false;
}
if(this.submenuIframes[id]){
this.submenuIframes[id].parentNode.removeChild(this.submenuIframes[id]);
this.submenuIframes[id] = false;
}
}
this.menuBarObj.innerHTML = '';
}
,
__deleteMenuItems : function(idOfParentMenuItem,includeParent)
{
if(includeParent)this.menuItemObj.__deleteANode(idOfParentMenuItem);
if(!this.submenuGroups[idOfParentMenuItem])return;	// No sub items exists.
this.menuItem_objects[idOfParentMenuItem].__setHasSub(false);	// Delete existing sub menu divs.
this.menuItemObj.__deleteChildNodes(idOfParentMenuItem);	// Delete existing child nodes from menu model
var groupBox = this.submenuGroups[idOfParentMenuItem];
groupBox.parentNode.removeChild(groupBox);	// Delete sub menu group box.
if(this.submenuIframes[idOfParentMenuItem]){
this.submenuIframes[idOfParentMenuItem].parentNode.removeChild(this.submenuIframes[idOfParentMenuItem]);
}
this.submenuGroups.splice(idOfParentMenuItem,1);
this.submenuIframes.splice(idOfParentMenuItem,1);
}
,
__changeMenuBarState : function(){
var objectIndex = this.getAttribute('objectRef');
var obj = DHTMLSuite.variableStorage.arrayOfDhtmlSuiteObjects[objectIndex];
var parentId = this.id.replace(/[^0-9]/gi,'');
var state = obj.menuItem_objects[parentId].getState();
if(state=='disabled')return;
obj.menuBarState = !obj.menuBarState;
if(!obj.menuBarState)obj.hideSubMenus();else{
obj.hideSubMenus();
setTimeout('DHTMLSuite.variableStorage.arrayOfDhtmlSuiteObjects[' + objectIndex + '].__expandGroup(' + parentId+ ')',10);
}
}
,
__createMainMenuDiv : function()
{
window.refTomenuBar = this;	// Reference to menu strip object
this.menuBarObj = document.createElement('DIV');
this.menuBarObj.className = this.cssPrefix + 'menuBar_' + this.menuItemObj.submenuType[1];
if(!document.getElementById(this.targetId)){
alert('No target defined for the menu object');
return;
}
var target = document.getElementById(this.targetId);
target.appendChild(this.menuBarObj);
}
,
hideSubMenus : function(e)
{
if(this && this.tagName){	/* Method called from event */
if(document.all)e = event;
var srcEl = DHTMLSuite.commonObj.getSrcElement(e);
if(srcEl.tagName.toLowerCase()=='img')srcEl = srcEl.parentNode;
if(srcEl.className && srcEl.className.indexOf('arrow')>=0){
return;
}
}
for(var no=0;no<DHTMLSuite.variableStorage.menuBar_highlightedItems.length;no++){
DHTMLSuite.variableStorage.menuBar_highlightedItems[no].setState('regular');	// Set state back to regular
DHTMLSuite.variableStorage.menuBar_highlightedItems[no].__hideGroup();	// Hide eventual sub menus
}
DHTMLSuite.variableStorage.menuBar_highlightedItems = new Array();
}
,
__hideSubMenusAfterSmallDelay : function()
{
var ind = this.objectIndex;
setTimeout('DHTMLSuite.variableStorage.arrayOfDhtmlSuiteObjects[' + ind + '].hideSubMenus()',15);
}
,
__expandGroup : function(idOfParentMenuItem)
{
var groupRef = this.submenuGroups[idOfParentMenuItem];
var subDiv = groupRef.getElementsByTagName('DIV')[0];
var numericId = subDiv.id.replace(/[^0-9]/g,'');
groupRef.style.visibility='visible';	// Show menu group.
if(this.submenuIframes[idOfParentMenuItem])this.submenuIframes[idOfParentMenuItem].style.visibility = 'visible';	// Show iframe if it exists.
DHTMLSuite.variableStorage.menuBar_highlightedItems[DHTMLSuite.variableStorage.menuBar_highlightedItems.length] = this.menuItem_objects[numericId];
this.__positionSubMenu(idOfParentMenuItem);
if(DHTMLSuite.clientInfoObj.isOpera){	/* Opera fix in order to get correct height of sub menu group */
var subDiv = groupRef.getElementsByTagName('DIV')[0];	/* Find first menu item */
subDiv.className = subDiv.className.replace('_over','_over');	/* By "touching" the class of the menu item, we are able to fix a layout problem in Opera */
}
}
,
__activateMenuElements : function(parentMenuItemObject,menuBarObjectRef,firstIteration)
{
if(!this.menuBarState && !this.activeSubItemsOnMouseOver)return;	// Menu is not activated and it shouldn't be activated on mouse over.
var numericId = parentMenuItemObject.id.replace(/[^0-9]/g,'');	// Get a numeric reference to current menu item.
var state = this.menuItem_objects[numericId].getState();	// Get state of this menu item.
if(state=='disabled')return;	// This menu item is disabled - return from function without doing anything.
if(firstIteration && DHTMLSuite.variableStorage.menuBar_highlightedItems.length>0){
this.hideSubMenus();	// First iteration of this function=> Hide other sub menus.
}
var newState = 'over';
if(!firstIteration)newState = 'active';	// State should be set to 'over' for the menu item the mouse is currently over.
this.menuItem_objects[numericId].setState(newState);	// Switch state of menu item.
if(this.submenuGroups[numericId]){	// Sub menu group exists. call the __expandGroup method.
this.__expandGroup(numericId);	// Expand sub menu group
}
DHTMLSuite.variableStorage.menuBar_highlightedItems[DHTMLSuite.variableStorage.menuBar_highlightedItems.length] = this.menuItem_objects[numericId];	// Save this menu item in the array of highlighted elements.
if(menuBarObjectRef.menuItems[numericId].parentId){	// A parent element exists. Call this method over again with parent element as input argument.
this.__activateMenuElements(menuBarObjectRef.menuItem_objects[menuBarObjectRef.menuItems[numericId].parentId].divElement,menuBarObjectRef,false);
}
}
,
__createMenuItems : function()
{
var index = this.objectIndex;
var firstChild = false;
var firstChilds = document.getElementsByTagName('DIV');
if(firstChilds.length>0)firstChild = firstChilds[0]
for(var no=0;no<this.menuItemObj.menuItemsOrder.length;no++){	// Looping through menu items
var indexThis = this.menuItemObj.menuItemsOrder[no];
if(!this.menuItems[indexThis].id)continue;
try{
this.menuItem_objects[this.menuItems[indexThis].id] = new DHTMLSuite.menuItem();
}catch(e){
alert('Error: You need to include dhtmlSuite-menuItem.js in your html file');
}
if(this.menuItemCssPrefix)this.menuItem_objects[this.menuItems[indexThis].id].setCssPrefix(this.menuItemCssPrefix);	// Custom css prefix set
if(this.menuItemLayoutCss)this.menuItem_objects[this.menuItems[indexThis].id].setLayoutCss(this.menuItemLayoutCss);	// Custom css file name
var ref = this.menuItem_objects[this.menuItems[indexThis].id].createItem(this.menuItems[indexThis]); // Create div for this menu item.
if(!this.menuItems[indexThis].separator)DHTMLSuite.commonObj.addEvent(ref,"mouseover",function(){ DHTMLSuite.variableStorage.arrayOfDhtmlSuiteObjects[index].__activateMenuElements(this,DHTMLSuite.variableStorage.arrayOfDhtmlSuiteObjects[index],true); });
if(!this.menuItems[indexThis].jsFunction && !this.menuItems[indexThis].url){
ref.setAttribute('objectRef',index);	/* saving the index of this object in the DHTMLSuite.variableStorage array as a property of the tag - We need to do this in order to avoid circular references and thus memory leakage in IE */
ref.onclick = this.__changeMenuBarState;
DHTMLSuite.commonObj.__addEventElement(ref);
}
if(this.menuItem_objects[this.menuItems[indexThis].id].expandElement){	/* Small arrow at the right of the menu item exists - expand subs */
var expandRef = this.menuItem_objects[this.menuItems[indexThis].id].expandElement;	/* Creating reference to expand div/arrow div */
var parentId = DHTMLSuite.variableStorage.arrayOfDhtmlSuiteObjects[index].menuItems[indexThis].parentId + '';	// Get parent id.
var tmpId = expandRef.id.replace(/[^0-9]/gi,'');
expandRef.setAttribute('objectRef',index);	/* saving the index of this object in the DHTMLSuite.variableStorage array as a property of the tag - We need to do this in order to avoid circular references and thus memory leakage in IE */
expandRef.objectRef = index;
if(this.menuItems[indexThis].jsFunction || this.menuItems[indexThis].url){
expandRef.onclick = this.__changeMenuBarState;
DHTMLSuite.commonObj.__addEventElement(expandRef);
}
}
var target = this.menuBarObj;	// Temporary variable - target of newly created menu item. target can be the main menu object or a sub menu group(see below where target is updated).
if(this.menuItems[indexThis].depth==1 && this.menuItemObj.submenuType[this.menuItems[indexThis].depth]!='top' && this.menuItemObj.mainMenuGroupWidth){	/* Main menu item group width set */
var tmpWidth = this.menuItemObj.mainMenuGroupWidth + '';
if(tmpWidth.indexOf('%')==-1)tmpWidth = tmpWidth + 'px';
target.style.width = tmpWidth;
}
if(this.menuItems[indexThis].depth=='1'){	/* Top level item */
if(this.menuItemObj.submenuType[this.menuItems[indexThis].depth]=='top'){	/* Type = "top" - menu items side by side */
ref.style.styleFloat = 'left';
ref.style.cssText = 'float:left';
}
}else{
if(!this.menuItems[indexThis].depth){
alert('Error in menu model(depth not defined for a menu item). Remember to call the init() method for the menuModel object.');
return;
}
if(!this.submenuGroups[this.menuItems[indexThis].parentId]){	// Sub menu div doesn't exist - > Create it.
this.submenuGroups[this.menuItems[indexThis].parentId] = document.createElement('DIV');
this.submenuGroups[this.menuItems[indexThis].parentId].style.zIndex = 30000;
this.submenuGroups[this.menuItems[indexThis].parentId].style.position = 'absolute';
this.submenuGroups[this.menuItems[indexThis].parentId].id = 'DHTMLSuite_menuBarSubGroup' + this.menuItems[indexThis].parentId;
this.submenuGroups[this.menuItems[indexThis].parentId].style.visibility = 'hidden';	// Initially hidden.
this.submenuGroups[this.menuItems[indexThis].parentId].className = this.cssPrefix + 'menuBar_' + this.menuItemObj.submenuType[this.menuItems[indexThis].depth];
if(firstChild){
firstChild.parentNode.insertBefore(this.submenuGroups[this.menuItems[indexThis].parentId],firstChild);
}else{
document.body.appendChild(this.submenuGroups[this.menuItems[indexThis].parentId]);
}
if(DHTMLSuite.clientInfoObj.isMSIE && this.createIframesForOldIeBrowsers){	// Create iframe object in order to conver select boxes in older IE browsers(windows).
this.submenuIframes[this.menuItems[indexThis].parentId] = document.createElement('<IFRAME src="about:blank" frameborder=0>');
this.submenuIframes[this.menuItems[indexThis].parentId].id = 'DHTMLSuite_menuBarIframe_' + this.menuItems[indexThis].parentId;
this.submenuIframes[this.menuItems[indexThis].parentId].style.position = 'absolute';
this.submenuIframes[this.menuItems[indexThis].parentId].style.zIndex = 9000;
this.submenuIframes[this.menuItems[indexThis].parentId].style.visibility = 'hidden';
if(firstChild){
firstChild.parentNode.insertBefore(this.submenuIframes[this.menuItems[indexThis].parentId],firstChild);
}else{
document.body.appendChild(this.submenuIframes[this.menuItems[indexThis].parentId]);
}
}
}
target = this.submenuGroups[this.menuItems[indexThis].parentId];	// Change target of newly created menu item. It should be appended to the sub menu div("A group box").
}
target.appendChild(ref); // Append menu item to the document.
if(this.menuItems[indexThis].visible == false)this.hideMenuItem(this.menuItems[indexThis].id);	// Menu item hidden, call the hideMenuItem method.
if(this.menuItems[indexThis].state != 'regular')this.menuItem_objects[this.menuItems[indexThis].id].setState(this.menuItems[indexThis].state);	// Menu item hidden, call the hideMenuItem method.
}
this.__setSizeOfAllSubMenus();	// Set size of all sub menu groups
this.__positionAllSubMenus();	// Position all sub menu groups.
if(DHTMLSuite.clientInfoObj.isOpera)this.__fixMenuLayoutForOperaBrowser();	// Call a function which fixes some layout issues in Opera.
}
,
__fixMenuLayoutForOperaBrowser : function()
{
for(var no=0;no<this.menuItemObj.menuItemsOrder.length;no++){
var id = this.menuItemObj.menuItemsOrder[no];
if(!id)continue;
this.menuItem_objects[id].divElement.className = this.menuItem_objects[id].divElement.className.replace('_regular','_regular');	// Nothing is done but by "touching" the class of the menu items in Opera, we make them appear correctly
}
}
,
__setSizeOfAllSubMenus : function()
{
for(var no=0;no<this.menuItemObj.menuItemsOrder.length;no++){
var prop = this.menuItemObj.menuItemsOrder[no];
if(!prop)continue;
this.__setSizeOfSubMenus(prop);
}
}
,
__positionAllSubMenus : function()
{
for(var no=0;no<this.menuItemObj.menuItemsOrder.length;no++){
var prop = this.menuItemObj.menuItemsOrder[no];
if(!prop)continue;
if(this.submenuGroups[prop])this.__positionSubMenu(prop);
}
}
,
__positionSubMenu : function(parentId)
{
try{
var shortRef = this.submenuGroups[parentId];
var depth = this.menuItems[parentId].depth;
var dir = this.menuItemObj.submenuType[depth];
if(dir=='top'){
shortRef.style.left = DHTMLSuite.commonObj.getLeftPos(this.menuItem_objects[parentId].divElement) + 'px';
shortRef.style.top = (DHTMLSuite.commonObj.getTopPos(this.menuItem_objects[parentId].divElement) + this.menuItem_objects[parentId].divElement.offsetHeight) + 'px';
}else{
shortRef.style.left = (DHTMLSuite.commonObj.getLeftPos(this.menuItem_objects[parentId].divElement) + this.menuItem_objects[parentId].divElement.offsetWidth) + 'px';
shortRef.style.top = (DHTMLSuite.commonObj.getTopPos(this.menuItem_objects[parentId].divElement)) + 'px';
}
if(DHTMLSuite.clientInfoObj.isMSIE){
var iframeRef = this.submenuIframes[parentId]
iframeRef.style.left = shortRef.style.left;
iframeRef.style.top = shortRef.style.top;
iframeRef.style.width = shortRef.clientWidth + 'px';
iframeRef.style.height = shortRef.clientHeight + 'px';
}
}catch(e){
}
}
,
__setSizeOfSubMenus : function(parentId)
{
try{
var shortRef = this.submenuGroups[parentId];
var subWidth = Math.max(shortRef.offsetWidth,this.menuItem_objects[parentId].divElement.offsetWidth);
if(this.menuItems[parentId].submenuWidth)subWidth = this.menuItems[parentId].submenuWidth;
if(subWidth>400)subWidth = 150;	// Hack for IE 6 -> force a small width when width is too large.
subWidth = subWidth + '';
if(subWidth.indexOf('%')==-1)subWidth = subWidth + 'px';
shortRef.style.width = subWidth;
if(DHTMLSuite.clientInfoObj.isMSIE){
this.submenuIframes[parentId].style.width = shortRef.style.width;
this.submenuIFrames[parentId].style.height = shortRef.style.height;
}
}catch(e){
}
}
,
__repositionMenu : function(inputObj)
{
inputObj.menuBarObj.style.top = document.documentElement.scrollTop + 'px';
}
,
__menuItemRollOver : function(menuItemHTMLElementRef)
{
var numericId = menuItemHTMLElementRef.id.replace(/[^0-9]/g,'');
menuItemHTMLElementRef.className = 'DHTMLSuite_menuBar_menuItem_over_' + this.menuItems[numericId]['depth'];
}
,
__menuItemRollOut : function(menuItemHTMLElementRef)
{
var numericId = menuItemHTMLElementRef.id.replace(/[^0-9]/g,'');
menuItemHTMLElementRef.className = 'DHTMLSuite_menuBar_menuItem_' + this.menuItems[numericId]['depth'];
}
,
__menuNavigate : function(menuItemHTMLElementRef)
{
var numericIndex = menuItemHTMLElementRef.id.replace(/[^0-9]/g,'');
var url = this.menuItems[numericIndex]['url'];
if(!url)return;
}
,
__setBasicEvents : function()
{
var ind = this.objectIndex;
DHTMLSuite.commonObj.addEvent(document.documentElement,"click",this.hideSubMenus);
DHTMLSuite.commonObj.addEvent(document.documentElement,"mouseup",this.hideSubMenus);
}
}
DHTMLSuite.paneSplitterModel = function()
{
var panes;		// Array of paneSplitterPaneModel objects
this.panes = new Array();
try{
if(!standardObjectsCreated)DHTMLSuite.createStandardObjects();
}catch(e){
alert('You need to include the dhtmlSuite-common.js file');
}
}
DHTMLSuite.paneSplitterModel.prototype =
{
addPane : function(paneModelRef)
{
this.panes[this.panes.length] = paneModelRef;
}
,
getItems : function()
{
return this.panes;
}
}
DHTMLSuite.paneSplitterPaneModel = function(inputArray)
{
var id;					// Unique id of pane, in case you want to perform operations on this particular pane.
var position;			// Position, possible values: "West","East","North","South" and "Center"
var size;				// Current size of pane(for west and east, the size is equal to width, for south and north, size is equal to height)
var minSize;			// Minimum size(height or width) of the pane
var maxSize;			// Maximum size(height or width) of the pane.
var resizable;			// Boolean - true or false, is the pane resizable
var visible;			// Boolean - true or false, is the pane visible?
var scrollbars;			// Boolean - true or false, visibility of scrollbars when content size is bigger than visible area(default = true)
var contents;			// Array of paneSplitterContentModel objects
var collapsable;		// Boolean - true or false, is this pane collapsable
var state;				// State of a pane, possible values, "expanded","collapsed"; (default = expanded)
var callbackOnCollapse;	// Call back function - if ony function name is give, a reference to this model will be passed to the function with that name
var callbackOnHide;	// Call back function - if ony function name is give, a reference to this model will be passed to the function with that name
var callbackOnShow;		// Call back function - if ony function name is give, a reference to this model will be passed to the function with that name
var callbackOnExpand;	// Call back function - if ony function name is give, a reference to this model will be passed to the function with that name
var callbackOnSlideOut;	// Call back function - if ony function name is give, a reference to this model will be passed to the function with that name
var callbackOnSlideIn;	// Call back function - if ony function name is give, a reference to this model will be passed to the function with that name
var callbackOnCloseContent;	// Call back function - if ony function name is give, a reference to this model will be passed to the function with that name
var callbackOnBeforeCloseContent;	// Call back function - if ony function name is give, a reference to this model will be passed to the function with that name
var callbackOnTabSwitch;	// Call back function - if ony function name is give, a reference to this model will be passed to the function with that name
var callbackOnResize;	// Call back function - called whenever a tab has been resized manually by dragging the "handle".
this.contents = new Array();
this.scrollbars = true;
this.resizable = true;
this.collapsable = true;
this.state = 'expanded';
this.visible = true;
if(inputArray)this.setData(inputArray);
}
DHTMLSuite.paneSplitterPaneModel.prototype =
{
setData : function(inputArray)
{
if(inputArray["id"])this.id = inputArray["id"];
if(inputArray["position"])this.position = inputArray["position"];
if(inputArray["resizable"]===false || inputArray["resizable"]===true)this.resizable = inputArray["resizable"];
if(inputArray["size"])this.size = inputArray["size"];
if(inputArray["minSize"])this.minSize = inputArray["minSize"];
if(inputArray["maxSize"])this.maxSize = inputArray["maxSize"];
if(inputArray["visible"]===false || inputArray["visible"]===true)this.visible = inputArray["visible"];
if(inputArray["collapsable"]===false || inputArray["collapsable"]===true)this.collapsable = inputArray["collapsable"];
if(inputArray["scrollbars"]===false || inputArray["scrollbars"]===true)this.scrollbars = inputArray["scrollbars"];
if(inputArray["state"])this.state = inputArray["state"];
if(inputArray["callbackOnCollapse"])this.callbackOnCollapse = inputArray["callbackOnCollapse"];
if(inputArray["callbackOnHide"])this.callbackOnHide = inputArray["callbackOnHide"];
if(inputArray["callbackOnShow"])this.callbackOnShow = inputArray["callbackOnShow"];
if(inputArray["callbackOnExpand"])this.callbackOnExpand = inputArray["callbackOnExpand"];
if(inputArray["callbackOnSlideIn"])this.callbackOnSlideIn = inputArray["callbackOnSlideIn"];
if(inputArray["callbackOnSlideOut"])this.callbackOnSlideOut = inputArray["callbackOnSlideOut"];
if(inputArray["callbackOnCloseContent"])this.callbackOnCloseContent = inputArray["callbackOnCloseContent"];
if(inputArray["callbackOnBeforeCloseContent"])this.callbackOnBeforeCloseContent = inputArray["callbackOnBeforeCloseContent"];
if(inputArray["callbackOnTabSwitch"])this.callbackOnTabSwitch = inputArray["callbackOnTabSwitch"];
if(inputArray["callbackOnResize"])this.callbackOnResize = inputArray["callbackOnResize"];
}
,
setSize : function(newSizeInPixels)
{
this.size = newSizeInPixels;
}
,
addContent : function(paneSplitterContentObj)
{
for(var no=0;no<this.contents.length;no++){
if(this.contents[no].id==paneSplitterContentObj.id)return false;
}
this.contents[this.contents.length] = paneSplitterContentObj;	// Add content to the array of content objects.
return true;
}
,
getContents : function()
{
return this.contents;
}
,
getCountContent : function()
{
return this.contents.length;
}
,
getPosition : function()
{
return this.position.toLowerCase();
}
,
__setState : function(state)
{
this.state = state;
}
,
__getState : function(state)
{
return this.state;
}
,
__deleteContent : function(indexOfContentObjectToDelete)
{
try{
this.contents.splice(indexOfContentObjectToDelete,1);
}catch(e)
{
}
var retVal = indexOfContentObjectToDelete;
if(this.contents.length>(indexOfContentObjectToDelete-1))retVal--;
if(retVal<0 && this.contents.length==0)return false;
if(retVal<0)retVal=0;
return retVal;
}
,
__getIndexById : function(id)
{
for(var no=0;no<this.contents.length;no++){
if(this.contents[no].id==id)return no;
}
return false;
}
,
__setVisible : function(visible)
{
this.visible = visible;
}
}
DHTMLSuite.paneSplitterContentModel = function(inputArray)
{
var id;					// Unique id of pane, in case you want to perform operations on this particular pane.
var htmlElementId;		// Id of element on the page - if present, the content of this pane will be set to the content of this element
var title;				// Title of pane
var tabTitle;			// If more than one pane is present at this position, what's the tab title of this one.
var closable;			// Boolean - true or false, should it be possible to close this pane
var contentUrl;			// Url to content - used in case you want the script to fetch content from the server. the path is relative to your html page.
this.closable = true;	// Default value
var refreshAfterSeconds;
var displayRefreshButton;
this.displayRefreshButton = false;
this.refreshAfterSeconds = 0;
if(inputArray)this.setData(inputArray);	// Input array present, call the setData method.
}
DHTMLSuite.paneSplitterContentModel.prototype =
{
setData : function(inputArray)
{
if(inputArray["id"])this.id = inputArray["id"]; else this.id = inputArray['htmlElementId'];
if(inputArray["closable"]===false || inputArray["closable"]===true)this.closable = inputArray["closable"];
if(inputArray["displayRefreshButton"]===false || inputArray["displayRefreshButton"]===true)this.displayRefreshButton = inputArray["displayRefreshButton"];
if(inputArray["title"])this.title = inputArray["title"];
if(inputArray["tabTitle"])this.tabTitle = inputArray["tabTitle"];
if(inputArray["contentUrl"])this.contentUrl = inputArray["contentUrl"];
if(inputArray["htmlElementId"])this.htmlElementId = inputArray["htmlElementId"];
if(inputArray["refreshAfterSeconds"])this.refreshAfterSeconds = inputArray["refreshAfterSeconds"];
}
,
__setTitle : function(newTitle)
{
this.title = newTitle;
}
,
__setTabTitle : function(newTabTitle)
{
this.tabTitle = newTabTitle;
}
,
__setIdOfContentElement : function(htmlElementId)
{
this.htmlElementId = htmlElementId;
}
,
__setRefreshAfterSeconds : function(refreshAfterSeconds)
{
this.refreshAfterSeconds = refreshAfterSeconds;
}
,
__setContentUrl : function(contentUrl)
{
this.contentUrl = contentUrl;
}
,
__getClosable : function()
{
return this.closable;
}
}
DHTMLSuite.paneSplitterPane = function(parentRef)
{
var divElement;		// Reference to a div element for the content
var divElementCollapsed;	// Reference to the div element for the content ( collapsed state )
var divElementCollapsedInner;	// Reference to the div element for the content ( collapsed state )
var contentDiv;		// Div for the content
var headerDiv;		// Reference to the header div
var titleSpan;		// Reference to the <span> tag for the title
var paneModel;		// An array of paneSplitterPaneView objects
var resizeDiv;		// Div for the resize handle
var tabDiv;			// Div for the tabs
var divTransparentForResize;	// This transparent div is used to cover iframes when resize is in progress.
var parentRef;		// Reference to paneSplitter object
var divClose;		// Reference to close button
var divCollapse;	// Reference to collapse button
var divExpand;		// Reference to expand button
var divRefresh;		// Reference to refresh button
var slideIsInProgress;		// Internal variable used by the script to determine if slide is in progress or not
var zIndexCounter;			// Incremental value used when setting z-indexes.
var reloadIntervalHandlers;	// Array of setInterval objects, one for each content of this pane
var contentScrollTopPositions;	// Array of contents scroll top positions in pixels.
this.contents = new Array();
this.reloadIntervalHandlers = new Array();
this.contentScrollTopPositions = new Array();
this.parentRef = parentRef;
var activeContentIndex;	// Index of active content(default = 0)
this.activeContentIndex = false;
this.slideIsInProgress = false;
var objectIndex;			// Index of this object in the variableStorage array
this.zIndexCounter = 1;
this.objectIndex = DHTMLSuite.variableStorage.arrayOfDhtmlSuiteObjects.length;
DHTMLSuite.variableStorage.arrayOfDhtmlSuiteObjects[this.objectIndex] = this;
}
DHTMLSuite.paneSplitterPane.prototype =
{
addDataSource : function(paneModelRef)
{
this.paneModel = paneModelRef;
}
,
addContent : function(paneContentModelObject,jsCodeToExecuteWhenComplete)
{
var retValue = this.paneModel.addContent(paneContentModelObject);
if(!retValue)return false;	// Content already exists - return from this method.
this.__addOneContentDiv(paneContentModelObject,jsCodeToExecuteWhenComplete);
this.__updateTabContent();
this.__updateView();
if(this.paneModel.getCountContent()==1)this.showContent(paneContentModelObject.id);
return retValue
}
,
showContent : function(idOfContentObject)
{
for(var no=0;no<this.paneModel.contents.length;no++){
if(this.paneModel.contents[no].id==idOfContentObject){
this.__updatePaneView(no);
return;
}
}
}
,
loadContent : function(idOfContentObject,url,refreshAfterSeconds,internalCall,onCompleteJsCode)
{
if(!url)return;
for(var no=0;no<this.paneModel.contents.length;no++){
if(this.paneModel.contents[no].id==idOfContentObject){
if(internalCall && !this.paneModel.contents[no].refreshAfterSeconds)return;	// Refresh rate has been cleared - no reload.
var ajaxWaitMessage = this.parentRef.waitMessage;	// Please wait message
this.paneModel.contents[no].__setContentUrl(url);
if(refreshAfterSeconds && !internalCall){
this.paneModel.contents[no].__setRefreshAfterSeconds(refreshAfterSeconds);
}
if(refreshAfterSeconds)this.__handleContentReload(idOfContentObject,refreshAfterSeconds);
try{
var dynContent = new DHTMLSuite.dynamicContent();
}catch(e){
alert('You need to include dhtmlSuite-dynamicContent.js');
}
dynContent.setWaitMessage(ajaxWaitMessage);
dynContent.loadContent(this.paneModel.contents[no].htmlElementId,url,onCompleteJsCode);
dynContent = false;
return;
}
}
}
,
isUrlLoadedInPane : function(idOfContentObject,url)
{
var contentIndex = this.paneModel.__getIndexById(idOfContentObject);
if(contentIndex!==false){
if(this.paneModel.contents[contentIndex].contentUrl==url)return true;
}
return false;
}
,
reloadContent : function(idOfContentObject)
{
var contentIndex = this.paneModel.__getIndexById(idOfContentObject);
if(contentIndex!==false){
this.loadContent(idOfContentObject,this.paneModel.contents[contentIndex].contentUrl);
}
}
,
setRefreshAfterSeconds : function(idOfContentObject,refreshAfterSeconds)
{
for(var no=0;no<this.paneModel.contents.length;no++){
if(this.paneModel.contents[no].id==idOfContentObject){
if(!this.paneModel.contents[no].refreshAfterSeconds){
this.loadContent(idOfContentObject,this.paneModel.contents[no].contentUrl,refreshAfterSeconds);
}
this.paneModel.contents[no].__setRefreshAfterSeconds(refreshAfterSeconds);
this.__handleContentReload(idOfContentObject);
}
}
}
,
setContentTitle : function(idOfContent, newTitle)
{
var contentModelIndex = this.paneModel.__getIndexById(idOfContent);	// Get a reference to the content object
if(contentModelIndex!==false){
var contentModelObj = this.paneModel.contents[contentModelIndex];
contentModelObj.__setTitle(newTitle);
this.__updateHeaderBar(this.activeContentIndex);
}
}
,
setContentTabTitle : function(idOfContent, newTitle)
{
var contentModelIndex = this.paneModel.__getIndexById(idOfContent);	// Get a reference to the content object
if(contentModelIndex!==false){
var contentModelObj = this.paneModel.contents[contentModelIndex];
contentModelObj.__setTabTitle(newTitle);
this.__updateTabContent();
}
}
,
hidePane : function()
{
this.paneModel.__setVisible(false);	// Update the data source property
this.divElement.style.display='none';
this.__executeCallBack("hide",this.paneModel.contents[this.activeContentIndex]);
}
,
showPane : function()
{
this.paneModel.__setVisible(true);
this.divElement.style.display='block';
this.__executeCallBack("show",this.paneModel.contents[this.activeContentIndex]);
}
,
collapse : function()
{
this.__collapse();
}
,
expand : function()
{
this.__expand();
}
,
getIdOfCurrentlyDisplayedContent : function()
{
return this.paneModel.contents[this.activeContentIndex].id;
}
,
__getSizeOfPaneInPixels : function()
{
var retArray = new Array();
retArray.width = this.divElement.offsetWidth;
retArray.height = this.divElement.offsetHeight;
return retArray;
}
,
__reloadDisplayedContent : function()
{
this.reloadContent(this.paneModel.contents[this.activeContentIndex].id);
}
,
__getReferenceToMainDivElement : function()
{
return this.divElement;
}
,
__executeResizeCallBack : function()
{
this.__executeCallBack('resize');
}
,
__executeCallBack : function(whichCallBackAction,contentObj)
{
var callbackString = false;
switch(whichCallBackAction){	/* Which call back string */
case "show":
if(!this.paneModel.callbackOnShow)return;
callbackString = this.paneModel.callbackOnShow;
break;
case "collapse":
if(!this.paneModel.callbackOnCollapse)return;
callbackString = this.paneModel.callbackOnCollapse;
break;
case "expand":
if(!this.paneModel.callbackOnExpand)return;
callbackString = this.paneModel.callbackOnExpand;
break;
case "hide":
if(!this.paneModel.callbackOnHide)return;
callbackString = this.paneModel.callbackOnHide;
break;
case "slideIn":
if(!this.paneModel.callbackOnSlideIn)return;
callbackString = this.paneModel.callbackOnSlideIn;
break;
case "slideOut":
if(!this.paneModel.callbackOnSlideOut)return;
callbackString = this.paneModel.callbackOnSlideOut;
break;
case "closeContent":
if(!this.paneModel.callbackOnCloseContent)return;
callbackString = this.paneModel.callbackOnCloseContent;
break;
case "beforeCloseContent":
if(!this.paneModel.callbackOnBeforeCloseContent)return true;
callbackString = this.paneModel.callbackOnBeforeCloseContent;
break;
case "tabSwitch":
if(!this.paneModel.callbackOnTabSwitch)return;
callbackString = this.paneModel.callbackOnTabSwitch;
break;
case "resize":
if(!this.paneModel.callbackOnResize)return;
callbackString = this.paneModel.callbackOnResize;
break;
}
if(!callbackString)return;
if(!contentObj)contentObj=false;
callbackString = this.__getCallBackString(callbackString,whichCallBackAction,contentObj);
return this.__executeCallBackString(callbackString,contentObj);
}
,
__getCallBackString : function(callbackString,whichCallBackAction,contentObj)
{
if(callbackString.indexOf('(')>=0)return callbackString;
if(contentObj){
callbackString = callbackString + '(this.paneModel,"' + whichCallBackAction + '",contentObj)';
}else{
callbackString = callbackString + '(this.paneModel,"' + whichCallBackAction + '")';
}
callbackString = callbackString;
return callbackString;
}
,
__executeCallBackString : function(callbackString,contentObj)
{
try{
return eval(callbackString);
}catch(e){
alert('Could not execute specified call back function:\n' + callbackString + '\n\nError:\n' + e.name + '\n' + e.message + '\n' + '\nMake sure that there aren\'t any errors in your function.\nAlso remember that contentObj would not be present when you click close on the last tab\n(In case a close tab event triggered this callback function)');
}
}
,
__handleContentReload : function(id)
{
var ind = this.objectIndex;
var contentIndex = this.paneModel.__getIndexById(id);
if(contentIndex!==false){
var contentRef = this.paneModel.contents[contentIndex];
if(contentRef.refreshAfterSeconds){
if(this.reloadIntervalHandlers[id])clearInterval(this.reloadIntervalHandlers[id]);
this.reloadIntervalHandlers[id] = setInterval('DHTMLSuite.variableStorage.arrayOfDhtmlSuiteObjects[' + ind + '].loadContent("' + id + '","' + contentRef.contentUrl + '",' + contentRef.refreshAfterSeconds + ',true)',(contentRef.refreshAfterSeconds*1000));
}else{
if(this.reloadIntervalHandlers[id])clearInterval(this.reloadIntervalHandlers[id]);
}
}
}
,
__createPane : function()
{
this.divElement = document.createElement('DIV');	// Create the div for a pane.
this.divElement.style.position = 'absolute';
this.divElement.className = 'DHTMLSuite_pane';
this.divElement.id = 'DHTMLSuite_pane_' + this.paneModel.getPosition();
document.body.appendChild(this.divElement);
this.__createHeaderBar();	// Create the header
this.__createContentPane();	// Create content pane.
this.__createTabBar();	// Create content pane.
this.__createCollapsedPane();	// Create div element ( collapsed state)
this.__createTransparentDivForResize();	// Create transparent div for the resize;
this.__updateView();	// Update the view
this.__addContentDivs();
this.__setSize();
}
,
__createTransparentDivForResize : function()
{
this.divTransparentForResize = document.createElement('DIV');
var ref = this.divTransparentForResize;
ref.style.opacity = '0';
ref.style.display='none';
ref.style.filter = 'alpha(opacity=0)';
ref.style.position = 'absolute';
ref.style.left = '0px';
ref.style.top = this.headerDiv.offsetHeight + 'px';
ref.style.height = '90%';
ref.style.width = '100%';
ref.style.backgroundColor='#FFF';
ref.style.zIndex = '1000';
this.divElement.appendChild(ref);
}
,
__createCollapsedPane : function()
{
var ind = this.objectIndex;
var pos = this.paneModel.getPosition();
var buttonSuffix = 'Vertical';	// Suffix to the class names for the collapse and expand buttons
if(pos=='west' || pos=='east')buttonSuffix = 'Horizontal';
if(pos=='center')buttonSuffix = '';
this.divElementCollapsed = document.createElement('DIV');
var obj = this.divElementCollapsed;
obj.className = 'DHTMLSuite_pane_collapsed_' + pos;
obj.style.visibility='hidden';
obj.style.position = 'absolute';
this.divElementCollapsedInner = document.createElement('DIV');
this.divElementCollapsedInner.className= 'DHTMLSuite_pane_collapsedInner';
this.divElementCollapsedInner.onmouseover = this.__mouseoverHeaderButton;
this.divElementCollapsedInner.onmouseout = this.__mouseoutHeaderButton;
this.divElementCollapsedInner.onclick = function(e){ DHTMLSuite.variableStorage.arrayOfDhtmlSuiteObjects[ind].__slidePane(e); }
DHTMLSuite.commonObj.__addEventElement(this.divElementCollapsedInner);
obj.appendChild(this.divElementCollapsedInner);
var buttonDiv = document.createElement('DIV');
buttonDiv.className='buttonDiv';
this.divElementCollapsedInner.appendChild(buttonDiv);
this.divExpand = document.createElement('DIV');
if(pos=='south' || pos=='east')
this.divExpand.className='collapseButton' + buttonSuffix;
else
this.divExpand.className='expandButton' + buttonSuffix;
this.divExpand.onclick = function() { return DHTMLSuite.variableStorage.arrayOfDhtmlSuiteObjects[ind].__expand(); } ;
this.divExpand.onmouseover = this.__mouseoverHeaderButton;
this.divExpand.onmouseout = this.__mouseoutHeaderButton;
DHTMLSuite.commonObj.__addEventElement(this.divExpand);
buttonDiv.appendChild(this.divExpand);
document.body.appendChild(obj);
}
,
__autoSlideInPane : function(e)
{
if(document.all)e = event;
var state = this.paneModel.__getState();	// Get state of pane
if(state=='collapsed' && this.divElement.style.visibility!='hidden'){	// Element is collapsed but showing(i.e. temporary expanded)
if(!DHTMLSuite.commonObj.isObjectClicked(this.divElement,e))this.__slidePane(e,true);	// Slide in pane if element clicked is not the expanded pane
}
}
,
__slidePane : function(e,forceSlide)
{
if(this.slideIsInProgress)return;
this.zIndexCounter++;
if(document.all)e = event;	// IE
var src = DHTMLSuite.commonObj.getSrcElement(e);	// Get a reference to the element triggering the event
if(src.className.indexOf('collapsed')<0 && !forceSlide)return;	// If a button on the collapsed pane triggered the event->Return from the method without doing anything.
this.slideIsInProgress = true;
var state = this.paneModel.__getState();	// Get state of pane.
var hideWhenComplete = true;
if(this.divElement.style.visibility=='hidden'){	// The pane is currently not visible, i.e. not slided out.
this.__executeCallBack('slideOut',this.paneModel.contents[this.activeContentIndex]);
this.__setSlideInitPosition();
this.divElement.style.visibility='visible';
this.divElement.style.zIndex = 16000 + this.zIndexCounter;
this.divElementCollapsed.style.zIndex = 16000 + this.zIndexCounter;
var slideTo = this.__getSlideToCoordinates(true);	// Get coordinate, where to slide to
hideWhenComplete = false;
var slideSpeed = this.__getSlideSpeed(true);
}else{
this.__executeCallBack('slideIn',this.paneModel.contents[this.activeContentIndex]);
var slideTo = this.__getSlideToCoordinates(false);	// Get coordinate, where to slide to
var slideSpeed = this.__getSlideSpeed(false);
}
this.__processSlideByPixels(slideTo,slideSpeed*this.parentRef.slideSpeed,this.__getCurrentCoordinateInPixels(),hideWhenComplete);
}
,
__setSlideInitPosition : function()
{
var browserWidth = DHTMLSuite.clientInfoObj.getBrowserWidth();
var browserHeight = DHTMLSuite.clientInfoObj.getBrowserHeight();
var pos = this.paneModel.getPosition();
switch(pos){
case "west":
this.divElement.style.left = (0 - this.paneModel.size)+ 'px';
break;
case "east":
this.divElement.style.left = browserWidth + 'px';
break;
case "north":
this.divElement.style.top = (0 - this.paneModel.size)+ 'px';
break;
case "south":
this.divElement.style.top = browserHeight + 'px';
break;
}
}
,
__getCurrentCoordinateInPixels : function()
{
var pos = this.paneModel.getPosition();
switch(pos){
case "west": return this.divElement.style.left.replace('px','')/1;
case "east": return this.divElement.style.left.replace('px','')/1;
case "south": return this.divElement.style.top.replace('px','')/1;
case "north": return this.divElement.style.top.replace('px','')/1;
}
}
,
__getSlideSpeed : function(slideOut)
{
var pos = this.paneModel.getPosition();
switch(pos){
case "west":
case "north":
if(slideOut)return 1;else return -1;
break;
case "south":
case "east":
if(slideOut)return -1;else return 1;
}
}
,
__processSlideByPixels : function(slideTo,slidePixels,currentPos,hideWhenComplete)
{
var pos = this.paneModel.getPosition();
currentPos = currentPos + slidePixels;
var repeatSlide = true;	// Repeat one more time ?
if(slidePixels>0 && currentPos>slideTo){
currentPos = slideTo;
repeatSlide = false;
}
if(slidePixels<0 && currentPos<slideTo){
currentPos = slideTo;
repeatSlide = false;
}
switch(pos){
case "west":
case "east":
this.divElement.style.left = currentPos + 'px';
break;
case "north":
case "south":
this.divElement.style.top = currentPos + 'px';
}
if(repeatSlide){
var ind = this.objectIndex;
setTimeout('DHTMLSuite.variableStorage.arrayOfDhtmlSuiteObjects[' + ind + '].__processSlideByPixels(' + slideTo + ',' + slidePixels + ',' + currentPos + ',' + hideWhenComplete + ')',10);
}else{
if(hideWhenComplete){
this.divElement.style.visibility='hidden';
this.divElement.style.zIndex = 11000;
this.divElementCollapsed.style.zIndex = 12000;
}
this.slideIsInProgress = false;
}
}
,
__getSlideToCoordinates : function(slideOut)
{
var browserWidth = DHTMLSuite.clientInfoObj.getBrowserWidth();
var browserHeight = DHTMLSuite.clientInfoObj.getBrowserHeight();
var pos = this.paneModel.getPosition();
switch(pos){
case "west":
if(slideOut)
return this.parentRef.paneSizeCollapsed;	// End position is
else
return (0 - this.paneModel.size);
case "east":
if(slideOut)
return browserWidth - this.parentRef.paneSizeCollapsed - this.paneModel.size;
else
return browserWidth;
case "north":
if(slideOut)
return this.parentRef.paneSizeCollapsed;	// End position is
else
return (0 - this.paneModel.size);
case "south":
if(slideOut)
return browserHeight - this.parentRef.paneSizeCollapsed  - this.paneModel.size;
else
return browserHeight;
}
}
,
__updateCollapsedSize : function()
{
var pos = this.paneModel.getPosition();
var size;
if(pos=='west' || pos=='east')size = this.divElementCollapsed.offsetWidth;
if(pos=='north' || pos=='south')size = this.divElementCollapsed.offsetHeight;
if(size)this.parentRef.__setPaneSizeCollapsed(size);
}
,
__createHeaderBar : function()
{
var ind = this.objectIndex;	// Making it into a primitive variable
var pos = this.paneModel.getPosition();	// Get position of this pane
var buttonSuffix = 'Vertical';	// Suffix to the class names for the collapse and expand buttons
if(pos=='west' || pos=='east')buttonSuffix = 'Horizontal';
if(pos=='center')buttonSuffix = '';
this.headerDiv = document.createElement('DIV');
this.headerDiv.className = 'DHTMLSuite_paneHeader';
this.headerDiv.style.position = 'relative';
this.divElement.appendChild(this.headerDiv);
this.titleSpan = document.createElement('SPAN');
this.titleSpan.className = 'paneTitle';
this.headerDiv.appendChild(this.titleSpan);
var buttonDiv = document.createElement('DIV');
buttonDiv.style.position = 'absolute';
buttonDiv.style.right = '0px';
buttonDiv.style.top = '0px';
buttonDiv.className = 'DHTMLSuite_paneHeader_buttonDiv';
this.headerDiv.appendChild(buttonDiv);
this.divClose = document.createElement('DIV');
this.divClose.className = 'closeButton';
this.divClose.onmouseover = this.__mouseoverHeaderButton;
this.divClose.onmouseout = this.__mouseoutHeaderButton;
this.divClose.onclick = function() { return DHTMLSuite.variableStorage.arrayOfDhtmlSuiteObjects[ind].__close(); } ;
DHTMLSuite.commonObj.__addEventElement(this.divClose);
buttonDiv.appendChild(this.divClose);
if(pos!='center'){
this.divCollapse = document.createElement('DIV');
if(pos=='south' || pos=='east')
this.divCollapse.className='expandButton' + buttonSuffix;
else
this.divCollapse.className='collapseButton' + buttonSuffix;
this.divCollapse.onclick = function() { return DHTMLSuite.variableStorage.arrayOfDhtmlSuiteObjects[ind].__collapse(); } ;
this.divCollapse.onmouseover = this.__mouseoverHeaderButton;
this.divCollapse.onmouseout = this.__mouseoutHeaderButton;
this.divCollapse.style.display='none';
DHTMLSuite.commonObj.__addEventElement(this.divCollapse);
buttonDiv.appendChild(this.divCollapse);
}
this.divRefresh = document.createElement('DIV');
this.divRefresh.className='refreshButton';
this.divRefresh.onmouseover = this.__mouseoverHeaderButton;
this.divRefresh.onmouseout = this.__mouseoutHeaderButton;
this.divRefresh.onclick = function() { return DHTMLSuite.variableStorage.arrayOfDhtmlSuiteObjects[ind].__reloadDisplayedContent(); } ;
DHTMLSuite.commonObj.__addEventElement(this.divRefresh);
buttonDiv.appendChild(this.divRefresh);
this.headerDiv.onselectstart = DHTMLSuite.commonObj.cancelEvent;
}
,
__mouseoverHeaderButton : function()
{
if(this.className.indexOf('Over')==-1)this.className=this.className + 'Over';
}
,
__mouseoutHeaderButton : function()
{
this.className=this.className.replace('Over','');
}
,
__close : function(e)
{
var id = this.paneModel.contents[this.activeContentIndex].id;
var ok = this.__getOnBeforeCloseResult(this.activeContentIndex);
if(!ok)return;
if(id){
this.__executeCallBack('closeContent',this.paneModel.contents[this.activeContentIndex]);
document.getElementById(this.paneModel.contents[this.activeContentIndex].htmlElementId).parentNode.removeChild(document.getElementById(this.paneModel.contents[this.activeContentIndex].htmlElementId));
}
this.activeContentIndex = this.paneModel.__deleteContent(this.activeContentIndex);
this.__updatePaneView(this.activeContentIndex);
}
,
__closeAllClosableTabs : function()
{
for(var no=0;no<this.paneModel.contents.length;no++){
var closable = this.paneModel.contents[no].__getClosable();
if(closable){
var id = this.paneModel.contents[no].id;
document.getElementById(this.paneModel.contents[no].htmlElementId).parentNode.removeChild(document.getElementById(this.paneModel.contents[no].htmlElementId));
this.activeContentIndex = this.paneModel.__deleteContent(no);
this.__updatePaneView(this.activeContentIndex);
no--;
}
}
}
,
__getOnBeforeCloseResult : function(contentIndex)
{
return this.__executeCallBack('beforeCloseContent',this.paneModel.contents[contentIndex]);
}
,
__deleteContentByIndex : function(contentIndex)
{
if(this.paneModel.getCountContent()==0)return;	// No content to delete
if(!this.__getOnBeforeCloseResult(contentIndex))return;
var htmlElementId = this.paneModel.contents[contentIndex].htmlElementId;
if(htmlElementId){
try{
document.getElementById(htmlElementId).parentNode.removeChild(document.getElementById(htmlElementId));
}catch(e){
}
}
var tmpIndex = this.paneModel.__deleteContent(contentIndex);
if(contentIndex==this.activeContentIndex)this.activeContentIndex = tmpIndex;
if(this.activeContentIndex > contentIndex)this.activeContentIndex--;
if(tmpIndex===false)this.activeContentIndex=false;
this.__updatePaneView(this.activeContentIndex);
}
,
__deleteContentById : function(id)
{
var index = this.paneModel.__getIndexById(id);
if(index!==false)this.__deleteContentByIndex(index);
}
,
__collapse : function()
{
this.__updateCollapsedSize();
this.paneModel.__setState('collapsed');		// Updating the state property
this.divElementCollapsed.style.visibility='visible';
this.divElement.style.visibility='hidden';
this.__updateView();
this.parentRef.__hideResizeHandle(this.paneModel.getPosition());
this.parentRef.__positionPanes();	// Calling the positionPanes method of parent object
this.__executeCallBack("collapse",this.paneModel.contents[this.activeContentIndex]);
}
,
__expand : function()
{
this.paneModel.__setState('expanded');		// Updating the state property
this.divElementCollapsed.style.visibility='hidden';
this.divElement.style.visibility='visible';
this.__updateView();
this.parentRef.__showResizeHandle(this.paneModel.getPosition());
this.parentRef.__positionPanes();	// Calling the positionPanes method of parent object
this.__executeCallBack("expand",this.paneModel.contents[this.activeContentIndex]);
}
,
__updateHeaderBar : function(index)
{
if(index===false){	// No content in this pane
this.divClose.style.display='none';	// Hide close button
this.divRefresh.style.display='none';
if(this.paneModel.getPosition()!='center')this.divCollapse.style.display='block';else this.divCollapse.style.display='none';	// Make collapse button visible for all panes except center
this.titleSpan.innerHTML = '';	// Set title bar empty
return;	// Return from this method.
}
this.divClose.style.display='block';
this.divRefresh.style.display='block';
if(this.divCollapse)this.divCollapse.style.display='block';	// Center panes doesn't have collapse button, that's the reason for the if-statement
this.titleSpan.innerHTML = this.paneModel.contents[index].title;
var contentObj = this.paneModel.contents[index];
if(!contentObj.closable)this.divClose.style.display='none';
if(!contentObj.displayRefreshButton || !contentObj.contentUrl)this.divRefresh.style.display='none';
if(!this.paneModel.collapsable){	// Pane is collapsable
if(this.divCollapse)this.divCollapse.style.display='none';	// Center panes doesn't have collapse button, that's the reason for the if-statement
this.divExpand.style.display='none';
}
}
,
__showButtons : function()
{
var div = this.headerDiv.getElementsByTagName('DIV')[0];
div.style.visibility='visible';
}
,
__hideButtons : function()
{
var div = this.headerDiv.getElementsByTagName('DIV')[0];
div.style.visibility='hidden';
}
,
__updateView : function()
{
if(this.paneModel.getCountContent()>0 && this.activeContentIndex===false)this.activeContentIndex = 0;	// No content existed, but content has been added.
this.tabDiv.style.display='block';
this.headerDiv.style.display='block';
var pos = this.paneModel.getPosition();
if(pos=='south' || pos=='north')this.divElementCollapsed.style.height = this.parentRef.paneSizeCollapsed;
if(this.paneModel.getCountContent()<2)this.tabDiv.style.display='none';
if(this.activeContentIndex!==false)if(!this.paneModel.contents[this.activeContentIndex].title)this.headerDiv.style.display='none';	// Active content without title, hide header bar.
if(this.paneModel.state=='expanded')this.__showButtons();else this.__hideButtons();
this.__setSize();
}
,
__createContentPane : function()
{
this.contentDiv = document.createElement('DIV');
this.contentDiv.className = 'DHTMLSuite_paneContent';
this.contentDiv.id = 'DHTMLSuite_paneContent' + this.paneModel.getPosition();
if(!this.paneModel.scrollbars)this.contentDiv.style.overflow='hidden';
this.divElement.appendChild(this.contentDiv);
}
,
__createTabBar : function()
{
this.tabDiv = document.createElement('DIV');
this.tabDiv.className = 'DHTMLSuite_paneTabs';
this.divElement.appendChild(this.tabDiv);
this.__updateTabContent();
}
,
__updateTabContent : function()
{
this.tabDiv.innerHTML = '';
var tableObj = document.createElement('TABLE');
tableObj.style.padding = '0px';
tableObj.style.margin = '0px';
tableObj.cellPadding = 0;
tableObj.cellSpacing = 0;
this.tabDiv.appendChild(tableObj);
var tbody = document.createElement('TBODY');
tableObj.appendChild(tbody);
var row = tbody.insertRow(0);
var contents = this.paneModel.getContents();
var ind = this.objectIndex;
for(var no=0;no<contents.length;no++){
var cell = row.insertCell(-1);
var divTag = document.createElement('DIV');
divTag.className = 'paneSplitterInactiveTab';
cell.appendChild(divTag);
var aTag = document.createElement('A');
aTag.title = contents[no].tabTitle;	// Setting title of tab - useful when the tab isn't wide enough to show the label.
contents[no].tabTitle = contents[no].tabTitle + '';
aTag.innerHTML = contents[no].tabTitle.replace(' ','&nbsp;') + '';
aTag.id = 'paneTabLink' + no;
aTag.href='#';
aTag.onclick = function(e) { return DHTMLSuite.variableStorage.arrayOfDhtmlSuiteObjects[ind].__tabClick(e); } ;
divTag.appendChild(aTag);
DHTMLSuite.commonObj.__addEventElement(aTag);
divTag.appendChild(document.createElement('SPAN'));
}
this.__updateTabView(0);
}
,
__updateTabView : function(activeTab)
{
var tabDivs = this.tabDiv.getElementsByTagName('DIV');
for(var no=0;no<tabDivs.length;no++){
if(no==activeTab){
tabDivs[no].className = 'paneSplitterActiveTab';
}else tabDivs[no].className = 'paneSplitterInactiveTab';
}
}
,
__tabClick : function(e)
{
if(document.all)e = event;
var inputObj = DHTMLSuite.commonObj.getSrcElement(e);
if(inputObj.tagName!='A')inputObj = inputObj.parentNode;
var numericIdOfClickedTab = inputObj.id.replace(/[^0-9]/gi,'');
if(numericIdOfClickedTab!=this.activeContentIndex)this.__updatePaneContentScrollTopPosition(this.activeContentIndex,numericIdOfClickedTab);
this.__updatePaneView(numericIdOfClickedTab);
this.__executeCallBack("tabSwitch",this.paneModel.contents[this.activeContentIndex]);
return false;
}
,
__updatePaneContentScrollTopPosition : function(idOfContentToHide,idOfContentToShow)
{
var newScrollTop = 0;
if(this.contentScrollTopPositions[idOfContentToShow])newScrollTop = this.contentScrollTopPositions[idOfContentToShow];
var contentParentContainer = document.getElementById(this.paneModel.contents[idOfContentToHide].htmlElementId).parentNode;
this.contentScrollTopPositions[idOfContentToHide] = contentParentContainer.scrollTop;
setTimeout('document.getElementById("' + contentParentContainer.id + '").scrollTop = ' + newScrollTop,20);	// A small delay so that content can be inserted into the div first.
}
,
__addContentDivs : function(onCompleteJsCode)
{
var contents = this.paneModel.getContents();
for(var no=0;no<contents.length;no++){
this.__addOneContentDiv(this.paneModel.contents[no],onCompleteJsCode);
}
this.__updatePaneView(this.activeContentIndex);	// Display initial data
}
,
__addOneContentDiv : function(contentObj,onCompleteJsCode)
{
var htmlElementId = contentObj.htmlElementId;	// Get a reference to content id
var contentUrl = contentObj.contentUrl;	// Get a reference to content id
var refreshAfterSeconds = contentObj.refreshAfterSeconds;	// Get a reference to content id
if(htmlElementId){
try{
this.contentDiv.appendChild(document.getElementById(htmlElementId));
document.getElementById(htmlElementId).className = 'DHTMLSuite_paneContentInner';
document.getElementById(htmlElementId).style.display='none';
}catch(e){
}
}
if(contentUrl){	/* Url present */
if(!contentObj.htmlElementId || htmlElementId.indexOf('dynamicCreatedDiv__')==-1){	// Has this content been loaded before ? Might have to figure out a smarter way of checking this.
if(!document.getElementById(htmlElementId)){
this.__createAContentDivDynamically(contentObj);
this.loadContent(contentObj.id,contentUrl,refreshAfterSeconds,false,onCompleteJsCode);
}
}
}
}
,
__createAContentDivDynamically : function(contentObj)
{
var d = new Date();	// Create unique id for a new div
var divId = 'dynamicCreatedDiv__' + d.getSeconds() + (Math.random()+'').replace('.','');
if(!document.getElementById(contentObj.id))divId = contentObj.id;	// Give it the id of the element it's self if it doesn't alredy exists on the page.
contentObj.__setIdOfContentElement(divId);
var div = document.createElement('DIV');
div.id = divId;
div.className = 'DHTMLSuite_paneContentInner';
if(contentObj.contentUrl)div.innerHTML = this.parentRef.waitMessage;	// Content url present - Display wait message until content has been loaded.
this.contentDiv.appendChild(div);
div.style.display='none';
}
,
__updatePaneView : function(index)
{
if(!index && index!==0)index=this.activeContentIndex;
this.__updateTabContent();
this.__updateView();
this.__updateHeaderBar(index);
this.__showHideContentDiv(index);
this.__updateTabView(index);
this.activeContentIndex = index;
}
,
__showHideContentDiv : function(index)
{
if(index!==false){	// Still content in this pane
var htmlElementId = this.paneModel.contents[this.activeContentIndex].htmlElementId;
try{
document.getElementById(htmlElementId).style.display='none';
}catch(e){
}
var htmlElementId = this.paneModel.contents[index].htmlElementId;
if(htmlElementId){
try{
document.getElementById(htmlElementId).style.display='block';
}catch(e){
}
}
}
}
,
__setSize : function(recursive)
{
var pos = this.paneModel.getPosition().toLowerCase();
if(pos=='west' || pos=='east'){
this.divElement.style.width = this.paneModel.size + 'px';
}
if(pos=='north' || pos=='south'){
this.divElement.style.height = this.paneModel.size + 'px';
this.divElement.style.width = '100%';
}
try{
this.contentDiv.style.height = (this.divElement.clientHeight - this.tabDiv.offsetHeight - this.headerDiv.offsetHeight) + 'px';
}catch(e){
}
if(!recursive){
window.obj = this;
setTimeout('window.obj.__setSize(true)',100);
}
}
,
__setTopPosition : function(newTop)
{
this.divElement.style.top = newTop + 'px';
}
,
__setLeftPosition : function(newLeft)
{
this.divElement.style.left = newLeft + 'px';
}
,
__setWidth : function(newWidth)
{
if(this.paneModel.getPosition()=='west' || this.paneModel.getPosition()=='east')this.paneModel.setSize(newWidth);
newWidth = newWidth + '';
if(newWidth.indexOf('%')==-1)newWidth = Math.max(1,newWidth) + 'px';
this.divElement.style.width = newWidth;
}
,
__setHeight : function(newHeight)
{
if(this.paneModel.getPosition()=='north' || this.paneModel.getPosition()=='south')this.paneModel.setSize(newHeight);
this.divElement.style.height = Math.max(1,newHeight) + 'px';
this.__setSize();	// Set size of inner elements.
}
,
__showTransparentDivForResize : function()
{
this.divTransparentForResize.style.display = 'block';
var ref = this.divTransparentForResize;
ref.style.height = this.contentDiv.clientHeight + 'px';
ref.style.width = this.contentDiv.clientWidth + 'px';
}
,
__hideTransparentDivForResize : function()
{
this.divTransparentForResize.style.display = 'none';
}
}
DHTMLSuite.paneSplitter = function()
{
var dataModel;				// An object of class DHTMLSuite.paneSplitterModel
var panes;					// An array of DHTMLSuite.paneSplitterPane objects.
var panesAssociative;		// An associative array of panes. used to get a quick access to the panes
var paneContent;			// An array of DHTMLSuite.paneSplitterPaneView objects.
var layoutCSS;				// Name/Path of css file
var horizontalSplitterSize;	// Height of horizontal splitter
var horizontalSplitterBorderSize;	// Height of horizontal splitter
var verticalSplitterSize;	//
var paneSplitterHandles;				// Associative array of pane splitter handles
var paneDivsCollapsed;					// Associative array of divs ( collapsed state of pane )
var paneSplitterHandleOnResize;
var resizeInProgress;					// Variable indicating if resize is in progress
var resizeCounter;						// Internal variable used while resizing (-1 = no resize, 0 = waiting for resize)
var currentResize;						// Which pane is currently being resized ( string, "west", "east", "north" or "south"
var currentResize_min;
var currentResize_max;
var paneSizeCollapsed;					// Size of pane when it's collapsed ( the bar )
var paneBorderLeftPlusRight;			// Sum of border left and right for panes ( needed in a calculation)
var slideSpeed;							// Slide of pane slide
var waitMessage;						// Ajax wait message
this.resizeCounter = -1;
this.horizontalSplitterSize = 5;
this.verticalSplitterSize = 5;
this.paneBorderLeftPlusRight = 2;		// 1 pixel border at the right of panes, 1 pixel to the left
this.slideSpeed = 10;
this.horizontalSplitterBorderSize = 1;
this.resizeInProgress = false;
this.paneSplitterHandleOnResize = false;
this.paneSizeCollapsed = 26;
this.paneSplitterHandles = new Array();
this.paneDivsCollapsed = new Array();
this.dataModel = false;		// Initial value
this.layoutCSS = 'pane-splitter.css';
this.waitMessage = 'Loading content - please wait';
this.panes = new Array();
this.panesAssociative = new Array();
try{
if(!standardObjectsCreated)DHTMLSuite.createStandardObjects();
}catch(e){
alert('You need to include the dhtmlSuite-common.js file');
}
var objectIndex;			// Index of this object in the variableStorage array
this.objectIndex = DHTMLSuite.variableStorage.arrayOfDhtmlSuiteObjects.length;
DHTMLSuite.variableStorage.arrayOfDhtmlSuiteObjects[this.objectIndex] = this;
}
DHTMLSuite.paneSplitter.prototype =
{
addModel : function(newModel)
{
this.dataModel = newModel;
}
,
setLayoutCss : function(layoutCSS)
{
this.layoutCSS = layoutCSS;
}
,
setAjaxWaitMessage : function(newWaitMessage)
{
this.waitMessage = newWaitMessage;
}
,
setSlideSpeed : function(slideSpeed)
{
this.slideSpeed = slideSpeed;
}
,
init : function()
{
DHTMLSuite.commonObj.loadCSS(this.layoutCSS);	// Load css.
this.__createPanes();	// Create the panes
this.__positionPanes();	// Position panes
this.__createResizeHandles();
this.__addEvents();
this.__initCollapsePanes();
setTimeout("DHTMLSuite.variableStorage.arrayOfDhtmlSuiteObjects[" + this.objectIndex + "].__positionPanes();",100);
setTimeout("DHTMLSuite.variableStorage.arrayOfDhtmlSuiteObjects[" + this.objectIndex + "].__positionPanes();",500);
setTimeout("DHTMLSuite.variableStorage.arrayOfDhtmlSuiteObjects[" + this.objectIndex + "].__positionPanes();",1500);
}
,
isUrlLoadedInPane : function(id,url)
{
var ref = this.__getPaneReferenceFromContentId(id);	// Get a reference to the pane where the content is.
if(ref){	// Pane found
return ref.isUrlLoadedInPane(id,url);
}else return false;
}
,
loadContent : function(id,url,refreshAfterSeconds,onCompleteJsCode)
{
var ref = this.__getPaneReferenceFromContentId(id);	// Get a reference to the pane where the content is.
if(ref){	// Pane found
ref.loadContent(id,url,refreshAfterSeconds,false,onCompleteJsCode);		// Call the loadContent method of this object.
}
}
,
reloadContent : function(id)
{
var ref = this.__getPaneReferenceFromContentId(id);	// Get a reference to the pane where the content is.
if(ref){	// Pane found
ref.reloadContent(id);		// Call the loadContent method of this object.
}
}
,
setRefreshAfterSeconds : function(idOfContentObject,refreshAfterSeconds)
{
var ref = this.__getPaneReferenceFromContentId(idOfContentObject);	// Get a reference to the pane where the content is.
if(ref){	// Pane found
ref.setRefreshAfterSeconds(idOfContentObject,refreshAfterSeconds);		// Call the loadContent method of this object.
}
}
,
setContentTabTitle : function(idOfContentObject,newTitle)
{
var ref = this.__getPaneReferenceFromContentId(idOfContentObject);	// Get a reference to the pane where the content is.
if(ref)ref.setContentTabTitle(idOfContentObject,newTitle);
}
,
setContentTitle : function(idOfContentObject,newTitle)
{
var ref = this.__getPaneReferenceFromContentId(idOfContentObject);	// Get a reference to the pane where the content is.
if(ref)ref.setContentTitle(idOfContentObject,newTitle);
}
,
showContent : function(id)
{
var ref = this.__getPaneReferenceFromContentId(id);
if(ref)ref.showContent(id);
}
,
closeAllClosableTabs : function(panePosition)
{
if(this.panesAssociative[panePosition.toLowerCase()]) return this.panesAssociative[panePosition.toLowerCase()].__closeAllClosableTabs(); else return false;
}
,
addContent : function(panePosition,contentModel,onCompleteJsCode)
{
if(this.panesAssociative[panePosition.toLowerCase()]) return this.panesAssociative[panePosition.toLowerCase()].addContent(contentModel,onCompleteJsCode); else return false;
}
,
deleteContentById : function(id)
{
var ref = this.__getPaneReferenceFromContentId(id);
if(ref)ref.__deleteContentById(id);
}
,
deleteContentByIndex: function(panePosition,contentIndex)
{
if(this.panesAssociative[panePosition]){//Pane exists
this.panesAssociative[panePosition].__deleteContentByIndex(contentIndex);
}
}
,
hidePane : function(panePosition)
{
if(this.panesAssociative[panePosition] && panePosition!='center'){
this.panesAssociative[panePosition].hidePane();				 // Call method in paneSplitterPane class
if(this.paneSplitterHandles[panePosition])this.paneSplitterHandles[panePosition].style.display='none'; // Hide resize handle
this.__positionPanes();										 // Reposition panes
}else return false;
}
,
showPane : function(panePosition)
{
if(this.panesAssociative[panePosition] && panePosition!='center'){
this.panesAssociative[panePosition].showPane();					// Call method in paneSplitterPane class
if(this.paneSplitterHandles[panePosition])this.paneSplitterHandles[panePosition].style.display='block';	// Show resize handle
this.__positionPanes();											// Reposition panes
}else return false;
}
,
getReferenceToMainDivElementOfPane : function(panePosition)
{
if(this.panesAssociative[panePosition])return this.panesAssociative[panePosition].__getReferenceToMainDivElement();
return false;
}
,
getIdOfCurrentlyDisplayedContent : function(panePosition)
{
if(this.panesAssociative[panePosition])return this.panesAssociative[panePosition].getIdOfCurrentlyDisplayedContent();
return false;
}
,
getSizeOfPaneInPixels : function(panePosition)
{
if(this.panesAssociative[panePosition])return this.panesAssociative[panePosition].__getSizeOfPaneInPixels();
return false;
}
,
__setPaneSizeCollapsed : function(newSize)
{
if(newSize>this.paneSizeCollapsed)this.paneSizeCollapsed = newSize;
}
,
__createPanes : function()
{
var dataObjects = this.dataModel.getItems();	// An array of data source objects, i.e. panes.
for(var no=0;no<dataObjects.length;no++){
var index = this.panes.length;
this.panes[index] = new DHTMLSuite.paneSplitterPane(this);
this.panes[index].addDataSource(dataObjects[no]);
this.panes[index].__createPane();
this.panesAssociative[dataObjects[no].position.toLowerCase()] = this.panes[index];	// Save this pane in the associative array
}
}
,
__createResizeHandles : function()
{
var ind = this.objectIndex;
if(this.panesAssociative['north'] && this.panesAssociative['north'].paneModel.resizable){
this.paneSplitterHandles['north'] = document.createElement('DIV');
var obj = this.paneSplitterHandles['north'];
obj.className = 'DHTMLSuite_paneSplitter_horizontal';
obj.style.position = 'absolute';
obj.style.height = this.horizontalSplitterSize + 'px';
obj.style.width = '100%';
obj.style.zIndex = 10000;
DHTMLSuite.commonObj.addEvent(obj,'mousedown',function() { DHTMLSuite.variableStorage.arrayOfDhtmlSuiteObjects[ind].__initResizePane('north'); });
document.body.appendChild(obj);
}
if(this.panesAssociative['west'] && this.panesAssociative['west'].paneModel.resizable){
this.paneSplitterHandles['west'] = document.createElement('DIV');
var obj = this.paneSplitterHandles['west'];
obj.className = 'DHTMLSuite_paneSplitter_vertical';
obj.style.position = 'absolute';
obj.style.width = this.verticalSplitterSize + 'px';
obj.style.zIndex = 11000;
DHTMLSuite.commonObj.addEvent(obj,'mousedown',function() { DHTMLSuite.variableStorage.arrayOfDhtmlSuiteObjects[ind].__initResizePane('west'); });
document.body.appendChild(obj);
}
if(this.panesAssociative['east'] && this.panesAssociative['east'].paneModel.resizable){
this.paneSplitterHandles['east'] = document.createElement('DIV');
var obj = this.paneSplitterHandles['east'];
obj.className = 'DHTMLSuite_paneSplitter_vertical';
obj.style.position = 'absolute';
obj.style.width = this.verticalSplitterSize + 'px';
obj.style.zIndex = 11000;
DHTMLSuite.commonObj.addEvent(obj,'mousedown',function() { DHTMLSuite.variableStorage.arrayOfDhtmlSuiteObjects[ind].__initResizePane('east'); });
document.body.appendChild(obj);
}
if(this.panesAssociative['south'] && this.panesAssociative['south'].paneModel.resizable){
this.paneSplitterHandles['south'] = document.createElement('DIV');
var obj = this.paneSplitterHandles['south'];
obj.className = 'DHTMLSuite_paneSplitter_horizontal';
obj.style.position = 'absolute';
obj.style.height = this.horizontalSplitterSize + 'px';
obj.style.width = '100%';
obj.style.zIndex = 10000;
DHTMLSuite.commonObj.addEvent(obj,'mousedown',function() { DHTMLSuite.variableStorage.arrayOfDhtmlSuiteObjects[ind].__initResizePane('south'); });
document.body.appendChild(obj);
}
this.paneSplitterHandleOnResize = document.createElement('DIV');
var obj = this.paneSplitterHandleOnResize;
obj.className = 'DHTMLSuite_paneSplitter_onResize';
obj.style.position = 'absolute';
obj.style.zIndex = 955000;
obj.style.display='none';
document.body.appendChild(obj);
}
,
__getPaneReferenceFromContentId : function(id)
{
for(var no=0;no<this.panes.length;no++){
var contents = this.panes[no].paneModel.getContents();
for(var no2=0;no2<contents.length;no2++){
if(contents[no2].id==id)return this.panes[no];
}
}
return false;
}
,
__initCollapsePanes : function()
{
for(var no=0;no<this.panes.length;no++){	/* Loop through panes */
if(this.panes[no].paneModel.state=='collapsed'){	// State set to collapsed ?
this.panes[no].__collapse();
}
}
}
,
__getMinimumPos : function(pos)
{
var browserWidth = DHTMLSuite.clientInfoObj.getBrowserWidth();
var browserHeight = DHTMLSuite.clientInfoObj.getBrowserHeight();
if(pos=='west' || pos == 'north'){
return 	this.panesAssociative[pos].paneModel.minSize;
}else{
if(pos=='east')return 	browserWidth - this.panesAssociative[pos].paneModel.maxSize;
if(pos=='south')return 	browserHeight - this.panesAssociative[pos].paneModel.maxSize;
}
}
,
__getMaximumPos : function(pos)
{
var browserWidth = DHTMLSuite.clientInfoObj.getBrowserWidth();
var browserHeight = DHTMLSuite.clientInfoObj.getBrowserHeight();
if(pos=='west' || pos == 'north'){
return 	this.panesAssociative[pos].paneModel.maxSize;
}else{
if(pos=='east')return 	browserWidth - this.panesAssociative[pos].paneModel.minSize;
if(pos=='south')return 	browserHeight - this.panesAssociative[pos].paneModel.minSize;
}
}
,
__initResizePane : function(pos)
{
this.currentResize = pos;
this.currentResize_min = this.__getMinimumPos(pos);
this.currentResize_max = this.__getMaximumPos(pos);
this.paneSplitterHandleOnResize.style.left = this.paneSplitterHandles[pos].style.left;
this.paneSplitterHandleOnResize.style.top = this.paneSplitterHandles[pos].style.top;
this.paneSplitterHandleOnResize.style.width = this.paneSplitterHandles[pos].offsetWidth + 'px';
this.paneSplitterHandleOnResize.style.height = this.paneSplitterHandles[pos].offsetHeight + 'px';
this.paneSplitterHandleOnResize.style.display='block';
this.resizeCounter = 0;
DHTMLSuite.commonObj.__setOkToMakeTextSelections(false);
this.__timerResizePane(pos);
}
,
__timerResizePane : function(pos)
{
if(this.resizeCounter>=0 && this.resizeCounter<5){
this.resizeCounter++;
setTimeout('DHTMLSuite.variableStorage.arrayOfDhtmlSuiteObjects[' + this.objectIndex + '].__timerResizePane()',2);
}
if(this.resizeCounter==5){
this.__showTransparentDivForResize('show');
}
}
,
__showTransparentDivForResize : function()
{
if(DHTMLSuite.clientInfoObj.isOpera)return;	// Opera doesn't support transparent div the same way as FF and IE.
if(this.panesAssociative['west'])this.panesAssociative['west'].__showTransparentDivForResize();
if(this.panesAssociative['south'])this.panesAssociative['south'].__showTransparentDivForResize();
if(this.panesAssociative['east'])this.panesAssociative['east'].__showTransparentDivForResize();
if(this.panesAssociative['north'])this.panesAssociative['north'].__showTransparentDivForResize();
if(this.panesAssociative['center'])this.panesAssociative['center'].__showTransparentDivForResize();
}
,
__hideTransparentDivForResize : function()
{
if(this.panesAssociative['west'])this.panesAssociative['west'].__hideTransparentDivForResize();
if(this.panesAssociative['south'])this.panesAssociative['south'].__hideTransparentDivForResize();
if(this.panesAssociative['east'])this.panesAssociative['east'].__hideTransparentDivForResize();
if(this.panesAssociative['north'])this.panesAssociative['north'].__hideTransparentDivForResize();
if(this.panesAssociative['center'])this.panesAssociative['center'].__hideTransparentDivForResize();
}
,
__resizePane : function(e)
{
if(document.all)e = event;	// Get reference to event object.
if(DHTMLSuite.clientInfoObj.isMSIE && e.button!=1)this.__endResize();
if(this.resizeCounter==5){	/* Resize in progress */
if(this.currentResize=='west' || this.currentResize=='east'){
var leftPos = e.clientX;
if(leftPos<this.currentResize_min)leftPos = this.currentResize_min;
if(leftPos>this.currentResize_max)leftPos = this.currentResize_max;
this.paneSplitterHandleOnResize.style.left = leftPos + 'px';
}else{
var topPos = e.clientY;
if(topPos<this.currentResize_min)topPos = this.currentResize_min;
if(topPos>this.currentResize_max)topPos = this.currentResize_max;
this.paneSplitterHandleOnResize.style.top = topPos + 'px';
}
}
}
,
__endResize : function()
{
if(this.resizeCounter==5){	// Resize completed
this.__hideTransparentDivForResize();
var browserWidth = DHTMLSuite.clientInfoObj.getBrowserWidth();
var browserHeight = DHTMLSuite.clientInfoObj.getBrowserHeight();
var obj = this.panesAssociative[this.currentResize];
switch(this.currentResize){
case "west":
obj.__setWidth(this.paneSplitterHandleOnResize.style.left.replace('px','')/1);
break;
case "north":
obj.__setHeight(this.paneSplitterHandleOnResize.style.top.replace('px','')/1);
break;
case "east":
obj.__setWidth(browserWidth - this.paneSplitterHandleOnResize.style.left.replace('px','')/1);
break;
case "south":
obj.__setHeight(browserHeight - this.paneSplitterHandleOnResize.style.top.replace('px','')/1);
break;
}
this.__positionPanes();
obj.__executeResizeCallBack();
this.paneSplitterHandleOnResize.style.display='none';
this.resizeCounter = -1;
DHTMLSuite.commonObj.__setOkToMakeTextSelections(true);
}
}
,
__hideResizeHandle : function(pos){
if(this.paneSplitterHandles[pos])this.paneSplitterHandles[pos].style.display='none';
}
,
__showResizeHandle : function(pos){
if(this.paneSplitterHandles[pos])this.paneSplitterHandles[pos].style.display='block';
}
,
__positionResizeHandles : function()
{
if(this.paneSplitterHandles['north']){	// Position north splitter handle
this.paneSplitterHandles['north'].style.top = this.panesAssociative['north'].divElement.style.height.replace('px','')  + 'px';
}
var heightHandler = this.panesAssociative['center'].divElement.offsetHeight+1;	// Initial height
var topPos=0;
if(this.panesAssociative['center'])topPos +=this.panesAssociative['center'].divElement.style.top.replace('px','')/1;
if(this.paneSplitterHandles['west']){
if(this.paneSplitterHandles['east'])heightHandler+=this.horizontalSplitterBorderSize/2;
this.paneSplitterHandles['west'].style.left = this.panesAssociative['west'].divElement.offsetWidth + 'px';
this.paneSplitterHandles['west'].style.height = heightHandler + 'px';
this.paneSplitterHandles['west'].style.top = topPos + 'px';
}
if(this.paneSplitterHandles['east']){
var leftPos = this.panesAssociative['center'].divElement.style.left.replace('px','')/1 + this.panesAssociative['center'].divElement.offsetWidth;
this.paneSplitterHandles['east'].style.left = leftPos + 'px';
this.paneSplitterHandles['east'].style.height = heightHandler + 'px';
this.paneSplitterHandles['east'].style.top = topPos + 'px';
}
if(this.paneSplitterHandles['south']){
var topPos = this.panesAssociative['south'].divElement.style.top.replace('px','')/1;
topPos = topPos - this.horizontalSplitterSize - this.horizontalSplitterBorderSize;
this.paneSplitterHandles['south'].style.top = topPos + 'px';
}
this.resizeInProgress = false;
}
,
__positionPanes : function()
{
if(this.resizeInProgress)return;
var ind = this.objectIndex;
this.resizeInProgress = true;
var browserWidth = DHTMLSuite.clientInfoObj.getBrowserWidth();
var browserHeight = DHTMLSuite.clientInfoObj.getBrowserHeight();
var posTopMiddlePanes = 0;
if(this.panesAssociative['north'] && this.panesAssociative['north'].paneModel.visible){
if(this.panesAssociative['north'].paneModel.state=='expanded'){
posTopMiddlePanes = this.panesAssociative['north'].divElement.offsetHeight;
if(this.paneSplitterHandles['north'])posTopMiddlePanes+=(this.horizontalSplitterSize + this.horizontalSplitterBorderSize);
this.panesAssociative['north'].__setHeight(this.panesAssociative['north'].divElement.offsetHeight);
}else{
posTopMiddlePanes+=this.paneSizeCollapsed;
}
}
if(this.panesAssociative['center'])this.panesAssociative['center'].__setTopPosition(posTopMiddlePanes);
if(this.panesAssociative['west'])this.panesAssociative['west'].__setTopPosition(posTopMiddlePanes);
if(this.panesAssociative['east'])this.panesAssociative['east'].__setTopPosition(posTopMiddlePanes);
if(this.panesAssociative['west'])this.panesAssociative['west'].divElementCollapsed.style.top = posTopMiddlePanes + 'px';
if(this.panesAssociative['east'])this.panesAssociative['east'].divElementCollapsed.style.top = posTopMiddlePanes + 'px';
var posLeftCenterPane = 0;
if(this.panesAssociative['west']){
if(this.panesAssociative['west'].paneModel.state=='expanded'){	// West panel is expanded.
posLeftCenterPane = this.panesAssociative['west'].divElement.offsetWidth;
this.panesAssociative['west'].__setLeftPosition(0);
posLeftCenterPane+=(this.verticalSplitterSize);
}else{	// West panel is not expanded.
posLeftCenterPane+=this.paneSizeCollapsed  ;
}
}
this.panesAssociative['center'].__setLeftPosition(posLeftCenterPane);
var sizeCenterPane = browserWidth;
if(this.panesAssociative['west'] && this.panesAssociative['west'].paneModel.visible){	// Center pane exists and is visible - decrement width of center pane
if(this.panesAssociative['west'].paneModel.state=='expanded')
sizeCenterPane -= this.panesAssociative['west'].divElement.offsetWidth;
else
sizeCenterPane -= this.paneSizeCollapsed;
}
if(this.panesAssociative['east'] && this.panesAssociative['east'].paneModel.visible){	// East pane exists and is visible - decrement width of center pane
if(this.panesAssociative['east'].paneModel.state=='expanded')
sizeCenterPane -= this.panesAssociative['east'].divElement.offsetWidth;
else
sizeCenterPane -= this.paneSizeCollapsed;
}
sizeCenterPane-=this.paneBorderLeftPlusRight;
if(this.paneSplitterHandles['west'] && this.panesAssociative['west'].paneModel.state=='expanded')sizeCenterPane-=(this.verticalSplitterSize);
if(this.paneSplitterHandles['east'] && this.panesAssociative['east'].paneModel.state=='expanded')sizeCenterPane-=(this.verticalSplitterSize);
this.panesAssociative['center'].__setWidth(sizeCenterPane);
var posEastPane = posLeftCenterPane + this.panesAssociative['center'].divElement.offsetWidth;
if(this.paneSplitterHandles['east'])posEastPane+=(this.verticalSplitterSize);
if(this.panesAssociative['east']){
if(this.panesAssociative['east'].paneModel.state=='expanded')this.panesAssociative['east'].__setLeftPosition(posEastPane);
this.panesAssociative['east'].divElementCollapsed.style.left = (posEastPane - this.verticalSplitterSize) + 'px';
}
var heightMiddleFrames = browserHeight;
if(this.panesAssociative['north'] && this.panesAssociative['north'].paneModel.visible){
if(this.panesAssociative['north'].paneModel.state=='expanded'){
heightMiddleFrames-= this.panesAssociative['north'].divElement.offsetHeight;
if(this.paneSplitterHandles['north'])heightMiddleFrames-=(this.horizontalSplitterSize + this.horizontalSplitterBorderSize);
}else
heightMiddleFrames-= this.paneSizeCollapsed;
}
if(this.panesAssociative['south'] && this.panesAssociative['south'].paneModel.visible){
if(this.panesAssociative['south'].paneModel.state=='expanded'){
heightMiddleFrames-=this.panesAssociative['south'].divElement.offsetHeight;
if(!this.paneSplitterHandles['south'])heightMiddleFrames+=(this.horizontalSplitterSize + this.horizontalSplitterBorderSize);
}else
heightMiddleFrames-=this.paneSizeCollapsed;
}
if(this.panesAssociative['center'])this.panesAssociative['center'].__setHeight(heightMiddleFrames);
if(this.panesAssociative['west'])this.panesAssociative['west'].__setHeight(heightMiddleFrames);
if(this.panesAssociative['east'])this.panesAssociative['east'].__setHeight(heightMiddleFrames);
var posSouth = 0;
if(this.panesAssociative['north']){	/* Step 1 - get height of north pane */
if(this.panesAssociative['north'].paneModel.state=='expanded'){
posSouth = this.panesAssociative['north'].divElement.offsetHeight;
}else
posSouth = this.paneSizeCollapsed;
}
posSouth += heightMiddleFrames;
if(this.paneSplitterHandles['south'] && this.panesAssociative['south'].paneModel.state=='expanded'){
posSouth+=(this.horizontalSplitterSize + this.horizontalSplitterBorderSize);
}
if(this.panesAssociative['south']){
this.panesAssociative['south'].__setTopPosition(posSouth);
this.panesAssociative['south'].divElementCollapsed.style.top = posSouth + 'px';
this.panesAssociative['south'].__setWidth('100%');
}
try{
if(this.panesAssociative['west']){
this.panesAssociative['west'].divElementCollapsed.style.height = (heightMiddleFrames) + 'px';
this.panesAssociative['west'].divElementCollapsedInner.style.height = (heightMiddleFrames -5) + 'px';
}
}catch(e){
}
if(this.panesAssociative['east']){
this.panesAssociative['east'].divElementCollapsed.style.height = heightMiddleFrames + 'px';
this.panesAssociative['east'].divElementCollapsedInner.style.height = (heightMiddleFrames - 5) + 'px';
}
if(this.panesAssociative['south']){
this.panesAssociative['south'].divElementCollapsed.style.width = browserWidth + 'px';
this.panesAssociative['south'].divElementCollapsedInner.style.width = (browserWidth - 4) + 'px';
if(this.panesAssociative['south'].paneModel.state=='collapsed' && this.panesAssociative['south'].divElementCollapsed.offsetHeight){	// Increasing the size of the southern pane
var rest = browserHeight -  this.panesAssociative['south'].divElementCollapsed.style.top.replace('px','')/1 - this.panesAssociative['south'].divElementCollapsed.offsetHeight;
if(rest>0)this.panesAssociative['south'].divElementCollapsed.style.height = (this.panesAssociative['south'].divElementCollapsed.offsetHeight + rest) + 'px';
}
}
if(this.panesAssociative['north']){
this.panesAssociative['north'].divElementCollapsed.style.width = browserWidth + 'px';
this.panesAssociative['north'].divElementCollapsedInner.style.width = (browserWidth - 4) + 'px';
}
this.__positionResizeHandles();
setTimeout('DHTMLSuite.variableStorage.arrayOfDhtmlSuiteObjects[' + ind + '].__positionResizeHandles()',50);	// To get the tabs positioned correctly.
}
,
__autoSlideInPanes : function(e)
{
if(document.all)e = event;
if(this.panesAssociative['south'])this.panesAssociative['south'].__autoSlideInPane(e);
if(this.panesAssociative['west'])this.panesAssociative['west'].__autoSlideInPane(e);
if(this.panesAssociative['north'])this.panesAssociative['north'].__autoSlideInPane(e);
if(this.panesAssociative['east'])this.panesAssociative['east'].__autoSlideInPane(e);
}
,
__addEvents : function()
{
var ind = this.objectIndex;
DHTMLSuite.commonObj.addEvent(window,'resize',function() { DHTMLSuite.variableStorage.arrayOfDhtmlSuiteObjects[ind].__positionPanes(); });
DHTMLSuite.commonObj.addEvent(document.documentElement,'mouseup',function() { DHTMLSuite.variableStorage.arrayOfDhtmlSuiteObjects[ind].__endResize(); });
DHTMLSuite.commonObj.addEvent(document.documentElement,'mousemove',function(e) { DHTMLSuite.variableStorage.arrayOfDhtmlSuiteObjects[ind].__resizePane(e); });
DHTMLSuite.commonObj.addEvent(document.documentElement,'click',function(e) { DHTMLSuite.variableStorage.arrayOfDhtmlSuiteObjects[ind].__autoSlideInPanes(e); });
document.documentElement.onselectstart = function() { return DHTMLSuite.commonObj.__getOkToMakeTextSelections(); };
DHTMLSuite.commonObj.__addEventElement(window);
}
}
DHTMLSuite.listModel = function(inputArray)
{
var options;
this.options = new Array();
}
DHTMLSuite.listModel.prototype =
{
addElement : function(value,text)
{
var index = this.options.length;
this.options[index] = new Array();
this.options[index]['value'] = value;
this.options[index]['text'] = text;
}
,
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
addElement : function(inputArray)
{
if(inputArray['labelId'])this.labelId = inputArray['labelId'];
if(inputArray['elementId'])this.elementId = inputArray['elementId'];
if(inputArray['serverFile'])this.serverFile = inputArray['serverFile'];
if(inputArray['listModel'])this.listModel = inputArray['listModel'];
}
}
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
setLayoutCss : function(layoutCSS)
{
this.layoutCSS = layoutCSS;
}
,
setServersideFile : function(serversideFile)
{
this.serversideFile = serversideFile;
}
,
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
,
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
,
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
,
__clickOnLabel : function(e)
{
if(document.all)e = event;
var obj = DHTMLSuite.commonObj.getSrcElement(e);	// Reference to element triggering the event.
this.__setLabelClassName(obj,'active');
var elementId = obj.getAttribute('elementId');
this.__clickOnElement(false,document.getElementById(elementId));
}
,
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
,
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
,
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
,
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
,
__handleServerSideResponse : function(ajaxIndex,url)
{
if(DHTMLSuite.variableStorage.ajaxObjects[ajaxIndex].response!='OK'){
alert('An error occured in the textEdit widget when calling the url\n' + url);
}
DHTMLSuite.variableStorage.ajaxObjects[ajaxIndex] = null;
}
,
__handleAjaxError : function(ajaxIndex,url)
{
alert('Error when calling the url:\n' + url);
DHTMLSuite.variableStorage.ajaxObjects[ajaxIndex] = null;
}
}
var referenceToDHTMLSuiteContextMenu;
DHTMLSuite.contextMenu = function()
{
var menuModels;
var menuItems;
var menuObject;			// Reference to context menu div
var layoutCSS;
var menuUls;			// Array of <ul> elements
var width;				// Width of context menu
var srcElement;			// Reference to the element which triggered the context menu, i.e. the element which caused the context menu to be displayed.
var indexCurrentlyDisplayedMenuModel;	// Index of currently displayed menu model.
this.menuModels = new Array();
this.menuObject = false;
this.layoutCSS = 'context-menu.css';
this.menuUls = new Array();
this.width = 100;
this.srcElement = false;
this.indexCurrentlyDisplayedMenuModel = false;
try{
if(!standardObjectsCreated)DHTMLSuite.createStandardObjects();
}catch(e){
alert('You need to include the dhtmlSuite-common.js file');
}
}
DHTMLSuite.contextMenu.prototype =
{
setWidth : function(newWidth)
{
this.width = newWidth;
}
,
setLayoutCss : function(cssFileName)
{
this.layoutCSS = cssFileName;
}
,
attachToElement : function(element,elementId,menuModel)
{
window.refToThisContextMenu = this;
if(!element && elementId)element = document.getElementById(elementId);
if(!element.id){
element.id = 'context_menu' + Math.random();
element.id = element.id.replace('.','');
}
this.menuModels[element.id] = menuModel;
element.oncontextmenu = this.__displayContextMenu;
element.onmousedown = function() { window.refToThisContextMenu.__setReference(window.refToThisContextMenu); };
DHTMLSuite.commonObj.__addEventElement(element)
DHTMLSuite.commonObj.addEvent(document.documentElement,"click",this.__hideContextMenu);
}
,
__setReference : function(obj)
{
referenceToDHTMLSuiteContextMenu = obj;
}
,
__displayContextMenu : function(e)
{
if(document.all)e = event;
var ref = referenceToDHTMLSuiteContextMenu;
ref.srcElement = DHTMLSuite.commonObj.getSrcElement(e);
if(!ref.indexCurrentlyDisplayedMenuModel || ref.indexCurrentlyDisplayedMenuModel!=this.id){
if(!ref.indexCurrentlyDisplayedMenuModel)DHTMLSuite.commonObj.loadCSS(ref.layoutCSS);
if(ref.indexCurrentlyDisplayedMenuModel){
ref.menuObject.innerHTML = '';
}else{
ref.__createDivs();
}
ref.menuItems = ref.menuModels[this.id].getItems();
ref.__createMenuItems();
}
ref.indexCurrentlyDisplayedMenuModel=this.id;
ref.menuObject.style.left = (e.clientX + Math.max(document.body.scrollLeft,document.documentElement.scrollLeft)) + 'px';
ref.menuObject.style.top = (e.clientY + Math.max(document.body.scrollTop,document.documentElement.scrollTop)) + 'px';
ref.menuObject.style.display='block';
return false;
}
,
__hideContextMenu : function()
{
var ref = referenceToDHTMLSuiteContextMenu;
if(!ref)return;
if(ref.menuObject)ref.menuObject.style.display = 'none';
}
,
__createDivs : function()
{
var firstChild = false;
var firstChilds = document.getElementsByTagName('DIV');
if(firstChilds.length>0)firstChild = firstChilds[0];
this.menuObject = document.createElement('DIV');
this.menuObject.className = 'DHTMLSuite_contextMenu';
this.menuObject.style.backgroundImage = 'url(\'' + DHTMLSuite.configObj.imagePath + 'context-menu-gradient.gif' + '\')';
this.menuObject.style.backgroundRepeat = 'repeat-y';
if(this.width)this.menuObject.style.width = this.width + 'px';
if(firstChild){
firstChild.parentNode.insertBefore(this.menuObject,firstChild);
}else{
document.body.appendChild(this.menuObject);
}
}
,
__mouseOver : function()
{
this.className = 'DHTMLSuite_item_mouseover';
if(!document.all){
this.style.backgroundPosition = 'left center';
}
}
,
__mouseOut : function()
{
this.className = '';
if(!document.all){
this.style.backgroundPosition = '1px center';
}
}
,
__createMenuItems : function()
{
window.refToContextMenu = this;	// Reference to menu strip object
this.menuUls = new Array();
for(var no in this.menuItems){	// Looping through menu items
if(!this.menuUls[0]){	// Create main ul element
this.menuUls[0] = document.createElement('UL');
this.menuObject.appendChild(this.menuUls[0]);
}
if(this.menuItems[no].depth==1){
if(this.menuItems[no].separator){
var li = document.createElement('DIV');
li.className = 'DHTMLSuite_contextMenu_separator';
}else{
var li = document.createElement('LI');
if(this.menuItems[no].jsFunction){
this.menuItems[no].url = this.menuItems[no].jsFunction + '(this,referenceToDHTMLSuiteContextMenu.srcElement)';
}
if(this.menuItems[no].itemIcon){
li.style.backgroundImage = 'url(\'' + this.menuItems[no].itemIcon + '\')';
if(!document.all)li.style.backgroundPosition = '1px center';
}
if(this.menuItems[no].url){
var url = this.menuItems[no].url + '';
li.onclick = function(){ eval(url); };
}
li.innerHTML = '<a href="#" onclick="return false">' + this.menuItems[no].itemText + '</a>';
li.onmouseover = this.__mouseOver;
li.onmouseout = this.__mouseOut;
DHTMLSuite.commonObj.__addEventElement(li);
}
this.menuUls[0].appendChild(li);
}
}
}
}
DHTMLSuite.mediaModel = function(inputArray)
{
var id;
var thumbnailPathSmall;
var thumbnailPath;
var largeImagePath;
var title;
var caption;
try{
if(!standardObjectsCreated)DHTMLSuite.createStandardObjects();
}catch(e){
alert('You need to include the dhtmlSuite-common.js file');
}
if(inputArray)this.addItem(inputArray);
}
DHTMLSuite.mediaModel.prototype =
{
addItem : function(inputArray)
{
this.id = inputArray['id'];
if(inputArray['thumbnailPathSmall'])this.thumbnailPathSmall = inputArray['thumbnailPathSmall'];
if(inputArray['thumbnailPath'])this.thumbnailPath = inputArray['thumbnailPath'];
if(inputArray['largeImagePath'])this.largeImagePath = inputArray['largeImagePath'];
if(inputArray['title'])this.title = inputArray['title'];
if(inputArray['caption'])this.caption = inputArray['caption'];
}
}
DHTMLSuite.mediaCollection = function()
{
var mediaObjects;		// Array of DHTMLSuite.mediaModel objects
this.mediaObjects = new Array();
}
DHTMLSuite.mediaCollection.prototype = {
addItemsFromMarkup : function(elementId)
{
var ul = document.getElementById(elementId);
var lis = ul.getElementsByTagName('LI');
for(var no=0;no<lis.length;no++){
var img = lis[no].getElementsByTagName('IMG')[0];
var index = this.mediaObjects.length;
var mediaArray = new Array();
mediaArray.id = lis[no].id;
if(img){
mediaArray.thumbnailPath = img.src;
}
mediaArray.title = lis[no].title;
mediaArray.caption = lis[no].getAttribute('caption');
mediaArray.largeImagePath = lis[no].getAttribute('largeImagePath');
mediaArray.thumbnailPathSmall = lis[no].getAttribute('thumbnailPathSmall');
this.mediaObjects[index] = new DHTMLSuite.mediaModel(mediaArray);
}
ul.parentNode.removeChild(ul);
}
,
__removeImage : function(idOfMedia)
{
for(var no=0;no<this.mediaObjects.length;no++){
if(this.mediaObjects[no].id==idOfMedia){
var retVal = this.mediaObjects[no].id;
this.mediaObjects.splice(no,1);
return retVal;
}
}
return false;
}
,
getMediaById : function(idOfMedia)
{
for(var no=0;no<this.mediaObjects.length;no++){
if(this.mediaObjects[no].id==idOfMedia){
return this.mediaObjects[no];
}
}
return false;
}
}
DHTMLSuite.floatingGallery = function()
{
var collectionModel;		// Reference to an object of class DHTMLSuite.mediaCollection
var layoutCSS;
var divElement;				// Reference to parent div element.
var divElementImageBoxes;	// Array of div elements for the images
var idOfParentElementToGallery;	// Id of parent element
var callBackFunction_onClick;			// Call back on click
var callBackFunction_onDblClick;		// Call back on dbl click
var callBackFunction_onMouseOver;		// Call back on mouse over
var callBackFunction_onMouseMove;		// Call back mouse move
var imageSelectionObj;					// Object of class DHTMLSuite.imageSelection
var objectIndex;
this.layoutCSS = 'floating-gallery.css';
this.divElementImageBoxes = new Array();
try{
if(!standardObjectsCreated)DHTMLSuite.createStandardObjects();
}catch(e){
alert('You need to include the dhtmlSuite-common.js file');
}
this.objectIndex = DHTMLSuite.variableStorage.arrayOfDhtmlSuiteObjects.length;
DHTMLSuite.variableStorage.arrayOfDhtmlSuiteObjects[this.objectIndex] = this;
}
DHTMLSuite.floatingGallery.prototype =
{
setMediaCollectionRef : function(mediaCollectionRef)
{
this.collectionModel = mediaCollectionRef;
}
,
init : function()
{
try{
DHTMLSuite.commonObj.loadCSS(this.layoutCSS);
}catch(e){
alert('loadCSS method missing. You need to include dhtmlSuite-common.js');
}
this.__createMainDivElement();
this.__createImageBoxes();
this.__initiallyHandleimageSelection();
}
,
setTargetId : function(idOfParentElementToGallery)
{
this.idOfParentElementToGallery = idOfParentElementToGallery;
}
,
setCallBackFunctionOnClick : function(functionName)
{
this.callBackFunction_onClick = functionName;
}
,
setCallBackFunctionOnDblClick : function(functionName)
{
this.callBackFunction_onDblClick = functionName;
}
,
setCallBackFunctionOnMouseOver : function(functionName)
{
this.callBackFunction_onMouseOver = functionName;
}
,
setCallBackFunctionOnMouseMove : function(functionName)
{
this.callBackFunction_onMouseMove = functionName;
}
,
deleteImageFromGallery : function(idOfImage)
{
var retId = this.collectionModel.__removeImage(idOfImage);
if(retId){	// media model image exists with this id ?
var obj = document.getElementById(retId);
obj.parentNode.removeChild(obj);
}else{	// id doesn't match id in media collection model. loop through div elements and check each one by id.
for(var no=0;no<this.divElementImageBoxes.length;no++){
if(this.divElementImageBoxes[no].id == idOfImage){	// Match found
var mediaRefId = this.divElementImageBoxes[no].getAttribute('mediaRefId');	// get a media reference.
if(!mediaRefId)mediaRefId = this.divElementImageBoxes[no].mediaRefId;
var mediaRef = this.collectionModel.getMediaById(mediaRefId);
this.collectionModel.__removeImage(mediaRef.id);	// Remove media from collection mode.
this.divElementImageBoxes[no].parentNode.removeChild(this.divElementImageBoxes[no]);	// remove image from view.
}
}
}
}
,
destroy : function()
{
this.divElement.parentNode.removeChild(this.divElement);
}
,
addImageSelectionObject : function(imageSelectionObj)
{
this.imageSelectionObj = imageSelectionObj;
}
,
__createMainDivElement : function()
{
this.divElement = document.createElement('DIV');
this.divElement.className = 'DHTMLSuite_floatingGalleryContainer';
if(this.idOfParentElementToGallery)
document.getElementById(this.idOfParentElementToGallery).appendChild(this.divElement);
else
document.body.appendChild(this.divElement);
}
,
__createImageBoxes : function()
{
var ind = this.objectIndex;
for(var no=0;no<this.collectionModel.mediaObjects.length;no++){
this.divElementImageBoxes[no] = document.createElement('DIV');
this.divElementImageBoxes[no].className='DHTMLSuite_floatingGalleryImageBox';
this.divElementImageBoxes[no].id=this.collectionModel.mediaObjects[no].id;
this.divElementImageBoxes[no].style.backgroundImage = 'url("' + this.collectionModel.mediaObjects[no].thumbnailPath + '")';
this.divElementImageBoxes[no].setAttribute('mediaRefId',this.collectionModel.mediaObjects[no].id);
this.divElementImageBoxes[no].mediaRefId = this.collectionModel.mediaObjects[no].id;
this.divElement.appendChild(this.divElementImageBoxes[no]);
var titleDiv = document.createElement('DIV');
titleDiv.className='DHTMLSuite_floatingGalleryImageTitle';
titleDiv.innerHTML = this.collectionModel.mediaObjects[no].title;
this.divElementImageBoxes[no].appendChild(titleDiv);
if(this.callBackFunction_onClick)eval("DHTMLSuite.commonObj.addEvent(this.divElementImageBoxes[no],'click',function(){ DHTMLSuite.variableStorage.arrayOfDhtmlSuiteObjects[" + ind + "].__parseCallBackFunction('click'," + no+ "); });");
if(this.callBackFunction_onDblClick)eval("DHTMLSuite.commonObj.addEvent(this.divElementImageBoxes[no],'dblclick',function(){ DHTMLSuite.variableStorage.arrayOfDhtmlSuiteObjects[" + ind + "].__parseCallBackFunction('dblClick'," + no+ "); });");
if(this.callBackFunction_onMouseOver)eval("DHTMLSuite.commonObj.addEvent(this.divElementImageBoxes[no],'mouseover',function(){ DHTMLSuite.variableStorage.arrayOfDhtmlSuiteObjects[" + ind + "].__parseCallBackFunction('mouseOver'," + no+ "); });");
if(this.callBackFunction_onMouseMove)eval("DHTMLSuite.commonObj.addEvent(this.divElementImageBoxes[no],'mousemove',function(){ DHTMLSuite.variableStorage.arrayOfDhtmlSuiteObjects[" + ind + "].__parseCallBackFunction('mouseOver'," + no+ "); });");
}
var clearingDiv = document.createElement('DIV');
clearingDiv.style.clear = 'both';
this.divElement.appendChild(clearingDiv);
}
,
__parseCallBackFunction : function(action,mediaIndex)
{
var callBackString=false;
switch(action){
case "click":
callBackString=this.callBackFunction_onClick;
break;
case "dblClick":
callBackString=this.callBackFunction_onDblClick;
break;
case "mouseOver":
callBackString = this.callBackFunction_onMouseOver;
break;
case "mouseMove":
callBackString = this.callBackFunction_onMouseMove;
break;
}
if(callBackString)callBackString = callBackString + '(this.collectionModel.mediaObjects[' + mediaIndex + '])';
if(!callBackString)return;
try{
eval(callBackString);
}catch(e){
alert('Error in callback :\n' + callBackString + '\n' + e.message);
}
}
,
__initiallyHandleimageSelection : function()
{
if(!this.imageSelectionObj)return;
this.imageSelectionObj.__setMediaCollectionModelReference(this.collectionModel);
for(var no=0;no<this.divElementImageBoxes.length;no++){
this.imageSelectionObj.addSelectableElement(this.divElementImageBoxes[no]);
this.divElementImageBoxes[no].onselectstart = DHTMLSuite.commonObj.cancelEvent;
var subs = this.divElementImageBoxes[no].getElementsByTagName('*');
for(var no2=0;no2<subs.length;no2++)subs[no2].onselectstart = DHTMLSuite.commonObj.cancelEvent;
}
}
}
DHTMLSuite.imageSelection = function(){
var layoutCSS;
var callBackFunction_onDrop;		// Call back function on drop
var objectIndex;
var divElementSelection;
var divElementSelection_transparent;
var selectableElements;
var selectedElements;			// Array of selected elements
var selectableElementsScreenProperties;
var collectionModelReference;	// Reference to media collection ( data source for images )
var destinationElements;		// Array of destination elements.
var currentDestinationElement;	// Reference to current destination element
var selectionStatus;			// -1 when selection isn't in progress, 0 when it's being initialized, 5 when it's ready
var dragStatus;					// -1 = drag not started, 0-5 = drag initializing, 5 = drag in process.
var startCoordinates;
var selectionResizeInProgress;	// variable which is true when code for the selection area is running
var selectionStartArea;			// Selection can only starts within this element
var selectionOrDragStartEl;		// Element triggering drag or selection
var cssTextForSources;			// Manual css text for sources - you can use this instead of the imageSelection class. This may be useful when you have a lot of nodes
this.cssTextForSources = '';
this.selectionResizeInProgress = false;
this.selectionStatus = -1;
this.dragStatus = -1;
this.startCoordinates = new Array();
this.layoutCSS = 'image-selection.css';
this.selectableElements = new Array();
this.destinationElements = new Array();
this.collectionModelReference = false;
this.selectableElementsScreenProperties = new Array();
this.selectedElements = new Array();
try{
if(!standardObjectsCreated)DHTMLSuite.createStandardObjects();	// This line starts all the init methods
}catch(e){
alert('You need to include the dhtmlSuite-common.js file');
}
this.objectIndex = DHTMLSuite.variableStorage.arrayOfDhtmlSuiteObjects.length;
DHTMLSuite.variableStorage.arrayOfDhtmlSuiteObjects[this.objectIndex] = this;
}
DHTMLSuite.imageSelection.prototype = {
init : function()
{
try{
DHTMLSuite.commonObj.loadCSS(this.layoutCSS);
}catch(e){
alert('Unable to load css file dynamically. You need to include dhtmlSuite-common.js');
}
this.__createdivElementsForSelection();
this.__createdivElementsForDrag();
this.__addEvents();
this.__setSelectableElementsScreenProperties();
}
,
addDestinationElement : function(elementReference)
{
if(typeof elementReference == 'string'){
elementReference = document.getElementById(elementReference);
}
this.destinationElements[this.destinationElements.length] = elementReference;
}
,
addDestinationElementsByTagName : function(parentElementReference,tagName,className)
{
if(typeof parentElementReference == 'string'){
parentElementReference = document.getElementById(parentElementReference);
}
if(!parentElementReference){
return false;
}
var subs = parentElementReference.getElementsByTagName(tagName);
for(var no=0;no<subs.length;no++){
if(className && subs[no].className!=className)continue;
this.destinationElements[this.destinationElements.length] = subs[no];
}
this.__addEventsToDestinationElements(subs);
return true;
}
,
addSelectableElements : function(parentElementReference)
{
var obj;
if(typeof parentElementReference == 'string')obj = document.getElementById(parentElementReference);else obj = parentElementReference;
var subElement = obj.getElementsByTagName('*')[0];
while(subElement){
this.selectableElements[this.selectableElements.length] = subElement;
this.__addPropertiesToSelectableElement(subElement);
subElement = subElement.nextSibling;
}
}
,
addSelectableElement : function(elementReference)
{
if(typeof elementReference == 'string'){
this.selectableElements[this.selectableElements.length] = document.getElementById(elementReference);
}else{
this.selectableElements[this.selectableElements.length] = elementReference;
}
this.__addPropertiesToSelectableElement(elementReference);
}
,
setCallBackFunctionOnDrop : function(functionName)
{
this.callBackFunction_onDrop = functionName;
}
,
setSelectionStartArea : function(elementReference)
{
if(typeof elementReference == 'string'){
elementReference = document.getElementById(elementReference);
}
this.selectionStartArea = elementReference;
}
,
__createdivElementsForSelection : function()
{
this.divElementSelection = document.createElement('DIV');
this.divElementSelection.style.display='none';
this.divElementSelection.id = 'DHTMLSuite_imageSelectionSel';
document.body.insertBefore(this.divElementSelection,document.body.firstChild);
this.divElementSelection_transparent = document.createElement('DIV');
this.divElementSelection_transparent.id = 'DHTMLSuite_imageSelection_transparentDiv';
this.divElementSelection.appendChild(this.divElementSelection_transparent);
}
,
__setMediaCollectionModelReference : function(collectionModelReference){
this.collectionModelReference = collectionModelReference;
}
,
__createdivElementsForDrag : function()
{
this.divElementDrag = document.createElement('DIV');
this.divElementDrag.style.display='none';
this.divElementDrag.id = 'DHTMLSuite_imageSelectionDrag';
document.body.insertBefore(this.divElementDrag,document.body.firstChild);
this.divElementDragContent = document.createElement('DIV');
this.divElementDragContent.id = 'DHTMLSuite_imageSelectionDragContent';
this.divElementDrag.appendChild(this.divElementDragContent);
var divElementTrans = document.createElement('DIV');
divElementTrans.className = 'DHTMLSuite_imageSelectionDrag_transparentDiv';
this.divElementDrag.appendChild(divElementTrans);
}
,
__initImageSelection : function(e)
{
var initImageSelector;
if(document.all)e = event;
var src = DHTMLSuite.commonObj.getSrcElement(e);
if(src.onmousedown){	/* Exception for the drag */
if(src.onmousedown.toString().indexOf('initImageSelector')<0)return;
}
if(src.className.indexOf('paneSplitter_vertical')>=0 || src.className.indexOf('paneSplitter_horizontal')>=0 ){	/* Exception for the drag */
return;
}
this.selectionOrDragStartEl = src;
this.startCoordinates['x'] = e.clientX + document.documentElement.scrollLeft + 3;
this.startCoordinates['y'] = e.clientY + document.documentElement.scrollTop + 3;
if(!this.__isReadyForDrag(e)){	/* Image selection */
if(!e.shiftKey && !e.ctrlKey)this.__clearSelectedElementsArray();
this.selectionStatus = 0;
this.divElementSelection.style.left = this.startCoordinates['x'] + 'px';
this.divElementSelection.style.top = this.startCoordinates['y'] + 'px';
this.divElementSelection.style.width = '1px';
this.divElementSelection.style.height = '1px';
this.__setSelectableElementsScreenProperties();
this.__countDownToSelectionStart();
}else{	/* Drag selected images */
this.divElementDrag.style.left = this.startCoordinates['x'] + 'px';
this.divElementDrag.style.top = this.startCoordinates['y'] + 'px';
this.dragStatus = 0;
this.__countDownToDragStart();
}
return false;
}
,
__isReadyForDrag : function(e)
{
var src = DHTMLSuite.commonObj.getObjectByAttribute(e,'DHTMLSuite_selectableElement');
if(!src)return false;
if(this.selectedElements.length>0)return true;
return false;
}
,
__countDownToDragStart : function()
{
if(this.dragStatus>=0 && this.dragStatus<5){
var ind = this.objectIndex;
this.dragStatus++;
setTimeout('DHTMLSuite.variableStorage.arrayOfDhtmlSuiteObjects[' + ind + '].__countDownToDragStart()',30);
}
if(this.dragStatus==5){
this.__fillDragBoxWithSelectedItems();
this.divElementDrag.style.display='block';	// Show selection box.
}
}
,
__fillDragBoxWithSelectedItems : function()
{
this.divElementDragContent.innerHTML = '';
if(this.collectionModelReference){	/* Media model exists */
for(var no=0;no<this.selectedElements.length;no++){
var obj = this.selectedElements[no];
var mediaRefId = obj.getAttribute('mediaRefId');
if(!mediaRef)mediaRef = obj.mediaRefId;
var mediaRef = this.collectionModelReference.getMediaById(mediaRefId);
var div = document.createElement('DIV');
div.className = 'DHTMLSuite_imageSelectionDragBox';
div.style.backgroundImage = 'url(\'' + mediaRef.thumbnailPathSmall + '\')';
this.divElementDragContent.appendChild(div);
}
}else{	/* No media model - Just clone the node - May have to figure out something more clever here as this hasn't been tested fully yet */
for(var no=0;no<this.selectedElements.length;no++){
var el = this.selectedElements.cloneNode(true);
this.divElementDragContent.appendChild(el);
}
}
}
,
__countDownToSelectionStart : function()
{
if(this.selectionStatus>=0 && this.selectionStatus<5){
var ind = this.objectIndex;
this.selectionStatus++;
setTimeout('DHTMLSuite.variableStorage.arrayOfDhtmlSuiteObjects[' + ind + '].__countDownToSelectionStart()',10);
}
if(this.selectionStatus==5)this.divElementSelection.style.display='block';	// Show selection box.
return false;
}
,
__moveDragBox : function(e)
{
if(this.dragStatus<5)return;
if(document.all)e = event;
this.divElementDrag.style.left = (this.startCoordinates['x'] + (e.clientX + 3 - this.startCoordinates['x'])) + 'px';
this.divElementDrag.style.top = (this.startCoordinates['y'] + (e.clientY + 3 - this.startCoordinates['y'])) + 'px';
}
,
__resizeSelectionDivBox : function(e)
{
if(this.selectionStatus<5)return;	// Selection in progress ?
if(this.selectionResizeInProgress)return;	// If this function is allready running, don't start another iteration until it's finished.
this.selectionResizeInProgress = true;	// Selection code is running!
if(document.all)e = event;
var width = e.clientX - this.startCoordinates['x'];
var height = e.clientY + document.documentElement.scrollTop - this.startCoordinates['y'];
if(width>0){
this.divElementSelection.style.left = (this.startCoordinates['x']) + 'px';
this.divElementSelection.style.width = width + 'px';
}else{
this.divElementSelection.style.width = (this.startCoordinates['x'] - (this.startCoordinates['x'] + width)) + 'px';
this.divElementSelection.style.left =(this.startCoordinates['x'] + width) + 'px';
}
if(height>0){
this.divElementSelection.style.top = (this.startCoordinates['y']) + 'px';
this.divElementSelection.style.height = height + 'px';
}else{
this.divElementSelection.style.height = (this.startCoordinates['y'] - (this.startCoordinates['y'] + height)) + 'px';
this.divElementSelection.style.top =(this.startCoordinates['y'] + height) + 'px';
}
this.__highlightElementsWithinSelectionArea();
this.selectionResizeInProgress = false;
}
,
__clearSingleElementFromSelectedArray : function(el)
{
for(var no=0;no<this.selectedElements.length;no++){
if(this.selectedElements[no]==el){
this.selectedElements[no].className = this.selectedElements[no].className.replace(' imageSelection','');
this.selectedElements.splice(no,1);
return;
}
}
}
,
__clearSelectedElementsArray : function()
{
for(var no=0;no<this.selectedElements.length;no++){
if(this.selectedElements[no].className.indexOf('imageSelection')>=0)this.selectedElements[no].className = this.selectedElements[no].className.replace(' imageSelection','');
}
this.selectedElements = new Array();
}
,
__highlightElementsWithinSelectionArea : function()
{
var x1 = this.divElementSelection.style.left.replace('px','')/1;
var y1 = this.divElementSelection.style.top.replace('px','')/1;
var x2 = x1 + this.divElementSelection.style.width.replace('px','')/1;
var y2 = y1 + this.divElementSelection.style.height.replace('px','')/1;
for(var no=0;no<this.selectableElements.length;no++){
if(this.__isElementWithinSelectionArea(this.selectableElements[no],x1,y1,x2,y2)){
this.__addSelectedElement(this.selectableElements[no]);
}else{
this.__clearSingleElementFromSelectedArray(this.selectableElements[no]);
}
}
}
,
__isElementInSelectedArray : function(el)
{
for(var no=0;no<this.selectedElements.length;no++){	/* element allready added ? */
if(this.selectedElements[no]==el)return true;
}
return false;
}
,
__addSelectedElement : function(el)
{
if(el.className.indexOf('imageSelection')==-1){
if(el.className)
el.className = el.className + ' imageSelection';	// Adding " imageSelection" to the class name
else
el.className = 'imageSelection';
}
if(this.__isElementInSelectedArray(el))return;
this.selectedElements[this.selectedElements.length] = el;	// Add element to selected elements array
}
,
__isElementWithinSelectionArea : function(el,x1,y1,x2,y2)
{
var elX1 = this.selectableElementsScreenProperties[el.id].x;
var elY1 = this.selectableElementsScreenProperties[el.id].y;
var elX2 = this.selectableElementsScreenProperties[el.id].x + this.selectableElementsScreenProperties[el.id].width;
var elY2 = this.selectableElementsScreenProperties[el.id].y + this.selectableElementsScreenProperties[el.id].height;
if(elX2<x1)return false;
if(elY2<y1)return false;
if(elX1>x2)return false;
if(elY1>y2)return false;
if((elY1<=y1 && elY2>=y1) || (elY1>=y1 && elY2<=y2) || (elY1<=y2 && elY2>=y2)){	/* Y coordinates of element within selection area */
if(elX1<=x1 && elX2>=x1)return true;	/* left edge of element at the left of selection area, but right edge within */
if(elX1>=x1 && elX2<=x2)return true;	/* Both left and right edge of element within selection area */
if(elX1<=x2 && elX2>=x2)return true;	/* Left edge of element within selection area, but right element outside */
}
return false;
}
,
__setSelectableElementsScreenProperties : function()
{
for(var no=0;no<this.selectableElements.length;no++){
var obj = this.selectableElements[no];
var id = obj.id;
this.selectableElementsScreenProperties[id] = new Array();
var ref = this.selectableElementsScreenProperties[id];
ref.x = DHTMLSuite.commonObj.getLeftPos(obj);
ref.y = DHTMLSuite.commonObj.getTopPos(obj);
ref.width = obj.offsetWidth;
ref.height = obj.offsetHeight;
}
}
,
__endImageSelection : function(e)
{
if(document.all)e = event;
if(this.selectionStatus>=0){
this.divElementSelection.style.display='none';
if(this.__isReadyForDrag(e) && this.selectionStatus==-1)this.__clearSelectedElementsArray();
this.selectionStatus = -1;
}
if(this.dragStatus>=0){
var src = DHTMLSuite.commonObj.getSrcElement(e);
if(this.currentDestinationElement)this.__handleCallBackFunctions('drop');
this.divElementDrag.style.display='none';
if(src!=this.selectionOrDragStartEl || !src.className)this.__clearSelectedElementsArray();
this.__deselectDestinationElement();
this.dragStatus = -1;
}
}
,
__handleCallBackFunctions : function(action)
{
var callbackString = '';
switch(action)
{
case 'drop':
if(this.callBackFunction_onDrop)callbackString = this.callBackFunction_onDrop;
break;
}
if(callbackString)eval(callbackString + '(this.selectedElements,this.currentDestinationElement)');
}
,
__deselectDestinationElement : function(e)
{
if(this.dragStatus<5)return;
if(!this.currentDestinationElement)return;
if(document.all)e = event;
if(e && !DHTMLSuite.commonObj.isObjectClicked(this.currentDestinationElement,e))return;
this.currentDestinationElement.className = this.currentDestinationElement.className.replace(' imageSelection','');
this.currentDestinationElement.className = this.currentDestinationElement.className.replace('imageSelection','');
this.currentDestinationElement = false;
}
,
__selectDestinationElement : function(e){
if(this.dragStatus<5)return;
if(document.all)e = event;
var src = DHTMLSuite.commonObj.getObjectByAttribute(e,'imageSelectionDestination');
this.currentDestinationElement = src;
if(this.currentDestinationElement.className)
this.currentDestinationElement.className = this.currentDestinationElement.className + ' imageSelection';
else
this.currentDestinationElement.className = 'imageSelection';
}
,
__selectSingleElement : function(e,eventType)
{
if(document.all) e = event;
var src = DHTMLSuite.commonObj.getObjectByAttribute(e,'DHTMLSuite_selectableElement');
if(eventType=='mousedown' && 1==2){
this.__addSelectedElement(src);
}else{
var elementAllreadyInSelectedArray =this.__isElementInSelectedArray(src);
if(!e.ctrlKey && !elementAllreadyInSelectedArray)this.__clearSelectedElementsArray();
if(e.ctrlKey && elementAllreadyInSelectedArray){
this.__clearSingleElementFromSelectedArray(src);
}else
this.__addSelectedElement(src);
}
}
,
__addPropertiesToSelectableElement : function(elementReference)
{
var ind = this.objectIndex;
DHTMLSuite.commonObj.addEvent(elementReference,'mousedown',function(e){ return DHTMLSuite.variableStorage.arrayOfDhtmlSuiteObjects[ind].__selectSingleElement(e,'mousedown'); });	// Add click event to single element
elementReference.setAttribute('DHTMLSuite_selectableElement','1');
this.__addOnScrolleventsToSelectableElements(elementReference);
}
,
__addEventsToDestinationElements : function(inputElements)
{
var ind = this.objectIndex;
if(inputElements){
for(var no=0;no<inputElements.length;no++){
inputElements[no].onmouseover = function(e){ return DHTMLSuite.variableStorage.arrayOfDhtmlSuiteObjects[ind].__selectDestinationElement(e); };
inputElements[no].onmouseout = function(e){ return DHTMLSuite.variableStorage.arrayOfDhtmlSuiteObjects[ind].__deselectDestinationElement(e); };
inputElements[no].setAttribute('imageSelectionDestination','1');
inputElements[no].imageSelectionDestination = '1';
}
}else{
for(var no=0;no<this.destinationElements.length;no++){
this.destinationElements[no].onmouseover = function(e){ return DHTMLSuite.variableStorage.arrayOfDhtmlSuiteObjects[ind].__selectDestinationElement(e); };
this.destinationElements[no].onmouseout = function(e){ return DHTMLSuite.variableStorage.arrayOfDhtmlSuiteObjects[ind].__deselectDestinationElement(e); };
this.destinationElements[no].setAttribute('imageSelectionDestination','1');
this.destinationElements[no].imageSelectionDestination = '1';
}
}
}
,
__addOnScrolleventsToSelectableElements : function(el)
{
var ind = this.objectIndex;
var src = el;
while(src && src.tagName.toLowerCase()!='body'){
src = src.parentNode;
if(!src.onscroll)DHTMLSuite.commonObj.addEvent(src,'scroll',function(e){ return DHTMLSuite.variableStorage.arrayOfDhtmlSuiteObjects[ind].__endImageSelection(e); });
}
}
,
__addEvents : function()
{
var ind = this.objectIndex;
document.documentElement.onselectstart = DHTMLSuite.commonObj.cancelEvent;	// disable text selection
if(this.selectionStartArea){
DHTMLSuite.commonObj.addEvent(this.selectionStartArea,'mousedown',function(e){ return DHTMLSuite.variableStorage.arrayOfDhtmlSuiteObjects[ind].__initImageSelection(e); });
}else{
DHTMLSuite.commonObj.addEvent(document.documentElement,'mousedown',function(e){ return DHTMLSuite.variableStorage.arrayOfDhtmlSuiteObjects[ind].__initImageSelection(e); });
}
DHTMLSuite.commonObj.addEvent(document.documentElement,'mousemove',function(e){ return DHTMLSuite.variableStorage.arrayOfDhtmlSuiteObjects[ind].__resizeSelectionDivBox(e); });
DHTMLSuite.commonObj.addEvent(document.documentElement,'mousemove',function(e){ return DHTMLSuite.variableStorage.arrayOfDhtmlSuiteObjects[ind].__moveDragBox(e); });
DHTMLSuite.commonObj.addEvent(document.documentElement,'mouseup',function(e){ return DHTMLSuite.variableStorage.arrayOfDhtmlSuiteObjects[ind].__endImageSelection(e); });
DHTMLSuite.commonObj.addEvent(window,'resize',function(){ return DHTMLSuite.variableStorage.arrayOfDhtmlSuiteObjects[ind].__setSelectableElementsScreenProperties(); });
var imgs = document.getElementsByTagName('IMG');
for(var no=0;no<imgs.length;no++){
imgs[no].ondragstart = DHTMLSuite.commonObj.cancelEvent;
if(!imgs[no].onmousedown)imgs[no].onmousedown = DHTMLSuite.commonObj.cancelEvent;
DHTMLSuite.commonObj.__addEventElement(imgs[no]);
}
this.__addEventsToDestinationElements();
}
}
DHTMLSuite.imageEnlarger = function(){
var layoutCSS;
this.layoutCSS = 'image-enlarger.css';
var divElement;
var divElementImageBox;
var divElementInner;
var iframeElement;
var currentImagePath;
var objectIndex;
var transparentDiv;
var captionDiv;
var msieOpacity;
var isDragable;
var isModal;
this.isDragable = false;
this.msieOpacity = 50;
this.isModal = true;
var shadowSize;
var resizeTransparentAllowed;
var closeLinkTxt;
var dragObject;
var dragOffsetX;
var dragOffsetY;
this.dragOffsetX = 0;
this.dragOffsetY = 0;
this.closeLinkTxt = 'Close';
this.shadowSize = 1;
this.resizeTransparentAllowed = true;
try{
if(!standardObjectsCreated)DHTMLSuite.createStandardObjects();	// This line starts all the init methods
}catch(e){
alert('You need to include the dhtmlSuite-common.js file');
}
this.objectIndex = DHTMLSuite.variableStorage.arrayOfDhtmlSuiteObjects.length;
DHTMLSuite.variableStorage.arrayOfDhtmlSuiteObjects[this.objectIndex] = this;
}
DHTMLSuite.imageEnlarger.prototype = {
displayImage : function(imagePath,title,description)
{
DHTMLSuite.commonObj.loadCSS(this.layoutCSS);
if(!this.divElement)this.__createHTMLElements();
this.__resizeTransparentDiv();
this.__clearHTMLElement();
this.__addImageElement(imagePath);
this.__setCaptionText(title,description);
this.__displayDivElement();
this.currentImagePath = imagePath;
}
,
setIsDragable : function(isDragable)
{
this.isDragable = isDragable;
}
,
setIsModal : function(isModal)
{
this.isModal = isModal;
}
,
setDragOffset : function(dragOffsetX,dragOffsetY)
{
this.dragOffsetX = dragOffsetX;
this.dragOffsetY = dragOffsetY;
}
,
hide : function()
{
this.__hideDivElement();
return false;
}
,
setCloseLinkTxt : function(closeLinkTxt)
{
this.closeLinkTxt = closeLinkTxt;
}
,
setLayoutCss : function(newLayoutCss)
{
this.layoutCSS = newLayoutCss;
}
,
setLayoutCss : function(shadowSize)
{
this.shadowSize = shadowSize;
}
,
__createHTMLElements : function()
{
var ind = this.objectIndex;
this.divElement = document.createElement('DIV');
this.divElement.className='DHTMLSuite_imageEnlarger';
this.divElement.ondragstart = DHTMLSuite.commonObj.cancelEvent;
DHTMLSuite.commonObj.__addEventElement(this.divElement);
document.body.appendChild(this.divElement);
this.divElementInner = document.createElement('DIV');
this.divElementInner.className = 'DHTMLSuite_imageEnlarger_imageBox';
this.divElement.appendChild(this.divElementInner);
this.divElementImageBox = document.createElement('DIV');
this.divElementInner.appendChild(this.divElementImageBox);
this.transparentDiv = document.createElement('DIV');
this.transparentDiv.className = 'DHTMLSuite_imageEnlarger_transparentDivs';
document.body.appendChild(this.transparentDiv);
this.transparentDiv.style.display='none';
this.transparentDiv.style.filter = 'alpha(opacity=' + this.msieOpacity + ')';
DHTMLSuite.commonObj.addEvent(window,'resize',function(){ DHTMLSuite.variableStorage.arrayOfDhtmlSuiteObjects[ind].__resizeTransparentDiv(); });
var closeButton = document.createElement('DIV');
closeButton.className = 'DHTMLSuite_imageEnlarger_close';
closeButton.onmouseover = this.__mouseOverEffectCloseButton;
closeButton.onmouseout = this.__mouseoutCalendarButton;
DHTMLSuite.commonObj.addEvent(closeButton,'click',function(){ DHTMLSuite.variableStorage.arrayOfDhtmlSuiteObjects[ind].hide(); });
this.divElementInner.appendChild(closeButton);
this.captionDiv = document.createElement('DIV');
this.captionDiv.className = 'DHTMLSuite_imageEnlarger_caption';
this.divElementInner.appendChild(this.captionDiv);
if(DHTMLSuite.clientInfoObj.isMSIE){
this.iframeElement = document.createElement('<iframe frameborder=0 src="about:blank" scrolling="no">');
this.iframeElement.className = 'DHTMLSuite_imageEnlarger_iframe';
this.divElement.appendChild(this.iframeElement);
}
if(this.isDragable){
setTimeout('DHTMLSuite.variableStorage.arrayOfDhtmlSuiteObjects[' + ind + '].__makeElementDragable()',1);
}
}
,
__makeElementDragable : function()
{
try{
this.dragObject = new DHTMLSuite.dragDropSimple(this.divElement,false,this.dragOffsetX,this.dragOffsetY);
}catch(e){
alert('You need to include DHTMLSuite-dragDropSimple.js for the drag feature');
}
}
,
__mouseOverEffectCloseButton : function()
{
this.className = 'DHTMLSuite_imageEnlarger_closeOver';
}
,
__mouseoutCalendarButton : function()
{
this.className = 'DHTMLSuite_imageEnlarger_close';
}
,
__clearHTMLElement : function()
{
this.divElementImageBox.innerHTML = '';
}
,
__displayDivElement : function()
{
this.divElement.style.visibility = 'hidden';
if(this.isModal)this.transparentDiv.style.display='block';
if(this.iframeElement)this.iframeElement.style.display='block';
}
,
__hideDivElement : function()
{
this.divElement.style.visibility = 'hidden';
this.transparentDiv.style.display='none';
if(this.iframeElement){
this.iframeElement.style.display='none';
this.iframeElement.style.height = '1px';
this.iframeElement.style.width = '1px';
}
}
,
__resizeTransparentDiv : function()
{
var ind = this.objectIndex;
if(!this.resizeTransparentAllowed)return;
this.resizeTransparentAllowed = false;
var divHeight = Math.max(document.body.clientHeight,document.documentElement.clientHeight,document.documentElement.scrollHeight);
var divWidth = Math.max(document.body.clientWidth,document.documentElement.clientWidth);
this.transparentDiv.style.width = divWidth + 'px';
this.transparentDiv.style.height = divHeight + 'px';
setTimeout('DHTMLSuite.variableStorage.arrayOfDhtmlSuiteObjects[' + ind + '].resizeTransparentAllowed=true',10);
}
,
__addImageElement : function(imagePath)
{
var ind = this.objectIndex;
var img = document.createElement('IMG');
this.divElementImageBox.appendChild(img);
DHTMLSuite.commonObj.addEvent(img,'resize',function(){ DHTMLSuite.variableStorage.arrayOfDhtmlSuiteObjects[ind].__repositionHTMLElement(); });
DHTMLSuite.commonObj.addEvent(img,'load',function(){ DHTMLSuite.variableStorage.arrayOfDhtmlSuiteObjects[ind].__repositionHTMLElement(); });
img.src = imagePath;
}
,
__setCaptionText : function(title,description)
{
var ind = this.objectIndex;
var txt = '';
if(title)txt = '<span class="DHTMLSuite_imageEnlarger_captionTitle">' + title + '</span>'
if(description)txt = txt + '<span class="DHTMLSuite_imageEnlarger_captionDescription">' + description + '</span>';
if(this.closeLinkTxt)txt = txt + '<a class="DHTMLSuite_imageEnlarger_closeLink" href="#" onclick="return DHTMLSuite.variableStorage.arrayOfDhtmlSuiteObjects[' + ind + '].hide()">' + this.closeLinkTxt + '</a>';
this.captionDiv.innerHTML = txt;
}
,
__repositionHTMLElement : function(internalCall)
{
var ind = this.objectIndex;
var imgs = this.divElementImageBox.getElementsByTagName('IMG');
var img = imgs[0];
this.divElementImageBox.style.width = img.width + 'px';
this.divElementImageBox.style.height = img.height + 'px';
this.divElement.style.width = (this.divElementInner.offsetWidth + this.shadowSize) + 'px';
this.divElement.style.height = (this.divElementInner.offsetHeight + this.shadowSize) + 'px';
this.divElementInner.style.width = this.divElementImageBox.offsetWidth + 'px';
this.divElementInner.style.height = (this.divElementImageBox.offsetHeight + this.captionDiv.offsetHeight) + 'px';
if(this.isDragable){
this.divElement.style.left = (DHTMLSuite.clientInfoObj.getBrowserWidth()/2 - this.divElement.offsetWidth/2) + 'px';
this.divElement.style.top = (DHTMLSuite.clientInfoObj.getBrowserHeight()/2 - this.divElement.offsetHeight/2) + 'px';
this.divElement.style.marginLeft = '0px';
this.divElement.style.marginTop = '0px';
this.divElement.style.cursor = 'move';
}else{
this.divElement.style.marginLeft = Math.round(this.divElementImageBox.offsetWidth/2 *-1) + 'px';
this.divElement.style.marginTop = Math.round(this.divElementImageBox.offsetHeight/2 *-1) + 'px';
}
if(this.iframeElement){
this.iframeElement.style.width = this.divElementInner.style.width;
this.iframeElement.style.height = this.divElementInner.style.height;
}
if(!internalCall){
setTimeout('DHTMLSuite.variableStorage.arrayOfDhtmlSuiteObjects[' + ind + '].__repositionHTMLElement(true)',50);
}else this.divElement.style.visibility = 'visible';
}
}
DHTMLSuite.calendarLanguageModel = function(languageCode){
var monthArray;							// An array of months.
var monthArrayShort;					// An array of the months, short version
var dayArray;							// An array of the days in a week
var weekString;							// String representation of the string "Week"
var todayString;						// String representatinon of the string "Today"
var todayIsString;						// String representation of the string "Today is"
var timeString;							// String representation of the string "Time"
this.monthArray = new Array();
this.monthArrayShort = new Array();
this.dayArray = new Array();
if(!languageCode)languageCode = 'en';
this.languageCode = languageCode;
this.__setCalendarProperties();
}
DHTMLSuite.calendarLanguageModel.prototype = {
__setCalendarProperties : function()
{
switch(this.languageCode){
case "ge":	/* German */
this.monthArray = ['Januar','Februar','März','April','Mai','Juni','Juli','August','September','Oktober','November','Dezember'];
this.monthArrayShort = ['Jan','Feb','Mar','Apr','Mai','Jun','Jul','Aug','Sep','Okt','Nov','Dez'];
this.dayArray = ['Mon','Die','Mit','Don','Fre','Sam','Son'];
this.weekString = 'Woche';
this.todayIsString = 'Heute';
this.todayString = 'Heute';
this.timeString = '';
break;
case "no":	/* Norwegian */
this.monthArray = ['Januar','Februar','Mars','April','Mai','Juni','Juli','August','September','Oktober','November','Desember'];
this.monthArrayShort = ['Jan','Feb','Mar','Apr','Mai','Jun','Jul','Aug','Sep','Okt','Nov','Des'];
this.dayArray = ['Man','Tir','Ons','Tor','Fre','L&oslash;r','S&oslash;n'];
this.weekString = 'Uke';
this.todayIsString = 'Dagen i dag er';
this.todayString = 'I dag';
this.timeString = 'Tid';
break;
case "nl":	/* Dutch */
this.monthArray = ['Januari','Februari','Maart','April','Mei','Juni','Juli','Augustus','September','Oktober','November','December'];
this.monthArrayShort = ['Jan','Feb','Mar','Apr','Mei','Jun','Jul','Aug','Sep','Okt','Nov','Dec'];
this.dayArray = ['Ma','Di','Wo','Do','Vr','Za','Zo'];
this.weekString = 'Week';
this.todayIsString = 'Vandaag';
this.todayString = 'Vandaag';
this.timeString = '';
break;
case "es": /* Spanish */
this.monthArray = ['Enero','Febrero','Marzo','April','Mayo','Junio','Julio','Agosto','Septiembre','Octubre','Noviembre','Diciembre'];
this.monthArrayShort =['Ene','Feb','Mar','Abr','May','Jun','Jul','Ago','Sep','Oct','Nov','Dic'];
this.dayArray = ['Lun','Mar','Mie','Jue','Vie','Sab','Dom'];
this.weekString = 'Semana';
this.todayIsString = 'Hoy es';
this.todayString = 'Hoy';
this.timeString = '';
break;
case "pt-br":  /* Brazilian portuguese (pt-br) */
this.monthArray = ['Janeiro','Fevereiro','Mar&ccedil;o','Abril','Maio','Junho','Julho','Agosto','Setembro','Outubro','Novembro','Dezembro'];
this.monthArrayShort = ['Jan','Fev','Mar','Abr','Mai','Jun','Jul','Ago','Set','Out','Nov','Dez'];
this.dayArray = ['Seg','Ter','Qua','Qui','Sex','S&aacute;b','Dom'];
this.weekString = 'Sem.';
this.todayIsString = 'Hoje &eacute;';
this.todayString = 'Hoje';
this.timeString = '';
break;
case "fr":      /* French */
this.monthArray = ['Janvier','Février','Mars','Avril','Mai','Juin','Juillet','Août','Septembre','Octobre','Novembre','Décembre'];
this.monthArrayShort = ['Jan','Fev','Mar','Avr','Mai','Jun','Jul','Aou','Sep','Oct','Nov','Dec'];
this.dayArray = ['Lun','Mar','Mer','Jeu','Ven','Sam','Dim'];
this.weekString = 'Sem';
this.todayIsString = "Aujourd'hui";
this.todayString = 'Aujourd';
this.timeString = '';
break;
case "da": /*Danish*/
this.monthArray = ['januar','februar','marts','april','maj','juni','juli','august','september','oktober','november','december'];
this.monthArrayShort = ['jan','feb','mar','apr','maj','jun','jul','aug','sep','okt','nov','dec'];
this.dayArray = ['man','tirs','ons','tors','fre','l&oslash;r','s&oslash;n'];
this.weekString = 'Uge';
this.todayIsString = 'I dag er den';
this.todayString = 'I dag';
this.timeString = 'Tid';
break;
case "it":	/* Italian*/
this.monthArray = ['Gennaio','Febbraio','Marzo','Aprile','Maggio','Giugno','Luglio','Agosto','Settembre','Ottobre','Novembre','Dicembre'];
this.monthArrayShort = ['Gen','Feb','Mar','Apr','Mag','Giu','Lugl','Ago','Set','Ott','Nov','Dic'];
this.dayArray = ['Lun',';Mar','Mer','Gio','Ven','Sab','Dom'];
this.weekString = 'Settimana';
this.todayIsString = 'Oggi &egrave; il';
this.todayString = 'Oggi &egrave; il';
this.timeString = '';
break;
case "sv":	/* Swedish */
this.monthArray = ['Januari','Februari','Mars','April','Maj','Juni','Juli','Augusti','September','Oktober','November','December'];
this.monthArrayShort = ['Jan','Feb','Mar','Apr','Maj','Jun','Jul','Aug','Sep','Okt','Nov','Dec'];
this.dayArray = ['M&aring;n','Tis','Ons','Tor','Fre','L&ouml;r','S&ouml;n'];
this.weekString = 'Vecka';
this.todayIsString = 'Idag &auml;r det den';
this.todayString = 'Idag &auml;r det den';
this.timeString = '';
break;
default:	/* English */
this.monthArray = ['January','February','March','April','May','June','July','August','September','October','November','December'];
this.monthArrayShort = ['Jan','Feb','Mar','Apr','May','Jun','Jul','Aug','Sep','Oct','Nov','Dec'];
this.dayArray = ['Mon','Tue','Wed','Thu','Fri','Sat','Sun'];
this.weekString = 'Week';
this.todayIsString = '';
this.todayString = 'Today';
this.timeString = 'Time';
break;
}
}
}
DHTMLSuite.calendarModel = function(inputArray){
var initialDay;							// Initial day (i.e. day in month)
var initialMonth;						// Initial month(1-12)
var initialYear;						// Initial Year(4 digits)
var initialHour;						// Initial Hour(0-23)
var initialMinute;						// Initial Minute
var displayedDay;						// Currently displayed day
var displayedMonth;						// Currently displayed month
var displayedYear;						// Current displayed year
var displayedMinute;					// Currently displayed Minute
var displayedHour;						// Current displayed Hour
var languageCode;						// Current language code
var languageModel;						// Reference to object of class DHTMLSuite.calendarLanguageModel
var invalidDateRange;					// Array of invalid date ranges
var weekStartsOnMonday;					// Should the week start on monday?
this.weekStartsOnMonday = true;	// Default - start week on monday
this.languageCode = 'en';
this.invalidDateRange = new Array();
this.__createDefaultModel(inputArray);
}
DHTMLSuite.calendarModel.prototype =
{
setCallbackFunctionOnMonthChange : function(functionName)
{
this.callbackFunctionOnMonthChange = functionName;
}
,
addInvalidDateRange : function(fromDateAsArray,toDateAsArray)
{
var index = this.invalidDateRange.length;
this.invalidDateRange[index] = new Array();
if(fromDateAsArray){
fromDateAsArray.day = fromDateAsArray.day + '';
fromDateAsArray.month = fromDateAsArray.month + '';
fromDateAsArray.year = fromDateAsArray.year + '';
if(!fromDateAsArray.month)fromDateAsArray.month = fromDateAsArray.month='1';
if(!fromDateAsArray.day)fromDateAsArray.day = fromDateAsArray.day='1';
if(fromDateAsArray.day.length==1)fromDateAsArray.day = '0' + fromDateAsArray.day;
if(fromDateAsArray.month.length==1)fromDateAsArray.month = '0' + fromDateAsArray.month;
this.invalidDateRange[index].fromDate = fromDateAsArray.year + fromDateAsArray.month + fromDateAsArray.day;
}else{
this.invalidDateRange[index].fromDate = false;
}
if(toDateAsArray){
toDateAsArray.day = toDateAsArray.day + '';
toDateAsArray.month = toDateAsArray.month + '';
toDateAsArray.year = toDateAsArray.year + '';
if(!toDateAsArray.month)toDateAsArray.month = toDateAsArray.month='1';
if(!toDateAsArray.day)toDateAsArray.day = toDateAsArray.day='1';
if(toDateAsArray.day.length==1)toDateAsArray.day = '0' + toDateAsArray.day;
if(toDateAsArray.month.length==1)toDateAsArray.month = '0' + toDateAsArray.month;
this.invalidDateRange[index].toDate = toDateAsArray.year + toDateAsArray.month + toDateAsArray.day;
}else{
this.invalidDateRange[index].toDate = false;
}
}
,
isDateWithinValidRange : function(inputDate)
{
if(this.invalidDateRange.length==0)return true;
var month = inputDate.month+'';
if(month.length==1)month = '0' + month;
var day = inputDate.day+'';
if(day.length==1)day = '0' + day;
var dateToCheck = inputDate.year + month + day;
for(var no=0;no<this.invalidDateRange.length;no++){
if(!this.invalidDateRange[no].fromDate && this.invalidDateRange[no].toDate>=dateToCheck)return false;
if(!this.invalidDateRange[no].toDate && this.invalidDateRange[no].fromDate<=dateToCheck)return false;
if(this.invalidDateRange[no].fromDate<=dateToCheck && this.invalidDateRange[no].toDate>=dateToCheck)return false;
}
return true;
}
,
setInitialDateFromInput : function(inputReference,format)
{

if(inputReference.value.length>0){
if(format.indexOf('MMM') != -1){
alert("MAD");
}
else
if(!format.match(/^[0-9]*?$/gi)){
var items = inputReference.value.split(/[^0-9]/gi);
var positionArray = new Array();
positionArray['m'] = format.indexOf('mm');
if(positionArray['m']==-1)positionArray['m'] = format.indexOf('m');
positionArray['d'] = format.indexOf('dd');
if(positionArray['d']==-1)positionArray['d'] = format.indexOf('d');
positionArray['y'] = format.indexOf('yyyy');
positionArray['h'] = format.indexOf('hh');
positionArray['i'] = format.indexOf('ii');
var positionArrayNumeric = Array();
positionArrayNumeric[0] = positionArray['m'];
positionArrayNumeric[1] = positionArray['d'];
positionArrayNumeric[2] = positionArray['y'];
positionArrayNumeric[3] = positionArray['h'];
positionArrayNumeric[4] = positionArray['i'];
positionArrayNumeric = positionArrayNumeric.sort(this.__calendarSortItems);
var itemIndex = -1;
this.initialHour = '00';
this.initialMinute = '00';
for(var no=0;no<positionArrayNumeric.length;no++){
if(positionArrayNumeric[no]==-1)continue;
itemIndex++;
if(positionArrayNumeric[no]==positionArray['m']){
this.initialMonth = items[itemIndex];
continue;
}
if(positionArrayNumeric[no]==positionArray['y']){
this.initialYear = items[itemIndex];
continue;
}
if(positionArrayNumeric[no]==positionArray['d']){
tmpDay = items[itemIndex];
continue;
}
if(positionArrayNumeric[no]==positionArray['h']){
this.initialHour = items[itemIndex];
continue;
}
if(positionArrayNumeric[no]==positionArray['i']){
this.initialMinute = items[itemIndex];
continue;
}
}
this.initialMonth = this.initialMonth / 1;
tmpDay = tmpDay / 1;
this.initialDay = tmpDay;
}else{
var monthPos = format.indexOf('mm');
this.initialMonth = inputReference.value.substr(monthPos,2)/1;
var yearPos = format.indexOf('yyyy');
this.initialYear = inputReference.value.substr(yearPos,4);
var dayPos = format.indexOf('dd');
tmpDay = inputReference.value.substr(dayPos,2);
this.initialDay = tmpDay;
var hourPos = format.indexOf('hh');
if(hourPos>=0){
tmpHour = inputReference.value.substr(hourPos,2);
this.initialHour = tmpHour;
}else{
this.initialHour = '00';
}
var minutePos = format.indexOf('ii');
if(minutePos>=0){
tmpMinute = inputReference.value.substr(minutePos,2);
this.initialMinute = tmpMinute;
}else{
this.initialMinute = '00';
}
}
}
this.__setDisplayedDateToInitialData();
}
,
__setDisplayedDateToInitialData : function()
{
this.displayedYear = this.initialYear;
this.displayedMonth = this.initialMonth;
this.displayedDay = this.initialDay;
this.displayedHour = this.initialHour;
this.displayedMinute = this.initialMinute;
}
,
__calendarSortItems : function(a,b)
{
return a/1 - b/1;
}
,
setWeekStartsOnMonday : function(weekStartsOnMonday)
{
this.weekStartsOnMonday = weekStartsOnMonday;
}
,
setLanguageCode : function(languageCode)
{
this.languageModel = new DHTMLSuite.calendarLanguageModel(languageCode);	// Default english language model
}
,
__isLeapYear : function(inputYear)
{
if(inputYear%400==0||(inputYear%4==0&&inputYear%100!=0)) return true;
return false;
}
,
getWeekStartsOnMonday : function()
{
return this.weekStartsOnMonday;
}
,
__createDefaultModel : function(inputArray)
{
var d = new Date();
this.initialYear = d.getFullYear();
this.initialMonth = d.getMonth() + 1;
this.initialDay = d.getDate();
this.initialHour = d.getHours();
if(inputArray){	/* Initial date data sent to the constructor ? */
if(inputArray.initialYear)this.initialYear = inputArray.initialYear;
if(inputArray.initialMonth)this.initialMonth = inputArray.initialMonth;
if(inputArray.initialDay)this.initialDay = inputArray.initialDay;
if(inputArray.initialHour)this.initialHour = inputArray.initialHour;
if(inputArray.initialMinute)this.initialMinute = inputArray.initialMinute;
if(inputArray.languageCode)this.languageCode = inputArray.languageCode;
}
this.displayedYear = this.initialYear;
this.displayedMonth = this.initialMonth;
this.displayedDay = this.initialDay;
this.displayedHour = this.initialHour;
this.displayedMinute = this.initialMinute;
this.languageModel = new DHTMLSuite.calendarLanguageModel();	// Default english language model
}
,
__getDisplayedDay : function()
{
return this.displayedDay;
}
,
__getDisplayedHourWithLeadingZeros : function()
{
var retVal = this.__getDisplayedHour()+'';
if(retVal.length==1)retVal = '0' + retVal;
return retVal;
}
,
__getDisplayedMinuteWithLeadingZeros : function()
{
var retVal = this.__getDisplayedMinute()+'';
if(retVal.length==1)retVal = '0' + retVal;
return retVal;
}
,
__getDisplayedDayWithLeadingZeros : function()
{
var retVal = this.__getDisplayedDay()+'';
if(retVal.length==1)retVal = '0' + retVal;
return retVal;
}
,
__getDisplayedMonthNumberWithLeadingZeros : function()
{
var retVal = this.__getDisplayedMonthNumber()+'';
if(retVal.length==1)retVal = '0' + retVal;
return retVal;
}
,
__getDisplayedYear : function()
{
return this.displayedYear;
}
,
__getDisplayedHour : function()
{
if(!this.displayedHour)this.displayedHour = 0;
return this.displayedHour;
}
,
__getDisplayedMinute : function()
{
if(!this.displayedMinute)this.displayedMinute = 0;
return this.displayedMinute;
}
,
__getDisplayedMonthNumber : function()
{
return this.displayedMonth;
}	,
__getInitialDay : function()
{
return this.initialDay;
}
,
__getInitialYear : function()
{
return this.initialYear;
}
,
__getInitialMonthNumber : function()
{
return this.initialMonth;
}
,
__getMonthNameByMonthNumber : function(monthNumber)
{
return this.languageModel.monthArray[monthNumber-1];
}
,
__moveOneYearBack : function()
{
this.displayedYear--;
}
,
__moveOneYearForward : function()
{
this.displayedYear++;
}
,
__moveOneMonthBack : function()
{
this.displayedMonth--;
if(this.displayedMonth<1){
this.displayedMonth = 12;
this.displayedYear--;
}
}
,
__moveOneMonthForward : function()
{
this.displayedMonth++;
if(this.displayedMonth>12){
this.displayedMonth=1;
this.displayedYear++;
}
}
,
__setDisplayedYear : function(year)
{
var success = year!=this.displayedYear;
this.displayedYear = year;
return success
}
,
__setDisplayedMonth : function(month)
{
var success = month!=this.displayedMonth;
this.displayedMonth = month;
return success;
}
,
__setDisplayedDay : function(day)
{
this.displayedDay = day;
}
,
__setDisplayedHour : function(hour)
{
this.displayedHour = hour/1;
}
,
__setDisplayedMinute : function(minute)
{
this.displayedMinute = minute/1;
}
,
__getPreviousYearAndMonthAsArray : function()
{
var month = this.displayedMonth-1;
var year = this.displayedYear;
if(month==0){
month = 12;
year = year-1;
}
var retArray = [year,month];
return retArray;
}
,
__getNumberOfDaysInCurrentDisplayedMonth : function()
{
return this.__getNumberOfDaysInAMonthByMonthAndYear(this.displayedYear,this.displayedMonth);
}
,
__getNumberOfDaysInAMonthByMonthAndYear : function(year,month)
{
var daysInMonthArray = [31,28,31,30,31,30,31,31,30,31,30,31];
var daysInMonth = daysInMonthArray[month-1];
if(daysInMonth==28){
if(this.__isLeapYear(year))daysInMonth=29;
}
return daysInMonth/1;
}
,
__getStringWeek : function()
{
return this.languageModel.weekString;
}
,
__getDaysMondayToSunday : function()
{
return this.languageModel.dayArray;
}
,
__getDaysSundayToSaturday : function()
{
var retArray = this.languageModel.dayArray.concat();
var lastDay = new Array(retArray[retArray.length-1]);
retArray.pop();
return lastDay.concat(retArray);
}
,
__getWeekNumberFromDayMonthAndYear : function(year,month,day)
{
day = day/1;
year = year /1;
month = month/1;
var a = Math.floor((14-(month))/12);
var y = year+4800-a;
var m = (month)+(12*a)-3;
var jd = day + Math.floor(((153*m)+2)/5) +
(365*y) + Math.floor(y/4) - Math.floor(y/100) +
Math.floor(y/400) - 32045;      // (gregorian calendar)
var d4 = (jd+31741-(jd%7))%146097%36524%1461;
var L = Math.floor(d4/1460);
var d1 = ((d4-L)%365)+L;
NumberOfWeek = Math.floor(d1/7) + 1;
return NumberOfWeek;
}
,
__getRemainingDaysInPreviousMonthAsArray : function()
{
var d = new Date();
d.setFullYear(this.displayedYear);
d.setDate(1);
d.setMonth(this.displayedMonth-1);
var dayStartOfMonth = d.getDay();
if(this.weekStartsOnMonday){
if(dayStartOfMonth==0)dayStartOfMonth=7;
dayStartOfMonth--;
}
var previousMonthArray = this.__getPreviousYearAndMonthAsArray();
var daysInPreviousMonth = this.__getNumberOfDaysInAMonthByMonthAndYear(previousMonthArray[0],previousMonthArray[1]);
var returnArray = new Array();
for(var no=0;no<dayStartOfMonth;no++){
returnArray[returnArray.length] = daysInPreviousMonth-dayStartOfMonth+no+1;
}
return returnArray;
}
,
__getMonthNames : function()
{
return this.languageModel.monthArray;
}
,
__getTodayAsString : function()
{
return this.languageModel.todayString;
}
,
__getTimeAsString : function()
{
return this.languageModel.timeString;
}
}
DHTMLSuite.calendar = function(propertyArray)
{
var id;								// Unique identifier - optional
var divElement;
var divElementContent;	// Div element for the content inside the calendar
var divElementHeading;
var divElementNavigationBar;
var divElementMonthView;			// Div for the main view - weeks, days, months etc.
var divElementMonthNameInHeading;
var divElementYearInHeading;
var divElementBtnPreviousYear;		// Button - previous year
var divElementBtnNextYear;			// Button - next year
var divElementBtnPreviousMonth;		// Button - previous Month
var divElementBtnNextMonth;			// Button - next Month
var divElementYearDropdown;			// Dropdown box - years
var divElementYearDropDownParentYears;	// Inner div inside divElementYearDropdown which is parent to all the small year divs.
var divElementHourDropDownParentHours;	// Inner div inside divElementYearDropdown which is parent to all the small year hours.
var divElementHourDropDown;			// Drop down hours
var divElementMinuteDropDownParentMinutes;	// Inner div inside divElementYearDropdown which is parent to all the small year Minutes.
var divElementMinuteDropDown;			// Drop down Minutes
var divElementTodayInNavigationBar;	// Today in navigation bar.
var divElementHourInTimeBar;		// Div for hour in timer bar
var divElementMinuteInTimeBar;		// Div for minute in timer bar
var divElementTimeStringInTimeBar;		// Div for "Time" string in timer bar
var iframeElement;
var iframeElementDropDowns;
var calendarModelReference;			// Reference to object of class calendarModel
var objectIndex;
var targetReference;				// Where to insert the calendar.
var layoutCSS;
var isDragable;						// Is the calendar dragable - default = false
var referenceToDragDropObject;		// Reference to object of class DHTMLSuite.dragDropSimple
var scrollInYearDropDownActive;		// true when mouse is over up and down arrows in year dropdown
var scrollInHourDropDownActive;		// true when mouse is over up and down arrows in hour dropdown
var scrollInMinuteDropDownActive;		// true when mouse is over up and down arrows in minute dropdown
var yearDropDownOffsetInYear;		// Offset in year relative to current displayed year.
var hourDropDownOffsetInHour;		// Offset in hours relative to current displayed hour.
var minuteDropDownOffsetInHour;		// Offset in minute relative to current displayed minute.
var displayCloseButton;					// Display close button at the top right corner
var displayNavigationBar;				// Display the navigation bar ? ( default = true)
var displayTodaysDateInNavigationBar;	// Display the string "Today" in the navigation bar(default = true)
var displayTimeBar;					// Display timer bar - default = false;
var positioningReferenceToHtmlElement;	// reference to html element to position the calendar at
var positioningOffsetXInPixels;			// Offset in positioning when positioning calendar at a element
var positioningOffsetYInPixels;			// Offset in positioning when positioning calendar at a element
var htmlElementReferences;
var minuteDropDownInterval;				// Minute drop down interval(interval between each row in the minute drop down list)
var numberOfRowsInMinuteDropDown;		// Number of rows in minute drop down. (default = 10)
var numberOfRowsInHourDropDown;			// Number of rows in hour drop down. (default = 10)
var numberOfRowsInYearDropDown;			// Number of rows in year drop down. (default = 10)
this.displayTimeBar = false;
this.minuteDropDownInterval = 5;
this.htmlElementReferences = new Array();
this.positioningReferenceToHtmlElement = false;
this.displayCloseButton = true;		// Default value - close button visible at the top right corner.
this.displayNavigationBar = true;
this.displayTodaysDateInNavigationBar = true;
this.yearDropDownOffsetInYear = 0;
this.hourDropDownOffsetInHour = 0;
this.minuteDropDownOffsetInHour = 0;
this.minuteDropDownOffsetInMinute = 0;
this.layoutCSS = 'calendar.css';
this.isDragable = false;
this.scrollInYearDropDownActive = false;
this.scrollInHourDropDownActive = false;
this.scrollInMinuteDropDownActive = false;
this.numberOfRowsInMinuteDropDown = 10;
this.numberOfRowsInHourDropDown = 10;
this.numberOfRowsInYearDropDown = 10;
var callbackFunctionOnDayClick;			// Name of call back function to call when you click on a day
var callbackFunctionOnClose;			// Name of call back function to call when the calendar is closed.
var callbackFunctionOnMonthChange;		// Name of call back function to call when the month is changed in the view
try{
if(!standardObjectsCreated)DHTMLSuite.createStandardObjects();	// This line starts all the init methods
}catch(e){
alert('You need to include the dhtmlSuite-common.js file');
}
this.objectIndex = DHTMLSuite.variableStorage.arrayOfDhtmlSuiteObjects.length;
DHTMLSuite.variableStorage.arrayOfDhtmlSuiteObjects[this.objectIndex] = this;
if(propertyArray)this.__setInitialData(propertyArray);
}
DHTMLSuite.calendar.prototype =
{
setCallbackFunctionOnDayClick : function(functionName)
{
this.callbackFunctionOnDayClick = functionName;
}
,
setCallbackFunctionOnMonthChange : function(functionName)
{
if(!this.calendarModelReference){
this.calendarModelReference = new DHTMLSuite.calendarModel();
}
this.callbackFunctionOnMonthChange = functionName;
}
,
setCallbackFunctionOnClose : function(functionName)
{
this.callbackFunctionOnClose = functionName;
}
,
setCalendarModelReference : function(calendarModelReference)
{
this.calendarModelReference = calendarModelReference;
}
,
setCalendarPositionByHTMLElement : function(referenceToHtmlElement,offsetXInPixels,offsetYInPixels)
{
if(typeof referenceToHtmlElement == 'string'){
referenceToHtmlElement = document.getElementById(referenceToHtmlElement);
}
this.positioningReferenceToHtmlElement = referenceToHtmlElement;
if(!offsetXInPixels)offsetXInPixels = 0;
if(!offsetYInPixels)offsetYInPixels = 0;
this.positioningOffsetXInPixels = offsetXInPixels;
this.positioningOffsetYInPixels = offsetYInPixels;
}
,
addHtmlElementReference : function(key,referenceToHtmlElement)
{
if(typeof referenceToHtmlElement == 'string'){
referenceToHtmlElement = document.getElementById(referenceToHtmlElement);
}
if(key){
this.htmlElementReferences[key] = referenceToHtmlElement;
}
}
,
getHtmlElementReferences : function()
{
return this.htmlElementReferences;
}
,
setDisplayCloseButton : function(displayCloseButton)
{
this.displayCloseButton = displayCloseButton;
}
,
setTargetReference : function(targetReference)
{
if(typeof targetReference == 'string'){
targetReference = document.getElementById(targetReference);
}
this.targetReference = targetReference;
}
,
setIsDragable : function(isDragable){
this.isDragable = isDragable;
}
,
resetViewDisplayedMonth : function()
{
if(!this.divElement)return;
if(!this.calendarModelReference){
this.calendarModelReference = new DHTMLSuite.calendarModel();
}
this.calendarModelReference.__setDisplayedDateToInitialData();
this.__populateCalendarHeading();	// Populate heading with data
this.__populateMonthViewWithMonthData();	// Populate month view with month data
}
,
setLayoutCss : function(nameOfCssFile)
{
this.layoutCSS = nameOfCssFile;
}
,
__init : function()
{
if(!this.divElement){
DHTMLSuite.commonObj.loadCSS(this.layoutCSS);	// Load css
if(!this.calendarModelReference){
this.calendarModelReference = new DHTMLSuite.calendarModel();
}
this.__createPrimaryHtmlElements();	// Create main html elements for the calendar
this.__createCalendarHeadingElements();	// Create html elements for the heading
this.__createNavigationBar();	// Create the navigation bar below the heading.
this.__populateNavigationBar();	// Fill navigation bar with todays date.
this.__populateCalendarHeading();	// Populate heading with data
this.__createCalendarMonthView();	// Create div element for the main view, i.e. days, weeks months, etc.
this.__populateMonthViewWithMonthData();	// Populate month view with month data
this.__createTimeBar();				// Create div elements for the timer bar.
this.__populateTimeBar();			// Populate the timer bar
this.__createDropDownYears();
this.__populateDropDownYears();
this.__positionDropDownYears();
this.__createDropDownMonth();
this.__populateDropDownMonths();
this.__positionDropDownMonths();
this.__createDropDownHours();
this.__populateDropDownHours();
this.__positionDropDownHours();
this.__createDropDownMinutes();
this.__populateDropDownMinutes();
this.__positionDropDownMinutes();
this.__addEvents();
}else{
this.divElement.style.display = 'block';
this.__populateCalendarHeading();	// Populate heading with data
this.__populateMonthViewWithMonthData();	// Populate month view with month data
}
this.__resizePrimaryIframeElement();
}
,
display : function()
{
if(!this.divElement)this.__init();
this.__positionCalendar();
this.divElement.style.display='block';
}
,
hide : function()
{
this.divElement.style.display='none';
this.divElementYearDropdown.style.display='none';
this.divElementMonthDropdown.style.display='none';
}
,
isVisible : function()
{
if(!this.divElement)return false;
return this.divElement.style.display=='block'?true:false;
}
,
setInitialDateFromInput : function(inputReference,format)
{
if(!this.calendarModelReference){
this.calendarModelReference = new DHTMLSuite.calendarModel();
}
this.calendarModelReference.setInitialDateFromInput(inputReference,format);
}
,
setDisplayedYear : function(year)
{
var success = this.calendarModelReference.__setDisplayedYear(year);	// Year has actually changed
this.__populateCalendarHeading();
this.__populateMonthViewWithMonthData();
if(success)this.__handleCalendarCallBack('monthChange');
}
,
setDisplayedMonth : function(month)
{
var success = this.calendarModelReference.__setDisplayedMonth(month);	// Month have actually changed
this.__populateCalendarHeading();
this.__populateMonthViewWithMonthData();
if(success)this.__handleCalendarCallBack('monthChange');
}
,
setDisplayedHour : function(hour)
{
this.calendarModelReference.__setDisplayedHour(hour);	// Month have actually changed
this.__populateTimeBar();
}
,
setDisplayedMinute : function(minute)
{
this.calendarModelReference.__setDisplayedMinute(minute);	// Month have actually changed
this.__populateTimeBar();
}
,
__createDropDownMonth : function()
{
this.divElementMonthDropdown = document.createElement('DIV');
this.divElementMonthDropdown.style.display='none';
this.divElementMonthDropdown.className = 'DHTMLSuite_calendar_monthDropDown';
document.body.appendChild(this.divElementMonthDropdown);
}
,
__populateDropDownMonths : function()
{
this.divElementMonthDropdown.innerHTML = '';	// Initially clearing drop down.
var ind = this.objectIndex;	// Get a reference to this object in the global object array.
var months = this.calendarModelReference.__getMonthNames();	// Get an array of month name according to current language settings
for(var no=0;no<months.length;no++){	// Loop through names
var div = document.createElement('DIV');	// Create div element
div.className = 'DHTMLSuite_calendar_dropDownAMonth';
if((no+1)==this.calendarModelReference.__getDisplayedMonthNumber())div.className = 'DHTMLSuite_calendar_yearDropDownCurrentMonth';	// Highlight current month.
div.innerHTML = months[no];	// Set text of div
div.id = 'DHTMLSuite_calendarMonthPicker' + (no+1);	// Set id of div. this is used inside the __setMonthFromDropdown in order to pick up the date.
div.onmouseover = this.__mouseoverMonthInDropDown;
div.onmouseout = this.__mouseoutMonthInDropDown;
div.onclick = function(e){ DHTMLSuite.variableStorage.arrayOfDhtmlSuiteObjects[ind].__setMonthFromDropdown(e); }
this.divElementMonthDropdown.appendChild(div);
DHTMLSuite.commonObj.__addEventElement(div);
}
}
,
__createDropDownYears : function()
{
this.divElementYearDropdown = document.createElement('DIV');
this.divElementYearDropdown.style.display='none';
this.divElementYearDropdown.className = 'DHTMLSuite_calendar_yearDropDown';
document.body.appendChild(this.divElementYearDropdown);
}
,
__createDropDownHours : function()
{
this.divElementHourDropdown = document.createElement('DIV');
this.divElementHourDropdown.style.display='none';
this.divElementHourDropdown.className = 'DHTMLSuite_calendar_hourDropDown';
document.body.appendChild(this.divElementHourDropdown);
}
,
__createDropDownMinutes : function()
{
this.divElementMinuteDropdown = document.createElement('DIV');
this.divElementMinuteDropdown.style.display='none';
this.divElementMinuteDropdown.className = 'DHTMLSuite_calendar_minuteDropDown';
document.body.appendChild(this.divElementMinuteDropdown);
}
,
__populateDropDownMinutes : function()
{
var ind = this.objectIndex;
this.divElementMinuteDropdown.innerHTML = '';
var divPrevious = document.createElement('DIV');
divPrevious.className = 'DHTMLSuite_calendar_dropDown_arrowUp';
divPrevious.onmouseover = function(e){ DHTMLSuite.variableStorage.arrayOfDhtmlSuiteObjects[ind].__mouseoverUpAndDownArrowsInDropDownMinutes(e); } ;
divPrevious.onmouseout =function(e){ DHTMLSuite.variableStorage.arrayOfDhtmlSuiteObjects[ind].__mouseoutUpAndDownArrowsInDropDownMinutes(e); } ;
this.divElementMinuteDropdown.appendChild(divPrevious);
DHTMLSuite.commonObj.__addEventElement(divPrevious);
this.divElementMinuteDropDownParentMinutes = document.createElement('DIV');
this.divElementMinuteDropdown.appendChild(this.divElementMinuteDropDownParentMinutes);
this.__populateMinutesInsideDropDownMinutes(this.divElementMinuteDropDownParentMinutes);
var divNext = document.createElement('DIV');
divNext.className = 'DHTMLSuite_calendar_dropDown_arrowDown';
divNext.onmouseover = function(e){ DHTMLSuite.variableStorage.arrayOfDhtmlSuiteObjects[ind].__mouseoverUpAndDownArrowsInDropDownMinutes(e); } ;
divNext.onmouseout =function(e){ DHTMLSuite.variableStorage.arrayOfDhtmlSuiteObjects[ind].__mouseoutUpAndDownArrowsInDropDownMinutes(e); } ;
DHTMLSuite.commonObj.__addEventElement(divNext);
this.divElementMinuteDropdown.appendChild(divNext);
if(60 / this.minuteDropDownInterval	< this.numberOfRowsInMinuteDropDown){
divPrevious.style.display='none';
divNext.style.display='none';
}
}
,
__populateMinutesInsideDropDownMinutes : function()
{
var ind = this.objectIndex;
this.divElementMinuteDropDownParentMinutes.innerHTML = '';
if(60 / this.minuteDropDownInterval	< this.numberOfRowsInMinuteDropDown){
startMinute=0;
}else{
var startMinute = Math.max(0,(this.calendarModelReference.__getDisplayedMinute()-Math.round(this.numberOfRowsInMinuteDropDown/2)));
startMinute+=(this.minuteDropDownOffsetInMinute*this.minuteDropDownInterval)
if(startMinute<0){	/* Start minute negative - adjust it and change offset value */
startMinute+=this.minuteDropDownInterval;
this.minuteDropDownOffsetInMinute++;
}
if(startMinute + (this.numberOfRowsInMinuteDropDown * this.minuteDropDownInterval) >60){	/* start minute in drop down + number of records shown * interval larger than 60 -> adjust it */
startMinute-=this.minuteDropDownInterval;
this.minuteDropDownOffsetInMinute--;
}
}
for(var no=startMinute;no<Math.min(60,startMinute+this.numberOfRowsInMinuteDropDown*(this.minuteDropDownInterval));no+=this.minuteDropDownInterval){
var div = document.createElement('DIV');
div.className = 'DHTMLSuite_calendar_dropDownAMinute';
if(no==this.calendarModelReference.__getDisplayedMinute())div.className = 'DHTMLSuite_calendar_minuteDropDownCurrentMinute';
var prefix = "";
if(no<10)prefix = "0";
div.innerHTML = prefix + no;
div.onmouseover = this.__mouseoverMinuteInDropDown;
div.onmouseout = this.__mouseoutMinuteInDropDown;
div.onclick = function(e){ DHTMLSuite.variableStorage.arrayOfDhtmlSuiteObjects[ind].__setMinuteFromDropdown(e); }
this.divElementMinuteDropDownParentMinutes.appendChild(div);
DHTMLSuite.commonObj.__addEventElement(div);
}
}
,
__populateDropDownHours : function()
{
var ind = this.objectIndex;
this.divElementHourDropdown.innerHTML = '';
var div = document.createElement('DIV');
div.className = 'DHTMLSuite_calendar_dropDown_arrowUp';
div.onmouseover = function(e){ DHTMLSuite.variableStorage.arrayOfDhtmlSuiteObjects[ind].__mouseoverUpAndDownArrowsInDropDownHours(e); } ;
div.onmouseout =function(e){ DHTMLSuite.variableStorage.arrayOfDhtmlSuiteObjects[ind].__mouseoutUpAndDownArrowsInDropDownHours(e); } ;
this.divElementHourDropdown.appendChild(div);
DHTMLSuite.commonObj.__addEventElement(div);
this.divElementHourDropDownParentHours = document.createElement('DIV');
this.divElementHourDropdown.appendChild(this.divElementHourDropDownParentHours);
this.__populateHoursInsideDropDownHours(this.divElementHourDropDownParentHours);
var div = document.createElement('DIV');
div.className = 'DHTMLSuite_calendar_dropDown_arrowDown';
div.onmouseover = function(e){ DHTMLSuite.variableStorage.arrayOfDhtmlSuiteObjects[ind].__mouseoverUpAndDownArrowsInDropDownHours(e); } ;
div.onmouseout =function(e){ DHTMLSuite.variableStorage.arrayOfDhtmlSuiteObjects[ind].__mouseoutUpAndDownArrowsInDropDownHours(e); } ;
DHTMLSuite.commonObj.__addEventElement(div);
this.divElementHourDropdown.appendChild(div);
}
,
__populateHoursInsideDropDownHours : function()
{
var ind = this.objectIndex;
this.divElementHourDropDownParentHours.innerHTML = '';
var startHour = Math.max(0,(this.calendarModelReference.__getDisplayedHour()-Math.round(this.numberOfRowsInHourDropDown/2)));
startHour = Math.min(14,startHour);
if((startHour + this.hourDropDownOffsetInHour + this.numberOfRowsInHourDropDown)>24){
this.hourDropDownOffsetInHour = (24-startHour-this.numberOfRowsInHourDropDown);
}
if((startHour + this.hourDropDownOffsetInHour)<0){
this.hourDropDownOffsetInHour = startHour*-1;
}
startHour+=this.hourDropDownOffsetInHour;
if(startHour<0)startHour = 0;
if(startHour>(24-this.numberOfRowsInHourDropDown))startHour = (24-this.numberOfRowsInHourDropDown);
for(var no=startHour;no<startHour+this.numberOfRowsInHourDropDown;no++){
var div = document.createElement('DIV');
div.className = 'DHTMLSuite_calendar_dropDownAHour';
if(no==this.calendarModelReference.__getDisplayedHour())div.className = 'DHTMLSuite_calendar_hourDropDownCurrentHour';
var prefix = "";
if(no<10)prefix = "0";
div.innerHTML = prefix + no;
div.onmouseover = this.__mouseoverHourInDropDown;
div.onmouseout = this.__mouseoutHourInDropDown;
div.onclick = function(e){ DHTMLSuite.variableStorage.arrayOfDhtmlSuiteObjects[ind].__setHourFromDropdown(e); }
this.divElementHourDropDownParentHours.appendChild(div);
DHTMLSuite.commonObj.__addEventElement(div);
}
}
,
__populateDropDownYears : function()
{
var ind = this.objectIndex;
this.divElementYearDropdown.innerHTML = '';
var div = document.createElement('DIV');
div.className = 'DHTMLSuite_calendar_dropDown_arrowUp';
div.onmouseover = function(e){ DHTMLSuite.variableStorage.arrayOfDhtmlSuiteObjects[ind].__mouseoverUpAndDownArrowsInDropDownYears(e); } ;
div.onmouseout =function(e){ DHTMLSuite.variableStorage.arrayOfDhtmlSuiteObjects[ind].__mouseoutUpAndDownArrowsInDropDownYears(e); } ;
this.divElementYearDropdown.appendChild(div);
DHTMLSuite.commonObj.__addEventElement(div);
this.divElementYearDropDownParentYears = document.createElement('DIV');
this.divElementYearDropdown.appendChild(this.divElementYearDropDownParentYears);
this.__populateYearsInsideDropDownYears(this.divElementYearDropDownParentYears);
var div = document.createElement('DIV');
div.className = 'DHTMLSuite_calendar_dropDown_arrowDown';
div.onmouseover = function(e){ DHTMLSuite.variableStorage.arrayOfDhtmlSuiteObjects[ind].__mouseoverUpAndDownArrowsInDropDownYears(e); } ;
div.onmouseout =function(e){ DHTMLSuite.variableStorage.arrayOfDhtmlSuiteObjects[ind].__mouseoutUpAndDownArrowsInDropDownYears(e); } ;
DHTMLSuite.commonObj.__addEventElement(div);
this.divElementYearDropdown.appendChild(div);
}
,
__populateYearsInsideDropDownYears : function(divElementToPopulate)
{
var ind = this.objectIndex;
this.divElementYearDropDownParentYears.innerHTML = '';
var startYear = this.calendarModelReference.__getDisplayedYear()-5 + this.yearDropDownOffsetInYear;
for(var no=startYear;no<startYear+this.numberOfRowsInYearDropDown;no++){
var div = document.createElement('DIV');
div.className = 'DHTMLSuite_calendar_dropDownAYear';
if(no==this.calendarModelReference.__getDisplayedYear())div.className = 'DHTMLSuite_calendar_yearDropDownCurrentYear';
div.innerHTML = no;
div.onmouseover = this.__mouseoverYearInDropDown;
div.onmouseout = this.__mouseoutYearInDropDown;
div.onclick = function(e){ DHTMLSuite.variableStorage.arrayOfDhtmlSuiteObjects[ind].__setYearFromDropdown(e); }
this.divElementYearDropDownParentYears.appendChild(div);
DHTMLSuite.commonObj.__addEventElement(div);
}
}
,
__positionDropDownMonths : function()
{
this.divElementMonthDropdown.style.left = DHTMLSuite.commonObj.getLeftPos(this.divElementMonthNameInHeading) + 'px';
this.divElementMonthDropdown.style.top = (DHTMLSuite.commonObj.getTopPos(this.divElementMonthNameInHeading) + this.divElementMonthNameInHeading.offsetHeight) + 'px';
if(this.iframeElementDropDowns){
this.iframeElementDropDowns.style.left = this.divElementMonthDropdown.style.left;
this.iframeElementDropDowns.style.top = this.divElementMonthDropdown.style.top;
this.iframeElementDropDowns.style.width = (this.divElementMonthDropdown.clientWidth) + 'px';
this.iframeElementDropDowns.style.height = this.divElementMonthDropdown.clientHeight + 'px';
this.iframeElementDropDowns.style.display = this.divElementMonthDropdown.style.display;
}
}
,
__positionDropDownYears : function()
{
this.divElementYearDropdown.style.left = DHTMLSuite.commonObj.getLeftPos(this.divElementYearInHeading) + 'px';
this.divElementYearDropdown.style.top = (DHTMLSuite.commonObj.getTopPos(this.divElementYearInHeading) + this.divElementYearInHeading.offsetHeight) + 'px';
if(this.iframeElementDropDowns){
this.iframeElementDropDowns.style.left = this.divElementYearDropdown.style.left;
this.iframeElementDropDowns.style.top = this.divElementYearDropdown.style.top;
this.iframeElementDropDowns.style.width = (this.divElementYearDropdown.clientWidth) + 'px';
this.iframeElementDropDowns.style.height = this.divElementYearDropdown.clientHeight + 'px';
this.iframeElementDropDowns.style.display = this.divElementYearDropdown.style.display;
}
}
,
__positionDropDownHours : function()
{
this.divElementHourDropdown.style.left = DHTMLSuite.commonObj.getLeftPos(this.divElementHourInTimeBar) + 'px';
this.divElementHourDropdown.style.top = (DHTMLSuite.commonObj.getTopPos(this.divElementHourInTimeBar) + this.divElementHourInTimeBar.offsetHeight) + 'px';
if(this.iframeElementDropDowns){
this.iframeElementDropDowns.style.left = this.divElementHourDropdown.style.left;
this.iframeElementDropDowns.style.top = this.divElementHourDropdown.style.top;
this.iframeElementDropDowns.style.width = (this.divElementHourDropdown.clientWidth) + 'px';
this.iframeElementDropDowns.style.height = this.divElementHourDropdown.clientHeight + 'px';
this.iframeElementDropDowns.style.display = this.divElementHourDropdown.style.display;
}
}
,
__positionDropDownMinutes : function()
{
this.divElementMinuteDropdown.style.left = DHTMLSuite.commonObj.getLeftPos(this.divElementMinuteInTimeBar) + 'px';
this.divElementMinuteDropdown.style.top = (DHTMLSuite.commonObj.getTopPos(this.divElementMinuteInTimeBar) + this.divElementMinuteInTimeBar.offsetHeight) + 'px';
if(this.iframeElementDropDowns){
this.iframeElementDropDowns.style.left = this.divElementMinuteDropdown.style.left;
this.iframeElementDropDowns.style.top = this.divElementMinuteDropdown.style.top;
this.iframeElementDropDowns.style.width = (this.divElementMinuteDropdown.clientWidth) + 'px';
this.iframeElementDropDowns.style.height = this.divElementMinuteDropdown.clientHeight + 'px';
this.iframeElementDropDowns.style.display = this.divElementMinuteDropdown.style.display;
}
}
,
__setMonthFromDropdown : function(e)
{
if(document.all)e = event;
var src = DHTMLSuite.commonObj.getSrcElement(e);
this.__showHideDropDownBoxMonth();
this.setDisplayedMonth(src.id.replace(/[^0-9]/gi,''));
}
,
__setYearFromDropdown : function(e)
{
if(document.all)e = event;
var src = DHTMLSuite.commonObj.getSrcElement(e);
this.__showHideDropDownBoxYear();
this.setDisplayedYear(src.innerHTML);
}
,
__setHourFromDropdown : function(e)
{
if(document.all)e = event;
var src = DHTMLSuite.commonObj.getSrcElement(e);
this.__showHideDropDownBoxHour();
this.setDisplayedHour(src.innerHTML);
}
,
__setMinuteFromDropdown : function(e)
{
if(document.all)e = event;
var src = DHTMLSuite.commonObj.getSrcElement(e);
this.__showHideDropDownBoxMinute();
this.setDisplayedMinute(src.innerHTML);
}
,
__autoHideDropDownBoxes : function(e)
{
if(document.all)e = event;
var src = DHTMLSuite.commonObj.getSrcElement(e);
if(src.className.indexOf('MonthAndYear')>=0 || src.className.indexOf('HourAndMinute')>=0){	// class name of element same as element triggering the dropdowns ?
if(DHTMLSuite.commonObj.isObjectClicked(this.divElement,e))return;	// if element clicked is a sub element of main calendar div - return
}
this.__showHideDropDownBoxMonth('none');
this.__showHideDropDownBoxYear('none');
this.__showHideDropDownBoxHour('none');
this.__showHideDropDownBoxMinute('none');
}
,
__showHideDropDownBoxMonth : function(forcedDisplayAttribute)
{
if(!forcedDisplayAttribute){
this.__showHideDropDownBoxYear('none');	// Hide year drop down.
this.__showHideDropDownBoxHour('none');	// Hide year drop down.
}
if(forcedDisplayAttribute){
this.divElementMonthDropdown.style.display = forcedDisplayAttribute;
}else{
this.divElementMonthDropdown.style.display=(this.divElementMonthDropdown.style.display=='block'?'none':'block');
}
this.__populateDropDownMonths();
this.__positionDropDownMonths();
}
,
__showHideDropDownBoxYear : function(forcedDisplayAttribute)
{
if(!forcedDisplayAttribute){
this.__showHideDropDownBoxMonth('none');	// Hide year drop down.
this.__showHideDropDownBoxHour('none');	// Hide year drop down.
this.__showHideDropDownBoxMinute('none');	// Hide year drop down.
}
if(forcedDisplayAttribute){
this.divElementYearDropdown.style.display = forcedDisplayAttribute;
}else{
this.divElementYearDropdown.style.display=(this.divElementYearDropdown.style.display=='block'?'none':'block');
}
if(this.divElementYearDropdown.style.display=='none' ){
this.yearDropDownOffsetInYear = 0;
}else{
this.__populateDropDownYears();
}
this.__positionDropDownYears();
}
,
__showHideDropDownBoxHour : function(forcedDisplayAttribute)
{
if(!forcedDisplayAttribute){
this.__showHideDropDownBoxYear('none');	// Hide Hour drop down.
this.__showHideDropDownBoxMonth('none');	// Hide Hour drop down.
this.__showHideDropDownBoxMinute('none');	// Hide Hour drop down.
}
if(forcedDisplayAttribute){
this.divElementHourDropdown.style.display = forcedDisplayAttribute;
}else{
this.divElementHourDropdown.style.display=(this.divElementHourDropdown.style.display=='block'?'none':'block');
}
if(this.divElementHourDropdown.style.display=='none' ){
this.hourDropDownOffsetInHour = 0;
}else{
this.__populateDropDownHours();
}
this.__positionDropDownHours();
}
,
__showHideDropDownBoxMinute : function(forcedDisplayAttribute)
{
if(!forcedDisplayAttribute){
this.__showHideDropDownBoxYear('none');	// Hide Minute drop down.
this.__showHideDropDownBoxMonth('none');	// Hide Minute drop down.
this.__showHideDropDownBoxHour('none');	// Hide Minute drop down.
}
if(forcedDisplayAttribute){
this.divElementMinuteDropdown.style.display = forcedDisplayAttribute;
}else{
this.divElementMinuteDropdown.style.display=(this.divElementMinuteDropdown.style.display=='block'?'none':'block');
}
if(this.divElementMinuteDropdown.style.display=='none' ){
this.minuteDropDownOffsetInMinute = 0;
}else{
this.__populateDropDownMinutes();
}
this.__positionDropDownMinutes();
}
,
__createPrimaryHtmlElements : function()
{
this.divElement = document.createElement('DIV');
this.divElement.className = 'DHTMLSuite_calendar';
this.divElementContent = document.createElement('DIV');
this.divElement.appendChild(this.divElementContent);
this.divElementContent.className = 'DHTMLSuite_calendarContent';
if(this.targetReference)this.targetReference.appendChild(this.divElement);else document.body.appendChild(this.divElement);
if(this.isDragable){
try{
this.referenceToDragDropObject = new DHTMLSuite.dragDropSimple(this.divElement,false,0,0);
}catch(e){
alert('You need to include DHTMLSuite-dragDropSimple.js for the drag feature');
}
}
if(DHTMLSuite.clientInfoObj.isMSIE && DHTMLSuite.clientInfoObj.navigatorVersion<8){
this.iframeElement = document.createElement('<iframe src="about:blank" frameborder="0">');
this.iframeElement.className = 'DHTMLSuite_calendar_iframe';
this.divElement.appendChild(this.iframeElement);
this.iframeElementDropDowns = document.createElement('<iframe src="about:blank" frameborder="0">');
this.iframeElementDropDowns.className = 'DHTMLSuite_calendar_iframe';
this.iframeElementDropDowns.style.display='none';
document.body.appendChild(this.iframeElementDropDowns);
}
}
,
__createCalendarHeadingElements : function()
{
this.divElementHeading = document.createElement('DIV');
if(this.isDragable){	/* Calendar is dragable */
this.referenceToDragDropObject.addDragHandle(this.divElementHeading);
this.divElementHeading.style.cursor = 'move';
}
this.divElementHeading.className='DHTMLSuite_calendarHeading';
this.divElementContent.appendChild(this.divElementHeading);
this.divElementHeading.style.position = 'relative';
this.divElementClose = document.createElement('DIV');
this.divElementClose.className = 'DHTMLSuite_calendarCloseButton';
this.divElementHeading.appendChild(this.divElementClose);
if(!this.displayCloseButton)this.divElementClose.style.display='none';
this.divElementHeadingTxt = document.createElement('DIV');
this.divElementHeadingTxt.className = 'DHTMLSuite_calendarHeadingTxt';
if(DHTMLSuite.clientInfoObj.isMSIE){
var table = document.createElement('<TABLE cellpadding="0" cellspacing="0" border="0">');
}else{
var table = document.createElement('TABLE');
table.setAttribute('cellpadding',0);
table.setAttribute('cellspacing',0);
table.setAttribute('border',0);
}
table.style.margin = '0 auto';
this.divElementHeadingTxt.appendChild(table);
var row = table.insertRow(0);
var cell = row.insertCell(-1);
this.divElementMonthNameInHeading = document.createElement('DIV');
this.divElementMonthNameInHeading.className = 'DHTMLSuite_calendarHeaderMonthAndYear';
cell.appendChild(this.divElementMonthNameInHeading);
var cell = row.insertCell(-1);
var span = document.createElement('SPAN');
span.innerHTML = ', ';
cell.appendChild(span);
var cell = row.insertCell(-1);
this.divElementYearInHeading = document.createElement('DIV');
this.divElementYearInHeading.className = 'DHTMLSuite_calendarHeaderMonthAndYear';
cell.appendChild(this.divElementYearInHeading);
this.divElementHeading.appendChild(this.divElementHeadingTxt);
}
,
__createNavigationBar : function()
{
this.divElementNavigationBar = document.createElement('DIV');
this.divElementNavigationBar.className='DHTMLSuite_calendar_navigationBar';
this.divElementContent.appendChild(this.divElementNavigationBar);
this.divElementBtnPreviousYear = document.createElement('DIV');
this.divElementBtnPreviousYear.className='DHTMLSuite_calendar_btnPreviousYear';
this.divElementNavigationBar.appendChild(this.divElementBtnPreviousYear);
this.divElementBtnNextYear = document.createElement('DIV');
this.divElementBtnNextYear.className='DHTMLSuite_calendar_btnNextYear';
this.divElementNavigationBar.appendChild(this.divElementBtnNextYear);
this.divElementBtnPreviousMonth = document.createElement('DIV');
this.divElementBtnPreviousMonth.className='DHTMLSuite_calendar_btnPreviousMonth';
this.divElementNavigationBar.appendChild(this.divElementBtnPreviousMonth);
this.divElementBtnNextMonth = document.createElement('DIV');
this.divElementBtnNextMonth.className='DHTMLSuite_calendar_btnNextMonth';
this.divElementNavigationBar.appendChild(this.divElementBtnNextMonth);
this.divElementTodayInNavigationBar = document.createElement('DIV');
this.divElementTodayInNavigationBar.className='DHTMLSuite_calendar_navigationBarToday';
this.divElementNavigationBar.appendChild(this.divElementTodayInNavigationBar);
if(!this.displayNavigationBar)this.divElementNavigationBar.style.display='none';
if(!this.displayTodaysDateInNavigationBar)this.divElementTodayInNavigationBar.style.display='none';
}
,
__populateNavigationBar : function()
{
var ind = this.objectIndex;
this.divElementTodayInNavigationBar.innerHTML = '';
var span = document.createElement('SPAN');
span.innerHTML = this.calendarModelReference.__getTodayAsString();
span.onclick = function(){ DHTMLSuite.variableStorage.arrayOfDhtmlSuiteObjects[ind].__displayMonthOfToday(); }
this.divElementTodayInNavigationBar.appendChild(span);
}
,
__createCalendarMonthView : function()
{
this.divElementMonthView = document.createElement('DIV');
this.divElementMonthView.className = 'DHTMLSuite_calendar_monthView';
this.divElementContent.appendChild(this.divElementMonthView);
}
,
__populateMonthViewWithMonthData : function()
{
var ind = this.objectIndex;
var dateOfToday = new Date();
this.divElementMonthView.innerHTML = '';
var modelRef = this.calendarModelReference;
if(DHTMLSuite.clientInfoObj.isMSIE){
var table = document.createElement('<TABLE cellpadding="1" cellspacing="0" border="0" width="100%">');
}else{
var table = document.createElement('TABLE');
table.setAttribute('cellpadding',1);
table.setAttribute('cellspacing',0);
table.setAttribute('border',0);
table.width = '100%';
}
this.divElementMonthView.appendChild(table);
var row = table.insertRow(-1);	// Insert a new row at the end of the table
row.className = 'DHTMLSuite_calendar_monthView_headerRow';
var cell = row.insertCell(-1);
cell.className = 'DHTMLSuite_calendar_monthView_firstColumn';
cell.innerHTML = modelRef.__getStringWeek();
if(modelRef.getWeekStartsOnMonday()){
var days = modelRef.__getDaysMondayToSunday();
}else{
var days = modelRef.__getDaysSundayToSaturday();
}
for(var no=0;no<days.length;no++){
var cell = row.insertCell(-1);
cell.innerHTML = days[no];
cell.className = 'DHTMLSuite_calendar_monthView_headerCell';
if(modelRef.getWeekStartsOnMonday() && no==6){
cell.className = 'DHTMLSuite_calendar_monthView_headerSunday';
}
if(!modelRef.getWeekStartsOnMonday() && no==0){
cell.className = 'DHTMLSuite_calendar_monthView_headerSunday';
}
}
var row = table.insertRow(-1);
var cell = row.insertCell(-1);
cell.className = 'DHTMLSuite_calendar_monthView_firstColumn';
var week = modelRef.__getWeekNumberFromDayMonthAndYear(modelRef.__getDisplayedYear(),modelRef.__getDisplayedMonthNumber(),1);
cell.innerHTML = week;
var daysRemainingInPreviousMonth = modelRef.__getRemainingDaysInPreviousMonthAsArray();
for(var no=0;no<daysRemainingInPreviousMonth.length;no++){
var cell = row.insertCell(-1);
cell.innerHTML = daysRemainingInPreviousMonth[no];
cell.className = 'DHTMLSuite_calendar_monthView_daysInOtherMonths';
}
var daysInCurrentMonth = modelRef.__getNumberOfDaysInCurrentDisplayedMonth();
var cellCounter = daysRemainingInPreviousMonth.length+1;
for(var no=1;no<=daysInCurrentMonth;no++){
var cell = row.insertCell(-1);
cell.innerHTML = no;
cell.className = 'DHTMLSuite_calendar_monthView_daysInThisMonth';
DHTMLSuite.commonObj.__addEventElement(cell);
if(cellCounter%7==0 && modelRef.getWeekStartsOnMonday()){
cell.className = 'DHTMLSuite_calendar_monthView_sundayInThisMonth';
}
if(cellCounter%7==1 && !modelRef.getWeekStartsOnMonday()){
cell.className = 'DHTMLSuite_calendar_monthView_sundayInThisMonth';
}
if(no==modelRef.__getInitialDay() && modelRef.__getDisplayedYear() == modelRef.__getInitialYear() && modelRef.__getDisplayedMonthNumber()==modelRef.__getInitialMonthNumber()){
cell.className = 'DHTMLSuite_calendar_monthView_initialDate';
}
if(!modelRef.isDateWithinValidRange({year:modelRef.__getDisplayedYear(),month:modelRef.__getDisplayedMonthNumber(),day:no})){
cell.className = 'DHTMLSuite_calendar_monthView_invalidDate';
}else{
cell.onmousedown = function(e){ DHTMLSuite.variableStorage.arrayOfDhtmlSuiteObjects[ind].__mousedownOnDayInCalendar(e); }
cell.onmouseover = this.__mouseoverCalendarDay;
cell.onmouseout = this.__mouseoutCalendarDay;
}
if(no==dateOfToday.getDate() && modelRef.__getDisplayedYear() == dateOfToday.getFullYear() && modelRef.__getDisplayedMonthNumber()==(dateOfToday.getMonth()+1)){
cell.className = 'DHTMLSuite_calendar_monthView_currentDate';
}
if(cellCounter%7==0 && no<daysInCurrentMonth){
var row = table.insertRow(-1);
var cell = row.insertCell(-1)
cell.className = 'DHTMLSuite_calendar_monthView_firstColumn';
var week = modelRef.__getWeekNumberFromDayMonthAndYear(modelRef.__getDisplayedYear(),modelRef.__getDisplayedMonthNumber(),(no+1));
cell.innerHTML = week;
}
cellCounter++;
}
if((cellCounter-1)%7>0){
var dayCounter = 1;
for(var no=(cellCounter-1)%7;no<7;no++){
var cell = row.insertCell(-1);
cell.innerHTML = dayCounter;
cell.className = 'DHTMLSuite_calendar_monthView_daysInOtherMonths';
dayCounter++;
}
}
}
,
__createTimeBar : function()
{
this.divElementTimeBar = document.createElement('DIV');
this.divElementTimeBar.className = 'DHTMLSuite_calendar_timeBar';
this.divElementContent.appendChild(this.divElementTimeBar);
if(DHTMLSuite.clientInfoObj.isMSIE){
var table = document.createElement('<TABLE cellpadding="0" cellspacing="0" border="0">');
}else{
var table = document.createElement('TABLE');
table.setAttribute('cellpadding',0);
table.setAttribute('cellspacing',0);
table.setAttribute('border',0);
}
table.style.margin = '0 auto';
this.divElementTimeBar.appendChild(table);
var row = table.insertRow(0);
var cell = row.insertCell(-1);
this.divElementHourInTimeBar = document.createElement('DIV');
this.divElementHourInTimeBar.className = 'DHTMLSuite_calendar_timeBarHourAndMinute';
cell.appendChild(this.divElementHourInTimeBar);
var cell = row.insertCell(-1);
var span = document.createElement('SPAN');
span.innerHTML = ' : ';
cell.appendChild(span);
var cell = row.insertCell(-1);
this.divElementMinuteInTimeBar = document.createElement('DIV');
this.divElementMinuteInTimeBar.className = 'DHTMLSuite_calendar_timeBarHourAndMinute';
cell.appendChild(this.divElementMinuteInTimeBar);
this.divElementTimeStringInTimeBar = document.createElement('DIV');
this.divElementTimeStringInTimeBar.className = 'DHTMLSuite_calendarTimeBarTimeString';
this.divElementTimeBar.appendChild(this.divElementTimeStringInTimeBar);
if(!this.displayTimeBar)this.divElementTimeBar.style.display='none';
}
,
__populateTimeBar : function()
{
this.divElementHourInTimeBar.innerHTML = this.calendarModelReference.__getDisplayedHourWithLeadingZeros();
this.divElementMinuteInTimeBar.innerHTML = this.calendarModelReference.__getDisplayedMinuteWithLeadingZeros();
this.divElementTimeStringInTimeBar.innerHTML = this.calendarModelReference.__getTimeAsString() + ':';
}
,
__populateCalendarHeading : function()
{
this.divElementMonthNameInHeading.innerHTML = this.calendarModelReference.__getMonthNameByMonthNumber(this.calendarModelReference.__getDisplayedMonthNumber());
this.divElementYearInHeading.innerHTML = this.calendarModelReference.__getDisplayedYear();
}
,
__mousedownOnDayInCalendar : function(e)
{
if(document.all)e = event;
var src = DHTMLSuite.commonObj.getSrcElement(e);
this.calendarModelReference.__setDisplayedDay(src.innerHTML);
this.__handleCalendarCallBack('dayClick');
}
,
__handleCalendarCallBack : function(action)
{
var callbackString = '';
switch(action){
case 'dayClick':
if(this.callbackFunctionOnDayClick)callbackString = this.callbackFunctionOnDayClick;
break;
case "monthChange":
if(this.callbackFunctionOnMonthChange)callbackString = this.callbackFunctionOnMonthChange;
break;
}
if(callbackString){
callbackString = callbackString +
'({'
+ ' year:' + this.calendarModelReference.__getDisplayedYear()
+ ',month:"' + this.calendarModelReference.__getDisplayedMonthNumberWithLeadingZeros() + '"'
+ ',day:"' + this.calendarModelReference.__getDisplayedDayWithLeadingZeros() + '"'
+ ',hour:"' + this.calendarModelReference.__getDisplayedHourWithLeadingZeros() + '"'
+ ',minute:"' + this.calendarModelReference.__getDisplayedMinuteWithLeadingZeros() + '"'
+ ',calendarRef:this'
callbackString = callbackString + '})';
}
if(callbackString)this.__evaluateCallBackString(callbackString);
}
,
__evaluateCallBackString : function(callbackString)
{
try{
eval(callbackString);
}catch(e){
alert('Could not excute call back function ' + callbackString + '\n' + e.message);
}
}
,
__displayMonthOfToday : function()
{
var d = new Date();
var month = d.getMonth()+1;
var year = d.getFullYear();
this.setDisplayedYear(year);
this.setDisplayedMonth(month);
}
,
__moveOneYearBack : function()
{
this.calendarModelReference.__moveOneYearBack();
this.__populateCalendarHeading();
this.__populateMonthViewWithMonthData();
this.__handleCalendarCallBack('monthChange');
}
,
__moveOneYearForward : function()
{
this.calendarModelReference.__moveOneYearForward();
this.__populateCalendarHeading();
this.__populateMonthViewWithMonthData();
this.__handleCalendarCallBack('monthChange');
}
,
__moveOneMonthBack : function()
{
this.calendarModelReference.__moveOneMonthBack();
this.__populateCalendarHeading();
this.__populateMonthViewWithMonthData();
this.__handleCalendarCallBack('monthChange');
}
,
__moveOneMonthForward : function()
{
this.calendarModelReference.__moveOneMonthForward();
this.__populateCalendarHeading();
this.__populateMonthViewWithMonthData();
this.__handleCalendarCallBack('monthChange');
}
,
__addEvents : function()
{
var ind = this.objectIndex;
this.divElementClose.onmouseover = this.__mouseoverCalendarButton;
this.divElementClose.onmouseout = this.__mouseoutCalendarButton;
this.divElementClose.onclick = function(e){ DHTMLSuite.variableStorage.arrayOfDhtmlSuiteObjects[ind].hide(); }
DHTMLSuite.commonObj.__addEventElement(this.divElementClose);
this.divElementBtnPreviousYear.onmouseover = this.__mouseoverCalendarButton;
this.divElementBtnPreviousYear.onmouseout = this.__mouseoutCalendarButton;
this.divElementBtnPreviousYear.onclick = function(e){ DHTMLSuite.variableStorage.arrayOfDhtmlSuiteObjects[ind].__moveOneYearBack(); }
DHTMLSuite.commonObj.__addEventElement(this.divElementBtnPreviousYear);
this.divElementBtnNextYear.onmouseover = this.__mouseoverCalendarButton;
this.divElementBtnNextYear.onmouseout = this.__mouseoutCalendarButton;
this.divElementBtnNextYear.onclick = function(e){ DHTMLSuite.variableStorage.arrayOfDhtmlSuiteObjects[ind].__moveOneYearForward(); }
DHTMLSuite.commonObj.__addEventElement(this.divElementBtnNextYear);
this.divElementBtnPreviousMonth.onmouseover = this.__mouseoverCalendarButton;
this.divElementBtnPreviousMonth.onmouseout = this.__mouseoutCalendarButton;
this.divElementBtnPreviousMonth.onclick = function(e){ DHTMLSuite.variableStorage.arrayOfDhtmlSuiteObjects[ind].__moveOneMonthBack(); }
DHTMLSuite.commonObj.__addEventElement(this.divElementBtnPreviousMonth);
this.divElementBtnNextMonth.onmouseover = this.__mouseoverCalendarButton;
this.divElementBtnNextMonth.onmouseout = this.__mouseoutCalendarButton;
this.divElementBtnNextMonth.onclick = function(e){ DHTMLSuite.variableStorage.arrayOfDhtmlSuiteObjects[ind].__moveOneMonthForward(); }
DHTMLSuite.commonObj.__addEventElement(this.divElementBtnNextMonth);
this.divElementYearInHeading.onmouseover = this.__mouseoverMonthAndYear;
this.divElementYearInHeading.onmouseout = this.__mouseoutMonthAndYear;
this.divElementYearInHeading.onclick = function(e){ DHTMLSuite.variableStorage.arrayOfDhtmlSuiteObjects[ind].__showHideDropDownBoxYear(); }
DHTMLSuite.commonObj.__addEventElement(this.divElementYearInHeading);
this.divElementMonthNameInHeading.onmouseover = this.__mouseoverMonthAndYear;
this.divElementMonthNameInHeading.onmouseout = this.__mouseoutMonthAndYear;
this.divElementMonthNameInHeading.onclick = function(e){ DHTMLSuite.variableStorage.arrayOfDhtmlSuiteObjects[ind].__showHideDropDownBoxMonth(); }
DHTMLSuite.commonObj.__addEventElement(this.divElementMonthNameInHeading);
this.divElementHourInTimeBar.onmouseover = this.__mouseoverHourAndMinute;
this.divElementHourInTimeBar.onmouseout = this.__mouseoutHourAndMinute;
this.divElementHourInTimeBar.onclick = function(e){ DHTMLSuite.variableStorage.arrayOfDhtmlSuiteObjects[ind].__showHideDropDownBoxHour(); }
DHTMLSuite.commonObj.__addEventElement(this.divElementHourInTimeBar);
this.divElementMinuteInTimeBar.onmouseover = this.__mouseoverHourAndMinute;
this.divElementMinuteInTimeBar.onmouseout = this.__mouseoutHourAndMinute;
this.divElementMinuteInTimeBar.onclick = function(e){ DHTMLSuite.variableStorage.arrayOfDhtmlSuiteObjects[ind].__showHideDropDownBoxMinute(); }
DHTMLSuite.commonObj.__addEventElement(this.divElementMinuteInTimeBar);
this.divElementHeading.onselectstart = DHTMLSuite.commonObj.cancelEvent;
DHTMLSuite.commonObj.__addEventElement(this.divElementHeading);
DHTMLSuite.commonObj.addEvent(document.documentElement,'click',function(e){ DHTMLSuite.variableStorage.arrayOfDhtmlSuiteObjects[ind].__autoHideDropDownBoxes(e); },ind+'');
}
,
__resizePrimaryIframeElement : function()
{
if(!this.iframeElement)return;
this.iframeElement.style.width = this.divElement.clientWidth + 'px';
this.iframeElement.style.height = this.divElement.clientHeight + 'px';
}
,
__scrollInYearDropDown : function(scrollDirection)
{
if(!this.scrollInYearDropDownActive)return;
var ind = this.objectIndex;
this.yearDropDownOffsetInYear+=scrollDirection;
this.__populateYearsInsideDropDownYears();
setTimeout('DHTMLSuite.variableStorage.arrayOfDhtmlSuiteObjects[' + ind + '].__scrollInYearDropDown(' + scrollDirection + ')',150);
}
,
__mouseoverUpAndDownArrowsInDropDownYears : function(e)
{
var ind = this.objectIndex;
if(document.all)e = event;
var src = DHTMLSuite.commonObj.getSrcElement(e);
var scrollDirection = (src.className.toLowerCase().indexOf('up')>=0?-1:1);
src.className = src.className + ' DHTMLSuite_calendarDropDown_dropDownArrowOver';
this.scrollInYearDropDownActive = true;
setTimeout('DHTMLSuite.variableStorage.arrayOfDhtmlSuiteObjects[' + ind + '].__scrollInYearDropDown(' + scrollDirection + ')',100);
}
,
__mouseoutUpAndDownArrowsInDropDownYears : function(e)
{
if(document.all)e = event;
var src = DHTMLSuite.commonObj.getSrcElement(e);
src.className = src.className.replace(' DHTMLSuite_calendarDropDown_dropDownArrowOver','');
this.scrollInYearDropDownActive = false;
}
,
__scrollInHourDropDown : function(scrollDirection)
{
if(!this.scrollInHourDropDownActive)return;
var ind = this.objectIndex;
this.hourDropDownOffsetInHour+=scrollDirection;
this.__populateHoursInsideDropDownHours();
setTimeout('DHTMLSuite.variableStorage.arrayOfDhtmlSuiteObjects[' + ind + '].__scrollInHourDropDown(' + scrollDirection + ')',150);
}
,
__mouseoverUpAndDownArrowsInDropDownHours : function(e)
{
var ind = this.objectIndex;
if(document.all)e = event;
var src = DHTMLSuite.commonObj.getSrcElement(e);
var scrollDirection = (src.className.toLowerCase().indexOf('up')>=0?-1:1);
src.className = src.className + ' DHTMLSuite_calendarDropDown_dropDownArrowOver';
this.scrollInHourDropDownActive = true;
setTimeout('DHTMLSuite.variableStorage.arrayOfDhtmlSuiteObjects[' + ind + '].__scrollInHourDropDown(' + scrollDirection + ')',100);
}
,
__mouseoutUpAndDownArrowsInDropDownHours : function(e)
{
if(document.all)e = event;
var src = DHTMLSuite.commonObj.getSrcElement(e);
src.className = src.className.replace(' DHTMLSuite_calendarDropDown_dropDownArrowOver','');
this.scrollInHourDropDownActive = false;
}
,
__scrollInMinuteDropDown : function(scrollDirection)
{
if(!this.scrollInMinuteDropDownActive)return;
var ind = this.objectIndex;
this.minuteDropDownOffsetInMinute+=scrollDirection;
this.__populateMinutesInsideDropDownMinutes();
setTimeout('DHTMLSuite.variableStorage.arrayOfDhtmlSuiteObjects[' + ind + '].__scrollInMinuteDropDown(' + scrollDirection + ')',150);
}
,
__mouseoverUpAndDownArrowsInDropDownMinutes : function(e)
{
var ind = this.objectIndex;
if(document.all)e = event;
var src = DHTMLSuite.commonObj.getSrcElement(e);
var scrollDirection = (src.className.toLowerCase().indexOf('up')>=0?-1:1);
src.className = src.className + ' DHTMLSuite_calendarDropDown_dropDownArrowOver';
this.scrollInMinuteDropDownActive = true;
setTimeout('DHTMLSuite.variableStorage.arrayOfDhtmlSuiteObjects[' + ind + '].__scrollInMinuteDropDown(' + scrollDirection + ')',100);
}
,
__mouseoutUpAndDownArrowsInDropDownMinutes : function(e)
{
if(document.all)e = event;
var src = DHTMLSuite.commonObj.getSrcElement(e);
src.className = src.className.replace(' DHTMLSuite_calendarDropDown_dropDownArrowOver','');
this.scrollInMinuteDropDownActive = false;
}
,
__mouseoverYearInDropDown : function()
{
this.className = this.className + ' DHTMLSuite_calendar_dropdownAYearOver';
}
,
__mouseoutYearInDropDown : function()
{
this.className = this.className.replace(' DHTMLSuite_calendar_dropdownAYearOver','');
}
,
__mouseoverHourInDropDown : function()
{
this.className = this.className + ' DHTMLSuite_calendar_dropdownAnHourOver';
}
,
__mouseoutHourInDropDown : function()
{
this.className = this.className.replace(' DHTMLSuite_calendar_dropdownAnHourOver','');
}
,
__mouseoverMinuteInDropDown : function()
{
this.className = this.className + ' DHTMLSuite_calendar_dropdownAMinuteOver';
}
,
__mouseoutMinuteInDropDown : function()
{
this.className = this.className.replace(' DHTMLSuite_calendar_dropdownAMinuteOver','');
}
,
__mouseoverMonthInDropDown : function()
{
this.className = this.className + ' DHTMLSuite_calendar_dropdownAMonthOver';
}
,
__mouseoutMonthInDropDown : function()
{
this.className = this.className.replace(' DHTMLSuite_calendar_dropdownAMonthOver','');
}
,
__mouseoverCalendarDay : function()
{
this.className = this.className + ' DHTMLSuite_calendarDayOver';
}
,
__mouseoutCalendarDay : function()
{
this.className = this.className.replace(' DHTMLSuite_calendarDayOver','');
}
,
__mouseoverCalendarButton : function()
{
this.className = this.className + ' DHTMLSuite_calendarButtonOver';
}
,
__mouseoutCalendarButton : function()
{
this.className = this.className.replace(' DHTMLSuite_calendarButtonOver','');
}
,
__mouseoverMonthAndYear : function()
{
this.className = this.className + ' DHTMLSuite_calendarHeaderMonthAndYearOver';
}
,
__mouseoutMonthAndYear : function()
{
this.className = this.className.replace(' DHTMLSuite_calendarHeaderMonthAndYearOver','');
}	,
__mouseoverHourAndMinute : function()
{
this.className = this.className + ' DHTMLSuite_calendarTimeBarHourAndMinuteOver';
}
,
__mouseoutHourAndMinute : function()
{
this.className = this.className.replace(' DHTMLSuite_calendarTimeBarHourAndMinuteOver','');
}
,
__positionCalendar : function()
{
if(!this.positioningReferenceToHtmlElement)return;
this.divElement.style.position='absolute';
this.divElement.style.left = (DHTMLSuite.commonObj.getLeftPos(this.positioningReferenceToHtmlElement) + this.positioningOffsetXInPixels) + 'px';
this.divElement.style.top = (DHTMLSuite.commonObj.getTopPos(this.positioningReferenceToHtmlElement) + this.positioningOffsetYInPixels) + 'px';
}
,
__setInitialData : function(propertyArray)
{
if(propertyArray.id)this.id = propertyArray.id;
if(propertyArray.targetReference)this.targetReference = propertyArray.targetReference;
if(propertyArray.calendarModelReference)this.calendarModelReference = propertyArray.calendarModelReference;
if(propertyArray.callbackFunctionOnDayClick)this.callbackFunctionOnDayClick = propertyArray.callbackFunctionOnDayClick;
if(propertyArray.callbackFunctionOnMonthChange)this.callbackFunctionOnMonthChange = propertyArray.callbackFunctionOnMonthChange;
if(propertyArray.callbackFunctionOnClose)this.callbackFunctionOnClose = propertyArray.callbackFunctionOnClose;
if(propertyArray.displayCloseButton || propertyArray.displayCloseButton===false)this.displayCloseButton = propertyArray.displayCloseButton;
if(propertyArray.displayNavigationBar || propertyArray.displayNavigationBar===false)this.displayNavigationBar = propertyArray.displayNavigationBar;
if(propertyArray.displayTodaysDateInNavigationBar || propertyArray.displayTodaysDateInNavigationBar===false)this.displayTodaysDateInNavigationBar = propertyArray.displayTodaysDateInNavigationBar;
if(propertyArray.minuteDropDownInterval)this.minuteDropDownInterval = propertyArray.minuteDropDownInterval;
if(propertyArray.numberOfRowsInHourDropDown)this.numberOfRowsInHourDropDown = propertyArray.numberOfRowsInHourDropDown;
if(propertyArray.numberOfRowsInMinuteDropDown)this.numberOfRowsInHourDropDown = propertyArray.numberOfRowsInMinuteDropDown;
if(propertyArray.numberOfRowsInYearDropDown)this.numberOfRowsInYearDropDown = propertyArray.numberOfRowsInYearDropDown;
if(propertyArray.isDragable || propertyArray.isDragable===false)this.isDragable = propertyArray.isDragable;
if(propertyArray.displayTimeBar || propertyArray.displayTimeBar===false)this.displayTimeBar = propertyArray.displayTimeBar;
}
}
DHTMLSuite.windowModel = function(arrayOfProperties)
{
var id;							// Id of window.
var title;						// Title of window
var icon;						// Icon of window
var isDragable;					// Window is dragable ? (default = true)
var isResizable;				// Window is resizable ?
var isMinimizable;				// Window is minimizable ?
var isClosable;					// Window is closable
var xPos;						// Current x position of window
var yPos;						// Current y position of window
var width;						// Current width of window
var height;						// Current height of window
var cookieName;					// Name of cookie to store x,y,width,height,state,activeTab and zIndex
var state;						// State of current window(minimized,closed etc.)
var activeTabId;				// id of active tab
var tabsVisible;				// Tabs are visible? If not, we will only show a simple window with content and no tabs.
var zIndex;						// Current z-index of window.
var minWidth;					// Minimum width of window
var maxWidth;					// Maximum width of window
var minHeight;					// Minimum height of window
var maxHeight;					// Maximum height of window.
var windowContents;				// Array of DHTMLSuite.windowTabModel objects.
this.windowContents = new Array();
this.isDragable = true;
this.isMinimizable = true;
this.isResizable = true;
this.isClosable = true;
try{
if(!standardObjectsCreated)DHTMLSuite.createStandardObjects();	// This line starts all the init methods
}catch(e){
alert('You need to include the dhtmlSuite-common.js file\n'+ e.message);
}
if(arrayOfProperties)this.__setInitialWindowProperties(arrayOfProperties);
}
DHTMLSuite.windowModel.prototype = {
createWindowModelFromMarkUp : function(referenceToHTMLElement)
{
if(typeof referenceToHTMLElement == 'string'){
referenceToHTMLElement = document.getElementById(referenceToHTMLElement);
}
if(!referenceToHTMLElement){
alert('Error in windowModel class - Could not get a reference to element ' + referenceToHTMLElement);
return;
}
this.id = referenceToHTMLElement.id;
var properties = referenceToHTMLElement.getAttribute('windowProperties');
if(!properties)properties = referenceToHTMLElement.windowProperties;
this.__setInitialWindowProperties(DHTMLSuite.commonObj.getAssociativeArrayFromString(properties));
var subDivs = referenceToHTMLElement.getElementsByTagName('DIV');
for(var no=0;no<subDivs.length;no++){
if(subDivs[no].className.toLowerCase()=='dhtmlsuite_windowcontent'){	/* Window found */
var index = this.windowContents.length;
this.windowContents[index] = new DHTMLSuite.windowTabModel();
this.windowContents[index].__createContentModelFromHTMLElement(subDivs[no]);
}
}
}
,
__setInitialWindowProperties : function(arrayOfProperties)
{
if(arrayOfProperties.cookieName)this.cookieName=arrayOfProperties.cookieName;
if(arrayOfProperties.title)this.title=arrayOfProperties.title;
if(arrayOfProperties.icon)this.icon=arrayOfProperties.icon;
if(arrayOfProperties.width)this.width=arrayOfProperties.width;
if(arrayOfProperties.height)this.height=arrayOfProperties.width;
if(arrayOfProperties.isResizable)this.isResizable=arrayOfProperties.isResizable;
if(arrayOfProperties.isMinimizable)this.isMinimizable=arrayOfProperties.isMinimizable;
if(arrayOfProperties.isClosable)this.isClosable=arrayOfProperties.isClosable;
if(arrayOfProperties.state)this.state=arrayOfProperties.state;
if(arrayOfProperties.xPos)this.xPos=arrayOfProperties.xPos;
if(arrayOfProperties.yPos)this.xPos=arrayOfProperties.yPos;
if(arrayOfProperties.activeTabId)this.activeTabId=arrayOfProperties.activeTabId;
if(arrayOfProperties.minWidth)this.minWidth=arrayOfProperties.minWidth;
if(arrayOfProperties.maxWidth)this.maxWidth=arrayOfProperties.maxWidth;
if(arrayOfProperties.minHeight)this.minHeight=arrayOfProperties.minHeight;
if(arrayOfProperties.maxHeight)this.maxHeight=arrayOfProperties.maxHeight;
}
,
__getTitle : function()
{
return this.title;
}
,
__getContentObjects : function()
{
return this.windowContents;
}
,
__setActiveTabIdAutomatically : function()
{
for(var no=0;no<this.windowContents.length;no++){
if(!this.windowContents[no].isDeleted){
this.activeTabId = this.windowContents[no].id;
return;
}
}
}
,
__setContentUrl : function(contentId,url)
{
for(var no=0;no<this.windowContents.length;no++){
if(this.windowContents[no].id==contentId){
this.windowContents[no].__setContentUrl(url);
return true;
}
}
return false;
}
,
__getContentObjectById : function(contentId)
{
for(var no=0;no<this.windowContents.length;no++){
if(this.windowContents[no].id==contentId)return this.windowContents[no];
}
return false;
}
,
__setWidth : function(newWidth)
{
if(this.minWidth && newWidth/1<this.minWidth/1)newWidth = this.minWidth;
if(this.maxWidth && newWidth/1>this.maxWidth/1)newWidth = this.maxWidth;
this.width = newWidth;
}
,
__setHeight : function(newHeight)
{
if(this.minHeight && newHeight/1<this.minHeight/1)newHeight = this.minHeight;
if(this.maxHeight && newHeight/1>this.maxHeight/1)newHeight = this.maxHeight;
this.height = newHeight;
}
,
__setXPos : function(newXPos)
{
if(newXPos>DHTMLSuite.clientInfoObj.getBrowserWidth()){
newXPos = DHTMLSuite.clientInfoObj.getBrowserWidth()-30;
}
this.xPos = newXPos;
}
,
__setYPos : function(newYPos)
{
if(newYPos>DHTMLSuite.clientInfoObj.getBrowserHeight()){
newYPos = DHTMLSuite.clientInfoObj.getBrowserHeight()-30;
}
this.yPos = newYPos;
}
,
__setActiveTabId : function(newActiveTabId)
{
var index = this.__getIndexOfTabById(newActiveTabId);
if(index!==false && !this.__getIsDeleted(newActiveTabId)){
this.activeTabId = newActiveTabId;
return;
}
this.__setActiveTabIdAutomatically();
}
,
__setZIndex : function(zIndex)
{
this.zIndex = zIndex;
}
,
__setState : function(state)
{
this.state = state;
}
,
__getWidth : function()
{
return this.width;
}
,
__getHeight : function()
{
return this.height;
}
,
__getXPos : function()
{
if(this.xPos>DHTMLSuite.clientInfoObj.getBrowserWidth()){
xPos = DHTMLSuite.clientInfoObj.getBrowserWidth()-30;
}
return this.xPos;
}
,
__getYPos : function()
{
return this.yPos;
}
,
__getActiveTabId : function()
{
return this.activeTabId;
}
,
__getZIndex : function()
{
return this.zIndex;
}
,
__getState : function()
{
return this.state;
}
,
__deleteTab : function(idOfTab)
{
var index = this.__getIndexOfTabById(idOfTab);
if(index!==false){
this.windowContents[index].__setDeleted(true);
return true;
}
return false;
}
,
__restoreTab : function(idOfTab)
{
var index = this.__getIndexOfTabById(idOfTab);
if(index!==false){
this.windowContents[index].__setDeleted(false);
return true;
}
return false;
}
,
__getIndexOfTabById : function(idOfTab)
{
for(var no=0;no<this.windowContents.length;no++){
if(this.windowContents[no].id==idOfTab)return no;
}
return false;
}
,
__getIsDeleted : function(idOfTab)
{
var index = this.__getIndexOfTabById(idOfTab);
if(index!==false){
return this.windowContents[index].isDeleted;
}
}
,
__addTab : function(properties)
{
for(var no=0;no<this.windowContents.length;no++){	// Check if the window already exists.
if(this.windowContents[no].id==properties.id)return false;
}
var newIndex = this.windowContents.length;
this.windowContents[newIndex] = new DHTMLSuite.windowTabModel(properties);
return this.windowContents[newIndex];
}
}
DHTMLSuite.windowTabModel = function(tabProperties)
{
var tabTitle;
var textContent;
var id;
var htmlElementId;
var contentUrl;
var isDeleted;
if(tabProperties)this.__setInitialTabProperties(tabProperties);
}
DHTMLSuite.windowTabModel.prototype =
{
__createContentModelFromHTMLElement : function(elementReference)
{
if(typeof elementReference == 'string'){
elementReference = document.getElementById(elementReference);
}
this.textContent = elementReference.innerHTML;
var properties = elementReference.getAttribute('tabProperties');
if(!properties)properties = referenceToHTMLElement.tabProperties;
this.id = elementReference.id;
this.htmlElementId = elementReference.id;
this.__setInitialTabProperties(DHTMLSuite.commonObj.getAssociativeArrayFromString(properties));
}
,
__setInitialTabProperties : function(arrayOfProperties)
{
if(arrayOfProperties.tabTitle)this.tabTitle = arrayOfProperties.tabTitle;
if(arrayOfProperties.contentUrl)this.contentUrl = arrayOfProperties.contentUrl;
if(arrayOfProperties.id)this.id = arrayOfProperties.id;
if(arrayOfProperties.textContent)this.textContent = arrayOfProperties.textContent;
if(arrayOfProperties.htmlElementId)this.htmlElementId = arrayOfProperties.htmlElementId;
if(arrayOfProperties.isDeleted)this.htmlElementId = arrayOfProperties.isDeleted;
}
,
__setContentUrl : function(url)
{
this.contentUrl = url;
}
,
__setDeleted : function(isDeleted)
{
this.isDeleted = isDeleted;
}
}
DHTMLSuite.windowWidget = function(windowModel)
{
var windowModel;
var layoutCSS;
var objectIndex;
var divElement;					// parent element of divElementInner and eventual iframe(old MSIE)
var divElementInner;			// Div element for all the content of an iframe
var divTitleBar;				// Div element for the title bar.
var divElementContent;			// Div for the content
var divCloseButton;				// Close button
var divMinimizeButton;			// Minimize button
var divStatusBarTxt;			// Text element in status bar
var divResizeHandle;			// Div element for the resize handle
var iframeElement;				// Iframe element used to cover select boxes in IE.
var divElementTitle_txt;
var referenceToDragDropObject;	// Reference to object of class dragDropSimple.
var contentDivs;				// Array of div elements for the content
var resizeObj;					// Reference to object of class DHTMLSuite.resize
var slideSpeed;					// Slide speed when window is "sliding" into position by a windowCollection call.
this.layoutCSS = 'window.css';
this.contentDivs = new Array();
this.slideSpeed = 25;
try{
if(!standardObjectsCreated)DHTMLSuite.createStandardObjects();	// This line starts all the init methods
}catch(e){
alert('You need to include the dhtmlSuite-common.js file');
}
this.objectIndex = DHTMLSuite.variableStorage.arrayOfDhtmlSuiteObjects.length;
DHTMLSuite.variableStorage.arrayOfDhtmlSuiteObjects[this.objectIndex] = this;
if(windowModel)this.addWindowModel(windowModel);
}
DHTMLSuite.windowWidget.prototype = {
addWindowModel : function(windowModel)
{
this.windowModel = windowModel;
}
,
setLayoutCss : function(cssFileName)
{
this.layoutCSS = cssFileName;
}
,
setStatusBarText : function(text)
{
this.divStatusBarTxt.innerHTML = text;
}
,
setSlideSpeed : function(slideSpeed)
{
this.slideSpeed = slideSpeed;
}
,
init : function()
{
var ind = this.objectIndex;
DHTMLSuite.commonObj.loadCSS(this.layoutCSS);
this.__getWindowPropertiesFromCookie();
if(!this.windowModel.activeTabId)this.windowModel.__setActiveTabIdAutomatically();
this.__createPrimaryDivElements();
this.__createIframeElement();
this.__createTitleBar();
this.__createTabRow();
this.__createContentArea();
this.__createStatusBar();
this.__initiallyPopulateContentArea();
this.__displayActiveContent();
this.__populateTabRow();
this.__populateTitleBar();
this.__showHideButtonElements();
this.__makeWindowDragable();
this.__makeWindowResizable();
this.__initiallySetPositionAndSizeOfWindow();
setTimeout("DHTMLSuite.variableStorage.arrayOfDhtmlSuiteObjects[" +ind + "].__setSizeOfDivElements()",100);
}
,
loadContent : function(idOfWindowContentObject,url)
{
this.windowModel.__setContentUrl(idOfWindowContentObject,url);
var dynContent = new DHTMLSuite.dynamicContent();
var ref = this.windowModel.__getContentObjectById(idOfWindowContentObject);
if(ref)dynContent.loadContent(ref.htmlElementId,url);
}
,
activateTab : function(idOfContent)
{
this.windowModel.__setActiveTabId(idOfContent);
this.__setLayoutOfTabs();
this.__displayActiveContent();
this.__saveCookie();
}
,
deleteTab : function(idOfTab)
{
this.windowModel.__deleteTab(idOfTab);
if(this.windowModel.__getActiveTabId()==idOfTab)this.windowModel.__setActiveTabIdAutomatically();
this.__populateTabRow();
this.__setLayoutOfTabs();
this.__displayActiveContent();
}
,
restoreTab : function(idOfTab)
{
this.windowModel.__restoreTab(idOfTab);
this.__populateTabRow();
this.__setLayoutOfTabs();
this.__displayActiveContent();
}
,
addTab : function(tabProperties)
{
var contentObj = this.windowModel.__addTab(tabProperties);
if(contentObj){
this.__createContentForATab(contentObj);
this.__populateTabRow();
this.__setLayoutOfTabs();
this.__displayActiveContent();
}
}
,
setWidthOfWindow : function(newWidth)
{
this.windowModel.__setWidth(newWidth);
this.divElement.style.width = this.windowModel.__getWidth() + 'px';
this.__updateWindowModel();
}
,
setHeightOfWindow : function(newHeight)
{
this.windowModel.__setHeight(newHeight);
this.divElement.style.height = this.windowModel.__getHeight() + 'px';
this.__setSizeOfDivElements();
this.__updateWindowModel();
}
,
__createPrimaryDivElements : function()
{
this.divElement = document.createElement('DIV');
this.divElement.className = 'DHTMLSuite_window';
document.body.appendChild(this.divElement);
this.divElementInner = document.createElement('DIV');
this.divElementInner.className = 'DHTMLSuite_windowInnerDiv';
this.divElementInner.style.zIndex = 5;
this.divElement.appendChild(this.divElementInner);
}
,
__createIframeElement : function()
{
if(DHTMLSuite.clientInfoObj.isMSIE){
this.iframeElement = document.createElement('<IFRAME src="about:blank" frameborder=0>');
this.iframeElement.style.position = 'absolute';
this.iframeElement.style.top = '0px';
this.iframeElement.style.left = '0px';
this.iframeElement.style.width = '105%';
this.iframeElement.style.height = '105%';
this.iframeElement.style.zIndex = 1;
this.iframeElement.style.visibility = 'visible';
this.divElement.appendChild(this.iframeElement);
}
}
,
__createTitleBar : function()
{
var ind = this.objectIndex;
this.divTitleBar = document.createElement('DIV');	// Creating title bar div
this.divTitleBar.className = 'DHTMLSuite_windowTitleBar';
this.divElementInner.appendChild(this.divTitleBar);
var buttonDiv = document.createElement('DIV');		// Creating parent div for buttons
buttonDiv.className = 'DHTMLSuite_windowButtonDiv';
this.divTitleBar.appendChild(buttonDiv);
this.divCloseButton = document.createElement('DIV');		// Creating close button
this.divCloseButton.onmouseover = this.__mouseoverButton;
this.divCloseButton.onmouseout = this.__mouseoutButton;
this.divCloseButton.className='DHTMLSuite_windowCloseButton';
this.divCloseButton.onclick = function(){ return DHTMLSuite.variableStorage.arrayOfDhtmlSuiteObjects[ind].deleteWindow(); }
buttonDiv.appendChild(this.divCloseButton);
DHTMLSuite.commonObj.__addEventElement(this.divCloseButton);
this.divMinimizeButton = document.createElement('DIV');		// Creating minimize button
this.divMinimizeButton.onmouseover = this.__mouseoverButton;
this.divMinimizeButton.onmouseout = this.__mouseoutButton;
this.divMinimizeButton.className='DHTMLSuite_windowMinimizeButton';
this.divMinimizeButton.onclick = function(){ return DHTMLSuite.variableStorage.arrayOfDhtmlSuiteObjects[ind].minimizeWindow(); }
buttonDiv.appendChild(this.divMinimizeButton);
DHTMLSuite.commonObj.__addEventElement(this.divMinimizeButton);
this.divMaximizeButton = document.createElement('DIV');		// Creating maximize button
this.divMaximizeButton.onmouseover = this.__mouseoverButton;
this.divMaximizeButton.onmouseout = this.__mouseoutButton;
this.divMaximizeButton.className='DHTMLSuite_windowMaximizeButton';
this.divMaximizeButton.onclick = function(){ return DHTMLSuite.variableStorage.arrayOfDhtmlSuiteObjects[ind].maximizeWindow(); }
buttonDiv.appendChild(this.divMaximizeButton);
this.divMaximizeButton.style.display='none';
DHTMLSuite.commonObj.__addEventElement(this.divMaximizeButton);
this.divElementTitle_txt = document.createElement('DIV');		// Creating div element holding the title text
this.divElementTitle_txt.className = 'DHTMLSuite_windowTitleInTitleBar';
this.divTitleBar.onselectstart = function() { return DHTMLSuite.commonObj.cancelEvent() };
this.divTitleBar.appendChild(this.divElementTitle_txt);
DHTMLSuite.commonObj.__addEventElement(this.divTitleBar);
}
,
__createTabRow : function()
{
this.divElementTabRow = document.createElement('DIV');
this.divElementTabRow.className = 'DHTMLSuite_windowTabRow';
this.divElementInner.appendChild(this.divElementTabRow);
}
,
__createContentArea : function()
{
this.divElementContent = document.createElement('DIV');
this.divElementContent.className = 'DHTMLSuite_windowContent';
this.divElementInner.appendChild(this.divElementContent);
}
,
__createStatusBar : function()
{
this.divStatusBar = document.createElement('DIV');
this.divStatusBar.className = 'DHTMLSuite_windowStatusBar';
this.divElementInner.appendChild(this.divStatusBar);
this.divResizeHandle = document.createElement('DIV');
this.divResizeHandle.className = 'DHTMLSuite_windowResizeHandle';
this.divStatusBar.appendChild(this.divResizeHandle);
this.divStatusBarTxt = document.createElement('DIV');
this.divStatusBarTxt.className = 'DHTMLSuite_windowStatusBarText';
this.divStatusBar.appendChild(this.divStatusBarTxt);
}
,
__populateTitleBar : function()
{
this.divElementTitle_txt.innerHTML = this.windowModel.__getTitle();
}
,
__initiallyPopulateContentArea : function()
{
var contentObjects = this.windowModel.__getContentObjects();	// Get an array of content objects
for(var no=0;no<contentObjects.length;no++){
this.__createContentForATab(contentObjects[no]);
}
}
,
__createContentForATab : function(contentObject)
{
if(contentObject.htmlElementId){
if(document.getElementById(contentObject.htmlElementId)){
this.contentDivs[contentObject.id] = document.getElementById(contentObject.htmlElementId);
}else{
this.contentDivs[contentObject.id] = document.createElement('DIV');
this.contentDivs[contentObject.id].id = contentObject.htmlElementId;
}
this.divElementContent.appendChild(this.contentDivs[contentObject.id]);
}
if(contentObject.contentUrl){
this.loadContent(contentObject.id,contentObject.contentUrl);
}
this.contentDivs[contentObject.id].className = 'DHTMLSuite_windowContentInner';
this.contentDivs[contentObject.id].style.display='none';
}
,
__displayActiveContent : function()
{
var contentObjects = this.windowModel.__getContentObjects();	// Get an array of content objects
for(var no=0;no<contentObjects.length;no++){
if(contentObjects[no].id==this.windowModel.activeTabId)this.contentDivs[contentObjects[no].id].style.display='block'; else this.contentDivs[contentObjects[no].id].style.display='none';
}
}
,
__populateTabRow : function()
{
var ind = this.objectIndex;
this.divElementTabRow.innerHTML = '';	// Clear existing content from the tab row
var contentObjects = this.windowModel.__getContentObjects();	// Get an array of content objects
if(DHTMLSuite.clientInfoObj.isMSIE){
var table = document.createElement('<TABLE cellpadding="0" cellspacing="0" border="0">');
}else{
var table = document.createElement('TABLE');
table.setAttribute('cellpadding',0);
table.setAttribute('cellspacing',0);
table.setAttribute('border',0);
}
this.divElementTabRow.appendChild(table);
var row = table.insertRow(0);
for(var no=0;no<contentObjects.length;no++){
if(!this.windowModel.__getIsDeleted(contentObjects[no].id)){
var cell = row.insertCell(-1);
cell.className = 'DHTMLSuite_windowATab';
cell.id = 'windowTab_' + contentObjects[no].id;
cell.setAttribute('contentId',contentObjects[no].id);
cell.onclick = function(e){ DHTMLSuite.variableStorage.arrayOfDhtmlSuiteObjects[ind].__activateTabByClick(e); };
DHTMLSuite.commonObj.__addEventElement(cell);
var innerDiv = document.createElement('DIV');
innerDiv.className = 'DHTMLSuite_windowATabInnerDiv';
innerDiv.innerHTML = contentObjects[no].tabTitle;
cell.appendChild(innerDiv);
}
}
this.__setLayoutOfTabs();
}
,
__clearActiveAndInactiveStylingFromTabs : function()
{
var cells = this.divElementTabRow.getElementsByTagName('TD');	// Get an array of <td> elements.
var divs = this.divElementTabRow.getElementsByTagName('DIV');	// Get an array of <td> elements.
for(var no=0;no<cells.length;no++){
cells[no].className = cells[no].className.replace('DHTMLSuite_windowActiveTabCell','');
cells[no].className = cells[no].className.replace('DHTMLSuite_windowInactiveTabCell','');
cells[no].style.left = '0px';
}
for(var no=0;no<divs.length;no++){
divs[no].className = divs[no].className.replace(' DHTMLSuite_windowActiveTabCellContent','');
divs[no].className = divs[no].className.replace(' DHTMLSuite_windowInactiveTabCellContent','');
}
}
,
__setLayoutOfTabs : function()
{
this.__clearActiveAndInactiveStylingFromTabs();	// Clear layout of tab
var cells = this.divElementTabRow.getElementsByTagName('TD');	// Get an array of <td> elements.
var contentObjects = this.windowModel.__getContentObjects();	// Get an array of content objects
var activeTabIndex = 0;
for(var no=0;no<cells.length;no++){
if(cells[no].id=='windowTab_' + this.windowModel.activeTabId){
activeTabIndex = no;
break;
}
}
var leftPadding = 0;
if(activeTabIndex>0){
leftPadding = -7;
}
for(var no=1;no<activeTabIndex;no++){
cells[no].style.left = leftPadding +'px';
cells[no].style.zIndex = 100-no;
leftPadding-=7;
}
for(var no=activeTabIndex;no<cells.length;no++){
cells[no].style.left = leftPadding +'px';
cells[no].style.zIndex = 100-no;
leftPadding-=7;
}
cells[activeTabIndex].style.zIndex=200;
for(var no=0;no<cells.length;no++){
var div = cells[no].getElementsByTagName('DIV')[0];
if(no==activeTabIndex){
cells[no].className = cells[no].className + ' DHTMLSuite_windowActiveTabCell';
div.className = div.className + ' DHTMLSuite_windowActiveTabCellContent';
}else{
cells[no].className = cells[no].className + ' DHTMLSuite_windowInactiveTabCell';
div.className = div.className + ' DHTMLSuite_windowInactiveTabCellContent';
}
}
}
,
__setSizeOfDivElements : function()
{
try{
this.divElementContent.style.height = (this.divElementInner.offsetHeight - (this.divTitleBar.offsetHeight + this.divStatusBar.offsetHeight + this.divElementTabRow.offsetHeight + 8)) + 'px';
}catch(e){
this.divElementContent.style.height = '1px';
}
try{
if(this.windowModel.__getState()=='minimized')this.divElement.style.height = (this.divTitleBar.offsetHeight + this.divElementTabRow.offsetHeight + this.divStatusBar.offsetHeight) + 'px';
}catch(e)
{
}
}
,
__activateTabByClick : function(e)
{
if(document.all)e = event;
var src = DHTMLSuite.commonObj.getSrcElement(e);
if(src.tagName.toLowerCase()=='div')src = src.parentNode;
var idOfContent = src.getAttribute('contentId');
this.activateTab(idOfContent);
}
,
__updateWindowModel : function()
{
this.windowModel.__setWidth(this.divElement.style.width.replace('px','')/1);
if(this.windowModel.__getState()!='minimized')this.windowModel.__setHeight(this.divElement.style.height.replace('px','')/1);
this.windowModel.__setXPos(this.divElement.style.left.replace('px','')/1);
this.windowModel.__setYPos(this.divElement.style.top.replace('px','')/1);
this.windowModel.__setZIndex(this.divElement.style.zIndex);
this.__saveCookie();
}
,
__saveCookie : function()
{
if(!this.windowModel.cookieName)return;
var cookieValue = 'width:' + this.windowModel.__getWidth();
cookieValue+=',height:' + this.windowModel.__getHeight();
cookieValue+=',xPos:' + this.windowModel.__getXPos();
cookieValue+=',yPos:' + this.windowModel.__getYPos();
cookieValue+=',zIndex:' + this.divElement.style.zIndex;
cookieValue+=',activeTabId:' + this.windowModel.__getActiveTabId();
cookieValue+=',state:' + this.windowModel.__getState();
DHTMLSuite.commonObj.setCookie(this.windowModel.cookieName,cookieValue,500);
}
,
__getWindowPropertiesFromCookie : function()
{
if(!this.windowModel.cookieName)return;
var cookieValue = DHTMLSuite.commonObj.getCookie(this.windowModel.cookieName);
var propertyArray = DHTMLSuite.commonObj.getAssociativeArrayFromString(cookieValue);
if(!propertyArray)return;
if(propertyArray.width)this.windowModel.__setWidth(propertyArray.width);
if(propertyArray.height)this.windowModel.__setHeight(propertyArray.height);
if(propertyArray.xPos)this.windowModel.__setXPos(propertyArray.xPos);
if(propertyArray.yPos)this.windowModel.__setYPos(propertyArray.yPos);
if(propertyArray.zIndex)this.windowModel.__setZIndex(propertyArray.zIndex);
if(propertyArray.state)this.windowModel.__setState(propertyArray.state);
if(propertyArray.activeTabId)this.windowModel.__setActiveTabId(propertyArray.activeTabId);
}
,
__initiallySetPositionAndSizeOfWindow : function()
{
this.divElement.style.position='absolute';
var width = this.windowModel.__getWidth();
var height = this.windowModel.__getHeight();
var xPos = this.windowModel.__getXPos();
var yPos = this.windowModel.__getYPos();
var zIndex = this.windowModel.__getZIndex();
var state = this.windowModel.__getState();
if(width && width!='0')this.divElement.style.width = width + 'px';
if(height && height!='0')this.divElement.style.height = height + 'px';
if(xPos)this.divElement.style.left = xPos + 'px';
if(yPos)this.divElement.style.top = yPos + 'px';
if(zIndex)this.divElement.style.zIndex = zIndex;
if(state && state=='minimized')this.minimizeWindow();
}
,
__deleteTabByClick : function()
{
}
,
__makeWindowResizable : function()
{
if(!this.windowModel.isResizable)return;
var ind = this.objectIndex;
this.resizeObj = new DHTMLSuite.resize({ minWidth:this.windowModel.minWidth,minHeight:this.windowModel.minHeight,maxWidth:this.windowModel.maxWidth,maxHeight:this.windowModel.maxHeight } );
this.resizeObj.setElementRoResize(this.divElement);
this.resizeObj.addResizeHandle(this.divResizeHandle,'southeast');
this.resizeObj.setCallbackOnBeforeResize('DHTMLSuite.variableStorage.arrayOfDhtmlSuiteObjects[' + ind + '].__isOkToResize');
this.resizeObj.setCallbackOnAfterResize('DHTMLSuite.variableStorage.arrayOfDhtmlSuiteObjects[' + ind + '].__updateWindowModel');
this.resizeObj.setCallbackOnDuringResize('DHTMLSuite.variableStorage.arrayOfDhtmlSuiteObjects[' + ind + '].__setSizeOfDivElements');
this.resizeObj.init();
this.divStatusBarTxt.onselectstart = function() { return DHTMLSuite.commonObj.cancelEvent() };
this.divStatusBar.onselectstart = function() { return DHTMLSuite.commonObj.cancelEvent() };
DHTMLSuite.commonObj.__addEventElement(this.divStatusBarTxt);
DHTMLSuite.commonObj.__addEventElement(this.divStatusBar);
}
,
__isOkToResize : function()
{
if(this.windowModel.__getState()=='minimized')return false;
return true;
}
,
__makeWindowDragable : function()
{
var ind = this.objectIndex;
if(this.windowModel.isDragable){	// Add drag and drop features to the window.
this.referenceToDragDropObject = new DHTMLSuite.dragDropSimple(this.divElement,false,0,0,false);
this.referenceToDragDropObject.setCallbackOnAfterDrag('DHTMLSuite.variableStorage.arrayOfDhtmlSuiteObjects[' + ind + '].__updateWindowModel');
this.referenceToDragDropObject.setCallbackOnBeforeDrag('DHTMLSuite.variableStorage.arrayOfDhtmlSuiteObjects[' + ind + '].__updateWindowModel');
this.referenceToDragDropObject.addDragHandle(this.divTitleBar);
this.divTitleBar.style.cursor = 'move';
this.referenceToDragDropObject.__setNewCurrentZIndex(this.windowModel.__getZIndex());
}
}
,
deleteWindow : function()
{
this.divElement.style.display='none';
}
,
restoreWindow : function()
{
this.divElement.style.display='block';
}
,
minimizeWindow : function()
{
this.windowModel.__setState('minimized');
this.divElementContent.style.display='none';
this.divStatusBar.style.display='none';
this.divElementTabRow.style.display='none';
this.divMinimizeButton.style.display='none';
this.divMaximizeButton.style.display='block';
this.__setSizeOfDivElements();
this.__saveCookie();
}
,
maximizeWindow : function()
{
this.windowModel.__setState('maximized');
this.divElementContent.style.display='block';
this.divStatusBar.style.display='block';
this.divElementTabRow.style.display='block';
this.divMinimizeButton.style.display='block';
this.divMaximizeButton.style.display='none';
this.divElement.style.height = this.windowModel.__getHeight() + 'px';
this.__setSizeOfDivElements();
this.__saveCookie();
}
,
slideWindowToXAndY : function(toX,toY)
{
var slideFactors = this.__getSlideFactors(toX,toY);
var slideDirections = this.__getSlideDirections(toX,toY);
var slideTo = new Array();
slideTo.x = toX;
slideTo.y = toY;
var currentPos = new Array();
currentPos.x = this.windowModel.__getXPos();
currentPos.y = this.windowModel.__getYPos();
if(currentPos.x==slideTo.x && currentPos.y==slideTo.y)return;
this.__performSlide(slideTo,currentPos,slideFactors,slideDirections);
}
,
__performSlide : function(slideTo,currentPos,slideFactors,slideDirections)
{
var ind = this.objectIndex;
currentPos.x = currentPos.x/1 + (this.slideSpeed * slideFactors.y * slideDirections.x);
currentPos.y = currentPos.y/1 + (this.slideSpeed * slideFactors.x * slideDirections.y);
repeatSlide = false;
if(slideDirections.x<0){
if(currentPos.x<=slideTo.x){
currentPos.x = slideTo.x;
}else{
repeatSlide=true;
}
}else{
if(currentPos.x>=slideTo.x){
currentPos.x = slideTo.x;
}else{
repeatSlide=true;
}
}
if(slideDirections.y<0){
if(currentPos.y<=slideTo.y){
currentPos.y = slideTo.y;
}else{
repeatSlide=true;
}
}else{
if(currentPos.y>=slideTo.y){
currentPos.y = slideTo.y;
}else{
repeatSlide=true;
}
}
this.divElement.style.left = Math.round(currentPos.x) + 'px';
this.divElement.style.top = Math.round(currentPos.y) + 'px';
if(repeatSlide){
setTimeout('DHTMLSuite.variableStorage.arrayOfDhtmlSuiteObjects[' + ind + '].__performSlide({x:' + slideTo.x + ',y:' + slideTo.y + '},{x:' + currentPos.x + ',y:' + currentPos.y + '},{x:' + slideFactors.x + ',y:' + slideFactors.y + '},{ x:' + slideDirections.x + ',y:' + slideDirections.y + '})',10);
}else{
this.__updateWindowModel();
}
}
,
__getSlideFactors : function(toX,toY)
{
var retArray = new Array();
var currentX = this.windowModel.__getXPos();
var currentY = this.windowModel.__getYPos();
var distance_x = Math.abs(toX-currentX);
var distance_y = Math.abs(toY-currentY);
if(distance_x<distance_y){
retArray.x = distance_y/distance_x;
retArray.y = 1;
}else{
retArray.y = distance_x/distance_y;
retArray.x = 1;
}
return retArray;
}
,
__getSlideDirections : function(toX,toY)
{
var retArray = new Array();
if(toX<this.windowModel.__getXPos())retArray.x = -1; else retArray.x = 1;
if(toY<this.windowModel.__getYPos())retArray.y = -1; else retArray.y = 1;
return retArray;
}
,
__showHideButtonElements : function()
{
if(this.windowModel.isClosable)this.divCloseButton.style.display='block'; else this.divCloseButton.style.display='none';
}
,
__mouseoverButton : function()
{
this.className = this.className + ' DHTMLSuite_windowButtonOver';
}
,
__mouseoutButton : function()
{
this.className = this.className.replace(' DHTMLSuite_windowButtonOver','');
}
}
DHTMLSuite.windowCollection = function()
{
var windowWidgets;
var spaceBetweenEachWindowWhenCascaded;
var numberOfColumnsWhenTiled;
var divWindowsArea;							// Eventual div where the windows should be positioned
this.windowWidgets = new Array();
this.spaceBetweenEachWindowWhenCascaded = 20;
this.numberOfColumnsWhenTiled = 2;
}
DHTMLSuite.windowCollection.prototype =
{
addWindow : function(windowWidgetReference)
{
this.windowWidgets[this.windowWidgets.length] = windowWidgetReference;
}
,
tile : function()
{
this.windowWidgets = this.windowWidgets.sort(this.__sortItems);
var browserWidth = DHTMLSuite.clientInfoObj.getBrowserWidth()-20;
var browserHeight = DHTMLSuite.clientInfoObj.getBrowserHeight()-20;
var offsetX = 10;
var offsetY = 10;
if(this.divWindowsArea){
browserWidth = this.divWindowsArea.clientWidth;
browserHeight = this.divWindowsArea .clientHeight;
offsetX = DHTMLSuite.commonObj.getLeftPos(this.divWindowsArea);
offsetY = DHTMLSuite.commonObj.getTopPos(this.divWindowsArea);
}
var windowWidth = Math.floor(browserWidth/this.numberOfColumnsWhenTiled);
var windowHeight = Math.floor(browserHeight / Math.ceil(this.windowWidgets.length / this.numberOfColumnsWhenTiled));
for(var no=0;no<this.windowWidgets.length;no++){
this.windowWidgets[no].setWidthOfWindow(windowWidth);
this.windowWidgets[no].setHeightOfWindow(windowHeight-5);
var xPos = offsetX + (windowWidth * (no % this.numberOfColumnsWhenTiled));
var yPos = offsetY + (windowHeight * Math.floor((no) / this.numberOfColumnsWhenTiled));
this.windowWidgets[no].slideWindowToXAndY(xPos,yPos);
}
}
,
cascade : function()
{
this.windowWidgets = this.windowWidgets.sort(this.__sortItems);
var browserWidth = DHTMLSuite.clientInfoObj.getBrowserWidth() - 50;
var browserHeight = DHTMLSuite.clientInfoObj.getBrowserHeight() - 50;
var offsetX = 10;
var offsetY = 10;
if(this.divWindowsArea){
browserWidth = this.divWindowsArea.clientWidth;
browserHeight = this.divWindowsArea .clientHeight;
offsetX = DHTMLSuite.commonObj.getLeftPos(this.divWindowsArea);
offsetY = DHTMLSuite.commonObj.getTopPos(this.divWindowsArea);
}
var windowWidth = browserWidth - ((this.windowWidgets.length-1) * this.spaceBetweenEachWindowWhenCascaded);
var windowHeight = browserHeight - ((this.windowWidgets.length-1) * this.spaceBetweenEachWindowWhenCascaded);
for(var no=0;no<this.windowWidgets.length;no++){
this.windowWidgets[no].setWidthOfWindow(windowWidth);
this.windowWidgets[no].setHeightOfWindow(windowHeight);
this.windowWidgets[no].slideWindowToXAndY(offsetX + this.spaceBetweenEachWindowWhenCascaded*(no),offsetY + this.spaceBetweenEachWindowWhenCascaded*(no));
}
}
,
setNumberOfColumnsWhenTiled : function(numberOfColumnsWhenTiled)
{
this.numberOfColumnsWhenTiled = numberOfColumnsWhenTiled;
}
,
setDivWindowsArea : function(divWindowsArea)
{
if(typeof divWindowsArea == 'string'){
divWindowsArea = document.getElementById(divWindowsArea);
}
this.divWindowsArea = divWindowsArea;
}
,
__sortItems : function(a,b)
{
return a.windowModel.__getZIndex()-b.windowModel.__getZIndex();
}
}
DHTMLSuite.resize = function(propertyArray)
{
var resizeWhichElement;
var resizeHandles;
this.resizeHandles = new Array();
var preserveRatio;
var minWidth;
var maxWidth;
var minHeight;
var maxHeight;
var callbackOnBeforeResize;
var callbackOnAfterResize;
var callbackOnDuringResize;
var resizeTimer;
var resizeInWhichDirections;
var resizeHandleelativePath;
var objectIndex;
var mouseStartPos;					// Position of mouse pointer when the resize process starts
var initElementSize;
var currentResizeDirection;			// In which direction are we currently resizing the element(example: "west","east","southeast")
var classNameOfResizeHandles;		// Class name of resize handles, in case they are created dynamically
var layoutCSS;
var resizeHandlerOffsetInPixels;
var elementToResizeIsAbsolutePositioned;
var sizeOfWidthRelativeToHeight;
this.minWidth = 0;
this.minHeight = 0;
this.maxWidth = 150000;
this.maxHeight = 150000;
this.classNameOfResizeHandles = 'DHTMLSuite_resize_handle';
this.layoutCSS = 'resize.css';
this.resizeHandleelativePath = 'resize/small_square.gif';
this.resizeTimer = -1;
this.mouseStartPos = new Array();
this.initElementSize = new Array();
this.resizeHandlerOffsetInPixels = 4;
this.elementToResizeIsAbsolutePositioned = false;
try{
if(!standardObjectsCreated)DHTMLSuite.createStandardObjects();	// This line starts all the init methods
}catch(e){
alert('You need to include the dhtmlSuite-common.js file');
}
if(propertyArray)this.__setInitialProperties(propertyArray);
this.objectIndex = DHTMLSuite.variableStorage.arrayOfDhtmlSuiteObjects.length;
DHTMLSuite.variableStorage.arrayOfDhtmlSuiteObjects[this.objectIndex] = this;
}
DHTMLSuite.resize.prototype =
{
setLayoutCss : function(cssFileName)
{
this.layoutCSS = cssFileName;
}
,
setCssClassNameForResizeHandles : function(classNameOfResizeHandles)
{
this.classNameOfResizeHandles = classNameOfResizeHandles;
}
,
setElementRoResize : function(elementReference)
{
if(typeof elementReference == 'string'){
elementReference = document.getElementById(elementReference);
}
this.resizeWhichElement = elementReference;
}
,
addResizeHandle : function(resizeHandle,direction)
{
if(typeof resizeHandle == 'string'){
resizeHandle = document.getElementById(resizeHandle);
}
var index = this.resizeHandles.length;
this.resizeHandles[index] = new Array();
this.resizeHandles[index].element = resizeHandle;
this.resizeHandles[index].direction = direction;
}
,
setMinWidthInPixels : function(pixels)
{
this.minWidth = pixels;
}
,
setMaxWidthInPixels : function(pixels)
{
this.maxWidth = pixels;
}
,
setMinHeightInPixels : function(pixels)
{
this.minHeight = pixels;
}
,
setMaxHeightInPixels : function(pixels)
{
this.maxHeight = pixels;
}
,
setCallbackOnBeforeResize : function(functionName)
{
this.callbackOnBeforeResize = functionName;
}
,
setCallbackOnAfterResize : function(functionName)
{
this.callbackOnAfterResize = functionName;
}
,
setCallbackOnDuringResize : function(functionName)
{
this.callbackOnDuringResize = functionName;
}
,
setResizeHandlerOffsetInPixels : function(offsetInPx)
{
this.resizeHandlerOffsetInPixels = offsetInPx;
}
,
setIsResizeElementAbsolutePositioned : function(absolutePositioned)
{
this.elementToResizeIsAbsolutePositioned = absolutePositioned;
}
,
getReferenceToResizedElement : function()
{
return this.resizeWhichElement;
}
,
init : function()
{
DHTMLSuite.commonObj.loadCSS(this.layoutCSS);
this.__setAspectRatio();
this.__createResizeHandlesAutomatically();
this.__setCursorOfResizeHandles();
this.__addEventsToResizeHandles();
this.__addBasicEvents();
}
,
__setAspectRatio : function()
{
this.sizeOfWidthRelativeToHeight = this.resizeWhichElement.offsetWidth / this.resizeWhichElement.offsetHeight;
}
,
__setInitialProperties : function(propertyArray)
{
if(propertyArray.minWidth)this.minWidth = propertyArray.minWidth;
if(propertyArray.maxWidth)this.maxWidth = propertyArray.maxWidth;
if(propertyArray.minHeight)this.minHeight = propertyArray.minHeight;
if(propertyArray.maxHeight)this.maxHeight = propertyArray.maxHeight;
if(propertyArray.preserveRatio)this.preserveRatio = propertyArray.preserveRatio;
if(propertyArray.callbackOnBeforeResize)this.callbackOnBeforeResize = propertyArray.callbackOnBeforeResize;
if(propertyArray.callbackOnAfterResize)this.callbackOnAfterResize = propertyArray.callbackOnAfterResize;
if(propertyArray.callbackOnDuringResize)this.callbackOnDuringResize = propertyArray.callbackOnDuringResize;
if(propertyArray.resizeInWhichDirections)this.resizeInWhichDirections = propertyArray.resizeInWhichDirections;
}
,
__createResizeHandlesAutomatically : function()
{
if(this.resizeHandles.length>0)return;
if(!this.resizeInWhichDirections || this.resizeInWhichDirections=='all')this.resizeInWhichDirections = 'west,east,north,south,southeast,southwest,northwest,northeast';
var directions = this.resizeInWhichDirections.split(/,/g);
for(var no=0;no<directions.length;no++){
this.resizeHandles[no] = new Array();
this.resizeHandles[no].element = document.createElement('DIV');
this.resizeHandles[no].element.className = this.classNameOfResizeHandles;
this.resizeWhichElement.appendChild(this.resizeHandles[no].element);
this.resizeHandles[no].direction = directions[no];
var el = this.resizeHandles[no].element;
el.style.top = '50%';
el.style.left = '50%';
if(directions[no].indexOf('west')>=0)el.style.left = (0-this.resizeHandlerOffsetInPixels) + 'px';
if(directions[no].indexOf('east')>=0){
el.style.right = (0-this.resizeHandlerOffsetInPixels) + 'px';
el.style.left = '';
}
if(directions[no].indexOf('north')>=0)el.style.top = (0-this.resizeHandlerOffsetInPixels) + 'px';
if(directions[no].indexOf('south')>=0){
el.style.bottom = (0-this.resizeHandlerOffsetInPixels) + 'px';
el.style.top = '';
}
if(el.style.top=='50%')el.style.marginTop = '-' + Math.round(el.offsetHeight/2) + 'px';
if(el.style.left=='50%')el.style.marginLeft = '-' + Math.round(el.offsetWidth/2) + 'px';
}
}
,
__setCursorOfResizeHandles : function()
{
for(var no=0;no<this.resizeHandles.length;no++){
switch(this.resizeHandles[no].direction){
case "west":
case "east":
this.resizeHandles[no].element.style.cursor = 'e-resize';
break;
case "north":
case "south":
this.resizeHandles[no].element.style.cursor = 's-resize';
break;
case "northeast":
this.resizeHandles[no].element.style.cursor = 'ne-resize';
break;
case "northwest":
this.resizeHandles[no].element.style.cursor = 'nw-resize';
break;
case "southwest":
this.resizeHandles[no].element.style.cursor = 'sw-resize';
break;
case "southeast":
this.resizeHandles[no].element.style.cursor = 'se-resize';
break;
}
}
}
,
__addEventsToResizeHandles : function()
{
var ind = this.objectIndex;
for(var no=0;no<this.resizeHandles.length;no++){
this.resizeHandles[no].element.onmousedown = function(e){ DHTMLSuite.variableStorage.arrayOfDhtmlSuiteObjects[ind].__initResize(e); }
this.resizeHandles[no].element.onselectstart = function() { return DHTMLSuite.commonObj.cancelEvent() };
DHTMLSuite.commonObj.__addEventElement(this.resizeHandles[no].element);
this.resizeHandles[no].element.setAttribute('resizeInDirection',this.resizeHandles[no].direction);
this.resizeHandles[no].element.resizeInDirection = this.resizeHandles[no].direction;
}
}
,
__addBasicEvents : function()
{
var ind = this.objectIndex;
DHTMLSuite.commonObj.addEvent(document.documentElement,'mousemove',function(e){ DHTMLSuite.variableStorage.arrayOfDhtmlSuiteObjects[ind].__processResize(e); },ind);
DHTMLSuite.commonObj.addEvent(document.documentElement,'mouseup',function(e){ DHTMLSuite.variableStorage.arrayOfDhtmlSuiteObjects[ind].__endResize(e); },ind);
if(!document.documentElement.onselectstart)document.documentElement.onselectstart = function() { return DHTMLSuite.commonObj.__getOkToMakeTextSelections(); };
}
,
__initResize : function(e)
{
if(document.all)e = event;
var src = DHTMLSuite.commonObj.getSrcElement(e);
if(this.callbackOnBeforeResize){
var ok = this.__handleCallback('beforeResize');
if(!ok)return;
}
DHTMLSuite.commonObj.__setOkToMakeTextSelections(false);
this.resizeTimer = 0;
this.mouseStartPos.x = e.clientX;
this.mouseStartPos.y = e.clientY;
this.initElementSize.width = this.resizeWhichElement.offsetWidth;
this.initElementSize.height = this.resizeWhichElement.offsetHeight;
this.initElementSize.top = this.resizeWhichElement.style.top.replace('px','')/1;
if(this.elementToResizeIsAbsolutePositioned && !this.initElementSize.top)this.initElementSize.top = DHTMLSuite.commonObj.getTopPos(this.resizeWhichElement);
this.initElementSize.left = this.resizeWhichElement.style.left.replace('px','')/1;
if(this.elementToResizeIsAbsolutePositioned && !this.initElementSize.left)this.initElementSize.left = DHTMLSuite.commonObj.getLeftPos(this.resizeWhichElement);
this.currentResizeDirection = src.getAttribute('resizeInDirection');
this.__delayBeforeResize();
return false;
}
,
__delayBeforeResize : function()
{
if(this.resizeTimer>=0 && this.resizeTimer<5){
var ind = this.objectIndex;
this.resizeTimer++;
setTimeout('DHTMLSuite.variableStorage.arrayOfDhtmlSuiteObjects[' + ind + '].__delayBeforeResize()',20);
return;
}
}
,
__processResize : function(e)
{
if(document.all)e = event;
if(this.resizeTimer<5)return;
var newWidth = this.initElementSize.width;
var newHeight = this.initElementSize.height;
var newTop = this.initElementSize.top;
var newLeft = this.initElementSize.left;
switch(this.currentResizeDirection){
case "east":
case "northeast":
case "southeast":
newWidth = (this.initElementSize.width + e.clientX - this.mouseStartPos.x);
break;
}
switch(this.currentResizeDirection){
case "south":
case "southeast":
case "southwest":
newHeight = (this.initElementSize.height + e.clientY - this.mouseStartPos.y)
break;
}
if(this.currentResizeDirection.indexOf('north')>=0){
newTop = this.initElementSize.top  + e.clientY - this.mouseStartPos.y;
newHeight = newHeight - (newTop - this.initElementSize.top);
if(this.preserveRatio && this.currentResizeDirection=='north')newWidth = Math.round(newHeight * this.sizeOfWidthRelativeToHeight);
if(newHeight<this.minHeight){
newTop-=(this.minHeight-newHeight);
}
}
if(this.currentResizeDirection.indexOf('west')>=0){
newLeft = this.initElementSize.left  + e.clientX - this.mouseStartPos.x;
newWidth = newWidth - (newLeft - this.initElementSize.left);
if(this.preserveRatio && this.currentResizeDirection=='west')newHeight = Math.round(newWidth / this.sizeOfWidthRelativeToHeight);
if(newWidth<this.minWidth){
newLeft-=(this.minWidth-newWidth);
}
if(newWidth>this.maxWidth){
newLeft+=(newWidth-this.maxWidth);
}
}
if(newWidth<this.minWidth)newWidth = this.minWidth;
if(newHeight<this.minHeight)newHeight = this.minHeight;
if(this.maxWidth && newWidth>this.maxWidth)newWidth = this.maxWidth;
if(this.maxHeight && newHeight>this.maxHeight)newHeight = this.maxHeight;
if(this.currentResizeDirection.indexOf('east')>=0 && this.preserveRatio){
newHeight = Math.round(newWidth / this.sizeOfWidthRelativeToHeight);
}
if(this.currentResizeDirection.indexOf('south')>=0 && this.preserveRatio){
newWidth = Math.round(newHeight * this.sizeOfWidthRelativeToHeight);
}
if(this.currentResizeDirection=='northwest' && this.preserveRatio){
if(newWidth/newHeight > this.sizeOfWidthRelativeToHeight){
newHeight = Math.round(newWidth / this.sizeOfWidthRelativeToHeight);
}else{
newWidth = Math.round(newHeight * this.sizeOfWidthRelativeToHeight);
}
}
this.resizeWhichElement.style.width = newWidth + 'px';
this.resizeWhichElement.style.height = newHeight + 'px';
this.resizeWhichElement.style.top = newTop + 'px';
this.resizeWhichElement.style.left = newLeft + 'px';
if(this.callbackOnDuringResize)this.__handleCallback('duringResize');
}
,
__endResize : function(e)
{
DHTMLSuite.commonObj.__setOkToMakeTextSelections(true);
if(this.resizeTimer==5){
this.__handleCallback('afterResize');
}
this.resizeTimer=-1;
}
,
__handleCallback : function(action)
{
var ind = this.objectIndex;
var callbackString = '';
switch(action){
case "afterResize":
callbackString = this.callbackOnAfterResize;
break;
case "duringResize":
callbackString = this.callbackOnDuringResize;
break;
case "beforeResize":
callbackString = this.callbackOnBeforeResize;
break;
}
if(callbackString)callbackString = callbackString + '(DHTMLSuite.variableStorage.arrayOfDhtmlSuiteObjects[' + ind + '])';
try{
return eval(callbackString);
}catch(e){
alert('Could not execute call back string after resize');
}
}
}
