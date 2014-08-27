define([ 'marionette', 'text!views/html/dynamicContentTemplate.html' ], function (Marionette, dynamicContentTemplate) {

    var DynamicContentView = Marionette.LayoutView.extend({
        template: _.template(dynamicContentTemplate),
        regions: {
            filters: '#dynamic-filters-region',
            legends: '#dynamic-legends-region',
            results: '#dynamic-filters-region'
        }
    });

    return DynamicContentView;

});