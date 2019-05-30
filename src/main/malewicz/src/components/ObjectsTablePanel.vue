<template>
  <div>
    <ul class="nav">
      <li class="nav-item pt-2 pb-2">
        <nav aria-label="test">
          <ul class="pagination">
            <li class="page-item" v-bind:class="{ disabled: isActivePage(1) }">
              <a class="page-link" href="#" aria-label="Previous" v-on:click="prevPage">
                <span aria-hidden="true">&laquo;</span>
                <span class="sr-only">Previous</span>
              </a>
            </li>
            <li class="page-item" v-bind:class="{ active: isActivePage(1) }">
              <a class="page-link" href="#" v-on:click="setPageNumber(1)">1</a>
            </li>
            <li class="page-item" v-bind:class="{ active: isActivePage(2) }">
              <a class="page-link" href="#" v-on:click="setPageNumber(2)">2</a>
            </li>
            <li class="page-item" v-bind:class="{ active: isActivePage(3) }">
              <a class="page-link" href="#" v-on:click="setPageNumber(3)">3</a>
            </li>
            <li class="page-item" v-bind:class="{ active: isActivePage(4) }">
              <a class="page-link" href="#" v-on:click="setPageNumber(4)">4</a>
            </li>
            <li class="page-item">
              <a class="page-link" href="#" aria-label="Next" v-on:click="nextPage">
                <span aria-hidden="false">&raquo;</span>
                <span class="sr-only">Next</span>
              </a>
            </li>
            <li class="page-item">
              <a class="page-link" href="#" data-toggle="modal" data-target="#exampleModal"><i class="fas fa-cog fa-fw" aria-hidden="true"/></a>
            </li>
            <li class="page-item">
              <a class="page-link" href="#" data-target="#exampleModal" v-on:click="call"><i class="fas fa-reply fa-fw" aria-hidden="true"/></a>
            </li>
            <li class="page-item">
              <download-excel
                :data   = "items">
                <a class="page-link" href="#">To Excel</a>
              </download-excel>
            </li>
          </ul>
        </nav>
      </li>
    </ul>
    <table class="table table-striped table-hover table-sm table-responsive">
        <thead>
        <tr>
            <th scope="row">#</th>
            <th v-for="met in defaultMeta" data-toggle="tooltip" data-placement="left"
                v-bind:title="met.properties.description" :key="met.properties.key" v-on:click="setSort(met.properties.key)">{{met.properties.label}}
            </th>
        </tr>
        </thead>
        <tbody>
        <tr v-for="(item, index) in items" v-bind:class="item._style" :key="item.name">
          <th scope="row">{{index+1}}</th>
          <td v-for="met in defaultMeta" :key="item[met.properties.key]">
            <span v-bind:class="met.properties.class">
              <a v-if="item['_link_'+met.properties.key]" :href="'/#/' + item['database']+'/'+item['_link_'+met.properties.key]">{{item[met.properties.key]}}</a>
              <span v-else v-html="item[met.properties.key]"></span>
            </span>
          </td>
        </tr>
        </tbody>
    </table>

    <!-- Modal -->
    <div class="modal fade" id="exampleModal" tabindex="-1" role="dialog" aria-labelledby="exampleModalLabel"
         aria-hidden="true">
      <div class="modal-dialog" role="document">
        <div class="modal-content">
          <div class="modal-header">
            <h5 class="modal-title" id="exampleModalLabel" data-toggle="modal" data-target="#exampleModal">Columns</h5>
            <button type="button" class="close" data-dismiss="modal" aria-label="Close">
              <span aria-hidden="true">&times;</span>
            </button>
          </div>
          <div class="modal-body">
            <table class="table table-sm table-hover">
              <thead>
                <tr>
                  <th scope="col">#</th>
                  <th scope="col">Visible</th>
                  <th scope="col">Name</th>
                  <th scope="col">Description</th>
                </tr>
              </thead>
              <tbody>
                <tr v-for="(met, index) in meta">
                  <th scope="row">{{index + 1}}</th>
                  <td><input type="checkbox" value="" id="defaultCheck1" v-model="met.properties.visible"></td>
                  <td>{{met.properties.label}}</td>
                  <td>{{met.properties.description}}</td>
                </tr>
              </tbody>
            </table>
          </div>
          <div class="modal-footer">
            <button type="button" class="btn btn-secondary" data-dismiss="modal">Close</button>
          </div>
        </div>
      </div>
    </div>

  </div>
</template>
<script>
export default {
  name: 'ObjectsTablePanel',
  data () {
    return {
      meta: [],
      items: []
    }
  },
  created: function () {
    this.$http.get(this.$store.getters.preparedMetaUri).then(
      response => {
        this.meta = response.body
      }
    )
    this.$http.get(this.$store.getters.preparedUri).then(
      response => {
        this.items = response.body
        let field = this.$store.state.orderby.field
        /*this.items.sort(function (a, b) {
          console.log('sorting field = ' + field)
          if (a[field] < b[field]) return 1
          else return -1
        })*/
      }
    )
  },
  computed: {
    defaultMeta: function () {
      return this.meta.filter(function (v) { return v.properties.visible })
    },
    count () {
      return this.$store.getters.preparedMetaUri
    },
    count2 () {
      return this.$store.getters.preparedUri
    }
  },
  watch: {
    count (newValue) {
      this.meta = []
      this.$http.get(newValue).then(
        response => {
          this.meta = response.body
        }
      )
    },
    count2 (newValue) {
      this.items = []
      this.$http.get(newValue).then(
        response => {
          this.items = response.body
          let field = this.$store.state.orderby.field
          /*this.items.sort(function (a, b) {
            if (a[field] < b[field]) return 1
            else return -1
          })*/
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
      this.$http.get(this.$store.getters.preparedMetaUri).then(
        response => {
          this.meta = response.body
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
