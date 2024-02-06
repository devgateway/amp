var _ = require('underscore');
var $ = require('jquery');
var Backbone = require('backbone');

var ProgramSettings = function() {
    this.initialize.apply(this, arguments);
};

_.extend(ProgramSettings.prototype, Backbone.Events, {

    initialize: function() {
        var self = this;
        this.programSettings = {};
        $.ajax({
            url: '/rest/filters/programs'
        }).done(function(data) {
            // Parse the data as JSON
            try {
                self.programSettings = JSON.parse(data);
                console.log("Programs Settings data", self.programSettings);
            } catch (error) {
                console.error("Error parsing JSON:", error);
            }
        });
    },

});

module.exports = ProgramSettings;
