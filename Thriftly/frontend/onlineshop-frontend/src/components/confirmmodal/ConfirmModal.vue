<template>
  <transition name="pop">
    <div v-if="show" class="overlay" @click.self="$emit('cancel')">
      <div class="modal" role="dialog" aria-modal="true">
        <div class="icon">
          <span class="icon-mark">!</span>
        </div>

        <h3 class="title">{{ title }}</h3>
        <p class="desc">{{ message }}</p>

        <div class="actions">
          <!-- TIDAK merah -->
          <button class="btn cancel" @click="$emit('cancel')">
            {{ cancelText }}
          </button>

          <!-- YA hijau -->
          <button class="btn ok" @click="$emit('confirm')">
            {{ okText }}
          </button>
        </div>
      </div>
    </div>
  </transition>
</template>

<script>
export default {
  name: 'ConfirmModal',
  props: {
    show: { type: Boolean, default: false },
    title: { type: String, default: 'Konfirmasi' },
    message: { type: String, default: '' },
    okText: { type: String, default: 'Ya' },
    cancelText: { type: String, default: 'Tidak' },
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
  padding: 26px 24px 20px;
  box-shadow: 0 24px 70px rgba(0, 0, 0, 0.25);
  text-align: center;
}

.icon {
  width: 86px;
  height: 86px;
  border-radius: 999px;
  margin: 0 auto 12px;
  display: grid;
  place-items: center;
  background: linear-gradient(135deg, rgba(255, 90, 60, 0.12), rgba(255, 159, 28, 0.12));
}

.icon-mark {
  font-weight: 900;
  font-size: 36px;
  color: #ff5a3c;
}

.title {
  margin: 6px 0 4px;
  font-size: 22px;
  font-weight: 800;
  color: #111827;
}

.desc {
  margin: 0 0 18px;
  font-size: 14px;
  color: #6b7280;
}

.actions {
  display: flex;
  gap: 12px;
  justify-content: center;
  margin-top: 4px;
}

.btn {
  border: none;
  cursor: pointer;
  border-radius: 999px;
  padding: 12px 18px;
  font-size: 14px;
  font-weight: 800;
  min-width: 150px;
  transition: transform 0.06s ease, box-shadow 0.12s ease;
}

.btn:active {
  transform: translateY(1px);
}

/* ❌ Tidak merah */
.btn.cancel {
  background: linear-gradient(135deg, #ef4444, #f97316);
  color: #fff;
}
.btn.cancel:hover {
  box-shadow: 0 14px 26px rgba(239, 68, 68, 0.25);
}

/* ✅ Ya hijau */
.btn.ok {
  background: linear-gradient(135deg, #16a34a, #22c55e);
  color: #fff;
}
.btn.ok:hover {
  box-shadow: 0 14px 26px rgba(34, 197, 94, 0.28);
}

/* anim */
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
