define(["jquery", "vue", "echart"], function ($, Vue, echarts) {
    var lineOption = {
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
    };

    function init() {
        Vue.component('line', {
            template: "<div class='metric-widget'></div>",
            props: ['historyValue'],
            ready: function () {
                this.chart = echarts.init(this.$el);
                this.chart.setOption(lineOption);
            },
            watch: {
                "historyValue": function (val, oldVal) {
                    lineOption.xAxis[0].data = [];
                    lineOption.series[0].data = [];
                    for (var i in val) {
                        var line = JSON.parse(val[i]);
                        lineOption.xAxis[0].data.push(line.x);
                        lineOption.series[0].data.push(line.y);
                    }

                    this.chart.setOption(lineOption, true);
                }
            }
        });
    }

    init();
});