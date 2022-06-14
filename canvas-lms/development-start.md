## Install Ruby 2.7.5

```
brew install asdf
asdf plugin add ruby
asdf install ruby 2.7.5
asdf global ruby 2.7.5
```

## Install node 14

```
nvm install 14
nvm use 14
```

## Ruby Gems

```
gem install bundler
bundle install
yarn install --pure-lockfile
```

## Config database

`createdb canvas_development` (postgres command)

Edit `config/database.yml`

```
development:
  adapter: postgresql
  encoding: utf8
  database: canvas_development
  host: 127.0.0.1
  user: postgres
  password: #{PASSWORD}
  timeout: 5000
```

## Prepare 

```
for config in amazon_s3 delayed_jobs domain file_store outgoing_mail security external_migration; \
          do cp -v config/$config.yml.example config/$config.yml; done
```

```
cp config/dynamic_settings.yml.example config/dynamic_settings.yml

RAILS_ENV=development bundle exec rails canvas:compile_assets

RAILS_ENV=development bundle exec rails db:initial_setup
```

Edit `config/outgoing_mail.yml`

Edit `config/cache_store.yml`

```
test:
  cache_store: redis_store
development:
  cache_store: redis_store
production:
  cache_store: redis_store
```

Edit `config/redis.yml`

```
development:
  servers:
    - redis://localhost
```

## Temple file upload

sudo chown minhnt: /tmp/attachment_fu

sudo chown www-data:www-data /tmp/attachment_fu
sudo chmod 777 /tmp/attachment_fu

## Start

bundle exec rails server

### Token

Token to integrate thirty part apps

### Install Canvas RCE

Run node application in production

https://www.digitalocean.com/community/tutorials/how-to-set-up-a-node-js-application-for-production-on-ubuntu-18-04

https://pm2.keymetrics.io/docs/usage/quick-start/


## Config Nginx + Passenger

https://www.phusionpassenger.com/library/install/nginx/install/oss/bionic/


## Install Passenger + Nginx module

Replace bionic with focal

sudo sh -c 'echo deb https://oss-binaries.phusionpassenger.com/apt/passenger focal main > /etc/apt/sources.list.d/passenger.list'

sudo apt update

sudo apt-get install -y libnginx-mod-http-passenger

Tạo file /etc/nginx/conf.d/mod-http-passenger.conf

Restart nginx

## Install Canvas RCE API

Install pm2

```
yarn global add pm2
```

Start Canvas RCE service

```
git clone https://github.com/instructure/canvas-rce-api
cd canvas-rce-api
yarn
pm2 start app.js --name canvas-rce-api
```

Configure Canvas dynamic_settings: /var/canvas/config/dynamic_settings.yaml

```
rich-content-service:
  # if you're running canvas-rce-api on its own
  #app-host: "rce.canvas.docker"
  #app-host: "http://115.146.127.51:3001"
  app-host: "https://your-domain/rce"
```

## Configure Nginx to proxy subpath to RCE-API

/etc/nginx/site-enabled/canvas.conf

```
location /rce/ {
    proxy_pass http:///127.0.0.1:3001
}
```

## Run delay job

`bundle exec script/delayed_job run`
