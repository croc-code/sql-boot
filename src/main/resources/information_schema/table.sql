create table ${schema}.${table} (
  <#list srv.get("column") as c>
  ${c.name?right_pad(32)} ${c.getProp("data_type")}${c.getProp("length")}<#sep>,
</#list>);