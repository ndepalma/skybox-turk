/*! networking definitions for skybox interfacing 2014-6-16
* This includes websockets and POST functions
*
* @author ndepalma
* skybox_network.js
* Licensed MIT */

//////////////////////////////////////////
// Global Host
//////////////////////////////////////////

// which server to point to - localhost for debugging
var default_port = 8080;
var global_name = document.domain + ":" + default_port;
var global_host = global_name+"/"; // for testing
//var global_host = "nimbus.media.mit.edu/servlets/obj-collect-1.0-SNAPSHOT-standalone/"; // for deployment
var ws_connection;

var room_num;
var has_ui;
var UID;

//connect websocket and hold the connection to match state
function connectWSLive(_uid, _room_num) {
    try {
        console.log("Opening websocket");
        ws_connection = new WebSocket('ws://'+ global_host + 'attachroom?UID='+_uid+'&room-num='+_room_num);

        ws_connection.onopen = function(){
            /*Send a small message to the console once the connection is established */
            console.log('Connection open!');
        }
	ws_connection.onclose = function(){
	   console.log('Connection closed');
	}
	ws_connection.onerror = function(error){
	   console.log('Error detected: ' + error.message);
	}
	ws_connection.onmessage = function(e){
	    var server_message = e.data;
	    console.log("msg: " + server_message);
            jsob =eval("(" +  server_message + ")");
            console.log("JSOB: " + JSON.stringify(jsob));
            if(jsob == null) {
                console.log("Returning null for some reason?");
                return;
            }

            if("play" in jsob) {
                console.log("Got a play event!");
                document.getElementById("video1").play();
                timestream();
            } else if("rdebug" in jsob) {
                if(typeof(vizdebug) != "undefined") {
                    if(!vizdebug) {
                        debugOn();
                    }
                    rdebug = jsob.rdebug;
                    console.log("Debug: " + rdebug);
                    targetObj = "#" + rdebug.target;
                    rx = rdebug.x * $(document).width() - 75 + "px";
                    ry = (1.0-rdebug.y) * $(document).height() - 75 + "px";
                    if(typeof($(targetObj)) != "undefined") {
                        $(targetObj).css("left", rx)
                        $(targetObj).css("top", ry)
                    }
                }
            } else if("movie" in jsob) {
                movname = jsob.movie;
                setMovie(movname);
            } else {
                //console.log("ELSE");
	        read_all(jsob);
            }
	}
    }
    catch(err) {
        console.log(" websocket fail!");
    }
}


function sendMsg(msg) {
    ws_connection.send(msg);
}
