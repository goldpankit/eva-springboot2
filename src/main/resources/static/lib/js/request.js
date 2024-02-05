/**
 * 配置axios
 * @type {string}
 */
axios.defaults.headers.common['Content-Type'] = 'application/json;charset=UTF-8'
const request = axios.create({
  timeout: 30000
});
// 新建请求拦截器
request.interceptors.request.use(config => {
  // 设置操作平台
  config.headers['eva-platform'] = 'api-doc'
  // 设置请求来源
  config.headers['Request-Origion'] = 'Knife4j'
  return config
}, function (error) {
  return Promise.reject(error)
})
// 新建响应拦截器
request.interceptors.response.use((response) => {
  // 请求失败
  if (response.status !== 200) {
    return Promise.reject(new Error('服务器繁忙，请稍后再试'))
  }
  // 业务失败
  if (!response.data.success) {
    return Promise.reject(response.data)
  }
  return response.data.data
}, function (error) {
  console.error('error', error)
  if (error.code == null) {
    return Promise.reject(new Error('服务器繁忙，请稍后再试'))
  }
  if (error.code === 'ECONNABORTED' && error.message.indexOf('timeout') !== -1) {
    return Promise.reject(new Error('服务器响应超时，请稍后再试'))
  }
  return Promise.reject(error)
})