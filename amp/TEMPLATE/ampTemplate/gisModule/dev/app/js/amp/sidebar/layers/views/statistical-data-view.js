var fs = require('fs');
var _ = require('underscore');
var BaseControlView = require('../../base-control/base-control-view');
var FundingLayersView = require('./funding-layers-view');
var IndicatorLayersView = require('./indicator-layers-view');
var StatisticalLayersConfig = require('./statistical-layers-config');

var Template = fs.readFileSync(__dirname + '/../templates/multisection-layers-template.html', 'utf8');


module.exports = BaseControlView.extend({
  id: 'tool-layers-sd',
  title: 'Statistical Data',
  iconClass: 'ampicon-layers',
  subSections: [       
    FundingLayersView,
  ],
  radioButtonGroup: [],
  sections:[],
  template: _.template(Template),

  initialize: function() {
    BaseControlView.prototype.initialize.apply(this, arguments);  // sets this.app and this.baseTemplate
  },

  render: function() {
    // TODO: find a better way to keep our proxy collection up to date
    // Thad do you know a good pattern for this?
    BaseControlView.prototype.render.apply(this);
    this.$('.content').html(this.template({title: this.title}));
    //create layer manager
    var self = this;    

    self.app.data.indicators.loadAll().then(function() {  	
    	
    	self.addSection(StatisticalLayersConfig.STANDARD);
    	self.addSection(StatisticalLayersConfig.MY_LAYERS);
    	self.addSection(StatisticalLayersConfig.SHARED);
    	
    	_.each(self.subSections, function(SectionView) {
  	      /* Note: This object will access the radioButtonGroup
  	      * of this parent BaseControlView */
  	      var section = new SectionView({app: self.app, parent: self});    	      
  	      /* For Mutual Exclusion: */
  	      self.radioButtonGroup.push(section.collection);
  	      self.$('.content', self).append(section.render().el);
  	    });
  	
	 });     
    
    
    
    return this;
  },
  
  addSection: function(config){
		var self = this;
		var section = new IndicatorLayersView({app: self.app, parent: self, config: config});	      
	    self.radioButtonGroup.push(section.collection);
	    self.sections.push(section);
	    self.$('.content', self).append(section.render().el);
   }

});
