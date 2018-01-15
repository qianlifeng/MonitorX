<template>
    <div id="app">
        <nav class="navbar navbar-default navbar-fixed-top">
            <div class="container">
                <div class="navbar-header">
                    <button type="button" class="navbar-toggle collapsed" data-toggle="collapse" data-target="#monitorx-navbar-collapse" aria-expanded="false"
                        aria-controls="navbar">
                        <span class="sr-only">Toggle navigation</span>
                        <span class="icon-bar"></span>
                        <span class="icon-bar"></span>
                        <span class="icon-bar"></span>
                        </button>
                        <router-link class="navbar-brand" to="/">FCGYL-监控预警系统</router-link>
                </div>
                <div class="collapse navbar-collapse" id="monitorx-navbar-collapse">
                    <ul class="nav navbar-nav">
                        <li>
                            <router-link to="/notifier">Notifier</router-link>
                        </li>
                    </ul>
                </div>
            </div>
        </nav>
        <div class="container">
            <router-view></router-view>
        </div>
    </div>
</template>

<script>
    import Boostrap from 'bootstrap-webpack'
    import FontAwesome from 'font-awesome-webpack'

    export default {
        name: 'app',
        mounted() {
            setTimeout(function () {
                $('[data-toggle="tooltip"]').tooltip({ container: 'body' });
            }, 1000);
        }
    }

</script>

<style>
    body {
        padding-top: 50px;
        background-color: #f3f3f4 !important;
    }
    
    .footer {
        position: absolute;
        bottom: 0;
        width: 100%;
        height: 60px;
        background-color: #eee;
    }
    
    .container .text-muted {
        margin: 20px 0;
    }
    
    .text-muted {
        color: #777;
    }
    
    .content-container {
        margin-top: 20px;
    }
    
    .metric-widget {
        width: 100%;
        height: 270px;
    }
    
    .panel-body {
        min-height: 300px;
        position: relative;
    }
    
    .status-widget {
        border-radius: 5px;
        padding: 15px 20px;
        margin-bottom: 10px;
        margin-top: 10px;
    }
    
    .status-widget .lastupdate {
        float: right;
    }
    
    .status-widget .title {
        font-size: 30px;
    }
    
    .status-up {
        background-color: #1ab394;
        color: #ffffff;
    }
    
    .status-down {
        background-color: #ed5565;
        color: #ffffff;
    }
    
    .status-green {
        background-color: #23c6c8;
        color: #ffffff;
    }
    
    .status-blue {
        background-color: #1c84c6;
        color: #ffffff;
    }
    
    .status-yellow {
        background-color: #f8ac59;
        color: #ffffff;
    }
    
    .clickable {
        cursor: pointer;
    }
    
    .add-new {
        padding-top: 30px;
    }
    
    .add-new .fa {
        font-size: 1.5em;
    }
    
    .add-new a,
    .add-new a:visited {
        color: #777;
    }
    
    .add-new a:hover {
        color: #777;
    }
    
    @media (max-width: 767px) {
        .add-new {
            display: none !important;
        }
    }
</style>