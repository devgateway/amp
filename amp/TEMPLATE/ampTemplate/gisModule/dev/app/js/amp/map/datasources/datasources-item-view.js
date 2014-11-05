var _ = require('underscore');
var Backbone = require('backbone');

// classes for instanceof checks
var ADMClusters = require('../../data/models/adm-cluster-model');
var ProjectSitesMenu = require('../../data/models/structures-menu-model');

// model-specific item views
var ADMClustersDSView = require('./datasources-item-adm-clusters');
var ProjectSitesDSView = require('./datasources-item-structures');


var Unknown = Backbone.View.extend({

  template: _.template('<h3><%= title %></h3>' +
                       '<em>Data Sources not yet configured for this layer type</em>'),

  render: function() {
    this.$el.html(this.template(this.model.toJSON()));
    return this;
  }

});


module.exports = function(options) {
  if (options.model instanceof ADMClusters) {
    return new ADMClustersDSView(options);
  } if (options.model instanceof ProjectSitesMenu) {
    return new ProjectSitesDSView(options);
  } else {
    console.error('datasources for layer type not implemented: ', options.model);
    return new Unknown(options);
  }

};
