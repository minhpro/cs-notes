import math

class Vector:
  def __init__(self, x=0, y=0):
    self.x = x
    self.y = y

  def __repr__(self) -> str:
    return f'Vector({self.x!r}, {self.y!r})'
  
  def __abs__(self):
    return math.hypot(self.x, self.y)
  
  def __bool__(self):
    return bool(abs(self))
  
  def __add__(self, other):
    return Vector(self.x + other.x, self.y + other.y)
  
  def __mul__(self, scalar):
    return Vector(self.x * scalar, self.y * scalar)
  
  def __rmul__(self, other):
    return self * other
  
