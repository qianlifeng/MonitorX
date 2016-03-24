define(["jquery", "vue"], function ($, Vue) {

    function init() {
        Vue.component('text', {
            template: "<div class='metric-widget text'></div>",
            props: ['value'],
            ready: function () {
                this.$el.innerHTML = this.value;
            },
            watch: {
                "value": function (val, oldVal) {
                    this.$el.innerHTML = this.value;
                }
            }
        });
    }

    init();
});