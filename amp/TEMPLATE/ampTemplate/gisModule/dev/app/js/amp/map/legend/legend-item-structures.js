/* global app */
var fs = require('fs');
var _ = require('underscore');
var Backbone = require('backbone');
var Template = fs.readFileSync(__dirname + '/legend-item-structures.html', 'utf8');
var SettingsUtils = require('../../../libs/local/settings-utils.js');

module.exports = Backbone.View.extend({

  template: _.template(Template),
  className: 'legend-structure',

  initialize: function(options) {
    this.app = options.app;
  },


  render: function() {
    var self = this;
    //getStructuresWithActivities was null...
    self.model.structuresCollection.getStructuresWithActivities().then(function() {
      var renderObject = {
        status: 'loaded',
        colourBuckets: self.model.structuresCollection.palette.colours,
        selectedVertical: self.model.get('filterVertical')
      };
      
      var maxNumberOfIcons = SettingsUtils.getMaxNumberOfIcons(app.data.settings);
      // render icons if available
      if (self.model.structuresCollection.length < maxNumberOfIcons &&
        self.model.get('filterVertical') === 'Primary Sector') {
        renderObject.imageBuckets = self.model.iconMappings;
        renderObject.palletteElements = self.model.structuresCollection.palette.get('elements');
      }

      self.app.translator.promise.then(function() {
        self.app.translator.translateDOM(
          self.template(_.extend({}, self.model.toJSON(), renderObject))
        ).then(function(legend) {
          self.$el.html(legend);
        });

      });
      self.app.translator.translateList({
        'amp.gis:legend-popover': 'If there are less than',
        'amp.gis:legend-popover-2': 'points map will show icons otherwise: show coloured circles.',
        'amp.gis:title-Region': 'Region'
      }).then(function(legendPopoverList) {
        var legendPopover = [legendPopoverList['amp.gis:legend-popover'],
          ' ',
          maxNumberOfIcons,
          ' ',
          legendPopoverList['amp.gis:legend-popover-2']
        ].join('');
        self.$('[data-toggle="popover"]').popover();
        self.$('[data-toggle="popover"]').attr('data-content', legendPopover);
      });




      // add listener to select. Didn't work when i used 'events'
      // probably because happens after view populated...or translate strips events..
      self.$('select').change(function() {
        var verticalID = self.$('option:selected').val();
        self.model.set('filterVertical', verticalID);
      });
    });

    return this;
  }

});
