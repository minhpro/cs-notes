# Instalation

MacOS installation

1. Install `TinyTex` first: https://yihui.org/tinytex/
2. Install pandoc: `brew install pandoc`.
3. Install librsvg: `brew install librsvg`

**Convert markdown to pdf**

Using pandoc

`pandoc document.md -s -o document.pdf`

`--pdf-engine=xelatex` with unicode

Reading:
- https://bookdown.org/yihui/rmarkdown-cookbook/

# Install missing Latex packages

When converting to pdf, you may miss some package:

```log
Error producing PDF.
! LaTeX Error: File `bookmark.sty' not found.

Type X to quit or <RETURN> to proceed,
or enter new name. (Default extension: sty)

Enter file name: 
! Emergency stop.
<read *> 
         
l.47 \IfFileExists
```

You need to install the missing package:

`tlmgr install <missing_package> # e.g. bookmark`

# Too deeply nested

If you document (e.g. markdown) have some very deeply nested enumeration, you need to install `enumitem` Latex package.

`tlmgr install enumitem`

And then extend both `enumerate` and `itemize` environments, by adding following comment to head of your document.

```tex
---
header-includes:
  - \usepackage{enumitem}
  - \setlistdepth{20}
  - \renewlist{itemize}{itemize}{20}
  - \renewlist{enumerate}{enumerate}{20}
  - \setlist[itemize]{label=$\cdot$}
  - \setlist[itemize,1]{label=\textbullet}
  - \setlist[itemize,2]{label=--}
  - \setlist[itemize,3]{label=*}
output:
  rmarkdown::pdf_document:
      keep_tex: yes
---
```

# Link color

Add variables to your command

```sh
-V colorlinks=true \
-V linkcolor=blue \
-V urlcolor=cyan \ 
-V toccolor=gray
```
