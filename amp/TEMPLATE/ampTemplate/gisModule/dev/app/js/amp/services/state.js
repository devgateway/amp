// TODO: move this up a dir, and instantiate and attach to the app

var _ = require('underscore');
var Backbone = require('backbone');


var PREFIX = 'saved/';  // implementation detail for the URL, maybe not the best place...


function State() {
  'use strict';

  if (!(this instanceof State)) {
    throw new Error('State needs to be created with the `new` keyword.');
  }

  this.initialize.apply(this, arguments);
}


_.extend(State.prototype, Backbone.Events, {

  prefix: PREFIX,

  initialize: function(options) {
    this.data = options.data;
    this.url = options.url;
    // Anything that state must register itself. See `State.register`.
    this._stateRegistry = {};
    // When loading state, some things might not have registered yet.
    // It gets marked as unclaimed, and stored here.
    this._unclaimed = {};

    this.listenTo(this.url, 'change', this.maybeLoad);
    if (this.url.hash()) { this.maybeLoad(this.url.hash()); }  // initial state
  },

  maybeLoad: function(hash) {
    // load a state if we get the URL hash for one
    if (hash.indexOf(PREFIX) === 0) {
      var id = hash.split(PREFIX)[1];
      if (id) {
        this.loadById(id);
      } else {
        this.reset();
      }
    } else if (hash === '') {
      this.reset();
    } else {
      console.warn('hash changed but not for state:', hash);
    }
  },

  loadById: function(id) {
    this.data.savedMaps.load(id).done(_(function(stateModel) {
      this.load(stateModel.get('stateBlob'));
    }).bind(this));
  },

  load: function(statesBlob) {
    var parsed = JSON.parse(statesBlob);

    var changed = false;
    _.each(parsed, function(stateToSet, id) {
      var current = this._stateRegistry[id];
      if (_.isUndefined(current)) {
        this._unclaimed[id] = stateToSet;
      } else if (!_.isEqual(current.get(), stateToSet)) {
        current.set(stateToSet);
        changed = true;
      }
    }, this);

    this.trigger('load');
    if (changed) {
      this.trigger('change');
    }
  },

  reset: function() {
    // Restore all states to their defaults (specified at registration)
    var changed = false;
    _.each(this._stateRegistry, function(state) {
      var currentState = state.get();
      if (!_.isEqual(currentState, state.empty)) {
        // only call .set if resetting will actually change the state.
        state.set(state.empty);
        changed = true;
      }
    }, this);

    this.trigger('reset');
    if (changed) {
      this.trigger('change');
    }
  },

  freeze: function() {
    var stateSnapshot = {};
    _.each(this._stateRegistry, function(state, id) {
      var currentState = state.get();
      stateSnapshot[id] = _.clone(currentState);  // TODO: deep clone(!!)
    });
    var statesBlob = JSON.stringify(stateSnapshot);
    this.trigger('freeze', statesBlob);
    return statesBlob;
  },

  register: function(registrable, id, options) {
    if (id in this._stateRegistry) {
      throw new Error('Attempted registration of duplicate state id ' + id);
    }

    // register the state
    this._stateRegistry[id] = {
      get: _.bind(options.get, registrable),
      set: _.bind(options.set, registrable),
      empty: options.empty
    };

    // set to the currently loaded state, or its default empty state
    if (id in this._unclaimed) {
      this._stateRegistry[id].set(this._unclaimed[id]);
      delete this._unclaimed[id];
    } else {
      this._stateRegistry[id].set(options.empty);
    }

    this.trigger('register');
  }
});


module.exports = State;
