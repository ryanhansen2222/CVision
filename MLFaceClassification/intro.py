import numpy as np
import cv2 as cv

#Is name correct?
face_cascade = cv.CascadeClassifier('data/haarcascades/haarcascade_frontalface_alt.xml')
eye_cascade = cv.CascadeClassifier('data/haarcascades/haarcascade_eye.xml')

cv.namedWindow("Image")

#MAYBE SWAP TO JPG
img = cv.imread("pic.png", cv.IMREAD_COLOR)

gray = cv.cvtColor(img, cv.COLOR_BGR2GRAY)

faces = face_cascade.detectMultiScale(gray, 1.3, 5)
print("faces ")
print(faces)
for(x,y,w,h) in faces:
	cv.rectangle(img, (x,y), (x+w, y+h), (255,0,0), 2)

eyes = eye_cascade.detectMultiScale(gray)
for (ex,ey,ew,eh) in eyes:
	cv.rectangle(img, (ex,ey), (ex+ew, ey+eh), (0,255,255),2)

cv.imshow('Image', img)
cv.waitKey(0)
cv.destroyAllWindows()
