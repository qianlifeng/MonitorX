<template>
    <div class="detail">
        <div class="loading" v-if="loading">
            <img src="../assets/loading.gif">
        </div>
        <div v-else>
            <div class="status-widget" :class="{ 'status-up': status.status == 'up','status-down': status.status != 'up' }">
                <div class="row">
                    <div class="col-sm-6">
                        <span class="title" v-text="title"></span>
                    </div>
                    <div class="col-sm-6 operations">
                        <i class="fa fa-trash-o clickable pull-right" v-on:click="removeNode()" data-toggle="tooltip" data-placement="top" title="Remove node"></i>
                        <i class="fa fa-plus clickable pull-right forewarning-icon" data-toggle="tooltip" data-placement="top" title="Add forewarning" @click="addNodeForewarning()"></i>
                            <i class="fa forewarning-icon clickable pull-right" @click="editNodeForewarning(nodeNotifier.forewarningId)" data-toggle="tooltip"
                                data-placement="top" :title="'When ' + nodeNotifier.forewarningTitle + ', notify ' + nodeNotifier.title" :class="[nodeNotifier.fontawesomeIcon]"
                             :key="index" v-for="(nodeNotifier,index) in getNodeNotifers()"></i>
                    </div>
                </div>
                <div class="lastupdate" v-show="status.formattedLastUpdateDate != ''">
                    <i class="fa fa-clock-o"></i> <span v-text="status.formattedLastUpdateDate"></span>
                </div>
                <div class="clearfix"></div>
            </div>
            <div class="row">
                <div class="col-sm-12 " :class="getMetricWidthClass(metric)" :key="index" v-for="(metric,index) in status.metrics">
                    <div class="panel panel-default">
                        <div class="panel-heading">
                            <div class="row">
                                <div class="col-sm-7">
                                    <h3 class="panel-title" v-html="metric.title"></h3>
                                </div>
                                <div class="col-sm-5 operations">
                                    <div class="pull-right">
                                        <i class="fa forewarning-icon clickable" @click="editMetricForewarning(metric,notifier.forewarningId)" data-toggle="tooltip"
                                            data-placement="top" :title="'When ' + notifier.forewarningTitle + ', notify ' + notifier.title"
                                            :class="[notifier.fontawesomeIcon]" :key="index"  v-for="(notifier,index) in getMetricNotifers(metric)"></i>
                                            <i class="fa fa-plus clickable" title="Add metric forewarning" data-toggle="tooltip" data-placement="top" @click="addForewarning(metric)"></i>
                                    </div>
                                </div>
                            </div>
                        </div>
                        <div class="panel-body">
                            <component :is="'widget-' + metric.type" :value="metric.value" :history-value="metric.historyValue"></component>
                        </div>
                    </div>
                </div>
            </div>
        </div>

        <div class="modal fade" id="forewarningDialog" role="dialog">
            <div class="modal-dialog" role="document">
                <div class="modal-content">
                    <div class="modal-header">
                        <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
                        <h4 class="modal-title">Forewarning</h4>
                    </div>
                    <div class="modal-body">
                        <forewarning ref="forewarningRef" :edit="forewarning.edit" :forewarningId="forewarning.forewarningId" :metric="forewarning.metric.title"
                            :node="code" />
                    </div>
                    <div class="modal-footer">
                        <button type="button" @click="saveForewarning" class="btn btn-success">Save</button>
                        <button type="button" @click="removeForewarning" class="btn btn-danger ">Remove</button>
                    </div>
                </div>
            </div>
        </div>
    </div>
</template>

<script>
import forewarning from "./forewarning.vue";
import widgetNumber from "./widget/widget-number.vue";
import widgetGauge from "./widget/widget-gauge.vue";
import widgetText from "./widget/widget-text.vue";
import widgetLine from "./widget/widget-line.vue";
import widgetPie from "./widget/widget-pie.vue";
import Stomp from "stompjs";
import SockJS from "sockjs-client";

