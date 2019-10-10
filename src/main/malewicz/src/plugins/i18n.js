import Vue from 'vue';
import VueI18n from 'vue-i18n';

Vue.use(VueI18n);

const messages = {
    'en': {
        connections: 'Connections',
        databaseNavigator: 'Database Navigator',
        columns: 'Columns',
        sqlQuery: 'SQL-query',
        search: 'Search'
    },
    'ru': {
        connections: 'Соединения',
        databaseNavigator: 'Объекты БД',
        columns: 'Колонки',
        sqlQuery: 'SQL-запрос',
        search: 'Поиск'
    },
    'es': {
        connections: 'Conexiones',
        databaseNavigator: 'Objetos de BD',
        columns: 'Oradores',
        sqlQuery: 'SQL-consulta',
        search: 'Buscar'
    }
};

const i18n = new VueI18n({
    locale: 'en',
    fallbackLocale: 'ru',
    messages,
});

export default i18n;
