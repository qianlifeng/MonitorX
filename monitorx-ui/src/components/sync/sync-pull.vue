<template>
    <div>
        <div class="form-group">
            <div class="col-sm-10 col-sm-offset-2">
                <span class="text-muted">MonitorX will pull status from Node</span>
            </div>
        </div>
        <div class="form-group">
            <label class="col-sm-2 control-label">Url</label>
            <div class="col-sm-10">
                <input type="text" class="form-control" v-model="config.url" placeholder="Url that Node expose its status">
            </div>
        </div>
        <div class="form-group">
            <label class="col-sm-2 control-label">Interval</label>
            <div class="col-sm-10">
                <input type="text" class="form-control" v-model="config.interval" placeholder="Seconds">
            </div>
        </div>
    </div>
</template>

<script>
    import syncMixin from "./sync-mixin.js"

    export default {
        name: 'sync-pull',
        mixins: [syncMixin]
    }

</script>