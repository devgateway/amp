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
            var listDefs = data.listDefinitions;

            if (listDefs) {
                self.settingsObject.name = "Program Type";
                self.settingsObject.id = "program-setting";
                self.settingsObject.selected = "National Planning Objective Level 1";
                self.settingsObject.options = [];
                listDefs.forEach( function (def)  {
                    const ids = def.filterIds;
                    console.log("IDS", ids)

                    if (ids) {

                        ids.forEach(function (id) {
                            self.settingsObject.options.push({'id': id, 'name': capitalizeSentence(id)})
                        });

                    }
                });

            }
        });
    },

});


function capitalizeSentence(sentence) {
    return sentence.toLowerCase().split('-').map(word => {
        return word.charAt(0).toUpperCase() + word.slice(1);
    }).join(' ');
}

module.exports = ProgramSettings;

