<section id="file-system-container">
    <nav>
        <ul>
            <li style="padding-left: 0px;">title</li>
            <li style="padding-left: 380px;">size</li>
            <li style="padding-left: 18px;">uploaded</li>
        </ul>
        <hr />
    </nav>
    <table id="files-container" cellspacing="0">
        <c:forEach var="file" items = "${files}" >
            <tr />
            <td class="td-favorites"><img src="images/icons/star105.png"/></td>
            <td class="td-title"><span>${file.title}</span></td>
            <td class="td-size"><span>${file.size}</span></td>
            <td class="td-date"><span>${file.date}</span></td>
            <td class="td-download">
                <a href="files/${file.downloadLink}">
                    <img src="images/icons/download14.png" style="width:25px;" 
                                         onmouseover="this.src='images/icons/download14_white.png'" onmouseout="this.src='images/icons/download14.png'"/>
                </a>
                </td>
        </c:forEach>
            
    </table>
</section>