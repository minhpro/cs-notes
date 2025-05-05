// Build for production

import * as esbuild from 'esbuild'

await esbuild.build({
  entryPoints: ['./src/app.jsx'],
  bundle: true,
  minify: true,
  outdir: 'build',
})