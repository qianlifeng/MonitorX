<template>
    <chart :options="chartOptions" />
</template>

<script>
    import widgetMixin from "./widget-mixin.js"
    import ECharts from 'vue-echarts/components/ECharts.vue'
    import 'echarts/lib/chart/gauge'

    export default {
        name: 'widget-gauge',
        mixins: [widgetMixin],
        components: {
            "chart": ECharts
        },
        data() {
            return {
                chartOptions: {
                    series: [
                        {
                            name: '',
                            type: 'gauge',
                            detail: { formatter: '{value}' },
                            data: [
                                { value: this.value, name: '' }
                            ]
                        }
                    ]
                }
            };
        },
        watch: {
            value: function (val, oldVal) {
                this.chartOptions.series[0].data[0].value = val;
            }
        }
    }

</script>

<style scoped>
    .echarts {
        height: 270px;
        width: 100%;
    }
</style>