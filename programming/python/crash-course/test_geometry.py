from geometry import Point, point_from_trigonometry

EPSILON = 0.000001

def test_from_trigonometry():
  # Test construct Point from trigonometry
  p = point_from_trigonometry(10, 30)
  assert abs(p.y - 5) < EPSILON

