import sys
import pygame

from AiCharacter import Character
from settings import Settings
from bullet import Bullet
from alien import AlienGroup

class AlienInvasion:
  # Manage game assets and behavior.
  def __init__(self, settings: Settings = Settings()):
    pygame.init()

    self.settings = settings
    # Full screen
    # self.screen = pygame.display.set_mode((0, 0), pygame.FULLSCREEN)
    self.screen = pygame.display.set_mode((self.settings.screen_width, self.settings.screen_height))
    pygame.display.set_caption("Alien Invasion")
    self.clock = pygame.time.Clock()
    self.ship = Character(self.screen, "images/ship.bmp", settings.ship_speed)
    self.bullets: list[Bullet] = []
    self.bullet_count = 0
    self.alien_group = AlienGroup(self.screen, "images/alien.bmp")
    self._feet_aliens(1)

  def run_game(self):
    # Start main loop for the game.
    while True:
      self._handle_event() # Watch for keyboard and mouse events.
      self._update()
      self._update_screen()
      self.clock.tick(60)

  def _handle_event(self):
    for event in pygame.event.get():
      if event.type == pygame.QUIT:
        sys.exit()
      elif event.type == pygame.KEYDOWN:
        self._handle_key_down(event)
      elif event.type == pygame.KEYUP:
        self._handle_key_up(event)
  
  def _update_screen(self):
    # Redraw the screen during each pass through the loop
    self.screen.fill(self.settings.bg_color)
    self.ship.blitme()
    for bullet in self.bullets:
      bullet.draw_bullet()
    self.alien_group.draw_aliens()
    pygame.display.flip()
  
  def _fire_bullet(self):
    if (self.bullet_count < self.settings.bullets_allowed):
      self.bullet_count += 1
      bullet = Bullet(self.screen, self.settings.bullet_settings, self.ship.rect.midtop)
      self.bullets.append(bullet)
      
  def _handle_key_down(self, event: pygame.event.Event):
    if event.key == pygame.K_SPACE:
      self._fire_bullet()
    else:
      self.ship.handle_key_down(event.key)
  
  def _handle_key_up(self, event: pygame.event.Event):
    self.ship.handle_key_up(event.key)
  
  def _update(self):
    self.ship.update()
    for bullet in self.bullets:
      bullet.update()

    # clear all bullets that have disappeared
    for bullet in self.bullets.copy():
      if bullet.rect.bottom <= 0:
        self.bullets.remove(bullet)

    self.alien_group.update_aliens()
    
  # direction = 1: right, -1: left  
  def _feet_aliens(self, direction: int = 1):
    align_width = self.alien_group.init_rect.width + self.settings.space_between_alien
    max_num_aliens_inline = float.__floor__((self.settings.screen_width - 2 * self.alien_group.init_rect.x) / align_width)

    # Feet a row of aliens
    for i in range(0, max_num_aliens_inline):
      self.alien_group.generate_alien_at(self.alien_group.init_rect.x + i * align_width, direction)

