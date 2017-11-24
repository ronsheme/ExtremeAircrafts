function contains(a, obj) {
    for (var i = 0; i < a.length; i++) {
        if (a[i] === obj) {
            return true;
        }
    }
    return false;
}

//initialize mapbox
mapboxgl.accessToken = 'pk.eyJ1Ijoicm9uc2hlbWUiLCJhIjoiY2ozcTU1OGJqMDAzejMybG1wbmJlYnk5dyJ9.0Cmc-BT-eKrWPXc3hZ_8rw';
var map = new mapboxgl.Map({
    container: 'map',
    center: [8.3221, 46.5928],
    zoom: 1,
    style: 'mapbox://styles/mapbox/streets-v9'
});
// disable map rotation using right click + drag
map.dragRotate.disable();

var geojson;
var aircraftsIds = [];

//setup auto update of data from url
var url = document.URL+'api/aircrafts';
map.on('load', function () {
        var datasource = new Datasource();
        window.setInterval(function() {
        geojson = datasource.aircrafts;
            map.getSource('aircrafts').setData(geojson);

            for(i=0;i<geojson["features"].length;i++){
                var currGeoJson = geojson["features"][i];
                if(currGeoJson["geometry"]["type"] === "Point"){
                    var id = currGeoJson["properties"]["uuid"];
                    var heading = parseFloat(currGeoJson["properties"]["heading"]);
                    //new aircraft
                    if(!contains(aircraftsIds,id)){
                        aircraftsIds.push(id);

                        map.addSource(id,{ type: 'geojson', data: currGeoJson});
                        map.addLayer({
                            "id": id,
                            "type": "symbol",
                            "source": id,
                            "layout": {
                                "icon-image": "airport-15",
                                "icon-rotate": heading
                                },
                            });
                        } else {//aircraft already exists, just update
                            map.getSource(id).setData(currGeoJson);
                            map.setLayoutProperty(id, "icon-rotate",heading);
                        }
                }
                }
            }, 10000);

//aircrafts data source- both the points of the aircrafts and the trails arrive in the same geojson
map.addSource('aircrafts', { type: 'geojson',data : url});

//trails layer- appears first to be beneath the aircrafts layer
map.addLayer({
            "id": "aircraft_routes",
            "type": "line",
            "source": "aircrafts",
            "layout": {
                        "line-join": "round",
                        "line-cap": "round"
                    },
            "paint": {
                         "line-color": "#888",
                         "line-width": 4
                     },
            "filter": ["==", "$type", "LineString"]});//here we separate the aircraft points from the trails
});