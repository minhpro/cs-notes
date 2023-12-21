# Installation

FreeBSD

`pkg install emacs`

MacOS

`brew install emacs`

# The Organization of the Screen

On the graphical display, Emacs occupies a graphical window. On a text terminal, Emacs occupies the entire terminal screen. The term `frame` means a graphical window or terminal screen occupied by Emacs.

At the top of the frame is a `menu bar`, which allows you to access commands via a series of menus. On a graphical display, below the menu bar is a `tool bar`, a row of icons that performs editing commands when you click on them.
At the very bottom of the frame is an `echo area`, where informative messages are displayed and where you enter information when Emacs asks for it.

The main area of the frame, below the tool bar (if one exists) and above the echo area, is called the `window`. An Emacs window is where the `buffer` - the text or other graphics you are editing or viewing - is displayed.
The last line of the window is a `mode line`. It displays various information about what going on in the buffer, such as whether there are unsaved changes, the editing modes that are in use, the current line number, and so forth.

The text you are editing in Emacs resides in a data structure called `buffer`. Each time you visit a file, a buffer is used to hold the file's text. Each time you invoke directory, a buffer is used to hold the directory listing.
If you send a message with `C-x m`, a buffer is used to hold the text of the message. When you ask for a command's documentation, that appears in a buffer named `*Help*`.

When a buffer is displayed in a window, its name is shown in the mode line. A newly started Emacs has several buffers, one of it is `*scratch*`, which can be used for evaluating Lisp expressions and is not associated with any file.
Aside from its textual contents, each buffer contains several pieces of information, such as what file it is visiting (if any), whether it is modied and what major mode and minor mode are in effect. These are stored in `buffer-local variables`.

# Keys

To invoke commands, you have to use `modifier keys`. Two common modifier keys are `Control` (usaully labeled `Ctrl`), and `Meta` (usually labeled `Alt`).

* C-a : holding the `Ctrl` key while pressing `a`.
* M-a : holding the `Alt` key while pressing `a`.

Meta can be typed by two-character sequences starting with ESC.

* M-a = ESC a
* C-M-a = ESC C-a

