var $ = require('jquery');
var Backbone = require('backbone');
var _ = require('underscore');

module.exports = Backbone.Collection.extend({
  siblingGroupList: [],
  initialize: function(models, options) {
    /* SiblingGroupsLists are used when radio-button
     * mutual exclusion needs to span several diff. models
     *
     * They are an array of all the lists that should be iteratively
     *  unticked when this one is ticked (and vice versa)
     **/
    if (options && options.siblingGroupList) {
      this.siblingGroupList = options.siblingGroupList;
    } else {
      this.siblingGroupList = [this];
    }

    this.listenTo(this, 'toggleSelect', this.toggleSelect);

    /* Since we will be reaching in from outside for
     * clearing sibling groups, we need to bind the context
     **/
    _.bindAll(this, 'select', 'unselect', 'toggleSelect', 'getSelected', 'clearSelected');

  },

  select: function(model) {
    _.each(this.siblingGroupList, function(child) {
      if(child) child.clearSelected();
    });

    $('#map-loading').show();
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
