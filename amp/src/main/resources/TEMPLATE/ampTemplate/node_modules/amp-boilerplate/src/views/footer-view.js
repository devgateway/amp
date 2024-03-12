var fs = require('fs');
var Backbone = require('backbone');
var _ = require('underscore');
require('bootstrap/dist/js/bootstrap');

var Template = fs.readFileSync(__dirname + '/../templates/footer-template.html', 'utf8');
var LayoutModel = require('../models/amp-layout-model.js');

module.exports = Backbone.View.extend({
  model: null,
  template: _.template(Template),
  el: '#amp-footer',
  layoutFetched: new $.Deferred(),
  showAdminFooter: true,
  showDGFooter: true,  
  initialize: function(options) {
    this.showAdminFooter = options.showAdminFooter;
    this.showDGFooter = options.showDGFooter;
    this.model = options.model;
    this.render();    
    _.bindAll(this, 'render');
  },
  render: function() {
    if (this.model) {
      var self = this;
      this.$el.html(this.template({
        properties: self.model,
        showAdminLinks: self.showAdminFooter,
        showDGFooter: self.showDGFooter
      }));
    }
    return this;
  }  

});
