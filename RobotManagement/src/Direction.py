from enum import Enum

class Direction(Enum):

    def __str__(self):
        return str(self.value)

    FORWARD = "F\n"
    RIGHT = "R\n"
    LEFT = "L\n"