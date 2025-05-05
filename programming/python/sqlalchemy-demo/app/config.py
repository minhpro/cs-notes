import os

from dotenv import load_dotenv

load_dotenv()

class Setting:
    db_host = os.getenv("DB_HOST")
    db_port = os.getenv("DB_PORT")
    if db_port is None:
        db_port = 5432
    db_user = os.getenv("DB_USER")
    db_password = os.getenv("DB_PASSWORD")
    db_name = os.getenv("DB_NAME")

setting = Setting()
