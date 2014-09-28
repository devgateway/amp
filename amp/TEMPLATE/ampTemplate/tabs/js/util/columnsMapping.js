define(function() {

	"use strict";

	// TODO: We need to receive the same column name from both endpoints!!!
	// We can define here some properties like min-width, formats, etc.
	var map = new Object();
	map['[Project Title]'] = 'Project Title';
	map['[Region Name]'] = 'Region';
	map['[Total Measures][Actual Commitments]'] = 'Actual Commitments';
	map['[Actual Commitments]'] = 'Actual Commitments';
	map['[Planned Commitments]'] = 'Planned Commitments';
	map['[Total Measures][Actual Disbursements]'] = 'Actual Disbursements';
	map['[Actual Disbursements]'] = 'Actual Disbursements';
	map['[AMP ID]'] = 'AMP ID';
	map['[Level 0 Sector]'] = 'Primary Sector';
	map['[Level 1 Sector]'] = 'Primary Sector Sub-Sector';
	map['[Level 2 Sector]'] = 'Primary Sector Sub-Sub-Sector';
	map['[Donor Agency]'] = 'Donor Agency';

	function ColumnsMapping() {
		if (!(this instanceof ColumnsMapping)) {
			throw new TypeError("ColumnsMapping constructor cannot be called as a function.");
		}
	}

	ColumnsMapping.getMap = function() {
		return map;
	};

	ColumnsMapping.createJQGridColumnNames = function(metadata, isGrouped) {
		var ret = [];
		ret.push(''); // edit icon column.
		$(metadata.columns.models).each(function(i, item) {
			var colName = item.get('columnName');
			var auxColName = "";
			// If we are using grouping then change the title of the first
			// non-grouped column.
			if ((metadata.columns.models.length - 1) == i && isGrouped) {
				$(metadata.hierarchies.models).each(function(j, item2) {
					auxColName += item2.get('columnName') + ' / ';
				});
				ret.push(auxColName + colName);
			} else {
				ret.push(colName);
			}
		});
		$(metadata.measures.models).each(function(i, item) {
			ret.push(item.get('measureName'));
		});
		console.log(ret);
		return ret;
	};

	ColumnsMapping.createJQGridColumnModel = function(metadata) {
		var ret = [];
		ret.push({
			name : 'editColumn',
			width : 5,
			sortable : false,
			formatter : function() {
				return "<img src='/TEMPLATE/ampTemplate/img_2/ico_edit.gif'/>";
			}
		});
		$(metadata.columns.models).each(function(i, item) {
			ret.push({
				name : item.get('columnName')
			});
		});
		/*
		 * $(metadata.hierarchies.models).each(function(i, item) { ret.push({
		 * name : item.get('columnName') }); });
		 */
		$(metadata.measures.models).each(function(i, item) {
			ret.push({
				name : item.get('measureName'),
				width : 32
			});
		});
		console.log(ret);
		return ret;
	};

	ColumnsMapping.createJQGridGroupingModel = function(metadata, grouping) {
		if (grouping) {
			var ret = {};
			var fields = [];
			var hiddenFields = [];
			var styleText = [];
			// var summary = [];
			$(metadata.hierarchies.models).each(function(i, item) {
				fields.push(item.get('columnName'));
				hiddenFields.push(false);
				styleText.push('<b>{0} - ({1})</b>');
				// summary.push(true);
			});
			ret.groupField = fields;
			ret.groupColumnShow = hiddenFields;
			ret.groupText = styleText;
			// ret.groupSummary = summary;
			console.log(ret);
			return ret;
		} else {
			return {};
		}
	};

	ColumnsMapping.prototype = {
		constructor : ColumnsMapping
	};

	return ColumnsMapping;
});