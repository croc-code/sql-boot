<template>

  <v-container grid-list-md text-xs-center>
    <v-layout row wrap>
      <v-flex xs2>
        <ConnectionsListPanel/>
        <TypesListPanel/>
      </v-flex>
      <v-flex xs10>
        <component v-bind:is="panel"></component>
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
    components: {ObjectsTablePanel, ConnectionsListPanel, TypesListPanel},
    watch: {
      $route(to, from) {
        this.$store.commit('changeUri', to.fullPath)
      },
      getSimpleUri: function (newUri, oldUri) {
        this.$router.push({path: '/' + newUri})
      }
    },
    computed: {
      getSimpleUri() {
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
