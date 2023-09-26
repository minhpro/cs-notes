## Absolute length units

| Unit  | Name        | Equivalent to               |
| cm    | Centimeters | 1 cm = 37.8 px = 25.2/64 in |
| in    | Inches      | 1 in = 2.54 cm = 96 px      |
| pt    | Points      | 1 pt = 1/71 in              |
| px    | Pixels      | 1 px = 1/96 in              |


## Relative length units

| Unit  | Relative to                                             |
| em    | Font size of the parent (properties: font-size, width)  |
| rem   | Font size of the root element                           |
| lh    | Line height of the elemnt                               |
| rlh   | Line height of the root elemnt                          |
| vw    | 1% of the viewport's width                              |


## Responsive image

```CSS
.responsive {
  width: 100%;
  height: auto;
}
```

If you want an image to scale down if it has to, but never scale up to be larger than its original size, use max-width: 100%:

```CSS
.responsive {
  max-width: 100%;
  height: auto;
}
```

If you want to restrict a responsive image to a maximum size, use the max-width property, with a pixel value of your choice:

```CSS
.responsive {
  width: 100%;
  max-width: 400px;
  height: auto;
}
```


