const path = require('path');
const webpack = require('webpack');
require('dotenv').config();
const HtmlWebpackPlugin = require('html-webpack-plugin');

module.exports = {
  entry: {
    main: './src/scripts/index.js',
    cadastro: './src/scripts/cadastro.js',
    sala: './src/scripts/sala.js',
    'listagem-salas': './src/scripts/listagem-salas.js',
  },
  output: {
    filename: '[name].js',
    path: path.resolve(__dirname, 'dist'),
    clean: true,
  },
  plugins: [
    new webpack.DefinePlugin({
      'process.env.API_URL': JSON.stringify(process.env.API_URL),
    }),
    new HtmlWebpackPlugin({
      template: './src/index.html',
      filename: 'index.html',
      chunks: ['main'],
    }),
    new HtmlWebpackPlugin({
      template: './src/listagem-salas.html',
      filename: 'listagem-salas.html',
      chunks: ['listagem-salas'],
    }),
    new HtmlWebpackPlugin({
      template: './src/cadastro.html',
      filename: 'cadastro.html',
      chunks: ['cadastro'],
    }),
    new HtmlWebpackPlugin({
      template: './src/sala.html',
      filename: 'sala.html',
      chunks: ['sala'],
    }),
  ],
  module: {
    rules: [
      {
        test: /\.css$/,
        use: ['style-loader', 'css-loader'],
      },
      {
        test: /\.(png|jpe?g|gif)$/i,
        type: 'asset/resource',
      },
    ],
  },
  mode: 'production',
};
