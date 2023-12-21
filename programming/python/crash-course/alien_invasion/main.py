from AlienInvasion import AlienInvasion
from settings import Settings

if __name__ == '__main__':
  settings = Settings(ship_speed=10)
  ai = AlienInvasion(settings)
  ai.run_game()
