/*
Copyright (c) 2007, Yahoo! Inc. All rights reserved.
Code licensed under the BSD License:
http://developer.yahoo.net/yui/license.txt
version: 2.2.0 
 */

YAHOO.widget.DataTable = function(elContainer, oColumnSet, oDataSource,
		oConfigs) {
	var i;
	this._nIndex = YAHOO.widget.DataTable._nCount;
	this._sName = "instance" + this._nIndex;
	this.id = "yui-dt" + this._nIndex;
	if (typeof oConfigs == "object") {
		for ( var sConfig in oConfigs) {
			this[sConfig] = oConfigs[sConfig];
		}
	}
	if (oDataSource) {
		if (oDataSource instanceof YAHOO.util.DataSource) {
			this.dataSource = oDataSource;
		} else {
		}
	}
	if (oColumnSet && (oColumnSet instanceof YAHOO.widget.ColumnSet)) {
		this._oColumnSet = oColumnSet;
	} else {
		return;
	}
	this._oRecordSet = new YAHOO.widget.RecordSet();
	elContainer = YAHOO.util.Dom.get(elContainer);
	if (elContainer && elContainer.tagName
			&& (elContainer.tagName.toLowerCase() == "div")) {
		this._elContainer = elContainer;
		var elTable = null;
		if (elContainer.hasChildNodes()) {
			var children = elContainer.childNodes;
			for (i = 0; i < children.length; i++) {
				if (children[i].tagName
						&& children[i].tagName.toLowerCase() == "table") {
					elTable = children[i];
					break;
				}
			}
		}
		if (elTable && !this.dataSource) {
			var aRecords = [];
			for (i = 0; i < elTable.tBodies.length; i++) {
				var elBody = elTable.tBodies[i];
				for ( var j = 0; j < elBody.rows.length; j++) {
					var elRow = elBody.rows[j];
					var oRecord = {};
					for ( var k = 0; k < elRow.cells.length; k++) {
						oRecord[oColumnSet.keys[k].key] = oColumnSet.keys[k]
								.parse(elRow.cells[k].innerHTML);
					}
					aRecords.push(oRecord);
				}
			}
			this._initTable();
			var ok = this.doBeforeLoadData(aRecords);
			if (ok) {
				this._oRecordSet.addRecords(aRecords);
				this.paginateRows();
			}
		} else if (this.dataSource) {
			this._initTable();
			oDataSource.sendRequest(this.initialRequest,
					this.onDataReturnPaginateRows, this);
		} else {
			this._initTable();
			this.showEmptyMessage();
		}
	} else {
		return;
	}
	this.subscribe("headCellClickEvent", this.onEventSortColumn);
	if (this.contextMenu && this.contextMenuOptions) {
		this.contextMenu = new YAHOO.widget.ContextMenu(this.id + "-cm", {
			trigger :this._elBody.rows
		});
		this.contextMenu.addItem("delete item");
		this.contextMenu.render(document.body);
	}
	elTable = this._elTable;
	YAHOO.util.Event.addListener(elTable, "click", this._onClick, this);
	YAHOO.util.Event
			.addListener(elTable, "dblclick", this._onDoubleclick, this);
	YAHOO.util.Event.addListener(elTable, "mouseout", this._onMouseout, this);
	YAHOO.util.Event.addListener(elTable, "mouseover", this._onMouseover, this);
	YAHOO.util.Event.addListener(elTable, "mousedown", this._onMousedown, this);
	YAHOO.util.Event.addListener(elTable, "keydown", this._onKeydown, this);
	YAHOO.util.Event.addListener(elTable, "keypress", this._onKeypress, this);
	YAHOO.util.Event
			.addListener(document, "keyup", this._onDocumentKeyup, this);
	YAHOO.util.Event.addListener(elTable, "keyup", this._onKeyup, this);
	YAHOO.util.Event.addListener(elTable, "blur", this._onBlur, this);
	this.createEvent("cellMouseoverEvent");
	this.createEvent("cellMouseoutEvent");
	this.createEvent("headCellMouseoverEvent");
	this.createEvent("headCellMouseoutEvent");
	this.createEvent("tableMouseoverEvent");
	this.createEvent("tableMouseoutEvent");
	this.createEvent("cellMousedownEvent");
	this.createEvent("headCellMousedownEvent");
	this.createEvent("tableMousedownEvent");
	this.checkboxClickEvent = this.createEvent("checkboxClickEvent");
	this.createEvent("radioClickEvent");
	this.createEvent("cellClickEvent");
	this.createEvent("headCellClickEvent");
	this.createEvent("tableClickEvent");
	this.createEvent("cellDoubleclickEvent");
	this.createEvent("headCellDoubleclickEvent");
	this.createEvent("tableDoubleclickEvent");
	this.createEvent("columnSortEvent");
	this.createEvent("editorShowEvent");
	this.createEvent("cellEditEvent");
	this.createEvent("columnResizeEvent");
	this.createEvent("tableInitEvent");
	this.createEvent("tableFocusEvent");
	this.createEvent("dataReturnEvent");
	this.createEvent("paginateEvent");
	this.createEvent("cellFormatEvent");
	this.createEvent("selectEvent");
	this.createEvent("unselectEvent");
	this.createEvent("highlightEvent");
	this.createEvent("unhighlightEvent");
	this.createEvent("rowDeleteEvent");
	this.subscribe("rowDeleteEvent", this._onRowDelete);
	this.createEvent("rowAppendEvent");
	this.createEvent("recordSetUpdateEvent");
	this._oRecordSet.subscribe("recordUpdateEvent", this._onRecordUpdate, this,
			true);
	YAHOO.widget.DataTable._nCount++;
	this.fireEvent("tableInitEvent");
};
if (YAHOO.util.EventProvider) {
	YAHOO.augment(YAHOO.widget.DataTable, YAHOO.util.EventProvider);
} else {
}
YAHOO.widget.DataTable.CLASS_BODY = "yui-dt-body";
YAHOO.widget.DataTable.CLASS_HEADCONTAINER = "yui-dt-headcontainer";
YAHOO.widget.DataTable.CLASS_HEADRESIZER = "yui-dt-headresizer";
YAHOO.widget.DataTable.CLASS_HEADTEXT = "yui-dt-headtext";
YAHOO.widget.DataTable.CLASS_EVEN = "yui-dt-even";
YAHOO.widget.DataTable.CLASS_ODD = "yui-dt-odd";
YAHOO.widget.DataTable.CLASS_EMPTY = "yui-dt-empty";
YAHOO.widget.DataTable.CLASS_LOADING = "yui-dt-loading";
YAHOO.widget.DataTable.CLASS_SELECTED = "yui-dt-selected";
YAHOO.widget.DataTable.CLASS_HIGHLIGHT = "yui-dt-highlight";
YAHOO.widget.DataTable.CLASS_SCROLLABLE = "yui-dt-scrollable";
YAHOO.widget.DataTable.CLASS_SORTABLE = "yui-dt-sortable";
YAHOO.widget.DataTable.CLASS_SORTEDBYASC = "yui-dt-sortedbyasc";
YAHOO.widget.DataTable.CLASS_SORTEDBYDESC = "yui-dt-sortedbydesc";
YAHOO.widget.DataTable.CLASS_FIRSTLINK = "yui-dt-firstlink";
YAHOO.widget.DataTable.CLASS_FIRSTPAGE = "yui-dt-firstpage";
YAHOO.widget.DataTable.CLASS_LASTLINK = "yui-dt-lastlink";
YAHOO.widget.DataTable.CLASS_LASTPAGE = "yui-dt-lastpage";
YAHOO.widget.DataTable.CLASS_PREVLINK = "yui-dt-prevlink";
YAHOO.widget.DataTable.CLASS_PREVPAGE = "yui-dt-prevpage";
YAHOO.widget.DataTable.CLASS_NEXTLINK = "yui-dt-nextlink";
YAHOO.widget.DataTable.CLASS_NEXTPAGE = "yui-dt-nextpage";
YAHOO.widget.DataTable.CLASS_PAGELINK = "yui-dt-pagelink";
YAHOO.widget.DataTable.CLASS_CURRENTPAGE = "yui-dt-currentpage";
YAHOO.widget.DataTable.CLASS_PAGESELECT = "yui-dt-pageselect";
YAHOO.widget.DataTable.CLASS_PAGELINKS = "yui-dt-pagelinks";
YAHOO.widget.DataTable.CLASS_EDITABLE = "yui-dt-editable";
YAHOO.widget.DataTable.CLASS_CHECKBOX = "yui-dt-checkbox";
YAHOO.widget.DataTable.CLASS_CURRENCY = "yui-dt-currency";
YAHOO.widget.DataTable.CLASS_DATE = "yui-dt-date";
YAHOO.widget.DataTable.CLASS_EMAIL = "yui-dt-email";
YAHOO.widget.DataTable.CLASS_LINK = "yui-dt-link";
YAHOO.widget.DataTable.CLASS_NUMBER = "yui-dt-number";
YAHOO.widget.DataTable.CLASS_STRING = "yui-dt-string";
YAHOO.widget.DataTable.MSG_EMPTY = "No records found.";
YAHOO.widget.DataTable.MSG_LOADING = "Loading...";
YAHOO.widget.DataTable._nCount = 0;
YAHOO.widget.DataTable.prototype._nIndex = null;
YAHOO.widget.DataTable.prototype._sName = null;
YAHOO.widget.DataTable.prototype._elContainer = null;
YAHOO.widget.DataTable.prototype._elTable = null;
YAHOO.widget.DataTable.prototype._elBody = null;
YAHOO.widget.DataTable.prototype._oColumnSet = null;
YAHOO.widget.DataTable.prototype._oRecordSet = null;
YAHOO.widget.DataTable.prototype._aSelectedRecords = null;
YAHOO.widget.DataTable.prototype._bFocused = false;
YAHOO.widget.DataTable.prototype._totalPages = null;
YAHOO.widget.DataTable.prototype._initTable = function() {
	this._elContainer.innerHTML = "";
	if (this.scrollable) {
		YAHOO.util.Dom.addClass(this._elContainer,
				YAHOO.widget.DataTable.CLASS_SCROLLABLE);
	}
	this._elTable = this._elContainer.appendChild(document
			.createElement("table"));
	var elTable = this._elTable;
	elTable.tabIndex = 0;
	if (this.summary) {
		elTable.summary = this.summary;
	}
	if (this.caption) {
		this._elCaption = elTable
				.appendChild(document.createElement("caption"));
		this._elCaption.innerHTML = this.caption;
	}
	this._initHead(elTable, this._oColumnSet);
	var elMsgBody = document.createElement("tbody");
	elMsgBody.tabIndex = -1;
	this._elMsgRow = elMsgBody.appendChild(document.createElement("tr"));
	var elMsgRow = this._elMsgRow;
	var elMsgCell = elMsgRow.appendChild(document.createElement("td"));
	elMsgCell.colSpan = this._oColumnSet.keys.length;
	this._elMsgCell = elMsgCell;
	this._elMsgBody = elTable.appendChild(elMsgBody);
	this.showLoadingMessage();
	this._elBody = elTable.appendChild(document.createElement("tbody"));
	this._elBody.tabIndex = -1;
	YAHOO.util.Dom.addClass(this._elBody, YAHOO.widget.DataTable.CLASS_BODY);
	if (this.scrollable) {
		YAHOO.util.Dom.addClass(this._elBody,
				YAHOO.widget.DataTable.CLASS_SCROLLABLE);
	}
};
YAHOO.widget.DataTable.prototype._initHead = function() {
	var i, oColumn;
	var elHead = document.createElement("thead");
	elHead.tabIndex = -1;
	var colTree = this._oColumnSet.tree;
	for (i = 0; i < colTree.length; i++) {
		var elHeadRow = elHead.appendChild(document.createElement("tr"));
		elHeadRow.id = this.id + "-hdrow" + i;
		for ( var j = 0; j < colTree[i].length; j++) {
			oColumn = colTree[i][j];
			var elHeadCell = elHeadRow
					.appendChild(document.createElement("th"));
			elHeadCell.id = oColumn.getId();
			this._initHeadCell(elHeadCell, oColumn, i, j);
		}
	}
	this._elHead = this._elTable.appendChild(elHead);
	for (i = 0; i < this._oColumnSet.keys.length - 1; i++) {
		oColumn = this._oColumnSet.keys[i];
		if (oColumn.resizeable && YAHOO.util.DD) {
			if (!this.fixedWidth || (this.fixedwidth && !oColumn.isLast)) {
				var elHeadContainer = (YAHOO.util.Dom.getElementsByClassName(
						YAHOO.widget.DataTable.CLASS_HEADCONTAINER, "div",
						YAHOO.util.Dom.get(oColumn.getId())))[0];
				var elHeadResizer = elHeadContainer.appendChild(document
						.createElement("span"));
				elHeadResizer.id = oColumn.getId() + "-resizer";
				YAHOO.util.Dom.addClass(elHeadResizer,
						YAHOO.widget.DataTable.CLASS_HEADRESIZER);
				oColumn.ddResizer = new YAHOO.util.WidthResizer(this, oColumn
						.getId(), elHeadResizer.id, elHeadResizer.id);
				var cancelClick = function(e) {
					YAHOO.util.Event.stopPropagation(e);
				};
				YAHOO.util.Event.addListener(elHeadResizer, "click",
						cancelClick);
			}
			if (this.fixedWidth) {
				elHeadContainer.style.overflow = "hidden";
				elHeadContent.style.overflow = "hidden";
			}
		}
	}
};
YAHOO.widget.DataTable.prototype._initHeadCell = function(elHeadCell, oColumn,
		row, col) {
	var index = this._nIndex;
	elHeadCell.columnIndex = col;
	if (oColumn.abbr) {
		elHeadCell.abbr = oColumn.abbr;
	}
	if (oColumn.width) {
		elHeadCell.style.width = oColumn.width;
	}
	if (oColumn.className) {
		YAHOO.util.Dom.addClass(elHeadCell, oColumn.className);
	}
	if (this.sortedBy && this.sortedBy.colKey) {
		if (this.sortedBy.colKey == oColumn.key) {
			var sortClass = (this.sortedBy.dir && (this.sortedBy.dir != "asc")) ? YAHOO.widget.DataTable.CLASS_SORTEDBYDESC
					: YAHOO.widget.DataTable.CLASS_SORTEDBYASC;
			YAHOO.util.Dom.addClass(elHeadCell, sortClass);
			this.sortedBy._id = elHeadCell.id;
		}
	}
	elHeadCell.innerHTML = "";
	var elHeadContainer = elHeadCell.appendChild(document.createElement("div"));
	elHeadContainer.id = this.id + "-hdrow" + row + "-container" + col;
	YAHOO.util.Dom.addClass(elHeadContainer,
			YAHOO.widget.DataTable.CLASS_HEADCONTAINER);
	var elHeadContent = elHeadContainer.appendChild(document
			.createElement("span"));
	elHeadContent.id = this.id + "-hdrow" + row + "-text" + col;
	YAHOO.util.Dom.addClass(elHeadContent,
			YAHOO.widget.DataTable.CLASS_HEADTEXT);
	elHeadCell.rowSpan = oColumn.getRowSpan();
	elHeadCell.colSpan = oColumn.getColSpan();
	var contentText = oColumn.text || oColumn.key || "";
	if (oColumn.sortable) {
		YAHOO.util.Dom.addClass(elHeadContent,
				YAHOO.widget.DataTable.CLASS_SORTABLE);
		elHeadContent.innerHTML = "<a href=\"#\" title=\"Click to sort\">"
				+ contentText + "</a>";
	} else {
		elHeadContent.innerHTML = contentText;
	}
};
YAHOO.widget.DataTable.prototype._addRow = function(oRecord, index) {
	this.hideTableMessages();
	var insert = (isNaN(index)) ? false : true;
	if (!insert) {
		index = this._elBody.rows.length;
	}
	var oColumnSet = this._oColumnSet;
	var oRecordSet = this._oRecordSet;
	var elRow = (insert && this._elBody.rows[index]) ? this._elBody
			.insertBefore(document.createElement("tr"),
					this._elBody.rows[index]) : this._elBody
			.appendChild(document.createElement("tr"));
	var recId = oRecord.id;
	elRow.id = this.id + "-bdrow" + index;
	elRow.recordId = recId;
	for ( var j = 0; j < oColumnSet.keys.length; j++) {
		var oColumn = oColumnSet.keys[j];
		var elCell = elRow.appendChild(document.createElement("td"));
		elCell.id = this.id + "-bdrow" + index + "-cell" + j;
		elCell.headers = oColumn.id;
		elCell.columnIndex = j;
		elCell.headers = oColumnSet.headers[j];
		oColumn.format(elCell, oRecord);
		if (this.fixedWidth) {
			elCell.style.overflow = "hidden";
		}
	}
	if (this.isEmpty && (this._elBody.rows.length > 0)) {
	}
	if (!insert) {
		if (index % 2) {
			YAHOO.util.Dom.addClass(elRow, YAHOO.widget.DataTable.CLASS_ODD);
		} else {
			YAHOO.util.Dom.addClass(elRow, YAHOO.widget.DataTable.CLASS_EVEN);
		}
	} else {
		this._restripeRows();
	}
	return elRow.id;
};
YAHOO.widget.DataTable.prototype._restripeRows = function(range) {
	if (!range) {
		var rows = this._elBody.rows;
		for ( var i = 0; i < rows.length; i++) {
			if (i % 2) {
				YAHOO.util.Dom.removeClass(rows[i],
						YAHOO.widget.DataTable.CLASS_ODD);
				YAHOO.util.Dom.addClass(rows[i],
						YAHOO.widget.DataTable.CLASS_EVEN);
			} else {
				YAHOO.util.Dom.removeClass(rows[i],
						YAHOO.widget.DataTable.CLASS_EVEN);
				YAHOO.util.Dom.addClass(rows[i],
						YAHOO.widget.DataTable.CLASS_ODD);
			}
		}
	} else {
	}
};
YAHOO.widget.DataTable.prototype._updateRow = function(oRecord, index) {
	this.hideTableMessages();
	var elRow = this._elBody.rows[index];
	elRow.recordId = oRecord.id;
	var columns = this._oColumnSet.keys;
	for ( var j = 0; j < columns.length; j++) {
		columns[j].format(elRow.cells[j], oRecord);
	}
	return elRow.id;
};
YAHOO.widget.DataTable.prototype._select = function(els) {
	for ( var i = 0; i < els.length; i++) {
		YAHOO.util.Dom.addClass(YAHOO.util.Dom.get(els[i]),
				YAHOO.widget.DataTable.CLASS_SELECTED);
	}
	this._lastSelected = els[els.length - 1];
};
YAHOO.widget.DataTable.prototype._unselect = function(els) {
	for ( var i = 0; i < els.length; i++) {
		YAHOO.util.Dom.removeClass(YAHOO.util.Dom.get(els[i]),
				YAHOO.widget.DataTable.CLASS_SELECTED);
	}
};
YAHOO.widget.DataTable.prototype._unselectAllRows = function() {
	var selectedRows = YAHOO.util.Dom.getElementsByClassName(
			YAHOO.widget.DataTable.CLASS_SELECTED, "tr", this._elBody);
	this._unselect(selectedRows);
};
YAHOO.widget.DataTable.prototype._unselectAllCells = function() {
	var selectedCells = YAHOO.util.Dom.getElementsByClassName(
			YAHOO.widget.DataTable.CLASS_SELECTED, "td", this._elBody);
	this._unselect(selectedCells);
};
YAHOO.widget.DataTable.prototype._deleteRow = function(elRow) {
	var allRows = this._elBody.rows;
	var id = elRow.id;
	var recordId = elRow.recordId;
	for ( var i = 0; i < allRows.length; i++) {
		if (id == allRows[i].id) {
			this._elBody.deleteRow(i);
			this._oRecordSet.deleteRecord(i);
			break;
		}
	}
	if (this._elBody.rows.length === 0) {
		this.showEmptyMessage();
	}
};
YAHOO.widget.DataTable.prototype._onBlur = function(e, oSelf) {
	this._bFocused = false;
};
YAHOO.widget.DataTable.prototype._onMouseover = function(e, oSelf) {
	var elTarget = YAHOO.util.Event.getTarget(e);
	var elTag = elTarget.tagName.toLowerCase();
	var knownTag = false;
	if (elTag != "table") {
		while (!knownTag) {
			switch (elTag) {
			case "body":
				knownTag = true;
				break;
			case "td":
				oSelf.fireEvent("cellMouseoverEvent", {
					target :elTarget,
					event :e
				});
				knownTag = true;
				break;
			case "th":
				oSelf.fireEvent("headCellMouseoverEvent", {
					target :elTarget,
					event :e
				});
				knownTag = true;
				break;
			default:
				break;
			}
			elTarget = elTarget.parentNode;
			if (elTarget) {
				elTag = elTarget.tagName.toLowerCase();
			} else {
				break;
			}
		}
	}
	oSelf.fireEvent("tableMouseoverEvent", {
		target :elTarget,
		event :e
	});
};
YAHOO.widget.DataTable.prototype._onMouseout = function(e, oSelf) {
	var elTarget = YAHOO.util.Event.getTarget(e);
	var elTag = elTarget.tagName.toLowerCase();
	var knownTag = false;
	if (elTag != "table") {
		while (!knownTag) {
			switch (elTag) {
			case "body":
				knownTag = true;
				break;
			case "td":
				oSelf.fireEvent("cellMouseoutEvent", {
					target :elTarget,
					event :e
				});
				knownTag = true;
				break;
			case "th":
				oSelf.fireEvent("headCellMouseoutEvent", {
					target :elTarget,
					event :e
				});
				knownTag = true;
				break;
			default:
				break;
			}
			elTarget = elTarget.parentNode;
			if (elTarget) {
				elTag = elTarget.tagName.toLowerCase();
			} else {
				break;
			}
		}
	}
	oSelf.fireEvent("tableMouseoutEvent", {
		target :elTarget,
		event :e
	});
};
YAHOO.widget.DataTable.prototype._onMousedown = function(e, oSelf) {
	var elTarget = YAHOO.util.Event.getTarget(e);
	var elTag = elTarget.tagName.toLowerCase();
	var knownTag = false;
	if (elTag != "table") {
		while (!knownTag) {
			switch (elTag) {
			case "body":
				knownTag = true;
				break;
			case "td":
				YAHOO.util.Event.stopEvent(e);
				oSelf.fireEvent("cellMousedownEvent", {
					target :elTarget,
					event :e
				});
				knownTag = true;
				break;
			case "th":
				YAHOO.util.Event.stopEvent(e);
				oSelf.fireEvent("headCellMousedownEvent", {
					target :elTarget,
					event :e
				});
				knownTag = true;
				break;
			default:
				break;
			}
			elTarget = elTarget.parentNode;
			if (elTarget) {
				elTag = elTarget.tagName.toLowerCase();
			} else {
				break;
			}
		}
	}
	oSelf.fireEvent("tableMousedownEvent", {
		target :elTarget,
		event :e
	});
};
YAHOO.widget.DataTable.prototype._onClick = function(e, oSelf) {
	var elTarget = YAHOO.util.Event.getTarget(e);
	var elTag = elTarget.tagName.toLowerCase();
	var knownTag = false;
	if (oSelf.activeEditor) {
		oSelf.activeEditor.hide();
		oSelf.activeEditor = null;
		oSelf._bFocused = false;
		oSelf.focusTable();
	}
	if (elTag != "table") {
		while (!knownTag) {
			switch (elTag) {
			case "body":
				knownTag = true;
				break;
			case "input":
				if (elTarget.type.toLowerCase() == "checkbox") {
					oSelf.fireEvent("checkboxClickEvent", {
						target :elTarget,
						event :e
					});
				} else if (elTarget.type.toLowerCase() == "radio") {
					oSelf.fireEvent("radioClickEvent", {
						target :elTarget,
						event :e
					});
				}
				knownTag = true;
				break;
			case "td":
				YAHOO.util.Event.stopEvent(e);
				oSelf.fireEvent("cellClickEvent", {
					target :elTarget,
					event :e
				});
				knownTag = true;
				break;
			case "th":
				YAHOO.util.Event.stopEvent(e);
				oSelf.fireEvent("headCellClickEvent", {
					target :elTarget,
					event :e
				});
				knownTag = true;
				break;
			default:
				break;
			}
			elTarget = elTarget.parentNode;
			elTag = elTarget.tagName.toLowerCase();
		}
	}
	oSelf.focusTable();
	oSelf.fireEvent("tableClickEvent", {
		target :elTarget,
		event :e
	});
};
YAHOO.widget.DataTable.prototype._onDoubleclick = function(e, oSelf) {
	var elTarget = YAHOO.util.Event.getTarget(e);
	var elTag = elTarget.tagName.toLowerCase();
	var knownTag = false;
	if (oSelf.activeEditor) {
		oSelf.activeEditor.hide();
		oSelf.activeEditor = null;
		oSelf._bFocused = false;
		oSelf.focusTable();
	}
	if (elTag != "table") {
		while (!knownTag) {
			switch (elTag) {
			case "body":
				knownTag = true;
				break;
			case "td":
				YAHOO.util.Event.stopEvent(e);
				oSelf.fireEvent("cellDoubleclickEvent", {
					target :elTarget,
					event :e
				});
				knownTag = true;
				break;
			case "th":
				YAHOO.util.Event.stopEvent(e);
				oSelf.fireEvent("headCellDoubleclickEvent", {
					target :elTarget,
					event :e
				});
				knownTag = true;
				break;
			default:
				break;
			}
			elTarget = elTarget.parentNode;
			elTag = elTarget.tagName.toLowerCase();
		}
	}
	oSelf.fireEvent("tableDoubleclickEvent", {
		target :elTarget,
		event :e
	});
};
YAHOO.widget.DataTable.prototype._onKeypress = function(e, oSelf) {
	var isMac = (navigator.userAgent.toLowerCase().indexOf("mac") != -1);
	if (isMac) {
		if (e.keyCode == 40) {
			YAHOO.util.Event.stopEvent(e);
		} else if (e.keyCode == 38) {
			YAHOO.util.Event.stopEvent(e);
		}
	}
};
YAHOO.widget.DataTable.prototype._onKeydown = function(e, oSelf) {
	var oldSelected = oSelf._lastSelected;
	if (oldSelected && oSelf.isSelected(oldSelected)) {
		var newSelected;
		if (e.keyCode == 40) {
			YAHOO.util.Event.stopEvent(e);
			if (oldSelected.tagName.toLowerCase() == "tr") {
				if (oldSelected.sectionRowIndex + 1 < oSelf._elBody.rows.length) {
					if (!e.shiftKey) {
						oSelf.unselectAllRows();
					}
					newSelected = oSelf._elBody.rows[oldSelected.sectionRowIndex + 1];
					oSelf.select(newSelected);
				}
			} else if (oldSelected.tagName.toLowerCase() == "td") {
			}
			oSelf._bFocused = false;
			oSelf.focusTable();
		} else if (e.keyCode == 38) {
			YAHOO.util.Event.stopEvent(e);
			if (oldSelected.tagName.toLowerCase() == "tr") {
				if ((oldSelected.sectionRowIndex > 0)) {
					if (!e.shiftKey) {
						oSelf.unselectAllRows();
					}
					newSelected = oSelf._elBody.rows[oldSelected.sectionRowIndex - 1];
					oSelf.select(newSelected);
				}
			} else if (oldSelected.tagName.toLowerCase() == "td") {
				if ((oldSelected.sectionRowIndex > 0)) {
					if (!e.shiftKey) {
						oSelf.unselectAllRows();
					}
					newSelected = oSelf._elBody.rows[oldSelected.sectionRowIndex - 1];
					oSelf.select(newSelected);
				}
			}
			oSelf._bFocused = false;
			oSelf.focusTable();
		}
	}
};
YAHOO.widget.DataTable.prototype._onKeyup = function(e, oSelf) {
	var key = YAHOO.util.Event.getCharCode(e);
	if (key == 46) {
	}
};
YAHOO.widget.DataTable.prototype._onDocumentKeyup = function(e, oSelf) {
	if ((e.keyCode == 27) && (oSelf.activeEditor)) {
		oSelf.activeEditor.hide();
		oSelf.activeEditor = null;
		oSelf._bFocused = false;
		oSelf.focusTable();
	}
	if ((e.keyCode == 13) && (oSelf.activeEditor)) {
		var elCell = oSelf.activeEditor.cell;
		var oColumn = oSelf.activeEditor.column;
		var oRecord = oSelf.activeEditor.record;
		var oldValue = oRecord[oColumn.key];
		var newValue = oSelf.activeEditor.getValue();
		oSelf._oRecordSet.updateRecord(oRecord, oColumn.key, newValue);
		oSelf.formatCell(elCell);
		oSelf.activeEditor.hide();
		oSelf.activeEditor = null;
		oSelf._bFocused = false;
		oSelf.focusTable();
		oSelf.fireEvent("cellEditEvent", {
			target :elCell,
			oldData :oldValue,
			newData :newValue
		});
	}
};
YAHOO.widget.DataTable.prototype._onPagerClick = function(e, oSelf) {
	var elTarget = YAHOO.util.Event.getTarget(e);
	var elTag = elTarget.tagName.toLowerCase();
	var knownTag = false;
	if (elTag != "table") {
		while (!knownTag) {
			switch (elTag) {
			case "body":
				knownTag = true;
				break;
			case "a":
				YAHOO.util.Event.stopEvent(e);
				switch (elTarget.className) {
				case YAHOO.widget.DataTable.CLASS_PAGELINK:
					oSelf.showPage(parseInt(elTarget.innerHTML, 10));
					break;
				case YAHOO.widget.DataTable.CLASS_FIRSTLINK:
					oSelf.showPage(1);
					break;
				case YAHOO.widget.DataTable.CLASS_LASTLINK:
					oSelf.showPage(oSelf._totalPages);
					break;
				case YAHOO.widget.DataTable.CLASS_PREVLINK:
					oSelf.showPage(oSelf.pageCurrent - 1);
					break;
				case YAHOO.widget.DataTable.CLASS_NEXTLINK:
					oSelf.showPage(oSelf.pageCurrent + 1);
					break;
				}
				knownTag = true;
				break;
			default:
				break;
			}
			elTarget = elTarget.parentNode;
			if (elTarget) {
				elTag = elTarget.tagName.toLowerCase();
			} else {
				break;
			}
		}
	}
};
YAHOO.widget.DataTable.prototype._onPagerSelect = function(e, oSelf) {
	var elTarget = YAHOO.util.Event.getTarget(e);
	var elTag = elTarget.tagName.toLowerCase();
	var oldRowsPerPage = oSelf.rowsPerPage;
	var rowsPerPage = parseInt(elTarget[elTarget.selectedIndex].text, 10);
	if (rowsPerPage && (rowsPerPage != oSelf.rowsPerPage)) {
		if (rowsPerPage > oldRowsPerPage) {
			oSelf.pageCurrent = 1;
		}
		oSelf.rowsPerPage = rowsPerPage;
		oSelf.paginateRows();
	}
};
YAHOO.widget.DataTable.prototype._onRowDelete = function(oArgs) {
	this._restripeRows();
};
YAHOO.widget.DataTable.prototype._onRecordUpdate = function(oArgs) {
	this.fireEvent("recordSetUpdateEvent", oArgs);
};
YAHOO.widget.DataTable.prototype.dataSource = null;
YAHOO.widget.DataTable.prototype.initialRequest = "";
YAHOO.widget.DataTable.prototype.caption = null;
YAHOO.widget.DataTable.prototype.summary = null;
YAHOO.widget.DataTable.prototype.fixedWidth = false;
YAHOO.widget.DataTable.prototype.scrollable = false;
YAHOO.widget.DataTable.prototype.rowSingleSelect = false;
YAHOO.widget.DataTable.prototype.contextMenu = null;
YAHOO.widget.DataTable.prototype.pageCurrent = 1;
YAHOO.widget.DataTable.prototype.rowsPerPage = 500;
YAHOO.widget.DataTable.prototype.startRecordIndex = 1;
YAHOO.widget.DataTable.prototype.pageLinksLength = -1;
YAHOO.widget.DataTable.prototype.rowsPerPageDropdown = null;
YAHOO.widget.DataTable.prototype.pageLinksStart = 1;
YAHOO.widget.DataTable.prototype.pagers = null;
YAHOO.widget.DataTable.prototype.isEmpty = false;
YAHOO.widget.DataTable.prototype.sortedBy = null;
YAHOO.widget.DataTable.prototype.toString = function() {
	return "DataTable " + this._sName;
};
YAHOO.widget.DataTable.prototype.getTable = function() {
	return (this._elTable);
};
YAHOO.widget.DataTable.prototype.getHead = function() {
	return (this._elHead);
};
YAHOO.widget.DataTable.prototype.getBody = function() {
	return (this._elBody);
};
YAHOO.widget.DataTable.prototype.getRow = function(index) {
	return (this._elBody.rows[index]);
};
YAHOO.widget.DataTable.prototype.getCell = function(row, col) {
	return (this._elBody.rows[row].cells[col]);
};
YAHOO.widget.DataTable.prototype.showEmptyMessage = function() {
	if (this.isEmpty) {
		return;
	}
	if (this.isLoading) {
		this.hideTableMessages();
	}
	this._elMsgBody.style.display = "";
	var elCell = this._elMsgCell;
	elCell.className = YAHOO.widget.DataTable.CLASS_EMPTY;
	elCell.innerHTML = YAHOO.widget.DataTable.MSG_EMPTY;
	this.isEmpty = true;
};
YAHOO.widget.DataTable.prototype.showLoadingMessage = function() {
	if (this.isLoading) {
		return;
	}
	if (this.isEmpty) {
		this.hideTableMessages();
	}
	this._elMsgBody.style.display = "";
	var elCell = this._elMsgCell;
	elCell.className = YAHOO.widget.DataTable.CLASS_LOADING;
	elCell.innerHTML = YAHOO.widget.DataTable.MSG_LOADING;
	this.isLoading = true;
};
YAHOO.widget.DataTable.prototype.hideTableMessages = function() {
	if (!this.isEmpty && !this.isLoading) {
		return;
	}
	this._elMsgBody.style.display = "none";
	this.isEmpty = false;
	this.isLoading = false;
};
YAHOO.widget.DataTable.prototype.focusTable = function() {
	var elTable = this._elTable;
	if (!this._bFocused) {
		setTimeout( function() {
			elTable.focus();
		}, 0);
		this._bFocused = true;
		this.fireEvent("tableFocusEvent");
	}
};
YAHOO.widget.DataTable.prototype.doBeforeLoadData = function(sRequest,
		oResponse) {
	return true;
};
YAHOO.widget.DataTable.prototype.appendRows = function(aRecords) {
	if (aRecords && aRecords.length > 0) {
		this.hideTableMessages();
		var rowIds = [];
		for ( var i = 0; i < aRecords.length; i++) {
			var rowId = this._addRow(aRecords[i]);
			rowIds.push(rowId);
		}
		this.fireEvent("rowAppendEvent", {
			rowIds :rowIds
		});
	}
};
YAHOO.widget.DataTable.prototype.insertRows = function(aRecords) {
	if (aRecords && aRecords.length > 0) {
		this.hideTableMessages();
		var rowIds = [];
		for ( var i = 0; i < aRecords.length; i++) {
			var rowId = this._addRow(aRecords[i], 0);
			rowIds.push(rowId);
		}
		this.fireEvent("rowInsertEvent", {
			rowIds :rowIds
		});
	}
};
YAHOO.widget.DataTable.prototype.replaceRows = function(aRecords) {
	var i;
	if (aRecords && aRecords.length > 0) {
		this.hideTableMessages();
		var elBody = this._elBody;
		var elRows = this._elBody.rows;
		//alert(elRows.length);
		//alert(aRecords.length);
		while (elBody.hasChildNodes()) {
			elBody.deleteRow(0);
		}
		var selectedRecords = this.getSelectedRecordIds();
		if (selectedRecords.length > 0) {
			this._unselectAllRows();
		}
		var rowIds = [];
		for (i = 0; i < elRows.length; i++) {
			if (aRecords[i]) {
				var oRecord = aRecords[i];
				rowIds.push(this._updateRow(oRecord, i));
			}
		}
		for (i = elRows.length; i < aRecords.length; i++) {
			rowIds.push(this._addRow(aRecords[i]));
		}
		for (i = 0; i < selectedRecords.length; i++) {
			var allRows = elBody.rows;
			for ( var j = 0; j < allRows.length; j++) {
				if (selectedRecords[i] == allRows[j].recordId) {
					this._select( [ allRows[j] ]);
				}
			}
		}
		this.fireEvent("rowReplaceEvent", {
			rowIds :rowIds
		});
	}
};
YAHOO.widget.DataTable.prototype.addRow = function(oRecord, index) {
	if (oRecord) {
		var rowId = this._addRow(oRecord, index);
		if (index !== undefined) {
			this.fireEvent("rowInsertEvent", {
				rowIds : [ rowId ]
			});
		} else {
			this.fireEvent("rowAppendEvent", {
				rowIds : [ rowId ]
			});
		}
	}
};
YAHOO.widget.DataTable.prototype.updateRow = function(oRecord, index) {
	if (oRecord) {
		var rowId = this._updateRow(oRecord, index);
		this.fireEvent("rowUpdateEvent", {
			rowIds : [ rowId ]
		});
	}
};
YAHOO.widget.DataTable.prototype.deleteRows = function(rows) {
	var rowIndexes = [];
	for ( var i = 0; i < rows.length; i++) {
		var rowIndex = (rows[i].sectionRowIndex !== undefined) ? rows[i].sectionRowIndex
				: null;
		rowIndexes.push(rowIndex);
		this._deleteRow(rows[i]);
		this.fireEvent("rowDeleteEvent", {
			rowIndexes :rowIndexes
		});
	}
};
YAHOO.widget.DataTable.prototype.deleteRow = function(elRow) {
	if (elRow) {
		var rowIndex = (elRow.sectionRowIndex !== undefined) ? elRow.sectionRowIndex
				: null;
		this._deleteRow(elRow);
		this.fireEvent("rowDeleteEvent", {
			rowIndexes : [ rowIndex ]
		});
	}
};
YAHOO.widget.DataTable.prototype.highlight = function(els) {
	if (els.constructor != Array) {
		els = [ els ];
	}
	YAHOO.util.Dom.addClass(els, YAHOO.widget.DataTable.CLASS_HIGHLIGHT);
	this.fireEvent("highlightEvent", {
		els :els
	});
};
YAHOO.widget.DataTable.prototype.unhighlight = function(els) {
	if (els.constructor != Array) {
		els = [ els ];
	}
	YAHOO.util.Dom.removeClass(els, YAHOO.widget.DataTable.CLASS_HIGHLIGHT);
	this.fireEvent("unhighlightEvent", {
		els :els
	});
};
YAHOO.widget.DataTable.prototype.select = function(els) {
	if (els) {
		if (els.constructor != Array) {
			els = [ els ];
		}
		this._select(els);
		var tracker = this._aSelectedRecords || [];
		for ( var i = 0; i < els.length; i++) {
			var id = els[i].recordId;
			if (tracker.indexOf && (tracker.indexOf(id) > -1)) {
				tracker.splice(tracker.indexOf(id), 1);
			} else {
				for ( var j = 0; j < tracker.length; j++) {
					if (tracker[j] === id) {
						tracker.splice(j, 1);
					}
				}
			}
			tracker.push(id);
		}
		this._aSelectedRecords = tracker;
		this.fireEvent("selectEvent", {
			els :els
		});
	}
};
YAHOO.widget.DataTable.prototype.unselect = function(els) {
	if (els) {
		if (els.constructor != Array) {
			els = [ els ];
		}
		this._unselect(els);
		var tracker = this._aSelectedRecords || [];
		for ( var i = 0; i < els.length; i++) {
			var id = els[i].recordId;
			if (tracker.indexOf && (tracker.indexOf(id) > -1)) {
				tracker.splice(tracker.indexOf(id), 1);
			} else {
				for ( var j = 0; j < tracker.length; j++) {
					if (tracker[j] === id) {
						tracker.splice(j, 1);
					}
				}
			}
		}
		this._aSelectedRecords = tracker;
		this.fireEvent("unselectEvent", {
			els :els
		});
	}
};
YAHOO.widget.DataTable.prototype.unselectAllRows = function() {
	var selectedRows = YAHOO.util.Dom.getElementsByClassName(
			YAHOO.widget.DataTable.CLASS_SELECTED, "tr", this._elBody);
	this.unselect(selectedRows);
	this.fireEvent("unselectEvent", {
		els :selectedRows
	});
};
YAHOO.widget.DataTable.prototype.unselectAllCells = function() {
	var selectedCells = YAHOO.util.Dom.getElementsByClassName(
			YAHOO.widget.DataTable.CLASS_SELECTED, "td", this._elBody);
	this.unselect(selectedCells);
	this.fireEvent("unselectEvent", {
		els :selectedCells
	});
};
YAHOO.widget.DataTable.prototype.isSelected = function(el) {
	return YAHOO.util.Dom.hasClass(el, YAHOO.widget.DataTable.CLASS_SELECTED);
};
YAHOO.widget.DataTable.prototype.getSelectedRecordIds = function() {
	return this._aSelectedRecords || [];
};
YAHOO.widget.DataTable.prototype.getSelectedRows = function() {
	return YAHOO.util.Dom.getElementsByClassName(
			YAHOO.widget.DataTable.CLASS_SELECTED, "tr", this._elBody);
};
YAHOO.widget.DataTable.prototype.getSelectedCells = function() {
	return YAHOO.util.Dom.getElementsByClassName(
			YAHOO.widget.DataTable.CLASS_SELECTED, "td", this._elBody);
};
YAHOO.widget.DataTable.prototype.getColumnSet = function() {
	return this._oColumnSet;
};
YAHOO.widget.DataTable.prototype.getRecordSet = function() {
	return this._oRecordSet;
};
YAHOO.widget.DataTable.prototype.showPage = function(nPage) {
	if (!nPage || isNaN(nPage) || (nPage < 1) || (nPage > this._totalPages)) {
		nPage = 1;
	}
	this.pageCurrent = nPage;
	this.paginateRows();
};
YAHOO.widget.DataTable.prototype.paginateRows = function() {
	var i;
	var recordsLength = this._oRecordSet.getLength();
	var maxRows = (this.rowsPerPage < recordsLength) ? this.rowsPerPage
			: recordsLength;
	this._totalPages = Math.ceil(recordsLength / maxRows);
	this.startRecordIndex = (this.pageCurrent - 1) * this.rowsPerPage;
	var pageLinksLength = ((this.pageLinksLength > 0) && (this.pageLinksLength < this._totalPages)) ? this.pageLinksLength
			: this._totalPages;
	this.pageLinksStart = (Math.ceil(this.pageCurrent / pageLinksLength - 1) * pageLinksLength) + 1;
	var pageRecords = this._oRecordSet.getRecords(this.startRecordIndex,
			this.rowsPerPage);
	this.replaceRows(pageRecords);
	if (this.rowsPerPage < recordsLength) {
		var isFirstPage = (this.pageCurrent == 1) ? true : false;
		var isLastPage = (this.pageCurrent == this._totalPages) ? true : false;
		var firstPageLink = (isFirstPage) ? " <span class=\""
				+ YAHOO.widget.DataTable.CLASS_FIRSTPAGE + "\"></span> "
				: " <a href=\"#\" class=\""
						+ YAHOO.widget.DataTable.CLASS_FIRSTLINK
						+ "\">&lt;&lt;</a> ";
		var prevPageLink = (isFirstPage) ? " <span class=\""
				+ YAHOO.widget.DataTable.CLASS_PREVPAGE + "\"></span> "
				: " <a href=\"#\" class=\""
						+ YAHOO.widget.DataTable.CLASS_PREVLINK
						+ "\">&lt;</a> ";
		var nextPageLink = (isLastPage) ? " <span class=\""
				+ YAHOO.widget.DataTable.CLASS_NEXTPAGE + "\"></span> "
				: " <a href=\"#\" class=\""
						+ YAHOO.widget.DataTable.CLASS_NEXTLINK
						+ "\">&gt;</a> ";
		var lastPageLink = (isLastPage) ? " <span class=\""
				+ YAHOO.widget.DataTable.CLASS_LASTPAGE + "\"></span> "
				: " <a href=\"#\" class=\""
						+ YAHOO.widget.DataTable.CLASS_LASTLINK
						+ "\">&gt;&gt;</a> ";
		var markup = firstPageLink + prevPageLink;
		var maxLinks = (this.pageLinksStart + pageLinksLength < this._totalPages) ? this.pageLinksStart
				+ pageLinksLength - 1
				: this._totalPages;
		for (i = this.pageLinksStart; i <= maxLinks; i++) {
			if (i != this.pageCurrent) {
				markup += " <a href=\"#\" class=\""
						+ YAHOO.widget.DataTable.CLASS_PAGELINK + "\">" + i
						+ "</a> ";
			}

			else {
				markup += " <span class=\""
						+ YAHOO.widget.DataTable.CLASS_CURRENTPAGE + "\">" + i
						+ "</span>";
			}
		}
		markup += nextPageLink + lastPageLink;
		var dropdown = this.rowsPerPageDropdown;
		var select1, select2;
		if (dropdown && (dropdown.constructor == Array)
				&& (dropdown.length > 0)) {
			select1 = document.createElement("select");
			select1.className = YAHOO.widget.DataTable.CLASS_PAGESELECT;
			select2 = document.createElement("select");
			select2.className = YAHOO.widget.DataTable.CLASS_PAGESELECT;
			for (i = 0; i < dropdown.length; i++) {
				var option1 = document.createElement("option");
				var option2 = document.createElement("option");
				option1.value = dropdown[i];
				option2.value = dropdown[i];
				option1.innerHTML = dropdown[i];
				option2.innerHTML = dropdown[i];
				if (this.rowsPerPage === dropdown[i]) {
					option1.selected = true;
					option2.selected = true;
				}
				option1 = select1.appendChild(option1);
				option2 = select2.appendChild(option2);
			}
		}
		if (!this.pagers || (this.pagers.length === 0)) {
			var pager1 = document.createElement("span");
			pager1.className = YAHOO.widget.DataTable.CLASS_PAGELINKS;
			var pager2 = document.createElement("span");
			pager2.className = YAHOO.widget.DataTable.CLASS_PAGELINKS;
			pager1 = this._elContainer.insertBefore(pager1, this._elTable);
			select1 = (select1 === undefined) ? null : this._elContainer
					.insertBefore(select1, this._elTable);
			select2 = (select2 === undefined) ? null : this._elContainer
					.insertBefore(select2, this._elTable.nextSibling);
			pager2 = this._elContainer.insertBefore(pager2,
					this._elTable.nextSibling);
			this.pagers = [ {
				links :pager1,
				select :select1
			}, {
				links :pager2,
				select :select2
			} ];
		}
		for (i = 0; i < this.pagers.length; i++) {
			this.pagers[i].links.innerHTML = markup;
			YAHOO.util.Event.purgeElement(this.pagers[i].links);
			if (this.pagers[i].select) {
				YAHOO.util.Event.purgeElement(this.pagers[i].select);
			}
			this.pagers[i].innerHTML = markup;
			YAHOO.util.Event.addListener(this.pagers[i].links, "click",
					this._onPagerClick, this);
			if (this.pagers[i].select) {
				YAHOO.util.Event.addListener(this.pagers[i].select, "change",
						this._onPagerSelect, this);
			}
		}
	}
	this.fireEvent("paginateEvent");
};
YAHOO.widget.DataTable.prototype.sortColumn = function(oColumn) {
	if (!oColumn) {
		return;
	}
	if (!oColumn instanceof YAHOO.widget.Column) {
		return;
	}
	if (oColumn.sortable) {
		var sortDir = (oColumn.sortOptions && oColumn.sortOptions.defaultOrder) ? oColumn.sortOptions.defaultOrder
				: "asc";
		if (oColumn.key && this.sortedBy
				&& (this.sortedBy.colKey == oColumn.key)) {
			if (this.sortedBy.dir) {
				sortDir = (this.sortedBy.dir == "asc") ? "desc" : "asc";
			} else {
				sortDir = (sortDir == "asc") ? "desc" : "asc";
			}
		} else if (!this.sortedBy) {
			this.sortedBy = {};
		}
		var sortFnc = null;
		if ((sortDir == "desc") && oColumn.sortOptions
				&& oColumn.sortOptions.descFunction) {
			sortFnc = oColumn.sortOptions.descFunction;
		} else if ((sortDir == "asc") && oColumn.sortOptions
				&& oColumn.sortOptions.ascFunction) {
			sortFnc = oColumn.sortOptions.ascFunction;
		}
		if (!sortFnc && oColumn.key) {
			var sorted;
			sortFnc = function(a, b) {
				if (sortDir == "desc") {
					sorted = YAHOO.util.Sort.compareDesc(a[oColumn.key],
							b[oColumn.key]);
					if (sorted === 0) {
						return YAHOO.util.Sort.compareDesc(a.id, b.id);
					} else {
						return sorted;
					}
				} else {
					sorted = YAHOO.util.Sort.compareAsc(a[oColumn.key],
							b[oColumn.key]);
					if (sorted === 0) {
						return YAHOO.util.Sort.compareAsc(a.id, b.id);
					} else {
						return sorted;
					}
				}
			};
		}
		if (sortFnc) {
			this._oRecordSet.sort(sortFnc);
			this.paginateRows();
			YAHOO.util.Dom.removeClass(this.sortedBy._id,
					YAHOO.widget.DataTable.CLASS_SORTEDBYASC);
			YAHOO.util.Dom.removeClass(this.sortedBy._id,
					YAHOO.widget.DataTable.CLASS_SORTEDBYDESC);
			var newClass = (sortDir == "asc") ? YAHOO.widget.DataTable.CLASS_SORTEDBYASC
					: YAHOO.widget.DataTable.CLASS_SORTEDBYDESC;
			YAHOO.util.Dom.addClass(oColumn.getId(), newClass);
			this.sortedBy.colKey = oColumn.key;
			this.sortedBy.dir = sortDir;
			this.sortedBy._id = oColumn.getId();
			this.fireEvent("columnSortEvent", {
				column :oColumn,
				dir :sortDir
			});
		}
	} else {
	}
};
YAHOO.widget.DataTable.prototype.editCell = function(elCell) {
	if (elCell && !isNaN(elCell.columnIndex)) {
		var column = this._oColumnSet.keys[elCell.columnIndex];
		if (column && column.editor) {
			this.activeEditor = column.getEditor(elCell, this._oRecordSet
					.getRecord(elCell.parentNode.recordId));
		}
		this._bFocused = true;
		this.fireEvent("editorShowEvent", {
			target :elCell,
			column :column
		});
	}
};
YAHOO.widget.DataTable.prototype.formatCell = function(elCell) {
	if (elCell && !isNaN(elCell.columnIndex)) {
		var column = this._oColumnSet.keys[elCell.columnIndex];
		column.format(elCell, this._oRecordSet
				.getRecord(elCell.parentNode.recordId));
		this.fireEvent("cellFormatEvent", {
			el :elCell
		});
	}
};
YAHOO.widget.DataTable.prototype.onEventSortColumn = function(oArgs) {
	var evt = oArgs.event;
	var target = oArgs.target;
	YAHOO.util.Event.stopEvent(evt);
	var columnIndex = target.columnIndex;
	if (!isNaN(columnIndex)) {
		this.sortColumn(this._oColumnSet.keys[columnIndex]);
	}
};
YAHOO.widget.DataTable.prototype.onEventSelectRow = function(oArgs) {
	var i;
	var evt = oArgs.event;
	var target = oArgs.target;
	while (target.tagName.toLowerCase() != "tr") {
		target = target.parentNode;
	}
	if (this.isSelected(target)) {
		this.unselect(target);
	} else {
		if (this.rowSingleSelect && !evt.ctrlKey && !evt.shiftKey) {
			this.unselectAllRows();
		}
		if (evt.shiftKey) {
			var startRow = this._lastSelected;
			if (startRow && this.isSelected(startRow)) {
				this.unselectAllRows();
				if (startRow.sectionRowIndex < target.sectionRowIndex) {
					for (i = startRow.sectionRowIndex; i <= target.sectionRowIndex; i++) {
						this.select(this._elBody.rows[i]);
					}
				} else {
					for (i = target.sectionRowIndex; i <= startRow.sectionRowIndex; i++) {
						this.select(this._elBody.rows[i]);
					}
				}
			} else {
				this.select(target);
			}
		} else {
			this.select(target);
		}
	}
};
YAHOO.widget.DataTable.prototype.onEventSelectCell = function(oArgs) {
	var evt = oArgs.event;
	var target = oArgs.target;
	while (target.tagName.toLowerCase() != "td") {
		target = target.parentNode;
	}
	if (this.isSelected(target)) {
		this.unselect(target);
	} else {
		if (this.rowSingleSelect && !evt.ctrlKey) {
			this.unselectAllCells();
		}
		this.select(target);
	}
};
YAHOO.widget.DataTable.prototype.onEventFormatCell = function(oArgs) {
	var evt = oArgs.event;
	var element = oArgs.target;
	while (element.tagName.toLowerCase() != "td") {
		element = element.parentNode;
	}
	this.formatCell(element);
};
YAHOO.widget.DataTable.prototype.onEventHighlightCell = function(oArgs) {
	var evt = oArgs.event;
	var element = oArgs.target;
	while (element.tagName.toLowerCase() != "td") {
		element = element.parentNode;
	}
	this.highlight(element);
};
YAHOO.widget.DataTable.prototype.onEventUnhighlightCell = function(oArgs) {
	var evt = oArgs.event;
	var element = oArgs.target;
	while (element.tagName.toLowerCase() != "td") {
		element = element.parentNode;
	}
	this.unhighlight(element);
};
YAHOO.widget.DataTable.prototype.onEventEditCell = function(oArgs) {
	var evt = oArgs.event;
	var element = oArgs.target;
	while (element.tagName.toLowerCase() != "td") {
		element = element.parentNode;
	}
	this.editCell(element);
};
YAHOO.widget.DataTable.prototype.onDataReturnPaginateRows = function(sRequest,
		oResponse) {
	this.fireEvent("dataReturnEvent", {
		request :sRequest,
		response :oResponse
	});
	var ok = this.doBeforeLoadData(sRequest, oResponse);
	if (ok) {
		var newRecords = this._oRecordSet.append(oResponse);
		if (newRecords) {
			this.paginateRows();
		}
	}
};
YAHOO.widget.DataTable.prototype.onDataReturnAppendRows = function(sRequest,
		oResponse) {
	this.fireEvent("dataReturnEvent", {
		request :sRequest,
		response :oResponse
	});
	var ok = this.doBeforeLoadData(sRequest, oResponse);
	if (ok) {
		var newRecords = this._oRecordSet.append(oResponse);
		if (newRecords) {
			this.appendRows(newRecords);
		}
	}
};
YAHOO.widget.DataTable.prototype.onDataReturnInsertRows = function(sRequest,
		oResponse) {
	this.fireEvent("dataReturnEvent", {
		request :sRequest,
		response :oResponse
	});
	var ok = this.doBeforeLoadData(sRequest, oResponse);
	if (ok) {
		var newRecords = this._oRecordSet.insert(oResponse);
		if (newRecords) {
			this.insertRows(newRecords);
		}
	}
};
YAHOO.widget.DataTable.prototype.onDataReturnReplaceRows = function(sRequest,
		oResponse) {
	this.fireEvent("dataReturnEvent", {
		request :sRequest,
		response :oResponse
	});
	var ok = this.doBeforeLoadData(sRequest, oResponse);
	if (ok) {
		var newRecords = this._oRecordSet.replace(oResponse);
		if (newRecords) {
			this.replaceRows(newRecords);
		}
	}
};
YAHOO.widget.ColumnSet = function(aHeaders) {
	this._sName = "instance" + YAHOO.widget.ColumnSet._nCount;
	var tree = [];
	var flat = [];
	var keys = [];
	var headers = [];
	var nodelevel = -1;
	var parseColumns = function(nodeList, parent) {
		nodelevel++;
		if (!tree[nodelevel]) {
			tree[nodelevel] = [];
		}
		var nodeLevelMaxChildren = 0;
		var recurseChildren = function(nodeList) {
			var tmpMax = 0;
			for ( var i = 0; i < nodeList.length; i++) {
				if (nodeList[i].children) {
					tmpMax++;
					recurseChildren(nodeList[i].children);
				}
				if (tmpMax > nodeLevelMaxChildren) {
					nodeLevelMaxChildren = tmpMax;
				}
			}
		};
		recurseChildren(nodeList);
		for ( var j = 0; j < nodeList.length; j++) {
			var oColumn = new YAHOO.widget.Column(nodeList[j]);
			flat.push(oColumn);
			if (parent) {
				oColumn._parent = parent;
			}
			oColumn._rowspan = 1;
			oColumn._colspan = 1;
			if (nodeList[j].children) {
				var children = nodeList[j].children;
				var length = children.length;
				for ( var k = 0; k < length; k++) {
					var child = children[k];
					if (oColumn.className && (child.className === undefined)) {
						child.className = oColumn.className;
					}
					if (oColumn.editor && (child.editor === undefined)) {
						child.editor = oColumn.editor;
					}
					if (oColumn.formatter && (child.formatter === undefined)) {
						child.formatter = oColumn.formatter;
					}
					if (oColumn.parser && (child.parser === undefined)) {
						child.parser = oColumn.parser;
					}
					if (oColumn.resizeable && (child.resizeable === undefined)) {
						child.resizeable = oColumn.resizeable;
					}
					if (oColumn.type && (child.type === undefined)) {
						child.type = oColumn.type;
					}
					if (oColumn.width && (child.width === undefined)) {
						child.width = oColumn.width;
					}
				}
				oColumn._colspan = length;
				if (parent && parent._colspan) {
					parent._colspan += length - 1;
					parent._children = [];
					parent._children.push(oColumn);
				}
				if (!tree[nodelevel + 1]) {
					tree[nodelevel + 1] = [];
				}
				parseColumns(children, oColumn);
			} else if (nodeLevelMaxChildren > 0) {
				oColumn._rowspan += nodeLevelMaxChildren;
				oColumn._index = keys.length;
				keys.push(oColumn);
			} else {
				oColumn._index = keys.length;
				keys.push(oColumn);
			}
			tree[nodelevel].push(oColumn);
		}
		nodelevel--;
	};
	if (aHeaders.length > 0) {
		parseColumns(aHeaders);
	}
	var recurseAncestors = function(i, oColumn) {
		headers[i].push(oColumn._id);
		if (oColumn._parent) {
			recurseAncestors(i, oColumn._parent);
		}
	};
	for ( var i = 0; i < keys.length; i++) {
		headers[i] = [];
		recurseAncestors(i, keys[i]);
		headers[i] = headers[i].reverse();
		headers[i] = headers[i].join(" ");
	}
	this.tree = tree;
	this.flat = flat;
	this.keys = keys;
	this.headers = headers;
	YAHOO.widget.ColumnSet._nCount++;
};
YAHOO.widget.ColumnSet._nCount = 0;
YAHOO.widget.ColumnSet.prototype._sName = null;
YAHOO.widget.ColumnSet.prototype.tree = null;
YAHOO.widget.ColumnSet.prototype.flat = null;
YAHOO.widget.ColumnSet.prototype.keys = null;
YAHOO.widget.ColumnSet.prototype.headers = null;
YAHOO.widget.ColumnSet.prototype.toString = function() {
	return "ColumnSet " + this._sName;
};
YAHOO.widget.Column = function(oConfigs) {
	this._id = "yui-dtcol" + YAHOO.widget.Column._nCount;
	if (typeof oConfigs == "object") {
		for ( var sConfig in oConfigs) {
			if (sConfig) {
				this[sConfig] = oConfigs[sConfig];
			}
		}
	}
	YAHOO.widget.Column._nCount++;
};
YAHOO.widget.Column._nCount = 0;
YAHOO.widget.Column.prototype._id = null;
YAHOO.widget.Column.prototype._index = null;
YAHOO.widget.Column.prototype._colspan = 1;
YAHOO.widget.Column.prototype._rowspan = 1;
YAHOO.widget.Column.prototype._parent = null;
YAHOO.widget.Column.prototype._children = null;
YAHOO.widget.Column.prototype._width = null;
YAHOO.widget.Column.prototype._minWidth = null;
YAHOO.widget.Column.prototype.key = null;
YAHOO.widget.Column.prototype.text = null;
YAHOO.widget.Column.prototype.type = "string";
YAHOO.widget.Column.prototype.abbr = null;
YAHOO.widget.Column.prototype.children = null;
YAHOO.widget.Column.prototype.width = null;
YAHOO.widget.Column.prototype.className = null;
YAHOO.widget.Column.prototype.formatter = null;
YAHOO.widget.Column.prototype.parser = null;
YAHOO.widget.Column.prototype.editor = null;
YAHOO.widget.Column.prototype.resizeable = false;
YAHOO.widget.Column.prototype.sortable = false;
YAHOO.widget.Column.prototype.descFunction = null;
YAHOO.widget.Column.prototype.ascFunction = null;
YAHOO.widget.Column.prototype.getId = function() {
	return this._id;
};
YAHOO.widget.Column.prototype.getColSpan = function() {
	return this._colspan;
};
YAHOO.widget.Column.prototype.getRowSpan = function() {
	return this._rowspan;
};
YAHOO.widget.Column.prototype.format = function(elCell, oRecord) {
	var oData = (this.key) ? oRecord[this.key] : null;
	if (this.formatter) {
		this.formatter(elCell, oRecord, this, oData);
	} else {
		var type = this.type;
		var markup = "";
		var classname = "";
		switch (type) {
		case "checkbox":
			YAHOO.widget.Column.formatCheckbox(elCell, oRecord, this, oData);
			classname = YAHOO.widget.DataTable.CLASS_CHECKBOX;
			break;
		case "currency":
			YAHOO.widget.Column.formatCurrency(elCell, oRecord, this, oData);
			classname = YAHOO.widget.DataTable.CLASS_CURRENCY;
			break;
		case "date":
			YAHOO.widget.Column.formatDate(elCell, oRecord, this, oData);
			classname = YAHOO.widget.DataTable.CLASS_DATE;
			break;
		case "email":
			YAHOO.widget.Column.formatEmail(elCell, oRecord, this, oData);
			classname = YAHOO.widget.DataTable.CLASS_EMAIL;
			break;
		case "link":
			YAHOO.widget.Column.formatLink(elCell, oRecord, this, oData);
			classname = YAHOO.widget.DataTable.CLASS_LINK;
			break;
		case "number":
			YAHOO.widget.Column.formatNumber(elCell, oRecord, this, oData);
			classname = YAHOO.widget.DataTable.CLASS_NUMBER;
			break;
		case "select":
			YAHOO.widget.Column.formatSelect(elCell, oRecord, this, oData);
			classname = YAHOO.widget.DataTable.CLASS_SELECT;
			break;
		default:
			elCell.innerHTML = (oData) ? oData.toString() : "";
			classname = YAHOO.widget.DataTable.CLASS_STRING;
			break;
		}
		YAHOO.util.Dom.addClass(elCell, classname);
		if (this.className) {
			YAHOO.util.Dom.addClass(elCell, this.className);
		}
	}
	if (this.editor) {
		YAHOO.util.Dom.addClass(elCell, YAHOO.widget.DataTable.CLASS_EDITABLE);
	}
};
YAHOO.widget.Column.formatCheckbox = function(elCell, oRecord, oColumn, oData) {
	var bChecked = oData;
	bChecked = (bChecked) ? " checked" : "";
	elCell.innerHTML = "<input type=\"checkbox\"" + bChecked + " class=\""
			+ YAHOO.widget.DataTable.CLASS_CHECKBOX + "\">";
};
YAHOO.widget.Column.formatCurrency = function(elCell, oRecord, oColumn, oData) {
	var nAmount = oData;
	var markup;
	if (nAmount) {
		markup = "$" + nAmount;
		var dotIndex = markup.indexOf(".");
		if (dotIndex < 0) {
			markup += ".00";
		} else {
			while (dotIndex != markup.length - 3) {
				markup += "0";
			}
		}
	} else {
		markup = "";
	}
	elCell.innerHTML = markup;
};
YAHOO.widget.Column.formatDate = function(elCell, oRecord, oColumn, oData) {
	var oDate = oData;
	if (oDate) {
		try {
			var formatter		= new FormatDateHelper(oData);
			elCell.innerHTML	= formatter.formatDate();
		}
		catch(err) {
			elCell.innerHTML = (oDate.getMonth() + 1) + "/" + oDate.getDate() + "/"
				+ oDate.getFullYear();
		}
	} else {
		elCell.innerHTML = "";
	}
};
YAHOO.widget.Column.formatEmail = function(elCell, oRecord, oColumn, oData) {
	var sEmail = oData;
	if (sEmail) {
		elCell.innerHTML = "<a href=\"mailto:" + sEmail + "\">" + sEmail
				+ "</a>";
	} else {
		elCell.innerHTML = "";
	}
};
YAHOO.widget.Column.formatLink = function(elCell, oRecord, oColumn, oData) {
	var sLink = oData;
	if (sLink) {
		elCell.innerHTML = "<a href=\"" + sLink + "\">" + sLink + "</a>";
	} else {
		elCell.innerHTML = "";
	}
};
YAHOO.widget.Column.formatNumber = function(elCell, oRecord, oColumn, oData) {
	var nNumber = oData;
	if (nNumber) {
		elCell.innerHTML = nNumber.toString();
	} else {
		elCell.innerHTML = "";
	}
};
YAHOO.widget.Column.formatSelect = function(elCell, oRecord, oColumn, oData) {
	var selectedValue = oData;
	var options = oColumn.selectOptions;
	var markup = "<select>";
	if (options) {
		for ( var i = 0; i < options.length; i++) {
			var option = options[i];
			markup += "<option value=\"" + option + "\"";
			if (selectedValue === option) {
				markup += " selected";
			}
			markup += ">" + option + "</option>";
		}
	} else {
		if (selectedValue) {
			markup += "<option value=\"" + selectedValue + "\" selected>"
					+ selectedValue + "</option>";
		}
	}
	markup += "</select>";
	elCell.innerHTML = markup;
};
YAHOO.widget.Column.prototype.parse = function(sMarkup) {
	if (this.parser) {
		return this.parser(sMarkup);
	} else {
		var data = null;
		switch (this.type) {
		case "checkbox":
			data = YAHOO.widget.Column.parseCheckbox(sMarkup);
			break;
		case "currency":
			data = YAHOO.widget.Column.parseCurrency(sMarkup);
			break;
		case "date":
			data = YAHOO.widget.Column.parseDate(sMarkup);
			break;
		case "number":
			data = YAHOO.widget.Column.parseNumber(sMarkup);
			break;
		case "select":
			data = YAHOO.widget.Column.parseSelect(sMarkup);
			break;
		default:
			if (sMarkup) {
				data = sMarkup;
			}
			break;
		}
		return data;
	}
};

