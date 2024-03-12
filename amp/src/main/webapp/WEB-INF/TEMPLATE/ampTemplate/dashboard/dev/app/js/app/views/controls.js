var BackboneDash = require('../backbone-dash');
var Filters = require('./filters');
var Settings = require('./settings');
var Share = require('./share');


module.exports = BackboneDash.View.extend({

  className: 'row',

  initialize: function(options) {
    this.app = options.app;    
    this.settings = new Settings({ app: this.app });
    this.filters = new Filters({ app: this.app });
    this.share = new Share({ app: this.app });
  },

  render: function() {
    this.$el.html([
      this.filters.render().el,
      this.settings.render().el,
      this.share.render().el
    ]);
    return this;
  }

});
