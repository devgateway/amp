var fs = require('fs');
var _ = require('underscore');
var BackboneDash = require('../backbone-dash');
var template = _.template(fs.readFileSync(
  __dirname + '/../templates/main.html', 'UTF-8'));

// var FilterLauncher = require()
var Footer = require('../views/footer');



module.exports = BackboneDash.View.extend({

  initialize: function(options) {
    this.app = options.app;

    this.footer = new Footer({ app: this.app });
  },

  render: function() {
    this.$el.html(template());
    this.$('.container').html([
      this.footer.render().el
    ]);
    return this;
  }

});