export default {
  components: {
    forewarning: forewarning,
    "widget-number": widgetNumber,
    "widget-gauge": widgetGauge,
    "widget-text": widgetText,
    "widget-line": widgetLine,
    "widget-pie": widgetPie
  },
  data() {
    return {
      stompClient: null,
      partialUpdate: false,
      intervalInstance: null,
      loading: true,
      title: " ",
      code: this.$route.params.code,
      status: {
        metrics: []
      },
      allNotifiers: [],
      showForewarning: false,
      forewarning: {
        edit: false,
        metric: "",
        forewarningId: ""
      },
      forewarnings: []
    };
  },
  watch: {
    showForewarning: function(newVal) {
      if (newVal) {
        $("#forewarningDialog").modal("show");
      } else {
        $("#forewarningDialog").modal("hide");
      }
    }
  },
  methods: {
    saveForewarning() {
      this.$refs.forewarningRef.addForewarning();
    },
    removeForewarning() {
      this.$refs.forewarningRef.removeForewarning();
    },
    getMetricWidthClass: function(metric) {
      var ratio = metric.width || 0.5;
      return "col-md-" + ratio * 12 + " col-lg-" + ratio * 12;
    },
    getMetricNotifers: function(metric) {
      var notifiers = [];
      for (var forewarningIndex in this.forewarnings) {
        var forewarning = this.forewarnings[forewarningIndex];
        if (forewarning.metric === metric.title) {
          for (var notifierIndex in forewarning.notifiers) {
            var notifierId = forewarning.notifiers[notifierIndex];
            var notifier = this.getNotifier(notifierId);
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
    getNodeNotifers: function() {
      var notifiers = [];
      for (var forewarningIndex in this.forewarnings) {
        var forewarning = this.forewarnings[forewarningIndex];
        if (forewarning.metric === "") {
          for (var notifierIndex in forewarning.notifiers) {
            var notifierId = forewarning.notifiers[notifierIndex];
            var notifier = this.getNotifier(notifierId);
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
    getNodeMetrics: function() {
      var metrics = [];
      if (this.status != null && this.status.metrics != null) {
        metrics = this.status.metrics;
      }

      return metrics;
    },
    addForewarning: function(metric) {
      this.forewarning.edit = false;
      this.forewarning.metric = metric;
      this.showForewarning = true;
    },
    editMetricForewarning: function(metric, forewarningId) {
      this.forewarning.metric = metric;
      this.forewarning.forewarningId = forewarningId;
      this.forewarning.edit = true;
      this.showForewarning = true;
    },
    addNodeForewarning: function() {
      this.forewarning.edit = false;
      this.forewarning.metric = "";
      this.showForewarning = true;
    },
    editNodeForewarning: function(forewarningId) {
      this.forewarning.metric = "";
      this.forewarning.forewarningId = forewarningId;
      this.forewarning.edit = true;
      this.showForewarning = true;
    },
    removeNode: function() {
      if (confirm(" Do you want to remove this node?")) {
        $.ajax({
          url: "/api/node/" + this.code + "/",
          type: "DELETE",
          success: res => {
            this.$router.push("/");
          }
        });
      }
    },
    getNotifier(id) {
      for (var index in this.allNotifiers) {
        var notifier = this.allNotifiers[index];
        if (notifier.id == id) return $.extend(true, {}, notifier);
      }

      return null;
    },
    sync(node) {
      if (this.partialUpdate) {
        if (node.status != null) {
          //only update metric values
          this.status.status = node.status.status;
          this.status.formattedLastUpdateDate = node.status.formattedLastUpdateDate;
          this.status.lastUpdateDate = node.status.lastUpdateDate;

          if (this.status.metrics.length == 0 && node.status.metrics.length != 0) {
            this.status.metrics = node.status.metrics;
          } else {
            for (var index in this.status.metrics) {
              var metricTitle = this.status.metrics[index].title;

              for (var j in node.status.metrics) {
                var waitingUpdateMetric = node.status.metrics[j];
                if (waitingUpdateMetric.title == metricTitle) {
                  this.status.metrics[index].value = waitingUpdateMetric.value;
                  this.status.metrics[index].historyValue = waitingUpdateMetric.historyValue;
                }
              }
            }
          }
        }
      } else {
        this.partialUpdate = true;
        this.status = node.status;
        this.title = node.title;
        this.forewarnings = node.forewarnings;
        this.loading = false;
      }

      if (this.status == null) {
        this.status = {
          status: "down",
          formattedLastUpdateDate: "",
          metrics: []
        };
      }
    },
    initWebsocket() {
      let url = window.location.origin;
      if (process.env.NODE_ENV !== "production") {
        url = "http://localhost:8080";
      }
      this.stompClient = Stomp.over(new SockJS(url + "/ws"));
      this.stompClient.debug = false;
      this.stompClient.connect({}, frame => {
        this.stompClient.subscribe("/topic/sync/node/" + this.code, resp => {
          let node = JSON.parse(resp.body);
          this.loading = false;
          this.sync(node);
        });
        this.stompClient.send("/app/sync/node/" + this.code, {}, "");
      });
    }
  },
  mounted() {
    this.initWebsocket();
    $.get("/api/notifier/", res => {
      this.allNotifiers = res.data;
    });
    $("#forewarningDialog").on("hidden.bs.modal", () => {
      this.showForewarning = false;
    });
  },
  beforeRouteLeave(to, from, next) {
    this.stompClient.disconnect();
    next();
  }
};
</script>

<style scoped>
.modal-dialog {
  width: 80%;
}

.detail .status-widget {
  margin-bottom: 20px;
}

.loading {
  width: 40px;
  margin: auto;
}

.loading img {
  margin-top: 50px;
}

.number {
  font-weight: bold;
  font-size: 90px;
  font-family: 黑体 !important;
  text-align: center;
  padding-top: 60px;
}

.forewarning-icon {
  margin-right: 10px;
}

@media (max-width: 767px) {
  .operations {
    display: none !important;
  }
}
</style>
