var fs = require('fs');
var _ = require('underscore');
var $ = require('jquery');
var Backbone = require('backbone');
var d3 = require('d3');
var nv =  require('nvd3');
var nvd3 = window.nv;

var ProjectListTemplate = fs.readFileSync(__dirname + '/../templates/project-list-template.html', 'utf8');
var Template = fs.readFileSync(__dirname + '/../templates/cluster-popup-template.html', 'utf8');

// Tabs: https://www.mapbox.com/mapbox.js/example/v1.0.0/marker-tooltip-tab-groups/

module.exports = Backbone.View.extend({
  template: _.template(Template),
  projectListTemplate: _.template(ProjectListTemplate),

  tempDOM:null,

  initialize: function(popup, admLayer) {
    this.popup = popup;
    this.admLayer = admLayer;
    this.tempDOM =$('<div/>');
  },

  render: function() {
    this.generateInfoWindow(this.popup, this.admLayer);
    return this;
  },


  generateInfoWindow: function(popup, admLayer){
    var featureCollection = admLayer.get('features');
    this.cluster = _.find(featureCollection,function(feature){
      return feature.properties.admName === popup._source._clusterId;
    });

    // get appropriate cluster model:
    if(this.cluster){
      this.tempDOM.html(this.template(this.cluster));
      popup.setContent(this.tempDOM.html());
      this._generateChart();
      return this._generateProjectList(popup, this.cluster);
    }else{
      console.error('no matching cluster: ', admLayer, popup._source._clusterId);
      this.popup.setContent('error finding cluster');
    }
  },

  _generateChart: function(){

    var exampleData = [ { 'label': 'U.N','value' : Math.round(Math.random()*this.cluster.properties.activityid.length/5) } , 
    { 'label': 'World Bank','value' : Math.round(Math.random()*this.cluster.properties.activityid.length/5) }, 
    { 'label': 'UNICEF','value' : Math.round(Math.random()*this.cluster.properties.activityid.length/5) }, 
    { 'label': 'AfDB','value' : Math.round(Math.random()*this.cluster.properties.activityid.length/5 )}, 
    { 'label': 'DFID','value' : Math.round(Math.random()*this.cluster.properties.activityid.length/5 )}];

    //this.tempDOM.find('.charts').html("TODO: chart");
    nvd3.addGraph(function() {
      var chart = nvd3.models.pieChart()
          .x(function(d) { return d.label })
          .y(function(d) { return d.value })
          .showLabels(true)
          .showLegend(false);

        d3.select(".charts svg")
            .datum(exampleData)
            .transition().duration(350)
            .call(chart);
      return chart;
    });   
  },

  _generateProjectList: function(popup, cluster){
    var self = this;
    var PAGE_SIZE = 20; ///TODO: move elsewhere when real pagination is done.
    var activityIDs = _.first(cluster.properties.activityid, PAGE_SIZE);
    
    // Phil: is this how we want to do app access...?
    return window.app.data.activities.getActivites(activityIDs).then(function(activityCollection){
      console.log('activityCollection', activityCollection);
      self.tempDOM.find('.project-list').append(
        self.projectListTemplate({activities: activityCollection})
        );
    });
  }
});
