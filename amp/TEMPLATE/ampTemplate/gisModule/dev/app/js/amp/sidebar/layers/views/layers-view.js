var fs = require('fs');
var _ = require('underscore');
var Backbone = require('backbone');
var $ = require('jquery');
var state = require('../../../services/state');

var IndicatorCollection = require('../collections/indicator-collection');
var ProjectLayerCollection = require('../collections/project-layer-collection');

var BaseControlView = require('../../base-control/base-control-view');

var Template = fs.readFileSync(__dirname + '/../templates/layers-template.html', 'utf8');
var RadioOptionTemplate = fs.readFileSync(__dirname + '/../templates/radio-option-template.html', 'utf8');
var ProjectLayersTemplate = fs.readFileSync(__dirname + '/../templates/project-layer-template.html', 'utf8');


module.exports = BaseControlView.extend({
  id: 'tool-layers',
  title: 'Layers',
  iconClass: 'ampicon-layers',
  description: 'Select Points or Indicators to visualize data on the main map.',

  template: _.template(Template),
  radioOptionTemplate: _.template(RadioOptionTemplate),
  projectLayersTemplate: _.template(ProjectLayersTemplate),


  initialize: function() {
    var self = this;
    BaseControlView.prototype.initialize.apply(this);

    // Get Indicators Collection and render...
    this.indicators =  new IndicatorCollection();
    this.indicators.fetch({reset:true});

    this.listenTo(this.indicators, 'reset', this.renderIndicatorList);
    this._initProjectLayerCollection();

    // register state:    
    state.register(this, 'layers-view', {
      get: function(){ return this.projectLayerCollection.toJSON(); },
      set: function(obj){ if(obj){this.projectLayerCollection.set(obj);} },
      empty: null
    });
  },

  _initProjectLayerCollection: function(){
    this.projectLayerCollection = new ProjectLayerCollection([
      {
        selected: true,
        title: 'Projects by Region',
        value:'aggregate-adm1'
      },
      {
        title: 'Projects by District',
        value:'aggregate-adm2'
      },
      {
        title: 'Projects Sites',
        value:'locations',
        helpText: 'See individual project sites.'
      }
    ]);

    // TODO: on 'selected' change update ui...

  },

  render: function(){
    var self = this;
    BaseControlView.prototype.render.apply(this);

    // add content
    this.$('.content').html(this.template({title: this.title}));
    this.renderProjectList();

    // Indicator listener
    this._addIndicatorListener();

    return this;
  },


  renderProjectList: function(){
    var self = this;

    this.$('#point-selector').html(this.projectLayersTemplate());
    this.projectLayerCollection.each(function(model) {
      self.$('#project-layer-options').append(self.radioOptionTemplate(model.toJSON()));
    });

    this._addPointsListener();

    // setup listener:
    this.$('#point-selector input:radio').change(function(){
      var val = $(this).val();
      Backbone.trigger('MAP_LOAD_PROJECT_LAYER', val);
    });
  },

  _addPointsListener: function(){
    var self = this;


    this.$('#point-layers').change(function(evt){
      var pointsEnabled = $(this).prop('checked');
      if(pointsEnabled){
        self.$('#project-layer-options').show();
        var val = self.$('#project-layer-options input:radio:checked:visible').val();
        Backbone.trigger('MAP_LOAD_PROJECT_LAYER', val);

      } else {
        Backbone.trigger('MAP_LOAD_PROJECT_LAYER', null);
        self.$('#project-layer-options').hide();
      }
    });
  },

  // TODO: Move indicators to own view.
  renderIndicatorList: function(){
    var self = this;
    this.$('.indicator-selector').html('');
    this.indicators.each(function(indicator){
      self.$('.indicator-selector').append(self.radioOptionTemplate(indicator.toJSON()));
    });

    // setup listener:
    this.$('.indicator-selector input:radio').change(function(){
      var modelId = $(this).val();

      var indicator = self.indicators.find(function(model) { return model.get('id') === parseInt(modelId); });
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
        if(modelId){      
          var indicator = self.indicators.find(function(model) { return model.get('id') === parseInt(modelId); });
          Backbone.trigger('MAP_LOAD_INDICATOR', indicator);
        }
      } else {
        Backbone.trigger('MAP_LOAD_INDICATOR', null);
        self.$('.indicator-selector').hide();
      }
    });
  },

});
