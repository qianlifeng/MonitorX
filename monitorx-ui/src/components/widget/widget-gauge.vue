<template>
    <div ref="chart" class="echarts" />
</template>

<script>
    import widgetMixin from "./widget-mixin.js"
    import echarts from "echarts"

    export default {
        name: 'widget-gauge',
        mixins: [widgetMixin],
        data() {
            return {
                chart: null,
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
        mounted() {
            this.chart = echarts.init(this.$refs.chart);
            this.chart.setOption(this.chartOptions, true);
        },
        watch: {
            value: function (val, oldVal) {
                this.chartOptions.series[0].data[0].value = val;
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