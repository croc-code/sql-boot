<template xmlns:v-slot="http://www.w3.org/1999/XSL/Transform">
  <div>
    <v-toolbar>
      <v-toolbar-title class="text" v-if="meta.properties">{{ meta.properties.title }}</v-toolbar-title>
      <v-spacer></v-spacer>
      <ColumnsComponent :meta="meta"/>
      <CodeViewComponent :code="meta.query"/>
      <FilterComponent :meta="meta"/>
      <v-btn @click="call()" icon>
        <v-icon>fa-sync-alt</v-icon>
      </v-btn>
    </v-toolbar>

    <v-data-table
      :headers="defaultMeta"
      :items="items"
      :pagination.sync="uriPagination"
      :loading="isLoading"
      no-data-text="No data available"
      hide-actions
      class="elevation-1">
      <v-progress-linear v-slot:progress color="blue" indeterminate></v-progress-linear>
      <template slot="headerCell" slot-scope="props">
        <v-tooltip bottom>
          <span slot="activator">{{ props.header.text }}</span>
          <span v-if="props.header.properties.description">{{ props.header.properties.description }}</span>
          <span v-else>{{ props.header.text }}</span>
        </v-tooltip>
      </template>
      <template v-slot:items="props">
        <td v-for="met in defaultMeta" v-bind:key="met.name">
          <CustomComponent :met="met" :props="props"/>
        </td>
      </template>
    </v-data-table>

    <div class="text-xs-center pt-3">
      <v-pagination
        v-model="message"
        :length="getPageCount()"
        :total-visible="5"
        circle/>
    </div>

  </div>
</template>
<script>
import CustomComponent from './CustomComponent'
import FilterComponent from './FilterComponent'
import ColumnsComponent from './ColumnsComponent'
import CodeViewComponent from './CodeViewComponent'

export default {
  name: 'ObjectsTablePanel',
  components: {CodeViewComponent, ColumnsComponent, FilterComponent, CustomComponent},
  data () {
    return {
      meta: [],
      items: [],
      isLoading: false
    }
  },
  computed: {
    getSort () {
      return this.pagination
    },
    defaultMeta: function () {
      if (this.meta.metadata) {
        return this.meta.metadata.filter(v => {
          return v.properties.visible !== false
        })
      } else {
        return this.meta.metadata
      }
    },
    completeUri () {
      return this.$store.getters.getUri
    },
    types () {
      return this.$store.getters.getTypes
    },
    message: {
      get () {
        return this.$store.getters.getPageNumber
      },
      set (value) {
        return this.$store.commit('pageNumber', value)
      }
    },
    uriPagination: {
      get () {
        return this.$store.getters.getPagination
      },
      set (value) {
        return this.$store.commit('setPagination', value)
      }
    }
  },
  watch: {
    completeUri: {
      handler (newVal, oldVal) {
        if (this.$store.getters.getTypes.length === 0) {
          return
        }
        if (this.$store.getters.getConnections.length === 0) {
          this.items = []
          return
        }
        this.meta = this.$store.getters.getTypes.find(v => {
          return v.name === this.$store.getters.getUri.type
        })
        this.items = []
        this.isLoading = true
        this.$http.get(this.$store.getters.preparedUri).then(
          response => {
            this.items = response.body
            if (this.items.length >= 15 && this.message === this.getPageCount()) {
              this.increasePageCount()
            }
            this.isLoading = false
          }, response => {
            this.$notify({group: 'foo', type: 'error', title: 'Server error', text: response})
            this.isLoading = false
          }
        )
      },
      deep: true
    },
    types: {
      handler (newVal, oldVal) {
        if (this.$store.getters.getConnections.length === 0) {
          this.items = []
          return
        }
        this.meta = this.$store.getters.getTypes.find(v => {
          return v.name === this.$store.getters.getUri.type
        })
        this.items = []
        this.isLoading = true
        this.$http.get(this.$store.getters.preparedUri).then(
          response => {
            this.items = response.body
            if (this.items.length >= 15 && this.message === this.getPageCount()) {
              this.increasePageCount()
            }
            this.isLoading = false
          }, response => {
            this.$notify({group: 'foo', type: 'error', title: 'Server error', text: response})
            this.isLoading = false
          }
        )
      },
      deep: true
    },
    getSort (newValue) {
      if (newValue.descending === true) {
        this.$store.commit('setSort', {'field': newValue.sortBy, 'ord': 'desc'})
      } else if (newValue.descending === false) {
        this.$store.commit('setSort', {'field': newValue.sortBy, 'ord': 'asc'})
      } else {
        this.$store.commit('setSort', {})
      }
    }
  },
  methods: {
    call () {
      this.meta = this.$store.getters.getTypes.find(v => {
        return v.name === this.$store.getters.getUri.type
      })
      this.items = []
      this.$http.get(this.$store.getters.preparedUri).then(
        response => {
          this.items = response.body
        }
      )
    },
    nextPage () {
      return this.$store.commit('nextPage')
    },
    prevPage () {
      return this.$store.commit('prevPage')
    },
    setPageNumber (number) {
      return this.$store.commit('pageNumber', number)
    },
    isActivePage (number) {
      return number === this.$store.state.uri.page.number
    },
    setSort (field) {
      this.$store.commit('setSort', {field: field, ord: 'desc'})
    },
    setFilter () {
      this.$store.commit('setFilter', this.selected)
    },
    getPageNumber () {
      return this.$store.getters.getPageNumber()
    },
    getPageCount () {
      return this.$store.getters.getPageCount
    },
    setPageCount (count) {
      this.$store.commit('setPageCount', count)
    },
    increasePageCount () {
      this.$store.commit('increasePageCount')
    }
  }
}
</script>
