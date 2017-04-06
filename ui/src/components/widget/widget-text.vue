<template>
    <div class="text" v-html="value"></div>
</template>

<script>
    import widgetMixin from "./widget-mixin.js"

    export default {
        name: 'widget-text',
        mixins: [widgetMixin]
    }

</script>

<style scoped>
    .echarts {
        height: 270px;
        width: 100%;
    }
</style>