/*https://stackoverflow.com/questions/6535948/nested-models-in-backbone-js-how-to-approach?rq=1*/
/*https://github.com/icereval/backbone-documentmodel*/
define([ 'underscore', 'backbone', 'documentModel' ], function (_, Backbone, DocumentModel) {

    var TabContent = Backbone.DocumentModel.extend({
        defaults: {
            name: '',
            id: 0,
            metadata: {
                filter: {
                    sector: [0],
                    donor: [0]
                }
            },
            resultset: ['', 0]
        }
    });

    return TabContent;
});