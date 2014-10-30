var fs = require('fs');
var _ = require('underscore');
var $ = require('jquery');
var Backbone = require('backbone');
var d3 = require('d3-browserify');
var nvd3 = window.nv;

var ProjectListTemplate = fs.readFileSync(__dirname + '/../templates/project-list-template.html', 'utf8');
var Template = fs.readFileSync(__dirname + '/../templates/cluster-popup-template.html', 'utf8');

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

    var exampleData = [
      { label: 'Health', value: Math.round(Math.random() * this.cluster.properties.activityid.length / 5) },
      { label: 'Agriculture', value: Math.round(Math.random() * this.cluster.properties.activityid.length / 5) },
      { label: 'Science', value: Math.round(Math.random() * this.cluster.properties.activityid.length / 5) },
      { label: 'Transport', value: Math.round(Math.random() * this.cluster.properties.activityid.length / 5)},
      { label: 'Planning', value: Math.round(Math.random() * this.cluster.properties.activityid.length / 5)}
    ];
    this._generateBaseChart(exampleData, '#charts-pane-sector .amp-chart svg');
  },

  _generateDonorChart: function() {

    var exampleData = [
      { label: 'U.N', value: Math.round(Math.random() * this.cluster.properties.activityid.length / 5) },
      { label: 'World Bank', value: Math.round(Math.random() * this.cluster.properties.activityid.length / 5) },
      { label: 'UNICEF', value: Math.round(Math.random() * this.cluster.properties.activityid.length / 5) },
      { label: 'AfDB', value: Math.round(Math.random() * this.cluster.properties.activityid.length / 5)},
      { label: 'DFID', value: Math.round(Math.random() * this.cluster.properties.activityid.length / 5)}
    ];

    this._generateBaseChart(exampleData, '#charts-pane-donor .amp-chart svg');
  },

  _generateBaseChart: function(data, selector) {
    nvd3.addGraph(function() {
      var chart = nvd3.models.pieChart()
          .x(function(d) { return d.label; })
          .y(function(d) { return d.value; })
          .showLabels(true)
          .showLegend(false);

      d3.select(selector)
          .datum(data)
          .transition().duration(350)
          .call(chart);

      return chart;
    });

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


  //TODO: should be done in data.adm cluster..then we can render full list on second or third click...
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
      console.log('activityCollection', activityCollection);
      self.tempDOM.find('.project-list').append(
        self.projectListTemplate({activities: activityCollection})
        );
    });
  }
});
