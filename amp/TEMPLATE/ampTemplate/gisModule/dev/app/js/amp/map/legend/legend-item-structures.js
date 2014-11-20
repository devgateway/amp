var fs = require('fs');
var $ = require('jquery');
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

      //TODO render icons if it's icons...
      // TODO: set 'selected' based on model..
      self.$el.html(self.template(_.extend({}, self.model.toJSON(), {
        status: 'loaded',
        colourBuckets: self.model.structuresCollection.palette.colours,
        selectedVertical: self.model.get('filterVertical')
      })));

      // add listener to select. Didn't work when i used 'events'
      // probably because happens after view populated...or translate strips events..
      self.$('select').change(function(evt) {
        var verticalID = self.$('option:selected').val();
        self.model.set('filterVertical', verticalID);
      });
    });

    return this;
  }

});
