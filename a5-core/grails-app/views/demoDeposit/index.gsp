<!DOCTYPE html>
<html>
	<head>
		<meta name="layout" content="a5">
		<title>Deposit Demo</title>
	</head>
	<body>
        <h1>Deposit Demo</h1>
        <div id="deposit-mediaclient" class="collapse">
            <div class="row text-center"><span class="mediaclient-label">BAN_AM_NS_20121203_bawangkesuna.mp4</span></div>
            <div class="row text-right"><button class="mediaclient-closefile btn btn-sm">Close</button></div>
            <div class="row embed-responsive embed-responsive-16by9">
                <iframe style="width:100%;" src=""></iframe>
            </div>
        </div>
        <div id="deposit-fileclient">
            <div id="deposit-fileupload">
                <div class="row">
                    <div class="col-md-2">
                        <label class="btn btn-default btn-block">
                            <span>Upload file...</span>
                            <input class="deposit-upload-input" type="file" name="files[]">
                        </label>
                    </div>
                    <div class="col-md-10">
                        <div class="progress" style="height: 3em;">
                            <div class="progress-bar">0%</div>
                        </div>
                    </div>
                </div>
                <div class="container">
                    <span class="deposit-fileupload-response"></span>
                </div>
            </div>
            <div id="myfiles" class="container-fluid">
                <div id="fileactions" class="text-right">
                    <button class="btn btn-sm delete" disabled>Delete</button>
                </div>
                <table id="deposit-filetable" class="table table-bordered">
                    <caption>My Files</caption>
                    <thead>
                        <tr>
                            <th></th>
                            <th>Name</th>
                            <th>Size</th>
                            <th>Last update</th>
                            <th></th>
                        </tr>
                    </thead>
                    <tbody>
                        <g:each var="file" in="${fileList?.files}">
                            <tr>
                                <td>${file.path}</td>
                                <td class="demo-fileshow">${file.name}</td>
                                <td>${file.sizeFormatted}</td>
                                <td>${file.lastModified}</td>
                                <td><input class="demo-fileselect filerow-checkbox" type="checkbox"></td>
                            </tr>
                        </g:each>
                    </tbody>
                </table>
            </div>
            <div class="text-info container">
                <h4>Settings</h4>
                <ul class="list-unstyled">
                    <li>Maximum User Space: ${maxUserSpace}</li>
                    <li>Maximum File Size: ${maxFileSize}</li>
                </ul>
            </div>
        </div>
        <script type="text/javascript">
            $(document).ready(function() {
                $(document).demodeposit("${request.contextPath}");
            });
        </script>
	</body>
</html>