define([ 'underscore', 'backbone' ], function (_, Backbone) {
    var Filter = Backbone.Model.extend({
        defaults: {
            name: '',
            values: [],
            entityType: null
        }
    });
    return Filter;
});