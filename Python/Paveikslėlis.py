import math as m
import time

class image:
    def __init__(self, length, recursion):
        self.actions = 0
        self.XY = length*3**recursion
        self.rows = ((self.XY + 31) // 32) * 4
        self.dir = 0
        self.create_map()

        self.draw(length, recursion, self.XY // 2, self.XY // 2)
        self.actions += 5
    def create_map(self):
        self.colors = bytearray(self.rows * self.XY)
        self.header = bytearray([66, 77])
        size = 62 + self.rows * self.XY
        self.header += bytearray([size % 256, size // 256 % 256,
                                  size // (256 ** 2) % 256, size // (256 ** 3) % 256])
        self.header += bytearray([0, 0, 0, 0,
                                  62, 0, 0, 0,
                                  40, 0, 0, 0])
        for _ in range(2):
            self.header += bytearray([self.XY % 256, self.XY // 256 % 256,
                                      self.XY // (256 ** 2) % 256, self.XY // (256 ** 3) % 256])
        self.header += bytearray([1, 0,
                                  1, 0,
                                  0, 0, 0, 0,
                                  0, 0, 0, 0,
                                  0, 0, 0, 0,
                                  0, 0, 0, 0,
                                  0, 0, 0, 0,
                                  0, 0, 0, 0,
                                  255, 255, 255, 0,
                                  122, 122, 0, 0])
        self.actions += 8

    def create_image(self, filename):
        file = open(filename, "wb")
        file.write(self.header + self.colors)
        file.close()

    def draw(self, length, recursion, x, y):
        self.draw_triangle(length * 3 ** (recursion - 1), x, y)
        self.actions += 2
        if recursion > 1:
            self.draw(length, recursion - 1, x+length * 3 ** (recursion - 1), y+length * 3 ** (recursion - 1))
            self.draw(length, recursion - 1, x+length * 3 ** (recursion - 1), y-length * 3 ** (recursion - 1))
            self.draw(length, recursion - 1, x-length * 3 ** (recursion - 1), y+length * 3 ** (recursion - 1))
            self.draw(length, recursion - 1, x-length * 3 ** (recursion - 1), y-length * 3 ** (recursion - 1))
            self.actions += 4


    def draw_triangle(self, length, x, y):
        h = int(m.sqrt(length ** 2 - (length / 2) ** 2))
        ty = y - h // 2
        tx = x
        y += h // 2
        x += length // 2
        self.actions += 6
        for i in range(length):
            self.put(x - i, y)
            self.actions += 1
        self.bresenham(y, ty, x, tx, length)
        self.actions += 1

    def bresenham(self, y1, y2, x1, x2, length):
        self.actions += 1
        if x1 > x2:
            x1, x2 = x2, x1
            y1, y2 = y2, y1
            self.actions += 2
        if y1 > y2:
            x1, x2 = x2, x1
            y1, y2 = y2, y1
            self.actions += 2
        dy = y2 - y1
        dx = x2 - x1
        pk = dy * 2 - dx
        self.put(x1, y1)
        self.put(x2, y2)
        self.put(x2 - length, y2)
        x3 = x1
        while y1 != y2:
            self.actions += 1
            if pk >= 0:
                pk = pk + 2 * dx - 2 * dy
                x3 -= 1
                x1 += 1
                y1 += 1
                self.put_trian_front(x1, x3, y1)
                self.actions += 6
            else:
                pk = pk + 2 * dx
                y1 += 1
                self.put_trian_front(x1, x3, y1)
                self.actions += 4

    def put_trian_front(self, x1, x2, y):
        for i in range(x2, x1 + 1):
            self.actions += 2
            self.put(i, y)

    def put(self, x, y):
        self.colors[self.rows * y + x // 8] |= 1 << (7 - (x % 8))
        self.actions += 1


# p = image(10, 4)
# p.create_image("image")
for i in range(1, 6):
    p = image(10, i)
    p.create_image("image")
    print(p.actions)
