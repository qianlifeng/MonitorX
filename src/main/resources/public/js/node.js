define(["jquery", "vue", "js/components/line", "js/components/pie"], function ($, Vue, line, pie) {
    var vm;

    function init() {
        vm = new Vue({
            el: ".detail",
            data: {
                node: {
                    status: {
                        metrics: []
                    }
                }
            },
            methods: {}
        });
        sync();
    }

    function sync() {
        $.get("/node/" + getUrlParameter("node") + "/", function (res) {
            vm.node = res;
        });
    }

    function getUrlParameter(sParam) {
        var sPageURL = decodeURIComponent(window.location.search.substring(1)),
            sURLVariables = sPageURL.split('&'),
            sParameterName,
            i;

        for (i = 0; i < sURLVariables.length; i++) {
            sParameterName = sURLVariables[i].split('=');

            if (sParameterName[0] === sParam) {
                return sParameterName[1] === undefined ? true : sParameterName[1];
            }
        }
    }

    init();
    setInterval(sync, 3000);
});