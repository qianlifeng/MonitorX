var Vue = require('vue')
var Node = require('./components/node.vue')

new Vue({
    el: 'body',
    components: {
        node: Node
    }
});