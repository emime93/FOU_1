/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
function dodo(e) {
    
}
function uploadDropboxFile() {
    var path = document.getElementById('edit-title').innerHTML;
    var fd = new FormData();
    var tags = document.getElementById("upload-file-tags").value;
    var description = document.getElementById("upload-file-description").value;
    var file = document.getElementById('file').files[0];

    if (file == null || tags == "" || description == "")
        alert("please add all the required info");
    else {
        fd.append("file", file);
        fd.append("tags", tags);
        fd.append("description", description);
        fd.append("path", path);
        fd.append("title","dropbox");
        var xhr = new XMLHttpRequest();

        xhr.upload.addEventListener("progress", uploadProgress, false);
        xhr.addEventListener("load", uploadComplete, false);
        xhr.addEventListener("error", uploadFailed, false);
        xhr.addEventListener("abort", uploadCanceled, false);

        xhr.open("POST", "http://localhost:8080/FOU_1/my-drive/file-upload");
        xhr.send(fd);
        
    }
    
}
function getDropboxFileInfo() {
    var path = document.getElementById('edit-title').innerHTML;
    $.ajax({
        type: "GET",
        url: "http://localhost:8080/FOU_1/my-drive/dropbox/get-info",
        data: {path: path}
    }).done(function(arr) {
        $(".bootstrap-tagsinput input").empty();
        $(".bootstrap-tagsinput input").val(arr[0]);
        document.getElementById('file-description').value = arr[1];
        
        var e = jQuery.Event("keydown");
        e.which = 14; // # Some key code value
        e.keyCode = 14;
        $(".bootstrap-tagsinput input").trigger(e);
        
    }).fail(function(msg) {
        alert("error:" + msg);
    });
}
function editDropboxFile() {

    var tags = document.getElementById('file-tags-input').value;
    var path = document.getElementById('edit-title').innerHTML;
    var description = document.getElementById('file-description').value;
    
    $.ajax({
        type: "POST",
        url: "http://localhost:8080/FOU_1/my-drive/dropbox/edit",
        data: {path: path, tags: tags, description: description}
    }).done(function(msg) {
        location.reload();
        document.getElementById("close-edit-modal").click();
    }).fail(function(msg) {
        alert("error:" + msg);
    });

}
function uploadFile() {
    var fd = new FormData();
    var tags = document.getElementById("file-tags").value;
    var description = document.getElementById("file-description").value;
    var file = document.getElementById('file').files[0];

    if (file == null || tags == "" || description == "")
        alert("please add all the required info");
    else {
        fd.append("file", file);
        fd.append("tags", tags);
        fd.append("description", description);
        fd.append("title","my-drive");
        var xhr = new XMLHttpRequest();

        /* event listners */

        xhr.upload.addEventListener("progress", uploadProgress, false);
        xhr.addEventListener("load", uploadComplete, false);
        xhr.addEventListener("error", uploadFailed, false);
        xhr.addEventListener("abort", uploadCanceled, false);

        xhr.open("POST", "my-drive/file-upload");
        xhr.send(fd);
    }

}
function deleteDropboxFile(path) {
     $.ajax({
        type: "POST",
        url: "http://localhost:8080/FOU_1/my-drive/dropbox/delete",
        data: {path: path}
    }).done(function(msg) {
         location.reload();
    }).fail(function(msg) {
        alert("error:" + msg);
    });
}
function deleteFile(fileID) {
    $.ajax({
        type: "POST",
        url: "http://localhost:8080/FOU_1/my-drive/delete",
        data: {fileID: fileID}
    }).done(function(msg) {
        getTableValues("all");
    }).fail(function(msg) {
        alert("error:" + msg);
    });
}

function fileSelected() {
    var file = document.getElementById('file').files[0];
    if (file) {
        var fileSize = 0;
        if (file.size > 1024 * 1024)
            fileSize = (Math.round(file.size * 100 / (1024 * 1024)) / 100).toString() + 'MB';
        else
            fileSize = (Math.round(file.size * 100 / 1024) / 100).toString() + 'KB';

        document.getElementById('fileName').style.display = "block";
        document.getElementById('fileSize').style.display = "block";
        document.getElementById('fileType').style.display = "block";
        
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
    }
    else {
        document.getElementById('progressNumber').innerHTML = 'unable to compute';
    }
}

function uploadComplete(evt) {
    /* This event is raised when the server send back a response */
    getTableValues("all");
    document.getElementById('fileName').innerHTML = '';
    document.getElementById('fileSize').innerHTML = '';
    document.getElementById('fileType').innerHTML = '';
    document.getElementById('progress-bar-upload').style.width = "0%";
    document.getElementById("show-notification").click();
    document.getElementById("close-upload-modal").click();
    document.getElementById("file-tags").value = "";
}

function uploadFailed(evt) {
    alert("There was an error attempting to upload the file.");
}

function uploadCanceled(evt) {
    alert("The upload has been canceled by the user or the browser dropped the connection.");
}
