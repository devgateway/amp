var Backbone = require('backbone');
var _ = require('underscore');
var $ = require('jquery');
var GenericFilterModel = require('../models/generic-filter-model');
var YearsFilterModel = require('../models/years-filter-model');
var YearsOnlyFilterModel = require('../models/years-only-filter-model');
var Constants = require('../utils/constants');
var Collator = new Intl.Collator(undefined, {ignorePunctuation: true});
var UNDEFINED_ID = -999999999;
var FILTERS_TO_SORT_BY_ID = ['disaster-response-marker'];

//TODO: move most code from filters-view here.
module.exports = Backbone.Collection.extend({
    url: Constants.ALL_FILTERS_URL,
    _loaded: null,
    _allDeferreds: [],
    componentCaller: null,
    translator: null,
    initialize: function (models, options) {
        this.on('add', this._cleanUpAfterAdd);
        this.load(options.reportType);
        this.translator = options.translator;
        if (options.caller) {
            componentCaller = options.caller;
        }
    },

    load: function (reportType) {
        var self = this;
        if (!this._loaded) {
            this._loaded = new $.Deferred();
            if (reportType === 'P') {
                this.url = Constants.ALL_FILTERS_URL + '?report-type=P';
            }
            this.fetch({remove: false}).then(function () {
                // when all child calls are done resolve.
                $.when.apply($, self._allDeferreds)
                    .done(self._loaded.resolve)
                    .fail(self._loaded.reject);
            }).fail(self._loaded.reject);
        }

        return this._loaded;
    },

    _cleanUpAfterAdd: function (model) {

        var self = this;
        // remove if ui false also catches empty dummy filters we add in 'model' function below.
        if (!model.get('ui')) {
            self.remove(model);
        }
        //remove if the endpoint returns the filter type (dashboard,tabs,reports and/or GIS) for which
        //the model should be visible and the caller is not of the same tpe

        if (model.get('componentType')) {
            var isOfRequiredType = _.some(model.get('componentType'), function (type) {
                return type === componentCaller || type === Constants.COMPONENT_TYPE_ALL;
            });

            if (!isOfRequiredType) {
                self.remove(model);
            }
        }
        // Expose this field for later usage.
        this.componentCaller = componentCaller;
    },

    parse: function (data) {
        //only keep filters with ui == true;
        data = _.filter(data, function (obj) {
            return obj.ui;
        });

        return data;
    },

    model: function (attrs, options) {
        var tmpModel = null;
        var self = options.collection;
        attrs.translator = self.translator;
        // switch for model polymorphism.

        if (attrs.fieldType === Constants.FIELD_DATA_TYPE_TREE && attrs.dataType === Constants.FIELD_DATA_TYPE_TEXT) {
            self._allDeferreds.push(self._buildTreeImplementation(self, attrs));
            tmpModel = new Backbone.Model({ui: false});
        } else {
            if (attrs.id == Constants.FILTER_ID_DATE || (attrs.id.indexOf('-date') != -1) || (attrs.id.indexOf('date-') != -1)) {
                attrs.displayName = attrs.name;
                tmpModel = new YearsFilterModel(attrs);  // hacky but less hacky than enumerating them. Long term solution -> the endpoint should return a field telling the type of a field
            } else if (attrs.id == Constants.FILTER_ID_COMPUTED_YEAR) {
                attrs.displayName = attrs.name;
                tmpModel = new YearsOnlyFilterModel(attrs);
            } else {
                tmpModel = new GenericFilterModel(attrs);
                self._allDeferreds.push(tmpModel.getTree());
            }
        }
        return tmpModel;
    },

    _getGroup: function (definition, attrs) {
        var group = definition.name;
        if (attrs.id === Constants.FILTER_ID_ORGS && definition.name !== Constants.FILTER_NAME_DONOR) {
            group = Constants.ROLE;
        }
        if (attrs.id === Constants.FILTER_ID_SECTOR) {
            group = Constants.SECTORS;
        }
        if (attrs.id === Constants.FILTER_ID_PROGRAM) {
            group = Constants.PROGRAMS;
        }
        return group;
    },

    _buildTreeImplementation: function (self, attrs) {
        var url = attrs.endpoint;
        var deferred = $.Deferred();
        var tmpDeferreds = [];
        var self = this;

        $.get(url, function (data) {
            if (data && !_.isEmpty(data)) {
                var listDefinitions = data.listDefinitions;
                _.each(listDefinitions, function (def) {
                    var items = data.items[def.items];
                    var tree = self._createTree(items, def);
                    var tmpModel = new GenericFilterModel({
                        id: def.name || def.id,
                        data: tree,
                        name: def.displayName,
                        displayName: def.displayName,
                        originalName: def.name,
                        tab: (def.tab && def.tab !== Constants.UNASSIGNED) ? def.tab : attrs.tab,
                        ui: true,
                        group: self._getGroup(def, attrs),
                        empty: false
                    });

                    self.add(tmpModel);
                    tmpDeferreds.push(tmpModel.getTree());

                });
            }

            $.when.apply($, tmpDeferreds).then(function () {
                deferred.resolve();
            });
        });

        return deferred;
    },

    _createTree: function (data, definition) {
        var self = this;
        var tree = [];
        var dataCopy = jQuery.extend(true, [], data);
        var filterIds;
        if (definition.filterIds) {
            filterIds = definition.filterIds.filter(function (id) {
                return (id !== null && id !== '');
            });
            dataCopy = self._sortByName(dataCopy, filterIds[0]);
        }
        _.each(dataCopy, function (level1) {
            var level1 = $.extend({}, level1);
            if (filterIds) {
                level1.filterId = filterIds[0];
                level1.level = Constants.LEVEL_ONE;
                if (level1.children && level1.children.length > 0) {
                    level1.children = self._updateLevelData(level1, definition, Constants.LEVEL_TWO, filterIds[1]);
                    _.each(level1.children, function (level2) {
                        if (level2.children && level2.children.length > 0) {
                            level2.children = self._updateLevelData(level2, definition,
                                Constants.LEVEL_THREE, filterIds[2]);
                            _.each(level2.children, function (level3) {
                                if (level3.children && level3.children.length > 0 &&
                                    filterIds.length >= Constants.LEVEL_FOUR) {
                                    level3.children = self._updateLevelData(level3, definition,
                                        Constants.LEVEL_FOUR, filterIds[3]);
                                    _.each(level3.children, function (level4) {
                                        if (level4.children && level4.children.length > 0 &&
                                            filterIds.length >= Constants.LEVEL_FIVE) {
                                            level4.children = self._updateLevelData(level4, definition,
                                                Constants.LEVEL_FIVE, filterIds[4]);
                                            _.each(level4.children, function (level5) {
                                                if (level5.children && level5.children.length > 0 &&
                                                    filterIds.length >= Constants.LEVEL_SIX) {
                                                    level5.children = self._updateLevelData(level5, definition,
                                                        Constants.LEVEL_SIX, filterIds[5]);
                                                } else {
                                                    level5.children = null;
                                                }
                                            });
                                        } else {
                                            level4.children = null;
                                        }
                                    });
                                } else {
                                    level3.children = null;
                                }
                            });
                        }
                    });
                }
            }
            tree.push(level1);
        });
        return tree;
    },

    _updateLevelData: function (node, definition, level, filterId) {
        var self = this;
        var children = node.children.filter(function (item) {

            // if node is a group node but does not have any valid children omit it
            var hasChildren = self._hasValidChildren(item, definition, filterId);
            if (item.listDefinitionIds) {
                return _.include(item.listDefinitionIds,definition.id) && hasChildren;
            }

            return hasChildren;
        }).map(function (item) {
            item.filterId = filterId;
            item.level = level;
            return item;
        });

        return self._sortByName(children, filterId || definition.filterIds[0]);
    },

    _sortByName: function (items, filterId) {
        var self = this;
        return items.sort(function (a, b) {
            // ensure Undefined is always last
            if (a.id === UNDEFINED_ID) {
                return 1;
            }

            if (b.id === UNDEFINED_ID) {
                return -1;
            }

            // sort disaster-response-marker by id
            if (FILTERS_TO_SORT_BY_ID.indexOf(filterId) > -1) {
                return Collator.compare(a.id, b.id);
            }

            return Collator.compare(a.name, b.name);
        });
    },

    _hasValidChildren: function (node, definition, filterId) {
        var hasChildren = true;
        if (filterId && _.include(filterId, Constants.GROUP) && !_.include(filterId ,Constants.PLEDGES)) {
            if (node.children) {
                hasChildren = node.children.filter(function (child) {
                    return _.include(child.listDefinitionIds, definition.id)
                }).length > 0;

            } else {
                hasChildren = false;
            }
        }

        return hasChildren;
    }
});
