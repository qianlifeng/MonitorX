<#import "layout.ftl" as layout />

<#assign headerContent>
<link rel="stylesheet" href="/css/notifier.css">
</#assign>

<@layout.masterTemplate title="Notifiers" header=headerContent initScript="js/notifier">
<div class="notifier">
    <div class="row">
        <div class="col-sm-9">
            <h1 class="title">Notifiers</h1>
        </div>
        <div class="col-sm-3 add-new">
            <a href="/notifier/new/" class="dropdown-toggle pull-right" role="button" data-toggle="tooltip" data-placement="bottom" title="Add notifier"><span class="fa fa-plus"></span></a>
        </div>
    </div>

    <div class="row">
        <div class="col-sm-12 col-lg-12 col-md-12" v-for="notifier in notifiers">
            <component :is="'notifier-' + notifier.type" :notifier="notifier"></component>
        </div>
    </div>
</div>
</@layout.masterTemplate>