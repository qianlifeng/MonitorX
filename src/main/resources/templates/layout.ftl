<#macro masterTemplate title="" header="" footer="" initScript="">
<!DOCTYPE html>
<html>
<head>
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <title>${title}</title>
    <meta content="width=device-width, initial-scale=1, maximum-scale=1, user-scalable=no" name="viewport">

    <link rel="stylesheet" href="/css/layout.css">
    <link rel="stylesheet" href="/lib/bootstrap/css/bootstrap.min.css">
    <link rel="stylesheet" href="/lib/font-awesome/css/font-awesome.min.css">

    <script type="text/javascript">
        var require = {
            baseUrl: '/',
            paths: {
                lib: "lib",
                js: "js",
                jquery: "lib/jquery/jQuery-2.1.4.min",
                bootstrap: "lib/bootstrap/js/bootstrap.min",
                vue: "lib/vuejs/vue",
                echart: "lib/echart/echarts.min",
            },
            deps: [
                "bootstrap"
            ],
            shim: {
                bootstrap: {
                    deps: ["jquery", "js/layout"]
                }
            }
        };
    </script>
    <script data-main="${initScript}" src="/lib/requirejs/requirejs-2.1.20.js"></script>
${header}
</head>
<body>
<nav class="navbar navbar-inverse navbar-fixed-top">
    <div class="container">
        <div class="navbar-header">
            <button type="button" class="navbar-toggle collapsed" data-toggle="collapse" data-target="#navbar" aria-expanded="false" aria-controls="navbar">
                <span class="sr-only">Toggle navigation</span>
                <span class="icon-bar"></span>
                <span class="icon-bar"></span>
                <span class="icon-bar"></span>
            </button>
            <a class="navbar-brand" href="/">MonitorX</a>
        </div>
        <div id="navbar" class="collapse navbar-collapse">
            <ul class="nav navbar-nav">
                <li class="dropdown">
                    <a href="#" role="button" aria-haspopup="true" aria-expanded="false">
                        Config
                    </a>
                </li>
            </ul>
        </div><!--/.nav-collapse -->
    </div>
</nav>
<div class="container">
    <#nested />
</div>

${footer}
</body>
</html>
</#macro>