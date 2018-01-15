<template>
    <div>
        <div class="form-group">
            <label class="col-sm-2 control-label">To</label>

            <div class="col-sm-10">
                <input type="text" class="form-control" v-model="config.toEmail" placeholder="Email address">
            </div>
        </div>
    </div>
</template>

<script>
    import notifierConfigMixin from '../notifier-config-mixin'

    export default {
        name: 'notifier-email-config',
        mixins: [notifierConfigMixin]
    }

</script>