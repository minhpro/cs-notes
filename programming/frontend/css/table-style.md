## Styling your table

You cannot set `border` on a `tr`.

You need to use collapsing borders:

```CSS
table {
  border-collapse: collapse;
}

tr:nth-chilh(3) {
  border: solid 1px #999;
}
```