import pygame

class Alien:
  # move_direction 1: right, -1: left
  def __init__(self, rect: pygame.rect.Rect, move_direction: int = 1, speed: int = 1):
    self.rect = rect
    self.move_direction = move_direction
    self.speed = speed
  
  def update(self, max_x):
    self.rect.x += self.move_direction * self.speed
    if self.rect.x >= max_x:
      self.rect.x = 0
    elif self.rect.x <= 0:
      self.rect.x = max_x

class AlienGroup:
  def __init__(self, screen: pygame.surface.Surface, image_path: str = 'images/alien.bmp'):
    self.screen = screen
    self.image = pygame.image.load(image_path)
    self.init_rect = self.image.get_rect()
    
    # Start each new alien near the top left of the screen
    self.init_rect.x = self.init_rect.width
    self.init_rect.y = self.init_rect.height

    self.aliens: list[Alien] = []

  def generate_alien(self, move_direction):
    alien = Alien(self.init_rect.copy(), move_direction)
    self.aliens.append(alien)

  def generate_alien_at(self, at_x: int, move_direction):
    alien = Alien(self.init_rect.copy(), move_direction)
    alien.rect.x = at_x
    self.aliens.append(alien)

  def update_aliens(self):
    for alien in self.aliens:
      alien.update(self.screen.get_rect().width)
  
  def draw_aliens(self):
    for alien in self.aliens:
      self.screen.blit(self.image, alien.rect) 
