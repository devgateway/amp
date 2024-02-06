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
            url: '/rest/filters/programs'
        }).done(function(data) {
            console.log("Programs Settings data", data)
            // var listDefs = data.listDefinitions;
            // console.log("Defs", listDefs)
            // if (listDefs)
            // {
            //     self.settingsObject.name="Program Type";
            //     self.settingsObject.id="program-setting";
            //     self.settingsObject.selected="National Planning Objective";
            //     self.settingsObject.options=[];
            //     listDefs.forEach(function(listDef) {
            //         self.settingsObject.options.push({'id': listDef.name, 'name': listDef.name})
            //     });
            //
            // }
        });
    },

});


module.exports = ProgramSettings;

