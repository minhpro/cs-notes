import uvicorn
from app import app

if __name__ == "__main__":
    uvicorn.run(app=app, port=8000, host='localhost')
