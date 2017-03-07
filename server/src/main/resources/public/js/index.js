define(["jquery", "vue"], function ($, Vue) {
    var vm;

    function init() {
        vm = new Vue({
            el: ".index",
            data: {
                nodes: []
            },
            methods: {
                navigate: function (code) {
                    window.location.href = "/node/?node=" + code;
                },
                isNodeUp: function (node) {
                    if (node.status == null) return false;
                    return node.status.status == "up";
                }
            }
        });

        sync();
    }

    function rebuildNullStatus() {
        for (var i in vm.nodes) {
            var node = vm.nodes[i];
            if (node.status == null) {
                node.status = {
                    status: "down",
                    formattedLastUpdateDate: "",
                    metrics: []
                };
            }
        }
    }

    function sync() {
        $.get("/api/node/", function (res) {
            vm.nodes = res.data;
            rebuildNullStatus();
        });
    }

    init();
    setInterval(sync, 1000);
});