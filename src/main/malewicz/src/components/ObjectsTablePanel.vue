<template xmlns:v-slot="http://www.w3.org/1999/XSL/Transform">
  <div>

    <v-toolbar>
      <v-toolbar-title v-if="meta.properties">{{ meta.properties.title }}</v-toolbar-title>
      <v-spacer></v-spacer>
      <ColumnsComponent :typeName="meta.name"/>
      <CodeViewComponent :code="meta.query"/>
      <FilterComponent :meta="meta"/>
      <v-btn @click="call()" icon>
        <v-icon>fa-sync-alt</v-icon>
      </v-btn>
    </v-toolbar>

    <v-data-table :headers="defaultMeta"
                  :items="items"
                  :loading="isLoading"
                  :options.sync = "options"
                  :no-data-text = "this.$t('noData')"
                  hide-default-footer
                  :fixed-header="true"
                  class="elevation-1">
      <template v-slot:progress>
        <v-progress-linear color="green" :height="10" indeterminate></v-progress-linear>
      </template>
      <template v-slot:header.endpoint="endp">
        {{ $t('cluster') }}
      </template>
      <template v-slot:item="props">
        <tr>
          <td v-for="met in defaultMeta" v-bind:key="met.name">
            <CustomComponent :met="met" :props="props"/>
          </td>
        </tr>
      </template>
    </v-data-table>

    <v-pagination v-model="message"
                  :length="getPageCount()"
                  :total-visible="5"
                  circle>
    </v-pagination>

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
      isLoading: false,
      options: { "itemsPerPage": 100 }
    }
  },
  computed: {
    getSort () {
      return this.options.sortBy
    },
    defaultMeta: function () {
      if (this.meta.metadata) {
        return this.meta.metadata.filter(v => {
          return v.visible !== false
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
      if (newValue.length > 0) {
        if (this.$data.options.sortDesc[0]) {
          this.$store.commit('setSort', {'field': newValue, 'ord': 'desc'})
        } else {
          this.$store.commit('setSort', {'field': newValue, 'ord': 'asc'})
        }
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
