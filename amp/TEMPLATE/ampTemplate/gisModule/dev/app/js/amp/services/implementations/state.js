var _ = require('underscore');
var Backbone = require('backbone');

function State() {
  'use strict';

  if (! (this instanceof State)) {
    throw new Error('State needs to be created with the `new` keyword.');
  }

  // Anything that state must register itself. See `State.register`.
  this._stateRegistry = {};
  // When loading state, some things might not have registered yet.
  // It gets marked as unclaimed, and stored here.
  this._unclaimed = {};

  // Make use of backbone's awesome events API for the state service
  _.extend(this, Backbone.Events);


  this.reset = function clearState() {
    // Restore all states to their defaults (specified at registration)
    var changed = false;
    _.each(this._stateRegistry, function(state, id) {
      var currentState = state.get();
      if (! _.isEqual(currentState, state.empty)) {
        // only call .set if resetting will actually change the state.
        state.set(state.empty);
        changed = true;
      }
    }, this);

    this.trigger('reset');
    if (changed) {
      this.trigger('change');
    }
  };


  this.load = function loadState(statesBlob) {
    var parsed = JSON.parse(statesBlob);

    var changed = false;
    _.each(parsed, function(stateToSet, id) {
      var current = this._stateRegistry[id];
      if (_.isUndefined(current)) {
        this._unclaimed[id] = stateToSet;
        console.warn('Saving state for unregistered id: ' + id);
      } else if (! _.isEqual(current.get(), stateToSet)) {
        current.set(stateToSet);
        changed = true;
      }
    }, this);

    this.trigger('load');
    if (changed) {
      this.trigger('change');
    }
  };


  this.freeze = function freezeState() {
    var stateSnapshot = {};
    _.each(this._stateRegistry, function(state, id) {
      var currentState = state.get();
      stateSnapshot[id] = _.clone(currentState);  // TODO: deep clone(!!)
    });
    var statesBlob = JSON.stringify(stateSnapshot);
    this.trigger('freeze', statesBlob);
    return statesBlob;
  };


  this.register = function registerStateSaver(registrable, id, options) {
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
      console.info('restoring state for previously unregistered id ', id);
      this._stateRegistry[id].set(this._unclaimed[id]);
      delete this._unclaimed[id];
    } else {
      this._stateRegistry[id].set(options.empty);
    }

    this.trigger('register');
  };
}

module.exports = State;
