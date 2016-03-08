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
 * Sets up workspace drop zones for DnD and other interaction
 */
var WorkspaceDropZone = Backbone.View.extend({

    template: function() {
        var template = $("#template-workspace-dropzones").html() || "";
        return _.template(template)();
    },
    
    events: {
        'sortstop .fields_list_body.details': 'set_measures',
        'sortstop .axis_fields': 'select_dimension',
        'click .d_measure' : 'remove_measure_click',
        'click .d_level': 'selections',
//        'click .d_measure span.sort' : 'sort_measure',
        'click .axis_fields_header.limit' : 'limit_axis',
        'click .clear_axis' : 'clear_axis'
    },
    
    initialize: function(args) {
        // Keep track of parent workspace
        this.workspace = args.workspace;
        
        // Maintain `this` in jQuery event handlers
        _.bindAll(this, "clear_axis");
    },
    
    render: function() {
        var self = this;
        // Generate drop zones from template
        $(this.el).html(this.template());

        // Activate drop zones
        $(this.el).find('.fields_list_body.details ul.connectable').sortable({
            items:          '> li',
            opacity:        0.60,
            placeholder:    'placeholder',
            tolerance:      'pointer',
            containment:    $(self.workspace.el),
            start:          function(event, ui) {
                ui.placeholder.text(ui.helper.text());
            }
        });

        $(this.el).find('.axis_fields ul.connectable').sortable({
            connectWith: $(self.el).find('.axis_fields ul.connectable'),
            forcePlaceholderSize:   false,
            forceHelperSize:        true,
            items:                  'li.selection',
            opacity:                0.60,
            placeholder:            'placeholder',
            tolerance:              'touch',
            cursorAt:               { top: 10, left: 60 },
            containment:            $(self.workspace.el),
            start:                  function(event, ui) {
                    var hierarchy = $(ui.helper).find('a').parent().parent().attr('hierarchycaption');
                    ui.placeholder.text(hierarchy);
                    $(ui.helper).css({ width: "auto", height: "auto"});
                    $(self.el).find('.axis_fields ul.hierarchy li.d_level:visible').addClass('temphide').hide();
            }
        });
        
        return this; 
    },

    set_measures: function(event, ui) {
        var details = [];

        $(this.el).find('.fields_list_body.details ul.connectable li.d_measure').each( function(index, element) {
            var measure = {
                "name": $(element).find('a').attr('measure'),
                "type": $(element).find('a').attr('type')
            };
            details.push(measure);
        });
        this.workspace.query.helper.setMeasures(details);
        this.workspace.sync_query();
        this.workspace.query.run();
    },

    remove_measure_click: function(event) {
        event.preventDefault();
        var target = $(event.target).hasClass('d_measure') ?  $(event.target).find('a') :  $(event.target);
        target.parent().remove();
        this.set_measures();
    },

    remove_dimension: function(event, ui) {
        if ($(ui.helper).hasClass('d_measure')) {
                $(ui.helper).detach();
                this.set_measures();
            } else {
                var hierarchy = $(ui.helper).find('a').attr('hierarchy');
                var fromAxis = $(this.el).find('ul.hierarchy[hierarchy="' + hierarchy + '"]').parents('.fields_list').attr('title');
                var level =  $(ui.helper).find('a').attr('level');
                this.workspace.query.helper.removeHierarchy(hierarchy);
                this.workspace.sync_query();
                this.workspace.query.run();
        }
    },


    synchronize_query: function() {
        var self = this;
        this.reset_dropzones();

        var model = this.workspace.query.helper.model();

        if (model.hasOwnProperty('queryModel') && model.queryModel.hasOwnProperty('axes')) {
            var axes = model.queryModel.axes;
            for (var axis in axes) {
                var $axis = $(self.el).find('.fields_list[title="' + axis + '"]');
                _.each(axes[axis].hierarchies, function(hierarchy) {
                    var h = $(self.workspace.dimension_list.el).find('ul.d_hierarchy[hierarchy="' + hierarchy.name + '"]').clone().removeClass('d_hierarchy').addClass('hierarchy');
                    h.find('li.d_level').hide();
                    for (var level in hierarchy.levels) {
                        h.find('li a[level="' + level + '"]').parent().show();

                        // sync attribute list
                        $(self.workspace.dimension_list.el).find('ul.d_hierarchy[hierarchy="' + hierarchy.name + '"] li a[level="' + level + '"]').parent()
                                .draggable('disable')
                                .parents('.parent_dimension')
                                .find('.folder_collapsed')
                                .addClass('selected');


                    }
                    var selection = $('<li class="selection"></li>');
                    selection.append(h);
                    selection.appendTo($axis.find('ul.connectable'));
                });
            }
            if(!model.queryModel.details)
            	return;
            var measures = model.queryModel.details.measures || [];
            _.each(measures, function (measure) {
                var m = $(self.workspace.dimension_list.el).find('.measure_tree a.measure[measure="' + measure.name + '"]').parent();
                var m2 = m.clone().show();
                m2.appendTo( $(self.el).find('.fields_list_body.details ul.connectable'));

                m.draggable('disable');
            });

            this.update_dropzones();
        }
    },

    reset_dropzones: function() {
            var self = this;
            $(self.el).find('.fields_list_body ul.connectable').find('li.selection, li.d_measure').remove();
            $(self.workspace.dimension_list.el).find('li.ui-draggable-disabled').draggable('enable');
            $(self.el).find('.fields_list[title="ROWS"] .limit').removeClass('on');
            $(self.el).find('.fields_list[title="COLUMNS"] .limit').removeClass('on');
            $(this.workspace.el).find('.fields_list_body .clear_axis').addClass('hide');

    },

    update_dropzones: function() {
        $(this.workspace.el).find('.fields_list_body').each(function(idx, ael) {
            var $axis = $(ael);
            if ($axis.find('ul.connectable li.selection, ul.connectable li.d_measure').length == 0) {
                $axis.siblings('.clear_axis').addClass('hide');
            } else {
                $axis.siblings('.clear_axis').removeClass('hide');
            }
        });


    },
    
    clear_axis: function(event) {
            var self = this;
            event.preventDefault();
            var axis = $(event.target).siblings('.fields_list_body').parent().attr('title');
            if (axis == "DETAILS") {
                this.workspace.query.helper.clearMeasures();
            } else {
                this.workspace.query.helper.clearAxis(axis);
            }
            
            this.workspace.sync_query();
            this.workspace.query.run();
            return false;
    },



    select_dimension: function(event, ui) {
        var self = this;
        // if we drop to remove dont execute the following
        
        if ( false || $(ui.item).is(':visible')) {
            $(self.el).find('.axis_fields ul.hierarchy').each( function(index, element) {
                $(element).find('li.temphide').show().removeClass('temphide');
            });

            var hierarchy = ui.item.find('.level').attr('hierarchy');
            var indexHierarchy = -1;
            ui.item.parents('ul.connectable').find('li.selection').each( function(index, element) {
                if ( $(element).find('ul.hierarchy').attr('hierarchy') == hierarchy) {
                    indexHierarchy = index;
                }
            });

            var toAxis = ui.item.parents('.axis_fields').parent().attr('title');
            var fromAxis = $(event.target).parents('.axis_fields').parent().attr('title');
            var isNew = ui.item.hasClass('d_level');
            if (isNew) {
                var level = ui.item.find('a.level').attr('level');
                this.workspace.query.helper.includeLevel(toAxis, hierarchy, level, indexHierarchy);
            } else {
                self.workspace.query.helper.moveHierarchy(fromAxis, toAxis, hierarchy, indexHierarchy);
            }
            
            $(ui.item).detach();
            this.workspace.sync_query();
            self.workspace.query.run();
            return;
        }
        return;
    },
    
    selections: function(event, ui) {
        if (event) {
            event.preventDefault();
        }
        // Determine dimension
        var $target = $(event.target).hasClass('d_level') ?
            $(event.target).find('.level') :
            $(event.target);
        var key = $target.attr('href').replace('#', '');
        
        // Launch selections dialog
        (new SelectionsModal({
            target: $target,
            name: $target.text(),
            key: key,
            workspace: this.workspace
        })).open();
        
        return false;
    },

    limit_axis: function(event) {
        var self = this;
        
        if (typeof this.workspace.query == "undefined" || this.workspace.query.model.type != "QUERYMODEL" || Settings.MODE == "view") {
            return false;
        }
        
        var $target =  $(event.target).hasClass('limit') ? $(event.target) : $(event.target).parent();
        var $axis = $target.siblings('.fields_list_body');
        var target = $axis.parent().attr('title');
        
        $.contextMenu('destroy', '.limit');
        $.contextMenu({
            appendTo: $target,
            selector: '.limit', 
            ignoreRightClick: true,
             build: function($trigger, e) {
                var query = self.workspace.query;
                var cube = self.workspace.selected_cube;
                var items = {};
                var measures = Saiku.session.sessionworkspace.cube[cube].get('data').measures;
                var a = self.workspace.query.helper.getAxis(target);

                var func, n, sortliteral, filterCondition, sortOrder, sortOrderLiteral, sortHl, topHl, filterHl, totalFunction;;
                var isFilter = false, isSort = false, isTop = false;
                if (a && a.filters) {
                    _.each(a.filters, function(filter) {
                        if (filter.flavour == "N") {
                            func = a["function"];
                            n = filter.expressions[0];
                            sortliteral = filter.expressions[1];
                            isTop = true;
                        }
                        if (filter.flavour == "Generic") {
                            filterCondition = filter.expressions[0];
                            isFilter = true;
                        }                         
                    });
                }
                if (a && a.sortOrder) {
                    sortOrder = a.sortOrder;
                    sortOrderLiteral = a.sortEvaluationLiteral;
                    isSort = true;
                }
                if (a && a.aggregators && a.aggregators.length > 0) {
                    totalFunction = a.aggregators[0];
                }

                if (func != null && sortliteral == null) {
                    topHl = func + "###SEPARATOR###" + n;
                } else if (func != null && sortliteral != null && n != null) {
                    topHl = "custom";
                }

                if (isSort && sortOrder != null) {
                    sortHl = "customsort";
                }

                _.each(measures, function(measure) {
                    items[measure.uniqueName] = {
                        name: measure.caption,
                        payload: {
                            "n"     : 10,
                            "sortliteral"    : measure.uniqueName
                        }
                    };
                });


                var addFun = function(items, fun) {
                    var ret = {};
                    for (key in items) {
                        ret[ (fun + '###SEPARATOR###'+ key) ] = _.clone(items[key]);
                        ret[ (fun + '###SEPARATOR###' + key) ].fun = fun;
                        if (fun == func && sortliteral == key && items[key].payload["n"] == n) {
                            topHl = fun + "Quick";
                            ret[ (fun + '###SEPARATOR###' + key) ].name =
                                    "<b>" + items[key].name + "</b>";
                        }
                        if (fun == sortOrder && sortOrderLiteral == key) {
                            sortHl = fun + "Quick";
                            ret[ (fun + '###SEPARATOR###' + key) ].name =
                                    "<b>" + items[key].name + "</b>";
                        }
                    }
                    return ret;
                };

                var citems = {
                        "filter" : {name: "Filter", i18n: true, items: 
                         { 
                                "customfilter": {name: "Custom...", i18n: true },
                                "clearfilter": {name: "Clear Filter", i18n: true }
                         }},
                        "limit" : {name: "Limit", i18n: true, items: 
                        {
                                "TopCount###SEPARATOR###10": {name: "Top 10", i18n: true },
                                "BottomCount###SEPARATOR###10": {name: "Bottom 10", i18n: true },
                                "TopCountQuick" : { name: "Top 10 by...", i18n: true, items: addFun(items, "TopCount") },
                                "BottomCountQuick" : { name: "Bottom 10 by...", i18n: true, items: addFun(items, "BottomCount") },
                                "customtop" : {name: "Custom Limit...", i18n: true },
                                "clearlimit" : {name: "Clear Limit", i18n: true }
                         }},
                        "sort" : {name: "Sort", i18n: true, items:
                        {
                            "ASCQuick": {name: "Ascending" , i18n: true, items: addFun(items, "ASC") },
                            "DESCQuick": {name: "Descending", i18n: true, items: addFun(items, "DESC")},
                            "BASCQuick": {name: "Ascending (Breaking Hierarchy)", i18n: true, items: addFun(items, "BASC")},
                            "BDESCQuick": {name: "Descending (Breaking Hierarchy)", i18n: true, items: addFun(items, "BDESC") },
                            "customsort" : { name: "Custom...", i18n: true },
                            "clearsort" : {name: "Clear Sort", i18n: true }
                        }},
                        "grand_totals" : {name: "Grand totals", i18n: true, items:
                        {
                            "show_totals_not": {name: "None", i18n: true},
                            "show_totals_sum": {name: "Sum", i18n: true},
                            "show_totals_min": {name: "Min", i18n: true},
                            "show_totals_max": {name: "Max", i18n: true},
                            "show_totals_avg": {name: "Avg", i18n: true}
                        }},

                        "cancel" : { name: "Cancel", i18n: true }

                };
                $.each(citems, function(key, item){
                    recursive_menu_translate(item, Saiku.i18n.po_file);
                });

                var totalItems = citems["grand_totals"].items;
                if (totalFunction) {
                    for (var key in totalItems) {
                        if (key.substring("show_totals_".length) == totalFunction) {
                            totalItems[key].name = "<b>" + totalItems[key].name + "</b";
                        }
                    }
                } else {
                    totalItems["show_totals_not"].name = "<b>" + totalItems["show_totals_not"].name + "</b";
                }
                

                items["10"] = {
                   payload: { "n" : 10 }
                }
                
                if (isFilter) {
                    var f = citems["filter"];
                    f.name = "<b>" + f.name + "</b>";
                    f.items["customfilter"].name = "<b>" + f.items["customfilter"].name + "</b>";
                }
                if (isSort) {
                    var s = citems["sort"].items;
                    citems["sort"].name = "<b>" + citems["sort"].name + "</b>";
                    if (sortHl in s) {
                        s[sortHl].name = "<b>" + s[sortHl].name + "</b>";    
                    }
                }
                if (isTop) {
                    var t = citems["limit"].items;
                    citems["limit"].name = "<b>" + citems["limit"].name + "</b>";
                    if (topHl in t) {
                        t[topHl].name = "<b>" + t[topHl].name + "</b>";    
                    }   
                }

                return {
                    callback: function(key, options) {
                            if (key == "cancel") {
                                return;
                            }

                            if (key == "clearfilter") {
                                $target.removeClass('on');
                                self.workspace.query.helper.removeFilter(a, 'Generic');
                                self.synchronize_query();
                                self.workspace.query.run();
                            } else if (key == "customfilter") {
                                var save_custom = function(filterCondition) {
                                    var expressions = [];
                                    expressions.push(filterCondition);

                                    self.workspace.query.helper.removeFilter(a, 'Generic');
                                    a.filters.push( 
                                        {   "flavour" : "Generic", 
                                            "operator": null, 
                                            "function" : "Filter",
                                            "expressions": expressions
                                        });
                                    self.synchronize_query();
                                    self.workspace.query.run();
                                };

                                 (new FilterModal({ 
                                    axis: target,
                                    success: save_custom, 
                                    query: self.workspace.query,
                                    expression: filterCondition,
                                    expressionType: "Filter"
                                })).render().open();

                            } else if (key == "clearlimit") {
                                $target.removeClass('on');
                                self.workspace.query.helper.removeFilter(a, 'N');
                                self.synchronize_query();
                                self.workspace.query.run();
                            } else if (key == "customtop") {

                                var save_custom = function(fun, n, sortliteral) {
                                    var expressions = [];
                                    expressions.push(n);
                                    if (sortliteral) {
                                        expressions.push(sortliteral);
                                    }

                                    self.workspace.query.helper.removeFilter(a, 'N');
                                    a.filters.push( 
                                        {   "flavour" : "N", 
                                            "operator": null, 
                                            "function" : fun,
                                            "expressions": expressions
                                        });
                                    self.synchronize_query();
                                    self.workspace.query.run();
                                };

                                 (new CustomFilterModal({ 
                                    axis: target,
                                    measures: measures,
                                    success: save_custom, 
                                    query: self.workspace.query,
                                    func: func,
                                    n: n,
                                    sortliteral: sortliteral
                                })).render().open();
                            } else if (key == "customsort") {

                                var save_customsort = function(sortO, sortL) {
                                    a.sortOrder = sortO;
                                    a.sortEvaluationLiteral = sortL;
                                    self.synchronize_query();
                                    self.workspace.query.run();
                                };

                                 (new FilterModal({ 
                                    axis: target,
                                    success: save_customsort, 
                                    query: self.workspace.query,
                                    expression: sortOrderLiteral,
                                    expressionType: "Order"
                                })).render().open();
                            } else if (key == "clearsort") {
                                a.sortOrder = null;
                                a.sortEvaluationLiteral = null;
                                alert('maybe?');
                                self.synchronize_query();
                                self.workspace.query.run();
                            } else if (key.indexOf("show_totals_") == 0){
                                var total = key.substring("show_totals_".length);
                                var aggs = [];
                                aggs.push(total);
                                a['aggregators'] = aggs;
                                self.workspace.query.run();
                            } else {

                                var fun = key.split('###SEPARATOR###')[0];
                                var ikey = key.split('###SEPARATOR###')[1];
                                var method = "";
                                var data = {};
                                if (_.indexOf(["ASC", "BASC", "DESC", "BDESC"], fun) > -1) {
                                    a.sortOrder = fun;
                                    a.sortEvaluationLiteral = items[ikey].payload["sortliteral"];

                                } else {
                                    var expressions = [];
                                    expressions.push(items[ikey].payload.n);
                                    var sl = items[ikey].payload["sortliteral"];
                                    if (sl) {
                                        expressions.push(sl);
                                    }

                                    self.workspace.query.helper.removeFilter(a, 'N');
                                    a.filters.push( 
                                        {   "flavour" : "N", 
                                            "operator": null, 
                                            "function" : fun,
                                            "expressions": expressions
                                        });
                                }
                                self.synchronize_query();
                                self.workspace.query.run();
                            }
                    },
                    items: citems
                } 
            }
        });
    $target.contextMenu();
    }
});
