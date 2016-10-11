var minus_sign='img_2/ico_minus.gif';
var plus_sign='img_2/ico_plus.gif';

function writeError(str, append)
{
  var dbgObj = document.getElementById("debug");
  dbgObj.innerHTML = append? (dbgObj.innerHTML + str+"<br/>"): str;
}

function getRowLevel(id){
	if (id!=null){
		return id.substring(0, id.indexOf("@"));
	}
	return "";
}
function getRowRelativeNo(id){
	if (id!=null){
		return id.substring(id.indexOf("@")+1);
	}
	return "";
}

function toggleRows(caller,hideId){
	var closing=false;
	if(caller.alt=='shown') 
		closing=true;
	
	
	//Get the project name tr
	var parentTR =$("tr[id='"+hideId+"']")[0];
	//Get the project name div
	var parentdiv =$("tr[id='"+hideId+"'] td:eq(1) div:eq(1)")[0];
	var depthRelatVar=parentTR.depthRelatVar;
	
	var amountcell_1 = caller.parentNode.parentNode.parentNode.children[2].children[0];
	var amountcell_2;
	if(caller.parentNode.parentNode.parentNode.children[3]!=null){
		amountcell_2 = caller.parentNode.parentNode.parentNode.children[3].children[0];
	}
	var tb = document.getElementById('reportTable');
	var len = tb.rows.length;
	var found=false;
	var hideDepth=getRowLevel(depthRelatVar);
	var hideRelat=getRowRelativeNo(depthRelatVar);

	//hideAllRows();
	if(caller.alt=='hidden') {
		caller.alt='shown';
		caller.src=minus_sign;
		YAHOO.util.Dom.removeClass(parentdiv, 'desktop_project_name'); 
		YAHOO.util.Dom.addClass(parentdiv, 'desktop_project_name_op');
		if(hideDepth!=2){
			YAHOO.util.Dom.addClass(parentTR, 'desktop_project_name_op_levels');
			YAHOO.util.Dom.addClass(parentdiv, 'desktop_project_name_op_levels');
		}
		else{
			YAHOO.util.Dom.addClass(parentTR, 'desktop_project_name_op_level1');
			YAHOO.util.Dom.addClass(parentdiv, 'desktop_project_name_op_level1');
		}	
		YAHOO.util.Dom.removeClass(amountcell_1, 'desktop_project_count');
		YAHOO.util.Dom.addClass(amountcell_1, 'desktop_project_count_bolder');
		if(typeof amountcell_2 != 'undefined') {
			YAHOO.util.Dom.removeClass(amountcell_2, 'desktop_project_count');
			YAHOO.util.Dom.addClass(amountcell_2, 'desktop_project_count_bolder');
		}
			
		
	} else {
		caller.alt='hidden'; 
		caller.src=plus_sign;
		if(hideDepth!=2){
			YAHOO.util.Dom.removeClass(parentTR, 'desktop_project_name_op_levels');
			YAHOO.util.Dom.removeClass(parentdiv, 'desktop_project_name_op_levels');
		}
		else{
			YAHOO.util.Dom.removeClass(parentTR, 'desktop_project_name_op_level1');
			YAHOO.util.Dom.removeClass(parentdiv, 'desktop_project_name_op_level1');
		}	
		YAHOO.util.Dom.removeClass(parentdiv, 'desktop_project_name_op');
		YAHOO.util.Dom.addClass(parentdiv, 'desktop_project_name');
		
		YAHOO.util.Dom.removeClass(amountcell_1, 'desktop_project_count_bolder');
		YAHOO.util.Dom.addClass(amountcell_1, 'desktop_project_count');
		if (typeof amountcell_2 != 'undefined') {
			YAHOO.util.Dom.removeClass(amountcell_2,
					'desktop_project_count_bolder');
			YAHOO.util.Dom.addClass(amountcell_2, 'desktop_project_count');
		}
		
		
	}
	var display= (caller.alt!='shown')? 'none':'';
	tb = document.getElementById('reportTable');


	
	
	//writeError(+"<br/>", true);
	var notLevelTooGreat = false;
	var areGreaterLevels = false;
	for(i=1 ; i< len; i++){
		if (tb.rows[i].id=="ignoreToggle"){
			continue;
		}
		
		var rowDepth=getRowLevel(tb.rows[i].getAttribute('depthRelatVar'));
		var rowRelat=getRowRelativeNo(tb.rows[i].getAttribute('depthRelatVar'));
	
 		if(tb.rows[i].id!=null && tb.rows[i].id==hideId && !found) {
			found=true;
			continue;
		}
		if((rowDepth<=hideDepth) && (tb.rows[i].id!='') &&( tb.rows[i].id!=hideId) && (found)) {
			break;
		}
		if ((found)){
			if (hideDepth < rowDepth)
				areGreaterLevels = true;
			if (!notLevelTooGreat){
				if (rowDepth - 1 > hideDepth)
					notLevelTooGreat = true;
			}
			else{
				if ((rowDepth - 1 <= hideDepth)&&(rowDepth != ""))
					notLevelTooGreat = false;
			}
		}
		//alert("Found=" + found + " RowDepth=" + rowDepth + " HideDepth=" + hideDepth + " Arty=" + notLevelTooGreat);
		if (((found)&&(((!notLevelTooGreat)&&(rowDepth != ""))||((rowDepth == "")&&(!areGreaterLevels))))||((found)&&(closing)) ){
			tb.rows[i].style.display = display;
			var first=tb.rows[i].children[1].children[0].children[0];
			var amountdiv_1=tb.rows[i].children[2].children[0];
			if(tb.rows[i].children[3]!=null){
				var amountdiv_2=tb.rows[i].children[3].children[0];
				YAHOO.util.Dom.removeClass(amountdiv_2, 'desktop_project_count');
				YAHOO.util.Dom.addClass(amountdiv_2, 'desktop_project_count_sel desktop_project_name_sel');	
			}
			
			if (amountdiv_1 != null && amountdiv_1.nodeName!='FONT'){
				YAHOO.util.Dom.removeClass(amountdiv_1, 'desktop_project_count');
				YAHOO.util.Dom.addClass(amountdiv_1, 'desktop_project_count_sel desktop_project_name_sel');
			}
			if (first != null && first.nodeName!='FONT'){
				YAHOO.util.Dom.removeClass(first, 'desktop_project_count');
				YAHOO.util.Dom.addClass(first, 'desktop_project_count_sel desktop_project_name_sel');
			}
		}
		}

	//put sub-images to -
	imgs = tb.getElementsByTagName('img');
	found = false;
	if (closing){
		for(i=0 ; i< imgs.length; i++){
			var imgDepth=getRowLevel(imgs[i].depthRelatVar);
			var imgRelat=getRowRelativeNo(imgs[i].depthRelatVar);
			if ((imgDepth == hideDepth) && (imgRelat == hideRelat) && (imgs[i].name==hideId)){
				found = true;
				continue;
			}
			if((imgs[i].id=='toggleImage') && (found))  {
				imgs[i].alt='hidden';
				imgs[i].src=plus_sign;
			}
			//alert(imgDepth + "@" + imgRelat + "  " + found);
			if ((found) && (imgDepth == hideDepth))
				break;
		}
	}
	
}

