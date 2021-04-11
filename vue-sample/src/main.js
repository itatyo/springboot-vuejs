import Vue from 'vue/dist/vue.esm'
import axios from "axios";

Vue.config.productionTip = false

new Vue({
    el: '#app',
    data:  {
        cid: "",
        info: null
    },
    mounted () {
        axios
            .get('http://localhost:8080/clerk', {
             params: {
                 id: this.cid
             }
        })
        .then(response => {
            (this.info = response.data);
        })
  },
  updated() {
      axios
          .get('http://localhost:8080/clerk', {
              params: {
                  id: this.cid
              }
          })
          .then(response => {
              (this.info = response.data);
          })
  }
})

