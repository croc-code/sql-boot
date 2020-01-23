<template>
  <v-container fluid grid-list-md>
    <v-layout row wrap>
      <v-flex xs12 sm12 md12 lg6 xl6 style="overflow:auto" v-for="type in types" :key="type.name">
        <v-card elevation="6">
          <LightObjectsTableComponent :type="type" />
        </v-card>
      </v-flex>
    </v-layout>
  </v-container>
</template>

<script>
import LightObjectsTableComponent from './LightObjectsTableComponent'
export default {
  name: 'QuickStartComponent',
  components: {LightObjectsTableComponent},
  computed: {
    types () {
      return this.$store.getters.getTypes
        .filter(v => { return v.properties.show_on_startup_page })
    }
  }
}
</script>
