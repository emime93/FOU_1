/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

function uploadFile() {
    var fd = new FormData();
    fd.append("file", document.getElementById('file').files[0]);

    var xhr = new XMLHttpRequest();

    /* event listners */
    xhr.upload.addEventListener("progress", uploadProgress, false);
    xhr.addEventListener("load", uploadComplete, false);
    xhr.addEventListener("error", uploadFailed, false);
    xhr.addEventListener("abort", uploadCanceled, false);
    /* Be sure to change the url below to the url of your upload server side script */
    if (xhr.readyState == 4 && xhr.status == 200) {

    }
    xhr.open("POST", "my-drive/file-upload");
    xhr.send(fd);
}

function fileSelected() {
    var file = document.getElementById('file').files[0];
    if (file) {
        var fileSize = 0;
        if (file.size > 1024 * 1024)
            fileSize = (Math.round(file.size * 100 / (1024 * 1024)) / 100).toString() + 'MB';
        else
            fileSize = (Math.round(file.size * 100 / 1024) / 100).toString() + 'KB';

        document.getElementById('fileName').innerHTML = 'Name: ' + file.name;
        document.getElementById('fileSize').innerHTML = 'Size: ' + fileSize;
        document.getElementById('fileType').innerHTML = 'Type: ' + file.type;
        document.getElementById('progress-bar-upload').style.width = "0%";
        document.getElementById('progressNumber').innerHTML = "";

        var elements = document.getElementsByClassName("fileDescription");
        for (var i = 0; i < elements.length; ++i) {
            elements[i].style.opacity = 1.0;
        }
    }
}

function uploadProgress(evt) {
    if (evt.lengthComputable) {
        var percentComplete = Math.round(evt.loaded * 100 / evt.total);
        document.getElementById('progress-bar-upload').style.width = percentComplete + "%";
        document.getElementById('progressNumber').innerHTML = percentComplete + "%";
    }
    else {
        document.getElementById('progressNumber').innerHTML = 'unable to compute';
    }
}

function uploadComplete(evt) {
    /* This event is raised when the server send back a response */

    document.getElementById('fileName').innerHTML = '';
    document.getElementById('fileSize').innerHTML = '';
    document.getElementById('fileType').innerHTML = '';
    document.getElementById('progress-bar-upload').style.width = "0%";
    document.getElementById("show-notification").click();
    document.getElementById("close-upload-modal").click();
}

function uploadFailed(evt) {
    alert("There was an error attempting to upload the file.");
}

function uploadCanceled(evt) {
    alert("The upload has been canceled by the user or the browser dropped the connection.");
}
