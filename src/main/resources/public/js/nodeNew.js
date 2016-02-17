define(["jquery", "vue", "js/components/gauge", "js/components/line"], function ($, Vue, gauge, line) {
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
                    });
                }
            }
        });
    }

    init();
});