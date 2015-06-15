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
this.summarizedReport = undefined;

AMPTableRenderer.prototype.render = function(data, options) {
	// When using this class to export the report we receive these extra
	// parameters from the endpoint because are unavailable in the constructor
	// call when using Rhino.
	if (type === 'PDF') {
		metadataColumns = data.columns;
		metadataHierarchies = data.hierarchies;
	}

	if (data.page !== null && data.page.pageArea !== null) {
		summarizedReport = checkIfSummarizedReportWithConstant(data.page);
		// Make an adjustment in the hierarchies list when showing a summarized
		// report.
		preprocessHierarchies();

		// Create HTML table, with header + content.
		var table = "<table>";
		var headerHtml = generateHeaderHtml(data.headers);
		var contentHtml = generateContentHtml(data.page, {
			reportTotalsString : data.reportTotalsString
		});
		table += headerHtml + contentHtml + "</table>";
		return table;
	} else {
		return "";
	}
};

/**
 * We dont have the parameter for summarized reports so if metadataColumns has
 * the same number of elements than metadataHierarchies then we remove the last
 * element from metadataHierarchies to avoid a visual defect shown in AMP-20295.
 * (The endpoint is not consistent in this case because it doesnt provide the
 * 'totals' row for the last hierarchy)
 */
function preprocessHierarchies() {
	if (this.metadataColumns !== undefined
			&& this.metadataHierarchies !== undefined
			&& this.metadataColumns.length === this.metadataHierarchies.length) {
		this.metadataHierarchies.splice(this.metadataHierarchies.length - 1, 1);
	}
}

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
				var groupCount = findSameHeaderHorizontally(i, j);
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

				// Change columnName when the endpoint sends "Constant" in a
				// summarized report.
				if (this.summarizedReport === true
						&& this.headerMatrix[i][j].hierarchicalName === "[Constant]") {
					this.headerMatrix[i][j].columnName = "-";
				}

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
	var self = this;
	var tempColumns = new Array();
	var lastRowColumns = this.headerMatrix.length - 1;
	for (var i = 0; i < this.metadataColumns.length; i++) {
		var found = _
				.find(
						this.headerMatrix[lastRowColumns],
						function(item) {
							return item.originalColumnName === self.metadataColumns[i].entityName;
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
function findSameHeaderHorizontally(i, j) {
	var count = 0;
	if (i === this.headerMatrix.length - 1) {
		// The last row can never be grouped.
		return 0;
	}
	var currentLabel = this.headerMatrix[i][j].columnName;
	for (var k = j; k < this.headerMatrix[i].length; k++) {
		if (this.headerMatrix[i][k] !== undefined
				&& this.headerMatrix[i][k].columnName === currentLabel) {
			if (this.headerMatrix[i][j].parentColumn === null
					&& this.headerMatrix[i][k].parentColumn === null) {
				// This is the top row or we dont have a parent.
				count++;
			} else {
				if (this.headerMatrix[i][j].parentColumn.hierarchicalName === this.headerMatrix[i][k].parentColumn.hierarchicalName) {
					// Same parent and same value, so we can group them.
					count++;
				} else {
					break;
				}
			}
		} else {
			break;
		}
	}
	return count;
}

function generateContentHtml(page, options) {
	var self = this;
	var content = "<tbody>";
	this.lastHeaderRow = this.headerMatrix.length - 1;
	// Add data rows.
	var dataHtml = generateDataRows(page, options);
	content += dataHtml;

	// Add last row with totals.
	var totalRow = "<tr>";
	for (var i = 0; i < this.headerMatrix[this.lastHeaderRow].length; i++) {
		// This check is for those summarized reports that dont return any
		// content.
		if (page.pageArea.contents !== null) {
			var totalValue = "<td class='data total'>"
					+ page.pageArea.contents[this.headerMatrix[this.lastHeaderRow][i].hierarchicalName].displayedValue
					+ "</td>";
			totalRow += totalValue;
		}
	}
	totalRow += "</tr>";
	content += totalRow;
	content += "</tbody>";
	return content;
}

function generateDataRows(page, options) {
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
						// This flag indicates in which column we start
						// applying
						// the total style.
						applyTotalRowStyle = true;
					}

					// Apply the special style for subtotal rows but
					// starting in
					// the right column index.
					if (applyTotalRowStyle === true) {
						// Trying something new here: show tooltip on the
						// now
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
					if (type === 'HTML') {
						cleanValue.text = '';
					}
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
					// Special case we receive the word "constant" from the
					// endpoint (summarized reports).
					if (this.summarizedReport === true && i === 0 && j === 0) {
						cell += options.reportTotalsString;
					} else {
						cell += this.contentMatrix[i][j].displayedValue;
					}
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
			if (this.type === 'CSV' || this.type === 'XLSX') {
				// If this is a hierarchy column.
				if (i < this.metadataHierarchies.length) {
					// If current cell is empty then take the above cell value.
					if (dataValue.displayedValue.length === 0) {
						dataValue = this.contentMatrix[this.currentContentIndexRow - 1][i];
					}
				}
			}
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

/**
 * Check if this is a summarized report with only one row, this kind of report
 * has a different structure with no total data, only 1 children and the word
 * "constant" hardcoded instead of "Report Totals" or similar.
 */
function checkIfSummarizedReportWithConstant(page) {
	var summarized = false;
	if (page.pageArea.contents === null && page.totalRecords === 1
			&& page.pageArea.children[0].contents['[Constant]'] !== undefined) {
		summarized = true;
	}
	return summarized;
}