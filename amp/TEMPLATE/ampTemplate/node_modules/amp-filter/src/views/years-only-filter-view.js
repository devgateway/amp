var fs = require('fs');
var _ = require('underscore');
var BaseFilterView = require('../views/base-filter-view');

var Template = fs.readFileSync(__dirname + '/../templates/years-only-template.html', 'utf8');

module.exports = BaseFilterView.extend({
    className: BaseFilterView.prototype.className + ' filter-years',
    template: _.template(Template),
    _loaded: null,
    initialize: function (options) {
        var self = this;
        this.filterView = options.filterView;
        BaseFilterView.prototype.initialize.apply(this, [options]);
        this.model = options.model;
        this.translator = options.translator;
        this.translate = options.translate;
        this._loaded = this.model.fetch().then(function () {
        });
        this.listenTo(this.model, 'change', this._updateTitle);
    },

    renderFilters: function () {
        var self = this;
        BaseFilterView.prototype.renderFilters.apply(this);
        this.$el.html(this.template(this.model.toJSON()));
        this._loaded.then(function () {
            self._renderSelector();
            // We need to re-translate some strings from the right panel.
            self.translate(self);
        });

        return this;
    },

    _renderSelector: function () {
        var self = this;
        var selector = this.$('#year-select');
        selector.append($("<option>").attr('value', '').text(''));
        _.each(this.model.get('items').values, function (item, i) {
            var option = $("<option>").attr('value', item.value).text(item.value);
            if (self.model.get('selectedYear') && item.value === self.model.get('selectedYear').toString()) {
                $(option).attr('selected', 'selected');
            }
            selector.append(option);
        });

        $(selector).on('change', function () {
            self.model.set('selectedYear', this.value);
        });
    },

    _updateTitle: function () {
        var selectedYear = this.model.get('selectedYear');
        this.$("#year-select").val(selectedYear);
        this.$titleEl.find('.filter-count').html(selectedYear);
    },

});
