import {hello} from 'module'

var aircrafts_geoJson = [];

aircrafts_geoJson["type"] = "FeatureCollection";
aircrafts_geoJson["features"] = [];

async function sleep(ms) {
  return new Promise(resolve => setTimeout(resolve, ms));
}

function update() {
    var xhttp = new XMLHttpRequest();
    xhttp.open("GET", "http://localhost:8080/api/aircrafts", false);
    xhttp.setRequestHeader("Content-type", "application/json");
    xhttp.send();
    var response = JSON.parse(xhttp.responseText);
    var keys = Object.keys(response);
    for (var i = 0; i < Object.keys(response).length; i++){
        var currKey = keys[i];
        var obj = response[currKey];
        aircrafts_geoJson["features"].items.push(obj);
        }
}

export function getAircrafts(){
    return aircrafts_geoJson;
}

while(true){
    update();
    await sleep(1000);
}