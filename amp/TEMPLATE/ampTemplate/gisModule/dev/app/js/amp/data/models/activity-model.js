var Backbone = require('backbone');

module.exports = Backbone.Model.extend({
  defaults: {
    id: null,
    name: 'Untitled Activity',
    ampUrl: '/aim/viewActivityPreview.do~public=true',
    matchesFilters: {
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