function showAllRows() {
	
	tb = document.getElementById('reportTable');
	for(var i=1 ; i< tb.rows.length; i++){
		 tb.rows[i].style.display = '';
	}
	//change  +/- images now...
	imgs = tb.getElementsByTagName('img');
	for(var i=0 ; i< imgs.length; i++){
		if(imgs[i].id=='toggleImage')  {
			imgs[i].alt='shown';
			imgs[i].src=minus_sign;
		}
	}
}

function hideAllRows() {
	tb = document.getElementById('reportTable');
	for(var i=1 ; i< tb.rows.length; i++){
		if(tb.rows[i].title=='' || tb.rows[i].title>2) {
		 tb.rows[i].style.display = 'none';
		 }
	}

	//change  +/- images now...
	imgs = tb.getElementsByTagName('img');
	for(var i=0 ; i< imgs.length; i++){
		if(imgs[i].id=='toggleImage')  {
			imgs[i].alt='hidden';
			imgs[i].src=plus_sign;
		}
	}
}


var lastClickedRow=-1;
var lastClickedRowNum=-1;

/**
 * Sets/unsets the pointer and marker in browse mode
 *
 * @param   object    the table row
 * @param   interger  the row number
 * @param   string    the action calling this script (over, out or click)
 * @param   string    the default background color
 * @param   string    the color to use for mouseover
 * @param   string    the color to use for marking a row
 *
 * @return  boolean  whether pointer is set or not
 */
