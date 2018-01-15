<template>
    <div>
        <div class="form-group">
            <div class="col-sm-10 col-sm-offset-2">
                <span class="text-muted">Please Enable <a href="https://docs.spring.io/spring-boot/docs/current-SNAPSHOT/reference/htmlsingle/#production-ready">Spring Actuator</a> First</span>
            </div>
        </div>
        <div class="form-group">
            <label class="col-sm-2 control-label">Url</label>
            <div class="col-sm-10">
                <input type="text" class="form-control" v-model="config.url" placeholder="Spring Actuator Url">
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