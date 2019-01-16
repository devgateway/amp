var AMPTableRenderer = function(options) {
	metadataHierarchies = new Array();
	metadataColumns = new Array();
	// We receive the structure metadata as parameter because Rhino will
	// complain about missing variables when exporting to PDF.
	if (options !== undefined) {
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
this.summarizedReport = undefined;
this.draftColumn = undefined;
this.approvalStatusColumn = undefined;
this.hiddenColumns = [];
this.hiddenColumnNames = undefined;
this.ACTIVITY_STATUS_CODES = undefined;
this.PLEDGE_ID_ADDER = 800000000; // java-side constant, taken MondrianETL
this.rowsFromBatch = 100;
this.showRowTotals = true;
var measureless = false;

AMPTableRenderer.prototype.render = function(data, options) {
	Saiku.logger.log('AMPTableRenderer.render INIT');
	window.saiku_time = new Date().getTime();

    showRowTotals = data.rowTotals;

	var measures = data.workspace.query.attributes.measures;
	var hasMeasures = (measures instanceof Array) && measures.length > 0;
	measureless = !hasMeasures;

	if (data !== undefined && data.page !== null && data.page.pageArea !== null && !data.isEmpty) {
		ACTIVITY_STATUS_CODES = data.colorSettings.activityStatusCodes;
		
		summarizedReport = checkIfSummarizedReportWithConstant(data.page);
		// Make an adjustment in the hierarchies list when showing a summarized
		// report.
		preprocessHierarchies();
		
		hiddenColumnNames = data.colorSettings.hiddenColumnNames;

		// Create HTML table, with header + content.
		
		var preTableWarning = generateWarningsHtml(data);
		var table = "<table>";
		var headerHtml = generateHeaderHtml(data);
		var contentHtml = generateContentHtml(data.page, {
			reportTotalsString : data.reportTotalsString
		});
		table += headerHtml + contentHtml + "</table>";
		Saiku.logger.log('AMPTableRenderer.render END');
		Saiku.logger.log(new Date().getTime() - window.saiku_time + "ms");
		return preTableWarning + table;
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

/**
 * returns one of HEADER_HIERARCHY, HEADER_COMMON (column), HEADER_MEASURE or undefined
 */
function getEntityTypeByColumnNumber(headerRowNumber, headerColumnNumber) {
	if (this.headerMatrix[headerRowNumber][headerColumnNumber] !== undefined) {
		if (headerColumnNumber < this.metadataHierarchies.length) 
			return 'HEADER_HIERARCHY';
		else if (headerColumnNumber < this.metadataColumns.length)
			return 'HEADER_COMMON';
		else 
			return 'HEADER_MEASURE';
	}
	return undefined;
}

function generateWarningsHtml(data) {
	var pre = '<div class="reportWarning">' 
	var post = "</div>";
	var errors = [];
	if (data.reportWarnings !== undefined) {
		for (var key in data.reportWarnings) {
			for (var key2 in data.reportWarnings[key]) {
				errors.push('Warning: ' + data.reportWarnings[key][key2].message);
				
			}
		}
	}
	if (errors.length > 0)
		return pre + errors.join(post + pre) + post;
	else
		return '';
}

function generateHeaderHtml(data) {
	
	var headers = data.headers;
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

	this.draftColumn = getIndexOfColumn('Draft');
	this.approvalStatusColumn = getIndexOfColumn('Approval Status');
	
	for (var i=0; i < hiddenColumnNames.length; i++) {
		hiddenColumns[i] = getIndexOfColumn(hiddenColumnNames[i]);
	}

	// Check columns metadata
	calculateColumnsDisposition();
	
	// Generate header HTML.	
	return generateReportHeaderHtml(data.generatedHeaders);
}

function generateReportHeaderHtml(headers) {
	var header = "<thead>"
	for(var i = 0; i < headers.length; i++) {
		header += "<tr class='nireport_header'>";
		for(var j = 0; j < headers[i].length; j++) {
			if (i === 0 && headers[i][j].rowSpan === headers.length 
					&& (isHiddenColumnByName(headers[i][j].originalName))) {
				continue;
			}
			
			header += "<th id='" + headers[i][j].fullOriginalName + "' ";
			header += "class='col hand-pointer'";

			if (i === headers.length - headers[i][j].rowSpan) {
				header += " sorting='true' ";
			}
			
			header += "rowSpan='" + headers[i][j].rowSpan + "' colSpan='" + headers[i][j].colSpan + "'>";
			header += "<div class = 'i18n'>"+ headers[i][j].name;
			
			if (headers[i][j].description) {
				header += "<img src='/TEMPLATE/ampTemplate/images/help.gif'" +
						" style= 'padding-left:5px'" + 
						" title='" + headers[i][j].description + "'>"
			}
			
			header +="</div></th>";
		}
		header += "</tr>\n";
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

/**
 * Return if the header cell can be sorted or not.
 * A header cell can be marked as not 'sortable' if entity === 'HEADER_MEASURE' 
 * and is not in the last row (that means it is a measure grouping other measures)
 */
function isHeaderCellSortable(i, entityName) {
	if (entityName === "HEADER_MEASURE" &&
			i < this.headerMatrix.length - 1) {
		return false;
	}
	
	return true;
}

/**
 * returns an imperatively-built totals row html markup
 */
function buildTotalsRow(page) {
	var totalRow = "<tr class='level_0'>";
	var isFirstColumn = true;
	for (var j = 0; j < this.headerMatrix[this.lastHeaderRow].length; j++) {
		// This check is for those summarized reports that dont return any
		// content.
		
		if (page.pageArea.contents !== null && !isHiddenColumn(j)) {
			var td = "<td class='data total i18n tooltipped' ";
			var auxTd = "";
			var reportTotals = "Report Totals";
			var cell = page.pageArea.contents[this.headerMatrix[this.lastHeaderRow][j].hierarchicalName];
			if ((cell === null) || (typeof(cell) == 'undefined')) {
				cell = {value: null, displayedValue: ""};
			}
			if (isFirstColumn) {
				cell.displayedValue = reportTotals;
			}			
			
			td += "original-title='" + reportTotals + "' data-subtotal='true'";
			auxTd += "<div class='total i18n'>" + escapeHtml(cell.displayedValue) + "</div>";			
			td += ">";
			td += auxTd + "</td>";
			totalRow += td;
			isFirstColumn = false;
		}
	}
	totalRow += "</tr>";
	return totalRow;
}

function generateContentHtml(page, options) {
	var self = this;
	var content = "<tbody>";
	this.lastHeaderRow = this.headerMatrix.length - 1;
	// Add data rows.
	var dataHtml = generateDataRows(page, options);
	content += dataHtml;

	var totalRow = showRowTotals ? buildTotalsRow(page) : "";

	content += totalRow;
	content += "</tbody>";
	return content;
}

function valueOrNbsp(val) {
	return val ? val : "&nbsp;";
}

function generateDataRows(page, options) {
	var self = this;
	var content = "";
	// Transform the tree data structure to 2d matrix.
	this.numberOfRows = this.getNumberOfRows(page.pageArea);
	if (page.pageArea.children !== null) {
		this.numberOfRows--;
	}
	this.contentMatrix = new Array(this.numberOfRows);
	this.rowHierarchyLevel = new Array(this.numberOfRows);
	for (var i = 0; i < this.numberOfRows; i++) {
		this.contentMatrix[i] = new Array(this.headerMatrix[this.headerMatrix.length - 1].length);
	}
	this.currentContentIndexRow = 0;
	if (page.pageArea.children !== null) {
		for (var i = 0; i < page.pageArea.children.length; i++) {
			extractDataFromTree(page.pageArea.children[i], page.pageArea, 0, i === page.pageArea.children.length - 1, []);
		}
	} else {
		// Usually this is a summary report.
		extractDataFromTree(page.pageArea, page.pageArea, 0, 1, []);
	}

	// AMP-19189 fill the cell containing data with colors
	if (this.draftColumn != null && this.approvalStatusColumn != null) {
		fillContentsWithColors();
	}

	for (var i = 0; i < this.contentMatrix.length; i++) {
		// Each "batch" value is used to show more rows when we scroll down the
		// report.
		if (i % this.rowsFromBatch === 0 && i > 0) {
			Saiku.totalBatches++;
		}

		var applyTotalRowStyle = false;
		var level = this.rowHierarchyLevel[i] + 1; // add 1 to account for totals row
		var row = "<tr class='hidden_row level_" + level + "' name='" + Saiku.totalBatches + "'>";
		for (var j = 0; j < this.contentMatrix[i].length; j++) {
			if (!isHiddenColumn(j)) {
				var cell = "";
				if (j < this.metadataColumns.length) {
					if (this.contentMatrix[i][j].isGrouped === true) {
						continue;
					}
					var group = findGroupVertically(this.contentMatrix, i, j);

					var rowSpan = " rowspan='" + group + "' ";
					var colId = j > 0 ? j - 1 : j;
					var value = this.contentMatrix[i][j].displayedValue;
					var totalValue = this.contentMatrix[i][colId].displayedValue;
					var cleanValue = splitText(value, 60);
					var cleanTotalValue = splitText(totalValue, 60);
					
					var styleClass = getCellDataStyleClass(contentMatrix, cleanValue, i, j);
					var coloredPrefix = "";

					if (cellContainsAsterisk(contentMatrix, i, j)) {
						coloredPrefix = "*";
					}

					// Ignore subtotal rows text and change style.
					if (this.contentMatrix[i][j].isTotal === true) {
						if (applyTotalRowStyle === false && cleanTotalValue.text.length > 0) {
							// This flag indicates in which column we start applying the total style.
							applyTotalRowStyle = true;
						}
						// Apply the special style for subtotal rows but starting in the right column index.
						if (applyTotalRowStyle === true) {
							// Trying something new here: show tooltip on the now empty "Hierarchy Value Totals" row.
							if (cleanTotalValue.text !== undefined) {
								styleClass = " class='row_total tooltipped i18n' data-subtotal='true' original-title='" + escapeQuotes(escapeHtml(cleanTotalValue.text)) + "' ";
							} else {
								styleClass = " class='row_total' ";
							}
						} else {
							styleClass = " class='row' ";
						}
						//cleanValue.text = '';
					}

					cell = "<th" + styleClass + rowSpan + ">";
					cell += coloredPrefix + valueOrNbsp(escapeHtml(cleanValue.text));
					cell += "</th>";
				} else {
					// Change amount styles if is a subtotal.
					if (this.contentMatrix[i][j] != null && this.contentMatrix[i][j].isTotal === true) {
						cell = "<td class='data total'>";
						cell += "<div class='total'>" + escapeHtml(this.contentMatrix[i][j].displayedValue) + "</div>";
						cell += "</td>";
					} else {
						cell = "<td class='data'>";
						// Special case we receive the word "constant" from the endpoint (summarized reports).
						if (this.summarizedReport === true && i === 0 && j === 0) {
							cell += options.reportTotalsString;
						} else {
							var auxNonTotalVal = this.contentMatrix[i][j] === null ? null : this.contentMatrix[i][j].displayedValue;
							if (auxNonTotalVal === null) {
								auxNonTotalVal = this.headerMatrix[this.lastHeaderRow][j].emptyCell.displayedValue;
							}
							cell += escapeHtml(auxNonTotalVal);							
						}
						cell += "</td>";
					}
				}
				row += cell;
			}
		}
		row += "</tr>";
		content += row;
	}
	return content;
}

function getCellDataStyleClass(contentMatrix, cleanValue, i, j) {
	var styleClass = " class='row " + contentMatrix[i][j].formatType + " ";
		
	if (contentMatrix[i][j].color) {
		styleClass += this.contentMatrix[i][j].color + " ";
	}

	if (j < this.metadataHierarchies.length) {
		styleClass += "hier_level_" + (j+1) + " ";
	}
	
	if (cleanValue.tooltip) {
		styleClass += "tooltipped' original-title='" + escapeQuotes(escapeHtml(cleanValue.tooltip)) + "' ";
	} else {
		styleClass += "'";
	}
	
	return styleClass;
}

function escapeQuotes(s) {
	return s.replace(/"/g, '&quot;')
			.replace(/'/g, '&apos;');
}

function cellContainsAsterisk(contentMatrix, i, j) {
	if (contentMatrix[i][j].asteriskPrefix) {
		return contentMatrix[i][j].asteriskPrefix;
	}
	
	return false;
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
			if (k > 0 && matchesPreviousRow(matrix, k, j)) {
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
 * Returns true if previous row (i-1) matches current row (i) for all columns
 * starting at 0 and ending at jmax inclusive.
 */
function matchesPreviousRow(matrix, i, jmax) {
	for (j = 0; j <= jmax; j++) {
		if (matrix[i][j].displayedValue !== matrix[i-1][j].displayedValue) {
			return false;
		}
	}
	return true;
}

/**
 * Convert data tree to 2d matrix structure.
 */
function extractDataFromTree(node, parentNode, level, isLastSubNode, hierarchiesData) {
	if (node.children === null || node.children === undefined) {
		// Add this node to contentMatrix, the order of the cells is defined by
		// the header's last row.
		for (var i = 0; i < this.headerMatrix[this.lastHeaderRow].length; i++) {
			var dataValue = node.contents[this.headerMatrix[this.lastHeaderRow][i].hierarchicalName];
			// Save isTotal flag.
			if (dataValue === null || dataValue === undefined) {				
				 dataValue = {displayedValue: "", formatType: "text"};				
			}
			if (dataValue.displayedValue === "" && i < level) {
				dataValue.displayedValue = hierarchiesData[i].displayedValue;
			}
			if(dataValue.displayedValue  === "" || dataValue.displayedValue === null){
				dataValue.displayedValue = this.headerMatrix[this.lastHeaderRow][i].emptyCell ? this.headerMatrix[this.lastHeaderRow][i].emptyCell.displayedValue : "";
			}
			
			dataValue.isTotal = node.isTotal;
			dataValue.formatType = this.headerMatrix[this.lastHeaderRow][i].formatType;

			if (this.contentMatrix[this.currentContentIndexRow]) {
                this.contentMatrix[this.currentContentIndexRow][i] = dataValue;
                this.rowHierarchyLevel[this.currentContentIndexRow] = level;
            }
		}
		this.currentContentIndexRow++;
	} else {
		var colName = this.headerMatrix[this.lastHeaderRow][level].hierarchicalName;
		hierarchiesData[level] = node.contents[colName];
		for (var i = 0; i < node.children.length; i++) {
			extractDataFromTree(node.children[i], node, level + 1, i === node.children.length - 1, hierarchiesData);
		}

        // Add the node that represents the subtotal.
        node.children = null;
        node.isTotal = true;
        if (isLastSubNode) {
            node.contents[colName].isGrouped = true;
        }
        extractDataFromTree(node, parentNode, level, isLastSubNode, hierarchiesData);

	}
}

/**
 * Return the number of rows (adding the total rows per category).
 */
function getNumberOfRows(node) {
	var c = 1;
	if (node.children !== null) {
		for (var i = 0; i < node.children.length; i++) {
			c += getNumberOfRows(node.children[i]);
		}
	}

	return c;
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
 * taken from http://shebang.brandonmintern.com/foolproof-html-escaping-in-javascript/
 */
function escapeHtml(str) {
    var div = document.createElement('div');
    div.appendChild(document.createTextNode(str));
    return div.innerHTML;
};

/**
 * generates a tooltip if needed && requested
 */
function splitText(text, maxLength) {
	var tooltip = undefined;
	if (maxLength !== undefined) {
		if (text.length > maxLength) {
			tooltip = text;
			text = text.substring(0, maxLength) + "...";
		}
	}
	return {
		text : text,
		tooltip : tooltip
	}
}

/**
 * @DEPRECATED - DO NOT USE
 * Remove characters that would break the html, shorten if is larger than 60
 * chars and generate a tooltip text if needed.
 */
function cleanText(text, maxLength) {
	var escapedText = text.replace(/<(?:.|\n)*?>/gm, '').replace(/["']/g, "");
	return splitText(escapedText, maxLength);
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

/**
 * This method will be used only for HTML output (never from Rhino). It adds
 * tooltips for all subtotal row's cell, not only for the first one.
 */
AMPTableRenderer.prototype.postProcessTooltips = function() {
	$(".tooltipped").tipsy();
}

/**
 * Populate cell with colors based on values of 'Draft' and 'Approval Status' hidden columns 
 * validated, unvalidated, draft, pledge
 * 
 * New Draft (*) => draft === 'true' && (aproval_status != 1 || approval_status != 2)
 * Existing Draft => draft === 'true' && (aproval_status === 1 || approval_status === 2)
 * Started Validated (*) => draft === 'false' && aproval_status === 3 => started validated
 * New Un-validated (*) => draft === 'false' && aproval_status === 4 => new unvalidated
 * 
 * More details in org.dgfoundation.amp.ar.AmpArFilter.buildApprovalStatusQuery()
 * 
 * 1 - Approved (validated)
 * 2 - Edited (unvalidated) 
 * 3 - Started approved (validated)
 * 4 - Started (unvalidated) 
 * 5 - Not Approved (unvalidated)
 * 6 - Rejected (unvalidated)
 * More details in org.dgfoundation.amp.ar.AmpArFilter.activityStatusToNr
 */
function fillContentsWithColors() {
	for (var i = 0; i < this.contentMatrix.length; i++) {
		for (var j = 0; j < this.contentMatrix[i].length; j++) {
			if (!this.contentMatrix[i][j].isTotal && isColoredColumn(j)) {
				var draftValue = this.contentMatrix[i][draftColumn].displayedValue;
				var statusValue = parseInt(this.contentMatrix[i][approvalStatusColumn].displayedValue);
				var activityIdValue = parseInt(this.contentMatrix[i][j].entityId);
				
				var color = undefined;
				var asteriskPrefix = false;
				
				if (isActivityPledge(activityIdValue)) {
					color = 'pledge';
				} else if (draftValue === 'true') {
					color = 'draft';
					if (statusValue != '1' && statusValue != '2') {
						asteriskPrefix = true;
					}
				} else if (this.ACTIVITY_STATUS_CODES.validated.indexOf(statusValue) > -1) {
					color = 'validated'
				} else if (this.ACTIVITY_STATUS_CODES.unvalidated.indexOf(statusValue) > -1) {
					color = 'unvalidated';
					if (statusValue === '4') {
						asteriskPrefix = true;
					}
				}
				
				if (color) {
					this.contentMatrix[i][j].color = "activity_status_" + color;
				}
				
				this.contentMatrix[i][j].asteriskPrefix = asteriskPrefix;
			}
		}
	}
}

function getIndexOfColumn(columnName) {
	var i = this.headerMatrix.length-1;
	for (var j=0; j<this.headerMatrix[i].length; j++) {
		if (this.headerMatrix[i][j] && columnName === this.headerMatrix[i][j].originalColumnName) {
			return j;
		}
	}
}

function isColoredColumn(i) {
	var columnName = this.headerMatrix[this.lastHeaderRow][i].originalColumnName;
	
	if (columnName === "Project Title" || columnName === "AMP ID" || columnName === "Pledges Titles")
		return true;
		
	return false;
}

function isHiddenColumn(i) {
	return hiddenColumns && hiddenColumns.indexOf(i) > -1;
}

function isHiddenColumnByName(columnName) {
	return hiddenColumnNames && hiddenColumnNames.indexOf(columnName) > -1;
}

function isActivityPledge(activityIdValue) {
	var idNumber = parseInt(activityIdValue, 10);
	
	return idNumber && idNumber > this.PLEDGE_ID_ADDER;
}