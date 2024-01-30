import random
import time

v = 0


def func1Analysis(arr):
    n = len(arr)
    k = n
    global v
    v = 2
    for _ in range(n * n):
        v += 2
        if arr[0] > 0:
            for _ in range(n):
                v += 2
                k -= 2
            for _ in range(n * n):
                v += 2
                k += 3
    v += 1
    return k


def func2Analysis(n):
    global v
    v = 2
    k = 0
    arr = [0] * n
    for i in range(n):
        v += 3
        arr[i] = random.randint(0, n)
        k += arr[i] + FF1(i)
    v += 1
    return k


def FF1(n):
    global v
    if n > 0:
        v += 2
        return FF1(n - 1)
    v += 2
    return n


var = [0, 10, 50, 100, 200, 500]

for z in var:
    # ar = [1] * z
    start = time.time()
    # func1Analysis(ar)
    func2Analysis(z)
    finish = time.time()

    print("{} {} {}".format(z, (finish - start), v))
