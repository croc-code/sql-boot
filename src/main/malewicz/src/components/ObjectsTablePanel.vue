<template>
  <div>

    <v-toolbar>
      <v-toolbar-title class="text">{{meta.properties.title}}</v-toolbar-title>
      <!--<v-spacer></v-spacer>
      <v-btn icon>
        <v-icon>settings</v-icon>
      </v-btn>
      <v-btn icon>
        <v-icon>search</v-icon>
      </v-btn>
      <v-btn icon>
        <v-icon>refresh</v-icon>
      </v-btn>-->
    </v-toolbar>

<!--    <ul class="nav">-->
<!--      <li class="nav-item pt-2 pb-2">-->
<!--        <nav aria-label="test">-->
<!--          <ul class="pagination">-->
<!--            <li class="page-item">-->
<!--              <a class="page-link" href="#" data-toggle="modal" data-target="#exampleModal"><i class="fas fa-cog fa-fw" aria-hidden="true"/></a>-->
<!--            </li>-->
<!--            <li class="page-item">-->
<!--              <a class="page-link" href="#" data-target="#exampleModal" v-on:click="call"><i class="fas fa-reply fa-fw" aria-hidden="true"/></a>-->
<!--            </li>-->
<!--            <li class="page-item">-->
<!--              <download-excel-->
<!--                :data   = "items">-->
<!--                <a class="page-link" href="#">To Excel</a>-->
<!--              </download-excel>-->
<!--            </li>-->
<!--          </ul>-->
<!--        </nav>-->
<!--      </li>-->
<!--    </ul>-->

   <v-data-table
    :headers="defaultMeta"
    :items="items"
    :pagination.sync="pagination"
    hide-actions
    class="elevation-1">
    <template v-slot:items="props">
      <td v-for="met in defaultMeta">{{ props.item[met.name] }}</td>
    </template>
   </v-data-table>

    <div class="text-xs-center pt-3">
      <v-pagination v-model="currentPageNumber" :length=15></v-pagination>
    </div>


    <!-- Modal -->
<!--    <div class="modal fade" id="exampleModal" tabindex="-1" role="dialog" aria-labelledby="exampleModalLabel"-->
<!--         aria-hidden="true">-->
<!--      <div class="modal-dialog" role="document">-->
<!--        <div class="modal-content">-->
<!--          <div class="modal-header">-->
<!--            <h5 class="modal-title" id="exampleModalLabel" data-toggle="modal" data-target="#exampleModal">Columns</h5>-->
<!--            <button type="button" class="close" data-dismiss="modal" aria-label="Close">-->
<!--              <span aria-hidden="true">&times;</span>-->
<!--            </button>-->
<!--          </div>-->
<!--          <div class="modal-body">-->
<!--            <table class="table table-sm table-hover">-->
<!--              <thead>-->
<!--                <tr>-->
<!--                  <th scope="col">#</th>-->
<!--                  <th scope="col">Visible</th>-->
<!--                  <th scope="col">Name</th>-->
<!--                  <th scope="col">Description</th>-->
<!--                </tr>-->
<!--              </thead>-->
<!--              <tbody>-->
<!--                <tr v-for="(met, index) in meta.metadata">-->
<!--                  <th scope="row">{{index + 1}}</th>-->
<!--                  <td><input type="checkbox" value="" id="defaultCheck1" v-model="met.properties.visible"></td>-->
<!--                  <td>{{met.properties.label}}</td>-->
<!--                  <td>{{met.properties.description}}</td>-->
<!--                </tr>-->
<!--              </tbody>-->
<!--            </table>-->
<!--          </div>-->
<!--          <div class="modal-footer">-->
<!--            <button type="button" class="btn btn-secondary" data-dismiss="modal">Close</button>-->
<!--          </div>-->
<!--        </div>-->
<!--      </div>-->
<!--    </div>-->

<!--      <div class="text-xs-center">
        <v-dialog v-model="dialog" width="1200">
          <template v-slot:activator="{ on }">
            <v-btn v-on="on">Click Me</v-btn>
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
      </div>-->

  </div>
</template>
<script>
export default {
  name: 'ObjectsTablePanel',
  data () {
    return {
      meta: [],
      items: [],
      currentPageNumber: 1,
      pagination: {
        rowsPerPage: -1
      }

    }
  },
  created: function () {
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
  computed: {
    defaultMeta: function () {
      return this.meta.metadata.filter(function (v) { return v.properties.visible || true })
    },
    count () {
      return this.$store.getters.preparedTypeUri
    },
    count2 () {
      return this.$store.getters.preparedUri
    }
  },
  watch: {
    currentPageNumber (newValue) {
      this.setPageNumber(newValue)
    },
    count (newValue) {
      this.meta = []
      this.$http.get(newValue).then(
        response => {
          this.meta = response.body[0]
        }
      )
    },
    count2 (newValue) {
      this.items = []
      this.$http.get(newValue).then(
        response => {
          this.items = response.body
        }, response => {
          this.$notify({
            group: 'foo',
            type: 'error',
            title: 'Server error',
            text: response
          })
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
    }
  }
}
</script>
