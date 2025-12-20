<template>
  <div class="console-main-page">
    <!-- 欢迎横幅 -->
    <a-card class="welcome-banner" :bordered="false">
      <div class="welcome-content">
        <div class="welcome-text">
          <a-typography-title :level="2" class="welcome-title">👋 欢迎回来</a-typography-title>
          <a-typography-paragraph class="welcome-subtitle">
            今天是个好日子，让我们开始高效工作吧！
          </a-typography-paragraph>
        </div>
        <div class="date-display">
          <div class="date-box">
            <div class="time-main">{{ currentDate.time }}</div>
            <div class="date-sep"></div>
            <span class="date-day">{{ currentDate.day }}</span>
            <div class="date-info">
              <span class="date-month">{{ currentDate.month }}</span>
              <span class="date-weekday">{{ currentDate.weekday }}</span>
            </div>
          </div>
        </div>
      </div>
    </a-card>

    <!-- 统计卡片 -->
    <div class="stats-section">
      <a-row :gutter="[24, 24]">
        <a-col :xs="24" :sm="12" :lg="8" v-for="stat in stats" :key="stat.title">
          <a-card hoverable class="stat-card" :body-style="{ padding: '24px' }">
            <div class="stat-card-inner">
              <div class="stat-icon-wrapper" :style="{ color: stat.color, background: `${stat.color}15` }">
                <component :is="stat.icon" />
              </div>
              <a-statistic :title="stat.title" :value="stat.value" class="stat-content">
                <template #suffix v-if="stat.suffix">{{ stat.suffix }}</template>
              </a-statistic>
            </div>
          </a-card>
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
          <a-card hoverable class="quick-action-card" @click="handleQuickAction(action)">
            <div class="action-icon" :style="{ background: action.gradient }">
              <component :is="action.icon" />
            </div>
            <div class="action-info">
              <div class="action-title">{{ action.title }}</div>
              <div class="action-desc">{{ action.description }}</div>
            </div>
          </a-card>
        </a-col>
      </a-row>
    </div>
  </div>
</template>

<script lang="ts">
import { defineComponent } from 'vue';
import {
  FileOutlined,
  FolderOutlined,
  CloudUploadOutlined,
  ShareAltOutlined,
  ThunderboltOutlined,
  PlusOutlined
} from '@ant-design/icons-vue';
import { getFileLibraryStatistics } from "@/api/FileLibraryApi";
import { getFileSpaceStatistics } from "@/api/FileSpaceApi";

export default defineComponent({
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
      timer: null as any,
      currentTime: new Date(),
      stats: [
        { title: '我的文件', value: 0, icon: 'FileOutlined', color: '#1890ff', suffix: '' },
        { title: '个人空间', value: 0, icon: 'FolderOutlined', color: '#52c41a', suffix: '' },
        { title: '存储已用', value: '0', icon: 'CloudUploadOutlined', color: '#fa8c16', suffix: 'B' }
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
    currentDate() {
      const now = this.currentTime;
      const weekdays = ['周日', '周一', '周二', '周三', '周四', '周五', '周六'];
      const months = ['1月', '2月', '3月', '4月', '5月', '6月', '7月', '8月', '9月', '10月', '11月', '12月'];
      
      const formatNumber = (n: number) => n < 10 ? `0${n}` : n;
      const hours = formatNumber(now.getHours());
      const minutes = formatNumber(now.getMinutes());
      const seconds = formatNumber(now.getSeconds());

      return {
        day: now.getDate(),
        month: months[now.getMonth()],
        weekday: weekdays[now.getDay()],
        time: `${hours}:${minutes}:${seconds}`
      };
    }
  },
  mounted() {
    this.loadStatistics();
    this.startTimer();
  },
  beforeUnmount() {
    this.stopTimer();
  },
  methods: {
    /**
     * 启动定时器
     */
    startTimer() {
      this.timer = setInterval(() => {
        this.currentTime = new Date();
      }, 1000);
    },
    /**
     * 停止定时器
     */
    stopTimer() {
      if (this.timer) {
        clearInterval(this.timer);
      }
    },
    /**
     * 加载统计数据
     */
    async loadStatistics() {
      try {
        const [libraryRes, spaceRes] = await Promise.all([
          getFileLibraryStatistics(),
          getFileSpaceStatistics()
        ]);

        if (libraryRes.data && libraryRes.data.data) {
          const data = libraryRes.data.data;
          this.stats[0].value = data.fileCount;
          const { value, suffix } = this.formatBytes(data.storageUsed);
          this.stats[2].value = value;
          this.stats[2].suffix = suffix;
        }

        if (spaceRes.data && spaceRes.data.data) {
          const data = spaceRes.data.data;
          this.stats[1].value = data.spaceCount;
        }
      } catch (error) {
        console.error('Failed to load statistics:', error);
      }
    },
    /**
     * 格式化字节数
     */
    formatBytes(bytes: number): { value: string | number, suffix: string } {
      if (bytes === 0) return { value: 0, suffix: 'B' };
      const k = 1024;
      const sizes = ['B', 'KB', 'MB', 'GB', 'TB'];
      const i = Math.floor(Math.log(bytes) / Math.log(k));
      return {
        value: parseFloat((bytes / Math.pow(k, i)).toFixed(2)),
        suffix: sizes[i]
      };
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
});
</script>

<style scoped>
.console-main-page {
  padding: 24px;
  min-height: 100%;
}

/* 欢迎横幅 - 保持渐变效果但使用 a-card 封装 */
.welcome-banner {
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  border-radius: 16px;
  margin-bottom: 24px;
  color: white;
}

.welcome-content {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.welcome-title {
  color: white !important;
  margin: 0 0 8px 0 !important;
}

.welcome-subtitle {
  color: rgba(255, 255, 255, 0.85) !important;
  font-size: 16px;
  margin-bottom: 0 !important;
}

.date-box {
  display: flex;
  align-items: center;
  gap: 16px;
  background: rgba(255, 255, 255, 0.2);
  backdrop-filter: blur(10px);
  padding: 16px 24px;
  border-radius: 12px;
}

.time-main {
  font-size: 32px;
  font-weight: 700;
  color: white;
  line-height: 1;
  font-family: monospace;
}

.date-sep {
  width: 1px;
  height: 32px;
  background: rgba(255, 255, 255, 0.3);
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

.stat-card-inner {
  display: flex;
  align-items: center;
  gap: 16px;
}

.stat-icon-wrapper {
  width: 48px;
  height: 48px;
  border-radius: 12px;
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 24px;
}

.stat-content :deep(.ant-statistic-title) {
  margin-bottom: 4px;
}

.stat-content :deep(.ant-statistic-content) {
  font-weight: 700;
}

/* 章节标题 */
.section-title {
  font-size: 18px;
  font-weight: 600;
  margin: 0 0 16px 0;
  display: flex;
  align-items: center;
  gap: 8px;
}

/* 快捷入口 */
.quick-access-section {
  margin-bottom: 24px;
}

.quick-action-card :deep(.ant-card-body) {
  padding: 24px;
  display: flex;
  flex-direction: column;
  align-items: center;
  text-align: center;
  gap: 12px;
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
  margin-bottom: 8px;
}

.action-title {
  font-size: 15px;
  font-weight: 600;
}

.action-desc {
  font-size: 12px;
  opacity: 0.6;
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

  .welcome-title {
    font-size: 24px !important;
  }

  .date-box {
    padding: 12px 20px;
  }

  .date-day {
    font-size: 32px;
  }
}
</style>

