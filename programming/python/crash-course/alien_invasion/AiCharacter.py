from pygame.surface import Surface
import pygame

class Character:
  def __init__(self, screen: Surface, image_path: str, move_speed: int = 1):
    self.screen = screen
    self.screen_rect = screen.get_rect()

    self.image = pygame.image.load(image_path)
    self.rect = self.image.get_rect()

    self.rect.midbottom = self.screen_rect.midbottom
    self.move_right = False
    self.move_left = False
    self.move_speed = move_speed

  def blitme(self):
    # Draw the ship at its current location.
    self.screen.blit(self.image, self.rect)
  
  def blitme_at(self, at_point: (int, int)):
    # Draw the ship at its current location.
    draw_rect = self.rect.__copy__()
    draw_rect.centerx = at_point[0]
    draw_rect.centery = at_point[1]
    self.screen.blit(self.image, draw_rect)      
  
  def handle_key_down(self, event_key: int):
    if event_key == pygame.K_RIGHT:
      self.move_right = True     
    elif event_key == pygame.K_LEFT:
      self.move_left = True

  def handle_key_up(self, event_key: int):
    if event_key == pygame.K_RIGHT:
      self.move_right = False     
    elif event_key == pygame.K_LEFT:
      self.move_left = False

  def update(self):
    if self.move_right:
      self.move_to_right(self.move_speed)
    elif self.move_left:
      self.move_to_left(self.move_speed)
  
  def move_to_right(self, pixels: int = 1):
    self.rect.x += pixels
    screen_right = self.screen_rect.right;
    if self.rect.right > screen_right:
      self.rect.right = screen_right

  def move_to_left(self, pixels: int = 1):
    self.rect.x -= pixels
    if self.rect.left < 0:
      self.rect.left = 0

  def move_down(self, pixels: int = 1):
    self.rect.y += pixels
    screen_top = self.screen_rect.top;
    if self.rect.top > screen_top:
      self.rect.top = screen_top

  def move_up(self, pixels: int = 1):
    self.rect.y -= pixels
    if self.rect.bottom < 0:
      self.rect.bottom = 0
