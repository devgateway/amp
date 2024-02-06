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
        // this = {}
        $.ajax({
            url: '/rest/filters/programs'
        }).done(function(data) {
            console.log("Programs Settings data", data)
            self = data;
        });
    },

});


module.exports = ProgramSettings;

