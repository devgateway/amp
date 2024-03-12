var fs = require('fs');
var $ = require('jquery');
var _ = require('underscore');
var Backbone = require('backbone');

var Template = fs.readFileSync(__dirname + '/../templates/export-template.html', 'utf-8');


module.exports = Backbone.View.extend({

  template: _.template(Template),

  events: {
    'click .gis-tool-export': 'exportOption'
  },

  initialize: function(options) {
    this.app = options.app;
    this.savedMaps = options.app.data.savedMaps;
  },

  render: function() {
	  var self = this;
  	  var foundPS = self.app.data.generalSettings.get('project-sites');         
        if (foundPS !== true) {
        	//need to remove project-sites
        	//find the index of project-sites in projectLayerCollection
//        	var index = undefined;
//        	for (i = 0; i < self.projectLayerCollection.models.length; i++ ) {
//        		if (self.projectLayerCollection.models[i].attributes.title === 'Project Sites') {
//        			index = i;
//        		}
//        	}
//        	self.projectLayerCollection.models.splice(index, 1);
    	    // append is important because 'share' is inside same el
//        	var value = $(self.template()).find('button.hideable').css('display','none').html();
        	
        	self.$el.append(self.template());        	
        	self.$el.find('button.hideable').css('display', 'none')
        } else {
    	    // append is important because 'share' is inside same el
    	    self.$el.append(self.template());        	
        }
  		
        self.app.translator.translateDOM(self.el);  	
        return this;
  },

  // can't call it export because that's a reserved word.
  exportOption: function(e) {
    $('#map-loading').show();
    var self = this;
    //add deferrance here -- have to wait for filters to load
    $.when(self.app.state.filtersLoaded().then(
			  //success handler
			  function(status){
				  
				    var currentTarget = e.currentTarget;
				    var exportType = $(currentTarget).data('type');
				    var currentStateModel =  self.savedMaps.create({  // create does POST
				        title: 'export',
				        description: 'export',
				        stateBlob: self.app.state.freeze() 
				      });
				    self.listenTo(currentStateModel, 'sync',
				    	      function(model) {
				    	        $('#map-loading').hide();
				    	        self._getExport(model, exportType);
				    	      });
			  }, 
			  //failure handler
			  function(status){
				  alert('fail');
			  },
			  //pending handler
			  function(status){
				    $('#map-loading').show();
			  })
    		); 
    	
    	
    	
    

  },

  // Download export
  _getExport: function(model, exportType) {
    var sUrl = '/rest/gis/export-map?mapId=' + model.id + '&exportType=' + exportType;
    window.open(sUrl, '_self');
  }

});
