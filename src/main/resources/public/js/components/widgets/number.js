define(["jquery", "vue", "odometer"], function ($, Vue, odometer) {

    function init(odometer) {
        Vue.component('number', {
            template: "<div class='odometer metric-widget number'></div>",
            props: ['value'],
            ready: function () {
                this.od = new odometer({
                    el: this.$el,
                    value: 0
                });
                this.od.update(this.value);
            },
            watch: {
                "value": function (val, oldVal) {
                    this.od.update(val);
                }
            }
        });
    }

    init(odometer);
});