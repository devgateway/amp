var Deferred = require('jquery').Deferred;
var _ = require('underscore');


module.exports = {

  load: function() {
    if (!_.has(this, '_loaded')) {
      var model = this;
      this._loaded = new Deferred();
      this.fetch()
        .done(function() {
          model._loaded.resolveWith(model, [model]);
        })
        .fail(function(xhr, textStatus) {
          console.warn('failed load:', textStatus, model, xhr);
          model._loaded.rejectWith(model, [model, textStatus]);
          delete model._loaded;  // so that retries can happen
        });
    }

    return this._loaded.promise();
  },

  loadAll: function() {
    // classes using this mixin can override this method to add more loading
    // and processing before resolving. For example a class which loads a
    // boundary could implement this method as:
    //
    // return jQuery.when(this.load(), this.loadBoundary()).promise();
    //
    return this.load();
  }

};
