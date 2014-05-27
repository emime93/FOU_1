<link rel="stylesheet" type="text/css" href="<c:url value="/css/bootstrap-tagsinput.css" />" />
<section class="container-fluid" style="max-width:900px;background-color: #fff;">
    <div class="panel-group" id="accordion" style="margin-top:60px;">
        <c:forEach var="file" items = "${files}" >
            <div class="panel panel-default">
                <div class="panel-heading">
                    <h4 class="panel-title">
                        <a data-toggle="collapse" data-parent="#accordion" href="#${file.downloadLink}">
                            ${file.title}
                        </a>
                    </h4>
                </div>
                <div id="${file.downloadLink}" class="panel-collapse collapse">
                    <div class="panel-body">
                        <div class="collumn">
                            <div class="col-xs-6 col-md-4">
                                <div class="panel panel-default">
                                    <div class="panel-heading">Tags</div>
                                    <div class="panel-body">
                                        <c:forEach var="tag" items="${file.tags}">
                                            <span class="label label-danger" style="display:block;float:left;margin:10px 0 0 2px;">${tag}</span>
                                        </c:forEach>
                                    </div>
                                </div>
                            </div>
                        </div>
                        <div class="collumn">
                            <div class="col-xs-6 col-md-4">
                                    <div class="panel panel-default">
                                        <div class="panel-heading">Description</div>
                                        <div class="panel-body">
                                           ${file.description}
                                        </div>
                                    </div>
                            </div>
                        </div>
                        <div class="collumn">
                            <div class="col-xs-6 col-md-4">
                                <div class="panel panel-default" style="min-width:200px;">
                                    <div class="panel-heading">Last modified: ${file.date}</div>
                                    <div class="panel-body">
                                        <ul class="nav nav-pills nav-stacked">
                                            <li class="active"><a href="files/${file.downloadLink}">Download</a></li>
                                            <li style="cursor:pointer;" onmousedown="deleteFile('${file.downloadLink}')"><a>Delete</a></li>
                                        </ul>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </c:forEach>
    </div>
</section>  
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
                        <input type="file" name="file" id="file" onchange="fileSelected();" class="upload" required="required"/>
                    </div>
                </form>
                <div class="panel panel-default" style="min-width:200px;">
                    <div class="panel-heading">Description</div>
                    <div class="panel-body">
                        <textarea class="form-control" rows="3" id="file-description"></textarea>
                    </div>
                </div>
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