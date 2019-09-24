<template xmlns:v-slot="http://www.w3.org/1999/XSL/Transform">

  <div id="app">
    <v-app id="inspire">

      <v-navigation-drawer v-model="drawer" app clipped>
        <ConnectionsListPanel/>
        <TypesListPanel/>
      </v-navigation-drawer>

      <v-app-bar app color="green" dark clipped-left dense>
        <v-app-bar-nav-icon @click.stop="drawer = !drawer"></v-app-bar-nav-icon>
      </v-app-bar>

      <component v-bind:is="panel"></component>

      <v-footer color="green" app>
        <span class="white--text">github.com/CrocInc/sql-boot</span>
        <v-spacer></v-spacer>
        <span class="white--text">&copy; 2019</span>
      </v-footer>
    </v-app>
  </div>

</template>

<script>
import TypesListPanel from './TypesListPanel'
import ConnectionsListPanel from './ConnectionsListPanel'
import ObjectsTablePanel from "./ObjectsTablePanel";

export default {
  data: () => ({
    drawer: true,
    drawerRight: true,
    right: null,
    left: null
  }),
  props: ['panel'],
  name: 'HelloWorld',
  components: {ObjectsTablePanel, ConnectionsListPanel, TypesListPanel },
  watch: {
    $route (to, from) {
      this.$store.commit('changeUri', to.fullPath)
    },
    getSimpleUri: function (newUri, oldUri) {
      this.$router.push({path: '/' + newUri})
    }
  },
  computed: {
    getSimpleUri () {
      return this.$store.getters.getSimpleUri
    }
  },
  created: function () {
    this.$store.commit('changeUri', this.$router.currentRoute.fullPath)
  }
}

</script>
