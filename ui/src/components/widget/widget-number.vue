<template>
    <odometer class="odometer number" :value="val"></odometer>
</template>

<script>
    import widgetMixin from "./widget-mixin.js"
    import odometer from 'vue-odometer';

    export default {
        name: 'widget-number',
        mixins: [widgetMixin],
        computed: {
            val() {
                return parseInt(this.value);
            }
        },
        components: {
            "odometer": odometer
        }
    }

</script>

<style scoped>
    .number {
        font-weight: bold;
        font-size: 90px;
        font-family: 黑体 !important;
        text-align: center;
        padding-top: 60px;
        height: 270px;
        width: 100%;
    }
</style>