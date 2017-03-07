define(["jquery", "vue", "echart"], function ($, Vue, echarts) {
    var pieOption = {
        tooltip: {
            trigger: 'item',
            formatter: "{c}"
        },
        series: [
            {
                name: '',
                type: 'pie',
                center: ['50%', '60%'],
                data: [
                    {value: 335, name: '直接访问'},
                ]
            }
        ]
    };


    function init() {
        Vue.component('pie', {
            template: "<div class='metric-widget'></div>",
            props: ['value'],
            ready: function () {
                this.chart = echarts.init(this.$el);
                pieOption.series[0].data = JSON.parse(this.value);
                this.chart.setOption(pieOption);
            },
            watch: {
                "value": function (val, oldVal) {
                    pieOption.series[0].data = JSON.parse(val);
                    this.chart.setOption(pieOption, true);
                }
            }
        });
    }

    init();
});