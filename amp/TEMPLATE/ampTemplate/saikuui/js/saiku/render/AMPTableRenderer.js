var AMPTableRenderer = function(options) {
	type = "HTML";
	metadataHierarchies = new Array();
	metadataColumns = new Array();
	// We receive the structure metadata as parameter because Rhino will
	// complain about missing variables when exporting to PDF.
	if (options !== undefined) {
		if (options.type !== undefined) {
			type = options.type;
		}
		if (options.hierarchies !== undefined) {
			metadataHierarchies = options.hierarchies;
		}
		if (options.columns !== undefined) {
			metadataColumns = options.columns;
		}
	}
};

this.headerMatrix = undefined;
this.metadataHierarchies = undefined;
this.metadataColumns = undefined;
this.contentMatrix = undefined;
this.lastHeaderRow = undefined;
this.currentContentIndexRow = undefined;
this.numberOfRows = undefined;
this.type = undefined;

AMPTableRenderer.prototype.render = function(data, options) {
	// Create HTML table, with header + content.
	var table = "<table>";
	var headerHtml = generateHeaderHtml(data.headers);
	var contentHtml = generateContentHtml(data.page);
	table += headerHtml + contentHtml + "</table>";
	return table;
};

function generateHeaderHtml(headers) {
	// Discover tree depth.
	var maxHeaderLevel = 0;
	_.each(headers, function(item) {
		var level = getMaxHeaderLevel(item, 1);
		if (maxHeaderLevel < level) {
			maxHeaderLevel = level;
		}
	});
	// Create the header matrix structure.
	this.headerMatrix = new Array(maxHeaderLevel);
	for (var i = 0; i < maxHeaderLevel; i++) {
		this.headerMatrix[i] = new Array(headers.length);
	}
	// Convert tree nodes into array cells.
	for (var i = 0; i < headers.length; i++) {
		var currentColumn = headers[i];
		generateHeaderRows(currentColumn, 0, i);
	}
	// Reorder matrix rows.
	this.headerMatrix.reverse();
	// Check columns metadata
	calculateColumnsDisposition();
	// Generate header HTML.
	var header = "<thead>";
	for (var i = 0; i < this.headerMatrix.length; i++) {
		var row = "<tr>";
		for (var j = 0; j < this.headerMatrix[i].length; j++) {
			if (this.headerMatrix[i][j] !== undefined) {
				// Add sorting info: HEADER_HIERARCHY for first columns that
				// define a hierarchy (if any), HEADER_COMMON for non
				// hierarchical columns and HEADER_MEASURE for measures (only in
				// the last header row).
				var sortingType = "";
				if (j < this.metadataHierarchies.length) {
					sortingType = " data-sorting-type='HEADER_HIERARCHY'";
				} else if (j < this.metadataColumns.length) {
					sortingType = " data-sorting-type='HEADER_COMMON'";
				} else if (i === this.headerMatrix.length - 1) {
					sortingType = " data-sorting-type='HEADER_MEASURE'";
				}
				// Since groupCount is 0 when no column grouping is applicable
				// then we don't need an extra IF for creating the 'col'
				// variable.
				var groupCount = findSameHeaderHorizontally(this.headerMatrix,
						i, j);
				// Define styles for the header.
				var style = " class='col";
				if (sortingType.length > 0) {
					style += " hand-pointer";
				}
				style += "'";
				// Define id based on its hierarchy.
				var id = " id='"
						+ convertHierarchicalNameToId(this.headerMatrix[i][j].hierarchicalName)
						+ "'";

				var col = "<th" + style + id + " data-header-level='" + i + "'"
						+ sortingType + " colspan='" + +groupCount + "'><div>"
						+ this.headerMatrix[i][j].columnName + "</div></th>";

				// We change 'j' in order to skip the next N columns.
				j += groupCount;
				if (groupCount > 0) {
					// Decrement by 1 to adjust the column index correctly.
					j -= 1;
				}
			} else {
				var col = "<th class='all_null'>&nbsp;</th>";
			}
			row += col;
		}
		row += "</tr>";
		header += row;
	}
	header += "</thead>";
	return header;
}

