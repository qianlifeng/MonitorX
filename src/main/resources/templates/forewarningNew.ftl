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
        <div class="panel-body" v-cloak>
            <form class="form-horizontal">
                <div class="form-group">
                    <label class="col-sm-2 control-label">Name</label>

                    <div class="col-sm-10">
                        <input type="text" class="form-control" v-model="title">
                    </div>
                </div>
                <div class="form-group">
                    <label class="col-sm-2 control-label">Node</label>

                    <div class="col-sm-10">
                        <p class="form-control-static">{{node}}</p>
                    </div>
                </div>
                <div class="form-group" v-if="metric != ''">
                    <label class="col-sm-2 control-label">Metric</label>

                    <div class="col-sm-10">
                        <p class="form-control-static">{{metric}}</p>
                    </div>
                </div>
                <div class="form-group">
                    <label class="col-sm-2 control-label">Snippet Context</label>

                    <div class="col-sm-10">
                        <p class="form-control-static">${context}</p>
                    </div>
                </div>
                <div class="form-group">
                    <label class="col-sm-2 control-label">Snippet</label>

                    <div class="col-sm-10">
                        <textarea class="form-control" rows="5" v-model="snippet" placeholder="Javascript Snippet"></textarea>
                    </div>
                </div>
                <div class="form-group">
                    <div class="col-sm-1 col-sm-offset-2">
                        <button type="button" v-on:click="evaluate()" class="btn btn-default">Evaluate</button>
                    </div>
                    <div class="col-sm-9">
                        <p class="form-control-static" v-html="evaluateResult"></p>
                    </div>
                </div>
                <div class="form-group">
                    <label class="col-sm-2 control-label">Notifiers</label>

                    <div class="col-sm-10">
                        <select multiple class="form-control" v-model="notifiers">
                            <option value="{{notifier.id}}" v-for="notifier in availableNotifiers">{{notifier.title}}</option>
                        </select>
                    </div>
                </div>
                <div class="form-group">
                    <label class="col-sm-2 control-label">When notify</label>

                    <div class="col-sm-10">
                        <select class="form-control" v-model="firerule">
                            <option selected value="firerule-immediately">Immediately</option>
                            <option value="firerule-continuallyDown">Continually Down</option>
                            <option value="firerule-oncePerDay">Once per day</option>
                        </select>
                    </div>
                </div>
                <div class="form-group">
                    <label class="col-sm-2 control-label">Notify message</label>

                    <div class="col-sm-10">
                        <textarea class="form-control" rows="5" v-model="msg" placeholder="Notify message support snippet context, please use {{expression}} to eval the real value"></textarea>
                    </div>
                </div>
                <div class="form-group">
                    <div class="col-sm-1 col-sm-offset-2">
                        <button type="button" v-on:click="previewMsg()" class="btn btn-default">Preview</button>
                    </div>
                    <div class="col-sm-9">
                        <p class="form-control-static" v-html="realMsg">ss</p>
                    </div>
                </div>
                <div class="form-group">
                    <div class="col-sm-offset-2 col-sm-10">
                        <button type="button" v-on:click="addForewarning" class="btn btn-success">Add</button>
                        <button type="button" v-on:click="removeForewarning" class="btn btn-danger" v-if="edit">Remove</button>
                    </div>
                </div>
            </form>
        </div>
    </div>
</div>
</@layout.masterTemplate>