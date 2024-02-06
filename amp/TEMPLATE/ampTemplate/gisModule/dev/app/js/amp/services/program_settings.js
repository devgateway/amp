/*/A
 * The GIS Data Engine
 * attaches to app.data
 */

var _ = require('underscore');
var $ = require('jquery');
var Backbone = require('backbone');



var ProgramSettings = function() {
    this.initialize.apply(this, arguments);
};


_.extend(ProgramSettings.prototype, Backbone.Events, {

    initialize: function() {
        var self = this;
        this.programs = {}
        $.ajax({
            url: '/rest/gis/program-settings'
        }).done(function(data) {
            console.log("Programs Settings data", data)
            self.programs = data;
        });
    },

});


module.exports = ProgramSettings;

