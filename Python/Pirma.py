import sys
import time

v = 0


def Rekursija(a, b, V):
    global v
    v += 4
    if a >= b:
        v += 1
        return 0
    min = sys.maxsize
    for i in range(a, b):
        v += 5
        sum = 0
        sum += Rekursija(a, i, V)
        sum += Rekursija(i + 1, b, V)
        sum += V[a - 1] * V[b]
        if sum < min:
            v += 1
            min = sum
    return min


def Dinamine(a, b, V):
    global v
    v += 4
    if a >= b:
        v += 1
        return 0
    tarr = [0] * b
    for i in range(b):
        v += v
        tarr[i] = [1] * b
    for j in range(1, b):
        v += 1
        for i in range(j - 1, -1, -1):
            v += 4
            min = sys.maxsize
            for k in range(i, j):
                v += 1
                for l in range(k, j + 1):
                    v += 3
                    if l == b - 1:
                        v += 1
                        break
                    temp = tarr[i][k] + tarr[k + 1][l] + V[i - 1] * V[j]
                    if min > temp:
                        v += 1
                        min = temp
            tarr[i][j] = min
            if a - 1 == i and b - 1 == j:
                v += 1
                return tarr[i][j]


N = [1, 10, 15, 17]

for n in N:
    arr = [0] * (n + 1)
    start = time.time()
    Rekursija(1, n, arr)
    finish = time.time()

    print("{} {} {}".format(n, (finish - start), v))
