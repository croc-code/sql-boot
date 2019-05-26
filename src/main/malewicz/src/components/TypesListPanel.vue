<template>
  <div>
    <div class="list-group">
      <li class="list-group-item list-group-item-primary">Objects:</li>
      <a v-for="type in types"
         class="list-group-item"
         v-bind:class="{ active: isActive(type) }"
         id="v-pills-home-tab" data-toggle="pill" href="#v-pills-home" role="tab"
         aria-controls="v-pills-home" aria-selected="true"
         v-on:click="setType(type.name)"
      ><i class="fa fa-table fa-fw" aria-hidden="true"></i>&nbsp; {{type.name}}</a>
    </div>
  </div>
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
  created: function () {
    this.$http.get(this.$store.state.host + '/api/' + this.$store.state.connections + '/types').then(
      response => {
        this.types = response.body
      }
    )
  },
  props: {
    uri: String
  }
}
</script>
