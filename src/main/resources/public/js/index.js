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
                    window.location.href = "/n/?node=" + code;
                },
                isNodeUp: function (node) {
                    if (node.status == null) return false;
                    return node.status.status == "up";
                }
            }
        });

        sync();
    }

    function sync() {
        $.get("/node/", function (res) {
            vm.nodes = res;
        });
    }

    init();
    setInterval(sync, 1000);
});