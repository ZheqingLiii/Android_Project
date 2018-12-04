
const express = require('express');
const app = express();
const router = express.Router();
const port = 3000;
const url = require('url');
Object.prototype.isEmpty = function() {
    for(var key in this) {
        if(this.hasOwnProperty(key))
            return false;
    }
    return true;
}

// url: http://localhost:3000/
app.get('/', (request, response) => response.send('Hello World'));

// using router.get() to prefix our path
router.get('/', (request, response) => {
  response.json({message: 'Hello, welcome to my server'});
});

// set the server to listen on port 3000
app.listen(port, () => console.log(`Listening on port ${port}`));


app.get('/getCarInfo', function (request, response){

  var urlParts = url.parse(request.url, true);
  var parameters = urlParts.query;
  var CarID_ = parameters.CarID;
  // e.g. myVenues = 12;
  var mysql = require('mysql');
 
    // config for your dat
  var con = mysql.createConnection({
        host: "REDACTED",
        user: "REDACTED",
        password: "REDACTED",
        database: "COEN268_Project"
        });
  console.log('heregetCar2');
    // connect to your database
  con.connect(function (err) {

        if (err) console.log(err);

        // query to the database and get the records
        con.query("SELECT * from Cars WHERE CarID=" + con.escape(CarID_) + "", function (err, recordset) {

            if (err){

                 console.log(err);
		response.send('Failed - Error');
            }
            else{
         // send records as a response

            var recordsetString = JSON.stringify(recordset);
            var json = JSON.parse(recordsetString);
            console.log(json);
            if(recordset.isEmpty()) {
                // Object is empty

                response.header("Access-Control-Allow-Origin", "*");
                response.send('No record');
            } else {

                console.log('heregetCar3');

                console.log(json);
                // Object is NOT empty (would return false in this example)
               response.header("Access-Control-Allow-Origin", "*");
                //response.send(json[json.length-1].Location);
                response.send(json);
            }

           }
           
        });
        
  con.end();

 });
 
});


app.get('/putCarInfo', function (request, response){

  var urlParts = url.parse(request.url, true);
  var parameters = urlParts.query;
  var Model_ = parameters.Model;
  var Year_ = parameters.Year;
  var Color_ = parameters.Color;
  var homeCity_ = parameters.HomeCity;
  var OwnerID_ = parameters.OwnerID;
  var PricePerDay_ = parameters.PricePerDay;
  var Detail_ = parameters.Detail;
  var isAvailable_ = parameters.isAvailable;
  var Lat_ = parameters.lat; 
  var Long_ = parameters.lng;
  var CarPhotoURL_ = parameters.CarPhotoURL;
  var OwnerEmail_ = parameters.OwnerEmail; 

// e.g. myVenues = 12;
  var mysql = require('mysql');

	console.log(urlParts);
	console.log(parameters);


   // config for your dat 
  var con = mysql.createConnection({
        host: "rwandasos.c0k8vg0pevvr.us-east-2.rds.amazonaws.com",
        user: "sgahima",
        password: "KW220987",
        database: "COEN268_Project"
        });
        
    // connect to your database
  con.connect(function (err) {

        if (err) console.log(err);

        // query to the database and get the records
        con.query("INSERT INTO Cars (Model, Year, Color, HomeCity, OwnerID, PricePerDay, Detail, isAvailable, lat, lng, CarPhotoURL, OwnerEmail) VALUES (" + con.escape(Model_) + ", " + con.escape(Year_) + ", " + con.escape(Color_) + ", " + con.escape(homeCity_) + ", " + con.escape(OwnerID_) + ", " + con.escape(PricePerDay_) + ", " + con.escape(Detail_) +  ", " + con.escape(isAvailable_) +  ", " + con.escape(Lat_)  +  ", " + con.escape(Long_) + ", " + con.escape(CarPhotoURL_) + ", " + con.escape(OwnerEmail_) + ")", function (err, recordset) {

	     var recordsetString = JSON.stringify(recordset);
            var json = JSON.parse(recordsetString);
            console.log(json);
           if (err){

                 console.log(err);
		response.send('Failed - Error');
            }
            else{

		console.log('successful creation');
		console.log(json.insertId);
		response.header("Access-Control-Allow-Origin", "*");
                response.send(json.insertId.toString());
                
		}
		
       });
       
 con.end();
 
 });
 
});



