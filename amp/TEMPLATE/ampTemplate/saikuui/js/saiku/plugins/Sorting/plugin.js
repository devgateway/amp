/*
 * This plugin will attach to the existent Saiku instance and provide methods and events to allow users to sort the
 * Results by clicking on the column's header.
 */
Saiku.Sorting = {

	initialize : function(workspace) {
		// For readability purposes lets define in one place all the fields required for Sorting to work.
		Saiku.Sorting.initialized = true;
		// Saiku.Sorting.hasHierarchies = false;
		Saiku.Sorting.notGroupedSortedColumns = [];
		Saiku.Sorting.groupedSortedColumns = [];
		Saiku.Sorting.sortingPopup = undefined;
		Saiku.Sorting.currentTableData = null;
		Saiku.Sorting.currentSorting = [];
		Saiku.Sorting.workspace = workspace;
	},

	/*
	 * One place to intercept clicking on report headers.
	 */
	processClickOnHeader : function(event, type) {
		var clickedColumn = event.currentTarget;
		var columnName = $(clickedColumn).data('level');

		// Reprocess the columnName in specific cases:
		if (columnName.indexOf("Category Name") > -1) {
			columnName = $(clickedColumn).data('dimension');
		} else {
			columnName = getFinalPartOfName(columnName);
		}

		var id = $(clickedColumn).attr('id');
		console.log(columnName + type + id);

		if (type === 'HEADER_CELL') {
			// Check if we clicked a non-hierarchical column like 'Project Title'.
			var auxColumn = _.find(getSortingColumns(), function(item) {
				return item.columnName === columnName;
			});
			// Check if we clicked a hierarchical column like 'Primary Sector'.
			if (auxColumn == undefined) {
				auxColumn = _.find(getSortingHierarchies(), function(item) {
					// return item.propertiesFromResult.level.indexOf(columnName) > -1;
					return item.entityName;
				});
			}
			if (auxColumn != undefined) {
				auxColumn = new ColumnModel({
					entityName : auxColumn.entityName,
					columnName : auxColumn.columnName,
					type : type
				});
				sortColumn(columnName, id, type);
				runQuery();
			}
			console.log(auxColumn);
		} else if (type === 'HEADER_CELL_MEASURE') {
			columnName = $(clickedColumn).data('uniquename');
			sortColumn(columnName, id, type);
			runQuery();
		} else {
			console.error('Invalid type parameter');
		}
	},

};

// TODO: improve this section.
Saiku.events.bind('session:new', function(session) {
	function new_workspace(args) {
		if (typeof Saiku.Sorting.initialized == "undefined" || Saiku.Sorting.initialized != true) {
			Saiku.Sorting.initialize(args.workspace);
		}

		args.workspace.bind('query:result', function(args) {
			if ($('#saiku_sort_button').length == 0) {
				addSortButton();
				args.workspace.toolbar.$el.find(".amp_sorting").removeClass("disabled_toolbar");
			}
		});

		args.workspace.bind('saikuTableRender:tableRenderedInDOM', function(args) {
			updateArrowIcon(args.data);
		});
	}

	function clear_workspace(args) {
		if (typeof Saiku.Sorting.initialized != "undefined") {
			if (args.workspace.amp_sorting != undefined) {
				$(args.workspace.amp_sorting.el).hide();
			}
		}
	}

	Saiku.session.bind("workspace:new", new_workspace);
	Saiku.session.bind("workspace:clear", clear_workspace);
});

/**
 * Add the sorting button in toolbar (only if the report has hierarchies).
 */
function addSortButton() {
	if (getSortingHierarchies().length > 0) {
		var sortButton = $(
				'<a href="#" id="saiku_sort_button" class="amp_sorting button i18n" title="Sorting">Sorting</a>').css({
			'width' : "44px"
		});
		var $sorting_li = $('<li class="seperator"></li>').append(sortButton);
		$(".workspace_toolbar").find("ul").append($sorting_li);
		$("#saiku_sort_button").bind("click", sortingPopup);
	}
}

/*
 * Create and open sorting popup.
 */
function sortingPopup() {
	Saiku.Sorting.sortingPopup = new SortingModal({
		title : "Please select hierarchy sorter criteria",
		position : 10
	});
	Saiku.Sorting.sortingPopup.open();
}

