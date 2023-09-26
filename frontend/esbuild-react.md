## esbuild Overview

* Source: https://github.com/evanw/esbuild
* Doc: https://esbuild.github.io/

`esbuild` là một bundler tool dành cho phát triển web. Hiệu năng của `esbuild` là hơn hẳn so với các tool khác như: Webpack, rollup, parcel. `esbuild` được viết đa phần bằng Golang.

Một điểm nổi bật của `esbuild` là nó cung cấp API rất cụ thể và rõ ràng, cho phép tích hợp dễ dàng với CLI, JS, và Golang. Bên cạnh đó thì tài liệu hướng dẫn sử dụng API cũng khá chi tiết, và có kèm theo ví dụ mô tả.

## Setup React project with esbuild

Initialize project

```sh
mkdir esbuild-react
cd esbuild-react
npm init -y
```

Install esbuild

`npm install --save-exact --save-dev esbuild`

Install react

`npm install react react-dom`

Write an entrypoint source file (`src/app.jsx`)

```jsx
import React from "react"
import { createRoot } from 'react-dom/client'

function App() {
  return (
    <div>
      <p>Hello, esbuild + React</p>
    </div>
  )
}

const container = document.getElementById('root')
const root = createRoot(container) // createRoot(container!) if you use TypeScript

root.render(<App />)
```

Write `esbuild.mjs` script (ECMAScript modules), bạn cũng có thể dùng Golang thay cho viết JS.

* https://esbuild.github.io/getting-started/#build-scripts

```js
import * as esbuild from 'esbuild'

await esbuild.build({
  entryPoints: ['src/app.jsx'],
  bundle: true,
  outdir: 'build',
})
```

Golang code

```go
package main

import "github.com/evanw/esbuild/pkg/api"
import "os"

func main() {
  result := api.Build(api.BuildOptions{
    EntryPoints: []string{"src/app.jsx"},
    Bundle:      true,
    Outdir:      "build",
  })
  if len(result.Errors) != 0 {
    os.Exit(1)
  }
}
```

Tạo một `index.html` file để sử dụng các files (js, css, ...) sẽ được build.

```html
<!DOCTYPE html>
<html>
<head>
  {...}
  <link rel="stylesheet" href="build/app.css"> <!-- load CSS -->
</head>
<body>
  <div id="root"></div>
  <script src="build/app.js"></script> <!-- load JS -->
</body>
</html>
```

Thêm build command vào `package.json`

```json
{
  "scripts": {
    "build": "node esbuild.mjs"
  }
}
```

Thực hiện build project

`npm run build`

Kết quả build nằm trong thư mục `build`:

```sh
├── build
│   ├── app.js
├── src
│   ├── app.jsx
├── esbuild.mjs
├── package.json
├── index.html
```

Now, visit the `index.html` from your browser.

## Enable live reload when develop project

Setup một local server để xem được kết quả build project, và cấu hình tự động rebuild project khi có file thay đổi bằng cách sử dụng `watch` and `serve` API.

Sửa file `esbuild.mjs` như sau:

```js
let ctx = await esbuild.context({
  entryPoints: ['src/app.jsx'],
  bundle: true,
  outdir: 'build',
})

await ctx.watch()

let { host, port } = await ctx.serve({
  servedir: '.',
  host: '127.0.0.1', // default is 0.0.0.0
  port: 8000, // default is one from range [8000 ... 8009]
})

console.log(`Dev server is running at address ${host} and port ${port}!`)
```

Visit the url `http://localhost:8000` để xem giao diện web đã được build.

