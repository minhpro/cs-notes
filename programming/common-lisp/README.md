# Install Common Lisp Compiler - SBCL

https://lisp-lang.org/learn/getting-started/

MacOS

`brew install sbcl`

Ubuntu/Debinan

`sudo apt-get install sbcl`

Arch Linux

`sudo pacman -S sbcl`

Next, setup Quicklisp, the package manager.

```sh
curl -o /tmp/ql.lisp http://beta.quicklisp.org/quicklisp.lisp
sbcl --no-sysinit --no-userinit --load /tmp/ql.lisp \
       --eval '(quicklisp-quickstart:install :path "~/.quicklisp")' \
       --eval '(ql:add-to-init-file)' \
       --quit
```

# Learning sources

- https://www.youtube.com/@CBaggers/featured
- https://lisp-lang.org/learn/getting-started/
- https://lispcookbook.github.io/cl-cookbook