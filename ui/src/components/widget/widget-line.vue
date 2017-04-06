<template>
    <chart :options="chartOptions" />
</template>

<script>
    import widgetMixin from "./widget-mixin.js"
    import ECharts from 'vue-echarts/components/ECharts.vue'
    import 'echarts/lib/chart/line'

    export default {
        name: 'widget-line',
        mixins: [widgetMixin],
        components: {
            "chart": ECharts
        },
        data() {
            return {
                chartOptions: {
                    grid: {
                        x: 50,
                        y: 50,
                        x2: 50,
                        y2: 50
                    },
                    tooltip: {
                        trigger: 'axis'
                    },
                    xAxis: [
                        {
                            type: 'category',
                            boundaryGap: false,
                            data: []
                        }
                    ],
                    yAxis: [
                        {
                            type: 'value'
                        }
                    ],
                    series: [
                        {
                            name: '',
                            type: 'line',
                            smooth: true,
                            symbol: 'none',
                            stack: 'a',
                            areaStyle: {
                                normal: {}
                            },
                            data: []
                        }
                    ]
                }
            };
        },
        watch: {
            historyValue: function (val, oldVal) {
                this.chartOptions.xAxis[0].data = [];
                this.chartOptions.series[0].data = [];
                for (var i in val) {
                    var line = JSON.parse(val[i]);
                    this.chartOptions.xAxis[0].data.push(line.x);
                    this.chartOptions.series[0].data.push(line.y);
                }
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