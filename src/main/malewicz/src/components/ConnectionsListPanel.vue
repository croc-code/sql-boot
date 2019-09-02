<template>
  <div>
    <v-toolbar color="green" dark>
      <v-toolbar-title>Connections</v-toolbar-title>
    </v-toolbar>
    <v-list>
      <v-list-tile v-for="item in $store.getters.getAllConnections" :key="item.name">
        <v-list-tile-action>
          <v-checkbox v-model="connections" :value="item.name"/>
        </v-list-tile-action>
        <v-list-tile-content>
          <v-list-tile-title>{{ item.name }}</v-list-tile-title>
        </v-list-tile-content>
      </v-list-tile>
    </v-list>
  </div>
</template>

<script>
export default {
  name: 'ConnectionsListPanel',
  data () {
    return {}
  },
  computed: {
    connections: {
      get () {
        return this.$store.getters.getConnections
      },
      set (value) {
        return this.$store.commit('setConnections', value)
      }
    }
  }
}
</script>
