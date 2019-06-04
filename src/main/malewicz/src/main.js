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

const store = new Vuex.Store({
  state: {
    host: '',
    // host: 'http://localhost:8007/',
    connections: '',
    type: 'table',
    path: [''],
    page: { 'number': 1, 'size': 15 },
    orderby: {},
    where: '',
    cache: false
  },
  getters: {
    preparedTypesUri: state => {
      return state.host + '/api/' + state.connections + '/types'
    },
    preparedTypeUri: state => {
      return state.host + '/api/' + state.connections + '/types' + '/' + state.type
    },
    preparedUri: state => {
      return state.host + '/api/' + state.connections + '/' + state.type + '/' + state.path +
        '?page=' + state.page.number + ',' + state.page.size +
        (state.orderby.field ? ('&orderby=' + state.orderby.field + '-' + state.orderby.ord) : '')
    },
    getSimpleUri: state => {
      return state.connections + '/' + state.type + '/' + (state.path ? state.path : '') +
        '?page=' + state.page.number + ',' + state.page.size +
        (state.orderby.field ? ('&orderby=' + state.orderby.field + '-' + state.orderby.ord) : '')
    }
  },
  mutations: {
    setConnection (state, connectionName) {
      state.connections = connectionName
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
    changeUri (state, uriString) {
      const parse = require('url-parse')
      const url = parse(uriString, true)
      var path = url.pathname.split('/').filter(v => v)
      state.connections = path[0]
      state.type = path[1]
      if (path[2]) {
        state.path = path[2]
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
