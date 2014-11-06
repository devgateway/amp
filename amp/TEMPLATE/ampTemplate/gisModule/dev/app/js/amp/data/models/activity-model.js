var Backbone = require('backbone');

module.exports = Backbone.Model.extend({
  idAttribute: 'Activity Id',
  defaults: {
    name: 'Untitled Activity',
    ampUrl: '/aim/viewActivityPreview.do~public=true',
    matchesFilters: {
      'Donor Id': '',
      programs: {
        1: [] /* x : [y..] */
      },
      organizations: {
        1: []
      },
      sectors: {
        1: []
      }
    }
  }
});
