/*https://gist.github.com/jonnyreeves/2474026*/
/*https://github.com/icereval/backbone-documentmodel*/
define([ 'marionette', 'collections/contents', 'models/content', 'views/dynamicContentView', 'text!views/html/filtersTemplate.html' ],
    function (Marionette, Contents, Content, DynamicContentView, filtersTemplate) {

        "use strict";

        function TabEvents() {
            // Constructor
            function TabEvents() {
            }
        }

        // Some private method.
        function putAnimation() {
            return '<span><img src="/images/ajax-loader.gif"/></span>';
        }

        function retrieveTabContent(selectedTabIndex) {
            var content = "";
            // Get collection with data we will use to render the tab content.
            var tabContents = new Contents();
            var firstContent = tabContents.first();

            /*
             1) Defino class Filter(model)
             2) defino class Filters(collection)
             3) defino class FilterItemView(itemView)
             4) defino class FiltersCollectionView(collectionView)
             5) defino region donde insertar la FiltersCollectionView.
             6) defino los templates para las 2 vistas (inline y simples por ahora).
             7) tomo los datos de firstContent.metadata.filters y los convierto en una colección de objetos Filter.
             8) instancio todos los objetos y los meto en la region.
             */

            // Create a region where the dynamic content will be rendered inside the tab.
            var regionsName = '#main-dynamic-content-region_' + selectedTabIndex;
            app.TabsApp.addRegions({'filtersRegion': regionsName});

            // "Filter" item (todo: cleanup this part).
            /*var values = [];
             firstContent.get('metadata').get('filter').get('sector').each(function (item) {
             values.push(item.attributes.value);
             }, this);*/
            var Filter = Backbone.Model.extend({
                defaults: {
                    name: '',
                    values: []
                }
            });
            var Filters = Backbone.Collection.extend({
                model: Filter
            });
            var filtersInstance = new Filters();
            var filtersJson = firstContent.get('metadata').get('filter');
            $(filtersJson.keys()).each(function (i, item) {
                if (filtersJson.get(item) instanceof Backbone.Collection) {
                    var content = [];
                    $(filtersJson.get(item)).each(function (j, item2) {
                        $(item2.models).each(function (j, item3) {
                            content.push(item3.get('name'));
                        });
                    });
                    var auxFilter = new Filter({name: item, values: content});
                    filtersInstance.push(auxFilter);
                }
            });
            console.log(filtersInstance);

            var FilterItemView = Marionette.ItemView.extend({
                template: $(filtersTemplate, '#template-filters').html(),
                model: Filter
            });
            var CompositeItemView = Marionette.CompositeView.extend({
                template: $(filtersTemplate, '#template-composite-filters').html(),
                childViewContainer: 'div',
                childView: FilterItemView
            });
            var compositeView1 = new CompositeItemView({
                collection: filtersInstance
            });

            // Create LayoutView object setting template.
            var dynamicLayoutView = new DynamicContentView();
            app.TabsApp.filtersRegion.show(dynamicLayoutView);
            dynamicLayoutView.filters.show(compositeView1);

            /*content = firstContent.get('name');
             firstContent.get('metadata').get('filter').get('sector').each(function (item) {
             content += ' ' + item.attributes.value;
             }, this);*/
        }

        // "Class" methods definition here.
        TabEvents.prototype = {
            constructor: TabEvents,
            onCreateTab: function (event, ui) {
                console.log('create tab');
                this.onActivateTab(event, ui);
            },
            onActivateTab: function (event, ui) {
                console.log('activate tab');

                //TODO: move this logic elsewhere.
                var panel = null;
                var selectedTabIndex = 0;
                if (ui.panel != undefined) {
                    panel = ui.panel;
                    selectedTabIndex = ui.tab.index();
                } else {
                    panel = ui.newPanel;
                    selectedTabIndex = ui.newTab.index();
                }
                // Put loading animation.
                //$(panel).html(putAnimation());
                // Simulate time consuming content.
                setTimeout(function () {
                    retrieveTabContent(selectedTabIndex);
                }, 1000);
            }
        };

        return TabEvents;
    });