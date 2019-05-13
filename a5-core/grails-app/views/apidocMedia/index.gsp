<!DOCTYPE html>
<html>
<head>
    <meta name="layout" content="a5"/>
    <title>Media API</title>
</head>
<body>
<h1>Media API</h1>
<p class="lead">API for media streaming and media processing.</p>
<p>The Media API follows the <strong>IIIF Image API</strong>:</p>
<dl class="dl-horizontal">
    <dt><a target="_blank"
           href="http://iiif.io/api/image/2.1/">[IIIF Image API]</a></dt>
    <dd>IIIF Image API 2.1. Edited by Michael Appleby, Tom Crane, Robert Sanderson, Jon Stroop, and Simeon Warner. Copyright © 2012-2016 Editors and contributors. Published by the IIIF Consortium under the CC-BY license.</dd>
</dl>
<p>A/V extensions are based on <a target="_blank" href="http://iiif.io/community/groups/av/">IIIF A/V Technical Specification Group</a>.</p>
<h5>Contents</h5>
<ul id="toc" class="list-unstyled"></ul>

<h2>Media File Request</h2>
<p><samp>GET {scheme}://{server}/api/media/{repository}/[{prefix}/]{identifier}</samp></p>
<p>Streams a media file from a file system according to a repository name and a unique identifier.
Supports <a target="_blank" href="https://tools.ietf.org/html/rfc7233">HTTP 1.1 Range Requests</a>.</p>
<h5>Examples</h5>
<dl class="dl-horizontal">
    <dt>Audio</dt>
    <dd><apidoc:link href="/api/media/a5-1/a5:/test-wav"/></dd>
    <dt>Image</dt>
    <dd><apidoc:link href="/api/media/a5-2/a5:/00-0000-0000-0000-0C8C-9"/></dd>
    <dt>Video</dt>
    <dd><apidoc:link href="/api/media/a5-1/a5:demo-mp4-1mb"/></dd>
    <dt>PDF</dt>
    <dd><apidoc:link href="/api/media/a5-1/a5:/test-pdf"/></dd>
    <dt>Text</dt>
    <dd><apidoc:link href="/api/media/a5-1/a5:test-txt"/></dd>
    <dt>XML</dt>
    <dd><apidoc:link href="/api/media/a5-1/a5:test-xml"/></dd>
</dl>
<%--
<apidoc:errtable>
    <tbody>
    <tr>
        <apidoc:errtablerow heading="Common"/>
    </tr>
    <tr>
        <apidoc:errtablerow err="${err.a50006}" arg="POST"/>
    </tr>
    <tr>
        <apidoc:errtablerow heading="Parameters"/>
    </tr>
    <tr>
        <apidoc:errtablerow err="${err.a50002}" arg="{repository}"/>
    </tr>
    <tr>
        <apidoc:errtablerow err="${err.a50003}" arg="[{prefix}/]{identifier}"/>
    </tr>
    </tbody>
</apidoc:errtable>
--%>

<h3>Repository <small>{repository}</small></h3>
<p><samp>GET {scheme}://{server}/api/media/<strong>{repository}</strong>/[{prefix}/]{identifier}</samp></p>
<p>The name of the repository.</p>
<apidoc:desctable>
    <tbody>
    <tr>
        <td><apidoc:link href="/api/media/a5-1/a5:/test-pdf" pattern="/a5-1/"/></td>
        <td>Returns a file from repository <samp>"a5-1"</samp>.</td>
    </tr>
    <tr>
        <td><apidoc:link href="/api/media/a5-2/a5:/00-0000-0000-0000-0C8C-9" pattern="/a5-2/"/></td>
        <td>Returns a file from repository <samp>"a5-2"</samp>.</td>
    </tr>
    </tbody>
</apidoc:desctable>

<h3>Identifier <small>[{prefix}/]{identifier}</small></h3>
<p><samp>GET {scheme}://{server}/api/media/{repository}/<strong>[{prefix}/]{identifier}</strong></samp></p>
<p>The unique identifier of the file.</p>
<apidoc:desctable>
    <tbody>
    <tr>
        <td><apidoc:link href="/api/media/a5-1/a5:/test-pdf" pattern="/a5:/test-pdf"/></td>
        <td>Returns a file by DOI <samp>"a5:/test-pdf"</samp>.</td>
    </tr>
    <tr>
        <td><apidoc:link href="/api/media/a5-1/a5:demo-mp4-1mb" pattern="/a5:demo-mp4-1mb"/></td>
        <td>Returns a file by UUID <samp>"a5:demo-mp4-1mb"</samp>.</td>
    </tr>
    </tbody>
