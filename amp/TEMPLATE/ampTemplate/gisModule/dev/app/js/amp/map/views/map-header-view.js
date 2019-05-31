var fs = require('fs');
var _ = require('underscore');
var Backbone = require('backbone');
var Template = fs.readFileSync(__dirname + '/../templates/map-header-info-template.html', 'utf8');


/**
 * Pertinent details contextualizing the data on the map.
 */
module.exports = Backbone.View.extend({

  className: 'map-header navbar-header navbar-brand',

  template: _.template(Template),

  initialize: function(options) {
    this.app = options.app;
    this.listenTo(this.app.data.title, 'update', this.render);
    this.listenTo(this.app.data.filter, 'apply', this.render);
  },

  render: function() {
    var title = this.app.data.title.current;
    var selectedFilters = this.app.data.filter.serialize();
    this.$el.html(this.template({title: title, dates: this.prepareDateFilters(selectedFilters.filters)}));
    return this;
  },
  prepareDateFilters: function(filters){
	  var dates = [];
	  if(filters && filters.date) {
          var isRtl =  app.data.generalSettings.get("rtl-direction");
          var language = app.data.generalSettings.get("language");
          var region =  app.data.generalSettings.get("region");
		  filters.date.start = filters.date.start || "";
		  filters.date.end = filters.date.end || "";		  
		  if(filters.date.start.length > 0){
			  var startDatePrefix = (filters.date.end.length === 0) ? this.app.translator.translateSync("amp.gis:date-from", "From ")  : "";
			  filters.date.start =  startDatePrefix  + " " + this.app.data.filter.formatDate(filters.date.start);
			  dates.push(TranslationManager.convertNumbersToEasternArabicIfNeeded(isRtl, language, region, filters.date.start));
		  }
		  if(filters.date.end.length > 0){
			  var endDatePrefix = (filters.date.start.length === 0) ? this.app.translator.translateSync("amp.gis:date-until", "Until ")  : "";			  
			  filters.date.end =  endDatePrefix + " " + this.app.data.filter.formatDate(filters.date.end);			  
			  dates.push(TranslationManager.convertNumbersToEasternArabicIfNeeded(isRtl, language, region, filters.date.end));
		  }
	  }
	  return dates	 
  }

});