app.get('/updateCarInfo', function (request, response){

  var urlParts = url.parse(request.url, true);
  var parameters = urlParts.query;
  // e.g. myVenues = 12;
  var mysql = require('mysql');

  console.log(urlParts);
  console.log(parameters);

  var parsed_parameter = urlParts.path;
  var parsed_parameter_ = parsed_parameter.split('?');
  var parsed_parameter__ = parsed_parameter_[1].split('&');
  var parsed_parameter0___ = parsed_parameter__[0].split('=');
  var parsed_parameter1___ = parsed_parameter__[1].split('=');
    // config for your dat
  var con = mysql.createConnection({
        host: "rwandasos.c0k8vg0pevvr.us-east-2.rds.amazonaws.com",
        user: "sgahima",
        password: "KW220987",
        database: "COEN268_Project"
        });
  console.log('hereUpdate2');
  
    // connect to your database
  con.connect(function (err) {

        if (err) console.log(err);

        // query to the database and get the records
        con.query("UPDATE Cars SET " + parsed_parameter1___[0] + " = " + con.escape(parsed_parameter1___[1]) + " WHERE CarID = " + con.escape(parsed_parameter0___[1]) + "", function (err, recordset) {

           if (err){

                 console.log(err)
		response.send('Failed - Error');
            }
            else{

                console.log('successful creation')
                response.header("Access-Control-Allow-Origin", "*");
                response.send('Successfully updated '  + parsed_parameter0___[1] + ' ');
                
                }
       });
       
  con.end();


  });

});


app.get('/updateCarInfoBulk', function (request, response){

  var urlParts = url.parse(request.url, true);
  var parameters = urlParts.query;
  // e.g. myVenues = 12;
  var mysql = require('mysql');

        console.log(urlParts);
        console.log(parameters);
 // console.log('herePut1' + FirstName_ + LastName_ + username_ + homeCity_ + isCurrentlyRentingCarIn_ + isCurrentlyRentingCarOut_);

 var parsed_parameter = urlParts.path;
 var parsed_parameter_ = parsed_parameter.split('?');
 var parsed_parameter__ = parsed_parameter_[1].split('&');
 var parsed_parameter0___ = parsed_parameter__[0].split('=');
 var parsed_parameter1___ = parsed_parameter__[1].split('=');
 var parsed_parameter2___ = parsed_parameter__[2].split('=');
 var parsed_parameter3___ = parsed_parameter__[3].split('=');
 var parsed_parameter4___ = parsed_parameter__[4].split('=');
 var parsed_parameter5___ = parsed_parameter__[5].split('=');
 var parsed_parameter6___ = parsed_parameter__[6].split('=');
 var parsed_parameter7___ = parsed_parameter__[7].split('=');
 var parsed_parameter8___ = parsed_parameter__[8].split('=');
 var parsed_parameter9___ = parsed_parameter__[9].split('=');
    // config for your dat
 var con = mysql.createConnection({
        host: "rwandasos.c0k8vg0pevvr.us-east-2.rds.amazonaws.com",
        user: "sgahima",
        password: "KW220987",
        database: "COEN268_Project"
        });
 console.log('hereUpdate2');
    // connect to your database
 con.connect(function (err) {

        if (err) console.log(err);

        // query to the database and get the records
        con.query("UPDATE Cars SET " + parsed_parameter1___[0] + " = " + con.escape(parsed_parameter1___[1]) + ", " + parsed_parameter2___[0] + " = " + con.escape(parsed_parameter2___[1]) + ", " + parsed_parameter3___[0] + " = " + con.escape(parsed_parameter3___[1]) + ", " + parsed_parameter4___[0] + " = " + con.escape(parsed_parameter4___[1]) + ", " + parsed_parameter5___[0] + " = " + con.escape(parsed_parameter5___[1]) + ", " + parsed_parameter6___[0] + " = " + con.escape(parsed_parameter6___[1]) + ", " + parsed_parameter7___[0] + " = " + con.escape(parsed_parameter7___[1]) + ", " + parsed_parameter8___[0] + " = " + con.escape(parsed_parameter8___[1]) + ", " + parsed_parameter9___[0] + " = " + con.escape(parsed_parameter9___[1]) + " WHERE CarID = " + con.escape(parsed_parameter0___[1]) + "", function (err, recordset) {

           if (err){

                 console.log(err);
		response.send('Failed - Error');
            }
            else{

                console.log('successful creation')
                response.header("Access-Control-Allow-Origin", "*");
                response.send('Successfully updated '  + parsed_parameter0___[1] + ' ');
                }
       });
 con.end();

 });

});



