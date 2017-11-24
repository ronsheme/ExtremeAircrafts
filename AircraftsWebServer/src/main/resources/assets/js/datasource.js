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
          }

   async autoUpdate(){
           while(true){
               this.update();
               await this.sleep(11000);
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