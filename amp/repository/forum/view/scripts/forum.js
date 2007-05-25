//Forum scripst

	function isAnyChecked(chkBoxCollection) {
		retVal = false;

		if (chkBoxCollection != null) {
			for (ind = 0; ind < chkBoxCollection.length; ind ++) {
				 if (chkBoxCollection[ind].checked) {
				 	retVal = true;
					break;
				 }
			}
		}
		
		return retVal;
	}


	function scrollToTop() {
		window.scrollTo(0, 0);
	}
	
	var popupPointer = null;

	function openNewWindow(wndWidth, wndHeight){
		if (wndWidth == null || 
			wndWidth == 0 || 
			wndHeight == null || 
			wndHeight == 0) {
			
			wndWidth = window.screen.availWidth/2;
			wndHeight = window.screen.availHeight/2;
		
		}
		popupPointer = window.open("about:blank", "forumPopup", "height=" + wndHeight + ",width=" + wndWidth + ",menubar=no");
	}
	
	function forumSubmit(form){
		disableButtons(form);
		form.submit();
	}
	
	function disableButtons(formPointer) {
		if (formPointer != null) {
			var elements = formPointer.elements;
			if (elements != null && elements.length > 0) {
				for (elementIndex = 0; 
					 elementIndex < elements.length; 
					 elementIndex ++ ) {
					 
					 if (elements[elementIndex].type.toLowerCase() == "button") {
					 	elements[elementIndex].disabled = true;
					 }
				}
			}
		}
		return true;
	}