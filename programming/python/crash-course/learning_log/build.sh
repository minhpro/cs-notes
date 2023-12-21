#! /bin/sh

pip install --upgrade pip
pip install -r requirements.txt
pip install -r requirements-live.txt

mkdir logs
python manage.py collectstatic
rm -rf logs
