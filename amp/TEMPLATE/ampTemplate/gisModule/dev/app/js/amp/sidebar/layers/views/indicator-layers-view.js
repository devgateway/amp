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


module.exports = BaseControlView.extend({
  id: 'tool-layers',
  title: 'Statistical Data',
  iconClass: 'ampicon-layers',
  description: 'Country indicators.',

  template: _.template(Template),
  radioOptionTemplate: _.template(RadioOptionTemplate),


  initialize: function() {
    var self = this;
    BaseControlView.prototype.initialize.apply(this);

    // Get Indicators Collection and render...
    this.indicators =  new IndicatorCollection();
    this.indicators.fetch({reset:true});

    this.listenTo(this.indicators, 'reset', this.renderIndicatorList);

    // register state:    
    state.register(this, 'indicator-layers-view', {
      get: function(){ return this.projectLayerCollection.toJSON(); },
      set: function(obj){ if(obj){this.projectLayerCollection.set(obj);} },
      empty: null
    });
  },


  render: function(){
    var self = this;
    BaseControlView.prototype.render.apply(this);

    // add content
    this.$('.content').html(this.template({title: this.title}));

    return this;
  },



  renderIndicatorList: function(){
    var self = this;
    this.$('.layer-selector').html('');
    this.indicators.each(function(indicator){
      self.$('.layer-selector').append(self.radioOptionTemplate(indicator.toJSON()));
    });

    // setup listener:
    this.$('.layer-selector input:checkbox').change(function(){
      var currentTarget = $(this);

      // TODO: make sure doesn't trigger many 'null requests'
      //disable rest, mutually eclusive:
      self.$('.layer-selector input:checkbox').each(function(){
        if(currentTarget.val() !== $(this).val()){
          $(this).prop('checked',false);
        }
      });

      var isEnabled = $(this).prop('checked');
      if(isEnabled){
        var modelId = $(this).val();
        var indicator = self.indicators.find(function(model) { return model.get('id') === parseInt(modelId); });
        Backbone.trigger('MAP_LOAD_INDICATOR', indicator);
      } else {
        Backbone.trigger('MAP_LOAD_INDICATOR', null);
      }
    });
  },

});
