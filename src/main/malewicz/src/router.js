import Vue from 'vue'
import Router from 'vue-router'
import HelloWorld from '@/components/HelloWorld'
import ObjectsTablePanel from '@/components/ObjectsTablePanel'
import QuickStartComponent from '@/components/QuickStartComponent'


Vue.use(Router)

export default new Router({
  routes: [
    {
      path: '/',
      name: 'StartPanel',
      component: HelloWorld,
      props: { panel: QuickStartComponent }
    },
    {
      path: '*',
      name: 'TablePanel',
      component: HelloWorld,
      props: { panel: ObjectsTablePanel }
    }
  ]
})
