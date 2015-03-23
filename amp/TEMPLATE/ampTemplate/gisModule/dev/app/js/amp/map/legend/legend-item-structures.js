var fs = require('fs');
var _ = require('underscore');
var Backbone = require('backbone');
var Template = fs.readFileSync(__dirname + '/legend-item-structures.html', 'utf8');


module.exports = Backbone.View.extend({

  template: _.template(Template),
  className: 'legend-structure',


  render: function() {
    var self = this;
    //getStructuresWithActivities was null...
    self.model.structuresCollection.getStructuresWithActivities().then(function() {
      var renderObject = {
        status: 'loaded',
        colourBuckets: self.model.structuresCollection.palette.colours,
        selectedVertical: self.model.get('filterVertical')
      };
      
      //TODO: Move this code to a config class.
      var MAX_NUM_FOR_ICONS = 0;
      var useIconsForSectors = _.find(self.model.appData.settings.models, function(item) {
          return (item.id === 'use-icons-for-sectors-in-project-list');
      });
      if (useIconsForSectors !== undefined && useIconsForSectors.get('name') === 'true') {
          MAX_NUM_FOR_ICONS = 300;
      }
      // render icons if available
      if (self.model.structuresCollection.length < MAX_NUM_FOR_ICONS &&
          self.model.get('filterVertical') === 'Primary Sector Id') {
        renderObject.imageBuckets = self.model.iconMappings;
        renderObject.palletteElements = self.model.structuresCollection.palette.get('elements');
      }


      self.$el.html(self.template(_.extend({}, self.model.toJSON(), renderObject)));

      self.$('[data-toggle="popover"]').popover();

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
