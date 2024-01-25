/*/A
 * The GIS Data Engine
 * attaches to app.data
 */

var _ = require('underscore');
var $ = require('jquery');
var Backbone = require('backbone');



var GisSettings = function() {
    this.initialize.apply(this, arguments);
};


_.extend(GisSettings.prototype, Backbone.Events, {

    initialize: function() {
        this.gisSettings = {}
        $.ajax({
            url: '/rest/amp/settings/gis'
        }).done(function(data) {
            self.gisSettings = data;
        });
    },

});


module.exports = GisSettings;