/*
 * This variable defines the popup where we show the sorting options (only for hierarchical reports).
 */
var SortingModal = Modal.extend({
	type : "sorting",

	buttons : [ {
		text : "Ok",
		method : "ok"
	}, {
		text : "Cancel",
		method : "close"
	} ],

	initialize : function(args) {
		_.extend(this, args);
		this.options.title = args.title;
		this.position = args.position;

		this.render();
		$(this.el).find('.dialog_icon').remove();
		$(this.el).find('.dialog_body').empty();

		var sort_template = _.template($("#template-sorting-popup").html())({
			hierarchies : getSortingHierarchies(),
			columns : getSortingColumns()
		});
		$(sort_template).appendTo($(this.el).find('.dialog_body'));

		Saiku.i18n.translate();
	},

	ok : function(event) {
		Saiku.Sorting.sortingPopup.close();
	}

});

var ColumnModel = Backbone.Model.extend({});

function getSortingHierarchies() {
	var auxHierarchies = Saiku.tabs._tabs[0].content.query.get('hierarchies');
	if (auxHierarchies != undefined) {
		if (Saiku.Sorting.currentTableData != null && Saiku.Sorting.currentTableData.cellset != null) {
			if (Saiku.Sorting.currentTableData.cellset[0] != undefined) {
				if (Saiku.Sorting.currentTableData.cellset[0][0].value === 'null') {
					for ( var i = 0; i < auxHierarchies.length; i++) {
						auxHierarchies[i].textValue = Saiku.Sorting.currentTableData.cellset[1][i].value;
						auxHierarchies[i].propertiesFromResult = Saiku.Sorting.currentTableData.cellset[1][i].properties;
						// Saiku.Sorting.hasHierarchies = true;
					}
				}
			}
		}
	} else {
		auxHierarchies = [];
	}
	return auxHierarchies;
}

/*
 * Return a list of all non hierarchical columns we could use for sorting.
 */
function getSortingColumns() {
	var columns = [];
	for ( var i = getSortingHierarchies().length; i < Saiku.tabs._tabs[0].content.query.get('columns').length; i++) {
		columns.push(Saiku.tabs._tabs[0].content.query.get('columns')[i]);
	}

	// Check if the report has data.
	if (Saiku.Sorting.currentTableData != null && Saiku.Sorting.currentTableData.cellset != null) {
		if (Saiku.Sorting.currentTableData.cellset[0] != undefined) {
			// Some check for data structure.
			if (Saiku.Sorting.currentTableData.cellset[0][0].value === 'null') {
				if (getSortingHierarchies().length > 0) {
					// Ignore the first columns which are hierarchical columns.
					for ( var i = getSortingHierarchies().length + 1; i < Saiku.Sorting.currentTableData.cellset[0].length; i++) {
						var newCol = new ColumnModel();
						var name = Saiku.Sorting.currentTableData.cellset[1][i].value + " - ";
						var name2 = Saiku.Sorting.currentTableData.cellset[0][i].properties.uniquename;
						name2 = name2.substring(name2.lastIndexOf('[') + 1, name2.length - 1);
						newCol.columnName = name + name2;
						newCol.entityName = Saiku.Sorting.currentTableData.cellset[0][i].properties.uniquename;
						newCol.properties = Saiku.Sorting.currentTableData.cellset[0][i].properties;
						columns.push(newCol);
					}
				} else {

				}
			}
		}
	}
	return columns;
}

function resetSorting() {
	Saiku.Sorting.currentSorting = [];
}

/*
 * Sort by this column and check if we need to reset the list of sortings or maintain a list. In some cases we need to
 * send more than one column (ie: when clicking yearly totals).
 */
