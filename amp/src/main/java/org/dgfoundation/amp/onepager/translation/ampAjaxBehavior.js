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

//////////////////////////////////////////////////////////////
//
// functions for trn label
//
//////////////////////////////////////////////////////////////

/**
 * The function will swap the label with an text box that submits through ajax 
 */
function spawnEditBox(labelId, javascript) {
	var myId = labelId;
	var editId = myId+"-editor";
	var editor = document.getElementById(editId);
	var label = document.getElementById(myId);
	
	label.style.display = "none";
	if (editor == null){
		//if label was not edited before since the page has been loaded
		//spawn a new edit box
		editor = document.createElement('input');
		editor.setAttribute('id', editId);
		var keypress = 'var kc=event.keyCode; if (kc==27) showLabel("'+ myId +'"); else if (kc!=13) { return true; } else saveEditBox("'+ myId+'","'+javascript+'");'; 
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
	//IE8 and earlier does not support trim
	if(typeof String.prototype.trim == 'function') {
		labelValue = labelValue.trim();
	}
	else {
		labelValue = labelValue.replace(/^\s+|\s+$/g, '');
	}
	if (label.tagName == "INPUT")
		labelValue = label.value;

	editor.value = labelValue;
	//set the position of the cursor
	if(editor.createTextRange) {
          var range = editor.createTextRange();
          range.move('character', 0);
          range.select();
      }
	  else {
          if(editor.selectionStart) {
        	  editor.focus();
        	  editor.setSelectionRange(0, 0);
          }
          else
        	  editor.focus();
      }
	//onblur is reset upon cancel or save must be set again
	editor.setAttribute('onblur', 'saveEditBox("'+ myId +'","'+javascript+'")');
	
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
	var editor = document.getElementById(labelId+"-editor");
	
	editor.setAttribute('onblur', '');
	label.style.display = "inline";
	editor.style.display = "none";
	label.focus();
}
/**
 * Submits the new value for the translation
 */
function saveEditBox(labelId,postProcessActions){
	var label = document.getElementById(labelId);
	var editor = document.getElementById(labelId+"-editor");
	
	var params = '&method=translate' + 
				 '&editorKey='+editor.getAttribute("key") + 
				 '&editorVal='+editor.value +
				 '&labelId='+labelId;
	
	//post save actions
	if (postProcessActions != "undefined" && postProcessActions!=null) {
		var tmpFunc = new Function(postProcessActions);
		tmpFunc();
		params += '&postSaveActions=true';
	}
	var wcall = Wicket.Ajax.get({"u":getAmpAjaxCallBackUrl() + params});
	
}

function wicketSwitchTranslationMode(){
	var path = window.location.pathname;
	var onepagerPath = "${onepagerPath}";
	var actId = path.substr(path.indexOf(onepagerPath) + onepagerPath.length);
	
	var params = '&method=switchtranslatormode&activity=' + actId;
	var wcall = Wicket.Ajax.get({"u":getAmpAjaxCallBackUrl() + params});
}

function wicketSwitchFMMode(){
	var path = window.location.pathname;
	var onepagerPath = "${onepagerPath}";
	var actId = path.substr(path.indexOf(onepagerPath) + onepagerPath.length);
	
	var params = '&method=switchfmmode&activity=' + actId;
	var wcall = Wicket.Ajax.get({"u":getAmpAjaxCallBackUrl() + params});
}

