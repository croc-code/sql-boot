<template>
  <div class="hello">

    <div class="row">

      <div class="col-sm-3 d-none d-sm-block">
        <ConnectionsListPanel class="mt-2"/>
        <TypesListPanel class="mt-2"/>
      </div>
      <div class="col-sm-9">
        <component v-bind:is="panel"></component>
      </div>
    </div>
  </div>

</template>

<script>

import TypesListPanel from './TypesListPanel'
import ConnectionsListPanel from './ConnectionsListPanel'
import ObjectsTablePanel from './ObjectsTablePanel'
import ChartPanel from './ChartPanel'

export default {
  props: ['panel'],
  name: 'HelloWorld',
  components: { ChartPanel, ObjectsTablePanel, ConnectionsListPanel, TypesListPanel },
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
  methods: {
    changeUri: function (event) {
      return this.$store.commit('changeUri', event.target.value)
    }
  },
  created: function () {
    if (this.$router.currentRoute.fullPath && this.$router.currentRoute.fullPath !== '/') {
      this.$store.commit('changeUri', this.$router.currentRoute.fullPath)
    }
  }
}

</script>
