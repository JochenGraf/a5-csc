<!DOCTYPE html>
<html lang="en">
<head>
    <title><g:layoutTitle default="KA3"/></title>
    <asset:javascript src="application.js"/>
    <asset:stylesheet src="application.css"/>
    <g:layoutHead/>
</head>
<body>
<div id="header">
    <nav class="navbar navbar-default navbar-static-top">
        <div class="container-fluid">
            <div class="navbar-header">
                <button type="button" class="navbar-toggle collapsed" data-toggle="collapse" data-target="#navbar"
                        aria-expanded="false" aria-controls="navbar">
                    <span class="sr-only">Toggle navigation</span>
                    <span class="icon-bar"></span>
                    <span class="icon-bar"></span>
                    <span class="icon-bar"></span>
                </button>
                <a class="navbar-brand" href="${request.contextPath}/">A<sup>5</sup></a>
            </div>
            <div id="navbar" class="navbar-collapse collapse">
                <ul class="nav navbar-nav">
                    <li ${request.getRequestURI().contains('/apidoc') ? 'class=active' : ''} class="dropdown">
                        <a href="#" class="dropdown-toggle" data-toggle="dropdown" role="button"
                           aria-haspopup="true" aria-expanded="false">API Reference <span class="caret"></span></a>
                        <ul class="dropdown-menu">
                            <li><a href="${request.contextPath}/apidoc/object"><strong>Object API</strong></a></li>
                            <li><a href="${request.contextPath}/apidoc/query"><strong>Query API</strong></a></li>
                            <li><a href="${request.contextPath}/apidoc/media"><strong>Media API</strong></a></li>
                            <li><a href="${request.contextPath}/apidoc/annotation"><strong>Annotation API</strong></a></li>
                            <li><a href="${request.contextPath}/apidoc/oai"><strong>OAI-PMH</strong></a></li>
                            <li role="separator" class="divider"></li>
                        </ul>
                    </li>
                    <%--
                        <li><a target="_blank" href="https://redmine.uni-koeln.de/projects/ka3">Code</a></li>
                    --%>
                </ul>
            </div>
        </div>
    </nav>
</div>
<div id="content" class="container-fluid">
    <g:layoutBody/>
</div>
</body>
</html>