var _ = require('underscore');

var BaseFilterModel = require('../models/base-filter-model');
var TreeNodeModel = require('../tree/tree-node-model');
var Constants = require('../utils/constants');


module.exports = BaseFilterModel.extend({

    initialize: function (options) {
        BaseFilterModel.prototype.initialize.apply(this, [options]);
        this.set('modelType', Constants.FIELD_DATA_TYPE_TREE);
        if (options.data) {
            this.set('data', options.data);
            this.getTree();
        }
    },

    // load tree if needed, else return what we already have..
    getTree: function () {
        var self = this;
        var loaded = this.get('_loaded');
        if (!loaded) {
            self.set('_loaded', this._createTree().then(function () {
                // trickle up numSelected.
                if (self.get('tree') != undefined) {
                    self.get('tree').on('change:numSelected', function (model, value) {
                        self.set('numSelected', value);
                        self.set('numPossible', self.get('tree').get('numPossible'));
                    });
                }

                return self.get('tree');
            }));

        }

        return this.get('_loaded');
    },

    getNumSelected: function () {
        var numSelected = this.get('numSelected');
        // if none selected, or all selected, treat the same.
        if (!numSelected || this.get('numSelected') === this.get('numPossible')) {
            return 0;
        } else {
            return numSelected;
        }
    },

    serialize: function (options) {
        var tree = this.get('tree');
        if (!tree) {
            //console.warn('no tree found', this.attributes);
            return {}; //no tree, nothing to serialize.
        } else {
            var tmpAry = tree.serialize(options);
            return tmpAry;
        }
    },

    deserialize: function (listOfSelected) {
        var self = this;
        var tree = this.get('tree');
        if (listOfSelected) {
            if (!tree) {
                console.warn('deserialize no tree found', self);
                return false; //no tree, nothing to serialize.
            } else {
                tree.deserialize(listOfSelected);
            }
        }
    },

    reset: function () {
        var tree = this.get('tree');
        if (tree && tree.get('numSelected') > 0) {
            // Locations need a special treatment because they can have parents selected without their children.
            if (tree._isLocation()) {
                this._resetLocation();
            } else {
                // force trigger, because otherwise nodes that are 'half-filled' but false won't refresh.
                tree.set('selected', true);
                tree.set('selected', false);
            }
        }
    },

    _resetLocation: function () {
        // Current value, not previous.
        if (this.get('tree').get('include-location-children') === false) {
            // Process as if it where checked and then revert the change.
            this.get('tree').set('include-location-children', true);
            while (this.get('tree')._hasAnySelectedChildren()) {
                this.get('tree').set('selected', true);
                this.get('tree').set('selected', false);
            }
            this.get('tree').set('include-location-children', false);
        } else {
            while (this.get('tree')._hasAnySelectedChildren()) {
                this.get('tree').set('selected', true);
                this.get('tree').set('selected', false);
            }
        }
    },

    _createTree: function () {
        var self = this;
        if (this.get('data')) {
            var deferred = new $.Deferred();
            this.createTreeNode(this.get('data'));
            deferred.resolve(this.get('tree'));
            return deferred.promise();

        } else {
            if (!this.url) {
                this.url = this.get('endpoint');
            }

            return this.fetch({
                type: this.get('method'),
                data: '{}'
            })
                .fail(function (jqXHR, textStatus, errorThrown) {
                    if (self.get('translator')) {
                        alert(self.get('translator').translateSync('amp.gis:error-loading-data', 'An error occcured while loading filters data') + ':' + self.get('name'));
                    } else {
                        alert('An error occcured while loading filters data:' + self.get('name'));
                    }
                    console.error('failed to get filter ', jqXHR, textStatus, errorThrown);
                });
        }

    },

    parse: function (data) {
        return this.createTreeNode(data);
    },

    createTreeNode: function (data) {
        var self = this;

        //if it's an obj, jam it into an array first, helps solve inconsistancy in API format.
        if (!_.isArray(data)) {
            data = data ? [data] : [];
        }

        var rootNodeObj = null;
        if (data.length === 1) {
            if (data[0].filterId && !data[0].name) {
                data[0].name = data[0].filterId;
            }
            if (data[0].values) {
                data[0].children = data[0].values;
            }

            data[0].isSelectable = false;
            rootNodeObj = data[0];
        } else {
            rootNodeObj = {
                id: -1,
                code: '-1',
                name: self.get('name'),
                originalName: self.get('originalName'),
                children: data,
                selected: undefined,
                expanded: false,
                isSelectable: false
            };
        }
        var treeModel = new TreeNodeModel(rootNodeObj);
        self.set('tree', treeModel);


    }
});

