DOM model

- [HTML Element](https://developer.mozilla.org/en-US/docs/Web/API/HTMLHtmlElement)

Get entire html document content

Get the root `<html>` element with document.documentElement then get its `.innerHTML`:

```js
const txt = document.documentElement.innerHTML;
alert(txt);
```

or its `.outerHTML` to get the `<html>` tag as well

```js
const txt = document.documentElement.outerHTML;
alert(txt);
```