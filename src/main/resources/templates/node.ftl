<#import "layout.ftl" as layout />

<#assign headerContent>
<link rel="stylesheet" href="/css/node.css">
<link rel="stylesheet" href="/lib/odometer/odometer-theme-default.css">
</#assign>

<@layout.masterTemplate title="Detail" header=headerContent initScript="js/node">
<div class="detail" v-cloak>
    <div class="status-widget" v-bind:class="{ 'status-up': isNodeUp(),'status-down': !isNodeUp() }">
        <div class="row">
            <div class="col-sm-9">
                <span class="title" v-text="node.title"></span>
            </div>
            <div class="col-sm-3">
                <i class="fa fa-trash-o clickable pull-right" v-on:click="removeNode()" data-toggle="tooltip" data-placement="top" title="Remove node"></i>
                <i class="fa fa-plus clickable pull-right" style="margin-right:10px;" data-toggle="tooltip" data-placement="top" title="Add forewarning" v-on:click="addNodeForewarning()"></i>
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
                        <div class="col-sm-4">
                            <h3 class="panel-title" v-html="metric.title"></h3>
                        </div>
                        <div class="col-sm-8">
                            <div class="pull-right">
                                <i class="fa fa-plus clickable" title="Add forewarning" v-on:click="addForewarning(metric)"></i>
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
</@layout.masterTemplate>