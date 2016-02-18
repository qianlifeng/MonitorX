define(["jquery", "vue", "js/components/syncTypes/pull/pull", "js/components/syncTypes/push/push"], function ($, Vue, pull, push) {
    var vm;

    function init() {
        vm = new Vue({
            el: ".content-container",
            data: {
                node: {
                    code: "",
                    title: "",
                    syncType: "push"
                }
            },
            methods: {
                addNode: function () {
                    $.post("/api/node/", this.node, function (res) {
                        if (res.success) {
                            window.location.href = "/";
                        }
                    });
                }
            }
        });
    }

    init();
});