var statusWndPointer = null;
function openStatusWnd (){
	var xPos = window.screen.availWidth/2-150;
	var yPos = window.screen.availHeight/2-50;

	var wndParams = "width=300, height=100, left=" + xPos + ", top=" + yPos;
        wndParams += ", directories=no, location=no, menubar=no, ";
		wndParams += "resizable=no, scrollbars=no, status=no, titlebar=no, toolbar=no";
	
	statusWndPointer = window.open ("about:blank", "someName", wndParams);
	prepareStatusWnd();
}

function prepareStatusWnd() {
	if (statusWndPointer != null &&
		statusWndPointer.document != null) {
		var statusWndContent = "";
		
		statusWndContent += "<html><head>";
		//Styles
		statusWndContent += "<style>";
		statusWndContent += 'table.progressContainer{font:14px "MS Sans Serif",Geneva,sans-serif;}';
		statusWndContent += "td.processed {font:1px;background-color:#DD0005;border:1px solid;border-top-color:white;border-left-color:white;border-bottom-color:Black;border-right-color:Black;}";
		statusWndContent += "td.notProcessed {font:1px;background-color:#EBEBEB;border:1px solid;border-top-color:black;border-left-style:none;border-bottom-color:white;border-right-color:white;}";		
		statusWndContent += "input.progressButton {background-color:#CACACA;border:1px solid;border-top-color:white;border-left-color:white;border-bottom-color:Black;border-right-color:Black;}";

		statusWndContent += "</style>"
		
		statusWndContent += "<title>Working</title></head>";
		statusWndContent += "<body bgcolor='#CACACA' leftmargin='0' topmargin='0' rightmargin='0' bottommargin='0'><html>";
		statusWndContent += "<table border='0' class='progressContainer' ";
		statusWndContent += "width='100%' height='100%'>";
		statusWndContent += "<tr><td id='text' align='center' nowrap>";
		statusWndContent += "</td></tr>";
		statusWndContent += "<tr><td id='progressCapt' align='center'>";
		statusWndContent += "&nbsp;";
		statusWndContent += "</td></tr>";		
		statusWndContent += "<tr><td>";
		
		statusWndContent += "<table border='0' cellpadding='0' cellspacing='0' width='100%'>";
		statusWndContent += "<tr id='progressRow'>";
		statusWndContent += "<td width='1%' class='processed' height='15'>";
		statusWndContent += "&nbsp;";
		statusWndContent += "</td>";		
		statusWndContent += "<td width='99%' class='notProcessed' height='15'>";
		statusWndContent += "&nbsp;";
		statusWndContent += "</td></tr>";		
		statusWndContent += "</table>";
		statusWndContent += "</td></tr>";
		statusWndContent += "<tr><td align='center'>";
		statusWndContent += "<input type='button' onclick='window.close()' value='Close' class='progressButton'>";
		statusWndContent += "</td></tr>";			
		statusWndContent += "</table></html></body>";
		
		statusWndPointer.document.write (statusWndContent);
	}
}

function setText (text){
	if (statusWndPointer != null && 
		!statusWndPointer.closed &&
		statusWndPointer.document != null && 
		statusWndPointer.document.getElementById("text") != null) {
			statusWndPointer.document.getElementById("text").innerHTML = text;
		}
}


function setProgressValue (prog) {
	if (statusWndPointer != null &&
		!statusWndPointer.closed &&
		statusWndPointer.document != null && 
		statusWndPointer.document.getElementById("progressRow") != null) {

			statusWndPointer.document.getElementById("progressCapt").innerHTML = prog + "%";
			statusWndPointer.document.getElementById("progressRow").cells[0].setAttribute("width", prog + "%", 0);
			statusWndPointer.document.getElementById("progressRow").cells[1].setAttribute("width", (100-prog) + "%", 0);
		}
}

function closeStatusWnd() {
	if (statusWndPointer != null && !statusWndPointer.closed) {
		statusWndPointer.close();
	}
}