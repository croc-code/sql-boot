<!--
  - BSD 3-Clause License
  -
  - Copyright (c) 2019, CROC Inc.
  - All rights reserved.
  -
  - Redistribution and use in source and binary forms, with or without
  - modification, are permitted provided that the following conditions are met:
  -
  - 1. Redistributions of source code must retain the above copyright notice, this
  -    list of conditions and the following disclaimer.
  -
  - 2. Redistributions in binary form must reproduce the above copyright notice,
  -    this list of conditions and the following disclaimer in the documentation
  -    and/or other materials provided with the distribution.
  -
  - 3. Neither the name of the copyright holder nor the names of its
  -    contributors may be used to endorse or promote products derived from
  -    this software without specific prior written permission.
  -
  - THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
  - AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
  - IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
  - DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE
  - FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL
  - DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
  - SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER
  - CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY,
  - OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE
  - OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
  -->

<template>
  <div>
    <span v-if="met.type==='json' && props.item[met.name]">
      <a :href="'#/'+props.item['endpoint']+'/'+JSON.parse(props.item[met.name].value).link+'?page=1,15'">
        <v-icon medium v-if="JSON.parse(props.item[met.name].value).icon">
          {{ JSON.parse(props.item[met.name].value).icon }}
        </v-icon>
        <v-chip v-else color="green" style="cursor: pointer" dark>{{ JSON.parse(props.item[met.name].value).label }}</v-chip>
      </a>
    </span>
    <span v-else-if="(met.type==='inet') && props.item[met.name]">
      {{ props.item[met.name].value }}
    </span>
    <span v-else-if="(met.type==='interval') && props.item[met.name]">
      {{ props.item[met.name].value }}
    </span>
    <span v-else-if="met.format==='sql'">
      <CodeViewComponent :code="props.item[met.name]"/>
    </span>
    <span v-else-if="met.type==='timestamptz'">{{ props.item[met.name] | formatDate }}</span>
    <span v-else-if="met.format==='size'">
      <v-tooltip bottom>
        <template v-slot:activator="{ on }">
          <span v-on="on">{{ props.item[met.name] | prettyByte }}</span>
        </template>
        <span>{{ props.item[met.name] }}</span>
      </v-tooltip>
    </span>
    <span v-else-if="met.type==='numeric' || met.type==='float8'">
      <v-tooltip bottom>
        <template v-slot:activator="{ on }">
          <span v-on="on">{{ props.item[met.name] | round(2) }}</span>
        </template>
        <span>{{ props.item[met.name] }}</span>
      </v-tooltip>
    </span>
    <span v-else>{{ props.item[met.name] }}</span>
  </div>
</template>

<script>

import moment from 'moment'
import CodeViewComponent from './CodeViewComponent'

export default {
  name: 'CustomComponent',
  components: {CodeViewComponent},
  props: {
    met: {},
    props: {}
  },
  filters: {
    formatDate: function (value) {
      if (value) {
        return moment(String(value)).format('DD.MM.YYYY HH:mm:ss')
      }
    },
    round: function (value, decimals) {
      if (typeof value !== 'number') {
        return value
      }
      if (!value) {
        value = 0
      }
      if (!decimals) {
        decimals = 0
      }
      value = Math.round(value * Math.pow(10, decimals)) / Math.pow(10, decimals)
      return value
    },
    prettyByte: function (num) {
      if (typeof num !== 'number' || isNaN(num)) {
        return num
        // throw new TypeError('Expected a number, ' + num)
      }
      var exponent
      var unit
      var neg = num < 0
      var units = ['B', 'kB', 'MB', 'GB', 'TB', 'PB', 'EB', 'ZB', 'YB']

      if (neg) {
        num = -num
      }

      if (num < 1) {
        return (neg ? '-' : '') + num + ' B'
      }

      exponent = Math.min(Math.floor(Math.log(num) / Math.log(1000)), units.length - 1)
      num = (num / Math.pow(1000, exponent)).toFixed(2) * 1
      unit = units[exponent]

      return (neg ? '-' : '') + num + ' ' + unit
    }
  }
}
</script>
