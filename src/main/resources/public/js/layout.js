define(["jquery", "vue", "bootstrap"], function ($, Vue, b) {
    Vue.config.debug = true;

    $(function () {
        $('[data-toggle="tooltip"]').tooltip({container: 'body'});
    });
});