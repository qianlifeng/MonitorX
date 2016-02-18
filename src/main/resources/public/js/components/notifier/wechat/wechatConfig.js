define(["jquery", "vue", "text!js/components/notifier/wechat/wechatConfig.html"], function ($, Vue, wechatTemplate) {

    function init() {
        Vue.component('notifierConfig-wechat', {
            template: wechatTemplate,
            props: ['config'],
            ready: function () {
                //init binding
                Vue.set(this.config, "secret", "");
            }
        });
    }

    init();
});