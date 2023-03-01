#!/usr/bin/python

import cv2
import mediapipe as mp
import socket
import time

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
                        order = ""
        return order

    def SendOrder(self):
        order = self.CaptureOrder()
        
        if order != "":
            self.clientsocket.send(str(order).encode("UTF-8"))
            return True
        else:
            return False

if __name__ == "__main__":

    host_ip = socket.gethostbyname(socket.gethostname())
    port = 2020

    print("IP:", host_ip)
    print("Port: ", port)
    print("Esperando conexion...")

    serverSocket = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
    serverSocket.bind((host_ip,port))
    serverSocket.listen()
    (clientConnected, clientAddress) = serverSocket.accept();

    c = VideoCommander(cv2.VideoCapture(0), serverSocket)

    print("Executing")

    while True:
        c.SendOrder()
        time.sleep(0.1)