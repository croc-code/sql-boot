<template>
  <div class="echarts">
    <h2><router-link to="/">Go Back</router-link></h2>
    <div>
      <button @click="doLoading">Random</button>
    </div>
    <div class="echart" :style="style">
      <IEcharts
        :option="bar"
        :loading="false"
        :resizable="true"
        @ready="onReady"
        @resize="onResize"
        @click="onClick"
      />
    </div>
  </div>
</template>

<script>
  import IEcharts from 'vue-echarts-v3/src/full.js'

  export default {
    name: 'Demo05',
    components: {
      IEcharts
    },
    data: () => ({
      loading: true,
      style: {},
      bar: {
        title: {
          text: 'ECharts Tests'
        },
        tooltip: {},
        xAxis: {
          data: ['one', 'two', 'three', 'four', 'five', 'six'],
          axisLabel: {
            color: 'red'
          }
        },
        yAxis: {},
        series: [{
          name: 'Simple char',
          type: 'bar',
          data: [5, 5, 7, 2, 1, 9]
        }]
      }
    }),
    methods: {
      doLoading () {
        const that = this
        that.bar.xAxis.data = ['one1', 'two1', 'three1', 'four1', 'five1', 'six1']
        that.bar.series[0].data = [10, 10, 20, 25, 30, 35]
      },
      onReady (ins) {
        console.dir(ins)
      },
      onResize (width, height) {
        console.log(width, height)
      },
      onClick (event, instance, echarts) {
        console.log(arguments[0].name)
      }
    }
  }
</script>

<!-- Add "scoped" attribute to limit CSS to this component only -->
<style scoped>
  .echarts {
    width: 1024px;
    height: 768px;
    margin: 0 auto;
  }
  .echart {
    width: 600px;
    height: 400px;
    border: 1px solid #000;
  }
  h1, h2 {
    font-weight: normal;
  }
  ul {
    list-style-type: none;
    padding: 0;
  }
  li {
    display: inline-block;
    margin: 0 10px;
  }
  a {
    color: #42b983;
  }
</style>
