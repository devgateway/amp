function createBasemapGallery() {
	// manually create basemaps to add to basemap gallery
	var basemaps = [];


	var worldphysicallayer = new esri.dijit.BasemapLayer(
			{url : "http://4.79.228.117:8399/arcgis/rest/services/World_Physical_Map/MapServer"});
	
	var WorldTopolayer = new esri.dijit.BasemapLayer(
			{url : "http://4.79.228.117:8399/arcgis/rest/services/World_Topo_Map/MapServer"});
	
	
	var worldphysicalmap = new esri.dijit.Basemap({
		layers : [ worldphysicallayer ],
		title : "World Physical Map",
		thumbnailUrl : "/TEMPLATE/ampTemplate/img_2/world_physical_map_thumb.png"
	});
	
	var worldtopodmap = new esri.dijit.Basemap({
		layers : [ WorldTopolayer ],
		title : "World Topo Map",
		thumbnailUrl : "/TEMPLATE/ampTemplate/img_2/world_shaded_map_thumb.png"
	});
	

	basemaps.push(worldphysicalmap);
	basemaps.push(worldtopodmap);

	

	var basemapGallery = new esri.dijit.BasemapGallery({
		showArcGISBasemaps : false,
		basemaps : basemaps,
		map : map
	}, "basemapGallery");
	basemapGallery.startup();

	dojo.connect(basemapGallery, "onError", function(error) {
		console.log(error)
	});
}

function createBasemapGalleryEsri() {
    //add the basemap gallery, in this case we'll display maps from ArcGIS.com including bing maps
    var basemapGallery = new esri.dijit.BasemapGallery({
      showArcGISBasemaps: true,
      bingMapsKey: 'Av1bH4keF8rXBtxWOegklgWGCYYz8UGYvBhsWKuvc4Z15kT76xVFOERk8jkKEDvT',
      map: map
    }, "basemapGalleryesri");

    basemapGallery.startup();
    
    dojo.connect(basemapGallery, "onError", function(msg) {console.log(msg)});
  }