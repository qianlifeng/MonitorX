<template>
    <form class="form-horizontal forewarning">
        <div class="form-group">
            <label class="col-sm-2 control-label">Name</label>

            <div class="col-sm-10">
                <input type="text" class="form-control" v-model="title">
            </div>
        </div>
        <div class="form-group">
            <label class="col-sm-2 control-label">Node</label>

            <div class="col-sm-10">
                <p class="form-control-static" v-html="node"></p>
            </div>
        </div>
        <div class="form-group" v-show="metric !== ''">
            <label class="col-sm-2 control-label">Metric</label>

            <div class="col-sm-10">
                <p class="form-control-static">{{metric}}</p>
            </div>
        </div>
        <div class="form-group">
            <label class="col-sm-2 control-label">Snippet Context</label>

            <div class="col-sm-10">
                <p class="form-control-static">{{context}}</p>
            </div>
        </div>
        <div class="form-group">
            <label class="col-sm-2 control-label">Snippet</label>

            <div class="col-sm-10">
                <textarea class="form-control" rows="5" v-model="snippet" placeholder="Javascript Snippet"></textarea>
            </div>
        </div>
        <div class="form-group">
            <div class="col-sm-1 col-sm-offset-2">
                <button type="button" v-on:click="evaluate()" class="btn btn-default">Evaluate</button>
            </div>
            <div class="col-sm-9">
                <p class="form-control-static" v-html="evaluateResult"></p>
            </div>
        </div>
        <div class="form-group">
            <label class="col-sm-2 control-label">Notifiers</label>

            <div class="col-sm-10">
                <select multiple class="form-control" v-model="notifiers">
                    <option :key="notifier.id" :value="notifier.id" v-for="notifier in availableNotifiers">{{notifier.title}}</option>
                </select>
            </div>
        </div>
        <div class="form-group">
            <label class="col-sm-2 control-label">Notify type</label>
            <div class="col-sm-10">
                <select class="form-control" v-model="forewarningCode">
                    <option :value="rule.code" :key="rule.code" v-for="rule in forewarningRules">{{rule.name}}</option>
                </select>
            </div>
        </div>
        <div class="form-group" v-if="selectedForewarning.description">
            <label class="col-sm-2 control-label"></label>
            <div class="col-sm-10" v-html="selectedForewarning.description"></div>
        </div>
        <div class="form-group" :key="index" v-for="(config,index) in selectedForewarning.config">
            <label class="col-sm-2 control-label">{{config.name}}</label>
            <div class="col-sm-10">
                <input type="text" class="form-control" v-model="forewarningConfig[config.code]" :placeholder="config.description">
            </div>
        </div>
        <div class="form-group">
            <label class="col-sm-2 control-label">Notify message</label>
            <div class="col-sm-10">
                <textarea class="form-control" rows="5" v-model="msg" :placeholder="'Notify message support snippet context, please use ' + expression + 'to eval the real value'"></textarea>
            </div>
        </div>
        <div class="form-group">
            <div class="col-sm-1 col-sm-offset-2">
                <button type="button" @click="previewMsg()" class="btn btn-default">Preview</button>
            </div>
            <div class="col-sm-9">
                <p class="form-control-static" v-html="realMsg"></p>
            </div>
        </div>
        <div class="form-group">
            <label class="col-sm-2 control-label">Recovery message</label>

            <div class="col-sm-10">
                <textarea class="form-control" rows="5" v-model="recoveredMsg" placeholder="Leave this empty if you don't want recovery notification"></textarea>
            </div>
        </div>
        <div class="form-group">
            <div class="col-sm-1 col-sm-offset-2">
                <button type="button" @click="previewRecoveredMsg()" class="btn btn-default">Preview</button>
            </div>
            <div class="col-sm-9">
                <p class="form-control-static" v-html="realRecoveredMsg"></p>
            </div>
        </div>
    </form>
</template>

<script>
import Vue from "vue";