function sortColumn(columnName, id, type) {
	// Look for this id in the list of currentSorting, if we find it then change the 'asc' param, otherwise
	// cleanup currentSorting and add this new column.
	var foundItem = _.find(Saiku.Sorting.currentSorting, function(item) {
		return item.id === id;
	});
	if (getSortingHierarchies().length == 0) {
		// Regular columns like 'Project Title', etc.
		if (type === 'HEADER_CELL') {
			if (foundItem != null) {
				foundItem.asc = !foundItem.asc;
				resetSorting();
				Saiku.Sorting.currentSorting.push(foundItem);
			} else {
				resetSorting();
				Saiku.Sorting.currentSorting.push({
					columnName : columnName,
					asc : true,
					id : id
				});
			}
		} else if (type === 'HEADER_CELL_MEASURE') {
			// Measure columns like '2010 - Actual Commitments'.
			if (foundItem != null) {
				foundItem.asc = !foundItem.asc;
				resetSorting();
				Saiku.Sorting.currentSorting.push(foundItem);
			} else {
				// Find the related cell immediately below the clicked one.
				// 1) Detect if we clicked on the upper cell (those cells share the same id), if not ignore the click.
				if ($('[id="' + id + '"]').length == 1) {
					resetSorting();
					var relatedBelowCell = $('#' + id).closest('tr').next().children().eq($('#' + id).index());
					var relatedBelowCellName = getFinalPartOfName($(relatedBelowCell).data('uniquename'));
					columnName = getFinalPartOfName(columnName);
					Saiku.Sorting.currentSorting.push({
						columnName : columnName + "," + relatedBelowCellName,
						asc : true,
						id : id
					});
				}
			}
		}
	} else {
		// TODO: Improve this section (is more complex than non-hierarchical).
		// Regular columns like 'Project Title', etc.
		if (type === 'HEADER_CELL') {
			if (foundItem != null) {
				foundItem.asc = !foundItem.asc;
				resetSorting();
				Saiku.Sorting.currentSorting.push(foundItem);
			} else {
				resetSorting();
				var isRegularColumn = _.find(getSortingColumns(), function(item) {
					return item.entityName === columnName;
				});
				if (isRegularColumn != undefined) {
					// In this case we have a hierarchical report but clicked on a regular column.
					Saiku.Sorting.currentSorting.push({
						columnName : columnName,
						asc : true,
						id : id
					});
				} else {
					// In a hierarchical report, if we click on some of the hierarchical headers then we need to send
					// another column for sorting too, in our case the first non-hierarchical column available.
					var hierarchicalColumn = getFinalPartOfName($('#' + id).data('level'));
					var firstRegularColumn = getSortingColumns()[0].entityName;
					Saiku.Sorting.currentSorting.push({
						id : id,
						columnName : hierarchicalColumn/* + "," + firstRegularColumn*/,
						asc : true
					});
				}
			}
		} else if (type === 'HEADER_CELL_MEASURE') {
			// Measure columns like '2010 - Actual Commitments'.
			if (foundItem != null) {
				foundItem.asc = !foundItem.asc;
				resetSorting();
				Saiku.Sorting.currentSorting.push(foundItem);
			} else {
				// Find the related cell immediately below the clicked one.
				// 1) Detect if we clicked on the upper cell (those cells share the same id), if not ignore the click.
				if ($('[id="' + id + '"]').length == 1) {
					resetSorting();
					var relatedBelowCell = $('#' + id).closest('tr').next().children().eq($('#' + id).index());
					var relatedBelowCellName = getFinalPartOfName($(relatedBelowCell).data('uniquename'));
					columnName = getFinalPartOfName(columnName);
					Saiku.Sorting.currentSorting.push({
						columnName : columnName + "," + relatedBelowCellName,
						asc : true,
						id : id
					});
				}
			}
		}
	}
}

/*
 * Call the event that refresh the table.
 */
function runQuery() {
	Saiku.Sorting.workspace.run_query();
}

/*
 * Based on the list of current sortings, put the right arrow icons on each header.
 */
function updateArrowIcon(data) {
	// alert('updateArrowIcon');
	$('#sorting-arrow').remove();
	_.each(Saiku.Sorting.currentSorting, function(item) {
		var col = $("#" + item.id);
		var imgHtml = "";
		if (item.asc) {
			imgHtml = "<img id='sorting-arrow' src='/TEMPLATE/ampTemplate/images/up.gif' />";
		} else {
			imgHtml = "<img id='sorting-arrow' src='/TEMPLATE/ampTemplate/images/down.gif' />";
		}
		$(col).find("div").append(imgHtml);
	});
}

function getFinalPartOfName(name) {
	if (name != undefined && name != null) {
		return name.substring(name.lastIndexOf('[') + 1, name.length - 1);
	} else {
		return "";
	}
}