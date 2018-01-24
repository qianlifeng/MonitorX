import Vue from "vue";
import Router from "vue-router";
import Index from "@/components/index";
import Notifier from "@/components/notifier";
import Node from "@/components/node";

Vue.use(Router);

export default new Router({
  routes: [
    {
      path: "/",
      component: Index
    },
    {
      path: "/notifier",
      component: Notifier
    },
    {
      path: "/node/:code",
      component: Node
    }
  ]
});
