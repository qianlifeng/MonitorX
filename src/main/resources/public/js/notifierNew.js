define(["jquery", "vue", "js/components/notifier/wechat/wechatConfig"], function ($, Vue, wechat) {
    var vm;

    function init() {
        vm = new Vue({
            el: ".content-container",
            data: {
                notifier: {
                    title: "",
                    type: "wechat",
                    config: {}
                }
            },
            methods: {
                addNotifier: function () {
                    $.post("/api/notifier/",
                        {
                            "notifier": JSON.stringify(this.notifier)
                        },
                        function (res) {
                            if (res.success) {
                                window.location.href = "/notifier/";
                            }
                        });
                }
            }
        });
    }

    init();
});