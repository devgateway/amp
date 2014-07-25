var _ = require('underscore');
var Backbone = require('backbone');


module.exports = Backbone.Model.extend({
  defaults: {
      title: '',
      selected: false,
      value:'',
      helpText:'',
      group: 'projects', //used for radio button grouping.
     // children: null, // collection of more options, use for nesting.
  },

});
