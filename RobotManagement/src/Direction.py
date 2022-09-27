from enum import Enum

class Direction(Enum):
    FORWARD = 0x1
    RIGHT = 0x2
    LEFT = 0x4
    REST = -1