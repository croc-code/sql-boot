/*
 * BSD 3-Clause License
 *
 * Copyright (c) 2019, CROC Inc.
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 * 1. Redistributions of source code must retain the above copyright notice, this
 *    list of conditions and the following disclaimer.
 *
 * 2. Redistributions in binary form must reproduce the above copyright notice,
 *    this list of conditions and the following disclaimer in the documentation
 *    and/or other materials provided with the distribution.
 *
 * 3. Neither the name of the copyright holder nor the names of its
 *    contributors may be used to endorse or promote products derived from
 *    this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE
 * FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL
 * DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
 * SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER
 * CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY,
 * OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE
 * OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

// The Vue build version to load with the `import` command
// (runtime-only or standalone) has been set in webpack.base.conf with an alias.
import Vue from 'vue'
import Vuex from 'vuex'
import App from './App'
import router from './router'
import VueResource from 'vue-resource'
import VueTreeNavigation from 'vue-tree-navigation'

import Notifications from 'vue-notification'
import JsonExcel from 'vue-json-excel'
import VueSSE from 'vue-sse'
import Vuetify from 'vuetify'
import VueHighlightJS from 'vue-highlightjs'
import moment from 'moment'


import 'vuetify/dist/vuetify.min.css' // Ensure you are using css-loader


Vue.use(VueResource)
Vue.use(Vuex)
Vue.use(VueTreeNavigation)
Vue.use(Notifications)
Vue.component('downloadExcel', JsonExcel)
Vue.use(VueSSE)
Vue.use(Vuetify)
Vue.use(VueHighlightJS)


Vue.config.productionTip = false

Vue.filter('formatDate', function(value) {
  if (value) {
    return moment(String(value)).format('DD.MM.YYYY HH:mm:ss')
  }
});

const store = new Vuex.Store({
  state: {
    host: '',
    // host: 'http://localhost:8007/',
    newConnections: [],
    type: 'table',
    path: [''],
    page: { 'number': 1, 'size': 15, 'count': 1 },
    orderby: {},
    where: '',
    cache: false
  },
  getters: {
    preparedTypesUri: state => {
      return state.host + '/api/' + state.newConnections[0] + '/types'
    },
    preparedTypeUri: state => {
      return state.host + '/api/' + state.newConnections[0] + '/types' + '/' + state.type
    },
    preparedUri: state => {
      return state.host + '/api/' + state.newConnections.join('|') + '/' + state.type + '/' + state.path +
        '?page=' + state.page.number + ',' + state.page.size +
        (state.orderby.field ? ('&orderby=' + state.orderby.field + '-' + state.orderby.ord) : '')
    },
    getSimpleUri: state => {
      return state.newConnections.join('|') + '/' + state.type + '/' + (state.path ? state.path : '') +
        '?page=' + state.page.number + ',' + state.page.size +
        (state.orderby.field ? ('&orderby=' + state.orderby.field + '-' + state.orderby.ord) : '')
    },
    getPage: state => {
      return state.page
    },
    getPageCount: state => {
      return state.page.count
    },
    getPageNumber: state => {
      return state.page.number
    },
    getConnections: state => {
      return state.newConnections
    }
  },
  mutations: {
    setNewConnection (state, connectionName) {
      state.newConnections = connectionName
    },
    setType (state, typeName) {
      state.type = typeName
    },
    setPath (state, path) {
      state.path = path
    },
    setSort (state, sort) {
      state.orderby = sort
    },
    nextPage (state) {
      state.page.number++
    },
    prevPage (state) {
      state.page.number--
    },
    pageNumber (state, number) {
      state.page.number = number
    },
    setPage (state, val) {
      state.page.number = parseInt(val.split(',')[0])
      state.page.size = parseInt(val.split(',')[1])
    },
    setPageCount (state, count) {
      state.page.count = count
    },
    increasePageCount (state) {
      state.page.count++
    },
    changeUri (state, uriString) {
      const parse = require('url-parse')
      const url = parse(uriString, true)
      var path = url.pathname.split('/').filter(v => v)
      state.newConnections = path[0].split('|')
      state.type = path[1]
      if (path[2]) {
        state.path = path[2]
      } else {
        state.path = ""
      }
      if (url.query.page) {
        state.page.number = parseInt(url.query.page.split(',')[0])
        state.page.size = parseInt(url.query.page.split(',')[1])
      }
      if (url.query.orderby) {
        let field = url.query.orderby.substring(0, url.query.orderby.indexOf('-'))
        state.orderby = {field: field, ord: 'desc'}
      } else {
        state.orderby = {}
      }
    }
  }
})

/* eslint-disable no-new */
new Vue({
  el: '#app',
  components: { App },
  template: '<App/>',
  store,
  router
}).$mount('#app')