</apidoc:desctable>

<h2>Media Manipulation Request</h2>
<p><samp>GET|HEAD {scheme}://{server}/api/media/{repository}/[{prefix}/]{identifier}/{section}/{region}/{size}/{rotation}/{filter}/{quality}.{format}</samp></p>
<p>Streams a media file in a manipulated form. Returns HTTP 202 if manipulation is in progress, HTTP 200 otherwise.</p>
<%--
<apidoc:errtable>
    <tbody>
    <tr>
        <apidoc:errtablerow heading="Common"/>
    </tr>
    <tr>
        <apidoc:errtablerow err="${err.a50006}" arg="POST"/>
    </tr>
    <tr>
        <apidoc:errtablerow err="${err.a50016}"/>
    </tr>
    <tr>
        <apidoc:errtablerow heading="Properties"/>
    </tr>
    <tr>
        <apidoc:errtablerow err="${err.a50002}" arg="{repository}"/>
    </tr>
    <tr>
        <apidoc:errtablerow err="${err.a50003}" arg="[{prefix}/]{identifier}"/>
    </tr>
    <tr>
        <apidoc:errtablerow err="${err.a50008}" arg="{section}"/>
    </tr>
    <tr>
        <apidoc:errtablerow err="${err.a50009}" arg="{region}"/>
    </tr>
    <tr>
        <apidoc:errtablerow err="${err.a50010}" arg="{size}"/>
    </tr>
    <tr>
        <apidoc:errtablerow err="${err.a50011}" arg="{rotation}"/>
    </tr>
    <tr>
        <apidoc:errtablerow err="${err.a50012}" arg="{filter}"/>
    </tr>
    <tr>
        <apidoc:errtablerow err="${err.a50007}" arg="{quality}"/>
    </tr>
    <tr>
        <apidoc:errtablerow err="${err.a50001}" arg="{format}"/>
    </tr>
    </tbody>
</apidoc:errtable>
--%>

<h3>Section <small>{section}</small></h3>
<p><samp>GET|HEAD {scheme}://{server}/api/media/{repository}/[{prefix}/]{identifier}/<strong>{section}</strong>/{region}/{size}/{rotation}/{filter}/{quality}.{format}</samp></p>
<p>Extracts a time section from a media file.</p>
<apidoc:desctable>
    <tbody>
    <tr>
        <td>
            <ul class="list-unstyled">
                <li>AUDIO: <apidoc:link href="/api/media/a5-1/a5:/test-wav/full/full/full/0/none/default.wav"/></li>
                <li>VIDEO: <apidoc:link href="/api/media/a5-1/a5:demo-mp4-1mb/full/full/full/0/none/default.mp4"/></li>
            </ul>
        </td>
        <td>The complete media file is returned without any cropping.</td>
        <td><samp>"full"</samp></td>
    </tr>
    <tr>
        <td>
            <ul class="list-unstyled">
                <li>AUDIO: <apidoc:link href="/api/media/a5-1/a5:/test-wav/20,20/full/full/0/none/default.wav" pattern="20,20"/></li>
                <li>VIDEO: <apidoc:link href="/api/media/a5-1/a5:demo-mp4-50mb/20,20/full/full/0/none/default.mp4" pattern="20,20"/></li>
            </ul>
        </td>
        <td>Extracts a time section from a media file. The examples seek to second <samp>20</samp> and extract a time section with <samp>20</samp> seconds of length.</td>
        <td><samp>offset[,length]</samp></td>
    </tr>
    </tbody>
</apidoc:desctable>

<h3>Region <small>{region}</small></h3>
<p><samp>GET|HEAD {scheme}://{server}/api/media/{repository}/[{prefix}/]{identifier}/{section}/<strong>{region}</strong>/{size}/{rotation}/{filter}/{quality}.{format}</samp></p>
<p>Extracts an image region from a media file.</p>
<apidoc:desctable>
    <tbody>
    <tr>
        <td>
            <ul class="list-unstyled">
                <li>IMAGE: <apidoc:link href="/api/media/a5-2/a5:/00-0000-0000-0000-0C8C-9/full/full/full/0/none/default.jpg"/></li>
            </ul>
        </td>
        <td>The complete image is returned without any cropping.</td>
        <td><samp>"full"</samp></td>
    </tr>
    <tr>
        <td>
            <ul class="list-unstyled">
                <li>IMAGE: <apidoc:link href="/api/media/a5-2/a5:/00-0000-0000-0000-0C8C-9/full/1150,1300,500,600/full/0/none/default.jpg" pattern="1150,1300,500,600"/></li>
                <li>VIDEO: <apidoc:link href="/api/media/a5-1/a5:demo-mp4-1mb/full/30,30,500,500/full/0/none/default.mp4" pattern="30,30,500,500"/></li>
            </ul>
        </td>
        <td>Offset <samp>x</samp>, offset <samp>y</samp>, width <samp>w</samp>, and height <samp>h</samp> of the region relative to the top-left of an image.</td>
        <td><samp>x,y,w,h</samp></td>
    </tr>
    </tbody>
