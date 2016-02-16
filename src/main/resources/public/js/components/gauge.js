define(["jquery", "vue", "echart"], function ($, Vue, echarts) {
    var gaugeOption = {
        series: [
            {
                name: '',
                type: 'gauge',
                detail: {formatter: '{value}'},
                data: [{value: 0, name: ''}]
            }
        ]
    };

    function init() {
        Vue.component('gauge', {
            template: "<div style='width:100%;height:270px;'></div>",
            props: ['value'],
            ready: function () {
                this.chart = echarts.init(this.$el);
                gaugeOption.series[0].data[0].value = this.value;
                this.chart.setOption(gaugeOption);
            },
            watch: {
                "value": function (val, oldVal) {
                    gaugeOption.series[0].data[0].value = val;
                    this.chart.setOption(gaugeOption, true);
                }
            }
        });
    }

    init();
});