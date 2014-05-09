/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
function appendTags(text){
    
    var tag = document.createElement("div");
    tag.style.display = "inline";
    tag.style.backgroundColor = "#0099ff";
    tag.style.padding = "10px";
    tag.style.borderRadius = "5px";
    tag.style.cursor = "pointer";
    tag.style.marginTop = "10px";
    tag.style.color = "#fff";
    
    tag.appendChild(document.createTextNode(text));
    document.getElementById("tag-upload-container").appendChild(tag);
}

