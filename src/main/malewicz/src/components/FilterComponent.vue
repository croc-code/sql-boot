<template xmlns:v-slot="http://www.w3.org/1999/XSL/Transform">
  <v-dialog v-model="show" width="600">
    <template v-slot:activator="{ on }">
      <v-btn :color="color" icon text v-on="on">
        <v-icon>fa-filter</v-icon>
      </v-btn>
    </template>
    <v-card>
      <v-card-title class="headline grey lighten-2" primary-title>{{ $t('search') }}</v-card-title>
      <v-card-text>
        <template>
          <v-form ref="form">
            <span v-for="item in meta.metadata" v-bind:key="item.name">
              <span v-if="item.sortable !== false">
                <span v-if="item.name === 'endpoint'"></span>
                <span v-else-if="item.type === 'timestamptz'">
                  <VueCtkDateTimePicker
                    :label="item.label"
                    format="YYYY-MM-DD HH:mm"
                    color="green"
                    :range=true
                    v-model='localFilter[item.name]' />
                </span>
                <v-combobox v-else-if="item.values"
                            v-model='localFilter[item.name]'
                            :label="item.label"
                            :items="item.values"
                            clearable>
                </v-combobox>
                <v-text-field v-else-if="item.type == 'numeric' || item.type == 'float8' || item.type == 'int8'"
                              v-model='localFilter[item.name]'
                              type = "number"
                              :label="item.label"
                              clearable
                              filled>
                </v-text-field>
                <v-text-field v-else
                              v-model='localFilter[item.name]'
                              :label="item.label"
                              clearable
                              filled>
                </v-text-field>
              </span>
            </span>
          </v-form>
        </template>
      </v-card-text>
      <v-divider></v-divider>
      <v-card-actions>
        <v-spacer></v-spacer>
        <v-btn color="primary" text @click="setFilter">Search</v-btn>
        <v-btn color="primary" text @click="show = false">Close</v-btn>
      </v-card-actions>
    </v-card>
  </v-dialog>
</template>
<script>
export default {
  name: 'FilterComponent',
  data: function () {
    return {
      show: false,
      localFilter: {}
    }
  },
  props: {
    meta: {}
  },
  methods: {
    setFilter () {
      for (const propName in this.$data.localFilter) {
        if (this.$data.localFilter[propName] === null || this.$data.localFilter[propName] === undefined) {
          delete this.$data.localFilter[propName]
        }
      }
      this.$store.commit('setFilter', JSON.parse(JSON.stringify(this.$data.localFilter)))
    }
  },
  computed: {
    stateFilter () {
      return this.$store.getters.getFilter
    },
    color () {
      const f = this.$store.getters.getFilter
      if (Object.entries(f).length === 0 && f.constructor === Object) {
        return ''
      } else {
        return 'warning'
      }
    }
  },
  watch: {
    stateFilter: {
      handler (newVal) {
        this.$data.localFilter = JSON.parse(JSON.stringify(newVal))
      },
      immediate: true,
      deep: true
    }
  }
}
</script>
