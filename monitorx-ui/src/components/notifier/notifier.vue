<template>
    <div class="notifier">
        <div class="row">
            <div class="col-sm-9">
                <h1 class="title">Notifiers</h1>
            </div>
            <div class="col-sm-3 add-new">
                <a href="javascript:;" class="dropdown-toggle pull-right" @click="showCreateNotifierDialog = true" data-toggle="tooltip" data-placement="top" title="Add notifier">
                    <span class="fa fa-plus"></span>
                </a>
            </div>
        </div>

        <div class="row">
            <div class="col-sm-12 col-lg-12 col-md-12" v-for="notifier in notifiers" :key="notifier.title">
                <component :is="'notifier-' + notifier.type" :notifier="notifier"></component>
            </div>
        </div>

        <div class="modal fade" id="createNotifierDialog" role="dialog">
            <div class="modal-dialog" role="document">
                <div class="modal-content">
                    <div class="modal-header">
                        <button type="button" class="close" data-dismiss="modal" aria-label="Close">
                            <span aria-hidden="true">&times;</span>
                        </button>
                        <h4 class="modal-title">Add Notifier</h4>
                    </div>
                    <div class="modal-body">
                        <form class="form-horizontal">
                            <div class="form-group">
                                <label class="col-sm-2 control-label">Title</label>
                                <div class="col-sm-10">
                                    <input type="text" class="form-control" v-model="notifier.title">
                                </div>
                            </div>
                            <div class="form-group">
                                <label class="col-sm-2 control-label">Type</label>
                                <div class="col-sm-10">
                                    <select class="form-control" v-model="notifier.type">
                                        <option value="wechat">Wechat</option>
                                        <option value="email">Email</option>
                                        <option value="callback">Callback</option>
                                    </select>
                                </div>
                            </div>
                            <component :is="'notifier-' + notifier.type+ '-config'" :config="notifier.config"></component>
                        </form>
                    </div>
                    <div class="modal-footer">
                        <button type="button" @click="addNotifier" class="btn btn-success">Add</button>
                        <button type="button" class="btn btn-default" @click="testNotifier">Test</button>
                    </div>
                </div>
            </div>
        </div>
    </div>
</template>

<script>
import NotifierEmail from "./email/notifier-email.vue";
import NotifierEmailConfig from "./email/notifier-email-config.vue";
import NotifierWechat from "./wechat/notifier-wechat.vue";
import NotifierWechatConfig from "./wechat/notifier-wechat-config.vue";
import NotifierCallback from "./callback/notifier-callback.vue";
import NotifierCallbackConfig from "./callback/notifier-callback-config.vue";

export default {
  name: "notifier",
  components: {
    "notifier-email": NotifierEmail,
    "notifier-email-config": NotifierEmailConfig,
    "notifier-wechat": NotifierWechat,
    "notifier-wechat-config": NotifierWechatConfig,
    "notifier-callback": NotifierCallback,
    "notifier-callback-config": NotifierCallbackConfig
  },
  data() {
    return {
      notifiers: [],
      showCreateNotifierDialog: false,
      notifier: {
        title: "",
        type: "wechat",
        config: {}
      }
    };
  },
  watch: {
    showCreateNotifierDialog: function(newVal) {
      if (newVal) {
        $("#createNotifierDialog").modal("show");
      } else {
        $("#createNotifierDialog").modal("hide");
      }
    }
  },
  methods: {
    testNotifier() {
      $.post(
        "/api/notifier/testsend/",
        {
          notifier: JSON.stringify(this.notifier),
          title: "Test Title From MonitorX",
          body: "This is test message body"
        },
        function(res) {
          if (res.success) {
            alert("Success");
          }
        }
      );
    },
    addNotifier() {
      $.post(
        "/api/notifier/",
        {
          notifier: JSON.stringify(this.notifier)
        },
        res => {
          if (res.success) {
            this.loadNotifiers();
            this.showCreateNotifierDialog = false;
          }
        }
      );
    },
    removeNotifier(notifier) {
      if (!confirm("Do you want to remove this notifier? All related notify will be removed too")) return;

      $.ajax({
        url: "/api/notifier/" + notifier.id + "/",
        type: "DELETE"
      }).done(res => {
        this.loadNotifiers();
      });
    },
    loadNotifiers() {
      $.get("/api/notifier/", res => {
        this.notifiers = res.data;
      });
    }
  },
  mounted() {
    this.loadNotifiers();
    $("#createNotifierDialog").on("hidden.bs.modal", () => {
      this.showCreateNotifierDialog = false;
    });
  }
};
</script>

<style scoped>
.notifier-title {
  font-size: 3em;
  margin-left: 10px;
}

@media (max-width: 767px) {
  .key,
  .add-new,
  .operations {
    display: none !important;
  }
}
</style>