var express = require('express')

//创建路由对象
var router = express.Router()

router.get('/',(req,res)=>{
  res.set({"A":"B"})
  res.status(201).send('Get Express!')
  res.end()
})
router.post('/',(req,res)=>{
  res.send('Post Express!')
})
router.put('/',(req,res)=>{
  res.send('Put Express!')
})
router.delete('/',(req,res)=>{
  res.send('Delete Express!')
})

module.exports = router