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
  	this.app.data.settings.load().then(function(){
  		
        var foundPS = _.find(self.app.data.settings.models, function(item) {
            return item.get('id') === 'project-sites';
          });

        //occasionally, foundPS.attributes.selected will be undefined, so we have to dig through the attributes
        if (foundPS !== undefined && foundPS.attributes.options[0].value !== 'true') {
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
  		
  		
  	});
    return this;
  },

  // can't call it export because that's a reserved word.
  exportOption: function(e) {
    $('#map-loading').show();
    var currentTarget = e.currentTarget;
    var exportType = $(currentTarget).data('type');

    var currentStateModel = this.savedMaps.create({  // create does POST
      title: 'export',
      description: 'export',
      stateBlob: this.app.state.freeze()
    });

    this.listenTo(currentStateModel, 'sync',
      function(model) {
        $('#map-loading').hide();
        this._getExport(model, exportType);
      });
  },

  // Download export
  _getExport: function(model, exportType) {
    var sUrl = '/rest/gis/export-map?mapId=' + model.id + '&exportType=' + exportType;
    window.open(sUrl, '_self');
  }

});
