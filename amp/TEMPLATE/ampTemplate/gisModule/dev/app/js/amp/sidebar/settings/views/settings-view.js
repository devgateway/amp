var fs = require('fs');
var _ = require('underscore');
var $ = require('jquery');
var BaseControlView = require('../../base-control/base-control-view');
var Template = fs.readFileSync(__dirname + '/../templates/settings-template.html', 'utf8');
const ProgramSettings = require('../../../services/program_settings')
var programSettings = new ProgramSettings();


module.exports = BaseControlView.extend({
  id: 'tool-settings',
  title:  'Settings',
    settingsObject:  {},
  iconClass:  'ampicon-settings',
  description:  '',
  selectedCurrency: null,
  template:  _.template(Template),
  events:{
   
  },

  initialize: function(options) {
    var self = this;
      console.log("Settings",programSettings)
      if (programSettings.listDefinitions)
    {
        self.settingsObject.name="Program Type";
        self.settingsObject.id="program-setting";
        self.settingsObject.selected="National Planning Objective";
        self.settingsObject.options=[];
        programSettings.listDefinitions.forEach(function(listDef) {
            self.settingsObject.options.push({'id': listDef.name, 'name': listDef.name})
        });

    }
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

    self.$('.content').html(this.template({title: this.title,settingsObject:this.settingsObject}));
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

