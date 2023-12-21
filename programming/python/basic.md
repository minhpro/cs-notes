## Strings

### Using varibles in strings

```python
first_name = "ada"
last_name = "odo"
full_name = f"{first_name} {last_name}"
print(full_name)
# You can even using functions
print(f"Hello, {full_name.title()}!")
```

### String remove prefix

```python
url = "https://exception.engine.io"
simple_domain = url.removefrefix("https://")
```

## Lists

Append an element to the end of a list

```python
motorcycles = ['honda', 'yamaha', 'suzuki']
motorcycles.append('ducati')
print(motorcycles) # ['honda', 'yamaha', 'suzuki', 'ducati']
```

Insert an element into a list

```python
motorcycles.insert(0, 'ducati')
print(motorcycles) # ['ducati', 'honda', 'yamaha', 'suzuki']
```

Removing an item using `del` statement

```python
motorcycles = ['honda', 'yamaha', 'suzuki']
del motorcycles[0]
print(motorcycles) # ['yamaha', 'suzuki']
```

Poping an item from a list

```python
popped_motorcycle = motorcycles.pop()
print(popped_motorcycle) # 'suzuki'
```

The `pop()` method removes the last item from a list and return this item.

Poping an item from any position in a list

```python
first_owned = motorcycles.pop(0)
print(first_owned) # 'honda'
```

Remove an item by value

```python
motorcycles.remove('honda')
print(motorcycles) # ['yamaha', 'suzuki']
```

Sort a list (itself)

```python
cars = ['bmw', 'audi', 'toyota', 'subaru']
cars.sort()
print(cars) #  ['audi', 'bmw', 'subaru', 'toyota']
```

Sort a list (keep the original, return a new sorted list)

```python
sorted_cars = sorted(cars)
print(cars)
print(sorted_cars)
```

Length of a list

```python
len(cars) # 4
```

## Working with lists

Looping through an entire list

```python
for car in cars:
  print(car.upper())
```

Making Numerical Lists

```python
# should print: 1, 2, 3, and 4
for value in range(1, 5):
  print(value)
```

`range` method returns an object (`range` object) that produces a sequence of integers from start (inclusive) to stop (exclusive) by step.

Using range() to make a list of numbers

```python
numbers = list(range(1, 6))
print(numbers)
```

The `list()` function returns an `list` object (built-in mutable sequence).

Some simple statistics with a list of numbers

```python
digits = [1,2,3,4,5,6,7,8,9,0]
min(digits) # 0
max(digits) # 9
sum(digits) # 45
```

List comprehensions

```python
squares = [value**2 for value in range(1, 11)]
print(squares)
```

Slicing a List

```python
print(digits[1:3]) # elements at index 1,2 (not include the end) 
# digits[:4] == digits[0:4]
# digits[2:] == from index 2 to the end
```

Slice create a new list from the original list.

Looping through a slice

```python
for i in digits[:3]:
  print(i)
```

You can make a copy of a list by slice an entire a list (omit the start and the end)

```python
other_digits = digits[:]
```

## Tuple

Tuple is the immutable sequence (list is the mutable sequence).

Tuple example,

```python
dimensions = (200, 50)
print(dimensions[0])
print(dimensions[1])
```

To creat a tuple with only one element

```python
my_t = (3,) # comman need here
```

Cannot modify tuples, but you can reassign a tuple to the new value

```python
original_t = (1,2)
original_t = (3,4,5)
```

## Branching

Branch with `if` statement.

```python
if condition:
  ...
else
  ...
```

`else` is optional

The `if-elif-else` chain

```python
if condition1:
  ...
elif contition2:
  ...
...
else:
  ...
```

### Condition tests

```python
car == 'bmw' # Equality

car != 'bmw' # Inequality

# numerical tests: equal, inequal, greater [or equal], less [or equal]
# ==, !=, >, <, >=, <=

# Combination: and / or / not
(age <= 30 and age > 10) or (age > 40 and age <= 50)

# Check whether a value in a list
car = 'bmw'
car in cars # True
car not in cars # False
```

## Dictionaries

Construct a dictionary

```python
my_dic = {"key1": 1, "key2": 12, "key3": 3,}
## access using brackets
print(my_dic["key1"])
# If the key not exist, there is an error `KeyError`
print(my_dic["weird_key"])
# Use get to access dictionary, return None if there is no key, or using the default value
print(my_dic.get("weird_key", 10))
# Add or update an item in dictionary
my_dic["key4"] = 15
```

Looping through a dictionary

```python
favorite_languages = {
  'user1': 'python',
  'user2': 'c',
  'user3': 'rust',
  'user4': 'python',
}

for name, language in favorite_languages:
  print(f"{name.title()}'s favorite language is {language.title()}.")

# Looping through all the keys
for name in favorite_languages.keys():
  print(name.title())

# Looping through all the values
for language in favorite_languages.values():
  print(language.title())
```

## Function

Function autonomy

```python
def function_name(parameter_one, parameter_second, parameter_third = 'default_value'):
  # parameters with default values must after all other parameter (without default values)
  ...

# Passing function with positional arguments (choose values for parameters)
function_name(1, 2, 'other_value')
function_name(1, 2) # can obmit the optional parameter

# Passing function with keyword arguments
function_name(parameter_one = 1, parameter_second = 2)
# mix positional and keyword arguments, all keyword arguments must come after all positional arguments
function_name(1, parameter_second = 2)
# e.g argument errors, 
function_name(parameter_one = 1, 2) # SyntaxError: positional argument follows keyword argument
```

## Module import

```python
import module_name
from module_name import function_name
from module_name import function_name as fn import module_name as mn
from module_name import *
```

## Classes

Class definition

```python
# geometry.py
class Point:
  # A point in a two dimensions space
  def __init__(self, x: int, y: int):
    # The constructor
    self.x = x
    self.y = y

  def display(self):
    # all instance methods have self as its first parameter and passed automatically
    print(f"Point({self.x}, {self.y})")

# main.py
from geometry import Point

p = Point(2,3)
p.display()
```

Inheritance

```python
class Car:
  def __int__(self, make, model, year):
    self.make = make
    self.model = model
    self.year = year
  
class ElectricCar(Car):
  def __init__(self, make, model, year, battery_size):
    super().__init__(make, model, year)
    self.battery_size = battery_size
```

But, in general situation, prefer composition over inheritance.

