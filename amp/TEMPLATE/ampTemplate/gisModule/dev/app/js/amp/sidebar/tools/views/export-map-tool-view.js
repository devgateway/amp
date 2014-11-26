var fs = require('fs');
var $ = require('jquery');
var _ = require('underscore');
var Backbone = require('backbone');

var Template = fs.readFileSync(__dirname + '/../templates/export-template.html', 'utf-8');


module.exports = Backbone.View.extend({

  template: _.template(Template),

  events: {
    'click .gis-tool-export': 'exportOption'
  },

  initialize: function(options) {
    this.app = options.app;
    this.savedMaps = options.app.data.savedMaps;
  },

  render: function() {
    // append is important because 'share' is inside same el
    this.$el.append(this.template());

    return this;
  },

  // can't call it export because that's a reserved word.
  exportOption: function(e) {
    $('#map-loading').show();
    var currentTarget = e.currentTarget;
    var exportType = $(currentTarget).data('type');

    var currentStateModel = this.savedMaps.create({  // create does POST
      title: 'export',
      description: 'export',
      stateBlob: this.app.state.freeze()
    });

    this.listenTo(currentStateModel, 'sync',
      function(model) {
        $('#map-loading').hide();
        this._getExport(model, exportType);
      });
  },

  // Download export
  _getExport: function(model, exportType) {
    var sUrl = '/rest/gis/export-map?mapId=' + model.id + '&exportType=' + exportType;
    window.open(sUrl, '_self');
  }

});
