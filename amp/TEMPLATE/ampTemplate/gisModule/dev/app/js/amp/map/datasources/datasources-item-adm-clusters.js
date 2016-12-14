var fs = require('fs');
var _ = require('underscore');
var chartUtils = require('../../../libs/local/chart-util');
var Backbone = require('backbone');
var Template = fs.readFileSync(__dirname + '/datasources-item-adm-clusters.html', 'utf8');


module.exports = Backbone.View.extend({
  tagName: 'tbody',

  template: _.template(Template),
  initialize: function(options) {
    this.app = options.app;

    _.bindAll(this, 'render');
  },


  render: function() {
	  var self = this;
	  this.collection.load().then(function() {

		  //TODO: inefficient to constantly redraw (if already on page), put in temp obj first.
		  // then only append once.
		  self.collection.each(function(project) {
			  // it joins on activity init, but for some reason it was being overridden...
			  // temp dirty force rejoin for now, otherwise use: getJoinedVersion          
			  var ampFormatter = new chartUtils.DecimalFormat(self.app.data.generalSettings.get('number-format'));          
			  // dec 31st, 2014 tried getjoinedversion INSTEAD OF tempDirtyForceJoin, but still doesn't work
			  project.tempDirtyForceJoin().then(function() {

				  // Get actual or planned based on funding type setting
				  var fundingType = 'Actual';
				  var selected = self.app.data.settingsWidget.definitions.getSelectedOrDefaultFundingTypeId();
				  if (selected.toLowerCase().indexOf('planned') >= 0) {
					  fundingType = 'Planned';
				  }



				  // Format values.
				  var formattedCommitments = ampFormatter.format(project.attributes[fundingType + ' Commitments']);
				  var formattedDisbursements = ampFormatter.format(project.attributes[fundingType + ' Disbursements']);
				  var currencyCode = self.app.data.settingsWidget.definitions.getSelectedOrDefaultCurrencyId();

				  // put them on the page.
				  self.$el.append(self.template({
					  activity: project.toJSON(),
					  formattedCommitments: [formattedCommitments ? formattedCommitments : 0, ' ', currencyCode].join(''),
					  formattedDisbursements: [formattedDisbursements ? formattedDisbursements : 0, ' ', currencyCode].join('')
				  }));
			  });
		  });

	  });
	  return this;
  }

});
