var http = require('http')

var app = http.createServer((req,res)=>{
  res.writeHead(200,{"contentType": "text/plain"})
  res.end('Hello World')
})

app.listen(3000, 'localhost')
console.log('http://localhost:3000')