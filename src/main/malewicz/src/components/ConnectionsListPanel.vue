<template>
  <div>
    <v-toolbar color="green" dark>
      <v-toolbar-title>Connections</v-toolbar-title>
    </v-toolbar>
    <v-list>
      <v-list-tile v-for="item in connections" :key="item.name">
        <v-list-tile-action>
          <v-checkbox v-model="$store.state.uri.newConnections" :value="item.name"/>
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
    data() {
      return {
        connections: []
      }
    },
    created: function () {
        this.$http.get(this.$store.state.uri.host + '/endpoints').then(
          response => {
            this.connections = response.body.filter(v => {
              return v.properties.visible !== false
            })
          }
        )
    }

  }
</script>
