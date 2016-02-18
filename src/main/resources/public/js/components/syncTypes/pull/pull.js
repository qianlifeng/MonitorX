define(["jquery", "vue", "text!js/components/syncTypes/pull/pull.html"], function ($, Vue, pullTemplate) {

    function init() {
        Vue.component('sync-pull', {
            template: pullTemplate
        });
    }

    init();
});