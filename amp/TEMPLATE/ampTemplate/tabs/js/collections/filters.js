define([ 'underscore', 'backbone', 'models/filter' ], function (_, Backbone, Filter) {
    var Filters = Backbone.Collection.extend({
        model: Filter
    });

    return Filters;
});
