<#import "layout.ftl" as layout />

<#assign headerContent>
<link rel="stylesheet" href="/css/notifierNew.css">
</#assign>

<@layout.masterTemplate header=headerContent initScript="js/notifierNew">
<div class="content-container">
    <div class="panel panel-default">
        <div class="panel-heading">
            <h3 class="panel-title">Add Notifier</h3>
        </div>
        <div class="panel-body">
            <form class="form-horizontal">
                <div class="form-group">
                    <label class="col-sm-2 control-label">Title</label>

                    <div class="col-sm-10">
                        <input type="text" class="form-control" v-model="notifier.title">
                    </div>
                </div>
                <div class="form-group">
                    <label class="col-sm-2 control-label">Notify Type</label>

                    <div class="col-sm-10">
                        <select class="form-control" v-model="notifier.type">
                            <option value="wechat">Wechat</option>
                            <option value="email">Email</option>
                            <option value="callback">Callback</option>
                        </select>
                    </div>
                </div>
                <component :is="'notifierConfig-' + notifier.type" :config="notifier.config"></component>
                <div class="form-group">
                    <div class="col-sm-offset-2 col-sm-10">
                        <button type="button" v-on:click="addNotifier" class="btn btn-success">Add</button>
                        <button type="button" data-toggle="modal" data-backdrop="static" data-target="#testSendModal" class="btn btn-default">Test</button>
                    </div>
                </div>
            </form>
        </div>
    </div>

    <div class="modal fade" id="testSendModal">
        <div class="modal-dialog">
            <div class="modal-content">
                <div class="modal-header">
                    <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
                    <h4 class="modal-title">Send Notification</h4>
                </div>
                <div class="modal-body">
                    <form class="form-horizontal">
                        <div class="form-group">
                            <label class="col-sm-2 control-label">Title</label>

                            <div class="col-sm-10">
                                <input type="text" class="form-control" v-model="msg.title" placeholder="Max length: 256">
                            </div>
                        </div>
                        <div class="form-group">
                            <label class="col-sm-2 control-label">Message</label>

                            <div class="col-sm-10">
                                <textarea class="form-control" rows="3" v-model="msg.body"></textarea>
                            </div>
                        </div>
                    </form>
                </div>
                <div class="modal-footer">
                    <button type="button" class="btn btn-default" data-dismiss="modal">Cancel</button>
                    <button type="button" v-on:click="send()" class="btn btn-primary">Send</button>
                </div>
            </div>
        </div>
    </div>

</div>
</@layout.masterTemplate>