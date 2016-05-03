var fs = require('fs');
var $ = require('jquery');
var _ = require('underscore');

var BaseControlView = require('../../base-control/base-control-view');
var ShareMapToolView = require('./share-map-tool-view');
var ExportMapToolView = require('./export-map-tool-view');

var Template = fs.readFileSync(__dirname + '/../templates/tools-template.html', 'utf8');


module.exports = BaseControlView.extend({

  id: 'tool-tools',
  title: 'Share',
  iconClass: 'ampicon-share',
  description: 'Download the data in this map or share a link to it',

  template:  _.template(Template),

  events: {
    'click .gis-tool-share': 'share'
  },

  initialize: function() {
    BaseControlView.prototype.initialize.apply(this, arguments);  // attaches this.app, etc.
    this.savedMaps = this.app.data.savedMaps;

  },

  render: function() {
	var self = this;
    BaseControlView.prototype.render.apply(this);
    var renderedTemplate = $(this.template({title: this.title}));

    // needed to wait until 'el' exists before creating and rendering, or events break.
    
    
	  this.app.data.settings.load().then(function(){
		  self.app.data.user.load().then(function() {		   
			  var hideEditableFormatSetting = _.find(self.app.data.settings.models, function(item) {
				  return item.get('id') === 'hide-editable-export-formats-public-view';
			  });
			  if(!(hideEditableFormatSetting !== undefined && hideEditableFormatSetting.get('defaultId') == "true" && self.app.data.user.get("logged") == false)){
				  self.exportMapToolView = new ExportMapToolView({app: self.app, el:renderedTemplate.find('.form-group')});
				  self.exportMapToolView.render();
			  }  
		  });
		});
	  
    

    this.$('.content').html(renderedTemplate);

    return this;
  },  
  loadSerialized: function(serializedState) {
    var stateBlob = this.savedMaps.model.deserializese(serializedState);
    this.app.state.load(stateBlob);
  },


  share: function() {
    $('#map-loading').show();
    var currentStateModel = this.savedMaps.create({  // create does POST
      title: this.$('#save-title').val(),
      description: this.$('#save-desc').val(),
      stateBlob: this.app.state.freeze()
    });
    this.show(new ShareMapToolView({
      app: this.app,
      model: currentStateModel
    }), '.gis-tool-share');
  },

  show: function(subView, toolClass) {
    this.$('.gis-tool').removeClass('active');
    this.$(toolClass).addClass('active');
    this.$('.gis-tool-share-form').html(subView.render().el);
  }

});
