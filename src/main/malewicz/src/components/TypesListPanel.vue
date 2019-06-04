<template>
    <v-navigation-drawer permanent>
      <v-toolbar flat>
        <v-list>
          <v-list-tile>
            <v-list-tile-title class="title">Objects</v-list-tile-title>
          </v-list-tile>
        </v-list>
      </v-toolbar>

      <v-divider></v-divider>

      <v-list dense class="pt-0">
        <v-list-tile
          v-for="item in types"
          :key="item.name"
          @click="setType(item.name)" v-if="item.properties.title">
          <v-list-tile-action>
            <v-icon large>{{item.properties.icon}}</v-icon>
          </v-list-tile-action>
          <v-list-tile-content>
            <v-list-tile-title>{{ item.properties.title }}</v-list-tile-title>
          </v-list-tile-content>
        </v-list-tile>
      </v-list>
    </v-navigation-drawer>
</template>

<script>
export default {
  name: 'TypesListPanel',
  data () {
    return {
      types: []
    }
  },
  methods: {
    isActive (type) {
      return this.$store.state.type === type.name
    },
    setType (type) {
      this.$store.commit('setSort', '')
      return this.$store.commit('setType', type)
    }
  },
  computed: {
    getPreparedTypesUri () {
      this.$http.get(this.$store.getters.preparedTypesUri).then (
        response => {
          this.types = response.body
        }
      )
      return this.$store.getters.preparedTypesUri
    }
  },
  watch: {
    getPreparedTypesUri (newVal, oldVal) {

    }
  }
}
</script>
