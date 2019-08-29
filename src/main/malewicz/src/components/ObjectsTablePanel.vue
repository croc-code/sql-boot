<template xmlns:v-slot="http://www.w3.org/1999/XSL/Transform">
  <div>
    <v-toolbar>
      <v-toolbar-title class="text" v-if="meta.properties">{{ meta.properties.title }}</v-toolbar-title>
      <v-spacer></v-spacer>
      <v-dialog v-model="dialog" width="1000">
        <template v-slot:activator="{ on }">
          <v-btn icon v-on="on">
            <v-icon>settings</v-icon>
          </v-btn>
        </template>
        <v-card>
          <v-card-title class="headline grey lighten-2" primary-title>Columns</v-card-title>
          <v-card-text>
            <template>
              <v-data-table :items="meta.metadata" class="elevation-1" hide-actions>
                <template v-slot:headers="props">
                  <tr>
                    <th>Visible</th>
                    <th>Name</th>
                    <th>Description</th>
                  </tr>
                </template>
                <template v-slot:items="props">
                  <td>
                    <v-checkbox v-model="props.item.properties.visible"/>
                  </td>
                  <td>{{ props.item.properties.label }}</td>
                  <td>{{ props.item.properties.description }}</td>
                </template>
              </v-data-table>
            </template>
          </v-card-text>
          <v-divider></v-divider>
          <v-card-actions>
            <v-spacer></v-spacer>
            <v-btn color="primary" flat @click="dialog = false">Close</v-btn>
          </v-card-actions>
        </v-card>
      </v-dialog>

      <v-dialog v-model="dialog2" width="1200">
        <template v-slot:activator="{ on }">
          <v-btn icon v-on="on">
            <v-icon>build</v-icon>
          </v-btn>
        </template>
        <v-card>
          <v-card-title class="headline grey lighten-2" primary-title>SQL-query</v-card-title>
          <v-card-text>
            <pre v-highlightjs="meta.query" class="text-sm-left"><code class="sql"></code></pre>
          </v-card-text>
          <v-divider></v-divider>
          <v-card-actions>
            <v-spacer></v-spacer>
            <v-btn color="primary" flat @click="dialog2 = false">Close</v-btn>
          </v-card-actions>
        </v-card>
      </v-dialog>

      <v-dialog v-model="dialog3" width="600">
        <template v-slot:activator="{ on }">
          <v-btn icon v-on="on">
            <v-icon>search</v-icon>
          </v-btn>
        </template>
        <v-card>
          <v-card-title class="headline grey lighten-2" primary-title>Search</v-card-title>
          <v-card-text>
            <template>
              <v-form ref="form">
                <span v-for="item in meta.metadata" v-bind:key="item.name">
                  <span v-if="item.properties.datatype === 'time'"></span>
                    <v-combobox v-if="item.properties.values"
                                :label="item.properties.label"
                                v-model='selected[item.name]'
                                :items="item.properties.values"
                                clearable>
                    </v-combobox>
                    <v-text-field v-else
                                  :label="item.properties.label"
                                  v-model='selected[item.name]'
                                  clearable filled>
                    </v-text-field>
                </span>
              </v-form>
            </template>
          </v-card-text>
          <v-divider></v-divider>
          <v-card-actions>
            <v-spacer></v-spacer>
            <v-btn color="primary" flat @click="setFilter">Search</v-btn>
            <v-btn color="primary" flat @click="dialog3 = false">Close</v-btn>
          </v-card-actions>
        </v-card>
      </v-dialog>

      <v-btn @click="call()" icon>
        <v-icon>refresh</v-icon>
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

export default {
  name: 'ObjectsTablePanel',
  components: {CustomComponent},
  data () {
    return {
      meta: [],
      items: [],
      dialog: false,
      dialog2: false,
      dialog3: false,
      isLoading: false,
      selected: {}
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
