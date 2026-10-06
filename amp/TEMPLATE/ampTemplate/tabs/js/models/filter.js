define([ 'underscore', 'backbone' ], function (_, Backbone) {
    var Filter = Backbone.Model.extend({
        defaults: {
            name: '',
            trnName: '',
            values: [],
            entityType: null
        }
    });
    return Filter;
});