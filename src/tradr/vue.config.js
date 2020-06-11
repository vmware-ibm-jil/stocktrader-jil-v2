/*
       Copyright 2018 IBM Corp All Rights Reserved

   Licensed under the Apache License, Version 2.0 (the "License");
   you may not use this file except in compliance with the License.
   You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

   Unless required by applicable law or agreed to in writing, software
   distributed under the License is distributed on an "AS IS" BASIS,
   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   See the License for the specific language governing permissions and
   limitations under the License.
 */

module.exports = {
  publicPath: "/tradr/",

  configureWebpack: {
    resolve: {
      modules: ["src/assets"],
    },
    resolveLoader: {
      modules: ["src/assets"],
    },
  },

  lintOnSave: true,

  devServer: {
/*     proxy: {
      "/portfolio": {
        target: "https://172.17.76.29:9442",
        changeOrigin: true,
        secure: false
      },
      "/trader": {
        target: "https://172.17.76.29:9443",
        changeOrigin: true,
        secure: false
      },
      "/tradr/login": {
        target: "https://localhost:3000",
        changeOrigin: true,
        secure: false
      }
    }, */
    proxy: {
      "/portfolio": {
        target: process.env.PORTFOLIO_HOST,
        changeOrigin: true,
        secure: false,
      },
      "/trader": {
        target: process.env.TRADER_HOST,
        changeOrigin: true,
        secure: false,
      },
      "/tradr/login": {
        target: process.env.PROXY_HOST,
        changeOrigin: true,
        secure: false,
      },
    },
  },

  productionSourceMap: false,
};
