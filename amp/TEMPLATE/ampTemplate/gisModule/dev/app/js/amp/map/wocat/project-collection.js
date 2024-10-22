var $ = require('jquery');
var Backbone = require('backbone');
const AMP_WOCAT_API= 'https://ggw-dashboard.dgstg.org/api/amp-wocat/search';
var ProjectModel = Backbone.Model.extend({});
Backbone.$ = $;
module.exports = Backbone.Collection.extend({
    model: ProjectModel,

    // Override the URL function to include query parameters
    url: function() {
        var size = this.size || 10;
        var page = this.page || 1;
        return AMP_WOCAT_API + '?size=' + size+'&page=' + page;
    },

    parse: function(response) {
        this.currentPage = response.currentPage;
        this.totalPages = response.totalPages;
        this.pageSize = response.pageSize;
        this.totalElements = response.totalElements;
        return response.content;
    },

    initialize: function(models, options) {
        if (options && options.size) {
            this.size = options.size;
        }
        if (options && options.page) {
            this.page = options.page;
        }
    }
});
