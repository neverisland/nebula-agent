<template>
  <router-view/>
</template>

<style>
html, body {
  margin: 0;
  padding: 0;
  display: flex;
  flex-direction: column;
  height: 100vh;
}

#app {
  font-family: Avenir, Helvetica, Arial, sans-serif;
  -webkit-font-smoothing: antialiased;
  -moz-osx-font-smoothing: grayscale;
  height: 100%;
}

.title {
  font-size: 20px;
  font-weight: bold;
}

.form-inline {
  display: flex;
}

.dialog-footer {
  text-align: right;
  margin-top: 20px;
}
</style>
