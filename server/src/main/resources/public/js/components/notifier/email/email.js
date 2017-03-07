define(["jquery", "vue", "text!js/components/notifier/email/email.html"], function ($, Vue, emailTemplate) {

    function init() {
        Vue.component('notifier-email', {
            template: emailTemplate,
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