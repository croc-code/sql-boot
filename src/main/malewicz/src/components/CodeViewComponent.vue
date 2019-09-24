<template xmlns:v-slot="http://www.w3.org/1999/XSL/Transform">
  <v-dialog v-model="show" width="1200">
    <template v-slot:activator="{ on }">
      <v-btn icon text color="green" v-on="on">
        <v-icon>fa-code</v-icon>
      </v-btn>
    </template>
    <v-card>
      <v-card-title class="headline grey lighten-2" primary-title>SQL-query</v-card-title>
      <v-card-text>
        <pre v-highlightjs="code" class="text-sm-left"><code class="sql"></code></pre>
      </v-card-text>
      <v-divider></v-divider>
      <v-card-actions>
        <v-spacer></v-spacer>
        <v-btn color="primary" text @click="show = false">Close</v-btn>
      </v-card-actions>
    </v-card>
  </v-dialog>
</template>
<script>
export default {
  name: 'CodeViewComponent',
  data () {
    return {
      show: false
    }
  },
  props: {
    code: ''
  }
}
</script>
