## Setup React project with Esbuild

https://esbuild.github.io/getting-started

Install esbuild

`npm install --save-exact --save-dev esbuild`

Install react

`npm install react react-dom`

Write `esbuild.mjs` script

* https://esbuild.github.io/getting-started/#build-scripts

Config loader to work with files (optional): images, ...

* https://esbuild.github.io/api/#loader

Add JS and CSS to the page such as

```html
...
<head>
  ...
  <link rel="stylesheet" href="dist/app.css">
  ...
</head>
<body>
  <div id="root"></div>
  <script src="dist/app.js"></script>
</body>
```

Enable live reload when develop project

* https://esbuild.github.io/api/#live-reload
* https://esbuild.github.io/api/#rebuild

## Some options to support develop project

### Sourcemap

https://esbuild.github.io/api/#sourcemap

Source maps can make it easier to debug your code. This is also useful if you prefer looking at individual files in your browser's developer tools instead of one big bundled file

## Using CSS

### CSS module

https://esbuild.github.io/content-types/#local-css
