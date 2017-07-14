class Datasource{
   async sleep(ms) {
              return new Promise(resolve => setTimeout(resolve, ms));
          }

          update() {
              this.xhttp.open("GET", this.url, false);
              this.xhttp.setRequestHeader("Content-type", "application/json");
              this.xhttp.send();
              var response = JSON.parse(this.xhttp.responseText);
              this.aircrafts_geoJson = response;
      //    var keys = Object.keys(response);
      //    for (var i = 0; i < Object.keys(response).length; i++){
      //        var currKey = keys[i];
      //        var obj = response[currKey];
      //        aircrafts_geoJson["features"].items.push(obj);
      //        }
          }

   async autoUpdate(){
           while(true){
               this.update();
               await this.sleep(1000);
           }
       }
   constructor(){
    this.aircrafts_geoJson = [];
    this.xhttp = new XMLHttpRequest();
    this.url = document.URL+'api/aircrafts';
    this.autoUpdate();
   }
    get aircrafts(){
        return this.aircrafts_geoJson;
    }
}