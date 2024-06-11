 function unload() {
 }


 if (!Array.prototype.indexOf) {
     Array.prototype.indexOf = function (searchElement /*, fromIndex */ ) {
         "use strict";
         if (this == null) {
             throw new TypeError();
         }
         var t = Object(this);
         var len = t.length >>> 0;
         if (len === 0) {
             return -1;
         }
         var n = 0;
         if (arguments.length > 1) {
             n = Number(arguments[1]);
             if (n != n) { // shortcut for verifying if it's NaN
                 n = 0;
             } else if (n != 0 && n != Infinity && n != -Infinity) {
                 n = (n > 0 || -1) * Math.floor(Math.abs(n));
             }
         }
         if (n >= len) {
             return -1;
         }
         var k = n >= 0 ? n : Math.max(len - Math.abs(n), 0);
         for (; k < len; k++) {
             if (k in t && t[k] === searchElement) {
                 return k;
             }
         }
         return -1;
     };
 }

 function getSelectedValue(selectId)
 {
 	var elem = document.getElementById(selectId);
 	return elem.options[elem.options.selectedIndex].value;
 }

 function setSelectedValue(selectId, value)
 {
 	var elem = document.getElementById(selectId);
 	for(var i = 0; i < elem.length; i++)
 	{
 		if (elem.options[i].value == value)
 		{
 			elem.options.selectedIndex = i;
 			return;
 		}
 	}
 	// nothing found, we can either alert() an error or set the first value. we'll do both
 	alert('unexistant value ' + value + ' in selectId ' + selectId);
 	elem.options.selectedIndex = 0;
 }

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
     caller = document.getElementById(callerId);
     inputs = caller.getElementsByTagName('input');
     for (i = 1; i < inputs.length; i++) {
		 // This code is executed only when caller has children
         if (inputs[i].type == "checkbox") {
             if (inputs[i].id.indexOf("moduleEdit") == 0 ||
				 inputs[i].id.indexOf("featureEdit") == 0 ||
				 inputs[i].id.indexOf("fieldEdit") == 0 ||
				 inputs[i].id.indexOf("moduleMandatory") == 0 ||
				 inputs[i].id.indexOf("featureMandatory") == 0 ||
				 inputs[i].id.indexOf("fieldMandatory") == 0) continue;

			 inputs[i].checked = inputs[0].checked;
         }
     }

     if (inputs[0].checked == true) {
         toggleParentVisibility(caller);
     } else {
		 if (checkAllChildrenAreUnchecked(caller)) toggleParentVisibility(caller, false);
	 }
 }

 function toggleParentVisibility(item, visibility = true) {
     hasParentCheckbox = true;
     if (item.parentElement.getAttribute('name') == 'dhtmltreeArray') {
         parent = item.parentElement.parentElement.parentElement;
         hasParentCheckbox = false;
     } else {
         parent = item.parentElement.parentElement;
     }

     if (parent) {
         if (parent.getElementsByTagName('input')) {
             var checkboxItem = parent.getElementsByTagName('input')[0];
             checkboxItem.checked = visibility;
         }

         if (parent && hasParentCheckbox) {
             if (visibility) toggleParentVisibility(parent, visibility);
			 else {
				 if (checkAllChildrenAreUnchecked(parent)) toggleParentVisibility(parent, visibility);
			 }
         }
 	}
 }

 // This function is used to know if all children are unchecked
 function checkAllChildrenAreUnchecked(item) {
	 if (item.parentElement.getAttribute('name') == 'dhtmltreeArray') {
		 parent = item.parentElement.parentElement.parentElement;
	 } else {
		 parent = item.parentElement.parentElement;
	 }

	 if (parent) {
		 inputs = parent.getElementsByTagName('input');
		 for (i = 1; i < inputs.length; i++) {
			 if (inputs[i].type == "checkbox") {
				 if (inputs[i].checked == true) {
					 return false;
				 }
			 }
		 }
		 return true;
	 } else return false;
 }

 function toggleParentVisibilityOfTheField(itemId) {
     var item = document.getElementById(itemId);
     toggleParentVisibility(item.parentElement);
}


 function openURLinWindow(url, wndWidth, wndHeight, resizeable)
 {
 	if ('undefined' === typeof resizeable) {
 		resizeable = false;
 	}
 	window.name = "opener" + new Date().getTime();
 	if (wndWidth == null || wndWidth == 0 || wndHeight == null || wndHeight == 0) {
 		wndWidth = window.screen.availWidth/2;
 		wndHeight = window.screen.availHeight/2;
 	}
 	var t = ((screen.width)-wndWidth)/2;
 	var l = ((screen.height)-wndHeight)/2;
 	var wparams = "height=" + wndHeight + ",width=" + wndWidth + ",top=" + l + ",left=" + t +",menubar=no,scrollbars=yes";
 	if (resizeable)
 		wparams = wparams + ",resizable";
 	popupPointer = window.open(url, "forumPopup", wparams);
 	return popupPointer;
 }

 function openURLinResizableWindow(url,wndWidth, wndHeight) {
 	return openURLinWindow(url, wndWidth, wndHeight, true);
 }

 function openResisableWindow(wndWidth, wndHeight) {
 	window.name = "opener" + new Date().getTime();
 	if (wndWidth == null || wndWidth == 0 || wndHeight == null || wndHeight == 0) {
 		wndWidth = window.screen.availWidth/2;
 		wndHeight = window.screen.availHeight/2;
 	}
 	var t = ((screen.width)-wndWidth)/2;
 	var l = ((screen.height)-wndHeight)/2;
 	var res = window.open("about:blank", "forumPopup", "height=" + wndHeight + ",width=" + wndWidth + ",top=" + l + ",left=" + t +",toolbar=yes,menubar=yes,scrollbars=yes,resizable=yes,location=no, directories=no, status=no");
 	return res;
 }

 function openResizableWindowWithURL(wndWidth, wndHeight, url, form)
 {
 	var wnd = openResisableWindow(wndWidth, wndHeight);
 	form.action = url;
 	form.target = wnd.name;
 	form.submit();
 }

 function openNewWindowWithName(wndWidth, wndHeight, popupName)
 {
 	window.name = "opener" + new Date().getTime();
 	if (wndWidth == null || wndWidth == 0 || wndHeight == null || wndHeight == 0) {
 		wndWidth = window.screen.availWidth / 2;
 		wndHeight = window.screen.availHeight / 2;
 	}
 	var t = ((screen.width) - wndWidth) / 2;
 	var l = ((screen.height) - wndHeight) / 2;

 	popupPointer = window.open("about:blank", popupName, "height=" + wndHeight + ",width=" + wndWidth + ",top=" + l + ",left=" + t +",menubar=no,scrollbars=yes");
 	return popupPointer;
 }

 function openNewWindow(wndWidth, wndHeight)
 {
 	return openNewWindowWithName(wndWidth, wndHeight, "forumPopup");
 }



 function openNewWindowWithMenubar(wndWidth, wndHeight, menubar){
 	if ('undefined' === typeof menubar) {
 		menubar = true;
 	}
 	window.name = "opener" + new Date().getTime();
 	if (wndWidth == null || wndWidth == 0 || wndHeight == null || wndHeight == 0) {
 		wndWidth = window.screen.availWidth/2;
 		wndHeight = window.screen.availHeight/2;
 	}
 	var t = ((screen.width)-wndWidth)/2;
 	var l = ((screen.height)-wndHeight)/2;
 	var menubarTxt = menubar ? "yes" : "no";
 	popupPointer = window.open("about:blank", "forumPopup", "height=" + wndHeight + ",width=" + wndWidth + ",top=" + l + ",left=" + t +",menubar=" + menubarTxt + ",scrollbars=yes,resizable");
 	return popupPointer;
 }


 function openNewRsWindow(wndWidth, wndHeight)
 {
 	return openNewWindowWithMenubar(wndWidth, wndHeight, false);
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
 	//var decPoints = 0;
 	var periodFlag = 0;
 	//var allNum = "";

 	for (var i = 0;  i < checkStr.value.length;  i++) {
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
 /*---snippet rafy---*/
 function css_browser_selector(u) {
     var ua=u.toLowerCase(),is=function(t){
         return ua.indexOf(t)>-1;
         },g='gecko',w='webkit',s='safari',o='opera',m='mobile',h=document.documentElement,b=[(!(/opera|webtv/i.test(ua))&&/msie\s(\d)/.test(ua))?('ie ie'+RegExp.$1):is('firefox/2')?g+' ff2':is('firefox/3.5')?g+' ff3 ff3_5':is('firefox/3.6')?g+' ff3 ff3_6':is('firefox/3')?g+' ff3':is('gecko/')?g:is('opera')?o+(/version\/(\d+)/.test(ua)?' '+o+RegExp.$1:(/opera(\s|\/)(\d+)/.test(ua)?' '+o+RegExp.$2:'')):is('konqueror')?'konqueror':is('blackberry')?m+' blackberry':is('android')?m+' android':is('chrome')?w+' chrome':is('iron')?w+' iron':is('applewebkit/')?w+' '+s+(/version\/(\d+)/.test(ua)?' '+s+RegExp.$1:''):is('mozilla/')?g:'',is('j2me')?m+' j2me':is('iphone')?m+' iphone':is('ipod')?m+' ipod':is('ipad')?m+' ipad':is('mac')?'mac':is('darwin')?'mac':is('webtv')?'webtv':is('win')?'win'+(is('windows nt 6.0')?' vista':''):is('freebsd')?'freebsd':(is('x11')||is('linux'))?'linux':'','js'];
     c = b.join(' ');
     h.className += ' '+c;
     return c;
 }
 css_browser_selector(navigator.userAgent);

 function globalOpenPopup(options, url)
 {
 	if (options == null)
 		options = 'channelmode=no,directories=no,menubar=no,resizable=yes,status=no,toolbar=no,scrollbars=yes,location=yes';
 	var windowname = 'popup' + new Date().getTime();

 	var openedWindow = window.open('', windowname, 'channelmode=no,directories=no,menubar=no,resizable=yes,status=no,toolbar=no,scrollbars=yes,location=yes');
 	if (navigator.appName.indexOf('Microsoft Internet Explorer') > -1){ //Workaround to allow HTTP REFERER to be sent in IE (AMP-12638)
 		var referLink = document.createElement('a');
 		referLink.href = href;
 		referLink.target = windowname;
 		document.body.appendChild(referLink);
 		referLink.click();
 	}
 	else
 	{
 		openedWindow.location = url;
 	}
 }
