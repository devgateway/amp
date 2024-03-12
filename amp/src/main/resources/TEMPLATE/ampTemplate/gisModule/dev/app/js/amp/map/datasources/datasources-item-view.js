
// model-specific item views
var ADMClustersDSView = require('./datasources-item-adm-clusters');

module.exports = function(options) {
  return new ADMClustersDSView(options);
};
