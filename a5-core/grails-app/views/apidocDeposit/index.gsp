<!DOCTYPE html>
<html>
	<head>
		<meta name="layout" content="a5"/>
		<title>${grailsApplication.config.getProperty('a5.apideposit.info.name')}</title>
	</head>
	<body>
        <h1>${grailsApplication.config.getProperty('a5.apideposit.info.name')}</h1>
        <p>${grailsApplication.config.getProperty('a5.apideposit.info.description')}</p>
        <h5>Contents</h5>
        <ul id="toc" class="list-unstyled"></ul>
        <div>
            <apidoc:h2 request="${req.upload}"/>
            <apidoc:h2 request="${req.stream}"/>
            <apidoc:h2 request="${req.delete}"/>
            <apidoc:h2 request="${req.info}"/>
            <apidoc:h2 request="${req.list}"/>
        </div>
	</body>
</html>