<template>
  <div class="console-main-page" :class="{ 'dark-theme': isDarkTheme }">
    <!-- 欢迎横幅 -->
    <div class="welcome-banner">
      <div class="welcome-content">
        <div class="welcome-text">
          <h1>👋 欢迎回来</h1>
          <p class="welcome-subtitle">今天是个好日子，让我们开始高效工作吧！</p>
        </div>
        <div class="date-display">
          <div class="date-box">
            <span class="date-day">{{ currentDate.day }}</span>
            <div class="date-info">
              <span class="date-month">{{ currentDate.month }}</span>
              <span class="date-weekday">{{ currentDate.weekday }}</span>
            </div>
          </div>
        </div>
      </div>
    </div>

    <!-- 统计卡片 -->
    <div class="stats-section">
      <a-row :gutter="[24, 24]">
        <a-col :xs="24" :sm="12" :lg="8" v-for="stat in stats" :key="stat.title">
          <div class="stat-card" :style="{ '--accent-color': stat.color }">
            <div class="stat-icon">
              <component :is="stat.icon" />
            </div>
            <div class="stat-info">
              <span class="stat-value">{{ stat.value }}</span>
              <span class="stat-title">{{ stat.title }}</span>
            </div>
          </div>
        </a-col>
      </a-row>
    </div>

    <!-- 快捷入口 -->
    <div class="quick-access-section">
      <h2 class="section-title">
        <ThunderboltOutlined />
        快捷入口
      </h2>
      <a-row :gutter="[16, 16]">
        <a-col :xs="24" :sm="8" v-for="action in quickActions" :key="action.title">
          <div class="quick-action-card" @click="handleQuickAction(action)">
            <div class="action-icon" :style="{ background: action.gradient }">
              <component :is="action.icon" />
            </div>
            <span class="action-title">{{ action.title }}</span>
            <span class="action-desc">{{ action.description }}</span>
          </div>
        </a-col>
      </a-row>
    </div>
  </div>
</template>

<script lang="ts">
import {
  FileOutlined,
  FolderOutlined,
  CloudUploadOutlined,
  ShareAltOutlined,
  ThunderboltOutlined,
  PlusOutlined
} from '@ant-design/icons-vue';
import { getHomeStatistics } from '@/api/HomeApi';

export default {
  name: "ConsoleMainPage",
  components: {
    FileOutlined,
    FolderOutlined,
    CloudUploadOutlined,
    ShareAltOutlined,
    ThunderboltOutlined,
    PlusOutlined
  },
  data() {
    return {
      stats: [
        { title: '我的文件', value: 0, icon: 'FileOutlined', color: '#1890ff' },
        { title: '个人空间', value: 0, icon: 'FolderOutlined', color: '#52c41a' },
        { title: '存储已用', value: '0 B', icon: 'CloudUploadOutlined', color: '#fa8c16' }
      ],
      quickActions: [
        {
          title: '上传文件',
          description: '快速上传新文件',
          icon: 'CloudUploadOutlined',
          gradient: 'linear-gradient(135deg, #667eea 0%, #764ba2 100%)',
          route: '/console/page/file-library'
        },
        {
          title: '新建空间',
          description: '创建个人空间',
          icon: 'PlusOutlined',
          gradient: 'linear-gradient(135deg, #11998e 0%, #38ef7d 100%)',
          route: '/console/page/personal-space'
        },
        {
          title: '分享管理',
          description: '管理我的分享',
          icon: 'ShareAltOutlined',
          gradient: 'linear-gradient(135deg, #ee0979 0%, #ff6a00 100%)',
          route: '/console/page/my-share'
        }
      ]
    };
  },
  computed: {
    isDarkTheme() {
      return this.$store.getters['theme/isDarkTheme'];
    },
    currentDate() {
      const now = new Date();
      const weekdays = ['周日', '周一', '周二', '周三', '周四', '周五', '周六'];
      const months = ['1月', '2月', '3月', '4月', '5月', '6月', '7月', '8月', '9月', '10月', '11月', '12月'];
      return {
        day: now.getDate(),
        month: months[now.getMonth()],
        weekday: weekdays[now.getDay()]
      };
    }
  },
  mounted() {
    this.loadStatistics();
  },
  methods: {
    /**
     * 加载统计数据
     */
    async loadStatistics() {
      try {
        const res = await getHomeStatistics();
        if (res.data && res.data.data) {
          const data = res.data.data;
          this.stats[0].value = data.fileCount;
          this.stats[1].value = data.spaceCount;
          this.stats[2].value = this.formatBytes(data.storageUsed);
        }
      } catch (error) {
        console.error('Failed to load statistics:', error);
      }
    },
    /**
     * 格式化字节数
     */
    formatBytes(bytes: number): string {
      if (bytes === 0) return '0 B';
      const k = 1024;
      const sizes = ['B', 'KB', 'MB', 'GB', 'TB'];
      const i = Math.floor(Math.log(bytes) / Math.log(k));
      return parseFloat((bytes / Math.pow(k, i)).toFixed(2)) + ' ' + sizes[i];
    },
    /**
     * 处理快捷入口点击
     */
    handleQuickAction(action: { route: string }) {
      if (action.route) {
        this.$router.push(action.route);
      }
    }
  }
};
</script>

