var _ = require('underscore');
var Backbone = require('backbone');


function StateLoadError(message, original) {
  if (!(this instanceof StateLoadError)) { return new SateLoadError(message, original); }
  this.message = message;
  this.original = original;

  this.toString = function() {
    return this.message;
  };

  return this;
}


function State() {
  'use strict';

  if (!(this instanceof State)) {
    throw new Error('State needs to be created with the `new` keyword.');
  }

  this.initialize.apply(this, arguments);
}


_.extend(State.prototype, Backbone.Events, {

  initialize: function(options) {
    this.saved = options.saved;
    // Anything that state must register itself. See `State.register`.
    this._stateRegistry = {};
    // When loading state, some things might not have registered yet.
    // It gets marked as unclaimed, and stored here.
    this._unclaimed = {};
    // use the url, if we have it
    if (options.url) {
      this.listenToURL(_(options).pick('url', 'prefix', 'autoinit'));
    }
  },

  listenToURL: function(options) {
    this._url = options.url;
    this._urlPrefixes = options.prefix || ['saved/'];

    // wrap prefixes in array to make backward compat:
    if (this._urlPrefixes.constructor !== Array){
      this._urlPrefixes = [this._urlPrefixes];
    }

    this.listenTo(this._url, 'change', this.urlMaybeLoad);
    if (options.autoinit && this._url.hash()) {
      this.urlMaybeLoad(this._url.hash());
    }  // initial state
  },

  urlMaybeLoad: function(hash) {
    var self = this;
    var matchFound = false;
    hash = hash || this._url.hash();

    // see if any prefixes match the hash.
    _.each(this._urlPrefixes, function(prefix){
      // load a state if we get the URL hash for one
      if (hash.indexOf(prefix) === 0) {
        var state_or_id = hash.split(prefix)[1];
        if (state_or_id) {
          matchFound = true;
          self[self.saved ? 'loadById' : 'load'](state_or_id, prefix);
        }
      }
    });
    if (!matchFound && hash === '') {
      self.reset();
    }
  },

  toHash: function(state_or_id) {
    if (!_(this).has('_url')) {
      console.error('Cannot generate URL hash without being initialized with URL instance');
    } else {
      return this._urlPrefixes[0] + state_or_id;
    }
  },

  loadById: function(id, matchedPrefix) {
    this.loadPromise = this.saved.load(id, matchedPrefix  );// TODO: drs: and pass matching prefix..
    this.loadPromise
      .done(_(function(stateModel) {
        this.load(stateModel.get('stateBlob'));
      }).bind(this))
      .fail(function() {
        throw new StateLoadError('Could not load state by id: ' + id);
      });
  },

  load: function(statesBlob) {
    var parsed = this.parse(statesBlob);

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

  parse: function(statesBlob) {
    // break out the call to JSON.parse so that load can still be optimized
    var parsed;
    try {
      parsed = JSON.parse(statesBlob);
    } catch (e) {
      if (e instanceof SyntaxError) {
        throw new StateLoadError('Could not parse state', e);
      } else {
        throw e;
      }
    }
    return parsed;
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

  
  filtersLoaded: function() {
	  var dfd = jQuery.Deferred();
	  var self = this;
	  var timer = setInterval(function() {
		  if (self._stateRegistry.filters !== undefined) {
			  clearInterval(timer);
			  dfd.resolve();
		  }
	  }, 1000);
	  return dfd.promise(); 
  },
  
  
  freeze: function(options) {
    options = options || {};
    var stateSnapshot = {};
    _.each(this._stateRegistry, function(state, id) {
      var currentState = state.get();
      stateSnapshot[id] = _.clone(currentState);  // TODO: deep clone(!!)
    });
    var statesBlob = JSON.stringify(stateSnapshot);
    if (options.toURL) {
      if (!this._url) { console.error('State cannot change URL if it does have a ref to it'); }
      this._url.hash(this._urlPrefixes[0] + encodeURIComponent(statesBlob));
    }
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


State.StateLoadError = StateLoadError;
module.exports = State;
