## Sed - Stream text editor

*Find and replace text in files recursively*

`find . -type f -name "*.txt" -print0 | xargs -0 sed -i '' -e 's/foo/bar/g'`

Here's how it works:

find . -type f -name '*.txt' finds, in the current directory (.) and below, all regular files (-type f) whose names end in .txt

| passes the output of that command (a list of filenames) to the next command

xargs gathers up those filenames and hands them one by one to sed
sed -i '' -e 's/foo/bar/g' means "edit the file in place, without a backup, and make the following substitution (s/foo/bar) multiple times per line (/g)" (see man sed)