</apidoc:desctable>

<h3>Size <small>{size}</small></h3>
<p><samp>GET|HEAD {scheme}://{server}/api/media/{repository}/[{prefix}/]{identifier}/{section}/{region}/<strong>{size}</strong>/{rotation}/{filter}/{quality}.{format}</samp></p>
<p>Scales an image to a specific size.</p>
<apidoc:desctable>
    <tbody>
    <tr>
        <td><apidoc:link href="/api/media/a5-2/a5:/00-0000-0000-0000-0C8C-9/full/full/full/0/none/default.jpg"/></td>
        <td>Returns a image file in its original size.</td>
        <td><samp>"full"</samp></td>
    </tr>
    <tr>
        <td><apidoc:link href="/api/media/a5-2/a5:/00-0000-0000-0000-0C8C-9/full/full/500,/0/none/default.jpg" pattern="/500,/"/></td>
        <td>The image is scaled so that its width is equal to <samp>w</samp>, and the height will be a calculated value that maintains the aspect ratio.</td>
        <td><samp>w,</samp></td>
    </tr>
    <tr>
        <td><apidoc:link href="/api/media/a5-2/a5:/00-0000-0000-0000-0C8C-9/full/full/,200/0/none/default.jpg" pattern="/,200/"/></td>
        <td>The image is scaled so that its height is equal to <samp>h</samp>, and the width will be a calculated value that maintains the aspect ratio.</td>
        <td><samp>,h</samp></td>
    </tr>
    <tr>
        <td><apidoc:link href="/api/media/a5-2/a5:/00-0000-0000-0000-0C8C-9/full/full/300,100/0/none/default.jpg" pattern="300,100"/></td>
        <td>The width and height of the returned image are equal to <samp>w</samp> and <samp>h</samp>. The aspect ratio of the
            returned image may be different resulting in a distorted image.</td>
        <td><samp>w,h</samp></td>
    </tr>
    </tbody>
</apidoc:desctable>

<h3>Rotation <small>{rotation}</small></h3>
<p><samp>GET|HEAD {scheme}://{server}/api/media/{repository}/[{prefix}/]{identifier}/{section}/{region}/{size}/<strong>{rotation}</strong>/{filter}/{quality}.{format}</samp></p>
<p>Rotates an image or video file.</p>
<apidoc:desctable>
    <tbody>
    <tr>
        <td>IMAGE:
            <ul class="list-unstyled">
                <li><apidoc:link href="/api/media/a5-2/a5:/00-0000-0000-0000-0C8C-9/full/full/full/0/none/default.jpg" pattern="/0/"/></li>
                <li><apidoc:link href="/api/media/a5-2/a5:/00-0000-0000-0000-0C8C-9/full/full/full/90/none/default.jpg" pattern="/90/"/></li>
            </ul>
            VIDEO:
            <ul class="list-unstyled">
                <li><apidoc:link href="/api/media/a5-1/a5:demo-mp4-1mb/full/full/full/180/none/default.mp4" pattern="/180/"/></li>
                <li><apidoc:link href="/api/media/a5-1/a5:demo-mp4-1mb/full/full/full/270/none/default.mp4" pattern="/270/"/></li>
            </ul>
        </td>
        <td>0, 90, 180, or 270 of clockwise rotation.</td>
        <td><samp>"0"|"90"|"180"|"270"</samp></td>
    </tr>
    </tbody>
</apidoc:desctable>

