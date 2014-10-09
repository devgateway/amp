var _ = require('underscore');
var BackboneDash = require('./backbone-dash');
var Filter = require('amp-filter');
var MainView = require('./views/main');
var FailView = require('./views/fail');


function App() {
  if (!(this instanceof App)) {
    throw new Error('App needs to be instantiated with the `new` keyword.');
  }
  this.initialize.apply(this, arguments);
}


_.extend(App.prototype, BackboneDash.Events, {

  initialize: function(options) {
    try {
      this.filter = new Filter({
        draggable: true,
        sync: BackboneDash.sync
      });
      this.view = new MainView({ app: this, el: options.el });
    } catch (e) {
      _.defer(function() { throw e; });
      this.view = new FailView({ app: this, el: options.el, err: e});
      this.err = e;
    }
  },

  render: function() {
    try {
      this.view.render();
    } catch (e) {
      _.defer(function() { throw e; });
      new FailView({ app: this, el: this.view.el, err: e}).render();
      this.err = e;
    }
  }

});


module.exports = App;
