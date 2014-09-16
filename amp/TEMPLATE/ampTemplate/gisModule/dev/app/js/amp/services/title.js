var _ = require('underscore');
var Backbone = require('backbone');

function Title() {
  'use strict';
  if (!(this instanceof Title)) {
    throw new Error('Title needs to be created with the `new` keyword.');
  }
  this.initialize.apply(this, arguments);
}


_.extend(Title.prototype, Backbone.Events, {

  initialize: function(root) {
    this.root = root;
    this.set(null);
  },

  set: function(title) {
    var titleString = this.root;
    if (title) {
      titleString = title + ' - ' + this.root;
    }
    document.title = titleString;
  }

});


module.exports = Title;
