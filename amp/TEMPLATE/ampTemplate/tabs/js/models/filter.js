define([ 'underscore', 'backbone' ], function (_, Backbone) {
    var Filter = Backbone.Model.extend({
        defaults: {
            name: '',
            values: []
        }
    });
    return Filter;
});