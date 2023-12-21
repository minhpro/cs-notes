from geometry import Point, point_from_trigonometry
from vehicle import Car, ElectricCar

# Program entrypoint
# Using the special variable __name__ 
if __name__ == "__main__":
  p = Point(2, 3)
  p.display()
  other = point_from_trigonometry(5, 30)
  other.display()

  car = Car("nissan", "1102", 1998)
  print(car.get_descriptive_name())

  other_car = ElectricCar("honda", "t003", 2028, 40)
  other_car.describe_battery()
