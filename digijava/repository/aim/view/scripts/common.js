function unload() {
}

/*
<SCRIPT LANGUAGE="javascript">
<!--
window.open ('titlepage.html', 'newwindow', config='height=100,
width=400, toolbar=yes, menubar=yes, scrollbars=no, resizable=yes,
location=no, directories=no, status=no')
-->
</SCRIPT>
*/

function load() {
}

function openURLinWindow(url,wndWidth, wndHeight) {
	window.name = "opener" + new Date().getTime();
	if (wndWidth == null || wndWidth == 0 || wndHeight == null || wndHeight == 0) {
		wndWidth = window.screen.availWidth/2;
		wndHeight = window.screen.availHeight/2;
	}
	var t = ((screen.width)-wndWidth)/2;
	var l = ((screen.height)-wndHeight)/2;	
	popupPointer = window.open(url, "forumPopup", "height=" + wndHeight + ",width=" + wndWidth + ",top=" + l + ",left=" + t +",menubar=no,scrollbars=yes");
}

function openResisableWindow(wndWidth, wndHeight) {
	window.name = "opener" + new Date().getTime();
	if (wndWidth == null || wndWidth == 0 || wndHeight == null || wndHeight == 0) {
		wndWidth = window.screen.availWidth/2;
		wndHeight = window.screen.availHeight/2;
	}
	var t = ((screen.width)-wndWidth)/2;
	var l = ((screen.height)-wndHeight)/2;	
	popupPointer = window.open("about:blank", "forumPopup", "height=" + wndHeight + ",width=" + wndWidth + ",top=" + l + ",left=" + t +",toolbar=yes,menubar=yes,scrollbars=yes,resizable=yes,location=no, directories=no, status=no");
}

function openNewWindow(wndWidth, wndHeight){
	window.name = "opener" + new Date().getTime();
	if (wndWidth == null || wndWidth == 0 || wndHeight == null || wndHeight == 0) {
		wndWidth = window.screen.availWidth/2;
		wndHeight = window.screen.availHeight/2;
	}
	var t = ((screen.width)-wndWidth)/2;
	var l = ((screen.height)-wndHeight)/2;	
	popupPointer = window.open("about:blank", "forumPopup", "height=" + wndHeight + ",width=" + wndWidth + ",top=" + l + ",left=" + t +",menubar=no,scrollbars=yes");
}

function checkNumeric(objName,period) {
	var numberfield = objName;
	if (chkNumeric(objName) == false) {
		numberfield.select();
		numberfield.focus();
		return false;
	} else {
		return true;
	}
}

function containsValidNumericValue(objName) {
	if (chkNumeric(objName) == true) {
		if (objName.value == 0) {
			return false;
		} else {
			return true;
		}
	} else {
		return false;
	}
}

function chkNumeric(objName)
{
	var checkOK = "0123456789.";
	var checkStr = objName;
	var allValid = true;
	var decPoints = 0;
	var periodFlag = 0;
	var allNum = "";

	for (i = 0;  i < checkStr.value.length;  i++) {
		ch = checkStr.value.charAt(i);
		if (ch == ".") {
			if (periodFlag == 1) {
				allValid = false;
				break;
			}
			periodFlag = 1;
		}
		for (j = 0;  j < checkOK.length;  j++)
			if (ch == checkOK.charAt(j))
				break;
				
		if (j == checkOK.length) {
			allValid = false;
			break;
		}
	}
	
	return allValid;
}


function trim(inputString) {
   // Removes leading and trailing spaces from the passed string. Also removes
   // consecutive spaces and replaces it with one space. If something besides
   // a string is passed in (null, custom object, etc.) then return the input.
   if (typeof inputString != "string") { return inputString; }
   var retValue = inputString;
   var ch = retValue.substring(0, 1);
   while (ch == " ") { // Check for spaces at the beginning of the string
      retValue = retValue.substring(1, retValue.length);
      ch = retValue.substring(0, 1);
   }
   ch = retValue.substring(retValue.length-1, retValue.length);
   while (ch == " ") { // Check for spaces at the end of the string
      retValue = retValue.substring(0, retValue.length-1);
      ch = retValue.substring(retValue.length-1, retValue.length);
   }
   while (retValue.indexOf("  ") != -1) { // Note that there are two spaces in the string - look for multiple spaces within the string
      retValue = retValue.substring(0, retValue.indexOf("  ")) + retValue.substring(retValue.indexOf("  ")+1, retValue.length); // Again, there are two spaces in each of the strings
   }
   return retValue; // Return the trimmed string back to the user
} // Ends the "trim"


// returns true- empty and false for notEmpty
function isEmpty(value)
{
	value = trim(value);
	if(value.length > 0)
		return false;
	else
		return true;	
}
