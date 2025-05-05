# Python Types Intro

**Simple types**
- int
- float
- bool
- bytes
- str

**Other types**
- list[str]
- tuple[int, str]
- dict[string, float]
- set[str]


**Union**
- str | int

**Optional types**

```python
def say_hi(name: str | None = None):
    if name is not None:
        print(f"Hey {name}!")
    else:
        print("Hello World")
```

**Generic types**

```python
from typing import Sequence, TypeVar

T = TypeVar('T')      # Declare type variable

def first(l: Sequence[T]) -> T:   # Generic function
    return l[0]
```

Reading:
- https://peps.python.org/pep-0484/
- https://fastapi.tiangolo.com/python-types/

# FastAPI tutorial

- https://fastapi.tiangolo.com/tutorial
