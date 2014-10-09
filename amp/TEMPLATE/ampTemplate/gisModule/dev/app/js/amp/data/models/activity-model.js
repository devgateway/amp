var Backbone = require('backbone');
require('backbone-associations');


module.exports = Backbone.AssociatedModel.extend({
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
  //relations: [
    //{
    //type: Backbone.Many,
    //key: 'matchesFilters',
    //collectionType:
    //}
  //]
});
