var Backbone = require('backbone');
var _ = require('underscore');
var fs = require('fs');
var Template = fs.readFileSync(__dirname + '/../templates/admin-mylayers-template.html', 'utf8');
var IndicatorLayerManager = require('gis-layers-manager/src/index');

module.exports = Backbone.View.extend({
    id: 'tool-mylayers',
    title: 'My Layers',
    iconClass: 'ampicon-layers',
    template: _.template(Template),
    events: {
        'click .showDialog': 'showDialog'
    },

    initialize: function(options) {
    },

    render: function() {
        this.$el.html(this.template({title: this.title}));
        var self = this;
        this.gisLayersManager = new IndicatorLayerManager({
            draggable: true,
            caller: 'GIS',
            el: this.$('#layers-manager-popup')
        });
        this.gisLayersManager.on('cancel', function() {
            self.$('#layers-manager-popup').hide();
        });
        this.$('#layers-manager-popup').hide();
        return this;
    },

    showDialog: function (e) {
        this.gisLayersManager.show();
        this.$('#layers-manager-popup').show();
    }

});