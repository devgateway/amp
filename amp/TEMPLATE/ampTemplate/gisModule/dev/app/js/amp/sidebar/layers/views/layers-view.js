var fs = require('fs');
var _ = require('underscore');
var Backbone = require('backbone');
var $ = require('jquery');
var BaseControlView = require('../../base-control/base-control-view');
var Template = fs.readFileSync(__dirname + '/../templates/layers-template.html', 'utf8');
var IndicatorTemplate = fs.readFileSync(__dirname + '/../templates/indicator-template.html', 'utf8');
var IndicatorCollection = require('../collections/indicator-collection');


module.exports = BaseControlView.extend({
  id: 'tool-layers',
  title: 'Layers',
  iconClass: 'ampicon-layers',
  description: 'Tool desc, remove if possible.',

  template: _.template(Template),
  indicatorTemplate: _.template(IndicatorTemplate),

  initialize: function() {
    var self = this;
    BaseControlView.prototype.initialize.apply(this);

    // Get Indicators Collection and render...
    this.indicators =  new IndicatorCollection();
    this.indicators.fetch({reset:true});
    this.indicators.on("reset", this.renderIndicatorList, this);
  },

  render: function(){
    var self = this;
    BaseControlView.prototype.render.apply(this);

    // add content
    this.$('.content').html(this.template({title: this.title}));

    // Indicator listener
    this._addIndicatorListener();

    return this;
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
      indicator = self.indicators.find(function(model) { return model.get('id') == modelId; });
      Backbone.trigger('MAP_LOAD_INDICATOR', indicator);
    });
  },

  _addIndicatorListener: function(){
    var self = this;

    this.$('#indicatorLayers').change(function(evt){
      var indicatorEnabled = $(this).prop('checked');
      if(indicatorEnabled){
        self.$('.indicator-selector').show();
        var modelId = self.$('.indicator-selector input:radio:checked').val();
        indicator = self.indicators.find(function(model) { return model.get('id') == modelId; });
        Backbone.trigger('MAP_LOAD_INDICATOR', indicator);

      } else {
        Backbone.trigger('MAP_LOAD_INDICATOR', null);
        self.$('.indicator-selector').hide();
      }
    });
  }
});