export default {
  props: {
    edit: Boolean,
    node: String,
    metric: {
      type: String,
      default: function() {
        return "";
      }
    },
    forewarningId: String
  },
  data() {
    return {
      title: "",
      context: "",
      snippet: "",
      forewarningCode: "",
      notifiers: [],
      forewarningRules: [],
      forewarningConfig: {},
      availableNotifiers: [],
      msg: "",
      realMsg: "",
      recoveredMsg: "",
      realRecoveredMsg: "",
      evaluateResult: "",
      expression: "{{expression}}"
    };
  },
  watch: {
    forewarningId: function(val) {
      if (val) {
        this.loadEditingForewarning();
      }
    },
    metric: function(val) {
      this.loadContext();
    }
  },
  computed: {
    selectedForewarning() {
      let forewarnings = this.forewarningRules.filter(o => o.code === this.forewarningCode);
      if (forewarnings.length > 0) return forewarnings[0];

      return {};
    }
  },
  methods: {
    loadContext() {
      $.get("/api/forewarning/context/?node=" + this.node + "&metric=" + this.metric, res => {
        this.context = res.data;
      });
    },
    loadEditingForewarning() {
      $.get("/api/forewarning/?node=" + this.node + "&forewarning=" + this.forewarningId, res => {
        var existingForwarning = res.data;
        this.snippet = existingForwarning.snippet;
        this.msg = existingForwarning.msg;
        this.recoveredMsg = existingForwarning.recoveredMsg;
        this.notifiers = existingForwarning.notifiers;
        this.forewarningCode = existingForwarning.forewarningCode;
        this.forewarningConfig = existingForwarning.forewarningConfig;
        this.title = existingForwarning.title;
      });
    },
    addForewarning: function() {
      if (this.edit) {
        $.post(
          "/api/forewarning/edit/",
          {
            title: this.title,
            node: this.node,
            metric: this.metric,
            snippet: this.snippet,
            forewarningCode: this.forewarningCode,
            forewarningConfig: JSON.stringify(this.forewarningConfig),
            notifiers: this.notifiers,
            msg: this.msg,
            recoveredMsg: this.recoveredMsg,
            forewarningId: this.forewarningId
          },
          res => {
            if (res.success) {
              this.$emit("done");
              alert("Success");
            } else {
              alert(res.message);
            }
          }
        );
      } else {
        $.post(
          "/api/forewarning/",
          {
            title: this.title,
            node: this.node,
            metric: this.metric,
            snippet: this.snippet,
            forewarningCode: this.forewarningCode,
            forewarningConfig: JSON.stringify(this.forewarningConfig),
            notifiers: this.notifiers,
            msg: this.msg,
            recoveredMsg: this.recoveredMsg
          },
          res => {
            if (res.success) {
              this.$emit("done");
              alert("Success");
            } else {
              alert(res.message);
            }
          }
        );
      }
    },
    removeForewarning: function() {
      if (confirm("Do you want to remove this forewarning?")) {
        $.post(
          "/api/forewarning/delete/",
          {
            title: this.title,
            node: this.node,
            metric: this.metric,
            snippet: this.snippet,
            forewarningCode: this.forewarningCode,
            forewarningConfig: JSON.stringify(this.forewarningConfig),
            notifiers: this.notifiers,
            msg: this.msg
          },
          res => {
            if (res.success) {
              this.$emit("done");
              alert("Success");
            } else {
              alert(res.message);
            }
          }
        );
      }
    },
    evaluate: function() {
      $.post(
        "/api/forewarning/evaluate/",
        {
          node: this.node,
          metric: this.metric,
          snippet: this.snippet
        },
        res => {
          if (res.success) {
            this.evaluateResult = res.data;
          }
        }
      );
    },
    previewMsg: function() {
      $.post(
        "/api/forewarning/previewMsg/",
        {
          node: this.node,
          metric: this.metric,
          msg: this.msg
        },
        res => {
          if (res.success) {
            this.realMsg = res.data;
          }
        }
      );
    },
    previewRecoveredMsg: function() {
      $.post(
        "/api/forewarning/previewMsg/",
        {
          node: this.node,
          metric: this.metric,
          msg: this.recoveredMsg
        },
        res => {
          if (res.success) {
            this.realRecoveredMsg = res.data;
          }
        }
      );
    }
  },
  mounted() {
    $.get("/api/forewarning/list/", res => {
      this.forewarningRules = res.data;
      if (this.forewarningRules.length > 0) {
        this.forewarningCode = this.forewarningRules[0].code;
      }
    });
    $.get("/api/notifier/", res => {
      this.availableNotifiers = res.data;
    });
    this.loadContext();
  }
};
</script>

<style>
.forewarning {
  max-height: 700px;
  overflow: auto;
}
</style>
