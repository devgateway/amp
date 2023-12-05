var fs = require('fs');
var _ = require('underscore');
var $ = require('jquery');

var Backbone = require('backbone');
var TitleTemplate = fs.readFileSync(__dirname + '/../templates/filter-title-template.html', 'utf8');
var ContentTemplate = fs.readFileSync(__dirname + '/../templates/filters-top-level-template.html', 'utf8');

var GenericFilterView = require('../views/generic-filter-view');
var YearsFilterView = require('../views/years-filter-view');
var YearsOnlyFilterView = require('../views/years-only-filter-view');

var YearsFilterModel = require('../models/years-filter-model');
var YearsOnlyFilterModel = require('../models/years-only-filter-model');

//TODO: rename to 'group' to be consistent
// Parent base view for filters.
module.exports = Backbone.View.extend({
    className: 'tab-pane',
    titleTemplate: _.template(TitleTemplate),
    contentTemplate: _.template(ContentTemplate),
    filterCollection: null,
    viewList: [],
    name: 'tbd',

    initialize: function (options) {
        var self = this;
        this.name = options.name;
        this.translator = options.translator;
        this.translate = options.translate;
        this.filterView = options.filterView;
        this.filterCollection = new Backbone.Collection();

        this.filterCollection.comparator = function (model) {
            if (model.get('displayName')) {
                return model.get('displayName').toLowerCase();
            } else {
                return model.get('name').toLowerCase();
            }

        };

        this.filterCollection.on('change:numSelected', function () {
            self._refreshTitle();
        });
    },

    /**
     * renders the items in a tab
     * the first element of the tab's contents will be rendered IFF options.renderFirstElement has been specified
     */
    renderFilters: function (renderFirstElement) {
        var self = this;
        var view = null;
        var first = true;
        this.$el.attr('id', 'filter-pane-' + this.name.replace(/ /g, ''));
        this.$el.html(this.contentTemplate());
        // renders the tabs of the filter (one tab for each filterCollection element)
        this.filterCollection.each(function (filter) {
            if (filter instanceof YearsFilterModel) {
                view = new YearsFilterView({
                    model: filter,
                    el: self.$('.sub-filters-content'),
                    translator: self.translator,
                    translate: self.translate,
                    filterView: self.filterView
                });
                self.viewList.push(view);
            } else if (filter instanceof YearsOnlyFilterModel) {
                view = new YearsOnlyFilterView({
                    model: filter,
                    el: self.$('.sub-filters-content'),
                    translator: self.translator,
                    translate: self.translate,
                    filterView: self.filterView
                });
                self.viewList.push(view);
            } else {
                view = new GenericFilterView({
                    model: filter,
                    el: self.$('.sub-filters-content'),
                    translator: self.translator,
                    translate: self.translate
                });
                self.viewList.push(view);
            }

            var titleElem = view.renderTitle().$titleEl;
            self.$('.sub-filters-titles').append(titleElem);

            // hacky way to open first one for now.
            if (first && renderFirstElement) {
                first = false;
                view.renderFilters();
                self.$('.sub-filters-titles li:first').addClass('active');
            }
        });

        // We need to re-translate some strings from the right panel.
        self.translate(this);

        return this;
    },

    _refreshTitle: function () {
        var totalSelected = 0;
        var countStr = '';

        this.filterCollection.each(function (filter) {
            if (filter.getNumSelected) {
                totalSelected += filter.getNumSelected();
            }
        });

        if (totalSelected !== 0) {
            countStr = totalSelected;
        }

        // TODO: should be locally scoped, not global jquery.
        $('#' + this.name.replace(/ /g, '') + ' .title-count').text(countStr);
    },

    /**
     * renders the title of a tab
     */
    renderTitle: function () {
        this.titleEl = this.titleTemplate({name: this.name});
        return this;
    }
});
