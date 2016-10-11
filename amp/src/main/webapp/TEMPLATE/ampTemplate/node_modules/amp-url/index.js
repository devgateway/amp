var _ = require('underscore');
var Backbone = require('backbone');


function URL() {
  'use strict';
  if (!(this instanceof URL)) {
    throw new Error('URL needs to be created with the `new` keyword.');
  }
  if (window._ampUrler) {
    throw new Error('Another instance of URL is already managing window.location');
  } else {
    window._ampUrler = true;
  }
  this.initialize.apply(this, arguments);
}


_.extend(URL.prototype, Backbone.Events, {

  initialize: function(options) {
    options = options || {};
    this.root = options.root || window.location.host + window.location.pathname;
    _.bindAll(this, 'change');
    window.addEventListener('hashchange', this.change);  // IE 8+
  },

  hash: function(newHash, options) {
    if (!arguments.length) {
      return window.location.hash.slice(1);  // remove '#'
    } else {
      if (options.silent) { window.removeEventListener('hashchange', this.change); }  // IE 9+
      window.location.hash = newHash;
      if (options.silent) { window.addEventListener('hashchange', this.change); }
    }
  },

  full: function() {
    return '' + window.location;
  },

  change: function() {
    this.trigger('change', this.hash());
  }

});


module.exports = URL;
