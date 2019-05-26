<template>
    <div>
  <script type="text/x-template" id="item-template">
    <li>
      <div
        :class="{bold: isFolder}"
        @click="toggle"
        @dblclick="changeType">
        {{ model.name }}
        <span v-if="isFolder">[{{ open ? '-' : '+' }}]</span>
      </div>
      <ul v-show="open" v-if="isFolder">
        <item
          class="item"
          v-for="(model, index) in model.children"
          :key="index"
          :model="model">
        </item>
        <li class="add" @click="addChild">+</li>
      </ul>
    </li>
  </script>

  <p>(You can double click on an item to turn it into a folder.)</p>

  <ul id="demo">
    <item
      class="item"
      :model="treeData">
    </item>
  </ul>
</div>
</template>

<script>
export default {
  name: 'TreeMenuComponent'
}
</script>

<style scoped>

</style>
