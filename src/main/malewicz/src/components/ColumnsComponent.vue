<template xmlns:v-slot="http://www.w3.org/1999/XSL/Transform">
  <v-dialog v-model="show" width="1000">
    <template v-slot:activator="{ on }">
      <v-btn icon v-on="on">
        <v-icon>fa-columns</v-icon>
      </v-btn>
    </template>
    <v-card>
      <v-card-title class="headline grey lighten-2" primary-title>Columns</v-card-title>
      <v-card-text>
        <template>
          <v-data-table :items="meta.metadata" class="elevation-1" hide-actions>
            <template v-slot:headers="props">
              <tr>
                <th>Visible</th>
                <th>Name</th>
                <th>Description</th>
              </tr>
            </template>
            <template v-slot:items="props">
              <td>
                <v-checkbox v-model="props.item.properties.visible"/>
              </td>
              <td>{{ props.item.properties.label }}</td>
              <td>{{ props.item.properties.description }}</td>
            </template>
          </v-data-table>
        </template>
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
  name: 'ColumnsComponent',
  data () {
    return {
      show: false
    }
  },
  props: {
    meta: {}
  }
}
</script>
