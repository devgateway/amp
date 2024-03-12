var Backbone = require('backbone');

// Parent model for filters.
module.exports = Backbone.Model.extend({
    defaults: {
        name: 'Filter Name',
        method: 'GET',
        totalCount: 0,
        activeCount: 0,
        _loaded: false,
        empty: false
    },

    initialize: function (options) {
        this.set('name', options.name);
        this.set('group', options.group || options.id);
        this.set('method', options.method);
        this.set('columns', options.columns);
        this.set('empty', options.empty || false);
        this.url = options.url || options.endpoint;
    }

});
