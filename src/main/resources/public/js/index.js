define(["jquery", "vue"], function ($, Vue) {
    var vm;

    function init() {
        vm = new Vue({
            el: ".index",
            data: {
                nodes: []
            },
            methods: {}
        });

        sync();
    }

    function sync() {
        $.get("/node/", function (res) {
            vm.nodes = res;
        });
    }

    init();
    setInterval(sync, 3000);
});