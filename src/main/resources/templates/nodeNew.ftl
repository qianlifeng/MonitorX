<#import "layout.ftl" as layout />

<#assign headerContent>
<link rel="stylesheet" href="/css/nodeNew.css">
</#assign>

<@layout.masterTemplate title="Detail" header=headerContent initScript="js/nodeNew">
<div class="detail">
    <div class="status-weight" v-bind:class="{ 'status-up': node.status.status == 'up', 'status-down': node.status.status == 'down' }">
        <div class="row">
            <div class="col-sm-6">
                <span class="title" v-text="node.title"></span>
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