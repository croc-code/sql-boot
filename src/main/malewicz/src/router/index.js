import Vue from 'vue'
import Router from 'vue-router'
import HelloWorld from '@/components/HelloWorld'
import ChartPanel from '@/components/ChartPanel'
import ObjectsTablePanel from '@/components/ObjectsTablePanel'

Vue.use(Router)

export default new Router({
  routes: [
    {
      path: '/chart*',
      name: 'ChartPanel',
      component: HelloWorld,
      props: { panel: ChartPanel }
    },
    {
      path: '*',
      name: 'TablePanel',
      component: HelloWorld,
      props: { panel: ObjectsTablePanel }
    }
  ]
})
