/**
 * Copyright (c) 2010 Development Gateway (www.developmentgateway.org)
 *
 */

/**
 * @author aartimon@dginternational.org
 * since Oct 4, 2010
 */
function getAmpAjaxCallBackUrl(){
	return "${callBackUrl}";
}

onepagerMode = ${onepagerMode};

//////////////////////////////////////////////////////////////
//
// functions for trn label
//
//////////////////////////////////////////////////////////////

/**
 * The function will swap the label with an text box that submits through ajax 
 */
function spawnEditBox(labelId) {
	var myId = labelId;
	var editId = myId+".editor";
	var editor = document.getElementById(editId);
	var label = document.getElementById(myId);
	
	label.style.display = "none";
	if (editor == null){
		//if label was not edited before since the page has been loaded
		//spawn a new edit box
		editor = document.createElement('input');
		editor.setAttribute('id', editId);
		var keypress = 'var kc=wicketKeyCode(event); if (kc==27) showLabel("'+ myId +'"); else if (kc!=13) { return true; } else saveEditBox("'+ myId +'");';
		editor.setAttribute('onkeypress', 'if (Wicket.Browser.isSafari()) { return; }; ' + keypress);
		editor.setAttribute('onkeydown', 'if (!Wicket.Browser.isSafari()) { return; }; ' + keypress);
		editor.setAttribute('class', 'inputx');
		editor.setAttribute('key', label.getAttribute('key'));
		editor.setAttribute('style', 'width: 300px');
		
		//neutralize all href parent containers in order for this to work
		var container = label.parentNode;
		while (container != null) {
			if (container.nodeName.toLowerCase() == "a") {
				container.removeAttribute('href');
			}
			container = container.parentNode;
		}
		
		label.parentNode.insertBefore(editor, label);
	}
	else
		editor.style.display = "inline"; //show the old editor
	
	var labelValue = label.innerHTML;
	if (label.tagName == "INPUT")
		labelValue = label.value;

	editor.value = labelValue;
	//onblur is reset upon cancel or save must be set again
	editor.setAttribute('onblur', 'saveEditBox("'+ myId +'")');
	editor.focus();
	
}

function updateLabel(labelId, newValue) {
	var label = document.getElementById(labelId);
	
	if (label.tagName == "INPUT")
		label.value = newValue;
	else
		label.innerHTML = newValue;
}

/**
 * This acts as a cancel for the translation
 */
function showLabel(labelId) {
	var label = document.getElementById(labelId);
	var editor = document.getElementById(labelId+".editor");
	
	editor.setAttribute('onblur', '');
	label.style.display = "inline";
	editor.style.display = "none";
	label.focus();
}
/**
 * Submits the new value for the translation
 */
function saveEditBox(labelId){
	var label = document.getElementById(labelId);
	var editor = document.getElementById(labelId+".editor");
	
	var params = '&method=translate' + 
				 '&editorKey='+editor.getAttribute("key") + 
				 '&editorVal='+editor.value +
				 '&labelId='+labelId;
	
	var wcall = wicketAjaxGet(getAmpAjaxCallBackUrl() + params, null, null, null);
}

function wicketSwitchTranslationMode(){
	var params = '&method=switchtranslatormode';
	var wcall = wicketAjaxGet(getAmpAjaxCallBackUrl() + params, null, null, null);
}

function wicketSwitchFMMode(){
	var params = '&method=switchfmmode';
	var wcall = wicketAjaxGet(getAmpAjaxCallBackUrl() + params, null, null, null);
}

function showSection(itemId){
	if (onepagerMode){
		$('#' + itemId).parent().parent().siblings('div:first').show();
		$('html, body').animate({scrollTop: $('#' + itemId).offset().top}, 1200);
	}
	else{
		$('span[name=section]').hide();
		$('#'+itemId).parents('span[name=section]').show();
	}
}

function switchOnepagerMode(){
	if (onepagerMode){
		onepagerMode = false;
		$('span[name=section]').hide();
		$('span[name=section]').eq(0).show();
		$('#imgGroupMode').hide();
		$('#imgOnepagerMode').show();
	}
	else{
		onepagerMode = true;
		$('span[name=section]').show();
		$('#imgOnepagerMode').hide();
		$('#imgGroupMode').show();
	}
}

//////////////////////////////////////////////////////////////
//
// functions for button label
//
//////////////////////////////////////////////////////////////


