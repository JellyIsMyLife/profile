import time


def fun1(a, m, n):
    #  T(n) = 4*T(n/10) + 1
    if n - m < 10:
        return
    for i in range(2):
        fun1(a, m, m + (n - m) // 10)
        fun1(a, n - (n - m) // 10, n)
    a[m] += 1


def fun2(a, m, n):
    #  T(n) = T(n/6) + T(n/4) + n**2
    if n - m < 4:
        return
    fun2(a, m, m + (n - m) // 6)
    fun2(a, n - (n - m) // 4, n)

    for i in range(n):
        for j in range(n):
            a[j] += 1


def fun3(a, n):
    #  T(n) = T(n-9) + T(n-2) + n
    if n < 2:
        return
    fun3(a, n - 2)
    fun3(a, n - 9)

    for i in range(n):
        a[i] += 1


var = [250, 500, 1000, 2000, 4000]  # 1,2 funkcijai
# var = [50, 60, 70, 80, 100, 101]  # 3 funkcijai
for z in var:
    ar = [0] * z
    start = time.time()
    # fun1(ar, 0, z)
    fun2(ar, 0, z)
    # fun3(ar, z)
    finish = time.time()
    print("{} - {} sek".format(z, (finish - start)))
