var $ = require('jquery');
var Backbone = require('backbone');
var ProjectModel = Backbone.Model.extend({});
Backbone.$ = $;
module.exports = Backbone.Collection.extend({
    model: ProjectModel,
    url: 'https://ggw-dashboard.dgstg.org/api/amp-wocat/search',
    parse: function(response) {
        console.log("Response: " +response)
        return response.content;
    }
});
