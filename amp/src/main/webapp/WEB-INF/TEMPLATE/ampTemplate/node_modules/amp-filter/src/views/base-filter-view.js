var fs = require('fs');
var _ = require('underscore');
var $ = require('jquery');

var Backbone = require('backbone');
var TitleTemplate = fs.readFileSync(__dirname + '/../templates/subfilter-title-template.html', 'utf8');
var ContentTemplate = fs.readFileSync(__dirname + '/../templates/filters-content-template.html', 'utf8');

// Parent base view for filters.
module.exports = Backbone.View.extend({

    className: 'filter-type',

    titleTemplate: _.template(TitleTemplate),
    contentTemplate: _.template(ContentTemplate),

    initialize: function (options) {
        this.app = options.app;
    },

    renderFilters: function () {

    },

    renderTitle: function () {
        var self = this;
        if (this.model.get('name')) {
            this.titleEl = this.titleTemplate(this.model.toJSON());
            this.$titleEl = $(this.titleEl);
            this.$titleEl.on('click', function (evt) {
                $(this).siblings().removeClass('active');
                $(this).addClass('active');
                self.$el.html('');
                self.renderFilters();
                return false;
            });
        }

        return this;
    }

});
