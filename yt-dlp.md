https://github.com/yt-dlp/yt-dlp

Download

`yt-dlp -x --audio-format m4a <url>`

Download sub

`yt-dlp  --sub-lang en --all-subs --skip-download <>`

`yt-dlp --skip-download --write-subs --write-auto-subs  --sub-lang en --sub-format ttml --convert-subs srt --exec before_dl:"sed -e '/^[0-9][0-9]:[0-9][0-9]:[0-9][0-9].[0-9][0-9][0-9] --> [0-9][0-9]:[0-9][0-9]:[0-9][0-9].[0-9][0-9][0-9]$/d' -e '/^[[:digit:]]\{1,3\}$/d' -e 's/<[^>]*>//g' -e '/^[[:space:]]*$/d' -i '' %(requested_subtitles.:.filepath)#q" 'YOUR_YOUTUBE_URL'`