var Deferred = require('jquery').Deferred;
var when = require('jquery').when;
var $ = require('jquery');
var _ = require('underscore');
var Backbone = require('backbone');
var LoadOnceMixin = require('../../mixins/load-once-mixin');
var L = require('../../../../../node_modules/esri-leaflet/dist/esri-leaflet.js');



module.exports = Backbone.Model
    .extend(LoadOnceMixin).extend({

        initialize: function () {
            // TODO: factor this behavior into an indicator base class
            this.listenTo(this, 'change:selected', function (blah, show) {
                this.trigger(show ? 'show' : 'hide', this);
            });
        },
        fetch: function () {
            if (!_(this).has('_wmsLoaded')) {
                this._wmsLoaded = new Deferred();;
                this.wmsLayer = new L.tileLayer.wms(this.get('link'), {
                    layers: this.get('layer'),
                    format: 'image/png',
                    transparent: true,
                    opacity: 0.75,
                    updateWhenIdle:true
                });
                this._wmsLoaded.resolveWith(this, [this, this.wmsLayer]);
            }
            return this._wmsLoaded.promise();
        },
        loadAll: function (options) {
            debugger;
            return when(this.load(options)).promise().done(function () {
                $('#map-loading').hide();
            });
        },
    });
