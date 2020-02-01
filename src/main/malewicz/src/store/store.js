import Vue from 'vue'
import Vuex from 'vuex'

Vue.use(Vuex)

export const store = new Vuex.Store({
  // strict: true,
  state: {
    host: '',
    // host: 'http://127.0.0.1:8007',
    // host: 'http://81.23.10.106:8007/',
    pageCount: 1,
    allConnections: [],
    currentType: {},
    types: [],
    uri: {
      connections: [],
      type: '',
      path: [],
      page: {number: 1, size: 15},
      orderby: {},
      filter: {}
    }
  },
  getters: {
    getHost: state => {
      return state.host
    },
    getPagination: state => {
      if (state.uri.orderby.ord === 'asc') {
        return {rowsPerPage: -1, sortBy: state.uri.orderby.field, descending: false, page: 1, totalItems: 0}
      } else if (state.uri.orderby.ord === 'desc') {
        return {rowsPerPage: -1, sortBy: state.uri.orderby.field, descending: true, page: 1, totalItems: 0}
      } else {
        return {rowsPerPage: -1, sortBy: state.uri.orderby.field, descending: null, page: 1, totalItems: 0}
      }
    },
    getTypes: state => {
      return state.types
    },
    getCurrentType: state => {
      return state.currentType
    },
    getType: state => {
      return state.uri.types
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
      return encodeURI(state.host + '/api/' + state.uri.connections.join('|') + '/' + state.uri.type + '/' + state.uri.path +
          '?page=' + state.uri.page.number + ',' + state.uri.page.size +
          (state.uri.orderby.field ? ('&orderby=' + state.uri.orderby.field + '-' + state.uri.orderby.ord) : '') +
          (state.uri.filter ? ('&filter=' + JSON.stringify(state.uri.filter)) : ''))
    },
    getSimpleUri: state => {
      if (state.uri.type) {
        return state.uri.connections.join('|') + '/' + state.uri.type + '/' + (state.uri.path ? state.uri.path : '') +
            '?page=' + state.uri.page.number + ',' + state.uri.page.size +
            (state.uri.orderby.field ? ('&orderby=' + state.uri.orderby.field + '-' + state.uri.orderby.ord) : '') +
            (state.uri.filter ? ('&filter=' + JSON.stringify(state.uri.filter)) : '')
      } else {
        return state.uri.connections.join('|')
      }
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
    getAllConnections: state => {
      return state.allConnections
    },
    getConnections: state => {
      return state.uri.connections
      /*let defaultConnection = state.allConnections.find(v => {
        return v.properties.default === true
      })
      if (state.uri.connections.length === 0) {
        return Object.values(defaultConnection)
      } else {
        return state.uri.connections
      }*/
    },
    getFilter: state => {
      return state.uri.filter
    }
  },
  mutations: {
    setPagination (state, pagination) {
      if (pagination.descending === false) {
        state.uri.orderby = {field: pagination.sortBy, ord: 'asc'}
      } else if (pagination.descending === true) {
        state.uri.orderby = {field: pagination.sortBy, ord: 'desc'}
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
    setConnections (state, connections) {
      state.uri.connections = connections
    },
    skipObjectUri (state, type) {
      const c = state.uri.connections
      state.pageCount = 1

      const meta = state.types.find(v => {
        return v.name === type
      })
      const defaultSort = meta.metadata.filter(v => {
        return v.sort
      }).map(v => v.name)[0]
      if (defaultSort) {
        const sortType = meta.metadata.filter(v => {
          return v.sort
        })[0].sort
        state.uri = {
          connections: c,
          type: type,
          path: [],
          orderby: {field: defaultSort, ord: sortType},
          page: {number: 1, size: 15},
          filter: {}
        }
      } else {
        state.uri = {connections: c, type: type, path: [], orderby: {}, page: {number: 1, size: 15}, filter: {}}
      }
    },
    setType (state, typeName) {
      state.uri.type = typeName
    },
    setCurrentType (state, currentType) {
      state.currentType = currentType
    },
    setPath (state, path) {
      state.uri.path = path
    },
    setSort (state, sort) {
      state.uri.orderby = sort
    },
    setFilter (state, filter) {
      state.uri.filter = filter
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
      const path = url.pathname.split('/').filter(v => v)
      const connections = path[0]
      if (connections) {
        state.uri.connections = connections.split('|')
        state.uri.type = path[1]
        if (path[2]) {
          state.uri.path = path[2]
        } else {
          state.uri.path = ''
        }
        if (url.query.page) {
          state.uri.page.number = parseInt(url.query.page.split(',')[0])
          state.uri.page.size = parseInt(url.query.page.split(',')[1])
        }
        if (url.query.orderby) {
          const field = url.query.orderby.split('-')[0]
          const ord = url.query.orderby.split('-')[1]
          state.uri.orderby = {field: field, ord: ord}
        } else {
          state.uri.orderby = {}
        }
        if (url.query.filter) {
          state.uri.filter = JSON.parse(url.query.filter)
        } else {
          state.uri.filter = {}
        }
      }
    }
  }
})