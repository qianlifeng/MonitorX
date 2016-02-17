define(["jquery", "vue", "js/components/gauge", "js/components/line", "js/components/number"], function ($, Vue, gauge, line, number) {
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
            methods: {
                addForewarning: function (metric) {
                    alert("add forewarning:" + metric.title);
                },
                isNodeUp: function (node) {
                    if (node.status == null) return false;
                    return node.status.status == "up";
                }
            }
        });
        sync(false);
    }

    function sync(partial) {
        $.get("/api/node/" + getUrlParameter("node") + "/", function (res) {
            if (partial) {
                if (vm.node.status == null) {
                    vm.node.status = {
                        metrics: []
                    };
                }

                if (res.status != null) {
                    //only update metric values
                    vm.node.status.status = res.status.status;
                    vm.node.status.formattedLastUpdateDate = res.status.formattedLastUpdateDate;
                    vm.node.status.lastUpdateDate = res.status.lastUpdateDate;
                    for (var index in vm.node.status.metrics) {
                        vm.node.status.metrics[index].value = res.status.metrics[index].value;
                    }
                }
            }
            else {
                vm.node = res;
            }
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
    setInterval(function () {
        sync(true)
    }, 1000);
});