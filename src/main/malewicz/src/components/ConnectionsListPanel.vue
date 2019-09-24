<template>
  <div>
    <v-toolbar :flat=true>
      <v-toolbar-title>Connections</v-toolbar-title>
    </v-toolbar>
    <v-list>
      <v-list-item v-for="item in $store.getters.getAllConnections" :key="item.name">
        <v-list-item-action>
          <v-checkbox v-model="connections" :value="item.name"/>
        </v-list-item-action>
        <v-list-item-content>
          <v-list-item-title>{{ item.name }}</v-list-item-title>
        </v-list-item-content>
      </v-list-item>
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
