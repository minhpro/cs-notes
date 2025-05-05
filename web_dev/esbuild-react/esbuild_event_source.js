// https://esbuild.github.io/api/#live-reload
new EventSource('/esbuild').addEventListener('change', () => location.reload())

import './src/app.jsx'