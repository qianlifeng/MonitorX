<template>
    <div>
        <div class="form-group">
            <div class="col-sm-10 col-sm-offset-2">
                <span class="text-muted">Node will push its status to MonitorX</span>
            </div>
        </div>
        <div class="form-group">
            <label class="col-sm-2 control-label">Status Check Interval</label>

            <div class="col-sm-10">
                <input type="text" class="form-control" v-model="config.interval" placeholder="If this node didn't push its status to MonitorX every x seconds, monitorx will consider it has down">
            </div>
        </div>
    </div>
</template>

<script>
    import syncMixin from "./sync-mixin.js"

    export default {
        name: 'sync-push',
        mixins: [syncMixin]
    }

</script>