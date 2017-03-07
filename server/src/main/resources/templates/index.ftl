<#import "layout.ftl" as layout />

<@layout.masterTemplate initScript="js/index">
<div class="index">
    <div class="row">
        <div class="col-sm-9">
            <h1 class="title">Nodes</h1>
        </div>
        <div class="col-sm-3 add-new">
            <a href="/node/new/" class="dropdown-toggle pull-right" role="button" data-toggle="tooltip" data-placement="top" title="Add node"><span class="fa fa-plus"></span></a>
        </div>
    </div>

    <div class="row" v-cloak>
        <div class="v-cloak-loading">
            <img src="/image/loading.gif">
        </div>
        <div class="v-cloak-content">
            <div class="col-sm-12 col-lg-6 col-md-6" v-for="node in nodes">
                <div class="status-widget clickable" v-on:click="navigate(node.code)" v-bind:class="{ 'status-up': node.status.status == 'up','status-down': node.status.status != 'up' }">
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
            </div>
        </div>
    </div>
</div>
</@layout.masterTemplate>