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
  title: 'Project Data',
  iconClass: 'ampicon-projects',
  description: 'Select Points or Indicators to visualize data on the main map.',

  template: _.template(Template),
  radioOptionTemplate: _.template(RadioOptionTemplate),


  initialize: function() {
    var self = this;
    BaseControlView.prototype.initialize.apply(this);


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


    return this;
  },


  renderProjectList: function(){
    var self = this;

    this.projectLayerCollection.each(function(model) {
      self.$('.layer-selector').append(self.radioOptionTemplate(model.toJSON()));
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

      var pointsEnabled = $(this).prop('checked');
      if(pointsEnabled){
        var val = $(this).val();
        Backbone.trigger('MAP_LOAD_PROJECT_LAYER', val);
      } else {
        Backbone.trigger('MAP_LOAD_PROJECT_LAYER', null);
      }
    });
  },


});
