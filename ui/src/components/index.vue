<template>
    <div class="index">
          <div class="row">
              <div class="col-sm-9">
                  <h1 class="title">Nodes</h1>
              </div>
              <div class="col-sm-3 add-new">
                  <a href="/node/new/" class="dropdown-toggle pull-right" role="button" data-toggle="tooltip" data-placement="top" title="Add node"><span class="fa fa-plus"></span></a>
              </div>
          </div>

          <div class="row">
              <div class="loading" v-if="loading">
                  <img src="../assets/loading.gif">
              </div>
              <div v-else>
                  <div class="col-sm-12 col-lg-6 col-md-6" v-for="node in nodes">
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
</template>

<script>
export default {
  name: 'index',
  data () {
    return {
      loading: true,
      nodes: []
    }
  },
  methods: {
    nodeStatus(node){
      if(node.status === null) return "status-down";
      if(node.status.status === "up") return "status-up";

      return "status-down";
    }
  },
  mounted () {
      $.get("/api/node/", res => {
          this.loading = false;
          this.nodes = res.data;
      });
  }
}
</script>

<style scoped >
  .loading {
      text-align: center;
  }
  .loading img {
      margin-top: 20px;
      width: 46px;
  }
</style>
