var fs = require('fs');
var _ = require('underscore');

var TreeNodeView = require('../tree/tree-node-view');
var BaseFilterView = require('../views/base-filter-view');
var Template = fs.readFileSync(__dirname + '/../templates/generic-filter-template.html', 'utf8');

// This is a generic model for filters. It assumes a tree structure.
// If you don't want a tree structure just extend base-filter
module.exports = BaseFilterView.extend({

    className: BaseFilterView.prototype.className + ' filter-generic',
    template: _.template(Template),
    _loaded: null,

    initialize: function (options) {
        var self = this;
        BaseFilterView.prototype.initialize.apply(this, [options]);

        this.model = options.model;
        this.translator = options.translator;
        this.translate = options.translate;
        this.treeView = new TreeNodeView({isRoot: true});

        // Create tree view
        // TODO: make tree loading content responsibility of model, not view...
        this.model.getTree().then(function (treeModel) {
            if (treeModel) {
                self._updateCountInMenu();
                treeModel.on('change:numSelected', function () {
                    self._updateCountInMenu();
                });
            } else {
                console.warn('no tree for: ', self.model);
            }
        });
    },

    onIncludeLocationChildrenChange: function (event) {
        if (event.target.checked) {
            this.$('.count').removeClass('hidden');
            this.model._resetLocation();
            this.model.get('tree').set('include-location-children', true);
        } else {
            this.model._resetLocation();
            this.$('.count').addClass('hidden');
            this.model.get('tree').set('include-location-children', false);
        }
    },

    searchKeyUp: function (event) {
        if (event.keyCode === 13 || // Pressed 'enter'
            this.$('.search-text').val() === '' ||
            this.$('.search-text').val().length > 1
        ) {
            this.model.get('tree').filterText(this.$('.search-text').val().toLowerCase());
        }
    },


    _updateCountInMenu: function () {
        if (this.$titleEl && this.model.get('tree')) {
            if (this.model.get('tree').get('numSelected') === this.model.get('tree').get('numPossible') ||
                this.model.get('tree').get('numSelected') === 0) {
                this.$titleEl.find('.filter-count').attr('data-i18n', 'amp.gis:pane-filters-all');
                this.$el.removeClass('active');
                this.translate(this.$titleEl.find('.filter-count').parent());
            } else {
                var normalBehavior = true;
                if (this.model.get('tree')._isLocationAndDontIncludeChildren() === true) {
                    this.$titleEl.find('.filter-count').text('');
                    normalBehavior = false;
                }
                if (normalBehavior) {
                    this.$titleEl.find('.filter-count').text(this.model.get('tree').get('numSelected') +
                        '/' +
                        this.model.get('tree').get('numPossible'));
                }
                this.$el.addClass('active');
                this.$titleEl.find('.filter-count').attr('data-i18n', '');
            }
        }
    },

    renderTitle: function () {
        BaseFilterView.prototype.renderTitle.apply(this);
        this._updateCountInMenu();
        return this;
    },

    renderFilters: function () {
        var self = this;
        BaseFilterView.prototype.renderFilters.apply(this);

        this.model.getTree().then(function (treeModel) {
            var json = self.model.toJSON();
            json.isLocationFilter = treeModel._isLocation();
            self.$el.html(self.template(json));

            // add event listeners
            self.$('input.search-text').on('keyup', function (event) {
                self.searchKeyUp(event);
            });
            self.$('.include-location-children').on('click', function (event) {
                self.onIncludeLocationChildrenChange(event);
            }); //return false to stop page refresh.

            if (treeModel) {
                self.$('.tree-container').append(self.treeView.render(self.model.get('tree'), self.model).$el);
                treeModel.set('expanded', true);
                treeModel.filterText('');//default no filter.
            } else {
                self.$('.tree-container').append("");
            }
        })
            .done(function () {
                self.translate(self);
            })
            .fail(function () {
                console.error('renderFilters failed :(');
            });

        return this;
    }

});