function setPointer(theRow, theRowNum, theAction, theDefaultColor, thePointerColor, theMarkColor)
{
    var theCells = null;
	var marked_row = new Array;
	var lastClickedCells = null;
	var firsColor=null;

	
    // 1. Pointer and mark feature are disabled or the browser can't get the
    //    row -> exits
    if ((thePointerColor == '' && theMarkColor == '')
        || typeof(theRow.style) == 'undefined') {
        return false;
    }

    // 2. Gets the current row and exits if the browser can't get it
    if (typeof(document.getElementsByTagName) != 'undefined') {
        theCells = theRow.getElementsByTagName('td');
        if (lastClickedRow != -1) lastClickedCells=lastClickedRow.getElementsByTagName('td');
    }
    else if (typeof(theRow.cells) != 'undefined') {
        theCells = theRow.cells;
        if (lastClickedRow != -1) lastClickedCells=lastClickedRow.cells;
    }
    else {
        return false;
    }

    // 3. Gets the current color...
    var rowCellsCnt  = theCells.length;
    var domDetect    = null;
    var currentColor = null;
    var newColor     = null;
    // 3.1 ... with DOM compatible browsers except Opera that does not return
    //         valid values with "getAttribute"
    if (typeof(window.opera) == 'undefined'
        && typeof(theCells[0].getAttribute) != 'undefined') {
        currentColor = theCells[0].getAttribute('bgcolor');
        domDetect    = true;
    }
    // 3.2 ... with other browsers
    else {
        currentColor = theCells[0].style.backgroundColor;
        domDetect    = false;
    } // end 3
	// alert(currentColor);
    // 3.3 ... Opera changes colors set via HTML to rgb(r,g,b) format so fix it
    
    if (currentColor == null) {
    	currentColor = '';    	
    }
    
    if (currentColor.indexOf("rgb") >= 0)
    {
        var rgbStr = currentColor.slice(currentColor.indexOf('(') + 1,
                                     currentColor.indexOf(')'));
        var rgbValues = rgbStr.split(",");
        currentColor = "#";
        var hexChars = "0123456789ABCDEF";
        for (var i = 0; i < 3; i++)
        {
            var v = rgbValues[i].valueOf();
            currentColor += hexChars.charAt(v/16) + hexChars.charAt(v%16);
        }
    }

    // 4. Defines the new color
    // 4.1 Current color is the default one
	//alert("Current="+currentColor+" Default="+ theDefaultColor);
    if (currentColor == ''
        || currentColor.toLowerCase() == theDefaultColor.toLowerCase()) {
        if (theAction == 'over' && thePointerColor != '') {
            newColor              = thePointerColor;
            //alert("New = "+ newColor);
        }
        else if (theAction == 'click' && theMarkColor != '') {
            newColor              = theMarkColor;
            marked_row[theRowNum] = true;

            // Garvin: deactivated onclick marking of the checkbox because it's also executed
            // when an action (like edit/delete) on a single item is performed. Then the checkbox
            // would get deactived, even though we need it activated. Maybe there is a way
            // to detect if the row was clicked, and not an item therein...
            // document.getElementById('id_rows_to_delete' + theRowNum).checked = true;
        }
    }
    // 4.1.2 Current color is the pointer one
    else if (currentColor.toLowerCase() == thePointerColor.toLowerCase()
             && (typeof(marked_row[theRowNum]) == 'undefined' || !marked_row[theRowNum])) {
        if (theAction == 'out') {
            newColor              = theDefaultColor;
        }
        else if (theAction == 'click' && theMarkColor != '') {
            newColor              = theMarkColor;
            marked_row[theRowNum] = true;
            // document.getElementById('id_rows_to_delete' + theRowNum).checked = true;
        }
    }
    // 4.1.3 Current color is the marker one
    else if (currentColor.toLowerCase() == theMarkColor.toLowerCase()) {
        if (theAction == 'click') {
            newColor              = (thePointerColor != '')
                                  ? thePointerColor
                                  : theDefaultColor;
            marked_row[theRowNum] = (typeof(marked_row[theRowNum]) == 'undefined' || !marked_row[theRowNum])
                                  ? true
                                  : null;
            // document.getElementById('id_rows_to_delete' + theRowNum).checked = false;
        }
    } // end 4

    // 5. Sets the new color...

    if (newColor) {
        var c = null;
        var aux=null;
        // 5.1 ... with DOM compatible browsers except Opera
        if (domDetect) {
        	if(lastClickedRowNum == -1) firstColor=currentColor;
            for (c = 0; c < rowCellsCnt; c++) {
            	//alert("Set2 color = " + newColor);
                theCells[c].setAttribute('bgcolor', newColor, 0);
                 if (lastClickedRow != -1&&lastClickedCells!=null)
                 	{
                 		if(lastClickedRowNum==-1){
                                lastClickedCells[c].setAttribute('bgcolor', firstColor, 0);
                                }
                 		//else {
                 		//		alert("PLM2");
                 		//	    lastClickedCells[c].setAttribute('bgcolor', lastClickedRowNum%2==0?"#eeeeee":"#dddddd", 0);
                        //       }
                 	} // theDefaultColor
                 lastClickedRow=theRow;
            } // end for
            lastClickedRowNum=theRowNum;
        }
        // 5.2 ... with other browsers
        else {
            for (c = 0; c < rowCellsCnt; c++) {
                theCells[c].style.backgroundColor = newColor;
             	if (lastClickedRow != -1)  lastClickedCells[c].setAttribute('bgcolor', theDefaultColor, 0);
                lastClickedRow=theRow;

            }
        }
    } // end 5

    return true;
} // end of the 'setPointer()' function

