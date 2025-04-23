var fs = require('fs');
var _ = require('underscore');
var Backbone = require('backbone');
var $ = require('jquery');

var Template = fs.readFileSync(__dirname + '/../tree/node-template.html', 'utf8');


var TreeNodeView = Backbone.View.extend({

    tagName: 'li',
    className: 'parent_li',
    template: _.template(Template),
    isRoot: false,
    initialize: function (options) {
        if (!_.isUndefined(options) && !_.isUndefined(options.isRoot)) {
            this.isRoot = options.isRoot;
        }

    },


    render: function (model) {
        this.model = model;
        this.model.set('hideCount', this.model._isLocationAndDontIncludeChildren() === true);
        if (model) {
            this.$el.html(this.template(model.toJSON()));
            this.renderChildren();
        }

        return this;
    },

    renderChildren: function () {
        var ul = $('<ul>');
        this.$el.append(ul);

        var children = this.model.get('children');
        if (!children.isEmpty()) {
            children.each(function (child) {
                var tmpView = new TreeNodeView();
                ul.append(tmpView.render(child).$el);
            });
        } else {
            this.$('.expanded').remove();
            this.$('> .node > .toggle-nav > .count').text('');
        }

        this._addModelListeners();
        this._addUIListeners();

        this._updateSelection();
        this._updateExpanded(ul);
    },

    _addModelListeners: function () {
        var self = this;

        //Add model listeneres
        this.model.on('change:selected', function () {
            self._updateSelection();
        });

        this.model.on('change:expanded', function () {
            self._updateExpanded();
        });

        this.model.on('change:numSelected', function () {
            self._updateCountUI();
        });

        this.model.on('change:visible', function () {
            self._updateVisibility();
        });
    },

    _addUIListeners: function () {
        var self = this;
        this.$('> .node > .selectable').on('click', function () {
            self.clickBox();

        });
        this.$('> .node > .toggle-nav').on('click', function () {
            self.clickName();
        });
    },

    _updateSelection: function () {
        this._updateCheckboxFill();
    },

    _updateCountUI: function () {
        if (!this.model.get('children').isEmpty()) {
            this.$('> .node > .toggle-nav > .count').text(
                '(' + this.model.get('numSelected') + ' / ' + this.model.get('numPossible') + ')');
            this._updateCheckboxFill();
        }
    },

    _updateVisibility: function () {
        if (this.model.get('visible')) {
            this.$el.show();
        } else {
            this.$el.hide();
        }
    },

    // For updating non-leaf nodes
    _updateCheckboxFill: function () {
        if (!this.model.get('children').isEmpty()) {
            // This a a branch node.
            if (this.model.get('numSelected') > 0) {
                if (this.model.get('numSelected') < this.model.get('numPossible')) {
                    if (this.model._isLocationAndDontIncludeChildren() === true && this.model.get('selected')) {
                        // Force update so parents on higher levels are correctly set to half-fill.
                        this.$('> .node > .selectable').addClass('half-fill');
                        this.$('> .node > .selectable').removeClass('selected');
                        this.$('> .node > .selectable').removeClass('half-fill');
                        this.$('> .node > .selectable').addClass('selected');
                    } else {
                        this.$('> .node > .selectable').addClass('half-fill');
                        this.$('> .node > .selectable').removeClass('selected');
                    }
                } else {
                    this.$('> .node > .selectable').removeClass('half-fill');
                    this.$('> .node > .selectable').addClass('selected');
                }
            } else {
                if (this.model._isLocationAndDontIncludeChildren() === true && this.model.get('selected')) {
                    // Force update so parents on higher levels are correctly set to half-fill.
                    this.$('> .node > .selectable').addClass('half-fill');
                    this.$('> .node > .selectable').removeClass('selected');
                    this.$('> .node > .selectable').removeClass('half-fill');
                    this.$('> .node > .selectable').addClass('selected');
                } else {
                    this.$('> .node > .selectable').removeClass('half-fill');
                    this.$('> .node > .selectable').removeClass('selected');
                }
            }
        } else { // else leaf node
            if (this.model.get('selected')) {
                this.$('> .node > .selectable').addClass('selected');
            } else {
                this.$('> .node > .selectable').removeClass('selected');
            }
        }
    },

    _updateExpanded: function (ul) {
        var iElement = this.$('> .node > .toggle-nav > .expanded');
        if (this.model.get('expanded')) {
            this.expand();
            iElement.text('-');
            iElement.addClass('open').removeClass('closed');
        } else {
            this.collapse();

            // to run on first time...need to use ul, since el is not on DOM yet
            if (ul) {
                ul.find('> li').hide();
            }

            iElement.text('+');
            iElement.addClass('closed').removeClass('open');
        }
    },


    clickBox: function () {
        if (this.isRoot) {
            this.model.set('selected', !this.model.get('selected'));
        } else {
            this.model.set('selected', !this.model.get('selected'), {
                propagation: true
            });
        }
    },

    clickName: function () {
        // if we have children expand
        if (!this.model.get('children').isEmpty()) {
            this.model.set('expanded', !this.model.get('expanded'));
        } else {
            // leaf node, so pretend the clicked on the box
            this.clickBox();
        }
    },


    collapse: function () {
        var children = this.$el.find(' > ul > li');
        children.hide('fast');

    },

    expand: function () {
        var children = this.$el.find(' > ul > li');
        children.show('fast');
    }

});

module.exports = TreeNodeView;
