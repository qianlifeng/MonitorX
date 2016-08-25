<#import "layout.ftl" as layout />

<#assign headerContent>
<link rel="stylesheet" href="/css/nodeNew.css">
</#assign>

<@layout.masterTemplate header=headerContent initScript="js/nodeNew">
<div class="content-container">
    <div class="panel panel-default">
        <div class="panel-heading">
            <h3 class="panel-title">Add Node</h3>
        </div>
        <div class="panel-body">
            <form class="form-horizontal">
                <div class="form-group">
                    <label class="col-sm-2 control-label">Code</label>

                    <div class="col-sm-10">
                        <input type="text" class="form-control" v-model="node.code">
                    </div>
                </div>
                <div class="form-group">
                    <label class="col-sm-2 control-label">Title</label>

                    <div class="col-sm-10">
                        <input type="text" class="form-control" v-model="node.title">
                    </div>
                </div>
                <div class="form-group">
                    <label class="col-sm-2 control-label">Sync Type</label>

                    <div class="col-sm-10">
                        <select class="form-control" v-model="node.syncType">
                            <option value="pull">Pull</option>
                            <option value="push">Push</option>
                        </select>
                    </div>
                </div>
                <component :is="'sync-' + node.syncType" :node="node"></component>
                <div class="form-group">
                    <div class="col-sm-offset-2 col-sm-10">
                        <button type="button" v-on:click="addNode" class="btn btn-success">Add</button>
                    </div>
                </div>
            </form>
        </div>
    </div>
</div>
</@layout.masterTemplate>