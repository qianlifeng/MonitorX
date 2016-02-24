define(["jquery", "vue"], function ($, Vue) {
    var vm;

    function init() {
        var node = getUrlParameter("node");
        var edit = getUrlParameter("edit");
        var metric = getUrlParameter("metric");
        if (typeof metric === "undefined" || metric == null) metric = "";
        if (typeof edit === "undefined" || edit == null) edit = "false";

        vm = new Vue({
            el: ".content-container",
            data: {
                edit: edit == "true",
                node: node,
                title: "",
                metric: metric,
                snippet: "",
                firerule: "",
                notifiers: [],
                availableNotifiers: [],
                msg: ""
            },
            methods: {
                addForewarning: function () {
                    $.post("/api/forewarning/",
                        {
                            "title": this.title,
                            "node": this.node,
                            "metric": this.metric,
                            "snippet": this.snippet,
                            "firerule": this.firerule,
                            "notifiers": this.notifiers,
                            "msg": this.msg
                        },
                        function (res) {
                            if (res.success) {
                                window.location.href = "/node/?node=" + node;
                            }
                            else {
                                alert(res.message);
                            }
                        });
                },
                removeForewarning: function () {
                    if (confirm("Do you want to remove this forewarning?")) {
                        $.post("/api/forewarning/delete/",
                            {
                                "title": this.title,
                                "node": this.node,
                                "metric": this.metric,
                                "snippet": this.snippet,
                                "firerule": this.firerule,
                                "notifiers": this.notifiers,
                                "msg": this.msg
                            },
                            function (res) {
                                if (res.success) {
                                    window.location.href = "/node/?node=" + node;
                                }
                            });
                    }
                }
            }
        });


        $.get("/api/notifier/", function (res) {
            vm.availableNotifiers = res.data;
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
});