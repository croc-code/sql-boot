<template xmlns:v-slot="http://www.w3.org/1999/XSL/Transform">
  <div>
    <v-toolbar color="green" dark>
      <v-toolbar-title>{{ type.properties.title }}</v-toolbar-title>
      <v-spacer></v-spacer>
      <v-btn icon>
        <v-icon>search</v-icon>
      </v-btn>
    </v-toolbar>

    <v-data-table :headers="defaultMeta"
                  :items="desserts"
                  class="elevation-1"
                  :loading="isLoading">
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
  created: function () {
    this.isLoading = true
    this.$http.get(this.$store.getters.getHost + 'api/' + this.$store.getters.getAllConnections.map(function (item) { return item.name }).join('|') + '/' + this.$props.type.name + '?page=1,15').then(
      response => {
        this.desserts = response.body
        this.isLoading = false
      }, response => {
        this.$notify({group: 'foo', type: 'error', title: 'Server error', text: response})
        this.isLoading = false
      }
    )
  },
  computed: {
    defaultMeta: function () {
      return this.$props.type.metadata.filter(v => {
        return v.properties.visible !== false
      })
    }
  },
  data: () => ({
    isLoading: false,
    desserts: []
  })
}
</script>
