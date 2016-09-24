function getParameterByName(name) {
    name = name.replace(/[\[]/, "\\\[").replace(/[\]]/, "\\\]");
    var regex = new RegExp("[\\?&]" + name + "=([^&#]*)"),
        results = regex.exec(location.search);
    return results == null ? "" : decodeURIComponent(results[1].replace(/\+/g, " "));
}

function generateUUID() {
    var d = new Date().getTime();
    var uuid = 'xxxxxxxx-xxxx-4xxx-yxxx-xxxxxxxxxxxx'.replace(/[xy]/g, function(c) {
        var r = (d + Math.random()*16)%16 | 0;
        d = Math.floor(d/16);
        return (c=='x' ? r : (r&0x3|0x8)).toString(16);
    });
    return uuid;
};

function takenExp() {
  var exp = getParameterByName("ex")+getParameterByName("movname");
  var name = "ndggprg" + exp;
  var val = $.cookie(name);
  return typeof val !== 'undefined';
}

function deleteExp() {
  var exp = getParameterByName("ex")+getParameterByName("movname");
  var name = "ndggprg" + exp;
  $.removeCookie(name);
}

function markExp(iduser) {
  var exp = getParameterByName("ex")+getParameterByName("movname");
  var name = "ndggprg" + exp;
  $.cookie(name, iduser, {expires:365});
}


function defaultSubmit(submitName, dat_out) {
  console.log("submit!");
  $(submitName).attr("disabled", true);

  var info = jsPsych.turk.turkInfo();
  var idUser;
  if(info.outsideTurk) {
    idUser = generateUUID();
  } else {
    idUser = info.workerId;
  }

  jsPsych.turk.submitToTurk(dat_out);


  markExp(idUser);
  setTimeout(function() {
     document.location='https://nimbus.media.mit.edu/story//terminate/2015/09/27/epsilon.html';
  }, 4000);
}

function endevt() {
  console.log("end event");
}




//////////////////////////////
// function agreed() {
//   console.log("They agreed.");
//   $("#consent").toggle();
//   $("#shadow").toggle();
// }

// function declined() {
//   console.log("They declined.");
// }

// if(turkInfo.previewMode && !turkInfo.outsideTurk) {
//   $("#golink").toggle();
// }

// if(!turkInfo.previewMode && !turkInfo.outsideTurk) {
//   $("#shadow").toggle();
//   $("#consent").toggle();
// }

// setTimeout(function() {
//   if(turkInfo.outsideTurk) {
//     if(takenExp()) {
//       $("#golink").toggle();
//       $("#instruction_block").html("<h2> You have already participated in this experiment</h2>");
//     } else {
//       $("#shadow").toggle();
//       $("#consent").toggle();
//     }
//   }
// }, 800);

// $("#consent").load("{{ '/consent/2015/09/27/alpha.html' | prepend: site.baseurl | prepend: site.url }} article", function() {
//    $("#agreeBut").attr("href", "#");
//    $("#agreeBut").attr("onclick", "agreed();");
//    $("#declineBut").attr("onclick", "declined();");
//    $("#declineBut").attr("href", "#");
//    $("#shadow").css("height", $(document).height());
// });

// $("#consent").toggle();
// $("#shadow").toggle();
