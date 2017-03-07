define(["jquery", "vue",
    "js/components/notifier/wechat/wechat",
    "js/components/notifier/email/email",
    "js/components/notifier/callback/callback"], function ($, Vue) {
    var vm;

    function init() {

        Vue.filter('mask', function (value) {
            if (value.length <= 5) return "***";
            return value.substring(0, value.length - 5) + "*****";
        });

        vm = new Vue({
            el: ".notifier",
            data: {
                notifiers: []
            },
            methods: {
                removeNotifier: function (notifier) {
                    if (confirm("Do you want to remove this notifier? All related notify will be removed too")) {
                        $.ajax({
                            url: "/api/notifier/" + notifier.id + "/",
                            type: 'DELETE',
                            success: function (res) {
                                window.location.href = "/notifier/";
                            }
                        });
                    }
                }
            }
        });

        $.get("/api/notifier/", function (res) {
            vm.notifiers = res.data;
        });
    }

    init();
});