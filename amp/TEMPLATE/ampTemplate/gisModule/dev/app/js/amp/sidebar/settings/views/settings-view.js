var fs = require('fs');
var _ = require('underscore');
var $ = require('jquery');
var BaseControlView = require('../../base-control/base-control-view');
var Template = fs.readFileSync(__dirname + '/../templates/settings-template.html', 'utf8');



module.exports = BaseControlView.extend({
  id: 'tool-settings',
  title:  'Settings',
  iconClass:  'ampicon-settings',
  description:  '',
  selectedCurrency: null,
  template:  _.template(Template),
  events:{
   
  },

  initialize: function(options) {
    var self = this;
    BaseControlView.prototype.initialize.apply(this, arguments);
    this.app = options.app;
    self.app.data.state.register(this, 'settings', {
          get: self.app.data.settingsWidget.toAPIFormat,
          set: self.app.data.settingsWidget.restoreFromSaved,
          empty: null
        });
   
    
  },

  render: function() {
    var self = this;
    BaseControlView.prototype.render.apply(this);

    self.$('.content').html(this.template({title: this.title}));
    // add content    
       
    this.app.data.settingsWidget.setElement(this.el.querySelector('#amp-settings'));
	this.app.data.settingsWidget.show();
	this.$('#amp-settings').show();	
	this.app.data.settingsWidget.on('close', function() {		
	});
	
	this.app.data.settingsWidget.on('applySettings', function() {	
		$('#map-loading').show();		
	});		
	
    return this;
  }

});

