var propertyViewContainer;
var srcXMLString;

function createPropertyView (node) {
	if (propertyViewContainer == null) { 
	alert ("No container") 
	} else {
		if (node != null) {
			var viewSrc = ""
			viewSrc += "<table class='propertyViewTable' border='1' width='100%' height='100%' cellpadding='0' cellspacing='0'>";
			viewSrc += "<tr class='propertyTitle'><td colspan='2' nowrap>&nbsp;" 


			

			if (node.parent != null) {
				type = getTypeByID(node.type);
				var iconSrc;
				if (type != null) {
					iconSrc = type.icon;
				} else {
					iconSrc = defaultIconSrc;
				}
				viewSrc += "<img src='" + iconSrc + "' align='absmiddle'>&nbsp;";
			} else {
				viewSrc += "<img src='" + rootIconSrc + "' align='absmiddle'>&nbsp;";
			}


			
			viewSrc += node.tagName + "</td></tr>";		

			if (node.attributes.length > 0) {
				for (arrtInd = 0;  
					 arrtInd < node.attributes.length; 
					 arrtInd ++) {
					 attr = node.attributes[arrtInd];
					 var attrName = trimString (attr.name);
					 if (attrName.length > 0) {
						 viewSrc += "<tr><td width='50%' class='propertyName' valign='top'>&nbsp;";
						 viewSrc += attrName;
						 viewSrc += "&nbsp;</td><td width='50%' class='propertyName'>&nbsp;";
						 viewSrc +=	attr.value;
						 viewSrc += "&nbsp;</td></tr>";
					 }
				}
			}

			val = trimString (node.value);
			if (val != null && val.length > 0) {
				viewSrc += "<tr><td colspan='2' class='valueFiled' nowrap>&nbsp;" + node.value + "</td></tr>";
			} else {
				viewSrc += "<tr><td colspan='2' class='noValueFiled' nowrap>&nbsp;</td></tr>";
			}
			
			//Display full XML source for root node
			if (node.parent == null) {
				viewSrc += "<tr><td colspan='2' class='valueFiled' nowrap>&nbsp;";
				viewSrc += "<textarea style='width:100%; height:250px'>" + srcXMLString + "</textarea>";
				viewSrc += "</td></tr>";
				viewSrc += "<tr><td colspan='2' class='valueFiled' nowrap>";
				viewSrc += "<input type='button' onclick='copyXMLToClipboard()' value='Copy to clipboard' class='propertyButton'>";
				viewSrc += "</td></tr>";
			}
			
			
			viewSrc += "<tr><td width='100%'  colspan='2' class='digiBgr' height='100%'>";
			viewSrc += "&nbsp;"
			viewSrc += "</td></tr>";
			viewSrc += "</table>";
			
			propertyViewContainer.innerHTML = viewSrc;
		}
	}
}

function trimString (str) {
	retVal = str;
	if (retVal != null && retVal.length > 0) {
		while (retVal.substring (0,1) == " " ||
				retVal.substring (0,1) == "\n" ||
				retVal.substring (0,1) == "\r" ||
				retVal.substring (0,1) == "\t") {
			retVal = retVal.substring (1, retVal.length);
		}
	}
	return retVal;
}

//Copy to clipboard
function copyXMLToClipboard() {
	clipboardData.setData ("Text", srcXMLString);
}