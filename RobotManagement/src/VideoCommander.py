#!/usr/bin/python

import cv2
import mediapipe as mp
import socket

from Direction import Direction

class VideoCommander:

    def __init__(self, cap, socket):
        self.cap = cap
        self.clientsocket, address = socket.accept()

    def __del__(self):
        self.cap.release()
        self.clientsocket.close()

    def CaptureOrder(self):
        order = None

        mp_hands = mp.solutions.hands

        with mp_hands.Hands(static_image_mode=True, max_num_hands=2, min_detection_confidence=0.5) as hands:
            if self.cap.isOpened():
                success, image = self.cap.read()
                if success:
                    image.flags.writeable = False
                    image = cv2.cvtColor(image, cv2.COLOR_BGR2RGB)

                    results = hands.process(image)
                    
                    # Print in terminal the hand (left or right) detected
                    if results.multi_handedness:
                        right, left = False, False
                        for idx, hand_handedness in enumerate(results.multi_handedness):
                            if (hand_handedness.classification[0].label == 'Right'):
                                right = True
                            else:
                                left = True
                        if (right and left):
                            order = Direction.FORWARD
                        else:
                            if right:
                                order = Direction.LEFT
                            else:
                                order = Direction.RIGHT
                    else:
                        order = Direction.REST
        return order

    def SendOrder(self):
        order = self.CaptureOrder()
        msg = ""

        if order == Direction.FORWARD:
            msg = "f"
        elif order == Direction.LEFT:
            msg = "l"
        elif order == Direction.RIGHT:
            msg = "r"
        
        if msg != "":
            self.clientsocket.send(bytes(msg,"utf-8"))
            return True
        else:
            return False

if __name__ == "__main__":

    s = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
    s.setsockopt(socket.SOL_SOCKET, socket.SO_REUSEADDR, 1)
    s.bind((socket.gethostname(), 1234))
    s.listen(5)

    c = VideoCommander(cv2.VideoCapture(0), s)

    print("Executing")
    while True:
        print(c.SendOrder())
