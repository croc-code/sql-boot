<template xmlns:v-slot="http://www.w3.org/1999/XSL/Transform">
    <v-dialog v-model="show" width="600">
        <template v-slot:activator="{ on }">
            <v-btn icon v-on="on">
                <v-icon>fa-columns</v-icon>
            </v-btn>
        </template>
        <v-card>
            <v-card-title class="headline grey lighten-2" primary-title>{{ $t('columns') }}</v-card-title>
            <v-card-text>
                <v-list>
                    <v-list-item v-for="column in currentType.metadata" v-bind:key="column.name">
                        <template v-slot:default="{ active, toggle }">
                            <v-list-item-action>
                                <v-checkbox v-model="column.visible" color="primary"/>
                            </v-list-item-action>

                            <v-list-item-content>
                                <v-list-item-title v-if="column.name==='endpoint'">{{ $t('cluster') }}</v-list-item-title>
                                <v-list-item-title v-else>{{ column.text }}</v-list-item-title>
                                <v-list-item-subtitle>
                                    <v-textarea v-if="column.name==='endpoint'" :value="$t('sourceCluster')" :rows=1 :flat=true readonly></v-textarea>
                                    <v-textarea v-else :value="column.description || column.text" :rows=1 :flat=true readonly></v-textarea>
                                </v-list-item-subtitle>
                            </v-list-item-content>
                        </template>
                    </v-list-item>
                </v-list>
            </v-card-text>
            <v-divider></v-divider>
            <v-card-actions>
                <v-spacer></v-spacer>
                <v-btn color="primary" text @click="show = false">{{ $t('close') }}</v-btn>
            </v-card-actions>
        </v-card>
    </v-dialog>
</template>
<script>
    export default {
        name: 'ColumnsComponent',
        data() {
            return {
                currentType: {},
                show: false
            }
        },
        props: {
            typeName: ''
        },
        watch: {
            typeName: function (newVal, oldVal) { // watch it
                this.currentType = this.$store.getters.getTypes.find(v => {
                    return v.name === this.$store.getters.getUri.type
                })
            }
        }
    }
</script>
