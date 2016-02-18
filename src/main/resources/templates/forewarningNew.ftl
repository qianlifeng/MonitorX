<#import "layout.ftl" as layout />

<#assign headerContent>
<link rel="stylesheet" href="/css/forewarningNew.css">
</#assign>

<@layout.masterTemplate title="Add Forewarning" header=headerContent initScript="js/forewarningNew">
<div class="content-container">
    <div class="panel panel-default">
        <div class="panel-heading">
            <h3 class="panel-title">Add forewarning</h3>
        </div>
        <div class="panel-body">
            <form class="form-horizontal">
                <div class="form-group">
                    <label class="col-sm-2 control-label">Target</label>

                    <div class="col-sm-10">
                        <input type="text" class="form-control" v-model="notifier.title">
                    </div>
                </div>
                <div class="form-group">
                    <label class="col-sm-2 control-label">Snippet</label>

                    <div class="col-sm-10">
                        <input type="text" class="form-control" v-model="notifier.title">
                    </div>
                </div>
                <component :is="'notifierConfig-' + notifier.type" :config="notifier.config"></component>
                <div class="form-group">
                    <div class="col-sm-offset-2 col-sm-10">
                        <button type="button" v-on:click="addNotifier" class="btn btn-success">Add</button>
                    </div>
                </div>
            </form>
        </div>
    </div>
</div>
</@layout.masterTemplate>