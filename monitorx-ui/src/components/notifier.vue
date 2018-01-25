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
                <div class="status-widget status-blue">
                    <div class="row">
                        <div class="col-sm-9">
                            <i class="fa fa-3x" :class="[notifier.fontawesomeIcon]"></i><span class="notifier-title">{{notifier.title}}</span>
                        </div>
                        <div class="col-sm-3 operations">
                            <i class="fa fa-trash-o clickable pull-right" @click="removeNotifier(notifier)" data-toggle="tooltip" data-placement="top" title="Remove notifier"></i>
                        </div>
                    </div>
                </div>
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
                                    <select class="form-control" v-model="notifier.notifierCode">
                                        <option :value="notifier.code" :key="notifier.code" v-for="notifier in notifierDefinitions">{{notifier.name}}</option>
                                    </select>
                                </div>
                            </div>
                            <div class="form-group">
                                <div class="col-sm-10 col-sm-offset-2">
                                    <span class="text-muted" v-html="selectedNotifier.description"></span>
                                </div>
                            </div>
                              <div class="form-group" :key="index" v-for="(config,index) in selectedNotifier.config">
                                <label class="col-sm-2 control-label">{{config.name}}</label>
                                <div class="col-sm-10">
                                    <input type="text" class="form-control" v-model="notifier.notifierConfig[config.code]" :placeholder="config.description">
                                </div>
                            </div>
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
export default {
  name: "notifier",
  data() {
    return {
      notifiers: [],
      notifierDefinitions: [],
      showCreateNotifierDialog: false,
      notifier: {
        title: "",
        notifierCode: "",
        notifierConfig: {}
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
  computed: {
    selectedNotifier() {
      let notifiers = this.notifierDefinitions.filter(o => o.code === this.notifier.notifierCode);
      if (notifiers.length > 0) return notifiers[0];

      return {};
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
    },
    loadNotifierDefinitions() {
      $.get("/api/notifier/codes/", res => {
        this.notifierDefinitions = res.data;
        if (this.notifierDefinitions.length > 0) {
          this.notifier.notifierCode = this.notifierDefinitions[0].code;
        }
      });
    }
  },
  mounted() {
    this.loadNotifiers();
    this.loadNotifierDefinitions();
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
