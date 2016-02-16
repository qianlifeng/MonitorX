<#import "layout.ftl" as layout />

<@layout.masterTemplate title="Home" initScript="js/index">
<div class="index">
    <h1>Nodes</h1>

    <div class="row">
        <div class="col-sm-12 col-lg-6 col-md-6" v-for="node in nodes">
            <div class="status-widget clickable" v-on:click="navigate(node.code)" v-bind:class="{ 'status-up': node.status.status == 'up', 'status-down': node.status.status == 'down' }">
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