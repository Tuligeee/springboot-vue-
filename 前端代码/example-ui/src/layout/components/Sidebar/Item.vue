<script>
export default {
  name: 'MenuItem',
  functional: true,
  props: {
    icon: {
      type: String,
      default: ''
    },
    title: {
      type: String,
      default: ''
    }
  },
  render(h, context) {
    const { icon } = context.props
    let { title } = context.props
    const vnodes = []

    if (icon) {
      vnodes.push(<svg-icon icon-class={icon}/>)
    }

    if (title) {
      // --- 资深程序员优化：前端实时标题映射转换 ---
      const titleMap = {
        '招生信息管理': '院校招生查询',
        '本校专业管理': '本校专业查询',
        '填报管理': '志愿模拟填报',
        '高考资讯管理': '政策资讯中心',
        '档线信息管理': '历年分数线查询',
        '轮播图管理': '首页内容展示'
      };
      
      // 如果命中映射表，则进行替换；否则进行通用“管理 -> 查询”模糊替换
      if (titleMap[title]) {
        title = titleMap[title];
      } else if (title.includes('管理') && !title.includes('系统')) {
        // 排除掉“系统管理”这类核心词，其他业务管理统一对学生友好化
        title = title.replace('管理', '查询');
      }
      // --- 优化结束 ---

      vnodes.push(<span slot='title'>{(title)}</span>)
    }
    return vnodes
  }
}
</script>