function setPointerhtml(theRow,theAction,color,theDefaultColor)
{
	if (theAction == 'click' && color != '' && theRow.className != 'clicked'){
		theRow.style.backgroundColor=color;
		theRow.className='clicked';
	}else if(theAction == 'click' && color != '' && theRow.className == 'clicked'){
		theRow.style.backgroundColor=theDefaultColor;
		theRow.className='';
	}else if(theAction != 'click' && color != '' && theRow.className != 'clicked'){
		theRow.style.backgroundColor=color;
	}
		
    return true;
} // end of the 'setPointer()' function

RowSelector.prototype.objectMap	= new Object();
RowSelector.prototype.uniqueId	= 0;

function RowSelector(rowEl, propertyObj, callback ) {
	 if (rowEl != null){
	 	this.propertyObj = propertyObj;
	 	this.rowEl = rowEl;
	 	this.originalColor = "white";
	 	if ( rowEl.style != null && rowEl.style.backgroundColor != null )
	 		this.originalColor = rowEl.style.backgroundColor;
	 	this.markerColor = propertyObj.markerColor;
	 	this.skippedClass = propertyObj.skippedClass;
	 	this.baseId = propertyObj.baseId;                   
	 	this.forever = false;
	 	this.isMarked = false;
	 	this.callback = callback;
	 }
}

