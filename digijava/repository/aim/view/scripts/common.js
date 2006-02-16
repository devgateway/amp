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
	
	if (!allValid) {	
		alertsay = "Please enter only these values \""
		alertsay = alertsay + checkOK + "\" in the \"" + checkStr.name + "\" field."
		alert(alertsay);
		return (false);
	}
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

function checkAmountLen(amt)
{
	var len = amt.length;
	var valid = false;
	var cnt = 0;
	for (var x = 0;x < amt.length;x++)
	{
		if(amt.charCodeAt(x) == 44)
		{
			len = len - 1;
		}
	}
	for (i = 0;i < amt.length; i++)
	{
		if(amt.charCodeAt(i) == 46)
		{
			if(i > 6)
			{
				cnt = i;
				for(j = 0;j < i; j++)
				{
					if(amt.charCodeAt(j) == 44)
						cnt = cnt - 1;
				}
				if(cnt > 6)
				{
					valid = confirm('All funding information should be entered in thousands "000". Do you wish to proceed with your entry?');
				}
				return valid;
			}
			else
				return true;
		}
	}
	if(len > 6)
	{
		valid = confirm('All funding information should be entered in thousands "000". Do you wish to proceed with your entry?');
		return valid;			
	}
}

function compareAmount( num1, num2)
{
	if( num1 == num2 )
		return true;
	else
	{
		if( num1.length >= num2.length )
			var len1 = num1.length;
		else
			var len2 = num2.length;
		for( var x=0,y=0; (x<len1 || y<len2) ; x++,y++ )
		{
			if( num1.charCodeAt(x) == num2.charCodeAt(y) )
				continue;
			else
			{
				if( num1.charCodeAt(x) == 44 )
				{
					x++;
					if( num1.charCodeAt(x) == num2.charCodeAt(y) )
						continue;
					else
						return false;
				}
				if( num1.charCodeAt(y) == 44 )
				{
					y++;
					if( num1.charCodeAt(x) == num2.charCodeAt(y) )
						continue;
					else
						return false;
				}
				if( num1.charCodeAt(x) == 46 )
				{
					x++;
					if( num1.charCodeAt(x) == 48 )
					{
						x++;
						if( num1.charCodeAt(x) == 48 || num1.charCodeAt(x) == 32)
							return true;
						else
							return false;
					}
					else
						return false;
				}
				if( num2.charCodeAt(y) == 46 )
				{
					y++;
					if( num2.charCodeAt(y) == 48 )
					{
						y++;
						if( num2.charCodeAt(y) == 48 || num2.charCodeAt(y) == 32)
							return true;
						else
							return false;
					}
					else
						return false;
				}
				return false;
			}
		}
	}
	return true;
}
