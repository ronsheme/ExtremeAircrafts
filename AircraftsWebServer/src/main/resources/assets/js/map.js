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

//setup auto update of data from url
var url = document.URL+'api/aircrafts';
map.on('load', function () {
        window.setInterval(function() {
            map.getSource('aircrafts').setData(url);
        }, 1000)

//aircrafts data source- both the points of the aircrafts and the trails arrive in the same geojson
map.addSource('aircrafts', { type: 'geojson', data: url });

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

//aircrafts layer
map.addLayer({
        "id": "aircrafts",
        "type": "symbol",
        "source": "aircrafts",
        "layout": {
            "icon-image": "airport-15"
        },
        "filter": ["==", "$type", "Point"]});//here we separate the aircraft points from the trails

//info popup
var popup = new mapboxgl.Popup({
        closeButton: false,
        closeOnClick: false
    });

map.on('mouseenter', 'aircrafts', function(e) {
    // Change the cursor style as a UI indicator.
    map.getCanvas().style.cursor = 'pointer';
    // Populate the popup and set its coordinates
    // based on the feature found.
    popup.setLngLat(e.features[0].geometry.coordinates)
                    .setHTML(e.features[0].properties.uuid)
                    .addTo(map);
    });

map.on('mouseleave', 'places', function() {
    map.getCanvas().style.cursor = '';
    popup.remove();
    });
});
