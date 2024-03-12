var Deferred = require('jquery').Deferred;
var _ = require('underscore');
var $ = require('jquery');
var Backbone = require('backbone');
var LoadOnceMixin = require('../../mixins/load-once-mixin');
var L = require('../../../../../node_modules/esri-leaflet/dist/esri-leaflet.js');


module.exports = Backbone.Model
.extend(LoadOnceMixin).extend({

  initialize: function() {
    // TODO: factor this behavior into an indicator base class
    this.listenTo(this, 'change:selected', function(blah, show) {
      this.trigger(show ? 'show' : 'hide', this);
    });

    // We listen to the "apply" event on filter widget and trigger a "filter" event that will be listened by our view.
    // This way the view will also receive this model as parameter.
    //TODO this is the same event we listen for indicator-join-model. Created AMP-29430 as followup to create
    // a base indicator class
    this.listenTo(app.data.filter, 'apply', function(blah, show) {
      this.trigger('applyFilter', this);
    });
    this.listenTo(app.data.settingsWidget, 'applySettings', function(blah, show) {
      this.trigger('applySettings', this);
    });
  },

  fetch: function() {
    if (!_(this).has('_esriLoaded')) {
      this._esriLoaded = new Deferred();
      this.esriLayer = new L.esri.DynamicMapLayer({url:this.get('link'),
        opacity: 0.5
      });
      this._esriLoaded.resolveWith(this, [this, this.esriLayer]);
    }

    // hide loading icon after layer loads.
    this.listenTo(this.esriLayer, 'load', function() {
      $('#map-loading').hide();
    });

    return this._esriLoaded.promise();
  },

  getLegend: function() {
    if (!this._loadedLegend) {
      this._loadedLegend = $.ajax({
        url: this.get('link') + '/legend',
        data:{f: 'pjson'},
        dataType: 'jsonp'
      });
    }

    return this._loadedLegend;
  }

});


