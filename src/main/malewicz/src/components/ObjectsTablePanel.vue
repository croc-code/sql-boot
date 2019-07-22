<template xmlns:v-slot="http://www.w3.org/1999/XSL/Transform">
  <div>

    <v-toolbar>
      <v-toolbar-title class="text">{{meta.properties.title}}</v-toolbar-title>
      <v-spacer></v-spacer>
      <!--<v-btn icon>
        <v-icon>settings</v-icon>
      </v-btn>
      <v-btn icon>
        <v-icon>search</v-icon>
      </v-btn>-->
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
                    <th>Видимость</th>
                    <th>Имя</th>
                    <th>Описание</th>
                  </tr>
                </template>
                <template v-slot:items="props">
                  <td>
                    <v-checkbox v-model="props.item.properties.visible" />
                  </td>
                    <td>{{ props.item.properties.label }}</td>
                    <td>{{ props.item.properties.description }}</td>
                </template>
              </v-data-table>
            </template>
          </v-card-text>
          <v-divider></v-divider>
          <!--<v-card-actions>-->
            <!--<v-spacer></v-spacer>-->
            <!--<v-btn color="primary" flat @click="dialog = false">I accept</v-btn>-->
          <!--</v-card-actions>-->
        </v-card>
      </v-dialog>

      <v-dialog v-model="dialog2" width="1200">
        <template v-slot:activator="{ on }">
          <v-btn icon v-on="on">
            <v-icon>build</v-icon>
          </v-btn>
        </template>
        <v-card>
          <v-card-title class="headline grey lighten-2" primary-title>
            Privacy Policy
          </v-card-title>
          <v-card-text>
            <pre v-highlightjs="meta.query" class="text-sm-left"><code class="sql"></code></pre>
          </v-card-text>
        </v-card>
      </v-dialog>

    </v-toolbar>




   <v-data-table
    :headers="defaultMeta"
    :items="items"
    :pagination.sync="pagination"
    :loading="isLoading"
    hide-actions
    class="elevation-1">
    <v-progress-linear v-slot:progress color="blue" indeterminate></v-progress-linear>
     <template slot="headerCell" scope="props">
       <v-tooltip bottom>
         <span slot="activator">{{ props.header.text }}</span>
         <span v-if="props.header.properties.description">{{ props.header.properties.description }}</span>
         <span v-else>{{ props.header.text }}</span>
       </v-tooltip>
     </template>
    <template v-slot:items="props">
      <td v-for="met in defaultMeta">
        <span v-if="met.properties.datatype">{{ props.item[met.name] | formatDate }}</span>
        <span v-else>{{ props.item[met.name] }}</span>
      </td>
    </template>
   </v-data-table>

    <div class="text-xs-center pt-3">
      <v-pagination
        v-model="message"
        :length="getPageCount()"
        :total-visible="5"
        next-icon="more_horiz"
        circle/>
    </div>

  </div>
</template>
<script>
export default {
  name: 'ObjectsTablePanel',
  data () {
    return {
      meta: [],
      items: [],
      pagination: {
        rowsPerPage: -1
      },
      dialog: false,
      dialog2: false,
      isLoading: false
    }
  },
  created: function () {
    this.isLoading = true
    this.$http.get(this.$store.getters.preparedTypeUri).then(
      response => {
        this.meta = response.body[0]
      }
    )
    this.$http.get(this.$store.getters.preparedUri).then(
      response => {
        this.items = response.body
        if (this.items.length === 15 && this.message === this.getPageCount()) {
          this.increasePageCount()
        }
        this.isLoading = false
      }
    )
  },
  computed: {
    defaultMeta: function () {
      return this.meta.metadata.filter(function (v) { return v.properties.visible !== false })
    },
    count () {
      return this.$store.getters.preparedTypeUri
    },
    count2 () {
      return this.$store.getters.preparedUri
    },
    message: {
      get () {
        return this.$store.getters.getPageNumber
      },
      set (value) {
        return this.$store.commit('pageNumber', value)
      }
    }
  },
  watch: {
    count (newValue) {
      this.meta = []
      this.$http.get(newValue).then(
        response => {
          this.meta = response.body[0]
          // this.setSort('time')
        }
      )
    },
    count2 (newValue) {
      this.items = []
      this.isLoading = true
      this.$http.get(newValue).then(
        response => {
          this.items = response.body
          if (this.items.length === 15 && this.message === this.getPageCount()) {
            this.increasePageCount()
          }
          this.isLoading = false
        }, response => {
          this.$notify({ group: 'foo', type: 'error', title: 'Server error', text: response })
          this.isLoading = false
        }
      )
    }
  },
  methods: {
    call () {
      this.meta = []
      this.items = []
      this.$http.get(this.$store.getters.preparedTypeUri).then(
        response => {
          this.meta = response.body[0]
        }
      )
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
      return number === this.$store.state.page.number
    },
    setSort (field) {
      this.$store.commit('setSort', {field: field, ord: 'desc'})
    },
    getPageNumber() {
      return this.$store.getters.getPageNumber()
    },
    getPageCount() {
      return this.$store.getters.getPageCount
    },
    setPageCount(count) {
      this.$store.commit('setPageCount', count)
    },
    increasePageCount() {
      this.$store.commit('increasePageCount')
    }
  }
}
</script>
