 <link rel="stylesheet" type="text/css" href="<c:url value="/css/bootstrap-tagsinput.css" />" />
<section id="file-system-container">
    <section id="file-system-container-top">
    </section>
    <section id="file-system-container-bottom">
        <table class="table" id="files-table">
            <tr />
            <td></td>
            <td>Title</td>
            <td>Size</td>
            <td>Date</td>

            <c:forEach var="file" items = "${files}" >
                <tr />
                <td ><img src="images/icons/star105.png" style="width:25px;"/></td>
                <td ><span>${file.title}</span></td>
                <td ><span>${file.size}</span></td>
                <td ><span>${file.date}</span></td>
                <td >
                    <a href="files/${file.downloadLink}">
                        <img src="images/icons/download14.png" style="width:25px;" 
                             onmouseover="this.src = 'images/icons/download14_white.png'" onmouseout="this.src = 'images/icons/download14.png'"/>
                    </a>
                </td>
            </c:forEach>

        </table>
    </section>  

</section>
<script src="<c:url value = "/js/fileUpload.js"/> "></script>
<div class="modal fade" id="my-modal">
    <div class="modal-dialog">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
                <h4 class="modal-title">Upload</h4>
            </div>
            <div class="modal-body">
                <form id="form1" enctype="multipart/form-data" method="POST">
                    <div class="fileUpload">
                        <button type="button" class="btn btn-primary">choose file</button>
                        <input type="file" name="file" id="file" onchange="fileSelected();" class="upload"/>
                    </div>
                </form>
                <input type="text" value="" data-role="tagsinput" placeholder="Add tags" id="file-tags"/>
                <div class="progress progress-striped active">
                    <div id="progress-bar-upload" class="progress-bar"  role="progressbar" aria-valuenow="45" aria-valuemin="0" aria-valuemax="100" style="width: 0%">
                        <span class="sr-only">80% Complete</span>
                    </div>
                </div>
            </div>
            <div class="modal-footer">
                <div style="float:left;">
                <div id="fileName" class="label label-danger"></div>
                <div id="fileSize" class="label label-primary"></div>
                <div id="fileType" class="label label-info"></div>
                </div>
                <button type="button" class="btn btn-default" data-dismiss="modal" id="close-upload-modal">Close</button>
                <button type="button" class="btn btn-primary" onclick="uploadFile()">upload</button>
            </div>
        </div><!-- /.modal-content -->
    </div><!-- /.modal-dialog -->
</div><!-- /.modal -->

<script src="<c:url value = "/js/bootstrap-tagsinput-angular.js"/> "></script>
<script src="<c:url value = "/js/bootstrap-tagsinput.js"/> "></script>
<script src="<c:url value = "/js/bootstrap-tagsinput.min.js"/> "></script>