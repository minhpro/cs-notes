var http = require('http');

var server = http.createServer(function (req, res) {
  setTimeout(function () { //simulate a long request
    res.writeHead(200, {'Content-Type': 'text/plain'});
    res.end('Hello World\n');
  }, 5000);
}).listen(9090, function (err) {
  console.log('listening http://localhost:9090/');
  console.log('pid is ' + process.pid)
});

process.on('SIGTERM', function () {
  console.warn('Terminate the server!')
  server.close(function () {
    process.exit(0);
  });
});
