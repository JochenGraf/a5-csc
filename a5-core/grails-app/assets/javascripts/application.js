// This is a manifest file that'll be compiled into application.js.
//
// Any JavaScript file within this directory can be referenced here using a relative path.
//
// You're free to add application-wide JavaScript to this file, but it's generally better
// to create separate JavaScript files as needed.
//
//= require jquery-2.2.0.min
//= require bootstrap
//= require jquery.toc
//= require jqueryfileupload/vendor/jquery.ui.widget
//= require jqueryfileupload/jquery.iframe-transport
//= require jqueryfileupload/jquery.fileupload
//= require demo/demodeposit
//= require demo/demoelan

if (typeof jQuery !== 'undefined') {
    (function($) {
        $('#spinner').ajaxStart(function() {
            $(this).fadeIn();
        }).ajaxStop(function() {
            $(this).fadeOut();
        });
    })(jQuery);
}

$(document).ready(function() {
    $("#toc").toc({
        headings: "h2, h3, h4"
    });
});
