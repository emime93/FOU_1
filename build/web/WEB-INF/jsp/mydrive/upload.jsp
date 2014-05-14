
<script src="<c:url value = "/js/fileUpload.js"/> ">

</script>
<section id="file-system-container">
    <section id="file-system-container-top"></section>
    <section id="file-system-container-bottom">
        <form id="form1" enctype="multipart/form-data" method="POST">
            <div class="fileUpload">
                <button type="button" class="btn btn-primary">choose file</button>
                <input type="file" name="file" id="file" onchange="fileSelected();" class="upload"/>
                
            </div>
            <div id="fileName" class="label label-danger"></div>
            <div id="fileSize" class="label label-primary"></div>
            <div id="fileType" class="label label-info"></div>
            <input type="button" onclick="uploadFile()" value="Upload" class="btn btn-info"/>
            
        </form>
        <div id="progress-bar-container">
            <div id="progressNumber"></div>
        </div>
        <input type="text" id="tag-input" onkeypress="if (event.keyCode == 13) {
                    appendTags(this.value);
                    this.value = '';
                }
                "/>
        <div id="tag-upload-container">
        </div>
    </section>
</section>

