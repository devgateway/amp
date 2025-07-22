function setStripsTable(tableId, classOdd, classEven) {
	var tableElement = document.getElementById(tableId);
	if (tableElement) {
		rows = tableElement.getElementsByTagName('tr');
		for (var i = 0, n = rows.length; i < n; ++i) {
			if (i%2 == 0) {
				rows[i].className = classEven;
			} else {
				rows[i].className = classOdd;
			}
		}
		rows = null;
	}
}
function setHoveredTable(tableId, hasHeaders) {

	var tableElement = document.getElementById(tableId);
	if(tableElement){
    	var className = 'Hovered',
        pattern   = new RegExp('(^|\\s+)' + className + '(\\s+|$)'),
        rows      = tableElement.getElementsByTagName('tr');

		for(var i = 0, n = rows.length; i < n; ++i) {
			rows[i].onmouseover = function() {
				this.className += ' ' + className;
			};
			rows[i].onmouseout = function() {
				this.className = this.className.replace(pattern, ' ');

			};
		}
		rows = null;
	}
}


function setHoveredRow(rowId) {

	var rowElement = document.getElementById(rowId);
	if(rowElement){
    	var className = 'Hovered',
        pattern   = new RegExp('(^|\\s+)' + className + '(\\s+|$)'),
        cells      = rowElement.getElementsByTagName('td');

		for(var i = 0, n = cells.length; i < n; ++i) {
			cells[i].onmouseover = function() {
				this.className += ' ' + className;
			};
			cells[i].onmouseout = function() {
				this.className = this.className.replace(pattern, ' ');

			};
		}
		cells = null;
	}
}