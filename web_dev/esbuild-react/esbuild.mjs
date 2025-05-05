import * as esbuild from 'esbuild'

let ctx = await esbuild.context({
  entryPoints: [{out: 'app', in: 'src/app.jsx'}],
  // entryPoints: [{out: 'app', in: 'esbuild_event_source.js'}], // enable the browser auto reload when build success
  bundle: true,
  outdir: 'build',
  sourcemap: true,
  logLevel: 'info',
})

await ctx.watch()

let { host, port } = await ctx.serve({
  servedir: '.',
  host: '127.0.0.1', // default is 0.0.0.0
  port: 8000, // default is one from range [8000 ... 8009]
})

console.log(`Dev server is running at address ${host} and port ${port}!`)
