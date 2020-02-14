import Vue from 'vue'
import Router from 'vue-router'
import Application from './components/Application'
import ObjectsTablePanel from './components/ObjectsTablePanel'
import QuickStartComponent from './components/QuickStartComponent'


Vue.use(Router)

export default new Router({
  routes: [
    {
      path: '/',
      alias: '/:connections',
      name: 'StartPanel',
      component: Application,
      props: { panel: QuickStartComponent }
    },
    {
      path: '*',
      name: 'TablePanel',
      component: Application,
      props: { panel: ObjectsTablePanel }
    }
  ]
})
