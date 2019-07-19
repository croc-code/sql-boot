<template>
  <div>

    <v-toolbar color="green" dark>
      <v-toolbar-title>Database Navigator</v-toolbar-title>
    </v-toolbar>

    <v-list two-line>
      <v-tooltip bottom v-for="item in types" :key="item.name">
        <template v-slot:activator="{ on }">
          <v-list-tile v-on="on" @click="setType(item.name)" v-if="item.properties.title">
            <v-list-tile-avatar>
              <v-icon class="green white--text" v-if="item.properties.icon">{{item.properties.icon}}</v-icon>
              <v-icon class="grey white--text" v-else>not_interested</v-icon>
            </v-list-tile-avatar>
            <v-list-tile-content>{{ item.properties.title }}</v-list-tile-content>
          </v-list-tile>
        </template>
        <span v-if="item.properties.description">{{ item.properties.description }}</span>
        <span v-else>{{ item.properties.title }}</span>
      </v-tooltip>
    </v-list>

  </div>
</template>

<script>
  export default {
    name: 'TypesListPanel',
    data() {
      return {
        types: []
      }
    },
    methods: {
      isActive(type) {
        return this.$store.state.type === type.name
      },
      setType(type) {
        this.$store.commit('setPageCount', 1)
        this.$store.commit('pageNumber', 1)
        this.$store.commit('setSort', '')
        return this.$store.commit('setType', type)
      }
    },
    computed: {
      getPreparedTypesUri() {
        this.$http.get(this.$store.getters.preparedTypesUri).then(
          response => {
            this.types = response.body
          }
        )
        return this.$store.getters.preparedTypesUri
      }
    },
    watch: {
      getPreparedTypesUri(newVal, oldVal) {

      }
    }
  }
</script>