<h3>Filter <small>{filter}</small></h3>
<p>Applies filters to the input media file.</p>
<p><samp>GET|HEAD {scheme}://{server}/api/media/{repository}/[{prefix}/]{identifier}/{section}/{region}/{size}/{rotation}/<strong>{filter}</strong>/{quality}.{format}</samp></p>
<apidoc:desctable>
    <tbody>
    <tr>
        <td>IMAGE: <apidoc:link href="/api/media/a5-1/a5:/large-tif/full/full/full/0/none/default.jpg" pattern="/none/"/></td>
        <td>No filter is applied.</td>
        <td><samp>"none"</samp></td>
    </tr>
    <tr>
        <td>
            <ul class="list-unstyled">
                <li>IMAGE: <apidoc:link href="/api/media/a5-1/a5:/large-tif/full/full/full/0/gray/default.jpg" pattern="/gray/"/></li>
                <li>VIDEO: <apidoc:link href="/api/media/a5-1/a5:demo-mp4-1mb/full/full/full/0/gray/default.mp4" pattern="/gray/"/></li>
            </ul>
        </td>
        <td>Media file is returned in grayscale.</td>
        <td><samp>"gray"</samp></td>
    </tr>
    </tbody>
</apidoc:desctable>

<h3>Quality <small>{quality}</small></h3>
<p><samp>GET|HEAD {scheme}://{server}/api/media/{repository}/[{prefix}/]{identifier}/{section}/{region}/{size}/{rotation}/{filter}/<strong>{quality}</strong>.{format}</samp></p>
<p>Defines the compression rate / quality of the returned media file.</p>
<apidoc:desctable>
    <tbody>
    <tr>
        <td>
            <ul class="list-unstyled">
                <li>AUDIO (WAV to MP3): <apidoc:link href="/api/media/a5-1/a5:/test-wav/full/full/full/0/none/default.mp3" pattern="default"/></li>
                <li>IMAGE (TIFF to JPEG): <apidoc:link href="/api/media/a5-1/a5:/large-tif/full/full/full/0/none/default.jpg" pattern="default"/></li>
                <li>VIDEO: (MPEG to MP4) <apidoc:link href="/api/media/a5-1/a5:test-mov/full/full/full/0/none/default.mp4" pattern="default"/></li>
            </ul>
        </td>
        <td><samp>"default"</samp> does not change the quality / compression rate of the media file and returns it as it is. <br>In combination
        with {format} conversion, a default compression rate is choosen.</td>
        <td><samp>"default"</samp></td>
    </tr>
    <tr>
        <td>
            <ul class="list-unstyled">
                <li>AUDIO (WAV to MP3): <apidoc:link href="/api/media/a5-1/a5:/test-wav/full/full/full/0/none/high.mp3" pattern="high"/></li>
                <li>IMAGE (TIFF to JPEG): <apidoc:link href="/api/media/a5-1/a5:/large-tif/full/full/full/0/none/high.jpg" pattern="high"/></li>
                <li>VIDEO: (MPEG to MP4) <apidoc:link href="/api/media/a5-1/a5:test-mov/full/full/full/0/none/high.mp4" pattern="high"/></li>
            </ul>
        </td>
        <td><samp>"high"</samp> returns the media file in the highest quality possible. <br>{quality} is useful in combination with format
        conversion, for example, if the file on the file system is a TIFF file and you want to return it as a high
        quality JPEG.</td>
        <td><samp>"high"</samp></td>
    </tr>
    <tr>
        <td>
            <ul class="list-unstyled">
                <li>AUDIO (WAV to MP3): <apidoc:link href="/api/media/a5-1/a5:/test-wav/full/full/full/0/none/medium.mp3" pattern="medium"/></li>
                <li>IMAGE (TIFF to JPEG): <apidoc:link href="/api/media/a5-1/a5:/large-tif/full/full/full/0/none/medium.jpg" pattern="medium"/></li>
                <li>VIDEO: (MPEG to MP4) <apidoc:link href="/api/media/a5-1/a5:test-mov/full/full/full/0/none/medium.mp4" pattern="medium"/></li>
            </ul>
        </td>
        <td><samp>"medium"</samp> returns the media file in medium quality with reduced size.</td>
        <td><samp>"medium"</samp></td>
    </tr>
    <tr>
        <td>
            <ul class="list-unstyled">
                <li>AUDIO (WAV to MP3): <apidoc:link href="/api/media/a5-1/a5:/test-wav/full/full/full/0/none/low.mp3" pattern="low"/></li>
                <li>IMAGE (TIFF to JPEG): <apidoc:link href="/api/media/a5-1/a5:/large-tif/full/full/full/0/none/low.jpg" pattern="low"/></li>
                <li>VIDEO: (MOV to MP4) <apidoc:link href="/api/media/a5-1/a5:test-mov/full/full/full/0/none/low.mp4" pattern="low"/></li>
            </ul>
        </td>
        <td><samp>"low"</samp> reduces the size of the media file as much as possible, probably leading to low quality.</td>
        <td><samp>"low"</samp></td>
    </tr>
    </tbody>
