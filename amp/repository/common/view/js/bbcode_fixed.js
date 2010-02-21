// bbCode control by
// subBlue design
// www.subBlue.com

// Startup variables
var imageTag = false;
var theSelection = false;

// Check for Browser & Platform for PC & IE specific bits
// More details from: http://www.mozilla.org/docs/web-developer/sniffer/browser_type.html
var clientPC = navigator.userAgent.toLowerCase(); // Get client info
var clientVer = parseInt(navigator.appVersion); // Get browser version

var is_ie = ((clientPC.indexOf("msie") != -1) && (clientPC.indexOf("opera") == -1));
var is_nav  = ((clientPC.indexOf('mozilla')!=-1) && (clientPC.indexOf('spoofer')==-1)
                && (clientPC.indexOf('compatible') == -1) && (clientPC.indexOf('opera')==-1)
                && (clientPC.indexOf('webtv')==-1) && (clientPC.indexOf('hotjava')==-1));

var is_win   = ((clientPC.indexOf("win")!=-1) || (clientPC.indexOf("16bit") != -1));
var is_mac    = (clientPC.indexOf("mac")!=-1);
var curForm = null;
var editField = null;

// Helpline messages
b_help = "Bold text: [b]text[/b]  (alt+b)";
i_help = "Italic text: [i]text[/i]  (alt+i)";
u_help = "Underlined text: [u]text[/u]  (alt+u)";
q_help = "Quoted text: [quote]text[/quote]  (alt+q)";
c_help = "View text as code: [code]code[/code]  (alt+c)";
l_help = "Un-ordered list: [list]text[/list] (alt+l)";
o_help = "Ordered list: [list=]text[/list]  (alt+o)";
p_help = "Insert an Image: [img]http://image_url[/img]  (alt+p)";
w_help = "Link to another site: [url]http://url[/url] &#4304;&#4316; [url=http://url]URL text[/url]  (alt+w)";
a_help = "Close all open bbCode tags";
s_help = "Color [color=red]text[/color]  you can also use color=#FF0000";
f_help = "Size: [size=x-small]small text[/size]";

// Define the bbCode tags
bbcode = new Array();
bbtags = new Array('[b]','[/b]','[i]','[/i]','[u]','[/u]','[quote]','[/quote]','[code]','[/code]','[list]','[/list]','[list=]','[/list]','[img]','[/img]','[url]','[/url]','','','','');
imageTag = false;

function setForm(form) {
	curForm = form;
}

function setEditField(edField) {
	editField = edField;
}

// Shows the help messages in the helpline window
function helpline(help) {
//  caused an error!  
//	document.post.helpbox.value = eval(help + "_help");
}


// Replacement for arrayname.length property
function getarraysize(thearray) {
	for (i = 0; i < thearray.length; i++) {
		if ((thearray[i] == "undefined") || (thearray[i] == "") || (thearray[i] == null))
			return i;
		}
	return thearray.length;
}

// Replacement for arrayname.push(value) not implemented in IE until version 5.5
// Appends element to the array
function arraypush(thearray,value) {
	thearray[ getarraysize(thearray) ] = value;
}

// Replacement for arrayname.pop() not implemented in IE until version 5.5
// Removes and returns the last element of an array
function arraypop(thearray) {
	thearraysize = getarraysize(thearray);
	retval = thearray[thearraysize - 1];
	delete thearray[thearraysize - 1];
	return retval;
}


function checkForm() {

	formErrors = false;

	if (editField.value.length < 2) {
		formErrors = "&#4332;&#4308;&#4320;&#4312;&#4314;&#4312;&#4321; &#4306;&#4304;&#4306;&#4310;&#4304;&#4309;&#4316;&#4304;&#4315;&#4307;&#4308; &#4335;&#4308;&#4320; &#4323;&#4316;&#4307;&#4304; &#4307;&#4304;&#4332;&#4308;&#4320;&#4317;&#4311; &#4312;&#4321;";
	}

	if (formErrors) {
		alert(formErrors);
		return false;
	} else {
		bbstyle(-1);
		//formObj.preview.disabled = true;
		//formObj.submit.disabled = true;
		return true;
	}
}

function emoticon(text) {
	text = ' ' + text + ' ';
	if (editField.createTextRange && editField.caretPos) {
		var caretPos = editField.caretPos;
		caretPos.text = caretPos.text.charAt(caretPos.text.length - 1) == ' ' ? text + ' ' : text;
		editField.focus();
	} else {
	editField.value  += text;
	editField.focus();
	}
}

function bbfontstyle(bbopen, bbclose) {
	if ((clientVer >= 4) && is_ie && is_win) {
		theSelection = document.selection.createRange().text;
		if (!theSelection) {
			editField.value += bbopen + bbclose;
			editField.focus();
			return;
		}
		document.selection.createRange().text = bbopen + theSelection + bbclose;
		editField.focus();
		return;
	} else {
		editField.value += bbopen + bbclose;
		editField.focus();
		return;
	}
	storeCaret(editField);
}


function bbstyle(bbnumber) {

	donotinsert = false;
	theSelection = false;
	bblast = 0;

	if (bbnumber == -1) { // Close all open tags & default button names
		while (bbcode[0]) {
			butnumber = arraypop(bbcode) - 1;
			editField.value += bbtags[butnumber + 1];
			buttext = eval('curForm.addbbcode' + butnumber + '.value');
			eval('curForm.addbbcode' + butnumber + '.value ="' + buttext.substr(0,(buttext.length - 1)) + '"');
		}
		imageTag = false; // All tags are closed including image tags :D
		editField.focus();
		return;
	}

	if ((clientVer >= 4) && is_ie && is_win)
		theSelection = document.selection.createRange().text; // Get text selection

	if (theSelection) {
		// Add tags around selection
		document.selection.createRange().text = bbtags[bbnumber] + theSelection + bbtags[bbnumber+1];
		editField.focus();
		theSelection = '';
		return;
	}

	// Find last occurance of an open tag the same as the one just clicked
	for (i = 0; i < bbcode.length; i++) {
		if (bbcode[i] == bbnumber+1) {
			bblast = i;
			donotinsert = true;
		}
	}

	if (donotinsert) {		// Close all open tags up to the one just clicked & default button names
		while (bbcode[bblast]) {
				butnumber = arraypop(bbcode) - 1;
				editField.value += bbtags[butnumber + 1];
				buttext = eval('curForm.addbbcode' + butnumber + '.value');
				eval('curForm.addbbcode' + butnumber + '.value ="' + buttext.substr(0,(buttext.length - 1)) + '"');
				imageTag = false;
			}
			editField.focus();
			return;
	} else { // Open tags

		if (imageTag && (bbnumber != 14)) {		// Close image tag before adding another
			editField.value += bbtags[15];
			lastValue = arraypop(bbcode) - 1;	// Remove the close image tag from the list
			curForm.addbbcode14.value = "Img";	// Return button back to normal state
			imageTag = false;
		}

		// Open tag
		editField.value += bbtags[bbnumber];
		if ((bbnumber == 14) && (imageTag == false)) imageTag = 1; // Check to stop additional tags after an unclosed image tag
		arraypush(bbcode,bbnumber+1);
		eval('curForm.addbbcode'+bbnumber+'.value += "*"');
		editField.focus();
		return;
	}
	storeCaret(editField);
}

// Insert at Claret position. Code from
// http://www.faqts.com/knowledge_base/view.phtml/aid/1052/fid/130
function storeCaret(textEl) {
	if (textEl.createTextRange) textEl.caretPos = document.selection.createRange().duplicate();
}