YAHOO.widget.Column.parseCheckbox = function(sMarkup) {
	return (sMarkup.indexOf("checked") < 0) ? false : true;
};

YAHOO.widget.Column.parseCurrency = function(sMarkup) {
	return parseFloat(sMarkup.substring(1));
};

YAHOO.widget.Column.parseDate = function(sMarkup) {
	var mm = trim(sMarkup.substring(0, sMarkup.indexOf("/")));
	sMarkup = trim(sMarkup.substring(sMarkup.indexOf("/") + 1));
	var dd = trim(sMarkup.substring(0, sMarkup.indexOf("/")));
	var yy = trim(sMarkup.substring(sMarkup.indexOf("/") + 1));
	var date = new Date();
	date.setYear(yy);
	// The value set by setMonth() is a number between 0 and 11
	date.setMonth(mm - 1);
	date.setDate(dd);
	return date
};

YAHOO.widget.Column.parseNumber = function(sMarkup) {
	return parseFloat(sMarkup);
};

YAHOO.widget.Column.parseSelect = function(sMarkup) {
};
YAHOO.widget.Column.prototype.getEditor = function(elCell, oRecord) {
	var oEditor = this.editor;
	if (oEditor.constructor == String) {
		oEditor = new YAHOO.widget.ColumnEditor(this.editor);
		oEditor.show(elCell, oRecord, this);
		this.editor = oEditor;
	} else if (oEditor instanceof YAHOO.widget.ColumnEditor) {
		oEditor.show(elCell, oRecord, this);
	}
	return oEditor;
};
YAHOO.widget.ColumnEditor = function(sType) {
	this.type = sType;
	var container = document.body.appendChild(document.createElement("div"));
	container.style.position = "absolute";
	container.style.zIndex = 9000;
	container.id = "yui-dt-coled" + YAHOO.widget.ColumnEditor._nCount;
	this.container = container;
	switch (this.type) {
	case "textbox":
		this.createTextboxEditor();
		break;
	case "textarea":
		this.createTextareaEditor();
		break;
	default:
		break;
	}
	YAHOO.widget.ColumnEditor._nCount++;
};
YAHOO.widget.ColumnEditor._nCount = 0;
YAHOO.widget.ColumnEditor.prototype.container = null;
YAHOO.widget.ColumnEditor.prototype.column = null;
YAHOO.widget.ColumnEditor.prototype.type = null;
YAHOO.widget.ColumnEditor.prototype.input = null;
YAHOO.widget.ColumnEditor.prototype.show = function(elCell, oRecord, oColumn) {
	this.cell = elCell;
	this.record = oRecord;
	this.column = oColumn;
	switch (this.type) {
	case "textbox":
		this.showTextboxEditor(elCell, oRecord, oColumn);
		break;
	case "textarea":
		this.showTextareaEditor(elCell, oRecord, oColumn);
		break;
	default:
		break;
	}
};
YAHOO.widget.ColumnEditor.prototype.getValue = function() {
	var value;
	switch (this.type) {
	case "textbox":
		value = this.getTextboxEditorValue();
		break;
	case "textarea":
		value = this.getTextareaEditorValue();
		break;
	default:
		break;
	}
	return value;
};
YAHOO.widget.ColumnEditor.prototype.createTextboxEditor = function() {
	var elTextbox = this.container.appendChild(document.createElement("input"));
	elTextbox.setAttribute("autocomplete", "off");
	this.input = elTextbox;
};
YAHOO.widget.ColumnEditor.prototype.createTextareaEditor = function() {
	var elTextarea = this.container.appendChild(document
			.createElement("textarea"));
	this.input = elTextarea;
};
YAHOO.widget.ColumnEditor.prototype.showTextboxEditor = function(elCell,
		oRecord, oColumn) {
	this.input.style.width = (parseInt(elCell.offsetWidth, 10) - 7) + "px";
	this.input.style.height = (parseInt(elCell.offsetHeight, 10) - 7) + "px";
	this.input.value = elCell.innerHTML;
	var x, y;
	if (navigator.userAgent.toLowerCase().indexOf("opera") != -1) {
		x = elCell.offsetLeft;
		y = elCell.offsetTop;
		while (elCell.offsetParent) {
			x += elCell.offsetParent.offsetLeft;
			y += elCell.offsetParent.offsetTop;
			elCell = elCell.offsetParent;
		}
	} else {
		var xy = YAHOO.util.Dom.getXY(elCell);
		x = parseInt(YAHOO.util.Dom.getX(elCell), 10);
		y = parseInt(YAHOO.util.Dom.getY(elCell), 10);
	}
	this.container.style.left = x + "px";
	this.container.style.top = y + "px";
	this.container.style.display = "block";
	this.input.tabIndex = 0;
	this.input.focus();
	this.input.select();
};
YAHOO.widget.ColumnEditor.prototype.showTextareaEditor = function(elCell,
		oRecord, oColumn) {
	this.input.style.width = (parseInt(elCell.offsetWidth, 10) - 7) + "px";
	this.input.style.height = 4 * (parseInt(elCell.offsetHeight, 10) - 7)
			+ "px";
	this.input.value = elCell.innerHTML;
	var x, y;
	if (navigator.userAgent.toLowerCase().indexOf("opera") != -1) {
		x = elCell.offsetLeft;
		y = elCell.offsetTop;
		while (elCell.offsetParent) {
			x += elCell.offsetParent.offsetLeft;
			y += elCell.offsetParent.offsetTop;
			elCell = elCell.offsetParent;
		}
	} else {
		var xy = YAHOO.util.Dom.getXY(elCell);
		x = parseInt(YAHOO.util.Dom.getX(elCell), 10);
		y = parseInt(YAHOO.util.Dom.getY(elCell), 10);
	}
	this.container.style.left = x + "px";
	this.container.style.top = y + "px";
	this.container.style.display = "block";
	this.input.tabIndex = 0;
	this.input.focus();
	this.input.select();
};
YAHOO.widget.ColumnEditor.prototype.hide = function() {
	this.input.tabIndex = -1;
	this.container.style.display = "none";
};
YAHOO.widget.ColumnEditor.prototype.getTextboxEditorValue = function() {
	return this.input.value;
};
YAHOO.widget.ColumnEditor.prototype.getTextareaEditorValue = function() {
	return this.input.value;
};
YAHOO.util.Sort = {
	compareAsc : function(a, b) {
		if (a.constructor == String) {
			a = a.toLowerCase();
		}
		if (b.constructor == String) {
			b = b.toLowerCase();
		}
		if (a < b) {
			return -1;
		} else if (a > b) {
			return 1;
		} else {
			return 0;
		}
	},
	compareDesc : function(a, b) {
		if (a.constructor == String) {
			a = a.toLowerCase();
		}
		if (b.constructor == String) {
			b = b.toLowerCase();
		}
		if (a < b) {
			return 1;
		} else if (a > b) {
			return -1;
		} else {
			return 0;
		}
	}
};
YAHOO.util.WidthResizer = function(oDataTable, colId, handleId, sGroup, config) {
	if (colId) {
		this.cell = YAHOO.util.Dom.get(colId);
		this.init(handleId, sGroup, config);
		this.datatable = oDataTable;
		this.setYConstraint(0, 0);
	} else {
	}
};
if (YAHOO.util.DD) {
	YAHOO.extend(YAHOO.util.WidthResizer, YAHOO.util.DD);
}
YAHOO.util.WidthResizer.prototype.onMouseDown = function(e) {
	this.startWidth = this.cell.offsetWidth;
	this.startPos = YAHOO.util.Dom.getX(this.getDragEl());
	if (this.datatable.fixedwidth) {
		var cellText = YAHOO.util.Dom.getElementsByClassName(
				YAHOO.widget.DataTable.CLASS_COLUMNTEXT, "span", this.cell)[0];
		this.minWidth = cellText.offsetWidth + 6;
		var sib = this.cell.nextSibling;
		var sibCellText = YAHOO.util.Dom.getElementsByClassName(
				YAHOO.widget.DataTable.CLASS_COLUMNTEXT, "span", sib)[0];
		this.sibMinWidth = sibCellText.offsetWidth + 6;
		var left = ((this.startWidth - this.minWidth) < 0) ? 0
				: (this.startWidth - this.minWidth);
		var right = ((sib.offsetWidth - this.sibMinWidth) < 0) ? 0
				: (sib.offsetWidth - this.sibMinWidth);
		this.setXConstraint(left, right);
	}
};
YAHOO.util.WidthResizer.prototype.onMouseUp = function(e) {
	var resizeStyle = YAHOO.util.Dom.get(this.handleElId).style;
	resizeStyle.left = "auto";
	resizeStyle.right = 0;
	resizeStyle.marginRight = "-6px";
	resizeStyle.width = "6px";
	this.datatable.fireEvent("columnResizeEvent", {
		datatable :this.datatable,
		target :YAHOO.util.Dom.get(this.id)
	});
};
YAHOO.util.WidthResizer.prototype.onDrag = function(e) {
	if (newWidth < this.minWidth) {
		newWidth = this.minWidth;
	}
	var oDataTable = this.datatable;
	var elCell = this.cell;
	if (oDataTable.fixedwidth) {
		var sib = elCell.nextSibling;
		var sibnewwidth = sib.offsetWidth - offsetX;
		if (sibnewwidth < this.sibMinWidth) {
			sibnewwidth = this.sibMinWidth;
		}
		for ( var i = 0; i < oDataTable._oColumnSet.length; i++) {
			if ((i != elCell.index) && (i != sibIndex)) {
				YAHOO.util.Dom.get(oDataTable._oColumnSet.keys[i].id).style.width = oDataTable._oColumnSet.keys[i].width
						+ "px";
			}
		}
		sib.style.width = sibnewwidth;
		elCell.style.width = newWidth + "px";
	} else {
		elCell.style.width = newWidth + "px";
	}
};
YAHOO.widget.RecordSet = function(data) {
	this._nIndex = YAHOO.widget.RecordSet._nCount;
	this._records = [];
	if (data) {
		if (data.constructor == Array) {
			this.addRecords(data);
		} else if (data.constructor == Object) {
			this.addRecord(data);
		}
	}
	this.createEvent("recordUpdateEvent");
	YAHOO.widget.RecordSet._nCount++;
};
if (YAHOO.util.EventProvider) {
	YAHOO.augment(YAHOO.widget.RecordSet, YAHOO.util.EventProvider);
} else {
}
YAHOO.widget.RecordSet._nCount = 0;
YAHOO.widget.RecordSet.prototype._nIndex = null;
YAHOO.widget.RecordSet.prototype._length = null;
YAHOO.widget.RecordSet.prototype.toString = function() {
	return "RecordSet instance " + this._nIndex;
};
YAHOO.widget.RecordSet.prototype.getLength = function() {
	return this._length;
};
YAHOO.widget.RecordSet.prototype.getRecord = function(identifier) {
	if (identifier) {
		if (identifier.constructor == String) {
			for ( var i = 0; i < this._records.length; i++) {
				if (this._records[i].id == identifier) {
					return this._records[i];
				}
			}
			return null;
		} else {
			return this._records[identifier];
		}
	}
	return null;
};
YAHOO.widget.RecordSet.prototype.getRecords = function(i, range) {
	if (i === undefined) {
		return this._records;
	}
	i = parseInt(i, 10);
	if (isNaN(i)) {
		return null;
	}
	if (range === undefined) {
		return this._records.slice(i);
	}
	range = parseInt(range, 10);
	if (isNaN(range)) {
		return null;
	}
	/*alert(this._records);
	alert(i);
	alert(range);*/
	var ii = i;
	if (ii > 0) {
		ii = ii -1;
	}
	return this._records.slice(ii, i + range);
};
YAHOO.widget.RecordSet.prototype.updateRecord = function(oRecord, sKey, oData) {
	var oldData = oRecord[sKey];
	oRecord[sKey] = oData;
	this.fireEvent("recordUpdateEvent", {
		record :oRecord,
		key :sKey,
		newData :oData,
		oldData :oldData
	});
};
YAHOO.widget.RecordSet.prototype.addRecord = function(oObjectLiteral, index) {
	if (oObjectLiteral) {
		var oRecord = new YAHOO.widget.Record(oObjectLiteral);
		if (!isNaN(index) && (index > -1)) {
			this._records.splice(index, 0, oRecord);
		} else {
			this._records.push(oRecord);
		}
		this._length++;
		return oRecord;
	} else {
		return null;
	}
};
YAHOO.widget.RecordSet.prototype.addRecords = function(data, index) {
	if (data) {
		if (data.constructor == Array) {
			var newRecords = [];
			for ( var i = 0; i < data.length; i++) {
				var record = this.addRecord(data[i], index);
				newRecords.push(record);
			}
			return newRecords;
		} else if (data.constructor == Object) {
			return this.addRecord(data);
		}
	} else {
		return null;
	}
};
YAHOO.widget.RecordSet.prototype.append = function(data) {
	if (data) {
		if (data.constructor == Array) {
			var newRecords = [];
			for ( var i = 0; i < data.length; i++) {
				var record = this.addRecord(data[i]);
				newRecords.push(record);
			}
			return newRecords;
		} else if (data.constructor == Object) {
			return this.addRecord(data);
		}
	} else {
		return null;
	}
};
YAHOO.widget.RecordSet.prototype.insert = function(data) {
	if (data) {
		if (data.constructor == Array) {
			var newRecords = [];
			for ( var i = data.length - 1; i > -1; i--) {
				var record = this.addRecord(data[i], 0);
				newRecords.push(record);
			}
			return newRecords;
		} else if (data.constructor == Object) {
			return this.addRecord(data, 0);
		}
	} else {
		return null;
	}
};
YAHOO.widget.RecordSet.prototype.replace = function(data) {
	if (data) {
		this.reset();
		return this.append(data);
	} else {
		return null;
	}
};
YAHOO.widget.RecordSet.prototype.sort = function(fnSort) {
	return this._records.sort(fnSort);
};
YAHOO.widget.RecordSet.prototype.deleteRecord = function(i, range) {
	if (!range || isNaN(range)) {
		range = 1;
	}
	if (i && !isNaN(i)) {
		this._records.splice(i, range);
		this._length = this._length - range;
	}
};
YAHOO.widget.RecordSet.prototype.reset = function() {
	this._records = [];
	this._length = 0;
};
YAHOO.widget.Record = function(oLiteral) {
	if (typeof oLiteral == "object") {
		for ( var sKey in oLiteral) {
			if (sKey) {
				this[sKey] = oLiteral[sKey];
			}
		}
	}
	this.id = "yui-dtrec" + YAHOO.widget.Record._nCount;
	YAHOO.widget.Record._nCount++;
};
YAHOO.widget.Record._nCount = 0;
YAHOO.widget.Record.prototype.id = null;
YAHOO.register("datatable", YAHOO.widget.DataTable, {
	version :"2.2.0",
	build :"127"
});