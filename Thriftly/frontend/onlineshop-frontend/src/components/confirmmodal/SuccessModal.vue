<template>
  <transition name="pop">
    <div v-if="show" class="overlay" @click.self="$emit('close')">
      <div class="modal" role="dialog" aria-modal="true">
        <div class="icon">
          <span class="check">✓</span>
        </div>

        <h3 class="title">{{ title }}</h3>

        <button class="okbtn" @click="$emit('close')">
          {{ buttonText }}
        </button>
      </div>
    </div>
  </transition>
</template>

<script>
export default {
  name: 'SuccessModal',
  props: {
    show: { type: Boolean, default: false },
    title: { type: String, default: 'Berhasil' },
    buttonText: { type: String, default: 'OK' },
  },
}
</script>

<style scoped>
.overlay {
  position: fixed;
  inset: 0;
  background: rgba(15, 23, 42, 0.45);
  backdrop-filter: blur(2px);
  display: grid;
  place-items: center;
  z-index: 9999;
}

.modal {
  width: min(560px, calc(100vw - 32px));
  background: #fff;
  border-radius: 26px;
  padding: 28px 24px 22px;
  box-shadow: 0 24px 70px rgba(0, 0, 0, 0.25);
  text-align: center;
}

.icon {
  width: 92px;
  height: 92px;
  border-radius: 999px;
  margin: 0 auto 14px;
  display: grid;
  place-items: center;
  background: linear-gradient(135deg, #ff5a3c, #ff9f1c);
}

.check {
  color: #fff;
  font-size: 48px;
  font-weight: 900;
}

.title {
  margin: 6px 0 18px;
  font-size: 26px;
  font-weight: 900;
  color: #111827;
  line-height: 1.1;
}

.okbtn {
  width: 100%;
  border: none;
  cursor: pointer;
  border-radius: 999px;
  padding: 14px 18px;
  font-size: 16px;
  font-weight: 800;
  color: #fff;
  background: linear-gradient(135deg, #ff5a3c, #ff9f1c);
  transition: box-shadow 0.12s ease, transform 0.06s ease;
}

.okbtn:hover {
  box-shadow: 0 16px 30px rgba(255, 90, 60, 0.25);
}
.okbtn:active {
  transform: translateY(1px);
}

.pop-enter-active,
.pop-leave-active {
  transition: opacity 0.14s ease, transform 0.14s ease;
}
.pop-enter-from,
.pop-leave-to {
  opacity: 0;
  transform: translateY(8px) scale(0.98);
}
</style>
