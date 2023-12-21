class Settings:
  # Store all settings for Alien Invasion.
  def __init__(self, ship_speed: int = 1, bullets_allowed: int = 30, alien_speed: int = 1.0):
    self.screen_width = 1200
    self.screen_height = 800
    self.bg_color = (230, 230, 230)
    self.ship_speed = ship_speed
    self.bullet_settings = BulletSettings()
    self.bullets_allowed = bullets_allowed
    self.space_between_alien = 30
    self.alien_speed = alien_speed

class BulletSettings:
  def __init__(self):
    self.bullet_speed = 2
    self.bullet_width = 3
    self.bullet_height = 15
    self.bullet_color = (60, 60, 60)