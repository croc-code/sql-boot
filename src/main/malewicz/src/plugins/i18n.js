import Vue from 'vue';
import VueI18n from 'vue-i18n';

Vue.use(VueI18n);

const messages = {
    'en': {
        connections: 'Connections',
        databaseNavigator: 'Database Navigator',
        columns: 'Columns',
        sqlQuery: 'SQL-query',
        search: 'Search',
        noData: 'No data available',
        close: 'Close',
        cluster: 'Cluster',
        sourceCluster: 'Source cluster',
        loading: 'Loading...'
    },
    'ru': {
        connections: 'Соединения',
        databaseNavigator: 'Объекты БД',
        columns: 'Колонки',
        sqlQuery: 'SQL-запрос',
        search: 'Поиск',
        noData: 'Нет данных',
        close: 'Закрыть',
        cluster: 'Кластер',
        sourceCluster: 'Кластер источник',
        loading: 'Загрузка...'
    },
    'es': {
        connections: 'Conexiones',
        databaseNavigator: 'Objetos de BD',
        columns: 'Oradores',
        sqlQuery: 'SQL-consulta',
        search: 'Buscar',
        noData: 'Datos no disponibles',
        close: 'Cerrar',
        cluster: 'Racimo',
        sourceCluster: 'Clúster fuente',
        loading: 'Cargando...'
    }
};

const i18n = new VueI18n({
    locale: 'en',
    fallbackLocale: 'ru',
    messages,
});

export default i18n;
