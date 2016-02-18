define(["jquery", "vue", "text!js/components/syncTypes/push/push.html"], function ($, Vue, pushTemplate) {
    function init() {
        Vue.component('sync-push', {
            template: pushTemplate
        });
    }

    init();
});