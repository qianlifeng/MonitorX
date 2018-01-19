<template>
    <div class="index">
        <div class="row">
            <div class="col-sm-9">
                <h1 class="title">Nodes</h1>
            </div>
            <div class="col-sm-3 add-new">
                <a href="javascript:;" @click="showCreateNodeDialog = true" class="dropdown-toggle pull-right" role="button" data-toggle="tooltip"
                   data-placement="top" title="Add node"><span class="fa fa-plus"></span></a>
            </div>
        </div>

        <div class="loading" v-if="loading">
            <img src="../assets/loading.gif">
        </div>
        <div v-else>
            <div class="row" :key="i" v-for="(nodeGroup,i) in nodeGroups">
                <div>
                    <div v-if="nodeGroup.group" class="group-name">{{nodeGroup.group}}</div>
                    <div class="col-sm-12 col-lg-6 col-md-6" :key="index" v-for="(node,index) in nodeGroup.nodes">
                        <div class="status-widget clickable" @click="navigate(node.code)" :class="[nodeStatus(node)]">
                            <div class="row">
                                <div class="col-sm-6">
                                    <span class="title" v-text="node.title"></span>
                                </div>
                            </div>
                            <div class="lastupdate">
                                <i class="fa fa-clock-o"></i> <span>{{node.status !== null ? node.status.formattedLastUpdateDate : ""}}</span>
                            </div>
                            <div class="clearfix"></div>
                        </div>
                    </div>
                </div>
            </div>
        </div>

        <div class="modal fade" id="createNodeDialog" role="dialog">
            <div class="modal-dialog" role="document">
                <div class="modal-content">
                    <div class="modal-header">
                        <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
                        <h4 class="modal-title">Add Node</h4>
                    </div>
                    <div class="modal-body">
                        <form class="form-horizontal">
                            <div class="form-group">
                                <label class="col-sm-2 control-label">Code</label>
                                <div class="col-sm-10">
                                    <input type="text" class="form-control" v-model="node.code">
                                </div>
                            </div>
                            <div class="form-group">
                                <label class="col-sm-2 control-label">Group</label>
                                <div class="col-sm-10">
                                    <input type="text" class="form-control" v-model="node.group" placeholder="You can leave it eampty if you don't want group your nodes">
                                </div>
                            </div>
                            <div class="form-group">
                                <label class="col-sm-2 control-label">Title</label>
                                <div class="col-sm-10">
                                    <input type="text" class="form-control" v-model="node.title">
                                </div>
                            </div>
                            <div class="form-group">
                                <label class="col-sm-2 control-label">Sync</label>
                                <div class="col-sm-10">
                                    <select class="form-control" v-model="node.sync">
                                        <option :value="sync.code" :key="sync.code" v-for="sync in syncs">{{sync.name}}</option>
                                    </select>
                                </div>
                            </div>
                            <div>
                                <div class="form-group">
                                    <div class="col-sm-10 col-sm-offset-2">
                                        <span class="text-muted">{{selectedSync.description}}</span>
                                    </div>
                                </div>
                                <div class="form-group" :key="index" v-for="(config,index) in selectedSync.config">
                                    <label class="col-sm-2 control-label">{{config.name}}</label>
                                    <div class="col-sm-10">
                                        <input type="text" class="form-control" v-model="node.syncConfig[config.code]" :placeholder="config.description">
                                    </div>
                                </div>
                            </div>
                        </form>
                    </div>
                    <div class="modal-footer">
                        <button type="button" @click="addNode" class="btn btn-success">Add</button>
                    </div>
                </div>
            </div>
        </div>
    </div>
</template>

<script>
export default {
  name: "index",
  data() {
    return {
      intervalInstance: null,
      loading: true,
      nodes: [],
      showCreateNodeDialog: false,
      syncs: [],
      node: {
        code: "",
        group: "",
        title: "",
        sync: "sync-push",
        syncConfig: {}
      }
    };
  },
  computed: {
    selectedSync() {
      let syncs = this.syncs.filter(o => o.code === this.node.sync);
      if (syncs.length > 0) return syncs[0];

      return {};
    },
    nodeGroups() {
      let groups = [
        {
          group: "",
          nodes: []
        }
      ];
      this.nodes.forEach(o => {
        if (o.group == null || typeof o.group === "undefined") {
          o.group = "";
        }

        let matchGroups = groups.filter(j => j.group === o.group);
        if (matchGroups.length > 0) {
          matchGroups[0].nodes.push(o);
        } else {
          groups.push({
            group: o.group,
            nodes: [o]
          });
        }
      });

      return groups;
    }
  },
  watch: {
    showCreateNodeDialog: function(newVal) {
      if (newVal) {
        $("#createNodeDialog").modal("show");
      } else {
        $("#createNodeDialog").modal("hide");
      }
    }
  },
  methods: {
    navigate(nodeCode) {
      this.$router.push({ path: "/node/" + nodeCode });
    },
    nodeStatus(node) {
      if (node.status === null) return "status-down";
      if (node.status.status === "up") return "status-up";

      return "status-down";
    },
    addNode: function() {
      $.post(
        "/api/node/",
        {
          node: JSON.stringify(this.node)
        },
        res => {
          if (res.success) {
            this.loadNodes();
            this.showCreateNodeDialog = false;
          }
        }
      );
    },
    loadNodes() {
      $.get("/api/node/", res => {
        this.loading = false;
        this.nodes = res.data;
      });
    },
    loadSyncTypes() {
      $.get("/api/sync/", res => {
        this.syncs = res.data;
      });
    }
  },
  mounted() {
    this.loadNodes();
    this.loadSyncTypes();
    $("#createNodeDialog").on("hidden.bs.modal", () => {
      this.showCreateNodeDialog = false;
    });
    this.intervalInstance = setInterval(this.loadNodes, 1000);
  },
  beforeRouteLeave(to, from, next) {
    if (this.intervalInstance != null) {
      clearInterval(this.intervalInstance);
    }

    next();
  }
};
</script>

<style scoped>
.modal-dialog {
  width: 80%;
}

.loading {
  text-align: center;
}

.loading img {
  margin-top: 20px;
  width: 46px;
}

.group-name {
  margin-left: 15px;
  font-size: 20px;
  margin-top: 30px;
}
</style>
