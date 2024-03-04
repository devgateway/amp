var fs = require('fs');
var _ = require('underscore');
var chartUtils = require('../../../libs/local/chart-util');
var Backbone = require('backbone');
const GisSettings = require("../../services/gis_settings");
var Template = fs.readFileSync(__dirname + '/datasources-item-adm-clusters.html', 'utf8');
var gisSettings = new GisSettings();


module.exports = Backbone.View.extend({
  tagName: 'tbody',

  template: _.template(Template),
  initialize: function(options) {
    this.app = options.app;

    _.bindAll(this, 'render');
  },


  render: function() {
	  var self = this;	  
	  $.when(self.app.data.generalSettings.loaded, self.collection.load()).then(function() {		 	  
			  //TODO: inefficient to constantly redraw (if already on page), put in temp obj first.
			  // then only append once.
			  self.collection.each(function(project) {
				  // it joins on activity init, but for some reason it was being overridden...
				  // temp dirty force rejoin for now, otherwise use: getJoinedVersion          
				  var ampFormatter = new chartUtils.DecimalFormat(self.app.data.generalSettings.get('number-format'));          
				  // dec 31st, 2014 tried getjoinedversion INSTEAD OF tempDirtyForceJoin, but still doesn't work
                  project.appData = this.app.data;
				  project.tempDirtyForceJoin().then(function() {

					  // Get actual or planned based on funding type setting
					  var fundingType = 'Actual';
					  var selected = self.app.data.settingsWidget.definitions.getSelectedOrDefaultFundingTypeId();
					  if (selected.toLowerCase().indexOf('planned') >= 0) {
						  fundingType = 'Planned';
					  }

                      var orgColumn, columnName1, columnName2;

                      if (selected.toLowerCase().indexOf('ssc') >= 0) {
                          columnName1 = 'Bilateral SSC Commitments';
                          columnName2 = 'Triangular SSC Commitments';
                          orgColumn = 'executingNames';
                      } else {
                          columnName1 = fundingType + ' Commitments';
                          columnName2 = fundingType + ' Disbursements';
                          orgColumn = 'donorNames';
                      }

					  // Format values.
					  var formattedColumnName1 = ampFormatter.format(project.attributes[columnName1]);
					  var formattedColumnName2 = ampFormatter.format(project.attributes[columnName2]);
					  var currencyCode = self.app.data.settingsWidget.definitions.getSelectedOrDefaultCurrencyId();

                      var activity = project.toJSON()
                      var orgColumnName = activity[orgColumn];

                      // put them on the page.

                          self.$el.append(self.template({
                              gisSettings: gisSettings.gisSettings,
                              activity: activity,
                              orgColumnName: orgColumnName ? orgColumnName : '',
                              formattedColumnName1: [formattedColumnName1 ? formattedColumnName1 : 0, ' ', currencyCode].join(''),
                              formattedColumnName2: [formattedColumnName2 ? formattedColumnName2 : 0, ' ', currencyCode].join('')
                          }));

				 
			  });

		  });
	  });
	  
	  
	  return this;
  }

});