</apidoc:desctable>
<p>For PDF, Text, and XML as well as for image, audio and video formats that do not support compression or different compression rates, <samp>{quality}</samp> has no effect.</p>

<h3>Format <small>{format}</small></h3>
<p><samp>GET|HEAD {scheme}://{server}/api/media/{repository}/[{prefix}/]{identifier}/{section}/{region}/{size}/{rotation}/{filter}/{quality}.<strong>{format}</strong></samp></p>
<p>Defines the format of the returned media file.</p>
<h5>Supported formats</h5>
<ul>
    <li><samp>"bmp"|"gif"|"jpg"|"png"|"tif"|"webp"</samp> (image)</li>
    <li><samp>"aiff"|"mp3"|"wav"|"webm"</samp> (audio)</li>
    <li><samp>"mp4"|"flv"|"mov"|"webm"</samp> (video)</li>
</ul>
<apidoc:desctable>
    <tbody>
    <tr>
        <td>
            <ul class="list-unstyled">
                <li>WAV to MP3: <apidoc:link href="/api/media/a5-1/a5:/test-wav/full/full/full/0/none/default.mp3 " pattern="mp3"/></li>
                <li>WAV to WEBM: <apidoc:link href="/api/media/a5-1/a5:/test-wav/full/full/full/0/none/default.webm " pattern="webm"/></li>
            </ul>
        </td>
        <td>Output format expressed as an extension at the end of the URI.</td>
    </tr>
    <tr>
        <td>
            <ul class="list-unstyled">
                <li>TIFF to BMP: <apidoc:link href="/api/media/a5-1/a5:/large-tif/full/full/full/0/none/default.bmp " pattern="bmp"/></li>
                <li>TIFF to GIF: <apidoc:link href="/api/media/a5-1/a5:/large-tif/full/full/full/0/none/default.gif " pattern="gif"/></li>
                <li>TIFF to JPG: <apidoc:link href="/api/media/a5-1/a5:/large-tif/full/full/full/0/none/default.jpg " pattern="jpg"/></li>
                <li>TIFF to PNG: <apidoc:link href="/api/media/a5-1/a5:/large-tif/full/full/full/0/none/default.png " pattern="png"/></li>
                <li>TIFF to WEBP: <apidoc:link href="/api/media/a5-1/a5:/large-tif/full/full/full/0/none/default.webp " pattern="webp"/></li>
            </ul>
        </td>
        <td>Output format expressed as an extension at the end of the URI.</td>
    </tr>
    <tr>
        <td>
            <ul class="list-unstyled">
                <li>MOV to MP4: <apidoc:link href="/api/media/a5-1/a5:test-mov/full/full/full/0/none/default.mp4 " pattern="mp4"/></li>
                <li>MOV to WEBM: <apidoc:link href="/api/media/a5-1/a5:test-mov/full/full/full/0/none/default.webm " pattern="webm"/></li>
            </ul>
        </td>
        <td>Output format expressed as an extension at the end of the URI.</td>
    </tr>
    </tbody>
</apidoc:desctable>
<p>For PDF and Text, <samp>{format}</samp> has no effect.</p>

<h2>Cross Media Request</h2>
<p><samp>GET|HEAD {scheme}://{server}/api/media/{repository}/[{prefix}/]{identifier}/{section}/{region}/{size}/{rotation}/{filter}/{quality}.{format}</samp></p>
<p>Streams a media file in a representation different from its original media type.</p>
<%--
<apidoc:errtable>
    <tbody>
        <tr>
            <apidoc:errtablerow heading="Common"/>
        </tr>
        <tr>
            <apidoc:errtablerow err="${err.a50006}" arg="GET"/>
        </tr>
        <tr>
            <apidoc:errtablerow err="${err.a50016}"/>
        </tr>
        <tr>
            <apidoc:errtablerow heading="Properties"/>
        </tr>
        <tr>
            <apidoc:errtablerow err="${err.a50002}" arg="{repository}"/>
        </tr>
        <tr>
            <apidoc:errtablerow err="${err.a50003}" arg="[{prefix}/]{identifier}"/>
        </tr>
        <tr>
            <apidoc:errtablerow err="${err.a50008}" arg="{section}"/>
        </tr>
        <tr>
            <apidoc:errtablerow err="${err.a50009}" arg="{region}"/>
        </tr>
        <tr>
            <apidoc:errtablerow err="${err.a50010}" arg="{size}"/>
        </tr>
        <tr>
            <apidoc:errtablerow err="${err.a50011}" arg="{rotation}"/>
        </tr>
        <tr>
            <apidoc:errtablerow err="${err.a50012}" arg="{filter}"/>
        </tr>
        <tr>
            <apidoc:errtablerow err="${err.a50007}" arg="{quality}"/>
        </tr>
        <tr>
            <apidoc:errtablerow err="${err.a50001}" arg="{format}"/>
        </tr>
    </tbody>
