var callerGisObject;
var structuresData;
function gisPopup(caller, data){
	const param = "width=780, height=500, scrollbars=yes,modal=yes, resizable, status";
	callerGisObject = caller;
	const centerCoordinates = [7.1881, 21.0938]; // Actual coordinates for the center of Africa
	const queryString = "center=" + centerCoordinates.join(',');

	if (data) {
	    try {
	    	structuresData = JSON.parse(data);
	    } catch(e) {
	        console.log('invalid json string');
	    }
	}
	// Append the queryString to the URL
	let url = "/esrigis/mainmap.do?popup=true";
	if (queryString !== '') {
		url += '&' + queryString;
	}

	window.open(url, "", param);
}



function postvaluesx(element){
	element.focus();
	setTimeout(function(){element.select();}, 500);
	setTimeout(function(){element.blur();}, 500);
}
function postvaluesy(element){
	setTimeout(function(){element.focus();}, 3000);
	setTimeout(function(){element.select();}, 1000);
	setTimeout(function(){element.blur();}, 3000);
}

function viewCoordinates(dataString) {	
	var data = JSON.parse(dataString);
	var html = '';
	if(data.coordinates.length > 0) {
		html += '<div class="coordinates-container"><b class="ins_title">' +  data.selectedShape + ": </b>" + data.shape +"<br><br></div>";
		html += '<div class="coordinates-container"><table class="inside">';
		html += '<tr><td class="inside"><b class="ins_title">'+ data.latitudeColName +'</b></td><td class="inside"><b class="ins_title">' + data.longitudeColName + '</b></td></tr><tbody>';	
		data.coordinates.forEach(function(coordinate){
		   html += '<tr><td class="inside">'+ coordinate.latitude +'</td><td class="inside">' + coordinate.longitude + '</td></tr>'; 	
		});
		html += '</tbody></table></div>';
	} else {
		html += '<div>' + data.noData + '</div>';
	}	
    var $dialog = $('<div></div>').dialog({title: data.title});
	$dialog.dialog('open');
	$dialog.html(html);
}