Nếu muốn webrowser tự động reload khi có file thay đổi và rebuild thành công, sử dụng `EventSource` API (https://developer.mozilla.org/en-US/docs/Web/API/EventSource). Thêm đoạn code sau vào app script.

```js
new EventSource('/esbuild').addEventListener('change', () => location.reload())
```

References:

* https://esbuild.github.io/api/#live-reload
* https://esbuild.github.io/api/#rebuild

## Some options to support project development

Một số option có thể cài đặt nhằm hỗ trợ quá trình phát triển và debug project.

### Sourcemap

https://esbuild.github.io/api/#sourcemap

Source maps can make it easier to debug your code. This is also useful if you prefer looking at individual files in your browser's developer tools instead of one big bundled file.

```js
await esbuild.build({
  entryPoints: ['src/app.jsx'],
  bundle: true,
  outdir: 'build',
  sourcemap: true,
})
```

### Build log level

https://esbuild.github.io/api/#log-level

Tùy chỉnh cho phép `esbuild` show thông tin khi build (hay transform API)

```js
await esbuild.build({
  entryPoints: ['src/app.jsx'],
  bundle: true,
  outdir: 'build',
  sourcemap: true,
  logLevel: 'info'
})
```

## Build production

Khi build project để deploy production, một số tham số có thể cân nhắc sử dụng.

### Source map

Sourcemap thường phục vụ việc debug code, nếu không cần thì có thể disable option này.

### Minify

Option `minify` cho phép giảm kích thước build output bằng cách:

* Xóa các ký tự spaces không cần thiết.
* Chuyển đổi syntax ngắn gọn hơn.
* Đổi tên các biến local để giảm độ dài của tên biến.

```js
await esbuild.build({
  entryPoints: ['src/app.jsx'],
  bundle: true,
  outdir: 'build',
  minify: true,
})
```

## Using CSS

https://esbuild.github.io/content-types/#css

`esbuild` mặc định sẽ bundle CSS. `css` loader được enabled cho `.css` files và `local-css` loader được enabled cho `.module.css` files. CSS được ưu tiên hỗ trợ (first-class content type) trong esbuild, vì thế bạn có thể bundle CSS files mà không cần import CSS từ JavaScript code:

```JS
require('esbuild').buildSync({
  entryPoints: ['app.css'],
  bundle: true, 
  outfile: 'out.css'
})
```

Bạn có thể [@import](https://developer.mozilla.org/en-US/docs/Web/CSS/@import) other CSS files and references image cũng như font files với hàm [url](https://developer.mozilla.org/en-US/docs/Web/CSS/url) và esbuild sẽ bundle tất cả lại. Tuy vậy bạn cần cấu hình loader cho images hay font files, vì esbuild không có pre-configured cho các files này. Thường thì chúng ta sẽ sử dụng [data URL](https://esbuild.github.io/content-types/#data-url) hoặc [external file] (https://esbuild.github.io/content-types/#external-file) loader.

### Import CSS từ JavaScript

Khi import CSS từ JavaScript, esbuild sẽ gom tất cả các file CSS được referenced từ một entry point đã chỉ định. Cụ thể, nếu esbuild generates `app.js` thì nó cũng tạo file `app.css` chứa tất cả các file CSS được referenced bời `app.js`. Xem ví dụ sau:

```js
import './button.css'

export let Button = ({ text }) =>
  <div className="button">{text}</div>
```

The bundled JavaScript được generated từ esbuild sẽ không tự động import file CSS vào HTML page. Bạn cần import file CSS cùng với JS đã được generated vào trang HTML. Qua đó browser cũng có thể download CSS và JavaScript một cách song song. Cách import như sau:

```html
<html>
  <head>
    <link href="app.css" rel="stylesheet">
    <script defer src="app.js"></script>
  </head>
</html>
```

### Sử dụng CSS module

https://esbuild.github.io/content-types/#local-css

[CSS modules](https://github.com/css-modules/css-modules) là một CSS preprocessor dùng được tránh việc xung đột tên trong CSS. Class names trong CSS thường là global, nhưng CSS modules cung cấp giải pháp sử dụng CSS class names một cách local với file CSS.

Để sử dụng CSS modules trong esbuild, tạo file CSS với extension `.module.css` và import CSS module code trong file JS. Xét ví dụ sau:

```js
// app.js
import { myButton } from './button.module.css'
const div = document.createElement('div')
div.className = myButton
document.body.appendChild(div)
```

```css
/* button.module.css */
.myButton {
  text-align: center;
  border-radius: 8px;
  border-style: none;
}
```

Khi bundle với lệnh sau (có thể dùng API trong file JS)

`esbuild app.js --bundle --outdir=out`

Kết quả thu được sẽ như sau:

```js
// out/app.js
(() => {
  // app.module.css
  var myButton = "button_myButton";

  // app.js
  var div = document.createElement("div");
  div.className = myButton;
  document.body.appendChild(div);
})();
```

```css
/* out/app.css */
.button_myButton {
  text-align: center;
  border-radius: 8px;
  border-style: none;
}
```

Như đã thấy, tất cả CSS names sẽ được prefix bởi module name: `{module}_{cssName}`. Như ví dụ trên thì tên module là `button` (file `button.module.css`) và CSS name là `myButton`.
