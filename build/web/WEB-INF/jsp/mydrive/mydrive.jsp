<section id="file-system-container">
    <section id="file-system-container-top">
    </section>
    <section id="file-system-container-bottom">
    <table class="table">
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