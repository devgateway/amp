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
				Saiku.logger.log("Workspace.initialize");
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
				Saiku.logger.log("Workspace.caption");
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
                                    cube_navigation: {} //Saiku.session.sessionworkspace.cube_navigation
								});
			},

			refresh : function(e) {
				Saiku.logger.log("Workspace.refresh");
				if (e) {
					e.preventDefault();
				};
				Saiku.session.sessionworkspace.refresh();
			},

			render : function() {
				Saiku.logger.log("Workspace.render");
				// Load template
				$(this.el).html(this.template());

				this.processing = $(this.el).find('.query_processing');

				// Show toolbar
				$(this.el).find('.workspace_toolbar').append($(this.toolbar.el));
				$(this.el).find('.query_toolbar').append($(this.querytoolbar.el));

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
				Saiku.logger.log("Workspace.clear");
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
				Saiku.logger.log("Workspace.adjust");
				// Adjust the height of the separator
				var $separator = $(this.el).find('.sidebar_separator');
				var heightReduction = 87;				
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
				Saiku.logger.log("Workspace.toggle_sidebar");
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
				Saiku.logger.log("Workspace.prepare");
			},

			new_query : function() {
				Saiku.logger.log("Workspace.new_query");
			},

			init_query : function(isNew) {
				Saiku.logger.log("Workspace.init_query");
				var self = this;
				$(this.toolbar.el).find('.auto, .toggle_fields, .query_scenario, .buckets, .non_empty, .swap_axis, .mdx, .switch_to_mdx, .zoom_mode').parent().show();
				$(this.el).find('.run').attr('href', '#run_query');
				
				// Start Custom Code for Pagination
				$(this.el).find('.first_page').attr('href', '#first_page');
				$(this.el).find('.prev_page').attr('href', '#prev_page');
				$(this.el).find('.next_page').attr('href', '#next_page');
				$(this.el).find('.last_page').attr('href', '#last_page');
				// End Custom Code for Pagination
					
				//this.adjust();
				//this.switch_view_state(this.viewState, true);
			    window.currentFilter.loaded.done(function() {
		            var auxFilters = self.query.get('filters');
		            auxFilters.includeLocationChildren = self.query.get('includeLocationChildren');
		            window.currentFilter.deserialize({filters: auxFilters}, {
		            	silent : true
		            });
		            var filterObject = window.currentFilter.serialize();
		            self.query.run(true, null,filterObject.filters ||{} );	
	            });
				Saiku.i18n.translate();
			},

			synchronize_query : function() {
				Saiku.logger.log("Workspace.synchronize_query");
			},

			sync_query : function(dimension_el) {
				Saiku.logger.log("Workspace.sync_query");
			},

			populate_selections : function(dimlist) {
				Saiku.logger.log("Workspace.populate_selections");
			},

			update_caption : function(increment) {
				Saiku.logger.log("Workspace.update_caption");
				var caption = this.caption(increment);
				this.tab.set_caption(caption);
			},

			update_parameters : function() {
				Saiku.logger.log("Workspace.update_parameters");
			},

			render_result : function(args) {
				Saiku.logger.log("Workspace.render_result");
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

				this.update_parameters();

				this.adjust();
				return;
			},

			switch_view_state : function(mode, dontAnimate) {
				Saiku.logger.log("Workspace.switch_view_state");
				var target = mode || 'edit';

				this.toolbar.toggle_fields_action('hide', dontAnimate);
				if (!$(this.el).find('.sidebar').hasClass('hide')) {
					this.toggle_sidebar();
				}

				$(this.toolbar.el).find(".auto, .toggle_fields, .toggle_sidebar,.switch_to_mdx").parent().hide();
				this.viewState = target;
				$(window).trigger('resize');

			},

			block : function(message) {
				Saiku.logger.log("Workspace.block");
				/*
				 * Most probably not needed anymore. Seems ok now with fix #192
				 * if (isIE) { var $msg = $("<span>" + message + "</span>");
				 * $msg.find('.processing_image').removeClass('processing_image');
				 * Saiku.ui.block($msg.html()); }
				 */
				$(this.el).block({message : '<span class="saiku_logo" style="float:left">&nbsp;&nbsp;</span> ' + message});
				Saiku.i18n.translate();
			},

			unblock : function() {
				Saiku.logger.log("Workspace.block");
				if (isIE) {
					Saiku.ui.unblock();
				} else {
					$(this.el).unblock();
					Saiku.ui.unblock();
				}
			},

			cancel : function(event) {
				Saiku.logger.log("Workspace.cancel");
				// AMP-19253 close windows when query is canceled
				window.top.close();
				/*
				 * var self = this; if (event) { event.preventDefault(); }
				 * this.query.action.del("/cancel", { success: function() {
				 * self.cancelled(); } });
				 */
			},

			cancelled : function(args) {
				Saiku.logger.log("Workspace.cancelled");
				this.processing
						.html(
								'<span class="processing_image">&nbsp;&nbsp;</span> <span class="i18n">Canceling Query...</span>')
						.show();
			},

			no_results : function(args) {
				Saiku.logger.log("Workspace.no_results");
				this.processing.html('<span class="i18n">No Results</span>')
						.show();
			},

			error : function(args) {
				Saiku.logger.log("Workspace.error");
				this.processing.html(safe_tags_replace(args.data.error)).show();
			}
		});
