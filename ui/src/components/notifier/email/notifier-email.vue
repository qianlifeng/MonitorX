<template>
    <div class="status-widget status-blue">
        <div class="row">
            <div class="col-sm-9">
                <i class="fa fa-envelope-o fa-3x"></i><span class="notifier-title" v-text="notifier.title"></span>
            </div>
            <div class="col-sm-3 operations">
                <i class="fa fa-trash-o clickable pull-right" v-on:click="removeNotifier()" data-toggle="tooltip" data-placement="top" title="Remove notifier"></i>
            </div>
        </div>
        <div class="row">
            <div class="col-sm-12">
                <div class="key pull-right">
                    <i class="fa fa-envelope-open-o"></i> <span class="" v-text="notifier.config.toEmail"></span>
                </div>
            </div>
        </div>
    </div>
</template>

<script>
    import notifierMixin from '../notifier-mixin'

    export default {
        name: 'notifier-email',
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