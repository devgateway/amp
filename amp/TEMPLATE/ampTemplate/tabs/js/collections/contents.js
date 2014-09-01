define([ 'underscore', 'backbone', 'documentModel', 'models/content' ], function (_, Backbone, DocumentModel, Content) {
    var Contents = Backbone.DocumentCollection.extend({
        model: Content,
        url: '/TEMPLATE/ampTemplate/tabs/content.json',
        initialize: function () {
            console.log('Initialized TabContents Collection');
            this.fetch({
                async: false,
                error: function (collection, response) {
                    console.log('error loading content.json');
                },
                success: function (collection, response) {
                    console.log(response);
                }
            });
        }
    });

    return Contents;
});