<!DOCTYPE html>
<html>
<head>
    <meta name="layout" content="a5">
    <title>Elan Demo</title>
    <style>
        #demo-elan {
            margin-top: 70px;
        }
        #demo-orientation-timeline {
            margin: 0px;
            padding: 0px;
            position: relative;
            float: none;
            width: 100%;
            height: 16px;
        }
        canvas {
            margin: 0px;
            padding: 0px;
        }
    </style>
</head>
<body>
    <div id="demo-elan">
        <div>
            <canvas id="demo-orientation-timeline">
                Your browser does not support the HTML5 canvas tag.
            </canvas>
        </div>
        <canvas draggable="true" id="demo-elan-audiocanvas" height="256">
            Your browser does not support the HTML5 canvas tag.
        </canvas>
        <div id="videoWrapper">
            <audio id="demo-elan-videoclient" style="width: 100%" controls>
                <source id="demo-elan-videoclient-src" src="/api/media/a5-1/a5:/test-eaf-wav" type="audio/wav">
                Your browser does not support the video tag.
            </audio>
        </div>
        <canvas draggable="true" id="demo-elan-textcanvas" height="400">
            Your browser does not support the HTML5 canvas tag.
        </canvas>
    </div>
    <script type="text/javascript">
        $(document).ready(function() {
            $(document).demoelan("${request.contextPath}");
        });
    </script>
</body>
</html>