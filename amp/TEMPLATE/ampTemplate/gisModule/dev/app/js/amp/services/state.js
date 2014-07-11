var _ = require('underscore');
var Backbone = require('backbone');

function State() {
  this._stateRegistry = {};

  // Make use of backbone's awesome events API for the state service
  _.extend(this, Backbone.Events);


  this.reset = function clearState() {
    var changed = false;
    _.each(this._stateRegistry, function(state, id) {
      var currentState = state.get();
      if (! _.isEqual(currentState, state.empty)) {
        state.set(state.empty);
        changed = true;
      }
    }, this);

    this.trigger('reset');
    if (changed) {
      this.trigger('changed');
    }
  };

  this.load = function loadState(statesBlob) {
    var changed = false;
    _.each(statesBlob, function(stateToSet, id) {
      var current = this._stateRegistry[id];
      if (_.isUndefined(current)) {
        throw new Error('Attempted to load state for unregistered id: ' + id);
      }
      if (! _.isEqual(current.get(), stateToSet)) {
        current.set(stateToSet);
        changed = true;
      }
    }, this);

    this.trigger('load');
    if (changed) {
      this.trigger('changed', changed);
    }
  };

  this.freeze = function freezeState() {
    var stateSnapshot = {};
    _.each(this._stateRegistry, function(state, id) {
      var currentState = state.get();
      stateSnapshot[id] = _.clone(currentState);  // TODO: deep clone(!!)
    });
    return stateSnapshot;
  };

  this.register = function registerStateSaver(id, getSetFns, emptyState) {
    // Objects saving state must:
    //  1. Deterministically create a unique id for themselves
    //  2. Implement a getState method, returning a json blob
    //  3. Implement a setState method, accepting a json blob
    if (id in this._stateRegistry) {
      throw new Error('Attempted registration of duplicate state id ' + id);
    }
    this._stateRegistry[id] = {
      get: getSetFns.get,
      set: getSetFns.set,
      empty: emptyState
    };
    getSetFns.set(emptyState);  // TODO: look up in loaded state first
  };
}


module.exports = State;
