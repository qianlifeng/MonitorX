<template>
    <chart :options="chartOptions" />
</template>

<script>
    import widgetMixin from "./widget-mixin.js"
    import ECharts from 'vue-echarts/components/ECharts.vue'
    import 'echarts/lib/chart/pie'

    export default {
        name: 'widget-pie',
        mixins: [widgetMixin],
        components: {
            "chart": ECharts
        },
        data() {
            return {
                chartOptions: {
                    tooltip: {
                        trigger: 'item',
                        formatter: "{c}"
                    },
                    series: [
                        {
                            name: '',
                            type: 'pie',
                            center: ['50%', '60%'],
                            data: JSON.parse(this.value)
                        }
                    ]
                }
            };
        },
        watch: {
            value: function (val, oldVal) {
                this.chartOptions.series[0].data = JSON.parse(val);
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