/**
 * We need to calculate how many hierarchical and common columns come from the
 * endpoint (we can't trust the metadata). Hierarchical columns (if any) come
 * always first in the list, then common columns and last measure columns.
 */
function calculateColumnsDisposition() {
	var tempColumns = new Array();
	var lastRowColumns = this.headerMatrix.length - 1;
	for (var i = 0; i < this.metadataColumns.length; i++) {
		var found = _
				.find(
						this.headerMatrix[lastRowColumns],
						function(item) {
							return item.originalColumnName === this.metadataColumns[i].entityName;
						});
		if (found !== undefined) {
			tempColumns.push(this.metadataColumns[i]);
		}
	}
	this.metadataColumns = tempColumns;
}

/**
 * Return a number > 0 if the current header cell can be merged with other cells
 * on its right side.
 */
function findSameHeaderHorizontally(matrix, i, j) {
	var count = 0;
	if (i === matrix.length - 1) {
		// The last row can never be grouped.
		return 0;
	}
	var currentLabel = matrix[i][j].columnName;
	for (var k = j; k < matrix[i].length; k++) {
		if (matrix[i][k] !== undefined
				&& matrix[i][k].columnName === currentLabel) {
			count++;
		} else {
			break;
		}
	}
	return count;
}

function generateContentHtml(page) {
	var self = this;
	var content = "<tbody>";
	this.lastHeaderRow = this.headerMatrix.length - 1;
	// Add data rows.
	var dataHtml = generateDataRows(page);
	content += dataHtml;

	// Add last row with totals.
	var totalRow = "<tr>";
	for (var i = 0; i < this.headerMatrix[this.lastHeaderRow].length; i++) {
		var totalValue = "<td class='data total'>"
				+ page.pageArea.contents[this.headerMatrix[this.lastHeaderRow][i].hierarchicalName].displayedValue
				+ "</td>";
		totalRow += totalValue;
	}
	totalRow += "</tr>";
	content += totalRow;
	content += "</tbody>";
	return content;
}

function generateDataRows(page) {
	var self = this;
	var content = "";
	// Transform the tree data structure to 2d matrix.
	this.numberOfRows = -1;
	getNumberOfRows(page.pageArea);
	this.contentMatrix = new Array(this.numberOfRows);
	for (var i = 0; i < this.numberOfRows; i++) {
		this.contentMatrix[i] = new Array(
				this.headerMatrix[this.headerMatrix.length - 1].length);
	}
	this.currentContentIndexRow = 0;
	for (var i = 0; i < page.pageArea.children.length; i++) {
		extractDataFromTree(page.pageArea.children[i],
				this.currentContentIndexRow);
	}

	for (var i = 0; i < this.contentMatrix.length; i++) {
		var applyTotalRowStyle = false;
		var row = "<tr>";
		for (var j = 0; j < this.contentMatrix[i].length; j++) {
			if (j < this.metadataColumns.length) {
				if (this.contentMatrix[i][j].isGrouped === true) {
					continue;
				}
				var group = findGroupVertically(this.contentMatrix, i, j);
				var rowSpan = " rowspan='" + group + "' ";
				var styleClass = "";
				var value = this.contentMatrix[i][j].displayedValue;
				var cleanValue = cleanText(value);
				if (cleanValue.tooltip !== undefined) {
					styleClass = " class='row tooltipped' original-title='"
							+ cleanValue.tooltip + "' ";
				} else {
					styleClass = " class='row'";
				}

				// Ignore subtotal rows text and change style.
				if (this.contentMatrix[i][j].isTotal === true) {
					if (applyTotalRowStyle === false
							&& cleanValue.text.length > 0) {
						// This flag indicates in which column we start applying
						// the total style.
						applyTotalRowStyle = true;
					}

					// Apply the special style for subtotal rows but starting in
					// the right column index.
					if (applyTotalRowStyle === true) {
						// Trying something new here: show tooltip on the now
						// empty "Hierarchy Value Totals" row.
						if (cleanValue.text != undefined) {
							styleClass = " class='row_total tooltipped' original-title='"
									+ cleanValue.text + "' ";
						} else {
							styleClass = " class='row_total' ";
						}
					} else {
						styleClass = " class='row' ";
					}
					cleanValue.text = '';
				}

				var cell = "<th" + styleClass + rowSpan + ">";
				cell += cleanValue.text;
				cell += "</th>";
			} else {
				// Change amount styles if is a subtotal.
				if (this.contentMatrix[i][j].isTotal === true) {
					var cell = "<td class='data total'>";
					cell += this.contentMatrix[i][j].displayedValue;
					cell += "</td>";
				} else {
					var cell = "<td class='data'>";
					cell += this.contentMatrix[i][j].displayedValue;
					cell += "</td>";
				}
			}
			row += cell;
		}
		row += "</tr>";
		content += row;
	}
	return content;
}

