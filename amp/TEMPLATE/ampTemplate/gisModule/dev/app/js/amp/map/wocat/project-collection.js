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
        return AMP_WOCAT_API + '?size=' + size;
    },

    parse: function(response) {
        return response;  // Return the content from the API response
    },

    initialize: function(models, options) {
        if (options && options.size) {
            this.size = options.size;
        }
    }
});
