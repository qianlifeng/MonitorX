define(["jquery", "vue", "echart"], function ($, Vue, echarts) {

    function init() {
        Vue.component('pie', {
            template: '<div id="main">pie</div>',
            props: ['value']
        });
    }

    init();
});