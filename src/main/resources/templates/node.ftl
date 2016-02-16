<#import "layout.ftl" as layout />

<@layout.masterTemplate title="Detail" initScript="js/node">
<div class="detail">
    <h1><span v-text="node.title"></span></h1>
    <div>
        <div class="panel panel-default" v-for="metric in node.status.metrics">
            <div class="panel-heading">
                <h3 class="panel-title">{{metric.title}}</h3>
            </div>
            <div class="panel-body">
                <component :is="metric.type" :value="metric.value" keep-alive></component>
            </div>
        </div>
    </div>
</div>
</@layout.masterTemplate>