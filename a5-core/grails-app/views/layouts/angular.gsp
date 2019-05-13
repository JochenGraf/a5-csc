<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="utf-8">
    <base href="/angular2/">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>KA3 Demo</title>
    <link rel="stylesheet" href="https://stackpath.bootstrapcdn.com/font-awesome/4.7.0/css/font-awesome.min.css">
    <script>
        var annotationApi = '${grailsApplication.config.getProperty('a5.api.annotation')}';
        var mediaApi = '${grailsApplication.config.getProperty('a5.api.media')}';
        var objectApi = '${grailsApplication.config.getProperty('a5.api.object')}';
        var queryApi = '${grailsApplication.config.getProperty('a5.api.query')}';
    </script>
</head>
<body>
    <g:layoutBody/>
    <script src="${request.contextPath}/assets/ng/dist/inline.bundle.js"></script>
    <script src="${request.contextPath}/assets/ng/dist/vendor.bundle.js"></script>
    <script src="${request.contextPath}/assets/ng/dist/main.bundle.js"></script>
    <script src="${request.contextPath}/assets/ng/dist/styles.bundle.js"></script>
</body>
</html>