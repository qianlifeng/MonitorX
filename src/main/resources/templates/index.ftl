<#import "layout.ftl" as layout />

<@layout.masterTemplate title="Home" initScript="js/index">
<div class="index">
    <div class="row">
        <div class="col-sm-9">
            <h1 class="title">Nodes</h1>
        </div>
        <div class="col-sm-3 add-new">
            <a href="/n/new/" class="dropdown-toggle pull-right" role="button" data-toggle="tooltip" data-placement="bottom" title="Add node"><span class="fa fa-plus"></span></a>
        </div>
    </div>

    <div class="row">
        <div class="col-sm-12 col-lg-6 col-md-6" v-for="node in nodes">
            <div class="status-widget clickable" v-on:click="navigate(node.code)" v-bind:class="{ 'status-up': isNodeUp(node),'status-down': !isNodeUp(node) }">
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
</@layout.masterTemplate>