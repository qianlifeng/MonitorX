define(["jquery", "vue", "text!js/components/notifier/wechat/wechat.html"], function ($, Vue, wechatTemplate) {

    function init() {
        Vue.component('notifier-wechat', {
            template: wechatTemplate,
            props: ['notifier'],
            ready: function () {
                $(function () {
                    $('[data-toggle="tooltip"]').tooltip({container: 'body'});
                });
            },
            methods: {
                removeNotifier: function () {
                    this.$parent.$options.methods.removeNotifier(this.notifier);
                }
            }
        });
    }

    init();
});