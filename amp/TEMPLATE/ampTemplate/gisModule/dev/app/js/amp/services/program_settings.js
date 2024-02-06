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
        this.settingsObject = {}
        $.ajax({
            url: '/rest/gis/program-settings'
        }).done(function(data) {
            console.log("Programs Settings data", data)

            // var programs = programsData.programs;
            // console.log("Settings",programs)
            if (data)
            {
                self.settingsObject.name="Program Type";
                self.settingsObject.id="program-setting";
                self.settingsObject.selected="National Plan Objective";
                self.settingsObject.options=[];
                data.forEach(function(lst) {
                    console.log(lst)
                    self.settingsObject.options.push({'id': lst.name, 'name': lst.name})
                });

            }
            // self.programs = data;
        });
    },

});


module.exports = ProgramSettings;

