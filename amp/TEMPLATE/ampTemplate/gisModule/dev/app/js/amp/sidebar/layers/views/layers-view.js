var fs = require('fs');
var _ = require('underscore');
var Backbone = require('backbone');
var $ = require('jquery');
var BaseControlView = require('../../base-control/base-control-view');
var IndicatorCollection = require('../collections/indicator-collection');

var Template = fs.readFileSync(__dirname + '/../templates/layers-template.html', 'utf8');
var IndicatorTemplate = fs.readFileSync(__dirname + '/../templates/indicator-template.html', 'utf8');
var PointsTemplate = fs.readFileSync(__dirname + '/../templates/points-template.html', 'utf8');


module.exports = BaseControlView.extend({
  id: 'tool-layers',
  title: 'Layers',
  iconClass: 'ampicon-layers',
  description: 'Tool desc, remove if possible.',

  template: _.template(Template),
  indicatorTemplate: _.template(IndicatorTemplate),
  pointsTemplate: _.template(PointsTemplate),


  initialize: function() {
    var self = this;
    BaseControlView.prototype.initialize.apply(this);

    // Get Indicators Collection and render...
    this.indicators =  new IndicatorCollection();
    this.indicators.fetch({reset:true});

    //TODO: onTrigger.. ?confirm syntax with phil
    this.indicators.on('reset', this.renderIndicatorList, this);
  },

  render: function(){
    var self = this;
    BaseControlView.prototype.render.apply(this);

    // add content
    this.$('.content').html(this.template({title: this.title}));

    this.renderPointList();

    // Indicator listener
    this._addIndicatorListener();

    return this;
  },



  renderPointList: function(){
    var self = this;

    this.$('#point-selector').html(this.pointsTemplate());
    this._addPointsListener();

    // setup listener:
    this.$('#point-selector input:radio').change(function(){
      var val = $(this).val();
      if(val === 'aggregated'){
        self.$('.aggregate-group').show(); 
        val = self.$('.point-options .amp-uses:input:radio:checked').val();
      } else if(val === 'locations'){        
        self.$('.aggregate-group').hide(); 
      }
      Backbone.trigger('MAP_LOAD_POINT_LAYER', val);
    });
  },


  renderIndicatorList: function(){
    var self = this;
    this.$('.indicator-selector').html('');
    this.indicators.each(function(indicator){
      self.$('.indicator-selector').append(self.indicatorTemplate(indicator.toJSON()));
    });

    // setup listener:
    this.$('.indicator-selector input:radio').change(function(){
      var modelId = $(this).val();

      //dobule equal needed!!!
      var indicator = self.indicators.find(function(model) { return model.get('id') == modelId; });
      Backbone.trigger('MAP_LOAD_INDICATOR', indicator);
    });
  },

  _addIndicatorListener: function(){
    var self = this;

    // TODO: make this collapse, null behaviour generic so we can use it on both...
    this.$('#indicatorLayers').change(function(evt){
      var indicatorEnabled = $(this).prop('checked');
      if(indicatorEnabled){
        self.$('.indicator-selector').show();
        var modelId = self.$('.indicator-selector input:radio:checked').val();

      //dobule equal needed!!!
        var indicator = self.indicators.find(function(model) { return model.get('id') == modelId; });
        Backbone.trigger('MAP_LOAD_INDICATOR', indicator);

      } else {        
        Backbone.trigger('MAP_LOAD_INDICATOR', null);
        self.$('.indicator-selector').hide();
      }
    });
  },

  _addPointsListener: function(){
    var self = this;

    // TODO: make this collapse, null behaviour generic so we can use it on both...
    this.$('#point-layers').change(function(evt){
      var pointsEnabled = $(this).prop('checked');
      if(pointsEnabled){
        self.$('.point-options').show();
        var val = self.$('.point-options .amp-uses:input:radio:checked').val();
        Backbone.trigger('MAP_LOAD_POINT_LAYER', val);

      } else {        
        Backbone.trigger('MAP_LOAD_POINT_LAYER', null);
        self.$('.point-options').hide();
      }
    });
  },
});
