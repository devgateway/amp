var Backbone = require('backbone');

module.exports = Backbone.Collection.extend({

  initialize: function() {
    this.listenTo(this, 'toggleSelect', this.toggleSelect);
  },

  select: function(model) {
    this.clearSelected();
    model.set('selected', true);
  },

  unselect: function(model) {
    model.unset('selected');
  },

  toggleSelect: function(model) {
    if (model.get('selected')) {
      this.unselect(model);
      return false;
    } else {
      this.select(model);
      return true;
    }
  },

  getSelected: function() {
    return this.chain()
      .filter(function(model) { return model.get('selected'); });
  },

  clearSelected: function() {
    this.getSelected().invoke('unset', 'selected');
  }

});
