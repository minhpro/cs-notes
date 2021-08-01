Want to graceful shutdown the application running inside the container. For example, the graceful shutdown of a webserver means we want to wait until all current requests have completed before shutdown

## Graceful shutdown in Node.js

**Unix signal** 

> A signal is an asynchronous notification sent to a process or to a specific thread within the same process in order to notify it of an event that occurred

There are two signals used to terminate a program:
* *SIGNTERM* is used to cause a program termination. It is a polite way to ask a program to terminate. The program can either handle this signal, clean up resources and then exit, or it can ignore the signal.
* *SIGKILL* is used to cause immediate termination. It can't be handled or ignored by the process.

Let try a simple node webserver application

```JS
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
```

We simulate a long service by delaying the response 5 seconds. In the middle of a request, sending SIGTERM to the program. The request will fail.

```JS
» curl http://localhost:9090 &
» kill 23703
[2] 23832
curl: (52) Empty reply from server
```

Fortunately, the http server has a close method that stops the server for receiving new connections and calls the callback once it finished handling all requests. This method comes from the NET module, so is pretty handy for any type of tcp connections.

Now, if I modify the example to something like this:

```JS
var http = require('http');

var server = http.createServer(function (req, res) {
  setTimeout(function () { //simulate a long request
    res.writeHead(200, {'Content-Type': 'text/plain'});
    res.end('Hello World\n');
  }, 4000);
}).listen(9090, function (err) {
  console.log('listening http://localhost:9090/');
  console.log('pid is ' + process.pid);
});

process.on('SIGTERM', function () {
  server.close(function () {
    process.exit(0);
  });
});
```

And then I use the same commands as above:

```JS
» curl http://localhost:9090 &
» kill 23703
Hello World
[1]  + 24730 done       curl http://localhost:9090
```

You will notice that the program doesn’t exit until it finished processing and serving the last request. More interesting is the fact that after the SIGTERM signal it doesn’t handle more requests:

```JS
» curl http://localhost:9090 &
[1] 25072

» kill 25070

» curl http://localhost:9090 &
[2] 25097

curl: (7) Failed connect to localhost:9090; Connection refused
[2]  + 25097 exit 7     curl http://localhost:9090

» Hello World
[1]  + 25072 done       curl http://localhost:9090
```

## Kubernetes graceful shutdown pods

How does Kubernetes terminate pods?

1. Send a SIGTERM signal to the main process (pid = 1) of containers
2. If a container doesn't terminate in a grace period, default to 30s, Kubernetes will send a SIGKILL to force terminate the container immediately.

**A common pitfall while handling the SIGTERM**

Let’s say your Dockerfile ends with a CMD in the shell form:

```sh
CMD myapp
```

The shell form runs the command with /bin/sh -c myapp, so the process that will get the SIGTERM is actually /bin/sh and not its child myapp. Depending on the actual shell you’re running, it could or could not pass the signal to its children.

For example, the shell shipped by default with Alpine Linux does not pass signals to children, while Bash does it. If your shell doesn’t pass signals to children, you’ve a couple of options to ensure the signal will be correctly delivered to the app

You can ensure your container includes Bash and run your command through it, in order to support environment variables passed as arguments.

```sh
CMD [ "/bin/bash", "-c", "myapp --arg=$ENV_VAR" ]
```
