/*  
 *   Copyright 2012 OSBI Ltd
 *
 *   Licensed under the Apache License, Version 2.0 (the "License");
 *   you may not use this file except in compliance with the License.
 *   You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 *   Unless required by applicable law or agreed to in writing, software
 *   distributed under the License is distributed on an "AS IS" BASIS,
 *   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *   See the License for the specific language governing permissions and
 *   limitations under the License.
 */

/**
 * The analysis workspace
 */
var Workspace = Backbone.View
		.extend({
			className : 'tab_container',

			events : {
				'click .sidebar_separator' : 'toggle_sidebar',
				'change .cubes' : 'new_query',
				'drop .sidebar' : 'remove_dimension',
				'drop .workspace_results' : 'remove_dimension',
				'click .refresh_cubes' : 'refresh',
				'click .cancel' : 'cancel'
			},

			initialize : function(args) {
				// Maintain `this` in jQuery event handlers
				_.bindAll(this, "caption", "adjust", "toggle_sidebar",
						"prepare", "new_query", "init_query", "update_caption",
						"populate_selections", "refresh", "sync_query",
						"cancel", "cancelled", "no_results", "error",
						"switch_view_state");

				// Attach an event bus to the workspace
				_.extend(this, Backbone.Events);
				this.loaded = false;
				this.bind('query:result', this.render_result);

				// Generate toolbar and append to workspace
				this.toolbar = new WorkspaceToolbar({
					workspace : this
				});
				this.toolbar.render();

				this.querytoolbar = new QueryToolbar({
					workspace : this
				});
				this.querytoolbar.render();

				// Generate table
				this.table = new Table({
					workspace : this
				});

				this.chart = {};

				// Pull query from args
				this.item = {};
				this.viewState = (args && args.viewState) ? args.viewState
						: Settings.DEFAULT_VIEW_STATE; // view / edit
				this.isReadOnly = (Settings.MODE == 'view' || false);
				if (args && args.item) {
					this.item = args.item;
					if (this.item && this.item.hasOwnProperty('acl')
							&& _.indexOf(this.item.acl, "WRITE") < 0) {
						this.isReadOnly = true;
						this.viewState = 'view';
					}
				}
				if (!args || (!args.query && !args.viewState)) {
					this.viewState = 'edit';
				}
				if (args && args.query) {
					this.query = args.query;
					this.query.workspace = this;
					this.query
							.save(
									{},
									{
										success : this.init_query,
										error : function() {alert('Error!!!');}
									});
				}

				// Flash cube navigation when rendered
				Saiku.session.bind('tab:add', this.prepare);
			},

			caption : function(increment) {
				if (this.query && this.query.model) {
					if (this.query.model.mdx)
						return this.query.model.name;
					else if (this.query.get('name'))
						return this.query.get('name');

				} else if (this.query && this.query.get('name')) {
					return this.query.get('name');
				}
				if (increment) {
					Saiku.tabs.queryCount++;
				}
				return "<span class='i18n'>Unsaved query</span> ("
						+ (Saiku.tabs.queryCount) + ")";
			},

			template : function() {
				var template = $("#template-workspace").html() || "";
				return _
						.template(template)
						(
								{
									cube_navigation : Saiku.session.sessionworkspace.cube_navigation
								});
			},

			refresh : function(e) {
				if (e) {
					e.preventDefault();
				}
				;
				Saiku.session.sessionworkspace.refresh();
			},

			render : function() {
				// Load template
				$(this.el).html(this.template());

				this.processing = $(this.el).find('.query_processing');

				if (this.isReadOnly
						|| Settings.MODE
						&& (Settings.MODE == "view" || Settings.MODE == "table")) {
					$(this.el).find('.workspace_editor').remove();
					this.toggle_sidebar();
					$(this.el).find('.sidebar_separator').remove();
					$(this.el).find('.workspace_inner').css({
						'margin-left' : 0
					});
					$(this.el).find('.workspace_fields').remove();
					$(this.el).find('.sidebar').hide();

					$(this.toolbar.el)
							.find(
									".run, .auto, .toggle_fields, .toggle_sidebar,.switch_to_mdx, .new")
							.parent().remove();

				} else {

				}

				if (Settings.MODE && Settings.MODE == "table") {
					$(this.el).find('.workspace_toolbar').remove();
					$(this.el).find('.query_toolbar').remove();
				} else {
					// Show toolbar
					$(this.el).find('.workspace_toolbar').append(
							$(this.toolbar.el));
					$(this.el).find('.query_toolbar').append(
							$(this.querytoolbar.el));
				}

				this.switch_view_state(this.viewState, true);

				// Add results table
				$(this.el).find('.workspace_results').append($(this.table.el));

				// Adjust tab when selected
				this.tab.bind('tab:select', this.adjust);
				$(window).resize(this.adjust);

				// Fire off new workspace event
				Saiku.session.trigger('workspace:new', {
					workspace : this
				});

				return this;
			},

			clear : function() {
				// Prepare the workspace for a new query
				this.table.clearOut();
				$(this.el).find('.workspace_results table,.connectable').html(
						'');
				$(this.el).find('.workspace_results_info').empty();
				$(this.el).find('.parameter_input').empty();

				$(this.querytoolbar.el).find('ul.options a.on').removeClass(
						'on');
				$(this.el).find('.fields_list[title="ROWS"] .limit')
						.removeClass('on');
				$(this.el).find('.fields_list[title="COLUMNS"] .limit')
						.removeClass('on');
				// Trigger clear event
				Saiku.session.trigger('workspace:clear', {
					workspace : this
				});

			},

			adjust : function() {
				// Adjust the height of the separator
				var $separator = $(this.el).find('.sidebar_separator');
				var heightReduction = 87;
				if (Settings.PLUGIN == true || Settings.BIPLUGIN == true) {
					heightReduction = 2;
					if (Settings.MODE == 'table') {
						heightReduction = -5;
					}
				}
				if ($('#header').length == 0 || $('#header').is('hidden')) {
					heightReduction = 2;
				}

				$separator.height($("body").height() - heightReduction);
				$(this.el).find('.sidebar').height(
						$("body").height() - heightReduction);

				$(this.querytoolbar.el).find('div').height(
						$("body").height() - heightReduction - 10);

				// Adjust the dimensions of the results window
				var editorHeight = $(this.el).find('.workspace_editor').is(
						':hidden') ? 0 : $(this.el).find('.workspace_editor')
						.height();
				var processingHeight = $(this.el).find('.query_processing').is(
						':hidden') ? 0 : $(this.el).find('.query_processing')
						.height() + 62;
				var upgradeHeight = $(this.el).find('.upgradeheader').is(
						':hidden') ? 0 : $(this.el).find('.upgrade').height();

				$(this.el).find('.workspace_results').css(
						{
							height : $("body").height()
									- heightReduction
									- $(this.el).find('.workspace_toolbar')
											.height()
									- $(this.el)
											.find('.workspace_results_info')
											.height() - editorHeight
									- processingHeight - upgradeHeight - 20
						});

				// Fire off the adjust event
				this.trigger('workspace:adjust', {
					workspace : this
				});
			},

			toggle_sidebar : function() {
				// Toggle sidebar
				$(this.el).find('.sidebar').toggleClass('hide');
				$(this.toolbar.el).find('.toggle_sidebar').toggleClass('on');
				var calculatedMargin = ($(this.el).find('.sidebar').is(
						':visible') ? $(this.el).find('.sidebar').width() : 0)
						+ ($(this.el).find('.sidebar_separator').width()) + 1;
				var new_margin = calculatedMargin;
				$(this.el).find('.workspace_inner').css({
					'margin-left' : new_margin
				});
			},

			prepare : function() {
				// Draw user's attention to cube navigation
				$(this.el).find('.cubes').parent().css({
					backgroundColor : '#AC1614'
				}).delay(300).animate({
					backgroundColor : '#fff'
				}, 'slow');
			},

			new_query : function() {
				
			},

			init_query : function(isNew) {
				var self = this;
				try {

					// TODO: This should be refactored, the workspace should
					// have a renderer set and always use that
					// probably extend the Table.js with TableRenderer and make
					// it an advanced one

					var properties = this.query.model.properties ? this.query.model.properties
							: {};

					var renderMode = ('RENDER_MODE' in Settings) ? Settings.RENDER_MODE
							: ('saiku.ui.render.mode' in properties) ? properties['saiku.ui.render.mode']
									: null;
					var renderType = ('RENDER_TYPE' in Settings) ? Settings.RENDER_TYPE
							: ('saiku.ui.render.type' in properties) ? properties['saiku.ui.render.type']
									: null;

					if (typeof renderMode != "undefined" && renderMode != null) {
						this.querytoolbar.switch_render(renderMode);
					}

					if ('chart' == renderMode) {
						$(this.chart.el).find('.canvas_wrapper').hide();
						this.chart.renderer.switch_chart(renderType);
						$(this.querytoolbar.el).find(
								'ul.chart [href="#' + renderType + '"]')
								.parent().siblings().find('.on').removeClass(
										'on');
						$(this.querytoolbar.el).find(
								'ul.chart [href="#' + renderType + '"]')
								.addClass('on');

					} else if ('table' == renderMode
							&& renderType in this.querytoolbar) {
						this.querytoolbar.render_mode = "table";
						this.querytoolbar.spark_mode = renderType;
						$(this.querytoolbar.el)
								.find('ul.table a.' + renderType)
								.addClass('on');
					}
				} catch (e) {
					Saiku.error(this.cid, e);
				}

				if ((Settings.MODE == "table") && this.query) {
					this.query.run(true);
					return;
				}

				if (this.query.model.type == "MDX") {
					this.query.setProperty("saiku.olap.result.formatter",
							"flat");
					if (!$(this.el).find('.sidebar').hasClass('hide')) {
						this.toggle_sidebar();
					}
					$(this.el).find('.workspace_fields').addClass('hide')
					this.toolbar.switch_to_mdx();
				} else {
					$(this.el).find('.workspace_editor').removeClass('hide')
							.show();
					$(this.el).find('.workspace_fields')
							.removeClass('disabled').removeClass('hide');
					$(this.el).find('.workspace_editor .mdx_input').addClass(
							'hide');
					$(this.el).find('.workspace_editor .editor_info').addClass(
							'hide');
					$(this.toolbar.el)
							.find(
									'.auto, .toggle_fields, .query_scenario, .buckets, .non_empty, .swap_axis, .mdx, .switch_to_mdx, .zoom_mode')
							.parent().show();
					$(this.el).find('.run').attr('href', '#run_query');
					// Start Custom Code for Pagination
					$(this.el).find('.first_page').attr('href', '#first_page');
					$(this.el).find('.prev_page').attr('href', '#prev_page');
					$(this.el).find('.next_page').attr('href', '#next_page');
					$(this.el).find('.last_page').attr('href', '#last_page');
					// End Custom Code for Pagination
				}
				this.adjust();
				this.switch_view_state(this.viewState, true);

				if (!$(this.el).find('.sidebar').hasClass('hide')
						&& (Settings.MODE == "table" || Settings.MODE == "view" || this.isReadOnly)) {
					this.toggle_sidebar();
				}
				if ((Settings.MODE == "view") && this.query || this.isReadOnly) {
					this.query.run(true);
					return;
				}

				// Find the selected cube
				if (this.selected_cube === undefined) {
					var schema = this.query.model.cube.schema;
					this.selected_cube = this.query.model.cube.connection
							+ "/"
							+ this.query.model.cube.catalog
							+ "/"
							+ ((schema == "" || schema == null) ? "null"
									: schema) + "/"
							+ encodeURIComponent(this.query.model.cube.name);
					$(this.el).find('.cubes').val(this.selected_cube);
				}

				if (this.selected_cube) {
					
				} else {
					// Someone literally selected "Select a cube"
					$(this.el).find('.calculated_measures, .addMeasure').hide();
					$(this.el).find('.dimension_tree').html('');
					$(this.el).find('.measure_tree').html('');
				}

				// is this a new query?
				// Run query once the filters widget is ready.
				/*this.query.initFiltersDeferred.done(function() {
					// console.log(window.currentFilter.serialize());
					self.query.run(true);
				});*/
				
				self.query.run(true);
				
				Saiku.i18n.translate();
			},

			synchronize_query : function() {
				var self = this;
				if (!self.isReadOnly
						&& (!Settings.hasOwnProperty('MODE') || (Settings.MODE != "table" && Settings.MODE != "view"))) {

				}

			},

			sync_query : function(dimension_el) {
				
			},

			populate_selections : function(dimlist) {
				
			},

			update_caption : function(increment) {
				var caption = this.caption(increment);
				this.tab.set_caption(caption);
			},

			remove_dimension : function(event, ui) {
				if (this.query.model.type == "QUERYMODEL") {
					this.drop_zones.remove_dimension(event, ui);
				}
			},

			update_parameters : function() {
				var self = this;
				if (!Settings.ALLOW_PARAMETERS)
					return;

				var paramDiv = "<span class='i18n'>Parameters</span>: ";
				var parameters = this.query.helper.model().parameters;
				var hasParams = false;
				for ( var key in parameters) {
					var val = "";
					if (parameters[key] && parameters[key] != null) {
						val = parameters[key];
					}
					paramDiv += "<b>" + key
							+ "</b> <input type='text' placeholder='" + key
							+ "' value='" + val + "' />";
					hasParams = true;
				}
				paramDiv += "";

				if (hasParams) {
					$(this.el).find('.parameter_input').html(paramDiv);
				} else {
					$(this.el).find('.parameter_input').html("");
				}

				$(this.el).find('.parameter_input input').off('change');
				$(this.el)
						.find('.parameter_input input')
						.on(
								'change',
								function(event) {
									var paramName = $(event.target).attr(
											'placeholder');
									var paramVal = $(event.target).val();
									self.query.helper.model().parameters[paramName] = paramVal;
								});

			},

			render_result : function(args) {
				var self = this;
				$(this.el).find(".workspace_results_info").empty();

				if (args.data != null && args.data.error != null) {
					return this.error(args);
				}
				// Check to see if there is data
				if (args.data == null
						|| (args.data.cellset && args.data.cellset.length === 0)) {
					return this.no_results(args);
				}

				var chour = new Date().getHours();
				if (chour < 10)
					chour = "0" + chour;

				var cminutes = new Date().getMinutes();
				if (cminutes < 10)
					cminutes = "0" + cminutes;

				var cdate = chour + ":" + cminutes;
				var runtime = args.data.runtime != null ? (args.data.runtime / 1000)
						.toFixed(2)
						: "";
				/*
				 * var info = '<b>Time:</b> ' + cdate + " &emsp;<b>Rows:</b> " +
				 * args.data.height + " &emsp;<b>Columns:</b> " +
				 * args.data.width + " &emsp;<b>Duration:</b> " + runtime +
				 * "s";
				 */
				var info = '<b><span class="i18n">Info:</span></b> &nbsp;'
						+ cdate + "&emsp;/ &nbsp;" + args.data.width + " x "
						+ args.data.height + "&nbsp; / &nbsp;" + runtime + "s";

				this.update_parameters();

				this.adjust();
				return;
			},

			switch_view_state : function(mode, dontAnimate) {
				var target = mode || 'edit';

				if (target == 'edit') {
					// $(this.el).find('.workspace_editor').show();
					this.toolbar.toggle_fields_action('show', dontAnimate);
					if (this.query && this.query.get('type') == "MDX") {
						this.toolbar.editor.gotoLine(0);
					}
					if ($(this.el).find('.sidebar').hasClass('hide')) {
						this.toggle_sidebar();
					}
					// $(this.el).find('.sidebar_separator').show();
					// $(this.el).find('.workspace_inner').removeAttr('style');
					$(this.toolbar.el)
							.find(
									".auto, .toggle_fields, .toggle_sidebar,.switch_to_mdx, .new")
							.parent().css({
								"display" : "block"
							});
				} else if (target == 'view') {
					// $(this.el).find('.workspace_editor').hide();
					this.toolbar.toggle_fields_action('hide', dontAnimate);
					if (!$(this.el).find('.sidebar').hasClass('hide')) {
						this.toggle_sidebar();
					}
					// $(this.el).find('.sidebar_separator').hide();
					// $(this.el).find('.workspace_inner').css({ 'margin-left':
					// 0 });

					$(this.toolbar.el)
							.find(
									".auto, .toggle_fields, .toggle_sidebar,.switch_to_mdx")
							.parent().hide();
				}
				this.viewState = target;
				$(window).trigger('resize');

			},

			block : function(message) {
				/*
				 * Most probably not needed anymore. Seems ok now with fix #192
				 * if (isIE) { var $msg = $("<span>" + message + "</span>");
				 * $msg.find('.processing_image').removeClass('processing_image');
				 * Saiku.ui.block($msg.html()); }
				 */
				$(this.el)
						.block(
								{
									message : '<span class="saiku_logo" style="float:left">&nbsp;&nbsp;</span> '
											+ message
								});
				Saiku.i18n.translate();
			},

			unblock : function() {
				if (isIE) {
					Saiku.ui.unblock();
				} else {
					$(this.el).unblock();
					Saiku.ui.unblock();
				}
			},

			cancel : function(event) {
				// AMP-19253 close windows when query is canceled
				window.top.close();
				/*
				 * var self = this; if (event) { event.preventDefault(); }
				 * this.query.action.del("/cancel", { success: function() {
				 * self.cancelled(); } });
				 */
			},

			cancelled : function(args) {
				this.processing
						.html(
								'<span class="processing_image">&nbsp;&nbsp;</span> <span class="i18n">Canceling Query...</span>')
						.show();
			},

			no_results : function(args) {
				this.processing.html('<span class="i18n">No Results</span>')
						.show();
			},

			error : function(args) {
				this.processing.html(safe_tags_replace(args.data.error)).show();
			}
		});
