var _ = require('underscore');
var Backbone = require('backbone');


var Title = function() {
  this.initialize.apply(this, arguments);
};

_.extend(Title.prototype, Backbone.Events, {

  current: '',

  initialize: function(options) {
    this.data = options.data;

    this.listenTo(this.data, 'show hide', this.updateTitle);
  },

  updateTitle: function() {
    // currently this just joins all the titles of the layers on ", "
    // ... but it should probably be smarter.
    var self = this;
    var unwrappedTitles;
    var titles = this.data.getAllVisibleLayers().map(function(layer) {

      var titleList = {};
      var key = ['amp.gis:title-', layer.get('title').replace(/ /g, '')].join('');

      titleList[key] = layer.get('title');
      /* returns from map() like [{amp.gis:title-Region: 'Region'}, ... ] */
      return titleList;

    }).value();  // getLayers() returns a underscore chain()

    /* Convert from [{x:y},{z:a}] to {x:y,z:a} */
    if (!_.isEmpty(titles)) {
      unwrappedTitles = _.reduce(titles, function(memo, num) { 
    	  return _.extend(memo, num); 
      });
    } else {
      unwrappedTitles = {};
    }
    
    var copyOfTitles = _(unwrappedTitles).clone();    
    /*TODO remove window and use data, first pass data doesn't have translator? */
    var localizedTitleList = window.app.translator.translateList(unwrappedTitles);

    localizedTitleList.then(function(localTitles) {
      /* return localized title */    
    	
      //if no localized titles available, use original title
      var localTitles = _.object(_.map(localTitles, function (value, key) {
    	    return [key, _.isUndefined(value) ? copyOfTitles[key] : value];
    	}));
      
      self.current = _.reject(_.values(localTitles), function(ttle){ return _.isUndefined(ttle)}).join(', ');
      self.trigger('update', self.current);

    }).fail(function() {
      self.current = _.reject(_.values(titles), function(ttle){ return _.isUndefined(ttle)}).join(', ');    	
      self.trigger('update', self.current);
    });

  }

});


module.exports = Title;
