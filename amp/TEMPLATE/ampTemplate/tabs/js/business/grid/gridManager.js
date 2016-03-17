define([ 'business/grid/columnsMapping', 'business/translations/translationManager', 'util/tabUtils' ], function(columnsMapping,
		TranslationManager, TabUtils) {

	"use strict";

	var gridBaseName = 'tab_grid_';
	var gridPagerBaseName = 'tab_grid_pager_';
	var partialTotals = null;

	// This variable will contain the mappings between different column names
	// (tab structure vs report data).
	var headers = [];

	function GridManager() {
		if (!(this instanceof GridManager)) {
			throw new TypeError("GridManager constructor cannot be called as a function.");
		}
	}

	function getURL(id) {
		// NIREPORTS: use nireport flag as needed. When ready, notify to finish full migration from backend.
		return '/rest/data/report/' + id + '/result/jqGrid?nireport=true';
	}

	GridManager.prototype = {
		constructor : GridManager
	};

	/**
	 * Apply filters and refresh the grid.
	 */
	GridManager.filter = function(id, jsonFilters, settings) {
		var grid = jQuery("#" + gridBaseName + id);
		jQuery(grid).jqGrid('clearGridData');
		jQuery(grid).jqGrid('setGridParam', {
			url : getURL(id)
		});

		// Do this or new filters will be merged with old postData.
		jQuery(grid).jqGrid('setGridParam', {
			postData : null
		});

		// TODO: we should also configure here "add_columns", etc as part of the client request, not on the backend
		var data = {
			page : 1,
			regenerate : true,
			columns_with_ids : app.TabsApp.COLUMNS_WITH_IDS,
			filters : jsonFilters,
			settings : settings
		};
		if (app.TabsApp.currentSorting !== undefined) {
			data.sidx = app.TabsApp.currentSorting.sidx;
			data.sord = app.TabsApp.currentSorting.sord;
		}
		jQuery(grid).jqGrid('setGridParam', {
			postData : data
		});
		jQuery(grid).jqGrid().trigger('reloadGrid', [ {
			page : 1
		} ]);

	};

	GridManager.populateGrid = function(id, dynamicLayoutView, firstContent) {
		var TableSectionView = Marionette.ItemView.extend({
			template : '#grid-template'
		});
		var tableSectionView = new TableSectionView();
		dynamicLayoutView.results.show(tableSectionView);

		// Define grid structure.
		var tableStructure = extractMetadata(firstContent);
		var grouping = (tableStructure.hierarchies.length > 0) ? true : false;
		var grid = jQuery("#tab_grid");
		jQuery(grid).attr("id", gridBaseName + id);
		var pager = jQuery("#tab_grid_pager");
		jQuery(pager).attr("id", gridPagerBaseName + id);

		// IMPORTANT NOTE: We need to call jqgrid this way because using the
		// standar require way will generate side effects like grouping buttons
		// stop working, the drawback is the client can not cache the jqgrid
		// library.
		var cacheEnabled = (/MSIE \d\d*/i.test(navigator.userAgent) || /rv:11.0/i.test(navigator.userAgent))? false: true;
		jQuery.ajaxSetup({
			cache : cacheEnabled
		});
		jQuery.getScript("/TEMPLATE/ampTemplate/tabs/js/lib/one_place/jqgrid-all.js", function(data, textStatus, jqxhr) {
			var rowNum = 0;
			var colModel = columnsMapping.createJQGridColumnModel(tableStructure);
			var grandTotals = null;
			var numberOfPages = null;
			jQuery(grid).jqGrid(
					{
						caption : false,
						/* url : '/rest/data/report/' + id + '/result/', */
						url : getURL(id),
						datatype : 'json',
						mtype : 'POST',
						ajaxGridOptions : {
							contentType : 'application/json; charset=utf-8'
						},
						postData : {
							page : 1,
							regenerate : true,
							columns_with_ids : app.TabsApp.COLUMNS_WITH_IDS,
							filters : null
						},
						serializeGridData : function(postData) {
							return JSON.stringify(postData);
						},
						jsonReader : {
							repeatitems : false,
							root : function(obj) {
								// rowNum = obj.recordsPerPage;
								jQuery(grid).jqGrid('setGridParam', {
									rowNum : obj.page.recordsPerPage
								});
								grandTotals = extractGrandTotals(obj, colModel);
								return transformData(obj, grouping, tableStructure.hierarchies);
							},
							page : function(obj) {
								return obj.page.currentPageNumber;
							},
							total : function(obj) {
								numberOfPages = obj.page.totalPageCount;
								return obj.page.totalPageCount;
							},
							records : function(obj) {
								// console.log(obj);
								return obj.page.totalRecords;
							}
						},
						sortname : (app.TabsApp.currentSorting.sidx !== undefined ? app.TabsApp.currentSorting.sidx : ""),
						sortorder : (app.TabsApp.currentSorting.sord !== undefined ? app.TabsApp.currentSorting.sord : "asc"),
						colNames : columnsMapping.createJQGridColumnNames(tableStructure, grouping),
						colModel : colModel,
						height : (jQuery(window).height() * 0.50),
						autowidth : true,
						shrinkToFit : true,
						forceFit : false,
						viewrecords : true,
						loadtext : "<span data-i18n='tabs.common:loading'>Loading...</span>",
						headertitles : true,
						gridview : true,
						rownumbers : false,
						rowNum : rowNum,
						pager : "#" + gridPagerBaseName + id,
						emptyrecords : "<span data-i18n='tabs.common:noRecordsToView'>No records to view</span>",
						grouping : grouping,
						groupingView : columnsMapping.createJQGridGroupingModel(tableStructure, grouping),
						footerrow : true,
						loadBeforeSend : function(xhr, settings) {
							TranslationManager.searchAndTranslate();
						},
						gridComplete : function() {
							// Save current sorting settings in case we save the tab later.
							app.TabsApp.currentTab.set('currentSorting', {
								sidx: grid.getGridParam("postData").sidx, 
								sord: grid.getGridParam("postData").sord
							});
							
							// background colors.
							jQuery(grid).find(">tbody>tr.jqgrow:odd").addClass("myAltRowClassEven");
							jQuery(grid).find(">tbody>tr.jqgrow:even").addClass("myAltRowClassOdd");

							// Change row color depending the status.
							var cRows = this.rows.length, iRow, row, className;
							
							var teamid;
							var crossTeamValidation;
							var teamlead;
							var validator;
							var teamtype;
							var onePagerParameter = app.TabsApp.DEFAULT_ONE_PAGER_PARAMETER;

							if(app.TabsApp.settings.teamId){
								teamid = app.TabsApp.settings.teamId;
								crossTeamValidation = (app.TabsApp.settings.crossTeamEnable === 'true');
								teamlead = (app.TabsApp.settings.teamLead === 'true');
								validator = (app.TabsApp.settings.validator === 'true');
								teamtype = app.TabsApp.settings.accessType;
							}
							if(app.TabsApp.settings.workspacePrefix && app.TabsApp.settings.workspacePrefix.defaultId ){
								onePagerParameter = app.TabsApp.settings.workspacePrefix.defaultId.toLowerCase();
							}

							for (iRow = 0; iRow < cRows; iRow++) {
								row = this.rows[iRow];
								className = row.className;
								
								// Ignore grouped rows.
								if (!(jQuery.inArray('jqgrow', className.split(' ')) > 0)) {
									continue;
								}
								//set default color and link for rows
								row = this.rows[iRow];
								className = row.className;
								var id = row.cells[1].textContent;
								var iconedit = "<a href='/wicket/onepager/"+ onePagerParameter +"/" + id
									+ "'><img src='/TEMPLATE/ampTemplate/tabs/css/images/ico_edit.gif'/></a>";
								var iconvalidated = "<a href='/wicket/onepager/"+ onePagerParameter +"/" + id
									+ "'><img src='/TEMPLATE/ampTemplate/tabs/css/images/validate.png'/></a>";
								var link = "<a href='/wicket/onepager/"+ onePagerParameter +"/" + id + "'>";
								
								
								row.className = className + ' status_1';
								jQuery(row.cells[0]).html(link);
								
								// Shorten texts.
								for (var j = 0; j < colModel.length; j++) {
									var cell = row.cells[j];
									if (jQuery(cell).css('display') !== 'none') {
										cell.textContent = shortenTexts(cell.textContent);
									}
								}
								
								//check public view - no team present
								if(!teamid) continue;
								
								teamid = app.TabsApp.settings.teamId;
								crossTeamValidation = (app.TabsApp.settings.crossTeamEnable === 'true');
								teamlead = (app.TabsApp.settings.teamLead === 'true');
								validator = (app.TabsApp.settings.validator === 'true');
								teamtype = app.TabsApp.settings.accessType;
								
								// Set font color according to status.
								var draft = row.cells[3].textContent;
								var approvalStatus = row.cells[2].textContent;
								var activityteamid = row.cells[4].textContent;
								
								
								// Status Mapping
								var statusMapping = {
									New_Draft : '0',
									New_Unvalidated : '1',
									Existing_Draft : '2',
									Validated_Activities : '3',
									Existing_Unvalidated : '4',
									Approved : '5',
									Rejected : 6
								};

								// Calculated status based on draft and
								// approval status.
								var getApprovalStatus = function(draft, approvalStatus) {
									var retVal ;
									var isNew = false;
									if (draft === 'true') {
										if (approvalStatus === '2' || approvalStatus === '1') {
											retVal = statusMapping.Existing_Draft;
										} else {
											isNew = true;
											retVal = statusMapping.New_Draft;
										}
									} else {
										switch (approvalStatus) {
										case '1':
										case '3':
											retVal = statusMapping.Approved;
											break;
										case '2':
										case '5':
										case '6':
											retVal = statusMapping.Existing_Unvalidated;									
											break;
										case '4':
											isNew = true;
											retVal = statusMapping.New_Unvalidated;
											break;
										default:
											break;
										}
									}
									if(isNew){
										var starColumn = $(row).find(".wrap-title").first();
										if(starColumn.length === 0){
											starColumn = $(row).find(".wrap-cell").not("[style*='display:none']").first();
										}
										starColumn.text('* ' + starColumn.text());
									}
									return retVal;
								};

								// Assign colors for each row for loggued users.
								var x = getApprovalStatus(draft, approvalStatus);
								if (x === statusMapping.Approved) {
									row.className = className + ' status_1';
									// Create link to edit activity.
									if (teamtype !== app.TabsApp.MANAGER_TYPE) {
										jQuery(row.cells[0]).html(iconedit + link);
									} else {
										jQuery(row.cells[0]).html(link);
									}

								} else if (x === statusMapping.Existing_Draft || x === statusMapping.New_Draft) {
									row.className = className + ' status_2';
									jQuery(row.cells[0]).html(iconedit);

								} else if (x === statusMapping.Existing_Unvalidated || x === statusMapping.New_Unvalidated) {
									row.className = className + ' status_3';
									// Cross team enable team lead and validators able to validate show icon.
									if (crossTeamValidation && (teamlead || validator)) {
										if (teamtype !== app.TabsApp.MANAGER_TYPE) {
											jQuery(row.cells[0]).html(iconvalidated);
										} else {
											jQuery(row.cells[0]).html(link);
										}
										// Cross team disable team lead and validators able to validate only
										// if the activity belongs to the workspace.
									} else if (!crossTeamValidation && activityteamid === teamid && (teamlead || validator)) {
										jQuery(row.cells[0]).html(iconvalidated);
									} else {
										jQuery(row.cells[0]).html(iconedit);
									}
								}

								// Create link to preview activity on first not grouped column.
								var colIndex = -1;
								jQuery(jQuery(grid).jqGrid('getGridParam', 'colModel')).each(function(i, item) {
									if (colIndex === -1 && item.hidden === false && i > 0) {
										colIndex = i;
									}
								});
								var newContent = "<span style='cursor: pointer;' onclick = \x22openPreviewPage(" + id + ")\x22>"
										+ jQuery(row.cells[colIndex]).html() + "</span>";
								jQuery(row.cells[colIndex]).html(newContent);
							}
							
							TranslationManager.searchAndTranslate();
						},
						loadComplete : function() {
							// Calculate footer totals row for this page.
							var colData = {};
							var totalColumnIndex = colModel.length - 1;
							var auxI = 0;
							jQuery.each(colModel, function(i, item) {								
								if (item.reportColumnType === app.TabsApp.COLUMN_TYPE_MEASURE) {									
									var sum = null;
									if(numberOfPages === 0) {
										colData[item.name] = "";
									} else if(numberOfPages > 1) {
										// TODO: Replace this js sum with data from the endpoint.
										sum = jQuery(grid).jqGrid('getCol', item.name, false, 'sum');
										colData[item.name] = TabUtils.numberToString(sum, app.TabsApp.numericFormatOptions);
									} else {
										// Save extra time calculating the sum of elements.
										if(grandTotals[auxI] !== undefined) {
											sum = grandTotals[auxI].displayedValue;
											colData[item.name] = sum;
										}
										auxI++;
									}									
									totalColumnIndex--;
								}							
							});
							colData[colModel[totalColumnIndex].name] = TranslationManager.getTranslated('Page Total:');
							jQuery(grid).jqGrid('footerData', 'set', colData);

							// Create an additional footer row for grand-totals
							// (not natively supported by jqgrid).
							jQuery("#grand_total_row_" + id).empty();
							jQuery("#grand_total_row_" + id).remove();
							var pageFooterRow = jQuery("#main-dynamic-content-region_" + id + " .ui-jqgrid-ftable .footrow-ltr");
							var grandTotalFooterRow = jQuery(pageFooterRow).clone();
							jQuery(grandTotalFooterRow).find("[aria-describedby^='tab_grid_" + id + "']").text("").attr("title", "");
							jQuery(grandTotalFooterRow).attr("id", "grand_total_row_" + id);
							var grandTotalText = TranslationManager.getTranslated('Grand Total:');
							jQuery(grandTotalFooterRow).find(
									"[aria-describedby='tab_grid_" + id + "_" + colModel[totalColumnIndex].name + "']")
									.text(grandTotalText).attr("title", grandTotalText);
							jQuery.each(grandTotals, function(i, item) {
								jQuery(grandTotalFooterRow).find("[aria-describedby='tab_grid_" + id + "_" + item.columnName + "']").text(
										item.displayedValue).attr("title", item.displayedValue);
							});
							jQuery(grandTotalFooterRow).insertAfter(pageFooterRow);

							// Add extra info to each grop row created by
							// jqgrid, this info comes from the endpoint.
							if (tableStructure.hierarchies.models.length > 0) {
								var numberOfColumns = tableStructure.columns.models.length - tableStructure.hierarchies.models.length + 1;
								var groupRows = jQuery("tr[id*='tab_grid_" + id + "ghead']");
								jQuery.each(groupRows, function(i, item) {
									jQuery(item.firstChild).attr("colspan", numberOfColumns);
									jQuery.each(tableStructure.measures.models, function(j, measure) {
										var auxTD = jQuery(item.firstChild).clone().html("").attr("colspan", 0).css("text-align", "right");
										var content = partialTotals[i].contents[app.TabsApp.TOTAL_COLUMNS_NAME_SUFIX + "[" + measure.get('measureName') + "]"].displayedValue;
										jQuery(auxTD).html("<span><b>" + content + "</b></span>");
										jQuery(item).append(auxTD);

										var firstColumnHtml = jQuery(item.firstChild).html();
										firstColumnHtml = firstColumnHtml.replace("@@currentActivitiesCount@@",
												partialTotals[i].currentActivitiesCount);
										firstColumnHtml = firstColumnHtml.replace("@@totalActivitiesCount@@",
												partialTotals[i].totalActivitiesCount);
										jQuery(item.firstChild).html(firstColumnHtml);
									});
								});
							}
						}
					});
			app.TabsApp.currentGrid = jQuery(grid);
		});
	};

	function extractMetadata(content) {
		var Metadata = Backbone.DocumentModel.extend({
			defaults : {
				columns : [],
				measures : [],
				hierarchies : []
			}
		});
		var metadata = new Metadata();
		var metadataJson = content.get('reportMetadata').get('reportSpec');
		metadata.columns = metadataJson.get('columns');
		metadata.hierarchies = metadataJson.get('hierarchies');
		metadata.measures = metadataJson.get('measures');
		metadata.projectTitleColumn = metadataJson.get('projectTitleColumn'); //TODO: do we still need it?
		return metadata;
	}

	/*
	 * Before trying to render the data from server we need to make some
	 * transformations and cleanups.
	 */
	function transformData(data, grouping, hierarchies) {
		var rows = [];
		partialTotals = [];
		// Process the headers for later usage.
		if (data.headers !== null) {
			jQuery.each(data.headers, function(i, item) {
				headers.push({
					columnName : item["columnName"],
					originalColumnName : item["originalColumnName"],
					hierarchicalName : item["hierarchicalName"]
				});
			});

			// If the tab has hierarchies we use this auxiliary variable to hold the current hierarchy value on each iteration of the recursive function at any level of depth.
			var auxCurrentHierarchiesValues = [];
			for(var k = 0; k < hierarchies.models.length; k++) {
				auxCurrentHierarchiesValues[hierarchies.models[k].get('columnName')] = "FAKE";
			}
			getContentRecursively(data.page.pageArea, rows, null, partialTotals, -1, hierarchies, auxCurrentHierarchiesValues);
		}
		//console.log(rows);
		//console.log(partialTotals);
		return rows;
	}

	function findInMapByColumnName(name, property) {
		var ret = undefined;
		ret = _.find(headers, function(item) {
			return item[property] === name;
		});
		return ret;
	}

	function getContentRecursively(obj, rows, parent, partialTotals, level, hierarchies, auxCurrentHierarchiesValues) {
		if (obj !== undefined && obj !== null) {
			level++;
			if (obj.children === null || obj.children.length === 0) {
				// This is the leaf with content we want.
				var row = {
					id : 0
				};
				jQuery.each(obj.contents, function(key, element) {
					var colName = null;
					var auxColumn = findInMapByColumnName(key, 'hierarchicalName'); 
					if (auxColumn !== undefined) {
						colName = auxColumn.originalColumnName;
					}
					if (colName !== undefined && colName !== null) {
						if (element !== null && element.displayedValue !== null && element.displayedValue.toString().length > 0) {
							row[colName] = element.displayedValue;
						} else {
							row[colName] = getParentContent(key, parent);
						}
						row['id'] = Math.random();
					}
					// Property entityId replaced column AMP_ID on NiReports.
					if (key === "[" + app.TabsApp.COLUMNS_WITH_IDS[0] + "]" && element.entityId !== undefined) {
						row[app.TabsApp.COLUMN_ACTIVITY_ID] = element.entityId;
					}
				});
				// To flatten the tree structure and maintain hierarchies values on every row we add the values from "auxCurrentHierarchiesValues".
				if (hierarchies.models.length > 0) {
					for (var k = 0; k < hierarchies.models.length; k++) {
						var hierarchyName = hierarchies.models[k].get('columnName');
						row[hierarchyName] = auxCurrentHierarchiesValues[hierarchyName];
					}
				}
				rows.push(row);
			} else {
				// Save all the 'Totals' rows from the endpoint for later
				// replacing the content of jqgrid automatically created grouped
				// rows (yes, it sounds scary).
				if (level > 0) {
					// Ignore top level because is 'Report Totals'.
					partialTotals.push({
						contents : obj.contents,
						totalActivitiesCount : obj.totalLeafActivitiesCount, //obj.totalChildrenCount,
						currentActivitiesCount : obj.currentLeafActivitiesCount, //obj.totalChildrenCount,
						level : level
					});
				}
				
				if (hierarchies.length > 0) {
					if (parent !== undefined && parent !== null) {
						for (var k = 0; k < hierarchies.models.length; k++) {
							var hierarchyName = hierarchies.models[k].get('columnName');
							var auxValue = parent["[" + hierarchyName + "]"].displayedValue;
							if (auxValue !== '') {
								auxValue = parent["[" + hierarchyName + "]"].displayedValue;
								auxCurrentHierarchiesValues[hierarchyName] = auxValue;
							} else {
								// If parent doesnt have information then try with current object.
								auxValue = obj.contents["[" + hierarchyName + "]"].displayedValue;
								if (auxValue !== '') {
									auxCurrentHierarchiesValues[hierarchyName] = auxValue;
								}
							}
						}
					}
				}
				
				jQuery(obj.children).each(function(i, item) {
					getContentRecursively(item, rows, obj.contents, partialTotals, level, hierarchies, auxCurrentHierarchiesValues);				
				});
				level--;
			}
		} else {
			console.log('No data.');
		}
	}

	/*
	 * The endpoint doesnt fill the hierarchical values after the 1st node so we
	 * need to take some values from the parent by recursion.
	 */
	function getParentContent(key, parent) {
		if (parent !== undefined && parent !== null) {
			if (parent[key].displayedValue !== null && parent[key].displayedValue.indexOf(app.TabsApp.TOTAL_COLUMNS_DATA_SUFIX) > 0) {
				return parent[key].displayedValue.substring(0, parent[key].displayedValue.indexOf(app.TabsApp.TOTAL_COLUMNS_DATA_SUFIX));
			} else {
				getParentContent(key, parent.parent);
			}
		} else {
			return 'NULL';
		}
	}

	/*
	 * Extract report grand totals (ignoring pagination) from the endpoint data.
	 */
	function extractGrandTotals(data, colModel) {
		var ret = [];
		jQuery.each(colModel, function(i, item) {
			if (item.reportColumnType === app.TabsApp.COLUMN_TYPE_MEASURE) {
				var col = {};
				col.columnName = item.name;
				if (data.page !== null && data.page.pageArea !== null) {
					try {
						col.value = data.page.pageArea.contents[app.TabsApp.TOTAL_COLUMNS_NAME_SUFIX + "[" + item.name + "]"].displayedValue;
						col.displayedValue = data.page.pageArea.contents[app.TabsApp.TOTAL_COLUMNS_NAME_SUFIX + "[" + item.name + "]"].displayedValue;
						ret.push(col);
					} catch (err) {
						console.error(err + item.name);
					}
				}
			}
		});
		return ret;
	}
	
	function shortenTexts(text) {
		var max = 100;
		if (text !== undefined && text !== null && text.length > max) {
			return text.substring(0, max) + "...";
		}
		return text;
	}

	return GridManager;
});