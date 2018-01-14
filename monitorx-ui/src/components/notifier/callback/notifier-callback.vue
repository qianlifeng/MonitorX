<template>
    <div class="status-widget status-blue">
        <div class="row">
            <div class="col-sm-9">
                <i class="fa fa-external-link fa-3x"></i><span class="notifier-title">{{notifier.title}}</span>
            </div>
            <div class="col-sm-3 operations">
                <i class="fa fa-trash-o clickable pull-right" @click="removeNotifier()" data-toggle="tooltip" data-placement="top" title="Remove notifier"></i>
            </div>
        </div>
        <div class="row">
            <div class="col-sm-12">
                <div class="key pull-right">
                    <i class="fa fa-link"></i> <span>{{notifier.config.callbackUrl}}</span>
                </div>
            </div>
        </div>
    </div>
</template>

<script>
    import notifierMixin from '../notifier-mixin'

    export default {
        name: 'notifier-callback',
        mixins: [notifierMixin]
    }

</script>

<style scoped>
    .notifier-title {
        font-size: 3em;
        margin-left: 10px;
    }
    
    @media (max-width: 767px) {
        .key,
        .add-new,
        .operations {
            display: none !important;
        }
    }
</style>