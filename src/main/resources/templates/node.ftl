<#import "layout.ftl" as layout />

<#assign headerContent>
<link rel="stylesheet" href="/css/node.css">
<link rel="stylesheet" href="/lib/odometer/odometer-theme-default.css">
</#assign>

<@layout.masterTemplate title="Detail" header=headerContent initScript="js/node">
<div class="detail" v-cloak>
    <div class="v-cloak-loading">
        <img src="/image/loading.gif">
    </div>
    <div class="v-cloak-content">
        <div class="status-widget" v-bind:class="{ 'status-up': node.status.status == 'up','status-down': node.status.status != 'up' }">
            <div class="row">
                <div class="col-sm-6">
                    <span class="title" v-text="node.title"></span>
                </div>
                <div class="col-sm-6 operations">
                    <i class="fa fa-trash-o clickable pull-right" v-on:click="removeNode()" data-toggle="tooltip" data-placement="top" title="Remove node"></i>
                    <i class="fa fa-plus clickable pull-right forewarning-icon" data-toggle="tooltip" data-placement="top" title="Add forewarning" v-on:click="addNodeForewarning()"></i>
                    <i class="fa forewarning-icon clickable pull-right" v-on:click="editNodeForewarning(nodeNotifier.forewarningId)" data-toggle="tooltip" data-placement="top" title="When {{nodeNotifier.forewarningTitle}}, notify {{nodeNotifier.title}}" v-bind:class="{'fa-link':nodeNotifier.type == 'callback','fa-wechat':nodeNotifier.type == 'wechat','fa-envelope-o':nodeNotifier.type=='email'}" v-for="nodeNotifier in getNodeNotifers()"></i>
                    <i></i>
                </div>
            </div>
            <div class="lastupdate">
                <i class="fa fa-clock-o"></i> <span v-text="node.status.formattedLastUpdateDate"></span>
            </div>
            <div class="clearfix"></div>
        </div>
        <div class="row">
            <div class="col-sm-12 col-lg-6 col-md-6" v-for="metric in node.status.metrics">
                <div class="panel panel-default">
                    <div class="panel-heading">
                        <div class="row">
                            <div class="col-sm-7">
                                <h3 class="panel-title" v-html="metric.title"></h3>
                            </div>
                            <div class="col-sm-5 operations">
                                <div class="pull-right">
                                    <i class="fa forewarning-icon clickable" v-on:click="editMetricForewarning(metric.title,notifier.forewarningId)" data-toggle="tooltip" data-placement="top" title="When {{notifier.forewarningTitle}}, notify {{notifier.title}}" v-bind:class="{'fa-link':notifier.type == 'callback','fa-wechat':notifier.type == 'wechat','fa-envelope-o':notifier.type=='email'}" v-for="notifier in getMetricNotifers(metric)"></i>
                                    <i class="fa fa-plus clickable" title="Add metric forewarning" data-toggle="tooltip" data-placement="top" v-on:click="addForewarning(metric)"></i>
                                </div>
                            </div>
                        </div>
                    </div>
                    <div class="panel-body">
                        <component :is="metric.type" :value="metric.value"></component>
                    </div>
                </div>
            </div>
        </div>
    </div>
</div>
</@layout.masterTemplate>