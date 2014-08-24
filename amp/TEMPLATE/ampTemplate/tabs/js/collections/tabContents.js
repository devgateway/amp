define([ 'underscore', 'backbone', 'documentModel', 'models/tabContent' ], function (_, Backbone, DocumentModel, TabContent) {
    var TabContents = Backbone.DocumentCollection.extend({
        model: TabContent,
        url: '/content.json',
        initialize: function () {
            console.log('Initialized TabContents Collection');
            this.fetch({
                async: false,
                error: function (collection, response) {
                    console.log('error loading content.json')
                },
                success: function (collection, response) {
                    console.log(response);
                }
            });
        }
    });

    return TabContents;
});