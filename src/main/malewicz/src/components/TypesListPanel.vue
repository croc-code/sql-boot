<template xmlns:v-slot="http://www.w3.org/1999/XSL/Transform">
  <div>

    <v-toolbar color="green" dark>
      <v-toolbar-title>Database Navigator</v-toolbar-title>
    </v-toolbar>

    <v-list two-line>
      <div v-for="tag in allTags()" v-bind:key="tag">
        <v-list-group v-if="typesByTag(tag).length > 0" prepend-icon="bookmark_border">
          <template v-slot:activator>
            <v-list-tile>
              <v-list-tile-title>{{tag}}</v-list-tile-title>
            </v-list-tile>
          </template>

          <v-tooltip right v-for="item in typesByTag(tag)" :key="item.name">
            <template v-slot:activator="{ on }">
              <v-list-tile v-on="on" @click="setType(item.name)" v-if="item.properties.title">
                <v-list-tile-avatar>
                  <v-icon v-bind:class="item.properties.icon_class">{{ item.properties.icon || 'not_interested' }}
                  </v-icon>
                </v-list-tile-avatar>
                <v-list-tile-content>{{ item.properties.title }}</v-list-tile-content>
              </v-list-tile>
            </template>
            <span v-if="item.properties.description">{{ item.properties.description }}</span>
            <span v-else>{{ item.properties.title }}</span>
          </v-tooltip>
        </v-list-group>
      </div>
    </v-list>

  </div>
</template>

<script>
export default {
  name: 'TypesListPanel',
  data () {
    return {}
  },
  methods: {
    allTags () {
      return this.$store.getters.getTypes
        .map(v => {
          return v.properties.tags
        })
        .filter(el => el === 0 || Boolean(el))
        .map(v => {
          return v.split(',')
        })
        .flatMap(v => v)
        .filter(this.onlyUnique)
        .filter(el => el !== 'ui')
    },
    typesByTag (tag) {
      return this.$store.getters.getTypes
        .filter(el => el === 0 || Boolean(el.properties.tags))
        .filter(v => {
          return v.properties.tags.includes(tag) && v.properties.tags.includes('ui')
        })
    },
    onlyUnique (value, index, self) {
      return self.indexOf(value) === index
    },
    isActive (type) {
      return this.$store.state.uri.type === type.name
    },
    setType (type) {
      this.$store.commit('skipObjectUri', type)
    }
  }
}
</script>