function getRowSelectorInstance(rowEl, propertyObj, callback, unique ){
	var objectMap = RowSelector.prototype.objectMap;
	if (unique != null && unique) {
		objectMap = UniqueRowSelector.prototype.objectMap;
	}
	if ( rowEl.id == null || rowEl.id.length==0) {
		rowEl.id = propertyObj.baseId + (RowSelector.prototype.uniqueId++);
	}
	var rowObj	= objectMap[rowEl.id];
	if (rowObj == null) { 
		if (unique != null && unique) {
			rowObj	= new UniqueRowSelector(rowEl, propertyObj, callback );
		}
		else{
			rowObj	= new RowSelector(rowEl, propertyObj, callback );
		}
		objectMap[rowEl.id]	= rowObj;
	}
	return rowObj;
}
RowSelector.prototype.colorRow	= function (color) {
	var children	= this.rowEl.childNodes;
	if (children!=null && children.length!=null) {
		for (var i=0; i<children.length; i++) {
			var child		= children[i];
			var childYuiEl	= new YAHOO.util.Element(child);
			//alert("childYuiEl: " + childYuiEl);
			if ( child.nodeName.toLowerCase()=="td" && (this.skippedClass==null || !childYuiEl.hasClass(this.skippedClass) ) ) {
			//if ( child.nodeName.toLowerCase()=="td" && (this.skippedClass==null ) ) {
				child.style.backgroundColor	= color;
			}
		}
	}
};

RowSelector.prototype.markRow	= function (forever) {
	if ( forever ) {
		this.colorRow( this.markerColor );
		this.forever	= true;
	}
	else 
		if ( !this.forever ) {
			this.colorRow( this.markerColor );
		}
};

RowSelector.prototype.unmarkRow	= function (forever) {
	if ( forever ) {
		this.colorRow("");
		this.forever	= false;
	}
	else 
		if ( !this.forever ) {
			this.colorRow("");
		}
};

RowSelector.prototype.toggleRow	= function () {
	if ( this.isMarked ) {
		this.unmarkRow(true);
		this.isMarked	= false;
		if (this.callback != null && this.callback.onDeselect != null ) {
			this.callback.onDeselect();
		}
	}
	else { 
		this.markRow(true);
		this.isMarked	= true;
		if (this.callback != null && this.callback.onSelect != null) {
			this.callback.onSelect();
		}
	}
};

UniqueRowSelector.prototype				= new RowSelector();
UniqueRowSelector.prototype.parent		= RowSelector;
UniqueRowSelector.prototype.constructor	= UniqueRowSelector;
UniqueRowSelector.prototype.objectMap	= new Object();
UniqueRowSelector.prototype.lastSelectedRow		= null;

function UniqueRowSelector(rowEl, skippedClass, baseId, markerColor, callback ) {
	this.parent.call(this, rowEl, skippedClass, baseId, markerColor, callback );
}

UniqueRowSelector.prototype.toggleRow	= function () {
	if ( this.isMarked ) {
		this.unmarkRow(true);
		this.isMarked	= false;
		if (this.callback != null && this.callback.onDeselect != null ) {
			this.callback.onDeselect();
		}
	}
	else { 
		this.markRow(true);
		this.isMarked	= true;
		if (this.propertyObj.lastSelectedRow != null) {
			this.propertyObj.lastSelectedRow.isMarked	= false;
			this.propertyObj.lastSelectedRow.unmarkRow(true);
		}
		this.propertyObj.lastSelectedRow = this;
		if (this.callback != null && this.callback.onSelect != null) {
			this.callback.onSelect();
		}
	}
};

function RowManagerProperty(skippedClass, baseId, markerColor) {
	this.lastSelectedRow	=  null;
	this.markerColor		= markerColor;
	this.skippedClass		= skippedClass;
	this.baseId				= baseId;
}

function sortHierarchy( columnName, prevOrder ) {
	//alert (columnName + "!!");
	var descending			= 1; 
	if ( prevOrder=="descending" ) 
		descending			= 0;
	var subForm				= document.getElementsByName("aimAdvancedReportForm")[0];
	for ( var i=0; i<subForm.levelPicked.options.length; i++ ) {
		if (subForm.levelPicked.options[i].text.trim() == columnName) { 
			subForm.levelPicked.selectedIndex	= i;
			break;
		}
	}
	subForm.levelSorter.selectedIndex			= 0;
	subForm.levelSortOrder.selectedIndex		= descending;
	subForm.applySorter.click();
	
}
