var fs = require('fs');
var _ = require('underscore');
var RadioListCollection = require('../collections/radio-list-collection');
var Backbone = require('backbone');
var OptionView = require('./option-view');
var Template = fs.readFileSync(__dirname + '/../templates/layers-template.html', 'utf8');


module.exports = Backbone.View.extend({
  id: 'tool-layers-sd-fund',
  title: 'Funding Data',
  iconClass: 'ampicon-layers',

  template: _.template(Template),

  initialize: function(options) {
    var self = this;
    /* Remember that this should be mutually exclusive to the other things in this multisection*/
    this.app = options.app;

    /* For mutual exclusion, we use this reference to the parent */
    this.parentMultisectionControl = options.parent;

    this.collection = new RadioListCollection(this.app.data.hilightFundingCollection.models, {
      siblingGroupList: this.parentMultisectionControl.radioButtonGroup
    });

    this.app.data.hilightFundingCollection.load().then(function() {
      self._registerSerializer();
    });

    this.listenTo(this.app.data.hilightFundingCollection, 'add', this.render);
  },

  render: function() {
    // TODO: find a better way to keep our proxy collection up to date
    // Thad do you know a good pattern for this?
    this.collection.reset(this.app.data.hilightFundingCollection.models);
    if(this.collection.length > 0){
    	 this.$el.html(this.template({title: this.title}));
    	    this.app.translator.translateDOM(this.el); /* After to catch disabled */
    	    //this.$(this.el,this).html(this.template({title: this.title}));

    	    this.$('.layer-selector', this).html(this.collection.map(function(indicator) {
    	      return (new OptionView({
    	        model: indicator,
    	        app: this.app
    	      })).render().el;
    	    }));
    }
    return this;
  },

  _registerSerializer: function() {
    var self = this;

    self.app.state.register(self, self.id, {
      get: function() {
        var tmp = self.collection.getSelected().first().value();
        if (tmp) {
          return tmp.toJSON().id; //TODO: should be an id....
        } else {
          return null;
        }
      },
      set: function(id) {
        if (id) {
          var selectedModel = self.collection.findWhere({id: id});
          self.collection.select(selectedModel);
        }
      },
      empty: null
    });
  }

});
