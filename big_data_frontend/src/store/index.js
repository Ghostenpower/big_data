// store.js
import Vue from 'vue';
import Vuex from 'vuex';

Vue.use(Vuex);

const store = new Vuex.Store({
  state: {
    // 试图从 localStorage 恢复数据，如果没有，则使用默认值
    user: JSON.parse(localStorage.getItem('user')) || null,
    cart: JSON.parse(localStorage.getItem('cart')) || []
  },
  mutations: {
    setUser(state, user) {
      state.user = user;
      // 保存到 localStorage
      localStorage.setItem('user', JSON.stringify(user));
    },
    addToCart(state, product) {
      state.cart.push(product);
      // 更新 cart 数据到 localStorage
      localStorage.setItem('cart', JSON.stringify(state.cart));
    },
    removeFromCart(state, productId) {
      state.cart = state.cart.filter(product => product.id !== productId);
      // 更新 cart 数据到 localStorage
      localStorage.setItem('cart', JSON.stringify(state.cart));
    }
  },
  actions: {
    // 可以在这里进行异步操作
    updateUser({ commit }, user) {
      commit('setUser', user);
    },
    addProductToCart({ commit }, product) {
      commit('addToCart', product);
    }
  }
});

export default store;