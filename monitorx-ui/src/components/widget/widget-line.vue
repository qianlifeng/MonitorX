<template>
    <div ref="chart" class="echarts" />
</template>

<script>
    import widgetMixin from "./widget-mixin.js"
    import echarts from "echarts"

    export default {
        name: 'widget-line',
        mixins: [widgetMixin],
        data() {
            return {
                chart: null,
                chartOptions: {
                    grid: {
                        x: 50,
                        y: 50,
                        x2: 50,
                        y2: 50
                    },
                    legend: {
                        data: []
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

                    ]
                }
            };
        },
        mounted() {
            this.chart = echarts.init(this.$refs.chart);
            this.chart.setOption(this.chartOptions, true);
        },
        methods: {
            initSeries(y) {
                if (this.chartOptions.series.length == 0) {
                    if (Object.prototype.toString.call(y) === '[object Array]') {
                        for (let item in y) {
                            this.chartOptions.series.push(
                                {
                                    name: '',
                                    type: 'line',
                                    smooth: true,
                                    symbol: 'none',
                                    areaStyle: {
                                        normal: {}
                                    },
                                    data: []
                                }
                            );
                        }
                    }
                    else {
                        this.chartOptions.series.push(
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
                        );
                    }
                }
            }
        },
        watch: {
            historyValue: function (val, oldVal) {
                this.chartOptions.xAxis[0].data = [];
                this.chartOptions.series = [];

                for (var i in val) {
                    var line = JSON.parse(val[i]);
                    this.initSeries(line.y);

                    this.chartOptions.xAxis[0].data.push(line.x);
                    if (Object.prototype.toString.call(line.y) === '[object Array]') {
                        let legends = [];
                        line.y.forEach((item, index) => {
                            this.chartOptions.series[index].data.push(item.value);
                            this.chartOptions.series[index].name = item.name;
                            legends.push(item.name);
                        });
                        this.chartOptions.legend.data = legends;
                    }
                    else {
                        this.chartOptions.series[0].data.push(line.y);
                    }
                }
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