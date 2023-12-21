from typing import TypeVar

import pygame

T = TypeVar('T')

class DrawableObject:
  # Objects can be draw on the screen
  def __init__(self, rect: pygame.Rect, color: (int, int, int), data: T):
    self.rect = rect
    self.color = color
    self.data = data

  def draw_object(self, screen: pygame.surface.Surface):
    pygame.draw.rect(screen, self.color, self.rect)

  def update(self):
    # real-time update, concrete classes have different update behavior
    pass