import pygame

from settings import BulletSettings

class Bullet:
  def __init__(self, screen: pygame.surface.Surface, bullet_settings: BulletSettings, start_x: int):
    super().__init__()
    self.screen = screen
    self.settings = bullet_settings
    self.color = bullet_settings.bullet_color

    # Create a bullet rect at (0, 0) and then correct its position
    self.rect = pygame.Rect(0, 0, self.settings.bullet_width, self.settings.bullet_height)
    self.rect.midtop = start_x

  def update(self):
    self.rect.y -= self.settings.bullet_speed

  def draw_bullet(self):
    pygame.draw.rect(self.screen, self.color, self.rect)