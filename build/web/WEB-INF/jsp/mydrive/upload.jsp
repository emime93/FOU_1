<script src="<c:url value = "/js/fileUpload.js"/> ">

</script>
<section id="file-system-container">
    <form id="form1" enctype="multipart/form-data" method="POST">
        <div class="fileUpload">
            <button type="button">choose file</button>
            <input type="file" name="file" id="file" onchange="fileSelected();" class="upload"/>
        </div>
        <div id="fileName" class="fileDescription"></div>
        <div id="fileSize" class="fileDescription"></div>
        <div id="fileType" class="fileDescription"></div>
        <input type="button" onclick="uploadFile()" value="Upload" class="upload-btn"/>
    </form>
        <div id="progress-bar-container">
            <div id="progressNumber"></div>
        </div>
        <input type="text" id="tag-input" onkeypress="if(event.keyCode == 13){ appendTags(this.value); this.value = '';} "/>
        <div id="tag-upload-container">
            
        </div>

</section>

