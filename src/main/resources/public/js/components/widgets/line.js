define(["jquery", "vue", "echart"], function ($, Vue, echarts) {
    var lineOption = {
        legend: {
            top: 'bottom',
            data: ['意向']
        },
        xAxis: [
            {
                type: 'category',
                boundaryGap: false,
                data: ["1", "2", "3"]
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
                data: [1, 2, 3]
            }
        ]
    };

    function init() {
        Vue.component('line', {
            template: "<div class='metric-widget'></div>",
            props: ['value'],
            ready: function () {
                this.chart = echarts.init(this.$el);
                this.chart.setOption(lineOption);
            },
            watch: {
                "value": function (val, oldVal) {
                    this.chart.setOption(lineOption, true);
                }
            }
        });
    }

    init();
});