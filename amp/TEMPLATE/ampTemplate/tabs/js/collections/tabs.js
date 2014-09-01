define([ 'underscore', 'backbone', 'models/tab' ], function (_, Backbone, Tab) {
    var Tabs = Backbone.Collection.extend({
        model: Tab,
        url: '/rest/data/tabs',
        initialize: function () {
            console.log('Initialized Tabs Collection');
            this.fetch({
                async: false,
                error: function (collection, response) {
                    console.log('error loading tabs url')
                },
                success: function (collection, response) {
                    console.log(response);
                }
            });
        }
    });

    return Tabs;
});
