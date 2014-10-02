var Backbone = require('backbone');
// var _ = require('underscore');

//TODO: move most code from filters-view here.
module.exports = Backbone.Collection.extend({
  // url: '/rest/filters',
  // model: FilterModel,

  // initialize: function(models, options) {

  // }
  // parse: function(data) {
  //   _(data).each(function({

  //   }));
  // }

  //TODO: in progress...
  setupOrgListener:function() {
    // var orgFilter = this.allFilters.findWhere({title: 'Organizations'});
    var orgGroupFilter = this.findWhere({title: 'OrganizationGroupList'});
    var orgTypeFilter = this.findWhere({title: 'OrgTypesList'});

    orgTypeFilter.getTree().then(function(tree) {
      // only listens to children...won't do nested types...
      tree.get('children').on('change:numSelected', function(type) {
        orgGroupFilter.getTree().then(function(groupTree) {
          groupTree.get('children').each(function(group) {
            //console.log('group', group);
            if (group.TypeID === type.id) {
              group.set('selected', true);
            }
          });
        });

      });

      //TODO: setup 'Organizations' to listen to 'OrganizationGroups'
    });
  }
});
