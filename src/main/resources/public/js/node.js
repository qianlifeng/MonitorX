define(["jquery", "vue",
    "js/components/widgets/gauge",
    "js/components/widgets/line",
    "js/components/widgets/number"], function ($, Vue, gauge, line, number) {
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
                isNodeUp: function () {
                    if (this.node.status == null) return false;
                    return this.node.status.status == "up";
                },
                removeNode: function () {
                    if (confirm("Do you want to remove this node?")) {
                        $.ajax({
                            url: "/api/node/" + this.node.code + "/",
                            type: 'DELETE',
                            success: function (res) {
                                window.location.href = "/";
                            }
                        });
                    }
                }
            }
        });
        sync(false);
    }

    function sync(partial) {
        $.get("/api/node/" + getUrlParameter("node") + "/", function (res) {
            var node = res.data;
            if (partial) {
                if (vm.node.status == null) {
                    vm.node.status = {
                        metrics: []
                    };
                }

                if (node.status != null) {
                    //only update metric values
                    vm.node.status.status = node.status.status;
                    vm.node.status.formattedLastUpdateDate = node.status.formattedLastUpdateDate;
                    vm.node.status.lastUpdateDate = node.status.lastUpdateDate;
                    for (var index in vm.node.status.metrics) {
                        vm.node.status.metrics[index].value = node.status.metrics[index].value;
                    }
                }
            }
            else {
                vm.node = node;
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