var fs = require('fs');
var $ = require('jquery');
var _ = require('underscore');
require('jquery-ui/draggable');
var BaseControlView = require('../../base-control/base-control-view');
var Template = fs.readFileSync(__dirname + '/../templates/layer-manager-template.html', 'utf8');
var IndicatorLayerManager = require('gis-layers-manager/src/index');

module.exports = BaseControlView.extend({
  id: 'tool-layer-manager',
  title: 'GIS Layer Manager',
  iconClass: 'layer-manager',
  description: 'Create indicator layers',
  

  events:{
    'click .accordion-heading': 'showLayerManager'
  },

  template: _.template(Template),

  initialize:function(options) {
    var self = this;
    this.app = options.app;
    BaseControlView.prototype.initialize.apply(this, arguments);
    this.indicatorLayerManager = new IndicatorLayerManager({
	      draggable: true,
	      caller: 'GIS'
	    });
  },

  render:function() {
    BaseControlView.prototype.render.apply(this);
    this.$('.content').html(this.template({title: this.title}));
    this.indicatorLayerManager.setElement(this.el.querySelector('#layer-manager-popup'));
    this._attachListeners();
    return this;
  },
  showLayerManager:function() {
    this.indicatorLayerManager.show(); 
    this.$('#layer-manager-popup').show();
  },
  
  _attachListeners: function() {
	    var self = this;    
	    this.indicatorLayerManager.on('cancel', function() {
	      self.$('.accordion-body').collapse('hide');
	      //self.$('#layer-manager-popup').hide();
	    });
   }

});