/**
 * Return > 1 if the current group cell can be merged with other cells below it.
 */
function findGroupVertically(matrix, i, j) {
	var count = 1;
	// Only process if the column is in the list of hierarchical columns (which
	// are always in the beginning). Also skip cells that represent the start of
	// a total row.
	if (j < this.metadataHierarchies.length && matrix[i][j].isTotal !== true) {
		for (var k = i + 1; k < matrix.length; k++) {
			// Due to the way the tree data is structured we don't need to check
			// for cells with the same value than the one being compared but
			// with empty cells.
			if (matrix[k][j].displayedValue.length === 0) {
				count++;
				// Mark the cell so we don't draw it later.
				matrix[k][j].isGrouped = true;
			} else {
				// Add the last row which has the total label and exit.
				break;
			}
		}
	}
	return count;
}

/**
 * Convert data tree to 2d matrix structure.
 */
function extractDataFromTree(node) {
	if (node.children == null) {
		// Add this node to contentMatrix, the order of the cells is defined by
		// the header's last row.
		for (var i = 0; i < this.headerMatrix[this.lastHeaderRow].length; i++) {
			var dataValue = node.contents[this.headerMatrix[this.lastHeaderRow][i].hierarchicalName];
			// Save isTotal flag.
			dataValue.isTotal = node.isTotal;
			this.contentMatrix[this.currentContentIndexRow][i] = dataValue;
		}
		this.currentContentIndexRow++;
	} else {
		for (var i = 0; i < node.children.length; i++) {
			extractDataFromTree(node.children[i]);
		}
		// Add the node that represents the subtotal.
		node.children = null;
		node.isTotal = true;
		extractDataFromTree(node);
	}
}

/**
 * Return the number of rows (adding the total rows per category).
 */
function getNumberOfRows(node) {
	if (node.children !== null) {
		for (var i = 0; i < node.children.length; i++) {
			getNumberOfRows(node.children[i]);
		}
	}
	this.numberOfRows++;
}

/**
 * Small recursive function to know how deep is the header.
 */
function getMaxHeaderLevel(column, level) {
	if (column.parentColumn !== null) {
		level++;
		level = getMaxHeaderLevel(column.parentColumn, level);
		return level;
	} else {
		return level;
	}
}

/**
 * Convert header tree to 2d matrix structure.
 */
function generateHeaderRows(column, iRow, iCol) {
	if (column.parentColumn !== null) {
		this.headerMatrix[iRow][iCol] = column;
		iRow++;
		iRow = generateHeaderRows(column.parentColumn, iRow, iCol);
		return iRow;
	} else {
		this.headerMatrix[iRow][iCol] = column;
		return iRow;
	}
}

/**
 * Remove characters that would break the html, shorten if is larger than 60
 * chars and generate a tooltip text if needed.
 */
function cleanText(text) {
	text = text.replace(/<(?:.|\n)*?>/gm, '').replace(/["']/g, "");
	var cellText = "";
	var tooltip = undefined;
	if (text.length > 60) {
		tooltip = text;
		text = text.substring(0, 60) + "...";
	}
	return {
		text : text,
		tooltip : tooltip
	}
}

/**
 * Convert a string like "[2014][Actual Commitments]" to "-2014--Actual
 * Commitments-" that woun't break the search by id in jquery.
 */
function convertHierarchicalNameToId(hierarchy) {
	var id = "";
	id = hierarchy.replace(/\[/g, '-').replace(/\]/g, '-');
	return id;
}