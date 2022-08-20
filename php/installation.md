## Deployment setup

Install PHP

1. Install php

`brew install php@7.4`

3. Install brew-php-switcher

`brew install brew-php-switcher`

3. Switch to php@7.4

`brew-php-switcher 7.4`

4. Add php to PATH

`echo 'export PATH="/opt/homebrew/opt/php@7.4/bin:$PATH"' >> ~/.zshrc`