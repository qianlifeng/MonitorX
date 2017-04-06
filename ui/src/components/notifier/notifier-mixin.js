export default {
    props: ['notifier'],
    methods: {
        removeNotifier: function () {
            this.$parent.removeNotifier(this.notifier);
        }
    }
}