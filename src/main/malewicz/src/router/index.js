import Vue from 'vue'
import Router from 'vue-router'
import HelloWorld from '@/components/HelloWorld'
import ObjectsTablePanel from '@/components/ObjectsTablePanel'

Vue.use(Router)

export default new Router({
  routes: [
    {
      path: '*',
      name: 'TablePanel',
      component: HelloWorld,
      props: { panel: ObjectsTablePanel }
    }
  ]
})
