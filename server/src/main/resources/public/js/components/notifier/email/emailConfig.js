define(["jquery", "vue", "text!js/components/notifier/email/emailConfig.html"], function ($, Vue, emailTemplate) {

    function init() {
        Vue.component('notifierConfig-email', {
            template: emailTemplate,
            props: ['config'],
            ready: function () {
                //init binding
                Vue.set(this.config, "toEmail", "");
            }
        });
    }

    init();
});