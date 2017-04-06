<template>
    <div class="status-widget status-yellow">
        <div class="row">
            <div class="col-sm-9">
                <i class="fa fa-weixin fa-3x"></i><span class="notifier-title">{{notifier.title}}</span>
            </div>
            <div class="col-sm-3 operations">
                <i class="fa fa-trash-o clickable pull-right" @click="removeNotifier()" data-toggle="tooltip" data-placement="top" title="Remove notifier"></i>
            </div>
        </div>
        <div class="row">
            <div class="col-sm-12">
                <div class="key pull-right">
                    <i class="fa fa-key"></i> <span>{{notifier.config.secret | mask}}</span>
                </div>
            </div>
        </div>
    </div>
</template>

<script>
    import notifierMixin from '../notifier-mixin'

    export default {
        name: 'notifier-wechat',
        mixins: [notifierMixin],
        filters: {
            mask: function (value) {
                if (value.length <= 5) return "***";
                return value.substring(0, value.length - 5) + "*****";
            }
        }
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