define(["jquery", "vue", "text!js/components/syncTypes/push/push.html"], function ($, Vue, pushTemplate) {
    function init() {
        Vue.component('sync-push', {
            template: pushTemplate,
            props: ['node'],
            ready: function () {
                //init binding
                Vue.set(this.node, "checkIntervalSeconds", 30);
            }
        });
    }

    init();
});