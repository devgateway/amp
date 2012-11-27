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
 var keepChecking=true;

function test(callerId) {
		caller = document.getElementById(callerId);
		inputs = caller.getElementsByTagName('input');
		for(i=1 ; i< inputs.length; i++){
			;//alert(inputs[i].id.indexOf("moduleEdit"));
		}

}

function toggleChildrenEdit(callerId) {
		caller = document.getElementById(callerId);
		inputs = caller.getElementsByTagName('input');
		var found=0;
		for(i=1 ; i< inputs.length; i++){
			if(inputs[i].type!="checkbox") continue;
			if(inputs[i].id.indexOf("moduleEdit")==0 ||
			inputs[i].id.indexOf("featureEdit")==0 ||
			inputs[i].id.indexOf("fieldEdit")==0)
			{
				if(found!=1)
				{
					found=1;
					inputaux=inputs[i];
				}
			}

			if(inputs[i].id.indexOf("moduleEdit")==0 ||
			inputs[i].id.indexOf("featureEdit")==0 ||
			inputs[i].id.indexOf("fieldEdit")==0)
			 {
			 	inputs[i].checked=inputaux.checked;
			 }
		}

}

function toggleChildrenVisibility(callerId) {
//alert("aaa"+callerId);

		caller = document.getElementById(callerId);
	//	alert("caller-"+caller);
		inputs = caller.getElementsByTagName('input');
		for(i=1 ; i< inputs.length; i++){
			if(inputs[i].type!="checkbox") continue;
			if(inputs[i].id.indexOf("moduleEdit")==0 ||
			inputs[i].id.indexOf("featureEdit")==0 ||
			inputs[i].id.indexOf("fieldEdit")==0 ||
			inputs[i].id.indexOf("moduleMandatory")==0 ||
			inputs[i].id.indexOf("featureMandatory")==0 ||
			inputs[i].id.indexOf("fieldMandatory")==0) continue;
			inputs[i].checked=inputs[0].checked;
		}

}


function toggleChildrenMandatory(callerId) {
		caller = document.getElementById(callerId);
		inputs = caller.getElementsByTagName('input');
		var found=0;
		for(i=1 ; i< inputs.length; i++){
			if(inputs[i].type!="checkbox") continue;
			if(inputs[i].id.indexOf("moduleMandatory")==0 ||
			inputs[i].id.indexOf("featureMandatory")==0 ||
			inputs[i].id.indexOf("fieldMandatory")==0)
			{
				if(found!=1)
				{
					found=1;
					inputaux=inputs[i];
				}
			}

			if(inputs[i].id.indexOf("moduleMandatory")==0 ||
			inputs[i].id.indexOf("featureMandatory")==0 ||
			inputs[i].id.indexOf("fieldMandatory")==0)
			 {
			 	inputs[i].checked=inputaux.checked;
			 }
		}

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

function openURLinResizableWindow(url,wndWidth, wndHeight) {
	window.name = "opener" + new Date().getTime();
	if (wndWidth == null || wndWidth == 0 || wndHeight == null || wndHeight == 0) {
		wndWidth = window.screen.availWidth/2;
		wndHeight = window.screen.availHeight/2;
	}
	var t = ((screen.width)-wndWidth)/2;
	var l = ((screen.height)-wndHeight)/2;

	popupPointer = window.open(url, "forumPopup", "height=" + wndHeight + ",width=" + wndWidth + ",top=" + l + ",left=" + t +",menubar=no,scrollbars=yes,resizable");
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

function openNewWindowWithName(wndWidth, wndHeight, popupName){
	window.name = "opener" + new Date().getTime();
	if (wndWidth == null || wndWidth == 0 || wndHeight == null || wndHeight == 0) {
		wndWidth = window.screen.availWidth/2;
		wndHeight = window.screen.availHeight/2;
	}
	var t = ((screen.width)-wndWidth)/2;
	var l = ((screen.height)-wndHeight)/2;

	popupPointer = window.open("about:blank",popupName, "height=" + wndHeight + ",width=" + wndWidth + ",top=" + l + ",left=" + t +",menubar=no,scrollbars=yes");
}


function openNewWindowWithMenubar(wndWidth, wndHeight){
	window.name = "opener" + new Date().getTime();
	if (wndWidth == null || wndWidth == 0 || wndHeight == null || wndHeight == 0) {
		wndWidth = window.screen.availWidth/2;
		wndHeight = window.screen.availHeight/2;
	}
	var t = ((screen.width)-wndWidth)/2;
	var l = ((screen.height)-wndHeight)/2;

	popupPointer = window.open("about:blank", "forumPopup", "height=" + wndHeight + ",width=" + wndWidth + ",top=" + l + ",left=" + t +",menubar=yes,scrollbars=yes,resizable");
}

function openNewRsWindow(wndWidth, wndHeight){
	window.name = "opener" + new Date().getTime();
	if (wndWidth == null || wndWidth == 0 || wndHeight == null || wndHeight == 0) {
		wndWidth = window.screen.availWidth/2;
		wndHeight = window.screen.availHeight/2;
	}
	var t = ((screen.width)-wndWidth)/2;
	var l = ((screen.height)-wndHeight)/2;

	popupPointer = window.open("about:blank", "forumPopup", "height=" + wndHeight + ",width=" + wndWidth + ",top=" + l + ",left=" + t +",menubar=no,scrollbars=yes,resizable");
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

function containsValidNumericValueZeroIncluded(objName) {
	if (chkNumeric(objName) == true) {
			return true;
	} else {
		return false;
	}
}

function chkNumeric(objName, groupSeparator, decimalSeparator)
{
	if ( groupSeparator == null )
		groupSeparator = " ";
	if ( decimalSeparator == null )
		decimalSeparator = ".";
	var checkOK = "0123456789" + groupSeparator + decimalSeparator;
	var checkStr = objName;
	var allValid = true;
	var decPoints = 0;
	var periodFlag = 0;
	var allNum = "";

	for (i = 0;  i < checkStr.value.length;  i++) {
		ch = checkStr.value.charAt(i);
		if (ch == decimalSeparator) {
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


function checkAmount(amt)
{
	var len = amt.length;
	var valid = false;
	var cnt=0;
	for (i = 0;i < amt.length;i++)
	{
		if(amt.charCodeAt(i) >=48 && amt.charCodeAt(i)<=57 || amt.charCodeAt(i)<=46 || amt.charCodeAt(i)<=44)
		{
			valid = true;
			if(amt.charCodeAt(i) == 46)
			{
				cnt = cnt + 1;
				if(cnt > 1)
				{
					valid = false;
					break;
				}
			}
		}
		else
		{
			valid = false;
			break;
		}
	}
	return valid;
}

function checkAmountLen(amt,msgConfFunding,groupSymbol,decimalSymbol)
{
	var amtIntPart = amt.split(decimalSymbol)[0]
	var amtInt = amt.replace(groupSymbol, "");

	if (amtInt.length >= 6 && keepChecking){
		valid = confirm(msgConfFunding);
		if(valid) keepChecking = false;
	}
	else {
		valid = true
	}
	return valid;
	
}

function checkAmountUsingSymbols(amount,groupSymbol,decimalSymbol){
		var validChars= "0123456789"+groupSymbol+decimalSymbol;
		
		for (i = 0;  i < amount.length;  i++) {
			var ch = amount.charAt(i);
			if (validChars.indexOf(ch)==-1){
				return false;
				break
			}
		}
		return true;
	}
