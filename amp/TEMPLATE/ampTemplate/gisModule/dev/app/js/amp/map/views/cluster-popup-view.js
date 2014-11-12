var fs = require('fs');
var _ = require('underscore');
var $ = require('jquery');
var Backbone = require('backbone');
var d3 = require('d3-browserify');
var nvd3 = window.nv;
var util = require('../../../libs/local/chart-util');

var ProjectListTemplate = fs.readFileSync(__dirname + '/../templates/project-list-template.html', 'utf8');
var Template = fs.readFileSync(__dirname + '/../templates/cluster-popup-template.html', 'utf8');
var topsTooltipTemplate = _.template(fs.readFileSync(__dirname + '/../templates/tooltip-tops.html', 'UTF-8'));


// TODO: remove tempDOM and use this.$el
module.exports = Backbone.View.extend({
  template: _.template(Template),
  projectListTemplate: _.template(ProjectListTemplate),
  PAGE_SIZE: 50,
  _currentPage:0,

  tempDOM:null,

  initialize: function(options, popup, admLayer) {
    this.app = options.app;
    this.popup = popup;
    this.admLayer = admLayer;
  },

  render: function() {
    this.generateInfoWindow(this.popup, this.admLayer);
    return this;
  },


  generateInfoWindow: function(popup, admLayer) {
    var featureCollection = admLayer.get('features');
    this.cluster = _.find(featureCollection, function(feature) {
      return feature.properties.admName === popup._source._clusterId;
    });

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
    this._generateDonorChart();
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

  _generateBaseChart: function(model, selector) {
    var tmpTotal = 0;
    var data = _.map(model.values, function(org) {
      tmpTotal += org.amount;
      return {label: org.name, value: org.amount};
    });
    if ((model.total - tmpTotal) > 1) {
      data.push({ label: 'Other', value: (model.total - tmpTotal)});
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
        return topsTooltipTemplate({
          label: raw.point.label,
          value: d3.format(',')(Math.round(raw.value)),
          currency: model.currency,
          percent: d3.format('%')(raw.value / model.total)
        });
      });

      d3.select(selector)
          .datum(data)
          .transition().duration(350)
          .call(chart);

      return chart;
    });

  },

  _getTops: function(type) {
    var tmpModel = new Backbone.Collection({});
    tmpModel.url = '/rest/dashboard/tops/' + type;

    var payload = { limit: 5, adjtype:'ac'};
    _.extend(payload, this.app.data.filter.serialize());

    // get funding type, ask for consistancy form API, and at least put this function inside settings collection..
    var settings = this.app.data.settings.serialize();
    if (settings && settings[0]) {
      if (settings[0] === 'Actual Commitments') {
        payload.adjtype = 'ac';
      } else if (settings[0] === 'Actual Disbursements') {
        payload.adjtype = 'ad';
      } else if (settings[0] === 'Actual Expenditures') {
        payload.adjtype = 'ae';
      }
    }

    //API wants these in the url, but other params go in post, strange but it's the way it is...
    tmpModel.url += '?adjtype=' + payload.adjtype + '&limit=' + payload.limit;

    if (!payload.columnFilters) {
      payload.columnFilters = {};
    }
    payload.columnFilters['Activity Id'] = this.cluster.properties.activityid;

    return tmpModel.fetch({type:'POST', data:JSON.stringify(payload)});
  },


  _generateProjectList: function() {
    var self = this;
    this._currentPage = 0;

    this.tempDOM.find('.load-more').click(function() {
      self._currentPage++;
      self._loadMoreProjects();
    });

    return this._loadMoreProjects();
  },


  //TODO: should be done in data.adm cluster..then we can render full list if someone closes and reopens
  _loadMoreProjects: function() {
    var self = this;
    var startIndex = this._currentPage * this.PAGE_SIZE;
    var activityIDs = this.cluster.properties.activityid.slice(startIndex, startIndex + this.PAGE_SIZE);

    // hide load more button if all activities loaded.
    if (startIndex + this.PAGE_SIZE >= this.cluster.properties.activityid.length) {
      this.tempDOM.find('.load-more').hide();
    } else {
      this.tempDOM.find('.load-more').text('load more ' + (startIndex + this.PAGE_SIZE) + '/' +
                                           this.cluster.properties.activityid.length);
    }

    return this.app.data.activities.getActivities(activityIDs).then(function(activityCollection) {

      self.tempDOM.find('#projects-pane .loading').remove();
      self.tempDOM.find('.project-list').append(
        self.projectListTemplate({activities: activityCollection})
        );
    });
  }
});
