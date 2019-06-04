<template>

     <v-container grid-list-md text-xs-center>
      <v-layout row wrap>
        <v-flex xs2>
          <v-card color="secondary">
            <v-card-text class="px-0">
              <ConnectionsListPanel/>
              <TypesListPanel/>
            </v-card-text>
          </v-card>
        </v-flex>
        <v-flex xs10>
          <v-card color="secondary">
            <v-card-text class="px-0">
              <component v-bind:is="panel"></component>
            </v-card-text>
          </v-card>
        </v-flex>
      </v-layout>
    </v-container>

</template>

<script>

import TypesListPanel from './TypesListPanel'
import ConnectionsListPanel from './ConnectionsListPanel'
import ObjectsTablePanel from './ObjectsTablePanel'

export default {
  props: ['panel'],
  name: 'HelloWorld',
  components: { ObjectsTablePanel, ConnectionsListPanel, TypesListPanel },
  watch: {
    $route (to, from) {
      this.$store.commit('changeUri', to.fullPath)
    },
    getSimpleUri: function (newUri, oldUri) {
      this.$router.push({ path: '/' + newUri })
    }
  },
  computed: {
    getSimpleUri () {
      return this.$store.getters.getSimpleUri
    }
  },
  created: function () {
    if (this.$router.currentRoute.fullPath && this.$router.currentRoute.fullPath !== '/') {
      this.$store.commit('changeUri', this.$router.currentRoute.fullPath)
    }
  }
}

</script>
