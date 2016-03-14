module.exports = {
    entry: './app.js',
    output: {
        path: __dirname,
        filename: 'build.js'
    },
    module: {
        loaders: [
            {
                test: /\.vue$/,
                loader: 'vue'
            }
        ]
    }
};