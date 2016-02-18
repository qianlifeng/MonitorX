define(["jquery", "vue",
    "js/components/notifier/wechat/wechatConfig",
    "js/components/notifier/email/emailConfig"], function ($, Vue, wechat, email) {
    var vm;

    function init() {
        vm = new Vue({
            el: ".content-container",
            data: {
                notifier: {
                    title: "",
                    type: "wechat",
                    config: {}
                },
                msg: {
                    title: "Test Title From MonitorX",
                    body: "This is test message body"
                }
            },
            methods: {
                addNotifier: function () {
                    $.post("/api/notifier/",
                        {
                            "notifier": JSON.stringify(this.notifier),
                        },
                        function (res) {
                            if (res.success) {
                                window.location.href = "/notifier/";
                            }
                        });
                },
                send: function () {
                    $.post("/api/notifier/testsend/",
                        {
                            "notifier": JSON.stringify(this.notifier),
                            "title": this.msg.title,
                            "body": this.msg.body
                        },
                        function (res) {
                            if (res.success) {
                                alert("Success");
                            }
                        });
                }
            }
        });
    }

    init();
});