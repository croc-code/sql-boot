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

import Notifications from 'vue-notification'
import JsonExcel from 'vue-json-excel'
import VueSSE from 'vue-sse'
import Vuetify from 'vuetify'
import VueHighlightJS from 'vue-highlightjs'
import moment from 'moment'


import 'vuetify/dist/vuetify.min.css' // Ensure you are using css-loader


Vue.use(VueResource)
Vue.use(Vuex)
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
    pageCount: 1,
    allConnections: [],
    types: [],
    uri: {
      connections: [],
      type: 'table',
      path: [],
      page: { number: 1, size: 15 },
      orderby: {}
    }
  },
  getters: {
    getPagination: state => {
      if (state.uri.orderby.ord === 'asc') {
        return { rowsPerPage: -1, sortBy: state.uri.orderby.field, descending: false, page: 1, totalItems: 0 }
      } else if (state.uri.orderby.ord === 'desc') {
        return { rowsPerPage: -1, sortBy: state.uri.orderby.field, descending: true, page: 1, totalItems: 0 }
      } else {
        return { rowsPerPage: -1, sortBy: state.uri.orderby.field, descending: null, page: 1, totalItems: 0 }
      }
    },
    getAllConnections: state => {
      return state.allConnections
    },
    getTypes: state => {
      return state.types
    },
    getUri: state => {
      return state.uri
    },
    preparedTypesUri: state => {
      return state.host + '/api/' + state.uri.connections[0] + '/types'
    },
    preparedTypeUri: state => {
      return state.host + '/api/' + state.uri.connections[0] + '/types' + '/' + state.uri.type
    },
    preparedUri: state => {
      return state.host + '/api/' + state.uri.connections.join('|') + '/' + state.uri.type + '/' + state.uri.path +
        '?page=' + state.uri.page.number + ',' + state.uri.page.size +
        (state.uri.orderby.field ? ('&orderby=' + state.uri.orderby.field + '-' + state.uri.orderby.ord) : '')
    },
    getSimpleUri: state => {
      return state.uri.connections.join('|') + '/' + state.uri.type + '/' + (state.uri.path ? state.uri.path : '') +
        '?page=' + state.uri.page.number + ',' + state.uri.page.size +
        (state.uri.orderby.field ? ('&orderby=' + state.uri.orderby.field + '-' + state.uri.orderby.ord) : '')
    },
    getPage: state => {
      return state.uri.page
    },
    getPageCount: state => {
      return state.pageCount
    },
    getPageNumber: state => {
      return state.uri.page.number
    },
    getConnections: state => {
      return state.uri.connections
    }
  },
  mutations: {
    setPagination (state, pagination) {
      if (pagination.descending === false) {
        state.uri.orderby = {field: pagination.sortBy, ord: "asc"}
      } else if (pagination.descending === true){
        state.uri.orderby = {field: pagination.sortBy, ord: "desc"}
      } else {
        state.uri.orderby = {}
      }
    },
    setAllConnections (state, connections) {
      state.allConnections = connections
    },
    setTypes (state, types) {
      state.types = types
    },
    setUri (state, uri) {
      state.uri = uri
    },
    skipObjectUri (state, type) {
      const c = state.uri.connections
      state.pageCount = 1

      const meta = state.types.find( v => { return v.name === type } )
      const defaultSort = meta.metadata.filter(v => { return v.properties.sort }).map(v => v.name)[0]
      if (defaultSort) {
        const sortType = meta.metadata.filter(v => { return v.properties.sort })[0].properties.sort
        state.uri = { connections: c, type: type, path: [], orderby: { field: defaultSort, ord: sortType }, page: { number: 1, size: 15 }}
      } else {
        state.uri = { connections: c, type: type, path: [], orderby: {}, page: { number: 1, size: 15 }}
      }

    },
    setType (state, typeName) {
      state.uri.type = typeName
    },
    setPath (state, path) {
      state.uri.path = path
    },
    setSort (state, sort) {
      state.uri.orderby = sort
    },
    nextPage (state) {
      state.uri.page.number++
    },
    prevPage (state) {
      state.uri.page.number--
    },
    pageNumber (state, number) {
      state.uri.page.number = number
    },
    setPage (state, val) {
      state.uri.page.number = parseInt(val.split(',')[0])
      state.uri.page.size = parseInt(val.split(',')[1])
    },
    setPageCount (state, count) {
      state.pageCount = count
    },
    increasePageCount (state) {
      state.pageCount++
    },
    changeUri (state, uriString) {
      const parse = require('url-parse')
      const url = parse(uriString, true)
      const path = url.pathname.split('/').filter(v => v);
      const rawConnections = path[0]
      if (rawConnections) {
        state.uri.connections = path[0].split('|')
        state.uri.type = path[1]
        if (path[2]) {
          state.uri.path = path[2]
        } else {
          state.uri.path = ""
        }
        if (url.query.page) {
          state.uri.page.number = parseInt(url.query.page.split(',')[0])
          state.uri.page.size = parseInt(url.query.page.split(',')[1])
        }
        if (url.query.orderby) {
          const field = url.query.orderby.split('-')[0]
          const ord = url.query.orderby.split('-')[1]
          state.uri.orderby = { field: field, ord: ord }
        } else {
          state.uri.orderby = {}
        }
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
  router,
  created: function() {
    this.$http.get(this.$store.state.host + '/endpoints').then(
      response => {
        this.$store.commit('setAllConnections', response.body)
      }
    )
  },
  computed: {
    getAllConnections() {
      return this.$store.getters.getAllConnections
    }
  },
  watch: {
    getAllConnections: {
      handler(newVal, oldVal) {
        this.$http.get(this.$store.state.host + '/api/' + newVal[0].name + '/types').then(
          response => {
            this.$store.commit('setTypes', response.body)
          }
        )
      }
    }
  }
}).$mount('#app')
