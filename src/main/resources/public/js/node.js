define(["jquery", "vue", "bootstrap",
    "js/components/widgets/gauge",
    "js/components/widgets/line",
    "js/components/widgets/pie",
    "js/components/widgets/text",
    "js/components/widgets/number"], function ($, Vue) {
    var vm;
    var allNotifiers = [];

    function getNotifier(id) {
        for (var index in allNotifiers) {
            var notifier = allNotifiers[index];
            if (notifier.id == id) return $.extend(true, {}, notifier);
        }

        return null;
    }

    function init() {
        vm = new Vue({
            el: ".detail",
            data: {
                node: {
                    status: {
                        metrics: []
                    },
                    forewarnings: []
                }
            },
            ready: function () {
                setTimeout(function () {
                    $('[data-toggle="tooltip"]').tooltip({container: 'body'});
                }, 1000);
            },
            methods: {
                addForewarning: function (metric) {
                    window.location.href = "/forewarning/new/?node=" + this.node.code + "&metric=" + metric.title;
                },
                getMetricNotifers: function (metric) {
                    var notifiers = [];
                    for (var forewarningIndex in this.node.forewarnings) {
                        var forewarning = this.node.forewarnings[forewarningIndex];
                        if (forewarning.metric == metric.title) {
                            for (var notifierIndex in forewarning.notifiers) {
                                var notifierId = forewarning.notifiers[notifierIndex];
                                var notifier = getNotifier(notifierId);
                                if (notifier != null) {
                                    notifier.forewarningTitle = forewarning.title;
                                    notifier.forewarningId = forewarning.id;
                                    notifiers.push(notifier);
                                }
                            }
                        }
                    }

                    return notifiers;
                },
                getNodeNotifers: function () {
                    var notifiers = [];
                    for (var forewarningIndex in this.node.forewarnings) {
                        var forewarning = this.node.forewarnings[forewarningIndex];
                        if (forewarning.metric == "") {
                            for (var notifierIndex in forewarning.notifiers) {
                                var notifierId = forewarning.notifiers[notifierIndex];
                                var notifier = getNotifier(notifierId);
                                if (notifier != null) {
                                    notifier.forewarningTitle = forewarning.title;
                                    notifier.forewarningId = forewarning.id;
                                    notifiers.push(notifier);
                                }
                            }
                        }
                    }

                    return notifiers;
                },
                getNodeMetrics: function () {
                    var metrics = [];
                    if (this.node.status != null && this.node.status.metrics != null) {
                        metrics = this.node.status.metrics;
                    }

                    return metrics;
                },
                editMetricForewarning: function (metric, forewarningId) {
                    window.location.href = "/forewarning/edit/?edit=true&node=" + this.node.code + "&forewarningId=" + forewarningId + "&metric=" + metric;
                },
                addNodeForewarning: function () {
                    window.location.href = "/forewarning/new/?node=" + this.node.code;
                },
                editNodeForewarning: function (forewarningId) {
                    window.location.href = "/forewarning/edit/?edit=true&node=" + this.node.code + "&forewarningId=" + forewarningId;
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

        $.get("/api/notifier/", function (res) {
            allNotifiers = res.data;
            sync(false);
        });
    }

    function sync(partial) {
        $.get("/api/node/" + getUrlParameter("node") + "/", function (res) {
            var node = res.data;
            if (partial) {
                if (node.status != null) {
                    //only update metric values
                    vm.node.status.status = node.status.status;
                    vm.node.status.formattedLastUpdateDate = node.status.formattedLastUpdateDate;
                    vm.node.status.lastUpdateDate = node.status.lastUpdateDate;

                    if (vm.node.status.metrics.length == 0 && node.status.metrics.length != 0) {
                        vm.node.status.metrics = node.status.metrics;
                    }
                    else {
                        for (var index in vm.node.status.metrics) {
                            var metricTitle = vm.node.status.metrics[index].title;

                            for (var j in node.status.metrics) {
                                var waitingUpdateMetric = node.status.metrics[j];
                                if (waitingUpdateMetric.title == metricTitle) {
                                    vm.node.status.metrics[index].value = waitingUpdateMetric.value;
                                }
                            }
                        }
                    }
                }
            }
            else {
                vm.node = node;
            }

            rebuildNullStatus();
        });
    }

    function rebuildNullStatus() {
        if (vm.node.status == null) {
            vm.node.status = {
                status: "down",
                formattedLastUpdateDate: "",
                metrics: []
            };
        }
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