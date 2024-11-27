const express = require('express')
const router = require('./router_demo')
const app = express()

app.use((req, res, next) =>{
  console.log('我是执行所有请求的前置函数')
  next()
})
app.use('/user',router)


app.listen(3000,()=>{
  console.log('Express app at: http://localhost:3000')
})