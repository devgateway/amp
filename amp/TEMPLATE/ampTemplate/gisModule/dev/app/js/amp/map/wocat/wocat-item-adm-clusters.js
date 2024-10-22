var fs = require('fs');
var _ = require('underscore');
var Backbone = require('backbone');
var countries = require('i18n-iso-countries');
var Template = fs.readFileSync(__dirname + '/wocat-item-adm-clusters.html', 'utf8');
var ProjectCollection = require('./project-collection');
const WOCAT_API='https://qcat.wocat.net';
countries.registerLocale(require('i18n-iso-countries/langs/en.json'));
module.exports = Backbone.View.extend({
  tagName: 'tbody',

  template: _.template(Template),
  initialize: function(options) {
    this.app = options.app;

    _.bindAll(this, 'render');
  },


    render: function(page) {
        var self = this;

        // Create a new instance of the collection
        var projects = new ProjectCollection([],{size:10, page:page});

        // Fetch the data from the API
        projects.fetch({
            success: function(collection, response, options) {
                // Iterate over the fetched data
                collection.each(function(project) {
                    var projectData = project.toJSON();

                    // Access the necessary fields from the payload
                    var name = projectData.name || 'N/A';
                    var description = projectData.description || 'N/A';
                    var formattedDescription = description.split(' ').length > 7 ? description.split(' ').slice(0, 7).join(' ') + '...' : description;

                    var compilerNames = projectData.compilerNames || 'N/A';
                    var thumbnail = projectData.thumbnail || 'default-thumbnail.jpg';  // Fallback thumbnail
                    var thumbnailDescription = projectData.thumbnailDescription || 'N/A';
                    var formattedThumbnailDescription = thumbnailDescription.split(' ').length > 7 ? thumbnailDescription.split(' ').slice(0, 7).join(' ') + '...' : thumbnailDescription;
                    var publicationDate = projectData.publicationDate || 'N/A';
                    var institutionName = projectData.ampWocatInstitution ? projectData.ampWocatInstitution.institutionName : 'N/A';
                    var detailsUrl = WOCAT_API + projectData.detailsUrl || '#';
                    var country = projectData.country?countries.getName(projectData.country, 'en'):'N/A';
                    // Append or render the content on the page using your template or directly
                    self.$el.append(self.template({
                        name: name,
                        description: formattedDescription,
                        compilerNames: compilerNames,
                        thumbnail: thumbnail,
                        thumbnailDescription: formattedThumbnailDescription,
                        publicationDate: publicationDate,
                        institutionName: institutionName,
                        detailsUrl: detailsUrl,
                        country: country
                    }));
                });
            },
            error: function(collection, response, options) {
                console.error('Failed to fetch data:', response);
            }
        });

        return this;
    }

});
