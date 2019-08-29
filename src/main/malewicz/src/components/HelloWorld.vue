<template>

    <v-app id="inspire">

      <v-toolbar color="green" dark fixed app clipped-right>
        <v-toolbar-side-icon @click.stop="drawer = !drawer"></v-toolbar-side-icon>
      </v-toolbar>

      <v-navigation-drawer fixed v-model="drawer" app>
        <ConnectionsListPanel/>
        <TypesListPanel/>
      </v-navigation-drawer>

      <v-content>
        <component v-bind:is="panel"></component>
      </v-content>

      <v-footer color="green" class="white--text" app>
        <span class="pa-3">CrocInc/sql-boot</span>
        <v-spacer></v-spacer>
        <span class="pa-3">&copy; 2019</span>
      </v-footer>
    </v-app>

</template>

<script>

import TypesListPanel from './TypesListPanel'
import ConnectionsListPanel from './ConnectionsListPanel'
import ObjectsTablePanel from './ObjectsTablePanel'

export default {
  data: () => ({
    drawer: true,
    drawerRight: true,
    right: null,
    left: null
  }),

  props: ['panel'],
  name: 'HelloWorld',
  components: {ObjectsTablePanel, ConnectionsListPanel, TypesListPanel},
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