</apidoc:errtable>
--%>

<h3>Video to Audio</h3>
<p><samp>GET|HEAD {scheme}://{server}/api/media/{repository}/[{prefix}/]{identifier}/{section}/{region}/{size}/{rotation}/{filter}/{quality}.{format}</samp></p>
<p>Extracts the audio channels from a video file.</p>
<apidoc:desctable>
    <tbody>
    <tr>
        <td>
            <apidoc:link href="/api/media/a5-1/a5:demo-mp4-50mb/full/full/full/180/none/default.mp3 " pattern="mp3"/>
        </td>
        <td>Example returns the audio channels in format "<samp>mp3</samp>" from a MP4 video file.</td>
        <td><samp>"(aiff|mp3|wav|webm)"</samp></td>
    </tr>
    <tr>
        <td>
            <apidoc:link href="/api/media/a5-1/a5:demo-mp4-1mb/full/full/full/180/none/default.wav " pattern="wav"/>
        </td>
        <td>Example returns the audio channels in format "<samp>wav</samp>" from a MP4 video file.</td>
        <td><samp>"(aiff|mp3|wav|webm)"</samp></td>
    </tr>
    </tbody>
</apidoc:desctable>

<h3>Audio to Image</h3>
<p><samp>GET|HEAD {scheme}://{server}/api/media/{repository}/[{prefix}/]{identifier}/{section}/{region}/{size}/{rotation}/<strong>spectrum</strong>/{quality}.{format}</samp></p>
<p><samp>GET|HEAD {scheme}://{server}/api/media/{repository}/[{prefix}/]{identifier}/{section}/{region}/{size}/{rotation}/<strong>waveform</strong>/{quality}.{format}</samp></p>
<p>Returns waveform and spectrum images.</p>
<apidoc:desctable>
    <tbody>
    <tr>
        <td>
            <ul class="list-unstyled">
                <li>AUDIO: <apidoc:link href="/api/media/a5-1/a5:/test-wav/full/full/12000,150/0/spectrum/default.png" pattern="spectrum"/></li>
                <li>VIDEO: <apidoc:link href="/api/media/a5-1/a5:demo-mp4-1mb/full/full/12000,150/0/spectrum/default.png" pattern="spectrum"/></li>
            </ul>
        </td>
        <td>Examples return a spectrum image, size "<samp>12000x150px</samp>", in format "<samp>png</samp>".</td>
    </tr>
    <tr>
        <td>
            <ul class="list-unstyled">
                <li>AUDIO: <apidoc:link href="/api/media/a5-1/a5:/test-wav/full/full/12000,150/0/waveform/default.png" pattern="waveform"/></li>
                <li>VIDEO: <apidoc:link href="/api/media/a5-1/a5:demo-mp4-1mb/full/full/12000,150/0/waveform/default.png" pattern="waveform"/></li>
            </ul>
        </td>
        <td>Examples return a waveform image, size "<samp>12000x150px</samp>", in format "<samp>png</samp>".</td>
    </tr>
    </tbody>
</apidoc:desctable>

<h3>Video to Image</h3>
<p><samp>GET|HEAD {scheme}://{server}/api/media/{repository}/[{prefix}/]{identifier}/{section}/{region}/{size}/{rotation}/<strong>thumbnail</strong>/{quality}.{format}</samp></p>
<p>Extracts a thumbnail image from a video at a specific position.</p>
<apidoc:desctable>
    <tbody>
    <tr>
        <td><apidoc:link href="/api/media/a5-1/a5:demo-mp4-1mb/3,/full/500,/0/thumbnail/default.jpg" pattern="thumbnail"/></td>
        <td>Returns a thumbnail image at time position "<samp>3sec</samp>".</td>
    </tr>
    </tbody>
</apidoc:desctable>
</body>
</html>