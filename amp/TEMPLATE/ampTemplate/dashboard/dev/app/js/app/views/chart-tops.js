var d3 = require('d3');
var ChartViewBase = require('./chart-view-base');
var ModalView = require('./chart-tops-info-modal');
var _ = require('underscore');

module.exports = ChartViewBase.extend({

  uiDefaults: {
    adjtype: 'FAKE'    
  },
  
  modalView: undefined,
  
  //Dont try to call initialize here because it throws a 'Module initialization error' :((
  /*initialize: function(options) {
	  this.modalView = new ModalView({ app: options.app, collection: this.model.collection });
  },*/

  downloadChartOptions: {
    trimLabels: false
  },

  getTTContent: function(context) {
	var ofTotal = app.translator.translateSync("amp.dashboard:of-total","of total");
	var units = app.translator.translateSync(app.settings.numberMultiplierDescription);
    var self = this;
    var currencyName = _.find(app.settings.get('1').get('options'), function(item) {return item.id === self.model.get('currency')}).value;
    return {tt: {
      heading: context.x.raw,
      bodyText: '<b>' + context.y.fmt + '</b> ' + currencyName + ' (' + units + ')',
      footerText: '<b>' + d3.format('%')(context.y.raw / this.model.get('total')) + '</b>&nbsp<span>' + ofTotal + '</span>'
    }};
  },

  chartClickHandler: function(context) {	  
    // clicking on the "others" bar loads five more.
    if (context.data[context.series.index]
               .values[context.x.index].special === 'others') {
      this.model.set('limit', this.model.get('limit') + 5);      
        this.model.set('big', true);      
    } else if (this.model.get('showCategoriesInfo') === true) {    	
    	this.modalView = new ModalView({ app: app, context: context, model: this.model });
    	this.openInfoWindow();    	    	
    }
  },
  
  openInfoWindow: function() {
	  var specialClass = 'dash-settings-modal';
	  this.app.modal('Category Detail', {
		  specialClass: specialClass,
	      bodyEl: this.modalView.render().el,
	      i18nTitle: 'amp.dashboard:dashboard-chart-tops-info-modal'
	  });	    
	  // Translate modal popup.
	  app.translator.translateDOM($("." + specialClass));
  }

});
