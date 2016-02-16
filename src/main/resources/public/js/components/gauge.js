define(["jquery", "vue", "echart"], function ($, Vue, echarts) {

    var gaugeOption = {
        series: [
            {
                name: '',
                type: 'gauge',
                detail: {formatter: '{value}'},
                data: [{value: 0, name: ''}]
            }
        ],
        animation: false
    };


    function init() {
        Vue.component('line', {
            template: "<div><div class='chart' style='width:600px;height:300px;'></div></div>",
            props: ['value'],
            ready: function () {
                this.chart = echarts.init($(".chart", $(this.$el))[0]);
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