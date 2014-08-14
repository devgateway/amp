var _ = require('underscore');
var Backbone = require('backbone');
var ProjectSitesModel = require('../models/project-sites-model');
var RadioMixin = require('../../mixins/radio-mixin');

var APIHelper = require('../../../libs/local/api-helper');


//TODO move projectLayerCollection to app.data
module.exports = Backbone.Collection
  .extend(RadioMixin)
  .extend({

  // LEGACY
  url: APIHelper.getAPIBase() + '/rest/gis/cluster',

  initialize : function(videos) {
    this.add([
      new ProjectSitesModel(),
      {
        title: 'Projects by Province',
        value: 'adm-1'
      },
      {
        title: 'Projects by District',
        value: 'adm-2'
      }
    ]);

    // LEGACY
    this.listenTo(this, 'change:selected', this._selectionChanged);
  },

  // LEGACY
  _selectionChanged: function(model, newSelectState) {
    // child is selected, disable rest, and emit
    // TODO: move to app.data and let layer listen to its changes
    if (_.contains(['adm-1', 'adm-2'], model.get('value'))) {
      if (newSelectState) {
        Backbone.trigger('MAP_LOAD_PROJECT_LAYER', model.get('value'));
      } else { // no children selected
        Backbone.trigger('MAP_LOAD_PROJECT_LAYER', null);
      }
    }
  }
});
