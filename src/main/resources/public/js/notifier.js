define(["jquery", "vue", "js/components/notifier/wechat/wechat"], function ($, Vue) {
    var vm;

    function init() {
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