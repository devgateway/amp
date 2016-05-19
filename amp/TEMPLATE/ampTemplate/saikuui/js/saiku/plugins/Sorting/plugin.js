/*
 * This plugin will attach to the existent Saiku instance and provide methods and events to allow users to sort the
 * Results by clicking on the column's header.
 */
Saiku.Sorting = {

	initialize : function(workspace) {
		// For readability purposes lets define in one place all the fields
		// required for Sorting to work.
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
	processClickOnHeader : function(event) {
		var clickedColumn = event.currentTarget;
		var id = $(clickedColumn).attr('id');
		
		if (Settings.NIREPORT) {
			sortNiReportColumn(id);
			if ($(clickedColumn).attr('sorting')) {
				runQuery();
			} else {
				event.preventDefault();
			}
		} else {
			var type = $(clickedColumn).data('sorting-type');

			switch (type) {
			case 'HEADER_COMMON':
				if (sortColumn(id, type) === true) {
					runQuery();
				} else {
					event.preventDefault();
				}
				break;
			case 'HEADER_MEASURE':
				if (sortColumn(id, type) === true) {
					runQuery();
				} else {
					event.preventDefault();
				}
				break;
			case 'HEADER_HIERARCHY':
				if (sortColumn(id, type) === true) {
					runQuery();
				} else {
					event.preventDefault();
				}
				break;
			}
		}
	},

};

// TODO: improve this section.
Saiku.events.bind('session:new', function(session) {
	function new_workspace(args) {
		if (typeof Saiku.Sorting.initialized == "undefined"
				|| Saiku.Sorting.initialized != true) {
			Saiku.Sorting.initialize(args.workspace);
		}
		args.workspace.bind('saikuTableRender:tableRenderedInDOM', function(
				args) {
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

function resetSorting() {
	Saiku.Sorting.currentSorting = [];
}

/*
 * Sort by this column and check if we need to reset the list of sortings or
 * maintain a list. In some cases we need to send more than one column (ie: when
 * clicking yearly totals).
 */
function sortColumn(id, name, type) {
	var sort = false;
	// Look for this id in the list of currentSorting, if we find it then change
	// the 'asc' param, otherwise
	// cleanup currentSorting and add this new column.
	var foundItem = _.find(Saiku.Sorting.currentSorting, function(item) {
		return item.id === id;
	});
	switch (type) {
	case 'HEADER_COMMON':
		if (foundItem !== undefined) {
			foundItem.asc = !foundItem.asc;
			resetSorting();
			Saiku.Sorting.currentSorting.push(foundItem);
		} else {
			resetSorting();
			Saiku.Sorting.currentSorting.push({
				columns : [ convertIdToName(name, type) ],
				asc : true,
				id : id
			});
		}
		sort = true;
		break;
	case 'HEADER_MEASURE':
		if (foundItem !== undefined) {
			foundItem.asc = !foundItem.asc;
			resetSorting();
			Saiku.Sorting.currentSorting.push(foundItem);
		} else {
			resetSorting();
			Saiku.Sorting.currentSorting.push({
				columns : convertIdToName(name, type),
				asc : true,
				id : id
			});
		}
		sort = true;
		break;
	case 'HEADER_HIERARCHY':
		if (foundItem !== undefined) {
			foundItem.asc = !foundItem.asc;
			resetSorting();
			Saiku.Sorting.currentSorting.push(foundItem);
		} else {
			resetSorting();
			Saiku.Sorting.currentSorting.push({
				columns : convertIdToName(name, type),
				asc : true,
				id : id
			});
		}
		sort = true;
		break;
	}
	return sort;
}

function sortNiReportColumn(id) {
	var foundItem = _.find(Saiku.Sorting.currentSorting, function(item) {
		return item.id === id;
	});
	
	if (foundItem !== undefined) {
		foundItem.asc = !foundItem.asc;
		resetSorting();
		Saiku.Sorting.currentSorting.push(foundItem);
	} else {
		resetSorting();
		Saiku.Sorting.currentSorting.push({
			columns : convertNiReportIdToName(id),
			asc : true,
			id : id
		});
	}
}


/*
 * Call the event that refresh the table.
 */
function runQuery() {
	Saiku.Sorting.workspace.run_query();
}

/*
 * Based on the list of current sortings, put the right arrow icons on each
 * header.
 */
function updateArrowIcon(data) {
	$('#sorting-arrow').remove();
	_
			.each(
					Saiku.Sorting.currentSorting,
					function(item) {
						var col = $("[id='" + item.id + "']");
						var imgHtml = "";
						if (item.asc) {
							imgHtml = "<img id='sorting-arrow' src='/TEMPLATE/ampTemplate/images/up.gif' />";
						} else {
							imgHtml = "<img id='sorting-arrow' src='/TEMPLATE/ampTemplate/images/down.gif' />";
						}
						$(col).find("div").append(imgHtml);
					});
}

function convertNiReportIdToName(id) {
	var name = "";
	name = id.replace("[Funding]", '');
	name = name.replace("[Totals]", '');
	
	return name.match(/[^[]+(?=\])/g);
}

function convertIdToName(id, type) {
	var name = "";
	switch (type) {
	case 'HEADER_COMMON':
		// Delete first '[' and last ']'
		name = name.replace(/^\[|]$/g, '');
		break;
	case 'HEADER_MEASURE':
		// TODO: Replace 'Total Measures' with a value from the endpoint.
		name = name.replace("[Total Measures]", '');
		// Delete first '[' and last ']' 
		name = name.replace(/^\[|]$/g, '');
		name = name.split("][");
		// Workaround for FF columns: Remove these elements so the backend can sort properly. The strings where taken from CategAmountCell.java
		name = _.without(name, _.findWhere(name, ' '));
		name = _.without(name, _.find(name, function(item) {return item.indexOf('DN') !== -1}));
		name = _.without(name, _.find(name, function(item) {return item.indexOf('EXEC') !== -1}));
		name = _.without(name, _.find(name, function(item) {return item.indexOf('IMPL') !== -1}));
		name = _.without(name, _.find(name, function(item) {return item.indexOf('BENF') !== -1}));
		break;
	case 'HEADER_HIERARCHY':
		name = name.replace(/^\[|]$/g, '');
		name = name.split("][");
		// Workaround for FF columns: Remove these elements so the backend can sort properly.
		name = _.without(name, _.findWhere(name, ' '));
		break;
	}
	return name;
}