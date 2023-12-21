class Car:
  # Car representation
  def __init__(self, make, model, year):
    self.make = make
    self.model = model
    self.year = year
    self.odometer_reading = 0 # default attribute

  def get_descriptive_name(self):
    # Return a neatly formatted descriptive name
    long_name = f"{self.year} {self.make} {self.model}"
    return long_name.title()
  
  def increment_odometer(self, miles):
    self.odometer_reading += miles

class ElectricCar(Car):
  # Cars with electric engine
  def __init__(self, make, model, year, battery_size):
    super().__init__(make, model, year)
    self.battery_size = battery_size
  
  def describe_battery(self):
    print(f"This car has a {self.battery_size}-kWh battery")
