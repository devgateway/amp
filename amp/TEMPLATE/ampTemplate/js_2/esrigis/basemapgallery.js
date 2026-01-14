function createBasemapGallery() {
	// manually create basemaps to add to basemap gallery
	var basemaps = [];
	/*
	var worldphysicallayer = new esri.dijit.BasemapLayer(
			{url : rooturl +"World_Physical_Map/MapServer"});
	*/
	var WorldTopolayer = new esri.dijit.BasemapLayer(
			{url : rooturl +"World_Topo_Map/MapServer"});
	
	var WorldImagery = new esri.dijit.BasemapLayer(
			{url : rooturl +"World_Imagery/MapServer"});
	/*
	var WorldTerrain = new esri.dijit.BasemapLayer(
			{url : rooturl +"World_Terrain_Base/MapServer"});
	*/
	/*
	var WorldShaded = new esri.dijit.BasemapLayer(
			{url : rooturl +"World_Shaded_Relief/MapServer"});
	*/

	var WorldStreet = new esri.dijit.BasemapLayer(
			{url : rooturl +"World_Street_Map/MapServer"});
	
	/*
	var worldphysicalmap = new esri.dijit.Basemap({
		layers : [ worldphysicallayer ],
		title : "World Physical Map",
		thumbnailUrl : "/TEMPLATE/ampTemplate/img_2/world_physical_map_thumb.png"
	});
	*/
	var worldtopodmap = new esri.dijit.Basemap({
		layers : [ WorldTopolayer ],
		title : "World Topo Map",
		thumbnailUrl : "/TEMPLATE/ampTemplate/img_2/world_topo_map_thumb.png"
	});
	
	var worldimagery = new esri.dijit.Basemap({
		layers : [ WorldImagery ],
		title : "World_Imagery",
		thumbnailUrl : "/TEMPLATE/ampTemplate/img_2/world_imagery_map_thumb.png"
	});
	/*
	var worldterrain = new esri.dijit.Basemap({
		layers : [ WorldTerrain ],
		title : "World Terrain",
		thumbnailUrl : "/TEMPLATE/ampTemplate/img_2/world_terrain_map_thumb.png"
	});
	*/
	/*
	var worldshaded = new esri.dijit.Basemap({
		layers : [ WorldShaded ],
		title : "World Shaded Relief",
		thumbnailUrl : "/TEMPLATE/ampTemplate/img_2/world_shaded_map_thumb.png"
	});
	*/
	var worldsstreet = new esri.dijit.Basemap({
		layers : [ WorldStreet ],
		title : "World Street",
		thumbnailUrl : "/TEMPLATE/ampTemplate/img_2/world_street_map_thumb.png"
	});
	
	//basemaps.push(worldphysicalmap);
	basemaps.push(worldtopodmap);
	basemaps.push(worldimagery);
	//basemaps.push(worldterrain);
	//basemaps.push(worldshaded);
	basemaps.push(worldsstreet);
	

	var basemapGallery = new esri.dijit.BasemapGallery({
		showArcGISBasemaps : false,
		basemaps : basemaps,
		map : map
	}, "basemapGallery");
	basemapGallery.startup();
	var domNode = $(basemapGallery.domNode);
  $(domNode).bind("click", function(){
		domNode.hide();
	});

	dojo.connect(basemapGallery, "onError", function(error) {
		console.log(error);
	});
}

function createBasemapGalleryEsri() {
    //add the basemap gallery, in this case we'll display maps from ArcGIS.com including bing maps
    var basemapGallery = new esri.dijit.BasemapGallery({
     showArcGISBasemaps: true,
      //bingMapsKey: 'Av1bH4keF8rXBtxWOegklgWGCYYz8UGYvBhsWKuvc4Z15kT76xVFOERk8jkKEDvT',
      map: map,
      Title : "ESRI MAPS"
    }, "basemapGalleryesri");

    basemapGallery.startup();
    var domNode = $(basemapGallery.domNode);
    $(domNode).bind("click", function(){
			domNode.hide();
		});
		
    dojo.connect(basemapGallery, "onError", function(msg) {console.log(msg)});
  }