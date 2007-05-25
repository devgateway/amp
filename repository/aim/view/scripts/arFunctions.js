var minus_sign='/repository/aim/view/images/images_dhtmlsuite/dhtmlgoodies_minus.gif';
var plus_sign='/repository/aim/view/images/images_dhtmlsuite/dhtmlgoodies_plus.gif';

function writeError(str, append)
{
  var dbgObj = document.getElementById("debug");
  dbgObj.innerHTML = append? (dbgObj.innerHTML + str+"<br/>"): str;
}


function toggleRows(caller,hideId){
	var exitnow=false;
	if(caller.alt=='shown') exitnow=true;	
	
 	hideAllRows();
 	if(exitnow) return;
	if(caller.alt=='hidden') {caller.alt='shown';caller.src=minus_sign;} else {caller.alt='hidden'; caller.src=plus_sign;}
	var display= (caller.alt!='shown')? 'none':'';
	tb = document.getElementById('reportTable');
	

	var len = tb.rows.length;
	var found=false;
	var hideDepth=document.getElementById(hideId).title;
	

	//writeError(+"<br/>", true);
	for(i=1 ; i< len; i++){
		var rowDepth=tb.rows[i].title;
 		if(tb.rows[i].id!=null && tb.rows[i].id==hideId && !found) {
		found=true;continue;
		}
		if(rowDepth<=hideDepth && tb.rows[i].id!='' && tb.rows[i].id!=hideId && found) {
		break;
		}
		if (found) tb.rows[i].style.display = display;
	}
	
	//put sub-images to -
	imgs = tb.getElementsByTagName('img');	
	for(i=0 ; i< imgs.length; i++){
		if(imgs[i].id=='toggleImage' && imgs[i].title>hideDepth)  {
			imgs[i].alt='shown';
			imgs[i].src=minus_sign;
		 }
	}
}


function hideAllRows() {
	tb = document.getElementById('reportTable');	
	for(i=1 ; i< tb.rows.length; i++){
		if(tb.rows[i].title=='' || tb.rows[i].title>2) {
		 tb.rows[i].style.display = 'none';		
		 }
	}
	
	//change  +/- images now...
	imgs = tb.getElementsByTagName('img');	
	for(i=0 ; i< imgs.length; i++){
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
	var firsColor=null



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
    if (currentColor == ''
        || currentColor.toLowerCase() == theDefaultColor.toLowerCase()) {
        if (theAction == 'over' && thePointerColor != '') {
            newColor              = thePointerColor;
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
                theCells[c].setAttribute('bgcolor', newColor, 0);
                 if (lastClickedRow != -1)  
                 	{
                 		if(lastClickedRowNum==-1) lastClickedCells[c].setAttribute('bgcolor', firstColor, 0);
                 		else lastClickedCells[c].setAttribute('bgcolor', lastClickedRowNum%2==0?"#FFFFFF":"DDDDDD", 0);
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

