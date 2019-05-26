<template>
    <div class="list-group">
      <li class="list-group-item list-group-item-primary">Connections:</li>
      <div v-for="connection in connections" class="list-group-item" v-bind:class="{ disabled: isDisabled(connection.health) }" :key="connection.name" >
        <label>
          <!--<input type="checkbox" id="defaultCheck1" v-if="isActiveConnection(connection.name)" checked="checked" />-->
          <!--<input type="checkbox" id="defaultCheck2" v-else />-->

          <input type="checkbox" :value="connection.name" v-model="checkedNames" />
          <i :class="connection.properties.css_class"></i>
          {{connection.name}}
        </label>
      </div>
    </div>
</template>

<script>
export default {
  name: 'ConnectionsListPanel',
  data () {
    return {
      checkedNames: ['dev'],
      connections: []
    }
  },
  created: function () {
    this.$http.get(this.$store.state.host + '/endpoints').then(
      response => {
        this.connections = response.body.filter(function (v) { return v.properties.visible !== false })
      }
    )
    this.doLoading()
  },
  methods: {
    isDisabled (health) {
      if (health === 'Ok') {
        return false
      }
      return true
    },
    doLoading () {
      this.$sse('http://localhost:8007/connections/health', { format: 'json' }) // or { format: 'plain' }
        .then(sse => {
          sse.onError(e => {
            console.error('lost connection; giving up!', e)
            sse.close()
          })

          sse.subscribe('', data => {
            if (data.properties.visible !== false) {
              this.connections = this.connections.filter(function (item) {
                return item.name !== data.name
              })
              this.connections.push(data)
            }
          })

          setTimeout(() => {
            sse.unsubscribe('')
            console.log('Stopped listening to event-less messages!')
          }, 35000)
        })
        .catch(err => {
          console.error('Failed to connect to server', err)
        })
    },
    isActiveConnection (connectionName) {
      return this.$store.state.connections.includes(connectionName)
    }
  },
  props: {
  }
}
</script>
