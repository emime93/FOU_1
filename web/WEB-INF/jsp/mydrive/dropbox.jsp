<section id="file-system-container">
    <c:choose>
        <c:when test="${dropbox_token == null}">
            <img src="<c:url value="/images/icons/dropbox_icon.ico" />" style="position:absolute;top:50%;left:50%;margin:-150px 0 0 -125px;"/>
            <a href="<c:url value="/dropbox" />"><button type="button" class="btn btn-primary success" style="position:absolute;top:50%;left:50%;margin:100px 0 0 -50px;">Link account</button></a>
        </c:when>
        <c:otherwise>
            <section id="file-system-container">
                <section id="file-system-container-top">
                    <h3>Welcome, ${dropbox_username} </h3>
                    <img src="<c:url value="/images/icons/cloud32.png" />"
                         onmouseover="this.src='<c:url value="/images/icons/cloud32_white.png" />'" 
                         onmouseout = "this.src='<c:url value="/images/icons/cloud32.png" />' "/>
                    
                </section>
                <section id="file-system-container-bottom">
                    <table class="table">
                        <tr />
                        <td >Title</td>
                        <td >Size</td>
                        <td >Last modified</td>
                        <td > </td>

                        <c:forEach var="entity" items = "${dropbox_entities}" >
                            <tr />
                            <td >
                                <c:choose>
                                    <c:when test="${entity.type.equals('folder')}"><a href="<c:url value="../dropbox${entity.path}" />"><span>${entity.name}</span><a/></c:when>
                                        <c:otherwise><span>${entity.name}</span></c:otherwise>
                                    </c:choose>

                            </td>
                            <td ><span>${entity.size}</span></td>
                            <td ><span>${entity.lastModified}</span></td>
                            <td >

                                <c:choose>
                                    <c:when test="${entity.type.equals('file')}">
                                        <a href="<c:url value="/my-drive/dropbox/downloads${entity.path}" />">
                                            <img src="<c:url value="/images/icons/download14.png"/>" style="width:25px;" />
                                        </a>
                                    </c:when>                                    
                                </c:choose>
                            </td>
                        </c:forEach>
                    </table>
                </section>
            </section>
        </c:otherwise>
    </c:choose>
</section>