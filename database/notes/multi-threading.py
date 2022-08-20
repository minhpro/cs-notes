import threading
from time import sleep

class Number:
    def __init__(self, value: int) -> None:
        self.value = value
    def increase(self):
        self.value += 1
    def getValue(self):
        return self.value

count = Number(0)

def increase():
    for _ in range(10000):
        count.increase()
        print(str(threading.get_ident()) + ": " + str(count.getValue()))


t1 = threading.Thread(target=increase)
t2 = threading.Thread(target=increase)

t1.start()
t2.start()

t1.join()
t2.join()

print("Final value: " + str(count.getValue()))