<style scoped>
.console-main-page {
  padding: 24px;
  min-height: 100%;
  background-color: #f0f2f5;
}

/* 黑暗模式适配 - 如果使用了 ant-design-vue 的 dark theme，通常会在 html 或 body 上加 data-theme='dark' 或 class='dark' */
:deep(.ant-layout-content) {
  background-color: #f0f2f5;
}

/* 欢迎横幅 */
.welcome-banner {
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  border-radius: 16px;
  padding: 32px;
  margin-bottom: 24px;
  box-shadow: 0 10px 40px rgba(102, 126, 234, 0.3);
  color: white;
}

.welcome-content {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.welcome-text h1 {
  color: white;
  font-size: 28px;
  font-weight: 600;
  margin: 0 0 8px 0;
}

.welcome-subtitle {
  color: rgba(255, 255, 255, 0.85);
  font-size: 16px;
  margin: 0;
}

.date-box {
  display: flex;
  align-items: center;
  gap: 12px;
  background: rgba(255, 255, 255, 0.2);
  backdrop-filter: blur(10px);
  padding: 16px 24px;
  border-radius: 12px;
}

.date-day {
  font-size: 42px;
  font-weight: 700;
  color: white;
  line-height: 1;
}

.date-info {
  display: flex;
  flex-direction: column;
  gap: 2px;
}

.date-month,
.date-weekday {
  color: rgba(255, 255, 255, 0.9);
  font-size: 14px;
}

/* 统计卡片 */
.stats-section {
  margin-bottom: 24px;
}

.stat-card {
  background: white;
  border-radius: 12px;
  padding: 24px;
  display: flex;
  align-items: center;
  gap: 16px;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.06);
  border: 1px solid #f0f0f0;
  transition: all 0.3s ease;
  position: relative;
  overflow: hidden;
}

.stat-card::before {
  content: '';
  position: absolute;
  left: 0;
  top: 0;
  bottom: 0;
  width: 4px;
  background: var(--accent-color);
}

.stat-card:hover {
  transform: translateY(-4px);
  box-shadow: 0 8px 24px rgba(0, 0, 0, 0.1);
  border-color: var(--accent-color);
}

.stat-icon {
  width: 56px;
  height: 56px;
  border-radius: 12px;
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 24px;
  color: var(--accent-color);
  background: color-mix(in srgb, var(--accent-color) 10%, #f9f9f9);
}

.stat-info {
  display: flex;
  flex-direction: column;
  flex: 1;
}

.stat-value {
  font-size: 28px;
  font-weight: 700;
  color: rgba(0, 0, 0, 0.85);
  line-height: 1.2;
}

.stat-title {
  font-size: 14px;
  color: rgba(0, 0, 0, 0.45);
  margin-top: 4px;
}

/* 章节标题 */
.section-title {
  font-size: 18px;
  font-weight: 600;
  color: rgba(0, 0, 0, 0.85);
  margin: 0 0 16px 0;
  display: flex;
  align-items: center;
  gap: 8px;
}

/* 快捷入口 */
.quick-access-section {
  margin-bottom: 24px;
}

.quick-action-card {
  background: white;
  border-radius: 12px;
  padding: 24px;
  display: flex;
  flex-direction: column;
  align-items: center;
  text-align: center;
  gap: 12px;
  cursor: pointer;
  border: 1px solid #f0f0f0;
  transition: all 0.3s ease;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.06);
}

.quick-action-card:hover {
  transform: translateY(-4px);
  box-shadow: 0 12px 32px rgba(0, 0, 0, 0.1);
  border-color: #1890ff;
}

.action-icon {
  width: 56px;
  height: 56px;
  border-radius: 14px;
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 24px;
  color: white;
}

.action-title {
  font-size: 15px;
  font-weight: 600;
  color: rgba(0, 0, 0, 0.85);
}

.action-desc {
  font-size: 12px;
  color: rgba(0, 0, 0, 0.45);
}

/* 黑暗模式强制覆盖 - 挂载到 body.dark */
:global(body.dark) .console-main-page {
  background-color: #141414 !important;
}

:global(body.dark) .stat-card,
:global(body.dark) .quick-action-card {
  background-color: #1f1f1f !important;
  border-color: #303030 !important;
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.5) !important;
}

:global(body.dark) .stat-value,
:global(body.dark) .section-title,
:global(body.dark) .action-title {
  color: rgba(255, 255, 255, 0.85) !important;
}

:global(body.dark) .stat-title,
:global(body.dark) .action-desc {
  color: rgba(255, 255, 255, 0.45) !important;
}

:global(body.dark) .stat-icon {
  background: rgba(255, 255, 255, 0.05) !important;
}

/* 响应式 */
@media (max-width: 768px) {
  .console-main-page {
    padding: 16px;
  }

  .welcome-content {
    flex-direction: column;
    gap: 16px;
    text-align: center;
  }

  .welcome-text h1 {
    font-size: 22px;
  }

  .date-box {
    padding: 12px 20px;
  }

  .date-day {
    font-size: 32px;
  }
}
</style>

