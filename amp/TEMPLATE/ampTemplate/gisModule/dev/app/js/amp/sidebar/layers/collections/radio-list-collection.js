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

    // Add the "Wocat" radio button model to the collection
    this.add({
      id: 'wocat',
      name: 'Wocat',
      selected: false // Initially unselected
    });

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
    // If "Wocat" is selected, update the app state and refresh models
    if (model.id === 'wocat') {
      this.triggerWocatSelection(true);
    } else {
      this.triggerWocatSelection(false);
    }
  },

  unselect: function(model) {
    model.unset('selected');
  },

  triggerWocatSelection: function(isWocatSelected) {
    var self = this;
    self.app.data.wocat = isWocatSelected;
    console.log('Wocat selected:', isWocatSelected);

    if (isWocatSelected) {
      // Refresh models to reflect the Wocat state
      self.app.data.admClusters.fetch({
        success: function() {
          console.log('admClusters refreshed successfully with Wocat.');
        },
        error: function() {
          console.error('Failed to refresh admClusters with Wocat.');
        }
      });
    }
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
