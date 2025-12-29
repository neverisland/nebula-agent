<template>
  <a-date-picker 
    v-model:value="expireTime" 
    :show-time="showTime"
    :placeholder="placeholder" 
    :value-format="valueFormat"
    style="width: 100%"
    :disabled="disabled"
    :locale="zhCNLocale"
    :format="dateFormat"
  />
</template>

<script lang="ts">
import dayjs from 'dayjs';
import 'dayjs/locale/zh-cn';
import zhCN from 'ant-design-vue/es/locale/zh_CN';

dayjs.locale('zh-cn');

interface Props {
  modelValue: string;
  showTime: boolean;
  disabled: boolean;
  placeholder: string;
}

export default {
  name: 'ExpireTimePicker',
  props: {
    modelValue: {
      type: String,
      default: ''
    },
    showTime: {
      type: Boolean,
      default: false
    },
    disabled: {
      type: Boolean,
      default: false
    },
    placeholder: {
      type: String,
      default: '选择过期时间'
    }
  },
  emits: ['update:modelValue'],
  computed: {
    expireTime: {
      get(this: Props & { $emit: (event: string, value: any) => void }): string {
        return this.modelValue;
      },
      set(this: Props & { $emit: (event: string, value: any) => void }, val: string): void {
        this.$emit('update:modelValue', val);
      }
    },
    valueFormat(this: Props): string {
      return this.showTime ? 'YYYY-MM-DD HH:mm:ss' : 'YYYY-MM-DD';
    },
    dateFormat(this: Props): string {
      return this.showTime ? 'YYYY-MM-DD HH:mm:ss' : 'YYYY-MM-DD';
    },
    zhCNLocale(): any {
      return zhCN.DatePicker;
    }
  }
};
</script>

