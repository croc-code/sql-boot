<template>
  <div>
    <div class="list-group">
      <li class="list-group-item list-group-item-primary">Objects:</li>
      <span v-for="type in types" data-toggle="tooltip" data-placement="left" v-bind:title="type.properties.description" >
      <a class="list-group-item"
         v-bind:class="{ active: isActive(type) }"
         id="v-pills-home-tab" data-toggle="pill" href="#v-pills-home" role="tab"
         aria-controls="v-pills-home" aria-selected="true"
         v-on:click="setType(type.name)"
      ><i class="fa fa-table fa-fw" aria-hidden="true"></i>&nbsp; {{ type.properties.title || type.name }}</a>
      </span>
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
