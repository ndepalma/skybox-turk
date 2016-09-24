var lineavailable = false;

function createLine(ax, ay, length, calc) {
    document.body.innerHTML += "<div id='line' style='height:" + length + "px;width:2px;background-color:red;position:absolute;top:" + (ay) + "px;left:" + (ax) + "px;transform:rotate(" + calc + "deg);-ms-transform:rotate(" + calc + "deg);transform-origin:0% 0%;-moz-transform:rotate(" + calc + "deg);-moz-transform-origin:0% 0%;-webkit-transform:rotate(" + calc  + "deg);-webkit-transform-origin:0% 0%;-o-transform:rotate(" + calc + "deg);-o-transform-origin:0% 0%;'></div>"
}

function updateLine(ax, ay, length,calc) {
   document.getElementById("line").style["height"] = length + "px";
   document.getElementById("line").style["top"] = ay + "px";
   document.getElementById("line").style["left"] = ax + "px";

   document.getElementById("line").style["transform"] = "rotate(" + calc + "deg)";
   document.getElementById("line").style["-ms-transform"] = "rotate(" + calc + "deg)";
   document.getElementById("line").style["-moz-transform"] = "rotate(" + calc + "deg)";
   document.getElementById("line").style["-webkit-transform"] = "rotate(" + calc + "deg)";
   document.getElementById("line").style["-o-transform"] = "rotate(" + calc + "deg)";
}


function linedraw(ax,ay,bx,by)
{
    var calc=Math.atan((ay-by)/(bx-ax));
    calc=-calc*180/Math.PI - 90;
    var length=Math.sqrt((ax-bx)*(ax-bx)+(ay-by)*(ay-by));
    if(!lineavailable) {
      createLine(ax, ay, length, calc);
      lineavailable = true;
    } else {
   //   console.log("ax: " + ax + " ay:" + ay + " l:" + length + " calc: " + calc);
      updateLine(ax, ay, length, calc);
    }
}

function hideLine() {
  updateLine(0, 0, 0, 0);
}



//document.body.addEventListener('mousedown',function(e){
//    alert(123);
//},false);

var md = false;
var startX;
var startY;
var endX;
var endY;
function linedown() {
 // console.log("X:" + event.clientX + " Y:" + event.clientY);
  startX = event.clientX;
  startY = event.clientY;
  md = true;
}
function lineup() {
  //console.log("EX:" + event.clientX + " EY:" + event.clientY);
  endX = event.clientX;
  endY = event.clientY;

  md= false;
  hideLine();
//  linedraw(startX, startY, endX, endY);
}

function linedrag() {
  if(md) {
    linedraw(startX, startY, event.clientX, event.clientY);
  }
}
