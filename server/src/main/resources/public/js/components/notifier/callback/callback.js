define(["jquery", "vue", "text!js/components/notifier/callback/callback.html"], function ($, Vue, template) {

    function init() {
        Vue.component('notifier-callback', {
            template: template,
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