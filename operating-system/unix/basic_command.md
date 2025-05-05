## Find and replace recursive

Macos

`find . -type f -name '*.txt' -exec sed -i '' s/this/that/g {} +`


`find . -type f -name '*.java' -exec sed -i '' s/.lstation.impexp/.lstation.domain.impexp/g {} +`


jp.co.net.logicom -> jp.co.net.logicom.lstation

.lstation.impexp -> .lstation.domain.impexp

.common.dto.csv.master -> .model.dto

Find

`grep -r -i --include=\*.txt 'searchterm' ./`

- `-i`: ignore case