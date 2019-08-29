<template xmlns:v-slot="http://www.w3.org/1999/XSL/Transform">
    <v-dialog v-model="show" width="1200">
        <template v-slot:activator="{ on }">
            <v-btn icon v-on="on">
                <v-icon>build</v-icon>
            </v-btn>
        </template>
        <v-card>
            <v-card-title class="headline grey lighten-2" primary-title>SQL-query</v-card-title>
            <v-card-text>
                <pre v-highlightjs="meta.query" class="text-sm-left"><code class="sql"></code></pre>
            </v-card-text>
            <v-divider></v-divider>
            <v-card-actions>
                <v-spacer></v-spacer>
                <v-btn color="primary" flat @click="show = false">Close</v-btn>
            </v-card-actions>
        </v-card>
    </v-dialog>
</template>
<script>
export default {
  name: 'CodeViewComponent',
  props: {
    show: {},
    meta: {}
  }
}
</script>
