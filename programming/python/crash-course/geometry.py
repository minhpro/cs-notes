import math

class Point:
  # A point in a two dimensions space
  def __init__(self, x: int, y: int):
    # The constructor
    self.x = x
    self.y = y

  def display(self):
    # all instance methods have self as its first parameter and passed automatically
    print(f"Point({self.x}, {self.y})")

def point_from_trigonometry(distance, degree) -> Point:
    return Point(distance * math.cos(degree * math.pi / 180), distance * math.sin(degree * math.pi / 180))