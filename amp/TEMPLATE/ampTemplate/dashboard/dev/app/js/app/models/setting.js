var _ = require('underscore');
var BackboneDash = require('../backbone-dash');


module.exports = BackboneDash.Model.extend({

  initialize: function(attrs, options) {
    this.app = options.app;
    this.url = options.url;
  },

  select: function(optionId) {
    // unselect old
    var old = _(this.attributes.options).findWhere({selected: true});
    delete old.selected;
    // select new
    var newOpt = _(this.attributes.options).findWhere({id: optionId});
    newOpt.selected = true;

    this.trigger('change');  // sort of a hack to do this manually...
  }

});
