<template>
    <div ref="chart" class="echarts" />
</template>

<script>
    import widgetMixin from "./widget-mixin.js"
    import echarts from "echarts"

    export default {
        name: 'widget-pie',
        mixins: [widgetMixin],
        data() {
            return {
                chart: null,
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
        mounted() {
            this.chart = echarts.init(this.$refs.chart);
            this.chart.setOption(this.chartOptions, true);
        },
        watch: {
            value: function (val, oldVal) {
                this.chartOptions.series[0].data = JSON.parse(val);
                this.chart.setOption(this.chartOptions, true);
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