app.get('/deleteCarInfo', function (request, response){

  var urlParts = url.parse(request.url, true);
  var parameters = urlParts.query;
  // e.g. myVenues = 12;
  var mysql = require('mysql');

        console.log(urlParts);
        console.log(parameters);
 // console.log('herePut1' + FirstName_ + LastName_ + username_ + homeCity_ + isCurrentlyRentingCarIn_ + isCurrentlyRentingCarOut_);

 var parsed_parameter = urlParts.path;
 var parsed_parameter_ = parsed_parameter.split('?');
 var parsed_parameter__ = parsed_parameter_[1].split('&');
 var parsed_parameter0___ = parsed_parameter__[0].split('=');
 //var parsed_parameter1___ = parsed_parameter__[1].split('=');

    // config for your dat
 var con = mysql.createConnection({
        host: "rwandasos.c0k8vg0pevvr.us-east-2.rds.amazonaws.com",
        user: "sgahima",
        password: "KW220987",
        database: "COEN268_Project"
        });
 console.log('hereDelete2');
    // connect to your database
 con.connect(function (err) {

        if (err) console.log(err);

        // query to the database and get the records
        con.query("DELETE FROM Cars WHERE CarID = " + con.escape(parsed_parameter0___[1]) + "", function (err, recordset) {

           if (err){

                 console.log(err);
		response.send('Failed - Error');
            }
            else{

                console.log('successful creation');
                response.header("Access-Control-Allow-Origin", "*");
                response.send('Successfully deleted '  + parsed_parameter0___[1] + ' ');
                }
                
       }); 
       
 con.end();

 });

});


app.get('/getCarsByLocation', function (request, response){

  var urlParts = url.parse(request.url, true);
  var parameters = urlParts.query;
  var Lat_ = parameters.Lat;
  var Long_ = parameters.Long;
  // e.g. myVenues = 12;
  var mysql = require('mysql');
  console.log('here1');
  console.log('myParam= ${myParam}');

    // config for your dat
  var con = mysql.createConnection({
        host: "rwandasos.c0k8vg0pevvr.us-east-2.rds.amazonaws.com",
        user: "sgahima",
        password: "KW220987",
        database: "COEN268_Project"
        });
  console.log('heregetCarsByLocation');
    // connect to your database
    con.connect(function (err) {

        if (err) console.log(err);

        // query to the database and get the records
       // con.query("SELECT * from Cars WHERE OwnerUsername=" + con.escape(OwnerUsername_) + "", function (err, recordset) {
        con.query("SELECT CarID, ( 3959 * acos( cos( radians(" + con.escape(Lat_) + ") ) * cos( radians( lat ) ) * cos( radians( lng ) - radians(" + con.escape(Long_) + ") ) + sin( radians(" + con.escape(Lat_) + ") ) * sin( radians( lat ) ) ) ) AS distance FROM Cars HAVING distance < 25 ORDER BY distance LIMIT 0 , 20", function (err, recordset) {
       

            if (err){

                 console.log(err)
            }
            else{
         // send records as a response

            var recordsetString = JSON.stringify(recordset);
            var json = JSON.parse(recordsetString);
            console.log(json);
            if(recordset.isEmpty()) {
                // Object is empty

                response.header("Access-Control-Allow-Origin", "*");
                response.send('No record');
            } else {

                console.log('heregetCarByLoc3');

                console.log(json);
               response.header("Access-Control-Allow-Origin", "*");
               response.send(json);
            }

           }
           
        });
        
  con.end();


 });
 
});



app.get('/getCarsByKey', function (request, response){

  var urlParts = url.parse(request.url, true);
  var parameters = urlParts.query;
  //var OwnerUsername_ = parameters.OwnerUsername;
  // e.g. myVenues = 12;
  var mysql = require('mysql');
  console.log('here1');
 console.log('myParam= ${myParam}');

 var parsed_parameter = urlParts.path;
 var parsed_parameter_ = parsed_parameter.split('?');
 var parsed_parameter__ = parsed_parameter_[1].split('&');
 var Key_Value0___ = parsed_parameter__[0].split('=');
 
 console.log('Inputs: ' + Key_Value0___[0] + '&' + Key_Value0___[1]);
    // config for your dat
 var con = mysql.createConnection({
        host: "rwandasos.c0k8vg0pevvr.us-east-2.rds.amazonaws.com",
        user: "sgahima",
        password: "KW220987",
        database: "COEN268_Project"
        });
 console.log('heregetCarsByKey');
    // connect to your database
 con.connect(function (err) {

        if (err) console.log(err);

        // query to the database and get the records
        con.query("SELECT CarID from Cars WHERE " + Key_Value0___[0] + "=" + con.escape(Key_Value0___[1]) + "", function (err, recordset) {
          console.log("SELECT CarID from Cars WHERE " + Key_Value0___[0] + "=" + con.escape(Key_Value0___[1]) + "");
            if (err){

                 console.log(err)
            }
            else{
         // send records as a response

            var recordsetString = JSON.stringify(recordset);
            var json = JSON.parse(recordsetString);
            console.log(json);
            if(recordset.isEmpty()) {
                // Object is empty

                response.header("Access-Control-Allow-Origin", "*");
                response.send('No record');
            } else {

                console.log('heregetCar3');

                console.log("JSON: " + json + "\n");
               response.header("Access-Control-Allow-Origin", "*");
               response.send(json);
               
            }

           }
           
        });
        
 con.end();

 });
 
});

           
