<section class="container-fluid" style="max-width:900px;background-color: #fff;">
    <c:choose>
        <c:when test="${dropbox_token == null}">
            <img src="<c:url value="/images/icons/dropbox_icon.ico" />" style="position:absolute;top:50%;left:50%;margin:-150px 0 0 -125px;"/>
            <a href="<c:url value="/dropbox" />"><button type="button" class="btn btn-primary success" style="position:absolute;top:50%;left:50%;margin:100px 0 0 -50px;">Link account</button></a>
        </c:when>
        <c:otherwise>
            <div class="panel-group" id="accordion" style="margin-top:60px;">
                <c:forEach var="file" items = "${dropbox_entities}" >
                    <div class="panel panel-default">
                        <div class="panel-heading">
                            <h4 class="panel-title">
                                <c:choose>
                                    <c:when test="${file.type.equals('folder')}">
                                        <a href="<c:url value="../dropbox${file.path}" />">${file.name}<a/>
                                        </c:when>
                                        <c:otherwise>
                                            <a data-toggle="collapse" data-parent="#accordion" href="#${file.rev}">
                                                ${file.name}
                                            </a></c:otherwise>
                                    </c:choose>
                            </h4>
                        </div>
                        <div id="${file.rev}" class="panel-collapse collapse">
                            <div class="panel-body">
                                <div class="collumn">
                                    <div class="col-xs-6 col-md-4">
                                        <div class="panel panel-default">
                                            <div class="panel-heading">Tags</div>
                                            <div class="panel-body">

                                            </div>
                                        </div>
                                    </div>
                                </div>
                                <div class="collumn">
                                    <div class="col-xs-6 col-md-4">
                                        <div class="panel panel-default">
                                            <div class="panel-heading">Description</div>
                                            <div class="panel-body">

                                            </div>
                                        </div>
                                    </div>
                                </div>
                                <div class="collumn">
                                    <div class="col-xs-6 col-md-4">
                                        <div class="panel panel-default" style="min-width:200px;">
                                            <div class="panel-heading">Last modified: ${file.lastModified}</div>
                                            <div class="panel-body">
                                                <ul class="nav nav-pills nav-stacked">
                                                    <li class="active"><a href="<c:url value="/my-drive/dropbox/downloads${file.path}" />">Download</a></li>
                                                    <li style="cursor:pointer;" onmousedown="deleteFile('${file.path}')"><a>Delete</a></li>
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
        </c:otherwise>
    </c:choose>
</section>  