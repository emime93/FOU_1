 <nav class="nav-bar">
            <ul>
                <li style="float:left;"><a href=""><image src="<c:url value="/images/icons/download12.png" />" id="avatar-img"/></a></li>
                <li><a><image src="<c:url value="/images/icons/user91.png" />" id="avatar-img"/></a>
                    <c:if test="${empty username}">
                        <ul>
                            <li><a href="signup">sign up</a></li>
                            <li><a href="login">login</a></li>
                        </ul>
                    </c:if>
                    <c:if test="${not empty username}">
                        <!--<ul>
                            <li><a>Welcome, ${username}</a></li>
                            <li><a href="<c:url value="/logout" />">logout</a></li>
                        </ul>
                        -->
                    </c:if>
                </li>
                <c:if test="${not empty username}">
                    <li><a id="search-btn"><input placeholder="search"/><image src="<c:url value="/images/icons/magnifier12.png" />" id="avatar-img"/></a>
                    </li>
                </c:if>
                <li><a><image src="<c:url value="/images/icons/facebook12.png" />" id="avatar-img" /></a></li>
            </ul>
        </nav>