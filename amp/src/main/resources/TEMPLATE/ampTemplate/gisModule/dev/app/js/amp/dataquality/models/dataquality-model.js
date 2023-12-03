var Backbone = require('backbone');


module.exports = Backbone.Model.extend({
  defaults: {
  },

  initialize: function() {
  },

  // returns a data quality score from 1-100
  getScore: function() {
    //TODO: emit an event, or some other way of finding all relevant models.
    //      and use responses to calculate an aproximate dataQuality score

    //temporary stub, random score:
    return Math.floor(Math.random() * 100);
  }
});
