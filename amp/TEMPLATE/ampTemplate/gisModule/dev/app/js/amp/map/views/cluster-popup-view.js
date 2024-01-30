/* global app */
var fs = require('fs');
var _ = require('underscore');
var $ = require('jquery');
var Backbone = require('backbone');
var d3 = require('d3-browserify');
var nvd3 = window.nv;
var util = require('../../../libs/local/chart-util');
const GisSettings = require("../../services/gis_settings");

var ProjectListTemplate = fs.readFileSync(__dirname + '/../templates/project-list-template.html', 'utf8');
var Template = fs.readFileSync(__dirname + '/../templates/cluster-popup-template.html', 'utf8');
var topsTooltipTemplate = _.template(fs.readFileSync(__dirname + '/../templates/tooltip-tops.html', 'UTF-8'));
var gisSettings = new GisSettings();

//TODO: put cluster popup code in own folder,
// with seperate view for charts and table.
// TODO: remove tempDOM and use this.$el
module.exports = Backbone.View.extend({
  template: _.template(Template),
  projectListTemplate: _.template(ProjectListTemplate),
  PAGE_SIZE: 50,
  _currentPage: 0,

  tempDOM: null,

  initialize: function(options, popup, admLayer) {
    this.app = options.app;
    this.popup = popup;
    this.admLayer = admLayer;
  },

  render: function() {
    this.generateInfoWindow(this.popup, this.admLayer);
    app.translator.translateDOM($('.cluster-popup'));
    return this;
  },


  generateInfoWindow: function(popup, admLayer) {

              var featureCollection = admLayer.get('features');
              this.cluster = _.find(featureCollection, function (feature) {
                  return feature.properties.admName === popup._source._clusterId;
              });
              this.cluster.gisSettings = gisSettings.gisSettings;
              this.cluster.fundingType = this.app.data.settingsWidget.definitions.getSelectedOrDefaultFundingTypeId();
              // get appropriate cluster model:
              if (this.cluster) {
                  popup.setContent(this.template(this.cluster));
                  this.tempDOM = $(popup._contentNode);

                  this._generateCharts();
                  return this._generateProjectList(popup, this.cluster);
              } else {
                  console.error('no matching cluster: ', admLayer, popup._source._clusterId);
                  this.popup.setContent('error finding cluster');
              }

  },

  _generateCharts: function() {
    this._generateSectorChart();
    var selected = self.app.data.settingsWidget.definitions.getSelectedOrDefaultFundingTypeId();
      if (selected.toLowerCase().indexOf('ssc') >= 0) {
          this._generateExecutingChart();
      } else {
          this._generateDonorChart();
    }
  },

  _generateSectorChart: function() {
    var self = this;
    this._getTops('ps').then(function(data) {
      self.tempDOM.find('#charts-pane-sector .loading').remove();
      self._generateBaseChart(data, '#charts-pane-sector .amp-chart svg');
    });
  },

  _generateDonorChart: function() {
    var self = this;
    this._getTops('do').then(function(data) {
      self.tempDOM.find('#charts-pane-donor .loading').remove();
      self._generateBaseChart(data, '#charts-pane-donor .amp-chart svg');
    });
  },

  _generateExecutingChart: function() {
    var self = this;
    this._getTops('ea').then(function(data) {
      self.tempDOM.find('#charts-pane-donor .loading').remove();
      self._generateBaseChart(data, '#charts-pane-donor .amp-chart svg');
    });
  },

  _generateBaseChart: function(model, selector) {
    var tmpTotal = 0;
    var data = _.map(model.values, function(org) {
      tmpTotal += org.amount;
      return {label: org.name, value: org.amount};
    });
    if ((model.total - tmpTotal) > 1) {
      data.push({
         label: app.translator.translateSync('amp.gis:legend-others', 'Others'),
         value: (model.total - tmpTotal)
       });
    }

    nvd3.addGraph(function() {
      var chart = nvd3.models.pieChart()
          .valueFormat(util.formatKMB(3))
          .labelType('percent')
          .x(function(d) { return d.label; })
          .y(function(d) { return d.value; })
          .donut(true)
          .donutRatio(0.35)
          //.showLabels(true)
          .showLegend(false);
      chart.color(util.categoryColours(data.length));
      chart.tooltipContent(function(a, y, raw) {
          var isRtl = app.data.generalSettings.get("rtl-direction");
          var percentage = "";

          if ( raw.value> 0) {

              percentage = d3.format('f')(raw.value / model.total * 100);
              if (isRtl) {
                  percentage = '% ' +  percentage;
              } else {
                  percentage = percentage + ' %';
              }
          }
          return topsTooltipTemplate({
          label: raw.point.label,
          value: d3.format(',')(Math.round(raw.value)),
          currency: model.currency,
          percent: percentage,
          totalLegend: app.translator.translateSync('amp.gis.cluster.tooltip-of-total', 'of total')
        });
      });

      d3.select(selector)
          .datum(data)
          .transition().duration(350)
          .call(chart);
      	if(app.data.generalSettings.get("rtl-direction")) {
      		d3.select(selector).select('.nv-pieLabels').selectAll('text')[0].forEach(function (element) {
      			if (element.textContent.length > 0 && element.textContent.lastIndexOf("%")) {
      				element.textContent = "%" + element.textContent.substring(0, element.textContent.length - 1);
      			}
      		});
      	}

        return chart;
    });

  },

  _getTops: function(type) {
    var tmpModel = new Backbone.Collection({});
    tmpModel.url = '/rest/dashboard/tops/' + type;

    var payload = { limit: 5};
    _.extend(payload, this.app.data.filter.serialize());

    // get funding type, ask for consistancy form API, and at least put this function inside settings collection..
    var settings = this.app.data.settingsWidget.toAPIFormat(); 	

    _.extend(payload, {settings: settings});

    //API wants these in the url, but other params go in post, strange but it's the way it is...
    tmpModel.url += '?limit=' + payload.limit;
    payload.filters['activity-id'] = this.cluster.properties.activityid;    
    if (this.cluster.properties.admLevel) {
       payload.filters[this.cluster.properties.admLevel.toLowerCase()] = [this.cluster.properties.admId];
    }
    
    return tmpModel.fetch({type:'POST', data:JSON.stringify(payload)});
  },


  _generateProjectList: function(popup, cluster) {
    var self = this;
    this._currentPage = 0;

    this.tempDOM.find('.load-more').click(function() {
      self._currentPage++;
      self._loadMoreProjects(cluster);
    });

    this._updatePlannedActualUI();

    return this._loadMoreProjects(cluster);
  },

  // If any of the 'planned' funding types are selected then the
  // table should show planned comitments and dispursements,
  // otherwise show actual values.
  _updatePlannedActualUI: function() {
	  var self = this;       
	  var selected = self.app.data.settingsWidget.definitions.getSelectedOrDefaultFundingTypeId();
      self.tempDOM.find('.setting-scc').hide();
      self.tempDOM.find('.setting-executings').hide();
	  if (selected.toLowerCase().indexOf('ssc') >= 0) {
          self.tempDOM.find('.setting-actual').hide();
          self.tempDOM.find('.setting-planned').hide();
          self.tempDOM.find('.setting-donors').hide();
          self.tempDOM.find('.setting-executings').show();
          self.tempDOM.find('.setting-scc').show();
      } else if (selected.toLowerCase().indexOf('planned') >= 0) {
		  self.tempDOM.find('.setting-actual').hide();
		  self.tempDOM.find('.setting-planned').show();
	  } else {
		  self.tempDOM.find('.setting-actual').show();
		  self.tempDOM.find('.setting-planned').hide();
	  }    
  },



  //TODO: should be done in data.adm cluster..then we can cache for if someone closes and reopens
  _loadMoreProjects: function(cluster) {
	  var self = this;
	  var startIndex = this._currentPage * this.PAGE_SIZE;
	  var activityIDs = this.cluster.properties.activityid.slice(startIndex, startIndex + this.PAGE_SIZE);

	  // hide load more button if all activities loaded.
	  if (startIndex + this.PAGE_SIZE >= this.cluster.properties.activityid.length) {
		  this.tempDOM.find('.load-more').hide();
	  } else {
		  this.tempDOM.find('.load-more').html('<span data-i18n="amp.gis:popup-loadmore">load more</span> ' +
				  (startIndex + this.PAGE_SIZE) + '/' + this.cluster.properties.activityid.length);
	  }
      
	  return this.app.data.activities.getActivitiesforLocation(activityIDs, cluster.properties.admLevel, cluster.properties.admId).then(function(activityCollection) {        
		  self.tempDOM.find('#projects-pane .loading').remove();
		  /* Format the numerical columns */
		  var ampFormatter = new util.DecimalFormat(self.app.data.generalSettings.get('number-format'));
		  var currencyCode = self.app.data.settingsWidget.definitions.getSelectedOrDefaultCurrencyId();
		  var fundingType = 'Actual';
		  var selected = self.app.data.settingsWidget.definitions.getSelectedOrDefaultFundingTypeId();
		  if (selected.toLowerCase().indexOf('planned') >= 0) {
			  fundingType = 'Planned';
		  }

          var columnName1, columnName2;

          if (selected.toLowerCase().indexOf('ssc') >= 0) {
			  columnName1 = 'Bilateral SSC Commitments';
              columnName2 = 'Triangular SSC Commitments';
		  } else {
              columnName1 = fundingType + ' Commitments';
              columnName2 = fundingType + ' Disbursements';
          }

		  var activityFormatted = _.map(activityCollection, function(activity) {             
			  var formattedColumnName1 = ampFormatter.format(activity.get(columnName1));
			  var formattedColumnName2 = ampFormatter.format(activity.get(columnName2));

			  //TODO: should be done elsewhere, for example in toJSON or parse.
			  activity.set('formattedColumnName1', [formattedColumnName1 ? formattedColumnName1 : 0, ' ', currencyCode].join(''));
			  activity.set('formattedColumnName2', [formattedColumnName2 ? formattedColumnName2 : 0, ' ', currencyCode].join(''));
			  return activity;
		  });

		  self.tempDOM.find('.project-list').append(
				  self.projectListTemplate({activities: activityFormatted})
		  );

	  });
  }
});
