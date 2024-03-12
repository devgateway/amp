define([ 'underscore', 'backbone' ], function(_, Backbone) {

	// Document Model
	var Document = Backbone.Model.extend({
		defaults : {
			name : "",
			uuid : "",
			title : "",
			description : "",
			notes : "",
			webLink : null,
			translatedTitles : null,
			translatedDescriptions : null,
			translatedNotes : null,
			escapedAmpDescription : "",
			escapedAmpTitle : "",
			generalLink : ""
		}
	});
	return Document;
});