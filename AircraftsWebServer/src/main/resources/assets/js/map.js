mapboxgl.accessToken = 'pk.eyJ1Ijoicm9uc2hlbWUiLCJhIjoiY2ozcTU1OGJqMDAzejMybG1wbmJlYnk5dyJ9.0Cmc-BT-eKrWPXc3hZ_8rw';

var map = new mapboxgl.Map({
    container: 'map',
    center: [8.3221, 46.5928],
    zoom: 1,
    style: 'mapbox://styles/mapbox/streets-v9'
});
// disable map rotation using right click + drag
map.dragRotate.disable();

var url = document.URL+'api/aircrafts';

map.on('load', function () {
        window.setInterval(function() {
            map.getSource('aircrafts').setData(url);
        }, 1000)

    map.addSource('aircrafts', { type: 'geojson', data: url });
    map.addLayer({
            "id": "aircrafts",
            "type": "symbol",
            "source": "aircrafts",
            "layout": {
                "icon-image": "airport-15"
            }
        });
});
