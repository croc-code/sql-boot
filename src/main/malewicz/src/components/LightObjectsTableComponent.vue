<template xmlns:v-slot="http://www.w3.org/1999/XSL/Transform">
  <div>
    <v-toolbar color="green" dark dense>
      <v-toolbar-title>{{ type.properties.title }}</v-toolbar-title>
      <v-spacer></v-spacer>
      <v-btn :to="mainUri" icon>
        <v-icon>storefront</v-icon>
      </v-btn>
    </v-toolbar>

    <v-data-table :headers="defaultMeta"
                  :items="items"
                  class="elevation-1"
                  :loading="isLoading"
                  :no-data-text = "this.$t('noData')"
                  :loading-text = "this.$t('loading')"
                  :options.sync = "options"
                  :hide-default-footer=true>
      <template v-slot:progress>
        <v-progress-linear color="green" :height="10" indeterminate></v-progress-linear>
      </template>
      <template v-slot:item="props">
        <tr>
        <td v-for="met in defaultMeta" v-bind:key="met.name">
          <CustomComponent :met="met" :props="props"/>
        </td>
        </tr>
      </template>
    </v-data-table>
  </div>
</template>
<script>
import CustomComponent from './CustomComponent'

export default {
  name: 'LightObjectsTableComponent',
  components: {CustomComponent},
  props: {
    type: {}
  },
  computed: {
    mainUri: function() {
      return this.$store.getters.getConnections.join("|") + "/" + this.type.name + "?page=1,15"
    },
    defaultMeta: function () {
      return this.$props.type.metadata.filter(v => {
        return v.visible !== false
      })
    },
    connections () {
      return this.$store.getters.getConnections
    }
  },
  watch: {
    connections: {
      handler () {
        this.items = []
        if (this.$store.getters.getConnections.length === 0) {
          return
        }
        this.isLoading = true
        this.$http.get(this.$store.getters.getHost + '/api/' + this.$store.getters.getConnections.join('|') + '/' + this.$props.type.name + '?page=1,5').then(
                response => {
                  this.items = response.body
                  this.isLoading = false
                }, response => {
                  this.$notify({group: 'foo', type: 'error', title: 'Server error', text: response})
                  this.isLoading = false
                }
        )
      },
      immediate: true
    },
  },
  created: function () {

  },
  data: () => ({
    isLoading: false,
    items: [],
    options: { "itemsPerPage": 5 }
  })
}
</script>
