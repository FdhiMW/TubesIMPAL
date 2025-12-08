<template>
  <div v-if="visible" class="overlay">
    <div class="modal-box">
      <div class="icon-circle">
        ✓
      </div>

      <h2 class="title">{{ title }}</h2>

      <button class="ok-btn" @click="close">
        OK
      </button>
    </div>
  </div>
</template>

<script>
export default {
  name: 'SuccessModal',
  props: {
    visible: Boolean,
    title: {
      type: String,
      default: 'Berhasil',
    },
  },
  methods: {
    close() {
      this.$emit('close')
    },
  },
}
</script>

<style scoped>
.overlay {
  position: fixed;
  inset: 0;
  background: rgba(0,0,0,0.55);
  display: flex;
  align-items: center;
  justify-content: center;
  z-index: 9999;
  backdrop-filter: blur(2px);
}

.modal-box {
  background: #fff;
  width: 320px;
  padding: 28px 20px 24px;
  border-radius: 20px;
  text-align: center;
  box-shadow: 0 8px 22px rgba(0,0,0,0.15);
}

.icon-circle {
  width: 90px;
  height: 90px;
  margin: 0 auto 16px;
  border-radius: 50%;
  display: flex;
  font-size: 48px;
  color: white;
  justify-content: center;
  align-items: center;
  background: linear-gradient(135deg, #f97316, #ef4444);
}

.title {
  font-size: 20px;
  font-weight: 700;
  margin-bottom: 22px;
}

.ok-btn {
  width: 100%;
  padding: 10px 0;
  font-size: 15px;
  font-weight: 600;
  border: none;
  border-radius: 12px;
  color: white;
  cursor: pointer;
  background: linear-gradient(135deg, #f97316, #ef4444);
}
</style>
