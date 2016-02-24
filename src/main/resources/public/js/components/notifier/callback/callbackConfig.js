define(["jquery", "vue", "text!js/components/notifier/callback/callbackConfig.html"], function ($, Vue, template) {

    function init() {
        Vue.component('notifierConfig-callback', {
            template: template,
            props: ['config'],
            ready: function () {
                //init binding
                Vue.set(this.config, "callbackUrl", "");
            }
        });
    }

    init();
});