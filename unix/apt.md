Append to /etc/apt/sources.list

echo "deb http://dl.google.com/linux/chrome/deb/ stable main" >> /tmp/google-chrome.list

sudo mv /tmp/google-chrome.list /etc/apt/sources.list.d/
