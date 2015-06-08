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
function sortColumn(id, type) {
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
				columns : [ convertIdToName(id, type) ],
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
				columns : convertIdToName(id, type),
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
				columns : convertIdToName(id, type),
				asc : true,
				id : id
			});
		}
		sort = true;
		break;
	}
	return sort;
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

function convertIdToName(id, type) {
	var name = "";
	switch (type) {
	case 'HEADER_COMMON':
		name = id.replace(/\-/g, '');
		break;
	case 'HEADER_MEASURE':
		// TODO: Replace 'Total Measures' with a value from the endpoint.
		name = id.replace("-Total Measures-", '');
		name = name.replace(/\--/g, ',');
		name = name.replace(/\-/g, '');
		name = name.split(",");
		break;
	case 'HEADER_HIERARCHY':
		name = id.replace(/\--/g, ',');
		name = name.replace(/\-/g, '');
		name = name.split(",");
		break;
	}
	return name;
}